package com.xinleju.platform.sys.base.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.base.entity.CustomArchivesItem;

/**
 * @author admin
 *
 */

public interface CustomArchivesItemDao extends BaseDao<String, CustomArchivesItem> {

	/**
	  * @Description:查询最大序号
	  * @author:zhangfangzhi
	  * @date 2017年3月6日 下午3:09:36
	  * @version V1.0
	 */
	Integer getCurrentMaxSortByMainId(String mainId);

	/**
	  * @Description:排序查询
	  * @author:zhangfangzhi
	  * @date 2017年3月16日 下午4:40:24
	  * @version V1.0
	 */
	List queryListSort(Map<String, Object> map);

	/**
	  * @Description:名称编码重复校验
	  * @author:zhangfangzhi
	  * @date 2017年3月21日 下午2:45:20
	  * @version V1.0
	 */
	Integer validateIsExist(CustomArchivesItem customArchivesItem, String type);

	/**
	  * @Description:分页查询（排序）
	  * @author:zhangfangzhi
	  * @date 2017年4月2日 下午2:16:18
	  * @version V1.0
	 */
	List<Map<String, Object>> getPageSort(Map map);

	/**
	  * @Description:分页查询（统计数量）
	  * @author:zhangfangzhi
	  * @date 2017年4月2日 下午2:16:37
	  * @version V1.0
	 */
	Integer getPageSortCount(Map map);

	/**
	  * @Description:删除档案子项（伪删除）
	  * @author:zhangfangzhi
	  * @date 2017年5月2日 下午6:21:32
	  * @version V1.0
	 */
	int updateCustomArchives(String id);

}