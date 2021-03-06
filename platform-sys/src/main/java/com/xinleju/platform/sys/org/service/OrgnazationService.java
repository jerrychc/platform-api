package com.xinleju.platform.sys.org.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.out.app.org.entity.UserAuthDataOrgList;
import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import com.xinleju.platform.sys.org.dto.OrgnazationExcelDto;
import com.xinleju.platform.sys.org.dto.OrgnazationNodeDto;
import com.xinleju.platform.sys.org.entity.Orgnazation;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;

/**
 * @author admin
 * 
 * 
 */

public interface OrgnazationService extends  BaseService <String,Orgnazation>{


	/**
	 * 获取目录下的一级集团和公司
	 * @param rootId
	 * @return
	 */
	public List<OrgnazationNodeDto> queryOrgListRoot(String rootId)  throws Exception;
	
	/**
	 * 获取目录下的一级集团和公司
	 * @param rootId
	 * @return
	 */
	public List<Orgnazation> queryOrgListRootReturnOrg(String rootId)  throws Exception;
	
	/**
	 * 获取目录下的一级集团和公司,序号后面的数据
	 * @param rootId
	 * @return
	 */
	public List<Orgnazation> queryOrgListRootReturnOrg(String rootId,Long sort)  throws Exception;
	
	
	/**
	 * 查询组织结构子节点
	 * @param parentId
	 * @return
	 */
	public List<OrgnazationNodeDto> queryOrgList(String parentId)  throws Exception;
	
	/**
	 * 查询所有组织机构
	 * @return
	 */
	public List<OrgnazationNodeDto> queryAllOrgList(Map<String,Object> map)  throws Exception;
	
	/**
	 * 查询所有组织机构
	 * @return
	 */
	public List<Orgnazation> queryAllOrgListReturnOrg()  throws Exception;
	
	/**
	 * 查询组织结构子节点
	 * @param parentId
	 * @return
	 */
	public List<Orgnazation> queryOrgListReturnOrg(String parentId)  throws Exception;
	
	/**
	 * 查询组织结构子节点 序号后面的数据
	 * @param parentId
	 * @return
	 */
	public List<Orgnazation> queryOrgListReturnOrg(String parentId,Long sort)  throws Exception;

	/**
	 * 获取所有的公司 (只查公司)
	 * @param paramater
	 * @return
	 */
	public List<Map<String,Object>> queryListCompany(Map map)throws Exception;
	
	/**
	 * 根据IDs获取结果集
	 * @param paramater
	 * @return
	 */
	public List<Map<String,String>> queryOrgsByIds(Map map)throws Exception;
	
	/**
	 * 修改全路径
	 * @param paramater
	 * @return
	 */
	public Integer updatePrefix(Map map)throws Exception;
	
	/**
	 * 修改新方法
	 * @param paramater
	 * @return
	 */
	public Integer updateNew(String updateJson)throws Exception;
	/**
	 * 禁用组织
	 * @param paramater
	 * @return
	 */
	public Integer lockOrg(Map map)throws Exception;
	
	/**
	 * 启用组织
	 * @param paramater
	 * @return
	 */
	public Integer unLockOrg(Map map)throws Exception;
	
	/**
	 * 根据组织机构Id查询以上级别的组织机构（当前用户）
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public List<OrgnazationDto> queryAuthOrgListByOrgId(Map<String, Object> paramater)throws Exception;


	/**
	 * @return
	 */
	public List<String> getCompanyOrgId()throws Exception;

