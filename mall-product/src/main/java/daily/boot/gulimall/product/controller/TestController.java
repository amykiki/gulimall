package daily.boot.gulimall.product.controller;

import daily.boot.gulimall.product.service.TestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("test")
@Api(tags = "测试接口")
public class TestController {
    @Autowired
    private TestService testService;
    
    @GetMapping("/smscode/{phone}")
    public String getSmsCode(@PathVariable(value = "phone") String phone) {
        return testService.getSmsCode(phone);
    }
    
    @PostMapping("/smscode/verify")
    public String verifySmsCode(String phone, String smsCode) {
        return testService.verifySmsCode(phone, smsCode);
    }
    
    @PostMapping("/smscode/clearall")
    public String clearAllSmsCode() {
        testService.clearAllSmsCodes();;
        return "cleared";
    }
}
