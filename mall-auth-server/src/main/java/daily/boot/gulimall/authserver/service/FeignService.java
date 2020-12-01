package daily.boot.gulimall.authserver.service;

import daily.boot.common.Result;
import daily.boot.gulimall.authserver.vo.UserRegisterVo;
import daily.boot.gulimall.service.api.to.MemberUserTo;
import daily.boot.gulimall.service.api.to.SmsTo;

public interface FeignService {
    void sendVerifyCode(SmsTo smsTo);
    
    Result register(UserRegisterVo registerVo);
    
    MemberUserTo getUserInfoByUsername(String username);
    
}
