package daily.boot.gulimall.seckill.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import daily.boot.gulimall.seckill.security.LoginUserInfo;
import daily.boot.gulimall.seckill.security.LoginUserInfoHolder;
import daily.boot.gulimall.seckill.service.RemoteService;
import daily.boot.gulimall.seckill.service.SeckillService;
import daily.boot.gulimall.seckill.to.SeckillSkuRedisTo;
import daily.boot.gulimall.service.api.to.SeckillSessionWithSkusVo;
import daily.boot.gulimall.service.api.to.SkuInfoVo;
import daily.boot.gulimall.service.api.to.mq.SeckillOrderTo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private RemoteService remoteService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Value("${gulimall.seckill.mq.order-event-exchange}")
    private String orderEventExchange;
    @Value("${gulimall.seckill.mq.order-seckill-order-route-key}")
    private String orderSeckillOrderRouteKey;
    
    private final String SESSION_CACHE_PREFIX = "seckill:sessions:";
    private final String SECKILL_CHARE_REFIX = "seckill:skus";
    //商品库存信号量
    private final String SKU_STOCK_SEMAPHONE = "seckill:stock:"; //+商品随机码
    
    
    @Override
    public void uploadSeckillSkuLatest3Days() {
        // 1. 扫描最近三天需要参加秒杀的商品
        List<SeckillSessionWithSkusVo> sessionData = remoteService.getLatest3DaySession();
        
        //缓存到Redis
        //1. 缓存活动信息
        saveSessionInfos(sessionData);
        
        //2. 缓存活动的关联商品信息
        saveSessionSkuInfo(sessionData);
        
    }
    
    /**
     * 缓存秒杀活动所关联的商品信息
     * @param sessions
     */
    private void saveSessionSkuInfo(List<SeckillSessionWithSkusVo> sessions) {
        sessions.forEach(session -> {
            //准备hash操作，绑定hash
            BoundHashOperations<String, String, SeckillSkuRedisTo> operations = redisTemplate.boundHashOps(SECKILL_CHARE_REFIX);
            session.getRelationSkus().forEach(seckillSkuVo -> {
                //生成随机码
                String token = UUID.randomUUID().toString().replace("-", "");
                String hashKey = seckillSkuVo.getPromotionSessionId().toString() + "-" + seckillSkuVo.getSkuId().toString();
                //幂等！！
                if (!operations.hasKey(hashKey)) {
                    //缓存商品信息
                    SeckillSkuRedisTo redisTo = new SeckillSkuRedisTo();
                    Long skuId = seckillSkuVo.getSkuId();
                    
                    //1. 先查询sku的基本信息，调用远程服务
                    SkuInfoVo skuInfo = remoteService.getSkuInfo(skuId);
                    redisTo.setSkuInfo(skuInfo);
                    
                    //2. sku的秒杀信息
                    BeanUtils.copyProperties(seckillSkuVo, redisTo);
                    
                    //3. 设置当前商品的秒杀时间信息
                    redisTo.setStartTime(session.getStartTime().getTime());
                    redisTo.setEndTime(session.getEndTime().getTime());
                    
                    //4. 设置商品的随机码，防止恶意攻击
                    redisTo.setRandomCode(token);
                    
                    //保存入redis中
                    operations.put(hashKey, redisTo);
                    
                    //如果当前这个场次的商品库存信息已经上架就不需要上架
                    
                    //5. 使用库存做为分布式Redission信号量，来限流
                    // 使用库存做为分布式信号量
    
                    RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHONE + token);
                    //商品可以秒杀的数量做为信号量
                    //trySetPermits
                    // 1). 信号量设置成功，返回true
                    // 2). 信号量已经被设置过，返回false
                    semaphore.trySetPermits(seckillSkuVo.getSeckillCount());
                }
            });
        });
    }
    
    /**
     * 缓存秒杀活动信息
     *
     * @param sessions
     */
    private void saveSessionInfos(List<SeckillSessionWithSkusVo> sessions) {
        sessions.forEach(session -> {
            //获取当前活动的开始和结束时间的时间戳
            long startTime = session.getStartTime().getTime();
            long endTime = session.getEndTime().getTime();
            
            //存入到Redis中做为key
            String key = SESSION_CACHE_PREFIX + startTime + "_" + endTime;
            //判断Redis中是否有该信息，没有才进行添加
            //幂等性
            Boolean hasKey = redisTemplate.hasKey(key);
            //缓存活动信息
            if (!hasKey) {
                //获取到活动中所有商品的skuId
                List<String> skuIds = session.getRelationSkus()
                                             .stream()
                                             .map(item -> item.getPromotionSessionId() + "-" + item.getSkuId().toString())
                                             .collect(Collectors.toList());
                //注意，这里有个小bug，如果直接传list，会导致传入的list整个被做为一个值保存到redis中，
                //参考 RedisTemplate使用rightPushAll往list中添加时的注意事项 https://www.cnblogs.com/lwjQAQ/p/12619797.html
                //redisTemplate.opsForList().leftPushAll(key, skuIds);
                String[] skuIdArray = skuIds.toArray(new String[0]);
                redisTemplate.opsForList().leftPushAll(key, skuIdArray);
            }
        });
    }
    
    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        //1. 确定当前属于哪个秒杀场次
        long currentTime = System.currentTimeMillis();
        
        //从Redis中查询到所有key以seckill:sessions开头的数据
        Set<String> keys = redisTemplate.keys(SESSION_CACHE_PREFIX + "*");
        Pattern pattern = Pattern.compile(".*?(\\d+)_(\\d+)");
        for (String key : keys) {
            //seckill:sessions:1594396764000_1594453242000
            //value为skuIds
            Matcher matcher = pattern.matcher(key);
            if (!matcher.matches()) {
                continue;
            }
            long startTime = Long.parseLong(matcher.group(1));
            long endTime = Long.parseLong(matcher.group(2));
            
            //判断是否是当前秒杀场次
            if (currentTime >= startTime && currentTime <= endTime) {
                //2. 获取秒杀场次需要的所有商品信息
                List<Object> range = redisTemplate.opsForList().range(key, 0, -1);
                BoundHashOperations<String, String, SeckillSkuRedisTo> hashOps = redisTemplate.boundHashOps(SECKILL_CHARE_REFIX);
                if (CollectionUtils.isEmpty(range)) {
                    continue;
                }
                List<String> skuKeys = range.stream().map(Object::toString).collect(Collectors.toList());
                List<SeckillSkuRedisTo> seckillSkuRedisTos = hashOps.multiGet(skuKeys);
                return seckillSkuRedisTos;
            }
        }
        return null;
    }
    
    /**
     * 根据skuId查询商品是否参加了秒杀活动
     * @param skuId
     * @return
     */
    @Override
    public SeckillSkuRedisTo getSkuSeckillInfo(Long skuId) {
        //1. 找到所有需要秒杀的商品的key信息 --- seckill:skus
        BoundHashOperations<String, String, SeckillSkuRedisTo> hashOps = redisTemplate.boundHashOps(SECKILL_CHARE_REFIX);
        SeckillSkuRedisTo skuRedisTo = null;
        //拿到所有key，进行正则匹配
        Set<String> keys = hashOps.keys();
        Pattern pattern = Pattern.compile("\\d+-" + skuId);
        for (String key : keys) {
            Matcher matcher = pattern.matcher(key);
            if (matcher.matches()) {
                //从Redis中取出数据
                SeckillSkuRedisTo seckillSkuRedisTo = hashOps.get(key);
                if (seckillSkuRedisTo == null) {
                    continue;
                }
                //校验时间
                long currentTime = System.currentTimeMillis();
                Long startTime = seckillSkuRedisTo.getStartTime();
                Long endTime = seckillSkuRedisTo.getEndTime();
                //判断当前时间是否处于活动时间内，如果是，则直接返回该结果
                //如果当前时间大于结束时间，则继续
                //如果当前时间小于开始时间，则记录下，并且随机码需要置空
                if (currentTime > endTime) {
                    continue;
                } else if (currentTime >= startTime) {
                    return seckillSkuRedisTo;
                } else {
                    //需比较一个时间最接近当前的返回
                    seckillSkuRedisTo.setRandomCode(null);
                    if (skuRedisTo == null) {
                        skuRedisTo = seckillSkuRedisTo;
                    } else {
                        if (skuRedisTo.getStartTime() > seckillSkuRedisTo.getStartTime()) {
                            skuRedisTo = seckillSkuRedisTo;
                        }
                    }
                }
                
                
            }
        }
        return skuRedisTo;
    }
    
    /**
     *
     * @param killId 秒杀id
     * @param key   商品随机码
     * @param num 购买数量
     * @return
     */
    @Override
    public String kill(String killId, String key, Integer num) {
        long s1 = System.currentTimeMillis();
        //获取当前用户信息
        LoginUserInfo loginUserInfo = LoginUserInfoHolder.getLoginUserInfo();
        
        //1. 从redis中获取当前秒杀商品的详细信息
        BoundHashOperations<String, String, SeckillSkuRedisTo> hashOps = redisTemplate.boundHashOps(SECKILL_CHARE_REFIX);
        SeckillSkuRedisTo skuRedisTo = hashOps.get(killId);
        if (skuRedisTo == null) {
            return null;
        }
        
        //合法性校验
        Long startTime = skuRedisTo.getStartTime();
        Long endTime = skuRedisTo.getEndTime();
        long currentTime = System.currentTimeMillis();
        //当前秒杀请求应该在活动区间内
        if (currentTime < startTime || currentTime > endTime) {
            return null;
        }
        
        //校验随机码和商品id
        String randomCode = skuRedisTo.getRandomCode();
        String skuId = skuRedisTo.getPromotionSessionId() + "-" + skuRedisTo.getSkuId();
        if (!randomCode.equals(key) || !skuId.equals(killId)) {
            return null;
        }
        //验证购物数量是否合理
        Integer seckillLimit = skuRedisTo.getSeckillLimit();
        if (num > seckillLimit) {
            return null;
        }
        //获取信号量，验证购物库存量是否充足
        Integer seckillCount = (Integer) redisTemplate.opsForValue().get(SKU_STOCK_SEMAPHONE + randomCode);
        if (seckillCount >= num) {
            //验证该账号是否已经买过了，幂等性处理
            //如果秒杀成功，就去占位，userId-sessionId-skuId
            //SETNX原子性处理
            String redisKey = loginUserInfo.getUserId() + "-" + skuId;
            //设置自动过期时间，活动结束时间 - 当前时间
            long ttl = endTime - currentTime;
            Boolean killed = redisTemplate.opsForValue().setIfAbsent(redisKey, num.toString(), ttl, TimeUnit.MILLISECONDS);
            if (killed) {
                //占位成功，说明从来没有买过
                //通过分布式锁，信号量来扣减库存
                RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHONE + randomCode);
                //秒杀成功，快速下单
                try {
                    boolean countStock = semaphore.tryAcquire(num, 50, TimeUnit.MILLISECONDS);
                    if (countStock) {
                        //创建订单号和订单信息发送给MQ
                        //秒杀成功，快速下单，发送消息到MQ
                        String orderSn = IdWorker.getTimeId();
                        SeckillOrderTo seckillOrderTo = SeckillOrderTo.builder()
                                                             .orderSn(orderSn)
                                                             .memberId(loginUserInfo.getUserId())
                                                             .num(num)
                                                             .promotionSessionId(skuRedisTo.getPromotionSessionId())
                                                             .skuId(skuRedisTo.getSkuId())
                                                             .seckillPrice(skuRedisTo.getSeckillPrice())
                                                             .build();
                        rabbitTemplate.convertAndSend(orderEventExchange, orderSeckillOrderRouteKey, seckillOrderTo, new CorrelationData(seckillOrderTo.getOrderSn()));
                        long s2 = System.currentTimeMillis();
                        log.info("秒杀成功，耗时。。。{}", s2 - s1);
                        //返回订单序列号
                        return orderSn;
                    }else {
                        //扣减库存失败，同样删去占位
                        redisTemplate.delete(redisKey);
                    }
                } catch (Exception e) {
                    //删去占位
                    redisTemplate.delete(redisKey);
                }
            }
        }
        
        return null;
    }
}
