package com.xinleju.platform.univ.task.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class TaskParametersDto extends BaseDto{

		
	//任务
	private String taskId;
    
  		
	//参数描述
	private String details;
    
  		
		
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
    
  		
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
    
  		
}
