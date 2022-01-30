package com.ouyang.fbyx.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * @description:
 * @author: ouyangxingjie
 * @create: 2022/1/30 16:52
 **/

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {
    void sendToMqtt(String data,@Header(MqttHeaders.TOPIC) String topic);
}
