package daily.boot.gulimall.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
/**
 * 默认sessionStragy是CompositeSessionAuthenticationStragtegy
 * Delegating to org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Resource(name = "userAuthService")
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PersistentTokenRepository persistentTokenRepository;
    
    @Autowired
    private AbstractRememberMeServices rememberMeServices;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/login/**");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    
        http.authorizeRequests()
            .antMatchers("/reg.html", "/register","/sms/**", "/home.html", "favicon.ico").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login.html")
            .loginProcessingUrl("/doLogin")
            .defaultSuccessUrl("/user.html")
            .failureUrl("/login.html")
            .permitAll()
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/home.html")
            .deleteCookies()
            .clearAuthentication(true)
            .invalidateHttpSession(true)
            .permitAll()
            .and()
            .rememberMe()
            .tokenValiditySeconds(2419200)
            .rememberMeCookieDomain("gulimall.com")
            .key("123Gulimall*") //如果不设定，应用每次重启，都会使用一个随机的UUID字符串，这会使应用每次重启后rememberMe失效
            //.tokenRepository(persistentTokenRepository)
            .rememberMeServices(rememberMeServices)
            .and()
            .csrf().disable();

    }
    
    
}
