package daily.boot.gulimall.order.service.impl;

import daily.boot.gulimall.order.service.RemoteService;
import daily.boot.gulimall.service.api.to.OrderItemTo;
import daily.boot.gulimall.service.api.feign.CartFeignService;
import daily.boot.gulimall.service.api.feign.MemberFeignService;
import daily.boot.gulimall.service.api.feign.ProductFeignService;
import daily.boot.gulimall.service.api.feign.WareFeignService;
import daily.boot.gulimall.service.api.service.AbstractRemoteService;
import daily.boot.gulimall.service.api.to.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RemoteServiceImpl extends AbstractRemoteService implements RemoteService {
    @Autowired
    private MemberFeignService memberFeignService;
    
    @Autowired
    private CartFeignService cartFeignService;
    
    @Autowired
    private WareFeignService wareFeignService;
    
    @Autowired
    private ProductFeignService productFeignService;
    
    @Override
    public List<MemberAddressTo> getMemberAddress(Long userId) {
        return call(() -> memberFeignService.getAddress(userId));
    }
    
    @Override
    public List<OrderItemTo> getCurrentCartItems(Long userId) {
        List<CartItemTo> rtn = call(() -> cartFeignService.getCurrentCartItems(userId));
        return rtn.stream().map(cartItem -> {
            OrderItemTo orderItemTo = new OrderItemTo();
            BeanUtils.copyProperties(cartItem, orderItemTo);
            return orderItemTo;
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<SkuHasStockTo> getSkuHasStock(List<Long> skuIds) {
        return call(() -> wareFeignService.getSkuHasStock(skuIds));
    }
    
    @Override
    public MemberFullInfoTo getMemberIntegration(Long userId) {
        return call(() -> memberFeignService.getMemberFullInfo(userId));
    }
    
    @Override
    public FareTo getFare(Long addrId) {
        return call(() -> wareFeignService.getFare(addrId));
    }
    
    @Override
    public SpuInfoTo getSpuInfoBySkuId(Long skuId) {
        return call(() -> productFeignService.getSpuInfoBySkuId(skuId));
    }
    
    @Override
    public boolean orderLockStock(WareSkuLockTo lockTo) {
        return call(() -> wareFeignService.orderLockStock(lockTo));
    }
}
