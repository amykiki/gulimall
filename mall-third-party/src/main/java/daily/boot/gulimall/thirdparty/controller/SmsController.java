package daily.boot.gulimall.thirdparty.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.thirdparty.service.AliSmsService;
import daily.boot.gulimall.thirdparty.vo.SmsVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/sms")
@Api(tags = "短信发送接口")
public class SmsController {
    @Autowired
    private AliSmsService smsService;
    
    @PostMapping("/sendSms")
    public Result<Boolean> sendSmsVerifyCode(@RequestBody @Validated SmsVo smsVo) {
        boolean rtn = smsService.sendRegVerifyCode(smsVo);
        //boolean rtn = smsService.sendRegVerifyCode(null);
        return Result.ok(rtn);
    }
}
