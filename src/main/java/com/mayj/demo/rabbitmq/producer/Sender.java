package com.mayj.demo.rabbitmq.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Sender
 * @Description TODO
 * @Author Mayj
 * @Date 2022/5/17 20:04
 **/
@Slf4j
@Component
public class Sender {

    //自动注入发送消息的模板
    @Autowired
    private RabbitTemplate rabbitTemplate;

//     /**
//       * 回调函数:confirm确认,实现相关方法
//       * @author Czw
//       * @date 2019/6/15 0015 下午 8:51
//       * @param correlationData 消息信息
//       * @param ack 消息是否返回成功
//       * @param ca
//       */
//    public final ConfirmCallback confirmCallback = (correlationData, ack, ca) -> {
//        log.debug("RabbitMQ回调函数，CorrelationData：{}", correlationData);
//        //获取消息ID
//        String messageId = correlationData == null ? "" : correlationData.getId();
//        if (ack) {
//            //confirm返回成功 则打印日志
//            log.debug("RabbitMQ回调函数返回成功 messageId为：{}", messageId);
//        } else {
//            log.error("RabbitMQ回调函数返回失败");
//        }
//    };

    public void test() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.96.66.74");
        factory.setPort(5672);
        factory.setPassword("mayj");
        factory.setUsername("mayj");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
    }

    public void send(List<String> list){
        log.info("***********************RabbitMQ开始发送消息*************************");
//        rabbitTemplate.setConfirmCallback(confirmCallback);
        CorrelationData correlationData=new CorrelationData();//correlationData消息唯一id
        correlationData.setId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("test-exchange",//exchange
                "test.mayj",//routingKey 路由key 在rabbitmq中交换机Rount Key对应的值
                list, //消息体内容
                correlationData);//correlationData消息唯一id
        log.info("***********************RabbitMQ发送消息完成*************************");
    }
}
