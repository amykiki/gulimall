package daily.boot.gulimall.service.api.feign;

import daily.boot.common.Result;
import daily.boot.gulimall.service.api.to.SmsTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${gulimall.feign.third}")
public interface ThirdPartyFeignService {
    
    @PostMapping("/sms/sendSms")
    Result<Boolean> sendSmsVerifyCode(@RequestBody SmsTo smsTo);
}
