package daily.boot.gulimall.authserver.config;

import daily.boot.gulimall.service.api.configuration.DateFormatRegister;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    
    @Bean
    public DateFormatRegister dateFormatRegister() {
        return new DateFormatRegister();
    }
}
