package daily.boot.gulimall.seckill.service.impl;

import daily.boot.gulimall.seckill.service.RemoteService;
import daily.boot.gulimall.service.api.feign.CouponFeignService;
import daily.boot.gulimall.service.api.feign.ProductFeignService;
import daily.boot.gulimall.service.api.service.AbstractRemoteService;
import daily.boot.gulimall.service.api.to.SeckillSessionWithSkusVo;
import daily.boot.gulimall.service.api.to.SkuInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RemoteServiceImp extends AbstractRemoteService implements RemoteService {
    @Autowired
    private ProductFeignService productFeignService;
    
    @Autowired
    private CouponFeignService couponFeignService;
    
    @Override
    public SkuInfoVo getSkuInfo(Long skuId) {
        return call(() -> productFeignService.info(skuId));
    }
    
    @Override
    public List<SeckillSessionWithSkusVo> getLatest3DaySession() {
        return call(() -> couponFeignService.getLatest3DaySession());
    }
}
