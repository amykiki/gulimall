package daily.boot.gulimall.order.service.impl;

import com.rabbitmq.client.Channel;
import daily.boot.gulimall.order.entity.OrderEntity;
import daily.boot.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RabbitListener(queues = {"hello-java-queue"})
@Service
@Slf4j
public class MsgImpl {
    
    /**
     * 消息实际类型是 org.springframework.amqp.core.Message
     * 参数可以写以下类型
     * 1. org.springframework.amqp.core.Message 原生消息，消息头 + 体
     * 2. <发送的消息类型>，如OrderReturnReasonEntity
     * 3. Channel channel, 当前传输数据通道
     *
     * Queue 可以很多消费者来监听，只要收到消息，队列删除消息，只能有一个消费者能收到消息
     * 只有一个消息完全处理完，方法运行结束，才可以接收下一个消息
     */
    @RabbitHandler
    public void receiveMsg(Message message, OrderReturnReasonEntity content, Channel channel) {
        //消息体
        byte[] body = message.getBody();
        //消息头属性信息
        MessageProperties messageProperties = message.getMessageProperties();
        //log.info("接收到消息{}--{}", content.getId(), message);
        //log.info("消息头{}", messageProperties.toString());
        log.info("接收到OrderReturnReasonEntity消息---{}", content);
        //log.info("Channel{}", channel);
        //try {
        //    Thread.sleep(3000L);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        //log.info("=======消息处理完成======={}", content.getId());
        
        //delevertyTag通道内按顺序自增
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //手动签收，非批量模式
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            //网络中断断了
            e.printStackTrace();
        }
    }
    
    @RabbitHandler
    public void receiveMsg(OrderEntity orderEntity) {
        log.info("接收到Order消息---{}", orderEntity);
    }
}
