package daily.boot.gulimall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

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
 * @RabbitListener: 类 + 方法
 * @RabbitHandler: 标在方法上，重载
 *
 */
@SpringBootApplication
@EnableRabbit
@EnableDiscoveryClient
@MapperScan({"daily.boot.gulimall.order.dao"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
