package daily.boot.gulimall.service.api.feign;

import daily.boot.gulimall.common.utils.Result;
import daily.boot.gulimall.service.api.to.SkuInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${gulimall.feign.product}")
public interface ProductFeignService {
    
    @GetMapping("/info/{skuId}")
    Result<SkuInfoVo> info(@PathVariable("skuId") Long skuId);
}
