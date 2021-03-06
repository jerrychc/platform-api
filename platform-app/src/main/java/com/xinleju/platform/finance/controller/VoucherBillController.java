package com.xinleju.platform.finance.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.xinleju.erp.data.cache.api.MDFinanceCacheService;
import com.xinleju.erp.data.cache.dto.FinancePaymentTypeDTO;
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
import com.xinleju.platform.finance.dto.AccountSetCompanyDto;
import com.xinleju.platform.finance.dto.AccountSetDto;
import com.xinleju.platform.finance.dto.BusinessFieldDto;
import com.xinleju.platform.finance.dto.BusinessObjectDto;
import com.xinleju.platform.finance.dto.SysRegisterDto;
import com.xinleju.platform.finance.dto.VoucherBillDto;
import com.xinleju.platform.finance.dto.VoucherBillEntryDto;
import com.xinleju.platform.finance.dto.VoucherData;
import com.xinleju.platform.finance.dto.VoucherTemplateDto;
import com.xinleju.platform.finance.dto.VoucherTemplateTypeDto;
import com.xinleju.platform.finance.dto.service.AccountSetCompanyDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.AccountSetDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.BusinessFieldDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.BusinessObjectDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.SysRegisterDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.VoucherBillDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.VoucherTemplateDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.VoucherTemplateTypeDtoServiceCustomer;
import com.xinleju.platform.finance.utils.CommonConsumer;
import com.xinleju.platform.finance.utils.NCSendData;
import com.xinleju.platform.finance.utils.NCXMlParse;
import com.xinleju.platform.finance.utils.QMap;
import com.xinleju.platform.sys.base.dto.PayTypeDto;
import com.xinleju.platform.sys.base.dto.service.PayTypeDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 凭证控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/finance/voucherBill")
public class VoucherBillController {

	private static Logger log = Logger.getLogger(VoucherBillController.class);
	
	@Autowired
	private VoucherBillDtoServiceCustomer voucherBillDtoServiceCustomer;
	@Autowired
	private AccountSetDtoServiceCustomer accountSetDtoServiceCustomer;
	@Autowired
	private SysRegisterDtoServiceCustomer sysRegisterDtoServiceCustomer;
	@Autowired
	private VoucherTemplateTypeDtoServiceCustomer voucherTemplateTypeDtoServiceCustomer;
	@Autowired
	private VoucherTemplateDtoServiceCustomer voucherTemplateDtoServiceCustomer;
	@Autowired
	private BusinessObjectDtoServiceCustomer finanaceBusinessObjectDtoServiceCustomer;
	@Autowired
	private BusinessFieldDtoServiceCustomer businessFieldDtoServiceCustomer;
	@Autowired
	private AccountSetCompanyDtoServiceCustomer accountSetCompanyDtoServiceCustomer;
	@Autowired
	private MDFinanceCacheService mDFinanceCacheService;
	@Autowired
	private SyncFinaSaData syncFinaSaData;
	@Autowired
	private PayTypeDtoServiceCustomer payTypeDtoServiceCustomer;
	
	/** 凭证分录概要模板 变量表达式 */
	private static final String VOUCHER_ENTRY_SUMMARY_VAR_EXPR = "\\{!(.+?):(.+?);\\}";
	private FinaQueryParams finaQueryParams = new FinaQueryParams();
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
			
