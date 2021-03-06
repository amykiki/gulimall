package daily.boot.gulimall.cart.configuration;

import daily.boot.gulimall.cart.security.CartUserKeyFilter;
import daily.boot.gulimall.cart.security.SSOIsLoginFilter;
import daily.boot.gulimall.cart.security.UserInfoFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebConfiguration implements WebMvcConfigurer {
    
    @Bean
    public FilterRegistrationBean disableAutoRegisterSSOIsLoginFilter(SSOIsLoginFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
    
    @Bean
    public FilterRegistrationBean disableAutoRegisterUserInfoFilter(UserInfoFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
    
    @Bean
    public FilterRegistrationBean disableAutoRegisterCartUserKeyFilter(CartUserKeyFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
}
