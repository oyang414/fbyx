package com.ouyang.fbyx.mqtt;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:
 * @author: ouyangxingjie
 * @create: 2022/1/30 16:53
 **/
@RestController
@RequestMapping("/mqtt")
public class MqttController {

    @Resource
    private MqttGateway mqttGateway;

    @Resource
    private MqttConsumerGateway mqttConsumerGateway;

    @RequestMapping("/send")
    public String send(String sendData,String topic){
        mqttGateway.sendToMqtt(sendData,topic);
        return "发送内容：<"+sendData+">成功----"+"主题："+topic;
    }

    @RequestMapping("/reply")
    public String reply(String sendData,String topic){
        mqttConsumerGateway.sendToMqtt(sendData,topic);
        return "发送内容：<"+sendData+">成功----"+"主题："+topic;
    }
}