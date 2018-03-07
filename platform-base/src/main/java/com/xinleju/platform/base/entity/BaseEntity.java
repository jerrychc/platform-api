package com.xinleju.platform.base.entity;
import java.sql.Timestamp;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Key;
import com.xinleju.platform.base.annotation.Version;

/**
 * @author yzp
 * 
 *
 */
public class BaseEntity  {
	@Key(desc="主键")
	private String id;  
		
	@Column(desc="租户")
	private String tendId;
	@Column(desc="创建时间")
	private Timestamp createDate; 
	@Column(desc="修改时间")
	private Timestamp updateDate; 
	@Column(desc="创建人Id")
	private String createPersonId;
	@Column(desc="创建人名称")
	private String createPersonName;  
	@Column(desc="修改人Id")
	private String updatePersonId;
	@Column(desc="修改人名称")
	private String updatePersonName;
	@Column(desc="创建部门Id")
	private String createOrgId;   
	@Column(desc="创建部门")
	private String createOrgName;  
	@Column(desc="创建公司Id")
	private String createCompanyId;   
	@Column(desc="创建公司")
	private String createCompanyName; 
	@Version(desc="版本")
	private Integer concurrencyVersion ; 
	@Column(desc="伪删除")
    private Boolean delflag =false;   //ture为删除，false不删除
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	public String getCreatePersonId() {
		return createPersonId;
	}
	public void setCreatePersonId(String createPersonId) {
		this.createPersonId = createPersonId;
	}
	public String getCreatePersonName() {
		return createPersonName;
	}
	public void setCreatePersonName(String createPersonName) {
		this.createPersonName = createPersonName;
	}
	public String getUpdatePersonId() {
		return updatePersonId;
	}
	public void setUpdatePersonId(String updatePersonId) {
		this.updatePersonId = updatePersonId;
	}
	public String getUpdatePersonName() {
		return updatePersonName;
	}
	public void setUpdatePersonName(String updatePersonName) {
		this.updatePersonName = updatePersonName;
	}
	public String getCreateOrgId() {
		return createOrgId;
	}
	public void setCreateOrgId(String createOrgId) {
		this.createOrgId = createOrgId;
	}
	public String getCreateOrgName() {
		return createOrgName;
	}
	public void setCreateOrgName(String createOrgName) {
		this.createOrgName = createOrgName;
	}
	public String getCreateCompanyId() {
		return createCompanyId;
	}
	public void setCreateCompanyId(String createCompanyId) {
		this.createCompanyId = createCompanyId;
	}
	public String getCreateCompanyName() {
		return createCompanyName;
	}
	public void setCreateCompanyName(String createCompanyName) {
		this.createCompanyName = createCompanyName;
	}


	public Integer getConcurrencyVersion() {
		return concurrencyVersion;
	}
	public void setConcurrencyVersion(Integer concurrencyVersion) {
		this.concurrencyVersion = concurrencyVersion;
	}
	public Boolean getDelflag() {
		return delflag;
	}
	public void setDelflag(Boolean delflag) {
		this.delflag = delflag;
	}
	public String getTendId() {
		return tendId;
	}
	public void setTendId(String tendId) {
		this.tendId = tendId;
	}
	


}
