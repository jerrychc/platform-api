package com.xinleju.platform.weixin.utils;

import com.xinleju.platform.flow.utils.ConfigurationUtil;

/**
 * @author maguanglei
 * @date   20141031
 * 参数API类*/
public class ParamesAPI {
	// 随机戳
	public static String encodingAESKey = "o2M9lE1bF1WjZIbJ6EZELGFWjxElngfurq7Xyn7eiqz";  
	 //你的企业号ID
     //public static String corpId = "wx3f25bc80e9df8a85";
	 //public static String corpId = PropertiesUtils.getProperty("weixin.corpId");//old-config
	 public static String corpId = ConfigurationUtil.getValue("weixin.corpId");
		
	 // 管理组的凭证密钥，每个secret代表了对应用、通讯录、接口的不同权限；不同的管理组拥有不同的secret
     //	public static String corpsecret = "Ho07aiznGkODc9KosbAWW6EVPRXJq-kfuxl2wU3XMydSxknDe2UJBf_cyo47uqG9";
	 // public static String corpsecret = PropertiesUtils.getProperty("weixin.corpsecret");//old-config
	 public static String corpsecret = ConfigurationUtil.getValue("weixin.corpsecret");
	 
	 // OAuth2 回调地址
	 public static String REDIRECT_URI = "http://10.200.1.4/mobiletodo/weixin/todoMsg/todoMsgInfo.jsp";
	 //移动审批AgentId
	 //public static String mobiletodoAgentId = PropertiesUtils.getProperty("weinxin.mobiletodo.agentid");//old-config
	 public static String mobiletodoAgentId = ConfigurationUtil.getValue("weinxin.mobiletodo.agentid");
}
