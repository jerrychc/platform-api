package com.xinleju.platform.generation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateFormatUtils;

import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * 生成Insert和DELETE的SQL脚本和Dubbo的customer和producer的配置的工具类
 * @author zhengjiajie
 *
 */
public class CreateSQLScriptConfigUtils {
	
	private final static  String dbInfoId = "000fbb2eef694532ab9df8c83632cba6";

	public static void main(String[] args) {
		executeCreateFLowDBData();
	}
	/**
	 * 执行生成流程相关的数据库的相关脚本, 
	 * 生成的内容有: DELETE和INSERT语句(可在MYSQL客户端执行)、tableID的值、customer和producer的配置代码
	 */
	public static void executeCreateFLowDBData(){
		StringBuffer customerSB = new StringBuffer("");
		StringBuffer producerSB = new StringBuffer("");
		List<String> tblIdList = new ArrayList<String>();
		List<String> instanceNameList = new ArrayList<String>();
		//List<LocalTableScheme> tblList = FlowDBData.addFlowDataData();
		//List<LocalTableScheme> tblList = FlowDBDataZhengjj.addFlowDataData();
		List<LocalTableScheme> tblList = FlowSimplifiedTextZhengjj.addFlowDataData();
		
		for(LocalTableScheme tblScheme : tblList){
			createInsertSQLOfTableScheme(tblScheme);
			for(LocalTableColumn tblCol : tblScheme.getColumnList()){
				createInsertSQLOfTableColumn(tblCol);
			}
			tblIdList.add(tblScheme.getTableId());
			instanceNameList.add(tblScheme.getInstanceName());
			customerSB.append(tblScheme.getCustomerConfig()).append("\n");
			producerSB.append(tblScheme.getProducerConfig()).append("\n");
		}
		System.out.println("\nTableID数组:\n "+ JacksonUtils.toJson(tblIdList));
		System.out.println("instanceNameList数组:\n "+ JacksonUtils.toJson(instanceNameList)+"\n");
		System.out.println("customer config:\n"+customerSB.toString());
		System.out.println("producer config:\n"+producerSB.toString());
	}
	
	/**
	 * 根据LocalTableColumn生成systable_column的INSERT语句
	 * @param col
	 */
	public static void createInsertSQLOfTableColumn(LocalTableColumn col){
		String columnSQL = "INSERT INTO pt_generation_systable_column ("+
				"max_value, min_value, max_length, min_length, validate_type,"+
				"sort, settings, show_type, query_type, is_show,"+
				"is_query, is_list, is_update, is_insert, java_field,"+
				"java_type, is_persistent, is_null, is_fk, is_pk,"+
				"comments, column_length, column_type, column_name, show_name,"+
				"name, systable_id, id, create_person_id, create_person_name,"+
				"create_org_id, create_org_name, create_company_id, create_company_name, create_date,"+
				"update_person_id, update_person_name, update_date, delflag, concurrency_version) "+
				"VALUES ('', '', '', '', '','";
		StringBuffer sbCol = new StringBuffer(columnSQL);
		sbCol.append(col.getSort()).append("', '', '文本框', '范围', '0','0', '0', '0', '0','")
		.append(col.getJavaField()).append("','").append(col.getJavaType()).append("', '', '', '', '','");
		// '1', '', '文本框', '范围', '0','0', '0', '0', '0','accountId',";
		//'String', '', '', '', '',
		sbCol.append(col.getComment()).append("', '").append(col.getColumnLength()).append("', '")
		.append(col.getColumnType()).append("', '").append(col.getColumnName()).append("', '").append(col.getShowName());
		//'单位ID', '32', 'varchar', 'account_id', '单位ID', 
		
		sbCol.append("', '").append(col.getShowName()).append("', '")
		.append(col.getTableId()).append("', '").append(col.getColumnId())
		.append("', 'wangw', '管理员', '', '', '"+dateTime()+"', '', null, '', '', null, '0', null);");
		//'单位ID', '000fbb2eef692ab9dmeeting20171120', '000fbb2eef692ab9dc1a2b3c20170003', 'wangw', '管理员',
		// '', '', '', '', null, '', '', null, '0', null);	
		System.out.println("-- column(tableID="+col.getTableId()+"):  \n"+ sbCol.toString());
	}

