package daily.boot.gulimall.ware.service.impl;

import daily.boot.gulimall.service.api.feign.MemberFeignService;
import daily.boot.gulimall.service.api.service.AbstractRemoteService;
import daily.boot.gulimall.service.api.to.MemberAddressTo;
import daily.boot.gulimall.ware.service.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoteServiceImpl extends AbstractRemoteService implements RemoteService {
    @Autowired
    private MemberFeignService memberFeignService;
    @Override
    public MemberAddressTo getAddressInfoById(Long addrId) {
        return call(() -> memberFeignService.getByAddrId(addrId));
    }
}
