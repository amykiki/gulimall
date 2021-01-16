package daily.boot.gulimall.service.api.feign;

import daily.boot.common.Result;
import daily.boot.gulimall.service.api.to.SkuInfoVo;
import daily.boot.gulimall.service.api.to.SpuInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "${gulimall.feign.product}")
public interface ProductFeignService {
    
    @GetMapping("/api/product/skuinfo/info/{skuId}")
    Result<SkuInfoVo> info(@PathVariable("skuId") Long skuId);
    
    @GetMapping("/api/product/skuinfo/{skuId}/price")
    Result<BigDecimal> getPrice(@PathVariable("skuId") Long skuId);
    
    @GetMapping("/product/skusaleattrvalue/stringList/{skuId}")
    Result<List<String>> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId);
    
    @GetMapping("/api/product/spuinfo/skuId/{skuId}")
    Result<SpuInfoTo> getSpuInfoBySkuId(@PathVariable("skuId") Long skuId);
    
    
}
