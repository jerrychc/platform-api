package com.xinleju.platform.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 流程相关的数据库相关对象
 * @author zhengjiajie
 *
 */
public class FlowDBDataZhengjj {
	public static final String baseID = "platform-zhengjj-";
	public static final String basePKG = "com.xinleju.platform";
	public static final String module = "flow";
	public static final String submodule = "";
	
	public static List<LocalTableScheme> addFlowDataData(){
		CreateSQLScriptConfigUtils tsUtil = new CreateSQLScriptConfigUtils();
		List<LocalTableScheme>  tblList = new ArrayList<LocalTableScheme>();
		
		// 0218-001 已经生成pt_flow_business_object_variable
		//tableId##className##tableName##showName
		String tblSchm101 = "0218-001##BusinessObjectVariable##pt_flow_business_object_variable##业务对象变量";
		//javaField##javaType##columnLength##columnType##columnName##showName&&javaField##javaType##columnLength##columnType##columnName##showName 
		//长度没有数据的话,用NA替代
		String tblCol101 ="name##String##200##varchar##name##名称&&keyName##String##32##varchar##key_name##键名";
		tblCol101+="&&type##String##32##varchar##type##变量类型&&relationCode##String##32##varchar##relation_code##关联组件编号"+
		"&&businessObjectId##String##32##varchar##business_object_id##业务对象id"
		+"&&parentId##String##32##varchar##parent_id##上级";
		LocalTableScheme tblScheme101 = tsUtil.createFromText(tblSchm101, tblCol101, baseID, basePKG, module, submodule);
		tblList.add(tblScheme101);
		
		// 0218-002 已经生成pt_flow_business_object
		//tableId##className##tableName##showName
		String tblSchm102 = "0218-002##BusinessObject##pt_flow_business_object##流程业务对象";
		//javaField##javaType##columnLength##columnType##columnName##showName&&javaField##javaType##columnLength##columnType##columnName##showName 
		//长度没有数据的话,用NA替代
		String tblCol102 ="code##String##100##varchar##code##编号&&name##String##200##varchar##name##名称"
		+"&&pcUrl##String##1000##varchar##pc_url##pc业务表单访问url"
		+"&&phoneUrl##String##1000##varchar##phone_url##phone业务表单访问url"+
		"&&phoneBusinessUrl##String##1000##varchar##phone_business_url##phone业务表单键值对url"
		+"&&paramUrl##String##1000##varchar##param_url##业务对象变量取值url"
		+"&&appId##String##32##varchar##app_id##系统id";
		LocalTableScheme tblScheme102 = tsUtil.createFromText(tblSchm102, tblCol102, baseID, basePKG, module, submodule);
		tblList.add(tblScheme102);
		
		// 0218-003 已经生成pt_flow_fl
		//tableId##className##tableName##showName
		String tblSchm103 = "0218-003##Fl##pt_flow_fl##流程模板";
		//javaField##javaType##columnLength##columnType##columnName##showName&&javaField##javaType##columnLength##columnType##columnName##showName 
		//长度没有数据的话,用NA替代
		String tblCol103 ="code##String##100##varchar##code##编号"
		+ "&&name##String##200##varchar##name##名称"
		+"&&flowTitle##String##100##varchar##flow_title##默认标题规则"
		+"&&titleUpdate##boolean##NA##tinyint##title_update##是否修改标题"
		//4
		+"&&businessObjectId##String##32##varchar##business_object_id##业务对象id"
		+"&&isDefault##boolean##NA##tinyint##is_default##是否默认"
		+"&&version##Long##NA##bigint##version##版本"
		+ "&&sort##Long##NA##bigint##sort##序号"
		
		//4
		+"&&approvalRepeat##String##2##varchar##approval_repeat##审批人重复策略"
		+"&&postIsNull##String##2##varchar##post_isNull##岗位为空策略"
		+"&&approvalPersonIsNull##String##2##varchar##approvalPerson_isNull##审批人为空策略"
		+"&&postMultiPerson##String##2##varchar##post_multiPerson##同岗多人审批策略"
		//4
		+"&&retract##boolean##NA##tinyint##retract##发起人是否可撤回"
		+"&&useStatus##boolean##NA##tinyint##use_status##流程状态"
		+"&&remark##String##2000##varchar##remark##流程描述"
		+"&&status##String##2##varchar##status##发布状态";
		LocalTableScheme tblScheme103 = tsUtil.createFromText(tblSchm103, tblCol103, baseID, basePKG, module, submodule);
		tblList.add(tblScheme103);
				
		// 0218-004 已经生成pt_flow_step
		//tableId##className##tableName##showName
		String tblSchm104 = "0218-004##Step##pt_flow_step##模板环节连线";
		//javaField##javaType##columnLength##columnType##columnName##showName&&javaField##javaType##columnLength##columnType##columnName##showName 
		//长度没有数据的话,用NA替代
		String tblCol104 ="code##String##100##varchar##code##编号"
		+ "&&name##String##200##varchar##name##名称"
		+"&&flId##String##32##varchar##fl_id##流程模板Id"
		+"&&sourceId##String##32##varchar##source_id##源节点"
		+"&&targetId##String##32##varchar##target_id##目标结点"
		
		+"&&startX##Long##NA##bigint##start_x##开始点x坐标"
		+"&&startY##Long##NA##bigint##start_y##开始点y坐标"
		+"&&targetX##Long##NA##bigint##target_x##目标点x坐标"
		+"&&targetY##Long##NA##bigint##target_y##目标点y坐标"
		+"&&conditionExpression##String##1000##varchar##condition_expression##条件表达式";
		LocalTableScheme tblScheme104 = tsUtil.createFromText(tblSchm104, tblCol104, baseID, basePKG, module, submodule);
		tblList.add(tblScheme104);
		
		
		// 0218-005已经生成pt_flow_step
		//tableId##className##tableName##showName
		String tblSchm105 = "0218-005##AC##pt_flow_ac##流程模板环节";
		//javaField##javaType##columnLength##columnType##columnName##showName&&
		//长度没有数据的话,用NA替代  5+4+5
		String tblCol105 ="code##String##100##varchar##code##编号"
		+ "&&name##String##200##varchar##name##名称"
		+"&&flId##String##32##varchar##fl_id##流程模板Id"
		+"&&acType##String##2##varchar##ac_type##节点类型"
		+"&&approveTypeId##String##32##varchar##approve_type_id##审批类型id"
		
		+"&&x##Long##NA##bigint##x##x坐标"
		+"&&y##Long##NA##bigint##y##y坐标"
		+"&&width##Long##NA##bigint##width##宽"
		+"&&heigth##Long##NA##bigint##heigth##高"
		
        +"&&isAddLabel##boolean##NA##tinyint##is_add_label##手工选择审批人"
        +"&&isStart##boolean##NA##tinyint##is_start##手工选择审批人为空是否发起"
		+"&&approveStrategy##String##2##varchar##approve_strategy##多岗策略"
		+"&&postMultiPerson##String##2##varchar##post_multiPerson##同岗多人策略"
		+"&&remark##String##2000##varchar##remark##描述";
		LocalTableScheme tblScheme105 = tsUtil.createFromText(tblSchm105, tblCol105, baseID, basePKG, module, submodule);
		tblList.add(tblScheme105);
				
		return tblList;
	}
	
