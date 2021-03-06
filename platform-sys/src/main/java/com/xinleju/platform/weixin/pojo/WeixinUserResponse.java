package com.xinleju.platform.weixin.pojo; 

import java.util.List;

public class WeixinUserResponse extends WeixinResponse {

	private static final long serialVersionUID = -5644385467790271762L;

	private List<WeixinUser> userlist;

	public List<WeixinUser> getUserlist() {
		return userlist;
	}

	public void setUserlist(List<WeixinUser> userlist) {
		this.userlist = userlist;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
