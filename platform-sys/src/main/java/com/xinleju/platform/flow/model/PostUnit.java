package com.xinleju.platform.flow.model;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PostUnit {
	private String id;
	private String postId;
	private String postName;
	private int postSeq;
	private String postStatus;
	private String acId;
	private Timestamp startTime;
	private Timestamp endTime;
	
	@JsonIgnore
	private ACUnit owner;					//所属的环节
	private List<ApproverUnit> approvers;	//下属的审批人
	
	private int leftPerson;	//岗位中剩余待处理人的数量
	
	private boolean change = false;
	
	private int dbAction = 0;			//1新增2删除 TODO zhangdaoqiang 与change合并
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public int getPostSeq() {
		return postSeq;
	}
	public void setPostSeq(int postSeq) {
		this.postSeq = postSeq;
		this.setChange(true);
	}
	public String getPostStatus() {
		return postStatus;
	}
	public void setPostStatus(String postStatus) {
		this.postStatus = postStatus;
		this.setChange(true);
	}
	public String getAcId() {
		return acId;
	}
	public void setAcId(String acId) {
		this.acId = acId;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
		this.setChange(true);
	}
	public ACUnit getOwner() {
		return owner;
	}
	public void setOwner(ACUnit owner) {
		this.owner = owner;
	}
	public List<ApproverUnit> getApprovers() {
		return approvers;
	}
	public void setApprovers(List<ApproverUnit> approvers) {
		this.approvers = approvers;
	}
	public int getLeftPerson() {
		return leftPerson;
	}
	public void setLeftPerson(int leftPerson) {
		this.leftPerson = leftPerson;
		this.setChange(true);
	}
	public boolean isChange() {
		return change;
	}
	public void setChange(boolean change) {
		this.change = change;
	}
	public int getDbAction() {
		return dbAction;
	}
	public void setDbAction(int dbAction) {
		this.dbAction = dbAction;
	}
}
