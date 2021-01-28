package daily.boot.gulimall.seckill.scheduled;

import daily.boot.gulimall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * 秒杀商品定时上架
 * 每天晚上3点，上架最近三天需要秒杀的商品
 * 当天00:00:00 - 23:59:59
 * 明天00:00:00 - 23:59:59
 * 后台00:00:00 - 23:59:59
 */
@Slf4j
@Service
public class SecKillScheduled {
    @Autowired
    private SeckillService seckillService;
    
    @Autowired
    private RedissonClient redissonClient;
    
    //秒杀商品上架功能的锁
    private final String UPLOAD_LOCK = "seckill:upload:lock";
    
    // TODO: 2021/1/27 暂时注释 
    //保持幂等性
    //@Scheduled(cron = "0 0 3 * * ?")
    //@Scheduled(cron = "*/5 * * * * ?")
    public void uploadSeckillSkuLates3Days() {
        //1. 重复上架无需处理
        log.info("=====定时上架秒杀商品=====");
        
        
        //2. 分布式锁，防止多个应用同时上架
        RLock lock = redissonClient.getLock(UPLOAD_LOCK);
        try {
            //加锁
            lock.lock(10, TimeUnit.SECONDS);
            //保证同时只有一个应用在进行上架，
            //以防重复
            seckillService.uploadSeckillSkuLatest3Days();
            log.info("=====秒杀商品上架成功=====");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        
    }
    
}
