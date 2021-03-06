package com.xinleju.platform.finance.controller;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javassist.bytecode.SignatureAttribute.ClassType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.erp.flow.flowutils.bean.FlowResult;
import com.xinleju.erp.flow.flowutils.bean.PageBean;
import com.xinleju.erp.sm.extend.dto.FinaData;
import com.xinleju.erp.sm.extend.dto.FinaQueryParams;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.ErrorInfoCode;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.finance.dto.AssMappingDto;
import com.xinleju.platform.finance.dto.AssTypeDto;
import com.xinleju.platform.finance.dto.BusinessObjectDto;
import com.xinleju.platform.finance.dto.SysRegisterDto;
import com.xinleju.platform.finance.dto.service.AssMappingDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.AssTypeDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.BusinessObjectDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.SysRegisterDtoServiceCustomer;
import com.xinleju.platform.finance.utils.CommonConsumer;
import com.xinleju.platform.finance.utils.IsDirectCode;
import com.xinleju.platform.finance.utils.NCSendData;
import com.xinleju.platform.finance.utils.NCXMlParse;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 辅助核算明细控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/finance/assMapping")
public class AssMappingController {

	private static Logger log = Logger.getLogger(AssMappingController.class);
	
	@Autowired
	private AssMappingDtoServiceCustomer assMappingDtoServiceCustomer;
	@Autowired
	private BusinessObjectDtoServiceCustomer finanaceBusinessObjectDtoServiceCustomer;
	@Autowired
	private AssTypeDtoServiceCustomer assTypeDtoServiceCustomer;
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
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			String dubboResultInfo=assMappingDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AssMappingDto assMappingDto=JacksonUtils.fromJson(resultInfo, AssMappingDto.class);
				result.setResult(assMappingDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			//e.printStackTrace();
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
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
		    String dubboResultInfo=assMappingDtoServiceCustomer.getPage(userJson, paramaterJson);
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
			//e.printStackTrace();
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
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			String dubboResultInfo=assMappingDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<AssMappingDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,AssMappingDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			//e.printStackTrace();
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
	public @ResponseBody MessageResult save(@RequestBody AssMappingDto t){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=assMappingDtoServiceCustomer.save(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AssMappingDto assMappingDto=JacksonUtils.fromJson(resultInfo, AssMappingDto.class);
				result.setResult(assMappingDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
		} catch (Exception e) {
			try {
				//e.printStackTrace();
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
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			String dubboResultInfo=assMappingDtoServiceCustomer.deleteObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AssMappingDto assMappingDto=JacksonUtils.fromJson(resultInfo, AssMappingDto.class);
				result.setResult(assMappingDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			//e.printStackTrace();
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
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			String dubboResultInfo=assMappingDtoServiceCustomer.deleteAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AssMappingDto assMappingDto=JacksonUtils.fromJson(resultInfo, AssMappingDto.class);
				result.setResult(assMappingDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			//e.printStackTrace();
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
		AssMappingDto assMappingDto=null;
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			String dubboResultInfo=assMappingDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=assMappingDtoServiceCustomer.update(userJson, updateJson);
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
			 String  paramJson = mapper.writeValueAsString(assMappingDto);
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
	 * 伪删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			String dubboResultInfo=assMappingDtoServiceCustomer.deletePseudoObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AssMappingDto assMappingDto=JacksonUtils.fromJson(resultInfo, AssMappingDto.class);
				result.setResult(assMappingDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			//e.printStackTrace();
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
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			String dubboResultInfo=assMappingDtoServiceCustomer.deletePseudoAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AssMappingDto assMappingDto=JacksonUtils.fromJson(resultInfo, AssMappingDto.class);
				result.setResult(assMappingDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			//e.printStackTrace();
		    log.error("调用deletePseudoBatch方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	/**
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/saveAllAssMapp",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult saveAllAssMapp(@RequestBody List<Map<String,Object>> list){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			String saveJson= JacksonUtils.toJson(list);
			String dubboResultInfo=assMappingDtoServiceCustomer.saveAllAssMapp(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AssMappingDto assMappingDto=JacksonUtils.fromJson(resultInfo, AssMappingDto.class);
				result.setResult(assMappingDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setCode(dubboServiceResultInfo.getResult());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getExceptionMsg());
		    }
		} catch (Exception e) {
			try {
				////e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(list);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			} catch (JsonProcessingException e1) {
				//e1.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryListByBusinessObj",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryListByBusinessObj(@RequestBody Map<String,Object> map,HttpServletRequest req){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			//获取业务id assMappingId
			String assMappingId = (String) map.get("assMappingId");
			if(null!=assMappingId&&!"null".equals(assMappingId)){
				//初始化对象
				AssTypeDto assTypeDto = new AssTypeDto();
				BusinessObjectDto businessObjectDto = new BusinessObjectDto();
				
				String dubboResultInfo=assTypeDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+assMappingId+"\"}");
				DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
				if(dubboServiceResultInfo.isSucess()){
					String resultInfo= dubboServiceResultInfo.getResult();
					 assTypeDto=JacksonUtils.fromJson(resultInfo, AssTypeDto.class);
					 //默认设置
					 if(IsDirectCode.DEFAULT.getCode().equals(assTypeDto.getIsDirectCode())){
						String assDubboResultInfo=assMappingDtoServiceCustomer.queryList(userJson, paramaterJson);
					    DubboServiceResultInfo assDubboServiceResultInfo= JacksonUtils.fromJson(assDubboResultInfo, DubboServiceResultInfo.class);
					    if(assDubboServiceResultInfo.isSucess()){
							String assResultInfo= assDubboServiceResultInfo.getResult();
							List<AssMappingDto> list=JacksonUtils.fromJson(assResultInfo, ArrayList.class,AssMappingDto.class);
							result.setResult(list);
							result.setSuccess(MessageInfo.GETSUCCESS.isResult());
							result.setMsg(MessageInfo.GETSUCCESS.getMsg());
					    }else{
					    	result.setSuccess(MessageInfo.GETERROR.isResult());
							result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
					    }
					 }else{
						String objDubboResultInfo=finanaceBusinessObjectDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+assTypeDto.getBizObjectId()+"\"}");
						DubboServiceResultInfo objDubboServiceResultInfo= JacksonUtils.fromJson(objDubboResultInfo, DubboServiceResultInfo.class);
						if(objDubboServiceResultInfo.isSucess()){
							String objResultInfo= objDubboServiceResultInfo.getResult();
							 businessObjectDto=JacksonUtils.fromJson(objResultInfo, BusinessObjectDto.class);
							 //业务对象 获取类
							 String fetchClass = businessObjectDto.getFetchClass();
							//业务对象 获取方法
							 String fetchMethod = businessObjectDto.getFetchMethod();
							//通过完整的类型路径获取类
						    Class<?> classType = Class.forName(fetchClass);
						    
						    CommonConsumer commonConsumer = new CommonConsumer();
						    
						    commonConsumer.loadConsumerToMap(classType, fetchClass,req);
						    
						    Map<String,Object> consumerMap = commonConsumer.getConsumerMap();
						    
						    Object obj = consumerMap.get(fetchClass);
						    List<FinaData> list = null;
						    FinaQueryParams finaQueryParams = new FinaQueryParams();
						    finaQueryParams.setCorpId(assTypeDto.getCompanyId());
						    finaQueryParams.setCurrentPage(0);
						    finaQueryParams.setFlag("1");
						    if(fetchClass.contains("com.xinleju.erp")){
							    //获取InvokeTester类的方法
							    Method addMethod = classType.getDeclaredMethod(fetchMethod, new Class[]{FinaQueryParams.class});
							    FlowResult ResultInfoJson  = (FlowResult) addMethod.invoke(obj,new Object[] {finaQueryParams});
							    PageBean<FinaData> page=(PageBean<FinaData>)ResultInfoJson.getResult();
							    list = page.getItems();
						    }else{
						        //调用invokeTester对象上的方法
						        Method addMethod = classType.getDeclaredMethod(fetchMethod, new Class[]{String.class,String.class});
						        Map<String,Object> param = new HashMap<String,Object>();
						        param.put("companyId", assTypeDto.getCompanyId());
						        param.put("finaQueryParams", JacksonUtils.toJson(finaQueryParams));
							    String ResultInfoJson  = (String) addMethod.invoke(obj,userJson,JacksonUtils.toJson(param));
						        DubboServiceResultInfo MappingDubboServiceResultInfo= JacksonUtils.fromJson(ResultInfoJson, DubboServiceResultInfo.class);
						        if(MappingDubboServiceResultInfo.isSucess()){
						        	list = new ArrayList<FinaData>();
						        	String mappingResultInfo= MappingDubboServiceResultInfo.getResult();
						        	List<Map<String,Object>> resultlist = JacksonUtils.fromJson(mappingResultInfo, ArrayList.class);
									for(Map<String,Object> objMap : resultlist){
										FinaData data = new FinaData();
										data.put("id", objMap.get("id")+"");
										data.put("code", objMap.get("code")+"");
										data.put("name",objMap.get("name")+"");
										list.add(data);
									}
						        }

						    }
				        	if(null!=list){
								String assDubboResultInfo=assMappingDtoServiceCustomer.queryList(userJson, paramaterJson);
							    DubboServiceResultInfo assDubboServiceResultInfo= JacksonUtils.fromJson(assDubboResultInfo, DubboServiceResultInfo.class);
							    if(assDubboServiceResultInfo.isSucess()){
									String assResultInfo= assDubboServiceResultInfo.getResult();
									List<AssMappingDto> assList=JacksonUtils.fromJson(assResultInfo, ArrayList.class,AssMappingDto.class);
									List<AssMappingDto> resList = new ArrayList<AssMappingDto>();
									for(FinaData objMap : list){
										AssMappingDto assMappingDto = new AssMappingDto();
										assMappingDto.setId(objMap.get("id")+"");
										for(AssMappingDto bean : assList){
											if(bean.getObjectId().equals(objMap.get("id")+"")){
												assMappingDto = bean;
												break;
											}
										}
										assMappingDto.setAssMappingId(assTypeDto.getId());
										assMappingDto.setObjectId(objMap.get("id")+"");
										assMappingDto.setObjectItemCode((String)objMap.get("code"));
										assMappingDto.setObjectItemName((String)objMap.get("name"));
										resList.add(assMappingDto);
									}
									result.setResult(resList);
									result.setSuccess(MessageInfo.GETSUCCESS.isResult());
									result.setMsg(MessageInfo.GETSUCCESS.getMsg());
							    }else{
							    	result.setSuccess(MessageInfo.GETERROR.isResult());
									result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
							    }
					        }else{
						    	result.setSuccess(MessageInfo.GETERROR.isResult());
								result.setMsg(MessageInfo.GETERROR.getMsg()+"调用业务系统接口出错");
						    }
						}else{
					    	result.setSuccess(MessageInfo.GETERROR.isResult());
							result.setMsg(MessageInfo.GETERROR.getMsg()+"获取业务对象出错");
					    }
					 }
				}else{
					result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg(MessageInfo.GETERROR.getMsg()+"获取辅助核算类型失败！");
				}
			}else{
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"参数传递为空！");
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
	 * 推送到nc
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/sendAssMappingNC",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult sendAssMappingNC(@RequestBody Map<String,Object> map,HttpServletRequest req){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String xmlFile = "";
			// NC系统返回的xml字符串
			String res = null;
			String dubboResultInfo=assMappingDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<AssMappingDto> assMappingList=JacksonUtils.fromJson(resultInfo, ArrayList.class,AssMappingDto.class);
				Map<String,Object> regmap = new HashMap<String,Object>();
				regmap.put("status", 1);
				regmap.put("delflag", 0);
				String regparamaterJson = JacksonUtils.toJson(regmap);
				String dubboResultSysInfo = sysRegisterDtoServiceCustomer.queryList(userJson, regparamaterJson);
				DubboServiceResultInfo dubboServiceResultSysInfo= JacksonUtils.fromJson(dubboResultSysInfo, DubboServiceResultInfo.class);
				if(dubboServiceResultSysInfo.isSucess()){
					String regresultInfo= dubboServiceResultSysInfo.getResult();
					List<SysRegisterDto> reglist=JacksonUtils.fromJson(regresultInfo, ArrayList.class,SysRegisterDto.class);
					if(reglist != null && reglist.size() > 0){
						SysRegisterDto sysDto = reglist.get(0);
						String webUrl = sysDto.getWebUrl();
						String url = webUrl + "?account=02&receiver=0001" ;
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
						for(AssMappingDto dto : assMappingList){
							String createJson = JacksonUtils.toJson(dto);
							xmlFile = assMappingDtoServiceCustomer.createSyncXml2NC(createJson,sysDto.getSender());
							if (xmlFile != null && !xmlFile.trim().equals("") && !url.trim().equals("") && url != null) {
								res = NCSendData.getPostResponse(url, xmlFile);
							}
//							System.out.println("NC返回的信息===="+res);
							if (null != res) {
								if (NCXMlParse.XmlErrorCode(res) >= 0) {
									dto.setSendStatus("1");// 输出成功
									dto.setSendDate(sf.format(new Date()));
									dto.setErrmsg("");
								} else {
									String errorinfo = NCXMlParse.XmlErrorInfo(res);
									Integer errCode = NCXMlParse.XmlErrorCode(res);
									dto.setSendStatus("2");//  输出失败
									dto.setSendDate(sf.format(new Date()));
									String error = "错误代码：" + errCode.toString() + " 错误内容："+ errorinfo;
									dto.setErrmsg(error);
								}
								//saveFile1(res);
							} else {
								dto.setSendStatus("0");// 未输出
								dto.setSendDate(sf.format(new Date()));
								dto.setErrmsg("生成xml文件失败，未输出！");
							}
							String updateJson = JacksonUtils.toJson(dto);
							assMappingDtoServiceCustomer.update(userJson, updateJson);
						}
						result.setSuccess(MessageInfo.GETSUCCESS.isResult());
						result.setMsg("推送成功");
					}
			    }else{
			    	result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			    }
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
		} catch (Exception e) {
			log.error("调用sendAssMappingNC方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("推送失败");
		}
		return result;
	}
	
	@RequestMapping(value="/sendAssMappingNCRe/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult sendAssMappingNCRe(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String xmlFile = "";
			// NC系统返回的xml字符串
			String res = null;
			String dubboResultInfo=assMappingDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AssMappingDto assMappingDto=JacksonUtils.fromJson(resultInfo, AssMappingDto.class);
				Map<String,Object> regmap = new HashMap<String,Object>();
				regmap.put("status", 1);
				regmap.put("delflag", 0);
				String regparamaterJson = JacksonUtils.toJson(regmap);
				String dubboResultSysInfo = sysRegisterDtoServiceCustomer.queryList(userJson, regparamaterJson);
				DubboServiceResultInfo dubboServiceResultSysInfo= JacksonUtils.fromJson(dubboResultSysInfo, DubboServiceResultInfo.class);
				if(dubboServiceResultSysInfo.isSucess()){
					String regresultInfo= dubboServiceResultSysInfo.getResult();
					List<SysRegisterDto> reglist=JacksonUtils.fromJson(regresultInfo, ArrayList.class,SysRegisterDto.class);
					if(reglist != null && reglist.size() > 0){
						SysRegisterDto sysDto = reglist.get(0);
						String webUrl = sysDto.getWebUrl();
						String url = webUrl + "?account=02&receiver=0001" ;
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
						String createJson = JacksonUtils.toJson(assMappingDto);
						xmlFile = assMappingDtoServiceCustomer.createSyncXml2NC(createJson,sysDto.getSender());
						if (xmlFile != null && !xmlFile.trim().equals("") && !url.trim().equals("") && url != null) {
							res = NCSendData.getPostResponse(url, xmlFile);
						}
//							System.out.println("NC返回的信息===="+res);
						if (null != res) {
							if (NCXMlParse.XmlErrorCode(res) >= 0) {
								assMappingDto.setSendStatus("1");// 输出成功
								assMappingDto.setSendDate(sf.format(new Date()));
								assMappingDto.setErrmsg("");
								result.setSuccess(MessageInfo.GETSUCCESS.isResult());
								result.setMsg("推送成功");
							} else {
								String errorinfo = NCXMlParse.XmlErrorInfo(res);
								Integer errCode = NCXMlParse.XmlErrorCode(res);
								assMappingDto.setSendStatus("2");//  输出失败
								assMappingDto.setSendDate(sf.format(new Date()));
								String error = "错误代码：" + errCode.toString() + " 错误内容："+ errorinfo;
								assMappingDto.setErrmsg(error);
								result.setSuccess(MessageInfo.GETERROR.isResult());
								result.setMsg("推送失败");
							}
							//saveFile1(res);
						} else {
							assMappingDto.setSendStatus("0");// 未输出
							assMappingDto.setSendDate(sf.format(new Date()));
							assMappingDto.setErrmsg("生成xml文件失败，未输出！");
							result.setSuccess(MessageInfo.GETERROR.isResult());
							result.setMsg("推送失败");
						}
						String updateJson = JacksonUtils.toJson(assMappingDto);
						assMappingDtoServiceCustomer.update(userJson, updateJson);
					}
			    }else{
			    	result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			    }
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用sendAssMappingNCRe方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("推送失败");
		}
		return result;
	}
	
	/**
	 * 同步成本合同
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/getCoContract",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult getCoContract(@RequestBody Map<String,Object> map,HttpServletRequest req){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String assMappingId = (String) map.get("assMappingId");
			//初始化对象
			AssTypeDto assTypeDto = new AssTypeDto();
			BusinessObjectDto businessObjectDto = new BusinessObjectDto();
			
			String dubboResultInfo=assTypeDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+assMappingId+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				assTypeDto=JacksonUtils.fromJson(resultInfo, AssTypeDto.class);

				String objDubboResultInfo=finanaceBusinessObjectDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+assTypeDto.getBizObjectId()+"\"}");
				DubboServiceResultInfo objDubboServiceResultInfo= JacksonUtils.fromJson(objDubboResultInfo, DubboServiceResultInfo.class);
				if(objDubboServiceResultInfo.isSucess()){
					String objResultInfo= objDubboServiceResultInfo.getResult();
					 businessObjectDto=JacksonUtils.fromJson(objResultInfo, BusinessObjectDto.class);
					 //业务对象 获取类
					 String fetchClass = businessObjectDto.getFetchClass();
					//业务对象 获取方法
					 String fetchMethod = businessObjectDto.getFetchMethod();
					//业务对象回掉方法
					 String fetchBackMethod = businessObjectDto.getCallbackMethod();
					//通过完整的类型路径获取类
				    Class<?> classType = Class.forName(fetchClass);
				    
				    CommonConsumer commonConsumer = new CommonConsumer();
				    
				    commonConsumer.loadConsumerToMap(classType, fetchClass,req);
				    
				    Map<String,Object> consumerMap = commonConsumer.getConsumerMap();
				    
				    Object obj = consumerMap.get(fetchClass);
				    List<FinaData> list = null;
				    //获取InvokeTester类的方法
				    Method addMethod = classType.getDeclaredMethod(fetchMethod, new Class[]{FinaQueryParams.class});
				    
				    Method backMethod = classType.getDeclaredMethod(fetchBackMethod, new Class[]{FinaData.class});

				    FinaQueryParams finaQueryParams = new FinaQueryParams();
				    finaQueryParams.setCorpId(assTypeDto.getCompanyId());
				    finaQueryParams.setCurrentPage(0);
				    finaQueryParams.setFlag("2");
				    FlowResult ResultInfoJson  = (FlowResult) addMethod.invoke(obj,new Object[] {finaQueryParams});

				    PageBean<FinaData> page=(PageBean<FinaData>)ResultInfoJson.getResult();
				    list = page.getItems();
				    List<Long> finaSuccessList = new ArrayList<Long>();
					List<Long> finaErrorList = new ArrayList<Long>();
					FinaData finaData = new FinaData();
		        	if(null!=list){
		        		for(FinaData objMap : list){
		        			AssMappingDto assMappingDto = null;
		        			Map<String,Object> assMappingMap = new HashMap<String,Object>();
		        			assMappingMap.put("assMappingId", assTypeDto.getId());
		        			assMappingMap.put("objectId", objMap.get("id")+"");
		        			assMappingMap.put("delflag", "0");
		        			String dubboResultInfoAssMapping = assMappingDtoServiceCustomer.queryList(userJson, JacksonUtils.toJson(assMappingMap));
		        			DubboServiceResultInfo dubboServiceResultInfoAssMapping= JacksonUtils.fromJson(dubboResultInfoAssMapping, DubboServiceResultInfo.class);
		        			List<AssMappingDto> assMappingList = null ;
		        			if(dubboServiceResultInfoAssMapping.isSucess()){
		        				assMappingList=JacksonUtils.fromJson(dubboServiceResultInfoAssMapping.getResult(), ArrayList.class,AssMappingDto.class);
		        				if(null != assMappingList && assMappingList.size()>0){
		        					assMappingDto = assMappingList.get(0);
		        				}else{
		        					assMappingDto = new AssMappingDto();
		        					assMappingDto.setId(IDGenerator.getUUID());
		        				}
		        			}else{
		        				assMappingDto = new AssMappingDto();
		        				assMappingDto.setId(IDGenerator.getUUID());
		        			}
							
							assMappingDto.setAssMappingId(assTypeDto.getId());
							assMappingDto.setAssItemCode((String)objMap.get("code"));
							assMappingDto.setAssItemName((String)objMap.get("name"));
							assMappingDto.setObjectId(objMap.get("id")+"");
							assMappingDto.setObjectItemCode((String)objMap.get("code"));
							assMappingDto.setObjectItemName((String)objMap.get("name"));
							assMappingDto.setSendStatus("0");
							assMappingDto.setErrmsg("");
							try{
								String saveJson= JacksonUtils.toJson(assMappingDto);
								if(null != assMappingList && assMappingList.size()>0){
									assMappingDtoServiceCustomer.update(userJson, saveJson);
								}else{
									assMappingDtoServiceCustomer.save(userJson, saveJson);
								}
								finaSuccessList.add(Long.valueOf(objMap.get("id")+""));
							}catch(Exception e){
								finaErrorList.add(Long.valueOf(objMap.get("id")+""));
								e.printStackTrace();
							}
							finaData.put("success", finaSuccessList);
							finaData.put("error", finaErrorList);
						}
		        		backMethod.invoke(obj,new Object[] {finaData});
		        		result.setSuccess(MessageInfo.GETSUCCESS.isResult());
			        }else{
				    	result.setSuccess(MessageInfo.GETERROR.isResult());
						result.setMsg(MessageInfo.GETERROR.getMsg()+"调用业务系统接口出错");
				    }
				}else{
			    	result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg(MessageInfo.GETERROR.getMsg()+"获取业务对象出错");
			    }
			 
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"获取辅助核算类型失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用sendAssMappingNC方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("推送失败");
		}
		return result;
	}
}
