package daily.boot.gulimall.service.api.feign;

import daily.boot.common.Result;
import daily.boot.gulimall.service.api.to.FareTo;
import daily.boot.gulimall.service.api.to.SkuHasStockTo;
import daily.boot.gulimall.service.api.to.SkuInfoVo;
import daily.boot.gulimall.service.api.to.WareSkuLockTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "${gulimall.feign.ware}")
public interface WareFeignService {
    
    @PostMapping("/ware/waresku/hasStock")
    Result<List<SkuHasStockTo>> getSkuHasStock(@RequestBody List<Long> skuIds);
    
    @GetMapping("/ware/wareinfo/fare")
    Result<FareTo> getFare(@RequestParam("addrId") Long addrId);
    
    @PostMapping("/ware/waresku/lock/order")
    Result<Boolean> orderLockStock(@RequestBody WareSkuLockTo lockTo);
}
