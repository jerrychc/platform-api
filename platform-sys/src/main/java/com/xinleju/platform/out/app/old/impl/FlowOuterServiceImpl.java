package com.xinleju.platform.out.app.old.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.xinleju.erp.flow.flowutils.bean.DebugInfo;
import com.xinleju.erp.flow.flowutils.bean.FlowResult;
import com.xinleju.erp.flow.service.api.extend.FlowOuterService;

public class FlowOuterServiceImpl implements FlowOuterService {

	private static Logger log = Logger.getLogger(FlowOuterServiceImpl.class);
	
	@Autowired
	private JdbcTemplate jt;
	
	@Override
	public FlowResult<Boolean> giveBackFiNew(String bizId, String flowCode, String endTag, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<Boolean> finishFiBranchNew(String bizId, String flowCode, String endTag, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<Boolean> isFiComplete(String flowCode, String bizId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<Boolean> isExistsRunningProofreadStep(String bizId, String flowCode, String currLoginName) {
		System.out.println("\n\n ---------------------------------------------- isExistsRunningProofreadStep start");
		log.info("isExistsRunningProofreadStep start");

		FlowResult<Boolean> result = new FlowResult<Boolean>();

		LinkedList<String> lstErrorInfoList = new LinkedList<String>();
		if (StringUtils.isBlank(bizId)) {
			lstErrorInfoList.add("业务ID为空");
		}
		if (StringUtils.isBlank(flowCode)) {
			lstErrorInfoList.add("流程编码为空");
		}
		if (lstErrorInfoList.size() > 0) {
			DebugInfo debugInfo = new DebugInfo();
			debugInfo.setErrDesc(lstErrorInfoList);
			result.faliure();
			result.setDebugInfo(debugInfo);
			return result;
		}

		log.info("businessId=[" + bizId + "],flCode=[" + flowCode + "],currLoginName=[" + currLoginName + "]");

		String sql = "select i.id from pt_flow_instance_task t,pt_flow_instance_group g,pt_flow_instance_post p, pt_flow_instance_ac ac,pt_flow_instance i,pt_flow_fl fl "
				+ "where t.group_id=g.id and g.ac_post_id=p.id and p.ac_id=ac.id and i.fl_id=fl.id and ac.fi_id=i.id "
				+ "and ifnull(t.delflag,0)=0 and ifnull(g.delflag,0)=0 and ifnull(p.delflag,0)=0 and ifnull(ac.delflag,0)=0 and ifnull(i.delflag,0)=0 "
				+ "and t.status='2' and p.status='2' and i.status in ('1','9') and ac.approve_type_id='JG' "
				+ "and i.business_id='"+bizId+"' and fl.code='"+flowCode+"' and t.approver_id='"+currLoginName+"'";
				//+ "and i.business_id=? and fl.code=? and t.approver_id=?";
		log.info("full sql="+sql);
		try {
			List<String> queryList = jt.queryForList(sql,  String.class);//, String.class, , , currLoginName);
			//List<String> queryList = jt.queryForList(sql, String.class, bizId, flowCode, currLoginName);
			if (CollectionUtils.isEmpty(queryList)) {
				result.setResult(false);
			} else {
				result.setResult(true);
			}
		} catch (Exception e) {
			result.faliure();
			log.error("isExistsRunningProofreadStep error", e);
		}

		log.info("isExistsRunningProofreadStep end");

		return result;
	}

	@Override
	public FlowResult<Boolean> isExistsFi(String bizId, String flowCode) {

		log.info("isExistsFi start");

		FlowResult<Boolean> result = new FlowResult<Boolean>();

		LinkedList<String> lstErrorInfoList = new LinkedList<String>();
		if (StringUtils.isBlank(bizId)) {
			lstErrorInfoList.add("业务ID为空");
		}
		if (StringUtils.isBlank(flowCode)) {
			lstErrorInfoList.add("流程编码为空");
		}
		if (lstErrorInfoList.size() > 0) {
			DebugInfo debugInfo = new DebugInfo();
			debugInfo.setErrDesc(lstErrorInfoList);
			result.faliure();
			result.setDebugInfo(debugInfo);
			return result;
		}

		log.info("businessId=[" + bizId + "],flCode=[" + flowCode + "]");

		String sql = "select distinct inst.business_id from pt_flow_instance inst,pt_flow_fl fl where inst.fl_id = fl.id and inst.business_id = ? and fl.code = ?";
		try {
			List<String> queryList = jt.queryForList(sql, String.class, bizId, flowCode);
			if (CollectionUtils.isEmpty(queryList)) {
				result.setResult(false);
			} else {
				result.setResult(true);
			}
		} catch (Exception e) {
			result.faliure();
			log.error("isExistsFi error", e);
		}

		log.info("isExistsFi end");

		return result;
	}
	
	public static void main(String args[]){
		FlowOuterServiceImpl test = new FlowOuterServiceImpl();
		//serviceObjectDefineId   flName  flCode
		//(30879, 8449169, 'af6f583d7b2041b38174782f27264e88');
		//"685", "4481657", "af6f583d7b2041b38174782f27264e88"
		String bizId = "685";
		String flowCode = "4481657";
		String currLoginName = "af6f583d7b2041b38174782f27264e88";
		test.isExistsRunningProofreadStep(bizId, flowCode, currLoginName);
	}

}
