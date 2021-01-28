package daily.boot.gulimall.seckill.service;

import daily.boot.gulimall.seckill.to.SeckillSkuRedisTo;

import java.util.List;

public interface SeckillService {
    void uploadSeckillSkuLatest3Days();
    
    String kill(String killId, String key, Integer num);
    
    List<SeckillSkuRedisTo> getCurrentSeckillSkus();
    
    SeckillSkuRedisTo getSkuSeckillInfo(Long skuId);
}
