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
     * MQTT消息处理器（消费者）
     *
     * @return {@link org.springframework.messaging.MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttConsumerInboundChannel")
    public MessageHandler consumerHandler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println("消费端收到消息："+message.getPayload());
                System.out.println(message.toString());
            }
        };
    }
    /**
     * MQTT消息处理器（客户端）
     *
     * @return {@link org.springframework.messaging.MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println("客户端收到消息："+message.getPayload());
                System.out.println(message.toString());
            }
        };
    }

}
