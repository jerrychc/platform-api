package com.xinleju.platform.generation.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * @author sy
 * 
 *
 */
@Table(value="PT_GENERATION_DBINFO",desc="不同系统的数据库信息")
public class GenerationDbinfo extends BaseEntity{
	
	@Column(value="datasource",desc="数据源")
	private String datasource;
	
	@Column(value="db_port",desc="数据库端口")
	private String dbPort;
	
	@Column(value="db_ip",desc="数据库IP")
	private String dbIp;
	
	@Column(value="db_password",desc="数据库密码")
	private String dbPassword;
	
	@Column(value="db_username",desc="数据库用户名")
	private String dbUsername;
	
	@Column(value="db_instance_name",desc="数据库实例名")
	private String dbInstanceName;
	
	@Column(value="db_type",desc="数据库类型")
	private String dbType;
	
	@Column(value="syscode",desc="系统CODE")
	private String syscode;
	
	@Column(value="sysname",desc="系统名称")
	private String sysname;
	
	public String getDatasource() {
		return datasource;
	}
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	public String getDbPort() {
		return dbPort;
	}
	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}
	public String getDbIp() {
		return dbIp;
	}
	public void setDbIp(String dbIp) {
		this.dbIp = dbIp;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public String getDbUsername() {
		return dbUsername;
	}
	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}
	public String getDbInstanceName() {
		return dbInstanceName;
	}
	public void setDbInstanceName(String dbInstanceName) {
		this.dbInstanceName = dbInstanceName;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getSyscode() {
		return syscode;
	}
	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	
}
