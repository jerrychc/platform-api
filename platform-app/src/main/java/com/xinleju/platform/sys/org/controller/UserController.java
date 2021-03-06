package com.xinleju.platform.sys.org.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.finance.utils.excel.StringUtil;
import com.xinleju.platform.sys.log.dto.LogDubboDto;
import com.xinleju.platform.sys.log.dto.service.LogDubboDtoServiceCustomer;
import com.xinleju.platform.sys.org.dto.OrgnazationNodeDto;
import com.xinleju.platform.sys.org.dto.SyncOrgnazationArchive;
import com.xinleju.platform.sys.org.dto.SyncUserArchive;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.sys.org.dto.service.OrgnazationDtoServiceCustomer;
import com.xinleju.platform.sys.org.dto.service.PostDtoServiceCustomer;
import com.xinleju.platform.sys.org.dto.service.UserDtoServiceCustomer;
import com.xinleju.platform.sys.res.dto.AppSystemDto;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.InvalidCustomException;
import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.platform.uitls.OpeLogInfo;
import com.xinleju.platform.uitls.WhiteIpUtils;
import com.xinleju.platform.uitls.openOffice.utils.ExcelReadUser;
import org.apache.http.HttpRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户表控制层
 * @author sy
 *
 */
@Controller
@RequestMapping("/sys/org/user")
public class UserController {

	private static Logger log = Logger.getLogger(UserController.class);
	
	@Autowired
	private UserDtoServiceCustomer userDtoServiceCustomer;
	
	@Autowired
	private LogDubboDtoServiceCustomer logDubboDtoServiceCustomer;
	@Autowired
	private OrgnazationDtoServiceCustomer orgnazationDtoServiceCustomer;

	@Autowired
	private PostDtoServiceCustomer postDtoServiceCustomer;

