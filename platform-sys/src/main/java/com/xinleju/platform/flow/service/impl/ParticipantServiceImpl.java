package com.xinleju.platform.flow.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.flow.dao.AcDao;
import com.xinleju.platform.flow.dao.FlDao;
import com.xinleju.platform.flow.dao.InstanceAccessibleDao;
import com.xinleju.platform.flow.dao.InstanceDao;
import com.xinleju.platform.flow.dao.InstanceVariableDao;
import com.xinleju.platform.flow.dao.ParticipantDao;
import com.xinleju.platform.flow.dto.AcDto;
import com.xinleju.platform.flow.dto.BatchModifyReaderDto;
import com.xinleju.platform.flow.dto.FlDto;
import com.xinleju.platform.flow.dto.ParticipantDto;
import com.xinleju.platform.flow.dto.PostDto;
import com.xinleju.platform.flow.dto.UserDto;
import com.xinleju.platform.flow.entity.Instance;
import com.xinleju.platform.flow.entity.InstanceAccessible;
import com.xinleju.platform.flow.entity.InstanceVariable;
import com.xinleju.platform.flow.entity.Participant;
import com.xinleju.platform.flow.exception.FlowException;
import com.xinleju.platform.flow.service.ParticipantService;
import com.xinleju.platform.flow.utils.FlowApproverUtils;
import com.xinleju.platform.sys.org.dto.FlowAcPostDto;
import com.xinleju.platform.sys.org.dto.FlowPostParticipantDto;
import com.xinleju.platform.sys.org.dto.service.PostDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 * 
 */

@Service
public class ParticipantServiceImpl extends  BaseServiceImpl<String,Participant> implements ParticipantService{
	private static Logger log = Logger.getLogger(ParticipantServiceImpl.class);

	@Autowired
	private ParticipantDao participantDao;

	@Autowired
	private AcDao acDao;

	@Autowired
	private FlDao flDao;

	@Autowired
	private InstanceDao instanceDao;

	@Autowired
	private InstanceVariableDao instanceVariableDao;

	@Autowired
	private InstanceAccessibleDao instanceAccessibleDao;
	
	@Autowired
	private PostDtoServiceCustomer postDtoServiceCustomer;
	@Override
	public void batchModifyReader(List<Participant> readerList) throws Exception {
		if(readerList==null || readerList.size()==0){
			return;
		}
		Participant firstReader = readerList.get(0);
		String action = firstReader.getAcId();
		for(Participant reader : readerList){
			if("3".equals(action)){//3-重置,需要先删除原有的可阅读人的数据 
				participantDao.deleteReaderDataByFlId(reader.getFlId());
			}
			reader.setAcId(null);
			reader.setId(IDGenerator.getUUID());
			int result = this.save(reader);
		}
		//2-删除暂时不做
	}

	@Override
	public void replaceFlowParticipant(List<ParticipantDto> approverList) throws Exception {
		if(approverList==null || approverList.size()==0){
			return;
		}
		for(ParticipantDto participantDto : approverList){
			//oldParticipantId:oldParticipantId, oldParticipantType: oldType, flId:item.id, 
			//participantId:replacerId, participantType:type, participantScope:scope
			int result = participantDao.replaceFlowParticipant(participantDto);
		}
		
	}

