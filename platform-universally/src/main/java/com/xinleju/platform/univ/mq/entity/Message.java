package com.xinleju.platform.univ.mq.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author xubaoyong
 * 
 * 
 */

@Table(value="PT_UNIV_MQ_MESSAGE",desc="消息中间件消息表")
public class Message extends BaseEntity{
	
		
	@Column(value="msg_key",desc="消息关键字")
	private String msgKey;
    
  		
	@Column(value="topic",desc="消息主题")
	private String topic;
    
  		
	@Column(value="body",desc="消息内容")
	private String body;
    
  		
	@Column(value="tag",desc="消息标签")
	private String tag;
    
  		
	@Column(value="state",desc="消息状态")
	private Integer state;
    
  		
	@Column(value="parent_id",desc="消息父ID")
	private String parentId;
    
  		
	@Column(value="isReSend",desc="是否属于重发消息")
	private Integer isReSend;

	@Column(value="topic_id",desc="主题的ID")
	private String topicId;


	/**
	 * 预发送
	 */
	public final static  Integer MqMessageState_PreSend = Integer.valueOf("1");
	/**
	 * 已发送，未消费
	 */
	public final static  Integer MqMessageState_WaitConsume = Integer.valueOf("2");
	/**
	 * 已消费
	 */
	public final static  Integer MqMessageState_Consumed = Integer.valueOf("3");

	/**
	 * 消费失败
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
