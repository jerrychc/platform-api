package com.xinleju.platform.out.app.old.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.xinleju.erp.data.cache.api.MDProjectCacheService;
import com.xinleju.erp.data.cache.dto.ProductConsDTO;
import com.xinleju.erp.data.cache.dto.ProductTypeDTO;
import com.xinleju.erp.data.cache.dto.ProjectBaseDTO;
import com.xinleju.erp.data.cache.dto.ProjectBranchDTO;
import com.xinleju.erp.data.cache.dto.ProjectDatePlanDTO;
import com.xinleju.erp.data.cache.dto.ProjectTargetDTO;
import com.xinleju.erp.flow.flowutils.bean.DebugInfo;
import com.xinleju.erp.flow.flowutils.bean.FlowResult;
import com.xinleju.erp.flow.flowutils.bean.PageBean;
import com.xinleju.erp.flow.service.api.extend.BaseAPI;
import com.xinleju.erp.flow.service.api.extend.OrgnService;
import com.xinleju.erp.flow.service.api.extend.UserSerivce;

public class MDProjectCacheServiceImpl implements MDProjectCacheService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private BaseAPI api;
	@Autowired
	private OrgnService orgnService;
	@Autowired
	private UserSerivce userService;
	/**
	 * 根据公司查询项目
	 * @param orgId
	 * @return
	 */
	@Override
	public FlowResult<List<ProjectBaseDTO>> getProjectBaseByOrgId(String orgId) {
		FlowResult<List<ProjectBaseDTO>> result=new FlowResult<>();
		String sql="SELECT o.id,o.`name`,o.`status`,o.parent_id orgnId from pt_sys_org_orgnazation o"
					+" where o.type='group' and o.parent_id=? and o.delflag=0 and o.`status`=1";
		RowMapper<ProjectBaseDTO> rowMapper=new BeanPropertyRowMapper<ProjectBaseDTO>(ProjectBaseDTO.class);
		List<ProjectBaseDTO> list= jdbcTemplate.query(sql,rowMapper,orgId);
		result.setResult(list);
		return result;
	}
	
	/**
	 * 根据所有项目
	 * @return
	 */
	@Override
	public FlowResult<List<ProjectBaseDTO>> getAllProjectBase() {
		FlowResult<List<ProjectBaseDTO>> result=new FlowResult<>();
		String sql="SELECT o.id,o.`name`,o.`status`,o.parent_id orgnId from pt_sys_org_orgnazation o"
					+" where o.type='group' and o.delflag=0 and o.`status`=1";
		RowMapper<ProjectBaseDTO> rowMapper=new BeanPropertyRowMapper<ProjectBaseDTO>(ProjectBaseDTO.class);
		List<ProjectBaseDTO> list= jdbcTemplate.query(sql,rowMapper);
		result.setResult(list);
		return result;
	}
	/**
	 * 根据项目Id查询项目
	 * @param id
	 * @return
	 */
	@Override
	public FlowResult<ProjectBaseDTO> getProjectBaseById(String projectBaseId) {
		FlowResult<ProjectBaseDTO> result=new FlowResult<>();
		String sql="SELECT o.id,o.`name`,o.`status`,o.parent_id orgnId from pt_sys_org_orgnazation o"
					+" where o.type='group' and o.delflag=0 and o.`status`=1 and o.id=?";
		RowMapper<ProjectBaseDTO> rowMapper=new BeanPropertyRowMapper<ProjectBaseDTO>(ProjectBaseDTO.class);
		List<ProjectBaseDTO> list= jdbcTemplate.query(sql,rowMapper,projectBaseId);
		if (list!=null&&list.size()>0) {
			result.setResult(list.get(0));
		}else {
			result.setResult(null);
		}
		return result;
	}
	/**
	 * 取得所有分期
	 * @return
	 */
	@Override
	public FlowResult<List<ProjectBranchDTO>> getAllProjectBranch() {
		FlowResult<List<ProjectBranchDTO>> result=new FlowResult<>();
//		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId from pt_sys_org_orgnazation o where o.type='branch' and o.delflag=0 and o.`status`=1";
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId ,p.id pId,p.`name` pName,p.`status` pStatus,p.parent_id pOrgnId from pt_sys_org_orgnazation o "
					+" LEFT JOIN pt_sys_org_orgnazation p on p.id=o.parent_id"
					+" where o.type='branch' and o.delflag=0 and o.`status`=1";
//		RowMapper<ProjectBranchDTO> rowMapper=new BeanPropertyRowMapper<ProjectBranchDTO>(ProjectBranchDTO.class);
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql);
		List<ProjectBranchDTO> resList=new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map=list.get(i);
			ProjectBranchDTO dto=new ProjectBranchDTO();
			ProjectBaseDTO base=new ProjectBaseDTO();
			dto.setId(map.get("id").toString());
			dto.setCode(map.get("code").toString());
			dto.setName(map.get("name").toString());
			dto.setStatus(Integer.valueOf(map.get("status").toString()));
			dto.setProjectId(map.get("projectId").toString());
			dto.setEarthName(map.get("pName").toString());
			dto.setOrgnId(map.get("pOrgnId").toString());
			base.setId(map.get("pId").toString());
			base.setName(map.get("pName").toString());
			base.setStatus(Integer.valueOf(map.get("pStatus").toString()));
			base.setOrgnId(map.get("pOrgnId").toString());
			dto.setProject(base);
			resList.add(dto);
		}
		result.setResult(resList);
		return result;
	}

	@Override
	public FlowResult<Map<String, String>> getProjectBaseByIds(String[] projectBaseIds) {
		FlowResult<Map<String, String>> result=new FlowResult<>();
		String sql="SELECT o.id, o.`name` FROM  pt_sys_org_orgnazation o WHERE o.id in ('%s')  ";
		if(projectBaseIds!=null&&projectBaseIds.length>0){
			String ids=StringUtils.join(projectBaseIds, "','");
			sql=String.format(sql, ids);
			List<Map<String, Object>> list= jdbcTemplate.queryForList(sql);
			Map<String, String> res=new HashMap<String,String>();
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map=list.get(i);
					res.put(map.get("id").toString(), map.get("name").toString());
				}
				result.setResult(res);
			}else {
				result.setResult(null);
			}
		}else {
			result.setResult(null);
			result.setSuccess(false);
		}
		return result;
	}

	@Override
	public FlowResult<List<ProjectBaseDTO>> getProjectBaseListByIds(String[] projectBaseIds) {
		
		return null;
	}
	/**
	 * 根据分期ID查询分期
	 * @param id
	 * @return
	 */
	@Override
	public FlowResult<ProjectBranchDTO> getProjectBranchByProjectBranchId(String projectBranchId) {
		FlowResult<ProjectBranchDTO> result=new FlowResult<>();
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId,p.id pId,p.`name` pName,p.`status` pStatus,p.parent_id pOrgnId from pt_sys_org_orgnazation o  "
				+" LEFT JOIN pt_sys_org_orgnazation p on p.id=o.parent_id"
				+ " where o.type='branch' and o.delflag=0 and o.`status`=1 and o.id=?";
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql,projectBranchId);
		if(list!=null&&list.size()>0){
			Map<String,Object> map=list.get(0);
			ProjectBranchDTO dto=new ProjectBranchDTO();
			ProjectBaseDTO base=new ProjectBaseDTO();
			dto.setId(map.get("id").toString());
			dto.setCode(map.get("code").toString());
			dto.setName(map.get("name").toString());
			dto.setStatus(Integer.valueOf(map.get("status").toString()));
			dto.setProjectId(map.get("projectId").toString());
			dto.setEarthName(map.get("pName").toString());
			dto.setOrgnId(map.get("pOrgnId").toString());
			base.setId(map.get("pId").toString());
			base.setName(map.get("pName").toString());
			base.setStatus(Integer.valueOf(map.get("pStatus").toString()));
			base.setOrgnId(map.get("pOrgnId").toString());
			dto.setProject(base);
			result.setResult(dto);
		}else{
			result.setResult(null);
		}
		return result;
	}
	/**
	 * 根据分期ID、type查询分期
	 * @param id
	 * @param type
	 * @return
	 */
	@Override
	public FlowResult<ProjectBranchDTO> getProjectBranchByProjectBranchId(String projectBranchId,Map<String,String> map) {
		FlowResult<ProjectBranchDTO> result=new FlowResult<>();
		String sql="SELECT p.id,p.project_id,p.projectname,p.delflag,p.buildarea,parent.name,s.code FROM pt_sys_base_planningindex p  "
				+ " LEFT JOIN pt_sys_org_orgnazation s ON p.project_id = s.id "
				+ " LEFT JOIN pt_sys_org_orgnazation parent ON parent.id = s.parent_id "
				+ " IFNULL(p.delflag, 0) = 0 AND p.approvestatus=2 AND p.iseffective=0 AND p.project_id =? ";
		
		List<Map<String,Object>> list = null;
		if(map != null && StringUtils.isNotBlank(map.get("type"))){
			sql +=  "  AND p.type =?  ORDER BY p.versionnum DESC LIMIT 1 ";
			list = jdbcTemplate.queryForList(sql,projectBranchId,map.get("type"));
		}else{
			sql +=  "  ORDER BY type DESC,versionnum DESC LIMIT 1 ";
			list = jdbcTemplate.queryForList(sql,projectBranchId);
		}
		
		if(list!=null&&list.size()>0){
			Map<String,Object> map1=list.get(0);
			ProjectBranchDTO dto=new ProjectBranchDTO();
			dto.setId(map1.get("id").toString());
			dto.setProjectId(map1.get("project_id").toString());
			dto.setName(map1.get("projectname").toString());
			dto.setIsDelete(Integer.valueOf(map1.get("delflag").toString()));
			dto.setBuidArea((BigDecimal)map1.get("buildarea"));
			dto.setEarthName(map1.get("name").toString());
			dto.setCode(map1.get("code").toString());
			result.setResult(dto);
		}else{
			result.setResult(null);
		}
		return result;
	}
	
	/**
	 * 根据多ID查询项目分期
	 * @param arg0
	 * @return 项目分期ID 和名称
	 */
	@Override
	public FlowResult<Map<String, String>> getProjectBranchByProjectBranchIds(String[] projectBranchIds) {
		FlowResult<Map<String, String>> result=new FlowResult<>();
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId from pt_sys_org_orgnazation o "
				+ " where o.type='branch' and o.delflag=0 and o.`status`=1 and o.id in ('%s')";
		String ids=StringUtils.join(projectBranchIds,"','");
		sql=String.format(sql, ids);
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql);
		Map<String,String> res=new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map=list.get(i);
			res.put(map.get("id").toString(), map.get("name").toString());
		}
		result.setResult(res);
		return result;
	}
	/**
	 * 根据分期ID查询分期
	 * @param projectBranchIds
	 * @return
	 */
	@Override
	public FlowResult<List<ProjectBranchDTO>> getProjectBranchListByProjectBranchIds(String[] projectBranchIds) {
		FlowResult<List<ProjectBranchDTO>> result=new FlowResult<>();
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId,p.id pId,p.`name` pName,p.`status` pStatus,p.parent_id pOrgnId  from pt_sys_org_orgnazation o "
				+" LEFT JOIN pt_sys_org_orgnazation p on p.id=o.parent_id"
				+ " where o.type='branch' and o.delflag=0 and o.`status`=1 and o.id in ('%s')";
		String ids=StringUtils.join(projectBranchIds,"','");
		sql=String.format(sql, ids);
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql);
		List<ProjectBranchDTO> resList=new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map=list.get(i);
			ProjectBranchDTO dto=new ProjectBranchDTO();
			ProjectBaseDTO base=new ProjectBaseDTO();
			dto.setId(map.get("id").toString());
			dto.setCode(map.get("code").toString());
			dto.setName(map.get("name").toString());
			dto.setStatus(Integer.valueOf(map.get("status").toString()));
			dto.setProjectId(map.get("projectId").toString());
			dto.setEarthName(map.get("pName").toString());
			dto.setOrgnId(map.get("pOrgnId").toString());
			base.setId(map.get("pId").toString());
			base.setName(map.get("pName").toString());
			base.setStatus(Integer.valueOf(map.get("pStatus").toString()));
			base.setOrgnId(map.get("pOrgnId").toString());
			dto.setProject(base);
			resList.add(dto);
		}
		result.setResult(resList);
		return result;
	}
	/**
	 * 根据项目ID查询分期
	 * @param id
	 * @return
	 */
	@Override
	public FlowResult<List<ProjectBranchDTO>> getProjectBranchByProjectBaseId(String projectBaseId) {
		FlowResult<List<ProjectBranchDTO>> result=new FlowResult<>();
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId,o.`name` earthName,p.id pId,p.`name` pName,p.`status` pStatus,p.parent_id pOrgnId  from pt_sys_org_orgnazation o "
				+" LEFT JOIN pt_sys_org_orgnazation p on p.id=o.parent_id "
				+" where o.type='branch' and o.delflag=0 and o.`status`=1 and o.parent_id=?";
		/*RowMapper<ProjectBranchDTO> rowMapper=new BeanPropertyRowMapper<ProjectBranchDTO>(ProjectBranchDTO.class);
		List<ProjectBranchDTO> list= jdbcTemplate.query(sql,rowMapper,projectBaseId);*/
		
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql,projectBaseId);
		List<ProjectBranchDTO> resList=new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map=list.get(i);
			ProjectBranchDTO dto=new ProjectBranchDTO();
			ProjectBaseDTO base=new ProjectBaseDTO();
			dto.setId(map.get("id").toString());
			dto.setCode(map.get("code").toString());
			dto.setName(map.get("name").toString());
			dto.setStatus(Integer.valueOf(map.get("status").toString()));
			dto.setProjectId(map.get("projectId").toString());
			dto.setEarthName(map.get("pName").toString());
			dto.setOrgnId(map.get("pOrgnId").toString());
			base.setId(map.get("pId").toString());
			base.setName(map.get("pName").toString());
			base.setStatus(Integer.valueOf(map.get("pStatus").toString()));
			base.setOrgnId(map.get("pOrgnId").toString());
			dto.setProject(base);
			resList.add(dto);
		}
		result.setResult(resList);
		return result;
	}

	@Override
	public FlowResult<Map<String, String>> getProjectBranchMapByOrgId(String orgId) {
		
		return null;
	}
	/**
	 * 根据公司查询项目分期
	 * @param arg0
	 * @return 
	 */
	@Override
	public FlowResult<List<ProjectBranchDTO>> getProjectBranchListByOrgId(String orgId) {
		FlowResult<List<ProjectBranchDTO>> result=new FlowResult<>();
		/*String getchildSql="SELECT getChildList(?)";
		String ids= jdbcTemplate.queryForObject(getchildSql, String.class,orgId);
		if (ids!=null) {
			ids=ids.replaceAll(orgId, "");
			if (StringUtils.isNotBlank(ids)) {
				ids=StringUtils.join(ids.split(","),"','");
				String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId from pt_sys_org_orgnazation o "
						+ " where o.type='branch' and o.delflag=0 and o.`status`=1 and o.id in ('%s')";
				RowMapper<ProjectBranchDTO> rowMapper=new BeanPropertyRowMapper<ProjectBranchDTO>(ProjectBranchDTO.class);
				sql=String.format(sql, ids);
				List<ProjectBranchDTO> list= jdbcTemplate.query(sql,rowMapper);
				result.setResult(list);
			}else{
				result.setResult(null);
			}
		}else{
			result.setResult(null);
		}*/
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId ,p.id pId,p.`name` pName,p.`status` pStatus,p.parent_id pOrgnId from pt_sys_org_orgnazation o "
				+" LEFT JOIN pt_sys_org_orgnazation p on p.id=o.parent_id"
				+" where o.type='branch' and o.delflag=0 and o.`status`=1  and p.parent_id= ?";
	//	RowMapper<ProjectBranchDTO> rowMapper=new BeanPropertyRowMapper<ProjectBranchDTO>(ProjectBranchDTO.class);
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql,orgId);
		List<ProjectBranchDTO> resList=new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map=list.get(i);
			ProjectBranchDTO dto=new ProjectBranchDTO();
			ProjectBaseDTO base=new ProjectBaseDTO();
			dto.setId(map.get("id").toString());
			dto.setCode(map.get("code").toString());
			dto.setName(map.get("name").toString());
			dto.setStatus(Integer.valueOf(map.get("status").toString()));
			dto.setProjectId(map.get("projectId").toString());
			dto.setEarthName(map.get("pName").toString());
			dto.setOrgnId(map.get("pOrgnId").toString());
			base.setId(map.get("pId").toString());
			base.setName(map.get("pName").toString());
			base.setStatus(Integer.valueOf(map.get("pStatus").toString()));
			base.setOrgnId(map.get("pOrgnId").toString());
			dto.setProject(base);
			resList.add(dto);
		}
		result.setResult(resList);
		return result;
		
	}

	@Override
	public FlowResult<List<ProjectBranchDTO>> getProjectBranchListByOrgIds(String[] orgIds) {
		
		return null;
	}

	@Override
	public FlowResult<ProductConsDTO> getProductConsByProductConsId(String productConsId) {
		
		return null;
	}

	//@Override
	public FlowResult<List<ProductConsDTO>> getProductConsByProjectBranchId(String projectBranchId,Map<String, Object> map) {
		FlowResult<List<ProductConsDTO>> result=new FlowResult<List<ProductConsDTO>>();
		List<ProductConsDTO> lists=new ArrayList<ProductConsDTO>();
		String sql="SELECT p.id,p.project_id,p.projectname,p.delflag,p.buildarea,parent.name,s.code FROM pt_sys_base_planningindex p  "
				+ " LEFT JOIN pt_sys_org_orgnazation s ON p.project_id = s.id "
				+ " LEFT JOIN pt_sys_org_orgnazation parent ON parent.id = s.parent_id "
				+ " IFNULL(p.delflag, 0) = 0 AND p.approvestatus=2 AND p.iseffective=0 AND p.project_id =? ";
		
		List<Map<String,Object>> list = null;
		if(map != null && StringUtils.isNotBlank(map.get("type").toString())){
			sql +=  "  AND p.type =?  ORDER BY p.versionnum DESC LIMIT 1 ";
			list = jdbcTemplate.queryForList(sql,projectBranchId,map.get("type"));
		}else{
			sql +=  "  ORDER BY type DESC,versionnum DESC LIMIT 1 ";
			list = jdbcTemplate.queryForList(sql,projectBranchId);
		}
		
		if(list!=null&&list.size()>0){
			Map<String,Object> map1=list.get(0);
			ProductConsDTO dto=new ProductConsDTO();
			dto.setId(map1.get("id").toString());
			dto.setIsSplite(Integer.parseInt(map1.get("").toString()));
			dto.setIsDelete(Integer.parseInt(map1.get("").toString()));
			dto.setBuidingArea(new BigDecimal(map1.get("").toString()));
			dto.setSellArea(new BigDecimal(map1.get("").toString()));
			dto.setFloorArea(new BigDecimal(map1.get("").toString()));
			dto.setIndoorArea(new BigDecimal(map1.get("").toString()));
			dto.setBuildingUpArea(new BigDecimal(map1.get("").toString()));
			
			
			
			lists.add(dto);
			result.setResult(lists);
		}else{
			result.setResult(null);
		}
		return result;
	}

	@Override
	public FlowResult<ProjectTargetDTO> getProjectTargetByProjectTargetId(String projectTargetId) {
		
		return null;
	}

	//@Override
	public FlowResult<ProjectTargetDTO> getProjectTargetByProjectBranchId(String projectBranchId,Map<String, Object> map) {
		FlowResult<ProjectTargetDTO> result=new FlowResult<ProjectTargetDTO>();
		String sql="SELECT p.id,p.project_id,p.projectname,p.delflag,p.buildarea,parent.name,s.code FROM pt_sys_base_planningindex p  "
				+ " LEFT JOIN pt_sys_org_orgnazation s ON p.project_id = s.id "
				+ " LEFT JOIN pt_sys_org_orgnazation parent ON parent.id = s.parent_id "
				+ " IFNULL(p.delflag, 0) = 0 AND p.approvestatus=2 AND p.iseffective=0 AND p.project_id =? ";
		
		List<Map<String,Object>> list = null;
		if(map != null && StringUtils.isNotBlank(map.get("type").toString())){
			sql +=  "  AND p.type =?  ORDER BY p.versionnum DESC LIMIT 1 ";
			list = jdbcTemplate.queryForList(sql,projectBranchId,map.get("type"));
		}else{
			sql +=  "  ORDER BY type DESC,versionnum DESC LIMIT 1 ";
			list = jdbcTemplate.queryForList(sql,projectBranchId);
		}
		
		if(list!=null&&list.size()>0){
			Map<String,Object> map1=list.get(0);
			ProjectTargetDTO dto=new ProjectTargetDTO();
             dto.setSellUpArea(new BigDecimal(map1.get("").toString()));
             dto.setSellDownArea(new BigDecimal(map1.get("").toString()));
             dto.setSellArea(new BigDecimal(map1.get("").toString()));
             dto.setBuidingArea(new BigDecimal(map1.get("").toString()));
             dto.setFloorArea(new BigDecimal(map1.get("").toString()));
             dto.setAreaRatio(new BigDecimal(map1.get("").toString()));
             dto.setParkNum(Integer.parseInt(map1.get("").toString()));
             dto.setBuildingUpArea(new BigDecimal(map1.get("").toString()));
             dto.setBuildingDownArea(new BigDecimal(map1.get("").toString()));
//             dto.setIndoorArea(new BigDecimal(map1.get("").toString()));
             
             
			result.setResult(dto);
		}else{
			result.setResult(null);
		}
		return result;
	}

	@Override
	public FlowResult<ProjectDatePlanDTO> getProjectDatePlanByProjectDatePlanId(String projectDatePlanId) {
		
		return null;
	}

	@Override
	public FlowResult<List<ProjectDatePlanDTO>> getProjectDatePlanByProjectBranchId(String projectBranchId) {
		
		return null;
	}

	//@Override
	public FlowResult<ProductTypeDTO> getProductTypeByIdProductConsId(String productConsId,Map<String, Object> map) {
		FlowResult<ProductTypeDTO> result=new FlowResult<ProductTypeDTO>();
		String sql="SELECT p.id,p.project_id,p.projectname,p.delflag,p.buildarea,parent.name,s.code FROM pt_sys_base_planningindex p  "
				+ " LEFT JOIN pt_sys_org_orgnazation s ON p.project_id = s.id "
				+ " LEFT JOIN pt_sys_org_orgnazation parent ON parent.id = s.parent_id "
				+ " IFNULL(p.delflag, 0) = 0 AND p.approvestatus=2 AND p.iseffective=0 AND p.project_id =? ";
		
		List<Map<String,Object>> list = null;
		if(map != null && StringUtils.isNotBlank(map.get("type").toString())){
			sql +=  "  AND p.type =?  ORDER BY p.versionnum DESC LIMIT 1 ";
			list = jdbcTemplate.queryForList(sql,productConsId,map.get("type"));
		}else{
			sql +=  "  ORDER BY type DESC,versionnum DESC LIMIT 1 ";
			list = jdbcTemplate.queryForList(sql,productConsId);
		}
		
		if(list!=null&&list.size()>0){
			Map<String,Object> map1=list.get(0);
			ProductTypeDTO dto=new ProductTypeDTO();
			dto.setId(map1.get("id").toString());
			dto.setName(map1.get("name").toString());
			dto.setCode(map1.get("code").toString());
			dto.setFullCode(map1.get("fullcode").toString());
			dto.setStatus(Integer.parseInt(map1.get("status").toString()));
			dto.setIsDelete(Integer.parseInt(map1.get("isDelete").toString()));
			result.setResult(dto);
		}else{
			result.setResult(null);
		}
		return result;
	}

	@Override
	public FlowResult<List<ProjectBaseDTO>> getProjectBaseByOrgId(String orgId, String moduleCode,
			String authUserLoginName, String ctrId, String fieldId) {
		FlowResult<List<ProjectBaseDTO>> result=new FlowResult<>();
		String org[]={orgId};
		FlowResult<List> res=getAuthOrgIds(moduleCode, authUserLoginName, 1,org);
		result.setDebugInfo(res.getDebugInfo());
		result.setResult(res.getResult());
		result.setSuccess(res.isSuccess());
		return result;
	}

	@Override
	public FlowResult<List<ProjectBaseDTO>> getProjectBaseByOrgIds(String[] orgIds, String moduleCode,
			String authUserLoginName, String ctrId, String fieldId) {
		FlowResult<List<ProjectBaseDTO>> result=new FlowResult<>();
		FlowResult<List> res=getAuthOrgIds(moduleCode, authUserLoginName,1,orgIds);
		result.setDebugInfo(res.getDebugInfo());
		result.setResult(res.getResult());
		result.setSuccess(res.isSuccess());
		return result;
	}

	@Override
	public FlowResult<List<ProjectBaseDTO>> getProjectBaseByOrgIds(String[] orgIds) {
		
		return null;
	}
	/**
	 * 取得所有有权限项目
	 * @return
	 */
	@Override
	public FlowResult<List<ProjectBaseDTO>> getAllProjectBase(String moduleCode, String authUserLoginName, String ctrId,
			String fieldId) {
		FlowResult<List<ProjectBaseDTO>> result=new FlowResult<>();
		FlowResult<List> res=getAuthOrgIds(moduleCode, authUserLoginName, 1,null);
		result.setDebugInfo(res.getDebugInfo());
		result.setResult(res.getResult());
		result.setSuccess(res.isSuccess());
		return result;
	}

	@Override
	public FlowResult<ProjectBaseDTO> getProjectBaseById(String projectBaseId, String moduleCode,
			String authUserLoginName, String ctrId, String fieldId) {
		
		return null;
	}
	/**
	 * 取得所有有权限分期
	 * @return
	 */
	@Override
	public FlowResult<List<ProjectBranchDTO>> getAllProjectBranch(String moduleCode, String authUserLoginName,String ctrId, String fieldId) {
		FlowResult<List<ProjectBranchDTO>> result=new FlowResult<>();
		FlowResult<List> res=getAuthOrgIds(moduleCode, authUserLoginName, 2,null);
		result.setDebugInfo(res.getDebugInfo());
		result.setResult(res.getResult());
		result.setSuccess(res.isSuccess());
		return result;
	}
	
	/**
	 * @param moduleCode 系统编码
	 * @param authUserLoginName 登录用户用户名
	 * @param authType 返回内容：1项目，2分期
	 * @return
	 */
	public FlowResult<List> getAuthOrgIds(String moduleCode,String authUserLoginName,int authType,String[] orgs) {
		FlowResult<List> result=new FlowResult<>();
		DebugInfo info=new DebugInfo();
		//判断用户id
		String userSql="SELECT u.id from pt_sys_org_user u WHERE u.login_name=? and u.delflag=0 and u.`status`=1";
		String uId= jdbcTemplate.queryForObject(userSql,String.class,authUserLoginName);
		if (uId==null||StringUtils.isBlank(uId)) {
			result.setSuccess(false);
			info.setCompleteAt("该用户不存在或被禁用");
			result.setDebugInfo(info);
			return result;
		}
		//判断系统id
		String appSql="SELECT a.id from pt_sys_res_app a WHERE a.`code`=? and a.delflag=0 and a.`status`=1";
		String appId= jdbcTemplate.queryForObject(appSql,String.class,moduleCode);
		if (appId==null||StringUtils.isBlank(appId)) {
			result.setSuccess(false);
			info.setCompleteAt("该系统不存在或被禁用");
			result.setDebugInfo(info);
			return result;
		}
		/*//查询用户所有角色
		String uRoleSql="SELECT GROUP_CONCAT(\"'\",t.id,\"'\") from ("
				+" SELECT r.id FROM pt_sys_org_standard_role r ,pt_sys_org_post p ,pt_sys_org_post_user pu"
				+" where r.id = p.role_id and r.delflag = 0 and r.status = 1 "
				+" and  p.id = pu.post_id and pu.user_id = ?"
				+" and pu.delflag = 0 and p.delflag = 0 and p.status = 1"
				+" UNION"
				+" SELECT r.id FROM pt_sys_org_standard_role r ,pt_sys_org_role_user ru "
				+" where r.id = ru.role_id and ru.user_id =? and r.delflag = 0 and r.status = 1"
				+" ) t";
		String uRoles= jdbcTemplate.queryForObject(uRoleSql,String.class,uId,uId);*/
		String uRoles= selectAuthTypeId(uId);
		//查询所有角色授权控制点
		String dataPointSql="SELECT GROUP_CONCAT(po.`code`) from pt_sys_res_data_permission p" 
				+" LEFT JOIN pt_sys_res_data_point po on po.id=data_point_id"
				+" LEFT JOIN pt_sys_res_data_item i on i.id=po.item_id"
				+" WHERE p.role_id in (%s)"
				+" and i.item_code='group' and i.app_id=? and po.delflag=0 and i.delflag=0";
		dataPointSql=String.format(dataPointSql, uRoles);
		String authDdataPoint= jdbcTemplate.queryForObject(dataPointSql,String.class,appId);
		if(authDdataPoint==null||StringUtils.isBlank(authDdataPoint)){
			result.setSuccess(false);
			info.setCompleteAt("没有数据授权信息");
			result.setDebugInfo(info);
			return result;
		}
		//根据授权情况获取授权公司
		if(authDdataPoint.contains("allOrg")){
			//全全部
			//获取授权项目或分期
			String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId,o.parent_id orgnId,p.id pId,p.`name` pName,p.`status` pStatus,p.parent_id pOrgnId   from pt_sys_org_orgnazation o "
					+" LEFT JOIN pt_sys_org_orgnazation p on p.id=o.parent_id "
					+" where o.delflag=0 and o.`status`=1 and o.type %s";
			if (authType==1) {//项目
				sql=String.format(sql, " ='group'");
				if (orgs!=null&&orgs.length>0) {
					String ids=StringUtils.join(orgs, "','");
					sql="SELECT DISTINCT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId,o.parent_id orgnId  from pt_sys_org_orgnazation o "
						+" LEFT JOIN pt_sys_org_orgnazation t on o.prefix_id LIKE CONCAT('',t.prefix_id,'%%')"
						+" where o.delflag=0 and o.`status`=1 and o.type ='group' and t.id in ('"+ids+"')";
				}
				RowMapper<ProjectBaseDTO> rowMapper=new BeanPropertyRowMapper<ProjectBaseDTO>(ProjectBaseDTO.class);
				List<ProjectBaseDTO> list= jdbcTemplate.query(sql,rowMapper);
				result.setResult(list);
			}else{//分期
				sql=String.format(sql, " ='branch'");
				if (orgs!=null&&orgs.length>0) {
					String ids=StringUtils.join(orgs, "','");
					sql="SELECT DISTINCT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId,o.parent_id orgnId,p.id pId,p.`name` pName,p.`status` pStatus,p.parent_id pOrgnId  from pt_sys_org_orgnazation o "
							+" LEFT JOIN pt_sys_org_orgnazation p on p.id=o.parent_id "
							+" LEFT JOIN pt_sys_org_orgnazation t on o.prefix_id LIKE CONCAT('',t.prefix_id,'%%')"
							+" where o.delflag=0 and o.`status`=1 and o.type ='branch' and t.id in ('"+ids+"')";
				}
//				RowMapper<ProjectBranchDTO> rowMapper=new BeanPropertyRowMapper<ProjectBranchDTO>(ProjectBranchDTO.class);
				List<Map<String,Object>> list= jdbcTemplate.queryForList(sql);
				List<ProjectBranchDTO> resList=new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					Map<String,Object> map=list.get(i);
					ProjectBranchDTO dto=new ProjectBranchDTO();
					ProjectBaseDTO base=new ProjectBaseDTO();
					dto.setId(map.get("id").toString());
					dto.setCode(map.get("code").toString());
					dto.setName(map.get("name").toString());
					dto.setStatus(Integer.valueOf(map.get("status").toString()));
					dto.setProjectId(map.get("projectId").toString());
					dto.setEarthName(map.get("pName").toString());
					dto.setOrgnId(map.get("pOrgnId").toString());
					base.setId(map.get("pId").toString());
					base.setName(map.get("pName").toString());
					base.setStatus(Integer.valueOf(map.get("pStatus").toString()));
					base.setOrgnId(map.get("pOrgnId").toString());
					dto.setProject(base);
					resList.add(dto);
				}
				result.setResult(resList);
			}
				
		}else {
			//所有授权公司id
			List<String> ids=new ArrayList<>();
			if (authDdataPoint.contains("thisCompany")||authDdataPoint.contains("thisGroup")||authDdataPoint.contains("thisBranch")) {
				//查询用户所有组织id=用户所属组织+用户岗位组织
				String uOrgSql="SELECT u.belong_org_id id from pt_sys_org_user u WHERE u.id=?"
						+" UNION"
						+" SELECT p.ref_id id from pt_sys_org_post_user pu "
						+" LEFT JOIN pt_sys_org_post p on pu.post_id=p.id"
						+" WHERE pu.user_id=? and p.delflag=0 and p.`status`=1";
				List<String> uOrg= jdbcTemplate.queryForList(uOrgSql,String.class,uId,uId);
				//获取用户组织对应的上级组织id
				String upOrgSql="SELECT top.id"
						+" from pt_sys_org_orgnazation t,pt_sys_org_orgnazation top "
						+" where  t.id =? and top.type %s "
						+" and t.prefix_id like concat(top.prefix_id,'%%') "
						+" ORDER BY LENGTH(top.prefix_id)";
				//查询组织所有下级id（即授权项目/分期）
				String allDownSql="SELECT o.id from pt_sys_org_orgnazation o"
						+" LEFT JOIN pt_sys_org_orgnazation t on o.prefix_id LIKE CONCAT('',t.prefix_id,'%%')"
						+" where t.id in ('%s') and o.delflag=0 and o.`status`=1 and o.type %s ";
				if (authDdataPoint.contains("thisCompany")) {
					//用户所有本公司
					String[] orgArr=new String[uOrg.size()];
					upOrgSql=String.format(upOrgSql, " in ('zb','company')");
					for (int i = 0; i < uOrg.size(); i++) {
						//组织结构
						List<String> upOrgs=jdbcTemplate.queryForList(upOrgSql,String.class,uOrg.get(i));
						if (upOrgs!=null&&upOrgs.size()>0) {
							String orgId=upOrgs.get(upOrgs.size()-1);//本公司
							orgArr[i]=orgId;
						}else {
							orgArr[i]=uOrg.get(i);
						}
					}
					String tid= StringUtils.join(orgArr,"','");
					if (authType==1) {//项目
						allDownSql=String.format(allDownSql,tid," ='group'");
					}else{//分期
						allDownSql=String.format(allDownSql,tid," ='branch'");
					}
					List<String> upOrgs=jdbcTemplate.queryForList(allDownSql,String.class);
					if (upOrgs!=null&&upOrgs.size()>0) {
						ids.addAll(upOrgs);
					}
				}else if(authDdataPoint.contains("thisGroup")){
					//用户所有 本项目
					String[] orgArr=new String[uOrg.size()];
					upOrgSql=String.format(upOrgSql, " ='group'");
					for (int i = 0; i < uOrg.size(); i++) {
						//组织结构
						List<String> upOrgs=jdbcTemplate.queryForList(upOrgSql,String.class,uOrg.get(i));
						if (upOrgs!=null&&upOrgs.size()>0) {
							String orgId=upOrgs.get(0);//本项目
							orgArr[i]=orgId;
						}else {
							orgArr[i]=uOrg.get(i);
						}
					}
					String tid= StringUtils.join(orgArr,"','");
					if (authType==1) {//项目
						allDownSql=String.format(allDownSql,tid," ='group'");
					}else{//分期
						allDownSql=String.format(allDownSql,tid," ='branch'");
					}
					List<String> upOrgs=jdbcTemplate.queryForList(allDownSql,String.class);
					if (upOrgs!=null&&upOrgs.size()>0) {
						ids.addAll(upOrgs);
					}
				}else if (authDdataPoint.contains("thisBranch")) {
					//用户所有 本分期
					String[] orgArr=new String[uOrg.size()];
					upOrgSql=String.format(upOrgSql, "  in ('branch','group')");
					for (int i = 0; i < uOrg.size(); i++) {
						//组织结构
						List<String> upOrgs=jdbcTemplate.queryForList(upOrgSql,String.class,uOrg.get(i));
						if (upOrgs!=null&&upOrgs.size()>0) {
							String orgId=upOrgs.get(0);// 本分期所属的项目
							orgArr[i]=orgId;
						}else {
							orgArr[i]=uOrg.get(i);
						}
					}
					String tid= StringUtils.join(orgArr,"','");
					if (authType==1) {//项目
						allDownSql=String.format(allDownSql,tid," ='group'");
					}else{//分期
						allDownSql=String.format(allDownSql,tid," ='branch'");
					}
					List<String> upOrgs=jdbcTemplate.queryForList(allDownSql,String.class);
					if (upOrgs!=null&&upOrgs.size()>0) {
						ids.addAll(upOrgs);
					}
				}
			}
			if (authDdataPoint.contains("designOrg")) {
				//获取指定授权的授权值
				String dataPointIdsSql="SELECT v.val from pt_sys_res_data_permission p "
						+" LEFT JOIN pt_sys_res_data_point po on po.id=data_point_id"
						+" LEFT JOIN pt_sys_res_data_item i on i.id=po.item_id"
						+" LEFT JOIN  pt_sys_res_data_point_permission_val v on p.id=v.data_permission_id"
						+" WHERE p.role_id in (%s)"
						+" and i.item_code='group' and i.app_id=? and po.type=2 and po.delflag=0 and i.delflag=0 ";
				dataPointIdsSql=String.format(dataPointIdsSql, uRoles);
				List<String> upOrgs=jdbcTemplate.queryForList(dataPointIdsSql,String.class,appId);
				if (upOrgs!=null&&upOrgs.size()>0) {
					ids.addAll(upOrgs);
				}
			}
			//去除重复id
			Set<String> setIds = new HashSet<String>(ids); 
			//获取授权项目/分期
			String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId,o.parent_id orgnId,p.id pId,p.`name` pName,p.`status` pStatus,p.parent_id pOrgnId from pt_sys_org_orgnazation o "
					+" LEFT JOIN pt_sys_org_orgnazation p on p.id=o.parent_id "
					+" where o.delflag=0 and o.`status`=1 and o.type %s and o.id in ('%s')";
			Object[] allIds=  setIds.toArray();
			String allId=StringUtils.join(allIds, "','");
			if (authType==1) {//项目
				sql=String.format(sql," ='group'",allId);
				if (orgs!=null&&orgs.length>0) {
					String ids1=StringUtils.join(orgs, "','");

					sql="SELECT DISTINCT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId,o.parent_id orgnId  from pt_sys_org_orgnazation o "
						+" LEFT JOIN pt_sys_org_orgnazation t on o.prefix_id LIKE CONCAT('',t.prefix_id,'%%')"
						+" where o.delflag=0 and o.`status`=1 and o.type ='group' and o.id in ('%s') and t.id in ('"+ids1+"')";
					sql=String.format(sql,allId);
				}
				RowMapper<ProjectBaseDTO> rowMapper=new BeanPropertyRowMapper<ProjectBaseDTO>(ProjectBaseDTO.class);
				List<ProjectBaseDTO> list= jdbcTemplate.query(sql,rowMapper);
				result.setResult(list);
			}else{//分期
				sql=String.format(sql," ='branch'",allId);
				if (orgs!=null&&orgs.length>0) {
					String ids1=StringUtils.join(orgs, "','");
					sql="SELECT DISTINCT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId,o.parent_id orgnId ,p.id pId,p.`name` pName,p.`status` pStatus,p.parent_id pOrgnId from pt_sys_org_orgnazation o "
							+" LEFT JOIN pt_sys_org_orgnazation p on p.id=o.parent_id "
							+" LEFT JOIN pt_sys_org_orgnazation t on o.prefix_id LIKE CONCAT('',t.prefix_id,'%%')"
							+" where o.delflag=0 and o.`status`=1 and o.type ='branch' and o.id in ('%s') and t.id in ('"+ids1+"')";
					sql=String.format(sql,allId);
				}
/*				RowMapper<ProjectBranchDTO> rowMapper=new BeanPropertyRowMapper<ProjectBranchDTO>(ProjectBranchDTO.class);
				List<ProjectBranchDTO> list= jdbcTemplate.query(sql,rowMapper);
				result.setResult(list);*/
				List<Map<String,Object>> list= jdbcTemplate.queryForList(sql);
				List<ProjectBranchDTO> resList=new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					Map<String,Object> map=list.get(i);
					ProjectBranchDTO dto=new ProjectBranchDTO();
					ProjectBaseDTO base=new ProjectBaseDTO();
					dto.setId(map.get("id").toString());
					dto.setCode(map.get("code").toString());
					dto.setName(map.get("name").toString());
					dto.setStatus(Integer.valueOf(map.get("status").toString()));
					dto.setProjectId(map.get("projectId").toString());
					dto.setEarthName(map.get("pName").toString());
					dto.setOrgnId(map.get("pOrgnId").toString());
					base.setId(map.get("pId").toString());
					base.setName(map.get("pName").toString());
					base.setStatus(Integer.valueOf(map.get("pStatus").toString()));
					base.setOrgnId(map.get("pOrgnId").toString());
					dto.setProject(base);
					resList.add(dto);
				}
				result.setResult(resList);
			}
			
		}
		return result;
	}
	
	public String selectAuthTypeId(String uId) {
		String sql="SELECT u.id from pt_sys_org_user u WHERE u.id=?"
						+" UNION "
						+" SELECT t.id from pt_sys_org_standard_role t "
						+" LEFT JOIN pt_sys_org_post p on p.role_id=t.id"
						+" LEFT JOIN pt_sys_org_post_user pu on pu.post_id=p.id"
						+" WHERE pu.user_id=? and t.delflag=0 and t.`status`=1"
						+" UNION "
						+" SELECT p.id from pt_sys_org_post p "
						+" LEFT JOIN pt_sys_org_post_user pu on pu.post_id=p.id"
						+" WHERE pu.user_id=? and p.delflag=0"
						+" UNION"
						+" SELECT r.id from pt_sys_org_standard_role r"
						+" LEFT JOIN pt_sys_org_role_user ru on ru.role_id=r.id "
						+" WHERE ru.user_id=? and r.delflag=0 and r.`status`=1 "
						+" UNION"
						+" SELECT r.id from pt_sys_org_standard_role r"
						+" LEFT JOIN pt_sys_org_role_user ru on ru.role_id=r.id"
						+" LEFT JOIN pt_sys_org_post p on p.id=ru.post_id AND IFNULL(ru.user_id,'')='' "
						+" LEFT JOIN pt_sys_org_post_user pu on pu.post_id=p.id"
						+" WHERE pu.user_id=? and p.delflag=0";
//		String uRoles= jdbcTemplate.queryForObject(sql,String.class,uId,uId,uId,uId,uId);
		List<String> upOrgs=jdbcTemplate.queryForList(sql,String.class,uId,uId,uId,uId,uId);
		String uRoles="";
		if(!upOrgs.isEmpty()&&upOrgs.size()>0){
			for (String ids : upOrgs) {
				uRoles+="'"+ids+"',";
			}
		}
		if(uRoles.length()>0){
			uRoles=uRoles.substring(0,uRoles.length()-1);
		}
		return uRoles;
	}
	@Override
	public FlowResult<Map<String, String>> getProjectBaseByIds(String[] projectBaseIds, String moduleCode,
			String authUserLoginName, String ctrId, String fieldId) {
		
		return null;
	}

	@Override
	public FlowResult<List<ProjectBaseDTO>> getProjectBaseListByIds(String[] projectBaseIds, String moduleCode,
			String authUserLoginName, String ctrId, String fieldId) {
		
		return null;
	}

	@Override
	public FlowResult<ProjectBranchDTO> getProjectBranchByProjectBranchId(String projectBranchId, String moduleCode,
			String authUserLoginName, String ctrId, String fieldId) {
		
		return null;
	}

	@Override
	public FlowResult<Map<String, String>> getProjectBranchByProjectBranchIds(String[] projectBranchIds,
			String moduleCode, String authUserLoginName, String ctrId, String fieldId) {
		
		return null;
	}

	@Override
	public FlowResult<List<ProjectBranchDTO>> getProjectBranchListByProjectBranchIds(String[] projectBranchIds,
			String moduleCode, String authUserLoginName, String ctrId, String fieldId) {
		
		return null;
	}

	@Override
	public FlowResult<List<ProjectBranchDTO>> getProjectBranchByProjectBaseId(String projectBaseId, String moduleCode,
			String authUserLoginName, String ctrId, String fieldId) {
		String org[]={projectBaseId};
		FlowResult<List<ProjectBranchDTO>> result=new FlowResult<>();
		FlowResult<List> res=getAuthOrgIds( moduleCode, authUserLoginName,2,org);
		result.setDebugInfo(res.getDebugInfo());
		result.setResult(res.getResult());
		result.setSuccess(res.isSuccess());
		return result;
	}

	@Override
	public FlowResult<List<ProjectBranchDTO>> getProjectBranchByProjectBaseIds(String[] projectBaseIds,
			String moduleCode, String authUserLoginName, String ctrId, String fieldId) {
		FlowResult<List<ProjectBranchDTO>> result=new FlowResult<>();
		FlowResult<List> res=getAuthOrgIds(moduleCode, authUserLoginName, 2,projectBaseIds);
		result.setDebugInfo(res.getDebugInfo());
		result.setResult(res.getResult());
		result.setSuccess(res.isSuccess());
		return result;
	}

	@Override
	public FlowResult<List<ProjectBranchDTO>> getProjectBranchByProjectBaseIds(String[] projectBaseIds) {
		
		return null;
	}

	@Override
	public FlowResult<Map<String, String>> getProjectBranchMapByOrgId(String orgId, String moduleCode,
			String authUserLoginName, String ctrId, String fieldId) {
		
		return null;
	}

	@Override
	public FlowResult<List<ProjectBranchDTO>> getProjectBranchListByOrgId(String orgId, String moduleCode,
			String authUserLoginName, String ctrId, String fieldId) {
		FlowResult<List<ProjectBranchDTO>> result=new FlowResult<>();
		String org[]={orgId};
		FlowResult<List> res=getAuthOrgIds(moduleCode, authUserLoginName, 2,org);
		result.setDebugInfo(res.getDebugInfo());
		result.setResult(res.getResult());
		result.setSuccess(res.isSuccess());
		return result;
	}

	@Override
	public FlowResult<List<ProjectBranchDTO>> getProjectBranchListByOrgIds(String[] orgIds, String moduleCode,
			String authUserLoginName, String ctrId, String fieldId) {
		FlowResult<List<ProjectBranchDTO>> result=new FlowResult<>();
		FlowResult<List> res=getAuthOrgIds(moduleCode, authUserLoginName, 2,orgIds);
		result.setDebugInfo(res.getDebugInfo());
		result.setResult(res.getResult());
		result.setSuccess(res.isSuccess());
		return result;
	}

	@Override
	public FlowResult<PageBean<ProjectBranchDTO>> getProjectBranchByCodeOrName(String code, String name, Integer start,
			Integer limit) {
		FlowResult<PageBean<ProjectBranchDTO>> result=new FlowResult<>();
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id projectId from pt_sys_org_orgnazation o  where o.type='branch' and o.delflag=0 and o.`status`=1 ";
		String sqlCount="SELECT count(*) from pt_sys_org_orgnazation o where o.type='branch' and o.delflag=0 and o.`status`=1 ";
		if (code!=null&&StringUtils.isNotBlank(code)) {
			sql=sql+" and o.`code` like '%"+code+"%' ";
			sqlCount=sqlCount+" and o.`code` like '%"+code+"%' ";
		}
		if (name!=null&&StringUtils.isNotBlank(name)) {
			sql=sql+" and o.`name` like '%"+name+"%' ";
			sqlCount=sqlCount+" and o.`name` like '%"+name+"%' ";
		}
		if (start!=null&&limit!=null) {
			sql=sql+" limit "+start+" ,"+limit;
		}
		RowMapper<ProjectBranchDTO> rowMapper=new BeanPropertyRowMapper<ProjectBranchDTO>(ProjectBranchDTO.class);
		List<ProjectBranchDTO> list= jdbcTemplate.query(sql,rowMapper);
		Integer count=jdbcTemplate.queryForObject(sqlCount,Integer.class);
		PageBean<ProjectBranchDTO> pageBean=new PageBean<>(start,limit,count,list);
		result.setResult(pageBean);
		return result;
	}
}
