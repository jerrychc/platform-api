package com.xinleju.platform.sys.security.controller;

import com.google.code.kaptcha.Producer;
import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.encrypt.EndecryptUtil;
import com.xinleju.platform.out.app.org.service.AuthorizationOutServiceCustomer;
import com.xinleju.platform.out.app.org.service.UserOutServiceCustomer;
import com.xinleju.platform.qr.QRCodeUtil;
import com.xinleju.platform.sys.log.dto.service.LogOperationDtoServiceCustomer;
import com.xinleju.platform.sys.notice.dto.service.MailMsgDtoServiceCustomer;
import com.xinleju.platform.sys.notice.dto.service.SysNoticePhoneMsgDtoServiceCustomer;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.sys.security.dto.service.AuthenticationDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.EncryptionUtils;
import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.tend.server.dto.service.TendOrgUserDtoServiceCustomer;
import com.xinleju.tend.server.dto.service.TendTrialDtoServiceCustomer;
import com.xinleju.tend.server.dto.service.dto.TendUserDto;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/sys/authentication")
public class AuthenticationController {

	private static Logger log = Logger.getLogger(AuthenticationController.class);
	@Autowired
	private AuthenticationDtoServiceCustomer authenticationDtoServiceCustomer;
	@Autowired
	private UserOutServiceCustomer userOutServiceCustomer;
	@Autowired
	private MailMsgDtoServiceCustomer mailMsgDtoServiceCustomer;
	@Autowired
	private SysNoticePhoneMsgDtoServiceCustomer sysNoticePhoneMsgDtoServiceCustomer;
	@Autowired
	protected RedisTemplate<String, String> redisTemplate;
	@Autowired  
	private Producer captchaProducer ;  
	@Autowired  
	private LogOperationDtoServiceCustomer logOperationDtoServiceCustomer ;  
	@Autowired  
	private TendTrialDtoServiceCustomer tendTrialDtoServiceCustomer ;

	@Autowired
	private AuthorizationOutServiceCustomer authorizationOutServiceCustomer;
	
	@Autowired  
	private TendOrgUserDtoServiceCustomer tendOrgUserDtoServiceCustomer ;  
	
	@Value("#{configuration['supperManager.username']?:''}")
	private String supperManagerUsername;
	
	@Value("#{configuration['supperManager.password']?:''}")
	private String supperManagerPassword;

	@Value("#{configuration['qr.code']?:''}")
	private String qrCode;
	@Value("#{configuration['bSysCASLogoutUrl']?:''}")
	private String bSysCASLogoutUrl;

	/**
	 * 系统登录
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/login",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult login(@RequestBody Map<String,Object> map){
		Date tt = new Date();
		System.out.println("登陆总用时开始："+tt.getTime());
		MessageResult result=new MessageResult();
		HttpServletRequest  request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		map.put("ip", request.getRemoteAddr());
		//
		boolean isAuth = false;
		if(map.get("loginName")!=null){
			String login_demain_Name=(String) map.get("loginName");
			String login_password=(String) map.get("password");
			if(login_demain_Name.indexOf("#") > 0){
				String superLoginName = login_demain_Name.substring(0,login_demain_Name.indexOf("#"));
				String superPassword = EncryptionUtils.getEncryptInfo(superLoginName, login_password);
				if(superLoginName.equals(supperManagerUsername) && superPassword.equals(supperManagerPassword)){
					isAuth = true;
					String loginName=login_demain_Name.substring(login_demain_Name.indexOf("#")+1,login_demain_Name.indexOf("@"));
					String demainName=login_demain_Name.substring(login_demain_Name.indexOf("@")+1,login_demain_Name.length());
					map.put("loginName", loginName);
					map.put("domain", demainName);
				}
			}else{
				String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
				String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
				//0账号，1邮箱，2手机，3带二级域名全路径登录
				String loginType = "0";
				if(login_demain_Name.matches(REGEX_EMAIL)){
					loginType = "1";
				}else if(login_demain_Name.matches(REGEX_MOBILE)){
					loginType = "2";
				}else if(login_demain_Name.indexOf("@")>-1){
					loginType = "3";
				}
				if(loginType.equals("3")){
					String loginName=login_demain_Name.substring(0,login_demain_Name.indexOf("@"));
					String demainName=login_demain_Name.substring(login_demain_Name.indexOf("@")+1,login_demain_Name.length());
					map.put("loginName", loginName);
					map.put("domain", demainName);
				}else if(loginType.equals("0")){
					map.put("loginName", login_demain_Name);
					map.put("domain", "xy");
				}else{
					//调用server服务，获取二级域名和账号
					Map<String,Object> mapCon = new HashMap<String,Object>();
					mapCon.put("phoneOrEmail", login_demain_Name);
					mapCon.put("type", loginType);
					String dubboTendResultInfo = tendOrgUserDtoServiceCustomer.getTendOrgUserInfo(null, JacksonUtils.toJson(mapCon));
					MessageResult messageResult= JacksonUtils.fromJson(dubboTendResultInfo, MessageResult.class);
					 if(messageResult.isSuccess()){
						 Map demain = (Map)messageResult.getResult();
						 map.put("loginName", (String)demain.get("loginName"));
						 map.put("domain", (String)demain.get("domain"));
					 }else{
						 result = messageResult;
						return result;
					 }
//					serverService.getLoginNameAndDomain(null,JacksonUtils.toJson(mapCon));
				}
				
				
			}
		}
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo=new SecurityUserBeanInfo();
		SecurityUserBeanRelationInfo securityUserBeanRelationInfo=new SecurityUserBeanRelationInfo();
		securityUserBeanInfo.setSecurityUserDto(new SecurityUserDto());
		
		//保存baseUrl add by zhangdaoqiag
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath();
		securityUserBeanInfo.setBaseUrl(baseUrl);
		
		try {
			Date t0 = new Date();
			System.out.println("切换库开始："+t0.getTime());
			String dubboResultInfo=authenticationDtoServiceCustomer.getDomain(null, paramaterJson);
			Date t1 = new Date();
			System.out.println("切换库结束："+t1.getTime()+"用时：：：：：：：：："+(t1.getTime()-t0.getTime()));
			 DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
		    	String Info= dubboServiceResultInfo.getResult();
		    	Map demain=JacksonUtils.fromJson(Info, HashMap.class);
		    	if(demain!=null){
			    	securityUserBeanInfo.setTendId(demain.get("tendId").toString());
			    	securityUserBeanInfo.setTendCode(demain.get("tendCode").toString());
		    	}
		    }else{
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
				result.setCode(dubboServiceResultInfo.getCode());
				return result;
		    }
		} catch (Exception e1) {
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("切换库异常"+e1.getMessage());
			result.setCode("");
			return result;
		}
		try {
			String dubboResultInfo="";
			if(isAuth){
				dubboResultInfo=authenticationDtoServiceCustomer.preCheck(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
			}else{
				String s=JacksonUtils.toJson(securityUserBeanInfo);
				//TODO(gyh)
				//判断是否为初始密码，进行AD认证--begin by gyh
				//获取用户数据库密码
				/*Map<String,Object> loginNames=new HashMap<String,Object>();
				String uLoginName=map.get("loginName").toString();//用户登录名
				loginNames.put("loginNames", uLoginName);
				dubboResultInfo = userOutServiceCustomer.getUserByUserLoginNames(s, JacksonUtils.toJson(loginNames));
				DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
				if (dubboServiceResultInfo.isSucess()) {
					String resultInfo= dubboServiceResultInfo.getResult();
					List<UserDto> uL=JacksonUtils.fromJson(resultInfo, ArrayList.class,UserDto.class);
					if(uL!=null&&uL.size()>0){
						UserDto upwd=uL.get(0);
						String pwdDB=upwd.getPassword();//用户初始密码
						String pwd=DigestUtils.md5Hex("XINJUKEJI"+"xinyuan2018!"+uLoginName);//默认初始密码
						if(pwd.equals(pwdDB)){//若为初始密码，则进行ad认证
							//TODO ad认证
							String auth_password=(String) map.get("password");//用户输入的密码
							MessageResult res=AdAuthenticationUtil.authenticate(uLoginName,auth_password);
							if(res.isSuccess()){//认证通过，跳到修改密码页面
								result.setSuccess(res.isSuccess());
								result.setMsg("AD认证");
								result.setCode("AD_AUTH");
								Map<String,Object> r=new HashMap<>();
								r.put("id", upwd.getId());
								r.put("loginName", upwd.getLoginName());
								r.put("domain", map.get("domain").toString());
								result.setResult(r);
								return result;
							}else {//认证不通过，提示密码错误
								result.setSuccess(res.isSuccess());
								result.setMsg(res.getMsg());
								result.setCode(res.getCode());
								return result;
							}
						}
					}
				}*/
				//判断是否为初始密码，进行AD认证--end by gyh
				
