package com.roydon.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ErrorExchangeConfig
 *
 * @AUTHOR: roydon
 * @DATE: 2023/9/30
 **/
@Configuration
@ConditionalOnProperty(name = "spring.rabbitmq.listener.simple.retry.enabled", havingValue = "true")
public class ErrorExchangeConfig {

    public static final String ERROR_EXCHANGE_NAME = "error.direct";
    public static final String ERROR_QUEUE_1_NAME = "error.queue1";
    public static final String ERROR_QUEUE_1_ROUTING_KEY = "error";

    @Bean
    public DirectExchange errorExchange() {
        return new DirectExchange(ERROR_EXCHANGE_NAME);
    }

    @Bean
    public Queue errorQueue1() {
        return new Queue(ERROR_QUEUE_1_NAME);
    }

    @Bean
    public Binding errorBinding(DirectExchange errorExchange, Queue errorQueue1) {
        return BindingBuilder.bind(errorQueue1).to(errorExchange).with(ERROR_QUEUE_1_ROUTING_KEY);
    }

    @Bean
    public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, ERROR_EXCHANGE_NAME, ERROR_QUEUE_1_ROUTING_KEY);
    }

}
