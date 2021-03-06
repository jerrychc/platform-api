package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class InstanceCsDto extends BaseDto{

		
	//岗位解析类型: 1:角色,2:岗位,3:人员
	private String parseType;
    
  		
	//岗位名称
	private String postName;
    
  		
	//岗位id
	private String postId;
    
  		
	//任务参与者
	private String participantName;
    
  		
	//任务参与者id
	private String participantId;
    
  		
	//实例id
	private String fiId;
    
  		
	//环节id
	private String acId;
    
  		
		
	public String getParseType() {
		return parseType;
	}
	public void setParseType(String parseType) {
		this.parseType = parseType;
	}
    
  		
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
    
  		
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
    
  		
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
    
  		
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
    
  		
	public String getFiId() {
		return fiId;
	}
	public void setFiId(String fiId) {
		this.fiId = fiId;
	}
    
  		
	public String getAcId() {
		return acId;
	}
	public void setAcId(String acId) {
		this.acId = acId;
	}
    
  		
}
