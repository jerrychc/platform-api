package com.xinleju.platform.flow.service;

import com.xinleju.platform.flow.enumeration.FlowChangeType;

public class FlowProcessorFactory {

	public static FlowProcessor newProcessor(String changeType) {
		FlowProcessor processor = null;
		if(FlowChangeType.PROXY.getValue().equals(changeType)) {
			processor = new ProxyFlowProcessor();
			
		} else if(FlowChangeType.ADDLABEL.getValue().equals(changeType)) {
			processor = new AddLabelFlowProcessor();
			
		} else if(FlowChangeType.REPALCEAPPROVER.getValue().equals(changeType)) {
			processor = new ReplaceApproverFlowProcessor();
			
		} else if(FlowChangeType.WITHDRAW.getValue().equals(changeType)) {
			processor = new WithdrawFlowProcessor();
			
		} else {
			processor = new DefaultFlowProcessor();
		}
		return processor;
	}

}