	@Override
	public List<ParticipantDto> queryFlowReaderList(Map<String, Object> paramMap) {
		String flowIds = (String)paramMap.get("flowIds");
		if(flowIds!=null && flowIds.length()>10){
			String idArray[] = flowIds.split(",");
			paramMap.put("flowIds", idArray);
		}
		
		return participantDao.queryFlowReaderList(paramMap);
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void deleteReaderByFormData(BatchModifyReaderDto readerDto) throws Exception {
		String flowIdText = readerDto.getFlowIdText();
		String synInstance = readerDto.getSynInstance();
		String dataIdArray[] = flowIdText.split(",");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idArray", dataIdArray);
		participantDao.deleteReaderDataByParamMap(params);
//		List<String> flList=flDao.queryUpdateFlDataList(params);
//		toDealInstanceDel(synInstance,flList);
	}

	private void toDealInstanceDel(String synInstance, List<String> flList) throws Exception {
		List<String> oldInstanceIds=new ArrayList<String>();
		List<InstanceAccessible>  accessibleListAll=new ArrayList<InstanceAccessible>();
		List<InstanceAccessible>  accessibleList=new ArrayList<InstanceAccessible>();
		if(synInstance!=null && "1".equals(synInstance) && flList!=null && flList.size()>0){
			Map<String,List<Instance>> instanceMap=this.getAllInstanceByFlId(flList);
			Map<String,List<Participant>> participantMap=this.getAllParticipantByFlId(flList);
			if(flList!=null && flList.size()>0){
				for(int i=0;i<flList.size();i++){
					String flId=flList.get(i);
					List<AcDto> accessibleAcDtos = new ArrayList<AcDto>();
					List<Instance> instanceList=instanceMap.get(flId);
					List<Participant> participantList=participantMap.get(flId);
					if(instanceList!=null && instanceList.size()>0){
						Map<String,Map<String, Object>> instanceVariableMap=this.getAllInstanceVariableByFlId(flId);
						for(int m=0;m<instanceList.size();m++){
							Instance instance=instanceList.get(m);
							Map<String, Object> instanceVariable=instanceVariableMap.get(instance.getId());
							List<ParticipantDto> participantListNew=new ArrayList<ParticipantDto>();
							if(participantList!=null && participantList.size()>0){
								for(int s=0;s<participantList.size();s++){
									ParticipantDto accessible = new ParticipantDto();
									BeanUtils.copyProperties(participantList.get(s), accessible);
									participantListNew.add(accessible);
								}
								AcDto accessibleacDto = new AcDto();
								accessibleacDto.setId(IDGenerator.getUUID());
								accessibleacDto.setParticipant(JacksonUtils.toJson(participantListNew));
								accessibleAcDtos.add(accessibleacDto);
								
								String accessiblePosts = this.loadAccessiblePosts(accessibleAcDtos,instanceVariable); // 可阅人岗位信息
								accessibleList=generateAccessibleData(accessiblePosts,instance);
								instanceAccessibleDao.deleteByInstanceId(instance.getId());
								instanceAccessibleDao.saveBatch(accessibleList);
							}
						}
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("flId", flId);
						List<Instance> instanceListOld=instanceDao.getOldVersionInstanceByCurrentFlId(params);
						if(instanceListOld!=null && instanceListOld.size()>0){
							for(int k=0;k<instanceListOld.size();k++){
								Instance instanceOld=instanceListOld.get(k);
								accessibleListAll.addAll(this.changeToNewAccessible(instanceOld,accessibleList));
								oldInstanceIds.add(instanceOld.getId());
							}
						}
					}
				}
				//批量设置旧版本可阅人为当前生效版本可阅人
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("instanceIds", oldInstanceIds);
				instanceAccessibleDao.deleteReaderDataByParamMap(paramMap);
				instanceAccessibleDao.saveBatch(accessibleListAll);
			}
		}
	}

	private List<InstanceAccessible> changeToNewAccessible(Instance instance,List<InstanceAccessible> accessibleList) {
		if(accessibleList!=null && accessibleList.size()>0){
			for(int i=0;i<accessibleList.size();i++){
				accessibleList.get(i).setId(IDGenerator.getUUID());
				accessibleList.get(i).setFiId(instance.getId());
			}
		}
		return accessibleList;
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void addResetReaderFormData(BatchModifyReaderDto readerDto) throws Exception {
		String operateType = readerDto.getOperateType();
		String synInstance = readerDto.getSynInstance();
		String flowIdText = readerDto.getFlowIdText();
		String flowIds[] = flowIdText.split(",");
		//System.out.println("\n\n addResetReaderFormData operateType="+operateType+" instanceIdText = "+instanceIdText);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("flowIds", flowIds);
		List<String> flList=flDao.queryAllFlByCode(paramMap);
		paramMap.clear();
		paramMap.put("flowIds", flList);
		if("reset".equals(operateType)){//重新设置需要删除对应的流程实例的可阅读人数据
			participantDao.deleteReaderDataByParamMap(paramMap);
		}
		
		List<ParticipantDto> readerList = readerDto.getPartcipantList();
		for(int idx1 = 0; idx1<flList.size(); idx1++){
			String flowId = flList.get(idx1);
			for(ParticipantDto tempData : readerList){
				Participant reader = new Participant();
				BeanUtils.copyProperties(tempData, reader);
				reader.setFlId(flowId);
				reader.setId(IDGenerator.getUUID());
				reader.setDelflag(false);
				reader.setType("3");
				participantDao.save(reader);
			}
		}
		
		toDealInstance(synInstance, flList);
	}

	private void toDealInstance(String synInstance, List<String> flList) throws Exception {
		if(synInstance!=null && "1".equals(synInstance) && flList!=null && flList.size()>0){
			Map<String,List<Instance>> instanceMap=this.getAllInstanceByFlId(flList);
			Map<String,List<Participant>> participantMap=this.getAllParticipantByFlId(flList);
			if(flList!=null && flList.size()>0){
				for(int i=0;i<flList.size();i++){
					String flId=flList.get(i);
					List<AcDto> accessibleAcDtos = new ArrayList<AcDto>();
					List<Instance> instanceList=instanceMap.get(flId);
					List<Participant> participantList=participantMap.get(flId);
					if(instanceList!=null && instanceList.size()>0){
						Map<String,Map<String, Object>> instanceVariableMap=this.getAllInstanceVariableByFlId(flId);
						for(int m=0;m<instanceList.size();m++){
							Instance instance=instanceList.get(m);
							Map<String, Object> instanceVariable=instanceVariableMap.get(instance.getId());
							List<ParticipantDto> participantListNew=new ArrayList<ParticipantDto>();
							for(int s=0;s<participantList.size();s++){
								ParticipantDto accessible = new ParticipantDto();
								BeanUtils.copyProperties(participantList.get(s), accessible);
								participantListNew.add(accessible);
							}
							AcDto accessibleacDto = new AcDto();
							accessibleacDto.setId(IDGenerator.getUUID());
							accessibleacDto.setParticipant(JacksonUtils.toJson(participantListNew));
							accessibleAcDtos.add(accessibleacDto);
							
							String accessiblePosts = this.loadAccessiblePosts(accessibleAcDtos,instanceVariable); // 可阅人岗位信息
							List<InstanceAccessible>  accessibleList=generateAccessibleData(accessiblePosts,instance);
							instanceAccessibleDao.deleteByInstanceId(instance.getId());
							instanceAccessibleDao.saveBatch(accessibleList);
						}
					}
				}
			}
		}
	}

	private Map<String, Map<String, Object>> getAllInstanceVariableByFlId(String flId) {
		Map<String, Map<String, Object>> resultMap=new HashMap<String,Map<String, Object>>();
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("flId", flId);
		paramMap.put("delflag", "0");
		List<InstanceVariable> instanceVariableList=instanceVariableDao.queryVariableByFlId(paramMap);
		if(instanceVariableList!=null && instanceVariableList.size()>0){
			for(int i=0;i<instanceVariableList.size();i++){
				InstanceVariable instanceVariable=instanceVariableList.get(i);
				if(resultMap.get(instanceVariable.getFiId())!=null){
					resultMap.get(instanceVariable.getFiId()).put(instanceVariable.getName(),instanceVariable.getVal());
				}else{
					Map<String, Object> map=new HashMap<String,Object>();
					map.put(instanceVariable.getName(),instanceVariable.getVal());
					resultMap.put(instanceVariable.getFiId(), map);
				}
			}
		}
		return resultMap;
	}

	private Map<String, List<Participant>> getAllParticipantByFlId(List<String> flList) {
		Map<String,List<Participant>> participantMap=new HashMap<String,List<Participant>>();
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("flList", flList);
		paramMap.put("type", "3");
		List<Participant> instanceList=participantDao.getAllParticipantByFlId(paramMap);
		List<Participant> instanceTemp=null;
		if(instanceList!=null && instanceList.size()>0){
			for(int i=0;i<instanceList.size();i++){
				Participant participant=instanceList.get(i);
				if(participantMap.get(participant.getFlId())!=null){
					participantMap.get(participant.getFlId()).add(participant);
				}else{
					instanceTemp=new ArrayList<Participant>();
					instanceTemp.add(participant);
					participantMap.put(participant.getFlId(), instanceTemp);
				}
			}
		}
		return participantMap;
	}

	private Map<String, List<Instance>> getAllInstanceByFlId(List<String> flList) {
		Map<String,List<Instance>> instanceMap=new HashMap<String,List<Instance>>();
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("flList", flList);
		List<Instance> instanceList=instanceDao.getAllInstanceByFlId(paramMap);
		List<Instance> instanceTemp=null;
		if(instanceList!=null && instanceList.size()>0){
			for(int i=0;i<instanceList.size();i++){
				Instance instance=instanceList.get(i);
				if(instanceMap.get(instance.getFlId())!=null){
					instanceMap.get(instance.getFlId()).add(instance);
				}else{
					instanceTemp=new ArrayList<Instance>();
					instanceTemp.add(instance);
					instanceMap.put(instance.getFlId(), instanceTemp);
				}
			}
		}
		return instanceMap;
	}

	private Map<String, Object> queryVariable(Map<String, Object> paramMap) {
		List<InstanceVariable> instanceVariableList=instanceVariableDao.queryList(paramMap);
		Map<String, Object> returnMap=new HashMap<String, Object>();
		if(instanceVariableList!=null && instanceVariableList.size()>0){
			for(int i=0;i<instanceVariableList.size();i++){
				InstanceVariable instanceVariable=instanceVariableList.get(i);
				returnMap.put(instanceVariable.getName(), instanceVariable.getVal());
			}
		}
		return returnMap;
	}

	private String loadAccessiblePosts(List<AcDto> accessibleAcDtos, Map<String, Object> instanceVariable) {
		String accessiblePosts = "";
		try {
			List<AcDto> readerAcDtos = new ArrayList<AcDto>();
			if(accessibleAcDtos!=null && accessibleAcDtos.size()>0){
				AcDto tempAcDto = accessibleAcDtos.get(0);//模拟的开始节点不需要设置审批人信息
				AcDto startAcDto = new AcDto();
				//模拟一个开始环节,id不能使用已有的，否则会使真正的开始环节的审批人消失了
				startAcDto.setId(IDGenerator.getUUID());
				startAcDto.setAcType("1");
				startAcDto.setParticipant(tempAcDto.getParticipant());
				startAcDto.setCcPerson(null);
				startAcDto.setPosts(null);
				startAcDto.setCsPosts(null);
				startAcDto.setFlowPostParticipantDtos(null);
				startAcDto.setParticipant(accessibleAcDtos.get(0).getParticipant());
				readerAcDtos.add(startAcDto);
				
				AcDto approveAcDto = new AcDto(); //模拟一个审核节点
				approveAcDto.setId(IDGenerator.getUUID());
				approveAcDto.setParticipant(tempAcDto.getParticipant());
				approveAcDto.setApproveTypeId("SH");
				approveAcDto.setAcType("2");
				readerAcDtos.add(approveAcDto);
				
				String accessibleAcDtosJSON2 = JacksonUtils.toJson(readerAcDtos);
				FlowApproverUtils.setPostDtoServiceCustomer(postDtoServiceCustomer);
				log.info("==== 获取可阅人岗位信息 FlowApproverUtils.parsePost() accessibleAcDtosJSON2="+accessibleAcDtosJSON2);
				List<FlowAcPostDto> accessibleResultList = FlowApproverUtils.parsePost(instanceVariable, readerAcDtos);
				if (accessibleResultList != null) {
					for (FlowAcPostDto flowAcPostDto : accessibleResultList) {
						log.info("====  获取可阅人岗位信息 for loop 001 flowAcPostDto="+ JacksonUtils.toJson(flowAcPostDto));
						List<FlowPostParticipantDto> flowPostParticipantDtos = flowAcPostDto.getFlowPostParticipantDtos();
						log.info("====  获取可阅人岗位信息 for loop 002 flowPostParticipantDtos="+ JacksonUtils.toJson(flowPostParticipantDtos));
						if (flowPostParticipantDtos != null && flowPostParticipantDtos.size() > 0) {
							accessiblePosts = processAcPosts(flowPostParticipantDtos);
							log.info("====  获取可阅人岗位信息 for loop 003 accessiblePosts="+ accessiblePosts);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new FlowException("获取可阅人岗位失败！", e);
		}
		return accessiblePosts;
	}
	
	/**
	 * 结构化人员岗位信息
	 * @param flowPostParticipantDtos
	 * @return
	 */
	private static String processAcPosts(List<FlowPostParticipantDto> flowPostParticipantDtos) {
		Map<String, PostDto> postMap = new LinkedHashMap<String, PostDto>();
		for (FlowPostParticipantDto flowPostParticipantDto : flowPostParticipantDtos) {
			Map<String, UserDto> userMap = new HashMap<String, UserDto>();
			PostDto postDto = new PostDto();
			postDto.setId(flowPostParticipantDto.getPostId());
			postDto.setName(flowPostParticipantDto.getPostPrefixName());
			for (FlowPostParticipantDto flowPostParticipantDto1 : flowPostParticipantDtos) {
				if (flowPostParticipantDto1.getPostId().equals(flowPostParticipantDto.getPostId())) {
					UserDto userDto = new UserDto();
					userDto.setId(flowPostParticipantDto1.getUserId());
					userDto.setName(flowPostParticipantDto1.getUserName());
					userMap.put(userDto.getId(), userDto);
				}
			}
			postDto.setUsers(new ArrayList<UserDto>(userMap.values()));
			postMap.put(postDto.getId(), postDto);
		}
		return JacksonUtils.toJson(postMap.values());
	}
	
	private List<InstanceAccessible> generateAccessibleData(String accessiblePosts, Instance instance) throws Exception {
		List<PostDto> postList = JacksonUtils.fromJson(accessiblePosts, ArrayList.class, PostDto.class);
		List<InstanceAccessible> accessibleList = new ArrayList<InstanceAccessible>();
		Set<String> repeatFilter=new HashSet<String>();
		if (postList != null) {
			for (PostDto postDto : postList) {
				List<UserDto> users = postDto.getUsers();
				if(CollectionUtils.isEmpty(users)) {
					continue;
				}
				for (UserDto userDto : users) {
					if(StringUtils.isEmpty(userDto.getId())) {
						continue;
					}
					String[] userIdArr = userDto.getId().split(",");
					String[] userNameArr = userDto.getName().split(",");
					if (userIdArr != null && userIdArr.length > 0 && userNameArr != null && userNameArr.length > 0) {
						for (int i = 0; i < userIdArr.length; i++) {
							if(repeatFilter.contains(userIdArr[i])){
								continue;
							}else{
								repeatFilter.add(userIdArr[i]);
							}
							InstanceAccessible instanceAccessible = new InstanceAccessible();
							instanceAccessible.setId(IDGenerator.getUUID());
							instanceAccessible.setFiId(instance.getId());
							instanceAccessible.setAccessibleId(userIdArr[i]);
							instanceAccessible.setAccessibleName(userNameArr[i]);
							accessibleList.add(instanceAccessible);
						}
					}
				}
			}			
		}

	    return accessibleList;
	}

	@Override
	public List<Participant> queryReplaceParticipantList(Map<String, Object> paramMap) {
		String acIds = (String) paramMap.get("acIds");
		if(StringUtils.isNotBlank(acIds)){
			String[] acIdArr = acIds.split(",");
			List<String> acIdList = Arrays.asList(acIdArr);
			paramMap.remove("acIds");
			paramMap.put("acIdList",acIdList);
		}
		return participantDao.queryReplaceParticipantList(paramMap);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<Participant> saveReplaceApprover(Map<String,Object> userInfo,Map<String, Object> saveReplaceMap) throws Exception {
		String acIds = (String) saveReplaceMap.get("acIds");
		List<String> acIdList = null;
		if(StringUtils.isNotBlank(acIds)){
			String[] acIdArr = acIds.split(",");
			acIdList = Arrays.asList(acIdArr);
		}

		Map<String,Object> newParticipantMap = (Map<String, Object>) saveReplaceMap.get("approverMap");
		Set<String> keySet = newParticipantMap.keySet();
		List<String> participantIdList = new ArrayList<String>();
		participantIdList.addAll(keySet);

		Map<String,Object> queryParam = new HashMap<String,Object>();
		queryParam.put("acIdList",acIdList);
		queryParam.put("participantIdList",participantIdList);

		List<Participant> list = participantDao.queryReplaceParticipantListForUpdate(queryParam);
		Set<String> flIdSet = new HashSet<String>();
		for (Participant oldParticipant:list) {
			String participantId = oldParticipant.getParticipantId();
			Map<String,Object> newParticipant = (Map<String, Object>) newParticipantMap.get(participantId);
			if(newParticipant==null){
				continue;
			}
			String newParticipantId = (String) newParticipant.get("newParticipantId");
			String newParticipantName = (String) newParticipant.get("newParticipantName");
			String newParticipantScope = (String) newParticipant.get("newParticipantScope");
			String newParticipantType = (String) newParticipant.get("newParticipantType");
			String newParamValue = (String) newParticipant.get("newParamValue");
			oldParticipant.setParticipantId(newParticipantId);
			oldParticipant.setParticipantName(newParticipantName);
			oldParticipant.setParticipantScope(newParticipantScope);
			oldParticipant.setParticipantType(newParticipantType);
			oldParticipant.setParamValue(newParamValue);
			oldParticipant.setUpdateDate(new Timestamp(System.currentTimeMillis()));
			oldParticipant.setUpdatePersonId((String) userInfo.get("updatePersonId"));
			oldParticipant.setUpdatePersonName((String) userInfo.get("updatePersonName"));
			flIdSet.add(oldParticipant.getFlId());
			newParticipantMap.remove(participantId);
		}
		participantDao.updateBatch(list);

		//获取节点和模板id映射关系
		Map<String,Object> acFlIdMap = (Map<String, Object>) saveReplaceMap.get("acFlIdMap");
		if(newParticipantMap!=null&&newParticipantMap.size()>0){
			List<Participant> saveBatchList = new ArrayList<Participant>();
			for (Map.Entry<String,Object> acFlIdEntry:acFlIdMap.entrySet()) {
				String acId = acFlIdEntry.getKey();
				String flId = (String) acFlIdEntry.getValue();
				for(Map.Entry<String,Object> newParticipantEntry:newParticipantMap.entrySet()){
					String id = newParticipantEntry.getKey();
					Map<String,Object> newParticipant = (Map<String, Object>) newParticipantMap.get(id);

					String newParticipantId = (String) newParticipant.get("newParticipantId");
					String newParticipantName = (String) newParticipant.get("newParticipantName");
					String newParticipantScope = (String) newParticipant.get("newParticipantScope");
					String newParticipantType = (String) newParticipant.get("newParticipantType");
					String newParamValue = (String) newParticipant.get("newParamValue");

					Participant newParticipantDto = new Participant();
					newParticipantDto.setId(id);
					newParticipantDto.setType("1");
					newParticipantDto.setFlId(flId);
					newParticipantDto.setAcId(acId);
					newParticipantDto.setParticipantId(newParticipantId);
					newParticipantDto.setParticipantName(newParticipantName);
					newParticipantDto.setParticipantScope(newParticipantScope);
					newParticipantDto.setParticipantType(newParticipantType);
					newParticipantDto.setParamValue(newParamValue);
					newParticipantDto.setUpdatePersonId((String) userInfo.get("updatePersonId"));
					newParticipantDto.setUpdatePersonName((String) userInfo.get("updatePersonName"));
					newParticipantDto.setCreatePersonId((String) userInfo.get("updatePersonId"));
					newParticipantDto.setCreatePersonName((String) userInfo.get("updatePersonId"));
					newParticipantDto.setCreateDate(new Timestamp(System.currentTimeMillis()));
					newParticipantDto.setUpdateDate(new Timestamp(System.currentTimeMillis()));
					saveBatchList.add(newParticipantDto);
				}
				flIdSet.add(flId);
			}

			if(saveBatchList.size()>0){
				participantDao.saveBatch(saveBatchList);
			}
		}



		List<String> flIdList = new ArrayList<String>();
		flIdList.addAll(flIdSet);
		userInfo.put("flIdList",flIdList);
		acDao.batchUpdateFlOperationPerson(userInfo);

		return list;
	}
}
