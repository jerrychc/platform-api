package com.xinleju.platform.ld.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 *
 *
 */

@Table(value="PT_LANDRAY_FLOW_USER",desc="人员对应")
public class LandraySysUser extends BaseEntity{


	@Column(value="oa_id",desc="oa人员id")
	private String oaId;


	@Column(value="oa_real_name",desc="oa人员真实姓名")
	private String oaRealName;


	@Column(value="login_name",desc="人员登陆用户名")
	private String loginName;


	@Column(value="landray_id",desc="蓝凌人员id")
	private String landrayId;


	@Column(value="landray_real_name",desc="蓝凌人员真实姓名")
	private String landrayRealName;



	public String getOaId() {
		return oaId;
	}
	public void setOaId(String oaId) {
		this.oaId = oaId;
	}


	public String getOaRealName() {
		return oaRealName;
	}
	public void setOaRealName(String oaRealName) {
		this.oaRealName = oaRealName;
	}


	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	public String getLandrayId() {
		return landrayId;
	}
	public void setLandrayId(String landrayId) {
		this.landrayId = landrayId;
	}


	public String getLandrayRealName() {
		return landrayRealName;
	}
	public void setLandrayRealName(String landrayRealName) {
		this.landrayRealName = landrayRealName;
	}



}
