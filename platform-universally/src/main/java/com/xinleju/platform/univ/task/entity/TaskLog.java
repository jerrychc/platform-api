package com.xinleju.platform.univ.task.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_UNIV_TASK_LOG",desc="任务日志")
public class TaskLog extends BaseEntity{
	
		
	@Column(value="task_id",desc="任务")
	private String taskId;
    
  		
	@Column(value="status",desc="任务运行状态。(1-等待、2-运行、3-错误、4-完成)")
	private String status;
    
  		
	@Column(value="last_start_time",desc="最后一次任务开始时间")
	private Timestamp lastStartTime;
    
  		
	@Column(value="last_end_time",desc="最后一次任务结束时间")
	private Timestamp lastEndTime;
    
  		
	@Column(value="next_start_time",desc="下次任务开始时间")
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
