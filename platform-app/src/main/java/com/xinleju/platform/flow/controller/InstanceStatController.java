package com.xinleju.platform.flow.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.flow.dto.ApprovalStatDto;
import com.xinleju.platform.flow.dto.InstanceDto;
import com.xinleju.platform.flow.dto.InstanceStatDto;
import com.xinleju.platform.flow.dto.service.InstanceStatDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.ExportDataUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 流程实例统计中间表控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/flow/instanceStat")
public class InstanceStatController {

	private static Logger log = Logger.getLogger(InstanceStatController.class);
	
	@Autowired
	private InstanceStatDtoServiceCustomer instanceStatDtoServiceCustomer;
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
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				InstanceStatDto instanceStatDto=JacksonUtils.fromJson(resultInfo, InstanceStatDto.class);
				result.setResult(instanceStatDto);
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
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
		    String dubboResultInfo=instanceStatDtoServiceCustomer.getPage(userJson, paramaterJson);
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
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceStatDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceStatDto.class);
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
	public @ResponseBody MessageResult save(@RequestBody InstanceStatDto t){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=instanceStatDtoServiceCustomer.save(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				InstanceStatDto instanceStatDto=JacksonUtils.fromJson(resultInfo, InstanceStatDto.class);
				result.setResult(instanceStatDto);
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
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.deleteObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				InstanceStatDto instanceStatDto=JacksonUtils.fromJson(resultInfo, InstanceStatDto.class);
				result.setResult(instanceStatDto);
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
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.deleteAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				InstanceStatDto instanceStatDto=JacksonUtils.fromJson(resultInfo, InstanceStatDto.class);
				result.setResult(instanceStatDto);
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
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		InstanceStatDto instanceStatDto=null;
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=instanceStatDtoServiceCustomer.update(userJson, updateJson);
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
			 String  paramJson = mapper.writeValueAsString(instanceStatDto);
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
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.deletePseudoObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				InstanceStatDto instanceStatDto=JacksonUtils.fromJson(resultInfo, InstanceStatDto.class);
				result.setResult(instanceStatDto);
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
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.deletePseudoAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				InstanceStatDto instanceStatDto=JacksonUtils.fromJson(resultInfo, InstanceStatDto.class);
				result.setResult(instanceStatDto);
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
	 * 统计流程模板的使用次数
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/statUseTimes",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult statUseTimes(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		System.out.println("\n\n statUseTimes  paramaterJson="+paramaterJson);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.statUseTimes(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceStatDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceStatDto.class);
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
	 * 导出流程模板的使用次数
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/exportUseTimes", method = RequestMethod.GET) 
	public void exportUseTimes(HttpServletRequest request,  HttpServletResponse response){
		MessageResult result=new MessageResult();
		String paramaterJson = "";
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			String appId = getParamFromRequest(request,"appId");
			String startDate = getParamFromRequest(request,"startDate");
			String endDate = getParamFromRequest(request,"endDate");
			String busiObjectId = getParamFromRequest(request,"busiObjectId");
			
			map.put("appId", appId);
			map.put("busiObjectId", busiObjectId);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			
			paramaterJson = JacksonUtils.toJson(map);
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.statUseTimes(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceStatDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceStatDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
				String fileName = "流程模板使用次数.xlsx";
				String header[] = {"系统名称","模板名称","模板编码","业务对象名称","使用次数"};
				ExportDataUtils exportExcel = new ExportDataUtils("流程模板使用次数", header);
				exportExcel = exportExcel.addUseTimesDataList(list);
	    		exportExcel.write(response, fileName);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
		}
		
	}
	
	/**
	 * 导出流程的特殊操作次数
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/exportOperateTimes", method = RequestMethod.GET) 
	public void exportOperateTimes(HttpServletRequest request,  HttpServletResponse response){
		MessageResult result=new MessageResult();
		String paramaterJson = "";
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			String appId = getParamFromRequest(request,"appId");
			String statWay = getParamFromRequest(request,"statWay");
			String busiObjectId = getParamFromRequest(request,"busiObjectId");
			
			String startDate1 = getParamFromRequest(request,"startDate1");
			String endDate1 = getParamFromRequest(request,"endDate1");
			String startDate2 = getParamFromRequest(request,"startDate2");
			String endDate2 = getParamFromRequest(request,"endDate2");
			
			map.put("appId", appId);
			map.put("statWay", statWay);
			map.put("busiObjectId", busiObjectId);
			map.put("startDate1", startDate1);
			map.put("endDate1", endDate1);
			map.put("startDate2", startDate2);
			map.put("endDate2", endDate2);
			
			paramaterJson = JacksonUtils.toJson(map);
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.statOperateTimes(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceStatDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceStatDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
				String fileName = "流程特殊操作次数.xlsx";
				String header[] = {"统计维度","打回次数","发起人撤回流程次数","作废次数","转办次数","协办次数","撤回任务次数","被修改审批意见次数"};
				ExportDataUtils exportExcel = new ExportDataUtils("流程特殊操作次数", header);
				exportExcel = exportExcel.addOperateTimesDataList(list);
	    		exportExcel.write(response, fileName);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
		}
		
	}
	
	/**
	 * 导出流程的特殊操作次数
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/exportInstanceEfficiency", method = RequestMethod.GET) 
	public void exportInstanceEfficiency(HttpServletRequest request,  HttpServletResponse response){
		MessageResult result=new MessageResult();
		String paramaterJson = "";
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			String appId = getParamFromRequest(request,"appId");
			String statWay = getParamFromRequest(request,"statWay");
			String busiObjectId = getParamFromRequest(request,"busiObjectId");
			
			String startDate1 = getParamFromRequest(request,"startDate1");
			String endDate1 = getParamFromRequest(request,"endDate1");
			String startDate2 = getParamFromRequest(request,"startDate2");
			String endDate2 = getParamFromRequest(request,"endDate2");
			
			String compareType = getParamFromRequest(request,"compareType");
			String startSum = getParamFromRequest(request,"startSum");
			String endSum = getParamFromRequest(request,"endSum");
			
			map.put("appId", appId);
			map.put("statWay", statWay);
			map.put("busiObjectId", busiObjectId);
			map.put("startDate1", startDate1);
			map.put("endDate1", endDate1);
			map.put("startDate2", startDate2);
			map.put("endDate2", endDate2);
			
			map.put("compareType", compareType);
			map.put("startSum", startSum);
			map.put("endSum", endSum);
			
			paramaterJson = JacksonUtils.toJson(map);
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.statInstanceEffiency(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceStatDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceStatDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
				//String fileName = "instance_stat_export.xlsx";
				String fileName = "流程实例效率统计.xlsx";
				String header[] = {"统计维度","平均时长(小时)","最大时长(小时)","最小时长(小时)"};
				ExportDataUtils exportExcel = new ExportDataUtils("流程实例效率统计", header);
				exportExcel = exportExcel.addInstanceEffiencyDataList(list);
	    		exportExcel.write(response, fileName);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
		}
	}
	
	/**
	 * 导出流程任务时长统计
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/exportTaskLength", method = RequestMethod.GET) 
	public void exportTaskLength(HttpServletRequest request,  HttpServletResponse response){
		MessageResult result=new MessageResult();
		String paramaterJson = "";
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			String appId = getParamFromRequest(request,"appId");
			String statWay = getParamFromRequest(request,"statWay");
			String busiObjectId = getParamFromRequest(request,"busiObjectId");
			
			String startDate1 = getParamFromRequest(request,"startDate1");
			String endDate1 = getParamFromRequest(request,"endDate1");
			String startDate2 = getParamFromRequest(request,"startDate2");
			String endDate2 = getParamFromRequest(request,"endDate2");
			
			String compareType = getParamFromRequest(request,"compareType");
			String startSum = getParamFromRequest(request,"startSum");
			String endSum = getParamFromRequest(request,"endSum");
			
			map.put("appId", appId);
			map.put("statWay", statWay);
			map.put("busiObjectId", busiObjectId);
			map.put("startDate1", startDate1);
			map.put("endDate1", endDate1);
			map.put("startDate2", startDate2);
			map.put("endDate2", endDate2);
			
			map.put("compareType", compareType);
			map.put("startSum", startSum);
			map.put("endSum", endSum);

			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
            map.put("tendCode",userBeanInfo.getTendCode());
            paramaterJson=  JacksonUtils.toJson(map);
            System.out.println("paramaterJson="+paramaterJson);
			String dubboResultInfo=instanceStatDtoServiceCustomer.statTaskLength(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceStatDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceStatDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
				String fileName = "流程任务时长统计.xlsx";
				String header[] = {"统计维度","审批任务数","平均时长(小时)","最大时长(小时)","最小时长(小时)"};
				ExportDataUtils exportExcel = new ExportDataUtils("流程任务时长统计", header);
				exportExcel = exportExcel.addTaskLengthDataList(list);
	    		exportExcel.write(response, fileName);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
		}
	}


	private String getParamFromRequest(HttpServletRequest request, String paraName) {
		String paraValue = request.getParameter(paraName);
		if(paraValue == null){
			paraValue = "";
		}
		return paraValue;
		
	}

	
	/**
	 * 统计流程实例的效率
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/statInstanceEfficiency",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult statInstanceEffiency(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.statInstanceEffiency(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceStatDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceStatDto.class);
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
	 * 统计流程特殊操作次数
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/statOperateTimes",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult statOperateTimes(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.statOperateTimes(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceStatDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceStatDto.class);
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
	 * 查询流程效率的明细
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/detailOperateTimesList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult detailOperateTimesList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.detailOperateTimesList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceDto.class);
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
	 * 查询流程效率的明细
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/detailInstanceEfficiencyList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult detailInstanceEfficiencyList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.detailInstanceEfficiencyList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceStatDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceStatDto.class);
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
	 * 查询流程特殊操作次数的明细
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/detailTaskLengthList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult detailTaskLengthList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			map.put("tendCode",userBeanInfo.getTendCode());
			paramaterJson=  JacksonUtils.toJson(map);
			String dubboResultInfo=instanceStatDtoServiceCustomer.detailTaskLengthList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceStatDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceStatDto.class);
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
	 * 统计流程任务时长
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/statTaskLength",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult statTaskLength(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			map.put("tendCode",userBeanInfo.getTendCode());
			paramaterJson=  JacksonUtils.toJson(map);
			String dubboResultInfo=instanceStatDtoServiceCustomer.statTaskLength(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceStatDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceStatDto.class);
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
	 * 导出流程任务时长传阅页面统计
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/exportTaskLengthDetail", method={RequestMethod.POST}) 
	public void exportTaskLengthDetail(HttpServletRequest request,  HttpServletResponse response){
		MessageResult result=new MessageResult();
		String paramaterJson = "";
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			//appId=9d6cba61c4b24a5699c339a49471a0e7  exportTaskLengthDetail
			//&busiObjectId=3d890c7fae7a48399221eeb2392a73cd
			//&statWay=COM
			//&statWayId=1fadc6e6f5a34b40bc59d5add3364df5
			String appId = getParamFromRequest(request,"appId");
			String statWay = getParamFromRequest(request,"statWay");
			String busiObjectId = getParamFromRequest(request,"busiObjectId");
			String statWayId = getParamFromRequest(request,"statWayId");
			//&startDate1=&endDate1=&startDate2=&endDate2=&compareType=-1&startSum=&endSum=
			String startDate1 = getParamFromRequest(request,"startDate1");
			String endDate1 = getParamFromRequest(request,"endDate1");
			String startDate2 = getParamFromRequest(request,"startDate2");
			String endDate2 = getParamFromRequest(request,"endDate2");
			String compareType = getParamFromRequest(request,"compareType");
			String startSum = getParamFromRequest(request,"startSum");
			String endSum = getParamFromRequest(request,"endSum");
			
			map.put("appId", appId);
			map.put("statWay", statWay);
			map.put("statWayId", statWayId);
			map.put("busiObjectId", busiObjectId);
			map.put("startDate1", startDate1);
			map.put("endDate1", endDate1);
			map.put("startDate2", startDate2);
			map.put("endDate2", endDate2);
			
			map.put("compareType", compareType);
			map.put("startSum", startSum);
			map.put("endSum", endSum);

			
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
            map.put("tendCode",userBeanInfo.getTendCode());
            paramaterJson = JacksonUtils.toJson(map);
            System.out.println("paramaterJson="+paramaterJson);
			String dubboResultInfo=instanceStatDtoServiceCustomer.detailTaskLengthList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceStatDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceStatDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
				String fileName = "任务停留明细.xlsx";
				 
//				String header[] = {"审批人","停留时长(小时)","流程实例","审批环节","审核类型","处理时间", "工作日处理", "事务处理方式","流程发起公司"};
                String header[] = {"流程标题","系统","业务对象","审批状态","停留时长(小时)","接受时间","处理时间","处理人","处理人所在部门","处理人所在公司","流程模板","节点名称","发起人","发起人所在部门","发起人所在公司"};
				ExportDataUtils exportExcel = new ExportDataUtils("流程任务时长统计", header);
				exportExcel = exportExcel.addTaskLengthDetailDataList(list);
	    		exportExcel.write(response, fileName);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
		}
	}
	
	/**
	 * 导出流程任务时长传阅页面统计
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/exportInstanceEfficiencyDetail", method={RequestMethod.POST}) 
	public void exportInstanceEfficiencyDetail(HttpServletRequest request,  HttpServletResponse response){
		MessageResult result=new MessageResult();
		String paramaterJson = "";
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			//appId=9d6cba61c4b24a5699c339a49471a0e7  exportTaskLengthDetail
			//&busiObjectId=3d890c7fae7a48399221eeb2392a73cd
			//&statWay=COM
			//&statWayId=1fadc6e6f5a34b40bc59d5add3364df5
			String appId = getParamFromRequest(request,"appId");
			String statWay = getParamFromRequest(request,"statWay");
			String busiObjectId = getParamFromRequest(request,"busiObjectId");
			String statWayId = getParamFromRequest(request,"statWayId");
			//&startDate1=&endDate1=&startDate2=&endDate2=&compareType=-1&startSum=&endSum=
			String startDate1 = getParamFromRequest(request,"startDate1");
			String endDate1 = getParamFromRequest(request,"endDate1");
			String startDate2 = getParamFromRequest(request,"startDate2");
			String endDate2 = getParamFromRequest(request,"endDate2");
			String compareType = getParamFromRequest(request,"compareType");
			String startSum = getParamFromRequest(request,"startSum");
			String endSum = getParamFromRequest(request,"endSum");
			
			map.put("appId", appId);
			map.put("statWay", statWay);
			map.put("statWayId", statWayId);
			map.put("busiObjectId", busiObjectId);
			map.put("startDate1", startDate1);
			map.put("endDate1", endDate1);
			map.put("startDate2", startDate2);
			map.put("endDate2", endDate2);
			
			map.put("compareType", compareType);
			map.put("startSum", startSum);
			map.put("endSum", endSum);
			
			paramaterJson = JacksonUtils.toJson(map);
			System.out.println("paramaterJson="+paramaterJson);
			
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.detailInstanceEfficiencyList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceStatDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceStatDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
				String fileName = "效率分析明细.xlsx";
				
				String header[] = {"变量编码","流程标题","业务对象","发起时间","发起人","发起部门", "发起公司", "流程模板","历时"};
				ExportDataUtils exportExcel = new ExportDataUtils("流程任务时长统计", header);
				exportExcel = exportExcel.addInstanceEfficiencyDetailList(list);
	    		exportExcel.write(response, fileName);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
		}
	}
	
	/**
	 * 导出特殊操作传阅页面统计
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/exportOperateTimesDetailList", method={RequestMethod.POST}) 
	public void exportOperateTimesDetailList(HttpServletRequest request,  HttpServletResponse response){
		MessageResult result=new MessageResult();
		String paramaterJson = "";
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			/*var paramText = "?operateType="+operateType+"&statWay="+statWay+"&statWayId="+statWayId
			+"&appId="+appId+"&busiObjectId="+busiObjectId
			+"&startDate1="+startDate1+"&endDate1="+endDate1
			+"&startDate2="+startDate2+"&endDate2="+endDate2;*/
			String operateType = getParamFromRequest(request,"operateType");
			String appId = getParamFromRequest(request,"appId");
			String statWay = getParamFromRequest(request,"statWay");
			String busiObjectId = getParamFromRequest(request,"busiObjectId");
			String statWayId = getParamFromRequest(request,"statWayId");
			
			//&startDate1=&endDate1=&startDate2=&endDate2=
			String startDate1 = getParamFromRequest(request,"startDate1");
			String endDate1 = getParamFromRequest(request,"endDate1");
			String startDate2 = getParamFromRequest(request,"startDate2");
			String endDate2 = getParamFromRequest(request,"endDate2");
			
			map.put("operateType", operateType);
			map.put("appId", appId);
			map.put("statWay", statWay);
			map.put("statWayId", statWayId);
			map.put("busiObjectId", busiObjectId);
			map.put("startDate1", startDate1);
			map.put("endDate1", endDate1);
			map.put("startDate2", startDate2);
			map.put("endDate2", endDate2);
			
			paramaterJson = JacksonUtils.toJson(map);
			System.out.println("paramaterJson="+paramaterJson);
			
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.detailOperateTimesList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
				String fileName = "特殊操作统计明细.xlsx";
				
				String header[] = {"变量编码","流程标题","业务对象","发起时间","发起人","发起部门", "发起公司", "流程模板", "当前审批人" ,"流程状态"};
				ExportDataUtils exportExcel = new ExportDataUtils("流程任务时长统计", header);
				exportExcel = exportExcel.addOperateTimesDetailList(list);
	    		exportExcel.write(response, fileName);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
		}
	}
	
	/**
	  * @Description:流程效率统计明细
	  * @author:zhangfangzhi
	  * @date 2018年2月13日 上午11:05:03
	  * @version V1.0
	 */
	@RequestMapping(value="/statInstanceEfficiencyMxPage",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult statInstanceEfficiencyMxPage(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.statInstanceEfficiencyMxPage(userJson, paramaterJson);
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
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	  * @Description:导出流程效率统计明细
	  * @author:zhangfangzhi
	  * @date 2018年2月23日 下午2:30:27
	  * @version V1.0
	 */
	@RequestMapping(value = "/exportInstanceEfficiencyMx", method = RequestMethod.GET) 
	public void exportInstanceEfficiencyMx(HttpServletRequest request,  HttpServletResponse response){
		MessageResult result=new MessageResult();
		String paramaterJson = "";
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			String startDate1 = getParamFromRequest(request,"startDate1");
			String endDate1 = getParamFromRequest(request,"endDate1");
			
			String appId = getParamFromRequest(request,"appId");
			String flowId = getParamFromRequest(request,"flowId");
			String busiObjectId = getParamFromRequest(request,"busiObjectId");
			
			String approverIds = getParamFromRequest(request,"approverIds");
			String orgIds = getParamFromRequest(request,"orgIds");
			
			String holdTime = getParamFromRequest(request,"holdTime");
			String hfTask = getParamFromRequest(request,"hfTask");
			if(!"1".equals(hfTask)){
				hfTask="-1";
			}
			String workDay = getParamFromRequest(request,"workDay");
			
			map.put("startDate1", startDate1);
			map.put("endDate1", endDate1);
			map.put("appId", appId);
			map.put("flowId", flowId);
			map.put("busiObjectId", busiObjectId);
			map.put("approverIds", approverIds);
			map.put("orgIds", orgIds);
			
			map.put("holdTime", holdTime);
			map.put("hfTask", hfTask);
			map.put("workDay", workDay);
			map.put("start", null);
			map.put("limit", null);
			
			paramaterJson = JacksonUtils.toJson(map);
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=instanceStatDtoServiceCustomer.statInstanceEfficiencyMxPage(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
		    	String resultInfo= dubboServiceResultInfo.getResult();
		    	PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class, ApprovalStatDto.class);
				result.setResult(pageInfo.getList());
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
				//String fileName = "instance_stat_export.xlsx";
				String fileName = "流程效率统计明细.xlsx";
				String header[] = {"流程状态","流程模板名称","流程实例名称","业务系统","业务对象","停留时长","到达时间","处理时间","处理人","处理人所在部门","处理人所在公司","发起人","发起人所在部门","发起人所在公司"};
				ExportDataUtils exportExcel = new ExportDataUtils("流程效率统计明细", header);
				exportExcel = exportExcel.addTaskEffiencyDataList(pageInfo.getList());
	    		exportExcel.write(response, fileName);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
		}
	}
}
