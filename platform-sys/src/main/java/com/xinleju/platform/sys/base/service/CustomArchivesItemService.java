package com.xinleju.platform.sys.base.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.entity.CustomArchivesItem;

/**
 * @author admin
 * 
 * 
 */

public interface CustomArchivesItemService extends  BaseService <String,CustomArchivesItem>{

	/**
	  * @Description:查询最大序号
	  * @author:zhangfangzhi
	  * @date 2017年3月6日 下午3:08:41
	  * @version V1.0
	 */
	Integer getCurrentMaxSortByMainId(String mainId);

	/**
	  * @Description:排序查询
	  * @author:zhangfangzhi
	  * @date 2017年3月16日 下午4:39:45
	  * @version V1.0
	 */
	List queryListSort(Map<String, Object> map);

	/**
	  * @Description:名称编码重复校验
	  * @author:zhangfangzhi
	  * @date 2017年3月21日 下午2:44:19
	  * @version V1.0
	 */
	Integer validateIsExist(CustomArchivesItem customArchivesItem, String type);

	/**
	  * @Description:档案子项列表批量保存
	  * @author:zhangfangzhi
	  * @date 2017年3月22日 下午2:02:58
	  * @version V1.0
	 */
	DubboServiceResultInfo saveList(String userInfo, String saveJson);

	/**
	  * @Description:保存并且校验
	  * @author:zhangfangzhi
	  * @date 2017年3月22日 下午6:50:50
	  * @version V1.0
	 */
	String validateBeforeSave(String userInfo, String saveJson);

	/**
	  * @Description:修改并且校验
	  * @author:zhangfangzhi
	  * @date 2017年3月22日 下午8:24:13
	  * @version V1.0
	 */
	String validateBeforeUpdate(String userInfo, String updateJson);

	/**
	  * @Description:分页查询（排序）
	  * @author:zhangfangzhi
	  * @date 2017年4月2日 下午2:15:15
	  * @version V1.0
	 */
	Page getPageSort(Map map);

	/**
	  * @Description:删除档案子项（伪删除）
	  * @author:zhangfangzhi
	  * @date 2017年5月2日 下午6:20:18
	  * @version V1.0
	 */
	int updateCustomArchives(String id);

	
}
