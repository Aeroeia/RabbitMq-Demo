package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfigration {
    @Bean
    public FanoutExchange fanoutExchange(){
        return ExchangeBuilder.fanoutExchange("hm.fanout").build();
    }
    @Bean
    public Queue fanoutQueue1(){
//        return new Queue("fanout-queue3");
        return QueueBuilder.durable("fanout-queue3").build();
    }
    @Bean
    public Queue fanoutQueue2(){
        return QueueBuilder.durable("fanout-queue4").build();
    }
    @Bean
    public Binding fanoutQueueBinding1(FanoutExchange fanoutExchange, Queue fanoutQueue1){
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }
    @Bean Binding fanoutQueueBinding2(FanoutExchange fanoutExchange, Queue fanoutQueue2){
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}
