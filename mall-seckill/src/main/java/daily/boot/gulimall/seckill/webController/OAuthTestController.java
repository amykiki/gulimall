package daily.boot.gulimall.seckill.webController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seckill/user")
public class OAuthTestController {
    
    @GetMapping("/test")
    public String oauthclient() {
        return "oauthclient.html";
    }
}
