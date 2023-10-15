package com.roydon.publisher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

/**
 * PubrisherTest
 *
 * @AUTHOR: roydon
 * @DATE: 2023/9/27
 **/
@Slf4j
@SpringBootTest
public class PublisherTest {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    void testSimpleQueue() {
        String queue = "simple.queue";
        rabbitTemplate.convertAndSend(queue, "hello,mq!");
    }

    /**
     * 一个队列被多个消费者监听，消息消费配置能者优先，但可能造成消息堆积甚至死信
     */
    @Test
    void testWorkQueue() {
        String queue = "work.queue";
        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend(queue, "work msg - " + i);
        }
    }

    /**
     * 广播
     * 声明交换机绑定多个队列，队列的消费者都能消费消息
     *
     * @throws InterruptedException
     */
    @Test
    void testFanoutQueue() throws InterruptedException {
        String exchange = "amq.fanout";
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend(exchange, "", "fanout msg - " + i);
            Thread.sleep(1000);
        }
    }

    /**
     * 可路由的direct交换机，路由到不同队列，一个队列可以绑定多个key
     */
    @Test
    void testDirectQueue() {
//        String exchange = "amq.direct";
//        String exchange = "study.direct";
        String exchange = "zhujie.direct";
//        String routingKey = "red";
//        String routingKey = "blue";
        String routingKey = "yellow";
        rabbitTemplate.convertAndSend(exchange, routingKey, "direct msg - " + routingKey);
    }

    /**
     * topic通配符路由消息
     */
    @Test
    void testTopicQueue() {
        String exchange = "amq.topic";
//        String routingKey = "china.news";
//        String routingKey = "usa.news";
        String routingKey = "china.weather";
        rabbitTemplate.convertAndSend(exchange, routingKey, "topic msg - " + routingKey);
    }

    /**
     * 发送对象测试序列化结果：乱码
     * rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAACdAAEbmFtZXQA
     * BnJveWRvbnQAA2FnZXNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAU
     * eA==
     * <p>
     * headers:
     * content_type:	application/x-java-serialized-object
     * <p>
     * 发现默认是jdk序列化，不推荐,有安全漏洞
     */
    @Test
    void testObjectQueue() {
        String queue = "object.queue";
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "roydon");
        map.put("age", 20);
        rabbitTemplate.convertAndSend(queue, map);
    }

    @Test
    void testPublisherConfirm() {
        // 1.创建CorrelationData
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        // 2.给Future添加ConfirmCallback
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                // 2.1.Future发生异常时的处理逻辑，基本不会触发
                log.error("send message fail", ex);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                // 2.2.Future接收到回执的处理逻辑，参数中的result就是回执内容
                if (result.isAck()) { // result.isAck()，boolean类型，true代表ack回执，false 代表 nack回执
                    log.debug("发送消息成功，收到 ack!");
                } else { // result.getReason()，String类型，返回nack时的异常描述
                    log.error("发送消息失败，收到 nack, reason : {}", result.getReason());
                }
            }
        });
        // 3.发送消息
        rabbitTemplate.convertAndSend("zhujie.direct11", "blue11", "hello", cd);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPageOut() {
        Message msg = MessageBuilder.withBody("hello".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        for (int i = 0; i < 200000; i++) {
            rabbitTemplate.convertAndSend("simple.queue", msg);
        }
    }

    /**
     * 基于消息过期的死信队列
     */
    @Test
    void testDlxQueue() {
        String exchange = "expire.direct";
        rabbitTemplate.convertAndSend(exchange, "dlx", "hello,expire message!", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
        log.info("带有过期时间的消息发送成功");
    }

    /**
     * 发送延时消息
     */
    @Test
    void testPublisherDelayMessage() {
        // 1.创建消息
        String message = "hello, delayed message";
        // 2.发送消息，利用消息后置处理器添加消息头
        rabbitTemplate.convertAndSend("delay.direct", "delay", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 添加延迟消息属性
                message.getMessageProperties().setDelay(5000);
                return message;
            }
        });
    }

}
