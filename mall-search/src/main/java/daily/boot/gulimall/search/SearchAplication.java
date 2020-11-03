package daily.boot.gulimall.search;

import daily.boot.gulimall.search.elasticsearch.config.ElasticSearchConfig;
import daily.boot.unified.dispose.annotation.EnableGlobalDispose;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableGlobalDispose
public class SearchAplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SearchAplication.class);
    }
}
