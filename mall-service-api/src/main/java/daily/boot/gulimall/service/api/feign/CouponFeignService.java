package daily.boot.gulimall.service.api.feign;

import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.service.api.to.SkuReductionTo;
import daily.boot.gulimall.service.api.to.SpuBoundsTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "${gulimall.feign.coupon}")
public interface CouponFeignService {
    
    @PostMapping("/coupon/skufullreduction/saveSkuReduction")
    R saveSkuReduction(@RequestBody List<SkuReductionTo> skuReductionTos);
    
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);
}
