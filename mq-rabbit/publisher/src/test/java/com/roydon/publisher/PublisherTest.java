package com.roydon.publisher;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * PubrisherTest
 *
 * @AUTHOR: roydon
 * @DATE: 2023/9/27
 **/
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

}
