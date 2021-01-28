package daily.boot.gulimall.seckill.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableOAuth2Sso
public class SSOSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${gulimall.sso.request-matchers}")
    private String requestMatchers;
    @Value("${gulimall.sso.auth-request-matchers}")
    private String authRequestMatchers;
    @Value("${gulimall.sso.logout-url}")
    private String logoutUrl;
    @Value("${gulimall.sso.auth-server}")
    private String authServer;
    @Value("${server.servlet.session.cookie.name}")
    private String cookieName;
    @Autowired
    private SSOIsLoginFilter ssoIsLoginFilter;
    @Autowired
    private UserInfoFilter userInfoFilter;
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favicon.ico",
                                   "/error",
                                   "/doc.html/**",
                                   "/v2/**",
                                   "/webjars/**",
                                   "/swagger-resources/**",
                                   "/api/**",
                                   "/callback/**");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
            .antMatchers(requestMatchers)
            .and()
            .authorizeRequests().antMatchers(authRequestMatchers).authenticated()
            .anyRequest().permitAll()
            .and()
            .logout()
            .logoutUrl(logoutUrl)
            .deleteCookies(cookieName)
            .logoutSuccessUrl(authServer + "/logout")
            .permitAll()
            .and()
            .csrf().disable();
    
        http.addFilterBefore(ssoIsLoginFilter, FilterSecurityInterceptor.class);
        http.addFilterAfter(userInfoFilter, FilterSecurityInterceptor.class);
    }
}
