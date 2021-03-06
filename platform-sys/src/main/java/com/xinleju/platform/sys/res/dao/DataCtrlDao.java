package com.xinleju.platform.sys.res.dao;

import java.util.List;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.res.dto.DataItemDto;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.entity.DataCtrl;
import com.xinleju.platform.sys.res.entity.DataItem;

/**
 * @author admin
 *
 */

public interface DataCtrlDao extends BaseDao<String, DataCtrl> {
	/**
	 * 根据数据控制对象id获取所有数据对象控制项集合
	 * @param  paramater(数据控制对象id)
	 */
	List<DataItemDto> querylistDataItem(String id);
	
	

}