	/**
	 * 根据LocalTableColumn生成systable和scheme的DELETE和INSERT语句
	 * @param col
	 */
	public static void createInsertSQLOfTableScheme(LocalTableScheme tblScheme){
		
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM pt_generation_systable where id='"+tblScheme.getTableId()+"'; \nINSERT INTO pt_generation_systable(")
		.append("id, issync, class_name, table_type, comments,")
		.append("show_name, table_name, remarks, parent_table_fk, parent_table,")
		.append("dbinfo_id, create_person_id, create_person_name, create_org_id, create_org_name,")
		.append("create_company_id, create_company_name, create_date, update_person_id, update_person_name,") 
		.append("update_date, delflag, concurrency_version) ");
		sb.append(" VALUES ('"+tblScheme.getTableId()+"', '0', '"+tblScheme.getClassName()+"', '0', '"+tblScheme.getShowName()+"', ")
		.append("'"+tblScheme.getShowName()+"', '"+tblScheme.getTableName()+"', '', '', '', ")
	    .append("'"+dbInfoId+"', 'admin', '管理员', '', '', ")
		.append("'', '', '"+dateTime()+"', '', '', null, '0', null);");
		System.out.println("\n\n-- systable(tableID="+tblScheme.getTableId()+"):  \n"+ sb.toString()+"\n");
		
		String schemeSQL = "DELETE FROM pt_generation_scheme where systable_id='"+tblScheme.getTableId()+"'; \nINSERT INTO pt_generation_scheme("+
		"remarks, describes, author, function_name_simple, function_name,"+ 
		"sub_module_name, module_name, local_url, package_name, code_style,"+
		"scheme_name, systable_id,  id, create_person_id, create_person_name,"+
		"create_org_id, create_org_name, create_company_id, create_company_name,create_date ,"+
		"update_person_id, update_person_name, update_date, delflag, concurrency_version) "+
		"values ('default-value', '";
		
		StringBuffer sbScheme = new StringBuffer(schemeSQL);
		sbScheme.append(tblScheme.getShowName()).append("', 'admin', 'default-value', 'functionname','")
		.append(tblScheme.getSubmoduleName()).append("', '").append(tblScheme.getModuleName())
		.append("', 'C:/code', '").append(tblScheme.getPackageName()).append("', '0','生成")
		.append(tblScheme.getShowName()).append("类方案', '").append(tblScheme.getTableId())
		.append("', '").append(tblScheme.getSchemeId()).append("', 'admin', '管理员',")
		.append("null, null, null, null, '"+dateTime()+"',  null, null, null, '0', null);");
		System.out.println("-- scheme(tableID="+tblScheme.getTableId()+"):  \n"+ sbScheme.toString()+"\n");
		String deleteColumnSQL = "delete from pt_generation_systable_column where systable_id='"+tblScheme.getTableId()+"';";
		System.out.println(deleteColumnSQL+"\n");
	}
	
	/**
	 * 根据tableSchemeText和tableColumnText及其其他参数,生成LocalTableScheme的实例对象
	 * @param tableSchemeText
	 * @param tableColumnText
	 * @param baseID
	 * @param basePackage
	 * @param module
	 * @param submodule
	 * @return
	 */
	public LocalTableScheme createFromText(String tableSchemeText, String tableColumnText, 
			String baseID, String basePackage, String module, String submodule) {
		
		LocalTableScheme tblScheme = createSingleTableScheme(tableSchemeText, baseID, basePackage, module, submodule);
		
		List<LocalTableColumn> columnList = new ArrayList<LocalTableColumn>();
		//System.out.println("tableColumnText="+tableColumnText);
		String tableColumns[] = tableColumnText.split("&&");
		for(int i=0; i<tableColumns.length; i++){
			LocalTableColumn tblCol = new LocalTableColumn();
			tblCol.setTableId(tblScheme.getTableId());
			tblCol.setSort(""+(i+1));
			tblCol.setColumnId(tblScheme.getTableId()+"-00"+tblCol.getSort());
			String columnItemText = tableColumns[i];
			
			String colItems[] = columnItemText.split("##");
			//System.out.println("开始处理:"+ (i+1)+"=>>"+columnItemText+"; colItems.length="+colItems.length);
			tblCol.setJavaField(colItems[0]);
			tblCol.setJavaType(colItems[1]);
			if("NA".equals(colItems[2])){
				tblCol.setColumnLength("");
			}else{
				tblCol.setColumnLength(colItems[2]);
			}
			tblCol.setColumnType(colItems[3]);
			tblCol.setColumnName(colItems[4]);
			tblCol.setShowName(colItems[5]);
			columnList.add(tblCol);
		}
		tblScheme.setColumnList(columnList);
		return tblScheme;
	}
	
	//+"&&paramUrl##String##1000##varchar##param_url##"
	
	public LocalTableScheme createFromSimplerText(String tableSchemeText, String tableColumnText, 
			String baseID, String basePackage, String module, String submodule) {
		LocalTableScheme tblScheme = createSingleTableScheme(tableSchemeText, baseID, basePackage, module, submodule);
		List<LocalTableColumn> columnList = new ArrayList<LocalTableColumn>();
		//System.out.println("tableColumnText="+tableColumnText);
		String tableColumns[] = tableColumnText.split("&&");
		for(int i=0; i<tableColumns.length; i++){
			LocalTableColumn tblCol = new LocalTableColumn();
			tblCol.setTableId(tblScheme.getTableId());
			tblCol.setSort(""+(i+1));
			tblCol.setColumnId(tblScheme.getTableId()+"-00"+tblCol.getSort());
			String columnItemText = tableColumns[i];
			
			//"业务对象变量取值url##param_url##varchar(1000)"
			//postMultiPerson##String##2##varchar##post_multiPerson##同岗多人策略"
			System.out.println("columnItemText="+columnItemText);
			String colItems[] = columnItemText.split("##");
			tblCol.setShowName(colItems[0]);
			tblCol.setColumnName(colItems[1]);
			tblCol.setJavaField(lineToHump(tblCol.getColumnName()));
			String typeLength = colItems[2];
			if(colItems.length>=4){
				tblCol.setComment(colItems[0]+": "+colItems[3]);
			}else{
				tblCol.setComment(colItems[0]);
			}
			if(typeLength.contains("varchar")){//如果是"varchar"类型
				tblCol.setColumnType("varchar");
				String lengthText = typeLength.replaceAll("varchar", "");
				tblCol.setColumnLength(lengthText);
				tblCol.setJavaType("String");
			}else{//如果不是,应该是datetime , tinyint, int bigint的一种
				tblCol.setColumnType(typeLength);
				tblCol.setColumnLength("");
				if("bigint".equals(typeLength)){
					tblCol.setJavaType("Long");
				}else if("int".equals(typeLength)){
					tblCol.setJavaType("Integer");
				}else if("bool".equals(typeLength)){
					tblCol.setJavaType("Boolean");
				}else if("timestamp".equals(typeLength)){ //timestamp --> Timestamp
					tblCol.setJavaType("Timestamp");
				}else {
					tblCol.setJavaType("String");
				}
			}
			columnList.add(tblCol);
		}
		tblScheme.setColumnList(columnList);
		return tblScheme;
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
	
	public LocalTableScheme createSingleTableScheme(String tableSchemeText, 
			String baseID, String basePackage, String module, String submodule){
		String items[] = tableSchemeText.split("##");
		LocalTableScheme tblScheme = new LocalTableScheme();
		tblScheme.setTableId(baseID+module+submodule+"-"+items[0]);
		tblScheme.setSchemeId(tblScheme.getTableId()+timeNumText());
		tblScheme.setClassName(items[1]);
		tblScheme.setTableName(items[2]);
		tblScheme.setShowName(items[3]);
		tblScheme.setModuleName(module);
		tblScheme.setSubmoduleName(submodule);
		if("".equals(submodule)){
			tblScheme.setPackageName(basePackage+"."+module);
		}else{
			tblScheme.setPackageName(basePackage+"."+submodule+"."+module);
		}
		return tblScheme;
	}
	
	public static void testJSONTextToObject(String jsonText){
		LocalTableScheme tblScheme = JacksonUtils.fromJson(jsonText, LocalTableScheme.class);
		System.out.println("tableId="+tblScheme.getTableId()+
				"; className="+tblScheme.getClassName()+"; size="+tblScheme.getColumnList().size());
	}
	
	 //首字母大写
    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }
    
	public static String dateTime() {
		Date date = new Date();
		return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String dateTimeNumText() {
		Date date = new Date();
		return DateFormatUtils.format(date, "yyyyMMddHHmmssSSS");
	}
	
	public static String timeNumText() {
		Date date = new Date();
		return DateFormatUtils.format(date, "HHmmssSSS");
	}
	
	public static String dateNumText() {
		Date date = new Date();
		return DateFormatUtils.format(date, "yyyyMMdd");
	}
}
