package com.xinleju.platform.out.app.old.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.xinleju.erp.data.cache.api.MDFinanceCacheService;
import com.xinleju.erp.data.cache.dto.BankAccountDTO;
import com.xinleju.erp.data.cache.dto.FinanceLegalPersonDTO;
import com.xinleju.erp.data.cache.dto.FinancePaymentTypeDTO;
import com.xinleju.erp.data.cache.dto.FinanceSettlementTypeDTO;
import com.xinleju.erp.data.cache.dto.ProjectBaseDTO;
import com.xinleju.erp.flow.flowutils.bean.FlowResult;

public class MDFinanceCacheServiceImpl implements MDFinanceCacheService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public FlowResult<FinanceLegalPersonDTO> getFinanceLegalPersonById(
			String financeLegalPersonId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<List<BankAccountDTO>> getBankAccountByFinanceLegalPersonId(
			String ownerId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取所有法人
	 * @return
	 */
	@Override
	public FlowResult<List<FinanceLegalPersonDTO>> getFinanceLegalPersonAll() {
		FlowResult<List<FinanceLegalPersonDTO>> result=new FlowResult<>();
		String sql=" select t.id,t.name,t.code,t.`status`,tt.`code` provinceCode,tt.`name` provinceName,ttt.`code` cityCode, "+
				" ttt.`name` cityName ,t.delflag isDelete ,t.remark statement from pt_sys_base_corporation t  "+
				" LEFT JOIN pt_sys_base_region tt on t.province_id = tt.id  "+
				" LEFT JOIN pt_sys_base_region ttt on t.city_id = ttt.id where t.delflag = '0' and t.`status` = '1'";
		RowMapper<FinanceLegalPersonDTO> rowMapper=new BeanPropertyRowMapper<FinanceLegalPersonDTO>(FinanceLegalPersonDTO.class);
		List<FinanceLegalPersonDTO> list= jdbcTemplate.query(sql,rowMapper);
		result.setResult(list);
		return result;
	}

	/**
	 * 根据公司获取法人
	 * @param companyId
	 * @return
	 */
	@Override
	public FlowResult<List<FinanceLegalPersonDTO>> getFinanceLegalPersonByCompanyId(
			String companyId) {
		FlowResult<List<FinanceLegalPersonDTO>> result=new FlowResult<>();
		String sql=" select t.id,t.name,t.code,t.`status`,tt.`code` provinceCode,tt.`name` provinceName,ttt.`code` cityCode, "+
				" ttt.`name` cityName ,t.delflag isDelete ,t.remark statement from pt_sys_base_corporation t  "+
				" LEFT JOIN pt_sys_base_region tt on t.province_id = tt.id  "+
				" LEFT JOIN pt_sys_base_region ttt on t.city_id = ttt.id "+
				" where  (t.company_id ='"+companyId+"' or ifnull(t.company_id,'')='' ) and t.delflag = '0' and t.`status` = '1'";
		RowMapper<FinanceLegalPersonDTO> rowMapper=new BeanPropertyRowMapper<FinanceLegalPersonDTO>(FinanceLegalPersonDTO.class);
		List<FinanceLegalPersonDTO> list= jdbcTemplate.query(sql,rowMapper);
		result.setResult(list);
		return result;
	}
	/**
	 * 根据父ID查询付款类型
	 * @param id
	 * @return
	 */
	@Override
	public FlowResult<List<FinancePaymentTypeDTO>> getFinancePaymentTypeByParentId(
			String financePaymentTypeId) {
		FlowResult<List<FinancePaymentTypeDTO>> result=new FlowResult<>();
		String sql=" select id,name,code,status,remark as statement,parent_id as parentId,delflag as isDelete from pt_sys_pay_type WHERE parent_id=?";
		RowMapper<FinancePaymentTypeDTO> rowMapper=new BeanPropertyRowMapper<FinancePaymentTypeDTO>(FinancePaymentTypeDTO.class);
		List<FinancePaymentTypeDTO> list= jdbcTemplate.query(sql,rowMapper,financePaymentTypeId);
		result.setResult(list);
		return result;
	}
	/**
	 * 根据ID查询付款类型
	 * @param id
	 * @return
	 */
	@Override
	public FlowResult<FinancePaymentTypeDTO> getFinancePaymentTypeById(
			String financePaymentTypeId) {
		FlowResult<FinancePaymentTypeDTO> result=new FlowResult<>();
		String sql=" select id,name,code,status,remark as statement,parent_id as parentId,delflag as isDelete from pt_sys_pay_type WHERE id=?";
		RowMapper<FinancePaymentTypeDTO> rowMapper=new BeanPropertyRowMapper<FinancePaymentTypeDTO>(FinancePaymentTypeDTO.class);
		List<FinancePaymentTypeDTO> list= jdbcTemplate.query(sql,rowMapper,financePaymentTypeId);
		if(null!= list && list.size()>0){
			result.setResult(list.get(0));
		}else{
			result.setResult(null);
		}
		
		return result;
	}

	/**
	 * 获取全部付款类型
	 * @return
	 */
	@Override
	public FlowResult<List<FinancePaymentTypeDTO>> getAllFinancePaymentListType() {
		FlowResult<List<FinancePaymentTypeDTO>> result=new FlowResult<>();
		String sql=" select id,name,code,status,remark as statement,parent_id as parentId,delflag as isDelete from pt_sys_pay_type  ";
		RowMapper<FinancePaymentTypeDTO> rowMapper=new BeanPropertyRowMapper<FinancePaymentTypeDTO>(FinancePaymentTypeDTO.class);
		List<FinancePaymentTypeDTO> list= jdbcTemplate.query(sql,rowMapper);
		result.setResult(list);
		return result;
	}

	@Override
	public FlowResult<Map<FinancePaymentTypeDTO, List<FinancePaymentTypeDTO>>> getAllFinancePaymentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<FinanceSettlementTypeDTO> getFinanceSettlementTypeById(
			String financeSettlementTypeId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取所有结算类型
	 * @return
	 */
	@Override
	public FlowResult<List<FinanceSettlementTypeDTO>> getAllFinanceSettlementType() {
		FlowResult<List<FinanceSettlementTypeDTO>> result=new FlowResult<>();
		String sql=" select id,name,code,status, remark as statement,delflag as isDelete from pt_sys_settlement_trades ";
		RowMapper<FinanceSettlementTypeDTO> rowMapper=new BeanPropertyRowMapper<FinanceSettlementTypeDTO>(FinanceSettlementTypeDTO.class);
		List<FinanceSettlementTypeDTO> list= jdbcTemplate.query(sql,rowMapper);
		result.setResult(list);
		return result;
	}

}
