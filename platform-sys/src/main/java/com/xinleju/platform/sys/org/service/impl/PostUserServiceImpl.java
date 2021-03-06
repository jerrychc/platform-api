package com.xinleju.platform.sys.org.service.impl;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.flow.dto.AcDto;
import com.xinleju.platform.sys.org.dao.PostUserDao;
import com.xinleju.platform.sys.org.dao.StandardRoleDao;
import com.xinleju.platform.sys.org.dto.FlowAcPostDto;
import com.xinleju.platform.sys.org.dto.FlowPostParticipantDto;
import com.xinleju.platform.sys.org.entity.PostUser;
import com.xinleju.platform.sys.org.service.PostUserService;
import com.xinleju.platform.sys.org.vo.CommonRolePostUserVo;
import com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 *
 *
 */

@Service
public class PostUserServiceImpl extends BaseServiceImpl<String, PostUser> implements PostUserService {
	private Logger logger = Logger.getLogger (PostUserServiceImpl.class);
	@Autowired
	private PostUserDao postUserDao;
	@Autowired
	private StandardRoleDao roleDao;

	/**
	 * 批量保存post_user和role_user
	 *
	 * @param list
	 */
	@Override
	public Integer savePostUserAndRoleUser(List<Map<String, Object>> list) throws Exception {
		return postUserDao.savePostUserAndRoleUser(list);
	}

	@Override
	public Integer delPostUserAndRoleUser(String userId) throws Exception {
		return postUserDao.delPostUserAndRoleUser(userId);
	}

