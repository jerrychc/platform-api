package com.xinleju.platform.univ.attachment.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.csource.common.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.FastDFSClient;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.platform.univ.attachment.dto.AttachmentTempDto;
import com.xinleju.platform.univ.attachment.dto.service.AttachmentTempDtoServiceCustomer;


/**
 * 附件临时表控制层
 * @author haoqp
 *
 */
@Controller
@RequestMapping("/univ/attachment/attachmentTemp")
public class AttachmentTempController {

	private static Logger log = Logger.getLogger(AttachmentTempController.class);
	
	@Autowired
	private AttachmentTempDtoServiceCustomer attachmentTempDtoServiceCustomer;
	
	
	ThreadLocal<RandomAccessFile> fileThreadLocal = new ThreadLocal<>();
	
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
			String dubboResultInfo=attachmentTempDtoServiceCustomer.getObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AttachmentTempDto attachmentTempDto=JacksonUtils.fromJson(resultInfo, AttachmentTempDto.class);
				result.setResult(attachmentTempDto);
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
		    String dubboResultInfo=attachmentTempDtoServiceCustomer.getPage(JacksonUtils.toJson(userInfo), paramaterJson);
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
		map.put("sidx","CONVERT( name USING gbk ) COLLATE gbk_chinese_ci");
		map.put("sord","asc");
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=attachmentTempDtoServiceCustomer.queryList(JacksonUtils.toJson(userInfo), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<AttachmentTempDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,AttachmentTempDto.class);
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
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody AttachmentTempDto t){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=attachmentTempDtoServiceCustomer.save(JacksonUtils.toJson(userInfo), saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AttachmentTempDto attachmentTempDto=JacksonUtils.fromJson(resultInfo, AttachmentTempDto.class);
				result.setResult(attachmentTempDto);
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
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=attachmentTempDtoServiceCustomer.deleteObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AttachmentTempDto attachmentTempDto=JacksonUtils.fromJson(resultInfo, AttachmentTempDto.class);
				result.setResult(attachmentTempDto);
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
			String dubboResultInfo=attachmentTempDtoServiceCustomer.deleteAllObjectByIds(JacksonUtils.toJson(userInfo), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AttachmentTempDto attachmentTempDto=JacksonUtils.fromJson(resultInfo, AttachmentTempDto.class);
				result.setResult(attachmentTempDto);
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
		AttachmentTempDto attachmentTempDto=null;
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=attachmentTempDtoServiceCustomer.getObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=attachmentTempDtoServiceCustomer.update(JacksonUtils.toJson(userInfo), updateJson);
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
			 String  paramJson = mapper.writeValueAsString(attachmentTempDto);
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
			String dubboResultInfo=attachmentTempDtoServiceCustomer.deletePseudoObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AttachmentTempDto attachmentTempDto=JacksonUtils.fromJson(resultInfo, AttachmentTempDto.class);
				result.setResult(attachmentTempDto);
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
			String dubboResultInfo=attachmentTempDtoServiceCustomer.deletePseudoAllObjectByIds(JacksonUtils.toJson(userInfo), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				AttachmentTempDto attachmentTempDto=JacksonUtils.fromJson(resultInfo, AttachmentTempDto.class);
				result.setResult(attachmentTempDto);
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
	@RequestMapping(value="/upload", method = RequestMethod.POST, produces={"text/html;charset=UTF-8"})
	public void upload(MultipartHttpServletRequest request, HttpServletResponse response) {
		MessageResult result=new MessageResult();
		PrintWriter pw = null;
		List<AttachmentTempDto> files = new LinkedList<AttachmentTempDto>();
		try {
			response.setContentType("text/html;charset=UTF-8");
			pw = response.getWriter();
			Iterator<String> itr = request.getFileNames();
			while (itr.hasNext()) {

				final MultipartFile uploadFile = request.getFile(itr.next());

				final AttachmentTempDto fileMeta = new AttachmentTempDto();
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

			// 保存文件信息到附件临时表
			String saveJsonList = JacksonUtils.toJson(files);
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo = attachmentTempDtoServiceCustomer.saveBatch(JacksonUtils.toJson(userInfo), saveJsonList);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo,
					DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				String resultInfo = dubboServiceResultInfo.getResult();
				List<AttachmentTempDto> attachmentTempDto = JacksonUtils.fromJson(resultInfo, ArrayList.class, AttachmentTempDto.class);
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
			////e.printStackTrace();
			log.error("调用upload方法: 【" + e.getMessage() + "】");
			result.setSuccess(MessageInfo.SAVEERROR.isResult());
			if (e instanceof MaxUploadSizeExceededException) {
				result.setCode("0001");
//				pw.write("<script language=\"javascript\">alert('上传文件大小不能超过 100 MB');</script>");
			} else {
				result.setCode("0000");
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
			}
		} finally {
			try {
				pw.close();
			} catch (Exception e){}
		}
		
	}
	
	@RequestMapping(value="/chunkUpload", method = RequestMethod.POST, produces={"text/html;charset=UTF-8"})
	public @ResponseBody void upload(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MessageResult result=new MessageResult();
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			// 获取传入文件
			multipartRequest.setCharacterEncoding("utf-8");
			MultipartFile file = multipartRequest.getFile("files[]");
			String contentRange = request.getHeader("Content-Range");
			
			int startPosition = 0;
			int endPosition = 0;
			long totalSize = file.getSize();
			if (StringUtils.isNotBlank(contentRange)) {
				contentRange = contentRange.replace("bytes", "").trim();
				String range = contentRange.substring(0, contentRange.indexOf("/"));
				String[] ranges = range.split("-");
				startPosition = Integer.parseInt(ranges[0]);
				endPosition = Integer.parseInt(ranges[1]);
				totalSize = Long.valueOf(contentRange.substring(contentRange.indexOf("/") + 1));
			}
			
			// 文件名
			String uploadFileName = file.getOriginalFilename();
			int dotIndex = uploadFileName.lastIndexOf('.');
			
			AttachmentTempDto fileMeta = new AttachmentTempDto();
			result.setResult(fileMeta);
			if (StringUtils.isBlank(contentRange) || startPosition == 0) {
				fileMeta.setId(IDGenerator.getUUID());
//				request.getSession().setAttribute(request.getParameter("businessId") + "_upload_uuid", fileMeta.getId());
			} else {
				String fileId = request.getParameter("currentFileId");
				fileMeta.setId(fileId);
			}
			
			fileMeta.setName(uploadFileName.substring(0, dotIndex));
			fileMeta.setExtendName(uploadFileName.substring(dotIndex + 1));
			fileMeta.setFullName(uploadFileName);
			fileMeta.setFileSize(totalSize);
			fileMeta.setAppId(request.getParameter("appId"));
			fileMeta.setBusinessId(request.getParameter("businessId"));
			fileMeta.setCategoryId(request.getParameter("categoryId"));
			fileMeta.setType(request.getParameter("type"));
			
//			this.SaveAs( , file, request, response);
			
			// 设置返回值
			String saveFilePath = "uploadDemo" + File.separator + uploadFileName.substring(0, dotIndex) + "_" + fileMeta.getId();

			response.setHeader("Content-File-Uuid", fileMeta.getId());
			
			System.out.println(contentRange);
			
			// 非断点续传
			if (StringUtils.isBlank(contentRange)) {
//				FileUtils.writeByteArrayToFile(new File(saveFilePath + uploadFileName.substring(dotIndex)), file.getBytes());
				
				// 元数据信息
				NameValuePair[] metaList = new NameValuePair[3];
				metaList[0] = new NameValuePair("fileName", fileMeta.getFullName());
				metaList[1] = new NameValuePair("fileExtName", fileMeta.getExtendName());
				metaList[2] = new NameValuePair("fileLength", String.valueOf(fileMeta.getFileSize()));

				String[] upResults = new FastDFSClient().upload(file.getBytes(), fileMeta.getExtendName(),
						metaList);
				fileMeta.setPath(upResults[0] + "/" + upResults[1]);
				fileMeta.setUrl(upResults[2]);
				
				String saveJsonList = JacksonUtils.toJson(Arrays.asList(fileMeta));
				SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
				String dubboResultInfo = attachmentTempDtoServiceCustomer.saveBatch(JacksonUtils.toJson(userInfo), saveJsonList);
				DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo,
						DubboServiceResultInfo.class);
				if (dubboServiceResultInfo.isSucess()) {
					String resultInfo = dubboServiceResultInfo.getResult();
					List<AttachmentTempDto> attachmentTempDto = JacksonUtils.fromJson(resultInfo, ArrayList.class, AttachmentTempDto.class);
					result.setResult(Arrays.asList(fileMeta));
					result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
					result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
				} else {
					result.setSuccess(MessageInfo.SAVEERROR.isResult());
					result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
				}
			} else {
				Path folderPath = Paths.get(saveFilePath);
				if (Files.notExists(folderPath)) {
					Files.createDirectories(folderPath);
				}
				// 根据分片范围确定文件名
				long chunkLength = Long.valueOf(request.getParameter("chunkSize"));
				String chunkFileName = Long.toString(startPosition / chunkLength);
				
				file.transferTo(new File(saveFilePath, chunkFileName));
				
				// 完成下载，合并分片
				if (endPosition + 1 >= totalSize) {
					request.getSession().removeAttribute(request.getParameter("businessId") + "_upload_uuid");
					ReadWriteLock lock = new ReentrantReadWriteLock();
					File outFile = new File(saveFilePath + uploadFileName.substring(dotIndex));
					try (FileOutputStream outStream = new FileOutputStream(outFile)) {
						lock.readLock().lock();

						FileChannel outChannel = outStream.getChannel();

						File folder = new File(saveFilePath);
						List<File> chunkFileList = Arrays.stream(folder.listFiles(f -> f.isFile()))
								.sorted((f1, f2) -> Integer.valueOf(f1.getName()) - Integer.valueOf(f2.getName()))
								.collect(Collectors.toList());

						for (File f : chunkFileList) {
							outChannel.write(ByteBuffer.wrap(Files.readAllBytes(f.toPath())));
							f.delete();
						}
						outChannel.force(true);
						folder.delete();
						
						
						// 元数据信息
						NameValuePair[] metaList = new NameValuePair[3];
						metaList[0] = new NameValuePair("fileName", fileMeta.getFullName());
						metaList[1] = new NameValuePair("fileExtName", fileMeta.getExtendName());
						metaList[2] = new NameValuePair("fileLength", String.valueOf(fileMeta.getFileSize()));

						String[] upResults = new FastDFSClient().upload(outFile.getCanonicalPath(), fileMeta.getExtendName(),
								metaList);
						
						fileMeta.setPath(upResults[0] + "/" + upResults[1]);
						fileMeta.setUrl(upResults[2]);
						
						String saveJsonList = JacksonUtils.toJson(Arrays.asList(fileMeta));
						SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
						String dubboResultInfo = attachmentTempDtoServiceCustomer.saveBatch(JacksonUtils.toJson(userInfo), saveJsonList);
						DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo,
								DubboServiceResultInfo.class);
						if (dubboServiceResultInfo.isSucess()) {
							String resultInfo = dubboServiceResultInfo.getResult();
							List<AttachmentTempDto> attachmentTempDto = JacksonUtils.fromJson(resultInfo, ArrayList.class, AttachmentTempDto.class);
							result.setResult(Arrays.asList(fileMeta));
							result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
							result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
						} else {
							result.setSuccess(MessageInfo.SAVEERROR.isResult());
							result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
						}
						
					} finally {
						try {
							lock.readLock().unlock();
						} catch (Exception e) {
						}
						try {
							outFile.delete();
						} catch (Exception e) {
						}

					}
				}
			
			}
			
			response.setStatus(200);
			writer.write(JSON.toJSONString(result));
		} catch (Exception e) {
			log.error(e);
			result.setSuccess(false);
			writer.write(JSON.toJSONString(result));
		}
	}
	
	
	@GetMapping("/getUploadedSize")
	public @ResponseBody MessageResult getUploadedSize(String fileName, String fileId)
			throws Exception {
		MessageResult result = new MessageResult();
		Map<String, Object> map = new HashMap<>();
		try {
			if (StringUtils.isBlank(fileName) || StringUtils.isBlank(fileId)) {
				result.setSuccess(true);
				map.put("fileSize", 0);
				result.setResult(map);
			} else {
				String fileName1 = fileName.indexOf('.') != -1? fileName.substring(0, fileName.lastIndexOf('.')):fileName;
				String filePath = "uploadDemo" + File.separator + fileName1 + "_" + fileId;
				
			}
			// 文件路径
			
			// 根据文件名定位文件路径，返回已上传文件大小
			System.out.println("------------fileName - - - - |" + fileName);
			
			
		} catch (Exception e) {
			log.error(e);
			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
//	@RequestMapping(value="/upload", method = RequestMethod.POST, produces={"text/html;charset=UTF-8"})
//	public @ResponseBody MessageResult upload(MultipartHttpServletRequest request, HttpServletResponse response) {
//		MessageResult result=new MessageResult();
//		List<AttachmentTempDto> files = new LinkedList<AttachmentTempDto>();
//		try {
//			Iterator<String> itr = request.getFileNames();
//			while (itr.hasNext()) {
//
//				final MultipartFile uploadFile = request.getFile(itr.next());
//
//				final AttachmentTempDto fileMeta = new AttachmentTempDto();
//				// 文件名
//				String uploadFileName = uploadFile.getOriginalFilename();
//				int dotIndex = uploadFileName.lastIndexOf('.');
//
//				fileMeta.setName(uploadFileName.substring(0, dotIndex));
//				fileMeta.setExtendName(uploadFileName.substring(dotIndex + 1));
//				fileMeta.setFullName(uploadFileName);
//				fileMeta.setFileSize(uploadFile.getSize());
//
//				// 元数据信息
//				NameValuePair[] metaList = new NameValuePair[3];
//				metaList[0] = new NameValuePair("fileName", fileMeta.getFullName());
//				metaList[1] = new NameValuePair("fileExtName", fileMeta.getExtendName());
//				metaList[2] = new NameValuePair("fileLength", String.valueOf(fileMeta.getFileSize()));
//
//				String[] upResults = new FastDFSClient().upload(uploadFile.getBytes(), fileMeta.getExtendName(),
//						metaList);
////				String[] upResults = {"group1","M00/00/00/wKiZC1irIkeAWka8AAc9Nx4lZAs201.zip","192.168.1.2"};
//				fileMeta.setPath(upResults[0] + "/" + upResults[1]);
//				fileMeta.setUrl(upResults[2]);
//				fileMeta.setId(IDGenerator.getUUID());
//				fileMeta.setAppId(request.getParameter("appId"));
//				fileMeta.setBusinessId(request.getParameter("businessId"));
//				fileMeta.setCategoryId(request.getParameter("categoryId"));
//				fileMeta.setType(request.getParameter("type"));
//				files.add(fileMeta);
//			}
//
//			// 保存文件信息到附件临时表
//			String saveJsonList = JacksonUtils.toJson(files);
//			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
//			String dubboResultInfo = attachmentTempDtoServiceCustomer.saveBatch(JacksonUtils.toJson(userInfo), saveJsonList);
//			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo,
//					DubboServiceResultInfo.class);
//			if (dubboServiceResultInfo.isSucess()) {
//				String resultInfo = dubboServiceResultInfo.getResult();
//				List<AttachmentTempDto> attachmentTempDto = JacksonUtils.fromJson(resultInfo, ArrayList.class, AttachmentTempDto.class);
//				result.setResult(files);
//				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
//				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
//			} else {
//				result.setSuccess(MessageInfo.SAVEERROR.isResult());
//				result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
//			}
//			
//		} catch (Exception e) {
//			////e.printStackTrace();
//			log.error("调用upload方法: 【" + e.getMessage() + "】");
//			result.setSuccess(MessageInfo.SAVEERROR.isResult());
//			if (e instanceof MaxUploadSizeExceededException) {
//				result.setCode("0001");
//				try {
//					PrintWriter pw = response.getWriter();
//					pw.write("<script language=\"javascript\">alert('上传文件大小不能超过 100 MB');</script>");
//					pw.flush();
//					pw.close();
//				} catch (IOException e1) {
//				}
//			} else {
//				result.setCode("0000");
//				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
//			}
//		}
//		
//		return result;
//	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public void handleException(MaxUploadSizeExceededException ex, HttpServletResponse response) {
		PrintWriter writer = null;
		try {
			response.setContentType("text/html;charset=UTF-8");
			writer = response.getWriter();
			writer.write("<script language=\"javascript\">alert('上传文件大小不能超过 " + ex.getMaxUploadSize()/(1024*1024)+ " MB');</script>");
			log.debug("-------------> 上传文件大小超过最大限制：" + ex.getMaxUploadSize()/(1024*1024)+ " MB");
			writer.flush();
		} catch (IOException e) {
			log.error(null, e);
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			} 
		}
	}
	
}
