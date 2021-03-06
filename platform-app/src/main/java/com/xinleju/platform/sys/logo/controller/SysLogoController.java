package com.xinleju.platform.sys.logo.controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageInputStreamImpl;
import javax.servlet.http.HttpServletResponse;

import com.xinleju.cloud.oa.util.CompressImgUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.cloud.oa.shortcutmenu.dto.ShortcutMenuDto;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.sys.logo.dto.SysLogoDto;
import com.xinleju.platform.sys.logo.dto.service.SysLogoDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 平台logo控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/sys/sysLogo")
public class SysLogoController {

	private static Logger log = Logger.getLogger(SysLogoController.class);
	
	@Autowired
	private SysLogoDtoServiceCustomer sysLogoDtoServiceCustomer;
	/**
	 * 根据Id获取业务对象
	 * 
	 * @param id  业务对象主键
	 * 
	 * @return     业务对象
	 */
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult get(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=sysLogoDtoServiceCustomer.getObjectById(getUserInfoJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysLogoDto sysLogoDto=JacksonUtils.fromJson(resultInfo, SysLogoDto.class);
				if(sysLogoDto.getIcon() != null){
					sysLogoDto.setPic(Base64.getEncoder().encodeToString(sysLogoDto.getIcon()));
				}
				
				result.setResult(sysLogoDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用get方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	
	/**
	 * 返回分页对象
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
		    String dubboResultInfo=sysLogoDtoServiceCustomer.getPage(getUserInfoJson(), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用page方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	/**
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			String dubboResultInfo=sysLogoDtoServiceCustomer.queryList(getUserInfoJson(), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysLogoDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysLogoDto.class);
				if(list != null && list.size() > 0){
					for(int i = 0; i < list.size(); i++){
						byte[] icon = list.get(i).getIcon();
						if(icon != null){
							list.get(i).setPic(Base64.getEncoder().encodeToString(icon));
						}
					}
				}
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 返回符合条件的列表(get请求)
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/getLogo",method=RequestMethod.GET)
	public @ResponseBody MessageResult getLogo(){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=sysLogoDtoServiceCustomer.queryList(getUserInfoJson(), null);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
		    	SysLogoDto sysLogoDto = null;
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysLogoDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysLogoDto.class);
				if(list != null && list.size() > 0){
					/*for(int i = 0; i < list.size(); i++){
						byte[] icon = list.get(i).getIcon();
						byte[] icon2 = icon;//CompressImgUtil.compressImg2(icon,119,36);
						if(icon.length>32*1024){
							icon2 = CompressImgUtil.compressImg2(icon,119,36);
						}
						if(icon2 != null){
							list.get(i).setPic(Base64.getEncoder().encodeToString(icon2));
						}
					}*/
					if(null==list.get(0).getUrl()||"".equals(list.get(0).getUrl())){
						sysLogoDto = new SysLogoDto();
						sysLogoDto.setUrl("sysManager/logo/logo.jpg");
						sysLogoDto.setName("logo");
					}else{
						sysLogoDto = list.get(0);
					}
				}else{
					sysLogoDto = new SysLogoDto();
					sysLogoDto.setUrl("sysManager/logo/logo.jpg");
					sysLogoDto.setName("logo");
				}
				result.setResult(sysLogoDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用getLogo方法:【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}


	/**
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value = "/save",method = RequestMethod.POST)
    public void save(MultipartHttpServletRequest request, HttpServletResponse response) {
        MessageResult result = new MessageResult();
        SysLogoDto t = new SysLogoDto();
        PrintWriter pw = null;
		Boolean isReturn = true;
        try {
        	response.setContentType("text/html;charset=UTF-8");
        	pw = response.getWriter();
			MultipartFile uploadfile =  request.getFile("icon");
        	if(uploadfile != null){
        		long length = uploadfile.getSize();
        		if(length>1*1024*1024){
        			result.setSuccess(false);
        			result.setMsg("图片尺寸不能大于1M");
        			pw.print(JacksonUtils.toJson(result));
					pw.flush();
					isReturn = false;
        		}
        	}
    		if(isReturn){
	    		String id = request.getParameter("id");
	    		String name = request.getParameter("name");
				String isDelPic = request.getParameter("isDelPic");
				byte[] headpic={};
				if("1".equals(isDelPic) && uploadfile != null){
					InputStream is = uploadfile.getInputStream();
					headpic = new byte[is.available()];  
					is.read(headpic);  
					is.close();
				}
				t.setId(id);
				t.setName(name);
				t.setIcon(headpic);
				t.setIsDelPic(isDelPic);
	            String saveJson = JacksonUtils.toJson(t);
	            String dubboResultInfo = sysLogoDtoServiceCustomer.save(getUserInfoJson(), saveJson);
	            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
	            if (dubboServiceResultInfo.isSucess()) {
	                String resultInfo = dubboServiceResultInfo.getResult();
	                ShortcutMenuDto shortcutMenuDto = JacksonUtils.fromJson(resultInfo, ShortcutMenuDto.class);
	                result.setResult(shortcutMenuDto);
	                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
	                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
	            } else {
	                result.setSuccess(MessageInfo.SAVEERROR.isResult());
	                String msg = dubboServiceResultInfo.getExceptionMsg()==null?dubboServiceResultInfo.getMsg():dubboServiceResultInfo.getExceptionMsg();
	                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + msg + "】");
	            }
	            pw.print(JacksonUtils.toJson(result));
				pw.flush();
    		}
        } catch (Exception e) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(t);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
                pw.print(JacksonUtils.toJson(result));
				pw.flush();
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }finally {
			try {
				pw.close();
			} catch (Exception e){}
		}
    }
	
	/**
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=sysLogoDtoServiceCustomer.deleteObjectById(getUserInfoJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysLogoDto sysLogoDto=JacksonUtils.fromJson(resultInfo, SysLogoDto.class);
				result.setResult(sysLogoDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用delete方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	
	/**
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deleteBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=sysLogoDtoServiceCustomer.deleteAllObjectByIds(getUserInfoJson(), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysLogoDto sysLogoDto=JacksonUtils.fromJson(resultInfo, SysLogoDto.class);
				result.setResult(sysLogoDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用delete方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	/**
	 * 修改修改实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public void update(MultipartHttpServletRequest request, HttpServletResponse response) {
        MessageResult result = new MessageResult();
        SysLogoDto t = new SysLogoDto();
        PrintWriter pw = null;
		Boolean isReturn = true;
        try {
        	/*response.setContentType("text/html;charset=UTF-8");
        	pw = response.getWriter();
			MultipartFile uploadfile =  request.getFile("icon");
        	if(uploadfile != null){
        		long length = uploadfile.getSize();
        		if(length>1*1024*1024){
        			result.setSuccess(false);
        			result.setMsg("图片尺寸不能大于1M");
        			pw.print(JacksonUtils.toJson(result));
					pw.flush();
					isReturn = false;
        		}
        	}*/
    		if(isReturn){
	    		String id = request.getParameter("id");
	    		String name = request.getParameter("name");
				String url = request.getParameter("url");
				/*byte[] headpic={};
				if("1".equals(isDelPic) && uploadfile != null){
					InputStream is = uploadfile.getInputStream();
					headpic = new byte[is.available()];  
					is.read(headpic);  
					is.close();  
				}*/
				t.setId(id);
				t.setName(name);
				t.setUrl(url);
	            String saveJson = JacksonUtils.toJson(t);
	            String dubboResultInfo = sysLogoDtoServiceCustomer.update(getUserInfoJson(), saveJson);
	            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
	            if (dubboServiceResultInfo.isSucess()) {
	                String resultInfo = dubboServiceResultInfo.getResult();
	                ShortcutMenuDto shortcutMenuDto = JacksonUtils.fromJson(resultInfo, ShortcutMenuDto.class);
	                result.setResult(shortcutMenuDto);
	                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
	                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
	            } else {
	                result.setSuccess(MessageInfo.SAVEERROR.isResult());
	                String msg = dubboServiceResultInfo.getExceptionMsg()==null?dubboServiceResultInfo.getMsg():dubboServiceResultInfo.getExceptionMsg();
	                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + msg + "】");
	            }
	            //pw.print(JacksonUtils.toJson(result));
				//pw.flush();
    		}
        } catch (Exception e) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(t);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
                //pw.print(JacksonUtils.toJson(result));
				//pw.flush();
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
    }

	/**
	 * 伪删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=sysLogoDtoServiceCustomer.deletePseudoObjectById(getUserInfoJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysLogoDto sysLogoDto=JacksonUtils.fromJson(resultInfo, SysLogoDto.class);
				result.setResult(sysLogoDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用deletePseudo方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	
	/**
	 * 伪删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudoBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=sysLogoDtoServiceCustomer.deletePseudoAllObjectByIds(getUserInfoJson(), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysLogoDto sysLogoDto=JacksonUtils.fromJson(resultInfo, SysLogoDto.class);
				result.setResult(sysLogoDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    log.error("调用deletePseudoBatch方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	/**
	 * 获取用户信息
	 * @return 用户信息JSON格式字符串
	 */
	private String getUserInfoJson(){
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
		return userInfo;
	}
	
	/** 
     * 对象转数组 
     * @param obj 
     * @return 
	 * @throws IOException 
     */  
    public byte[] toByteArray (Object obj) throws IOException {     
        byte[] bytes = null;     
        ByteArrayOutputStream bos = new ByteArrayOutputStream();     
        ObjectOutputStream oos = new ObjectOutputStream(bos);        
        oos.writeObject(obj);       
        oos.flush();        
        bytes = bos.toByteArray ();     
        oos.close();        
        bos.close();       
        return bytes;   
    }  
	
}
