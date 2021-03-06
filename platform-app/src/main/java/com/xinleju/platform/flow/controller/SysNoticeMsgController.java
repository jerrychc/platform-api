package com.xinleju.platform.flow.controller;


import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.erp.flow.frameapi.domain.User;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserDto;
import com.xinleju.platform.encrypt.EndecryptUtil;
import com.xinleju.platform.flow.dto.SysNoticeMsgDto;
import com.xinleju.platform.flow.dto.SysNoticeMsgStatDto;
import com.xinleju.platform.flow.dto.service.SysNoticeMsgDtoServiceCustomer;
import com.xinleju.platform.out.app.org.service.UserOutServiceCustomer;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.sys.org.dto.service.UserDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.FileUtils;
import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.platform.uitls.PageQueryAcrossDbUtil;

import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.EmailMessageSchema;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.FindItemsResults;
import microsoft.exchange.webservices.data.Folder;
import microsoft.exchange.webservices.data.Item;
import microsoft.exchange.webservices.data.ItemView;
import microsoft.exchange.webservices.data.SearchFilter;
import microsoft.exchange.webservices.data.WebCredentials;
import microsoft.exchange.webservices.data.WellKnownFolderName;


/**
 * 系统通知消息控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/flow/sysNoticeMsg")
public class SysNoticeMsgController {

	private static Logger log = Logger.getLogger(SysNoticeMsgController.class);

	@Autowired
	private SysNoticeMsgDtoServiceCustomer sysNoticeMsgDtoServiceCustomer;

	@Autowired
	private UserOutServiceCustomer userOutServiceCustomer;
	@Autowired
	private UserDtoServiceCustomer userDtoServiceCustomer;
	@Autowired
	protected RedisTemplate<String, String> redisTemplate;

	/**
	 * 根据Id获取业务对象
	 *
	 * @param id  业务对象主键
	 *
	 * @return     业务对象
	 */
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult get(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			System.out.println("\n\n get()>> userJson="+userJson);
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto=JacksonUtils.fromJson(resultInfo, SysNoticeMsgDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用get方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}


	/**
	 * 返回分页对象
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);

		    String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.getPage(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用page方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	/**
	 * 返回符合条件的列表
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/queryList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(userBeanInfo!=null && userBeanInfo.getSecurityUserDto()!=null){
				String userId = userBeanInfo.getSecurityUserDto().getId();
				//String loginName = userBeanInfo.getSecurityUserDto().getLoginName();
				map.put("userId", userId);
				paramaterJson = JacksonUtils.toJson(map);
				System.out.println("\n queryHaveDoneList >>>userId="+userId);
			}else{
				System.out.println("\n queryList >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysNoticeMsgDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysNoticeMsgDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }

		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 修改消息的状态
	 * @param map [newStatus, oldStatus,  id,  businessId、extendInfo]
	 * @return
	 */
	@RequestMapping(value="/updateStatusOfNoticeMsg",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult updateStatusOfNoticeMsg(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			map.put("currentUserId", userBeanInfo.getSecurityUserDto().getId());
			paramaterJson = JacksonUtils.toJson(map);
			System.out.println("------------------------- updateStatusOfNoticeMsg paramaterJson="+paramaterJson);

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.updateStatusOfNoticeMsg(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }

		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 设置待办消息为打开过状态
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value="/setMessageOpened",method={RequestMethod.GET}, consumes="application/json")
	public @ResponseBody MessageResult setMessageOpened(String messageId){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.setMessageOpened(userJson, messageId);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }

		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用setMessageOpened方法:  【参数"+messageId+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 返回符合条件的列表
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/queryDBDYList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryDBDYList(@RequestBody Map<String,Object> map){
		long time1 = System.currentTimeMillis();
		System.out.println("\n Controller queryDBDYList >> time1="+time1);


		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(userBeanInfo!=null && userBeanInfo.getSecurityUserDto()!=null){
				String userId = userBeanInfo.getSecurityUserDto().getId();
				//String loginName = userBeanInfo.getSecurityUserDto().getLoginName();
				map.put("userId", userId);
				paramaterJson = JacksonUtils.toJson(map);
				System.out.println("\n queryHaveDoneList >>>userId="+userId);
			}else{
				System.out.println("\n queryHaveDoneList >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.queryDBDYList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysNoticeMsgDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysNoticeMsgDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }

		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		long time2 = System.currentTimeMillis();
		System.out.println("\n Controller queryDBDYList >> time2="+time2 );
		System.out.println("\n Controller queryDBDYList >> time2-time1="+(time2-time1) );
		return result;
	}

	/**
	 * 返回符合条件的列表
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/queryHaveDoneList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryHaveDoneList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(userBeanInfo!=null && userBeanInfo.getSecurityUserDto()!=null){
				String userId = userBeanInfo.getSecurityUserDto().getId();
				//String loginName = userBeanInfo.getSecurityUserDto().getLoginName();
				map.put("userId", userId);
				paramaterJson = JacksonUtils.toJson(map);
				System.out.println("\n queryHaveDoneList >>>userId="+userId);
			}else{
				System.out.println("\n queryHaveDoneList >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.queryHaveDoneList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysNoticeMsgDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysNoticeMsgDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }

		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}



	/**
	 * 返回待办和待阅的汇总数据
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/queryTwoSumData",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryTwoSumData(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);

			if(userBeanInfo!=null && userBeanInfo.getSecurityUserDto()!=null){
				String userId = userBeanInfo.getSecurityUserDto().getId();
				String loginName = userBeanInfo.getSecurityUserDto().getLoginName();
				map.put("loginName", loginName);
				paramaterJson = JacksonUtils.toJson(map);
				System.out.println("\n queryHaveDoneList >>>userId="+userId);
			}else{
				System.out.println("\n queryStatiscData >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}
			List<String> lines = FileUtils.readLines("/msg-sync-path.properties");
			List<String> syncTendCodes = getSyncTendCode(lines, userBeanInfo);
			List<SysNoticeMsgDto> list  = PageQueryAcrossDbUtil.getMsgMoreSumData(sysNoticeMsgDtoServiceCustomer,
					userOutServiceCustomer, syncTendCodes, userBeanInfo, map);
			result.setSuccess(true);
			result.setResult(list);

			/*String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.queryTwoSumData(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysNoticeMsgDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysNoticeMsgDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }*/

		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}


	/**
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody SysNoticeMsgDto t){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);

			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.save(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto=JacksonUtils.fromJson(resultInfo, SysNoticeMsgDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
		} catch (Exception e) {
			try {
				////e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				////e1.printStackTrace();
			}

		}
		return result;
	}

	/**
	 * 删除实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.deleteObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto=JacksonUtils.fromJson(resultInfo, SysNoticeMsgDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用delete方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}

		return result;
	}


	/**
	 * 删除实体对象
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deleteBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.deleteAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto=JacksonUtils.fromJson(resultInfo, SysNoticeMsgDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用delete方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}

		return result;
	}

	/**
	 * 修改修改实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		SysNoticeMsgDto msgDto=null;
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=sysNoticeMsgDtoServiceCustomer.update(null, updateJson);
				 DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
				 if(updateDubboServiceResultInfo.isSucess()){
					 Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
					 result.setResult(i);
					 result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
					 result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
				 }else{
					 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
					 result.setMsg(updateDubboServiceResultInfo.getMsg()+"【"+updateDubboServiceResultInfo.getExceptionMsg()+"】");
				 }
			}else{
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
			}
		} catch (Exception e) {
			try{
			 ////e.printStackTrace();
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(msgDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
			}catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				////e1.printStackTrace();
			}

		}
		return result;
	}



	/**
	 * 伪删除实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.deletePseudoObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto=JacksonUtils.fromJson(resultInfo, SysNoticeMsgDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用deletePseudo方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}

		return result;
	}


	/**
	 * 伪删除实体对象
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deletePseudoBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.deletePseudoAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto=JacksonUtils.fromJson(resultInfo, SysNoticeMsgDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用deletePseudoBatch方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}

		return result;
	}
	
	/**
	  * @Description:删除任务并添加日志记录
	  * @author:zhangfangzhi
	  * @date 2017年12月21日 上午10:01:08
	  * @version V1.0
	 */
	@RequestMapping(value="/deletePseudoBatchAndRecord/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudoBatchAndRecord(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.deletePseudoBatchAndRecord(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用deletePseudoBatch方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}

		return result;
	}

	/**
	 * 根据关键字模糊查询当前用户的相关消息
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/searchDataByKeyword",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult searchDataByKeyword(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(userBeanInfo!=null && userBeanInfo.getSecurityUserDto()!=null&&Objects.equals (map.get ("more"),true)){
					//如用户名为中包含单引号，转义为\'
					String loginName = userBeanInfo.getSecurityUserDto().getLoginName().replaceAll("\'","\\\\'");



					map.put("loginName", loginName);
					paramaterJson = JacksonUtils.toJson(map);
					System.out.println("\n queryHaveDoneList >>>userId="+loginName);
			}else{
				System.out.println("\n queryStatiscData >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}
			if(map.get("receiverId")!=null && !"".equals((String)map.get("receiverId"))){
				List<String> receiverIdList=Arrays.asList(map.get("receiverId").toString().split(","));
				map.put("receiverIdList", receiverIdList);
			}
			List<String> lines = FileUtils.readLines("/msg-sync-path.properties");
			List<String> syncTendCodes = getSyncTendCode(lines, userBeanInfo);
			PageBeanInfo<SysNoticeMsgDto> msgMorePage = PageQueryAcrossDbUtil.getMsgMorePage(sysNoticeMsgDtoServiceCustomer,
					userOutServiceCustomer, syncTendCodes, userBeanInfo, map);
			result.setSuccess(true);
			result.setResult(msgMorePage);

		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}



	/**
	 * 根据 businessId、userId删除对应的opType、
	 * @param map [ opType,  businessId、userId]
	 * @return
	 */
	@RequestMapping(value="/deleteOpTypeDataByParamMap",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult deleteOpTypeDataByParamMap(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.deleteOpTypeDataByParamMap(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }

		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}



	/**
	 * 修改消息的状态
	 * @param map [newStatus, oldStatus,  id,  businessId、extendInfo]
	 * @return
	 */
	@RequestMapping(value="/updateStatusOfNoticeMsgByCurrentUser",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult updateStatusOfNoticeMsgByCurrentUser(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			map.put("userId", userBeanInfo.getSecurityUserDto().getId());
            if(Objects.equals (userBeanInfo.getSecurityUserDto ().getType (),"2")){
                //管理员可以操作所有
                map.put("userId","-1");
            }
            map.put("operatorId", userBeanInfo.getSecurityUserDto().getId());
            map.put("operatorName", userBeanInfo.getSecurityUserDto().getRealName());
			paramaterJson = JacksonUtils.toJson(map);
			System.out.println("------------------------- updateStatusOfNoticeMsgByCurrentUser paramaterJson="+paramaterJson);

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.updateStatusOfNoticeMsgByCurrentUser(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }

		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	  * @Description:批量修改消息状态（置为已办）
	  * @author:zhangfangzhi
	  * @date 2017年12月15日 上午9:23:30
	  * @version V1.0
	 */
	@RequestMapping(value="/updateStatusOfNoticeMsgBatch",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult updateStatusOfNoticeMsgBatch(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			map.put("userId", userBeanInfo.getSecurityUserDto().getId());
            if(Objects.equals (userBeanInfo.getSecurityUserDto ().getType (),"2")){
                //管理员可以操作所有
                map.put("userId","-1");
            }
			paramaterJson = JacksonUtils.toJson(map);
			System.out.println("------------------------- updateStatusOfNoticeMsgByCurrentUser paramaterJson="+paramaterJson);

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.updateStatusOfNoticeMsgBatch(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }

		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 查询当前用户的各类主要的消息统计数据
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/queryTotalStatData",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryTotalStatData(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(userBeanInfo!=null && userBeanInfo.getSecurityUserDto()!=null){
				String userId = userBeanInfo.getSecurityUserDto().getId();
				String loginName = userBeanInfo.getSecurityUserDto().getLoginName();
				map.put("loginName", loginName);
				paramaterJson = JacksonUtils.toJson(map);
				System.out.println("\n queryHaveDoneList >>>userId="+userId);
			}else{
				System.out.println("\n queryHaveDoneList >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}
			List<String> lines = FileUtils.readLines("/msg-sync-path.properties");
			List<String> syncTendCodes = getSyncTendCode(lines, userBeanInfo);
			SysNoticeMsgDto msgDto  = PageQueryAcrossDbUtil.getMsgMoreTotalData(sysNoticeMsgDtoServiceCustomer,
					userOutServiceCustomer, syncTendCodes, userBeanInfo, map);
			result.setSuccess(true);
			result.setResult(msgDto);
			/*String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.queryTotalStatData(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto = JacksonUtils.fromJson(resultInfo, SysNoticeMsgDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }*/

		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 查询当前用户的各类消息的一级分类的汇总数据
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/queryFirstTypeStatData",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryFirstTypeStatData(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(userBeanInfo!=null && userBeanInfo.getSecurityUserDto()!=null){
				String userId = userBeanInfo.getSecurityUserDto().getId();
				String loginName = userBeanInfo.getSecurityUserDto().getLoginName().replaceAll("\'","\\\\'");
				map.put("userId", userId);
				map.put("loginName", loginName);
				paramaterJson = JacksonUtils.toJson(map);
				System.out.println("\n queryHaveDoneList >>>userId="+userId);
			}else{
				System.out.println("\n queryHaveDoneList >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}

			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.queryFirstTypeStatData(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgStatDto msgDto = JacksonUtils.fromJson(resultInfo, SysNoticeMsgStatDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }

		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 给微信平台发送消息
	 * @param
	 * @return
	 */
	@RequestMapping(value="/sendWeixinMsg",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult sendWeixinMsg(){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String updateJson = "";
			String dubboResultInfo = sysNoticeMsgDtoServiceCustomer.sendWeixinMsg(userJson, updateJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
		} catch (Exception e) {
			try {
				log.error("调用sendWeixinMsg()方法 失败, e="+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return result;
	}

	/**
	 * 首页代办列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getMsgListForPortal", method = RequestMethod.GET,produces = "text/html; charset=utf-8")
	@ResponseBody
	public String getMsgListForPortal(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		String html = "";
		StringBuffer buffer = new StringBuffer();

		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		SecurityUserDto userDto = userBeanInfo.getSecurityUserDto();

		int start = 0;
		int limit = 5;
		try {
			List<String> lines = FileUtils.readLines("/msg-sync-path.properties");
			List<String> syncTendCodes = getSyncTendCode(lines, userBeanInfo);
//			PageBeanInfo<SysNoticeMsgDto> dbMsgPage = PageQueryAcrossDbUtil.getMsgPage(sysNoticeMsgDtoServiceCustomer, userOutServiceCustomer, syncTendCodes, userBeanInfo, start, limit, "DB","0");
			PageBeanInfo<SysNoticeMsgDto> dyMsgPage = PageQueryAcrossDbUtil.getMsgPage(sysNoticeMsgDtoServiceCustomer, userOutServiceCustomer, syncTendCodes, userBeanInfo, start, limit, "DY","1");
//			PageBeanInfo<SysNoticeMsgDto> ybMsgPage = PageQueryAcrossDbUtil.getMsgPage(sysNoticeMsgDtoServiceCustomer, userOutServiceCustomer, syncTendCodes, userBeanInfo, start, limit, "YB","1");
//			PageBeanInfo<SysNoticeMsgDto> fqMsgPage = PageQueryAcrossDbUtil.getMsgPage(sysNoticeMsgDtoServiceCustomer, userOutServiceCustomer, syncTendCodes, userBeanInfo, start, limit, "FQ","1");

			//获取用户邮件列表
			Map<String,Object> emailMap = getXyreEmailInfo(userDto.getId(),userDto.getEmail(),userDto.getEmailPwd());
			int emailTotal = emailMap!=null&&emailMap.get("unReadCount")!=null?(Integer) emailMap.get("unReadCount"):0;
			List<Map<String,Object>> emailList = emailMap!=null&&emailMap.get("emailInfoList")!=null?(List<Map<String, Object>>) emailMap.get("emailInfoList"):new ArrayList<Map<String,Object>>();

			buffer.append("<div>\n");
			buffer.append("<ul class=\"nav nav-tabs\" role=\"tablist\" id=\"portalMyTaskTab\">\n");
			buffer.append("<li role=\"presentation\" class=\"active\">\n" +
					"            <a id=\"toApprove_A\" href=\"#toApprove\" aria-controls=\"toApprove\" role=\"tab\" data-toggle=\"tab\" title=\"待审(pending approval)\">待审()</a>\n" +
					"        </li>\n");
			buffer.append("<li role=\"presentation\">\n" +
					"            <a id=\"toRead_A\" href=\"#toRead\" aria-controls=\"toRead\" role=\"tab\" data-toggle=\"tab\" title=\"待阅(unread)\">待阅("+dyMsgPage.getTotal()+")</a>\n" +
					"        </li>\n");
			buffer.append("<li role=\"presentation\">\n" +
					"            <a id=\"haveDone_A\" href=\"#haveDone\" aria-controls=\"haveDone\" role=\"tab\" data-toggle=\"tab\" title=\"已办(approved)\">已办</a>\n" +
					"        </li>\n");
			buffer.append("<li role=\"presentation\">\n" +
					"            <a id=\"myStart_A\" href=\"#myStart\" aria-controls=\"myStart\" role=\"tab\" data-toggle=\"tab\" title=\"我的发起(my submission)\">我的发起</a>\n" +
					"        </li>\n");
			buffer.append("<li role=\"presentation\">\n" +
					"            <a id=\"email_A\" href=\"#myEmailDiv\" aria-controls=\"email\" role=\"tab\" data-toggle=\"tab\" title=\"邮箱(e_mail)\">邮箱("+emailTotal+")</a>\n" +
					"        </li>\n");
			buffer.append("<li class=\"pull-right\">\n" +
					"            <a href=\"javascript:void(0);\" id=\"sortMsgListBtn\" style=\"display:inline-block;\" title=\"排序\"><i class=\"glyphicon glyphicon-up\"></i></a>\n" +
					"            <a href=\""+contextPath+"/flow/runtime/mytask/task_list.html?dataType=DB\" target=\"_blank\" id=\"moreMsgListBtn\" style=\"display:inline-block;\" title=\"更多\"><i class=\"glyphicon\"></i></a>\n" +
					"        </li>\n");
			buffer.append("</ul>\n");


			buffer.append("<div class=\"tab-content\">\n");
			//待办
			buffer.append("\t<div role=\"tabpanel\" class=\"tab-pane active\" id=\"toApprove\">\n");
			buffer.append("\t\t<ul class=\"tab_list\" id=\"toApproveDataList\">\n");
			//getLiHtml(buffer,contextPath, dbMsgPage.getList(), "DB");
			buffer.append("\t\t</ul>");
			buffer.append("\t</div>");

			//待阅
			buffer.append("\t<div role=\"tabpanel\" class=\"tab-pane \" id=\"toRead\">\n");
			buffer.append("\t\t<ul class=\"tab_list\" id=\"toReadDataList\">\n");
			//getLiHtml(buffer,contextPath,dyMsgPage.getList(),"DY");
			buffer.append("\t\t</ul>");
			buffer.append("\t</div>");

			//已办/已阅
			buffer.append("\t<div role=\"tabpanel\" class=\"tab-pane \" id=\"haveDone\">\n");
			buffer.append("\t\t<ul class=\"tab_list\" id=\"haveDoneDataList\">\n");
			//getLiHtml(buffer,contextPath,ybMsgPage.getList(),"haveDone");
			buffer.append("\t\t</ul>");
			buffer.append("\t</div>");

			//我的发起
			buffer.append("\t<div role=\"tabpanel\" class=\"tab-pane \" id=\"myStart\">\n");
			buffer.append("\t\t<ul class=\"tab_list\" id=\"myStartDataList\"> \n");
			//getLiHtml(buffer,contextPath,fqMsgPage.getList(),"myStart");
			buffer.append("\t\t</ul>");
			buffer.append("\t</div>");

			//邮件
			buffer.append("\t<div role=\"tabpanel\" class=\"tab-pane \" id=\"myEmailDiv\">\n");
			buffer.append("\t\t<ul class=\"tab_list\" id=\"myEmailList\">\n");
			getLiHtmlForEmail(buffer,emailList);
			buffer.append("\t\t</ul>");
			buffer.append("\t</div>");
			buffer.append("</div>");

			buffer.append("</div>\n");

			buffer.append("<script type=\"text/javascript\">\n");
			buffer.append("function refreshMyTaskData(){\n" +
					"    switchTaskTab('toApprove');\n" +
					"    switchTaskTab('toRead');\n" +
					"    switchTaskTab('haveDone');\n" +
					"    switchTaskTab('myStart');\n" +
					"    getEmailInfo();\n" +
					/*"	  var taskTabId = $('#flowTask').find('li.active>a').attr(\"id\");\n" +
					"    if (taskTabId == 'toApprove') {\n" +
					"        $('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=DB');\n" +
					"    } else if (taskTabId == 'toRead') {\n" +
					"        $('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=DY');\n" +
					"    } else if (taskTabId == 'haveDone') {\n" +
					"        $('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=HAVE_DONE');\n" +
					"    } else if (taskTabId == 'myStart') {\n" +
					"        $('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=FQ');\n" +
					"    } else if(taskTabId == 'email_A'){\n" +
					"        $('#moreMsgListBtn').hide();\n" +
					"    }" +*/
					"}");
			buffer.append("\t$(function(){\n" +
					//"debugger;\n"+
					"\t\t$('#sortMsgListBtn').on('click',function(){" +
					"\t\t\tvar upFlag = $(this).find('i').hasClass('glyphicon-up');\n" +
					"\t\t\tvar that = $(this) ;\n" +
					"\t\t\tvar sort ;\n" +
					"\t\t\tif(upFlag){\n" +
					"\t\t\tsort = '1';\n"+
					"\t\t\t}else{\n" +
					"\t\t\tsort = '0';\n"+
					"\t\t\t}\n" +
					"\t$.ajax({\n"+
					"\t\t\ttype: 'get',\n"+
					"\t\t\turl: hostUrl + 'flow/sysNoticeMsg/setNoticMsgSort?sortType=' + $('#portalMyTaskTab li.active a').attr('aria-controls')+'&sort=' + sort+'&time='+Math.random(),\n"+
					"\t\t\tdataType: 'json',\n"+
					"\t\t\tasync : false,\n"+
					"\t\tsuccess: function (contentObj) {\n"+
					"\t\t\tif(contentObj.success){\n"+
					"\t\t\t\t//sort = contentObj.result;\n"+
					"\t\t\t$('#portalMyTaskTab li.active a').click()\n" +
					"\t\t\tif(upFlag){\n" +
					"\t\t\t\tthat.find('i').removeClass('glyphicon-up').addClass('glyphicon-down');\n" +
					"\t\t\t}else{\n" +
					"\t\t\t\tthat.find('i').removeClass('glyphicon-down').addClass('glyphicon-up');\n" +
					"\t\t\t}\n" +
					"\t\t\t}\n"+
					"\t\t},\n"+
					"\t\terror:function (xhr) {\n"+
					"\t\t\t$.xljUtils.tip('red', '设置用户排序失败！');\n" +
					"\t\t}\n"+
					"\t});\n"+
					"\t\t});\n" +
					"\t\t$('#toApprove_A').on('click',function(){\n" +
					"\t\t\tswitchTaskTab('toApprove');\n" +
					"\t\t});\n" +
					"\t\t$('#toRead_A').on('click',function(){\n" +
					"\t\t\tswitchTaskTab('toRead');\n" +
					"});\n" +
					"\t\t$('#haveDone_A').on('click',function(){\n" +
					"\t\t\tswitchTaskTab('haveDone');\n" +
					"\t\t});\n" +
					"\t\t$('#myStart_A').on('click',function(){\n" +
					"\t\t\tswitchTaskTab('myStart');\n" +
					"\t\t});\n" +
					"\t\t$('#email_A').on('click',function(){" +
					"\t\t\t$('#moreMsgListBtn').hide();" +
					"\t\t\t$('#sortMsgListBtn').hide();" +
					"\t\t\tgetEmailInfo();" +
					"\t\t});\n" +
					"\t\t$('#toApprove>ul>li>a').each(function(index,ele){$(ele).attr('href',encodeURI($(ele).attr('href')))});" +
					"\t\t$('#portalMyTaskTab li.active a').click()\n"+
					"\t});\n");
			buffer.append("getEmailInfo();\n");
			buffer.append("function switchTaskTab(tabType) {\n" +
					"    $('#moreMsgListBtn').show();\n" +
					"    $('#sortMsgListBtn').show();\n" +
//					"    var opType = [];\n" +
					"    var opType = '';\n" +
					"    //['DB','DY','YB','YY','FQ']\n" +
					"    if(tabType=='toApprove'){\n" +
//					"        opType.push('DB');\n" +
					"        opType = 'DB';\n" +
					"    }else if(tabType=='toRead') {\n" +
//					"        opType.push('DY');\n" +
					"        opType = 'DY';\n" +
					"    }else if(tabType=='haveDone') {\n" +
//					"        opType.push('YB');\n" +
//					"        opType.push('YY');\n" +
					"        opType = 'YB';\n" +
					"    }else if(tabType=='myStart') {\n" +
//					"        opType.push('FQ');\n" +
					"        opType = 'FQ';\n" +
					"    }\n" +
					" var sort ;\n" +
					//"debugger;\n" +
					"$.ajax({\n"+
						"type: 'get',\n"+
						"url: hostUrl + 'flow/sysNoticeMsg/getNoticMsgSort?sortType=' + tabType+'&time='+Math.random(),\n"+
						"dataType: 'json',\n"+
						"async : false,\n"+
						"success: function (contentObj) {\n"+
							"if(contentObj.success){\n"+
								"sort = contentObj.result;\n"+
							"}\n"+
						"},\n"+
						"error:function (xhr) {\n"+
							"sort = '1';\n"+
						"}\n"+
					"});\n"+
					"    var paramData = {\n" +
					"        start:0,\n" +
					"        limit:5,\n" +
					"        opType:opType,\n" +
					"        sord:sort\n" +
					"    };\n" +
					"	 if(tabType=='haveDone'){paramData.orderByField='deal_date'} \n" +
					"    $.ajax({ //发送更新的ajax请求\n" +
					"        type: \"POST\",\n" +
					"        url: hostUrl + \"flow/sysNoticeMsg/querySysNoticeMsgByPage?_time=\"+new Date().getTime(),\n" +
					"        dataType: \"JSON\",\n" +
					"        data: JSON.stringify( paramData ),\n" +
					"        contentType: 'application/json;charset=utf-8', //设置请求头信息\n" +
					"        success: function (data) {\n" +
					"            if(data.success) {\n" +
					"                var result = data.result;\n" +
					"                var total = result.total;\n" +
					"                var list = result.list;\n" +
					"                var ulObj = $('<ul></ul>');\n" +
					"                for (var i = 0; i < list.length; i++) {\n" +
					"                    var obj = list[i];\n" +
					"                    var hourSum = obj.hourSum;\n" +
					"                    var hourSumTxt = hourSum + \"小时\";\n" +
					/*"                    if(hourSum>24){\n" +
					"                        var daySum = (hourSum / 24);\n" +
					"                        var leftHour = hourSum % 24;\n" +
					"                        if(leftHour>0){\n" +
					"                            hourSumTxt = daySum+\"天\"+leftHour+\"小时\";\n" +
					"                        }else{\n" +
					"                            hourSumTxt = daySum+\"天\";\n" +
					"                        }\n" +
					"                    }\n" +*/
					"                    hourSumTxt = \"停留\"+hourSumTxt;\n" +
					"                    var url = obj.url;  \n" +
					"                    if (tabType=='toApprove') {\n" +
					"						if(url && url.indexOf(\"http\")==0){\n"+
                    "							url = url+\"&time=\"+Math.random();\n"+
                    "						} else {\n"+
                    "							url = hostUrl+url+\"&_t=\"+new Date().getTime();\n"+
                    "						}\n"+
					"                    } else if (tabType=='toRead') {\n" +
					"						if(url && url.indexOf(\"http\")==0){\n"+
		            "							url = url+\"&firstTime=YES&time=\"+Math.random();\n"+
		            "						} else {\n"+
		            "							url = hostUrl+url+\"&firstTime=YES&_t=\"+new Date().getTime();\n"+
		            "						}\n"+
					"                    } else if (tabType=='haveDone'||tabType=='myStart') {\n" +
					"						if(url && url.indexOf(\"http\")==0){\n"+
		            "							url = url+\"&action=view&time=\"+Math.random();\n"+
		            "						} else {\n"+
		            "							url = hostUrl+url+\"&action=view&_t=\"+new Date().getTime();\n"+
		            "						}\n"+
					"                    }\n" +
					"\n" +
					"                    var liObj = $('<li></li>');\n" +
					"                    var delaySpanObj = $('<span class=\"t_delay\"></span>');\n" +
					"                    delaySpanObj.text(hourSumTxt);\n" +
					"                    delaySpanObj.attr('title',hourSumTxt);\n" +
					"						delaySpanObj.dotdotdot();\n" +
					"                    var aObj = $('<a class=\"t_content\" href=\"javaScript:void(0);\"></a>');\n" +
					"                    aObj.attr('onclick','checkLogin(\"\'+url+\'\");');\n" +
					"                    aObj.attr('title',$.xljUtils.htmlDecode(obj.title));\n" +
					"                   // aObj.text($.xljUtils.htmlDecode(obj.title));\n" +
					"					if(tabType=='myStart'){ \n" +
					"						aObj.text($.xljUtils.htmlDecode(obj.title+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 当前审批人('+obj.approver+')'));		\n" +
					"					}else { \n" +
					"						aObj.text($.xljUtils.htmlDecode(obj.title)); \n" +
					"					}	" +
					"\n" +
					"                    if(\"DB\"==opType||\"DY\"==opType){\n" +
					"                        liObj.append(delaySpanObj);\n" +
					"                    }\n" +
					"\n" +




					"                    liObj.append(aObj);\n" +
					"						if(obj.operationTip&&obj.operationTip!=''&&obj.operationTip.indexOf('{')==0){			 \n" +
					"							var xbInfo = JSON.parse(obj.operationTip);                                         \n" +
					"							if(xbInfo.remainder>0){                                     \n " +
					"								liObj.append('<img class=\"xb\" title=\"已回复人员：'+xbInfo.replyNames+'\\n未回复人员：'+xbInfo.remainderNames+'\" src=\"../../common/img/xb_no.png\"></img'); \n " +
					"							}else{                                                                  \n" +
					"								liObj.append('<img class=\"xb\" title=\"已回复人员：'+xbInfo.replyNames+'\\n未回复人员：'+xbInfo.remainderNames+'\" src=\"../../common/img/xb_yes.png\"></img'); \n" +
					"							}	                                                                         \n" +
					"						}                                                                               \n" +
					"                    ulObj.append(liObj);\n" +
					"\n" +
					"                }\n" +
					"                    if(tabType=='toApprove'){\n" +
					"                        $('#toApprove_A').text('待审('+total+')');\n" +
					"                        $('#moreMsgListBtn').attr('href', encodeURI(hostUrl+'flow/runtime/mytask/task_list.html?dataType=DB'));\n" +
					"                        $('#toApproveDataList').empty();\n" +
					"                        $('#toApproveDataList').append(ulObj.html());\n" +
					"\n" +
					"                    }else if(tabType=='toRead') {\n" +
					"                        $('#toRead_A').text('待阅('+total+')');\n" +
					"                        $('#moreMsgListBtn').attr('href', encodeURI(hostUrl+'flow/runtime/mytask/task_list.html?dataType=DY'));\n" +
					"                        $('#toReadDataList').empty();\n" +
					"                        $('#toReadDataList').append(ulObj.html());\n" +
					"                    }else if(tabType=='haveDone') {\n" +
					"                        $('#moreMsgListBtn').attr('href', encodeURI(hostUrl+'flow/runtime/mytask/task_list.html?dataType=HAVE_DONE'));\n" +
					"                        $('#haveDoneDataList').empty();\n" +
					"                        $('#haveDoneDataList').append(ulObj.html());\n" +
					"                    }else if(tabType=='myStart') {\n" +
					"                        $('#moreMsgListBtn').attr('href', encodeURI(hostUrl+'flow/runtime/mytask/task_list.html?dataType=FQ'));\n" +
					"                        $('#myStartDataList').empty();\n" +
					"                        $('#myStartDataList').append(ulObj.html());\n" +
					"                    }\n" +
					"						//计算待办列表宽度\n" +
					"            		$('#portalMyTaskTab').parents('div.groupnews_container').find('ul.tab_list li').each(function (i,liObj) {\n" +
					"                		var spanObj = $(liObj).find('span.t_delay');\n" +
					"                		var aObj = $(liObj).find('a.t_content');\n" +
					"						var aWidth = $(liObj).find('a').width();\n" +
					"						var spanWidth = spanObj.outerWidth();\n" +
					"                		var imgWidth = $(liObj).find('img.xb').outerWidth();" +
					"                		imgWidth = imgWidth?imgWidth:0;" +
					"                		$(liObj).find('a').css({'display':'inline'});\n" +
					"                		var aWidth = $(liObj).find('a').width();\n" +
					"						var liWidth = $(liObj).width();\n" +
					"                		if(aWidth>(liWidth-spanWidth-imgWidth)){\n" +
					"                    	$(liObj).find('a').css({width:(liWidth-spanWidth-imgWidth-10)+'px'});\n" +
					"                		}\n" +
					"                		$(liObj).find('a').css({'display':'inline'});\n" +
					"                		$(liObj).find('a').dotdotdot();\n" +
					"            		});\n"	+
					"						//默认选择的tab更多链接\n" +
					"                		var taskTabId = $('#portalMyTaskTab').find('li.active a').attr(\"id\");\n" +
					"                		if (taskTabId == 'toApprove_A') {\n" +
					"                    		$('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=DB');\n" +
					"                		} else if (taskTabId == 'toRead_A') {\n" +
					"                    		$('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=DY');\n" +
					"                		} else if (taskTabId == 'haveDone_A') {\n" +
					"                    		$('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=HAVE_DONE');\n" +
					"                		} else if (taskTabId == 'myStart_A') {\n" +
					"                    		$('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=FQ');\n" +
					"                		} else if(taskTabId == 'email_A'){\n" +
					"                    		$('#moreMsgListBtn').hide();\n" +
					"                    		$('#sortMsgListBtn').hide();\n" +
					"                		}\n" +
										"if('1'==sort){\n"+
										"\t\t\t\t$('#sortMsgListBtn').find('i').removeClass('glyphicon-up').addClass('glyphicon-down');\n" +
										"}else{\n"+
										"\t\t\t\t$('#sortMsgListBtn').find('i').removeClass('glyphicon-down').addClass('glyphicon-up');\n" +
										"}\n"+
					"            }\n" +
					"        },\n" +
					"        error: function (data) {\n" +
					"            if (data.msg) {\n" +
					"                $.xljUtils.tip('red', data.msg);\n" +
					"            } else {\n" +
					"                $.xljUtils.tip('red', \"查询任务列表数据失败！\");\n" +
					"            }\n" +
					"        }\n" +
					"    });\n" +
					"}\n");
			buffer.append("function getEmailInfo(){\n" +
					"    $.ajax({\n" +
					"        type: 'GET',\n" +
					"        url: hostUrl + 'flow/sysNoticeMsg/getUserEmail?_time='+new Date().getTime(),\n" +
					"        dataType:'JSON',\n" +
					"        success: function (resultData) {\n" +
					"            if(resultData.success){\n" +
					"\n" +
					"                $('#myEmailList').empty();\n" +
					"                var emailMap = resultData.result;\n" +
					"                var unReadCount = emailMap.unReadCount;\n" +
					"                $('#email_A').text('邮箱('+unReadCount+')');\n" +
					"\n" +
					"                var emailList = emailMap.emailInfoList;\n" +
					"                var outlook = hostUrl+'outlook_mail.html';\n" +
					"                var ulObj = $('<ul></ul>');\n" +
					"                for (var i=0;i<emailList.length;i++){\n" +
					"                    var liObj = $('<li></li>');\n" +
					"                    var titleSpan = $('<a href=\"'+outlook+'\" target=\"_blank\" class=\" email_title \"></a>');\n" +
//					"                    var titleSpan = $('<span class=\"email_title\"></span>');\n" +
					"                    titleSpan.text(emailList[i].emailSubject);\n" +
					"                    titleSpan.attr('title',emailList[i].emailSubject);\n" +
					"					\n" +
					"                    var sendUserSpan = $('<span class=\"email_author\"></span>');\n" +
					"                    sendUserSpan.text(emailList[i].sendUser);\n" +
					"                    sendUserSpan.attr('title',emailList[i].sendUser);\n" +
					"\n" +
					"                    var sendTimeSpan = $('<span class=\"email_time\"></span>');\n" +
					"                    sendTimeSpan.text(emailList[i].emailSendTime);\n" +
					"                    sendTimeSpan.attr('title',emailList[i].emailSendTime);\n" +
					"\n" +
					"                    liObj.append(sendTimeSpan);\n" +
					"                    liObj.append(sendUserSpan);\n" +
					"                    liObj.append(titleSpan);\n" +
					"                    ulObj.append(liObj);\n" +
					"                }\n" +
					"                $('#myEmailList').append(ulObj.html());\n" +
					"					$('#myEmailList').find('li').each(function (i,liObj) {\n" +
					//"						debugger;\n" +
					"                		$(liObj).find('a').css({width:($(liObj).width()-170)+'px'});\n" +
					"            		});\n" +
					"            }\n" +
					"        },\n" +
					"        error:function (xhr) {\n" +
					"            //console.info(xhr);\n" +
					"        }\n" +
					"    });\n" +
					"}\n");
			buffer.append("$('#email_A').dblclick(function(){\n" +
					"		window.open(hostUrl+'outlook_mail.html');\n" +
					"	});\n" );
			buffer.append("function openOutlook(){\n"
					+ "		window.open(hostUrl+'outlook_mail.html');\n"
					+ "}\n");
			buffer.append("</script>\n");
			html = buffer.toString();
		} catch (Exception e) {
			log.error("调用方法【getMsgListForPortal】出错："+e.getMessage());
		}

		return html;
	}

	/**
	 * 首页代办列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getMsgListForPortalByAmerica", method = RequestMethod.GET,produces = "text/html; charset=utf-8")
	@ResponseBody
	public String getMsgListForPortalByAmerica(HttpServletRequest request) {

		String contextPath = request.getContextPath();
		String html = "";
		StringBuffer buffer = new StringBuffer();

		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		SecurityUserDto userDto = userBeanInfo.getSecurityUserDto();

		int start = 0;
		int limit = 5;
		try {
			List<String> lines = FileUtils.readLines("/msg-sync-path.properties");
			List<String> syncTendCodes = getSyncTendCode(lines, userBeanInfo);
//			PageBeanInfo<SysNoticeMsgDto> dbMsgPage = PageQueryAcrossDbUtil.getMsgPage(sysNoticeMsgDtoServiceCustomer, userOutServiceCustomer, syncTendCodes, userBeanInfo, start, limit, "DB","0");
			PageBeanInfo<SysNoticeMsgDto> dyMsgPage = PageQueryAcrossDbUtil.getMsgPage(sysNoticeMsgDtoServiceCustomer, userOutServiceCustomer, syncTendCodes, userBeanInfo, start, limit, "DY","1");
//			PageBeanInfo<SysNoticeMsgDto> ybMsgPage = PageQueryAcrossDbUtil.getMsgPage(sysNoticeMsgDtoServiceCustomer, userOutServiceCustomer, syncTendCodes, userBeanInfo, start, limit, "YB","1");
//			PageBeanInfo<SysNoticeMsgDto> fqMsgPage = PageQueryAcrossDbUtil.getMsgPage(sysNoticeMsgDtoServiceCustomer, userOutServiceCustomer, syncTendCodes, userBeanInfo, start, limit, "FQ","1");

			//获取用户邮件列表
			Map<String,Object> emailMap = getXyreEmailInfo(userDto.getId(),userDto.getEmail(),userDto.getEmailPwd());
			int emailTotal = emailMap!=null&&emailMap.get("unReadCount")!=null?(Integer) emailMap.get("unReadCount"):0;
			//List<Map<String,Object>> emailList = emailMap!=null&&emailMap.get("emailInfoList")!=null?(List<Map<String, Object>>) emailMap.get("emailInfoList"):new ArrayList<Map<String,Object>>();

			buffer.append("<div>\n");
			buffer.append("<ul class=\"nav nav-tabs\" role=\"tablist\" id=\"portalMyTaskTab\">\n");
			buffer.append("<li role=\"presentation\" class=\"active\">\n" +
					"            <a id=\"toApprove_A\" href=\"#toApprove\" aria-controls=\"toApprove\" role=\"tab\" data-toggle=\"tab\" title=\"Pending Approval/待审\">Pending Approval/待审()</a>\n" +
					"        </li>\n");
			buffer.append("<li role=\"presentation\">\n" +
					"            <a id=\"toRead_A\" href=\"#toRead\" aria-controls=\"toRead\" role=\"tab\" data-toggle=\"tab\" title=\"Unread/待阅\">Unread/待阅("+dyMsgPage.getTotal()+")</a>\n" +
					"        </li>\n");
			buffer.append("<li role=\"presentation\">\n" +
					"            <a id=\"haveDone_A\" href=\"#haveDone\" aria-controls=\"haveDone\" role=\"tab\" data-toggle=\"tab\" title=\"Handled/已办\">Handled/已办</a>\n" +
					"        </li>\n");
			buffer.append("<li role=\"presentation\">\n" +
					"            <a id=\"myStart_A\" href=\"#myStart\" aria-controls=\"myStart\" role=\"tab\" data-toggle=\"tab\" title=\"My Sponsor/我的发起\">My Sponsor/我的发起</a>\n" +
					"        </li>\n");
			/*buffer.append("<li role=\"presentation\">\n" +
					"            <a id=\"email_A\" href=\"#myEmailDiv\" aria-controls=\"email\" role=\"tab\" data-toggle=\"tab\" title=\"E_mail/邮箱\">E_mail/邮箱("+emailTotal+")</a>\n" +
					"        </li>\n");*/
			buffer.append("<li class=\"pull-right\">\n" +
					"            <a href=\"javascript:void(0);\" id=\"sortMsgListBtn\" style=\"display:inline-block;\" title=\"Sort/排序\"><i class=\"glyphicon glyphicon-up\"></i></a>\n" +
					"            <a href=\""+contextPath+"/flow/runtime/mytask/task_list.html?dataType=DB\" target=\"_blank\" id=\"moreMsgListBtn\" style=\"display:inline-block;\" title=\"More/更多\"><i class=\"glyphicon\"></i></a>\n" +
					"        </li>\n");
			buffer.append("</ul>\n");


			buffer.append("<div class=\"tab-content\">\n");
			//待办
			buffer.append("\t<div role=\"tabpanel\" class=\"tab-pane active\" id=\"toApprove\">\n");
			buffer.append("\t\t<ul class=\"tab_list\" id=\"toApproveDataList\">\n");
			//getLiHtml(buffer,contextPath, dbMsgPage.getList(), "DB");
			buffer.append("\t\t</ul>");
			buffer.append("\t</div>");

			//待阅
			buffer.append("\t<div role=\"tabpanel\" class=\"tab-pane \" id=\"toRead\">\n");
			buffer.append("\t\t<ul class=\"tab_list\" id=\"toReadDataList\">\n");
			//getLiHtml(buffer,contextPath,dyMsgPage.getList(),"DY");
			buffer.append("\t\t</ul>");
			buffer.append("\t</div>");

			//已办/已阅
			buffer.append("\t<div role=\"tabpanel\" class=\"tab-pane \" id=\"haveDone\">\n");
			buffer.append("\t\t<ul class=\"tab_list\" id=\"haveDoneDataList\">\n");
			//getLiHtml(buffer,contextPath,ybMsgPage.getList(),"haveDone");
			buffer.append("\t\t</ul>");
			buffer.append("\t</div>");

			//我的发起
			buffer.append("\t<div role=\"tabpanel\" class=\"tab-pane \" id=\"myStart\">\n");
			buffer.append("\t\t<ul class=\"tab_list\" id=\"myStartDataList\"> \n");
			//getLiHtml(buffer,contextPath,fqMsgPage.getList(),"myStart");
			buffer.append("\t\t</ul>");
			buffer.append("\t</div>");

			//邮件
			/*buffer.append("\t<div role=\"tabpanel\" class=\"tab-pane \" id=\"myEmailDiv\">\n");
			buffer.append("\t\t<ul class=\"tab_list\" id=\"myEmailList\">\n");
			getLiHtmlForEmail(buffer,emailList);
			buffer.append("\t\t</ul>");
			buffer.append("\t</div>");
			buffer.append("</div>");*/

			buffer.append("</div>\n");

			buffer.append("<script type=\"text/javascript\">\n");
			buffer.append("function refreshMyTaskData(){\n" +
					"    switchTaskTab('toApprove');\n" +
					"    switchTaskTab('toRead');\n" +
					"    switchTaskTab('haveDone');\n" +
					"    switchTaskTab('myStart');\n" +
					"    getEmailInfo();\n" +
					/*"	  var taskTabId = $('#flowTask').find('li.active>a').attr(\"id\");\n" +
					"    if (taskTabId == 'toApprove') {\n" +
					"        $('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=DB');\n" +
					"    } else if (taskTabId == 'toRead') {\n" +
					"        $('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=DY');\n" +
					"    } else if (taskTabId == 'haveDone') {\n" +
					"        $('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=HAVE_DONE');\n" +
					"    } else if (taskTabId == 'myStart') {\n" +
					"        $('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=FQ');\n" +
					"    } else if(taskTabId == 'email_A'){\n" +
					"        $('#moreMsgListBtn').hide();\n" +
					"    }" +*/
					"}");
			buffer.append("\t$(function(){\n" +
					//"debugger;\n"+
					"\t\t$('#sortMsgListBtn').on('click',function(){" +
					"\t\t\tvar upFlag = $(this).find('i').hasClass('glyphicon-up');\n" +
					"\t\t\tvar that = $(this) ;\n" +
					"\t\t\tvar sort ;\n" +
					"\t\t\tif(upFlag){\n" +
					"\t\t\tsort = '1';\n"+
					"\t\t\t}else{\n" +
					"\t\t\tsort = '0';\n"+
					"\t\t\t}\n" +
					"\t$.ajax({\n"+
					"\t\t\ttype: 'get',\n"+
					"\t\t\turl: hostUrl + 'flow/sysNoticeMsg/setNoticMsgSort?sortType=' + $('#portalMyTaskTab li.active a').attr('aria-controls')+'&sort=' + sort+'&time='+Math.random(),\n"+
					"\t\t\tdataType: 'json',\n"+
					"\t\t\tasync : false,\n"+
					"\t\tsuccess: function (contentObj) {\n"+
					"\t\t\tif(contentObj.success){\n"+
					"\t\t\t\t//sort = contentObj.result;\n"+
					"\t\t\t$('#portalMyTaskTab li.active a').click()\n" +
					"\t\t\tif(upFlag){\n" +
					"\t\t\t\tthat.find('i').removeClass('glyphicon-up').addClass('glyphicon-down');\n" +
					"\t\t\t}else{\n" +
					"\t\t\t\tthat.find('i').removeClass('glyphicon-down').addClass('glyphicon-up');\n" +
					"\t\t\t}\n" +
					"\t\t\t}\n"+
					"\t\t},\n"+
					"\t\terror:function (xhr) {\n"+
					"\t\t\t$.xljUtils.tip('red', '设置用户排序失败！');\n" +
					"\t\t}\n"+
					"\t});\n"+
					"\t\t});\n" +
					"\t\t$('#toApprove_A').on('click',function(){\n" +
					"\t\t\tswitchTaskTab('toApprove');\n" +
					"\t\t});\n" +
					"\t\t$('#toRead_A').on('click',function(){\n" +
					"\t\t\tswitchTaskTab('toRead');\n" +
					"});\n" +
					"\t\t$('#haveDone_A').on('click',function(){\n" +
					"\t\t\tswitchTaskTab('haveDone');\n" +
					"\t\t});\n" +
					"\t\t$('#myStart_A').on('click',function(){\n" +
					"\t\t\tswitchTaskTab('myStart');\n" +
					"\t\t});\n" +
					"\t\t$('#email_A').on('click',function(){" +
					"\t\t\t$('#moreMsgListBtn').hide();" +
					"\t\t\t$('#sortMsgListBtn').hide();" +
					"\t\t\tgetEmailInfo();" +
					"\t\t});\n" +
					"\t\t$('#toApprove>ul>li>a').each(function(index,ele){$(ele).attr('href',encodeURI($(ele).attr('href')))});" +
					"\t\t$('#portalMyTaskTab li.active a').click()\n"+
					"\t});\n");
			buffer.append("getEmailInfoForAmerica();\n");
			buffer.append("function switchTaskTab(tabType) {\n" +
					"    $('#moreMsgListBtn').show();\n" +
					"    $('#sortMsgListBtn').show();\n" +
//					"    var opType = [];\n" +
					"    var opType = '';\n" +
					"    //['DB','DY','YB','YY','FQ']\n" +
					"    if(tabType=='toApprove'){\n" +
//					"        opType.push('DB');\n" +
					"        opType = 'DB';\n" +
					"    }else if(tabType=='toRead') {\n" +
//					"        opType.push('DY');\n" +
					"        opType = 'DY';\n" +
					"    }else if(tabType=='haveDone') {\n" +
//					"        opType.push('YB');\n" +
//					"        opType.push('YY');\n" +
					"        opType = 'YB';\n" +
					"    }else if(tabType=='myStart') {\n" +
//					"        opType.push('FQ');\n" +
					"        opType = 'FQ';\n" +
					"    }\n" +
					" var sort ;\n" +
					//"debugger;\n" +
					"$.ajax({\n"+
					"type: 'get',\n"+
					"url: hostUrl + 'flow/sysNoticeMsg/getNoticMsgSort?sortType=' + tabType+'&time='+Math.random(),\n"+
					"dataType: 'json',\n"+
					"async : false,\n"+
					"success: function (contentObj) {\n"+
					"if(contentObj.success){\n"+
					"sort = contentObj.result;\n"+
					"}\n"+
					"},\n"+
					"error:function (xhr) {\n"+
					"sort = '1';\n"+
					"}\n"+
					"});\n"+
					"    var paramData = {\n" +
					"        start:0,\n" +
					"        limit:5,\n" +
					"        opType:opType,\n" +
					"        sord:sort\n" +
					"    };\n" +
					"	 if(tabType=='haveDone'){paramData.orderByField='deal_date'} \n" +
					"    $.ajax({ //发送更新的ajax请求\n" +
					"        type: \"POST\",\n" +
					"        url: hostUrl + \"flow/sysNoticeMsg/querySysNoticeMsgByPage?_time=\"+new Date().getTime(),\n" +
					"        dataType: \"JSON\",\n" +
					"        data: JSON.stringify( paramData ),\n" +
					"        contentType: 'application/json;charset=utf-8', //设置请求头信息\n" +
					"        success: function (data) {\n" +
					"            if(data.success) {\n" +
					"                var result = data.result;\n" +
					"                var total = result.total;\n" +
					"                var list = result.list;\n" +
					"                var ulObj = $('<ul></ul>');\n" +
					"                for (var i = 0; i < list.length; i++) {\n" +
					"                    var obj = list[i];\n" +
					"                    var hourSum = obj.hourSum;\n" +
					"                    var hourSumTxt = hourSum + \" hour(s)\";\n" +
					/*"                    if(hourSum>24){\n" +
					"                        var daySum = (hourSum / 24);\n" +
					"                        var leftHour = hourSum % 24;\n" +
					"                        if(leftHour>0){\n" +
					"                            hourSumTxt = daySum+\"天\"+leftHour+\"小时\";\n" +
					"                        }else{\n" +
					"                            hourSumTxt = daySum+\"天\";\n" +
					"                        }\n" +
					"                    }\n" +*/
					"                    hourSumTxt = \"stay \"+hourSumTxt;\n" +
					"                    var url = obj.url;  \n" +
					"                    if (tabType=='toApprove') {\n" +
					"						if(url && url.indexOf(\"http\")==0){\n"+
					"							url = url+\"&time=\"+Math.random();\n"+
					"						} else {\n"+
					"							url = hostUrl+url+\"&_t=\"+new Date().getTime();\n"+
					"						}\n"+
					"                    } else if (tabType=='toRead') {\n" +
					"						if(url && url.indexOf(\"http\")==0){\n"+
					"							url = url+\"&firstTime=YES&time=\"+Math.random();\n"+
					"						} else {\n"+
					"							url = hostUrl+url+\"&firstTime=YES&_t=\"+new Date().getTime();\n"+
					"						}\n"+
					"                    } else if (tabType=='haveDone'||tabType=='myStart') {\n" +
					"						if(url && url.indexOf(\"http\")==0){\n"+
					"							url = url+\"&action=view&time=\"+Math.random();\n"+
					"						} else {\n"+
					"							url = hostUrl+url+\"&action=view&_t=\"+new Date().getTime();\n"+
					"						}\n"+
					"                    }\n" +
					"\n" +
					"                    var liObj = $('<li></li>');\n" +
					"                    var delaySpanObj = $('<span class=\"t_delay\"></span>');\n" +
					"                    delaySpanObj.text(hourSumTxt);\n" +
					"                    delaySpanObj.attr('title',hourSumTxt);\n" +
					"						delaySpanObj.dotdotdot();\n" +
					"                    var aObj = $('<a class=\"t_content\" href=\"javaScript:void(0);\"></a>');\n" +
					"                    aObj.attr('onclick','checkLogin(\"\'+url+\'\");');\n" +
					"                    aObj.attr('title',$.xljUtils.htmlDecode(obj.title));\n" +
					"                    aObj.text($.xljUtils.htmlDecode(obj.title));\n" +
					"\n" +
					"                    if(\"DB\"==opType||\"DY\"==opType){\n" +
					"                        liObj.append(delaySpanObj);\n" +
					"                    }\n" +
					"\n" +
					"                    liObj.append(aObj);\n" +
					"						if(obj.extendInfo=='0'){\n" +
					"							liObj.append('<img class=\"xb\" src=\"../../common/img/xb_no.png\"></img');" +
					"						}else if(obj.extendInfo=='1'){\n" +
					"							liObj.append('<img class=\"xb\" src=\"../../common/img/xb_yes.png\"></img');" +
					"						}\n" +
					"                    ulObj.append(liObj);\n" +
					"\n" +
					"                }\n" +
					"                    if(tabType=='toApprove'){\n" +
					"                        $('#toApprove_A').text('Pending Approval/待审('+total+')');\n" +
					"                        $('#moreMsgListBtn').attr('href', encodeURI(hostUrl+'flow/runtime/mytask/task_list.html?dataType=DB'));\n" +
					"                        $('#toApproveDataList').empty();\n" +
					"                        $('#toApproveDataList').append(ulObj.html());\n" +
					"\n" +
					"                    }else if(tabType=='toRead') {\n" +
					"                        $('#toRead_A').text('Unread/待阅('+total+')');\n" +
					"                        $('#moreMsgListBtn').attr('href', encodeURI(hostUrl+'flow/runtime/mytask/task_list.html?dataType=DY'));\n" +
					"                        $('#toReadDataList').empty();\n" +
					"                        $('#toReadDataList').append(ulObj.html());\n" +
					"                    }else if(tabType=='haveDone') {\n" +
					"                        $('#moreMsgListBtn').attr('href', encodeURI(hostUrl+'flow/runtime/mytask/task_list.html?dataType=HAVE_DONE'));\n" +
					"                        $('#haveDoneDataList').empty();\n" +
					"                        $('#haveDoneDataList').append(ulObj.html());\n" +
					"                    }else if(tabType=='myStart') {\n" +
					"                        $('#moreMsgListBtn').attr('href', encodeURI(hostUrl+'flow/runtime/mytask/task_list.html?dataType=FQ'));\n" +
					"                        $('#myStartDataList').empty();\n" +
					"                        $('#myStartDataList').append(ulObj.html());\n" +
					"                    }\n" +
					"						//计算待办列表宽度\n" +
					"            		$('#portalMyTaskTab').parents('div.groupnews_container').find('ul.tab_list li').each(function (i,liObj) {\n" +
					"                		var spanObj = $(liObj).find('span.t_delay');\n" +
					"                		var aObj = $(liObj).find('a.t_content');\n" +
					"						var aWidth = $(liObj).find('a').width();\n" +
					"						var spanWidth = spanObj.outerWidth();\n" +
					"                		var imgWidth = $(liObj).find('img.xb').outerWidth();" +
					"                		imgWidth = imgWidth?imgWidth:0;" +
					"                		$(liObj).find('a').css({'display':'inline'});\n" +
					"                		var aWidth = $(liObj).find('a').width();\n" +
					"						var liWidth = $(liObj).width();\n" +
					"                		if(aWidth>(liWidth-spanWidth-imgWidth)){\n" +
					"                    	$(liObj).find('a').css({width:(liWidth-spanWidth-imgWidth-10)+'px'});\n" +
					"                		}\n" +
					"                		$(liObj).find('a').css({'display':'inline'});\n" +
					"                		$(liObj).find('a').dotdotdot();\n" +
					"            		});\n"	+
					"						//默认选择的tab更多链接\n" +
					"                		var taskTabId = $('#portalMyTaskTab').find('li.active a').attr(\"id\");\n" +
					"                		if (taskTabId == 'toApprove_A') {\n" +
					"                    		$('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=DB');\n" +
					"                		} else if (taskTabId == 'toRead_A') {\n" +
					"                    		$('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=DY');\n" +
					"                		} else if (taskTabId == 'haveDone_A') {\n" +
					"                    		$('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=HAVE_DONE');\n" +
					"                		} else if (taskTabId == 'myStart_A') {\n" +
					"                    		$('#moreMsgListBtn').attr('href', hostUrl + 'flow/runtime/mytask/task_list.html?dataType=FQ');\n" +
					"                		} else if(taskTabId == 'email_A'){\n" +
					"                    		$('#moreMsgListBtn').hide();\n" +
					"                    		$('#sortMsgListBtn').hide();\n" +
					"                		}\n" +
					"if('1'==sort){\n"+
					"\t\t\t\t$('#sortMsgListBtn').find('i').removeClass('glyphicon-up').addClass('glyphicon-down');\n" +
					"}else{\n"+
					"\t\t\t\t$('#sortMsgListBtn').find('i').removeClass('glyphicon-down').addClass('glyphicon-up');\n" +
					"}\n"+
					"            }\n" +
					"        },\n" +
					"        error: function (data) {\n" +
					"            if (data.msg) {\n" +
					"                $.xljUtils.tip('red', data.msg);\n" +
					"            } else {\n" +
					"                $.xljUtils.tip('red', \"查询任务列表数据失败！\");\n" +
					"            }\n" +
					"        }\n" +
					"    });\n" +
					"}\n");
			buffer.append("function getEmailInfo(){\n" +
					"    $.ajax({\n" +
					"        type: 'GET',\n" +
					"        url: hostUrl + 'flow/sysNoticeMsg/getUserEmail?_time='+new Date().getTime(),\n" +
					"        dataType:'JSON',\n" +
					"        success: function (resultData) {\n" +
					"            if(resultData.success){\n" +
					"\n" +
					"                $('#myEmailList').empty();\n" +
					"                var emailMap = resultData.result;\n" +
					"                var unReadCount = emailMap.unReadCount;\n" +
					"                $('#email_A').text('E_mail/邮箱('+unReadCount+')');\n" +
					"\n" +
					"                var emailList = emailMap.emailInfoList;\n" +
					"                var outlook = hostUrl+'outlook_mail.html';\n" +
					"                var ulObj = $('<ul></ul>');\n" +
					"                for (var i=0;i<emailList.length;i++){\n" +
					"                    var liObj = $('<li></li>');\n" +
					"                    var titleSpan = $('<a href=\"'+outlook+'\" target=\"_blank\" class=\" email_title \"></a>');\n" +
//					"                    var titleSpan = $('<span class=\"email_title\"></span>');\n" +
					"                    titleSpan.text(emailList[i].emailSubject);\n" +
					"                    titleSpan.attr('title',emailList[i].emailSubject);\n" +
					"					\n" +
					"                    var sendUserSpan = $('<span class=\"email_author\"></span>');\n" +
					"                    sendUserSpan.text(emailList[i].sendUser);\n" +
					"                    sendUserSpan.attr('title',emailList[i].sendUser);\n" +
					"\n" +
					"                    var sendTimeSpan = $('<span class=\"email_time\"></span>');\n" +
					"                    sendTimeSpan.text(emailList[i].emailSendTime);\n" +
					"                    sendTimeSpan.attr('title',emailList[i].emailSendTime);\n" +
					"\n" +
					"                    liObj.append(sendTimeSpan);\n" +
					"                    liObj.append(sendUserSpan);\n" +
					"                    liObj.append(titleSpan);\n" +
					"                    ulObj.append(liObj);\n" +
					"                }\n" +
					"                $('#myEmailList').append(ulObj.html());\n" +
					"					$('#myEmailList').find('li').each(function (i,liObj) {\n" +
					//"						debugger;\n" +
					"                		$(liObj).find('a').css({width:($(liObj).width()-170)+'px'});\n" +
					"            		});\n" +
					"            }\n" +
					"        },\n" +
					"        error:function (xhr) {\n" +
					"            //console.info(xhr);\n" +
					"        }\n" +
					"    });\n" +
					"}\n");
			buffer.append("$('#email_A').dblclick(function(){\n" +
					"		window.open(hostUrl+'outlook_mail.html');\n" +
					"	});\n" );
			buffer.append("function openOutlook(){\n"
					+ "		window.open(hostUrl+'outlook_mail.html');\n"
					+ "}\n");
			buffer.append("</script>\n");
			html = buffer.toString();
		} catch (Exception e) {
			log.error("调用方法【getMsgListForPortal】出错："+e.getMessage());
		}

		return html;

	}

	/**
	 * 组装消息列表html
	 * @param buffer
	 * @param contextPath
	 * @param sysNoticeMsgDtoList
	 * @param opType
	 */
	private void getLiHtml(StringBuffer buffer,String contextPath,List<SysNoticeMsgDto> sysNoticeMsgDtoList,String opType) throws Exception{
		for (SysNoticeMsgDto gTaskDto : sysNoticeMsgDtoList) {
			String hourSumStr = gTaskDto.getHourSum();
			int hourSum = Integer.parseInt(hourSumStr);
			String hourSumTxt = hourSumStr + "小时";
			/*if(hourSum>24){
				int daySum = (int)(hourSum / 24);
				int leftHour = hourSum % 24;
				if(leftHour>0){
					hourSumTxt = daySum+"天"+leftHour+"小时";
				}else{
					hourSumTxt = daySum+"天";
				}
			}*/
			hourSumTxt = "停留"+hourSumTxt;

			String url = gTaskDto.getUrl();
			if ("DB".equals(opType)) {
				//url = contextPath+"/"+url+"&_t="+new Date().getTime();
				if(url.startsWith("http")){
					url = url+"&_t="+new Date().getTime();
				}else{
					url = contextPath+"/"+url+"&_t="+new Date().getTime();
				}
			} else if ("DY".equals(opType)) {
				//url = contextPath+"/"+url+"&firstTime=YES&_t="+new Date().getTime();
				if(url.startsWith("http")){
					url = url+"&firstTime=YES&_t="+new Date().getTime();
				}else{
					url = contextPath+"/"+url+"&firstTime=YES&_t="+new Date().getTime();
				}
			} else if ("haveDone".equals(opType)||"myStart".equals(opType)) {
				//url = contextPath+"/"+url+"&action=view&_t="+new Date().getTime();
				if(url.startsWith("http")){
					url = url+"&action=view&_t="+new Date().getTime();
				}else{
					url = contextPath+"/"+url+"&action=view&_t="+new Date().getTime();
				}
			}

			buffer.append("\t\t\t<li>\n");
			if("DB".equals(opType)||"DY".equals(opType)){
				buffer.append("\t\t\t\t<span class=\"t_delay\">"+hourSumTxt+"</span>\n");
			}
			// href=""+url+""
			buffer.append("\t\t\t\t<a href='javaScript:void(0);' class=\"t_content\" onclick='checkLogin(\"" + url + "\")'  title=\""+gTaskDto.getTitle()+"\">"+gTaskDto.getTitle());
			buffer.append("</a>");
			if ("0".equals(gTaskDto.getExtendInfo())) {
				buffer.append("\t\t\t\t<img class=\"xb\" src=\"../../common/img/xb_no.png\"></img>");
			}else if ("1".equals(gTaskDto.getExtendInfo())) {
				buffer.append("\t\t\t\t<img class=\"xb\"src=\"../../common/img/xb_yes.png\"></img>");
			}

			buffer.append("\t\t\t</li>");
		}
	}

	/**
	 * 组装消息列表html（美国公司专用）
	 * @param buffer
	 * @param contextPath
	 * @param sysNoticeMsgDtoList
	 * @param opType
	 * @throws Exception
	 */
	private void getLiHtmlByAmerica(StringBuffer buffer,String contextPath,List<SysNoticeMsgDto> sysNoticeMsgDtoList,String opType) throws Exception{
		for (SysNoticeMsgDto gTaskDto : sysNoticeMsgDtoList) {
			String hourSumStr = gTaskDto.getHourSum();
			int hourSum = Integer.parseInt(hourSumStr);
			String hourSumTxt = hourSumStr + " hour(s)";
			/*if(hourSum>24){
				int daySum = (int)(hourSum / 24);
				int leftHour = hourSum % 24;
				if(leftHour>0){
					hourSumTxt = daySum+"天"+leftHour+"小时";
				}else{
					hourSumTxt = daySum+"天";
				}
			}*/
			hourSumTxt = "stay "+hourSumTxt;

			String url = gTaskDto.getUrl();
			if ("DB".equals(opType)) {
				//url = contextPath+"/"+url+"&_t="+new Date().getTime();
				if(url.startsWith("http")){
					url = url+"&_t="+new Date().getTime();
				}else{
					url = contextPath+"/"+url+"&_t="+new Date().getTime();
				}
			} else if ("DY".equals(opType)) {
				//url = contextPath+"/"+url+"&firstTime=YES&_t="+new Date().getTime();
				if(url.startsWith("http")){
					url = url+"&firstTime=YES&_t="+new Date().getTime();
				}else{
					url = contextPath+"/"+url+"&firstTime=YES&_t="+new Date().getTime();
				}
			} else if ("haveDone".equals(opType)||"myStart".equals(opType)) {
				//url = contextPath+"/"+url+"&action=view&_t="+new Date().getTime();
				if(url.startsWith("http")){
					url = url+"&action=view&_t="+new Date().getTime();
				}else{
					url = contextPath+"/"+url+"&action=view&_t="+new Date().getTime();
				}
			}

			buffer.append("\t\t\t<li>\n");
			if("DB".equals(opType)||"DY".equals(opType)){
				buffer.append("\t\t\t\t<span class=\"t_delay delay_america\">"+hourSumTxt+"</span>\n");
			}
			// href=""+url+""
			buffer.append("\t\t\t\t<a href='javaScript:void(0);' class=\"t_content\" onclick='checkLogin(\"" + url + "\")'  title=\""+gTaskDto.getTitle()+"\">"+gTaskDto.getTitle()+"</a>");
			if ("0".equals(gTaskDto.getExtendInfo())) {
				buffer.append("\t\t\t\t<img class=\"xb\" src=\"../../common/img/xb_no.png\"></img>");
			}else if ("1".equals(gTaskDto.getExtendInfo())) {
				buffer.append("\t\t\t\t<img class=\"xb\"src=\"../../common/img/xb_yes.png\"></img>");
			}
			buffer.append("\t\t\t</li>");
		}
	}

	/**
	 * 组装email列表
	 * @param buffer
	 * @param emailList
	 */
	private void getLiHtmlForEmail(StringBuffer buffer,List<Map<String,Object>> emailList) {
		for (Map<String,Object> emailMap:emailList) {
			String emailSubject = ((String)emailMap.get("emailSubject"));
			String sendUser = (String) emailMap.get("sendUser");
			Date emailSendTime = (Date) emailMap.get("emailSendTime");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String emailSendTimeStr = format.format(emailSendTime);
			buffer.append("\t<li>\n");
			buffer.append("\t\t<span class=\"email_title\" title=\""+emailSubject+"\">"+emailSubject+"</span>\n");
			buffer.append("\t\t<span class=\"email_author\" title=\"" + sendUser + "\">"+sendUser+"</span>\n");
			buffer.append("\t\t<span class=\"email_time\" title=\"" + emailSendTimeStr + "\">"+emailSendTimeStr+"</span>\n");
			buffer.append("\t</li>\n");
		}
	}

	/**
	 * 获取用户前Excheng10条邮件信息
	 * @param userId
	 * @param emailUserName
	 * @param emailPwd
	 * @return
	 */
	private Map<String,Object> getXyreEmailInfo(String userId,String emailUserName, String emailPwd){
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			//从数据库重新获取emailPwd
			String dubboResultInfo = userDtoServiceCustomer.getObjectById(JacksonUtils.toJson(userBeanInfo),  "{\"id\":\""+userId+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				UserDto userDto=JacksonUtils.fromJson(resultInfo, UserDto.class);
				emailPwd = userDto.getEmailPwd();
			}else{
				return null;
			}
			Map<String,Object> resultMap = new HashMap<String, Object>() ;
			//判断用户邮箱信息是否为空
			if (StringUtils.isBlank(userId)||StringUtils.isBlank(emailUserName)||StringUtils.isBlank(emailPwd)){
				resultMap.put("success",false);
				resultMap.put("msg","用户邮箱/邮箱密码不能为空！");
				return resultMap;
			}

			//解密邮箱密码
			EndecryptUtil endecryptUtil = new EndecryptUtil();
			String emailPassword = endecryptUtil.get3DESDecrypt(emailPwd,userId);


			List<Map<String,Object>> emailInfoList=new ArrayList<>();
			//ExchangeService版本
			ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010);
			//用户名、密码、域
			ExchangeCredentials credentials = new WebCredentials(emailUserName, emailPassword, "xyre");
			//设置超时
			service.setTimeout(20000);
			service.setCredentials(credentials);
			//设置邮件服务器地址
			service.setUrl(new URI("https://"+"mail.xyre.com"+"/EWS/Exchange.asmx"));

			//创建过滤器
			Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
			int unread = inbox.getUnreadCount();
			resultMap.put("unReadCount",unread);//获取未读邮件数量
			//创建过滤器条件，查询10封邮件
			ItemView view = new ItemView(5);

			//查询
//			FindItemsResults<Item> findResults = service.findItems(inbox.getId(), view);
			//查询未读
			FindItemsResults<microsoft.exchange.webservices.data.Item> findResults = service.findItems(inbox.getId(),new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false), view);
			for (Item item : findResults.getItems()) {
				Map<String,Object> m=new HashMap<String,Object>();
				EmailMessage message = EmailMessage.bind(service, item.getId());
				message.load();
			   /* System.out.println(message.getSender());
			    System.out.println("Sub -->" + item.getSubject());*/
//				m.put("sendUser", message.getSender().toString());
				m.put("sendUser", message.getFrom().getName());
				m.put("emailSubject", item.getSubject());
				m.put("emailSendTime", getOneHoursAgoTime(item.getDateTimeSent()) );
				emailInfoList.add(m);
			}
			resultMap.put("emailInfoList",emailInfoList);
			resultMap.put("success",true);
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	//时间偏移8小时，有时差
	public static  String  getOneHoursAgoTime (Date date) {
		String oneHoursAgoTime =  "" ;
        Calendar cal = Calendar.getInstance ();
        cal.setTime(date);
        cal.add(Calendar.HOUR ,8 ) ;
        oneHoursAgoTime =  new  SimpleDateFormat( "yyyy/MM/dd HH:mm" ).format(cal.getTime());//获取到完整的时间
         return  oneHoursAgoTime;
	}
	/**
	 * 分页获取消息列表，主要用于首页待办列表
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/querySysNoticeMsgByPage",method={RequestMethod.POST}, consumes="application/json")
	@ResponseBody
	public MessageResult querySysNoticeMsgByPage(@RequestBody Map<String,Object> map) {
		MessageResult result=new MessageResult();
		String paramaterJson = null;
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String type = (String) map.get("opType");

			int start = 0;
			int limit = 5;
			List<String> lines = FileUtils.readLines("/msg-sync-path.properties");
			List<String> syncTendCodes = getSyncTendCode(lines, userBeanInfo);
			String sord = String.valueOf(map.get("sord"));
			PageBeanInfo<SysNoticeMsgDto> dbMsgPage = PageQueryAcrossDbUtil.getMsgPage(sysNoticeMsgDtoServiceCustomer,
					userOutServiceCustomer, syncTendCodes, userBeanInfo, start, limit, type,sord);
			result.setResult(dbMsgPage);
			result.setSuccess(MessageInfo.GETSUCCESS.isResult());
			result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用querySysNoticeMsgByPage方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	@RequestMapping(value="/getUserEmail",method= RequestMethod.GET)
	public @ResponseBody MessageResult getUserEmail(){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			SecurityUserDto securityUserDto = securityUserBeanInfo.getSecurityUserDto();
			String userId = securityUserDto.getId();
			String emailPwd = securityUserDto.getEmailPwd();
//            String email = securityUserDto.getEmail();
			String loginName = securityUserDto.getLoginName();
//            Map<String,Object> emailInfoMap = getUserEmailInfo(userId,email,emailPwd);
			Map<String,Object> emailInfoMap = getXyreEmailInfo(userId,loginName,emailPwd);
			if(emailInfoMap!=null){
				boolean successFlag = (boolean) emailInfoMap.get("success");
				String msg = (String) emailInfoMap.get("msg");
				//List<Map<String,String>> emailInfoList = (List<Map<String, String>>) emailInfoMap.get("emailInfoList");
				result.setResult(emailInfoMap);
				result.setSuccess(successFlag);
				result.setMsg(msg);
			}else{
				result.setSuccess(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用getUserEmail方法:  【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	@RequestMapping("/getEmailUnreadCount")
	public @ResponseBody MessageResult getEmailUnreadCount(){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			SecurityUserDto securityUserDto = securityUserBeanInfo.getSecurityUserDto();
			String userId = securityUserDto.getId();
			String emailPwd = securityUserDto.getEmailPwd();
//            String email = securityUserDto.getEmail();
			String loginName = securityUserDto.getLoginName();
//            Map<String,Object> emailInfoMap = getUserEmailInfo(userId,email,emailPwd);
			Map<String,Object> emailInfoMap = getXyreEmailInfo(userId,loginName,emailPwd);
			Map<String,Object> unreadMap = new HashMap<String,Object>();
			unreadMap.put("unReadCount",emailInfoMap.get("unReadCount"));
			if(emailInfoMap!=null){
				boolean successFlag = (boolean) emailInfoMap.get("success");
				String msg = (String) emailInfoMap.get("msg");
				//List<Map<String,String>> emailInfoList = (List<Map<String, String>>) emailInfoMap.get("emailInfoList");
				result.setResult(unreadMap);
				result.setSuccess(successFlag);
				result.setMsg(msg);
			}else{
				result.setSuccess(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用getUserEmail方法:  【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 获取首页待办用户排序
	 * @return
	 */
	@RequestMapping(value="/getNoticMsgSort",method={RequestMethod.GET})
	public @ResponseBody MessageResult getNoticMsgSort(@RequestParam(name = "sortType")String sortType){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String tendId = securityUserBeanInfo.getTendId();
			SecurityUserDto securityUserDto = securityUserBeanInfo.getSecurityUserDto();
			String userId = securityUserDto.getId();
			String key = tendId+"_"+userId+"_"+sortType;
			if (redisTemplate.hasKey(key)) {//判断是否存在key
				result.setResult(redisTemplate.opsForValue().get(key));
				result.setSuccess(true);
			}else{
				redisTemplate.opsForValue().set(key,"1");
				result.setResult(redisTemplate.opsForValue().get(key));
				result.setSuccess(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用getNoticMsgSort方法:  【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 设置首页待办用户排序
	 * @return
	 */
	@RequestMapping(value="/setNoticMsgSort",method={RequestMethod.GET})
	public @ResponseBody MessageResult setNoticMsgSort(@RequestParam(name = "sortType")String sortType,@RequestParam(name = "sort")String sort){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String tendId = securityUserBeanInfo.getTendId();
			SecurityUserDto securityUserDto = securityUserBeanInfo.getSecurityUserDto();
			String userId = securityUserDto.getId();
			String key = tendId+"_"+userId+"_"+sortType;
			redisTemplate.opsForValue().set(key,sort);
			result.setResult(redisTemplate.opsForValue().get(key));
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用getNoticMsgSort方法:  【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	private List<String> getSyncTendCode(List<String> lines, SecurityUserBeanInfo userInfo) {
		List<String> syncTendCode = new ArrayList<String>();
		syncTendCode.add(userInfo.getTendCode());
		if(CollectionUtils.isEmpty(lines)) {
			return syncTendCode;
		}

		for(String line : lines) {
			String[] mapArray = line.split(":");
			if(mapArray == null || mapArray.length != 3) {
				log.info("租户间消息同步配置错误：" + line);
				continue;

			} else {
				if(userInfo.getTendCode().equals(mapArray[0])
						&& mapArray[2].contains(userInfo.getSecurityUserDto().getLoginName())) {
					syncTendCode.add(mapArray[1]);
				}
			}
		}

		return syncTendCode;
	}

	@RequestMapping("/loginOutlook")
	public void loginOutlook(){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			SecurityUserDto securityUserDto = securityUserBeanInfo.getSecurityUserDto();
			String userId = securityUserDto.getId();
			String emailPwd = null;
			//从数据库重新获取emailPwd
			String dubboResultInfo = userDtoServiceCustomer.getObjectById(JacksonUtils.toJson(securityUserBeanInfo),  "{\"id\":\""+userId+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				UserDto userDto=JacksonUtils.fromJson(resultInfo, UserDto.class);
				emailPwd = userDto.getEmailPwd();
			}
			String loginName = securityUserDto.getLoginName();

			//解密邮箱密码
			EndecryptUtil endecryptUtil = new EndecryptUtil();
			String emailPassword = endecryptUtil.get3DESDecrypt(emailPwd,userId);
			Map<String, Object> reqContent = new HashMap<String, Object>();//请求map
			reqContent.put("destination", "https://mail.xyre.com/owa");
			reqContent.put("username", loginName);
			reqContent.put("password", emailPassword);
			reqContent.put("flags", "0");
			reqContent.put("forcedownlevel", "0");
			reqContent.put("isutf8", "1");
			reqContent.put("trusted", "1");
			String url = "https://mail.xyre.com/owa";
			CloseableHttpClient httpClient= HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
			httpPost.addHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
			StringEntity se = new StringEntity(JacksonUtils.toJson(reqContent), "utf-8");
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用getUserXyre方法:  【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
	}
}