	/**
	 * 设置主岗
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer setDefaultPost(Map<String, Object> map) throws Exception {
		return postUserDao.setDefaultPost(map);
	}

	/*
	 * 获取流程中审批人 (non-Javadoc)
	 * 
	 * @see
	 * com.xinleju.platform.sys.org.service.PostUserService#getFlowPostData(java
	 * .util.Map, java.util.List)
	 */
	@Override
	public List<FlowAcPostDto> getFlowPostData(Map<String, Object> bussinesInfo, List<AcDto> flowAcInfo)
			throws Exception {
		// participantType="组织机构类型: 1: ,2: 岗位,3:角色,4:相对参与人 ，5:通用角色
		// participantScope="角色参与者计算范围: 11:指定人员，12:表单人员 ;21:指定岗位，22:表单岗位
		// ;31:指定角色（逻辑表示），311:集团，312：本公司，313：本部部门，
		// 314：本项目，315:本分期 ，316:指定机构 ，317:表单机构,40
		// 发起人,41：发起人直接领导，42：发起人部门领导，43：发起人一级部门领导，44：上一环节审批人顶级部门领导" ，51：通用角色值
		// TODO Auto-generated method stub

		logger.info ("人员转化前参数bussinesInfo="+bussinesInfo);
		List<FlowAcPostDto> flowAcPostDtos = new ArrayList<FlowAcPostDto>();
		// 获取发起人; //start_user_id
		String start_user_id = (String) bussinesInfo.get("start_user_id");
		// 项目; //flow_business_project_id
		String flow_business_project_id = (String) bussinesInfo.get("flow_business_project_id");
		// 项目分期; //flow_business_project_branch_id
		String flow_business_project_branch_id = (String) bussinesInfo.get("flow_business_project_branch_id");
		// 公司; //flow_business_company_id
		String flow_business_company_id = (String) bussinesInfo.get("flow_business_company_id");
		// 部门; //flow_business_dept_id
		String flow_business_dept_id = (String) bussinesInfo.get("flow_business_dept_id");
		List<FlowPostParticipantDto> flowPostParticipantDtoDefualt = null;
		//如果业务系统之传递了分期，没有传递项目，我们可以根据分期查找出对应的项目
		if(StringUtils.isNotBlank(flow_business_project_branch_id) && StringUtils.isBlank(flow_business_project_id)){
			//根据分期查找出对应的项目
			flow_business_project_id=postUserDao.getProjectIdByProjectBranchId(flow_business_project_branch_id);

		}



		logger.info ("人员转化前参数flowAcInfo=="+JacksonUtils.toJson (flowAcInfo));
		// 处理环节
		for (AcDto acDto : flowAcInfo) {
			FlowAcPostDto flowAcPostDto = new FlowAcPostDto();
			flowAcPostDto.setId(acDto.getId());
			String pareticipant = acDto.getParticipant();
			logger.info ("人员转化前参数acDto.getParticipant()=="+acDto.getParticipant ());
			if (StringUtils.isEmpty(pareticipant)) {
				continue;
			}

			String actype = acDto.getAcType();
			String postIsNull = acDto.getPostIsNull();//岗位为空策略
			if (flowPostParticipantDtoDefualt == null) {
				List<FlowPostParticipantDto> flowPostParticipantDtos = new ArrayList<FlowPostParticipantDto>();
				Map paramMap = new HashMap();
				paramMap.put("person_id", start_user_id);
				List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao
						.getFlowUserOrgnazationPostUserVos(paramMap);
				OrgnazationPostUserVo userDefualt = null;
				OrgnazationPostUserVo userCompany = null;
				OrgnazationPostUserVo userDept = null;
				OrgnazationPostUserVo userBranch = null;
				OrgnazationPostUserVo userGroup = null;
				for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
					if (vo.getIsDefault() != null && "1".contentEquals(vo.getIsDefault())) {
						userDefualt = vo;
					}
					if ("branch".equals(vo.getOrgType()) && vo.getOrgId().equals(flow_business_project_branch_id)) {
						userBranch = vo;
					}
					if ("group".equals(vo.getOrgType()) && vo.getOrgId().equals(flow_business_project_id)) {
						userGroup = vo;
					}

					if ("dept".equals(vo.getOrgType()) && vo.getOrgId().equals(flow_business_dept_id)) {
						userDept = vo;
					}
					if ("company".equals(vo.getOrgType()) && vo.getOrgId().equals(flow_business_company_id)) {
						userCompany = vo;
					}
				}
				if (userBranch != null) {
					FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
							.fromJson(JacksonUtils.toJson(userBranch), FlowPostParticipantDto.class);
					flowPostParticipantDtos.add(flowPostParticipantDto);
				} else if (userGroup != null) {
					FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
							.fromJson(JacksonUtils.toJson(userGroup), FlowPostParticipantDto.class);
					flowPostParticipantDtos.add(flowPostParticipantDto);
				} else if (userDept != null) {
					FlowPostParticipantDto flowPostParticipantDto = JacksonUtils.fromJson(JacksonUtils.toJson(userDept),
							FlowPostParticipantDto.class);
					flowPostParticipantDtos.add(flowPostParticipantDto);
				} else if (userCompany != null) {
					FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
							.fromJson(JacksonUtils.toJson(userCompany), FlowPostParticipantDto.class);
					flowPostParticipantDtos.add(flowPostParticipantDto);
				}

				if (userDefualt != null && (flowPostParticipantDtos == null || flowPostParticipantDtos.size() == 0
						|| (flowPostParticipantDtos.get(0).getOrgId()).equals(userDefualt.getOrgId()))) {
					FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
							.fromJson(JacksonUtils.toJson(userDefualt), FlowPostParticipantDto.class);
					flowPostParticipantDtos.clear();
					flowPostParticipantDtos.add(flowPostParticipantDto);
				}
				//无岗人员返回其所属组织
				if(CollectionUtils.isEmpty(flowPostParticipantDtos)){
					paramMap.put("uIds",new String[]{start_user_id});
					orgnazationPostUserVos = postUserDao.selectOrgUserById(paramMap);
					for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
						FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
								.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
						flowPostParticipantDtos.add(flowPostParticipantDto);
					}
				}

				flowAcPostDto.setFlowPostParticipantDtos(flowPostParticipantDtos);
				flowPostParticipantDtoDefualt = flowPostParticipantDtos;
				flowAcPostDtos.add(flowAcPostDto);
				continue;
			}

