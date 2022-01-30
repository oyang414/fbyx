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


    @Bean
    public MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        return mqttConnectOptions;
    }
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }


    //================================================================客户端=======================================

    //MQTT-连接服务器默认客户端ID
    private String clientId = "mqttId111";

    //MQTT-默认的消息推送主题，实际可在调用接口时指定(生产者 发送)
    private String defaultTopic = "world";

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =  new MqttPahoMessageHandler(clientId+"_outBound", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(defaultTopic);
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        // 可以同时消费（订阅）多个Topic
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        clientId + "_inBound", mqttClientFactory(),
                        StringUtils.split(defaultTopic, ","));
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        // 设置订阅通道
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;
    }

    @Bean(name = "mqttInboundChannel")
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

//================================================================消费端=======================================
    //默认的接收主题，可以订阅(消费者 接收)多个Topic，逗号分隔
    private String consumerDefaultTopic = "hello,123";

    //连接服务器默认客户端ID
    private String consumerClientId = "mqttId222";

    @Bean
    @ServiceActivator(inputChannel = "mqttConsumerOutboundChannel")
    public MessageHandler mqttConsumerOutbound() {
        MqttPahoMessageHandler messageHandler =  new MqttPahoMessageHandler(consumerClientId+"_outBound", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(consumerDefaultTopic);
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttConsumerOutboundChannel() {
        return new DirectChannel();
    }


    /**
     * MQTT消息订阅绑定（消费者）
     *
     * @return {@link org.springframework.integration.core.MessageProducer}
     */

    @Bean
    public MessageProducer consumerInbound() {
        // 可以同时消费（订阅）多个Topic
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        consumerClientId+"_inBound", mqttClientFactory(),
                        StringUtils.split(consumerDefaultTopic, ","));
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        // 设置订阅通道
        adapter.setOutputChannel(mqttConsumerInboundChannel());
        return adapter;
    }

    /**
     * MQTT信息通道（消费者）
     *
     * @return {@link org.springframework.messaging.MessageChannel}
     */
    @Bean(name = "mqttConsumerInboundChannel")
    public MessageChannel mqttConsumerInboundChannel() {
        return new DirectChannel();
    }


}