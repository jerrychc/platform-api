package com.xinleju.platform.sys.base.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.base.dto.CustomFormInstanceDto;
import com.xinleju.platform.sys.base.entity.CustomFormInstance;

/**
 * @author admin
 *
 */

public interface CustomFormInstanceDao extends BaseDao<String, CustomFormInstance> {

	/**
	  * @Description:分页查询（排序）
	  * @author:zhangfangzhi
	  * @date 2017年3月29日 下午7:08:10
	  * @version V1.0
	 */
	List<Map<String, Object>> getPageSort(Map<String, Object> map);

	/**
	  * @Description:分页查询（排序统计个数）
	  * @author:zhangfangzhi
	  * @date 2017年3月29日 下午7:08:41
	  * @version V1.0
	 */
	Integer getPageSortCount(Map<String, Object> map);

	/**
	  * @Description:流程回调修改状态
	  * @author:zhangfangzhi
	  * @date 2017年4月20日 下午7:36:56
	  * @version V1.0
	 */
	int updateStatus(CustomFormInstance customFormInstance);

	/**
	  * @Description:查询当天最大编号数
	  * @author:zhangfangzhi
	  * @date 2017年6月16日 上午11:10:31
	  * @version V1.0
	 */
	Integer queryCurrentNumber();

	/**
	  * @Description:付款管理分页查询
	  * @author:zhangfangzhi
	  * @date 2017年11月6日 下午4:39:25
	  * @version V1.0
	 */
	List<CustomFormInstanceDto> getFundPageSort(Map map);

	/**
	  * @Description:付款管理分页查询（排序统计个数）
	  * @author:zhangfangzhi
	  * @date 2017年11月6日 下午4:39:46
	  * @version V1.0
	 */
	Integer getFundPageSortCount(Map map);

	/**
	  * @Description:根据参数查询
	  * @author:zhangfangzhi
	  * @date 2017年11月7日 下午5:06:41
	  * @version V1.0
	 */
	List<CustomFormInstanceDto> queryListByParam(Map<String, Object> paramMap);

	/**
	  * @Description:根据单据编号查询单据
	  * @author:zhangfangzhi
	  * @date 2017年11月8日 下午2:29:26
	  * @version V1.0
	 */
	CustomFormInstance getObjectByBusinessCode(String billcode);

	/**
	  * @Description:获取相应模板下有权限的表单实例id
	  * @author:zhangfangzhi
	  * @date 2017年11月14日 上午9:46:43
	  * @version V1.0
	 */
	List<String> getAuthsInstanceIds(Map map);

	/**
	  * @Description:将实例指向历史模板
	  * @author:zhangfangzhi
	  * @date 2017年11月30日 上午10:08:19
	  * @version V1.0
	 */
	void updateInstanceToHisTemplate(Map<String, Object> paramMap);

	/**
	  * @Description:处理费用老数据同步
	  * @author:zhangfangzhi
	  * @date 2018年1月2日 下午3:58:21
	  * @version V1.0
	 */
	String queryBusinessIdByCode(String businessId);

	/**
	  * @Description:判断是否是自定义表单
	  * @author:zhangfangzhi
	  * @date 2018年1月17日 上午10:43:00
	  * @version V1.0
	 */
	String isCustomformInstance(String id);

	/**
	  * @Description:我的发起分页查询
	  * @author:zhangfangzhi
	  * @date 2018年1月19日 下午7:15:11
	  * @version V1.0
	 */
	List<Map<String, Object>> getMyFormPageSort(Map map);

	/**
	  * @Description:我的发起分页查询(排序统计个数)
	  * @author:zhangfangzhi
	  * @date 2018年1月19日 下午7:16:25
	  * @version V1.0
	 */
	Integer getMyFormPageCount(Map map);

}
