package com.xinleju.platform.base.init;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

public class DynamicDataSourceRegister  implements ImportBeanDefinitionRegistrar, EnvironmentAware{

	@Override
	public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata,
			BeanDefinitionRegistry registry) {
		  // TODO Auto-generated method stub
		  Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
          System.out.println(registry.getBeanDefinitionNames());
 
	}

	@Override
	public void setEnvironment(Environment environment) {
		// TODO Auto-generated method stub
		
	}

}
