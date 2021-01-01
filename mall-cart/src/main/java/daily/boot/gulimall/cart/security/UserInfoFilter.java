package daily.boot.gulimall.cart.security;

import daily.boot.common.Result;
import daily.boot.gulimall.service.api.feign.MemberFeignService;
import daily.boot.gulimall.service.api.to.MemberUserTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Spring Security的authentication中不包含userId。新加filter，保存userId
 *
 * 注意，在springboot中，使用了@bean @component注解的filter，会自动注册，
 * 参考https://docs.spring.io/spring-boot/docs/1.5.x/reference/htmlsingle/#howto-add-a-servlet-filter-or-listener-as-spring-bean
 * https://stackoverflow.com/questions/38820782/does-spring-security-do-automatic-filter-injection
 * 要禁止自动注册，需要在webconfig中定义
 * @Bean
 * public FilterRegistrationBean registration(MyFilter filter) {
 *     FilterRegistrationBean registration = new FilterRegistrationBean(filter);
 *     registration.setEnabled(false);
 *     return registration;
 * }
 */
@Component
@Slf4j
public class UserInfoFilter extends OncePerRequestFilter {
    private static final String USER_INFO_ATTR = "LOGIN_USER_INFO";
    
    @Autowired
    private MemberFeignService memberFeignService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean changed = false;
        LoginUserInfo loginUserInfo = readUserInfoFromSession(request);
        if (loginUserInfo == null || loginUserInfo.getUserId() == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
                //userInfo为null但是authentication存在，说明这是第一次访问
                //查询member库，获取userid
                String username = (String) authentication.getPrincipal();
                Long userId = null;
                try {
                    Result<MemberUserTo> rtn = memberFeignService.getUserInfoByUsername(username);
                    if (rtn.isOk() && rtn.getData() != null) {
                        userId = rtn.getData().getId();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (userId != null) {
                    if (loginUserInfo == null) {
                        loginUserInfo = new LoginUserInfo();
                    }
                    loginUserInfo.setUserId(userId);
                    loginUserInfo.setUserName(username);
                    changed = true;
                }
    
            }
        }
        try {
            if (loginUserInfo != null) {
                LoginUserInfoHolder.setLoginUserInfo(loginUserInfo);
            }
            filterChain.doFilter(request, response);
        } finally {
            LoginUserInfoHolder.clearLoginUserInfo();
            //如果userInfo发生改变，则要写会session中
            if (changed) {
                saveUserInfoToSession(request, loginUserInfo);
            }
        }
        
    }
    
    private LoginUserInfo readUserInfoFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            log.debug("UserInfoFilter---No HttpSession Currently exists)");
            return null;
        }
    
        Object userinfoFromSession = session.getAttribute(USER_INFO_ATTR);
        if (userinfoFromSession == null) {
            log.debug("UserInfoFilter--- No UserInfo for Current Session");
            return null;
        }
    
        if (!(userinfoFromSession instanceof LoginUserInfo)) {
            log.warn("UserInfoFilter--- did not contain a Valid UserInfo but contained{}", userinfoFromSession);
            return null;
        }
        return (LoginUserInfo)userinfoFromSession;
    }
    
    private void saveUserInfoToSession(HttpServletRequest request, LoginUserInfo loginUserInfo) {
        //注意，这里如果session不存在，需要创建session
        //因为必须要保存userInfo
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_INFO_ATTR, loginUserInfo);
    }
}
