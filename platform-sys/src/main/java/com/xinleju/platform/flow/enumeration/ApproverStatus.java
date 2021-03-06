package com.xinleju.platform.flow.enumeration;

public enum ApproverStatus {

	NOT_RUNNING("未运行", "1"), 
	RUNNING("运行中", "2"), 
	FINISHED("完成", "3"),
	HANGUP("挂起", "4");

	private String name;
	private String value;

	private ApproverStatus(String name, String value) {
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
