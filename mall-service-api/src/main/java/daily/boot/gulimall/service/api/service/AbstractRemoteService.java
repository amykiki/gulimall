package daily.boot.gulimall.service.api.service;

import daily.boot.common.Result;
import daily.boot.common.exception.BusinessException;
import daily.boot.common.exception.error.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public abstract class AbstractRemoteService {
    protected  <T> T call(Callable<Result<T>> fun, String methodName) {
        try {
            Result<T> rtn = fun.call();
            if (rtn.isOk()) {
                return rtn.getData();
            }
            log.error("call {} fail", methodName);
            throw new BusinessException(CommonErrorCode.RPC_ERROR);
        }catch (Exception e) {
            log.error("Call {} exception", methodName, e);
            throw new BusinessException(CommonErrorCode.RPC_ERROR);
        }
    }
}