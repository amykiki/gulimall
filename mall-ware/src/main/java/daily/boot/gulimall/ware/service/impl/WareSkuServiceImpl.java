package daily.boot.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.common.Result;
import daily.boot.common.exception.BusinessException;
import daily.boot.common.exception.error.CommonErrorCode;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.service.api.feign.ProductFeignService;
import daily.boot.gulimall.service.api.to.*;
import daily.boot.gulimall.service.api.to.mq.StockLockedTo;
import daily.boot.gulimall.ware.dao.WareSkuDao;
import daily.boot.gulimall.ware.entity.PurchaseDetailEntity;
import daily.boot.gulimall.ware.entity.WareOrderTaskDetailEntity;
import daily.boot.gulimall.ware.entity.WareOrderTaskEntity;
import daily.boot.gulimall.ware.entity.WareSkuEntity;
import daily.boot.gulimall.ware.exception.WareErrorCode;
import daily.boot.gulimall.ware.service.*;
import io.seata.core.context.RootContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("wareSkuService")
@Slf4j
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Value("${gulimall.ware.mq.stock-event-exchange}")
    private String stockEventExchange;
    @Value("${gulimall.ware.mq.stock-locked-routing-key}")
    private String stockLockedRoutingKey;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ProductFeignService productFeignService;
    @Autowired
    private PurchaseDetailService purchaseDetailService;
    @Autowired
    private WareOrderTaskService wareOrderTaskService;
    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;
    
    @Autowired
    private RemoteService remoteService;

    @Override
    public PageInfo<WareSkuEntity> queryPage(PageQueryVo queryVo, WareSkuEntity wareSkuEntity) {
        LambdaQueryWrapper<WareSkuEntity> queryWrapper = Wrappers.lambdaQuery(WareSkuEntity.class);
        queryWrapper.eq(Objects.nonNull(wareSkuEntity.getWareId()), WareSkuEntity::getWareId, wareSkuEntity.getWareId())
                    .eq(Objects.nonNull(wareSkuEntity.getSkuId()), WareSkuEntity::getSkuId, wareSkuEntity.getSkuId());
        IPage<WareSkuEntity> page = this.page(Query.getPage(queryVo), queryWrapper);
        return PageInfo.of(page);
    }
    
    @Override
    public void addStockByPurchaseDetail(List<Long> finishedIds) {
        // 获取完整的采购需求
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.listByIds(finishedIds);
        
        //先判断仓库对应sku是否存在
        purchaseDetailEntities.forEach(detail -> {
            LambdaQueryWrapper<WareSkuEntity> queryWrapper = Wrappers.lambdaQuery(WareSkuEntity.class)
                                                           .eq(WareSkuEntity::getWareId, detail.getWareId())
                                                           .eq(WareSkuEntity::getSkuId, detail.getSkuId());
            WareSkuEntity entity = this.getOne(queryWrapper);
            // 条目不存在，新创建条目
            if (Objects.isNull(entity)) {
                entity = new WareSkuEntity();
                entity.setWareId(detail.getWareId());
                entity.setSkuId(detail.getSkuId());
                entity.setStock(detail.getSkuNum());
                entity.setStockLocked(0);
                //远程查询skuName
                try {
                    Result<SkuInfoVo> info = productFeignService.info(detail.getSkuId());
                    log.info(info.toString());
                    if (Objects.nonNull(info) && info.isOk()) {
                        entity.setSkuName(info.getData().getSkuName());
                    } else {
                        log.warn("远程查询productFeignService.info({})信息为空！！", detail.getSkuId());
                    }
                } catch (Exception e) {
                    log.warn("远程查询productFeignService.info({})信息失败", detail.getSkuId(), e);
                }
                this.save(entity);
            } else {
                //条目存在，更新
                int newStock = entity.getStock() + detail.getSkuNum();
                this.lambdaUpdate()
                    .set(WareSkuEntity::getStock, newStock)
                    .eq(WareSkuEntity::getStock, entity.getStock())
                    .eq(WareSkuEntity::getSkuId, entity.getSkuId())
                    .eq(WareSkuEntity::getWareId, entity.getWareId())
                    .update();
                
            }
        
        });
    }
    
    @Override
    public List<SkuHasStockTo> getSkuHasStock(List<Long> skuIds) {
        String ids = skuIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sku_id as skuId, ifnull(sum(stock - stock_locked), 0) as stockSum").inSql("sku_id", ids).groupBy("sku_id");
        List<Map<String, Object>> mapList = this.listMaps(queryWrapper);
        Map<Long, Integer> stockMap = mapList.stream()
                                             //不知道为什么这里mp自动把stockSum的类型转为BigDecimal了
                                             .collect(Collectors.toMap(item -> (Long) item.get("skuId"), item -> ((BigDecimal)item.get("stockSum")).intValue()));
    
        List<SkuHasStockTo> skuHasStockList = skuIds.stream().map(skuId -> {
            SkuHasStockTo to = new SkuHasStockTo();
            to.setSkuId(skuId);
            if (stockMap.containsKey(skuId)) {
                to.setHasStock(true);
            } else {
                to.setHasStock(false);
            }
            return to;
        }).collect(Collectors.toList());
    
        return skuHasStockList;
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean orderLockStock(WareSkuLockTo lockTo) {
        //String xid = RootContext.getXID();
        //System.out.println("xid = " + xid);
        /**
         * 保存库存工作单详情信息，追溯
         */
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(lockTo.getOrderSn());
        wareOrderTaskEntity.setCreateTime(new Date());
        wareOrderTaskService.save(wareOrderTaskEntity);
        
        //1. 按照下单的收货地址，找到一个就近仓库，锁定库存
        //2. 找到每个商品在哪个仓库都有库存
        List<OrderItemTo> locks = lockTo.getLocks();
    
        List<SkuWareHasStock> skuWareHasStockList = locks.stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(item.getCount());
            //查询这个商品在哪个仓库有库存
            List<Long> wareIdList = this.listWareIdHasSkuStock(skuId);
            stock.setWareId(wareIdList);
            return stock;
        }).collect(Collectors.toList());
        
        //3. 锁定库存
        boolean skuStocked = false;
        for (SkuWareHasStock hasStock : skuWareHasStockList) {
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
    
            if (CollectionUtils.isEmpty(wareIds)) {
                throw new BusinessException(WareErrorCode.WARE_SKU_NO_STOCK, skuId + "无货");
            }
            
            //如果每一个商品都锁定成功，将当前商品锁定了几件的工作单记录发给MQ
            //锁定失败，前面保存的工作单信息都回滚，
            //发送出去的消息，即使要解锁库存，由于在数据库查不到指定的ID，所以不用解锁
            for (Long wareId : wareIds) {
                //锁定成功返回1， 失败就返回0
                Long updated = this.baseMapper.lockSkuStock(skuId, wareId, hasStock.getNum());
                if (updated == 1) {
                    skuStocked = true;
                    WareOrderTaskDetailEntity taskDetailEntity =
                            WareOrderTaskDetailEntity.builder()
                                                     .skuId(skuId).skuName("")
                                                     .skuNum(hasStock.getNum()).taskId(wareOrderTaskEntity.getId())
                                                     .wareId(wareId).lockStatus(1)
                                                     .build();
                    wareOrderTaskDetailService.save(taskDetailEntity);
    
                    // TODO: 2021/1/13 告诉MQ库存锁定成功
                    StockLockedTo lockedTo = new StockLockedTo();
                    lockedTo.setWareOrderTaskId(wareOrderTaskEntity.getId());
                    lockedTo.setWareOrderTaskDetailId(taskDetailEntity.getId());
                    rabbitTemplate.convertAndSend(stockEventExchange, stockLockedRoutingKey, lockedTo);
                    break;
                } else {
                    //当前仓库锁失败，重试下一个仓库
                }
            }
        }
        if (!skuStocked) {
            throw new BusinessException(WareErrorCode.WARE_SKU_NO_STOCK,  "OrderSn[" + lockTo.getOrderSn() + "]锁定失败，没有足够的库存!" );
        }
        //测试异常情况下解锁
        //int i = 10 /0;
        return true;
    }
    
    @Override
    @Transactional
    public void unlockStock(StockLockedTo to) {
        //库存工作单id
        Long detailId = to.getWareOrderTaskDetailId();
        log.info("++++++++收到解锁库存操作--wareOrdertaskId{}--taskDetailId{}++++++++", to.getWareOrderTaskId(), to.getWareOrderTaskDetailId());
    
        /**
         * 解锁
         * 1. 查询数据库关于这个订单锁定库存信息
         * 有： 证明库存锁定成功了
         * 解锁： 查询订单状况
         *
         * 1. 没有这个订单，必须解锁库存
         * 2. 有这个订单，不一定解锁库存
         *    订单状态： 已取消： 解锁库存
         *             已支持： 不能解锁库存
         */
        WareOrderTaskDetailEntity taskDetailInfo = wareOrderTaskDetailService.getById(detailId);
        if (taskDetailInfo != null) {
            //查出wms_ware_order_task工作单信息
            Long taskId = to.getWareOrderTaskId();
            WareOrderTaskEntity orderTaskInfo = wareOrderTaskService.getById(taskId);
            //获取订单号
            String orderSn = orderTaskInfo.getOrderSn();
            log.info("++++++++待解锁订单序列号{}--待解锁SKU{}--库存单详情{}++++++++", orderSn, taskDetailInfo.getSkuId(), taskDetailInfo.getId());
            //远程查询订单信息
            OrderTo orderInfo = remoteService.getOrderStatus(orderSn);
            //判断订单状态是否已取消或者订单不存在
            //调用过程中信息调用失败，抛出异常，StockReleaseListener会处处理这个异常
            //拒绝消息将消息重新放入队列中，让别人继续消费解锁
            if (orderInfo == null || orderInfo.getStatus() == 4) {
                //订单已被取消或者不存在才能解锁库存
                if (taskDetailInfo.getLockStatus() == 1) {
                    //当前库存工作单详情状态1，已锁定但是未解锁才可以解锁
                    unLockStock(taskDetailInfo.getSkuId(), taskDetailInfo.getWareId(), taskDetailInfo.getSkuNum(), detailId);
                }else {
                    log.info("++++++++收到解锁库存操作，库存已解锁，无需再解锁！！--wareOrdertaskId{}--taskDetailId{}++++++++", to.getWareOrderTaskId(), to.getWareOrderTaskDetailId());
                }
            }else {
                log.info("++++++++待解锁订单序列号{}--待解锁SKU{}--订单状态{}，保持锁定状态++++++++", orderSn, taskDetailInfo.getSkuId(), orderInfo.getStatus());
            }
        } else {
            log.info("++++++++收到解锁库存操作，没有查询到库存工单详情，无需再解锁！！--wareOrdertaskId{}--taskDetailId{}++++++++", to.getWareOrderTaskId(), to.getWareOrderTaskDetailId());
        }
    }
    @Override
    @Transactional
    public void unlockStock(OrderTo orderTo) {
        String orderSn = orderTo.getOrderSn();
        log.info("++++++++收到order方推送解锁库存操作--待解锁order序列号{}++++++++", orderTo.getOrderSn());
        //查最新的库存解锁状态，防止重复解锁
        WareOrderTaskEntity orderTaskEntity = wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
        
        //按照工作单的id找到所有没有解锁的库存，进行解锁
        Long id = orderTaskEntity.getId();
    
        List<WareOrderTaskDetailEntity> detailEntities = wareOrderTaskDetailService.listUnLockedByTaskId(id);
        detailEntities.forEach(entity -> {
            log.info("++++++++根据取消订单序列号{}--解锁SKU{}--库存单详情{}++++++++", orderSn, entity.getSkuId(), entity.getId());
            unLockStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum(), entity.getId());
            }
        );
    }
    
    /**
     * 解锁库存
     * @param skuId
     * @param wareId
     * @param skuNum
     * @param detailId
     */
    private void unLockStock(Long skuId, Long wareId, Integer skuNum, Long detailId) {
        //解锁库存
        this.baseMapper.unLockStock(skuId, wareId, skuNum);
        
        //更新工作单的状态，变更为已解锁
        WareOrderTaskDetailEntity taskDetailEntity =
                WareOrderTaskDetailEntity.builder()
                                         .id(detailId)
                                         .lockStatus(2)
                                         .build();
        wareOrderTaskDetailService.updateById(taskDetailEntity);
        
        //解锁失败异常测试
        //int i = 10/0;
    }
    
    @Override
    public List<Long> listWareIdHasSkuStock(Long skuId) {
        LambdaQueryWrapper<WareSkuEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(WareSkuEntity::getWareId)
                    .eq(WareSkuEntity::getSkuId, skuId)
                    .apply("stock - stock_locked > 0");
        return this.listObjs(queryWrapper, wareId -> (Long) wareId);
    }
    
    @Data
    class SkuWareHasStock {
        private Long skuId;
        private Integer num;
        private List<Long> wareId;
    }
}