package com.xinleju.platform.out.app.org.service;

public interface OrgnazationOutServiceCustomer {
	
	/**
	 * 获取所有公司
	 * @param userJson
	 * @param paramJson:{isLeaf:true(如果true,返回所有公司，如果是false，返回顶级公司}
	 * @return
	 */
	String getAllCompanyList(String userJson, String paramJson);
	/**
	 * 根据ID获取组织机构（可以多个）
	 * @param userJson
	 * @param paramJson:{ids}
	 * @return
	 */
	String getOrgsByIds(String userJson, String paramJson);
	
	/**
	 * 获取所有部门
	 * @param userJson
	 * @param paramJson:{isLeaf:true(如果true,返回所有公司，如果是false，返回顶级公司}
	 * @return
	 */
	String getAllDeptList(String userJson, String paramJson);
	/**
	 * 获取所有项目
	 * @param userJson
	 * @param paramJson:{isLeaf:true(如果true,返回所有公司，如果是false，返回顶级公司}
	 * @return
	 */
	String getAllProjectList(String userJson, String paramJson);
	/**
	 * 获取指定公司的下级公司
	 * @param userJson
	 * @param paramJson:{companyIds:"指定公司Ids",isLeaf:true(如果true,返回指定公司的所有下级公司，如果是false，返回指定公司的直接公司)}
	 * @return
	 */
	String getChildCompanyList(String userJson, String paramJson);
	/**
	 * 获取指定公司的部门
	 * @param userJson
	 * @param paramJson:{companyIds:"指定公司Ids"，isLeaf:true(如果true,返回指定公司所有部门，如果是false，指定公司所有顶级部门}
	 * @return
	 */
	String getDeptList(String userJson, String paramJson);
	/**
	 * 获取指定公司的项目
	 * @param userJson
	 * @param paramJson:{companyIds:"指定公司Ids"}
	 * @return
	 */
	public String getProjectList(String userJson, String paramJson);
	/**
	 * 获取指定公司的所有分期
	 * @param userJson
	 * @param paramJson:{companyIds:"指定公司Ids"}
	 * @return
	 */
	public String getAllProjectBrachListByCompanyIds(String userJson, String paramJson);
	/**
	 * 获取项目对应所有分期
	 * @param userJson
	 * @param paramJson:{projectIds:"指定项目Ids"}
	 * @return
	 */
	public String getAllProjectBrachListByProjectIds(String userJson, String paramJson);
	/**
	 * 获取用户岗位对应公司
	 * @param userJson
	 * @param paramJson:{userIds:"指用户Ids"}
	 * @return
	 */
	String getUserPostRelationCompanyList(String userJson, String paramJson);
	/**
	 * 获取用户岗位对应部门
	 * @param userJson
	 * @param paramJson:{userIds:"指用户Ids"}
	 * @return
	 */
	String getUserPostRelationDeptList(String userJson, String paramJson);
	/**
	 * 获取用户直属组织(集团/公司/部门/项目/分期)
	 * @param userJson
	 * @param paramJson:{userIds:"指用户Ids"}
	 * @return
	 */
	String getUserOrgnazationList(String userJson, String paramJson);
	/**
	 * 获取指定岗位的组织信息
	 * @param userJson
	 * @param paramJson:{postIds:"指岗位Ids"}
	 * @return
	 */
	String getOrgRelationByPostId(String userJson, String paramJson);
	
	
	/**
	 * 获取所有的项目和分期
	 * @param userJson
	 * @param  
	 * @return
	 */
	String getProjectAndBranchList(String userJson, String paramJson);
	/**
	 * 获取指定公司所有的项目和分期
	 * @param userJson
	 * @param  paramJson:{companyIds:"指定公司Ids"}
	 * @return
	 */
	String getProjectAndBranchListByComId(String userJson, String paramJson);
	/**
	 * 根据组织id查询组织dto
	 * @param userJson
	 * @param paramJson:{orgIds:"组织ids"}
	 * @return
	 */
	String getOrgDtoByOrgIds(String userJson, String paramJson);
	/**
	 * 获取所有组织机构
	 * @param userJson
	 * @param paramJson:{orgIds:"组织ids"}
	 * @return
	 */
	String getAllOrgList(String userJson, String paramJson);
	/**
	 * 获取指定组织下岗位
	 * @param userJson
	 * @param paramJson:{orgIds:"组织ids"}
	 * @return
	 */
	String getPostListByOrgId(String userJson, String paramJson);
	/**
	 * 获取指定用户下岗位
	 * @param userJson
	 * @param paramJson:{userIds:"用户ids"}
	 * @return
	 */
	String getPostListByUserId(String userJson, String paramJson);
	/**
	 * 获取指定用户的主岗对应组织信息
	 * @param userJson
	 * @param paramJson:{userIds:"用户ids"}
	 * @return
	 */
	String getMainOrgRelationByUserId(String userJson, String paramJson);
	/**
	 * 获取上级组织信息
	 * @param userJson
	 * @param paramJson:{orgIds:"组织ids"}
	 * @return
	 */
	String getParentOrgByOrgId(String userJson, String paramJson);
	/**
	 * 获取指定组织的下级组织机构
	 * @param userJson
	 * @param paramJson:{orgIds:"组织ids"，isLeaf:true(如果true,返回所有下级，如果是false，返回直接下级）}
	 * @return
	 */
	String getChildOrgListByOrgId(String userJson, String paramJson);
	/**
	 * 获取用户数据授权公司、部门
	 * @param userJson
	 * @param paramJson:{userIds:"指用户Ids"，appId:"系统id"}
	 * @return
	 */
	String getUserDataAuthCoAndDeptList(String userJson, String paramJson);
	/**
	 * 获取用户数据授权公司、部门
	 * @param userJson
	 * @param paramJson:{userIds:"指用户Ids"，appId:"系统id"}
	 * @return
	 */
	String getUserDataAuthGroupAndBranchList(String userJson, String paramJson);
	
