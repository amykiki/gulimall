package daily.boot.gulimall.authserver.controller;

import daily.boot.gulimall.authserver.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
    public String user(Authentication authentication, Model model, HttpServletRequest request) {
        Object principal = authentication.getPrincipal();
        String header = request.getHeader("x-forwarded-for");
        String remoteAddr = request.getRemoteAddr();
        model.addAttribute("loginUser", principal);
        return "user";
    }
}
