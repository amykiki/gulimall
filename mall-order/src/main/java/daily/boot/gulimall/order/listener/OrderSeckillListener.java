package daily.boot.gulimall.order.listener;

import com.rabbitmq.client.Channel;
import daily.boot.gulimall.order.service.OrderService;
import daily.boot.gulimall.service.api.to.mq.SeckillOrderTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RabbitListener(queues = "order.seckill.order.queue")
public class OrderSeckillListener {
    @Autowired
    private OrderService orderService;
    
    @RabbitHandler
    public void listener(SeckillOrderTo orderTo, Channel channel, Message message) throws IOException {
        log.info("====准备秒杀订单的详细信息===");
        try {
            orderService.createSeckillOrder(orderTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("创建秒杀订单异常", e);
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
