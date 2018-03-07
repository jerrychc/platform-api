package com.xinleju.platform.sys.org.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.out.app.org.service.OrgnazationOutServiceCustomer;
import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import com.xinleju.platform.sys.org.dto.OrgnazationExcelDto;
import com.xinleju.platform.sys.org.dto.service.OrgnazationDtoServiceCustomer;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.InvalidCustomException;
import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.platform.uitls.OpeLogInfo;
import com.xinleju.platform.uitls.openOffice.utils.ExcelReadOrg;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 组织结构控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/sys/org/orgnazation")
public class OrgnazationController {
 
	private static Logger log = Logger.getLogger(OrgnazationController.class);
	
	@Autowired
	private OrgnazationDtoServiceCustomer orgnazationDtoServiceCustomer;
	
	@Autowired
	private OrgnazationOutServiceCustomer orgnazationOutServiceCustomer;
	/*@Autowired
	private UserOutServiceCustomer userOutServiceCustomer;*/
//	@Autowired
//	private UserSerivce userSerivce;
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
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				OrgnazationDto orgnazationDto=JacksonUtils.fromJson(resultInfo, OrgnazationDto.class);
				result.setResult(orgnazationDto);
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
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
		    String dubboResultInfo=orgnazationDtoServiceCustomer.getPage(userJson, paramaterJson);
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
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<OrgnazationDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,OrgnazationDto.class);
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
	@OpeLogInfo(node="新增组织机构")
	public @ResponseBody MessageResult save(@RequestBody OrgnazationDto t){
		MessageResult result=new MessageResult();
		try {
			String saveJson= JacksonUtils.toJson(t);
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.save(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				OrgnazationDto orgnazationDto=JacksonUtils.fromJson(resultInfo, OrgnazationDto.class);
				result.setResult(orgnazationDto);
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
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@OpeLogInfo(node="根据id删除组织机构")
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.deleteObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				OrgnazationDto orgnazationDto=JacksonUtils.fromJson(resultInfo, OrgnazationDto.class);
				result.setResult(orgnazationDto);
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
	@OpeLogInfo(node="根据id批量删除组织机构")
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.deleteAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				OrgnazationDto orgnazationDto=JacksonUtils.fromJson(resultInfo, OrgnazationDto.class);
				result.setResult(orgnazationDto);
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
	@OpeLogInfo(node="根据id批量修改组织机构")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		OrgnazationDto orgnazationDto=null;
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 resultInfo=resultInfo.replaceAll("\\\\", "\\\\\\\\");
				 resultInfo=resultInfo.replaceAll("\\'", "\\\\\\\\\'");
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=orgnazationDtoServiceCustomer.update(userJson, updateJson);
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
			 String  paramJson = mapper.writeValueAsString(orgnazationDto);
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
	 * 禁用启用组织结构
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/updateOrgStatus",method=RequestMethod.PUT,consumes="application/json")
	@OpeLogInfo(node="启用或禁用组织机构")
	public @ResponseBody MessageResult updateOrgStatus(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.updateOrgStatus(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				result.setResult(dubboServiceResultInfo.getResult());
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }else{
		    	result.setResult(dubboServiceResultInfo.getResult());
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }
		} catch (Exception e) {
			////e.printStackTrace();
			ObjectMapper mapper = new ObjectMapper();
			log.error("调用update方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}


	/**
	 * 获取符合条件的根目录下树列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/updateOrgSort",method={RequestMethod.POST}, consumes="application/json")
	@OpeLogInfo(node="拖拽组织机构")
	public @ResponseBody MessageResult updateOrgSort(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//暂时查询的是一级根目录下的树
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.updateOrgSort(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				result.setResult(dubboServiceResultInfo.getResult());
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }else{
		    	result.setResult(dubboServiceResultInfo.getResult());
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用getTree方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(false);
			result.setMsg("更新失败---"+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 获取所有的公司 (只查公司)
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryListCompany",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryListCompany(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.queryListCompany(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
			  List<Map<String,Object>> list=JacksonUtils.fromJson(resultInfo, ArrayList.class);
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
	 * 获取所有的公司树 (只查公司)
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryListCompanyTree",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryListCompanyTree(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.queryListCompanyTree(userJson, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<Map<String,Object>> list=JacksonUtils.fromJson(resultInfo, ArrayList.class);
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
	 * 获取所有的公司和集团树 
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryListCompanyAndZb",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryListCompanyAndZb(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.queryListCompanyAndZb(userJson, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<Map<String,Object>> list=JacksonUtils.fromJson(resultInfo, ArrayList.class);
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
	 * 根据公司获取分期
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryBrachByCompanyId",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryBrachByCompanyId(@RequestBody Map<String,Object> map){
		String id = (String) map.get("companyIds");
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationOutServiceCustomer.getAllProjectBrachListByCompanyIds(userJson, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map<String,Object> resultMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				List<OrgnazationDto> list = (List<OrgnazationDto>) resultMap.get(id);
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
	 * 伪删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	@OpeLogInfo(node="根据id伪删除组织机构")
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.deletePseudoObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				OrgnazationDto orgnazationDto=JacksonUtils.fromJson(resultInfo, OrgnazationDto.class);
				result.setResult(orgnazationDto);
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
	@OpeLogInfo(node="根据id批量伪删除组织机构")
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.deletePseudoAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				OrgnazationDto orgnazationDto=JacksonUtils.fromJson(resultInfo, OrgnazationDto.class);
				result.setResult(orgnazationDto);
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
	 * 修改组织机构状态
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/updateStatus",method={RequestMethod.POST}, consumes="application/json")
	@OpeLogInfo(node="修改组织机构状态")
	public @ResponseBody MessageResult updateStatus(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.updateStatus(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Integer i=JacksonUtils.fromJson(resultInfo, Integer.class);
				result.setResult(i);
				result.setSuccess(MessageInfo.UPDATESTATUSSUCCESS.isResult());
				result.setMsg(MessageInfo.UPDATESTATUSSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.UPDATESTATUSERROR.isResult());
				result.setMsg(MessageInfo.UPDATESTATUSERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用updateStatus方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.UPDATESTATUSERROR.isResult());
			result.setMsg(MessageInfo.UPDATESTATUSERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	
	/**
	 * 根据IDs获取结果集（组织、角色、岗位、人员）
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryResListByIds",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryResListByIds(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.queryResListByIds(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<Map> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,Map.class);
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
	
	@RequestMapping(value="/userRPOM",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult userRPOM(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.getUserRPOMInfoByUserId(userJson, JacksonUtils.toJson(map));	
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AuthenticationDto orgnazationDto=JacksonUtils.fromJson(resultInfo, AuthenticationDto.class);
				result.setResult(orgnazationDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			log.error("调用get方法:  【参数"+JacksonUtils.toJson(map)+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	//查询用户所有组织信息：所属组织U岗位组织
	@RequestMapping(value="/getUserAllOrgs",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getUserAllOrgs(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.getUserAllOrgs(userJson, JacksonUtils.toJson(map));	
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<OrgnazationDto> list=JacksonUtils.fromJson(resultInfo, List.class,OrgnazationDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			log.error("调用get方法:  【参数"+JacksonUtils.toJson(map)+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	//查询部门 或项目分期 （包含集团和公司）
	
	@RequestMapping(value="/getDeptOrBranch/{qType}",method=RequestMethod.POST,consumes="application/json")
	public @ResponseBody MessageResult getDeptOrBranch(@PathVariable("qType")  String qType,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.getDeptOrBranch(userJson,"{\"qType\":\""+qType+"\"}");	
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<Map<String,Object>> list=JacksonUtils.fromJson(resultInfo, List.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用get方法:  【参数"+qType+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/*//测试dubbo
	@RequestMapping(value="/orgTest",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult orgTest(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
//			String dubboResultInfo=userOutServiceCustomer.getUserListByRoleIds(userJson, JacksonUtils.toJson(map));	
			String dubboResultInfo=orgnazationOutServiceCustomer.getUserDataAuthGroupAndBranchList(userJson, JacksonUtils.toJson(map));	
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map<String,Object> list=JacksonUtils.fromJson(resultInfo, HashMap.class);
//					List<Map<String,Object>> list=JacksonUtils.fromJson(resultInfo, List.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用get方法:  【参数"+JacksonUtils.toJson(map)+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}*/
	//根据岗位id获取其上级组织id
	@RequestMapping(value="/getOrgsByPostId",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getOrgsByPostId(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.getOrgsByPostId(userJson, JacksonUtils.toJson(map));	
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<OrgnazationDto> list=JacksonUtils.fromJson(resultInfo, List.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用post方法:  【参数"+JacksonUtils.toJson(map)+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	
	/**
	 * 复制粘贴组织结构
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/copyAndPasteOrg",method={RequestMethod.POST}, consumes="application/json")
	@OpeLogInfo(node="复制粘贴组织结构")
	public @ResponseBody MessageResult copyAndPasteOrg(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=orgnazationDtoServiceCustomer.copyAndPasteOrg(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
		    	String resultInfo= dubboServiceResultInfo.getResult();
		    	Map<String, OrgnazationDto> list=JacksonUtils.fromJson(resultInfo, HashMap.class);
				result.setResult(list);
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }else{
		    	result.setResult(dubboServiceResultInfo.getResult());
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg()+dubboServiceResultInfo.getExceptionMsg());
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用copyAndPasteOrg方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(false);
			result.setMsg("更新失败---"+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 读取Excel数据到数据库
	 * @param file excel文件
	 * @param parentId 父节点id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/readExcelForOrg")
//	@OpeLogInfo(node="导入组织")
	public @ResponseBody MessageResult readExcelForOrg(@RequestParam(value="excelFile") MultipartFile file, String parentId) throws IOException {
		MessageResult result=new MessageResult();
		//判断文件是否为空
		if(file == null){
			result.setSuccess(false);
			result.setMsg("文件不可为空");
			return result;
		}
		if(StringUtils.isBlank(parentId)){
			result.setSuccess(false);
			result.setMsg("导入节点不存在");
			return result;
		}
		//读取Excel数据到List中
		List<OrgnazationExcelDto> list = null;
		try {
			Long t1 = System.currentTimeMillis();
			 list= new ExcelReadOrg().readExcel(file);
			Long t2 = System.currentTimeMillis();
			System.out.println("read excel...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+(t2-t1));
		}catch (InvalidCustomException e){
			result.setSuccess(false);
			result.setMsg(e.getMsg());
			return result;
		}
		if (list != null && list.size() >0){
			if(list.size() > 5000){
				result.setSuccess(false);
				result.setMsg("您导入的数据太多，不要超过5000，请分批导入");
				return result;
			}
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			Long t2 = System.currentTimeMillis();
			String dubboResultInfo = orgnazationDtoServiceCustomer.readExcelAndInsert(userJson,list,parentId);
			Long t3 = System.currentTimeMillis();
			System.out.println("save data...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+(t3-t2));
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map<String, Object> c = JacksonUtils.fromJson(resultInfo, HashMap.class);
				result.setResult(c);
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}else{
				result.setResult(dubboServiceResultInfo.getResult());
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg()+dubboServiceResultInfo.getExceptionMsg());
			}
			Long t4 = System.currentTimeMillis();
			System.out.println("save data...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+(t4-t3));
		}else{
			result.setSuccess(false);
			result.setMsg("上传内容为空，或excel模板不合法");
		}
		return result;
	}
	/**
	 * 文件下载
	 * @Description:
	 * @param fileName
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/download")
	public String downloadFile(@RequestParam("fileName") String fileName,HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (fileName != null) {
			String realPath = request.getServletContext().getRealPath(
					"WEB-INF/File/");
			File file = new File(realPath, fileName);
			if (file.exists()) {
				response.setContentType("application/force-download");// 设置强制下载不打开
				//设置文件名
//				response.addHeader("Content-Disposition", "attachment;filename="+  fileName);
				response.addHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("GB2312"),"ISO-8859-1"));
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}

}
