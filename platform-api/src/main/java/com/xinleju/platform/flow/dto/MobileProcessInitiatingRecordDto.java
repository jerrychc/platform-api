package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class MobileProcessInitiatingRecordDto extends BaseDto{

		
	//一级分类id
	private String categoryLevel1Id;

	//二级分类id
	private String categoryLevel2Id;

	//业务类型 1自定义表单 2人力系统
	private String businessType;

	//业务事项id
	private String businessItemId;

	//业务事项名称
	private String businessItemName;

	//业务对象名称
	private String businessObjectName;

	//标题
	private String title;

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
  		
  		
}
