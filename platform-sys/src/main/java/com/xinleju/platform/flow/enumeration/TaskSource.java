package com.xinleju.platform.flow.enumeration;

/**
 * 任务来源类型：来源: 1:加签 ,2:正常 ,3:代理 ,4:监控 ,5:替换 ,6:授权
 * 目前只有代理情况下使用！！！
 * 
 * @author daoqi
 *
 */
public enum TaskSource {

	PROXY("代理", "3"), 			//3:代理
	AUTHORIZER("授权", "6");		//6:授权
	
	private String name;
	private String value;

	private TaskSource(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
