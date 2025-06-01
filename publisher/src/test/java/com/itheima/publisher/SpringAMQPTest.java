package com.itheima.publisher;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SpringAMQPTest {
    @Autowired
    private  RabbitTemplate rabbitTemplate;

    private final String queueName = "simple-queue";
    @Test
    public void SimpleQueueTest(){
        rabbitTemplate.convertAndSend( queueName,"hello world");
    }
    //均匀分配测试
    @Test
    public void WorkQueueTest(){
        for(int i = 0; i < 50; i++){
            rabbitTemplate.convertAndSend( "work-queue","hello world"+i);
        }
    }
    //fanout交换机
    @Test
    public void fanoutTest(){
        rabbitTemplate.convertAndSend( "amq.fanout","", "hello world");
    }

    //direct交换机
    @Test
    public void directTest(){
        rabbitTemplate.convertAndSend( "amq.direct","blue", "hello world");
    }

    //topic交换机
    @Test
    public void topicTest(){
        rabbitTemplate.convertAndSend( "amq.topic","china.news", "hello world");
    }

    @Test
    public void annotationTest(){
        rabbitTemplate.convertAndSend( "hmdirect-exchange","red", "hello world");
    }
}