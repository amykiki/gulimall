package daily.boot.gulimall.order.service;

import daily.boot.gulimall.service.api.to.OrderItemTo;
import daily.boot.gulimall.service.api.to.*;

import java.util.List;

public interface RemoteService {
    List<MemberAddressTo> getMemberAddress(Long userId);
    
    List<OrderItemTo> getCurrentCartItems(Long userId);
    
    List<SkuHasStockTo> getSkuHasStock(List<Long> skuIds);
    
    MemberFullInfoTo getMemberIntegration(Long userId);
    
    FareTo getFare(Long addrId);
    
    SpuInfoTo getSpuInfoBySkuId(Long skuId);
    
    boolean orderLockStock(WareSkuLockTo lockTo);
}
