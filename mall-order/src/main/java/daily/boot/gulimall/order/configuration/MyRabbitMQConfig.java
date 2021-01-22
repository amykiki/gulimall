package daily.boot.gulimall.order.configuration;

import lombok.Setter;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 测试环境默认访问地址http://docker-serv:15672/
 */
@Configuration
@ConfigurationProperties(prefix = "gulimall.order.mq")
@Setter
public class MyRabbitMQConfig {
    //@Value("${gulimall.order.mq.order-event-exchange}")
    private String orderEventExchange;
    //@Value("${gulimall.order.mq.order-delay-queue}")
    private String orderDelayQueue;
    //@Value("${gulimall.order.mq.order-release-order-queue}")
    private String orderReleaseOrderQueue;
    //@Value("${gulimall.order.mq.order-create-order-routing-key}")
    private String orderCreateOrderRoutingKey;
    //@Value("${gulimall.order.mq.order-release-order-routing-key}")
    private String orderReleaseOrderRoutingKey;
    //@Value("${gulimall.order.mq.stock-release-stock-queue}")
    private String stockReleaseStockQueue;
    //@Value("${gulimall.order.mq.stock-release-stock-routing-key}")
    private String stockReleaseStockRoutingKey;
    private Integer delayQueueTtl;
    
    /*
     * 容器中的Queue，Exchange，Binging会自动创建(在RabbitMQ)不存在的情况下
     */
    
    /**
     * 死信队列
     */
    @Bean
    public Queue orderDelayQueue() {
        /*
            Queue(String name,  队列名字
            boolean durable,  是否持久化
            boolean exclusive,  是否排他
            boolean autoDelete, 是否自动删除
            Map<String, Object> arguments) 属性
         */
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", orderEventExchange);
        arguments.put("x-dead-letter-routing-key", orderReleaseOrderRoutingKey);
        arguments.put("x-message-ttl", delayQueueTtl); //消息过期时间
        return new Queue(orderDelayQueue, true, false, false, arguments);
    }
    
    /**
     * 普通队列
     */
    @Bean
    public Queue orderReleaseQueue() {
        return new Queue(orderReleaseOrderQueue, true, false, false);
    }
    
    /**
     * TopicExchange
     */
    @Bean
    public Exchange orderEventExchange() {
        /*
         *   String name,
         *   boolean durable,
         *   boolean autoDelete,
         *   Map<String, Object> arguments
         * */
        return new TopicExchange(orderEventExchange, true, false);
    }
    
    /**
     * 新建订单消息队列，订单消息放进延迟队
     * @return
     */
    @Bean
    public Binding orderCreateBinding() {
        /*
         * String destination, 目的地（队列名或者交换机名字）
         * DestinationType destinationType, 目的地类型（Queue、Exhcange）
         * String exchange,
         * String routingKey,
         * Map<String, Object> arguments
         * */
        return BindingBuilder.bind(orderDelayQueue())
                             .to(orderEventExchange())
                             .with(orderCreateOrderRoutingKey).noargs();
    }
    
    /**
     * 取消订单消息队列，从该队列中取出超时的订单并删除
     */
    @Bean
    public Binding orderReleaseBinding() {
        return BindingBuilder.bind(orderReleaseQueue())
                      .to(orderEventExchange())
                      .with(orderReleaseOrderRoutingKey)
                      .noargs();
    }
    
    /**
     * 订单释放直接和库存释放进行绑定
     */
    @Bean
    public Binding orderReleaseOtherBinding() {
        return new Binding(stockReleaseStockQueue,
                           Binding.DestinationType.QUEUE,
                           orderEventExchange,
                           stockReleaseStockRoutingKey,
                           null);
    }
}
