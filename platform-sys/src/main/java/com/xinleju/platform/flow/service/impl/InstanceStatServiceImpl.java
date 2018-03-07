package com.xinleju.platform.flow.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.xinleju.platform.flow.utils.CompareType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.datasource.DataSourceContextHolder;
import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.dao.CalendarDetailDao;
import com.xinleju.platform.flow.dao.FlDao;
import com.xinleju.platform.flow.dao.InstanceStatDao;
import com.xinleju.platform.flow.dao.SysNoticeMsgDao;
import com.xinleju.platform.flow.dto.ApprovalStatDto;
import com.xinleju.platform.flow.dto.InstanceDto;
import com.xinleju.platform.flow.dto.InstanceStatDto;
import com.xinleju.platform.flow.dto.SysNoticeMsgDto;
import com.xinleju.platform.flow.entity.InstanceStat;
import com.xinleju.platform.flow.service.InstanceStatService;
import com.xinleju.platform.flow.utils.DateHourUtils;
import com.xinleju.platform.flow.utils.DateUtils;
import com.xinleju.platform.sys.org.dao.OrgnazationDao;
import com.xinleju.platform.sys.org.service.OrgnazationService;
import com.xinleju.platform.sys.res.dao.AppSystemDao;
import com.xinleju.platform.sys.res.entity.AppSystem;
import com.xinleju.sa.utils.DateUtil;

/**
 * @author admin
 * 
 * 
 */

@Service
public class InstanceStatServiceImpl extends  BaseServiceImpl<String,InstanceStat> implements InstanceStatService{
	
	private static Map<String, String> approveTypeMap = new HashMap<String, String>(); 
	private static Map<String, String> operateTypeMap = new HashMap<String, String>(); 
	
	static {
		approveTypeMap.put("BL", "办理");
		approveTypeMap.put("HD", "核对");
		approveTypeMap.put("SP", "审批");
		approveTypeMap.put("SH", "审核");
		approveTypeMap.put("JG", "校稿");
		approveTypeMap.put("HQ", "会签");
		
		operateTypeMap.put("BJS", "不接受");
		operateTypeMap.put("JS", "接受");
		operateTypeMap.put("WYY", "无异议");
		operateTypeMap.put("GTFQR", "沟通发起人");
		operateTypeMap.put("HF", "回复");
		operateTypeMap.put("XB", "协办");
		operateTypeMap.put("DH", "打回");
		operateTypeMap.put("ZB", "转办");
		operateTypeMap.put("TY", "同意");
		operateTypeMap.put("START", "发起流程");
		
		operateTypeMap.put("SKIPCURRENT", "跳过当前审批人");
		operateTypeMap.put("FINISHFLOW", "审结流程");
		operateTypeMap.put("RESTART", "流程放行");
		operateTypeMap.put("INVALID", "作废流程");
		operateTypeMap.put("withdraw_task", "审批人撤回任务");
		operateTypeMap.put("withdraw_flow", "发起人撤回流程");
	}

	@Autowired
	private InstanceStatDao instanceStatDao;
	@Autowired
	private CalendarDetailDao calendarDetailDao;
	@Autowired
	private SysNoticeMsgDao sysNoticeMsgDao;
	@Autowired
	private AppSystemDao appSystemDao;
	@Autowired
	private OrgnazationDao orgnazationDao;
	@Autowired
	private FlDao flDao;
	@Autowired
	private OrgnazationService orgnazationService;
	@Override
	public List<InstanceStatDto> statUseTimes(Map<String, Object> map) {
		map = processBusiObjectId(map);
		return instanceStatDao.statUseTimes(map);
	}

	private Map<String, Object> processBusiObjectId(Map<String, Object> map) {
		String busiObjectId = (String)map.get("busiObjectId"); 
		if(busiObjectId!=null && !"-1".equals(busiObjectId) && !"".equals(busiObjectId)){
			System.out.println("\n\nbusiObjectId="+busiObjectId);
			String[] itemIds = busiObjectId.split(",");
			map.put("busiObjectId", itemIds);
		}else{
			map.remove("busiObjectId");
		}
		return map;
	}

