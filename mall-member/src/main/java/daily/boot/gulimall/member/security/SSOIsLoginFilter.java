package daily.boot.gulimall.member.security;

import daily.boot.common.Result;
import daily.boot.gulimall.service.api.feign.SSOFeignService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.logout.CompositeLogoutHandler;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
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
public class SSOIsLoginFilter extends OncePerRequestFilter implements ApplicationContextAware {
    @Autowired
    private SSOFeignService ssoFeignService;
    @Value("${server.servlet.session.cookie.name}")
    private String cookieName;
    @Value("${security.oauth2.client.client-id}")
    private String clientId;
    private OAuth2ClientContext oAuth2ClientContext;
    private ApplicationContext applicationContext;
    private LogoutHandler logoutHandler;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Override
    protected void initFilterBean() throws ServletException {
        OAuth2RestTemplate userInfoRestTemplate = applicationContext.getBean(UserInfoRestTemplateFactory.class).getUserInfoRestTemplate();
        this.oAuth2ClientContext = userInfoRestTemplate.getOAuth2ClientContext();
        createLogoutHandler();
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) ) {
            //如果session中有登录用户信息，需要调用authServer服务来验证用户是否还是登录状态
            String username = (String) authentication.getPrincipal();
            OAuth2AccessToken accessToken = oAuth2ClientContext.getAccessToken();
            if (accessToken == null || accessToken.getValue() == null) {
                //accessToken为空，直接登录
                doLogout(request, response, authentication);
            } else {
                //校验用户是否在authServer中还是登录状态
                String tokenValue = accessToken.getValue();
                Result<Boolean> rtn = ssoFeignService.isLogin(username, clientId, tokenValue);
                //调用不成功或者返回为false，都表示用户未登录，需退出
                if (!rtn.isOk() || !rtn.getData()) {
                    doLogout(request, response, authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
    
    private void createLogoutHandler() {
        SecurityContextLogoutHandler contextLogoutHandler = new SecurityContextLogoutHandler();
        contextLogoutHandler.setClearAuthentication(true);
        contextLogoutHandler.setInvalidateHttpSession(true);
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(cookieName);
        this.logoutHandler = new CompositeLogoutHandler(contextLogoutHandler, cookieClearingLogoutHandler);
    }
    
    private void doLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        this.logoutHandler.logout(request, response, authentication);
    }
}
