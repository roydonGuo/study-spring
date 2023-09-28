package com.roydon.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FanoutConfig
 *
 * @AUTHOR: roydon
 * @DATE: 2023/9/27
 **/
@Configuration
public class DirectConfig {

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("study.direct");
    }

    @Bean
    public Queue directQueue11() {
        return new Queue("direct.queue11"); //默认会消息持久化
    }

    @Bean
    public Binding directBinding11Red(DirectExchange directExchange, Queue directQueue11) {
        return BindingBuilder.bind(directQueue11).to(directExchange).with("red");
    }

    @Bean
    public Binding directBinding11Blue(DirectExchange directExchange, Queue directQueue11) {
        return BindingBuilder.bind(directQueue11).to(directExchange).with("blue");
    }

    @Bean
    public Queue directQueue12() {
        return new Queue("direct.queue12"); //默认会消息持久化
    }

    @Bean
    public Binding directBinding12Red() {
        return BindingBuilder.bind(directQueue12()).to(directExchange()).with("red");
    }

    @Bean
    public Binding directBinding12Yellow() {
        return BindingBuilder.bind(directQueue12()).to(directExchange()).with("yellow");
    }

}
