package com.xinleju.platform.flow.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.dao.SysNoticeMsgDao;
import com.xinleju.platform.flow.dto.SysNoticeMsgDto;
import com.xinleju.platform.flow.dto.SysNoticeMsgStatDto;
import com.xinleju.platform.flow.entity.SysNoticeMsg;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class SysNoticeMsgDaoImpl extends BaseDaoImpl<String,SysNoticeMsg> implements SysNoticeMsgDao{

	public SysNoticeMsgDaoImpl() {
		super();
	}

	@Override
	public List<SysNoticeMsgDto> queryTwoSumData(Map<String, String> paramMap) {
		return getSqlSession().selectList(SysNoticeMsg.class.getName()+".queryTwoSumData", paramMap);
	}

	@Override
	public List<SysNoticeMsg> queryHaveDoneList(Map<String, String> paramMap) {
		return getSqlSession().selectList(SysNoticeMsg.class.getName()+".queryHaveDoneList", paramMap);
	}

	@Override
	public List<SysNoticeMsgDto> queryDBDYList(Map<String, String> paramMap) {
		return getSqlSession().selectList(SysNoticeMsg.class.getName()+".queryDBDYList", paramMap);
	}

	@Override
	public int updateStatusOfNoticeMsg(Map<String, Object> paramMap) {
		String methodText = SysNoticeMsg.class.getName()+".updateStatusOfNoticeMsg";
		return getSqlSession().update(methodText, paramMap);
	}

	@Override
	public int setMessageOpened(String messageId) {
		return getSqlSession().update(SysNoticeMsg.class.getName() + ".setMessageOpened", messageId);
	}

	@Override
	public int deleteOpTypeDataByParamMap(Map<String, Object> paramMap) {
		String methodText = SysNoticeMsg.class.getName()+".deleteOpTypeDataByParamMap";
		return getSqlSession().update(methodText, paramMap);
	}

	@Override
	public Page pageQueryByParamMap(Map<String, Object> paramMap, Integer start, Integer limit) {
		List<SysNoticeMsgDto> dataList = doQueryByParamMap(paramMap, start, limit);
	    Page page = new Page();
	    page.setList(dataList);
	    String methodText2 = SysNoticeMsg.class.getName()+".pageCountByParamMap";
	    Integer count = getSqlSession().selectOne(methodText2, paramMap);
	    page.setTotal(count);
	    return page;
	}

	@Override
	public List<SysNoticeMsgDto> doQueryByParamMap(Map<String, Object> paramMap, Integer start, Integer limit) {
		paramMap.put("start", start);
		paramMap.put("limit", limit);
		
		String methodText = SysNoticeMsg.class.getName()+".pageQueryByParamMap";
	    List<SysNoticeMsgDto> dataList = getSqlSession().selectList(methodText, paramMap);
		return dataList;
	}
	
	

	@Override
	public int updateStatusOfNoticeMsgByCurrentUser(Map<String, Object> paramMap) {
		String methodText = SysNoticeMsg.class.getName()+".updateStatusOfNoticeMsgByCurrentUser";
		return getSqlSession().update(methodText, paramMap);
	}

	@Override
	public List<SysNoticeMsgDto> queryMsgDto24Hours(Map<String, String> paramMap) {
		String methodText = SysNoticeMsg.class.getName()+".queryMsgDto24Hours";
		return getSqlSession().selectList(methodText, paramMap);
	}

	@Override
	public SysNoticeMsgStatDto queryFirstTypeStatData(Map<String, String> paramMap) {
		String methodText = SysNoticeMsg.class.getName()+".queryFirstTypeStatData";
		return getSqlSession().selectOne(methodText, paramMap);
	}

	@Override
	public List<SysNoticeMsgDto> queryAllUserToDoList(Map<String, Object> queryMap) {
		String methodText = SysNoticeMsg.class.getName()+".queryAllUserToDoList";
		return getSqlSession().selectList(methodText, queryMap);
	}

	@Override
	public List<SysNoticeMsgDto> queryFlowMsgListByPage(Map<String,Object> parameters){

		return this.getSqlSession().selectList(SysNoticeMsg.class.getName()+".queryFlowMsgListByPage",parameters);
	}

	@Override
	public int queryFlowMsgListByPageCount(Map<String,Object> parameters){

		return this.getSqlSession().selectOne(SysNoticeMsg.class.getName()+".queryFlowMsgListByPageCount",parameters);
	}

	//@Override
	//public List<SysNoticeMsg> searchDataByKeyword(Map<String, String> paramMap) {
	//	return getSqlSession().selectList(SysNoticeMsg.class.getName()+".searchDataByKeyword", paramMap);
	//}
	@Override
	public List<SysNoticeMsgDto> searchDataByKeywordPageParam(Map<String, Object> paramMap) {
		String method = SysNoticeMsg.class.getName()+".searchDataByKeywordPageParam";
		return getSqlSession().selectList(method, paramMap);
	}

	@Override
	public Integer searchDataCountByKeywordPageParam(Map<String, Object> paramMap) {
		String method = SysNoticeMsg.class.getName()+".searchDataCountByKeywordPageParam";
		return getSqlSession().selectOne(method, paramMap);
	}

	@Override
	public int deleteMsgOfAdminBy(String instanceId) {
		return getSqlSession().update(SysNoticeMsg.class.getName() + ".deleteMsgOfAdminBy", instanceId);
	}

	@Override
	public List<SysNoticeMsgDto> queryNoticeMsg(Map map) {
		String method = SysNoticeMsg.class.getName()+".queryNoticeMsg";
		return  getSqlSession().selectList(method, map);
	}

	@Override
	public int updateMsg(Map<String, Object> params) {
		return getSqlSession().update(SysNoticeMsg.class.getName() + ".updateMsg", params);
	}

	@Override
	public List<SysNoticeMsgDto> statisticsNoticeMsg(Map map) {
		return getSqlSession().selectList(SysNoticeMsg.class.getName() + ".statisticsNoticeMsg", map);
	}

	@Override
	public List<Map> selectProcessInfo(Map map) {
		return getSqlSession().selectList(SysNoticeMsg.class.getName()+".selectProcessInfo",map);
	}
	@Override
	public List<Map> selectOrgInfo(Map map) {
		return getSqlSession().selectList(SysNoticeMsg.class.getName()+".selectOrgInfo",map);
	}

	@Override
	public int updateStatusOfNoticeMsgByBatch(Map<String, Object> paramMap) {
		String methodText = SysNoticeMsg.class.getName()+".updateStatusOfNoticeMsgByBatch";
		return getSqlSession().update(methodText, paramMap);
	}
	@Override
	public List<SysNoticeMsgDto> getMsgBussniessObjects(Map map) {
		return getSqlSession().selectList(SysNoticeMsg.class.getName() + ".getMsgBussniessObjects", map);
	}
	
	@Override
	public void assistMessageUpdate(Map<String, Object> paramMap) {
		getSqlSession().update(SysNoticeMsg.class.getName()+".assistMessageUpdate", paramMap);
	}

	@Override
	public SysNoticeMsg getLanuchAssist(Map<String, Object> paramMap) {
		return getSqlSession().selectOne(SysNoticeMsg.class.getName()+".getLanuchAssist", paramMap);
	}

	@Override
	public void delMsgAndRecord(Map<String, Object> paramMap) {
		getSqlSession().update(SysNoticeMsg.class.getName()+".delMsgAndRecord", paramMap);
	}

	@Override
	public SysNoticeMsg getMsgByGroupId(Map<String, Object> paramMap) {
		return getSqlSession().selectOne(SysNoticeMsg.class.getName()+".getMsgByGroupId",paramMap);
	}
}
