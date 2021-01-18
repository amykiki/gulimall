package daily.boot.gulimall.ware.service;

import daily.boot.gulimall.service.api.to.MemberAddressTo;
import daily.boot.gulimall.service.api.to.OrderTo;

public interface RemoteService {
    MemberAddressTo getAddressInfoById(Long addrId);
    
    OrderTo getOrderStatus(String orderSn);
}
