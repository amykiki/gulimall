package daily.boot.gulimall.product;

import daily.boot.gulimall.service.api.feign.CouponFeignService;
import daily.boot.gulimall.service.api.feign.SSOFeignService;
import daily.boot.gulimall.service.api.feign.SearchFeignService;
import daily.boot.gulimall.service.api.feign.WareFeignService;
import daily.boot.unified.dispose.annotation.EnableGlobalDispose;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"daily.boot.gulimall.product.dao"})
@EnableFeignClients(clients = {SSOFeignService.class, WareFeignService.class, SearchFeignService.class, CouponFeignService.class})
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableGlobalDispose
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
