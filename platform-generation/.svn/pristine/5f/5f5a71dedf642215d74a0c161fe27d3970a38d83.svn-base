package com.xinleju.platform.generation;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程相关的数据库相关对象
 * @author zhengjiajie
 *
 */
public class FlowDBData {
	public static final String baseID = "abcdefghijklmn-";
	public static final String basePKG = "com.xinleju.platform";
	public static final String module = "flow";
	public static final String submodule = "";
	
	public static List<LocalTableScheme> addFlowDataData(){
		CreateSQLScriptConfigUtils tsUtil = new CreateSQLScriptConfigUtils();
		List<LocalTableScheme>  tblList = new ArrayList<LocalTableScheme>();
		
		// 101 已经生成 pt_flow_business_object
		/*
		 //tableId##className##tableName##showName
		String tblSchm101 = "101##BusinessObject##pt_flow_business_object##流程业务对象";
		//javaField##javaType##columnLength##columnType##columnName##showName&&javaField##javaType##columnLength##columnType##columnName##showName 
		//长度没有数据的话,用NA替代
		String tblCol101 ="code##String##100##varchar##code##编号&&name##String##200##varchar##name##名称";
		tblCol101+="&&url##String##1000##varchar##url##业务对象访问url&&parentId##String##32##varchar##parent_id##上级&&appId##String##32##varchar##app_id##系统id";
		LocalTableScheme tblScheme101 = tsUtil.createFromText(tblSchm101, tblCol101, baseID, basePKG, module, submodule);
		tblList.add(tblScheme101);
		*/
		
		return tblList;
	}
	
	public static void main(String[] args) {
		//addFlowDataData();
		String clazzName = "SingleItem";
		String firstLetter =  clazzName.substring(0, 1);
		clazzName = firstLetter.toLowerCase()+clazzName.substring(1);
		System.out.println("clazzName="+clazzName);
	}
}
