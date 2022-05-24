package com.mayj.demo.rabbitmq.consumer;

import com.alibaba.druid.util.StringUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName Recevier
 * @Description TODO
 * @Author Mayj
 * @Date 2022/5/17 20:04
 **/
@Slf4j
@Component
public class Receiver {



    /*
    @RabbitListener 自动在RabbitMQ上面添加交换机、队列，以及设置相关属性；
    @Queue和@Exchange的durable属性都是设置队列为持久化。确保退出或者崩溃
    的时候，将会丢失所有队列和消息（交换机没设置是因为该属性默认true）；
     */

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "test-queue", durable = "true"),
            exchange = @Exchange(name = "test-exchange", type = "topic"),//交换机名称，durable指是否持久化到数据库，type：模式
            key = "test.*" //路由匹配规则
    ))
    @RabbitHandler//@RabbitHandler 只是标识方法如果有消息过来消费者要消费的时候调用这个方法
    public void onOrderMessage(@Payload List<String> list,
                               @Headers Map<String, Object> headrs,
                               Channel channe1) throws Exception{
        if (StringUtils.isEmpty(list.get(0))){
            //失败不签收ACK，等待人员后端处理
                Long deliverTag = (Long) headrs.get(AmqpHeaders.DELIVERY_TAG);//唯一标识ID
                channe1.basicAck(deliverTag, false);
            //throw new BusinessException(CommonExEnum.EXAM_NULL_DELETE);
        }
        list.stream().forEach(s -> System.out.println(s));
        //ACK 手动签收消息，告诉对方消息签收成功
        Long deliverTag = (Long) headrs.get(AmqpHeaders.DELIVERY_TAG);//唯一标识ID
        channe1.basicAck(deliverTag, false);
    }



}
