package com.xinleju.platform.base.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext; 

	@Override
	public void setApplicationContext(ApplicationContext arg)
			throws BeansException {
		// TODO Auto-generated method stub
		applicationContext=arg;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	  /** 
     * 根据bean的id来查找对象 
     * @param id 
     * @return 
     */
    public static Object getBeanById(String id){ 
        return applicationContext.getBean(id); 
    } 
      
    /** 
     * 根据bean的class来查找对象 
     * @param c 
     * @return 
     */
    public static Object getBeanByClass(Class c){ 
        return applicationContext.getBean(c); 
    } 

}
