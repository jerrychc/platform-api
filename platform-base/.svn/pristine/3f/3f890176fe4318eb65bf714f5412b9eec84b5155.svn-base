package com.xinleju.platform.base.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.rpc.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.xinleju.platform.base.datasource.DataSourceContextHolder;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.tools.data.JacksonUtils;


public class DubboServiceFilter  implements Filter{
	private static Logger log = Logger.getLogger(DubboServiceFilter.class);
	
	
	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		//处理dubbo异步调用serviceA时，serviceA调用serviceB时异步传染问题
		RpcContext.getContext().getAttachments().remove(Constants.ASYNC_KEY);
		//处理数据远切换
		Object[] objects=invocation.getArguments();
		if(objects!=null && objects.length>0){
		    Object obj=objects[0];
		    if(obj!=null && StringUtils.isNotBlank(obj.toString())){
			    SecurityUserBeanInfo securityUserBeanInfo=JacksonUtils.fromJson(obj.toString(), SecurityUserBeanInfo.class);
			    if(securityUserBeanInfo!=null){
				    LoginUtils.setSecurityUserBeanInfo(securityUserBeanInfo);
				    DataSourceContextHolder.clearDataSourceType();
				    DataSourceContextHolder.setDataSourceType(securityUserBeanInfo.getTendCode());
			    }else{
			    	 DataSourceContextHolder.clearDataSourceType();
			    }
		    }else{
		    	 DataSourceContextHolder.clearDataSourceType();
		    }
			Result result = invoker.invoke(invocation);
			long elapsed = System.currentTimeMillis() - start;
			if (invoker.getUrl() != null) {

				// log.info("[" +invoker.getInterface() +"] [" + invocation.getMethodName() +"] [" + elapsed +"]" );
//				log.info("[{}], [{}], {}, [{}], [{}], [{}]   "+"=="+ invoker.getInterface()+"=="+  invocation.getMethodName()+"=="+  
//	                         Arrays.toString(invocation.getArguments  ())+"=="+  result.getValue()+"=="+ 
//	                       result.getException()+"=="+  elapsed);
	                
			}
			return result;
		}else{
			Result result = invoker.invoke(invocation);
			return result;
		}
	}

}
