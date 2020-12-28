package daily.boot.gulimall.search;

import daily.boot.gulimall.search.elasticsearch.config.ElasticSearchConfig;
import daily.boot.gulimall.search.security.SSOIsLoginFilter;
import daily.boot.gulimall.service.api.feign.SSOFeignService;
import daily.boot.unified.dispose.annotation.EnableGlobalDispose;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients(clients = {SSOFeignService.class})
@EnableGlobalDispose
public class SearchAplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SearchAplication.class);
    }
}
