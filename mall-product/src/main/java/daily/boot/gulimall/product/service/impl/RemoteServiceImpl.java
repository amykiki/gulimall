package daily.boot.gulimall.product.service.impl;

import daily.boot.common.Result;
import daily.boot.gulimall.product.service.RemoteService;
import daily.boot.gulimall.service.api.feign.SeckillFeignService;
import daily.boot.gulimall.service.api.feign.WareFeignService;
import daily.boot.gulimall.service.api.service.AbstractRemoteService;
import daily.boot.gulimall.service.api.to.SeckillSkuItemVo;
import daily.boot.gulimall.service.api.to.SkuHasStockTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RemoteServiceImpl extends AbstractRemoteService implements RemoteService {
    @Autowired
    private WareFeignService wareFeignService;
    @Autowired
    private SeckillFeignService seckillFeignService;
    
    @Override
    @Cacheable(value = {"sku"}, key = "#root.methodName+ '-'+ #skuId")
    public boolean skuHasStock(Long skuId) {
        Result<List<SkuHasStockTo>> skuHasStock = wareFeignService.getSkuHasStock(Collections.singletonList(skuId));
        if (skuHasStock.getSucc()) {
            return skuHasStock.getData().get(0).getHasStock();
        }
        return false;
    }
    
    @Override
    public SeckillSkuItemVo getSkuSeckillInfo(Long skuId) {
        return call(() -> seckillFeignService.getSkuSeckillInfo(skuId));
    }
}
