package daily.boot.gulimall.service.api.feign;

import daily.boot.common.Result;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.service.api.to.SeckillSessionWithSkusVo;
import daily.boot.gulimall.service.api.to.SeckillSkuVo;
import daily.boot.gulimall.service.api.to.SkuReductionTo;
import daily.boot.gulimall.service.api.to.SpuBoundsTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "${gulimall.feign.coupon}")
public interface CouponFeignService {
    
    @PostMapping("/api//coupon/skufullreduction/saveSkuReduction")
    R saveSkuReduction(@RequestBody List<SkuReductionTo> skuReductionTos);
    
    @PostMapping("/api/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);
    
    @GetMapping("/api/coupon/seckillsession/Latest3DaySession")
    Result<List<SeckillSessionWithSkusVo>> getLatest3DaySession();
}
