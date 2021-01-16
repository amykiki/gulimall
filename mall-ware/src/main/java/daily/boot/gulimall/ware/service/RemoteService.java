package daily.boot.gulimall.ware.service;

import daily.boot.gulimall.service.api.to.MemberAddressTo;

public interface RemoteService {
    MemberAddressTo getAddressInfoById(Long addrId);
}
