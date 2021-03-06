package com.xinleju.platform.sys.base.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.base.dto.CustomArchivesItemDto;
import com.xinleju.platform.sys.base.entity.CustomArchives;

/**
 * @author admin
 *
 */

public interface CustomArchivesDao extends BaseDao<String, CustomArchives> {

	/**
	  * @Description:查询树
	  * @author:zhangfangzhi
	  * @date 2017年3月4日 下午1:53:45
	  * @version V1.0
	 */
	List<CustomArchivesItemDto> getTree(Map<String, Object> map);

	/**
	  * @Description:查询档案明细是否存在
	  * @author:zhangfangzhi
	  * @date 2017年3月6日 下午1:48:39
	  * @version V1.0
	 */
	Integer queryItemsById(String mainId);

	/**
	  * @Description:查询排序
	  * @author:zhangfangzhi
	  * @date 2017年3月17日 上午9:24:56
	  * @version V1.0
	 */
	List<CustomArchives> queryListSort(Map<String, Object> map);

	/**
	  * @Description:校验编码是否重复
	  * @author:zhangfangzhi
	  * @date 2017年3月21日 上午10:58:47
	  * @version V1.0
	 */
	Integer isExistCode(String code);
	
	

}
