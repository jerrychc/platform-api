package com.xinleju.platform.base.filter;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserDto;

public class BaseAuthorizedFilterOld implements Filter{
	private String caseServerUrl="";
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		caseServerUrl=filterConfig.getInitParameter("caseServerUrl");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
	    HttpSession session =httpRequest.getSession();
		SecurityUserBeanInfo securityUserBeanInfo= (SecurityUserBeanInfo) session.getAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER);
		if(securityUserBeanInfo!=null){
			SecurityUserDto securityUserDto=securityUserBeanInfo.getSecurityUserDto();
			String username=securityUserDto.getLoginName();
			String requestUrl="";
			// TODO Auto-generated method stub
			/*
			 * 默认密码，URL登录不校验密码，所以给个密码默认值
			 */
			String password = "111";
			HttpForwardProxy proxy = new HttpForwardProxy();
			HtmlParser parser = new HtmlParser();
	
			String html = proxy.get(requestUrl, null);
			Map<String, String> postArgs = parser.parseCasPostRequestArg(html);
			String  action="";
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			formParams.add(new BasicNameValuePair("username", username));
			formParams.add(new BasicNameValuePair("password", password));
			formParams.add(new BasicNameValuePair("urlLogin", Boolean.TRUE.toString()));
			formParams.add(new BasicNameValuePair("lt","e2s1"));
			formParams.add(new BasicNameValuePair("_eventId","submit"));
			if (postArgs.get("execution") != null) {
				formParams.add(new BasicNameValuePair("execution", postArgs.get("execution")));
			}
			CASLoginResult result = proxy.casLogin(caseServerUrl + action, formParams);
			if(result!=null){
				for (HttpCookie c : result.getCookies()) {
					Cookie cookie = new Cookie(c.getName(), c.getValue());
					cookie.setMaxAge(-1);
					cookie.setPath("/");
					httpResponse.addCookie(cookie);
				}
			}
		}
		chain.doFilter(request, response);
	}
	



	public String getCaseServerUrl() {
		return caseServerUrl;
	}

	public void setCaseServerUrl(String caseServerUrl) {
		this.caseServerUrl = caseServerUrl;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
