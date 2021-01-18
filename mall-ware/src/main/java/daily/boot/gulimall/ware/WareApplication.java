package daily.boot.gulimall.ware;

import daily.boot.gulimall.service.api.feign.MemberFeignService;
import daily.boot.gulimall.service.api.feign.OrderFeignService;
import daily.boot.gulimall.service.api.feign.ProductFeignService;
import daily.boot.gulimall.service.api.to.MemberAddressTo;
import daily.boot.unified.dispose.annotation.EnableGlobalDispose;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableRabbit
@EnableDiscoveryClient
@EnableGlobalDispose
@EnableFeignClients(clients = {ProductFeignService.class, MemberFeignService.class, OrderFeignService.class})
@MapperScan("daily.boot.gulimall.ware.dao")
public class WareApplication {
    public static void main(String[] args) {
        SpringApplication.run(WareApplication.class, args);
    }
}
