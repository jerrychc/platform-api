package com.xinleju.platform.sys.notice.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.notice.entity.MailMsg;

/**
 * @author admin
 *
 */

public interface MailMsgDao extends BaseDao<String, MailMsg> {
	

	/**
	 * 分页数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<MailMsg> getPageData(Map<String, Object> map);
	/**
	 * 总条数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer getPageDataCount(Map<String, Object> map);


}
