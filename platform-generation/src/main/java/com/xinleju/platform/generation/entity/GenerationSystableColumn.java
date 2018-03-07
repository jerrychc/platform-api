package com.xinleju.platform.generation.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * @author sy
 * 
 *
 */
@Table(value="PT_GENERATION_SYSTABLE_COLUMN",desc="系统业务表字段")
public class GenerationSystableColumn extends BaseEntity{
	
	
	@Column(value="max_value",desc="最大值")
	private String max_Value;
	
	@Column(value="min_value",desc="最小值")
	private String minValue;
	
	@Column(value="max_length",desc="最大长度")
	private String maxLength;
	
	@Column(value="min_length",desc="最小长度")
	private String minLength;   
	
	@Column(value="validate_type",desc="校验类型")
	private String validateType;
	
	@Column(value="sort",desc="排序")
	private String sort;         
	
	@Column(value="settings",desc="其他的一些设置（json）")
	private String settings;     
	
	@Column(value="show_type",desc="控件类型（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）")
	private String showType;    
	
	@Column(value="query_type",desc="查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）")
	private String queryType;   
	
	@Column(value="is_show",desc="是否在表单中进行显示（或者隐藏）")
	private String isShow;      
	
	@Column(value="is_query",desc="是否为查询条件字段")
	private String isQuery;     
	
	@Column(value="is_list",desc="是否为列表字段")
	private String isList;      
	
	@Column(value="is_update",desc="是否为编辑字段")
	private String isUpdate;    
	
	@Column(value="is_insert",desc="是否为插入字段")
	private String isInsert;    
	
	@Column(value="java_field",desc="Java属性名称")
	private String javaField;   
	
	@Column(value="java_type",desc="Java类型")
	private String javaType;    
	
	@Column(value="is_persistent",desc="是否持久化")
	private String isPersistent;
	
	@Column(value="is_null",desc="是否为空")
	private String isNull;      
	
	@Column(value="is_fk",desc="是否外键")
	private String isFk;        
	
	@Column(value="is_pk",desc="是否主键")
	private String isPk;        
	
	@Column(value="comments",desc="列的说明注释")
	private String comments;     
	
	@Column(value="column_length",desc="列的数据类型的字节长度")
	private String columnLength;
	
	@Column(value="column_type",desc="列类型")
	private String columnType;  
	
	@Column(value="column_name",desc="列名称")
	private String columnName;  
	
	@Column(value="show_name",desc="显示名称")
	private String showName;    
	
	@Column(value="name",desc="名称")
	private String name;         
	
	@Column(value="systable_id",desc="系统业务表ID")
	private String systableId;


	public String getMax_Value() {
		return max_Value;
	}

	public void setMax_Value(String max_Value) {
		this.max_Value = max_Value;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}


	public String getMinLength() {
		return minLength;
	}

	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}

	public String getValidateType() {
		return validateType;
	}

	public void setValidateType(String validateType) {
		this.validateType = validateType;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSettings() {
		return settings;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getIsQuery() {
		return isQuery;
	}

	public void setIsQuery(String isQuery) {
		this.isQuery = isQuery;
	}

	public String getIsList() {
		return isList;
	}

	public void setIsList(String isList) {
		this.isList = isList;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getIsInsert() {
		return isInsert;
	}

	public void setIsInsert(String isInsert) {
		this.isInsert = isInsert;
	}

	public String getJavaField() {
		return javaField;
	}

	public void setJavaField(String javaField) {
		this.javaField = javaField;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getIsPersistent() {
		return isPersistent;
	}

	public void setIsPersistent(String isPersistent) {
		this.isPersistent = isPersistent;
	}

	public String getIsNull() {
		return isNull;
	}

	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}

	public String getIsFk() {
		return isFk;
	}

	public void setIsFk(String isFk) {
		this.isFk = isFk;
	}

	public String getIsPk() {
		return isPk;
	}

	public void setIsPk(String isPk) {
		this.isPk = isPk;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getColumnLength() {
		return columnLength;
	}

	public void setColumnLength(String columnLength) {
		this.columnLength = columnLength;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSystableId() {
		return systableId;
	}

	public void setSystableId(String systableId) {
		this.systableId = systableId;
	}



}
