package daily.boot.gulimall.ware.service.impl;

import daily.boot.gulimall.service.api.feign.MemberFeignService;
import daily.boot.gulimall.service.api.feign.OrderFeignService;
import daily.boot.gulimall.service.api.service.AbstractRemoteService;
import daily.boot.gulimall.service.api.to.MemberAddressTo;
import daily.boot.gulimall.service.api.to.OrderTo;
import daily.boot.gulimall.ware.service.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoteServiceImpl extends AbstractRemoteService implements RemoteService {
    @Autowired
    private MemberFeignService memberFeignService;
    @Autowired
    private OrderFeignService orderFeignService;
    @Override
    public MemberAddressTo getAddressInfoById(Long addrId) {
        return call(() -> memberFeignService.getByAddrId(addrId));
    }
    
    @Override
    public OrderTo getOrderStatus(String orderSn) {
        return call(() -> orderFeignService.getOrderStatus(orderSn));
    }
}
