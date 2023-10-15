package com.roydon.publisher.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * MqConfirmConfig
 *
 * @AUTHOR: roydon
 * @DATE: 2023/9/30
 * 消息确认机制配置类
 **/
@Slf4j
@Configuration
public class MqConfirmConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        // 消息回调
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                log.error("触发return callback,");
                log.debug("exchange: {}", returnedMessage.getExchange());
                log.debug("routingKey: {}", returnedMessage.getRoutingKey());
                log.debug("message: {}", returnedMessage.getMessage());
                log.debug("replyCode: {}", returnedMessage.getReplyCode());
                log.debug("replyText: {}", returnedMessage.getReplyText());
            }
        });
    }
}
