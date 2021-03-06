package com.xinleju.platform.sys.org.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.org.dao.PostUserDao;
import com.xinleju.platform.sys.org.entity.PostUser;
import com.xinleju.platform.sys.org.vo.CommonRolePostUserVo;
import com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class PostUserDaoImpl extends BaseDaoImpl<String,PostUser> implements PostUserDao{

	public PostUserDaoImpl() {
		super();
	}
	
	/**
	 * 批量保存post_user和role_user
	 * @param objectList
	 */
	@Override
	public Integer savePostUserAndRoleUser(List<Map<String, Object>> list)throws Exception{
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("list", list);
		Integer count = getSqlSession().insert("com.xinleju.platform.sys.org.entity.PostUser.savePostUserAndRoleUser", map);
		return count;
	}
	@Override
	public Integer delPostUserAndRoleUser(String userId)throws Exception{
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("userId",userId);
		Integer count = getSqlSession().delete("com.xinleju.platform.sys.org.entity.PostUser.delPostUserAndRoleUser", map);
		return count;
	}

	/**
	 * 设置主岗
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer setDefaultPost(Map<String,Object> map) throws Exception{
		return getSqlSession().update("com.xinleju.platform.sys.org.entity.PostUser.setDefaultPost", map);
	}

	@Override
	public List<OrgnazationPostUserVo> getFlowCompanyOrgnazationPostUserVos(
			Map<String, Object> map) {
		
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowCompanyOrgnazationPostUserVos", map);
	}
	@Override
	public List<OrgnazationPostUserVo> getFlowCompanyOrgnazationPostUserVos_2(
			Map<String, Object> map) {
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowCompanyOrgnazationPostUserVos_2", map);
	}
	@Override
	public List<String> getUpCompanyIds(Map<String, Object> map) {
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getUpCompanyIds", map);
	}
	
	@Override
	public List<OrgnazationPostUserVo> getFlowCompanyUpOrgnazationPostUserVos(
			Map<String, Object> map) {
		
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowCompanyUpOrgnazationPostUserVos", map);
	}
	

	@Override
	public List<OrgnazationPostUserVo> getFlowPjectOrgnazationPostUserVos(
			Map<String, Object> map) {
		
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowPjectOrgnazationPostUserVos", map);
	}

	@Override
	public List<OrgnazationPostUserVo> getFlowDeptOrgnazationPostUserVos(
			Map<String, Object> map) {
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowDeptOrgnazationPostUserVos", map);
	}
	
	@Override
	public List<OrgnazationPostUserVo> getFlowOrgnazationPostUserVos_up(
			Map<String, Object> map) {
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowOrgnazationPostUserVos_up", map);
	}

	@Override
	public List<OrgnazationPostUserVo> getFlowDirectOrgnazationPostUserVos(
			Map<String, Object> map) {
		
	  return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowDirectOrgnazationPostUserVos", map);
	}

	@Override
	public List<OrgnazationPostUserVo> getFlowUserDefualtOrgnazationPostUserVos(
			Map<String, Object> map) {
		
		  return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowUserDefualtOrgnazationPostUserVos", map);
	}

	@Override
	public List<OrgnazationPostUserVo> getFlowUserDefualtOrgnazationPostUserVosByUsername(
			Map<String, Object> map) {

		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowUserDefualtOrgnazationPostUserVosByUsername", map);
	}

	@Override
	public List<OrgnazationPostUserVo> getFlowUserOrgnazationPostUserVos(
			Map<String, Object> map) {
		
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowUserOrgnazationPostUserVos", map);
	}

	@Override
	public List<OrgnazationPostUserVo> getFlowPostUserOrgnazationPostUserVos(
			Map<String, Object> map) {
		
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowPostUserOrgnazationPostUserVos", map);
	}
	


	@Override
	public List<CommonRolePostUserVo> getFlowCommonRolePostUserVos(
			Map<String, Object> map) {
		
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowCommonRolePostUserVos", map);
	}
	
	

	@Override
	public List<OrgnazationPostUserVo> getFlowCommonRolePostUser_PostVos(
			Map<String, Object> map) {
		
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowCommonRolePostUser_PostVos", map);
	}

	@Override
	public List<OrgnazationPostUserVo> getFlowCommonRolePostUser_UserVos(
			Map<String, Object> map) {
		
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowCommonRolePostUser_UserVos", map);
	}

	@Override
	public List<OrgnazationPostUserVo> getFlowStartUserOrgnazationPostUserVos(
			Map<String, Object> map) {
		
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowStartUserOrgnazationPostUserVos", map);
	}

	@Override
	public List<OrgnazationPostUserVo> getFlowStartUserDeptOrgnazationPostUserVos(
			Map<String, Object> map) {
		
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowStartUserDeptOrgnazationPostUserVos", map);
	}

	@Override
	public String  getFlowStartUserDeptOrgnazationLeaderId(
			Map<String, Object> map) {
		
		return getSqlSession().selectOne("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowStartUserDeptOrgnazationLeaderId", map);
	}

	@Override
	public List<OrgnazationPostUserVo> getFlowJTOrgnazationPostUserVos(Map<String, Object> map) {
		
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowJTOrgnazationPostUserVos", map);
	}
	
	

	@Override
	public String getProjectIdByProjectBranchId(String branchId) throws Exception {
		
		return getSqlSession().selectOne("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getProjectIdByProjectBranchId", branchId);
	}

	@Override
	public List<OrgnazationPostUserVo> getFlowUserDefualtOrgnazationUserVos(
			Map<String, Object> map) {
		
		return null;
	}

	@Override
	public Integer selectDefault(Map<String, Object> map) {
		return getSqlSession().selectOne("com.xinleju.platform.sys.org.entity.PostUser.selectDefault", map);
	}

	@Override
	public List<OrgnazationPostUserVo> getFlowJTOrgnazationPostVos(Map<String, Object> map) {
		
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getFlowJTOrgnazationPostVos", map);
	}

	@Override
	public List<OrgnazationPostUserVo> getLeaderPostOnOrgWhenLeaderOnPostIsNull(Map<String, Object> paramMap) {
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getLeaderPostOnOrgWhenLeaderOnPostIsNull", paramMap);
	}

	@Override
	public List<OrgnazationPostUserVo> getleaderPost(Map<String, Object> map) {
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getleaderPost", map);
	}
	@Override
	public List<OrgnazationPostUserVo>  getDeptDownUserPostLeaderAndDeptDownLeader(Map<String,Object> map){
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getDeptDownUserPostLeaderAndDeptDownLeader", map);
	}
	@Override
	public List<OrgnazationPostUserVo>  getDeftPostLeaderOrDeftPostOrgLeaderOrUserOrgLeader(Map<String,Object> map){
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.getDeftPostLeaderOrDeftPostOrgLeaderOrUserOrgLeader", map);
	}
	@Override
	public List<OrgnazationPostUserVo>  selectOrgUserById(Map<String,Object> map){
		return getSqlSession().selectList("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.selectOrgUserById", map);
	}
	@Override
	public String selectDriectComId(Map<String, Object> map) {
		return getSqlSession().selectOne("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.selectDriectComId", map);
	}
	@Override
	public OrgnazationPostUserVo selectDownDeptUserPost(Map<String,Object> map){
		return getSqlSession().selectOne("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.selectDownDeptUserPost", map);
	}
	@Override
	public OrgnazationPostUserVo selectUserMainPost(Map<String,Object> map){
		return getSqlSession().selectOne("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.selectUserMainPost", map);
	}
	@Override
	public OrgnazationPostUserVo selectUpDeptUserPost(Map<String,Object> map){
		return getSqlSession().selectOne("com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo.selectUpDeptUserPost", map);
	}
}
