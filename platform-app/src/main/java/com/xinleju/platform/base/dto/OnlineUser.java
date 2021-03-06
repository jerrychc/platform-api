package com.xinleju.platform.base.dto;

public class OnlineUser {
	
	public static final String  ONLINE_USER ="OnLineUser";
	
	//用户名
	private String userName;
	
	//帐号
	private String loginName;
	
	//主键
	private String id;
	
	//登陆ip
	private String ip;
	
	//登陆sessionId
	private String sessionId;
	
	//租户id
	private String tendId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTendId() {
		return tendId;
	}

	public void setTendId(String tendId) {
		this.tendId = tendId;
	}

	
 
}
