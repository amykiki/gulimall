package daily.boot.gulimall.order.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.order.entity.OrderEntity;
import daily.boot.gulimall.order.entity.OrderReturnReasonEntity;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/rabbitmq")
@Api(tags = "RabbitMQ-消息队列测试")
@Slf4j
public class RabbitMqTestContrller {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private String exchangeName = "hello-java-exchange";
    private String routeKey = "hello.java";
    
    @GetMapping("/sendMultiple")
    public Result sendMultiple(@RequestParam(name= "num", defaultValue = "10") Integer num) {
        for (int i = 0; i < num; i++) {
            if (i % 2 == 0) {
                OrderReturnReasonEntity entity = new OrderReturnReasonEntity();
                entity.setCreateTime(new Date());
                entity.setId(Integer.toUnsignedLong(i));
                entity.setName("退货对象" + i);
                rabbitTemplate.convertAndSend(exchangeName, routeKey, entity, new CorrelationData(UUID.randomUUID().toString()));
            } else {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setId(Integer.toUnsignedLong(i));
                rabbitTemplate.convertAndSend(exchangeName, routeKey, orderEntity, new CorrelationData(UUID.randomUUID().toString()));
            }
        }
        return Result.ok(num + "条消息发送完毕");
    }
    
    @GetMapping("/sendErrorRouteKey")
    public Result sendErrorRouteKey(@RequestParam(name= "num", defaultValue = "10") Integer num) {
        for (int i = 0; i < num; i++) {
            OrderReturnReasonEntity entity = new OrderReturnReasonEntity();
            entity.setCreateTime(new Date());
            entity.setId(Integer.toUnsignedLong(i));
            entity.setName("退货对象" + i);
            rabbitTemplate.convertAndSend(exchangeName, routeKey + "2", entity, new CorrelationData(UUID.randomUUID().toString()));
        }
        return Result.ok(num + "条消息发送完毕");
    }
}
