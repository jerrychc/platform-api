package com.xinleju.cloud.oa.sys.controller;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xinleju.cloud.oa.util.CompressImgUtil;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.log4j.Logger;
import org.csource.common.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.FastDFSClient;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.cloud.oa.office.dto.OfficeOutDto;
import com.xinleju.cloud.oa.sys.dto.SysCommonToolsDto;
import com.xinleju.cloud.oa.sys.dto.service.SysCommonToolsDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.platform.univ.attachment.dto.AttachmentTempDto;

/**
 * 常用工具表控制层
 * 
 * @author admin
 * 
 */
@Controller
@RequestMapping("/sys/sysCommonTools")
public class SysCommonToolsController {

	private static Logger log = Logger
			.getLogger(SysCommonToolsController.class);

	@Autowired
	private SysCommonToolsDtoServiceCustomer sysCommonToolsDtoServiceCustomer;

	/**
	 * 根据Id获取业务对象
	 * 
	 * @param id
	 *            业务对象主键
	 * 
	 * @return 业务对象
	 */
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public @ResponseBody
	MessageResult get(@PathVariable("id") String id) {
		MessageResult result = new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo = sysCommonToolsDtoServiceCustomer
					.getObjectById(userJson, "{\"id\":\"" + id + "\"}");
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
					.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				String resultInfo = dubboServiceResultInfo.getResult();
				SysCommonToolsDto sysCommonToolsDto = JacksonUtils.fromJson(
						resultInfo, SysCommonToolsDto.class);
				result.setResult(sysCommonToolsDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			} else {
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg() + "【"
						+ dubboServiceResultInfo.getExceptionMsg() + "】");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("调用get方法:  【参数" + id + "】======" + "【" + e.getMessage()
					+ "】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage()
					+ "】");
		}
		return result;
	}

	/**
	 *回显图片
	 * 
	 * @param paramater
	 * @return
	 */
