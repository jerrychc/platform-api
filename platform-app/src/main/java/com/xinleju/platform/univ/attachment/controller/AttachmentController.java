package com.xinleju.platform.univ.attachment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.DocConverter;
import com.xinleju.platform.uitls.HttpUtil;
import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.platform.univ.attachment.dto.service.AttachmentDtoServiceCustomer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.csource.common.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * 附件信息表控制层
 * @author haoqp
 *
 */
@Controller
@RequestMapping("/univ/attachment/attachment")
public class AttachmentController {

	private static Logger log = Logger.getLogger(AttachmentController.class);

	@Autowired
	protected RedisTemplate<String, String> redisTemplate;
	@Autowired
	private AttachmentDtoServiceCustomer attachmentDtoServiceCustomer;
	@Value("#{configuration['attachment.port']}")
	private String attachmentPort;
	
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
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=attachmentDtoServiceCustomer.getObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AttachmentDto attachmentDto=JacksonUtils.fromJson(resultInfo, AttachmentDto.class);
				result.setResult(attachmentDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
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
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
		    String dubboResultInfo=attachmentDtoServiceCustomer.getPage(JacksonUtils.toJson(userInfo), paramaterJson);
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
			////e.printStackTrace();
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
//		map.put("sidx","CONVERT( name USING gbk ) COLLATE gbk_chinese_ci");
//		map.put("sord","asc");
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=attachmentDtoServiceCustomer.queryList(JacksonUtils.toJson(userInfo), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<AttachmentDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,AttachmentDto.class);
				result.setResult(list);
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
	
	/**
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryListByCategoryIds",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryListByCategoryIds(@RequestBody String[] categoryIds){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(categoryIds);
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=attachmentDtoServiceCustomer.queryListByCategoryIds(JacksonUtils.toJson(userInfo), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<AttachmentDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,AttachmentDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryListByIds方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
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
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody AttachmentDto t){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=attachmentDtoServiceCustomer.save(JacksonUtils.toJson(userInfo), saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AttachmentDto attachmentDto=JacksonUtils.fromJson(resultInfo, AttachmentDto.class);
				result.setResult(attachmentDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
		} catch (Exception e) {
			try {
				////e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	/**
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/saveBatch",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult saveBatch(@RequestBody List<AttachmentDto> attList){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String saveJsonList= JacksonUtils.toJson(attList);
			String dubboResultInfo = attachmentDtoServiceCustomer.saveBatch(JacksonUtils.toJson(userInfo), saveJsonList);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<AttachmentDto> attachmentDtos=JacksonUtils.fromJson(resultInfo, ArrayList.class, AttachmentDto.class);
				result.setResult(attachmentDtos);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
		} catch (Exception e) {
			try {
				////e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(attList);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
		}
		return result;
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
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=attachmentDtoServiceCustomer.deleteObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AttachmentDto attachmentDto=JacksonUtils.fromJson(resultInfo, AttachmentDto.class);
				result.setResult(attachmentDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
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
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=attachmentDtoServiceCustomer.deleteAllObjectByIds(JacksonUtils.toJson(userInfo), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AttachmentDto attachmentDto=JacksonUtils.fromJson(resultInfo, AttachmentDto.class);
				result.setResult(attachmentDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
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
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		AttachmentDto attachmentDto=null;
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=attachmentDtoServiceCustomer.getObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=attachmentDtoServiceCustomer.update(JacksonUtils.toJson(userInfo), updateJson);
				 DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
				 if(updateDubboServiceResultInfo.isSucess()){
					 Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
					 result.setResult(i);
					 result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
					 result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
				 }else{
					 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
					 result.setMsg(updateDubboServiceResultInfo.getMsg()+"【"+updateDubboServiceResultInfo.getExceptionMsg()+"】");
				 }
			}else{
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
			}
		} catch (Exception e) {
			try{
			 ////e.printStackTrace();
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(attachmentDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
			}catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
		}
		return result;
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
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=attachmentDtoServiceCustomer.deletePseudoObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AttachmentDto attachmentDto=JacksonUtils.fromJson(resultInfo, AttachmentDto.class);
				result.setResult(attachmentDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
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
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=attachmentDtoServiceCustomer.deletePseudoAllObjectByIds(JacksonUtils.toJson(userInfo), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AttachmentDto attachmentDto=JacksonUtils.fromJson(resultInfo, AttachmentDto.class);
				result.setResult(attachmentDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用deletePseudoBatch方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	/**
	 * 文件上传处理
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public @ResponseBody void upload(MultipartHttpServletRequest request, HttpServletResponse response) {
		PrintWriter pw = null;
		MessageResult result=new MessageResult();
		List<AttachmentDto> files = new LinkedList<AttachmentDto>();
		Iterator<String> itr = request.getFileNames();
		try {
			while (itr.hasNext()) {
				pw = response.getWriter();
				final MultipartFile uploadFile = request.getFile(itr.next());

				final AttachmentDto fileMeta = new AttachmentDto();
				// 文件名
				String uploadFileName = uploadFile.getOriginalFilename();
				int dotIndex = uploadFileName.lastIndexOf('.');

				fileMeta.setName(uploadFileName.substring(0, dotIndex));
				fileMeta.setExtendName(uploadFileName.substring(dotIndex + 1));
				fileMeta.setFullName(uploadFileName);
				fileMeta.setFileSize(uploadFile.getSize());

				// 元数据信息
				NameValuePair[] metaList = new NameValuePair[3];
				metaList[0] = new NameValuePair("fileName", fileMeta.getFullName());
				metaList[1] = new NameValuePair("fileExtName", fileMeta.getExtendName());
				metaList[2] = new NameValuePair("fileLength", String.valueOf(fileMeta.getFileSize()));

				String[] upResults = new FastDFSClient().upload(uploadFile.getBytes(), fileMeta.getExtendName(),
						metaList);
//				String[] upResults = {"group1","M00/00/00/wKiZC1irIkeAWka8AAc9Nx4lZAs201.zip","192.168.1.2"};
				fileMeta.setPath(upResults[0] + "/" + upResults[1]);
				fileMeta.setUrl(upResults[2]);
				fileMeta.setId(IDGenerator.getUUID());
				fileMeta.setAppId(request.getParameter("appId"));
				fileMeta.setBusinessId(request.getParameter("businessId"));
				fileMeta.setCategoryId(request.getParameter("categoryId"));
				fileMeta.setType(request.getParameter("type"));
				files.add(fileMeta);
			}

			// 保存文件信息到附件表
			String saveJsonList = JacksonUtils.toJson(files);
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo = attachmentDtoServiceCustomer.saveBatch(JacksonUtils.toJson(userInfo), saveJsonList);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo,
					DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
//				String resultInfo = dubboServiceResultInfo.getResult();
//				List<AttachmentDto> attachmentDto = JacksonUtils.fromJson(resultInfo, ArrayList.class, AttachmentDto.class);
				result.setResult(files);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
			} else {
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
			}
			pw.print(JacksonUtils.toJson(result));
			pw.flush();
		} catch (Exception e) {
			log.error("调用upload方法: 【" + e.getMessage() + "】");
			result.setSuccess(MessageInfo.SAVEERROR.isResult());
			result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
		}finally {
			try {
				pw.close();
			} catch (Exception e){}
		}
	}
	
	/**
	 * 附件全部下载
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/downloadAll", method = RequestMethod.POST)
	public ResponseEntity<byte[]> downloadAll(@RequestParam final String attListJson, HttpServletRequest request) {
		if (StringUtils.isEmpty(attListJson)) {
			return null;
		}
		try {
			List<AttachmentDto> attList = JacksonUtils.fromJson(URLDecoder.decode(attListJson, "UTF-8"), ArrayList.class, AttachmentDto.class);
			String[] fileLocation = {System.getProperty("java.io.tmpdir") +File.separator+ UUID.randomUUID().toString()};
			new FastDFSClient().downloadFiles(attList, fileLocation);
			
			// 火狐浏览器不做转码处理
			String browser = request.getHeader("User-Agent").toLowerCase();
			boolean isFirefox = browser.contains("firefox");
			boolean isChrome = browser.contains("chrome");
			String fileName = fileLocation[0].substring(fileLocation[0].lastIndexOf(File.separatorChar) + 1);
			fileName = StringUtils.replace(fileName, " ", "");
			//fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
			if (isFirefox) {
				fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
			} else if(isChrome) {
				fileName = URLEncoder.encode(fileName, "UTF-8");
				fileName = fileName.replace("%23","#");
			}else{
				fileName = URLEncoder.encode(fileName, "UTF-8");
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", fileName);
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(fileLocation[0])),headers, HttpStatus.OK);

		} catch (Exception e) {
			log.error("调用downloadAll方法:  【参数" + attListJson + "】======" + "【" + e.getMessage() + "】");
		}
		return null;
	}
	
	@RequestMapping(value="/doDownloadAll", method = RequestMethod.POST)
	public ResponseEntity<byte[]> doDownloadAll(@RequestParam String filePath) {
		
		try {
			filePath = URLDecoder.decode(filePath,"UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", filePath.substring(filePath.lastIndexOf(File.separatorChar) + 1));
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(filePath)),headers, HttpStatus.CREATED);
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用downloadAll方法:  【参数" + filePath + "】======" + "【" + e.getMessage() + "】");
		}
		return null;
	}
	
	/**
	 * 删除附件
	 * 
	 * @param filePath
	 *            附件存储路径
	 * @return
	 */
	@RequestMapping(value = "/deletefile", method = RequestMethod.POST)
	public @ResponseBody MessageResult deleteFile(@RequestBody final AttachmentDto attachment) {
		MessageResult result=new MessageResult();
		if (null != attachment) {
			try {
				if (StringUtils.isNotEmpty(attachment.getPath())) {
					String groupName = attachment.getPath().substring(0, attachment.getPath().indexOf('/'));
					String remoteFileName = attachment.getPath().substring(attachment.getPath().indexOf('/') + 1);
					new FastDFSClient().delete(groupName, remoteFileName);
				}
				
				SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
				String dubboResultInfo=attachmentDtoServiceCustomer.deleteObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\""+attachment.getId()+"\"}");
				DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
				if(dubboServiceResultInfo.isSucess()){
//					String resultInfo= dubboServiceResultInfo.getResult();
//					AttachmentDto attachmentDto=JacksonUtils.fromJson(resultInfo, AttachmentDto.class);
//					result.setResult(attachmentDto);
					result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
					result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
				}else{
					result.setSuccess(MessageInfo.DELETEERROR.isResult());
					result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
				}
			} catch (Exception e) {
				////e.printStackTrace();
				log.error("调用deletePseudoBatch方法:  【参数"+attachment+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
			}
		}
		return result;

	}
	
	/**
	 * 取得文件的存储服务器IP
	 * 
	 * @param filePath
	 *            附件存储路径
	 * @return
	 */
	@RequestMapping(value = "/getStorageIP", method = RequestMethod.POST)
	public @ResponseBody String getStorageIP(@RequestParam String filePath) {
		if (StringUtils.isNotEmpty(filePath)) {
			try {
				String groupName = filePath.substring(0, filePath.indexOf('/'));
				String remoteFileName = filePath.substring(filePath.indexOf('/') + 1);
				return new FastDFSClient().getFileAddrIP(groupName, remoteFileName);
			} catch (Exception e) {
				////e.printStackTrace();
			}
		}
		return "";

	}
	@RequestMapping(value="/filesExportHelper")
	public void filesExportHelper(HttpServletRequest request, HttpServletResponse response )throws ServletException, IOException {
		try {
			String browser = request.getHeader("User-Agent").toLowerCase();
			boolean isFirefox = browser.contains("firefox");
			boolean isChrome = browser.contains("chrome");

			String filePath = request.getParameter("filePath");
			String fileName = request.getParameter("fileName");
            fileName = StringUtils.replace(fileName, " ", "");
			if (isFirefox) {
				fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
			} else if(isChrome) {
				fileName = URLEncoder.encode(fileName, "UTF-8");
				fileName = fileName.replace("%23","#");
			}else{
				fileName = URLEncoder.encode(fileName, "UTF-8");
			}


			String groupName = filePath.substring(0, filePath.indexOf('/'));
			String remoteFileName = filePath.substring(filePath.indexOf('/') + 1);
			FastDFSClient fds = new FastDFSClient();
			String httpPath = "http://"+fds.getFileAddrIP(groupName, remoteFileName)+":"+attachmentPort+"/"+filePath;

			URL url = new URL(httpPath);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//设置超时间为3秒
			conn.setConnectTimeout(3*1000);
			//防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

			//得到输入流
			InputStream inputStream = conn.getInputStream();

			//弹出下载框，下载excle文件
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			try{
				// 以流的形式下载文件。
				byte[] buffer = readInputStream(inputStream);
				// 清空response
				response.reset();
				// 设置response的Header
				response.addHeader("Content-Disposition", "attachment;filename="
						+ fileName);
				response.addHeader("Content-Length", "" + buffer.length);
				//response.setContentType("application/vnd.ms-excel;charset=utf-8");
				toClient.write(buffer);
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				if (toClient != null) {
					toClient.flush();
					toClient.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用附件导出方法======"+"【"+e.getMessage()+"】");
		}
	}
	
	/**
	 * 返回符合条件的附件URL列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryUrlList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryUrlList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		map.put("sidx","CONVERT( name USING gbk ) COLLATE gbk_chinese_ci");
		map.put("sord","asc");
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=attachmentDtoServiceCustomer.queryList(JacksonUtils.toJson(userInfo), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<AttachmentDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,AttachmentDto.class);
				
				// 根据附件信息查询附件URL
				FastDFSClient fdfsClient = new FastDFSClient();
				String groupName;
				String remoteFileName;
				String tempUrl;
				for (AttachmentDto dto : list) {
					if (StringUtils.isNotEmpty(dto.getPath())) {
						groupName = dto.getPath().substring(0, dto.getPath().indexOf('/'));
						remoteFileName = dto.getPath().substring(dto.getPath().indexOf('/') + 1);
						tempUrl = fdfsClient.getFileAddrIP(groupName, remoteFileName);
						if (StringUtils.isNotEmpty(tempUrl)) {
							dto.setUrl("http://" + tempUrl + ":"+attachmentPort);
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
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
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
	@RequestMapping(value="/pageByCategoryIds",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult pageByCategoryIds(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
		    String dubboResultInfo=attachmentDtoServiceCustomer.getPageByCategoryIds(JacksonUtils.toJson(userInfo), paramaterJson);
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
			////e.printStackTrace();
		    log.error("调用page方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 附件预览
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/docConverter",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult docConverter(HttpServletRequest request, @RequestBody Map<String,Object> map){
		long getHtml_starttime = System.currentTimeMillis();
		MessageResult result = new MessageResult();
		String mGroup = null;
		String mFileName = null;
		String rFileName = null;
        String putFilePath = null;
		String mFilePath = request.getSession().getServletContext().getRealPath("/") + "swfFiles";       //取得服务器路径

		String paramaterJson = JacksonUtils.toJson(map);
		if (paramaterJson != null) {
			Map<String, Object> paramMap = JacksonUtils.fromJson(paramaterJson, HashMap.class);
			mFileName = (String) paramMap.get("FILENAME");
			rFileName = mFileName.substring(0,mFileName.lastIndexOf("."));
			mGroup = (String) paramMap.get("GROUP");
		}
		try {
			String tempPath = mFilePath + File.separator + mFileName;//临时文件目录
			String group = (mGroup.indexOf("/") > -1) ? mGroup.substring(0, mGroup.indexOf("/")) : "";
			String fileName = (mGroup.lastIndexOf("/") > -1) ? mGroup.substring(mGroup.indexOf("/") + 1) : "";

			int ret = new FastDFSClient().download(group, fileName, tempPath);//下载附件服务器上的文件到临时目录
			//转html
//			ExecutorService threadPool = ThreadPool.getThreadPool();
//			Produce consumer2 = new Produce(tempPath);
//			threadPool.execute(consumer2);
//			Consumer consumer=Consumer.getInstance();
//			threadPool.execute(consumer);
			//转html
			DocConverter docConverter = new DocConverter(tempPath,redisTemplate);
			putFilePath = docConverter.conver();
		} catch (Exception e) {
			log.error("word转HTML失败",e);
		}
		long getHtml_endtime = System.currentTimeMillis();
		System.out.println("docConverter耗时========= "+(getHtml_endtime-getHtml_starttime)/1000+" 秒");
		result.setSuccess("".equals(putFilePath)?MessageInfo.CREATERROR.isResult():MessageInfo.CREATESUCCESS.isResult());
		result.setMsg("".equals(putFilePath)?"":putFilePath.substring(putFilePath.indexOf("swfFiles")));
		return result;
	}
	/**
	 * 从输入流中获取字节数组
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static  byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}



	/**
	 * 返回符合条件的列表-流程专用
	 * @return
	 */
	@RequestMapping(value="/queryFlowList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryFlowList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=attachmentDtoServiceCustomer.queryFlowList(JacksonUtils.toJson(userInfo), paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map<String,List<AttachmentDto>> attMap=JacksonUtils.fromJson(resultInfo, Map.class);
				result.setResult(attMap);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}

		} catch (Exception e) {
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 附件预览
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/docConverterByBusiness",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult docConverterByBusiness(HttpServletRequest request, @RequestBody Map<String,Object> map){
		long getHtml_starttime = System.currentTimeMillis();
		MessageResult result = new MessageResult();
		String mFileName = null;
		String extendName = null;
		String httpUrl = null;
		String putFilePath = null;
		String officeType = "*.pdf;*.doc;*.ppt;*.xls;*.docx;*.pptx;*.xlsx";
		String pngType = "*.jpg,*.jpeg,*.png,*.gif";
		String mFilePath = request.getSession().getServletContext().getRealPath("/") + "swfFiles";       //取得服务器路径

		String paramaterJson = JacksonUtils.toJson(map);
		if (paramaterJson != null) {
			Map<String, Object> paramMap = JacksonUtils.fromJson(paramaterJson, HashMap.class);
			httpUrl = (String) paramMap.get("PATH");
			mFileName = (String) paramMap.get("FILENAME");
			extendName = mFileName.substring(mFileName.lastIndexOf("."));
			String uuId = IDGenerator.getUUID();
			String cFileName = uuId + extendName;
			//下载文件到服务器
			String tempPath = mFilePath + File.separator + cFileName;//下载文件地址
			HttpUtil.download(httpUrl, tempPath);
			if (officeType.indexOf(extendName.toLowerCase()) > -1) {
				DocConverter docConverter = new DocConverter(tempPath, redisTemplate);
				putFilePath = docConverter.conver();
				result.setSuccess(MessageInfo.CREATESUCCESS.isResult());
				result.setMsg(putFilePath.substring(putFilePath.indexOf("swfFiles")));
			} else if (pngType.indexOf(extendName.toLowerCase()) > -1) {
				result.setSuccess(MessageInfo.CREATESUCCESS.isResult());
				result.setMsg(tempPath.substring(tempPath.indexOf("swfFiles")));
			} else {
				result.setSuccess(MessageInfo.CREATERROR.isResult());
				result.setMsg("");
			}
		}else{
			result.setSuccess(MessageInfo.PARAMERROR.isResult());
			result.setMsg(MessageInfo.PARAMERROR.getMsg());
		}
		return result;
	}
}