	@Autowired
	private RedisOperationsSessionRepository sessionRepository;

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
			String dubboResultInfo=userDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				UserDto userDto=JacksonUtils.fromJson(resultInfo, UserDto.class);
				result.setResult(userDto);
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
	 * 测试白名单，无用
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/getUserTest/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult getUserTest(@PathVariable("id")  String id, HttpServletRequest request){
		MessageResult result=new MessageResult();
		try {
			//白名单拦截ip , add by gyh 20180130
			boolean checkIp = WhiteIpUtils.checkHttpMethod();
			if(!checkIp){
				result.setSuccess(false);
				result.setMsg(String.format(ErrorInfoCode.WRONG_WHITE_IP.getName(),WhiteIpUtils.getIpAddress(request)));
				result.setResult(WhiteIpUtils.getIpAddress(request));
				return result;
			}
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				UserDto userDto=JacksonUtils.fromJson(resultInfo, UserDto.class);
				result.setResult(userDto);
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
		result.setResult(WhiteIpUtils.getIpAddress(request));
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
		    String dubboResultInfo=userDtoServiceCustomer.getPage(userJson, paramaterJson);
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
			String dubboResultInfo=userDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<UserDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,UserDto.class);
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
	 * 第三方档案系统获取人员和组织机构数据接口
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryUserOrOrgList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryUserOrOrgList(@RequestBody Map<String,Object> map,HttpServletRequest request){
		MessageResult result=new MessageResult();
		//白名单拦截ip , add by gyh 20180130
		boolean checkIp = WhiteIpUtils.checkHttpMethod();
		if(!checkIp){
			result.setSuccess(false);
			result.setMsg(String.format(ErrorInfoCode.WRONG_WHITE_IP.getName(),WhiteIpUtils.getIpAddress(request)));
			return result;
		}
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = new SecurityUserBeanInfo();
			String tendCode = (String)map.get("tendCode");
			String dataType = (String)map.get("dataType");
			userBeanInfo.setTendCode(tendCode);
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(dataType.equals("user")){
				String dubboResultInfo=userDtoServiceCustomer.queryList(userJson, paramaterJson);
			    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			    if(dubboServiceResultInfo.isSucess()){
					String resultInfo= dubboServiceResultInfo.getResult();
					List<SyncUserArchive> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SyncUserArchive.class);
					result.setResult(list);
					result.setSuccess(MessageInfo.GETSUCCESS.isResult());
					result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			    }else{
			    	result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			    }
			}else{
				String dubboResultInfo=orgnazationDtoServiceCustomer.queryList(userJson, paramaterJson);
			    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			    if(dubboServiceResultInfo.isSucess()){
					String resultInfo= dubboServiceResultInfo.getResult();
					List<SyncOrgnazationArchive> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SyncOrgnazationArchive.class);
					result.setResult(list);
					result.setSuccess(MessageInfo.GETSUCCESS.isResult());
					result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			    }else{
			    	result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			    }
			}
			
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用第三方档案系统获取人员和组织机构数据接口方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
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
	@OpeLogInfo(node="新增用户")
	public @ResponseBody MessageResult save(@RequestBody UserDto t){
		MessageResult result=new MessageResult();
		try {
			String saveJson= JacksonUtils.toJson(t);
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.save(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				UserDto userDto=JacksonUtils.fromJson(resultInfo, UserDto.class);
				result.setResult(userDto);
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
	}
	
	/**
	 * 批量保存用户排序号
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/saveUsersSort",method=RequestMethod.POST, consumes="application/json")
	@OpeLogInfo(node="批量保存用户排序号")
	public @ResponseBody MessageResult saveUsersSort(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String saveJson= JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.saveUsersSort(userJson, saveJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				UserDto userDto=JacksonUtils.fromJson(resultInfo, UserDto.class);
				result.setResult(userDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
			ObjectMapper mapper = new ObjectMapper();
			log.error("调用saveUsersSort方法:  【参数"+saveJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.SAVEERROR.isResult());
			result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@OpeLogInfo(node="根据id删除用户")
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.deleteObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				UserDto userDto=JacksonUtils.fromJson(resultInfo, UserDto.class);
				result.setResult(userDto);
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
	@OpeLogInfo(node="根据id批量删除用户")
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.deleteAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				UserDto userDto=JacksonUtils.fromJson(resultInfo, UserDto.class);
				result.setResult(userDto);
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
	@OpeLogInfo(node="根据id伪删除用户")
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.deletePseudoObjectById(userJson, "{\"id\":\""+id+"\"}");
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
	@OpeLogInfo(node="根据ids批量伪删除用户")
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.deletePseudoAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
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
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	@OpeLogInfo(node="根据id修改用户")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		UserDto userDto=null;
		try {
			/*String dubboResultInfo=userDtoServiceCustomer.getObjectById(null, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);*/
				
				 String updateJson= JacksonUtils.toJson(map);
				 SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
					String userJson = JacksonUtils.toJson(userBeanInfo);
				 String updateDubboResultInfo=userDtoServiceCustomer.update(userJson, updateJson);
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
			/*}else{
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
			}*/
		} catch (Exception e) {
			try{
			 ////e.printStackTrace();
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(userDto);
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
	/**
	 * 批量修改用户状态
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/lockUsersOrNot",method=RequestMethod.PUT,consumes="application/json")
	@OpeLogInfo(node="批量修改用户状态")
	public @ResponseBody MessageResult lockUsersOrNot(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		UserDto userDto=null;
		String updateJson= JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String updateDubboResultInfo=userDtoServiceCustomer.lockUsersOrNot(userJson, updateJson);
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
			try{
				////e.printStackTrace();
				ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(userDto);
				log.error("调用update方法:  【参数"+updateJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
			}catch (JsonProcessingException e1) {
			}

		}
		return result;
	}
	
	/**
	 * 记录调用dubbo日志
	 * @param 开始调用时间
	 * @param 结束调用时间
	 * @param 返回内容
	 * @param 调用方法
	 */
	public void saveDubboLog(Long startTime,Long endTime,String returnContent,String dubboMethod){
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try{
			LogDubboDto logDubboDto = new LogDubboDto();
			logDubboDto.setId(IDGenerator.getUUID());
			logDubboDto.setStartTime(new Timestamp(startTime));
			logDubboDto.setEndTime(new Timestamp(endTime));
			logDubboDto.setExecuteTime(endTime-startTime);
			logDubboDto.setReturnContent(returnContent);
			logDubboDto.setDubboMethod(dubboMethod);
			logDubboDto.setSysCode("/sys/org/user");
			String saveJson= JacksonUtils.toJson(logDubboDto);
			String logResultInfo=logDubboDtoServiceCustomer.save(userJson, saveJson);
		    DubboServiceResultInfo logServiceResultInfo= JacksonUtils.fromJson(logResultInfo, DubboServiceResultInfo.class);
		    if(logServiceResultInfo.isSucess()){
				log.info("/sys/org/user--存储调用dubbo日志成功");
		    }else{
				log.info("/sys/org/user--存储调用dubbo日志错误【"+logServiceResultInfo.getExceptionMsg()+"】");
		    }
		}catch(Exception e){
			////e.printStackTrace();
			log.error("/sys/org/user--存储调用dubbo日志异常错误【"+e.getMessage()+"】");
		}
		
	}

	/**
	 * 根据组织结构查询所有人
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value="/queryUserListByOrgId",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryUserListByOrgId(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
//			String dubboResultInfo=userDtoServiceCustomer.queryUserListByOrgId(userJson, paramaterJson);
			String dubboResultInfo=userDtoServiceCustomer.selectUserByQuery(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<UserDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,UserDto.class);
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
	 * 根据组织结构查询所有人
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value="/queryUserListByOrgIdForPortal",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryUserListByOrgIdForPortal(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
//			String dubboResultInfo=userDtoServiceCustomer.queryUserListByOrgId(userJson, paramaterJson);
			String dubboResultInfo=userDtoServiceCustomer.selectUserByQuery(userJson, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<UserDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,UserDto.class);
				Boolean hasPost = (Boolean) map.get("hasPost");
				if(hasPost!=null&&hasPost) {
					List<UserDto> userDtoList = new ArrayList<UserDto>();
					int count = 0;

					for (UserDto user:list) {
						count++;
						if(count>10){
							break;
						}
						//try {
							Map<String, Object> postParam = new HashMap<String, Object>();
							postParam.put("userId", user.getId());

							String postJson = postDtoServiceCustomer.queryPostRoleListByUserId(userJson, JacksonUtils.toJson(postParam));
							DubboServiceResultInfo postResultInfo = JacksonUtils.fromJson(postJson, DubboServiceResultInfo.class);
							List<Map<String, Object>> postMapList = JacksonUtils.fromJson(postResultInfo.getResult(), List.class, Map.class);
							if (!postMapList.isEmpty()) {
								user.setPostName((String) postMapList.get(0).get("roleName"));
							}
							userDtoList.add(user);
						//} catch (Exception e) {

						//}
					}
					result.setResult(userDtoList);

				}else{
					result.setResult(list);
				}

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
	 * 根据岗位查询用户列表
	 * @param paramaterJson
	 * @return
	 */
	@RequestMapping(value="/queryUserListByPostId",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryUserListByPostId(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.queryUserListByPostId(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<UserDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,UserDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryUserListByPostId方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 根据角色查询用户列表
	 * @param paramaterJson
	 * @return
	 */
	@RequestMapping(value="/queryUserListByRoleId",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryUserListByRoleId(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.queryUserListByRoleId(userJson, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<UserDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,UserDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryUserListByRoleId方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 根据虚拟角色查询用户列表
	 * @param paramaterJson
	 * @return
	 */
	@RequestMapping(value="/queryRoleUserByRoleId",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryRoleUserByRoleId(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.queryRoleUserByRoleId(userJson, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<UserDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,UserDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryRoleUserByRoleId方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 获取符合条件的用户树列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/getUserTree",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getUserTree(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//暂时查询的是一级根目录下的树
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.getUserTree(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<OrgnazationNodeDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,OrgnazationNodeDto.class);
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
	 * 获取当前登录用户
	 * @param id  业务对象主键
	 * @return     业务对象
	 */
	@RequestMapping(value="/getMyInfo",method=RequestMethod.GET)
	public @ResponseBody MessageResult getMyInfo(){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo beanInfo=LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(beanInfo);
			String dubboResultInfo=userDtoServiceCustomer.getMyInfo(userJson,null);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				UserDto userDto=JacksonUtils.fromJson(resultInfo, UserDto.class);
				
				result.setResult(userDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
			
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用getMyInfo方法:  ======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	/**
	 * 修改密码
	 */
	@RequestMapping(value="/updateMyPwd",method=RequestMethod.POST)
	@OpeLogInfo(node="修改用户密码")
	public @ResponseBody MessageResult updateMyPwd(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo beanInfo=LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(beanInfo);
			String dubboResultInfo=userDtoServiceCustomer.updateMyPwd(userJson, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				UserDto userDto=JacksonUtils.fromJson(resultInfo, UserDto.class);
				
				result.setResult(userDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用getMyInfo方法:  ======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 根据用户Id，获取用户的公司，部门，项目，分期
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/getOrgInfoByUserId",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getOrgInfoByUserId(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.getOrgInfoByUserId(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AuthenticationDto authenticationDto=JacksonUtils.fromJson(resultInfo,AuthenticationDto.class);
				result.setResult(authenticationDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用getOrgInfoByUserId方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 根据用户Id返回用户信息（可以多个）
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/getUserInfoByUserIds",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getUserInfoByUserIds(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.getUserInfoByUserIds(userJson, paramaterJson);
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
			log.error("调用getOrgInfoByUserId方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 根据搜索条件查询用户及其岗位
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryUserAndPostsByUname",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryUserAndPostsByUname(@RequestBody Map<String,Object> map, HttpServletResponse response, HttpServletRequest request){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String sessionId = request.getParameter("sessionId");
			if(StringUtil.isNotBlank(sessionId)){
				ExpiringSession session_ = sessionRepository.getSession(sessionId );
				userBeanInfo  = (SecurityUserBeanInfo) session_.getAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER);
			}
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=userDtoServiceCustomer.queryUserAndPostsByUname(userJson, paramaterJson);
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
			log.error("调用queryUserAndPostsByUname方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	/**
	 * 读取Excel数据到数据库
	 * @param file excel文件
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/readExcelForUser")
//	@OpeLogInfo(node="导入用户")
	public @ResponseBody MessageResult readExcelForUser(@RequestParam(value="excelFile") MultipartFile file) throws IOException {
		MessageResult result=new MessageResult();
		//判断文件是否为空
		if(file == null){
			result.setSuccess(false);
			result.setMsg("文件不可为空");
			return result;
		}
		//读取Excel数据到List中
		//读取Excel数据到List中
		List<UserDto> list = null;
		try {
			list= new ExcelReadUser().readExcel(file);
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
			result.setResult(list);
			String dubboResultInfo = userDtoServiceCustomer.readExcelAndInsert(userJson,list);
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
		}else{
			result.setSuccess(false);
			result.setMsg("上传内容为空");
		}
		return result;
	}
}
