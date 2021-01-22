package daily.boot.gulimall.order;

import daily.boot.gulimall.service.api.feign.*;
import daily.boot.unified.dispose.annotation.EnableGlobalDispose;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 使用RabbitMq
 * 1. 引入amqp场景，引入了RabbitAutoConfiguration
 * 2. 给容器自动配置了 RabbitTemplate, AmqpAdmin, CachingConnectionFactory, RabbitMessagingTemplate
 *    所有属性都是 spring.rabbitmq开头
 *    参考 RabbitProperties ，配置如下
 *    rabbitmq:
 *     host: docker-serv
 *     port: 5672
 *     username: root
 *     password: 123123
 *     virtual-host: /
 * 3. @EnableRabbit
 * 4. 监听消息，使用 @RabbitListener，必须先开启@EnableRabbit
 * //@RabbitListener: 类 + 方法
 * //@RabbitHandler: 标在方法上，重载
 *
 *
 * =+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
 * Seata分布式事务
 * 1. 每个微服务必须先创建undo_log
 * 2. 安装事务协调器，seata-server
 * 3. 整合
 *    ① 导入依赖 spring-cloud-starter-alibaba-seata
 *         <!--分布式事务-->
 *         <dependency>
 *             <groupId>io.seata</groupId>
 *             <artifactId>seata-spring-boot-starter</artifactId>
 *             <version>1.4.0</version>
 *         </dependency>
 *         <dependency>
 *             <groupId>com.alibaba.cloud</groupId>
 *             <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
 *             <exclusions>
 *                 <exclusion>
 *                     <groupId>io.seata</groupId>
 *                     <artifactId>seata-spring-boot-starter</artifactId>
 *                 </exclusion>
 *             </exclusions>
 *         </dependency>
 *     ② 解压并启动 seata-server
 *        registry.conf 注册中心配置
 *        file.conf 配置文件
 *     ③ 所有想要用分布式事务的微服务都要失业seata DataSourceProxy代理数据源
 *     ④ 每个微服务都必须导入 registry.conf, file.conf
 *     ⑤ 启动测试分布式事务
 *     ⑥ 给分布式大事务入口标注 @GlobalTransactional
 *     ⑦ 给每一个远程小事务用 @Transactional
 *
 *  支付宝电脑网站支付文档 https://opendocs.alipay.com/open/270/105902
 *
 */
@SpringBootApplication
@EnableRabbit
@EnableRedisHttpSession
@EnableGlobalDispose
@EnableFeignClients(clients = {ProductFeignService.class, MemberFeignService.class, SSOFeignService.class, CartFeignService.class, WareFeignService.class})
@EnableDiscoveryClient
@MapperScan({"daily.boot.gulimall.order.dao"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
