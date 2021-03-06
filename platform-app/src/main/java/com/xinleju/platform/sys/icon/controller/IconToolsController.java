package com.xinleju.platform.sys.icon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.cloud.oa.shortcutmenu.dto.ShortcutMenuDto;
import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.sys.icon.dto.IconToolsDto;
import com.xinleju.platform.sys.icon.dto.service.IconToolsDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.csource.common.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 平台icon控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/sys/iconTools")
public class IconToolsController {

	private static Logger log = Logger.getLogger(IconToolsController.class);

	@Value("#{configuration['attachment.port']}")
	private String attachmentPort;
	@Autowired
	private IconToolsDtoServiceCustomer iconToolsDtoServiceCustomer;
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
			String dubboResultInfo=iconToolsDtoServiceCustomer.getObjectById(getUserInfoJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				IconToolsDto iconToolsDto=JacksonUtils.fromJson(resultInfo, IconToolsDto.class);
				result.setResult(iconToolsDto);
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
		    String dubboResultInfo=iconToolsDtoServiceCustomer.getPage(getUserInfoJson(), paramaterJson);
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
			String dubboResultInfo=iconToolsDtoServiceCustomer.queryList(getUserInfoJson(), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<IconToolsDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,IconToolsDto.class);
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
	@RequestMapping(value="/getIcon/{code}",method=RequestMethod.GET)
	public @ResponseBody MessageResult getIcon(@PathVariable("code")  String code){
		MessageResult result=new MessageResult();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			map.put("code",code);
			map.put("delflag",0);
			String paramaterJson = JacksonUtils.toJson(map);
			String dubboResultInfo=iconToolsDtoServiceCustomer.queryList(getUserInfoJson(), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
		    	IconToolsDto iconToolsDto = null;
				String resultInfo= dubboServiceResultInfo.getResult();
				List<IconToolsDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,IconToolsDto.class);
				if(list.size()>0) iconToolsDto = list.get(0);
				result.setResult(iconToolsDto);
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
        IconToolsDto t = new IconToolsDto();
        PrintWriter pw = null;
		Boolean isReturn = true;
        try {
        	response.setContentType("text/html;charset=UTF-8");
        	pw = response.getWriter();
			MultipartFile uploadfile =  request.getFile("pic");
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
				String code = request.getParameter("code");
				String uploadFileName = "";
				String fileExtName = "";
				if (null != uploadfile) {
					// 文件名
					uploadFileName = uploadfile.getOriginalFilename();
					int dotIndex = uploadFileName.lastIndexOf('.');
					fileExtName = uploadFileName.substring(dotIndex + 1);
					// 元数据信息
					NameValuePair[] metaList = new NameValuePair[3];
					metaList[0] = new NameValuePair("fileName", uploadFileName);
					metaList[1] = new NameValuePair("fileExtName",fileExtName );
					metaList[2] = new NameValuePair("fileLength", String.valueOf(uploadfile.getSize()));
					String[] upResults = new FastDFSClient().upload(uploadfile.getBytes(), fileExtName,metaList);
					t.setUrl("http://"+new FastDFSClient().getFileAddrIP(upResults[0], upResults[1])+":"+attachmentPort+"/"+upResults[0] + "/" + upResults[1]);
					t.setExtendName(fileExtName);
					t.setFullName(uploadFileName);
					t.setIconSize(uploadfile.getSize());
				}
				t.setId(id);
				t.setCode(code);
				t.setName(name);
				t.setDelflag(false);
	            String saveJson = JacksonUtils.toJson(t);
	            String dubboResultInfo = iconToolsDtoServiceCustomer.save(getUserInfoJson(), saveJson);
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
			String dubboResultInfo=iconToolsDtoServiceCustomer.deleteObjectById(getUserInfoJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				IconToolsDto iconToolsDto=JacksonUtils.fromJson(resultInfo, IconToolsDto.class);
				result.setResult(iconToolsDto);
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
			String dubboResultInfo=iconToolsDtoServiceCustomer.deleteAllObjectByIds(getUserInfoJson(), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				IconToolsDto iconToolsDto=JacksonUtils.fromJson(resultInfo, IconToolsDto.class);
				result.setResult(iconToolsDto);
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
        PrintWriter pw = null;
		Boolean isReturn = true;
        try {
        	response.setContentType("text/html;charset=UTF-8");
        	pw = response.getWriter();
			MultipartFile uploadfile =  request.getFile("pic");
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
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
				IconToolsDto iconToolsDto = new IconToolsDto();
				String id = request.getParameter("id");
				String dubboResultInfo = iconToolsDtoServiceCustomer
						.getObjectById(userJson, "{\"id\":\"" + id + "\"}");
				DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
						.fromJson(dubboResultInfo, DubboServiceResultInfo.class);

				if (dubboServiceResultInfo.isSucess()) {
					String resultInfo = dubboServiceResultInfo.getResult();
					iconToolsDto = JacksonUtils.fromJson(resultInfo,
							IconToolsDto.class);
					String name = request.getParameter("name");
					String code = request.getParameter("code");
					String isDelPic = request.getParameter("isDelPic");
					iconToolsDto.setCode(code);
					iconToolsDto.setId(id);
					iconToolsDto.setName(name);
					String uploadFileName = "";
					String fileExtName = "";
					if (null != uploadfile) {
						if (uploadfile.getSize() > 0) {
							// 文件名
							uploadFileName = uploadfile.getOriginalFilename();
							int dotIndex = uploadFileName.lastIndexOf('.');
							fileExtName = uploadFileName.substring(dotIndex + 1);
							// 元数据信息
							NameValuePair[] metaList = new NameValuePair[3];
							metaList[0] = new NameValuePair("fileName", uploadFileName);
							metaList[1] = new NameValuePair("fileExtName",fileExtName );
							metaList[2] = new NameValuePair("fileLength", String.valueOf(uploadfile.getSize()));
							String[] upResults = new FastDFSClient().upload(uploadfile.getBytes(), fileExtName,metaList);
							iconToolsDto.setUrl("http://"+new FastDFSClient().getFileAddrIP(upResults[0], upResults[1])+":"+attachmentPort+"/"+upResults[0] + "/" + upResults[1]);
							iconToolsDto.setExtendName(fileExtName);
							iconToolsDto.setFullName(uploadFileName);
							iconToolsDto.setIconSize(uploadfile.getSize());
						}
					}else if("0".equals(isDelPic)){
						iconToolsDto.setUrl(null);
					}
					iconToolsDto.setDelflag(false);
					String updateJson = JacksonUtils.toJson(iconToolsDto);
					String updateDubboResultInfo = iconToolsDtoServiceCustomer
							.update(userJson, updateJson);
					DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils
							.fromJson(updateDubboResultInfo,
									DubboServiceResultInfo.class);
					if (updateDubboServiceResultInfo.isSucess()) {
						Integer i = JacksonUtils.fromJson(
								updateDubboServiceResultInfo.getResult(),
								Integer.class);
						result.setResult(i);
						result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
						result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
					} else {
						result.setSuccess(MessageInfo.UPDATEERROR.isResult());
						result.setMsg(updateDubboServiceResultInfo.getMsg());
					}
				} else {
					result.setSuccess(MessageInfo.UPDATEERROR.isResult());
					result.setMsg("不存在更新的对象");
				}
				pw.print(JacksonUtils.toJson(result));
				pw.flush();
			}
        } catch (Exception e) {
			ObjectMapper mapper = new ObjectMapper();
			result.setSuccess(MessageInfo.SAVEERROR.isResult());
			result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
			pw.print(JacksonUtils.toJson(result));
			pw.flush();
        }finally {
			try {
				pw.close();
			} catch (Exception e){}
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
			String dubboResultInfo=iconToolsDtoServiceCustomer.deletePseudoObjectById(getUserInfoJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				IconToolsDto iconToolsDto=JacksonUtils.fromJson(resultInfo, IconToolsDto.class);
				result.setResult(iconToolsDto);
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
			String dubboResultInfo=iconToolsDtoServiceCustomer.deletePseudoAllObjectByIds(getUserInfoJson(), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				IconToolsDto iconToolsDto=JacksonUtils.fromJson(resultInfo, IconToolsDto.class);
				result.setResult(iconToolsDto);
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
