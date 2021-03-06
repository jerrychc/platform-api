package com.xinleju.platform.finance.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.xinleju.cloud.oa.content.dto.ContentChildTreeData;
import com.xinleju.erp.flow.flowutils.bean.FlowResult;
import com.xinleju.erp.flow.flowutils.bean.PageBean;
import com.xinleju.erp.sm.cache.api.SyncFinaSaData;
import com.xinleju.erp.sm.extend.dto.FinaData;
import com.xinleju.erp.sm.extend.dto.FinaQueryParams;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.finance.dto.AccountCaptionDto;
import com.xinleju.platform.finance.dto.AccountSetCompanyDto;
import com.xinleju.platform.finance.dto.SysBizItemDto;
import com.xinleju.platform.finance.dto.service.AccountCaptionDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.AccountSetCompanyDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.SysBizItemDtoServiceCustomer;
import com.xinleju.platform.finance.utils.CommonConsumer;
import com.xinleju.platform.sys.base.dto.PayTypeDto;
import com.xinleju.platform.sys.base.dto.service.PayTypeDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 会计科目对照项注册控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/finance/sysBizItem")
public class SysBizItemController {

	private static Logger log = Logger.getLogger(SysBizItemController.class);
	
	@Autowired
	private SysBizItemDtoServiceCustomer sysBizItemDtoServiceCustomer;
	@Autowired
	private AccountCaptionDtoServiceCustomer accountCaptionDtoServiceCustomer;
	@Autowired
	private AccountSetCompanyDtoServiceCustomer accountSetCompanyDtoServiceCustomer;
	@Autowired
	private PayTypeDtoServiceCustomer payTypeDtoServiceCustomer;
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
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=sysBizItemDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysBizItemDto sysBizItemDto=JacksonUtils.fromJson(resultInfo, SysBizItemDto.class);
				result.setResult(sysBizItemDto);
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
		    String dubboResultInfo=sysBizItemDtoServiceCustomer.getPage(userJson, paramaterJson);
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
	@RequestMapping(value="/getSysBizItempage",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getSysBizItempage(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=sysBizItemDtoServiceCustomer.getSysBizItempage(userJson, paramaterJson);
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
			String dubboResultInfo=sysBizItemDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysBizItemDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysBizItemDto.class);
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
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/getSysBizItemList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getSysBizItemList(@RequestBody Map<String,Object> map,HttpServletRequest req){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		List<ContentChildTreeData> resultList = new ArrayList<ContentChildTreeData>();
		try {
			String  prefixId= (String) map.get("prefixId");
			if(StringUtils.isBlank(prefixId)){
				String dubboResultInfo= sysBizItemDtoServiceCustomer.getSysBizItemList(userJson, paramaterJson);
				DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
				List<SysBizItemDto> list=JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), ArrayList.class,SysBizItemDto.class);
				if (list.size() > 0) {
					for (SysBizItemDto treeNode : list) {
						ContentChildTreeData contentChildTreeData = new ContentChildTreeData();
						contentChildTreeData.setpId("1");
						contentChildTreeData.setName(String.valueOf(treeNode.getName()));
						contentChildTreeData.setId(String.valueOf(treeNode.getId()));
						contentChildTreeData.setParentId("1");
						contentChildTreeData.setCode(String.valueOf(treeNode.getId()));
						contentChildTreeData.setContentTypeCode("parent");
						resultList.add(contentChildTreeData);
						result.setResult(resultList);
					}
				}
			}else{
				AccountCaptionDto accountCaptionDto = getAccountCaptionDto(prefixId,result,userJson);
				if(StringUtils.isNotBlank(accountCaptionDto.getBizItemIds())){
					String dubboResultInfo= sysBizItemDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+accountCaptionDto.getBizItemIds()+"\"}");
					DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);	
					SysBizItemDto sysBizItemDto=JacksonUtils.fromJson(dubboServiceResultInfo.getResult(),SysBizItemDto.class);
					String url = sysBizItemDto.getUrl();//类的接口
					String method = sysBizItemDto.getMethod();
					Class <?> c= Class.forName(url);
				    CommonConsumer commonConsumer = new CommonConsumer();
				    commonConsumer.loadConsumerToMap(c, url,req);
				    Map<String,Object> consumerMap = commonConsumer.getConsumerMap();
				    Object obj = consumerMap.get(url);
				    List<FinaData> list = null;
				    
				    FinaQueryParams finaQueryParams = new FinaQueryParams();
				    String dubboResultInfoCom = accountSetCompanyDtoServiceCustomer.queryList(userJson, paramaterJson);
				    DubboServiceResultInfo dubboServiceResultInfoCom= JacksonUtils.fromJson(dubboResultInfoCom, DubboServiceResultInfo.class);
				    if(dubboServiceResultInfoCom.isSucess()){
						String resultInfo= dubboServiceResultInfoCom.getResult();
						List<AccountSetCompanyDto> listCom=JacksonUtils.fromJson(resultInfo, ArrayList.class,AccountSetCompanyDto.class);
					    finaQueryParams.setCorpId(listCom.get(0).getCompanyId());
					    finaQueryParams.setCurrentPage(0);
				    }
				    
				    if(url.contains("com.xinleju.erp")){
					    Method addMethod = c.getDeclaredMethod(method, new Class[]{FinaQueryParams.class});
					    FlowResult ResultInfoJson  = (FlowResult) addMethod.invoke(obj,new Object[] {finaQueryParams});
		
					    PageBean<FinaData> page=(PageBean<FinaData>)ResultInfoJson.getResult();
					    list = page.getItems();
					    if(sysBizItemDto.getName().equals("银行账户")){
						    for(FinaData data : list){
						    	data.put("code", data.get("id")+"");
						    	data.put("contentTypeCode", "sun");
						    }
						    result.setResult(list);
					    }
					    
					    if(sysBizItemDto.getName().equals("代收类型")){
					    	List<FinaData> listD = new ArrayList<FinaData>();
						    for(FinaData data : list){
						    	FinaData data1 = new FinaData();
						    	data1.put("code", data.get("rid")+"");
						    	data1.put("name", data.get("code") + " " + data.get("name")+"");
						    	data1.put("id", data.get("rid")+"");
						    	data1.put("parentId", data.get("pid")+"");
						    	data1.put("contentTypeCode", "sun");
						    	listD.add(data1);
						    }
						    result.setResult(listD);
					    }
					    
					    
				    }else{
				    	Method addMethod = c.getDeclaredMethod(method, new Class[]{String.class,String.class});
				    	Map<String,Object> param = new HashMap<String,Object>();
					    param.put("finaQueryParams", JacksonUtils.toJson(finaQueryParams));
					    String ResultInfoJson  = (String) addMethod.invoke(obj,userJson,JacksonUtils.toJson(param));
				        DubboServiceResultInfo MappingDubboServiceResultInfo= JacksonUtils.fromJson(ResultInfoJson, DubboServiceResultInfo.class);
				        if(MappingDubboServiceResultInfo.isSucess()){
				        	list = new ArrayList<FinaData>();
				        	List<Map<String,Object>> resultlist = JacksonUtils.fromJson(MappingDubboServiceResultInfo.getResult(), ArrayList.class);
				        	if(sysBizItemDto.getName().equals("银行账户") || sysBizItemDto.getName().equals("代收类型")){
							    for(Map<String,Object> data : resultlist){
							    	data.put("code", data.get("id")+"");
							    }
						    }
				        	for(Map<String,Object> objMap : resultlist){
								FinaData data = new FinaData();
								data.put("id", objMap.get("id")+"");
								data.put("code", objMap.get("code")+"");
								data.put("name",objMap.get("name")+"");
								data.put("parentId",objMap.get("parentId")+"");
								data.put("contentTypeCode", "sun");
								list.add(data);
							}
				        	result.setResult(list);
				        }
				    	
				    }
				}else{
					result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg("父级科目没有业务系统数据！");
				}
	
			}
			
			
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	private AccountCaptionDto getAccountCaptionDto(String id,MessageResult result,String userJson){
		AccountCaptionDto accountCaptionDto = null;
		String dubboResultCapInfo=accountCaptionDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
		DubboServiceResultInfo dubboServiceResultCapInfo= JacksonUtils.fromJson(dubboResultCapInfo, DubboServiceResultInfo.class);
		if(dubboServiceResultCapInfo.isSucess()){
			String resultInfo= dubboServiceResultCapInfo.getResult();
			accountCaptionDto=JacksonUtils.fromJson(resultInfo, AccountCaptionDto.class);
			if(StringUtils.isNotBlank(accountCaptionDto.getParentId()) && !accountCaptionDto.getParentId().equals("0")){
				accountCaptionDto = getAccountCaptionDto(accountCaptionDto.getParentId(),result,userJson);
			}else{
				return accountCaptionDto;
			}
		}else{
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultCapInfo.getExceptionMsg()+"】");
		}
		return accountCaptionDto;
	}

	/**
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody SysBizItemDto t){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=sysBizItemDtoServiceCustomer.save(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysBizItemDto sysBizItemDto=JacksonUtils.fromJson(resultInfo, SysBizItemDto.class);
				result.setResult(sysBizItemDto);
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
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=sysBizItemDtoServiceCustomer.deleteObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysBizItemDto sysBizItemDto=JacksonUtils.fromJson(resultInfo, SysBizItemDto.class);
				result.setResult(sysBizItemDto);
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
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deleteBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=sysBizItemDtoServiceCustomer.deleteAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysBizItemDto sysBizItemDto=JacksonUtils.fromJson(resultInfo, SysBizItemDto.class);
				result.setResult(sysBizItemDto);
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
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		SysBizItemDto sysBizItemDto=null;
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=sysBizItemDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=sysBizItemDtoServiceCustomer.update(userJson, updateJson);
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
			 String  paramJson = mapper.writeValueAsString(sysBizItemDto);
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
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=sysBizItemDtoServiceCustomer.deletePseudoObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysBizItemDto sysBizItemDto=JacksonUtils.fromJson(resultInfo, SysBizItemDto.class);
				result.setResult(sysBizItemDto);
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
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudoBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=sysBizItemDtoServiceCustomer.deletePseudoAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysBizItemDto sysBizItemDto=JacksonUtils.fromJson(resultInfo, SysBizItemDto.class);
				result.setResult(sysBizItemDto);
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
	
}
