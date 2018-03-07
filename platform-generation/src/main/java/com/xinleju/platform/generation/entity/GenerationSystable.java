package com.xinleju.platform.generation.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * @author sy
 * 
 *
 */
@Table(value="PT_GENERATION_SYSTABLE",desc="系统业务表")
public class GenerationSystable extends BaseEntity{
	
	
	@Column(value="issync",desc="是否已经同步到数据库")
	private String issync;
	
	@Column(value="remarks",desc="备注信息")
	private String remarks;
	
	@Column(value="parent_table_fk",desc="关联父表的外键")
	private String parentTableFk;
	
	@Column(value="parent_table",desc="关联父表")
	private String parentTable;
	
	@Column(value="class_name",desc="对应的实体类名称")
	private String className;
	
	@Column(value="table_type",desc="此表的类型（单表、主表、附表、树结构表等）")
	private String tableType;
	
	@Column(value="comments",desc="表的说明")
	private String comments;
	
	@Column(value="show_name",desc="显示名称")
	private String showName;
	
	@Column(value="table_name",desc="表名")
	private String tableName;
	
	@Column(value="dbinfo_id",desc="外键系统数据库信息表ID")
	private String dbinfoId;

	public String getIssync() {
		return issync;
	}

	public void setIssync(String issync) {
		this.issync = issync;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getParentTableFk() {
		return parentTableFk;
	}

	public void setParentTableFk(String parentTableFk) {
		this.parentTableFk = parentTableFk;
	}

	public String getParentTable() {
		return parentTable;
	}

	public void setParentTable(String parentTable) {
		this.parentTable = parentTable;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDbinfoId() {
		return dbinfoId;
	}

	public void setDbinfoId(String dbinfoId) {
		this.dbinfoId = dbinfoId;
	}


	
}
