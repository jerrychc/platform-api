package com.xinleju.cloud.oa.email.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserDto;
import com.xinleju.platform.encrypt.EndecryptUtil;
import com.xinleju.platform.finance.utils.excel.DateUtil;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.sys.org.dto.service.UserDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;

import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.EmailMessageSchema;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.FindItemsResults;
import microsoft.exchange.webservices.data.Folder;
import microsoft.exchange.webservices.data.Item;
import microsoft.exchange.webservices.data.ItemView;
import microsoft.exchange.webservices.data.SearchFilter;
import microsoft.exchange.webservices.data.WebCredentials;
import microsoft.exchange.webservices.data.WellKnownFolderName;

/**
 * Created by admin on 2017/6/26.
 */
@Controller
@RequestMapping("/oa/email/userEmail")
public class UserEmailController {
    private static Logger log = Logger.getLogger(UserEmailController.class);
    @Autowired
	private UserDtoServiceCustomer userDtoServiceCustomer;
    
    
    @RequestMapping(value="/getUserEmail",method= RequestMethod.GET)
    public @ResponseBody
    MessageResult getUserEmail(){
        MessageResult result=new MessageResult();
        try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            SecurityUserDto securityUserDto = securityUserBeanInfo.getSecurityUserDto();
            String userId = securityUserDto.getId();
            String emailPwd = securityUserDto.getEmailPwd();
//            String email = securityUserDto.getEmail();
            String loginName = securityUserDto.getLoginName();
//            Map<String,Object> emailInfoMap = getUserEmailInfo(userId,email,emailPwd);
            Map<String,Object> emailInfoMap = getXyreEmailInfo(userId,loginName,emailPwd);
            if(emailInfoMap!=null){
            	boolean successFlag = (boolean) emailInfoMap.get("success");
            	String msg = (String) emailInfoMap.get("msg");
            	//List<Map<String,String>> emailInfoList = (List<Map<String, String>>) emailInfoMap.get("emailInfoList");
            	result.setResult(emailInfoMap);
            	result.setSuccess(successFlag);
            	result.setMsg(msg);
            }else{
            	result.setSuccess(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用getUserEmail方法:  【"+e.getMessage()+"】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
        }
        return result;
    }
    @RequestMapping(value="/getUserXyre",method= RequestMethod.POST)
    @ResponseBody
    public MessageResult getUserXyre(){
    	MessageResult result=new MessageResult();
    	try {
    		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
    		SecurityUserDto securityUserDto = securityUserBeanInfo.getSecurityUserDto();
    		String userId = securityUserDto.getId();
    		String emailPwd = securityUserDto.getEmailPwd();
    		//从数据库重新获取emailPwd
			String dubboResultInfo = userDtoServiceCustomer.getObjectById(JacksonUtils.toJson(securityUserBeanInfo),  "{\"id\":\""+userId+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				UserDto userDto=JacksonUtils.fromJson(resultInfo, UserDto.class);
				emailPwd = userDto.getEmailPwd();
			}else{
				return null;
			}
    		String loginName = securityUserDto.getLoginName();

			//解密邮箱密码
			EndecryptUtil endecryptUtil = new EndecryptUtil();
			String emailPassword = endecryptUtil.get3DESDecrypt(emailPwd,userId);
			Map<String, Object> res=new HashMap<>();
			res.put("vstrServer", "mail.xyre.com");
			res.put("vstrUsername", loginName);
			res.put("vstrPassword", emailPassword);
			result.setSuccess(true);
			result.setResult(res);
			result.setMsg("成功");
    	} catch (Exception e) {
    		e.printStackTrace();
    		log.error("调用getUserXyre方法:  【"+e.getMessage()+"】");
    		result.setSuccess(MessageInfo.GETERROR.isResult());
    		result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
    	}
    	return result;
    }

    /**
     * 获取用户前10条邮件信息
     * @param userId
     * @param emailUserName
     * @param emailPwd
     * @return
     */
    private Map<String,Object> getUserEmailInfo(String userId,String emailUserName, String emailPwd){
    	 Map<String,Object> resultMap = new HashMap<String, Object>() ;
    	
       /* //判断用户邮箱信息是否为空
        if (StringUtils.isBlank(userId)||StringUtils.isBlank(emailUserName)||StringUtils.isBlank(emailPwd)){
            resultMap.put("success",false);
            resultMap.put("msg","用户邮箱/邮箱密码不能为空！");
            return resultMap;
        }

        //电子邮件格式验证
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(emailUserName);
        boolean isMatched = matcher.matches();
        if (!isMatched) {
            resultMap.put("success",false);
            resultMap.put("msg","用户邮箱格式不正确！");
            return resultMap;
        }

        //解密邮箱密码
        EndecryptUtil endecryptUtil = new EndecryptUtil();
        String emailPassword = endecryptUtil.get3DESDecrypt(emailPwd,userId);

        //从邮箱中获取邮箱host
        String emailHost = emailUserName.substring(emailUserName.indexOf("@")+1);
        emailHost = "imap."+emailHost;


        //设置SSL连接、邮件环境
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties props = System.getProperties();
        props.setProperty("mail.imap.socketFactory.class" , SSL_FACTORY);
        props.setProperty("mail.imap.socketFactory.fallback","false");
        props.setProperty("mail.imap.port","993" );
        props.setProperty("mail.imap.socketFactory.port","993");

        //建立邮件会话
        Session session = Session.getDefaultInstance(props, null);
        URLName url = new URLName("imap", emailHost, 993, null, emailUserName, emailPassword);
        Store store = null;
        Folder inbox = null;
        try {
            //得到邮件仓库并连接
            store = session.getStore(url);
            store.connect();
            Folder defaultFolder = store.getDefaultFolder();

            Folder[] allFolder = defaultFolder.list();
            for (Folder folder:allFolder) {
                System.out.println(folder.getName());
            }

            //得到收件箱并抓取邮件
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            //默认每次获取最近10条邮件数据
            int startIndex = inbox.getMessageCount()>7?inbox.getMessageCount()-7:1;
            Message[] messages = inbox.getMessages(inbox.getMessageCount()-inbox.getUnreadMessageCount()+1,inbox.getMessageCount());
            //打印收件箱邮件部分信息
            int length = messages.length;
            resultMap.put("unReadCount",inbox.getUnreadMessageCount());//获取未读邮件数量
            List<Map<String,String>> emailInfoList = new LinkedList<Map<String,String>>();
            for (int i = length-1; i >= length-9; i--) {
                Map<String,String> emailMap = new HashMap<String,String>();
                String from = MimeUtility.decodeText(messages[i].getFrom()[0].toString());
                InternetAddress ia = new InternetAddress(from);

                emailMap.put("sendUser",ia.getPersonal() + "(" + ia.getAddress() + ")");
                emailMap.put("emailSubject",messages[i].getSubject());
                Date sentDate = messages[i].getSentDate();
                emailMap.put("emailSendTime",sentDate!=null?new SimpleDateFormat("yyyy-MM-dd").format(sentDate):"");
                Flags flags = messages[i].getFlags();
                if (!flags.contains(Flags.Flag.SEEN)){
                    emailInfoList.add(emailMap);
                }  
            }
            resultMap.put("emailInfoList",emailInfoList);
            resultMap.put("success",true);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("msg","获取用户邮箱列表失败");
            log.error("获取用户邮箱列表失败："+e.getMessage());
        } finally {
            //关闭收件箱
            if(inbox!=null){
                try {
                    inbox.close(false);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            //关闭邮箱连接信息
            if (store!=null){
                try {
                    store.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

        }*/

        return resultMap;
    }
    /**
     * 获取用户前Excheng10条邮件信息
     * @param userId
     * @param emailUserName
     * @param emailPwd
     * @return
     */
    public Map<String,Object> getXyreEmailInfo(String userId,String emailUserName, String emailPwd){
        try {
			Map<String,Object> resultMap = new HashMap<String, Object>() ;
			//判断用户邮箱信息是否为空
			if (StringUtils.isBlank(userId)||StringUtils.isBlank(emailUserName)||StringUtils.isBlank(emailPwd)){
			    resultMap.put("success",false);
			    resultMap.put("msg","用户邮箱/邮箱密码不能为空！");
			    return resultMap;
			}

			//解密邮箱密码
			EndecryptUtil endecryptUtil = new EndecryptUtil();
			String emailPassword = endecryptUtil.get3DESDecrypt(emailPwd,userId);
			
			
			List<Map<String,Object>> emailInfoList=new ArrayList<>();
			//ExchangeService版本  
			ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010);  
			//用户名、密码、域  
			ExchangeCredentials credentials = new WebCredentials(emailUserName, emailPassword, "xyre");  
			service.setCredentials(credentials);  
			//设置邮件服务器地址  
			service.setUrl(new URI("https://"+"mail.xyre.com"+"/EWS/Exchange.asmx"));  
			  
			//创建过滤器  
			Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);  
			int unread = inbox.getUnreadCount();
			resultMap.put("unReadCount",unread);//获取未读邮件数量
			//创建过滤器条件，查询10封邮件  
			ItemView view = new ItemView(10);  
			  
			//查询  
//			FindItemsResults<Item> findResults = service.findItems(inbox.getId(), view);  
			//查询未读
		    FindItemsResults<microsoft.exchange.webservices.data.Item> findResults = service.findItems(inbox.getId(),new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false), view);  
			for (Item item : findResults.getItems()) {  
				Map<String,Object> m=new HashMap<String,Object>();
			    EmailMessage message = EmailMessage.bind(service, item.getId());  
			    message.load();  
			   /* System.out.println(message.getSender());  
			    System.out.println("Sub -->" + item.getSubject());*/
//			    m.put("sendUser", message.getSender().toString());
			    m.put("sendUser", message.getFrom().getName());
			    m.put("emailSubject", item.getSubject());
			    m.put("emailSendTime",DateUtil.format(item.getDateTimeSent(), DateUtil.PATTERN_GRACE_NORMAL) );
			    emailInfoList.add(m);
			}
			resultMap.put("emailInfoList",emailInfoList);
            resultMap.put("success",true);
            return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}  
	    
    }
}
