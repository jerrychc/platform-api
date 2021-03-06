package com.xinleju.platform.sys.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.utils.*;
import org.apache.commons.lang.StringUtils;
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
import com.xinleju.platform.finance.dto.SysRegisterDto;
import com.xinleju.platform.finance.dto.service.SysRegisterDtoServiceCustomer;
import com.xinleju.platform.finance.utils.NCSendData;
import com.xinleju.platform.finance.utils.NCXMlParse;
import com.xinleju.platform.sys.base.dto.BaseSupplierDto;
import com.xinleju.platform.sys.base.dto.service.BaseSupplierDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.platform.uitls.OpeLogInfo;


/**
 * 供方档案控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/sys/base/baseSupplier")
public class BaseSupplierController {

	private static Logger log = Logger.getLogger(BaseSupplierController.class);
	
	@Autowired
	private BaseSupplierDtoServiceCustomer baseSupplierDtoServiceCustomer;
	@Autowired
	private SysRegisterDtoServiceCustomer sysRegisterDtoServiceCustomer;
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
			String dubboResultInfo=baseSupplierDtoServiceCustomer.getSupplierAndAccontById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BaseSupplierDto baseSupplierDto=JacksonUtils.fromJson(resultInfo, BaseSupplierDto.class);
				result.setResult(baseSupplierDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg());
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
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object>map ){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
		    String dubboResultInfo=baseSupplierDtoServiceCustomer.getPage(userJson, paramaterJson);
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
			String dubboResultInfo=baseSupplierDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<BaseSupplierDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,BaseSupplierDto.class);
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
	@OpeLogInfo(node="保存供方档案")
	public @ResponseBody MessageResult save(@RequestBody BaseSupplierDto t){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=baseSupplierDtoServiceCustomer.saveSupplierAndAccont(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BaseSupplierDto baseSupplierDto=JacksonUtils.fromJson(resultInfo, BaseSupplierDto.class);
				result.setResult(baseSupplierDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(dubboServiceResultInfo.isSucess ());
				result.setMsg(dubboServiceResultInfo.getMsg ());
				result.setCode (dubboServiceResultInfo.getCode ());
		    }
		} catch (Exception e) {
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName ());
				result.setCode (ErrorInfoCode.SYSTEM_ERROR.getValue ());
		}
		return result;
	}
	
	/**
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@OpeLogInfo(node="删除供方档案")
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=baseSupplierDtoServiceCustomer.deleteSupplierAndAccont(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BaseSupplierDto baseSupplierDto=JacksonUtils.fromJson(resultInfo, BaseSupplierDto.class);
				result.setResult(baseSupplierDto);
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
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=baseSupplierDtoServiceCustomer.deleteAllByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BaseSupplierDto baseSupplierDto=JacksonUtils.fromJson(resultInfo, BaseSupplierDto.class);
				result.setResult(baseSupplierDto);
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
	 * 修改实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	@OpeLogInfo(node="修改供方档案")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		BaseSupplierDto baseSupplierDto=null;
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=baseSupplierDtoServiceCustomer.getSupplierAndAccontById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=baseSupplierDtoServiceCustomer.updateSupplierAndAccont(userJson, updateJson);
				 DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
				 if(updateDubboServiceResultInfo.isSucess()){
					 Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
					 result.setResult(i);
					 result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
					 result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
				 }else{
					 result.setSuccess(updateDubboServiceResultInfo.isSucess ());
					 result.setMsg(updateDubboServiceResultInfo.getMsg());
					 result.setCode (updateDubboServiceResultInfo.getCode ());
				 }
			}else{
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
				 result.setCode (ErrorInfoCode.NULL_ERROR.getValue ());
			}
		} catch (Exception e) {
			result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName ());
			result.setCode (ErrorInfoCode.SYSTEM_ERROR.getValue ());
		}
		return result;
	}
	/**
	 * 对业务对象设置启用 禁用
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/updateStatus/{id}",method=RequestMethod.PUT)
	@OpeLogInfo(node="修改供方档案状态")
	public @ResponseBody MessageResult updateStatus(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=baseSupplierDtoServiceCustomer.updateStatus(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				int i=JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), Integer.class);
				result.setResult(i);
				result.setSuccess(MessageInfo.UPDATESTATUSSUCCESS.isResult());
				result.setMsg(MessageInfo.UPDATESTATUSSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.UPDATESTATUSERROR.isResult());
				result.setMsg(MessageInfo.UPDATESTATUSERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用修改状态方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.UPDATESTATUSERROR.isResult());
			result.setMsg(MessageInfo.UPDATESTATUSERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	/**
	 * 重新推送nc
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/sendMessageToNCAgain/{id}",method=RequestMethod.PUT)
	@OpeLogInfo(node="供方档案重新推送到NC")
	public @ResponseBody MessageResult sendMessageToNCAgain(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=baseSupplierDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				BaseSupplierDto baseSupplierDto=JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), BaseSupplierDto.class);
				String xmlFile = "";
				// NC系统返回的xml字符串
				String res = null;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("status", 1);
				map.put("delflag", 0);
				String paramaterJson = JacksonUtils.toJson(map);
				String dubboResultSysInfo = sysRegisterDtoServiceCustomer.queryList(userJson, paramaterJson);
				DubboServiceResultInfo dubboServiceResultSysInfo= JacksonUtils.fromJson(dubboResultSysInfo, DubboServiceResultInfo.class);
				if(dubboServiceResultSysInfo.isSucess()){
					String resultInfo= dubboServiceResultSysInfo.getResult();
					List<SysRegisterDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysRegisterDto.class);
					if(list != null && list.size() > 0){
						SysRegisterDto sysDto = list.get(0);
						String createJson = JacksonUtils.toJson(baseSupplierDto);
						xmlFile = baseSupplierDtoServiceCustomer.createSyncXml2NC(createJson, sysDto.getSender());
						// NC接口相关
						String webUrl = sysDto.getWebUrl();
						String companyCode = "0001"; // 集团账套？
						// 获得财务系统url
						String url = webUrl + "?account=02&receiver=" + companyCode;
						if (xmlFile != null && !xmlFile.trim().equals("") && !url.trim().equals("") && url != null) {
							res = NCSendData.getPostResponse(url, xmlFile);
						}
						System.out.println("NC返回的信息===="+res);
						if (null != res) {
							if (NCXMlParse.XmlErrorCode(res) >= 0) {
								baseSupplierDto.setFinanceCode(baseSupplierDto.getCode());
								baseSupplierDto.setMessageType("1");// 输出成功
								baseSupplierDto.setMessageInfo("");
								result.setSuccess(MessageInfo.GETSUCCESS.isResult());
								result.setMsg("推送成功");
							} else {
								String errorinfo = NCXMlParse.XmlErrorInfo(res);
								Integer errCode = NCXMlParse.XmlErrorCode(res);
								baseSupplierDto.setMessageType("2");//  输出失败
								if(StringUtils.isBlank(baseSupplierDto.getFinanceCode()))
									baseSupplierDto.setFinanceCode(baseSupplierDto.getCode());
								String error = "错误代码：" + errCode.toString() + " 错误内容："+ errorinfo;
								baseSupplierDto.setMessageInfo(error);
								result.setSuccess(MessageInfo.GETERROR.isResult());
								result.setMsg("推送失败");
							}
						} else {
							baseSupplierDto.setMessageType("0");// 未输出
							baseSupplierDto.setMessageInfo("生成xml文件失败，未输出！");
							result.setSuccess(MessageInfo.GETERROR.isResult());
							result.setMsg("推送失败");
						}
						String updateJson = JacksonUtils.toJson(baseSupplierDto);
						baseSupplierDtoServiceCustomer.update(userJson, updateJson);
					}
			    }else{
			    	result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			    }
			}else{
				result.setSuccess(MessageInfo.UPDATESTATUSERROR.isResult());
				result.setMsg(MessageInfo.UPDATESTATUSERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("重新推送nc方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.UPDATESTATUSERROR.isResult());
			result.setMsg(MessageInfo.UPDATESTATUSERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	/**
	 * 重新推送nc
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/sendMessageToNC/",method=RequestMethod.POST)
	@OpeLogInfo(node="供方档案推送到NC")
	public @ResponseBody MessageResult sendMessageToNC(@RequestBody Map<String,Object> supplierMap){
		MessageResult result=new MessageResult();
		String paramaterJsonMP = JacksonUtils.toJson(supplierMap);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=baseSupplierDtoServiceCustomer.queryList(userJson, paramaterJsonMP);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				List<BaseSupplierDto> baseSupplierDtoList=JacksonUtils.fromJson(dubboServiceResultInfo.getResult(),ArrayList.class, BaseSupplierDto.class);
				String xmlFile = "";
				// NC系统返回的xml字符串
				String res = null;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("status", 1);
				map.put("delflag", 0);
				String paramaterJson = JacksonUtils.toJson(map);
				String dubboResultSysInfo = sysRegisterDtoServiceCustomer.queryList(userJson, paramaterJson);
				DubboServiceResultInfo dubboServiceResultSysInfo= JacksonUtils.fromJson(dubboResultSysInfo, DubboServiceResultInfo.class);
				if(dubboServiceResultSysInfo.isSucess()){
					String resultInfo= dubboServiceResultSysInfo.getResult();
					List<SysRegisterDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysRegisterDto.class);
					if(list != null && list.size() > 0){
						SysRegisterDto sysDto = list.get(0);
						
						// NC接口相关
						String webUrl = sysDto.getWebUrl();
						String companyCode = "0001"; // 集团账套？
						// 获得财务系统url
						String url = webUrl + "?account=02&receiver=" + companyCode;
						for(BaseSupplierDto baseSupplierDto : baseSupplierDtoList){
							String createJson = JacksonUtils.toJson(baseSupplierDto);
							xmlFile = baseSupplierDtoServiceCustomer.createSyncXml2NC(createJson, sysDto.getSender());
							if (xmlFile != null && !xmlFile.trim().equals("") && !url.trim().equals("") && url != null) {
								res = NCSendData.getPostResponse(url, xmlFile);
							}
							System.out.println("NC返回的信息===="+res);
							if (null != res) {
								if (NCXMlParse.XmlErrorCode(res) >= 0) {
									baseSupplierDto.setFinanceCode(baseSupplierDto.getCode());
									baseSupplierDto.setMessageType("1");// 输出成功
									baseSupplierDto.setMessageInfo("");
									result.setSuccess(MessageInfo.GETSUCCESS.isResult());
									result.setMsg("推送成功");
								} else {
									String errorinfo = NCXMlParse.XmlErrorInfo(res);
									Integer errCode = NCXMlParse.XmlErrorCode(res);
									baseSupplierDto.setMessageType("2");//  输出失败
									if(StringUtils.isBlank(baseSupplierDto.getFinanceCode()))
										baseSupplierDto.setFinanceCode(baseSupplierDto.getCode());
									String error = "错误代码：" + errCode.toString() + " 错误内容："+ errorinfo;
									baseSupplierDto.setMessageInfo(error);
									result.setSuccess(MessageInfo.GETERROR.isResult());
									result.setMsg("推送失败");
								}
							} else {
								baseSupplierDto.setMessageType("0");// 未输出
								baseSupplierDto.setMessageInfo("生成xml文件失败，未输出！");
								result.setSuccess(MessageInfo.GETERROR.isResult());
								result.setMsg("推送失败");
							}
							String updateJson = JacksonUtils.toJson(baseSupplierDto);
							baseSupplierDtoServiceCustomer.update(userJson, updateJson);
						}
					}
			    }else{
			    	result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			    }
			}else{
				result.setSuccess(MessageInfo.UPDATESTATUSERROR.isResult());
				result.setMsg(MessageInfo.UPDATESTATUSERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("推送nc方法:  【参数"+supplierMap+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.UPDATESTATUSERROR.isResult());
			result.setMsg(MessageInfo.UPDATESTATUSERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
}