				Date t0 = new Date();
				System.out.println("登录开始："+t0.getTime());
				dubboResultInfo=authenticationDtoServiceCustomer.login(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
				Date t1 = new Date();
				System.out.println("登录结束："+t1.getTime()+"用时：：：：：：：：："+(t1.getTime()-t0.getTime()));
			}
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
		    	//获取用户角色
				String resultInfo= dubboServiceResultInfo.getResult();
				SecurityUserDto securityUserDto=JacksonUtils.fromJson(resultInfo, SecurityUserDto.class);
				//非用户类型的用户，不可登录OA
				if(securityUserDto.getType().equals("0")){
					result.setSuccess(false);
					result.setCode(ErrorInfoCode.LOGIN_LOGINNAMEERROR.getValue());
					result.setMsg(ErrorInfoCode.LOGIN_LOGINNAMEERROR.getName());
					return result;
				}
				StringBuilder sb=new StringBuilder();
				Cookie[] cookies=request.getCookies();
				//update by dgh 如果cookies为空则抛出固定异常，用于解决登录报null的错误
				if(cookies==null){
					result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg("cookies为空！");
					result.setCode("COOKIES_ERROR");
					return result;
				}

//				for(Cookie cookie:cookies){
//					sb.append(cookie.getName()+"="+cookie.getValue()+";");
//				}
				log.info (securityUserDto.getLoginName ()+"登录时 login_cookies："+ JacksonUtils.toJson (cookies));
				for (Cookie cookie : cookies) {
					 if(Objects.equals (cookie.getName (),"DTL_SESSION_ID")){
						 sb.append (cookie.getName () + "=" + cookie.getValue () + ";");
						 break;
					 }
				 }

