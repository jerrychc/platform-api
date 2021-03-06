package com.xinleju.platform.univ.mq.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

import java.util.Date;


/**
 * @author xubaoyong
 * 
 * 
 */

@Table(value="SYS_TEND_MQ_TOPIC",desc="消息中间件主题表")
public class Topic extends BaseEntity{
	
		
	@Column(value="topic",desc="消息的主题")
	private String topic;

	//被回调的bean的name
	@Column(value="rollback_beanName",desc="被回调的bean的name")
	private String rollBackBeanName;

	//验证数据是否被执行成功的beanName
	@Column(value="validate_beanName",desc="验证数据是否被执行成功的beanName")
	private String validateBeanName;

	/**
	 * 调用测试验证时间
	 */
	@Column(value="test_validate_time",desc="调用测试验证的时间")
	private Date testValidateTime;

	/**
	 * 测试调用回滚bean的时间
	 */
	@Column(value="test_rollback_time",desc="调用测试回滚bean的时间")
	private Date testRollbackTime;

	/**
	 * 调用测试验证的结果
	 */
	@Column(value="test_validateRestlt",desc="调用测试验证的结果")
	private Boolean testValidateRestlt;
	/**
	 * 测试调用回滚bean的结果
	 */
	@Column(value="test_rollbackBeanRestlt",desc="调用测试回滚的结果")
	private Boolean testRollbackBeanRestlt;

	@Column(value="consumer_beanName",desc="消费用的bean的名称")
	private String consumerBeanName;
    
  		
		
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getRollBackBeanName() {
		return rollBackBeanName;
	}

	public void setRollBackBeanName(String rollBackBeanName) {
		this.rollBackBeanName = rollBackBeanName;
	}


	public String getValidateBeanName() {
		return validateBeanName;
	}

	public void setValidateBeanName(String validateBeanName) {
		this.validateBeanName = validateBeanName;
	}

	public Date getTestValidateTime() {
		return testValidateTime;
	}

	public void setTestValidateTime(Date testValidateTime) {
		this.testValidateTime = testValidateTime;
	}

	public Date getTestRollbackTime() {
		return testRollbackTime;
	}

	public void setTestRollbackTime(Date testRollbackTime) {
		this.testRollbackTime = testRollbackTime;
	}

	public Boolean getTestValidateRestlt() {
		return testValidateRestlt;
	}

	public void setTestValidateRestlt(Boolean testValidateRestlt) {
		this.testValidateRestlt = testValidateRestlt;
	}

	public Boolean getTestRollbackBeanRestlt() {
		return testRollbackBeanRestlt;
	}

	public void setTestRollbackBeanRestlt(Boolean testRollbackBeanRestlt) {
		this.testRollbackBeanRestlt = testRollbackBeanRestlt;
	}

	public String getConsumerBeanName() {
		return consumerBeanName;
	}

	public void setConsumerBeanName(String consumerBeanName) {
		this.consumerBeanName = consumerBeanName;
	}
}
