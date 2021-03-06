package com.xinleju.platform.sys.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserDto;
import com.xinleju.platform.sys.base.dto.CustomFormInstanceDto;
import com.xinleju.platform.sys.base.dto.service.CustomFormInstanceDtoServiceCustomer;
import com.xinleju.platform.sys.org.dto.service.UserDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 自定义表单实例控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/sys/base/customFormInstance")
public class CustomFormInstanceController {

	private static Logger log = Logger.getLogger(CustomFormInstanceController.class);
	
	@Autowired
	private CustomFormInstanceDtoServiceCustomer customFormInstanceDtoServiceCustomer;
	
	@Autowired
	protected RedisTemplate<String, String> redisTemplate;
	
	@Autowired
    private UserDtoServiceCustomer userDtoServiceCustomer;
	
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
			String dubboResultInfo=customFormInstanceDtoServiceCustomer.getObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormInstanceDto customFormInstanceDto=JacksonUtils.fromJson(resultInfo, CustomFormInstanceDto.class);
				result.setResult(customFormInstanceDto);
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
	  * @Description:获取实例对象（包括模板格式）
	  * @author:zhangfangzhi
	  * @date 2017年3月30日 下午4:21:52
	  * @version V1.0
	 */
	@RequestMapping(value="/getInstance/{id}/{customFormId}",method=RequestMethod.GET)
	public @ResponseBody MessageResult getInstance(@PathVariable("id")  String id,@PathVariable("customFormId")  String customFormId){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormInstanceDtoServiceCustomer.getInstanceByFormId(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\",\"customFormId\":\""+customFormId+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormInstanceDto customFormInstanceDto=JacksonUtils.fromJson(resultInfo, CustomFormInstanceDto.class);
				result.setResult(customFormInstanceDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
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
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
		    String dubboResultInfo=customFormInstanceDtoServiceCustomer.getPageCustom(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
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
	  * @Description:我的发起（手机端）
	  * @author:zhangfangzhi
	  * @date 2018年1月19日 下午7:12:16
	  * @version V1.0
	 */
	@RequestMapping(value="/myFormPage",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult myFormPage(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		map.put("operatorId", securityUserBeanInfo.getSecurityUserDto().getId());
		String paramaterJson = JacksonUtils.toJson(map);
		try {
		    String dubboResultInfo=customFormInstanceDtoServiceCustomer.getMyFormPage(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
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
	  * @Description:付款管理查询（分页）
	  * @author:zhangfangzhi
	  * @date 2017年11月6日 下午4:35:48
	  * @version V1.0
	 */
	@RequestMapping(value="/fundPage",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult fundPage(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
		    String dubboResultInfo=customFormInstanceDtoServiceCustomer.getFundPage(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
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
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormInstanceDtoServiceCustomer.queryList(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<CustomFormInstanceDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,CustomFormInstanceDto.class);
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
	public @ResponseBody MessageResult save(@RequestBody CustomFormInstanceDto t){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			t.setDelflag(false);
			t.setStatus("0");
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=customFormInstanceDtoServiceCustomer.save(JacksonUtils.toJson(securityUserBeanInfo), saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormInstanceDto customFormInstanceDto=JacksonUtils.fromJson(resultInfo, CustomFormInstanceDto.class);
				result.setResult(customFormInstanceDto);
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
	 * 删除实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormInstanceDtoServiceCustomer.deleteObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormInstanceDto customFormInstanceDto=JacksonUtils.fromJson(resultInfo, CustomFormInstanceDto.class);
				result.setResult(customFormInstanceDto);
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
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormInstanceDtoServiceCustomer.deleteAllObjectByIds(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormInstanceDto customFormInstanceDto=JacksonUtils.fromJson(resultInfo, CustomFormInstanceDto.class);
				result.setResult(customFormInstanceDto);
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
		CustomFormInstanceDto customFormInstanceDto=null;
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormInstanceDtoServiceCustomer.getObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=customFormInstanceDtoServiceCustomer.update(JacksonUtils.toJson(securityUserBeanInfo), updateJson);
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
			 String  paramJson = mapper.writeValueAsString(customFormInstanceDto);
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
	  * @Description:流程回调修改状态
	  * @author:zhangfangzhi
	  * @date 2017年4月20日 下午7:32:16
	  * @version V1.0
	 */
//	@RequestMapping(value="/updateStatus/{id}",method=RequestMethod.PUT,consumes="application/json")
//	public @ResponseBody MessageResult updateStatus(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
//		MessageResult result=new MessageResult();
//		CustomFormInstanceDto customFormInstanceDto=null;
//		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
//		try {
//			map.put("id", id);
//			 String updateJson= JacksonUtils.toJson(map);
//			 String updateDubboResultInfo=customFormInstanceDtoServiceCustomer.updateStatus(JacksonUtils.toJson(securityUserBeanInfo), updateJson);
//			 DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
//			 if(updateDubboServiceResultInfo.isSucess()){
//				 Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
//				 result.setResult(i);
//				 result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
//				 result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
//			 }else{
//				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
//				 result.setMsg(updateDubboServiceResultInfo.getMsg()+"【"+updateDubboServiceResultInfo.getExceptionMsg()+"】");
//			 }
//		} catch (Exception e) {
//			try{
//			 ////e.printStackTrace();
//			 ObjectMapper mapper = new ObjectMapper();
//			 String  paramJson = mapper.writeValueAsString(customFormInstanceDto);
//			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
//			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
//			 result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
//			}catch (JsonProcessingException e1) {
//				// TODO Auto-generated catch block
//				//e1.printStackTrace();
//			}
//			
//		}
//		return result;
//	}
	
	/**
	  * @Description:流程回调修改状态
	  * @author:zhangfangzhi
	  * @date 2017年4月20日 下午7:32:16
	  * @version V1.0
	 */
	@RequestMapping(value="/updateStatus",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult updateStatus(@RequestBody Map<String,Object> map, HttpServletRequest request){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		
//		SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
//		if(map.get("token")!=null){//加入白名单处理获取自定义表单流程业务变量查询
//			String token = String.valueOf(map.get("token"));
//			String[] args = token.split("@");
//			if(args.length==2){
//				securityUserBeanInfo.setTendCode(args[1]);
//			}
//		}else{
//			securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
//		}
		
		SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
        if(map.get("token")!=null){//加入白名单处理表单流程业务回调
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
		
		log.info("流程调用状态变更参数：" + map);
		try {
			String dubboResultInfo=customFormInstanceDtoServiceCustomer.updateStatus(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<CustomFormInstanceDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,CustomFormInstanceDto.class);
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
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormInstanceDtoServiceCustomer.deletePseudoObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormInstanceDto customFormInstanceDto=JacksonUtils.fromJson(resultInfo, CustomFormInstanceDto.class);
				result.setResult(customFormInstanceDto);
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
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormInstanceDtoServiceCustomer.deletePseudoAllObjectByIds(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormInstanceDto customFormInstanceDto=JacksonUtils.fromJson(resultInfo, CustomFormInstanceDto.class);
				result.setResult(customFormInstanceDto);
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
	  * @Description:获取流程变量值
	  * @author:zhangfangzhi
	  * @date 2017年4月13日 下午8:40:42
	  * @version V1.0
	 */
//	@RequestMapping(value="/getVariable/{id}",method=RequestMethod.GET)
//	public @ResponseBody MessageResult getVariable(@PathVariable("id")  String id){
//		MessageResult result=new MessageResult();
//		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
//		try {
//			String dubboResultInfo=customFormInstanceDtoServiceCustomer.getVariableById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
//			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
//			if(dubboServiceResultInfo.isSucess()){
//				result.setResult(dubboServiceResultInfo.getResult());
//				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
//				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
//			}else{
//				result.setSuccess(MessageInfo.GETERROR.isResult());
//				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
//			}
//		} catch (Exception e) {
//			////e.printStackTrace();
//		    log.error("调用getVariable方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
//			result.setSuccess(MessageInfo.GETERROR.isResult());
//			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
//		}
//		return result;
//	}
	
	/**
	  * @Description:获取流程变量值
	  * @author:zhangfangzhi
	  * @date 2017年4月20日 下午7:32:16
	  * @version V1.0
	 */
	@RequestMapping(value="/getVariable",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getVariable(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
		if(map.get("token")!=null){//加入白名单处理获取自定义表单流程业务变量查询
			String token = String.valueOf(map.get("token"));
			String[] args = token.split("@");
			if(args.length==2){
				securityUserBeanInfo.setTendCode(args[1]);
			}
		}else{
			securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		}
		try {
			String dubboResultInfo=customFormInstanceDtoServiceCustomer.getVariableById(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				result.setResult(dubboServiceResultInfo.getResult());
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
	  * @Description:测试接口
	  * @author:zhangfangzhi
	  * @date 2018年1月19日 下午1:55:33
	  * @version V1.0
	 */
	@RequestMapping(value="/getInvalid",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getInvalid(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
		if(map.get("token")!=null){//加入白名单处理获取自定义表单流程业务变量查询
			String token = String.valueOf(map.get("token"));
			String[] args = token.split("@");
			if(args.length==2){
				securityUserBeanInfo.setTendCode(args[1]);
			}
		}else{
			securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		}
		try {
//			String dubboResultInfo=customFormInstanceDtoServiceCustomer.getVariableById(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
//		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			DubboServiceResultInfo dubboServiceResultInfo= new DubboServiceResultInfo();
			dubboServiceResultInfo.setSucess(false);
			dubboServiceResultInfo.setMsg("失败了了了了了了！！！！！！！！！！");
			dubboServiceResultInfo.setResult("失败了！！！！！！！！！");
		    if(dubboServiceResultInfo.isSucess()){
				result.setResult(dubboServiceResultInfo.getResult());
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(dubboServiceResultInfo.getMsg());
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
	  * @Description:导入资金平台
	  * @author:zhangfangzhi
	  * @date 2017年11月7日 下午2:21:28
	  * @version V1.0
	 */
	@RequestMapping(value="/importFundPayment/{ids}",method=RequestMethod.POST)
	public @ResponseBody MessageResult importFundPayment(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormInstanceDtoServiceCustomer.importFundPayment(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+ids+"\"}");
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				result.setResult(dubboServiceResultInfo.getResult());
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }
		} catch (Exception e) {
			try {
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(ids);
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
	  * @Description:资金平台同步
	  * @author:zhangfangzhi
	  * @date 2017年11月8日 下午2:09:58
	  * @version V1.0
	 */
	@RequestMapping(value="/synFund",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult synFund(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormInstanceDtoServiceCustomer.synFund(JacksonUtils.toJson(securityUserBeanInfo),JacksonUtils.toJson(map));
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormInstanceDto customFormInstanceDto=JacksonUtils.fromJson(resultInfo, CustomFormInstanceDto.class);
				result.setResult(customFormInstanceDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
		} catch (Exception e) {
			try {
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(JacksonUtils.toJson(map));
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
