package com.itheima.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class SpringAMQPTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final String queueName = "simple-queue";

    @Test
    public void SimpleQueueTest() {
        rabbitTemplate.convertAndSend(queueName, "hello world");
    }

    //均匀分配测试
    @Test
    public void WorkQueueTest() {
        for (int i = 0; i < 50; i++) {
            rabbitTemplate.convertAndSend("work-queue", "hello world" + i);
        }
    }

    //fanout交换机
    @Test
    public void fanoutTest() {
        rabbitTemplate.convertAndSend("amq.fanout", "", "hello world");
    }

    //direct交换机
    @Test
    public void directTest() {
        rabbitTemplate.convertAndSend("amq.direct", "blue", "hello world");
    }

    //topic交换机
    @Test
    public void topicTest() {
        rabbitTemplate.convertAndSend("amq.topic", "china.news", "hello world");
    }

    @Test
    public void annotationTest() {
        rabbitTemplate.convertAndSend("hmdirect-exchange", "red", "hello world");
        rabbitTemplate.convertAndSend("t", "", "hello");
    }

    //消息转换器测试
    @Test
    public void converterTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        map.put("age", 18);
        rabbitTemplate.convertAndSend("test", map);
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
                log.error("SpringAmqp处理确认结果异常:{}", ex.getMessage());
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                if (result.isAck()) {
                    log.debug("消息发送成功");
                }
                //消息没到交换机时触发
                else {
                    log.debug("消息发送失败");
                }
            }
        });
        String exchange = "amq.direct";
        String message = "hello";
        rabbitTemplate.convertAndSend(exchange, "red", message, cd);
        Thread.sleep(2000);
    }

    /*测试消息堵塞 数据大规模时直接持久化到磁盘会比写入内存(非持久化)处理速度要快而且稳定
    是因为内存满了要写入磁盘 这个过程会导致堵塞
    */
    @Test
    public void testMeesageSend() {
        //自定义非持久化，消息发送默认持久化
        Message message = MessageBuilder.withBody("hello".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT)
                .build();
        for (int i = 0; i < 1000000; i++) {
            rabbitTemplate.convertAndSend("simple-queue",message);
        }
    }
}