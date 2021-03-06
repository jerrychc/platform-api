package com.xinleju.platform.univ.mq.dto;


import java.io.Serializable;

import com.xinleju.platform.base.dto.BaseDto;

/**
 * @author xubaoyong
 * 
 *
 */
public class MessageDto extends BaseDto implements Serializable{


	private static final long serialVersionUID = -2486480925742136949L;
	//消息关键字
	private String msgKey;
    
  		
	//消息主题
	private String topic;
    
  		
	//消息内容
	private String body;
    
  		
	//消息标签
	private String tag;
    
  		
	//消息状态
	private Integer state;
    
  		
	//消息父ID
	private String parentId;
    
  		
	//是否属于重发消息
	private Integer isReSend;
	//主题的ID
	private String topicId;
	
	//主题的ID
	private String userInfo;

	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * 预发送 注意与mqMessage.java里一致,预发送状态为1
	 */
	public final static  Integer MqMessageState_PreSend = Integer.valueOf("1");
	/**
	 * 已发送，未消费 注意与mqMessage.java里一致，,已发送，状态为2
	 */
	public final static  Integer MqMessageState_WaitConsume = Integer.valueOf("2");
	/**
	 * 已消费  注意与mqMessage.java里一致，,已消费状态为3
	 */
	public final static  Integer MqMessageState_Consumed = Integer.valueOf("3");

	/**
	 * 消费失败  注意与mqMessage.java里一致，,消费失败状态为4
	 */
	public final static  Integer MqMessageState_ConsumeFailed = Integer.valueOf("4");
		
	public String getMsgKey() {
		return msgKey;
	}
	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}
    
  		
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
    
  		
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
    
  		
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
    
  		
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    
  		
	public Integer getIsReSend() {
		return isReSend;
	}
	public void setIsReSend(Integer isReSend) {
		this.isReSend = isReSend;
	}


	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
}
