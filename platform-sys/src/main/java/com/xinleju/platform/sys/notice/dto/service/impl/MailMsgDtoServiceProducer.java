package com.xinleju.platform.sys.notice.dto.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.notice.dto.service.MailMsgDtoServiceCustomer;
import com.xinleju.platform.sys.notice.entity.MailMsg;
import com.xinleju.platform.sys.notice.service.MailMsgService;
import com.xinleju.platform.sys.notice.util.EmailManager;
import com.xinleju.platform.sys.org.entity.User;
import com.xinleju.platform.sys.org.service.UserService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class MailMsgDtoServiceProducer implements MailMsgDtoServiceCustomer{
	private static Logger log = Logger.getLogger(MailMsgDtoServiceProducer.class);
	@Autowired
	private MailMsgService mailMsgService;
	@Autowired
	private UserService userService;
	/*@Autowired
	protected RedisTemplate<Serializable, Serializable> redisTemplate;*/

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   MailMsg mailMsg=JacksonUtils.fromJson(saveJson, MailMsg.class);
		   mailMsgService.save(mailMsg);
		   info.setResult(JacksonUtils.toJson(mailMsg));
		   info.setSucess(true);
		   info.setMsg("保存对象成功!");
		} catch (Exception e) {
		 log.error("保存对象失败!"+e.getMessage());
		 info.setSucess(false);
		 info.setMsg("保存对象失败!");
		 info.setExceptionMsg(e.getMessage());
		}
	   return JacksonUtils.toJson(info);
	}

	@Override
	public String saveBatch(String userInfo, String saveJsonList)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateBatch(String userInfo, String updateJsonList)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(String userInfo, String updateJson)  {
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   MailMsg mailMsg=JacksonUtils.fromJson(updateJson, MailMsg.class);
			   int result=   mailMsgService.update(mailMsg);
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("更新对象成功!");
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   MailMsg mailMsg=JacksonUtils.fromJson(deleteJson, MailMsg.class);
			   int result= mailMsgService.deleteObjectById(mailMsg.getId());
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("删除对象成功!");
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteAllObjectByIds(String userInfo, String deleteJsonList)
   {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= mailMsgService.deleteAllObjectByIds(list);
				   info.setResult(JacksonUtils.toJson(result));
				   info.setSucess(true);
				   info.setMsg("删除对象成功!");
				}
			} catch (Exception e) {
			 log.error("删除对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String getObjectById(String userInfo, String getJson)
	 {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			MailMsg mailMsg=JacksonUtils.fromJson(getJson, MailMsg.class);
			MailMsg	result = mailMsgService.getObjectById(mailMsg.getId());
			info.setResult(JacksonUtils.toJson(result));
		    info.setSucess(true);
		    info.setMsg("获取对象成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getPage(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=mailMsgService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=mailMsgService.getPage(new HashMap(), null, null);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取分页对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取分页对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryList(String userInfo, String paramater){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List list=mailMsgService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=mailMsgService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getCount(String userInfo, String paramater)  {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String deletePseudoObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   MailMsg mailMsg=JacksonUtils.fromJson(deleteJson, MailMsg.class);
			   int result= mailMsgService.deletePseudoObjectById(mailMsg.getId());
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("删除对象成功!");
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deletePseudoAllObjectByIds(String userInfo, String deleteJsonList)
   {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= mailMsgService.deletePseudoAllObjectByIds(list);
				   info.setResult(JacksonUtils.toJson(result));
				   info.setSucess(true);
				   info.setMsg("删除对象成功!");
				}
			} catch (Exception e) {
			 log.error("删除对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}
	
	/**
	 * 发送邮件
	 * @return
	 */
	@Override
	public String sendMailMsg(String userInfo, String getJson)
	 {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			MailMsg mailMsg=JacksonUtils.fromJson(getJson, MailMsg.class);
			Map<String,Object> map=JacksonUtils.fromJson(getJson, HashMap.class);
			if(mailMsg.getSendAddress() ==null ){
				throw new InvalidCustomException("收件人账号不可为空");
			}
			if(mailMsg.getTitle() ==null ){
				throw new InvalidCustomException("主题不可为空");
			}
			if(mailMsg.getContext() ==null ){
				throw new InvalidCustomException("内容不可为空");
			}
			int c=0;
			boolean sendAgain=false;
			if (mailMsg.getId() == null||StringUtils.isBlank(mailMsg.getId())) {
				mailMsg.setId(IDGenerator.getUUID());
				mailMsg.setStatus("2");//未发送
				//保存消息
				c=mailMsgService.save(mailMsg);
			}else{
				//如果ID存在，说明为邮件重发
				sendAgain=true;
				mailMsg=mailMsgService.getObjectById(mailMsg.getId());
				mailMsg.setStatus("2");//未发送
				c=mailMsgService.update(mailMsg);
			}
			String serverId=null;
			boolean isServer=true;//是否有邮件服务器，存在邮件服务器说明是测试服务器是否可以发送
			if (map.containsKey("serverId")&&StringUtils.isNotBlank((String)map.get("serverId"))) {
				serverId=map.get("serverId").toString();
			}else {
				isServer=false;//没有邮件服务器，不必返回邮件发送状态
			}
			//发送邮件并修改消息状态
			Map<String, Object> mailRes=mailMsgService.sendMailAndUpdateStatus(mailMsg,serverId);
			if (isServer||sendAgain) {
				if (!(Boolean)mailRes.get("flag")) {
					info.setResult(null);
					info.setSucess(false);
					info.setExceptionMsg(mailRes.get("resMsg").toString());
				}else{
					info.setResult(null);
					info.setSucess(true);
					info.setMsg(mailRes.get("resMsg").toString());
				}
			}else{
				if(c>0){
					info.setResult(JacksonUtils.toJson(c));
					info.setSucess(true);
					info.setMsg("发送邮件成功!");
				}else{
					info.setResult(getJson);
					info.setSucess(false);
					info.setExceptionMsg("发送邮件失败!");
				}
			}
		} catch (Exception e) {
			 log.error("发送邮件失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("发送邮件失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	/**
	 * 模糊查询-分页
	 * @return
	 */
	@Override
	public String vaguePage(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=mailMsgService.vaguePage(map);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=mailMsgService.getPage(new HashMap<String,Object>(), null, null);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			 log.error("获取分页对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取分页对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	
}
