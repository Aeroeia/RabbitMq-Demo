package com.itheima.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
@Slf4j
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
        rabbitTemplate.convertAndSend("t","","hello");
    }
    //消息转换器测试
    @Test
    public void converterTest(){
        Map<String,Object> map = new HashMap<>();
        map.put("name","zhangsan");
        map.put("age",18);
        rabbitTemplate.convertAndSend( "test",map);
    }
    //ConfirmCallBack 在交换机层
    @Test
    public void confirmTest() throws InterruptedException {
        //创建correlationData
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        //Future 异步相关
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("SpringAmqp处理确认结果异常:{}",ex.getMessage());
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                if(result.isAck()){
                    log.debug("消息发送成功");
                }
                //消息没到交换机时触发
                else{
                    log.debug("消息发送失败");
                }
            }
        });
        String exchange = "amq.direct";
        String message = "hello";
        rabbitTemplate.convertAndSend(exchange,"red",message,cd);
        Thread.sleep(2000);
    }
}