package daily.boot.gulimall.authserver.config;

import daily.boot.gulimall.authserver.security.OAuth2SessionRelationService;
import daily.boot.gulimall.authserver.security.filters.AuthorizeCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Configuration
public class AuthWebConfig implements WebMvcConfigurer {
    
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private OAuth2SessionRelationService oAuth2SessionRelationService;
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/reg.html").setViewName("reg");
    }
    
    @Bean
    public Filter authorizeCodeFilter() {
        return new AuthorizeCodeFilter();
    }
    
    //@Bean
    //public Filter accessTokenFilter() {
    //    return new AccessTokenFilter(oAuth2SessionRelationService);
    //}
    
    
    //@Bean
    //public FilterRegistrationBean authorizeCodeFilterRegistration(Filter authorizeCodeFilter) {
    //    FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
    //    registration.setFilter(authorizeCodeFilter);
    //    registration.addUrlPatterns("/oauth/authorize");
    //    //registration.setUrlPatterns(Collections.singletonList("/oauth/authorize"));
    //    //registration.setOrder(1);
    //    return registration;
    //}
    
    @Bean
    public FilterRegistrationBean authorizeCodeFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("authorizeCodeFilter"));
        registration.addUrlPatterns("/oauth/authorize");
        registration.setName("authorizeCodeFilter");
        //registration.setUrlPatterns(Collections.singletonList("/oauth/authorize"));
        registration.setOrder(1);
        return registration;
    }
    
    @Bean
    public FilterRegistrationBean accessTokenFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("accessTokenFilter"));
        registration.addUrlPatterns("/oauth/token");
        registration.setName("accessTokenFilter");
        //registration.setUrlPatterns(Collections.singletonList("/oauth/authorize"));
        registration.setOrder(1);
        return registration;
    }
}
