package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NormalConfigration {
    @Bean
    public DirectExchange normalExchange(){
        return ExchangeBuilder.directExchange("normal.direct").build();
    }
    @Bean
    public Queue normalQueue(){
//        return new Queue("fanout-queue3");
        return QueueBuilder.durable("normal.queue").deadLetterExchange("dlx.direct").build();
    }
    @Bean
    public Binding fanoutQueueBinding(DirectExchange normalExchange, Queue normalQueue){
        return BindingBuilder.bind(normalQueue).to(normalExchange).with("hi");
    }
}