			String dubboResultInfo=voucherBillDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				VoucherBillDto voucherBillDto=JacksonUtils.fromJson(resultInfo, VoucherBillDto.class);
				result.setResult(voucherBillDto);
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
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
		    String dubboResultInfo=voucherBillDtoServiceCustomer.getPage(userJson, paramaterJson);
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
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=voucherBillDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<VoucherBillDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,VoucherBillDto.class);
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
	public @ResponseBody MessageResult save(@RequestBody VoucherBillDto t){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=voucherBillDtoServiceCustomer.save(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				VoucherBillDto voucherBillDto=JacksonUtils.fromJson(resultInfo, VoucherBillDto.class);
				result.setResult(voucherBillDto);
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
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=voucherBillDtoServiceCustomer.deleteObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				VoucherBillDto voucherBillDto=JacksonUtils.fromJson(resultInfo, VoucherBillDto.class);
				result.setResult(voucherBillDto);
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
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=voucherBillDtoServiceCustomer.deleteAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				VoucherBillDto voucherBillDto=JacksonUtils.fromJson(resultInfo, VoucherBillDto.class);
				result.setResult(voucherBillDto);
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
		VoucherBillDto voucherBillDto=null;
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=voucherBillDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=voucherBillDtoServiceCustomer.update(userJson, updateJson);
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
			 String  paramJson = mapper.writeValueAsString(voucherBillDto);
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
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=voucherBillDtoServiceCustomer.deletePseudoObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				VoucherBillDto voucherBillDto=JacksonUtils.fromJson(resultInfo, VoucherBillDto.class);
				result.setResult(voucherBillDto);
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
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=voucherBillDtoServiceCustomer.deletePseudoAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				VoucherBillDto voucherBillDto=JacksonUtils.fromJson(resultInfo, VoucherBillDto.class);
				result.setResult(voucherBillDto);
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
	 * 返回生成凭证页显示的列
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/getBillCol",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getBillCol(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			//查询凭证模板业务类型
			String dubboTempTypeResultInfo=voucherTemplateTypeDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+map.get("billType")+"\"}");
			DubboServiceResultInfo dubboTempTypeServiceResultInfo= JacksonUtils.fromJson(dubboTempTypeResultInfo, DubboServiceResultInfo.class);
			if(dubboTempTypeServiceResultInfo.isSucess()){
				String tempTypeInfo= dubboTempTypeServiceResultInfo.getResult();
				VoucherTemplateTypeDto voucherTemplateType=JacksonUtils.fromJson(tempTypeInfo, VoucherTemplateTypeDto.class);
				//查询凭证模板
				Map<String,String> templateMap = new HashMap<String,String>();
				templateMap.put("typeId", voucherTemplateType.getId());
				String paramaterTemplateJson = JacksonUtils.toJson(templateMap);
				String dubboTemplateResultInfo=voucherTemplateDtoServiceCustomer.queryList(userJson, paramaterTemplateJson);
				DubboServiceResultInfo dubboTemplateServiceResultInfo= JacksonUtils.fromJson(dubboTemplateResultInfo, DubboServiceResultInfo.class);
				if(dubboTemplateServiceResultInfo.isSucess()){
					String resultTemplateInfo= dubboTemplateServiceResultInfo.getResult();
					List<VoucherTemplateDto> templateList=JacksonUtils.fromJson(resultTemplateInfo, ArrayList.class,VoucherTemplateDto.class);
					//查询业务对象注册字段
					if(templateList !=null && templateList.size() > 0){
						Map<String,String> objectFileMap = new HashMap<String,String>();
						objectFileMap.put("bizObjectId", templateList.get(0).getBizObjectId());
						objectFileMap.put("display", "1");
						objectFileMap.put("delflag", "0");
						String paramaterObjectFileJson = JacksonUtils.toJson(objectFileMap);
						String dubboObjectFileResultInfo=businessFieldDtoServiceCustomer.queryList(userJson, paramaterObjectFileJson);
						DubboServiceResultInfo dubboObjectFileServiceResultInfo= JacksonUtils.fromJson(dubboObjectFileResultInfo, DubboServiceResultInfo.class);
						if(dubboObjectFileServiceResultInfo.isSucess()){
							String resultFileInfo= dubboObjectFileServiceResultInfo.getResult();
							List<BusinessFieldDto> filedList=JacksonUtils.fromJson(resultFileInfo, ArrayList.class,BusinessFieldDto.class);
							result.setResult(filedList);
							result.setSuccess(MessageInfo.GETSUCCESS.isResult());
							result.setMsg(MessageInfo.GETSUCCESS.getMsg());
						}else{
							result.setCode(dubboObjectFileServiceResultInfo.getResult());
						    result.setSuccess(dubboObjectFileServiceResultInfo.isSucess());
						    result.setMsg(dubboObjectFileServiceResultInfo.getMsg());
						}
					}
				}else{
					result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboTemplateServiceResultInfo.getExceptionMsg()+"】");
				}
				
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboTempTypeServiceResultInfo.getExceptionMsg()+"】");
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
	 * 返回业务系统单据
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/getBillPage",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getBillPage(@RequestBody Map<String,Object> map,HttpServletRequest req){
//		HttpServletRequest  request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = req.getSession();
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			FlowResult<PageBeanInfo<FinaData>> flowResult = null;
			finaQueryParams.setBillDateBegin(map.get("authBegain")+"");
			finaQueryParams.setBillDateEnd(map.get("authEnd")+"");
			finaQueryParams.setAuditDateBegin(map.get("appauthBegain")+"");
			finaQueryParams.setAuditDateEnd(map.get("appauthEnd")+"");
			finaQueryParams.setCurrentPage(Integer.valueOf(map.get("page")+""));
			finaQueryParams.setPageSize(Integer.valueOf(map.get("rows")+""));
			finaQueryParams.setCreateVouchStatus(Integer.valueOf(map.get("selectType")+""));
			finaQueryParams.setCorpId(map.get("companyId")+"");
			
			
			/*//查询凭证模板业务类型
			String dubboTempTypeResultInfo=voucherTemplateTypeDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+map.get("billType")+"\"}");
			DubboServiceResultInfo dubboTempTypeServiceResultInfo= JacksonUtils.fromJson(dubboTempTypeResultInfo, DubboServiceResultInfo.class);
			if(dubboTempTypeServiceResultInfo.isSucess()){
				String resultInfo= "{\"total\":2,\"limit\":20,\"start\":0,\"list\":[{\"id\":\"121\",\"1\":\"1\",\"2\":\"2\",\"4\":\"4\",\"7\":\"7\",\"5\":\"5\",\"6\":\"6\",\"billType\":\"收款单\"},{\"id\":\"122\",\"1\":\"11\",\"2\":\"21\",\"4\":\"41\",\"7\":\"71\",\"5\":\"51\",\"6\":\"61\",\"billType\":\"收款单\"}]}";
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				session.setAttribute("voucherPangeInfo", pageInfo);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
				//session.setAttribute("voucherPangeInfo", resultInfo);
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboTempTypeServiceResultInfo.getExceptionMsg()+"】");
			}*/
			//查询凭证模板业务类型
			String dubboTempTypeResultInfo=voucherTemplateTypeDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+map.get("billType")+"\"}");
			DubboServiceResultInfo dubboTempTypeServiceResultInfo= JacksonUtils.fromJson(dubboTempTypeResultInfo, DubboServiceResultInfo.class);
			if(dubboTempTypeServiceResultInfo.isSucess()){
				String tempTypeInfo= dubboTempTypeServiceResultInfo.getResult();
				VoucherTemplateTypeDto voucherTemplateType=JacksonUtils.fromJson(tempTypeInfo, VoucherTemplateTypeDto.class);
				//查询凭证模板
				Map<String,String> templateMap = new HashMap<String,String>();
				templateMap.put("typeId", voucherTemplateType.getId());
				String paramaterTemplateJson = JacksonUtils.toJson(templateMap);
				String dubboTemplateResultInfo=voucherTemplateDtoServiceCustomer.queryList(userJson, paramaterTemplateJson);
				DubboServiceResultInfo dubboTemplateServiceResultInfo= JacksonUtils.fromJson(dubboTemplateResultInfo, DubboServiceResultInfo.class);
				if(dubboTemplateServiceResultInfo.isSucess()){
					String resultTemplateInfo= dubboTemplateServiceResultInfo.getResult();
					List<VoucherTemplateDto> templateList=JacksonUtils.fromJson(resultTemplateInfo, ArrayList.class,VoucherTemplateDto.class);
					//查询业务对象
					if(templateList !=null && templateList.size() > 0){
						/*Map<String,String> objectMap = new HashMap<String,String>();
						objectMap.put("bizObjectId", templateList.get(0).getBizObjectId());
						objectMap.put("status", "1");
						String paramaterObjectJson = JacksonUtils.toJson(objectMap);
						String dubboObjectResultInfo=finanaceBusinessObjectDtoServiceCustomer.queryList(userJson, paramaterObjectJson);*/
						String dubboObjectResultInfo=finanaceBusinessObjectDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+templateList.get(0).getBizObjectId()+"\"}");
						DubboServiceResultInfo dubboObjectServiceResultInfo= JacksonUtils.fromJson(dubboObjectResultInfo, DubboServiceResultInfo.class);
						if(dubboObjectServiceResultInfo.isSucess()){
							String resultObjectInfo= dubboObjectServiceResultInfo.getResult();
//							List<BusinessObjectDto> objectList=JacksonUtils.fromJson(resultObjectInfo, ArrayList.class,BusinessObjectDto.class);
							BusinessObjectDto businessObjectDto=JacksonUtils.fromJson(resultObjectInfo, BusinessObjectDto.class);
							if(businessObjectDto != null){
								finaQueryParams.setBillType(businessObjectDto.getName());
							}
							//查询公司对照
							Map<String,Object> companyMap = new HashMap<String,Object>();
							companyMap.put("accountSetId", map.get("accountSetId"));
							companyMap.put("delflag", 0);
							String companyJson = JacksonUtils.toJson(companyMap);
							String dubboResultInfo=accountSetCompanyDtoServiceCustomer.queryList(userJson, companyJson);
						    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
						    List<AccountSetCompanyDto> accountSetCompanyList = new ArrayList<AccountSetCompanyDto>();
						    if(dubboServiceResultInfo.isSucess()){
								String resultInfo= dubboServiceResultInfo.getResult();
								accountSetCompanyList=JacksonUtils.fromJson(resultInfo, ArrayList.class,AccountSetCompanyDto.class);
						    }
						    //查询账套公司
						    // 查询FiAccountSet实体
							String accountSetDubboResultInfo = accountSetDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+map.get("accountSetId")+""+"\"}");
							DubboServiceResultInfo accountSetDubboServiceResultInfo= JacksonUtils.fromJson(accountSetDubboResultInfo, DubboServiceResultInfo.class);
							AccountSetDto accountSetDto = new AccountSetDto();
							if(accountSetDubboServiceResultInfo.isSucess()){
								String fiAccountSet= accountSetDubboServiceResultInfo.getResult();
								accountSetDto=JacksonUtils.fromJson(fiAccountSet, AccountSetDto.class);
							}
							// 获取模板的筛选条件
							String filter = templateList.get(0).getFilter();
							// 转换IK表达式
							Pattern pt = Pattern.compile(VOUCHER_ENTRY_SUMMARY_VAR_EXPR);
							Matcher mt = pt.matcher(filter);
							StringBuffer filResult = new StringBuffer();
							while (mt.find()) {
								mt.appendReplacement(filResult, mt.group(1));
							}
							mt.appendTail(filResult);

							// 将将单引号转换成双引号
							String condition = filResult.toString();
							condition = machCodition(condition, accountSetCompanyList, map.get("companyName")+"",accountSetDto.getAppCode());
							List<String> projectidList = new ArrayList<String>();
							for(AccountSetCompanyDto dto:accountSetCompanyList){
								if(dto.getProjectBranchId()!=null && StringUtils.isNotBlank(dto.getProjectBranchId()+"") && !dto.getProjectBranchId().equals("null")){
									projectidList.add(dto.getProjectBranchId());
								}
							}
							if(accountSetDto.getAppCode().equals("SA")){
								finaQueryParams.setBillType("");//销售系统：同模板的业务对象筛选条件
								finaQueryParams.setProjectIdList(projectidList);
								finaQueryParams.setHousename(map.get("housename")!=null?map.get("housename")+"":"");
								finaQueryParams.setBillNumber(map.get("feebillcode")!=null?map.get("feebillcode")+"":"");//billNumber
								finaQueryParams.setVjkrname(map.get("vjkrname")!=null?map.get("vjkrname")+"":"");//vjkrname
							}else if(accountSetDto.getAppCode().equals("CO")){
								finaQueryParams.setProjectIdList(projectidList); 
								finaQueryParams.setSubject(map.get("subject")!=null?map.get("subject")+"":"");//subject
								finaQueryParams.setBillNumber(map.get("billcode")!=null?map.get("billcode")+"":"");//billNumber
							}else if(accountSetDto.getAppCode().equals("EX")){
								if(condition.contains("nchmny!=0 and npaymny=0")){
									finaQueryParams.setSubject(map.get("subject")!=null?map.get("subject")+"":"");//subject
									finaQueryParams.setBillNumber(map.get("billcode")!=null?map.get("billcode")+"":"");//billNumber
									finaQueryParams.setVapplicant(map.get("vapplicant")!=null?map.get("vapplicant")+"":"");//vapplicant
									finaQueryParams.setNexpensemny(map.get("nexpensemny")!=null?map.get("nexpensemny")+"":"");//nexpensemny
									finaQueryParams.setBillDateBegin(null);
									finaQueryParams.setBillDateEnd(null);
								}
							}
							if(businessObjectDto != null ){
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
							    
							    //使用默认构造函数获取对象
						        //Object invokeTester = classType.newInstance();
						        //调用invokeTester对象上的方法
//							    String ResultInfoJson  = (String) addMethod.invoke(obj,userJson,finaQueryParams,condition);//new Object[] {"myname",888}
							    PageBean<FinaData> page = new PageBean<FinaData>();
							    if(fetchClass.contains("com.xinleju.erp")){
							    	//获取InvokeTester类的方法
								    Method addMethod = classType.getDeclaredMethod(fetchMethod, new Class[]{FinaQueryParams.class,String.class});
								    FlowResult ResultInfoJson  = (FlowResult) addMethod.invoke(obj,new Object[] {finaQueryParams,condition});
								    page = (PageBean<FinaData>)ResultInfoJson.getResult();
							    }else{
							    	 Method addMethod = classType.getDeclaredMethod(fetchMethod, new Class[]{String.class,String.class});
							    	Map<String,Object> param = new HashMap<String,Object>();
							        param.put("condition", condition);
							        param.put("finaQueryParams", JacksonUtils.toJson(finaQueryParams));
							        String ResultInfoJson  = (String) addMethod.invoke(obj,userJson,JacksonUtils.toJson(param));
							        DubboServiceResultInfo MappingDubboServiceResultInfo= JacksonUtils.fromJson(ResultInfoJson, DubboServiceResultInfo.class);
							        if(MappingDubboServiceResultInfo.isSucess()){
							        	String mappingResultInfo= MappingDubboServiceResultInfo.getResult();
							        	FlowResult<PageBean<FinaData>> resultlist = JacksonUtils.fromJson(mappingResultInfo, FlowResult.class);
//							        	page = (PageBean<FinaData>)resultlist.getResult();
							        	Map<String,Object> resultmap = (Map<String,Object>)resultlist.getResult();
							        	page.setStart(Integer.valueOf(resultmap.get("start").toString()));
							        	page.setTotal(Integer.valueOf(resultmap.get("total").toString()));
							        	page.setLimit(Integer.valueOf(resultmap.get("limit").toString()));
							        	List<Map<String,Object>> finaDataMap = (List<Map<String,Object>>)resultmap.get("items");
							        	List<FinaData> finaDataList = new ArrayList<FinaData>();
							        	for(Map<String,Object> finamap : finaDataMap){
							        		FinaData finadata = new FinaData();
							        		for(String str : finamap.keySet()){
							        			finadata.put(str, finamap.get(str));
							        		}
							        		finaDataList.add(finadata);
							        	}
							        	page.setItems(finaDataList);
							        }
							    }
					        	List<FinaData> fds = page.getItems();
								List<FinaData> jsonFds = new ArrayList<FinaData>();
					        	for (FinaData f : fds) {
									if (String.valueOf(f.get("type")).equals("单据")) {
										jsonFds.add(f);
									}
								 }
					        	PageBean pageBean = new PageBean();
//						        	pageBean.setItems(jsonFds);
					        	pageBean.setStart(page.getStart());
					        	pageBean.setLimit(page.getLimit());
					        	pageBean.setTotal(page.getTotal());
					        	pageBean.setList(jsonFds);
								result.setResult(pageBean);
								result.setSuccess(MessageInfo.GETSUCCESS.isResult());
								result.setMsg(MessageInfo.GETSUCCESS.getMsg());
					        	session.setAttribute("voucherPangeInfo", page);
							}
						}else{
							result.setCode(dubboObjectServiceResultInfo.getResult());
						    result.setSuccess(dubboObjectServiceResultInfo.isSucess());
						    result.setMsg(dubboObjectServiceResultInfo.getMsg());
						}
					}
				}else{
					result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboTemplateServiceResultInfo.getExceptionMsg()+"】");
				}
				
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboTempTypeServiceResultInfo.getExceptionMsg()+"】");
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
	 * 生成凭证
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/createVouchar/",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult createVouchar(@RequestBody Map<String,Object> map,HttpServletRequest req){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
//			HttpServletRequest  req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			HttpSession session = req.getSession();
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			PageBean<FinaData> p = (PageBean<FinaData>) session.getAttribute("voucherPangeInfo");
			p.setList(p.getItems());
			String flag = map.get("flag")+"";
			String btype = map.get("btype")+"";
			String billtypes = map.get("billtypes")+"";
			// 单据id
			List ids = (List)map.get("ids");
			try {
				billtypes = URLDecoder.decode(billtypes, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//查询账套公司
		    // 查询FiAccountSet实体
			String accountSetDubboResultInfo = accountSetDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+map.get("accountSetId")+""+"\"}");
			DubboServiceResultInfo accountSetDubboServiceResultInfo= JacksonUtils.fromJson(accountSetDubboResultInfo, DubboServiceResultInfo.class);
			AccountSetDto accountSetDto = new AccountSetDto();
			if(accountSetDubboServiceResultInfo.isSucess()){
				String fiAccountSet= accountSetDubboServiceResultInfo.getResult();
				accountSetDto=JacksonUtils.fromJson(fiAccountSet, AccountSetDto.class);
				String accountSetId = map.get("accountSetId")+"";//账套
				String companyId = map.get("companyId")+"";
				String accountSetCode = accountSetDto.getCode();//账套
				String typeId = map.get("typeId")+"";//单据类型
				finaQueryParams.setBillType(typeId);
				String appCode = accountSetDto.getAppCode();
				
				//查询凭证模板
				Map<String,String> templateMap = new HashMap<String,String>();
				templateMap.put("typeId", typeId);
				String paramaterTemplateJson = JacksonUtils.toJson(templateMap);
				String dubboTemplateResultInfo=voucherTemplateDtoServiceCustomer.queryList(userJson, paramaterTemplateJson);
				DubboServiceResultInfo dubboTemplateServiceResultInfo= JacksonUtils.fromJson(dubboTemplateResultInfo, DubboServiceResultInfo.class);
				if(dubboTemplateServiceResultInfo.isSucess()){
					String resultTemplateInfo= dubboTemplateServiceResultInfo.getResult();
					List<VoucherTemplateDto> templateList=JacksonUtils.fromJson(resultTemplateInfo, ArrayList.class,VoucherTemplateDto.class);
					//查询业务对象
					if(templateList !=null && templateList.size() > 0){
						String dubboObjectResultInfo=finanaceBusinessObjectDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+templateList.get(0).getBizObjectId()+"\"}");
						DubboServiceResultInfo dubboObjectServiceResultInfo= JacksonUtils.fromJson(dubboObjectResultInfo, DubboServiceResultInfo.class);
						if(dubboObjectServiceResultInfo.isSucess()){
							String resultObjectInfo= dubboObjectServiceResultInfo.getResult();
							BusinessObjectDto businessObjectDto=JacksonUtils.fromJson(resultObjectInfo, BusinessObjectDto.class);
							if(businessObjectDto != null){
								btype = businessObjectDto.getName();
							}
						}
					}
				}
				
				Map<String,String> idMapName = new HashMap<String,String>();
				/*List<FinancePaymentTypeDTO> fds = mDFinanceCacheService.getAllFinancePaymentListType().getResult();
				for(FinancePaymentTypeDTO dto:fds){
					idMapName.put(dto.getId()+"code", dto.getFullCode());
					idMapName.put(dto.getId()+"", dto.getName());
				}*/
				String dubboResultInfo=payTypeDtoServiceCustomer.queryList(userJson, "");
				DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			    if(dubboServiceResultInfo.isSucess()){
					String resultInfo= dubboServiceResultInfo.getResult();
					List<PayTypeDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,PayTypeDto.class);
					for(PayTypeDto dto:list){
						idMapName.put(dto.getId()+"code", dto.getCode());
						idMapName.put(dto.getId()+"", dto.getName());
					}
			    }else{
			    	result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			    }

				
				List<FinaData> selectedList = new ArrayList<FinaData>();
				if (p.getList().size() > 0) {
					for (FinaData f : p.getList()) {
						String fId = String.valueOf(f.get("id"));
						
						if (String.valueOf(f.get("type")).equals("单据")) {
							if (ids.toString().contains(fId))
								if (!selectedList.contains(f)) {
									if(appCode.equals("CO")){
										if(f.get("funds_Type_Id") != null && StringUtils.isNotBlank(f.get("funds_Type_Id").toString())){
											String fundtypeid = f.get("funds_Type_Id").toString();
											f.put("funds_Type_Id", idMapName.get(fundtypeid));
											f.put("funds_Type_Code", idMapName.get(fundtypeid+"code"));
										}
										if(f.get("funds_Name_Id") != null && StringUtils.isNotBlank(f.get("funds_Name_Id").toString())){
											f.put("funds_Name_Id", idMapName.get(f.get("funds_Name_Id").toString()));
										}
									}
									if(appCode.equals("CO")){
										if(btype.equals("工程投入")){
											if(StringUtils.isNotBlank(String.valueOf(f.get("billType"))) && billtypes.contains(String.format(",%s,", String.valueOf(f.get("billType"))))){
												selectedList.add(f);
											}
										}else{
											selectedList.add(f);
										}
									}else{
										selectedList.add(f);
									}
								}
					
						 }
					}

					List<FinaData> fdList = null;
					List<FinaData> czList = null;
					List<FinaData> hkList = null;
					List<FinaData> fpList = null;
					for (FinaData fd : selectedList) {
						String id = String.valueOf(fd.get("id"));
						String billType = "" ;
						if(appCode.equals("CO")){
							billType = String.valueOf(fd.get("billType"));
						}
						fdList = new ArrayList<FinaData>();
						czList = new ArrayList<FinaData>();
						hkList = new ArrayList<FinaData>();
						fpList = new ArrayList<FinaData>();
						for (FinaData f : p.getList()) {
							if (String.valueOf(f.get("type")).equals("科目分摊")) {
								// 成本主单据的ID 是 id 费用主单据的ID sourceid
								String sourceId = "";
								String billTypeSub = "" ;
								if(appCode.equals("CO")){
									billTypeSub = String.valueOf(f.get("billType"));
									sourceId = String.valueOf(f.get("id"));
									if(f.get("funds_Type_Id") != null && StringUtils.isNotBlank(f.get("funds_Type_Id").toString())){
										String fundtypeid = f.get("funds_Type_Id").toString();
										f.put("funds_Type_Id", idMapName.get(fundtypeid));
										f.put("funds_Type_Code", idMapName.get(fundtypeid+"code"));
									}
									if(f.get("funds_Name_Id") != null && StringUtils.isNotBlank(f.get("funds_Name_Id").toString())){
										f.put("funds_Name_Id", idMapName.get(f.get("funds_Name_Id").toString()));
									}
								}else{
									sourceId = String.valueOf(f.get("sourceid"));
								}
								if (id.equals(sourceId))
									if(appCode.equals("CO") && btype.equals("工程投入")){
										if(billType.equals(billTypeSub)){
											fdList.add(f);
										}
									}else{
										fdList.add(f);
									}
							}

							if (String.valueOf(f.get("type")).equals("冲账明细")) {
								if (id.equals(String.valueOf(f.get("conpaybillid"))))
									czList.add(f);
							}
							
							if (String.valueOf(f.get("type")).equals("手工还款")) {
								if (id.equals(String.valueOf(f.get("sourceid"))))
									hkList.add(f);
							}
							
							if (String.valueOf(f.get("type")).equals("发票信息")) {
								if (id.equals(String.valueOf(f.get("sourceid"))))
									fpList.add(f);
							}
						}

						fd.put("ft", fdList);
						fd.put("cz", czList);
						fd.put("hk", hkList);
						fd.put("fp", fpList);
					}

				}
				
				//随机数
				int random = (int)(1+Math.random()*100);
				if(flag.equals("2")){
					for (int i = 0; i < selectedList.size(); i++) {
						selectedList.get(i).put("mergeNum", random+"");
					}
				}

				QMap paramMap = new QMap("accountSetId", accountSetId);
				paramMap.add("btype", btype);
				paramMap.add("loginInfo", userJson);
				paramMap.add("appCode",appCode);
				paramMap.add("accountSetCode",accountSetCode);
				FlowResult<String> flowResult = voucherBillDtoServiceCustomer.createVoucher(userJson,selectedList, finaQueryParams, paramMap,companyId);
				if (flowResult.isSuccess()) {
					session.removeAttribute("pageBeanVoucher");
					result.setSuccess(true);
					result.setMsg("操作成功");
				} else {
					result.setSuccess(false);
					LinkedList<String> errDescList = flowResult.getDebugInfo().getErrDesc();
					String errdesc = "";
					for(String str : errDescList){
						errdesc += str+",";
					}
					result.setMsg(errdesc);
				}
			}else{
				result.setSuccess(false);
				result.setMsg(MessageInfo.CREATERROR.getMsg()+"【"+accountSetDubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用createVouchar方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(false);
			result.setMsg(MessageInfo.CREATERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	/**
	 * 不生成凭证
	 */
	@RequestMapping(value="/noCreateVoucher/",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult noCreateVoucher(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String btype = map.get("billtypes")+"";
			// 单据id
			List ids = (List)map.get("ids");
			btype = URLDecoder.decode(btype, "utf-8");
			//查询账套公司
		    // 查询FiAccountSet实体
			String accountSetDubboResultInfo = accountSetDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+map.get("accountSetId")+""+"\"}");
			DubboServiceResultInfo accountSetDubboServiceResultInfo= JacksonUtils.fromJson(accountSetDubboResultInfo, DubboServiceResultInfo.class);
			AccountSetDto accountSetDto = new AccountSetDto();
			if(accountSetDubboServiceResultInfo.isSucess()){
				String appCode = accountSetDto.getAppCode();
				//查询凭证模板业务类型
				String dubboTempTypeResultInfo=voucherTemplateTypeDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+map.get("billType")+"\"}");
				DubboServiceResultInfo dubboTempTypeServiceResultInfo= JacksonUtils.fromJson(dubboTempTypeResultInfo, DubboServiceResultInfo.class);
				if(dubboTempTypeServiceResultInfo.isSucess()){
					String tempTypeInfo= dubboTempTypeServiceResultInfo.getResult();
					VoucherTemplateTypeDto voucherTemplateType=JacksonUtils.fromJson(tempTypeInfo, VoucherTemplateTypeDto.class);
					//查询凭证模板
					Map<String,String> templateMap = new HashMap<String,String>();
					templateMap.put("typeId", voucherTemplateType.getId());
					String paramaterTemplateJson = JacksonUtils.toJson(templateMap);
					String dubboTemplateResultInfo=voucherTemplateDtoServiceCustomer.queryList(userJson, paramaterTemplateJson);
					DubboServiceResultInfo dubboTemplateServiceResultInfo= JacksonUtils.fromJson(dubboTemplateResultInfo, DubboServiceResultInfo.class);
					if(dubboTemplateServiceResultInfo.isSucess()){
						String resultTemplateInfo= dubboTemplateServiceResultInfo.getResult();
						List<VoucherTemplateDto> templateList=JacksonUtils.fromJson(resultTemplateInfo, ArrayList.class,VoucherTemplateDto.class);
						//查询业务对象注册字段
						if(templateList !=null && templateList.size() > 0){
							FlowResult<String> flowResult = voucherBillDtoServiceCustomer.rewrite(ids,templateList.get(0),appCode);
						}
					}
				}
				
			}else{
				result.setSuccess(false);
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+accountSetDubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用deletePseudoBatch方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(false);
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}

	
	/**
	 * 返回分页对象  凭证列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/getVoucherBillPage",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getVoucherBillPage(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		try {
			String dubboResultInfo=voucherBillDtoServiceCustomer.getVoucherBillPage(userJson, paramaterJson);
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
	 * 输出凭证
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/outputVoucher/{ids}",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult outputVoucher(@PathVariable("ids")  String ids,HttpServletRequest request){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			int flag = 2;
			String[] vhids = ids.split(",");
			String xmlFile = "";
			// NC系统返回的xml字符串
			String res = null;
			try {
				for (int i = 0; i < vhids.length; i++) {
					String voucherId = vhids[i];
					
					String voucherInfo = voucherBillDtoServiceCustomer.getVoucherById(userJson, "{\"id\":\""+voucherId+"\"}");
					DubboServiceResultInfo voucherResultInfo= JacksonUtils.fromJson(voucherInfo, DubboServiceResultInfo.class);
					if(voucherResultInfo.isSucess()){
						String voucher= voucherResultInfo.getResult();
						VoucherData fv=JacksonUtils.fromJson(voucher, VoucherData.class);
						List<VoucherBillEntryDto> entyDatas = fv.getEntryBillList();
						VoucherBillDto fvd = fv.getVoucherBillDto();
	
						// 从voucherData中获得accountSetId
						String accountSetId = fvd.getAccountSetId();
						// 查询FiAccountSet实体
						String accountSetDubboResultInfo = accountSetDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+accountSetId+"\"}");
						DubboServiceResultInfo accountSetDubboServiceResultInfo= JacksonUtils.fromJson(accountSetDubboResultInfo, DubboServiceResultInfo.class);
						if(accountSetDubboServiceResultInfo.isSucess()){
							String fiAccountSet= accountSetDubboServiceResultInfo.getResult();
							AccountSetDto accountSetDto=JacksonUtils.fromJson(fiAccountSet, AccountSetDto.class);
							String companyCode = accountSetDto.getCompanyCode();
							//查询SysRegisterDto 财务系统注册实体
							String sysdubboResultInfo=sysRegisterDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+accountSetDto.getRegisterId()+"\"}");
							DubboServiceResultInfo sysdubboServiceResultInfo= JacksonUtils.fromJson(sysdubboResultInfo, DubboServiceResultInfo.class);
							if(sysdubboServiceResultInfo.isSucess()){
								String sysresultInfo= sysdubboServiceResultInfo.getResult();
								SysRegisterDto sysRegisterDto=JacksonUtils.fromJson(sysresultInfo, SysRegisterDto.class);
								xmlFile = voucherBillDtoServiceCustomer.createSyncXml2NC(JacksonUtils.toJson(fvd),entyDatas,accountSetDto.getAppCode(),sysRegisterDto.getSender());
								// NC接口相关
								String webUrl = sysRegisterDto.getWebUrl();
								// 获得财务系统url
								String url = webUrl + "?account=" + accountSetDto.getCode()+ "&receiver=" + companyCode;
								// xml推送到url
								//System.out.println(url);
								if (xmlFile != null && !xmlFile.trim().equals("")
										&& !url.trim().equals("") && url != null) {
									res = NCSendData.getPostResponse(url, xmlFile);
								}
								// 获得返回xml 如果成功 设置输出标志 和输出日期
								if (null != res) {
									if (NCXMlParse.XmlErrorCode(res) >= 0) {
										String content = NCXMlParse.XmlContent(res);
										String[] contents = content.split("-");
										String voucherNo = contents[contents.length - 1];
										fvd.setVoucherNo(voucherNo);
										fvd.setSendStatus("1");// 输出成功
										SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
										fvd.setExportDate(sf.format(new Date()));
										fvd.setErrorCause("");
										flag = 1;
	
										// 调用回写接口
										// rewrite(voucherDates,result);
	
									} else {
										String errorinfo = NCXMlParse.XmlErrorInfo(res);
										Integer errCode = NCXMlParse.XmlErrorCode(res);
										String content = NCXMlParse.XmlContent(res);
										fvd.setSendStatus("2");// 输出失败
										String error = "错误代码：" + errCode.toString() + " 错误内容："
												+ errorinfo;
										fvd.setErrorCause(error);
										flag = 2;
									}
										saveFile(res,voucherId,"novoucher",request);
								} else {
									flag = 0;
									fvd.setSendStatus("0");// 未输出
								}
								String updateJson = JacksonUtils.toJson(fvd);
								voucherBillDtoServiceCustomer.update(userJson, updateJson);
							}
						}
					}
					saveFile(xmlFile,voucherId,"voucher",request);
				}
			} catch (Exception e) {
				flag = 2;
				log.error("", e);
				e.printStackTrace();

			}

			if (flag == 1) {
				result.setSuccess(true);
				result.setCode("1");
				result.setMsg("操作成功");
			} else if (flag == 0) {
				result.setSuccess(true);
				result.setCode("0");
				result.setMsg("未输出");
			} else {
				result.setSuccess(false);
				result.setMsg("输出失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用outputVoucher方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	public void saveFile(String xmlFile,String voucherId,String voucher,HttpServletRequest request) {

		FileWriter fw = null;
		String separator = File.separator;
		File f = new File(request.getContextPath()+separator+voucherId+separator+voucher+".xml");
//		File f = new File("c:/ncvoucher.xml");
		try {
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			fw = new FileWriter(f);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(xmlFile);
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/downLoad")
	public void downLoad(HttpServletRequest request,HttpServletResponse response){
		String separator = File.separator;
		File file = new File(request.getContextPath()+separator+request.getParameter("voucherid")+separator+request.getParameter("filename")+".xml");
		response.setContentType("multipart/form-data");  
        //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)  
        response.setHeader("Content-Disposition", "attachment;fileName="+request.getParameter("filename")+".xml");  
        ServletOutputStream out2;  
        FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
			 //3.通过response获取ServletOutputStream对象(out)  
			out2 = response.getOutputStream();
	        int b = 0;  
	        byte[] buffer = new byte[512];  
	        while (b != -1){  
	            b = inputStream.read(buffer);  
	            //4.写到输出流(out)中  
	            if(b != -1)
	            out2.write(buffer,0,b);  
	        }  
	        inputStream.close();  
	        out2.close();  
	        out2.flush();  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	/**
	 * 处理筛选条件
	 * 
	 * @param condition
	 * @return
	 */
	public String machCodition(String condition, List<AccountSetCompanyDto> fiSetDataList, String conpany,String appCode) {
		condition = condition.replace("==", "=");
		condition = condition.replace("&&", "and");
		condition = condition.replace("||", "or");
		condition = condition.replace("<>", "!=");
		if (condition.equals(" "))
			condition.trim();
		if(appCode.equals("EX")){
		 if(StringUtils.isNotBlank(condition) &&
		 StringUtils.isNotBlank(conpany)){
		 condition += " and corpname like'%"+conpany+"'";
		 }
		 if(StringUtils.isBlank(condition) &&
		 StringUtils.isNotBlank(conpany)){
		 condition += " corpname like'%"+conpany+"'";
		 }
		 if(StringUtils.isNotBlank(condition) && fiSetDataList != null){
			 String payUnitId = "";
			 for(AccountSetCompanyDto fd : fiSetDataList){
				 if(fd.getPaymentOrganId()!=null){
					 payUnitId += "'"+fd.getPaymentOrganId() + "'," ;
				 }
			 } 
			 if(StringUtils.isNotBlank(payUnitId)){
				 payUnitId = payUnitId.substring(0, payUnitId.lastIndexOf(","));
				 condition += " and payunitid in ("+payUnitId + ")";
			 }
		 }
		}else if(appCode.equals("CO")){
			List<FinancePaymentTypeDTO> fds = mDFinanceCacheService.getAllFinancePaymentListType().getResult();
			Map<String,String> nameMapId = new HashMap<String,String>();
			for(FinancePaymentTypeDTO data:fds){
				if(nameMapId.get(data.getName())!= null){
					nameMapId.put(data.getName(), nameMapId.get(data.getName()) + ",'" + data.getId() + "'");
				}else{
					nameMapId.put(data.getName(), "'" + data.getId() + "'");
				}
			}
			condition = replaceNameToId(condition,nameMapId);
		}
		return condition;
	}
	
	public String replaceNameToId(String condition,Map<String,String> nameMapId){
		Set<String> keys = nameMapId.keySet();
		
		for(String key : keys){
			String notkey = "!=\""+key+"\"";
			String iskey = "=\""+key+"\"";
			if(condition.contains(notkey)){
				condition = condition.replace(notkey, " not in ("+nameMapId.get(key)+")");
			}
			if(condition.contains(iskey)){
				condition = condition.replace(iskey, "  in ("+nameMapId.get(key)+")");
			}
		}
		
		return condition;
	}
}
