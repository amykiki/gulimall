package daily.boot.gulimall.authserver;

import daily.boot.gulimall.service.api.feign.MemberFeignService;
import daily.boot.gulimall.service.api.feign.ThirdPartyFeignService;
import daily.boot.unified.dispose.annotation.EnableGlobalDispose;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = {ThirdPartyFeignService.class, MemberFeignService.class})
@EnableGlobalDispose
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class);
    }
    
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localResolver=new SessionLocaleResolver();
        localResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return localResolver;
    }
    
    @Bean
    //国际化信息，参考
    //https://blog.codecentric.de/en/2017/08/localization-spring-security-error-messages-spring-boot/
    //https://stackoverflow.com/questions/46659679/spring-boot-application-and-messagesource
    //https://www.jianshu.com/p/46a4355ad0a3
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:org/springframework/security/messages");
        return messageSource;
    }
}
