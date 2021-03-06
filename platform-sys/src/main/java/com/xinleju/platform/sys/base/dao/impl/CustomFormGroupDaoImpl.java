package com.xinleju.platform.sys.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.CustomFormGroupDao;
import com.xinleju.platform.sys.base.dto.CustomFormDto;
import com.xinleju.platform.sys.base.dto.CustomFormNodeDto;
import com.xinleju.platform.sys.base.entity.CustomForm;
import com.xinleju.platform.sys.base.entity.CustomFormGroup;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class CustomFormGroupDaoImpl extends BaseDaoImpl<String,CustomFormGroup> implements CustomFormGroupDao{

	public CustomFormGroupDaoImpl() {
		super();
	}

	@Override
	public List<CustomFormDto> getTree(Map<String, Object> map) {
		return getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomForm.getTree",map);
	}

	@Override
	public Integer queryMaxSort() {
		return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomFormGroup.getMaxSort");
	}

	@Override
	public List<CustomFormNodeDto> queryCustomGroupSort(Map map) {
		return getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomFormGroup.queryCustomGroupSort",map);
	}

	@Override
	public List<CustomFormNodeDto> queryCustomSort() {
		return getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomForm.queryCustomSort");
	}

	@Override
	public Integer getCustomFormCountByGroupId(String id) {
		return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomFormGroup.getCustomFormCountByGroupId",id);
	}

	@Override
	public Integer validateIsExist(CustomFormGroup customFormGroup,String type) {
		if("code".equals(type)){
			return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomFormGroup.validateIsExistCode",customFormGroup);
		}else if("name".equals(type)){
			return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomFormGroup.validateIsExistName",customFormGroup);
		}
		return null;
	}

	@Override
	public Integer getCustomFormGroupCountByPID(String id) {
		return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomFormGroup.getCustomFormGroupCountByPID",id);
	}

	@Override
	public List queryListForQuickEntry(Map map) {
		return getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomFormGroup.queryListForQuickEntry",map);
	}

	@Override
	public void updateSort(CustomFormGroup customFormGroup) {
		getSqlSession().update("com.xinleju.platform.sys.base.entity.CustomFormGroup.updateSort", customFormGroup);
	}

	
	
}
