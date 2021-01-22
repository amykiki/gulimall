package daily.boot.gulimall.service.api.service;

import daily.boot.common.Result;
import daily.boot.common.exception.BusinessException;
import daily.boot.common.exception.error.CommonErrorCode;
import daily.boot.gulimall.common.page.PageInfo;
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
            log.error("call {} fail, fail message{}", methodName, rtn.getMsg() + rtn.getCode());
            throw new BusinessException(rtn.getMsg(), rtn.getCode());
        }catch (Exception e) {
            if (!(e instanceof BusinessException)) {
                log.error("Call {} exception", methodName, e);
                throw new BusinessException(CommonErrorCode.RPC_ERROR);
            }else {
                throw (BusinessException)e;
            }
        }
    }
    
    protected  <T> T call(Callable<Result<T>> fun) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callerStackTraceElement = stackTraceElements[2];
        String className = callerStackTraceElement.getClassName();
        String methodName = callerStackTraceElement.getMethodName();
        return call(fun, className + "-" + methodName);
    }
    
}
