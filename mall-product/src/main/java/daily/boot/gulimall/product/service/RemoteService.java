package daily.boot.gulimall.product.service;

import daily.boot.gulimall.service.api.to.SeckillSkuItemVo;

/**
 * 远程调用Service包装类
 */
public interface RemoteService {
    boolean skuHasStock(Long skuId);
    
    SeckillSkuItemVo getSkuSeckillInfo(Long skuId);
}
