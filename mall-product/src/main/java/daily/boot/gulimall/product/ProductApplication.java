package daily.boot.gulimall.product;

import daily.boot.unified.dispose.annotation.EnableGlobalDispose;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"daily.boot.gulimall.product.dao"})
@EnableFeignClients(basePackages = {"daily.boot.gulimall.service.api.feign"})
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableGlobalDispose
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
