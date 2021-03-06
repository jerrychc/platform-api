package com.xinleju.platform.univ.mq.utils;

import com.alibaba.fastjson.JSON;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.univ.mq.dto.TopicDto;
import com.xinleju.platform.univ.mq.dto.service.TopicDtoServiceCustomer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.*;

/**
 * Created by xubaoyong on 2017/3/30.
 */
public class ConsumerXmlLoadBeans {

    private static Logger log = Logger.getLogger(ConsumerXmlLoadBeans.class);

    private TopicDtoServiceCustomer mqTopicDtoServiceCustomer;

    private List<TopicDto> createdQueueList = new ArrayList<>();

    private Map<String,String> queueExistMap = new HashMap<>();

    private Map<String,TopicDto> consumerExistMap = new HashMap<>();


    public void doRegisterBean(Properties properties , ConfigurableApplicationContext applicationContext,List<TopicDto > createdQueueList) throws Exception {

        DefaultListableBeanFactory reg =  (DefaultListableBeanFactory) applicationContext .getBeanFactory();
        /**
         * 获得bean,主题mqTopicService
         */
        this.mqTopicDtoServiceCustomer = (TopicDtoServiceCustomer) applicationContext.getBean("mqTopicDtoServiceCustomer");

        if(createdQueueList != null && createdQueueList.size() > 0){
            this.createdQueueList = createdQueueList;
        }

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(reg);
        String generateXml = generateXml(reg);
        reader.loadBeanDefinitions(new MemoryXmlResource(generateXml));

        if(log.isDebugEnabled()){
            String[] beanNames = applicationContext.getBeanDefinitionNames();
            if(beanNames.length > 0){
                log.info("--------------------动态创建的bean有---------------------");
                for(String beanName :beanNames){
                    log.info(" {}"+beanName);
                }
                log.info(" 结束");
            }
        }

    }
    private String generateXml(DefaultListableBeanFactory beanFactory) throws Exception {
        String  content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                "\t  xmlns:dubbo=\"http://code.alibabatech.com/schema/dubbo\"\n"+
                "\t   xmlns:util=\"http://www.springframework.org/schema/util\"\n" +
                "\t   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rabbit=\"http://www.springframework.org/schema/rabbit\"\n" +
                "\t   xsi:schemaLocation=\"http://www.springframework.org/schema/beans\n" +
                "     http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\n" +
                "     http://www.springframework.org/schema/beans\n" +
                "     http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\n" +
                "     http://www.springframework.org/schema/rabbit\n" +
                "     http://www.springframework.org/schema/rabbit/spring-rabbit-1.0.xsd\n" +
                "        http://www.springframework.org/schema/util\n" +
                "        http://www.springframework.org/schema/util/spring-util-4.3.xsd\n" +
                "   http://code.alibabatech.com/schema/dubbo          \n" +
                "        http://code.alibabatech.com/schema/dubbo/dubbo.xsd\">\n";

        StringBuffer stringBuffer = new StringBuffer(content);
        //只有当createdQueueList为空时才重新从数据库中加载
        if(createdQueueList == null || createdQueueList.size() == 0){
            //创建队列
            doRecursionTopic( beanFactory, 0, stringBuffer);
        }
        //创建消费者对应的bean
       createMessageConsumer(beanFactory,this.createdQueueList,stringBuffer);
//        log.info("=="+stringBuffer.toString());
        //        创建dubbox引用
//        this.createDubboxReference(beanFactory,stringBuffer);
        //创建监听器
        createListenerContainer(beanFactory,stringBuffer);

        stringBuffer.append("\n\t</beans>");
        content  = stringBuffer.toString();

        if(log.isInfoEnabled()){
            log.info("自定义bean中消费者对应的bean为"+content);
        }
        return content;
    }


    //递归调用topic
    private void  doRecursionTopic(DefaultListableBeanFactory beanFactory,int pageNo,StringBuffer stringBuffer) throws Exception {
        int pageSize = 10000;
        PageBeanInfo page =  getAllTopic(pageNo,pageSize);
        List<TopicDto> topicList = page.getList();
        /**
         * 动态创建queue
         *
         */
        this.createQueueBeanByTopicList(beanFactory,topicList,stringBuffer);


        int total = page.getTotal();
        int countPage = total <= 0 ? 0:(int) Math.ceil((double) total / pageSize);
        if(pageNo < countPage){
            pageNo ++;
            doRecursionTopic( beanFactory, pageNo,stringBuffer);//执行下一页
        }
    }
    /**
     * 创建topicBean
     */
    public void createQueueBeanByTopicList(DefaultListableBeanFactory beanFactory,List<TopicDto> topicList,StringBuffer stringBuffer){
        if(topicList == null || topicList.size() == 0){
            log.warn("not get mqtopic from db");
            return;
        }else {
            String templetate = "<rabbit:queue id=\"#topicName#\" name=\"#topicName#\" durable=\"true\" auto-delete=\"false\" exclusive=\"false\" />";
            for (TopicDto tempTopic : topicList) {
                String beanName = String.format("Queue_%s_%s",tempTopic.getTendId()==null?"":tempTopic.getTendId(),tempTopic.getTopic());
                if(beanFactory.containsBean(beanName) || queueExistMap.containsKey(beanName)){
                    log.warn(String.format("%s have duplicate",beanName));
                }else {
                    String queueTempletate = templetate.replaceAll("#topicName#",beanName);

                    createdQueueList.add(tempTopic);
                    stringBuffer.append( queueTempletate);
                    queueExistMap.put(beanName,beanName);
                }

            }
        }
    }

