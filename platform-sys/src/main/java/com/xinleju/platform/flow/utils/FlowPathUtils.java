package com.xinleju.platform.flow.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.xinleju.platform.flow.dto.AcDto;
import com.xinleju.platform.flow.dto.NodeDto;
import com.xinleju.platform.flow.entity.Ac;
import com.xinleju.platform.flow.entity.Step;

/**
 * 搜索流程审批路径
 */
public class FlowPathUtils {
	
	private static Logger log = Logger.getLogger(FlowPathUtils.class);
	
	
	/**
	 * 获取可以通过环节列表
	 * 
	 * @param firstAc
	 * @param acMap
	 * @param stepSourceMap
	 * @param stepTargetMap
	 * @param businessVariableValue
	 * @return
	 */
	public static List<NodeDto>    getPassAcDtoNode(Ac firstAc,Map<String, Ac> acMap, Map<String, List<Step>> stepSourceMap, Map<String, List<Step>> stepTargetMap,Map<String, Object> businessVariableValue){
		List<NodeDto> nodeDtos=new ArrayList<NodeDto>();
		Map<String, Ac> result=getPassAcMap( firstAc, acMap, stepSourceMap,businessVariableValue);
		
		//add by zhangdaoqiang
		removeNotPassStepFromTarget(stepTargetMap, businessVariableValue);
		
		//装载发起节点
		Map<String, Ac> passAcMap =new LinkedHashMap<String, Ac>();
		passAcMap.put(firstAc.getId(), firstAc);
		passAcMap.putAll(result);
		NodeDto  endNodeDto=null;
		for (String key : passAcMap.keySet()) {
			NodeDto nodeDto = new NodeDto();
			Ac tempAc = passAcMap.get(key);
			AcDto tempAcDto=new AcDto();
			BeanUtils.copyProperties(tempAc, tempAcDto);
			nodeDto.setCurrentAcDto(tempAcDto);
			// 记录上一个节点
			List<AcDto> preAcDtos=new ArrayList<AcDto>();
			List<Step> preStep=stepTargetMap.get(key);
			if (preStep != null && preStep.size() > 0) {
				for (Step step : preStep) {
					 Ac ac=passAcMap.get(step.getSourceId());
					 if(ac!=null){
						 tempAcDto=new AcDto();
						 BeanUtils.copyProperties(ac, tempAcDto);
						 preAcDtos.add(tempAcDto);
					 }
				}
			}
			nodeDto.setPreviousAcDtos(preAcDtos);
			//过下一节点查找
			
			List<AcDto> nextAcDtos=new ArrayList<AcDto>();
			List<Step> nextStep=stepSourceMap.get(key);
			if (nextStep != null && nextStep.size() > 0) {
				for (Step step : nextStep) {
					 Ac ac=passAcMap.get(step.getTargetId());
					 if(ac!=null){
						 tempAcDto=new AcDto();
						 BeanUtils.copyProperties(ac, tempAcDto);
						 nextAcDtos.add(tempAcDto);
					 }
				}
			}
			nodeDto.setNextNodeDtos(nextAcDtos);
		
			if("3".equals(nodeDto.getCurrentAcDto().getAcType())){
				endNodeDto=nodeDto;
			}else{
				nodeDtos.add(nodeDto);
			}
	  }
	   //排序加工
	  if(endNodeDto!=null){
		 nodeDtos.add(endNodeDto);
	  }
	   return nodeDtos;
	}

	
	private static void removeNotPassStepFromTarget(Map<String, List<Step>> stepTargetMap,
			Map<String, Object> businessVariableValue) {

		for (List<Step> linkStep : stepTargetMap.values()) {
			// 表达式
			if (linkStep != null) {
				Iterator<Step> iter = linkStep.iterator();
				while (iter.hasNext()) {
					Step l = iter.next();
					String expression = l.getConditionExpression();
					if (StringUtils.isNotEmpty(expression)) {
						if (!ExpressionUtils.evaluate(expression, businessVariableValue)) {
							iter.remove();
						}
					} 
				}
			}
		}
	}

	/**
	 *获取更能经过的环节
	 * @param currentAC
	 * @param acMap
	 * @param stepMap
	 * @param businessVariableValue
	 * @return
	 */
	public static Map<String, Ac>    getPassAcMap(Ac currentAC,Map<String, Ac> acMap, Map<String, List<Step>> stepMap,Map<String, Object> businessVariableValue){
		   Map<String, Ac> passACs=new LinkedHashMap<String, Ac>();   
	       List<Step> passStep=new ArrayList<Step>();
	       String currentId= currentAC.getId();
	       List<Step> linkStep=stepMap.get(currentId);
	       System.out.println(currentId);
	       //表达式
	       if(linkStep!=null){
	    	   Iterator<Step> iter = linkStep.iterator();
			   while(iter.hasNext()){
				   Step l = iter.next();
				   String expression= l.getConditionExpression();
				   if(expression==null || "".equals(expression.trim())){
					   passStep.add(l);
				   }else if (ExpressionUtils.evaluate(expression, businessVariableValue)) {
					   passStep.add(l);
				   } else  {
					   iter.remove();
				   }
			   }
	       }
		   if(passStep.size()<=0){
			   return passACs;
		   }else{
			   for (Step step : passStep) {
					Ac ac = acMap.get(step.getTargetId());
					if (passACs.get(ac.getId()) != null) {
						passACs.remove(ac.getId());
						passACs.put(ac.getId(), ac);
					} else {
						passACs.put(ac.getId(), ac);
					}
					if ("3".equals(ac.getAcType())) {
						return passACs;
					}
					Map<String, Ac> resultMap =  getPassAcMap(ac,acMap, stepMap,businessVariableValue);
					for (String key : resultMap.keySet()) {
						Ac acTemp = resultMap.get(key);
						if (passACs.get(key) != null) {
							passACs.remove(key);
							passACs.put(key, acTemp);
						} else {
							passACs.put(acTemp.getId(), acTemp);
						}
					}
				}
				return passACs;
		   }
   }

}
