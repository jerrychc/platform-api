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
public class FlowSimplifiedTextZhengjj {
	public static final String baseID = "platform-zhengjj-";
	public static final String basePKG = "com.xinleju.platform";
	public static final String module = "flow";
	public static final String submodule = "";
	
	public static List<LocalTableScheme> addFlowDataData(){
		CreateSQLScriptConfigUtils tsUtil = new CreateSQLScriptConfigUtils();
		List<LocalTableScheme>  tblList = new ArrayList<LocalTableScheme>();
		
		// 0218-001 已经生成pt_flow_business_object_variable
		//tableId##className##tableName##showName
		String tblSchm101 = "0218-test01##BusinessObjectVariable##test_pt_flow_business_object_variable##业务对象变量";
		//javaField##javaType##columnLength##columnType##columnName##showName&&javaField##javaType##columnLength##columnType##columnName##showName 
		//长度没有数据的话,用NA替代
		String tblCol101 ="名称##name##varchar200&&键名##key_name##varchar32##业务备注信息";
		tblCol101+="&&业务对象id##business_object_id##varchar32##业务备注信息"
		+"&&上级##parent_id##varchar32";
		LocalTableScheme tblScheme101 = tsUtil.createFromSimplerText(tblSchm101, tblCol101, baseID, basePKG, module, submodule);
		tblList.add(tblScheme101);
		
		// 0218-002 已经生成pt_flow_business_object
		//tableId##className##tableName##showName
		String tblSchm102 = "0218-test02##BusinessObject##test_pt_flow_business_object##流程业务对象";
		//javaField##javaType##columnLength##columnType##columnName##showName&&javaField##javaType##columnLength##columnType##columnName##showName 
		//长度没有数据的话,用NA替代
		String tblCol102 ="名称##name##varchar200&&键名##key_name##varchar32##业务备注信息"
		+"&&phone业务表单键值对url##phone_business_url##varchar1000##业务备注信息";
		LocalTableScheme tblScheme102 = tsUtil.createFromSimplerText(tblSchm102, tblCol102, baseID, basePKG, module, submodule);
		tblList.add(tblScheme102);

		return tblList;
	}
	
	 //首字母大写
    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }
    
	public static void main(String[] args) {
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
		String lineToHump = lineToHump("f_parent_no_leader");  
        System.out.println(lineToHump);//fParentNoLeader  
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
