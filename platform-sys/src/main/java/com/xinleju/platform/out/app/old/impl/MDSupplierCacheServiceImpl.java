package com.xinleju.platform.out.app.old.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.xinleju.erp.data.cache.api.MDSupplierCacheService;
import com.xinleju.erp.data.cache.dto.BankAccountDTO;
import com.xinleju.erp.data.cache.dto.FinanceSettlementTypeDTO;
import com.xinleju.erp.data.cache.dto.SupplierCategoryDTO;
import com.xinleju.erp.data.cache.dto.SupplierDegreeDTO;
import com.xinleju.erp.data.cache.dto.SupplierInfoDTO;
import com.xinleju.erp.flow.flowutils.bean.FlowResult;
import com.xinleju.erp.flow.flowutils.bean.PageBean;

public class MDSupplierCacheServiceImpl implements MDSupplierCacheService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public FlowResult<List<BankAccountDTO>> getSupplierAccountByOwnerId(
			String ownerId) {
		FlowResult<List<BankAccountDTO>> result=new FlowResult<>();
		String sql=" select t.id,t.bank_name bankName,t.bank_code account,tt.`code` proCode,tt.`name` province,ttt.`code` cityCode, "+
				" ttt.`name` city ,t.address,t.supplier_id ownerId,t.delflag isDelete,t.create_date createTime from pt_sys_base_supplier_accont t  "+
				" LEFT JOIN pt_sys_base_region tt on t.province_id = tt.id  "+
				" LEFT JOIN pt_sys_base_region ttt on t.city_id = ttt.id "+
				" where t.supplier_id = '"+ownerId+"' and IFNULL(t.delflag,0)=0";
		RowMapper<BankAccountDTO> rowMapper=new BeanPropertyRowMapper<BankAccountDTO>(BankAccountDTO.class);
		List<BankAccountDTO> list= jdbcTemplate.query(sql,rowMapper);
		result.setResult(list);
		return result;
	}

	@Override
	public FlowResult<BankAccountDTO> getSupplierAccountById(
			String supplierAccountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<List<SupplierCategoryDTO>> getAllSupplierCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<List<SupplierCategoryDTO>> getSupplierCategoryByParentId(
			String supplierCategoryParentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<SupplierCategoryDTO> getSupplierCategoryById(
			String supplierCategoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<List<SupplierDegreeDTO>> getAllSupplierDegree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<SupplierDegreeDTO> getSupplierDegreeById(
			String supplierDegreeId) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
     * 根据Id查询供方基本信息
     * @param id
     * @return
     */
	@Override
	public FlowResult<SupplierInfoDTO> getSupplierInfoById(String supplierInfoId) {
		FlowResult<SupplierInfoDTO> result=new FlowResult<>();
		String sql=" select t.id,t.name,t.code,t.STATUS,t.company_id companyIds,t.province_id province,t.city_id city,t.representative legalName "+
					" ,t.address address,t.work_phone phoneNum,t.registration_date registerDate,t.license registerNum" +
					" ,phone contactNum,t.relation_person contactName,t.remark statement from pt_sys_base_supplier t where t.id = '"+supplierInfoId+"' ";
		RowMapper<SupplierInfoDTO> rowMapper=new BeanPropertyRowMapper<SupplierInfoDTO>(SupplierInfoDTO.class);
		List<SupplierInfoDTO> list= jdbcTemplate.query(sql,rowMapper);
		result.setSuccess(true);
		if(null!= list && list.size()>0){
			result.setResult(list.get(0));
		}else{
			result.setResult(null);
		}
		
		return result;
	}
	/**
     * 根据公司Id查询供方基本信息
     * @param id
     * @return
     */
	@Override
	public FlowResult<PageBean<SupplierInfoDTO>> getSupplierInfoByCompanyId(
			String companyId, Integer start, Integer limit) {
		FlowResult<PageBean<SupplierInfoDTO>> result=new FlowResult<>();
		String sql=" select DISTINCT t.id,t.name,t.code,t.STATUS,t.company_id companyIds,t.province_id province,t.city_id city,t.representative legalName "+
					" ,t.address address,t.work_phone phoneNum,t.registration_date registerDate,t.license registerNum" +
					" ,phone contactNum,t.relation_person contactName,t.remark statement from pt_sys_base_supplier t left join pt_sys_base_supplier_company tt "+
					" on tt.supper_id = t.id where t.delflag = '0' and t.`status`='1'  and t.company_id ='"+companyId+"' ";
		String sqlCount=" select count(*) from pt_sys_base_supplier t  left join pt_sys_base_supplier_company tt "+
				" on tt.supper_id = t.id where t.delflag = '0' and t.`status`='1'  and t.company_id ='"+companyId+"' ";
		
		if(null != start){
			String sqlLimit = " LIMIT "+start+","+limit+"  ";
			sql = sql + sqlLimit;
		}
		
		//查询总数
		int total= jdbcTemplate.queryForObject(sqlCount, Integer.class);
		
		RowMapper<SupplierInfoDTO> rowMapper=new BeanPropertyRowMapper<SupplierInfoDTO>(SupplierInfoDTO.class);
		List<SupplierInfoDTO> list= jdbcTemplate.query(sql,rowMapper);
		PageBean pb = new PageBean(start,limit,total,list);
		result.setSuccess(true);
		result.setResult(pb);
		return result;
	}

	@Override
	public FlowResult<PageBean<SupplierInfoDTO>> getSupplierInfoSearchName(
			String companyId, String supplierInfoName, Integer start,
			Integer limit) {
		FlowResult<PageBean<SupplierInfoDTO>> result=new FlowResult<>();
		String sql=" select DISTINCT t.id,t.name,t.code,t.STATUS,t.company_id companyIds,t.province_id province,t.city_id city,t.representative legalName "+
				" ,t.address address,t.work_phone phoneNum,t.registration_date registerDate,t.license registerNum" +
				" ,phone contactNum,t.relation_person contactName,t.remark statement from pt_sys_base_supplier t left join pt_sys_base_supplier_company tt "+
				" on tt.supper_id = t.id where t.delflag = '0' and t.`status`='1'  and t.company_id ='"+companyId+"' ";
	    String sqlCount=" select count(*) from pt_sys_base_supplier t  left join pt_sys_base_supplier_company tt "+
			" on tt.supper_id = t.id where t.delflag = '0' and t.`status`='1'  and t.company_id ='"+companyId+"' ";
	   //添加过滤条件
		if(StringUtils.isNotBlank(supplierInfoName)){
			   sqlCount+=" and t.name like '%"+supplierInfoName+"%'";
			   sql+=" and t.name like '%"+supplierInfoName+"%'";
		   }
		if(null != start){
			String sqlLimit = " LIMIT "+start+","+limit+"  ";
			sql = sql + sqlLimit;
		}
		
		//查询总数
		int total= jdbcTemplate.queryForObject(sqlCount, Integer.class);
		
		RowMapper<SupplierInfoDTO> rowMapper=new BeanPropertyRowMapper<SupplierInfoDTO>(SupplierInfoDTO.class);
		List<SupplierInfoDTO> list= jdbcTemplate.query(sql,rowMapper);
		PageBean pb = new PageBean(start,limit,total,list);
		result.setSuccess(true);
		result.setResult(pb);
		return result;
	}

}
