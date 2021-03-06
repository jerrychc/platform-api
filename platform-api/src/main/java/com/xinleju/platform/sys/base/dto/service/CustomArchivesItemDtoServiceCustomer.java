package com.xinleju.platform.sys.base.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;
import com.xinleju.platform.base.utils.MessageResult;

public interface CustomArchivesItemDtoServiceCustomer extends BaseDtoServiceCustomer{
	
	/**
	  * @Description:查询最大序号
	  * @author:zhangfangzhi
	  * @date 2017年3月6日 下午3:07:40
	  * @version V1.0
	 */
	Integer getCurrentMaxSortByMainId(String mainId);

	/**
	  * @Description:批量保存
	  * @author:zhangfangzhi
	  * @date 2017年3月16日 上午11:27:14
	  * @version V1.0
	 */
	String saveList(String userInfo, String saveJson);

	/**
	  * @Description:名称编码重复校验
	  * @author:zhangfangzhi
	  * @date 2017年3月21日 下午2:42:22
	  * @version V1.0
	 */
	String validateIsExist(Object object, String saveJson);

	/**
	  * @Description:保存并且校验
	  * @author:zhangfangzhi
	  * @date 2017年3月22日 下午6:50:50
	  * @version V1.0
	 */
	String validateBeforeSave(String userInfo, String saveJson);

	/**
	  * @Description:修改并校验
	  * @author:zhangfangzhi
	  * @date 2017年3月22日 下午8:23:04
	  * @version V1.0
	 */
	String validateBeforeUpdate(String userInfo, String updateJson);

}
