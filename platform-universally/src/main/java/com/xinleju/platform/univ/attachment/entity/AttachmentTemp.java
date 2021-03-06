package com.xinleju.platform.univ.attachment.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * @author haoqp
 * 
 * 
 */

@Table(value="PT_UNIV_FILE_ATTACHMENT_TEMP",desc="附件临时表")
public class AttachmentTemp extends BaseEntity{
	
		
	@Column(value="app_id",desc="系统id")
	private String appId;
    
  		
	@Column(value="business_id",desc="业务id")
	private String businessId;
    
  		
	@Column(value="name",desc="附件名称")
	private String name;
    
	@Column(value="type",desc="附件类型")
	private String type;
	
	
	@Column(value="extend_name",desc="附件扩展名")
	private String extendName;
    
  		
	@Column(value="full_name",desc="附件全名")
	private String fullName;
    
  		
	@Column(value="url",desc="上传全地址")
	private String url;
    
  		
	@Column(value="path",desc="存储路径")
	private String path;
    
  		
	@Column(value="file_size",desc="附件大小")
	private Long fileSize;
    
  		
	@Column(value="upload_time",desc="附件上传时间")
	private Timestamp uploadTime;
    
  		
	@Column(value="category_id",desc="附件分类id")
	private String categoryId;
    
  		
		
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
    
  		
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
  		
	public String getExtendName() {
		return extendName;
	}
	public void setExtendName(String extendName) {
		this.extendName = extendName;
	}
    
  		
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
    
  		
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    
  		
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
    
  		
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
    
  		
	public Timestamp getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}
    
  		
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
    
  		
	
}
