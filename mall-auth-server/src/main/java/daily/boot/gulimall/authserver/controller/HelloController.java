package daily.boot.gulimall.authserver.controller;

import daily.boot.gulimall.authserver.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    
    @GetMapping("/hello.html")
    public String hello() {
        return "hello";
    }
    
    @GetMapping("/home.html")
    public String home() {
        return "home";
    }
    
    @GetMapping("/user.html")
    public String user(Authentication authentication, Model model) {
        Object principal = authentication.getPrincipal();
    
        model.addAttribute("loginUser", principal);
        return "user";
    }
}
