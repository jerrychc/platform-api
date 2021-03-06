package com.xinleju.platform.out.app.old.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.xinleju.erp.data.cache.api.MDBusinessCacheService;
import com.xinleju.erp.data.cache.dto.BankAccountDTO;
import com.xinleju.erp.data.cache.dto.BusinessAreaDTO;
import com.xinleju.erp.data.cache.dto.BusinessDateSetDTO;
import com.xinleju.erp.data.cache.dto.BusinessHolidayDTO;
import com.xinleju.erp.data.cache.dto.BusinessSetDTO;
import com.xinleju.erp.flow.flowutils.bean.FlowResult;

public class MDBusinessCacheServiceImpl implements MDBusinessCacheService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public FlowResult<List<BusinessAreaDTO>> getAllBusinessArea() {
		FlowResult<List<BusinessAreaDTO>> result=new FlowResult<>();
		String sql=" select id,name,code,parent_id parentId,delflag isDelete from pt_sys_base_region  ";
		RowMapper<BusinessAreaDTO> rowMapper=new BeanPropertyRowMapper<BusinessAreaDTO>(BusinessAreaDTO.class);
		List<BusinessAreaDTO> list= jdbcTemplate.query(sql,rowMapper);
		result.setResult(list);
		return result;
	}

	@Override
	public FlowResult<BusinessAreaDTO> getBusinessAreaById(String businessAreaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<List<BusinessAreaDTO>> getBusinessAreaByParentId(
			String businessAreaParentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<List<BusinessDateSetDTO>> getAllBusinessDateSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<BusinessDateSetDTO> getBusinessDateSetById(
			String businessDateSetId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<List<BusinessHolidayDTO>> getAllBusinessHoliday() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<Boolean> isBusinessHoliday(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<BusinessHolidayDTO> getBusinessHolidayById(
			String businessHolidayId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<List<BusinessSetDTO>> getAllBusinessSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<BusinessSetDTO> getBusinessSetById(String businessSetId) {
		// TODO Auto-generated method stub
		return null;
	}


}
