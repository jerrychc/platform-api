package com.xinleju.platform.flow.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xinleju.platform.flow.service.MonitorSettingService;

@Aspect
@Component
public class MonitorInterceptor {
	
	@Autowired
	private MonitorSettingService monitorSettingService;
	
	@Pointcut()
	public void acceptTodo(){}

	@Before("acceptTodo()")
	public void before() {
		
	}
}
