package com.itheima.consumer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ErrorMessageConfiguration {
    private final RabbitTemplate rabbitTemplate;
    /*本地重连失败后返回nack 不然默认直接reject
    @Bean
    public MessageRecoverer messageRecoverer(){
        return new ImmediateRequeueMessageRecoverer();
   }*/


    //绑定交换机、队列
    @Bean
    public DirectExchange errorExchange(){
        return new  DirectExchange("error-exchange");
    }
    @Bean
    public Queue errorQueue(){
        return new Queue("error-queue");
    }
    @Bean
    public Binding binding(DirectExchange errorExchange,Queue errorQueue){
        return BindingBuilder.bind(errorExchange).to(errorExchange).with("error");
    }
    //本地重试失败后将消息返回到指定交换机、队列
    @Bean
    public MessageRecoverer messageRecoverer(){
        return new RepublishMessageRecoverer( rabbitTemplate,"error-exchange","error");
    }
}
