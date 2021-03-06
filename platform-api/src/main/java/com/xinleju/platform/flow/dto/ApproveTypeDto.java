package com.xinleju.platform.flow.dto;

import java.util.List;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class ApproveTypeDto extends BaseDto{

		
	//编号
	private String code;
    
  		
	//名称
	private String name;
    
  		
	//备注
	private String remark;
    
  		
	//状态
	private Boolean status;
    
	//这两个List对象是为审批类型和操作类型关联的功能服务的
	private List<OperationTypeDto> operationTypeList; 		
	private List<ApproveOperationDto> approveOperationList; 	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
    
	public List<OperationTypeDto> getOperationTypeList() {
		return operationTypeList;
	}
	public void setOperationTypeList(List<OperationTypeDto> operationTypeList) {
		this.operationTypeList = operationTypeList;
	}
	
	public List<ApproveOperationDto> getApproveOperationList() {
		return approveOperationList;
	}
	public void setApproveOperationList(List<ApproveOperationDto> approveOperationList) {
		this.approveOperationList = approveOperationList;
	}
}
