package com.xinleju.platform.univ.mq.utils;

public class ConfigConstant {
    /**
     * 约定于applicationContext-rabbit.xml中的交换器名称一致
     */
    public static final String EXCHANGE_NAME = "XLJ_EXCHANGE";

    public static final String RABBIT_TEMPLATEID = "amqpTemplate";
    
    public static final String RABBIT_LISTENER= "listener";
    /**
     * 约定于applicationContext-rabbit.xml中的文本转换器一致
     */
    public static final String JSONMESSAGECONVERTER   = "rabbitjsonMessageConverter";
    /**
     * 约定于applicationContext-rabbit.xml中的链接工厂一致
     */
    public static final String  RABBITIT_CONNECTIONFACTORY = "rabitConnectionFactory";

    public static final String  RABBIT_ACKNOWLEDGE = "acknowledge";
    /**
     * 约定于applicationContext-rabbit.xml中的消费者一致
     */
    public static final String RABBITMQCOMMONCONSUMER = "rabbitMqCommonConsumer";

    public static final String RABBITMQCOMMONCONSUMER_KEY = "rabbitMqCommonConsumer_key";
}
