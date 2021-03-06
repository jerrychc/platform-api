package com.xinleju.platform.flow.operation;

import com.xinleju.platform.flow.model.ACUnit;

/**
 * 接受操作
 * 
 * @author daoqi
 *
 */
public class CCOperation extends DefaultOperation implements Operation{

	CCOperation() {
		super(OperationType.CC);
	}

	@Override
	protected void complate(ACUnit currentAc) {
		
		//抄送人处理
		createToReadMsg(currentAc.getCcPerson(), instanceUnit);
	}

}
