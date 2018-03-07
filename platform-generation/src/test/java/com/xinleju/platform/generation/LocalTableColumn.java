package com.xinleju.platform.generation;

/**
 * 本地TableColumn对象
 * @author zhengjiajie
 *
 */
public class LocalTableColumn {
	
	private String tableId, columnId, javaField, javaType, columnLength, 
	columnType, columnName, showName, sort, comment;

	public String getComment() {
		if(comment == null || "".equals(comment)){
			comment = this.getShowName();
		}
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
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

}
