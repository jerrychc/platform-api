package com.xinleju.platform.sys.res.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xinleju.platform.out.app.log.service.LogOutServiceCustomer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.cloud.oa.sys.dto.SysLinkCenterDto;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.sys.res.dto.AppSystemDto;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.dto.service.AppSystemDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.platform.uitls.OpeLogInfo;

import org.springframework.web.multipart.MultipartHttpServletRequest;


/**
 * 系统控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/sys/res/appSystem")
public class AppSystemController {

	private static Logger log = Logger.getLogger(AppSystemController.class);
	@Autowired
	private AppSystemDtoServiceCustomer appSystemDtoServiceCustomer;

	@Autowired
	private LogOutServiceCustomer logOutServiceCustomer;
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
			String userInfo = JacksonUtils.toJson(LoginUtils.getSecurityUserBeanInfo());
			String dubboResultInfo=appSystemDtoServiceCustomer.getObjectById(userInfo, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AppSystemDto appSystemDto=JacksonUtils.fromJson(resultInfo, AppSystemDto.class);
				result.setResult(appSystemDto);
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
		    String dubboResultInfo=appSystemDtoServiceCustomer.getPage(userJson, paramaterJson);
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
			String dubboResultInfo=appSystemDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<AppSystemDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,AppSystemDto.class);
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
	@RequestMapping(value="/queryListForSelect",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryListForSelect(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=appSystemDtoServiceCustomer.queryListForSelect(userJson, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<AppSystemDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,AppSystemDto.class);
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
/*	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody AppSystemDto t){
		MessageResult result=new MessageResult();
		try {
			String saveJson= JacksonUtils.toJson(t);
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=appSystemDtoServiceCustomer.save(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AppSystemDto appSystemDto=JacksonUtils.fromJson(resultInfo, AppSystemDto.class);
				result.setResult(appSystemDto);
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
				//e1.printStackTrace();
			}
			
		}
		return result;
	}*/
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	//@OpeLogInfo(node="新增系统")
	public void save(MultipartHttpServletRequest request, HttpServletResponse response) {
		PrintWriter pw = null;
		Boolean isReturn = false;
		try {
			response.setContentType("text/html;charset=UTF-8");
			pw = response.getWriter();
			MultipartFile uploadfile =  request.getFile("icon");
			MessageResult result = new MessageResult();
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(null!=uploadfile){
				long length = uploadfile.getSize();
				if(length>1*1024*1024){
					result.setSuccess(false);
					result.setMsg("图片尺寸不能大于1M");
					pw.print(JacksonUtils.toJson(result));
					pw.flush();
					isReturn = true;
				}
			}
			if(!isReturn) {
				String id = request.getParameter("id");
				String isDelPic = request.getParameter("isDelPic");
				String name = request.getParameter("name");
				String code = request.getParameter("code");
				String fullName = request.getParameter("fullName");
				String url = request.getParameter("url");
				String isextsys = request.getParameter("isextsys");
				String status = request.getParameter("status");
				String openmode = request.getParameter("openmode");
				String remark = request.getParameter("remark");
				byte[] headpic = {};
				if (null != uploadfile) {
					InputStream is = uploadfile.getInputStream();
					headpic = new byte[is.available()];
					is.read(headpic);
					is.close();
				}
				AppSystemDto appSystemDto = new AppSystemDto();
				appSystemDto.setCode(code);
				appSystemDto.setId(id);
				appSystemDto.setName(name);
				appSystemDto.setStatus(status);
				appSystemDto.setUrl(url);
				appSystemDto.setIcon(headpic);
				appSystemDto.setRemark(remark);
				appSystemDto.setDelflag(false);
				appSystemDto.setIsextsys(isextsys);
				appSystemDto.setFullName(fullName);
				appSystemDto.setOpenmode(openmode);
				String saveJson = JacksonUtils.toJson(appSystemDto);
				String dubboResultInfo = appSystemDtoServiceCustomer.save(
						userJson, saveJson);
				DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
						.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
				if (dubboServiceResultInfo.isSucess()) {
					String resultInfo = dubboServiceResultInfo.getResult();
					AppSystemDto appSystemDtoResult = JacksonUtils.fromJson(
							resultInfo, AppSystemDto.class);
					result.setResult(appSystemDtoResult);
					result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
					result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
				} else {
					result.setSuccess(MessageInfo.SAVEERROR.isResult());
					result.setMsg(dubboServiceResultInfo.getMsg());
				}
				Map<String, Object> param=new HashMap<String, Object>();
				param.put("loginIp", getIpAddress(request));
				param.put("loginBrowser", request.getHeader("User-Agent"));
				param.put("node", "新增系统");//操作
				param.put("sysCode", "PT");
				param.put("note", String.format("入参：【%s】，结果：【%s】",saveJson, JacksonUtils.toJson(result)));//结果
				logOutServiceCustomer.saveOpeLog(userJson, JacksonUtils.toJson(param));

				pw.print(JacksonUtils.toJson(result));
				pw.flush();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			try {
				pw.close();
			} catch (Exception e){}
		}
	}
	/**
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@OpeLogInfo(node="根据id系统")
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=appSystemDtoServiceCustomer.deleteObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AppSystemDto appSystemDto=JacksonUtils.fromJson(resultInfo, AppSystemDto.class);
				result.setResult(appSystemDto);
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
	@OpeLogInfo(node="根据ids批量删除系统")
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=appSystemDtoServiceCustomer.deleteAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AppSystemDto appSystemDto=JacksonUtils.fromJson(resultInfo, AppSystemDto.class);
				result.setResult(appSystemDto);
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
	 * 伪删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	@OpeLogInfo(node="根据id伪删除系统")
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=appSystemDtoServiceCustomer.deletePseudoObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AppSystemDto appSystemDto=JacksonUtils.fromJson(resultInfo, AppSystemDto.class);
				result.setResult(appSystemDto);
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
	@OpeLogInfo(node="根据ids批量伪删除系统")
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=appSystemDtoServiceCustomer.deletePseudoAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AppSystemDto appSystemDto=JacksonUtils.fromJson(resultInfo, AppSystemDto.class);
				result.setResult(appSystemDto);
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
	 * 修改修改实体对象
	 * @return
	 */
	@RequestMapping(value="/updateStatus/{id}",method=RequestMethod.PUT,consumes="application/json")
	@OpeLogInfo(node = "启用或禁用系统")
	public @ResponseBody MessageResult updateStatus(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		AppSystemDto appSystemDto=null;
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=appSystemDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=appSystemDtoServiceCustomer.update(userJson, updateJson);
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
			 String  paramJson = mapper.writeValueAsString(appSystemDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
			}catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
		}
		return result;
	}
	@RequestMapping(value = "/update")
	@OpeLogInfo(node="修改系统")
	public void update(MultipartHttpServletRequest request, HttpServletResponse response) {
		MessageResult result = new MessageResult();
		PrintWriter pw = null;
		Boolean isReturn = false;
		try {
			response.setContentType("text/html;charset=UTF-8");
			pw = response.getWriter();
			MultipartFile uploadfile =  request.getFile("icon");
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(null!=uploadfile){
				long length = uploadfile.getSize();
				if(length>1*1024*1024){
					result.setSuccess(false);
					result.setMsg("图片尺寸不能大于1M");
					pw.print(JacksonUtils.toJson(result));
					pw.flush();
					isReturn = true;
				}
			}
			if(!isReturn) {
				String id = request.getParameter("id");
				String dubboResultInfo = appSystemDtoServiceCustomer.getObjectById(userJson, "{\"id\":\"" + id + "\"}");
				DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
						.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
				AppSystemDto pDto = new AppSystemDto();
				pDto.setId(id);
				if (dubboServiceResultInfo.isSucess()) {
					String resultInfo = dubboServiceResultInfo.getResult();
					AppSystemDto appSystemDto = JacksonUtils.fromJson(resultInfo,
							AppSystemDto.class);
					String isDelPic = request.getParameter("isDelPic");
					String name = request.getParameter("name");
					String code = request.getParameter("code");
					String fullName = request.getParameter("fullName");
					String url = request.getParameter("url");
					String isextsys = request.getParameter("isextsys");
					String status = request.getParameter("status");
					String openmode = request.getParameter("openmode");
					String remark = request.getParameter("remark");
					appSystemDto.setCode(code);
					appSystemDto.setId(id);
					appSystemDto.setStatus(status);
					appSystemDto.setName(name);
					appSystemDto.setRemark(remark);
					appSystemDto.setUrl(url);
					appSystemDto.setDelflag(false);
					appSystemDto.setIsextsys(isextsys);
					appSystemDto.setFullName(fullName);
					appSystemDto.setOpenmode(openmode);
					byte[] headpic = {};
					if (null != uploadfile) {
						if(uploadfile.getSize()>0){
							InputStream is = uploadfile.getInputStream();
							headpic = new byte[is.available()];
							is.read(headpic);
							is.close();
							appSystemDto.setIcon(headpic);
						}
					}else if("0".equals(isDelPic)){
						appSystemDto.setIcon(null);
					}
					pDto = appSystemDto;
					String updateJson = JacksonUtils.toJson(appSystemDto);
					String updateDubboResultInfo = appSystemDtoServiceCustomer
							.update(userJson, updateJson);
					DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils
							.fromJson(updateDubboResultInfo,
									DubboServiceResultInfo.class);
					if (updateDubboServiceResultInfo.isSucess()) {
						Integer i = JacksonUtils.fromJson(
								updateDubboServiceResultInfo.getResult(),
								Integer.class);
						result.setResult(i);
						result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
						result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
					} else {
						result.setSuccess(MessageInfo.UPDATEERROR.isResult());
						result.setMsg(updateDubboServiceResultInfo.getMsg());
					}
				} else {
					result.setSuccess(MessageInfo.UPDATEERROR.isResult());
					result.setMsg("不存在更新的对象");
				}

				Map<String, Object> param=new HashMap<String, Object>();
				param.put("loginIp", getIpAddress(request));
				param.put("loginBrowser", request.getHeader("User-Agent"));
				param.put("node", "修改系统");//操作
				param.put("sysCode", "PT");
				param.put("note", String.format("入参：【%s】，结果：【%s】",JacksonUtils.toJson(pDto), JacksonUtils.toJson(result)));//结果
				logOutServiceCustomer.saveOpeLog(userJson, JacksonUtils.toJson(param));
				pw.print(JacksonUtils.toJson(result));
				pw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			pw.print(JacksonUtils.toJson(result));
			pw.flush();
		}finally {
			try {
				pw.close();
			} catch (Exception e){}
		}
	}
	/**
	 * 上移或下移
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/upOrDown",method=RequestMethod.PUT,consumes="application/json")
	@OpeLogInfo(node="上移或下移系统")
	public @ResponseBody MessageResult upOrDown(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String updateJson= JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userinfo=LoginUtils.getSecurityUserBeanInfo();
			String userJson= JacksonUtils.toJson(userinfo);
			String updateDubboResultInfo=appSystemDtoServiceCustomer.upOrDown(userJson, updateJson);
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
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用upOrDown方法:  【参数"+updateJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 根据系统id获取数据对象控制点
	 * @param paramater(系统id)
	 * @return
	 */
	@RequestMapping(value="/getDataTree",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getDataTree(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
		    String dubboResultInfo=appSystemDtoServiceCustomer.getDataTree(userJson,paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<DataNodeDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,DataNodeDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用getTree方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	/**
	 * 根据系统id获取功能控制点(全部)
	 * @param paramater(系统id)
	 * @return
	 */
	@RequestMapping(value="/getAllOperationTree",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getAllOperationTree(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
		    String dubboResultInfo=appSystemDtoServiceCustomer.getOperationTree(userJson,paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		   if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<DataNodeDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,DataNodeDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用getTree方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
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
	@RequestMapping(value="/queryListToSupplier",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryListToSupplier(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=appSystemDtoServiceCustomer.queryListData(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<Map<String,Object>> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,Map.class);
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
	 * 获取对象的真实IP地址
	 *
	 */
	public  String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_REAL_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