	@Override
	public List<InstanceStatDto> statInstanceEffiency(Map<String, Object> map) {
		map = processBusiObjectId(map);
		List<InstanceStatDto> tempList = instanceStatDao.statInstanceEffiency(map);
		List<InstanceStatDto> dataList = new ArrayList<InstanceStatDto>();
		System.out.println("\n\n 001 statInstanceEffiency() ---tempList.size="+tempList.size());
		if(tempList!=null && tempList.size()>0){//对数据进行汇总和统计
			Map<String, InstanceStatDto> dataMap = new HashMap<String, InstanceStatDto>();
			Map<String, String> nameMap = new HashMap<String, String>();
			Map<String, String> dayMap=getAllDaysMap();
			//第一步: 开始进行数据的处理统计,汇总到dataMap
			for(int idx=0; idx<tempList.size(); idx++){
				InstanceStatDto statDto = tempList.get(idx);
				String statWay = statDto.getStatWay();//COM / DEPT / PERSON
				String statWayId = statDto.getStatWayId();
				
				//根据statDto统计任务所花的实际工时的小时数据
				float hourSum = calculteHourSumsByStatDto(statDto,dayMap);
				//增加任务时长的比较条件
				boolean addResultFlag = addCompareTypeCondition(map, hourSum);
				System.out.println("004---- addResultFlag="+addResultFlag);
				if(addResultFlag){//如果是符合加入结果集的条件 ,则加入结果集
					String keyText = statWay+"-"+statWayId;
					InstanceStatDto newDto = dataMap.get(keyText);
					
					String nameValue = nameMap.get(keyText);
					if(nameValue==null || "".equals(nameValue)  || "null".equals(nameValue)){
						nameValue = statDto.getStatWayName();
						nameMap.put(keyText, nameValue);
					}
					
					if(newDto == null){//如果是第一次存入的
						newDto = new InstanceStatDto();//statWay,statWayId
						newDto.setStatWay(statWay);
						newDto.setStatWayId(statWayId);
						newDto.setStatWayName(statDto.getStatWayName());
						newDto.setTaskSum("0");
						newDto.setTotalSum("0");
						newDto.setAvgSum("0");
						newDto.setMaxSum("0");
						newDto.setMinSum(Float.toString(hourSum));
					}
					int taskSum = Integer.parseInt(newDto.getTaskSum());
					float totalSum = Float.parseFloat(newDto.getTotalSum());
					float maxSum = Float.parseFloat(newDto.getMaxSum());
					float minSum = Float.parseFloat(newDto.getMinSum());
					taskSum = taskSum+1;
					totalSum = totalSum+hourSum;
					if(hourSum>maxSum){
						maxSum=hourSum;
					}
					
					if(hourSum<minSum){
						minSum=hourSum;
					}
					newDto.setTaskSum(Integer.toString(taskSum));
					newDto.setTotalSum(Float.toString(totalSum));
					newDto.setMaxSum(Float.toString(maxSum));
					newDto.setMinSum(Float.toString(minSum));
					newDto.setStatWayName(nameValue);
					dataMap.put(keyText, newDto);
				}
			}//end for loop
			
			//第二步: 从dataMap读取数据,转为List的数据
			DecimalFormat decimalFormat=new DecimalFormat("0.00");
			System.out.println(" 005 dataMap.values().size="+dataMap.values().size());
			for(InstanceStatDto resultDto : dataMap.values()){
				//此处还需增加将statWayId转换为statWayName的逻辑
				//可以考虑批量地获取statWayId对应的值
				System.out.println("----->>> statWayId="+resultDto.getStatWayId()+"; statWay="+resultDto.getStatWay());
				
				int taskSum = Integer.parseInt(resultDto.getTaskSum());
				float totalSum = Float.parseFloat(resultDto.getTotalSum());
				float avgSum = totalSum/taskSum;
				resultDto.setAvgSum(decimalFormat.format(avgSum));
				dataList.add(resultDto);
			}
			System.out.println(" 006 dataList.size="+dataList.size());
		}
		return dataList;
	}

	@Override
	public List<InstanceStatDto> statOperateTimes(Map<String, Object> map) {
		map = processBusiObjectId(map);
		
		return instanceStatDao.statOperateTimes(map);
	}

