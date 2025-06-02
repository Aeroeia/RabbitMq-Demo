package com.itheima.publisher.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
@Slf4j
//配置ReturnCallback 检查消息是否成功匹配到队列 在路由转发时触发
public class MqConfigration {
    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnsCallback(returned->{
            log.error("监听到了returncallback");
            log.debug("交换机:{}",returned.getExchange());
            log.debug("replyCode:{}",returned.getReplyCode());
            log.debug("路由键:{}",returned.getRoutingKey());
            log.debug("消息:{}",returned.getMessage());
            log.debug("原因:{}",returned.getReplyText());
        });
    }
}
