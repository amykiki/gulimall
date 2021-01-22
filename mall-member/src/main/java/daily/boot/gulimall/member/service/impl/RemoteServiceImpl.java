package daily.boot.gulimall.member.service.impl;

import daily.boot.common.Result;
import daily.boot.common.exception.BusinessException;
import daily.boot.common.exception.error.CommonErrorCode;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.member.service.RemoteService;
import daily.boot.gulimall.service.api.feign.OrderFeignService;
import daily.boot.gulimall.service.api.service.AbstractRemoteService;
import daily.boot.gulimall.service.api.to.OrderTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RemoteServiceImpl extends AbstractRemoteService implements RemoteService {
    @Autowired
    private OrderFeignService orderFeignService;
    
    
    @Override
    public PageInfo<OrderTo> listWithItem(PageQueryVo pageQueryVo) {
        try {
            Result<PageInfo<OrderTo>> rtn = orderFeignService.listWithItem(pageQueryVo);
            if (rtn.isOk()) {
                return rtn.getData();
            }
            log.error("call {} fail, fail message{}", "listWithItem", rtn.getMsg() + rtn.getCode());
            throw new BusinessException(rtn.getMsg(), rtn.getCode());
        }catch (Exception e) {
            if (!(e instanceof BusinessException)) {
                log.error("Call {} exception", "listWithItem", e);
                throw new BusinessException(CommonErrorCode.RPC_ERROR);
            }else {
                throw (BusinessException)e;
            }
        }
    }
}
