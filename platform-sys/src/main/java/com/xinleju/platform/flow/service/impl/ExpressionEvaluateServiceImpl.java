package com.xinleju.platform.flow.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.exception.ExpressionSyntaxErrorException;
import com.xinleju.platform.flow.exception.FlowException;
import com.xinleju.platform.flow.service.ExpressionEvaluateService;
import com.xinleju.platform.flow.service.ExpressionTranslator;

@Service
public class ExpressionEvaluateServiceImpl implements ExpressionEvaluateService {
	
	private static Logger log = Logger.getLogger(ExpressionEvaluateServiceImpl.class);

	@Override
	public boolean validate(String expression) {
		
		String innerExpression = translate(expression);
		
		try {
			AviatorEvaluator.compile(innerExpression);
			log.info("表达式校验成功：" + expression);
			return true;
			
		} catch(ExpressionSyntaxErrorException e) {
			log.error("表达式校验失败：" + expression, e);
			return false;
		} catch(Exception e) {
			log.error("表达式校验异常！", e);
			throw new FlowException(e);
		}
		
	}

	private String translate(String expression) {
		ExpressionTranslator translator = new GeneralExpressionTranslator();
		return translator.translate(expression);
	}

}
