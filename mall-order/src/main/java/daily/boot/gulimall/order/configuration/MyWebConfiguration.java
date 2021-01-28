package daily.boot.gulimall.order.configuration;

import daily.boot.gulimall.order.security.SSOIsLoginFilter;
import daily.boot.gulimall.order.security.UserInfoFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

@Configuration
public class MyWebConfiguration implements WebMvcConfigurer {
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;
    
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
    
    /**
     * 注册String到Date解析器，用于Controller自动解析
     * 主要是阿里支付宝回调会传入date字符串
     * 注意，这里注释了，因为在UnderlineToCamelArgumentResolver中使用了 BeanWrapper来反射注入属性，注册的converter没用
     * 参考 Spring官网阅读系列（十一）：Spring中的BeanWrapper及类型转换
     * 在UnderlineToCamelArgumentResolver中直接使用了beanWrapper来注入parameter，这里注册的类型转换没用
     * https://cloud.tencent.com/developer/article/1606433
     */
    //@PostConstruct
    //public void initEditableValidate() {
    //    ConfigurableWebBindingInitializer webBindingInitializer = (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
    //    assert webBindingInitializer != null;
    //    if (webBindingInitializer.getConversionService() != null) {
    //        GenericConversionService conversionService = (GenericConversionService) webBindingInitializer.getConversionService();
    //        conversionService.addConverter(String.class, Date.class, new String2DateConverter());
    //    }
    //}
    /**
     * 添加参数解析，将参数的形式从下划线转化为驼峰
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UnderlineToCamelArgumentResolver());
        
    }
}
