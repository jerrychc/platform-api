package com.xinleju.platform.out.app.old.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.xinleju.erp.flow.flowutils.bean.FlowResult;
import com.xinleju.erp.flow.service.api.extend.BaseAPI;
import com.xinleju.erp.flow.service.api.extend.CommonService;
import com.xinleju.erp.flow.service.api.extend.dto.UserDTO;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.flow.utils.DateUtils;
import com.xinleju.platform.sys.num.dto.service.RulerSubDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;

public class CommonServiceImpl implements CommonService {
	
	private static Logger log = Logger.getLogger(CommonServiceImpl.class);
	
	//@Autowired
	//private SysNoticeMsgService_backup sysNoticeMsgService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private BaseAPI baseAPI;

	@Autowired
	private RulerSubDtoServiceCustomer rulerSubDtoServiceCustomer;
	 /**
     * 发送消息接口 (必须实现)
     * 
     * @param module
     *            模块编码
     * @param msgId
     *            消息ID,模块内自行维护，且模块内唯一
     * @param title
     *            消息标题
     * @param url
     *            访问链接 (相对地址)
     * @param typedDate
     *            消息操作时间，对应为 待办发送时间，已办发送时间，阅读时间，格式为 yyyy-MM-dd HH:mm:ss
     * @param opType
     *            消息类别，取值为: DB(待办) WD(未读) YB(已办) YD(已读) RM(删除)，一个需要人工处理的消息，发DB消息；只读不需处理的消息，发 DY消息            
     * @param msgType
     *            消息类型,取值为 1 :通知、0 :待办
     * @param loginName
     *            登录名，发送目标用户
     * @param extParm
     *            预留字段
     * @return
     */
	@Override
	public FlowResult<Boolean> sendMsg(String module, String msgId, String title, String url, String typedDate,
			String opType, String msgType, String loginName, Map<String, Object> extParm) {
		return baseAPI.sendMsg(module, msgId, title, url, typedDate, opType, msgType, loginName, extParm);
	}

	/**
     * 收藏
     * 
     * @param module
     *            模块编码
     * @param storeType
     *            取值为：F: 快捷功能、D：数据文档
     * @param contentType
     *            业务系统自行定义,当storeType为数据文档时有效，表示收藏的数据文档的类别,譬如"流程"、"消息"
     * @param contentId
     *            收藏内容的ID,在 "module"和"contentType"中唯一
     * @param title
     *            标题
     * @param URL
     *            URL
     * @return
     */
	@Override
	public FlowResult<Boolean> store(String module, String storeType, String contentType, String contentId,
			String title, String url) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * 删除收藏
     * 
     * @param module
     *            模块编码
     * @param storeType
     *            取值为：F: 快捷功能、D：数据文档
     * @param contentType
     *            业务系统自行定义,当storeType为数据文档时有效，表示收藏的数据文档的类别,譬如"流程"、"消息"
     * @param contentId
     *            收藏内容的ID,在 "module"和"contentType"中唯一
     * @return
     */
	@Override
	public FlowResult<Boolean> unstore(String module, String storeType, String contentType, String contentId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * 
     * @param module
     *            模块编码
     * @param contentType
     *            收藏类别
     * @param contentId
     *            收藏内容的ID,在 "module"和"contentType"中唯一
     * @return 是否收藏 1:是 0:否
     */
	@Override
	public FlowResult<Boolean> isStored(String module, String storeType, String contentType, String contentId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * 获取下一个编码
     * 
     * @param defineCode
     *            规则定义编码
     * @return
     */
	@Override
	public FlowResult<String> getMaxBizCode(String defineCode) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * 获取下一个编码(取流水号--必须实现)
     * 
     * @param defineId
     *            规则定义ID
     * @return
     */
	@Override
	public FlowResult<String> getNextBizCode(String defineCode) {
		log.debug("\n\n 001 getNextBizCode() is caled, defineCode="+defineCode);
		String resultInfo = "";
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("code", defineCode);
		paramMap.put("status", "1");
		paramMap.put("delflag", 0);
		log.debug("002 getNextBizCode() paramMap="+JacksonUtils.toJson(paramMap));
		String dubboResultInfo = rulerSubDtoServiceCustomer.getBillNumber(userJson, paramMap);
		DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		if(dubboServiceResultInfo.isSucess()){
			resultInfo = dubboServiceResultInfo.getResult();
		}
		log.debug("003 getNextBizCode() resultInfo="+resultInfo);
		FlowResult<String> flowResult = new FlowResult<String>();
		flowResult.setResult(resultInfo);
		flowResult.setSuccess(true);
		return flowResult;
	}

}
