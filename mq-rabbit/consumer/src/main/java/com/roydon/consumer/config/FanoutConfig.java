package com.roydon.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FanoutConfig
 *
 * @AUTHOR: roydon
 * @DATE: 2023/9/27
 **/
@Configuration
public class FanoutConfig {

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("study.fanout");
    }

    @Bean
    public Queue fanoutQueue11() {
        return new Queue("fanout.queue11"); //默认会消息持久化
    }

    @Bean
    public Binding fanoutBinding11(FanoutExchange fanoutExchange, Queue fanoutQueue11) {
        return BindingBuilder.bind(fanoutQueue11).to(fanoutExchange);
    }

    @Bean
    public Queue fanoutQueue12() {
        return new Queue("fanout.queue12"); //默认会消息持久化
    }

    @Bean
    public Binding fanoutBinding12() {
        return BindingBuilder.bind(fanoutQueue12()).to(fanoutExchange());
    }

}
