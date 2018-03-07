package com.xinleju.platform.generation;

import java.util.List;

/**
 * 本地TableScheme对象
 * @author zhengjiajie
 *
 */
public class LocalTableScheme {
	
	private String tableId, className, tableName, showName;

	private String schemeId, packageName, moduleName, submoduleName;
	
	private List<LocalTableColumn> columnList;
	
	/*remarks, describes, author, function_name_simple, function_name, 
	  sub_module_name, module_name, local_url, package_name, code_style,
	  scheme_name, systable_id,  id, create_person_id, create_person_name,*/


	public String getInstanceName() {
		String clazzName = this.getClassName();
		String firstLetter =  clazzName.substring(0, 1);
		clazzName = firstLetter.toLowerCase()+clazzName.substring(1);
		return clazzName;
	}

	public List<LocalTableColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<LocalTableColumn> columnList) {
		this.columnList = columnList;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getSubmoduleName() {
		return submoduleName;
	}

	public void setSubmoduleName(String submoduleName) {
		this.submoduleName = submoduleName;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getCustomerConfig(){
		String clazzName = this.getClassName();
		String firstLetter =  clazzName.substring(0, 1);
		clazzName = firstLetter.toLowerCase()+clazzName.substring(1);
		return "<dubbo:reference id=\""+clazzName+"DtoServiceCustomer\"        interface=\""+this.getPackageName()+".dto.service."+this.getClassName()+"DtoServiceCustomer\" />\n";
	}
	
	public String getProducerConfig(){
		String clazzName = this.getClassName();
		String firstLetter =  clazzName.substring(0, 1);
		clazzName = firstLetter.toLowerCase()+clazzName.substring(1);
		String fullText = "<dubbo:service interface=\""+this.getPackageName()+".dto.service."+this.getClassName()+"DtoServiceCustomer\"       ref=\""+clazzName+"DtoServiceProducer\" retries=\"0\" timeout=\"50000\" />";
		return fullText + "\n<bean id=\""+clazzName+"DtoServiceProducer\"       class=\""+this.getPackageName()+".dto.service.impl."+this.getClassName()+"DtoServiceProducer\"></bean>\n"; 
	}
	
}
