package daily.boot.gulimall.seckill.service;

import daily.boot.gulimall.service.api.to.SeckillSessionWithSkusVo;
import daily.boot.gulimall.service.api.to.SkuInfoVo;

import java.util.List;

public interface RemoteService {
    SkuInfoVo getSkuInfo(Long skuId);
    
    List<SeckillSessionWithSkusVo> getLatest3DaySession();
}