				 if(sb.toString ().indexOf ("DTL_SESSION_ID")==-1){
				 log.info ("登录时 login_cookies 为空，设置request.getSession ().getId ()："+request.getSession ().getId ());
				   sb.append ("DTL_SESSION_ID="+request.getSession ().getId ());
				 }
				 securityUserBeanInfo.setCookies (sb.toString ());
				 securityUserBeanInfo.setSecurityUserDto(securityUserDto);
				try{
					Date t0 = new Date();
					System.out.println("获取认证信息总时间开始："+t0.getTime());
					String authenticationInfodubboResultInfo = authenticationDtoServiceCustomer.getUserAuthenticationInfo(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson(securityUserDto));
					Date t1 = new Date();
					System.out.println("获取认证信息总时间结束："+t1.getTime()+"用时：：：：：：：：："+(t1.getTime()-t0.getTime()));
					DubboServiceResultInfo authenticationInfodubboServiceResultInfo= JacksonUtils.fromJson(authenticationInfodubboResultInfo, DubboServiceResultInfo.class);
					if(authenticationInfodubboServiceResultInfo.isSucess()){
						String authenticationInforesultInfo= authenticationInfodubboServiceResultInfo.getResult();
						AuthenticationDto authenticationDto=JacksonUtils.fromJson(authenticationInforesultInfo, AuthenticationDto.class);
						//获取用户标准岗位
						List<SecurityStandardRoleDto> securityStandardRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getStandardRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
						securityUserBeanInfo.setSecurityStandardRoleDtoList(securityStandardRoleDtoList);
						
						//获取用户通用角色
						List<SecurityStandardRoleDto> securityCurrencyRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getCurrencyRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
						securityUserBeanInfo.setSecurityCurrencyRoleDtoList(securityCurrencyRoleDtoList);
						
						//获取用户岗位
						List<SecurityPostDto> securityPostDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getPostDtoList()),ArrayList.class,SecurityPostDto.class);
						securityUserBeanInfo.setSecurityPostDtoList(securityPostDtoList);
						//当前用户的菜单清单（未授权和已授权的）
						List<SecurityResourceDto> SecurityResourceDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getResourceDtoList()),ArrayList.class,SecurityResourceDto.class);
						securityUserBeanRelationInfo.setResourceDtoList(SecurityResourceDtoList);
						//当前用户所在组织的类型
						String securityOrganizationType = authenticationDto.getOrganizationType();
						securityUserBeanInfo.setSecurityOrganizationType(securityOrganizationType);
						//当前用户的一级公司
						SecurityOrganizationDto securityTopCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopCompanyDto()),SecurityOrganizationDto.class);
						securityUserBeanInfo.setSecurityTopCompanyDto(securityTopCompanyDto);
						//当前用户的直属公司
						SecurityOrganizationDto securityDirectCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectCompanyDto()),SecurityOrganizationDto.class);
						securityUserBeanInfo.setSecurityDirectCompanyDto(securityDirectCompanyDto);
						//当前用户的一级部门
						SecurityOrganizationDto securityTopDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopDeptDto()),SecurityOrganizationDto.class);
						securityUserBeanInfo.setSecurityTopDeptDto(securityTopDeptDto);
						//当前用户的直属部门
						SecurityOrganizationDto securityDirectDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectDeptDto()),SecurityOrganizationDto.class);
						securityUserBeanInfo.setSecurityDirectDeptDto(securityDirectDeptDto);
						//当前用户的项目
						SecurityOrganizationDto securityGroupDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getGroupDto()),SecurityOrganizationDto.class);
						securityUserBeanInfo.setSecurityGroupDto(securityGroupDto);
						//当前用户的分期
						SecurityOrganizationDto securityBranchDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getBranchDto()),SecurityOrganizationDto.class);
						securityUserBeanInfo.setSecurityBranchDto(securityBranchDto);
						
					}
				}catch(Exception ex){
					
				}
				Date t0 = new Date();
				System.out.println("session设置总时间开始："+t0.getTime());
			    session.setAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER,securityUserBeanInfo);
			    session.setAttribute(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU,securityUserBeanRelationInfo);
			    Date t1 = new Date();
				System.out.println("session设置总时间结束："+t1.getTime()+"用时：：：：：：：：："+(t1.getTime()-t0.getTime()));
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(dubboServiceResultInfo.getMsg());
				result.setCode(dubboServiceResultInfo.getCode());
		    }
		    try {
		    	Map<String, Object> logMap=new HashMap<String, Object>();
		    	logMap.put("ip", request.getRemoteAddr());
		    	logMap.put("loginBrowser", getIpAddress(request));
		    	logMap.put("msg", dubboServiceResultInfo.getMsg().replaceAll("！", "!"));
		    	logMap.putAll(map);
		    	Date t0 = new Date();
				System.out.println("记录登录日志总时间开始："+t0.getTime());
		    	authenticationDtoServiceCustomer.loginInLog(JacksonUtils.toJson(securityUserBeanInfo),JacksonUtils.toJson(logMap));
		    	Date t1 = new Date();
				System.out.println("记录登录日志总时间结束："+t1.getTime()+"用时：：：：：：：：："+(t1.getTime()-t0.getTime()));
		    } catch (Exception e) {
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		Date ttt = new Date();
		System.out.println("登录总用时结束："+ttt.getTime()+"用时：：：：：：：：："+(ttt.getTime()-tt.getTime()));
		return result;
	}
	
	/** 
	 * 获取对象的真实IP地址
	 * 
	 */  
	public  String getIpAddress(HttpServletRequest request) {  
		String ip = request.getHeader("x-forwarded-for");  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("HTTP_X_REAL_IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("WL-Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("HTTP_CLIENT_IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getRemoteAddr();  
		}  
		return ip;  
	}  

	
	/**
	 * 获取授权码
	 * @param response
	 */
	@RequestMapping(value="/authCode",method={RequestMethod.GET})
	public ModelAndView   authCode(HttpServletResponse response) throws Exception{
        response.setDateHeader("Expires",0);
        response.setHeader("Cache-Control","no-cache, must-revalidate");
        response.addHeader("Cache-Control","post-check=0, pre-check=0");
        response.setHeader("Pragma","no-cache");
        response.setContentType("image/jpeg");
        HttpServletRequest  request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取验证码存入redis
		String vCode=sixCode();//验证码
		String ip=request.getRemoteAddr();
		String key="UserToken_Auth_Code_"+ip;
		redisTemplate.opsForValue().set(key, vCode,120,TimeUnit.SECONDS);
	    BufferedImage bi = captchaProducer.createImage(vCode);  
        ServletOutputStream out = response.getOutputStream();  
        ImageIO.write(bi, "jpg", out);  
        try {  
            out.flush();  
        } finally {  
            out.close();  
        }  
        return null;  

	}
	
	
	/**
	 * 验证授权码
	 * @param map
	 */
	@RequestMapping(value="/authCheckCode",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult   authCheckCode(@RequestBody Map<String,Object> map){
	    MessageResult result=new MessageResult();
	    String paramaterJson_input = JacksonUtils.toJson(map);
	    try {
			HttpServletRequest  request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String key=null;
			String ip=request.getRemoteAddr();
			if(map.containsKey("aginGet")&&(Boolean)map.get("aginGet")){
				//再次发送验证码，不需校验验证码
			}else{
				key="UserToken_Auth_Code_"+ip;
				String userToken_Auth_Code=  redisTemplate.opsForValue().get(key);
				// 校验图片验证码
				if(StringUtils.isBlank(userToken_Auth_Code)){
					throw new Exception("验证码失效");
				}
				if(!map.containsKey("uPicCode")){
					throw new Exception("验证码不可为空");
				}
				//用户输入的图片验证码
				String uPicCode=map.get("uPicCode").toString();
				if(!userToken_Auth_Code.equals(uPicCode)){
					throw new Exception("验证码输入错误");
				}
				redisTemplate.delete(key);
			}
		    
		    SecurityUserBeanInfo securityUserBeanInfo=new SecurityUserBeanInfo();
		    securityUserBeanInfo.setSecurityUserDto(new SecurityUserDto());
		    //租户忘记密码，使用租户系统发送验证码
		    //TODO 判断用户是否是租户管理员
		    boolean isTendUser=false;
		    String isTendS=tendTrialDtoServiceCustomer.isTendUser(null, map.get("loginName").toString());
		    MessageResult isTend = JacksonUtils.fromJson(isTendS, MessageResult.class);
		    isTendUser = isTend.isSuccess();
		    //需要知道是那个租户中查询
		    if(map.get("loginName")!=null){
		    	String login_demain_Name=(String) map.get("loginName");
		    	if(!login_demain_Name.contains("@")){
		    		throw new Exception("不存在此用户");
		    	}
				String loginName=login_demain_Name.substring(0,login_demain_Name.indexOf("@"));
				String demainName=login_demain_Name.substring(login_demain_Name.indexOf("@")+1,login_demain_Name.length());
				map.put("loginName", loginName);
				map.put("domain", demainName);
			}
		    try {
				String dubboResultInfo=authenticationDtoServiceCustomer.getDomain(null, JacksonUtils.toJson(map));
				 DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			    if(dubboServiceResultInfo.isSucess()){
			    	String Info= dubboServiceResultInfo.getResult();
			    	Map demain=JacksonUtils.fromJson(Info, HashMap.class);
			    	if(demain!=null){
				    	securityUserBeanInfo.setTendId(demain.get("tendId").toString());
				    	securityUserBeanInfo.setTendCode(demain.get("tendCode").toString());
			    	}
			    }  else{
			    	result.setSuccess(MessageInfo.GETERROR.isResult());
			    	String mString=dubboServiceResultInfo.getMsg();
			    	if(mString.indexOf("登录失败,")>=0){
			    		mString=mString.replace("登录失败,", "");
			    	}
			    	result.setMsg(mString);
			    	result.setCode(dubboServiceResultInfo.getCode());
			    	return result;
			    }
			} catch (Exception e1) {
				e1.printStackTrace();
		        return null;
			}
		    
		    String paramaterJson = JacksonUtils.toJson(map);
		    String userInfo=JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo=authenticationDtoServiceCustomer.preCheck(userInfo, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
		    	//获取用户角色
				String resultInfo= dubboServiceResultInfo.getResult();
				SecurityUserDto securityUserDto=JacksonUtils.fromJson(resultInfo, SecurityUserDto.class);
				securityUserBeanInfo.setSecurityUserDto(securityUserDto);
			    userInfo=JacksonUtils.toJson(securityUserBeanInfo);
			    
			    //获取验证码存入redis
				String vCode=sixCode();//验证码
			    
				//发送短信或邮件
				String findType=(String)map.get("findType");
				String resInfo=null;
				String reStr=null;//模糊手机或邮箱
				if(findType!=null && findType.equals("0")){
					Map<String,Object> msg=new HashMap<String,Object>();
					if(securityUserDto.getMobile()==null){
						throw new Exception("绑定电话不存在");
					}
					String phone=securityUserDto.getMobile();//收件人
					if (isTendUser) {
						String resultJson = tendTrialDtoServiceCustomer.postAuthCode(null, phone,"sms");
						result = JacksonUtils.fromJson(resultJson, MessageResult.class);
						vCode=result.getResult().toString();
					}else{
						msg.put("phones", phone);
						msg.put("msg", String.format("您的验证码为：%s，请在2分钟内输入", vCode));
						String msgJson=JacksonUtils.toJson(msg);
						resInfo=sysNoticePhoneMsgDtoServiceCustomer.sendMsg(userInfo, msgJson);
						DubboServiceResultInfo dubboResInfo=JacksonUtils.fromJson(resInfo, DubboServiceResultInfo.class);
						if(dubboResInfo.isSucess()){
							result.setSuccess(MessageInfo.GETSUCCESS.isResult());
							result.setMsg(MessageInfo.GETSUCCESS.getMsg());
						}else{
							result.setSuccess(MessageInfo.GETERROR.isResult());
							result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
						}
					}
					reStr=phoneToStar(phone);
				}else{
					if(securityUserDto.getEmail()==null||!securityUserDto.getEmail().contains("@")){
						throw new Exception("绑定邮箱不存在，或邮箱格式不正确");
					}
					String email=securityUserDto.getEmail();//收件人
					if (isTendUser) {
						 //TODO 租户系统发送邮件
						String resultJson = tendTrialDtoServiceCustomer.postAuthCode(null, email,"mail");
						result = JacksonUtils.fromJson(resultJson, MessageResult.class);
						vCode=result.getResult().toString();
					}else{
						Map<String,Object> msg=new HashMap<String,Object>();
						msg.put("sendAddress", email);
						msg.put("title", "验证码");
						msg.put("context", String.format("您的验证码为：%s，请在2分钟内输入", vCode));
						String msgJson=JacksonUtils.toJson(msg);
						//发送邮件
						resInfo=mailMsgDtoServiceCustomer.sendMailMsg(userInfo, msgJson);
						DubboServiceResultInfo dubboResInfo=JacksonUtils.fromJson(resInfo, DubboServiceResultInfo.class);
						if(dubboResInfo.isSucess()){
							result.setSuccess(MessageInfo.GETSUCCESS.isResult());
							result.setMsg(MessageInfo.GETSUCCESS.getMsg());
						}else{
							result.setSuccess(MessageInfo.GETERROR.isResult());
							result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboResInfo.getExceptionMsg()+"】");
						}
					}
					reStr=mailToStar(email);
				}
				key="vCode_"+securityUserDto.getId()+"_"+ip;
				redisTemplate.opsForValue().set(key, vCode,120,TimeUnit.SECONDS);
				SecurityUserDto reUser=new SecurityUserDto();
				reUser.setId(securityUserDto.getId());
				reUser.setEmail(reStr);
				result.setResult(reUser);
		   }else{
			   result.setSuccess(MessageInfo.GETERROR.isResult());
			   String mString=dubboServiceResultInfo.getMsg();
			   if(mString.indexOf("用户名不存在")>0){
				   mString="用户名不存在！";
			   }
			   if(mString.indexOf("登录失败,")>=0){
				   mString= mString.replace("登录失败,", "");
			   }
			   result.setMsg(mString);
			   result.setCode(dubboServiceResultInfo.getCode());
		   }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	log.error("调用authCheckCode方法:  【参数"+paramaterJson_input+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("验证失败【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	
	/**
	 * 系统登出
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/logout",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult logout(@RequestBody Map<String,Object> map){
	    MessageResult result=new MessageResult();
	    HttpServletRequest  request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	    //记录退出登录日志
	    SecurityUserBeanInfo beanInfo=LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(beanInfo);
	    try {
	    	Map<String, Object> logMap=new HashMap<String, Object>();
			logMap.put("ip", request.getRemoteAddr());
			logMap.put("loginBrowser", getIpAddress(request));
			logMap.put("msg", "退出成功!");
			authenticationDtoServiceCustomer.loginOutLog(userJson,  JacksonUtils.toJson(logMap));
		} catch (Exception e) {
		}
	    HttpSession session = request.getSession();
	    session.removeAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER);
	    session.removeAttribute(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU);
	    session.invalidate();
	    log.info("bSysCASLogoutUrl>>>>>>>>>>>>>>>>>>>"+bSysCASLogoutUrl);
	    if(StringUtils.isNotBlank(bSysCASLogoutUrl)){
			result.setResult(bSysCASLogoutUrl);
		}
	    result.setSuccess(true);
	    result.setMsg("注销成功！");
	    
		return result;
	}
	
	/**
	 * 获取授权菜单
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getUserAuthenticationMenu",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getUserAuthenticationMenu(@RequestBody Map<String,Object> map){
	    MessageResult result=new MessageResult();
	    String appId = (String)map.get("appId");
	    //当前登录的所有信息
	    SecurityUserBeanRelationInfo beanInfo=LoginUtils.getSecurityUserBeanRelationInfo();
	    List<SecurityResourceDto>  list = beanInfo.getResourceDtoList();
	    List<DataNodeDto>  listTopMenu = new ArrayList<DataNodeDto>();
	    List<DataNodeDto>  listSecondMenu = new ArrayList<DataNodeDto>();
	    List<DataNodeDto> listReturn = new ArrayList<DataNodeDto>();
	    for(SecurityResourceDto srDto:list){
	    	DataNodeDto dataNodeDto = new DataNodeDto();
//	    	if(srDto.getId().equals("9092e0359a1645acb00182a18cb09401")){
//	    		System.out.println("123");
//	    		
//	    	}
//	    	if(srDto.getAppId().equals(appId)){
//	    		System.out.println(srDto.getName()+"---------------"+srDto.getLevel()+"----------"+srDto.getType()+"------------"+srDto.getIsAuth());
//	    	}
	    	if(srDto.getIsAuth().equals("1") && srDto.getAppId().equals(appId) && srDto.getType().equals("RESOURCE") && srDto.getLevel() == 2){
	    		dataNodeDto.setId(srDto.getId());
	    		dataNodeDto.setParentId(srDto.getParentId());
	    		dataNodeDto.setCode(srDto.getCode());
	    		dataNodeDto.setName(srDto.getName());
	    		dataNodeDto.setResourceurl(srDto.getResourceUrl());
	    		dataNodeDto.setOpenmode(srDto.getOpenmode());
	    		listTopMenu.add(dataNodeDto);
	    	}
	    	if(srDto.getIsAuth().equals("1") && srDto.getAppId().equals(appId) && srDto.getType().equals("RESOURCE") && srDto.getLevel() == 3){
	    		dataNodeDto.setId(srDto.getId());
	    		dataNodeDto.setParentId(srDto.getParentId());
	    		dataNodeDto.setCode(srDto.getCode());
	    		dataNodeDto.setName(srDto.getName());
	    		dataNodeDto.setResourceurl(srDto.getResourceUrl());
	    		dataNodeDto.setOpenmode(srDto.getOpenmode());
	    		listSecondMenu.add(dataNodeDto);
	    	}
	    }
	    
	    for(DataNodeDto srDto:listTopMenu){
	    	List<DataNodeDto> childDataNodeDto = new ArrayList<DataNodeDto>();
	    	for(DataNodeDto srDtoSec:listSecondMenu){
		    	if(srDtoSec.getParentId().equals(srDto.getId())){
		    		childDataNodeDto.add(srDtoSec);
		    	}
		    }
	    	srDto.setChildren(childDataNodeDto);
	    }
	    
	    result.setResult(listTopMenu);
	    result.setSuccess(true);
	    result.setMsg("获取成功");
	    
		return result;
	}
	
	/**
	 * 获取授权菜单
	 * @param
	 * @return
	 */
	/*@RequestMapping(value="/getUserAuthenticationApp",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getUserAuthenticationApp(@RequestBody Map<String,Object> map){
	    MessageResult result=new MessageResult();
	    //当前登录的所有信息
	    SecurityUserBeanRelationInfo beanInfo=LoginUtils.getSecurityUserBeanRelationInfo();
	    List<SecurityResourceDto>  list = beanInfo.getResourceDtoList();
	    List<SecurityResourceDto> listReturn = new ArrayList<SecurityResourceDto>();
	    for(SecurityResourceDto srDto:list){
	    	if(srDto.getIsAuth().equals("1") && srDto.getType().equals("APPSystem") ){
	    		listReturn.add(srDto);
	    	}
	    }
	    
	    result.setResult(listReturn);
	    result.setSuccess(true);
	    result.setMsg("获取成功");
	    
		return result;
	}*/

	@RequestMapping(value="/getUserAuthenticationApp",method={RequestMethod.GET})
	public @ResponseBody MessageResult getUserAuthenticationApp(@RequestParam(name = "appDelFlag",required = false)String appDelFlag,@RequestParam(name = "appStatus",required = false)String appStatus,HttpServletRequest request) {
		MessageResult result=new MessageResult();
		BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		try {
			//当前登录的所有信息
			SecurityUserBeanRelationInfo beanInfo=LoginUtils.getSecurityUserBeanRelationInfo();
			List<SecurityResourceDto>  list = beanInfo.getResourceDtoList();
			String ip = getIp(request);
			List<SecurityResourceDto> listReturn = new ArrayList<SecurityResourceDto>();
			for(SecurityResourceDto srDto:list){
				if(srDto.getIsAuth().equals("1") && srDto.getType().equals("APPSystem") ){
					//匹配经营分析系统 --- 1.绑定唯一二维码标识到用户  2.生成二维码
					if("BI".equals(srDto.getCode())){
						String k = bindQRToUser();
						srDto.setResourceUrl(srDto.getResourceUrl()+"&qr="+k);
						srDto.setIcon(encoder.encodeBuffer(getQRByUser(k)).trim());
					}
					listReturn.add(srDto);
				}
			}
			result.setResult(listReturn);
			result.setSuccess(true);
			result.setMsg("获取成功");
		} catch (Exception e) {
			log.error("调用getUserAuthenticationApp方法======" + "【" + e.getMessage() + "】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}

	/**
	 * 根据系统编码和菜单编码获取登录用户按钮权限
	 * add by dgh on 2017/08/21
	 * @param appCode
	 * @param menuCode
	 * @return
	 */
	@RequestMapping(value="/getUserAuthenticationOperation",method={RequestMethod.GET})
	public @ResponseBody MessageResult getUserAuthenticationOperation(@RequestParam(name = "appCode",required = false)String appCode,@RequestParam(name = "menuCode",required = false)String menuCode){
		MessageResult result = new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			SecurityUserDto userDto = userBeanInfo.getSecurityUserDto();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("userLoginName",userDto.getLoginName());
			paramMap.put("appCode",appCode);
			paramMap.put("menuCode",menuCode);
			String dubboResultInfo = authorizationOutServiceCustomer.getFuncButtonAuthByUserLoginNameAndAppCodeAndMenuCode(userJson,JacksonUtils.toJson(paramMap));
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				String resultInfo = dubboServiceResultInfo.getResult();
				List<Map<String,Object>> operationMaps = JacksonUtils.fromJson(resultInfo, List.class,Map.class);
				List<String> operationsCodes = new ArrayList<String>();
				for (Map<String,Object> operationMap:operationMaps) {
					operationsCodes.add((String) operationMap.get("code"));
				}
				result.setResult(operationsCodes);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			} else {
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
				result.setCode(dubboServiceResultInfo.getCode());
			}
		} catch (Exception e) {
			log.error("调用get方法:  【参数" + appCode + "】======" + "【" + e.getMessage() + "】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}


	/**
	 * 校验用户输入的验证码是否正确
	 * @param map 用户账号
	 * @return 
	 */
	@RequestMapping(value="/verifByVcode",method=RequestMethod.POST)
	public @ResponseBody MessageResult verifByVcode(@RequestBody Map<String,Object> map,HttpServletRequest request){
		MessageResult result=new MessageResult();
		String paramJson=JacksonUtils.toJson(map);
		try {
			String verifcode=(String)map.get("verifcode");
			String id=(String)map.get("id");
			String ip=request.getRemoteAddr();
			String key="vCode_"+id+"_"+ip;
			String redisCode=redisTemplate.opsForValue().get(key);//redis获取验证码
			if(redisCode==null ){
				throw new Exception("验证码失效");
			}
			if(!verifcode.equals(redisCode)){
				throw new  Exception("验证码错误");
			}
			//删除redis中的验证码
			redisTemplate.delete(key);
			result.setResult(null);
			result.setSuccess(MessageInfo.GETSUCCESS.isResult());
			result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用verifByVcode方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("发送失败【"+e.getMessage()+"】");
		}
		return result;
	}
	/**
	 * 重置密码
	 * @param map 用户ID和密码
	 * @return 
	 */
	@RequestMapping(value="/resetPwd",method=RequestMethod.POST)
	public @ResponseBody MessageResult resetPwd(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramJson=JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo=new SecurityUserBeanInfo();
	    securityUserBeanInfo.setSecurityUserDto(new SecurityUserDto());
		try {
			if(map.get("loginName")!=null){
				String login_demain_Name=(String) map.get("loginName");
				String loginName=login_demain_Name.substring(0,login_demain_Name.indexOf("@"));
				String demainName=login_demain_Name.substring(login_demain_Name.indexOf("@")+1,login_demain_Name.length());
				map.put("loginName", loginName);
				map.put("domain", demainName);
			}
		    try {
				String dubboResultInfo=authenticationDtoServiceCustomer.getDomain(null, JacksonUtils.toJson(map));
				 DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			    if(dubboServiceResultInfo.isSucess()){
			    	String Info= dubboServiceResultInfo.getResult();
			    	Map demain=JacksonUtils.fromJson(Info, HashMap.class);
			    	if(demain!=null){
				    	securityUserBeanInfo.setTendId(demain.get("tendId").toString());
				    	securityUserBeanInfo.setTendCode(demain.get("tendCode").toString());
			    	}
			    }  
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
		        return null;
			}
		    
		    String paramaterJson = JacksonUtils.toJson(map);
		    String userInfo=JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo=authenticationDtoServiceCustomer.resetPwd(userInfo, paramJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg("重置密码失败【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用resetPwd方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("重置密码失败【"+e.getMessage()+"】");
		}
		return result;
	}
	//TODO 注册
	/**
	 * 校验字段是否重复：是否在数据库已存在，调用luorongxin的dubbo服务
	 * @param map 手机号/邮箱/微信号
	 * @return 
	 */
	@RequestMapping(value="/checkRepeatByCol",method=RequestMethod.POST)
	public @ResponseBody MessageResult checkRepeatByCol(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramJson=JacksonUtils.toJson(map);
		try {
			if(!map.containsKey("checkType") || StringUtils.isBlank(map.get("checkType").toString())){
				throw new Exception("checkType不可为空");
			}
			if(!map.containsKey("value") || StringUtils.isBlank(map.get("value").toString())){
				throw new Exception("value不可为空");
			}
			map.put("param", map.get("value").toString());
			//调用luorongxin的dubbo服务
			String dubboResultInfo=tendTrialDtoServiceCustomer.checkDuplicate(null, JacksonUtils.toJson(map));
			result=JacksonUtils.fromJson(dubboResultInfo, MessageResult.class);
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用verifByVcode方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("失败【"+e.getMessage()+"】");
		}
		return result;
	}
	/**
	 * 注册发送验证码
	 * @param map
	 * @param request
	 */
	@RequestMapping(value="/registerSendCode",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult registerSendCode(@RequestBody Map<String,Object> map,HttpServletRequest request){
	    MessageResult result=new MessageResult();
	    String paramaterJson = JacksonUtils.toJson(map);
	    try {
		    if(!map.containsKey("phone")){
		    	throw new Exception("手机号不可为空");
		    }
		    //用户手机号
		    String phone=map.get("phone").toString();
		    
		    SecurityUserBeanInfo securityUserBeanInfo=new SecurityUserBeanInfo();
		    securityUserBeanInfo.setSecurityUserDto(new SecurityUserDto());
		    String userInfo=JacksonUtils.toJson(securityUserBeanInfo);
		    //获取验证码存入redis
			String vCode=sixCode();//验证码
		    String key="registerCode_"+phone+"_"+request.getRemoteAddr();
			redisTemplate.opsForValue().set(key, vCode,120,TimeUnit.SECONDS);
			//发送短信
			String resInfo=null;
			Map<String,Object> msg=new HashMap<String,Object>();
			msg.put("phones", phone);
			msg.put("msg", String.format("您的验证码为：%s，请在2分钟内输入", vCode));
			String msgJson=JacksonUtils.toJson(msg);
			resInfo=sysNoticePhoneMsgDtoServiceCustomer.sendMsg(userInfo, msgJson);
			DubboServiceResultInfo dubboResInfo=JacksonUtils.fromJson(resInfo, DubboServiceResultInfo.class);
			if (dubboResInfo.isSucess()) {
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
				result.setResult(dubboResInfo.getResult());
			}else{
			   result.setSuccess(MessageInfo.GETERROR.isResult());
			   result.setMsg(dubboResInfo.getMsg());
			   result.setResult(dubboResInfo.getResult());
		   }
	    } catch (Exception e) {
	    	log.error("调用registerSendCode方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("失败【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	/**
	 * 校验用户输入的验证码是否正确--注册
	 * @param map 手机号
	 * @return 
	 */
	@RequestMapping(value="/registerVerifCode",method=RequestMethod.POST)
	public @ResponseBody MessageResult registerVerifCode(@RequestBody Map<String,Object> map,HttpServletRequest request){
		MessageResult result=new MessageResult();
		String paramJson=JacksonUtils.toJson(map);
		try {
			if(!map.containsKey("phone")){
		    	throw new Exception("手机号不可为空");
		    }
			if(!map.containsKey("verifCode")){
				throw new Exception("验证码不可为空");
			}
		    //用户手机号
		    String phone=map.get("phone").toString();
			String verifCode=(String)map.get("verifCode");
			String ip=request.getRemoteAddr();
			String key="registerCode_"+phone+"_"+ip;
			String redisCode=redisTemplate.opsForValue().get(key);//redis获取验证码
			if(redisCode==null ){
				throw new Exception("验证码失效");
			}
			if(!verifCode.equals(redisCode)){
				throw new  Exception("验证码错误");
			}
			//删除redis中的验证码
			redisTemplate.delete(key);
			
			//TODO 调用luorongxin的dubbo服务-注册
			TendUserDto tend=new TendUserDto();
			tend.setPhone(phone);
			tend.setName(map.get("name").toString());
			tend.setWechat(map.get("wechat").toString());
			tend.setEmail(map.get("email").toString());
			tend.setCompanyName(map.get("companyName").toString());
			tend.setPersonNum(map.get("personNum").toString());
			tend.setDuty(map.get("duty").toString());
			//tend.setIndustry(map.get("industry").toString());
			String dubboResultInfo=tendTrialDtoServiceCustomer.registerTendUser(null, JacksonUtils.toJson(tend));
			result=JacksonUtils.fromJson(dubboResultInfo, MessageResult.class);
			//
			try {
		    	Map<String, Object> logMap=new HashMap<String, Object>();
		    	logMap.put("ip", request.getRemoteAddr());
		    	logMap.put("loginBrowser", getIpAddress(request));
		    	logMap.put("msg", result.getMsg());
		    	logMap.put("result", result.getMsg());
		    	if (result.isSuccess()&&result.getResult()!=null) {
		    		TendUserDto t=(TendUserDto)result.getResult();
		    		if(t!=null){
		    			logMap.put("loginName",t.getInitAccount());
		    		}
				}
		    	logMap.putAll(map);
		    	authenticationDtoServiceCustomer.registerLog(null,JacksonUtils.toJson(logMap));
		    } catch (Exception e) {
		    }
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用registerVerifCode方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("失败【"+e.getMessage()+"】");
		}
		return result;
	}
	
	//TODO tools
	/**
	 * @return 六位随机数
	 */
	public static String sixCode() {
		String ret = "";
		int randomNum = (int) (Math.random() * 900000 + 100000);
		ret = String.valueOf(randomNum);
		return ret;
	}
	/**
	 * @return 模糊邮箱
	 */
	public static String mailToStar(String email) {
		if(email.length()-email.indexOf("@")<4){
			return email;
		}
		String rep=email.substring(4, email.indexOf("@"));
		StringBuffer star=new StringBuffer();
		for (int i = 0; i < rep.length(); i++) {
			star.append("*");
		}
		email=email.replace(rep, star.toString());
		return email;
	}
	/**
	 * @return 模糊手机或
	 */
	public static String phoneToStar(String phone) {
		if(phone.length()<10){
			return "";
		}
		String rep=phone.substring(3, 7);
		phone=phone.replace(rep, "****");
		return phone;
	}
	
	/**
	 * 校验用户输入的验证码是否正确--注册
	 * @param map 手机号
	 * @return 
	 */
	@RequestMapping(value="/postCode",method=RequestMethod.POST)
	public @ResponseBody MessageResult postCode(@RequestBody Map<String,Object> map,HttpServletRequest request){
		MessageResult result=new MessageResult();
		String paramJson=JacksonUtils.toJson(map);
		try {
			 //用户手机号
		    String phone=map.get("phone").toString();
		    
		    String resultJson = tendTrialDtoServiceCustomer.postAuthCode(null, phone,"sms");
		    result = JacksonUtils.fromJson(resultJson, MessageResult.class);
		    result.setResult(null);
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用registerVerifCode方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("系统繁忙，请稍后重试！");
		}
		return result;
	}
	
	/**
	 * 校验用户输入的验证码是否正确--注册
	 * @param map 手机号
	 * @return 
	 */
	@RequestMapping(value="/checkAndRegister",method=RequestMethod.POST)
	public @ResponseBody MessageResult checkAndRegister(@RequestBody Map<String,Object> map,HttpServletRequest request){
		MessageResult result=new MessageResult();
		String paramJson=JacksonUtils.toJson(map);
		try {
			 //用户手机号
		    String phone=map.get("phone").toString();
		    //用户手机号验证码
		    String verifCode=map.get("verifCode").toString();
		    
		    String resultJson = tendTrialDtoServiceCustomer.checkAuthCode(null, phone, verifCode);
		    result = JacksonUtils.fromJson(resultJson, MessageResult.class);
		    if(result.isSuccess()){
		    	String registerTendUserJson = tendTrialDtoServiceCustomer.registerTendUser(null, JacksonUtils.toJson(map));
		    	result = JacksonUtils.fromJson(registerTendUserJson, MessageResult.class);
		    }
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用registerVerifCode方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("系统繁忙，请稍后重试！");
		}
		return result;
	}

	/**
	 * 产生二维码对应redis的值
	 */
	public String  bindQRToUser() throws Exception {
		Map<String,String> cloudMap = new HashMap();
		long startTime = System.currentTimeMillis ();
		cloudMap.put("tendId","");//租户id
		cloudMap.put("sysId","");//系统id
		cloudMap.put("appId","");//应用id
		cloudMap.put("createTime",String.valueOf(startTime));//二维码创建时间，毫秒级时间戳
		cloudMap.put("token","");//是否已扫码

		String qrContent = JacksonUtils.toJson(cloudMap);
		String key = getOrderIdByUUId();//"QR_"+user.getTendId()+"_"+user.getSecurityUserDto().getId()+"_"+ip;

		redisTemplate.opsForValue().set(key,qrContent);
		redisTemplate.expire(key,12,TimeUnit.HOURS);
		return key;
	}

	/**
	 * 产生用户登陆二维码
	 * @return
	 */
	public byte[] getQRByUser(String k){
		byte[] b = null;
		try {
			/*
				BI登录格式参数： http://im.xyre.com/xyre-mobile/s?f=10001&a=10001&k=3kdkdjgssekkfkkfdsfa
				f：10001                                        （此参数OA方面可根据参数范围自行定值，但是务必告诉im平台，因为我们要根据此参数来确定移动端要显示的内容，因为可能存在其他系统使用扫描登录功能）
				a：10001                                         （此参数为移动端扫描登录解析的固定值，不可更改）
				k：动态生成唯一标识符不要超过16位
			*/
			String MDVal = qrCode+k;
			BufferedImage image = QRCodeUtil.getEncodeImage(MDVal,null,"", true);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			boolean flag = ImageIO.write(image, "jpg", out);
			b = out.toByteArray();
		} catch (Exception e) {
			log.error("调用getQRByUser方法======"+"【"+e.getMessage()+"】");
		}
		return b;
	}
	/**
	 * 产生用户登陆二维码
	 * @return
	 */
	@RequestMapping(value="/getQRByUser",method=RequestMethod.POST)
	public @ResponseBody MessageResult getQRByUser(@RequestBody Map<String,Object> map,HttpServletRequest request){
		MessageResult result=new MessageResult();
		String paramJson=JacksonUtils.toJson(map);
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			String ip = request.getRemoteAddr();
			String key = "QR_"+user.getTendId()+"_"+user.getSecurityUserDto().getId()+"_"+ip;
			String value = redisTemplate.opsForValue().get(key);
			if(null==value||"".equals(value)){
				//bindQRToUser(user,ip);
				value = redisTemplate.opsForValue().get(key);
			}
			BufferedImage image = QRCodeUtil.getEncodeImage(value,null,"", true);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			boolean flag = ImageIO.write(image, "jpg", out);
			byte[] b = out.toByteArray();
			result.setResult(b);
			result.setSuccess(MessageInfo.GETSUCCESS.isResult());
			result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		} catch (Exception e) {
			log.error("调用getQRByUser方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("系统繁忙，请稍后重试！");
		}
		return result;
	}

	/**
	 * 用户轮询跳转页面
	 * @return
	 */
	@RequestMapping(value="/sendUrl",method=RequestMethod.POST)
	public @ResponseBody MessageResult sendUrl(HttpServletResponse response,HttpServletRequest request){
		MessageResult result=new MessageResult();
		String ip = request.getRemoteAddr();
		try {
			//获取用户对象
			String prourl =  request.getParameter("prourl");//"QR_"+user.getTendId()+"_"+user.getSecurityUserDto().getId()+"_"+ip;
			String biUrl = prourl.substring(0,prourl.indexOf("&qr="));
			String key = prourl.substring(prourl.indexOf("&qr=")+4);
			// 验证二维码有效性
			Boolean hasQR = redisTemplate.hasKey(key);
			if(hasQR == true){
				String value = redisTemplate.opsForValue().get(key);
				Map hashMap = JacksonUtils.fromJson(value,HashMap.class);
				String token = hashMap.get("token").toString();
				if(token.length()>0){
					HttpSession session = request.getSession();
					String loginInfoStr = redisTemplate.opsForValue().get(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
					String menuInfoStr = redisTemplate.opsForValue().get(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + token);
					SecurityUserBeanInfo securityUserBeanInfo = JacksonUtils.fromJson(loginInfoStr, SecurityUserBeanInfo.class);
					SecurityUserBeanRelationInfo securityUserBeanRelationInfo = JacksonUtils.fromJson(menuInfoStr, SecurityUserBeanRelationInfo.class);

					session.setAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER,securityUserBeanInfo);
					session.setAttribute(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU,securityUserBeanRelationInfo);
					// 将cookie信息写入用户安全信息中
					StringBuilder sb = new StringBuilder();
					Cookie[] cookies = request.getCookies();
					if (cookies != null) {
						for (Cookie cookie : cookies) {
							sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
						}
						securityUserBeanInfo.setCookies(sb.toString());
					}
					//判断用户权限
					//当前登录的所有信息
					List<SecurityResourceDto>  list = securityUserBeanRelationInfo.getResourceDtoList();
					for(SecurityResourceDto srDto:list){
						if(srDto.getIsAuth().equals("1") && srDto.getType().equals("APPSystem") ){
							//匹配经营分析系统 --- 1.绑定唯一二维码标识到用户  2.生成二维码
							if("DEMOBI".equals(srDto.getCode())){
								biUrl = srDto.getResourceUrl();
								break;
							}
						}
					}
					result.setResult(biUrl);
					result.setSuccess(MessageInfo.GETSUCCESS.isResult());
					hashMap.put("token","");
					hashMap.put(key,JacksonUtils.toJson(hashMap));
					redisTemplate.opsForValue().set(key,JacksonUtils.toJson(hashMap));
				}else{//不存的token
					result.setSuccess(MessageInfo.GETERROR.isResult());
				}
			}else{//失效二维码
				BASE64Encoder encoder = new sun.misc.BASE64Encoder();
				String newKey = bindQRToUser();
				result.setResult(encoder.encodeBuffer(getQRByUser(newKey)).trim());
				result.setMsg(newKey);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
			}
		} catch (Exception e) {
			log.error("调用sendUrl方法======"+"【"+e.getMessage()+"】");
		}
		return result;
	}
	/**
	 * 获取客户端ip
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if(index != -1){
				return ip.substring(0,index);
			}else{
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
			return ip;
		}
		return request.getRemoteAddr();
	}
	/**
	 * 获取uuid16位
	 * @return
	 */
	public static String getOrderIdByUUId() {
		String machineId = "qr";//最大支持1-9个集群机器部署
		int hashCodeV = UUID.randomUUID().toString().hashCode();
		if(hashCodeV < 0) {//有可能是负数
			hashCodeV = - hashCodeV;
		}
		// 0 代表前面补充0
		// 15 代表长度为15
		// d 代表参数为正数型
		return machineId + String.format("%014d", hashCodeV);
	}

	@RequestMapping(value="/queryUserPostRoleList",method=RequestMethod.POST)
	public @ResponseBody MessageResult queryUserPostRoleList(@RequestBody Map<String,Object> map,HttpServletRequest request){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo = authenticationDtoServiceCustomer.queryUserPostRoleList(userInfo, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<Map<String,Object>> authenticationDto =JacksonUtils.fromJson(resultInfo, List.class,Map.class);
				result.setResult(authenticationDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}

		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

}
