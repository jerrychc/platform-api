package com.xinleju.platform.flow.service;

import java.util.Map;

/**
 * 表达式翻译接口
 * 负责将页面易于理解的表达式转换成内部Aviator可识别的表达式
 * 如：(公司=巨洲云) and (金额>9000) or (过期标志=true)
 * 转换为：(公司=='巨洲云') && (金额>9000) || (过期标志==true)
 * 
 * @author daoqi
 *
 */
public interface ExpressionTranslator {

	public String translate(String expression);

	public String translate(String expression, Map<String, Object> env);

	void clearTempKeys(Map<String, Object> env);
}
