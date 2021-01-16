package daily.boot.gulimall.cart.service;


import daily.boot.gulimall.service.api.to.SkuInfoVo;

import java.math.BigDecimal;
import java.util.List;

public interface RemoteService {
    SkuInfoVo getSkuInfo(Long skuId);
    
    List<String> getSkuSaleAttrValues(Long skuId);
    
    BigDecimal getSkuPrice(Long skuId);
}
