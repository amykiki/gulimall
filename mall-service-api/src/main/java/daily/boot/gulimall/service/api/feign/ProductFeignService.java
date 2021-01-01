package daily.boot.gulimall.service.api.feign;

import daily.boot.common.Result;
import daily.boot.gulimall.service.api.to.SkuInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "${gulimall.feign.product}")
public interface ProductFeignService {
    
    @GetMapping("/product/skuinfo/info/{skuId}")
    Result<SkuInfoVo> info(@PathVariable("skuId") Long skuId);
    
    @GetMapping("/product/skusaleattrvalue/stringList/{skuId}")
    Result<List<String>> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId);
    
}
