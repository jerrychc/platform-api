package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;

import java.io.Serializable;


/**
 * @author admin
 * 
 * 
 */

public class SysNoticeMsgUserConfigDto extends BaseDto implements Serializable {

	private String postUrl;

	private String modifyUrl;

	private String loginName;

	private String userName;

	private String userId;

	private String tendCode;

	public String getPostUrl() {
		return postUrl;
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTendCode() {
		return tendCode;
	}

	public void setTendCode(String tendCode) {
		this.tendCode = tendCode;
	}

	public String getModifyUrl() {
		return modifyUrl;
	}

	public void setModifyUrl(String modifyUrl) {
		this.modifyUrl = modifyUrl;
	}
}
