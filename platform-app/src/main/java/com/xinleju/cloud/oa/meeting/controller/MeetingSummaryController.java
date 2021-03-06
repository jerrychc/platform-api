package com.xinleju.cloud.oa.meeting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.utils.*;

import com.xinleju.platform.sys.org.dto.service.UserDtoServiceCustomer;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.sys.security.dto.service.AuthenticationDtoServiceCustomer;
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
import com.xinleju.cloud.oa.meeting.dto.MeetingSummaryDto;
import com.xinleju.cloud.oa.meeting.dto.service.MeetingSummaryDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 会议纪要表控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/oa/meeting/meetingSummary")
public class MeetingSummaryController {

	private static Logger log = Logger.getLogger(MeetingSummaryController.class);
	
	@Autowired
	private MeetingSummaryDtoServiceCustomer meetingSummaryDtoServiceCustomer;

	@Autowired
	private UserDtoServiceCustomer userDtoServiceCustomer;

	@Autowired
	private AuthenticationDtoServiceCustomer authenticationDtoServiceCustomer;
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
			String dubboResultInfo=meetingSummaryDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MeetingSummaryDto meetingSummaryDto=JacksonUtils.fromJson(resultInfo, MeetingSummaryDto.class);
				result.setResult(meetingSummaryDto);
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
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
		    String dubboResultInfo=meetingSummaryDtoServiceCustomer.getPage(getUserJson(), paramaterJson);
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
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/queryList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			String dubboResultInfo=meetingSummaryDtoServiceCustomer.queryList(getUserJson(), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<MeetingSummaryDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,MeetingSummaryDto.class);
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
	public @ResponseBody MessageResult save(@RequestBody MeetingSummaryDto t){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			t.setCreateUserId(userBeanInfo.getSecurityUserDto().getId());
			t.setCreateUserName(userBeanInfo.getSecurityUserDto().getRealName());
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=meetingSummaryDtoServiceCustomer.save(getUserJson(), saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MeetingSummaryDto meetingSummaryDto=JacksonUtils.fromJson(resultInfo, MeetingSummaryDto.class);
				result.setResult(meetingSummaryDto);
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
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=meetingSummaryDtoServiceCustomer.deleteObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MeetingSummaryDto meetingSummaryDto=JacksonUtils.fromJson(resultInfo, MeetingSummaryDto.class);
				result.setResult(meetingSummaryDto);
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
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deleteBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=meetingSummaryDtoServiceCustomer.deleteAllObjectByIds(getUserJson(), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MeetingSummaryDto meetingSummaryDto=JacksonUtils.fromJson(resultInfo, MeetingSummaryDto.class);
				result.setResult(meetingSummaryDto);
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
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		MeetingSummaryDto meetingSummaryDto=null;
		try {
			String dubboResultInfo=meetingSummaryDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=meetingSummaryDtoServiceCustomer.update(getUserJson(), updateJson);
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
			 String  paramJson = mapper.writeValueAsString(meetingSummaryDto);
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
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=meetingSummaryDtoServiceCustomer.deletePseudoObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MeetingSummaryDto meetingSummaryDto=JacksonUtils.fromJson(resultInfo, MeetingSummaryDto.class);
				result.setResult(meetingSummaryDto);
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
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deletePseudoBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=meetingSummaryDtoServiceCustomer.deletePseudoAllObjectByIds(getUserJson(), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MeetingSummaryDto meetingSummaryDto=JacksonUtils.fromJson(resultInfo, MeetingSummaryDto.class);
				result.setResult(meetingSummaryDto);
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
	 * 暂存数据
	 *
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/temporary/save", method = RequestMethod.POST, consumes = "application/json")
	public
	@ResponseBody
	MessageResult saveTemporary(@RequestBody Map<String,Object> map) {
		MessageResult result = new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			map.put("createUserId", userBeanInfo.getSecurityUserDto().getId());
			map.put("createUserName", userBeanInfo.getSecurityUserDto().getRealName());
			String saveJson = JacksonUtils.toJson(map);
			String dubboResultInfo = meetingSummaryDtoServiceCustomer.saveTemporary(getUserJson(), saveJson);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				String resultInfo = dubboServiceResultInfo.getResult();
				MeetingSummaryDto meetingSummaryDto = JacksonUtils.fromJson(resultInfo, MeetingSummaryDto.class);
				result.setResult(meetingSummaryDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
			} else {
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
				result.setCode(dubboServiceResultInfo.getCode());
			}
		} catch (Exception e) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				String paramJson = mapper.writeValueAsString(map);
				log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			} catch (JsonProcessingException e1) {
			}
		}
		return result;
	}
	
	 private String getUserJson(){
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			return userJson;
	  }
		@RequestMapping(value="/getStartFlowInfo",method=RequestMethod.POST, consumes="application/json")
		public @ResponseBody MessageResult getStartFlowInfo(@RequestBody Map<String,Object> resmap){
			MessageResult result=new MessageResult();
			String id = (String) resmap.get("businessId");
			SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
			if(resmap.get("token")!=null){//加入白名单处理获取表单流程业务变量查询
				String token = String.valueOf(resmap.get("token"));
				String[] args = token.split("@");
				if(args.length==2){
					securityUserBeanInfo.setTendCode(args[1]);
					SecurityUserBeanInfo securityUserBeanInfo1 = LoginUtils.getSecurityUserBeanInfo();
					SecurityUserDto securityUserDto = null;
					if (securityUserBeanInfo1 == null) {
						Map<String,Object> paramMap = new HashMap<String,Object>();
						paramMap.put("loginName",args[0]);
						String userDubboInfoStr = userDtoServiceCustomer.queryList(JacksonUtils.toJson(securityUserBeanInfo),JacksonUtils.toJson(paramMap));
						DubboServiceResultInfo userDubboInfo = JacksonUtils.fromJson(userDubboInfoStr,DubboServiceResultInfo.class);
						if(userDubboInfo.isSucess()){
							String userInfo = userDubboInfo.getResult();
							List<SecurityUserDto> userDtos = JacksonUtils.fromJson(userInfo,List.class,SecurityUserDto.class);
							if(userDtos!=null&&userDtos.size()>0){
								securityUserDto = userDtos.get(0);
							}
						}
					}else{
						securityUserDto = securityUserBeanInfo1.getSecurityUserDto();
					}
					securityUserBeanInfo.setSecurityUserDto(securityUserDto);
					this.setUserAuthInfo(securityUserBeanInfo,securityUserDto);
				}

			}else{
				securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			}
			try {
				String dubboResultInfo=meetingSummaryDtoServiceCustomer.getStartFlowInfo(JacksonUtils.toJson(securityUserBeanInfo),id);
				DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
				if(dubboServiceResultInfo.isSucess()){
					String resultInfo= dubboServiceResultInfo.getResult();
					result.setResult(resultInfo);
					result.setSuccess(MessageInfo.GETSUCCESS.isResult());
					result.setMsg(MessageInfo.GETSUCCESS.getMsg());
				}else{
					result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
				}
			} catch (Exception e) {
				
				log.error("调用get方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
			}
			return result;
		}	
		@RequestMapping(value="/updateStatus",method=RequestMethod.POST,consumes="application/json")
		public @ResponseBody MessageResult updateCount(@RequestBody Map<String,Object> map){
			MessageResult result=new MessageResult();
			String paramaterJson = JacksonUtils.toJson(map);
			SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
			if(map.get("token")!=null){//加入白名单处理获取新闻/知识表单流程业务回调
				String token = String.valueOf(map.get("token"));
				String[] args = token.split("@");
				if(args.length==2){
					securityUserBeanInfo.setTendCode(args[1]);
					SecurityUserBeanInfo securityUserBeanInfo1 = LoginUtils.getSecurityUserBeanInfo();
					SecurityUserDto securityUserDto = null;
					if (securityUserBeanInfo1 == null) {
						Map<String,Object> paramMap = new HashMap<String,Object>();
						paramMap.put("loginName",args[0]);
						String userDubboInfoStr = userDtoServiceCustomer.queryList(JacksonUtils.toJson(securityUserBeanInfo),JacksonUtils.toJson(paramMap));
						DubboServiceResultInfo userDubboInfo = JacksonUtils.fromJson(userDubboInfoStr,DubboServiceResultInfo.class);
						if(userDubboInfo.isSucess()){
							String userInfo = userDubboInfo.getResult();
							List<SecurityUserDto> userDtos = JacksonUtils.fromJson(userInfo,List.class,SecurityUserDto.class);
							if(userDtos!=null&&userDtos.size()>0){
								securityUserDto = userDtos.get(0);
							}
						}

					}else{
						securityUserDto = securityUserBeanInfo1.getSecurityUserDto();
					}
					securityUserBeanInfo.setSecurityUserDto(securityUserDto);
				}
			}else{
				securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			}
			try {
				String dubboResultInfo=meetingSummaryDtoServiceCustomer.updateStatus(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
				DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
				if(dubboServiceResultInfo.isSucess()){
						Integer i=JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), Integer.class);
						result.setResult(i);
						result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
						result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
					}else{
						result.setSuccess(MessageInfo.UPDATEERROR.isResult());
						result.setMsg(dubboServiceResultInfo.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
				}
		
			} catch (Exception e) {
					log.error("调用update方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
					result.setSuccess(MessageInfo.UPDATEERROR.isResult());
					result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
			}
			return result;
		}
	@RequestMapping(value="/existence",method=RequestMethod.POST,consumes="application/json")
	public @ResponseBody MessageResult existMeetingSummary(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
		try {
			String dubboResultInfo=meetingSummaryDtoServiceCustomer.existMeetingSummary(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				Integer i=JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), Integer.class);
				result.setResult(i);
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}else{
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}

		} catch (Exception e) {
			log.error("调用update方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		return result;
	}

	private void setUserAuthInfo(SecurityUserBeanInfo securityUserBeanInfo,SecurityUserDto securityUserDto){
		try {
			String authenticationInfodubboResultInfo = authenticationDtoServiceCustomer.getUserAuthenticationInfoWithoutResource(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson(securityUserDto));
			DubboServiceResultInfo authenticationInfodubboServiceResultInfo= JacksonUtils.fromJson(authenticationInfodubboResultInfo, DubboServiceResultInfo.class);
			if(authenticationInfodubboServiceResultInfo.isSucess()){
				String authenticationInforesultInfo= authenticationInfodubboServiceResultInfo.getResult();
				AuthenticationDto authenticationDto=JacksonUtils.fromJson(authenticationInforesultInfo, AuthenticationDto.class);
				//获取用户标准岗位
				List<SecurityStandardRoleDto> securityStandardRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getStandardRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
				securityUserBeanInfo.setSecurityStandardRoleDtoList(securityStandardRoleDtoList);

				//获取用户通用角色
				List<SecurityStandardRoleDto> securityCurrencyRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getCurrencyRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
				securityUserBeanInfo.setSecurityCurrencyRoleDtoList(securityCurrencyRoleDtoList);

				//获取用户岗位
				List<SecurityPostDto> securityPostDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getPostDtoList()),ArrayList.class,SecurityPostDto.class);
				securityUserBeanInfo.setSecurityPostDtoList(securityPostDtoList);
                   /* //当前用户的菜单清单（未授权和已授权的）
                    List<SecurityResourceDto> SecurityResourceDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getResourceDtoList()),ArrayList.class,SecurityResourceDto.class);
                    securityUserBeanRelationInfo.setResourceDtoList(SecurityResourceDtoList);*/
				//当前用户所在组织的类型
				String securityOrganizationType = authenticationDto.getOrganizationType();
				securityUserBeanInfo.setSecurityOrganizationType(securityOrganizationType);
				//当前用户的一级公司
				SecurityOrganizationDto securityTopCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopCompanyDto()),SecurityOrganizationDto.class);
				securityUserBeanInfo.setSecurityTopCompanyDto(securityTopCompanyDto);
				//当前用户的直属公司
				SecurityOrganizationDto securityDirectCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectCompanyDto()),SecurityOrganizationDto.class);
				securityUserBeanInfo.setSecurityDirectCompanyDto(securityDirectCompanyDto);
				//当前用户的一级部门
				SecurityOrganizationDto securityTopDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopDeptDto()),SecurityOrganizationDto.class);
				securityUserBeanInfo.setSecurityTopDeptDto(securityTopDeptDto);
				//当前用户的直属部门
				SecurityOrganizationDto securityDirectDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectDeptDto()),SecurityOrganizationDto.class);
				securityUserBeanInfo.setSecurityDirectDeptDto(securityDirectDeptDto);
				//当前用户的项目
				SecurityOrganizationDto securityGroupDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getGroupDto()),SecurityOrganizationDto.class);
				securityUserBeanInfo.setSecurityGroupDto(securityGroupDto);
				//当前用户的分期
				SecurityOrganizationDto securityBranchDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getBranchDto()),SecurityOrganizationDto.class);
				securityUserBeanInfo.setSecurityBranchDto(securityBranchDto);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
