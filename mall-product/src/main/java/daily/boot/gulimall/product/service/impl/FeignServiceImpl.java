package daily.boot.gulimall.product.service.impl;

import daily.boot.common.Result;
import daily.boot.gulimall.product.service.FeignService;
import daily.boot.gulimall.service.api.feign.WareFeignService;
import daily.boot.gulimall.service.api.to.SkuHasStockTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class FeignServiceImpl implements FeignService {
    @Autowired
    private WareFeignService wareFeignService;
    
    @Override
    @Cacheable(value = {"sku"}, key = "#root.methodName+ '-'+ #skuId")
    public boolean skuHasStock(Long skuId) {
        Result<List<SkuHasStockTo>> skuHasStock = wareFeignService.getSkuHasStock(Collections.singletonList(skuId));
        if (skuHasStock.getSucc()) {
            return skuHasStock.getData().get(0).getHasStock();
        }
        return false;
    }
}
