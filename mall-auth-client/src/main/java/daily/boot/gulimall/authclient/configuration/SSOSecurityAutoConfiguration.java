package daily.boot.gulimall.authclient.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableOAuth2Sso
public class SSOSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${gulimall.sso.request-matchers}")
    private String requestMatchers;
    @Value("${gulimall.sso.logout-url}")
    private String logoutUrl;
    @Value("${gulimall.sso.auth-server}")
    private String authServer;
    @Value("${server.servlet.session.cookie.name}")
    private String cookieName;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
            .antMatchers(requestMatchers)
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .logout()
            .logoutUrl(logoutUrl)
            .deleteCookies(cookieName)
            .logoutSuccessUrl(authServer + "/logout")
            .permitAll()
            .and()
            .csrf().disable();
        
    }
}
