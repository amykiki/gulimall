package daily.boot.gulimall.product.service;

/**
 * 远程调用Service包装类
 */
public interface FeignService {
    boolean skuHasStock(Long skuId);
}
