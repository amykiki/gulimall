package daily.boot.gulimall.member.webController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member/user")
public class OAuthTestController {
    
    @GetMapping("/test")
    public String oauthclient() {
        return "oauthclient.html";
    }
}
