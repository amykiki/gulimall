package daily.boot.gulimall.seckill.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.seckill.service.SeckillService;
import daily.boot.gulimall.seckill.to.SeckillSkuRedisTo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seckill")
@Api(tags = "秒杀后台")
public class SeckillController {
    @Autowired
    private SeckillService seckillService;
    
    /**
     * 当前时间可以参与秒杀的商品信息
     * @return
     */
    @GetMapping("/getCurrentSeckillSkus")
    public Result<List<SeckillSkuRedisTo>> getCurrentSeckillSkus() {
        //获取当前可以参加秒杀商品的信息
        List<SeckillSkuRedisTo> vos = seckillService.getCurrentSeckillSkus();
        return Result.ok(vos);
    }
    
    
    /**
     * 根据skuId查询商品是否参加秒杀活动
     * @param skuId
     * @return
     */
    @GetMapping("/sku/seckill/{skuId}")
    public Result<SeckillSkuRedisTo> getSkuSeckillInfo(@PathVariable("skuId") Long skuId) {
        SeckillSkuRedisTo to = seckillService.getSkuSeckillInfo(skuId);
        return Result.ok(to);
    }
    
    @GetMapping("/test/uploadSeckill")
    public Result<Boolean> testUploadSeckillSkuLates3Days() {
        seckillService.uploadSeckillSkuLatest3Days();
        return Result.ok(true);
    }
    
}
