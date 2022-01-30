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

    /**
     * @Author ouyangxingjie
     * @Description 消息生产端（发布者）
     * @Date 0:35 2022/1/31
     * @param sendData 数据实体
     * @param topic 主题
     * @return java.lang.String
     */
    @RequestMapping("/send")
    public String send(String sendData,String topic){
        mqttGateway.sendToMqtt(sendData,topic);
        return "发送内容：<"+sendData+"> 成功----"+" 主题："+topic;
    }
}