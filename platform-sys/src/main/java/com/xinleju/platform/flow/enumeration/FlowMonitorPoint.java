package com.xinleju.platform.flow.enumeration;

/**
 * 流程监控点
 * 1流程发起2被监控人收到待办3被监控人处理完成4流程完成5挂起6作废7打回8发起人撤回
 * @author daoqi
 *
 */
public enum FlowMonitorPoint {

	FLOW_START("流程发起", "1"), 
	TODO_RECEIVE("被监控人收到待办", "2"), 
	TODO_DONE("被监控人处理完成", "3"),
	FLOW_END("流程完成", "4"),
	FLOW_HANGUP("挂 起", "5"),
	FLOW_CANCEL("作废", "6"),
	FLOW_RETURN("打回", "7"),
	FLOW_WITHDRAW("发起人撤回", "8");

	private String name;
	private String value;

	private FlowMonitorPoint(String name, String value) {
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