			// 获取数据paramValue
			List<FlowPostParticipantDto> flowPostParticipantDtos = new ArrayList<FlowPostParticipantDto>();
			int noPost=0;
			if (StringUtils.isNotBlank(pareticipant) && !"[]".equals(pareticipant)) {
				logger.info("人员转化前参数的pareticipant======"+pareticipant);
				List<Map> list = JacksonUtils.fromJson(pareticipant, ArrayList.class, Map.class);
				for (Map map : list) {
					String participantId = (String) map.get("participantId");
					String participantType = (String) map.get("participantType");
					String participantScope = (String) map.get("participantScope");

					//人员
					if ("1".equals(participantType)) {

						//指定人员
						if ("11".equals(participantScope)) {
							// 指定人员
							String participant = (String) map.get("participantId");
							String paramValue = (String) map.get("paramValue");
							Map paramMap = new HashMap();
							paramMap.put("person_id", participant);
							List<OrgnazationPostUserVo> orgnazationPostUserVos = null;
							//获取指定组织用户  add by gyh 20171106
							if(StringUtils.isNotBlank(paramValue)){
								paramMap.put("post_id", paramValue);
								orgnazationPostUserVos = postUserDao.getFlowUserDefualtOrgnazationPostUserVos(paramMap);
							}else{
								/*paramMap.put("uIds",new String[]{participant});
								orgnazationPostUserVos = postUserDao.selectOrgUserById(paramMap);*/
								//修改指定人计算规则 add by gyh 2018-02-02
								paramMap.put("userId",participant);
								paramMap.put("deptId",flow_business_dept_id);
								paramMap.put("companyId",flow_business_company_id);
								OrgnazationPostUserVo vo = getUserPostNew(paramMap);
								if(vo != null){
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}
							}
							if(CollectionUtils.isNotEmpty(orgnazationPostUserVos)){
								for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}
							}
							//表单人员
						} else if ("12".equals(participantScope)) {
							// 表单人员
							// TODO:
						//	String key = (String) map.get("paramValue");
							String key = (String) map.get("participantId");
						//	"participantId" -> "start_user_name"
							if (StringUtils.isNotBlank(key)) {
								String userId = (String) bussinesInfo.get(key);
								if (StringUtils.isNotBlank(userId)) {
									/*String[] personIds = relationPersonIds.split(",");
									for (String p : personIds) {
										Map paramMap = new HashMap();
										paramMap.put("person_id", p);
										List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao
												.getFlowUserDefualtOrgnazationPostUserVosByUsername(paramMap);

									//	List<OrgnazationPostUserVo> orgnazationPostUserVos = getUserPostNew(paramMap);


										for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
											FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
													.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
											flowPostParticipantDtos.add(flowPostParticipantDto);
										}

									}*/
									Map paramMap = new HashMap();
									String deptId = (String) bussinesInfo.get("flow_business_dept_id");
									String companyId = (String) bussinesInfo.get("flow_business_company_id");
									paramMap.put("deptId", deptId);
									paramMap.put("companyId", companyId);
									paramMap.put("userId",userId);
									OrgnazationPostUserVo vo = this.getUserPostNew(paramMap);
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}
							}
						}
					}

					//岗位
					if ("2".equals(participantType)) {

						//指定岗位
						if ("21".equals(participantScope)) {
							// 指定人员
							String paramValue = (String) map.get("participantId");
							Map paramMap = new HashMap();
							paramMap.put("post_id", paramValue);
							List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao
									.getFlowPostUserOrgnazationPostUserVos(paramMap);
							for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
								FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
										.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
								flowPostParticipantDtos.add(flowPostParticipantDto);
							}

							//表单岗位
						} else if ("22".equals(participantScope)) {
							// 表单岗位
							// TODO:
							String key = (String) map.get("paramValue");
							if (StringUtils.isNotBlank(key)) {
								String relationPostIds = (String) bussinesInfo.get(key);
								if (StringUtils.isNotBlank(relationPostIds)) {
									String[] postIds = relationPostIds.split(",");
									for (String p : postIds) {
										Map paramMap = new HashMap();
										paramMap.put("post_id", p);
										List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao
												.getFlowPostUserOrgnazationPostUserVos(paramMap);
										for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
											FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
													.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
											flowPostParticipantDtos.add(flowPostParticipantDto);
										}
									}
								}
							}
						}
					}

					//标准岗位
					if ("3".equals(participantType)) {

						//本集团
						if ("311".equals(participantScope)) {
							Map paramMap = new HashMap();
							paramMap.put("role_id", participantId);
							List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao
									.getFlowJTOrgnazationPostUserVos(paramMap);
							if (orgnazationPostUserVos != null && orgnazationPostUserVos.size() > 0) {
								for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}
							} else {

								paramMap.put("orgId", null);
								paramMap.put("orgType", null);
								orgnazationPostUserVos = postUserDao.getFlowJTOrgnazationPostVos(paramMap);
								for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}
							}

							//本公司
						} else if ("312".equals(participantScope)) {
							//获取公司角色下 的岗位
							Map paramMap = new HashMap();
							paramMap.put("role_id", participantId);
							paramMap.put("company_id", flow_business_company_id);
							//经办公司--update by gyh 20171012
							//一级查找范围
							List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao.getFlowCompanyOrgnazationPostUserVos(paramMap);
							//二级查找范围
							if(CollectionUtils.isEmpty(orgnazationPostUserVos)){
								orgnazationPostUserVos = postUserDao.getFlowCompanyOrgnazationPostUserVos_2(paramMap);
							}
							//三级查找范围
							if(CollectionUtils.isEmpty(orgnazationPostUserVos)){
								List<String> comIds = postUserDao.getUpCompanyIds(paramMap);
								for (String comId : comIds) {
									paramMap.put("company_id", comId);
									orgnazationPostUserVos = postUserDao.getFlowCompanyOrgnazationPostUserVos(paramMap);
									if(!CollectionUtils.isEmpty(orgnazationPostUserVos)){
										break;
									}
								}
							}
							for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
								FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
										.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
								flowPostParticipantDtos.add(flowPostParticipantDto);
							}
							/*List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao
									.getFlowCompanyOrgnazationPostUserVos(paramMap);
							if (orgnazationPostUserVos != null && orgnazationPostUserVos.size() > 0) {
								for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}
							} else {
								paramMap.put("orgId", flow_business_company_id);
								paramMap.put("orgType", "company");
								orgnazationPostUserVos = postUserDao.getFlowJTOrgnazationPostVos(paramMap);
								if(orgnazationPostUserVos != null && orgnazationPostUserVos.size() > 0){
									for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
										FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
												.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
										flowPostParticipantDtos.add(flowPostParticipantDto);
									}
								}else{
									//公司下岗位也没有
									Map paramMap_p = new HashMap();
									paramMap_p.put("role_id", participantId);
									paramMap_p.put("company_id", flow_business_company_id);
									 orgnazationPostUserVos = postUserDao
											.getFlowCompanyUpOrgnazationPostUserVos(paramMap_p);
									 if(orgnazationPostUserVos != null && orgnazationPostUserVos.size() > 0){
										 for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
											 FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
													 .fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
											 flowPostParticipantDtos.add(flowPostParticipantDto);
										 }
									 }else{

										 //delete by zhangdaoqiang 如果返回此虚拟岗位，流程运行时岗位为空无法判断，另外此虚拟岗也不应该保存在流程实例相关表中！！！
//										 if("3".equals(postIsNull)){//岗位为空策略为：允许发起，跳过，并显示该行 时，显示一条空岗位
//											 StandardRole r= roleDao.getObjectById(participantId);
//											 FlowPostParticipantDto flowPostParticipantDto=new FlowPostParticipantDto();
//											 flowPostParticipantDto.setPostId("noPost"+noPost++);
//											 flowPostParticipantDto.setPostName("无此岗位/"+r.getName());
//											 flowPostParticipantDto.setPostPrefixName("无此岗位/"+r.getName());
//											 flowPostParticipantDtos.add(flowPostParticipantDto);
//										 }
									 }
								}
							}*/

							//本部门
						} else if ("313".equals(participantScope)) {
							Map paramMap = new HashMap();
							paramMap.put("role_id", participantId);
							paramMap.put("dept_id", flow_business_dept_id);
							//经办部门及其下级的部门岗位--update by gyh 20171012
							List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao.getFlowDeptOrgnazationPostUserVos(paramMap);
							if(orgnazationPostUserVos!=null && orgnazationPostUserVos.size()>0 ){
								OrgnazationPostUserVo firstVo = orgnazationPostUserVos.get(0);
								String orgId = firstVo.getOrgId();//第一个岗位所属组织
								if(orgId.equals(flow_business_dept_id)){//经办部门岗
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils.fromJson(JacksonUtils.toJson(firstVo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}else{//经办部门下级部门岗位--取全部部门岗
									for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
										FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
												.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
										flowPostParticipantDtos.add(flowPostParticipantDto);
									}
								}
							}else{
								paramMap.put("orgType", "dept");
								//经办部门上级级部门岗位--取最靠近经办部门的部门岗
								orgnazationPostUserVos = postUserDao.getFlowOrgnazationPostUserVos_up(paramMap);
								for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}
							}
							/*List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao
									.getFlowDeptOrgnazationPostUserVos(paramMap);
							if (orgnazationPostUserVos != null && orgnazationPostUserVos.size() > 0) {
								String orgId = null;
								for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
									if (orgId != null && !orgId.equals(vo.getOrgId()))
										break;
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
									orgId = vo.getOrgId();
								}

							} else {
								paramMap.put("orgId", flow_business_dept_id);
								paramMap.put("orgType", "dept");
								orgnazationPostUserVos = postUserDao.getFlowJTOrgnazationPostVos(paramMap);
								if(orgnazationPostUserVos != null && orgnazationPostUserVos.size() > 0){
									for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
										FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
												.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
										flowPostParticipantDtos.add(flowPostParticipantDto);
									}
								}else{
									//部门下没有岗位
								}
							}*/


							//314本项目 315本分期
						} else if ("314".equals(participantScope) || "315".equals(participantScope)) {
							String project_id = null;
							if ("314".equals(participantScope)) {
								project_id = flow_business_project_id;
							} else if ("315".equals(participantScope)) {
								project_id = flow_business_project_branch_id;
							}
							Map paramMap = new HashMap();
							paramMap.put("role_id", participantId);
							paramMap.put("project_id", project_id);
							List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao
									.getFlowPjectOrgnazationPostUserVos(paramMap);
							if(orgnazationPostUserVos!=null && orgnazationPostUserVos.size()>0){
								for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}
							} else {
								paramMap.put("orgId", project_id);
								paramMap.put("orgType", "project");
								orgnazationPostUserVos = postUserDao.getFlowJTOrgnazationPostVos(paramMap);
								for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}
							}

							//指定组织
						} else if ("316".equals(participantScope)) {
							// 指定组织
							String paramValue = (String) map.get("paramValue");
							Map paramMap = new HashMap();
							paramMap.put("role_id", participantId);
							paramMap.put("org_id", paramValue);
							List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao
									.getFlowDirectOrgnazationPostUserVos(paramMap);
							for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
								FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
										.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
								flowPostParticipantDtos.add(flowPostParticipantDto);
							}

							//表单组织
						} else if ("317".equals(participantScope)) {
							// 表单传递组织
							// TODO:
						}
					}

					//相对参与人
					if ("4".equals(participantType)) {

						//发起人
						if ("40".equals(participantScope)) {
							for (FlowPostParticipantDto flowPostParticipantDto : flowPostParticipantDtoDefualt) {
								flowPostParticipantDtos.add(flowPostParticipantDto);
							}

							//发起人直接领导
						} else if ("41".equals(participantScope)) {
							Map paramMap = new HashMap();
							paramMap.put("start_id", start_user_id);
							/*paramMap.put("startPost", flowPostParticipantDtoDefualt.get(0).getPostId());
							List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao.getFlowStartUserOrgnazationPostUserVos(paramMap);
							//是否有上级领导岗位
							if(CollectionUtils.isEmpty(orgnazationPostUserVos)){
								orgnazationPostUserVos = postUserDao.getleaderPost(paramMap);
							}
							//当岗位上的领导岗为空时，查询岗位所有组织上的领导岗位 add by zhangdaoqiang
							if(CollectionUtils.isEmpty(orgnazationPostUserVos)) {
								orgnazationPostUserVos = postUserDao.getLeaderPostOnOrgWhenLeaderOnPostIsNull(paramMap);
							}*/


							//update by gyh 20171013
							paramMap.put("dept_id", flow_business_dept_id);
							//一级查找范围：经办部门(主岗领导岗>兼职岗领导岗>经办部门领导岗) > 经办部门下级组织(主岗领导岗>兼职岗领导岗>组织领导岗)
							List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao.getDeptDownUserPostLeaderAndDeptDownLeader(paramMap);
							if(CollectionUtils.isEmpty(orgnazationPostUserVos)){
								//二级查找范围：发起人默认岗位领导岗>发起人默认岗位所在组织领导岗位>发起人所在组织领导岗位
								orgnazationPostUserVos = postUserDao.getDeftPostLeaderOrDeftPostOrgLeaderOrUserOrgLeader(paramMap);
							}
							for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
								FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
										.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
								flowPostParticipantDtos.add(flowPostParticipantDto);
							}

							//发起人顶级部门领导
						} else if ("42".equals(participantScope)) {
							Map paramMap = new HashMap();
							paramMap.put("start_id", start_user_id);
							//发起岗
							//paramMap.put("startPost", flowPostParticipantDtoDefualt.get(0).getPostId());
							//经办部门--顶级部门领导。update by gyh 20171013
							paramMap.put("dept_id", flow_business_dept_id);
							List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao
									.getFlowStartUserDeptOrgnazationPostUserVos(paramMap);
							for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
								FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
										.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
								flowPostParticipantDtos.add(flowPostParticipantDto);
							}

							//上一环节审批人直接领导
						} else if ("43".equals(participantScope)) {
							Map paramMap = new HashMap();
							paramMap.put("start_id", start_user_id);
							String leaderId = postUserDao.getFlowStartUserDeptOrgnazationLeaderId(paramMap);
							if (StringUtils.isNotBlank(leaderId)) {
								paramMap = new HashMap();
								paramMap.put("post_id", leaderId);
								List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao
										.getFlowPostUserOrgnazationPostUserVos(paramMap);
								for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}
							}
						}

						//44:上一环节审批人顶级部门领导
					}

					//角色
					if ("5".equals(participantType)) {
						if ("51".equals(participantScope)) {
							List<String> commonRoleScopeIds = new ArrayList<String>();
							if (StringUtils.isNotBlank(flow_business_project_id)) {
								commonRoleScopeIds.add(flow_business_project_id);
							}
							if (StringUtils.isNotBlank(flow_business_project_branch_id)) {
								commonRoleScopeIds.add(flow_business_project_branch_id);
							}
							if (StringUtils.isNotBlank(flow_business_company_id)) {
								commonRoleScopeIds.add(flow_business_company_id);
							}
							if (StringUtils.isNotBlank(flow_business_dept_id)) {
								commonRoleScopeIds.add(flow_business_dept_id);
							}
							String paramValue = (String) map.get("paramValue");
							Map paramMap = new HashMap();
							paramMap.put("commonRoleId", participantId);
							paramMap.put("start_user_id", start_user_id);
							if (commonRoleScopeIds != null && commonRoleScopeIds.size() > 0) {
								paramMap.put("commonRoleScopeIds", commonRoleScopeIds);
							}
							//增加无管辖范围的角色对象
							List<CommonRolePostUserVo> commonRolePostUserVos = postUserDao
									.getFlowCommonRolePostUserVos(paramMap);
							List<String> onlyUserIds = new ArrayList<String>();
							List<String> userIds = new ArrayList<String>();
							List<String> userRelationPostIds = new ArrayList<String>();
							List<String> postIds = new ArrayList<String>();
							for (CommonRolePostUserVo vo : commonRolePostUserVos) {
								if (StringUtils.isNotEmpty(vo.getUserId())) {
									//获取指定组织用户  add by gyh 20171106
									if (StringUtils.isBlank(vo.getPostId())) {
										onlyUserIds.add(vo.getUserId());
										continue;
									}else{
										userIds.add(vo.getUserId());
										userRelationPostIds.add(vo.getPostId());
										continue;
									}
								}
								if (StringUtils.isNotEmpty(vo.getPostId())) {
									postIds.add(vo.getPostId());
								}
							}
							//获取指定组织用户  add by gyh 20171106
							if(CollectionUtils.isNotEmpty(onlyUserIds)){
								/*paramMap.put("uIds",onlyUserIds);
								List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao.selectOrgUserById(paramMap);
								for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}*/
								paramMap.put("deptId",flow_business_dept_id);
								paramMap.put("companyId",flow_business_company_id);
								for (int i = 0; i < onlyUserIds.size(); i++) {
									//修改指定人计算规则 add by gyh 2018-02-02
									paramMap.put("userId",onlyUserIds.get(i));
									OrgnazationPostUserVo vo = getUserPostNew(paramMap);
									if(vo != null){
										FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
												.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
										flowPostParticipantDtos.add(flowPostParticipantDto);
									}
								}
							}
							// 获取用户下的岗位人员
							if (userIds != null && userIds.size() > 0) {
								Map userparamMap = new HashMap();
								userparamMap.put("userIds", userIds);
								userparamMap.put("userRelationPostIds", userRelationPostIds);
								List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao
										.getFlowCommonRolePostUser_UserVos(userparamMap);
								for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}
							}

							// 获取 岗位下的人员

							if (postIds != null && postIds.size() > 0) {
								Map postparamMap = new HashMap();
								postparamMap.put("postIds", postIds);
								List<OrgnazationPostUserVo> orgnazationPostUserVos = postUserDao
										.getFlowCommonRolePostUser_PostVos(postparamMap);
								for (OrgnazationPostUserVo vo : orgnazationPostUserVos) {
									FlowPostParticipantDto flowPostParticipantDto = JacksonUtils
											.fromJson(JacksonUtils.toJson(vo), FlowPostParticipantDto.class);
									flowPostParticipantDtos.add(flowPostParticipantDto);
								}
							}

						}
					}

				}
				flowAcPostDto.setFlowPostParticipantDtos(flowPostParticipantDtos);
			} else {
				flowAcPostDto.setFlowPostParticipantDtos(flowPostParticipantDtos);
			}
			flowAcPostDtos.add(flowAcPostDto);
		}
		logger.info("人员转化前参数的flowAcPostDtos 返回结果======"+JacksonUtils.toJson (flowAcPostDtos));
		return flowAcPostDtos;
	}

	/**
	 * 流程配置人员，算人新规则
	 * 1、现找经办组织对应的岗位
	 计算经办部门下的岗位，即当前经办部门下是否有岗位，如果没有，再找上级部门，直到直属公司，在直属公司下的所有部门中查找（不包括子公司）。
	 如果上述经办部门树中还是没有，就到经办公司下的所有部门中进行查找（不包括子公司）
	 2、如果上述还是没有找到经办组织对应的岗位， 就取主岗，如果主岗也没有，就去人员落位的组织
	 *  userId 用户id
	 *  deptId 经办部门id
	 *  companyId 经办公司id
	 * @return
	 */
	public  OrgnazationPostUserVo getUserPostNew(Map<String,Object> map){
		 OrgnazationPostUserVo res = null;
		//经办部门
		if(map.get("deptId")!= null && StringUtils.isNotBlank(map.get("deptId").toString())){
			res = postUserDao.selectUpDeptUserPost(map);
			if(res != null){
				return res;
			}
			//经办在部门直属公司
			String directComId = postUserDao.selectDriectComId(map);
			map.put("directComId",directComId);
			//直属公司及以下部门
			res = postUserDao.selectDownDeptUserPost(map);
			if(res != null){
				return res;
			}
		}
		if(map.get("companyId")!= null && StringUtils.isNotBlank(map.get("companyId").toString())){
			map.put("directComId",map.get("companyId"));
			//经办公司及以下部门
			res = postUserDao.selectDownDeptUserPost(map);
			if(res != null){
				return res;
			}
		}
		//用户主岗
		res = postUserDao.selectUserMainPost(map);
		if(res != null){
			return res;
		}
		map.put("uIds",new String[]{map.get("userId").toString()});
		//用户所属组织
		List<OrgnazationPostUserVo> list = postUserDao.selectOrgUserById(map);
		if(CollectionUtils.isNotEmpty(list)){
			res = list.get(0);
		}
		return res;
	}

	// 批量保存post_user和role_user
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer savePostUserBatch(Map<String, Object> param) throws Exception {
		try {
			// 先删除之前的再添加新增
			String userId = param.get("userId").toString();
			postUserDao.delPostUserAndRoleUser(userId);
			List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
			if (null == list || list.size() == 0) {
				return 1;
			}
			// 查询是否存在主岗
			Integer isdefault = postUserDao.selectDefault(param);
			boolean ifDefault = isdefault != null && isdefault > 0 ? true : false;// ture有主岗，false无主岗
			Integer newFrist = null;
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				if (!map.containsKey("id") || map.get("id") == null || StringUtils.isBlank(map.get("id").toString())) {
					map.put("id", IDGenerator.getUUID());
					newFrist = i;
				}
				if (map.get("isDefault") != null && "1".equals(map.get("isDefault").toString())) {
					ifDefault = true;
				}
			}
			if (!ifDefault) {// 页面第一个选中的设为主岗
				if (newFrist != null) {
					Map<String, Object> map = list.get(newFrist);
					map.put("isDefault", 1);
				}
			}
			postUserDao.savePostUserAndRoleUser(list);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