    /**
     * 创建消息消费者
     * <bean id="messageReceiver" class="com.springrabbitmq.MessageConsumer"></bean>
     */
    public void createMessageConsumer(DefaultListableBeanFactory beanFactory,List<TopicDto> topicList,StringBuffer stringBuffer){
        if(topicList == null || topicList.size() == 0){
            log.warn("not get mqtopic from db");
            return;
        }else {
            String templetate = "<bean id=\"beanId\" class=\"#beanClass#\"></bean>";
            for (TopicDto tempTopic : topicList) {
               // String beanName = String.format("consumer_%s_%s",tempTopic.getTendId()==null?"":tempTopic.getTendId(),tempTopic.getTopic());
                String beanName =tempTopic.getConsumerBeanName();
                String beanClass = "";//tempTopic.getConsumerClass();
                if(StringUtils.isEmpty(beanName) || StringUtils.isEmpty(beanClass)){
                    if(StringUtils.isEmpty(beanName)){
                        log.warn(String.format("beanName is error,topic id is "+ tempTopic.getId()));
                    }
                    if(StringUtils.isEmpty(beanClass)){
                        log.warn(String.format("beanClass is error,topic id is "+ tempTopic.getId()));
                    }
                }else  if(beanFactory.containsBean(beanName) || consumerExistMap.containsKey(beanName)){
                    log.warn(String.format("%s have duplicate",beanName));
                }else {
                    String consumerTempletate = "";
                    consumerTempletate = templetate.replaceAll("beanId",beanName);
                    consumerTempletate = consumerTempletate.replaceAll("#beanClass#",beanClass);
                    //stringBuffer.append( consumerTempletate);
                    consumerExistMap.put(beanName,tempTopic);
                }

            }
        }
    }

    /**
     * 从数据库中加载所有可以使用的topic
     * @return
     */
    public PageBeanInfo getAllTopic(int pageNum,int pageSize ) throws Exception {

        String userInfo = null;
        Map<String, Object> params = new HashMap();
        params.put("delflag",false);
        params.put("start",pageNum*pageSize);
        params.put("limit",pageSize);
        params.put("start",pageNum);
        PageBeanInfo pageInfo= null;
        String pageResultString  = mqTopicDtoServiceCustomer.getPage(userInfo, JSON.toJSONString(params));
        DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(pageResultString, DubboServiceResultInfo.class);
        if(dubboServiceResultInfo.isSucess()){
            String resultInfo= dubboServiceResultInfo.getResult();
             pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);

        }
        return pageInfo;
    }

    public void createListenerContainer(DefaultListableBeanFactory beanFactory, StringBuffer stringBuffer){
// <!--
//                监听配置
//        queue litener  观察 监听模式 当有消息到达时会通知监听在对应的队列上的监听对象 ,手动确认消息
//        queues：监听的队列，多个的话用逗号（,）分隔
//        ref：监听器
//                -->
//    <rabbit:listener-container  id="listener-container" connection-factory="connectionFactory" acknowledge="manual">
//        <rabbit:listener queues="queueTest" ref="messageReceiver"/>
//        <rabbit:listener queues="queueOrder" ref="orderMessageReceiver"/>
//    </rabbit:listener-container>

        if(consumerExistMap.isEmpty()){
            return;
        }
        stringBuffer.append("<rabbit:listener-container  id=\"listener-container\" connection-factory=\""+ConfigConstant.RABBITIT_CONNECTIONFACTORY+"\" acknowledge=\"manual\">");


        for (Map.Entry<String, TopicDto> entry : consumerExistMap.entrySet()) {
                    String  ref = entry.getKey();
                TopicDto  tempTopic =entry.getValue();

                String queueName = String.format("Queue_%s_%s",tempTopic.getTendId()==null?"":tempTopic.getTendId(),tempTopic.getTopic());
                stringBuffer.append("<rabbit:listener queues=\""+queueName+"\" ref=\"commonConsumer\"/>");
        }
        stringBuffer.append("</rabbit:listener-container>");
    }

    private void createDubboxReference(DefaultListableBeanFactory beanFactory, StringBuffer stringBuffer){
//        	<dubbo:reference id="mqTopicDtoServiceCustomer" interface="com.xinleju.platform.middeleware.message.dto.service.TopicDtoServiceCustomer" />
        for(TopicDto tempTopic :createdQueueList){
            String beanName = tempTopic.getConsumerBeanName();
            if(!consumerExistMap.containsKey(beanName)){
                consumerExistMap.put(beanName,tempTopic);
                String reference = "<dubbo:reference id=\""+beanName+"\" interface=\"com.xinleju.platform.middeleware.message.plugins.interfaces.AbstractRabbitConsumer\" />";//desc="mq_topic表的id"
                stringBuffer.append(reference);
            }

        }
    }
}
