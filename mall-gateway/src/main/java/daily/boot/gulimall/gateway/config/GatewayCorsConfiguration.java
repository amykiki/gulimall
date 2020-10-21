package daily.boot.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class GatewayCorsConfiguration{
    
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*");//配置允许访问的跨域资源的请求域名
        corsConfig.addAllowedHeader("*");//配置允许请求header的访问，如 ：X-TOKEN
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));//配置允许访问该跨域资源服务器的请求方法，如：POST、GET、PUT、DELETE等
        corsConfig.setAllowCredentials(true);//跨域请求默认不包含cookie，设置为true可以包含cookie
        corsConfig.setMaxAge(3600L);
        
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsWebFilter(source);
    }
  
}
