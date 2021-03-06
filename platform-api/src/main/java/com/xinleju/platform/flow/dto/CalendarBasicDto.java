package com.xinleju.platform.flow.dto;

import java.util.ArrayList;
import java.util.List;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class CalendarBasicDto extends BaseDto{

		
	//年份
	private Integer year;
    
  		
	//工作日
	private String workDay;
    
  		
	//上班时间
	private String startTime;
    
  		
	//下班时间
	private String endTime;
    
  		
	//备注说明
	private String remark;
    
  	private List<CalendarDetailDto> detailList = new ArrayList<CalendarDetailDto>();
		
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
    
  		
	public String getWorkDay() {
		return workDay;
	}
	public void setWorkDay(String workDay) {
		this.workDay = workDay;
	}
    
  		
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
    
  		
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<CalendarDetailDto> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<CalendarDetailDto> detailList) {
		this.detailList = detailList;
	}
    
  		
}
