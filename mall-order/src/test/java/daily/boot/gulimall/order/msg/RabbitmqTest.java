package daily.boot.gulimall.order.msg;

import daily.boot.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;


//@ExtendWith(SpringExtension.class) 不用引入，SpringBootTest已经引入
@SpringBootTest
@Slf4j
class RabbitmqTest {
    @Autowired
    AmqpAdmin amqpAdmin;
    @Autowired
    RabbitTemplate rabbitTemplate;
    
    private String exchangeName = "hello-java-exchange";
    private String queueName = "hello-java-queue";
    private String routeKey = "hello.java";
    
    /**
     * 1. 如何创建Exchange, Queue, Bingding
     * 1） 使用AmqpAdmin创建
     * 2. 如何收发消息
     */
    @Test
    public void createExchange() {
        DirectExchange exchange = new DirectExchange(exchangeName, true, false);
        amqpAdmin.declareExchange(exchange);
        log.info("后台代码创建exchange成功:{}", exchange);
    }
    
    @Test
    public void creatQueue() {
        /**
         * 排它： 队列只能被一个声明的连接使用，实际使用不应该排它，都能被连接
         */
        Queue queue = new Queue(queueName, true, false, false);
        amqpAdmin.declareQueue(queue);
        log.info("后台代码创建Queue{}成功", queue);
    }
    
    @Test
    public void createBinding() {
        /**
         * String destination 【目的地】
         * DestinationType destinationType 【目的类型-Queue Exchange】
         * String exchange 【交换机】
         * String routingKey 【路由键】
         * @Nullable Map<String, Object> arguments 【自定义参数】
         */
        Binding binding = new Binding(
                queueName,
                Binding.DestinationType.QUEUE,
                exchangeName,
                routeKey, null);
        amqpAdmin.declareBinding(binding);
        log.info("后台代码创建Binding[{}]成功", binding);
    }
    
    @Test
    public void sendMessageTest() {
        rabbitTemplate.convertAndSend(exchangeName, routeKey, "第一条消息测试");
        log.info("消息发送成功");
    }
    
    @Test
    public void sendEntityMessageTest() {
        //1. 发送对象，默认使用java的序列化机制，因此对象要实现Serializable接口
        //2. 也可以使用json序列化对象，配置Jackson2JsonMessageConverter
    
        OrderReturnReasonEntity entity = new OrderReturnReasonEntity();
        entity.setCreateTime(new Date());
        entity.setId(112L);
        entity.setName("退货对象");
        entity.setSort(1);
        entity.setStatus(2);
    
        rabbitTemplate.convertAndSend(exchangeName, routeKey, entity);
        log.info("消息发送成功{}", entity);
    }
    
    @Test
    public void sendMultipleEntityMessages() {
        for (int i = 0; i < 10; i++) {
            OrderReturnReasonEntity entity = new OrderReturnReasonEntity();
            entity.setCreateTime(new Date());
            entity.setId(Integer.toUnsignedLong(i));
            entity.setName("退货对象" + i);
    
            rabbitTemplate.convertAndSend(exchangeName, routeKey, entity);
        }
    }
}