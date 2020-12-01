package daily.boot.gulimall.authserver.service.impl;

import daily.boot.common.Result;
import daily.boot.common.exception.BusinessException;
import daily.boot.gulimall.authserver.exception.AuthErrorCode;
import daily.boot.gulimall.authserver.service.FeignService;
import daily.boot.gulimall.authserver.vo.UserRegisterVo;
import daily.boot.gulimall.service.api.feign.MemberFeignService;
import daily.boot.gulimall.service.api.feign.ThirdPartyFeignService;
import daily.boot.gulimall.service.api.to.MemberUserTo;
import daily.boot.gulimall.service.api.to.SmsTo;
import daily.boot.gulimall.service.api.to.UserRegisterTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@Slf4j
public class FeignServiceImpl implements FeignService {
    @Autowired
    private ThirdPartyFeignService thirdPartyFeignService;
    
    @Autowired
    private MemberFeignService memberFeignService;
 
    @Override
    public void sendVerifyCode(SmsTo smsTo) {
        Result<Boolean> rtn = thirdPartyFeignService.sendSmsVerifyCode(smsTo);
        if (!rtn.isOk() || !rtn.getData()) {
            throw new BusinessException(rtn.getMsg(), rtn.getCode());
        }
    }
    
    
    @Override
    public Result register(UserRegisterVo registerVo) {
        UserRegisterTo to = new UserRegisterTo();
        BeanUtils.copyProperties(registerVo, to);
        try {
            return memberFeignService.register(to);
        } catch (Exception e) {
            log.error("注册用户{}出现异常", registerVo, e);
            return Result.fail(AuthErrorCode.AUTH_REG_EXCEPTION.getCode(), e.getMessage());
        }
    }
    
    @Override
    public MemberUserTo getUserInfoByUsername(@NotNull(message = "用户名不能为空") String username) {
        try {
            Result<MemberUserTo> result = memberFeignService.getUserInfoByUsername(username);
            if (result.isOk()) {
                return result.getData();
            } else {
                log.info("根据用户名{}查找用户失败{}", username, result);
                return null;
            }
        } catch (Exception e) {
            log.warn("根据用户名{}查找用户发生异常", username, e);
            throw e;
        }
    }
}
