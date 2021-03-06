package com.xinleju.platform.uitls;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanRelationInfo;

public class LoginUtils {
	public static SecurityUserBeanInfo getSecurityUserBeanInfo(){
		SecurityUserBeanInfo securityUserBeanInfo=new SecurityUserBeanInfo();
		HttpServletRequest  request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	    HttpSession session = request.getSession();
	    securityUserBeanInfo=(SecurityUserBeanInfo) session.getAttribute(securityUserBeanInfo.TOKEN_TEND_USER);
	    return securityUserBeanInfo;
	}
	
	public static SecurityUserBeanRelationInfo getSecurityUserBeanRelationInfo(){
		SecurityUserBeanRelationInfo securityUserBeanRelationInfo=new SecurityUserBeanRelationInfo();
		HttpServletRequest  request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	    HttpSession session = request.getSession();
	    securityUserBeanRelationInfo=(SecurityUserBeanRelationInfo) session.getAttribute(securityUserBeanRelationInfo.TOKEN_TEND_USER_MENU);
	    return securityUserBeanRelationInfo;
	}
	
	public static String getSessionId(){
		HttpServletRequest  request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	    HttpSession session = request.getSession();
	    return session.getId();
	}
}
