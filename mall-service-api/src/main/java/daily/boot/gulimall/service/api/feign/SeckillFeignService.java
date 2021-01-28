package daily.boot.gulimall.service.api.feign;

import daily.boot.common.Result;
import daily.boot.gulimall.service.api.to.SeckillSkuItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${gulimall.feign.seckill}")
public interface SeckillFeignService {
    
    
    /**
     * 根据skuId查询商品是否参加秒杀活动
     * @param skuId
     * @return
     */
    @GetMapping("/api/seckill/sku/seckill/{skuId}")
    Result<SeckillSkuItemVo> getSkuSeckillInfo(@PathVariable("skuId") Long skuId);
}
