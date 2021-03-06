package com.xinleju.platform.out.app.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.sys.base.dto.BaseSupplierDto;
import com.xinleju.platform.sys.base.service.BaseProjectTypeService;
import com.xinleju.platform.sys.base.service.BaseSupplierService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.tools.data.JacksonUtils;

public class BaseOutServiceProducer implements BaseOutServiceCustomer {
	private static Logger log = Logger.getLogger(BaseOutServiceProducer.class);
	
	@Autowired
	private BaseProjectTypeService baseProjectTypeService ;
	@Autowired
	private BaseSupplierService baseSupplierService ;
	/**
	 * 获取所有产品类型
	 * @param userJson
	 * @param paramJson:{}
	 * @return
	 */
	@Override
	public String getAllProductType(String userJson, String paramJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			List<Map<String, Object>> res=baseProjectTypeService.getAllProductType();
			info.setResult(JacksonUtils.toJson(res));
			info.setSucess(true);
			info.setMsg("获取所有产品类型成功!");
		} catch (Exception e) {
			log.error("获取所有产品类型失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取所有产品类型失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 根据公司获取供方档案
	 * @param userJson
	 * @param paramJson:{companyId:"公司id"}
	 * @return
	 */
	@Override
	public String getSupplierByCompanyId(String userJson, String paramJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			if( param.get("companyId")==null || StringUtils.isBlank(param.get("companyId").toString())){
				throw new InvalidCustomException("公司Id不可为空");
			}
			List<Map<String, Object>> res=baseSupplierService.getSupplierByCompanyId(param);
			info.setResult(JacksonUtils.toJson(res));
			info.setSucess(true);
			info.setMsg("获取供方档案成功!");
		} catch (Exception e) {
			log.error("获取供方档案失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取供方档案失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 根据主键获取供方档案
	 * @param userJson
	 * @param paramJson:{companyId:"公司id"}
	 * @return
	 */
	@Override
	public String getSupplierById(String userJson, String paramJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			if( param.get("id")==null || StringUtils.isBlank(param.get("id").toString())){
				throw new InvalidCustomException("Id不可为空");
			}
			BaseSupplierDto res=baseSupplierService.getSupplierAndAccontById(param.get("id").toString());
			info.setResult(JacksonUtils.toJson(res));
			info.setSucess(true);
			info.setMsg("获取供方档案成功!");
		} catch (Exception e) {
			log.error("获取供方档案失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取供方档案失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
}
