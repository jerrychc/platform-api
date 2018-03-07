package com.xinleju.platform.base.datasource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.xinleju.platform.base.service.DataSourceBeanService;
import com.xinleju.platform.base.vo.TendDataSourceVo;


public class DynamicDataSource extends AbstractRoutingDataSource implements ApplicationContextAware{
	private static ApplicationContext context = null;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		context=applicationContext;
	}

	@Override
	protected Object determineCurrentLookupKey() {
		// TODO Auto-generated method stub
		return DataSourceContextHolder.getDataSourceType();
	}

	@Override
	public void afterPropertiesSet() {
		// TODO Auto-generated method stub
		try {
			initDataSources();
			super.afterPropertiesSet();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	private void initDataSources() throws SQLException {
		DefaultListableBeanFactory defaultFactory  = (DefaultListableBeanFactory)context.getAutowireCapableBeanFactory();
		BeanDefinitionBuilder druidDatasource = BeanDefinitionBuilder.rootBeanDefinition(DruidDataSource.class);
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		DataSourceBeanService dataService = defaultFactory.getBean(DataSourceBeanService.class);
		try {
			List<TendDataSourceVo> databaseList = dataService.getTendDataSourceVoList();
			for (TendDataSourceVo database : databaseList) {
				System.out.println( database.getTendCode());
				druidDatasource.getBeanDefinition().setAttribute("id", database.getTendCode());
				druidDatasource.addPropertyValue("driverClassName", database.getDriverName());
				druidDatasource.addPropertyValue("url", database.getUrl());
				druidDatasource.addPropertyValue("username", database.getUserName());
				druidDatasource.addPropertyValue("password", database.getPassword());
				// <!-- 配置获取连接等待超时的时间 -->  
				druidDatasource.addPropertyValue("maxWait",60000 );
				// <!-- 初始化连接数量 -->
				druidDatasource.addPropertyValue("initialSize",2);
				// <!-- 最大并发连接数 ->
				druidDatasource.addPropertyValue("maxActive", 1000);
				 //-- 最小空闲连接数 --
				druidDatasource.addPropertyValue("minIdle", 2);
				//<!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
				druidDatasource.addPropertyValue("timeBetweenEvictionRunsMillis", 60000);
				//  <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->  
				druidDatasource.addPropertyValue("minEvictableIdleTimeMillis", 300000);
				druidDatasource.addPropertyValue("validationQuery", "select 'x' ");
	
				druidDatasource.setInitMethodName("init");
				druidDatasource.setDestroyMethodName("close");
				
				defaultFactory.registerBeanDefinition(database.getTendCode(), druidDatasource.getBeanDefinition());
				targetDataSources.put(database.getTendCode(), context.getBean(database.getTendCode()));
			}
			this.setTargetDataSources(targetDataSources);
			BeanDefinitionBuilder	sqlSessionFactory = BeanDefinitionBuilder.rootBeanDefinition(SqlSessionFactoryBean.class);
			sqlSessionFactory.getBeanDefinition().setAttribute("id", "sqlSessionFactory");
			sqlSessionFactory.addPropertyReference("dataSource", "dataSource");
			Properties property=(Properties) context.getBean("configuration");
			sqlSessionFactory.addPropertyValue("mapperLocations", property.get("mybatis.mapper"));
			defaultFactory.registerBeanDefinition("sqlSessionFactory", sqlSessionFactory.getBeanDefinition());
			
			
			
			//覆盖原有txManager
			BeanDefinitionBuilder	transactionManager  = BeanDefinitionBuilder.rootBeanDefinition(DataSourceTransactionManager.class);
			transactionManager.getBeanDefinition().setAttribute("id", "transactionManager");
			transactionManager.addPropertyReference("dataSource", "dataSource");
			defaultFactory.registerBeanDefinition("transactionManager", transactionManager.getBeanDefinition());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
