package com.xinleju.platform.ld.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 *
 *
 */

@Table(value="PT_LANDRAY_NEWS_INFO",desc="新闻信息")
public class LandrayNewsInfo extends BaseEntity{


	@Column(value="code",desc="编号")
	private String code;


	@Column(value="name",desc="主题")
	private String name;


	@Column(value="importance",desc="文档重要程度")
	private String importance;


	@Column(value="crt",desc="点击率")
	private String crt;


	@Column(value="news_type",desc="新闻类型")
	private String newsType;


	@Column(value="start_date",desc="发布时间")
	private String startDate;


	@Column(value="url",desc="静态页面url")
	private String url;



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


	public String getImportance() {
		return importance;
	}
	public void setImportance(String importance) {
		this.importance = importance;
	}


	public String getCrt() {
		return crt;
	}
	public void setCrt(String crt) {
		this.crt = crt;
	}


	public String getNewsType() {
		return newsType;
	}
	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}


	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}



}
