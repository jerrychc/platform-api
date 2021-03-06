package com.xinleju.cloud.oa.feedback.controller;

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
import com.xinleju.cloud.oa.feedback.dto.FeedBackDto;
import com.xinleju.cloud.oa.feedback.dto.service.FeedBackDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * ���ⷴ�����Ʋ�
 * @author admin
 *
 */
@Controller
@RequestMapping("/oa/feedBack")
public class FeedBackController {

	private static Logger log = Logger.getLogger(FeedBackController.class);
	
	@Autowired
	private FeedBackDtoServiceCustomer feedBackDtoServiceCustomer;
	/**
	 * ����Id��ȡҵ�����
	 * 
	 * @param id  ҵ���������
	 * 
	 * @return     ҵ�����
	 */
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult get(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=feedBackDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				FeedBackDto feedBackDto=JacksonUtils.fromJson(resultInfo, FeedBackDto.class);
				result.setResult(feedBackDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"��"+dubboServiceResultInfo.getExceptionMsg()+"��");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("����get����:  ������"+id+"��======"+"��"+e.getMessage()+"��");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"��"+e.getMessage()+"��");
		}
		return result;
	}
	
	
	/**
	 * ���ط�ҳ����
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
		    String dubboResultInfo=feedBackDtoServiceCustomer.getPage(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"��"+dubboServiceResultInfo.getExceptionMsg()+"��");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("����page����:  ������"+paramaterJson+"��======"+"��"+e.getMessage()+"��");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"��"+e.getMessage()+"��");
		}
		return result;
	}
	@RequestMapping(value="/getFeedBackPage",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getFeedBackPage(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=feedBackDtoServiceCustomer.getFeedBackPage(userJson, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+dubboServiceResultInfo.getExceptionMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询失败");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+e.getMessage());
		}
		return result;
	}
	/**
	 * ���ط����������б�
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=feedBackDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<FeedBackDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,FeedBackDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"��"+dubboServiceResultInfo.getExceptionMsg()+"��");
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("����queryList����:  ������"+paramaterJson+"��======"+"��"+e.getMessage()+"��");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"��"+e.getMessage()+"��");
		}
		return result;
	}


	/**
	 * ����ʵ�����
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody FeedBackDto t){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=feedBackDtoServiceCustomer.save(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				FeedBackDto feedBackDto=JacksonUtils.fromJson(resultInfo, FeedBackDto.class);
				result.setResult(feedBackDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"��"+dubboServiceResultInfo.getExceptionMsg()+"��");
		    }
		} catch (Exception e) {
			try {
				e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("����save����:  ������"+paramJson+"��======"+"��"+e.getMessage()+"��");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"��"+e.getMessage()+"��");
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	/**
	 * ɾ��ʵ�����
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=feedBackDtoServiceCustomer.deleteObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				FeedBackDto feedBackDto=JacksonUtils.fromJson(resultInfo, FeedBackDto.class);
				result.setResult(feedBackDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"��"+dubboServiceResultInfo.getExceptionMsg()+"��");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("����delete����:  ������"+id+"��======"+"��"+e.getMessage()+"��");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"��"+e.getMessage()+"��");
		}
		
		return result;
	}
	
	
	/**
	 * ɾ��ʵ�����
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deleteBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=feedBackDtoServiceCustomer.deleteAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				FeedBackDto feedBackDto=JacksonUtils.fromJson(resultInfo, FeedBackDto.class);
				result.setResult(feedBackDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"��"+dubboServiceResultInfo.getExceptionMsg()+"��");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("����delete����:  ������"+ids+"��======"+"��"+e.getMessage()+"��");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"��"+e.getMessage()+"��");
		}
		
		return result;
	}
	
	/**
	 * �޸��޸�ʵ�����
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		FeedBackDto feedBackDto=null;
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=feedBackDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=feedBackDtoServiceCustomer.update(userJson, updateJson);
				 DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
				 if(updateDubboServiceResultInfo.isSucess()){
					 Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
					 result.setResult(i);
					 result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
					 result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
				 }else{
					 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
					 result.setMsg(updateDubboServiceResultInfo.getMsg()+"��"+updateDubboServiceResultInfo.getExceptionMsg()+"��");
				 }
			}else{
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("�����ڸ��µĶ���");
			}
		} catch (Exception e) {
			try{
			 e.printStackTrace();
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(feedBackDto);
			 log.error("����update����:  ������"+id+","+paramJson+"��======"+"��"+e.getMessage()+"��");
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"��"+e.getMessage()+"��");
			}catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		return result;
	}

	/**
	 * αɾ��ʵ�����
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=feedBackDtoServiceCustomer.deletePseudoObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				FeedBackDto feedBackDto=JacksonUtils.fromJson(resultInfo, FeedBackDto.class);
				result.setResult(feedBackDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"��"+dubboServiceResultInfo.getExceptionMsg()+"��");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("����deletePseudo����:  ������"+id+"��======"+"��"+e.getMessage()+"��");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"��"+e.getMessage()+"��");
		}
		
		return result;
	}
	
	
	/**
	 * αɾ��ʵ�����
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudoBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=feedBackDtoServiceCustomer.deletePseudoAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				FeedBackDto feedBackDto=JacksonUtils.fromJson(resultInfo, FeedBackDto.class);
				result.setResult(feedBackDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"��"+dubboServiceResultInfo.getExceptionMsg()+"��");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("����deletePseudoBatch����:  ������"+ids+"��======"+"��"+e.getMessage()+"��");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"��"+e.getMessage()+"��");
		}
		
		return result;
	}
	
}
