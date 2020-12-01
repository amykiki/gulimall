package daily.boot.gulimall.member;

import daily.boot.gulimall.service.api.feign.OrderFeignService;
import daily.boot.unified.dispose.annotation.EnableGlobalDispose;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = {OrderFeignService.class})
@MapperScan({"daily.boot.gulimall.member.dao"})
@EnableGlobalDispose
public class MemberApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }
}
