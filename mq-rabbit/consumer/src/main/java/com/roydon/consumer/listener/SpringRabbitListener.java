package com.roydon.consumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * RabbitListener
 *
 * @AUTHOR: roydon
 * @DATE: 2023/9/27
 **/
@Slf4j
@Component
public class SpringRabbitListener {

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String msg) {
        log.debug(" [R] ==> {}", msg);
//        throw new RuntimeException("消费者监听消息失败！抛出异常");
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue1(String msg) throws InterruptedException {
        Thread.sleep(200);
        log.error(" [R] ==> {}", msg);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue2(String msg) throws InterruptedException {
        Thread.sleep(1000);
        log.debug(" [R] ==> {}", msg);
    }

    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String msg) {
        log.error(" [fanout1] ==> {}", msg);
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String msg) {
        log.debug(" [fanout2] ==> {}", msg);
    }

    @RabbitListener(queues = "direct.queue1")
    public void listenDirectQueue1(String msg) {
        log.error(" [direct1] ==> {}", msg);
    }

    @RabbitListener(queues = "direct.queue2")
    public void listenDirectQueue2(String msg) {
        log.debug(" [direct2] ==> {}", msg);
    }

    @RabbitListener(queues = "topic.queue1")
    public void listenTopicQueue1(String msg) {
        log.info(" [topic1] ==> {}", msg);
    }

    @RabbitListener(queues = "topic.queue2")
    public void listenTopicQueue2(String msg) {
        log.debug(" [topic2] ==> {}", msg);
    }

    /**
     * 使用注解绑定交换机和queue
     *
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue11", durable = "true"),
            exchange = @Exchange(name = "zhujie.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}))
    public void listenDirectQueueWithZhujie1(String msg) {
        log.info(" [Zhujie.direct1] ==> {}", msg);
    }

    /**
     * 使用注解绑定交换机和queue
     *
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue12", durable = "true"),
            exchange = @Exchange(name = "zhujie.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}))
    public void listenDirectQueueWithZhujie2(String msg) {
        log.debug(" [Zhujie.direct2] ==> {}", msg);
    }

    @RabbitListener(queues = "object.queue")
    public void listenObjectQueue(Map<String, Object> map) {
        log.debug(" [map] ==> {}", map);
    }

    /**
     * 死信队列监听器
     *
     * @param msg
     */
    @RabbitListener(queues = "dlx.queue")
    public void listenDlxQueue(String msg) {
        log.debug(" [dlx.queue] ==> {}", msg);
    }

    /**
     * 延时消息
     *
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "delay.queue", durable = "true"),
            exchange = @Exchange(name = "delay.direct", delayed = "true"),
            key = "delay"
    ))
    public void listenDelayMessage(String msg) {
        log.info("接收到delay.queue的延迟消息：{}", msg);
    }

}
