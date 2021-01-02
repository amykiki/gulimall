package daily.boot.gulimall.order.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class MyRabbitConfiguration {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 消息序列化机制
     */
    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        //指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //将类名称序列化到json串中
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        //设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new Jackson2JsonMessageConverter(objectMapper);
    }
    
    /**
     * 定制RabbitTemplate
     * 1. 服务收到消息就会回调
     *    ① spring.rabbitmq.publisher-confirm-type: correlated
     *    ② 设置确认回调
     *
     * 2. 消息正确抵达队列就会进行回调
     *    ① spring.rabbitmq.publisher-returns: true
     *       spring.rabbitmq.template.mandatory: true
     *    ② 设置确认回调ReturnCallback
     *
     * 3. 消费端确认(保证每个消息都被正确消费，此时才可以broker删除这个消息)
     *    ① 默认是自动确认，只有消息接收到，客户端会自动确认，服务端就会移除这个消息
     *       问题： 收到很多消息，自动回复服务器ack，只有一个消息处理成功，客户端宕机了，发生消息丢失
     *    ② 需要手动确认
     *       只要没有明确告诉MQ服务器，消息被确认，没有ACK，消息就一直是unacked状态
     *       即使consumer宕机，消息不会丢失，会重新变为Ready。如果有新的Consumer连接，就会读取到消息
     *    ③ 如何手动确认
     *      配置 spring.rabbitmq.listener.simple.acknowledge-mode: manual # 手动确认
     *
     *       channle.basicAck(deliveryTag, false) //签收获取信息
     *       channel.basicNack(deliverTag, true) //拒签
     *       - basic.ack    用于肯定确认，broker将移除此消息
     *       - basic.nack   否定确认，可以指定broker是否丢弃此消息，可以批量
     *       - basic.reject 否定确认，同上，但不能批量
     *
     *       ● 默认自动 ack，消息被消费者收到，就会从broker的queue中移除
     *       ● queue无消费者，消息依然会被存储，直到消费者消费
     *       ● 消费者收到消息，默认会自动ack，但是如果无法确认次消息是否被处理完成，或者成功处理，
     *         可以开启手动ack
     *           - 消息处理成功，ack() 接受下一个消息，此消息broker会移除
     *           - 消息处理失败，nack()/reject()，重新发送给其他人进行处理，或者容错处理后ack
     *           - 消息一直没有调用 ack/nack方法，broker认为此消息正在被处理，不会投递给别人，
     *              此时客户端断开，消息不会被broker移除，会投递给他人
     *
     */
    @PostConstruct //MyRabbitConfiguration对象创建完成后，执行这个方法
    public void initRabbitTemplate() {
        /**
         * 1. 只有消息抵达broker就 ack=true
         * correlationData: 当前消息唯一关联数据(即消息唯一id)
         * ack: 消息是否成功收到
         * cause: 失败原因
         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("confirm...corrlationData[{}]==>ack:[{}]==>cause:[{}]", correlationData, ack, cause);
        });
    
        /**
         * 只有消息没有投递给指定的队列，就触发这个失败回调
         * message: 投递失败消息详细信息
         * replyCode: 回复的状态码
         * replyText: 回复的文本内容
         * exchange: 这个消息发给哪个交换机
         * routingKey: 这个消息使用哪个路由键
         */
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.warn("Fail Message[{}]==>replyCode[{}]==>replyText[{}]==>exchange[{}]==>routingKey[{}]",
                         message, replyCode, replyText, exchange, routingKey);
            }
        });
    }
}
