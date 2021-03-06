package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_CALENDAR_DETAIL",desc="工作日历详情表")
public class CalendarDetail extends BaseEntity{
	
		
	@Column(value="year",desc="年份")
	private Integer year;
    
  		
	@Column(value="month",desc="月份")
	private Integer month;
    
  		
	@Column(value="day",desc="日期")
	private Integer day;
	
	@Column(value="week_day",desc="周几")
	private Integer weekDay;
	//1到7默认的是1-周日、2-周一、3-周二、4-周三、5-周四、6-周五、7-周六
    
  		
	@Column(value="day_type",desc="日期类型: [1-工作日 2-节假日]")
	private String dayType;
	
	@Column(value="day_text",desc="yyyy-mm-dd格式显示")
	private String dayText;
  		
	@Column(value="remark",desc="备注说明")
	private String remark;
    
  		
		
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
    
  		
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
    
  		
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
    
  		
	public String getDayType() {
		return dayType;
	}
	public void setDayType(String dayType) {
		this.dayType = dayType;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(Integer weekDay) {
		this.weekDay = weekDay;
	}
	public String getDayText() {
		return dayText;
	}
	public void setDayText(String dayText) {
		this.dayText = dayText;
	}
    
  		
	
}
