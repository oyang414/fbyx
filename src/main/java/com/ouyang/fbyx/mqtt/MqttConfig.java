package com.ouyang.fbyx.mqtt;

import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * @description:
 * @author: ouyangxingjie
 * @create: 2022/1/30 16:49
 **/
@Configuration
@IntegrationComponentScan
public class MqttConfig {

    //MQTT-用户名
    private String username = "qawsede2";

    //MQTT-密码
    private String password = "azsxdcd2";

    //MQTT-服务器连接地址，如果有多个，用逗号隔开，如：tcp://127.0.0.1:61613，tcp://192.168.2.133:61613
    private String hostUrl = "tcp://139.198.183.194:1883";

    /**
     * @Author ouyangxingjie
     * @Description 配置连接属性
     * @Date 0:20 2022/1/31
     * @return org.eclipse.paho.client.mqttv3.MqttConnectOptions
     */
    @Bean
    public MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        return mqttConnectOptions;
    }
    /**
     * @Author ouyangxingjie
     * @Description mqtt连接工厂
     * @Date 0:21 2022/1/31
     * @return org.springframework.integration.mqtt.core.MqttPahoClientFactory
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    /*********************************** 生产端（发布者）相关配置 ***********************************/
    private String outBoundId = "outBound";

    private String outBoundDefaultTopic = "topic";

    /**
     * @param
     * @description 发布消息的channel
     * @return org.springframework.messaging.MessageHandler
     * @author ouyangxingjie
     * @date 2022/1/31 0:11
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(outBoundId, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(outBoundDefaultTopic);
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }


    /*********************************** 消费端-one（订阅者） ***********************************/
    //MQTT-连接服务器默认客户端ID
    private String clientIdOne = "mqttId1";

    //MQTT-默认的消息推送主题，实际可在调用接口时指定(生产者 发送)
    private String defaultTopicOne = "world";

    @Bean
    public MessageProducer inboundOne() {
        // 可以同时消费（订阅）多个Topic
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        clientIdOne, mqttClientFactory(),
                        StringUtils.split(defaultTopicOne, ","));
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        // 设置订阅通道
        adapter.setOutputChannel(mqttInboundChannelOne());
        return adapter;
    }

    @Bean(name = "mqttInboundChannelOne")
    public MessageChannel mqttInboundChannelOne() {
        return new DirectChannel();
    }

    /*********************************** 消费端-two（订阅者） ***********************************/
    //默认的接收主题，可以订阅(消费者 接收)多个Topic，逗号分隔
    private String defaultTopicTwo = "hello,123";

    //连接服务器默认客户端ID
    private String clientIdTwo = "mqttId2";

    @Bean
    public MessageProducer inboundTwo() {
        // 可以同时消费（订阅）多个Topic
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        clientIdTwo, mqttClientFactory(),
                        StringUtils.split(defaultTopicTwo, ","));
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        // 设置订阅通道
        adapter.setOutputChannel(mqttInboundChannelTwo());
        return adapter;
    }


    @Bean(name = "mqttInboundChannelTwo")
    public MessageChannel mqttInboundChannelTwo() {
        return new DirectChannel();
    }


}