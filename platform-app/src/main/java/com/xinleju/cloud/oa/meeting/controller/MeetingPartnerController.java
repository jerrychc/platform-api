package com.xinleju.cloud.oa.meeting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.cloud.oa.meeting.dto.MeetingPartnerDto;
import com.xinleju.cloud.oa.meeting.dto.MeetingReplyDto;
import com.xinleju.cloud.oa.meeting.dto.service.MeetingPartnerDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 与会人员记录表控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/oa/meeting/meetingPartner")
public class MeetingPartnerController {

	private static Logger log = Logger.getLogger(MeetingPartnerController.class);
	
	@Autowired
	private MeetingPartnerDtoServiceCustomer meetingPartnerDtoServiceCustomer;
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
			String dubboResultInfo=meetingPartnerDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MeetingPartnerDto meetingPartnerDto=JacksonUtils.fromJson(resultInfo, MeetingPartnerDto.class);
				result.setResult(meetingPartnerDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用get方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	
	/**
	 * 返回分页对象
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
		    String dubboResultInfo=meetingPartnerDtoServiceCustomer.getPage(getUserJson(), paramaterJson);
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
			e.printStackTrace();
		    log.error("调用page方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	/**
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			String dubboResultInfo=meetingPartnerDtoServiceCustomer.queryList(getUserJson(), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<MeetingPartnerDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,MeetingPartnerDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
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
	public @ResponseBody MessageResult save(@RequestBody MeetingPartnerDto t){
		MessageResult result=new MessageResult();
		try {
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=meetingPartnerDtoServiceCustomer.save(getUserJson(), saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MeetingPartnerDto meetingPartnerDto=JacksonUtils.fromJson(resultInfo, MeetingPartnerDto.class);
				result.setResult(meetingPartnerDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
		} catch (Exception e) {
			try {
				e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	/**
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=meetingPartnerDtoServiceCustomer.deleteObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MeetingPartnerDto meetingPartnerDto=JacksonUtils.fromJson(resultInfo, MeetingPartnerDto.class);
				result.setResult(meetingPartnerDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用delete方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	
	/**
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deleteBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=meetingPartnerDtoServiceCustomer.deleteAllObjectByIds(getUserJson(), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MeetingPartnerDto meetingPartnerDto=JacksonUtils.fromJson(resultInfo, MeetingPartnerDto.class);
				result.setResult(meetingPartnerDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用delete方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	/**
	 * 修改修改实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		MeetingPartnerDto meetingPartnerDto=null;
		try {
			String dubboResultInfo=meetingPartnerDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=meetingPartnerDtoServiceCustomer.update(getUserJson(), updateJson);
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
			 e.printStackTrace();
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(meetingPartnerDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
			}catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		return result;
	}

	/**
	 * 伪删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=meetingPartnerDtoServiceCustomer.deletePseudoObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MeetingPartnerDto meetingPartnerDto=JacksonUtils.fromJson(resultInfo, MeetingPartnerDto.class);
				result.setResult(meetingPartnerDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用deletePseudo方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	
	/**
	 * 伪删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudoBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=meetingPartnerDtoServiceCustomer.deletePseudoAllObjectByIds(getUserJson(), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MeetingPartnerDto meetingPartnerDto=JacksonUtils.fromJson(resultInfo, MeetingPartnerDto.class);
				result.setResult(meetingPartnerDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用deletePseudoBatch方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	
	/**
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/getUserInfoByMeetingId",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getUserInfoByMeetingId(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			map.put("userId", userBeanInfo.getSecurityUserDto().getId());
			String dubboResultInfo=meetingPartnerDtoServiceCustomer.getUserInfoByMeetingId(getUserJson(), map);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MeetingPartnerDto meetingPartnerDto=JacksonUtils.fromJson(resultInfo,MeetingPartnerDto.class);
				result.setResult(meetingPartnerDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }

		} catch (Exception e) {
			//e.printStackTrace();
			log.error("调用queryMeetingReployByNew方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	
	
	/**
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/getUserIdentifiyByMeetingId",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getUserIdentifiyByMeetingId(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			map.put("userId", userBeanInfo.getSecurityUserDto().getId());
			String dubboResultInfo=meetingPartnerDtoServiceCustomer.getUserIdentifiyByMeetingId(getUserJson(), map);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<String> userIdentifiyList=JacksonUtils.fromJson(resultInfo,ArrayList.class);
				result.setResult(userIdentifiyList);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }

		} catch (Exception e) {
			//e.printStackTrace();
			log.error("调用queryMeetingReployByNew方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	
	
	
	/**
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/getUserIdentifiyAndIsJoinAndIsSummMeetingId",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getUserIdentifiyAndIsJoinAndIsSummMeetingId(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			map.put("userId", userBeanInfo.getSecurityUserDto().getId());
			String dubboResultInfo=meetingPartnerDtoServiceCustomer.getUserIdentifiyAndIsJoinAndIsSummMeetingId(getUserJson(), map);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map userIdentifiyList=JacksonUtils.fromJson(resultInfo,Map.class);
				result.setResult(userIdentifiyList);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }

		} catch (Exception e) {
			//e.printStackTrace();
			log.error("调用queryMeetingReployByNew方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	private String getUserJson(){
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		return userJson;
  }
	
}
