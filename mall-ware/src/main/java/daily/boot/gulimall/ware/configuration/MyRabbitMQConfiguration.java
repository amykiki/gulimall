package daily.boot.gulimall.ware.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class MyRabbitMQConfiguration {
    @Value("${gulimall.ware.mq.stock-event-exchange}")
    private String stockEventExchange;
    @Value("${gulimall.ware.mq.stock-delay-queue}")
    private String stockDelayQueue;
    @Value("${gulimall.ware.mq.stock-release-stock-queue}")
    private String stockReleaseStockQueue;
    @Value("${gulimall.ware.mq.stock-locked-routing-key}")
    private String stockLockedRoutingKey;
    @Value("${gulimall.ware.mq.stock-release-stock-routing-key}")
    private String stockReleaseStockRoutingKey;
    
    @Bean
    public Exchange stockEventExchange() {
        return new TopicExchange(stockEventExchange, true, false);
    }
    
    /**
     * 普通队列
     * @return
     */
    @Bean
    public Queue stockReleaseStockQueue() {
        return new Queue(stockReleaseStockQueue, true, false, false);
    }
    
    /**
     * 延迟队列
     */
    @Bean
    public Queue stockDelayQueue() {
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", stockEventExchange);
        arguments.put("x-dead-letter-routing-key", stockReleaseStockRoutingKey);
        //消息过期时间，2分钟比order自身过期时间长
        arguments.put("x-message-ttl", 120000);
        return new Queue(stockDelayQueue, true, false, false, arguments);
    }
    
    /**
     * 交换机与普通队列绑定，解锁库存
     */
    @Bean
    public Binding stockReleaseBinding() {
        return BindingBuilder.bind(stockReleaseStockQueue())
                             .to(stockEventExchange())
                             .with(stockReleaseStockRoutingKey).noargs();
    }
    
    /**
     * 交换机与延迟队列绑定
     * @return
     */
    @Bean
    public Binding stockLockedBinding() {
        return BindingBuilder.bind(stockDelayQueue())
                             .to(stockEventExchange())
                             .with(stockLockedRoutingKey).noargs();
    }
    
}
