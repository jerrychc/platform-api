package com.xinleju.platform.sys.base.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.BaseDataDictionaryItemDao;
import com.xinleju.platform.sys.base.dto.BaseDataDictionaryItemDto;
import com.xinleju.platform.sys.base.entity.BaseDataDictionaryItem;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class BaseDataDictionaryItemDaoImpl extends BaseDaoImpl<String,BaseDataDictionaryItem> implements BaseDataDictionaryItemDao{

	public BaseDataDictionaryItemDaoImpl() {
		super();
	}

	@Override
	public List<BaseDataDictionaryItemDto> getItemListByDictionaryId(String parameter) {
		// TODO Auto-generated method stub
		return  getSqlSession().selectList("getBaseDataDictionaryItemList", parameter);
	}

	
	
}
