package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_MOBILE_PROCESS_RECORD",desc="移动端流程发起记录表")
public class MobileProcessInitiatingRecord extends BaseEntity{
	
		
	@Column(value="category_level1_id",desc="一级分类id")
	private String categoryLevel1Id;

	@Column(value="category_level2_id",desc="二级分类id")
	private String categoryLevel2Id;

	@Column(value="business_type",desc="业务类型 1自定义表单 2人力系统")
	private String businessType;

	@Column(value="business_item_id",desc="业务事项id")
	private String businessItemId;

	@Column(value="business_item_name",desc="业务事项名称")
	private String businessItemName;

	@Column(value="business_object_name",desc="业务对象名称")
	private String businessObjectName;

	@Column(value="title",desc="标题")
	private String title;
	public String getBusinessObjectName() {
		return businessObjectName;
	}

	public void setBusinessObjectName(String businessObjectName) {
		this.businessObjectName = businessObjectName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategoryLevel1Id() {
		return categoryLevel1Id;
	}

	public void setCategoryLevel1Id(String categoryLevel1Id) {
		this.categoryLevel1Id = categoryLevel1Id;
	}

	public String getCategoryLevel2Id() {
		return categoryLevel2Id;
	}

	public void setCategoryLevel2Id(String categoryLevel2Id) {
		this.categoryLevel2Id = categoryLevel2Id;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getBusinessItemId() {
		return businessItemId;
	}

	public void setBusinessItemId(String businessItemId) {
		this.businessItemId = businessItemId;
	}

	public String getBusinessItemName() {
		return businessItemName;
	}

	public void setBusinessItemName(String businessItemName) {
		this.businessItemName = businessItemName;
	}
	
}
