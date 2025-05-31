package com.itheima.consumer.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringRabbitListener {
    private static int cnt1 = 0;
    private static int cnt2 = 0;
    //单消费者
    @RabbitListener(queues = "simple-queue")
    public void RabbitListener(String msg){
        log.info("消费者接收到的消息是：{}",msg);
    }

    //双消费者 一条消息只能被一个消费者处理 默认均匀分配
    @RabbitListener(queues = "work-queue")
    public void workQueueListener1(String msg) throws InterruptedException {
        cnt1++;
//        System.out.println("消费者1接收到的消息是：" + msg);
        Thread.sleep(20);
        System.out.println("消费者1处理了" + cnt1 + "条消息");
    }
    @RabbitListener(queues = "work-queue")
    public void workQueueListener2(String msg) throws InterruptedException {
        cnt2++;
//        System.err.println("消费者2接收到的消息是：" + msg);
        Thread.sleep(200);
        System.err.println("消费者2处理了" + cnt2 + "条消息");
    }
}
