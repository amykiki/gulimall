package daily.boot.gulimall.service.api.feign;

import daily.boot.common.Result;
import daily.boot.gulimall.service.api.to.SkuEsTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "${gulimall.feign.search}")
public interface SearchFeignService {
    
    /**
     * 上架商品
     */
    @PostMapping("/search/save/product")
    Result<Boolean> productStatusUp(@RequestBody List<SkuEsTo> skuEsList);
}