	//备份的数据
	public List<InstanceStatDto> statTaskLengthOld(Map<String, Object> map) {
		map = processBusiObjectId(map);
		return instanceStatDao.statTaskLength(map);
	}
	
	
	@Override
	public List<InstanceStatDto> statTaskLength(Map<String, Object> map) {
		map = processBusiObjectId(map);
//		List<InstanceStatDto> tempList = instanceStatDao.statTaskLength(map);
		List<InstanceStatDto> tempList = this.statTaskLengthByNoticeMsg(map);
		List<InstanceStatDto> dataList = new ArrayList<InstanceStatDto>();
		System.out.println("\n\n 001 statTaskLength() ---tempList.size="+tempList.size());
		DecimalFormat decimalFormat=new DecimalFormat("0.00");
		if(tempList!=null && tempList.size()>0){//对数据进行汇总和统计
			Map<String, InstanceStatDto> dataMap = new HashMap<String, InstanceStatDto>();
			
			Map<String, String> dayMap=getAllDaysMap();
			//第一步: 开始进行数据的处理统计,汇总到dataMap
			for(int idx=0; idx<tempList.size(); idx++){
				InstanceStatDto statDto = tempList.get(idx);
				String statWay = statDto.getStatWay();//COM / DEPT / PERSON
				String statWayId = statDto.getStatWayId();
				//根据statDto统计任务所花的实际工时的小时数据
				float hourSum = calculteHourSumsByStatDto(statDto,dayMap);
				//增加任务时长的比较条件
				boolean addResultFlag = addCompareTypeCondition(map, hourSum);
				if(addResultFlag){//如果是符合加入结果集的条件 ,则加入结果集
					String keyText = statWay+"-"+statWayId;
					InstanceStatDto newDto = dataMap.get(keyText);
					if(newDto == null){//如果是第一次存入的
						newDto = new InstanceStatDto();//statWay,statWayId
						newDto.setStatWay(statWay);
						newDto.setStatWayId(statWayId);
						newDto.setStatWayName(statDto.getStatWayName());
						newDto.setTaskSum("0");
						newDto.setTotalSum("0");
						newDto.setAvgSum("0");
						newDto.setMaxSum("0");
						newDto.setMinSum(decimalFormat.format(hourSum));
					}
					int taskSum = Integer.parseInt(newDto.getTaskSum());
					float totalSum = Float.parseFloat(newDto.getTotalSum());
					float maxSum = Float.parseFloat(newDto.getMaxSum());
					float minSum = Float.parseFloat(newDto.getMinSum());
					taskSum = taskSum+1;
					totalSum = totalSum+hourSum;
					if(hourSum>maxSum){
						maxSum=hourSum;
					}
					
					if(hourSum<minSum){
						minSum=hourSum;
					}
					newDto.setTaskSum(Integer.toString(taskSum));
					newDto.setTotalSum(decimalFormat.format(totalSum));
					newDto.setMaxSum(decimalFormat.format(maxSum));
					newDto.setMinSum(decimalFormat.format(minSum));
					dataMap.put(keyText, newDto);
				}
				
			}//end for loop
			
			//第二步: 从dataMap读取数据,转为List的数据

			for(InstanceStatDto resultDto : dataMap.values()){
				//此处还需增加将statWayId转换为statWayName的逻辑
				//可以考虑批量地获取statWayId对应的值
				
				int taskSum = Integer.parseInt(resultDto.getTaskSum());
				float totalSum = Float.parseFloat(resultDto.getTotalSum());
				float avgSum = totalSum/taskSum;
				resultDto.setAvgSum(decimalFormat.format(avgSum));
				dataList.add(resultDto);
			}
		}
		return dataList;
	}
	public Map<String, String> getAllDaysMap() {
		List<Map<String, String>> list = calendarDetailDao.selectAllDays();
		Map<String, String> dayMap=new HashMap<>();
		for (Map<String, String> map : list) {
			dayMap.put(map.get("day_text"), map.get("day_type"));
		}
		return dayMap;
	}
	//获取指定日期内的非工作日天数，和最大最小非工作日
	public InstanceStatDto queryHolidaySumAndMinMAxDate(Map<String, String> dayMap,String startDate,String endDate){
		InstanceStatDto statDayDto=new InstanceStatDto();
		String minDate=null;
		String maxDate=null;
		Date maxD=null;
		Date tempD=null;
		String temp=startDate;
		String type=null;
		int holidayDaySum=0;//非工作日天数
		while(true) {
			if(temp.equals(endDate)){
				break;
			}
			type=dayMap.get(temp);
			if("2".equals(type)){//非工作日
				holidayDaySum++;
				if(minDate==null){
					minDate=temp;
				}
				if(maxDate==null){
					maxDate=temp;
				}else {
					maxD=DateUtils.parseDate(maxDate, "yyyy-MM-dd");
					tempD=DateUtils.parseDate(temp, "yyyy-MM-dd");
					if(tempD.after(maxD)){
						maxDate=temp;
					}
				}
			}
			temp=oneDayAfter(temp);
		}
		if(temp.equals(endDate)){
			type=dayMap.get(temp);
			if("2".equals(type)){//非工作日
				holidayDaySum++;
			}
		}
		statDayDto.setHourSum(holidayDaySum+"");
		statDayDto.setStartDate(minDate);
		statDayDto.setEndDate(maxDate);
		return statDayDto;
	}
	//一天以后
	public String oneDayAfter (String d) {
		Date date=DateUtils.parseDate(d, "yyyy-MM-dd");
		String oneHoursAgoTime =  "" ;
	    Calendar cal = Calendar.getInstance ();
	    cal.setTime(date);
	    cal.add(Calendar.DATE ,1 ) ;
	    oneHoursAgoTime =  new  SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());//获取到完整的时间
	    return  oneHoursAgoTime;
	}
	private float calculteHourSumsByStatDto(InstanceStatDto statDto,Map<String, String> dayMap) {
		String startDate = statDto.getStartDate().substring(0, 10);
		String endDate = statDto.getEndDate().substring(0, 10);
		long totalSecondSum = Long.parseLong(statDto.getHourSum());//先得到总的秒数
		InstanceStatDto statDayDto =queryHolidaySumAndMinMAxDate(dayMap,startDate, endDate);
//		InstanceStatDto statDayDto = instanceStatDao.queryHolidaySumAndMinMAxDate(startDate, endDate);
		int holidayDaySum = Integer.parseInt(statDayDto.getHourSum());
		String minDate = statDayDto.getStartDate();
		String maxDate = statDayDto.getEndDate();
		if(holidayDaySum>0){
			//对几种情况进行特殊处理
			//case 1: 开始和结束是同一天,且都是节假日
			if(startDate.equals(minDate) && minDate.equals(maxDate) && endDate.equals(maxDate)){
				holidayDaySum = 0;
				totalSecondSum = 0;
			} else {
				
				if(startDate.equals(minDate)){//如果开始时间是节假日
					long secondSum = DateHourUtils.secondsBetween(statDto.getStartDate(), startDate+" 23:59:59");
					totalSecondSum = totalSecondSum -secondSum;
				}
				if(endDate.equals(maxDate)){//如果结束时间是节假日
					long secondSum = DateHourUtils.secondsBetween(endDate+" 00:00:00", statDto.getEndDate());
					totalSecondSum = totalSecondSum -secondSum;
				}
				
				if(totalSecondSum<0){
					totalSecondSum = 0;
				}
				
				if(totalSecondSum>0){
					int tempSum = 0;
					if(startDate.equals(minDate)){
						tempSum++;
					}
					if(endDate.equals(maxDate)){
						tempSum++;
					}
					totalSecondSum = totalSecondSum - (holidayDaySum-tempSum)*24*3600;//先扣中间(不含两头的)的节假日
				}
			}
			if(totalSecondSum<0){
				totalSecondSum = 0;
			}
		}
		BigDecimal b1 = new BigDecimal(String.valueOf(totalSecondSum));
		BigDecimal b2 = new BigDecimal("3600");
		float hourSum =  b1.divide(b2, 2, BigDecimal.ROUND_HALF_EVEN).floatValue();
//		long hourSum = totalSecondSum/3600;
		return hourSum;
	}

	private boolean addCompareTypeCondition(Map<String, Object> map, float hourSum) {
		//开始处理任务时长的查询条件 ----默认是可以加入结果集的
		boolean addResultFlag = true;
		//compareType ==greaterEqual|lessEqual|equal|between>>startSum endSum
		String compareType = (String)map.get("compareType");
		//compareType不为空的情况
		if(compareType!=null && !"".equals(compareType)  && !"null".equals(compareType)&& !"-1".equals(compareType)){
			addResultFlag = false;//compareType不为空的时候,则初步设置为不加入结果集
			String startSumText = (String)map.get("startSum");
			String endSumText = (String)map.get("endSum");
			float startSum = 0, endSum=0;
			if(startSumText!=null && !"".equals(startSumText) && !"null".equals(startSumText)){
				startSum = Float.parseFloat(startSumText);
			}
			if(endSumText!=null && !"".equals(endSumText) && !"null".equals(endSumText)){
				endSum = Float.parseFloat(endSumText);
			}
			
			//只有满足条件了,才能加入结果集
			if( "greaterEqual".equals(compareType) ){
				if(hourSum>=startSum){
					addResultFlag = true;
				}
			}
			if( "lessEqual".equals(compareType) ){
				if(hourSum<=startSum){
					addResultFlag = true;
				}
			}
			if( "equal".equals(compareType) ){
				if(hourSum==startSum){
					addResultFlag = true;
				}
			}
			if( "between".equals(compareType) ){
				if(hourSum>=startSum && hourSum<=endSum){
					addResultFlag = true;
				}
			}
		}
		return addResultFlag;
	}

	@Override
	public List<InstanceStatDto> detailTaskLengthList(Map map) {
		map = processBusiObjectId(map);
//		List<InstanceStatDto> tempList = instanceStatDao.detailTaskLengthList(map);
		List<InstanceStatDto> tempList = this.detailStatTaskLengthByNoticeMsg(map);
		List<InstanceStatDto> dataList = new ArrayList<InstanceStatDto>();
		if(tempList!=null && tempList.size()>0){//对数据进行汇总和统计
			Map<String, String> dayMap=getAllDaysMap();
			for(int idx=0; idx<tempList.size(); idx++){
				InstanceStatDto statDto = tempList.get(idx);
				//根据statDto统计任务所花的实际工时的小时数据
				float hourSum = calculteHourSumsByStatDto(statDto,dayMap);
				
				statDto.setHourSum(hourSum+"");
				statDto.setDayType("1");//默认是工作日处理
				String finishFlag = statDto.getFinishFlag();
				
				//增加任务时长的比较条件
				boolean addResultFlag = addCompareTypeCondition(map, hourSum);
				if(addResultFlag){
					
					//delete by zdq  #12329
					/*String operateContent = statDto.getOperateContent();//approveType:XXX,operationType:xxxx,
					if(operateContent!=null && operateContent.length()>10){
						String contentItems[] = operateContent.split(",");
						String approveType = contentItems[0].replaceAll("approveType:", "");
						String operationType = contentItems[1].replaceAll("operationType:", "");
						if("START".equals(operationType)){
							statDto.setOperationType("未处理");//此处需转换为对应的名称
						}else{
							statDto.setOperationType(operateTypeMap.get(operationType));
						}
						statDto.setApproveType(approveTypeMap.get(approveType));//此处需转换为对应的名称
					}else{
						statDto.setApproveType("暂无");//此处需转换为对应的名称
						statDto.setOperationType("暂无");//此处需转换为对应的名称
					}*/
					String dayType = statDto.getDayType();
					if("1".equals(dayType)){
						statDto.setDayType("是");
					}else{
						statDto.setDayType("否");
					}
//					if("0".equals(finishFlag)){ //未完成的情况
////						statDto.setEndDate("");
//					}else{
//						String endDate = statDto.getEndDate();
//						statDto.setEndDate(endDate.substring(0, 19));
//					}
					statDto.setId(statDto.getInstanceId());
//					System.out.println("--->>> idx="+idx+"; "+JacksonUtils.toJson(statDto));
					dataList.add(statDto);
				}
			}
		}
		return dataList;
	}

	@Override
	public List<InstanceDto> detailOperateTimesList(Map map) {
		map = processBusiObjectId(map);
		return instanceStatDao.detailOperateTimesList(map);
	}

	@Override
	public List<InstanceStatDto> detailInstanceEfficiencyList(Map map) {
		map = processBusiObjectId(map);
		List<InstanceStatDto> tempList = instanceStatDao.detailInstanceEfficiencyList(map);
		List<InstanceStatDto> dataList = new ArrayList<InstanceStatDto>();
		if(tempList!=null && tempList.size()>0){//对数据进行汇总和统计
			Map<String, String> dayMap=getAllDaysMap();
			for(int idx=0; idx<tempList.size(); idx++){
				InstanceStatDto statDto = tempList.get(idx);
				//根据statDto统计任务所花的实际工时的小时数据
				float hourSum = calculteHourSumsByStatDto(statDto,dayMap);
				String compareType = (String) map.get("compareType");
				if(CompareType.LESS_EQUAL.getCode().equals(compareType)){
					String startSum = (String) map.get("startSum");
					if(hourSum>Float.valueOf(startSum)){
						continue;
					}
				}else if(CompareType.GREATER_EQUAL.getCode().equals(compareType)){
					String startSum = (String) map.get("startSum");
					if(hourSum<Float.valueOf(startSum)){
						continue;
					}
				}else if(CompareType.EQUAL.getCode().equals(compareType)){
					String startSum = (String) map.get("startSum");
					if(!String.valueOf(hourSum).equals(startSum)){
						continue;
					}
				}else if(CompareType.BETWEEN.getCode().equals(compareType)){
					String startSum = (String) map.get("startSum");
					String endSum = (String) map.get("endSum");
					if(hourSum<Float.valueOf(startSum)||hourSum>Float.valueOf(endSum)){
						continue;
					}
				}
				statDto.setHourSum(hourSum+"");
				String startDate = statDto.getStartDate();
				statDto.setStartDate(startDate.substring(0, 19));
				dataList.add(statDto);
			}
		}
		return dataList;
	}

	/**
	 * 根据流程消息记录统计任务时长
	 * @param map
	 * @return
     */
	@Override
	public List<InstanceStatDto> statTaskLengthByNoticeMsg(Map map) {
		Map queryMap = new HashMap();
		String startDate = map.get("startDate1")==null?"": String.valueOf(map.get("startDate1"));
		String endDate =   map.get("endDate1")==null?"":String.valueOf(map.get("endDate1"));
		String busiObjectId = map.get("busiObjectId")==null?"":((String[])map.get("busiObjectId"))[0];
		String statWay = String.valueOf(map.get("statWay"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.isBlank(startDate)) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, -7);
			startDate = sdf.format(cal.getTime());
		}
		if(StringUtils.isBlank(endDate)){
			endDate = sdf.format(new Date());
		}
		queryMap.put("startDate",startDate);
		queryMap.put("endDate",endDate);
		List<SysNoticeMsgDto> list=  sysNoticeMsgDao.statisticsNoticeMsg(queryMap);
		List<InstanceStatDto> instanceStatList = new ArrayList<InstanceStatDto>();
		if(list!=null&&!list.isEmpty()){
			list.parallelStream().forEach(sysNoticeMsg -> {
				DataSourceContextHolder.clearDataSourceType();
				DataSourceContextHolder.setDataSourceType(String.valueOf(map.get("tendCode")));
				InstanceStatDto instanceStatDto = new InstanceStatDto();
			 	String url = sysNoticeMsg.getUrl();
				String[] params = url.split("&");
				String instanceId = "";//流程实例ID
				for(String param:params){
					if(param.indexOf("instanceId")>-1){
						String[] temp = param.split("=");
						if(temp!=null&&temp.length>1){
							instanceId = param.split("=")[1];
						}
					};
				}
				instanceStatDto.setInstanceId(instanceId);
				instanceStatDto.setStartDate(sdf2.format(new Date(sysNoticeMsg.getSendDate().getTime())));
				instanceStatDto.setEndDate(sdf2.format(new Date(sysNoticeMsg.getDealDate().getTime())));
				instanceStatDto.setHourSum(String.valueOf((sysNoticeMsg.getDealDate().getTime()-sysNoticeMsg.getSendDate().getTime())/1000));
				instanceStatDto.setOperatorId(sysNoticeMsg.getUserId());
				instanceStatDto.setOperatorName(sysNoticeMsg.getUserName());
				Map queryMap2 = new HashMap();
				queryMap2.put("instanceId",instanceId);
				queryMap2.put("userId",sysNoticeMsg.getUserId());
				queryMap2.put("busiObjectId",busiObjectId);
				List<Map> processInfoList = sysNoticeMsgDao.selectProcessInfo(queryMap2);
				if(processInfoList!=null&&!processInfoList.isEmpty()){
					Map processInfo = processInfoList.get(0);
	/*				String acName =  processInfo.get("acName").toString();
					String busObjectName = processInfo.get("busiObjectName").toString();
					String flowTemplate =  processInfo.get("flowTemplate").toString();
					String type = processInfo.get("type").toString();
					String pid = processInfo.get("pid").toString();*/
					String orgName = String.valueOf(processInfo.get("orgName"));
					String orgId = String.valueOf(processInfo.get("orgId"));
					queryMap2.clear();
					queryMap2.put("id",orgId);
					List<Map> orgList = sysNoticeMsgDao.selectOrgInfo(queryMap2);
					switch (statWay){
						case "COM":{
							List<Map> comOrgList = orgList.stream().filter(org->Objects.equals(String.valueOf(org.get("type")),"company")).collect(Collectors.toList());
							if(comOrgList!=null&&!comOrgList.isEmpty()){
								instanceStatDto.setStatWay(statWay);
								instanceStatDto.setStatWayId(String.valueOf(comOrgList.get(0).get("id")));
								instanceStatDto.setStatWayName(String.valueOf(comOrgList.get(0).get("prefix_name")));
							}
							break;
						}
						case "DEPT":{
							List<Map> deptOrgList = orgList.stream().filter(org->Objects.equals(String.valueOf(org.get("type")),"dept")).collect(Collectors.toList());
							if(deptOrgList!=null&&!deptOrgList.isEmpty()){
								instanceStatDto.setStatWay(statWay);
								instanceStatDto.setStatWayId(String.valueOf(deptOrgList.get(0).get("id")));
								instanceStatDto.setStatWayName(String.valueOf(deptOrgList.get(0).get("prefix_name")));
							}
							break;
						}
						case "USER":{
							instanceStatDto.setStatWay(statWay);
							instanceStatDto.setStatWayId(sysNoticeMsg.getUserId());
							instanceStatDto.setStatWayName(sysNoticeMsg.getUserName());
							break;
						}
					}

				}

				instanceStatList.add(instanceStatDto);
			});
		}

		return instanceStatList;
	}

	/**
	 * 根据流程消息记录统计任务时长详细
	 * @param map
	 * @return
	 */
	@Override
	public List<InstanceStatDto> detailStatTaskLengthByNoticeMsg(Map map) {
		Map queryMap = new HashMap();
		String startDate = map.get("startDate1")==null?"": String.valueOf(map.get("startDate1"));
		String endDate =   map.get("endDate1")==null?"":String.valueOf(map.get("endDate1"));
		String busiObjectId = map.get("busiObjectId")==null?"":((String[])map.get("busiObjectId"))[0];
		String statWay = String.valueOf(map.get("statWay"));
		String statWayId = String.valueOf(map.get("statWayId"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.isBlank(startDate)) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, -7);
			startDate = sdf.format(cal.getTime());
		}
		if(StringUtils.isBlank(endDate)){
			endDate = sdf.format(new Date());
		}
		queryMap.put("startDate",startDate);
		queryMap.put("endDate",endDate);
		List<SysNoticeMsgDto> list=  sysNoticeMsgDao.statisticsNoticeMsg(queryMap);
		List<InstanceStatDto> instanceStatList = new ArrayList<InstanceStatDto>();
		if(list!=null&&!list.isEmpty()){
			list.parallelStream().forEach(sysNoticeMsg -> {
				DataSourceContextHolder.clearDataSourceType();
				DataSourceContextHolder.setDataSourceType(String.valueOf(map.get("tendCode")));
				InstanceStatDto instanceStatDto = new InstanceStatDto();
				String url = sysNoticeMsg.getUrl();
				String[] params = url.split("&");
				String instanceId = "";//流程实例ID
				for(String param:params){
					if(param.indexOf("instanceId")>-1){
						String[] temp = param.split("=");
						if(temp!=null&&temp.length>1){
							instanceId = param.split("=")[1];
						}
					};
				}
				instanceStatDto.setInstanceId(instanceId);
				instanceStatDto.setStartDate(sdf2.format(new Date(sysNoticeMsg.getSendDate().getTime())));
				instanceStatDto.setEndDate(sdf2.format(new Date(sysNoticeMsg.getDealDate().getTime())));
				instanceStatDto.setHourSum(String.valueOf((sysNoticeMsg.getDealDate().getTime()-sysNoticeMsg.getSendDate().getTime())/1000));
				instanceStatDto.setOperatorId(sysNoticeMsg.getUserId());
				instanceStatDto.setOperatorName(sysNoticeMsg.getUserName());
				instanceStatDto.setOperateTime(Objects.equals(sysNoticeMsg.getOpType(),"DB")||Objects.equals(sysNoticeMsg.getOpType(),"DY")?null:sysNoticeMsg.getDealDate());
				if(Objects.equals(sysNoticeMsg.getOpType(),"DB")||Objects.equals(sysNoticeMsg.getOpType(),"DY")){
					instanceStatDto.setOperationType("待审");
				}else{
					instanceStatDto.setOperationType("已审");
				}
				instanceStatDto.setSource(sysNoticeMsg.getSource());
				instanceStatDto.setAppCode(sysNoticeMsg.getAppCode());

				Map queryMap2 = new HashMap();
				queryMap2.put("instanceId",instanceId);
				queryMap2.put("userId",sysNoticeMsg.getUserId());
				queryMap2.put("busiObjectId",busiObjectId);
				List<Map> processInfoList = sysNoticeMsgDao.selectProcessInfo(queryMap2);
				boolean isContain = false;
				if(processInfoList!=null&&!processInfoList.isEmpty()){
					Map processInfo = processInfoList.get(0);
					String acName =   String.valueOf(processInfo.get("acName"));
					String busObjectName =  String.valueOf(processInfo.get("busiObjectName"));
					String flowTemplate =   String.valueOf(processInfo.get("flowTemplate"));
					String type =  String.valueOf(processInfo.get("type"));
					String pid = String.valueOf(processInfo.get("pid"));
					String orgName = String.valueOf(processInfo.get("orgName"));
					String orgId = String.valueOf(processInfo.get("orgId"));
					String instanceName = String.valueOf(processInfo.get("instanceName"));
					String startUserName = String.valueOf(processInfo.get("startUserName"));
                    String startCompanyName = String.valueOf(processInfo.get("startCompanyName"));
                    String startDeptName = String.valueOf(processInfo.get("startDeptName"));
					instanceStatDto.setStartUserName(startUserName);
					instanceStatDto.setFlowBusinessDeptName(startDeptName);
					instanceStatDto.setFlowBusinessCompanyName(startCompanyName);
					instanceStatDto.setAcName(acName);
					instanceStatDto.setBusiObjectName(busObjectName);
					instanceStatDto.setInstanceId(instanceId);
					instanceStatDto.setInstanceName(instanceName);
					instanceStatDto.setFlowName(flowTemplate);
					queryMap2.clear();
					queryMap2.put("id",orgId);
					List<Map> orgList = sysNoticeMsgDao.selectOrgInfo(queryMap2);
					if(orgList!=null&&!orgList.isEmpty()){
						orgList.stream().forEach(org->{
							if(Objects.equals(String.valueOf(org.get("type")),"company")){
								 instanceStatDto.setOperatorCompanyName(String.valueOf(org.get("prefix_name")));
							}
							if(Objects.equals(String.valueOf(org.get("type")),"dept")){
								 instanceStatDto.setOperatorDeptName(String.valueOf(org.get("prefix_name")));
							}
						});
					}
					switch (statWay){
						case "COM":{
							List<Map> comOrgList = orgList.stream().filter(org->Objects.equals(String.valueOf(org.get("type")),"company")&&Objects.equals(org.get("id"),statWayId)).collect(Collectors.toList());
							if(comOrgList!=null&&!comOrgList.isEmpty()){
								instanceStatDto.setStatWay(statWay);
								instanceStatDto.setStatWayId(String.valueOf(comOrgList.get(0).get("id")));
								instanceStatDto.setStatWayName(String.valueOf(comOrgList.get(0).get("prefix_name")));
								isContain = true;
							}
							break;
						}
						case "DEPT":{
							List<Map> deptOrgList = orgList.stream().filter(org->Objects.equals(String.valueOf(org.get("type")),"dept")&&Objects.equals(org.get("id"),statWayId)).collect(Collectors.toList());
							if(deptOrgList!=null&&!deptOrgList.isEmpty()){
								instanceStatDto.setStatWay(statWay);
								instanceStatDto.setStatWayId(String.valueOf(deptOrgList.get(0).get("id")));
								instanceStatDto.setStatWayName(String.valueOf(deptOrgList.get(0).get("prefix_name")));
								isContain = true;
							}
							break;
						}
						case "USER":{
							if(Objects.equals(sysNoticeMsg.getUserId(),statWayId)){
								instanceStatDto.setStatWay(statWay);
								instanceStatDto.setStatWayId(sysNoticeMsg.getUserId());
								instanceStatDto.setStatWayName(sysNoticeMsg.getUserName());
								isContain = true;
							}
							break;
						}
					}

				}
				if(isContain){
					instanceStatList.add(instanceStatDto);
				}
			});
		}

		return instanceStatList;
	}
	public static void main(String args[]){
//		DecimalFormat decimalFormat=new DecimalFormat("0.00");
//		System.out.println(" 005 dataMap.values().size="+decimalFormat.format(0.50));

		String url = "flow/runtime/approve/flow.html?instanceId=ebe802c5ce414a74b10edaa4687021d2&taskId=d7e25ae24e17472b8e21be3e8be73bc2&time=1512107538989&msgId=00e21f65928c4ef69a6a9ec3b8bd101a";
		String[] params = url.split("&");
		String instanceId = "";
		for(String param:params){
			if(param.indexOf("instanceId")>-1){
				instanceId = param.split("=")[1];
			};
		}
		System.out.println("instanceId:"+instanceId);
		
	}

	@Override
	public Page getPageForForm(String userJson, Map map) {
		Map<String, String> dayMap=getAllDaysMap();
		List<String> approverIds=new ArrayList<String>();
		List<String> busiObjectIds=new ArrayList<String>();
		List<String> flIds=new ArrayList<String>();
		Map<String,Object> paramMap=new HashMap<String,Object>();
		String workDay=String.valueOf(map.get("workDay"));
		//人员
		if(map.get("approverIds")!=null && !"".equals(map.get("approverIds"))){
			approverIds=Arrays.asList(map.get("approverIds").toString().split(","));
			map.put("approverIds", approverIds);
		}else{
			map.put("approverIds", approverIds);
		}
		//业务对象
		if(map.get("busiObjectId")!=null && !"".equals(map.get("busiObjectId"))){
			busiObjectIds=Arrays.asList(map.get("busiObjectId").toString().split(","));
			map.put("busiObjectIds", busiObjectIds);
		}else{
			map.put("busiObjectIds", busiObjectIds);
		}
		//流程模板
		if(map.get("flowId")!=null && !"".equals(map.get("flowId"))){
			String flId=String.valueOf(map.get("flowId"));
			flIds.add(flId);
			paramMap.put("flowIds", flIds);
			flIds=flDao.queryAllFlByCode(paramMap);
			map.put("flowIds", flIds);
		}else{
			map.put("flowIds", flIds);
		}
		//组织
		if(map.get("orgIds")!=null && !"".equals(map.get("orgIds"))){
			List<String> orgIds=Arrays.asList(map.get("orgIds").toString().split(","));
			if(orgIds!=null && orgIds.size()>0){
				paramMap.put("orgIds", orgIds);
				List<String> userIds=orgnazationDao.getSubUserByOrgIds(paramMap);
				if(userIds!=null && userIds.size()>0){
					approverIds.addAll(userIds);
					map.put("approverIds", approverIds);
				}
			}
		}
		paramMap.put("appDelFlag", "0");
		paramMap.put("appStatus", "1");
		List<AppSystem> appSystemList=appSystemDao.queryList(paramMap);
		Map<String,String> appSystemMap=this.listToMap(appSystemList);
		Set<String> userSetIds=new HashSet<String>();
		List<String> userListIds=new ArrayList<String>();
		// 分页显示
		Page page=new Page();
		// 获取分页list 数据
		List<ApprovalStatDto> list = instanceStatDao.getPageSort(map);
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				ApprovalStatDto approvalStatDto=list.get(i);
				if(workDay!=null && "1".equals(workDay)){
					float hourSum = calculteHourSumsByWithOutHoliday(approvalStatDto,dayMap);
					System.out.println(new Double(Math.floor(hourSum)).intValue());
					approvalStatDto.setHoldTime(String.valueOf(new Double(Math.floor(hourSum)).intValue()));
				}
				approvalStatDto.setAppType(appSystemMap.get(approvalStatDto.getAppId()));
				userSetIds.add(approvalStatDto.getApproverId());
				userSetIds.add(approvalStatDto.getStarterId());
			}
		}
		userListIds.addAll(userSetIds);
		paramMap.clear();
		paramMap.put("userIds", userListIds);
		try {
			if(userListIds!=null && userListIds.size()>0){
				Map<String, Object> resultMap=orgnazationService.queryUserDirectDeptAndDirectCom(paramMap);
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						ApprovalStatDto approvalStatDto=list.get(i);
						if(approvalStatDto!=null){
							if(approvalStatDto.getTaskStartTime()!=null && approvalStatDto.getTaskStartTime().endsWith(".0")){
								approvalStatDto.setTaskStartTime(approvalStatDto.getTaskStartTime().substring(0, approvalStatDto.getTaskStartTime().length()-2));
							}
							if(approvalStatDto.getTaskEndTime()!=null && approvalStatDto.getTaskEndTime().endsWith(".0")){
								approvalStatDto.setTaskEndTime(approvalStatDto.getTaskEndTime().substring(0, approvalStatDto.getTaskEndTime().length()-2));
							}
						}
						Map<String,Object> statMxDtoApprover=(Map<String, Object>) resultMap.get(approvalStatDto.getApproverId());
						Map<String,Object> statMxDtostater=(Map<String, Object>) resultMap.get(approvalStatDto.getStarterId());
						if(statMxDtoApprover!=null){
							approvalStatDto.setApproverCompanyName(String.valueOf(statMxDtoApprover.get("comName")==null?"":statMxDtoApprover.get("comName")));
							approvalStatDto.setApproverDeptName(String.valueOf(statMxDtoApprover.get("deptName")==null?"":statMxDtoApprover.get("deptName")));
						}
						if(statMxDtostater!=null){
							approvalStatDto.setStarterCompanyName(String.valueOf(statMxDtostater.get("comName")==null?"":statMxDtostater.get("comName")));
							approvalStatDto.setStarterDeptName(String.valueOf(statMxDtostater.get("deptName")==null?"":statMxDtostater.get("deptName")));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 获取条件的总数据量
		Integer count = instanceStatDao.getPageSortCount(map);
		if(map.get("limit")!=null){
			page.setLimit((Integer) map.get("limit") );
		}
		if(map.get("start")!=null){
			page.setStart((Integer) map.get("start"));
		}
		page.setList(list);
		page.setTotal(count);
		//封装成page对象 传到前台
		return page;
	}
	
	/**
	  * @Description:计算去除节假日后的时间
	  * @author:zhangfangzhi
	  * @date 2018年2月23日 上午9:51:25
	  * @version V1.0
	 */
	private float calculteHourSumsByWithOutHoliday(ApprovalStatDto approvalStatDto,Map<String, String> dayMap) {
		String startDate=DateUtil.getCurDate();
		String endDate=DateUtil.getCurDate();
		if(approvalStatDto.getTaskStartTime()!=null){
			startDate = approvalStatDto.getTaskStartTime().substring(0, 10);
		}
		if(approvalStatDto.getTaskEndTime()!=null){
			endDate = approvalStatDto.getTaskEndTime().substring(0, 10);
		}
		long totalSecondSum = Long.parseLong(approvalStatDto.getHoldSecond().toString());//先得到总的秒数
		InstanceStatDto statDayDto =queryHolidaySumAndMinMAxDate(dayMap,startDate, endDate);
//		InstanceStatDto statDayDto = instanceStatDao.queryHolidaySumAndMinMAxDate(startDate, endDate);
		int holidayDaySum = Integer.parseInt(statDayDto.getHourSum());
		String minDate = statDayDto.getStartDate();
		String maxDate = statDayDto.getEndDate();
		if(holidayDaySum>0){
			//对几种情况进行特殊处理
			//case 1: 开始和结束是同一天,且都是节假日
			if(startDate.equals(minDate) && minDate.equals(maxDate) && endDate.equals(maxDate)){
				holidayDaySum = 0;
				totalSecondSum = 0;
			} else {
				
				if(startDate.equals(minDate)){//如果开始时间是节假日
					long secondSum = DateHourUtils.secondsBetween(approvalStatDto.getTaskStartTime(), startDate+" 23:59:59");
					totalSecondSum = totalSecondSum -secondSum;
				}
				if(endDate.equals(maxDate)){//如果结束时间是节假日
					long secondSum = DateHourUtils.secondsBetween(endDate+" 00:00:00", approvalStatDto.getTaskEndTime());
					totalSecondSum = totalSecondSum -secondSum;
				}
				
				if(totalSecondSum<0){
					totalSecondSum = 0;
				}
				
				if(totalSecondSum>0){
					int tempSum = 0;
					if(startDate.equals(minDate)){
						tempSum++;
					}
					if(endDate.equals(maxDate)){
						tempSum++;
					}
					totalSecondSum = totalSecondSum - (holidayDaySum-tempSum)*24*3600;//先扣中间(不含两头的)的节假日
				}
			}
			if(totalSecondSum<0){
				totalSecondSum = 0;
			}
		}
		BigDecimal b1 = new BigDecimal(String.valueOf(totalSecondSum));
		BigDecimal b2 = new BigDecimal("3600");
		float hourSum =  b1.divide(b2, 2, BigDecimal.ROUND_HALF_EVEN).floatValue();
//		long hourSum = totalSecondSum/3600;
		return hourSum;
	}

	private Map<String, String> listToMap(List<AppSystem> appSystemList) {
		Map<String, String> resultMap=new HashMap<String,String>();
		if(appSystemList!=null && appSystemList.size()>0){
			for(int i=0;i<appSystemList.size();i++){
				AppSystem appSystem=appSystemList.get(i);
				resultMap.put(appSystem.getId(), appSystem.getName());
			}
		}
		return resultMap;
	}
}
