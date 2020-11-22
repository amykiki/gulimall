package daily.boot.gulimall.thirdparty.configuration;

import daily.boot.gulimall.common.httpclient.HttpClientPool;
import daily.boot.gulimall.common.httpclient.HttpClientPoolConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HttpClientPoolConfig.class)
public class HttpClientPoolConfiguration {
    
    @Bean(name = "httpClientPool", destroyMethod = "close")
    public HttpClientPool httpClientPool(HttpClientPoolConfig config) {
        return new HttpClientPool(config);
    }
}
