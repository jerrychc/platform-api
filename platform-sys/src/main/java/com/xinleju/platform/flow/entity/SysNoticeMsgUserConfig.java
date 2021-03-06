package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_SYS_NOTICE_MSG_USER_CONFIG",desc="系统通知消息用户配置表")
public class SysNoticeMsgUserConfig extends BaseEntity{

	@Column(value="post_url",desc="消息推送地址")
	private String postUrl;

	@Column(value="modify_url",desc="消息修改地址")
	private String modifyUrl;

	@Column(value="login_name",desc="接收人登录账号")
	private String loginName;

	@Column(value="user_name",desc="接收人姓名")
	private String userName;

	@Column(value="user_id",desc="接收人id")
	private String userId;

	@Column(value="tend_code",desc="接收人所在数据库实例名")
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
