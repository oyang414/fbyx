package com.ouyang.fbyx.mqtt;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * @description: mqtt消费者
 * @author: ouyangxingjie
 * @create: 2022/1/30 17:16
 **/
@Component
public class MqttConsumer {


   /**
    * @Author ouyangxingjie
    * @Description MQTT消息处理器 消费者-one（订阅者）
    * @Date 0:38 2022/1/31
    * @return org.springframework.messaging.MessageHandler
    */
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelOne")
    public MessageHandler consumerHandler() {
        return message -> {
            System.out.println("one收到消息："+message.getPayload());
            System.out.println(message);
        };
    }
    /**
     * @Author ouyangxingjie
     * @Description MQTT消息处理器 消费者-two（订阅者）
     * @Date 0:38 2022/1/31
     * @return org.springframework.messaging.MessageHandler
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelTwo")
    public MessageHandler handler() {
        return message -> {
            System.out.println("two收到消息："+message.getPayload());
            System.out.println(message);
        };
    }

}
