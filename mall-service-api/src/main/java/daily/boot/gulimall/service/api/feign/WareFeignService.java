package daily.boot.gulimall.service.api.feign;

import daily.boot.common.Result;
import daily.boot.gulimall.service.api.to.SkuHasStockTo;
import daily.boot.gulimall.service.api.to.SkuInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "${gulimall.feign.ware}")
public interface WareFeignService {
    
    @PostMapping("/ware/waresku/hasStock")
    Result<List<SkuHasStockTo>> getSkuHasStock(@RequestBody List<Long> skuIds);
}