/*	@RequestMapping(value = "/showPic/{id}", method = RequestMethod.GET)
	public @ResponseBody
	MessageResult showPic(@PathVariable("id") String id) {
		
	}*/
	/**
	 * 返回分页对象
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/page", method = { RequestMethod.POST }, consumes = "application/json")
	public @ResponseBody
	MessageResult page(@RequestBody Map<String, Object> map) {
		MessageResult result = new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo = sysCommonToolsDtoServiceCustomer.getPage(
					userJson, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
					.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				String resultInfo = dubboServiceResultInfo.getResult();
				PageBeanInfo pageInfo = JacksonUtils.fromJson(resultInfo,
						PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			} else {
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg() + "【"
						+ dubboServiceResultInfo.getExceptionMsg() + "】");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("调用page方法:  【参数" + paramaterJson + "】======" + "【"
					+ e.getMessage() + "】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage()
					+ "】");
		}
		return result;
	}

	/**
	 * 返回符合条件的列表
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/queryList", method = { RequestMethod.POST }, consumes = "application/json")
	public @ResponseBody
	MessageResult queryList(@RequestBody Map<String, Object> map) {
		MessageResult result = new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo = sysCommonToolsDtoServiceCustomer
					.queryList(userJson, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
					.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				String resultInfo = dubboServiceResultInfo.getResult();
				List<SysCommonToolsDto> list = JacksonUtils.fromJson(
						resultInfo, ArrayList.class, SysCommonToolsDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			} else {
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg() + "【"
						+ dubboServiceResultInfo.getExceptionMsg() + "】");
			}

		} catch (Exception e) {
			// e.printStackTrace();
			log.error("调用queryList方法:  【参数" + paramaterJson + "】======" + "【"
					+ e.getMessage() + "】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage()
					+ "】");
		}
		return result;
	}
	/**
	 * 返回符合条件的列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getCommonToolsList", method = { RequestMethod.POST }, consumes = "application/json")
	public @ResponseBody
	MessageResult getCommonToolsList() {
		MessageResult result = new MessageResult();
		Map<String,Object>map=new HashMap<String, Object>();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo = sysCommonToolsDtoServiceCustomer
					.getCommonToolsList(userJson,paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
					.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				String resultInfo = dubboServiceResultInfo.getResult();
				List<SysCommonToolsDto> list = JacksonUtils.fromJson(
						resultInfo, ArrayList.class, SysCommonToolsDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			} else {
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg() + "【"
						+ dubboServiceResultInfo.getExceptionMsg() + "】");
			}
			
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("调用queryList方法:  【参数" + paramaterJson + "】======" + "【"
					+ e.getMessage() + "】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage()
					+ "】");
		}
		return result;
	}

	/**
	 * 保存实体对象
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public void save(MultipartHttpServletRequest request, HttpServletResponse response) {
		PrintWriter pw = null;
		Boolean isReturn = false;
		try {
			response.setContentType("text/html;charset=UTF-8");
			pw = response.getWriter();
			MultipartFile uploadfile =  request.getFile("pic");
			MessageResult result = new MessageResult();
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(null!=uploadfile){
				long length = uploadfile.getSize();
				if(length>1*1024*1024){
					result.setSuccess(false);
					result.setMsg("图片尺寸不能大于1M");
					pw.print(JacksonUtils.toJson(result));
					pw.flush();
					isReturn = true;
				}
			}
			if(!isReturn) {
				String id = request.getParameter("id");
				String name = request.getParameter("name");
				String code = request.getParameter("code");
				String isDelPic = request.getParameter("isDelPic");
				byte[] headpic = {};
				if (null != uploadfile) {
					InputStream is = uploadfile.getInputStream();
					headpic = new byte[is.available()];
					is.read(headpic);
					is.close();
				}
				SysCommonToolsDto sysCommonToolsDto = new SysCommonToolsDto();
				sysCommonToolsDto.setCode(code);
				sysCommonToolsDto.setId(id);
				sysCommonToolsDto.setName(name);
				sysCommonToolsDto.setPic(headpic);
				sysCommonToolsDto.setDelflag(false);
				String saveJson = JacksonUtils.toJson(sysCommonToolsDto);
				String dubboResultInfo = sysCommonToolsDtoServiceCustomer.save(
						userJson, saveJson);
				DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
						.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
				if (dubboServiceResultInfo.isSucess()) {
					String resultInfo = dubboServiceResultInfo.getResult();
					SysCommonToolsDto officeInfoOutDto = JacksonUtils.fromJson(
							resultInfo, SysCommonToolsDto.class);
					result.setResult(officeInfoOutDto);
					result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
					result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
				} else {
					result.setSuccess(MessageInfo.SAVEERROR.isResult());
					result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【"
							+ dubboServiceResultInfo.getExceptionMsg() + "】");
				}
				pw.print(JacksonUtils.toJson(result));
				pw.flush();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			try {
				pw.close();
			} catch (Exception e){}
		}
	}


	/**
	 * 删除实体对象
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	MessageResult delete(@PathVariable("id") String id) {
		MessageResult result = new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo = sysCommonToolsDtoServiceCustomer
					.deleteObjectById(userJson, "{\"id\":\"" + id + "\"}");
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
					.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				String resultInfo = dubboServiceResultInfo.getResult();
				SysCommonToolsDto sysCommonToolsDto = JacksonUtils.fromJson(
						resultInfo, SysCommonToolsDto.class);
				result.setResult(sysCommonToolsDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			} else {
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【"
						+ dubboServiceResultInfo.getExceptionMsg() + "】");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("调用delete方法:  【参数" + id + "】======" + "【"
					+ e.getMessage() + "】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【"
					+ e.getMessage() + "】");
		}

		return result;
	}

	/**
	 * 删除实体对象
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch/{ids}", method = RequestMethod.DELETE)
	public @ResponseBody
	MessageResult deleteBatch(@PathVariable("ids") String ids) {
		MessageResult result = new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo = sysCommonToolsDtoServiceCustomer
					.deleteAllObjectByIds(userJson, "{\"id\":\"" + ids + "\"}");
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
					.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				String resultInfo = dubboServiceResultInfo.getResult();
				SysCommonToolsDto sysCommonToolsDto = JacksonUtils.fromJson(
						resultInfo, SysCommonToolsDto.class);
				result.setResult(sysCommonToolsDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			} else {
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【"
						+ dubboServiceResultInfo.getExceptionMsg() + "】");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("调用delete方法:  【参数" + ids + "】======" + "【"
					+ e.getMessage() + "】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【"
					+ e.getMessage() + "】");
		}

		return result;
	}

	/**
	 * 修改修改实体对象
	 * 
	 * @return
	 */
	@RequestMapping(value = "/update")
	public void update(MultipartHttpServletRequest request, HttpServletResponse response) {
		PrintWriter pw = null;
		Boolean isReturn = false;
		try {
			response.setContentType("text/html;charset=UTF-8");
			pw = response.getWriter();
			MultipartFile uploadfile = request.getFile("pic");
			MessageResult result = new MessageResult();
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if (null != uploadfile) {
				long length = uploadfile.getSize();
				if (length > 1 * 1024 * 1024) {
					result.setSuccess(false);
					result.setMsg("图片尺寸不能大于1M");
					pw.print(JacksonUtils.toJson(result));
					pw.flush();
					isReturn = true;
				}
			}
			if(!isReturn){
				SysCommonToolsDto sysCommonToolsDto = new SysCommonToolsDto();
				String id = request.getParameter("id");
				String dubboResultInfo = sysCommonToolsDtoServiceCustomer
						.getObjectById(userJson, "{\"id\":\"" + id + "\"}");
				DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
						.fromJson(dubboResultInfo, DubboServiceResultInfo.class);

				if (dubboServiceResultInfo.isSucess()) {
					String resultInfo = dubboServiceResultInfo.getResult();
					sysCommonToolsDto = JacksonUtils.fromJson(resultInfo,
							SysCommonToolsDto.class);
					String name = request.getParameter("name");
					String code = request.getParameter("code");
					String url = request.getParameter("url");
					String isDelPic = request.getParameter("isDelPic");
					sysCommonToolsDto.setCode(code);
					sysCommonToolsDto.setId(id);
					sysCommonToolsDto.setName(name);
					sysCommonToolsDto.setUrl(url);
					byte[] headpic = {};
					if (null != uploadfile) {
						if (uploadfile.getSize() > 0) {
							InputStream is = uploadfile.getInputStream();
							headpic = new byte[is.available()];
							is.read(headpic);
							is.close();
							sysCommonToolsDto.setPic(headpic);
						}
					}else if("0".equals(isDelPic)){
						sysCommonToolsDto.setPic(null);
					}
					sysCommonToolsDto.setDelflag(false);
					String updateJson = JacksonUtils.toJson(sysCommonToolsDto);
					String updateDubboResultInfo = sysCommonToolsDtoServiceCustomer
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
			e.printStackTrace();
		}finally {
			try {
				pw.close();
			} catch (Exception e){}
		}
	}

	/**
	 * 伪删除实体对象
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deletePseudo/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	MessageResult deletePseudo(@PathVariable("id") String id) {
		MessageResult result = new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo = sysCommonToolsDtoServiceCustomer
					.deletePseudoObjectById(userJson, "{\"id\":\"" + id + "\"}");
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
					.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				String resultInfo = dubboServiceResultInfo.getResult();
				SysCommonToolsDto sysCommonToolsDto = JacksonUtils.fromJson(
						resultInfo, SysCommonToolsDto.class);
				result.setResult(sysCommonToolsDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			} else {
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【"
						+ dubboServiceResultInfo.getExceptionMsg() + "】");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("调用deletePseudo方法:  【参数" + id + "】======" + "【"
					+ e.getMessage() + "】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【"
					+ e.getMessage() + "】");
		}

		return result;
	}

	/**
	 * 伪删除实体对象
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deletePseudoBatch/{ids}", method = RequestMethod.DELETE)
	public @ResponseBody
	MessageResult deletePseudoBatch(@PathVariable("ids") String ids) {
		MessageResult result = new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo = sysCommonToolsDtoServiceCustomer
					.deletePseudoAllObjectByIds(userJson, "{\"id\":\"" + ids
							+ "\"}");
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
					.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				String resultInfo = dubboServiceResultInfo.getResult();
				SysCommonToolsDto sysCommonToolsDto = JacksonUtils.fromJson(
						resultInfo, SysCommonToolsDto.class);
				result.setResult(sysCommonToolsDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			} else {
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【"
						+ dubboServiceResultInfo.getExceptionMsg() + "】");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("调用deletePseudoBatch方法:  【参数" + ids + "】======" + "【"
					+ e.getMessage() + "】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【"
					+ e.getMessage() + "】");
		}

		return result;
	}

	/**
	 * 排序(上移/下移/置顶/置底)
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/updateSort/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody
	MessageResult updateSort(@PathVariable("id") String id,
			@RequestBody Map<String, Object> map) {
		MessageResult result = new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils
					.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo = sysCommonToolsDtoServiceCustomer
					.updateSort(userJson, "{\"id\":\"" + id + "\"}", map);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
					.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				String resultInfo = dubboServiceResultInfo.getResult();
				int i = JacksonUtils.fromJson(
						dubboServiceResultInfo.getResult(), Integer.class);
				result.setResult(i);
				result.setSuccess(MessageInfo.UPDATESORTSUCCESS.isResult());
				result.setMsg(MessageInfo.UPDATESORTSUCCESS.getMsg());
			} else {
				result.setSuccess(MessageInfo.UPDATESORTERROR.isResult());
				result.setMsg(MessageInfo.UPDATESORTERROR.getMsg() + "【"
						+ dubboServiceResultInfo.getExceptionMsg() + "】");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("调用修改状态方法:  【参数" + id + "】======" + "【" + e.getMessage()
					+ "】");
			result.setSuccess(MessageInfo.UPDATESORTERROR.isResult());
			result.setMsg(MessageInfo.UPDATESORTERROR.getMsg() + "【"
					+ e.getMessage() + "】");
		}

		return result;
	}

    /**
     * 门户首页展示
     * @param request
     * @return
     */
    @RequestMapping(value = "/getCommonToolsPortal", method = RequestMethod.GET,produces = "text/html; charset=utf-8")
    @ResponseBody
    public String getCommonToolsPortal(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String html = "";
        StringBuffer buffer = new StringBuffer();

        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            Map<String, Object> map = new HashMap<String, Object>();
            String paramaterJson = JacksonUtils.toJson(map);

            buffer.append("<ul class=\"toolicons clearfix\" id=\"oaCommonTools\">\n" );
            String dubboResultInfo = sysCommonToolsDtoServiceCustomer.getCommonToolsList(userJson, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<SysCommonToolsDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, SysCommonToolsDto.class);
                for (SysCommonToolsDto commonToolsDto:list) {
					byte[] icon1 = commonToolsDto.getPic();
					byte[] icon2 = icon1;//icon1!=null? CompressImgUtil.compressImg2(icon1,50,50):null;
					if (icon1 != null && icon1.length > 32 * 1024) {
						icon2 = icon1!=null? CompressImgUtil.compressImg2(icon1,50,50):null;
					}
					String base64Img = icon2!=null?"data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(icon2):"../../common/img/default2.png";
                    buffer.append("<li>\n<a href=\"javascript:void(0);\" onclick=\"downloadPackage('"+commonToolsDto.getId()+"')\">\n" +
                            "<img src=\""+ base64Img+"\" style=\"width:50px;height:50px;\" title=\""
                            +commonToolsDto.getName()+"\">\n<span>"+commonToolsDto.getName()+"</span></a>\n</li>\n");
                }
            }
            buffer.append("</ul>");
            buffer.append("<script type=\"text/javascript\">\n" +
                    "\tfunction downloadPackage(id){\n" +
                    "\t\t$.xljUtils.xljDownLoad('1',id,'1');\n" +
                    "\t}\n</script>");
            html = buffer.toString();
        } catch (Exception e) {
            log.error("调用getCommonToolsPortal方法出错："+e.getMessage());
            return html;
        }


        return html;
    }
}
