package com.xinleju.platform.sys.notice.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class MailMsgDto extends BaseDto{

		
	//主题
	private String title;
    
  		
	//发送人名称
	private String sendName;
    
  		
	//发送人地址
	private String sendAddress;
    
  		
	//抄送人名称
	private String copyName;
    
  		
	//抄送人地址
	private String copyAddress;
    
  		
	//正文
	private String context;
    
  		
	//状态
	private String status;
    
  		
	//发送次数
	private Integer num;
    
  		
	//服务器Id
	private String mailServerId;
    
  		
		
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    
  		
	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
    
  		
	public String getSendAddress() {
		return sendAddress;
	}
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
    
  		
	public String getCopyName() {
		return copyName;
	}
	public void setCopyName(String copyName) {
		this.copyName = copyName;
	}
    
  		
	public String getCopyAddress() {
		return copyAddress;
	}
	public void setCopyAddress(String copyAddress) {
		this.copyAddress = copyAddress;
	}
    
  		
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
    
  		
	public String getMailServerId() {
		return mailServerId;
	}
	public void setMailServerId(String mailServerId) {
		this.mailServerId = mailServerId;
	}
    
  		
}
