package com.xinleju.platform.utils;

import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.sys.sync.service.SyncDataService;


public class SyncDataThread  extends Thread{
	
	private SyncDataService syncDataService;
	
	private String userId = "";
	
	private String tendCode="";
	
	public SyncDataThread (String userId,SyncDataService syncDataService,String tendCode){
		this.userId = userId;
		this.syncDataService = syncDataService;
		this.tendCode = tendCode;
	}
	public void run() {  
		try{
			//同步数据到第三方系统
			MessageResult resultSync = syncDataService.syncDataOne(userId,tendCode);
		}catch(Exception e){
		}
		 
	}  
}