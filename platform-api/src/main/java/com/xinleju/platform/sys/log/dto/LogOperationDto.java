package com.xinleju.platform.sys.log.dto;

import com.xinleju.platform.base.dto.BaseDto;


import java.sql.Timestamp;



/**
 * @author admin
 * 
 *
 */
public class LogOperationDto extends BaseDto{

		
	//登录名
	private String loginName;
    
  		
	//姓名
	private String name;
    
  		
	//所在公司id
	private String comId;
    
  		
	//所在公司名称
	private String comName;
    
  		
	//登录ip
	private String loginIp;
    
  		
	//登录的浏览器
	private String loginBrowser;
    
  		
	//操作时间
	private Timestamp operationTime;
    
  		
	//操作类型ID
	private String operationTypeId;
    
  		
	//操作业务系统code
	private String sysCode;
    
  		
	//操作
	private String node;
    
  		
	//操作记录内容
	private String note;
  		
		
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getComId() {
		return comId;
	}
	public void setComId(String comId) {
		this.comId = comId;
	}
    
  		
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
    
  		
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
    
  		
	public String getLoginBrowser() {
		return loginBrowser;
	}
	public void setLoginBrowser(String loginBrowser) {
		this.loginBrowser = loginBrowser;
	}
    
  		
	public Timestamp getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(Timestamp operationTime) {
		this.operationTime = operationTime;
	}
  		
	public String getOperationTypeId() {
		return operationTypeId;
	}
	public void setOperationTypeId(String operationTypeId) {
		this.operationTypeId = operationTypeId;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
    
  		
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
    
  		
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
    
  		
}