	 //首字母大写
    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }
    
	public static void main(String[] args) {
		String testText = "varchar(200)";
		testText = testText.replaceFirst("varchar", "");
		System.out.println("testText="+testText);
		
		testText = testText.replaceFirst("varchar", "");
		System.out.println("testText="+testText);
		
		//addFlowDataData();
		/*String clazzName = "SingleItem";
		String firstLetter =  clazzName.substring(0, 1);
		clazzName = firstLetter.toLowerCase()+clazzName.substring(1);
		System.out.println("clazzName="+clazzName);*/
		System.out.println("new word="+captureName("dsafdsa"));
		/*String textStr ="startX##Long#NA##bigint##start_x##开始点x坐标";
		String colItems[] = textStr.split("##");
		int index = 0;
		for(String item : colItems){
			System.out.println("index="+(index++)+"; item="+item);
		}*/
		//String lineToHump = lineToHump("f_parent_no_leader");  
       // System.out.println(lineToHump);//fParentNoLeader  
        //System.out.println(humpToLine(lineToHump));//f_parent_no_leader  
        //System.out.println(humpToLine2(lineToHump));//f_parent_no_leade
	}
	
	private static Pattern linePattern = Pattern.compile("_(\\w)");  
    /**下划线转驼峰*/  
    public static String lineToHump(String str){  
        str = str.toLowerCase();  
        Matcher matcher = linePattern.matcher(str);  
        StringBuffer sb = new StringBuffer();  
        while(matcher.find()){  
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());  
        }  
        matcher.appendTail(sb);  
        return sb.toString();  
    }  
    /**驼峰转下划线(简单写法，效率低于{@link #humpToLine2(String)})*/  
    public static String humpToLine(String str){  
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();  
    }  
    private static Pattern humpPattern = Pattern.compile("[A-Z]");  
    /**驼峰转下划线,效率比上面高*/  
    public static String humpToLine2(String str){  
        Matcher matcher = humpPattern.matcher(str);  
        StringBuffer sb = new StringBuffer();  
        while(matcher.find()){  
            matcher.appendReplacement(sb, "_"+matcher.group(0).toLowerCase());  
        }  
        matcher.appendTail(sb);  
        return sb.toString();  
    }
}
