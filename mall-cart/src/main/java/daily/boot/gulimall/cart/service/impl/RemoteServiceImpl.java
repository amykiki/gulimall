package daily.boot.gulimall.cart.service.impl;

import daily.boot.common.Result;
import daily.boot.common.exception.BusinessException;
import daily.boot.common.exception.error.CommonErrorCode;
import daily.boot.common.exception.error.ErrorCode;
import daily.boot.gulimall.cart.service.RemoteService;
import daily.boot.gulimall.service.api.feign.ProductFeignService;
import daily.boot.gulimall.service.api.service.AbstractRemoteService;
import daily.boot.gulimall.service.api.to.SkuInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandle;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;

@Service
@Slf4j
public class RemoteServiceImpl extends AbstractRemoteService implements RemoteService{
    @Autowired
    private ProductFeignService productFeignService;
    
    @Override
    public SkuInfoVo getSkuInfo(Long skuId) {
        return call(() -> productFeignService.info(skuId), "getSkuInfo");
    }
    
    @Override
    public List<String> getSkuSaleAttrValues(Long skuId) {
        return call(() -> productFeignService.getSkuSaleAttrValues(skuId), "getSkuSaleAttrValues");
    }
    
    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        return call(() -> productFeignService.getPrice(skuId));
    }
    
    //private <T> T call(Callable<Result<T>> fun, String methodName) {
    //    try {
    //        Result<T> rtn = fun.call();
    //        if (rtn.isOk()) {
    //            return rtn.getData();
    //        }
    //        log.error("call {} fail", methodName);
    //        throw new BusinessException(CommonErrorCode.RPC_ERROR);
    //    }catch (Exception e) {
    //        log.error("Call {} exception", methodName, e);
    //        throw new BusinessException(CommonErrorCode.RPC_ERROR);
    //    }
    //}
}
