package com.xinleju.platform.finance.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.finance.dao.VoucherTemplateDao;
import com.xinleju.platform.finance.entity.VoucherTemplate;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class VoucherTemplateDaoImpl extends BaseDaoImpl<String,VoucherTemplate> implements VoucherTemplateDao{

	public VoucherTemplateDaoImpl() {
		super();
	}

	@Override
	public List queryListByTypeIds(List<String> paramList){
		return getSqlSession().selectList("com.xinleju.platform.finance.entity.VoucherTemplate.queryListByTypeIds", paramList);
	}
	
}