	/**
	 * 查询组织详情
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public OrgnazationDto getOrgById(Map<String, Object> paramater)throws Exception;
	/**
	 * 根据用户id，获取其角色/岗位/组织/菜单  等信息
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public AuthenticationDto getUserRPOMInfoByUserId(Map<String, Object> paramater)throws Exception;
	/**
	 * 查询用户所有组织信息：所属组织U岗位组织
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public List<OrgnazationDto> getUserAllOrgs(Map<String, Object> paramater)throws Exception;
	/**
	 * 查询部门 或项目分期 （包含集团和公司）
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getDeptOrBranch(Map<String, Object> paramater)throws Exception;
	
	/**
	 * 查询所有组织
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public List<OrgnazationDto> getAllOrgListDto(Map<String, Object> paramater)throws Exception;
	/**
	 * 根据ID获取组织机构（可以多个）
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public List<OrgnazationDto> getOrgsByIds(Map<String, Object> paramater)throws Exception;
	/**
	 * 获取指定公司的下级公司
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getChildOrgList(Map<String, Object> paramater)throws Exception;
	/**
	 * 获取指定公司的下级公司
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getChildBranchList(Map<String, Object> paramater)throws Exception;
	/**
	 * 获取用户岗位对应公司/部门
	 * @param paramJson:{userIds:"指用户Ids"}
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getUserPostRelationOrgList(Map<String, Object> paramater)throws Exception;
	/**
	 * 获取用户直属组织(集团/公司/部门/项目/分期)
	 * @param paramJson:{userIds:"指用户Ids"}
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getUserOrgnazationList(Map<String, Object> paramater)throws Exception;
	/**
	 * 获取指定岗位的组织信息
	 * @param paramJson:{postIds:"指岗位Ids"}
	 * @return
	 */
	public Map<String,Object> getOrgRelationByPostId(Map<String, Object> paramater)throws Exception;
	/**
	 * 根据组织id查询组织dto：公司/部门/项目/分期
	 * @param userJson
	 * @param paramJson:{orgIds:"组织ids"}
	 */
	public Map<String,Object> getOrgDtoByOrgIds(Map<String, Object> paramater)throws Exception;
	/**
	 * 获取所有组织机构
	 * @param userJson
	 */
	public List<OrgnazationDto> getAllOrgList(Map<String, Object> paramater)throws Exception;
	/**
	 * 获取用户主岗组织信息
	 * @param userJson
	 */
	public Map<String,Object> getMainOrgRelationByUserId(Map<String, Object> paramater)throws Exception;
	/**
	 * 获取上级组织信息
	 * @param userJson
	 */
	public Map<String,Object> getParentOrgByOrgId(Map<String, Object> paramater)throws Exception;

	/**
	 * @param param
	 * @return
	 */
	public List<Orgnazation> queryListCompanyAndZb()throws Exception;
	/**
	 * 获取用户授权公司和部门
	 * @param param
	 * @return
	 */
	public Map<String,UserAuthDataOrgList> getUserDataAuthCoAndDeptList(Map<String, Object> param)throws Exception;
	/**
	 * 获取用户授权项目和分期
	 * @param param
	 * @return
	 */
	public Map<String,UserAuthDataOrgList> getUserDataAuthGroupAndBranchList(Map<String, Object> param)throws Exception;
	/**
	 * 根据岗位id获取其上级组织id
	 * @param param
	 * @return
	 */
	public List<OrgnazationDto> getOrgsByPostId(Map<String, Object> param)throws Exception;
	//复制粘贴组织结构
	public Map<String, Orgnazation> saveCopyAndPasteOrg(Map<String, Object> param)throws Exception;
	//删除符合条件（没有被引用）的组织及其下级
	public Integer deleteOrgAllSon(Orgnazation orgnazation)throws Exception;
	//获取公司下级组织
	public List<Map<String,Object>> getSubOrgByComId(Map<String, Object> param)throws Exception;
	//获取公司下级分期
	public List<Map<String,Object>> getSubBranchByComId(Map<String, Object> param)throws Exception;
	//获取公司下级用户
	public List<Map<String,Object>> getSubUserByComId(Map<String, Object> param)throws Exception;
	/**
	 * 根据用户其所有组织的顶级部门和顶级公司
	 * add by gyh 2017-7-14
	 * @param user：用户id
	 */
	public List<Map<String,Object>> getTopDeptAnaTopComByUser(Map<String, Object> param)throws Exception;
	/**
	 * 根据用户获取其所有组织的直属部门和直属公司
	 * add by gyh 2017-8-4
	 * @param paramJson:{userId:"指用户Id"}
	 */
	public List<Map<String,Object>> getDirectDeptAnaDirectComByUser(Map<String, Object> param)throws Exception;
/**
	 * 读excel并插入db，校验数据的合法性，插入数据库，并返回结果
	 * add by gyh 2018-1-12
	 * @param list:读取excel组织数据
	 */
	public Map<String,Object> readExcelAndInsert(List<OrgnazationExcelDto> list, String parentId)throws Exception;

	/**
	 * 校验同级编码重复
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int checkCode(Map<String, Object> param)throws Exception;
	/**
	 * 查找用户直属部门和直属公司
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> queryUserDirectDeptAndDirectCom(Map<String, Object> param)throws Exception;
}
