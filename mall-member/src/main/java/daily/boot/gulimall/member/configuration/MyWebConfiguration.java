package daily.boot.gulimall.member.configuration;

import daily.boot.gulimall.member.security.SSOIsLoginFilter;
import daily.boot.gulimall.member.security.UserInfoFilter;
import daily.boot.gulimall.service.api.converter.String2DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * 参考https://www.cnblogs.com/smiler/p/9292693.html
 */
@Configuration
public class MyWebConfiguration implements WebMvcConfigurer {
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @PostConstruct
    public void initEditableValidate() {
        ConfigurableWebBindingInitializer webBindingInitializer = (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
        assert webBindingInitializer != null;
        if (webBindingInitializer.getConversionService() != null) {
            GenericConversionService conversionService = (GenericConversionService) webBindingInitializer.getConversionService();
            conversionService.addConverter(String.class, Date.class, new String2DateConverter());
        }
    }
    
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
}
