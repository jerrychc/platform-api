package com.xinleju.platform.univ.task.dto;

import com.xinleju.platform.base.dto.BaseDto;


import java.sql.Timestamp;



/**
 * @author admin
 * 
 *
 */
public class TaskLogDto extends BaseDto{

		
	//任务
	private String taskId;
    
  		
	//任务运行状态。(1-等待、2-运行、3-错误、4-完成)
	private String status;
    
  		
	//最后一次任务开始时间
	private Timestamp lastStartTime;
    
  		
	//最后一次任务结束时间
	private Timestamp lastEndTime;
    
  		
	//下次任务开始时间
	private Timestamp nextStartTime;
    
  		
		
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public Timestamp getLastStartTime() {
		return lastStartTime;
	}
	public void setLastStartTime(Timestamp lastStartTime) {
		this.lastStartTime = lastStartTime;
	}
    
  		
	public Timestamp getLastEndTime() {
		return lastEndTime;
	}
	public void setLastEndTime(Timestamp lastEndTime) {
		this.lastEndTime = lastEndTime;
	}
    
  		
	public Timestamp getNextStartTime() {
		return nextStartTime;
	}
	public void setNextStartTime(Timestamp nextStartTime) {
		this.nextStartTime = nextStartTime;
	}
    
  		
}
