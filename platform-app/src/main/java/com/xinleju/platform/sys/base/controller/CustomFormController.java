package com.xinleju.platform.sys.base.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.ErrorInfoCode;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.sys.base.dto.CustomArchivesItemDto;
import com.xinleju.platform.sys.base.dto.CustomFormDto;
import com.xinleju.platform.sys.base.dto.service.CustomFormDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 自定义表单控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/sys/base/customForm")
public class CustomFormController {

	private static Logger log = Logger.getLogger(CustomFormController.class);

	@Autowired
	private CustomFormDtoServiceCustomer customFormDtoServiceCustomer;
	
//	@Autowired
//	private FlDtoServiceCustomer flDtoServiceCustomer;
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
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.getObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormDto customFormDto=JacksonUtils.fromJson(resultInfo, CustomFormDto.class);
				customFormDto.setUserType(securityUserBeanInfo.getSecurityUserDto().getType());
				result.setResult(customFormDto);
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
	 * 根据Id获取业务对象
	 * 
	 * @param id  业务对象主键
	 * 
	 * @return     业务对象
	 */
	@RequestMapping(value="/getForUpdate/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult getForUpdate(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.getObjectByIdForUpdate(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormDto customFormDto=JacksonUtils.fromJson(resultInfo, CustomFormDto.class);
				result.setResult(customFormDto);
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
	 * 根据Id获取业务对象
	 * 
	 * @param id  业务对象主键
	 * 
	 * @return     业务对象
	 */
	@RequestMapping(value="/getTemplateById/{id}/{cfVid}",method=RequestMethod.GET)
	public @ResponseBody MessageResult getTemplateById(@PathVariable("id")  String id,@PathVariable("cfVid")  String cfVid){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.getTemplateById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\",\"cfVid\":\""+cfVid+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormDto customFormDto=JacksonUtils.fromJson(resultInfo, CustomFormDto.class);
				result.setResult(customFormDto);
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
	
//	@RequestMapping(value="/getTemplateById/{id}",method=RequestMethod.GET)
//	public @ResponseBody MessageResult getTemplateById(@PathVariable("id")  String id){
//		MessageResult result=new MessageResult();
//		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
//		try {
//			String dubboResultInfo=customFormDtoServiceCustomer.getTemplateById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
//			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
//			if(dubboServiceResultInfo.isSucess()){
//				String resultInfo= dubboServiceResultInfo.getResult();
//				CustomFormDto customFormDto=JacksonUtils.fromJson(resultInfo, CustomFormDto.class);
//				result.setResult(customFormDto);
//				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
//				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
//			}else{
//				result.setSuccess(MessageInfo.GETERROR.isResult());
//				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
//			}
//		} catch (Exception e) {
//			////e.printStackTrace();
//		    log.error("调用get方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
//			result.setSuccess(MessageInfo.GETERROR.isResult());
//			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
//		}
//		return result;
//	}
	
	/**
	 * 返回分页对象
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
		    String dubboResultInfo=customFormDtoServiceCustomer.getPage(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
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
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.queryList(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<CustomFormDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,CustomFormDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
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
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryListForQuickEntry",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryListForQuickEntry(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.queryListForQuickEntry(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<CustomFormDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,CustomFormDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
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
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody CustomFormDto t){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String saveJson= JacksonUtils.toJson(t);
			
			String dubboResultInfo=customFormDtoServiceCustomer.validateBeforeSave(JacksonUtils.toJson(securityUserBeanInfo),saveJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormDto customFormDto=JacksonUtils.fromJson(resultInfo, CustomFormDto.class);
				result.setResult(customFormDto);
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
				//e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	/**
	  * @Description:表单数据生成
	  * @author:zhangfangzhi
	  * @date 2017年6月14日 下午7:51:19
	  * @version V1.0
	 */
	@RequestMapping(value="/saveGenerateData",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult saveGenerateData(@RequestBody CustomFormDto t){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String saveJson= JacksonUtils.toJson(t);
			
			String dubboResultInfo=customFormDtoServiceCustomer.saveGenerateData(JacksonUtils.toJson(securityUserBeanInfo),saveJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormDto customFormDto=JacksonUtils.fromJson(resultInfo, CustomFormDto.class);
				result.setResult(customFormDto);
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
				//e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	
	/**
	  * @Description:表单数据生成-费用模板生成
	  * @author:zhangfangzhi
	  * @date 2017年6月14日 下午7:51:19
	  * @version V1.0
	 */
	@RequestMapping(value="/saveGenerateDataEx",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult saveGenerateDataEx(@RequestBody CustomFormDto t){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String saveJson= JacksonUtils.toJson(t);
			
			String dubboResultInfo=customFormDtoServiceCustomer.saveGenerateDataEx(JacksonUtils.toJson(securityUserBeanInfo),saveJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				result.setResult(dubboServiceResultInfo.getResult());
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(dubboServiceResultInfo.getMsg());
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
				//e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	/**
	  * @Description:表单数据生成-费用实例生成
	  * @author:zhangfangzhi
	  * @date 2017年6月14日 下午7:51:19
	  * @version V1.0
	 */
	@RequestMapping(value="/saveGenerateDataExInstance",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult saveGenerateDataExInstance(@RequestBody CustomFormDto t){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String saveJson= JacksonUtils.toJson(t);
			
			String dubboResultInfo=customFormDtoServiceCustomer.saveGenerateDataExInstance(JacksonUtils.toJson(securityUserBeanInfo),saveJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				result.setResult(dubboServiceResultInfo.getResult());
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(dubboServiceResultInfo.getMsg());
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
				//e1.printStackTrace();
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
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.deleteObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormDto customFormDto=JacksonUtils.fromJson(resultInfo, CustomFormDto.class);
				result.setResult(customFormDto);
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
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deleteBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.deleteAllObjectByIds(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormDto customFormDto=JacksonUtils.fromJson(resultInfo, CustomFormDto.class);
				result.setResult(customFormDto);
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
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		CustomFormDto customFormDto=null;
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.getObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				if(map.get("isInner")!=null){
					if("0".equals(String.valueOf(map.get("isInner"))) || "2".equals(String.valueOf(map.get("isInner")))){//外部资源,自定义表单需要清空资源
						map.put("resourceId", null);
						map.put("resourceName", null);
					}
				}
				oldMap.putAll(map);
				String updateJson= JacksonUtils.toJson(oldMap);
				String dubboResultInfoVal=customFormDtoServiceCustomer.validateBeforeUpdate(JacksonUtils.toJson(securityUserBeanInfo),updateJson);
				DubboServiceResultInfo dubboServiceResultInfoVal= JacksonUtils.fromJson(dubboResultInfoVal, DubboServiceResultInfo.class);
			    if(dubboServiceResultInfoVal.isSucess()){
					String resultInfoVal= dubboServiceResultInfoVal.getResult();
					CustomArchivesItemDto customArchivesItemDtoVal=JacksonUtils.fromJson(resultInfoVal, CustomArchivesItemDto.class);
					result.setResult(customArchivesItemDtoVal);
					result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
					result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
			    }else{
			    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
					result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			    }
			}else{
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
			}
		} catch (Exception e) {
			try{
			 ////e.printStackTrace();
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(customFormDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
			}catch (JsonProcessingException e1) {
				//e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	/**
	  * @Description:表单设计发布
	  * @author:zhangfangzhi
	  * @date 2017年11月22日 下午4:01:23
	  * @version V1.0
	 */
	@RequestMapping(value="/updateForPublish/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult updateForPublish(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		CustomFormDto customFormDto=null;
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.getObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				oldMap.putAll(map);
				String updateJson= JacksonUtils.toJson(oldMap);
				String updateDubboResultInfo=customFormDtoServiceCustomer.updateForPublish(JacksonUtils.toJson(securityUserBeanInfo), updateJson);
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
			 String  paramJson = mapper.writeValueAsString(customFormDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
			}catch (JsonProcessingException e1) {
				//e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	/**
	  * @Description:设计修改
	  * @author:zhangfangzhi
	  * @date 2017年4月18日 下午3:12:59
	  * @version V1.0
	 */
	@RequestMapping(value="/updateForDesign/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult updateForDesign(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		CustomFormDto customFormDto=null;
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.getObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				oldMap.putAll(map);
				String updateJson= JacksonUtils.toJson(oldMap);
				String updateDubboResultInfo=customFormDtoServiceCustomer.update(JacksonUtils.toJson(securityUserBeanInfo), updateJson);
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
			 String  paramJson = mapper.writeValueAsString(customFormDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
			}catch (JsonProcessingException e1) {
				//e1.printStackTrace();
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
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.deletePseudoObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormDto customFormDto=JacksonUtils.fromJson(resultInfo, CustomFormDto.class);
				result.setResult(customFormDto);
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
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudoBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.deletePseudoAllObjectByIds(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+ids+"\"}");
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
	  * @Description:获取登录sessionID
	  * @author:zhangfangzhi
	  * @date 2017年4月6日 上午11:21:07
	  * @version V1.0
	 */
	@RequestMapping(value="/getSessionId",method=RequestMethod.GET)
	public @ResponseBody MessageResult getSessionId(){
		MessageResult result=new MessageResult();
		result.setResult(LoginUtils.getSessionId());
		result.setSuccess(MessageInfo.GETSUCCESS.isResult());
		result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		return result;
	}
	
	/**
	  * @Description:复制设计模板
	  * @author:zhangfangzhi
	  * @date 2017年4月19日 下午4:16:31
	  * @version V1.0
	 */
	@RequestMapping(value="/saveTemplate",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult saveTemplate(@RequestBody CustomFormDto t){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String saveJson= JacksonUtils.toJson(t);
			
			String dubboResultInfo=customFormDtoServiceCustomer.validateBeforeCopy(JacksonUtils.toJson(securityUserBeanInfo),saveJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormDto customFormDto=JacksonUtils.fromJson(resultInfo, CustomFormDto.class);
				result.setResult(customFormDto);
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
				//e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	/**
	  * @Description:判断该模板下是否有实例生成
	  * @author:zhangfangzhi
	  * @date 2017年5月25日 上午9:03:12
	  * @version V1.0
	 */
	@RequestMapping(value="/getCount/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult getCount(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.getCount(JacksonUtils.toJson(securityUserBeanInfo),"{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormDto customFormDto=JacksonUtils.fromJson(resultInfo, CustomFormDto.class);
				result.setResult(customFormDto);
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
	
	@RequestMapping(value="/getFormNumber/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult getFormNumber(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.getFormNumber(JacksonUtils.toJson(securityUserBeanInfo),"{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setResult("");
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
	 * 对规则排序(上移/下移/置顶/置底)
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/updateSort/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult updateSort(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=customFormDtoServiceCustomer.updateSort(userJson, "{\"id\":\""+id+"\"}",map);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				int i=JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), Integer.class);
				result.setResult(i);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.UPDATESORTSUCCESS.getMsg());
		    }else{
				result.setCode(dubboServiceResultInfo.getResult());
			    result.setSuccess(dubboServiceResultInfo.isSucess());
			    result.setMsg(dubboServiceResultInfo.getMsg());
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用updateStatus方法:  【参数id"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;	
	}
	
	/**
	  * @Description:启用禁用
	  * @author:zhangfangzhi
	  * @date 2017年7月14日 下午2:17:36
	  * @version V1.0
	 */
	@RequestMapping(value="/enableOrNot",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult enableOrNot(@RequestBody CustomFormDto t){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormDtoServiceCustomer.getObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+t.getId()+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormDto customFormDto=JacksonUtils.fromJson(resultInfo, CustomFormDto.class);
				customFormDto.setStatus(t.getStatus());
				String updateJson= JacksonUtils.toJson(customFormDto);
				String updateDubboResultInfo=customFormDtoServiceCustomer.update(JacksonUtils.toJson(securityUserBeanInfo), updateJson);
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
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
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
				//e1.printStackTrace();
			}
			
		}
		return result;
	}
}
