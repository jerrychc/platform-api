package com.xinleju.platform.flow.operation;

public enum OperationType {
	AGREE("TY"),			//同意
	ASSIST("XB"),			//协办
	RETURN("DH"),			//打回
	TRANSFER("ZB"),			//转办
	REPLY("HF"),			//回复
	TALKTOFQR("GTFQR"),		//沟通发起人
	NOOBJECTION("WYY"),		//无异议
	ACCEPT("JS"),			//接受
	NOACCEPT("BJS"),		//不接受
	CC("CC"),				//抄送
	
	//管理员操作
	SKIPCURRENT("SKIPCURRENT"),		//跳过当前审批人
	FINISHFLOW("FINISHFLOW"),		//审结流程
	RESTART("RESTART"),				//流程放行
	INVALID("INVALID"),				//作废流程
	
	WITHDRAW_TASK("withdraw_task"),	//审批人撤回任务
	WITHDRAW_FLOW("withdraw_flow"),	//发起人撤回流程
	
	//流程内部自动操作类型
	START("START"),			//开始
	END("END"),				//结束
	EMPTY("NULL");			//空操作
	private String code;
	
	OperationType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