	/**
	 * 获取公司下的项目分期
	 * @param userJson
	 * @param paramJson:{companyId:"公司id"}
	 * @return
	 */
	public String getSubGroupAndBranchByCompany(String userJson, String paramJson);
	/**
	 * 获取所有一级公司
	 * @param userJson
	 * @param paramJson
	 * @return
	 */
	public String getTopCompany(String userJson, String paramJson);
	/**
	 * 获取公司下的公司
	 * @param userJson
	 * @param paramJson:{companyId:"公司id"}
	 * @return
	 */
	public String getSubCompanyByCompany(String userJson, String paramJson);
	/**
	 * 获取公司下的部门
	 * @param userJson
	 * @param paramJson:{companyId:"公司id"}
	 * @return
	 */
	public String getSubDeptByCompany(String userJson, String paramJson);
	/**
	 * 获取公司下的人员
	 * @param userJson
	 * @param paramJson:{companyId:"公司id"}
	 * @return
	 */
	public String getSubUserByCompany(String userJson, String paramJson);
	
	/**
	 * 根据授权点获取用户数据授权公司、部门
	 * @param userJson
	 * @param paramJson:{userIds:"指用户Ids"，appId:"系统id",itemCode:"数据授权点code"}
	 * @return
	 */
	public String getUserDataAuthCoAndDeptListByItemCode(String userJson, String paramJson) ;
	
	/**
	 * 根据授权点获取用户数据授权项目、分期
	 * @param userJson
	 * @param paramJson:{userIds:"指用户Ids"，appId:"系统id",itemCode:"数据授权点code"}
	 * @return
	 */
	public String getUserDataAuthGroupAndBranchListByItemCode(String userJson, String paramJson);
	/**
	 * 根据用户获取其所有组织的顶级部门和顶级公司
	 * add by gyh 2017-7-14
	 * @param userJson
	 * @param paramJson:{userId:"指用户Id"}
	 * @return
	 */
	public String getTopDeptAnaTopComByUser(String userJson, String paramJson);
	/**
	 * 根据用户获取其所有组织的直属部门和直属公司
	 * add by gyh 2017-8-4
	 * @param userJson
	 * @param paramJson:{userId:"指用户Id"}
	 * @return
	 */
	public String getDirectDeptAnaDirectComByUser(String userJson, String paramJson);
	/**
	 * 根据岗位ids获取岗位信息
	 * add by sy 2017-9-11
	 * @param userJson
	 * @param paramJson:{postId:"指定postId"}
	 * @return
	 */
	public String getPostInfoBypostIds(String userJson, String paramJson);
}
