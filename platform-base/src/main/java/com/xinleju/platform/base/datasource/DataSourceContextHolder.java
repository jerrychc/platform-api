package com.xinleju.platform.base.datasource;

import org.apache.log4j.Logger;
public class DataSourceContextHolder {


	private static Logger log = Logger.getLogger(DataSourceContextHolder.class);
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    
    public static void setDataSourceType(String dataSourceType) {
    	  contextHolder.set(dataSourceType);
    	  log.info("setDataSourceType:"+dataSourceType);
    }
    
    public static String getDataSourceType() {
    	String dataSourceType = contextHolder.get();
    	log.info("getDataSourceType:"+dataSourceType);
        return dataSourceType;
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
 

}
