package com.xinleju.platform.flow.service;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.dto.SysNoticeMsgDto;
import com.xinleju.platform.flow.dto.SysNoticeMsgStatDto;
import com.xinleju.platform.flow.dto.UserDto;
import com.xinleju.platform.flow.entity.SysNoticeMsg;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

public interface SysNoticeMsgService extends  BaseService <String,SysNoticeMsg>{

	List<SysNoticeMsgDto> queryTwoSumData(Map<String, String> map);

	List<SysNoticeMsg> queryHaveDoneList(Map<String, String> map);

	/**
	 * 改变消息状态
	 * 
	 * @param msgId
	 * @return
	 */
	boolean completeMessage(String msgId, String status);

	List<SysNoticeMsgDto> queryDBDYList(Map<String, String> map);

	int updateStatusOfNoticeMsg(Map<String, Object> map) throws Exception;

	

	/**
	 * 创建流程消息
	 * 
	 * @param user		:消息接收者
	 * @param msgType	：消息类型
	 * @param msgTitle	：消息标题
	 * @param url		：消息URL
	 * @return
	 */
	SysNoticeMsg newFlowMsg(UserDto user, String msgType, String msgTitle, String url, String mobileUrl, String mobileParam);

	/**
	 * 将指定消息状态置为打开状态
	 * 
	 * @param messageId
	 */
	boolean setMessageOpened(String messageId);

	int deleteOpTypeDataByParamMap(Map<String, Object> map);

	Page pageQueryByParamMap(Map<String, Object> map, Integer start, Integer limit) throws Exception;

	int updateStatusOfNoticeMsgByCurrentUser(Map<String, Object> map);

	String saveAndNotifyOthers(SysNoticeMsg msg) throws Exception;
	
    void batchSaveAndNotifyOthers(List<SysNoticeMsg> messages) throws Exception;

	SysNoticeMsgDto queryTotalStatData(Map<String, String> map);

	SysNoticeMsgStatDto queryFirstTypeStatData(Map<String, String> map);

	int doSendWeixinMsgAction() throws Exception;

	/**
	 * 分页获取消息列表，主要用于首页待办
	 * @param parameters
	 * @return
	 */
	public Page queryMsgListByPage(Map<String, Object> parameters);

	/**
	 * 获取消息列表，用于首页展示
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> queryPersonalMsgForPortal(Map<String, Object> paramMap);
	
	//List<SysNoticeMsg> searchDataByKeyword(Map<String, String> map);
	//优化修改为分页查询
	Page searchDataByKeywordPageParam(Map<String, Object> map);
	
	/**
	 * 删除指定流程实例ID中的管理员待办消息
	 * 
	 * @param instanceId
	 * @return
	 */
	public int deleteMsgOfAdminBy(String instanceId);

	void deleteTodo(List<String> msgIds);

	//跨租户查询消息
	int queryMsgTotal(Map<String, Object> params);

	List<SysNoticeMsgDto> queryMsgList(Map<String, Object> params);

	int queryMsgMoreTotal(Map<String, Object> params);

	List<SysNoticeMsgDto> queryMsgMoreList(Map<String, Object> params);

    List<SysNoticeMsgDto>  queryNoticeMsg(Map map);

	int updateStatusOfNoticeMsgBatch(Map<String, Object> map);
	List<SysNoticeMsgDto>  getMsgBussniessObjects(Map<String, Object> map);

	void deletePseudoBatchAndRecord(List<String> list);

	void assistMessageUpdate(Map<String, Object> paramMap);

	SysNoticeMsg getLanuchAssist(Map<String, Object> paramMap);
}
