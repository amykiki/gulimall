package daily.boot.gulimall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.common.exception.BusinessException;
import daily.boot.gulimall.common.exception.OrderErrorCode;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.order.constant.OrderConstant;
import daily.boot.gulimall.order.dao.OrderDao;
import daily.boot.gulimall.order.entity.OrderEntity;
import daily.boot.gulimall.order.entity.OrderItemEntity;
import daily.boot.gulimall.order.enums.OrderStatusEnum;
import daily.boot.gulimall.order.security.LoginUserInfo;
import daily.boot.gulimall.order.security.LoginUserInfoHolder;
import daily.boot.gulimall.order.service.OrderItemService;
import daily.boot.gulimall.order.service.OrderService;
import daily.boot.gulimall.order.service.RemoteService;
import daily.boot.gulimall.order.to.OrderCreateTo;
import daily.boot.gulimall.order.vo.OrderConfirmVo;
import daily.boot.gulimall.service.api.to.OrderItemTo;
import daily.boot.gulimall.order.vo.OrderSubmitVo;
import daily.boot.gulimall.order.vo.SubmitOrderResponseVo;
import daily.boot.gulimall.service.api.to.*;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("orderService")
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Value("${gulimall.order.mq.order-event-exchange}")
    private String orderEventExchange;
    @Value("${gulimall.order.mq.order-create-order-routing-key}")
    private String orderCreateOrderRoutingKey;
    @Value("${gulimall.order.mq.stock-release-stock-routing-key}")
    private String stockReleaseStockRoutingKey;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private ExecutorService orderExecutor;
    @Autowired
    private RemoteService remoteService;
    
    @Autowired
    private OrderItemService orderItemService;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    

    @Override
    public PageInfo<OrderEntity> queryPage(PageQueryVo queryVo) {
        IPage<OrderEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public List<OrderEntity> getOrdersByMemberId(Long memberId) {
        LambdaQueryWrapper<OrderEntity> wrapper = Wrappers.<OrderEntity>lambdaQuery()
                .eq(OrderEntity::getMemberId, memberId);
        return this.list(wrapper);
    }
    
    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        LoginUserInfo loginUserInfo = LoginUserInfoHolder.getLoginUserInfo();
    
        //构建OrderConfirmVo
        OrderConfirmVo confirmVo = new OrderConfirmVo();
        //远程异步查询所有的收货地址列表
        CompletableFuture<List<MemberAddressTo>> memberAddressFuture = CompletableFuture.supplyAsync(() -> remoteService.getMemberAddress(loginUserInfo.getUserId()), orderExecutor);
        
        //远程异步查询购物车中所有选中的购物项
        CompletableFuture<List<OrderItemTo>> currentCartItemsFuture =  CompletableFuture.supplyAsync(() -> remoteService.getCurrentCartItems(loginUserInfo.getUserId()), orderExecutor);
    
        //远程异步查询用户积分
        CompletableFuture<MemberFullInfoTo> memberFullInfoToCompletableFuture = CompletableFuture.supplyAsync(() -> remoteService.getMemberIntegration(loginUserInfo.getUserId()), orderExecutor);
        
    
        //设置购物车项目
        //远程异步查询商品库存信息
        List<OrderItemTo> orderItemTos = currentCartItemsFuture.get();
        confirmVo.setItems(orderItemTos);
        CompletableFuture<Map<Long, Boolean>> skuHashStockFuture = CompletableFuture.supplyAsync(() -> {
            List<OrderItemTo> items = confirmVo.getItems();
            //获取全部商品Id
            List<Long> skuIds = items.stream().map(OrderItemTo::getSkuId).collect(Collectors.toList());
        
            //远程查询商品库存信息
            List<SkuHasStockTo> skuHasStockTos = remoteService.getSkuHasStock(skuIds);
        
            if (!CollectionUtils.isEmpty(skuHasStockTos)) {
                //将skuStockTos集合转为map
                return skuHasStockTos.stream().collect(Collectors.toMap(SkuHasStockTo::getSkuId, SkuHasStockTo::getHasStock));
            }
            return null;
        }, orderExecutor);
    
        //设置用户地址
        List<MemberAddressTo> memberAddressTos = memberAddressFuture.get();
        confirmVo.setMemberAddressTos(memberAddressTos);
        
        //设置用户积分
        MemberFullInfoTo memberFullInfoTo = memberFullInfoToCompletableFuture.get();
        confirmVo.setIntegration(memberFullInfoTo.getIntegration());
        
        //设置商品库存信息
        Map<Long, Boolean> skuStockMap = skuHashStockFuture.get();
        confirmVo.setStocks(skuStockMap);
        
        //防重复令牌，防止重复提交
        //为用户设置一个token，测试可以使用三十分钟内过期
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX + loginUserInfo.getUserId(), token, 30, TimeUnit.MINUTES);
        confirmVo.setOrderToken(token);
        
        return confirmVo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    //@GlobalTransactional
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo vo) {
        //String xid = RootContext.getXID();
        SubmitOrderResponseVo responseVo = new SubmitOrderResponseVo();
        responseVo.setCode(0);
        
        //创建订单，校验令牌，校验价格，锁定库存
        
        //1. 校验令牌是否合法【令牌的对比和删除必须保证原子性】
        boolean checkToken = checkToken(vo.getOrderToken());
        if (!checkToken) {
            //令牌验证失败
            responseVo.setCode(1);
            return responseVo;
        }
        //令牌验证成功
        //1. 创建订单，订单项等消息
        OrderCreateTo orderCreateTo = createOrder(vo);
        
        //2. 验证价格
        BigDecimal realPayAmount = orderCreateTo.getOrder().getPayAmount();
        BigDecimal payPrice = vo.getPayPrice();
    
        if (Math.abs(realPayAmount.subtract(payPrice).doubleValue()) >= 0.01) {
            //订单价格发生了变动
            responseVo.setCode(2);
            return responseVo;
        }
        //3. 保存订单
        saveOrder(orderCreateTo);
        
        //4. 库存锁定，只要有异常，回滚订单信息，订单号，所有订单项信息
        WareSkuLockTo lockTo = new WareSkuLockTo();
        lockTo.setOrderSn(orderCreateTo.getOrder().getOrderSn());
        
        
        //获取要锁定的商品数据信息
        List<OrderItemTo> orderItemTos = orderCreateTo.getOrderItems().stream().map(entity -> {
            OrderItemTo to = new OrderItemTo();
            to.setSkuId(entity.getSkuId());
            to.setCount(entity.getSkuQuantity());
            to.setTitle(entity.getSkuName());
            return to;
        }).collect(Collectors.toList());
    
        lockTo.setLocks(orderItemTos);
        
        //远程调用锁定库存
        //可能出现的问题：扣减库存成功了，但是由于网络原因超时，出现异常，导致订单事务回滚，库存事务不回滚
        //解决方案一：seata, seata的问题是不够高并发，需要加锁，并行化提升不了效率
        //解决方案二： 使用消息给库存服务
    
        boolean rtn = remoteService.orderLockStock(lockTo);
        if (rtn) {
            //锁定成功
            responseVo.setOrder(orderCreateTo.getOrder());
    
            // TODO: 2021/1/13 订单创建成功，发送消息给mq
            rabbitTemplate.convertAndSend(orderEventExchange, orderCreateOrderRoutingKey, orderCreateTo.getOrder(), new CorrelationData(orderCreateTo.getOrder().getOrderSn()));
            // TODO: 2021/1/13 删除购物车里的数据
            List<Long> skuOrdered = orderCreateTo.getOrderItems().stream().map(OrderItemEntity::getSkuId).collect(Collectors.toList());
            // 测试全局事务
            //int i = 10/0;
            return responseVo;
            //remoteService.deleteCartItem(loginUserInfo.getUserId(), skuOrdered);
        } else {
            //锁定失败
            throw new BusinessException(OrderErrorCode.ORDER_LOCK_STOCK_FAIL);
        }
    }
    
    /**
     * 关闭订单
     * @param orderEntity
     */
    @Override
    public void closeOrder(OrderEntity orderEntity) {
        OrderEntity orderInfo = this.lambdaQuery().eq(OrderEntity::getOrderSn, orderEntity.getOrderSn()).one();
        //首先确定订单存在且状态为新建，有可能订单都并存在，在下订单的时候就回滚了
        if (Objects.nonNull(orderInfo) && orderInfo.getStatus().equals(OrderStatusEnum.CREATE_NEW.getCode())) {
            log.info("*******GULIMALL-ORDER需要关闭订单{}，订单状态{}********", orderEntity.getOrderSn(), orderEntity.getStatus());
            //待付款状态进行关单
            OrderEntity orderUpdate = new OrderEntity();
            orderUpdate.setId(orderInfo.getId());
            orderUpdate.setStatus(OrderStatusEnum.CANCLED.getCode());
            this.updateById(orderUpdate);
            
            //发送消息给MQ--库存，库存服务需解锁库存
            OrderTo orderTo = new OrderTo();
            BeanUtils.copyProperties(orderInfo, orderTo);
            try {
                // TODO: 2021/1/17 事务信息
                // 确保每个消息发送成功，给每个消息做好日志记录
                // 可以给数据库保存每个详细信息
                log.info("*******GULIMALL-ORDER已关闭订单{}，推送解锁库存信息至GULIMALL-WARE服务解锁*******", orderEntity.getOrderSn());
                rabbitTemplate.convertAndSend(orderEventExchange, stockReleaseStockRoutingKey, orderTo, new CorrelationData(orderTo.getOrderSn()));
            } catch (Exception e) {
                // TODO: 2021/1/17 定期扫描数据库，重新发送失败信息 
            }
    
        }else {
            log.info("*******GULIMALL-ORDER无需关闭订单{}，订单状态{}*******", orderEntity.getOrderSn(), orderInfo == null ? null : orderInfo.getStatus());
        }
    }
    
    @Override
    public OrderEntity getorderByOrderSn(String orderSn) {
        return this.lambdaQuery().eq(OrderEntity::getOrderSn, orderSn).one();
    }
    
    /**
     * 保存订单所有数据
     * @param orderCreateTo
     */
    private void saveOrder(OrderCreateTo orderCreateTo) {
        //获取订单信息
        OrderEntity order = orderCreateTo.getOrder();
        order.setModifyTime(new Date());
        order.setCreateTime(new Date());
        //保存订单
        save(order);
        
        //获取订单项信息
        List<OrderItemEntity> orderItems = orderCreateTo.getOrderItems();
        orderItems.forEach(item -> item.setOrderId(order.getId()));
        //批量保存订单项数据
        orderItemService.saveBatch(orderItems);
    }
    
    private boolean checkToken(String orderToken) {
        LoginUserInfo loginUserInfo = LoginUserInfoHolder.getLoginUserInfo();
        String tokenKey = OrderConstant.USER_ORDER_TOKEN_PREFIX + loginUserInfo.getUserId();
        //校验令牌是否合法【令牌的对比和删除必须保证原子性】
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        //通过lua脚本原子验证令牌和删除令牌
        Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList(tokenKey), orderToken);
    
        //令牌验证失败
        return result != null && result != 0L;
    }
    
    private OrderCreateTo createOrder(OrderSubmitVo orderSubmitVo) {
        OrderCreateTo createTo = new OrderCreateTo();
        
        //1. 生成订单号
        String orderSn = IdWorker.getTimeId();
        OrderEntity orderEntity = buildOrder(orderSubmitVo, orderSn);
        
        //2. 获取所有订单项
        List<OrderItemEntity> orderItemEntities = buildOrderItems(orderSn);
        
        //3. 验价(计算价格，积分等信息)
        computePrice(orderEntity, orderItemEntities);
        
        createTo.setOrder(orderEntity);
        createTo.setOrderItems(orderItemEntities);
        
        return createTo;
    }
    
    /**
     * 计算价格的方法
     * @param orderEntity
     * @param orderItemEntities
     */
    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        //总价
        BigDecimal total = new BigDecimal("0.0");
        //优惠价
        BigDecimal coupon = new BigDecimal("0.0");
        BigDecimal integration = new BigDecimal("0.0");
        BigDecimal promotion = new BigDecimal("0.0");
        
        //积分，成长值
        Integer integrationTotal = 0;
        Integer growthTotal = 0;
        
        //订单总额，叠加每一个订单项的总额信息
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            //优惠价格信息
            coupon = coupon.add(orderItemEntity.getCouponAmount());
            promotion = promotion.add(orderItemEntity.getPromotionAmount());
            integration = integration.add(orderItemEntity.getIntegrationAmount());
            
            //总价
            total = total.add(orderItemEntity.getRealAmount());
            
            //积分信息，成长值信息
            integrationTotal += orderItemEntity.getGiftIntegration();
            growthTotal += orderItemEntity.getGiftGrowth();
        }
        
        //订单价格相关
        orderEntity.setTotalAmount(total);
        //设置应付总额
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        //优惠额度
        orderEntity.setCouponAmount(coupon);
        orderEntity.setPromotionAmount(promotion);
        orderEntity.setIntegrationAmount(integration);
        
        //设置积分&成长值
        orderEntity.setIntegration(integrationTotal);
        orderEntity.setGrowth(growthTotal);
        
        //设置删除状态(0 -- 未删除， 1-- 已删除)
        orderEntity.setDeleteStatus(0);
    }
    
    /**
     * 构建所有订单项数据
     * @param orderSn
     * @return
     */
    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        List<OrderItemEntity> orderItemEntityList;
        List<OrderItemTo> currentCartItems = remoteService.getCurrentCartItems(LoginUserInfoHolder.getLoginUserInfo().getUserId());
        orderItemEntityList = currentCartItems.stream().map(item -> {
            OrderItemEntity orderItemEntity = buildOrderItem(item);
            orderItemEntity.setOrderSn(orderSn);
            return orderItemEntity;
        }).collect(Collectors.toList());
        return orderItemEntityList;
    }
    
    /**
     * 构建订单数据
     * @param item
     * @return
     */
    private OrderItemEntity buildOrderItem(OrderItemTo item) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        
        //1. 商品的spu信息
        Long skuId = item.getSkuId();
        //获取商品的spu信息
        SpuInfoTo spuInfoTo = remoteService.getSpuInfoBySkuId(skuId);
    
        orderItemEntity.setSpuId(spuInfoTo.getId());
        orderItemEntity.setSpuName(spuInfoTo.getSpuName());
        orderItemEntity.setSpuBrand(spuInfoTo.getBrandName());
        orderItemEntity.setCategoryId(spuInfoTo.getCatelogId());
        
        //2. 商品的sku信息
        orderItemEntity.setSkuId(skuId);
        orderItemEntity.setSkuName(item.getTitle());
        orderItemEntity.setSkuPic(item.getImage());
        orderItemEntity.setSkuPrice(item.getPrice());
        orderItemEntity.setSkuQuantity(item.getCount());
        
        //使用StringUtils.collectionToDelimitedString将list集合转换为String
        String skuAttrValues = StringUtils.collectionToDelimitedString(item.getSkuAttrValues(), ";");
        orderItemEntity.setSkuAttrsVals(skuAttrValues);
    
        // TODO: 2021/1/12 3. 商品的优惠信息
        
        //4.商品的积分信息
        orderItemEntity.setGiftGrowth(item.getPrice().multiply(new BigDecimal(item.getCount())).intValue());
        orderItemEntity.setGiftIntegration(item.getPrice().multiply(new BigDecimal(item.getCount() * 1.5)).intValue());
        
        //5. 订单项的价格信息
        orderItemEntity.setPromotionAmount(BigDecimal.ZERO);
        orderItemEntity.setCouponAmount(BigDecimal.ZERO);
        orderItemEntity.setIntegrationAmount(BigDecimal.ZERO);
        
        //当前订单项的实际金额：总额 - 各种优惠价格
        //原来的价格
        BigDecimal originPrice = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity()));
        
        //原价减去优惠价格得到的最终价格
        BigDecimal subtract = originPrice.subtract(orderItemEntity.getCouponAmount())
                                         .subtract(orderItemEntity.getPromotionAmount())
                                         .subtract(orderItemEntity.getIntegrationAmount());
        orderItemEntity.setRealAmount(subtract);
        
        return orderItemEntity;
    }
    
    /**
     * 构建订单数据
     * @param orderSn
     * @return
     */
    private OrderEntity buildOrder(OrderSubmitVo orderSubmitVo, String orderSn) {
        LoginUserInfo loginUserInfo = LoginUserInfoHolder.getLoginUserInfo();
        
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setMemberId(loginUserInfo.getUserId());
        orderEntity.setOrderSn(orderSn);
        orderEntity.setMemberUsername(loginUserInfo.getUserName());
        
        //远程获取收货地址和运费信息
        FareTo fareTo = remoteService.getFare(orderSubmitVo.getAddrId());
        orderEntity.setFreightAmount(fareTo.getFare());
        
        //获取收货地址信息
        MemberAddressTo address = fareTo.getAddress();
        //设置收货人信息
        orderEntity.setReceiverName(address.getName());
        orderEntity.setReceiverPhone(address.getPhone());
        orderEntity.setReceiverPostCode(address.getPostCode());
        orderEntity.setReceiverProvince(address.getProvince());
        orderEntity.setReceiverCity(address.getCity());
        orderEntity.setReceiverRegion(address.getRegion());
        orderEntity.setReceiverDetailAddress(address.getDetailAddress());
        
        //设置订单相关的状态信息
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        orderEntity.setAutoConfirmDay(7);
        orderEntity.setConfirmStatus(0);
        return orderEntity;
    }
}