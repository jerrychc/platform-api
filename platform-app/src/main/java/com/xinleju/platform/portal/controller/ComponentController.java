package com.xinleju.platform.portal.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xinleju.cloud.oa.sys.dto.SysCommonToolsDto;
import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.uitls.LoginUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.portal.dto.ComponentDto;
import com.xinleju.platform.portal.dto.service.ComponentDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;


/**
 * 组件表控制层
 *
 * @author admin
 */
@Controller
@RequestMapping("/portal/component")
public class ComponentController {

    private static Logger log = Logger.getLogger(ComponentController.class);

    @Autowired
    private ComponentDtoServiceCustomer componentDtoServiceCustomer;

    /**
     * 根据Id获取业务对象
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public @ResponseBody MessageResult get(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = componentDtoServiceCustomer.getObjectById(userInfo, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ComponentDto componentDto = JacksonUtils.fromJson(resultInfo, ComponentDto.class);
                result.setResult(componentDto);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            ////e.printStackTrace();
            log.error("调用get方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
 			 result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
        }
        return result;
    }


    /**
     * 返回分页对象
     *
     * @param map 分页参数
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.POST}, consumes = "application/json")
    public @ResponseBody MessageResult page(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = componentDtoServiceCustomer.getPage(userInfo, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PageBeanInfo pageInfo = JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
                result.setResult(pageInfo);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            ////e.printStackTrace();
            log.error("调用page方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
 			 result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
        }
        return result;
    }

    /**
     * 返回符合条件的列表
     *
     * @param map 查询条件参数
     * @return
     */
    @RequestMapping(value = "/queryList", method = {RequestMethod.POST}, consumes = "application/json")
    public @ResponseBody MessageResult queryList(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = componentDtoServiceCustomer.queryList(userInfo, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ComponentDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, ComponentDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }

        } catch (Exception e) {
            ////e.printStackTrace();
            log.error("调用queryList方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
 			 result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
        }
        return result;
    }

    @RequestMapping(value = "/queryAllList", method = {RequestMethod.POST}, consumes = "application/json")
    public @ResponseBody MessageResult queryAllList() {
        MessageResult result = new MessageResult();
        //String paramaterJson = JacksonUtils.toJson(map);

        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = componentDtoServiceCustomer.queryAllList(userInfo);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<Map<String,Object>> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, Map.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }

        } catch (Exception e) {
            ////e.printStackTrace();
            log.error("调用queryList方法:  【参数】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
        }
        return result;
    }


    /**
     * 保存实体对象
     *
     * @param t
     * @return
     */
    
	@RequestMapping(value = "/save")
	public @ResponseBody
	MessageResult save(@RequestParam(value = "titleIcon") MultipartFile uploadfile,
			HttpServletRequest request, HttpServletResponse response) {
		MessageResult result = new MessageResult();
		SecurityUserBeanInfo userBeanInfo = LoginUtils
				.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		long length = uploadfile.getSize();
		if(length>1*1024*1024){
			result.setSuccess(false);
			result.setMsg("图片尺寸不能大于1M");
			return result;
		}
        ComponentDto componentDto = new ComponentDto();
		try {
			InputStream is = uploadfile.getInputStream();
			byte[] headpic = new byte[is.available()];  
	        is.read(headpic);  
	        is.close();  
			String id = request.getParameter("id");
			String title = request.getParameter("title");
			String displayDelete = request.getParameter("displayDelete");
			String moreUrl = request.getParameter("moreUrl");
			String description = request.getParameter("description");
			String displayRefresh = request.getParameter("displayRefresh");
			String contentUrl = request.getParameter("contentUrl");
			String categoryId = request.getParameter("categoryId");
			String displayMove = request.getParameter("displayMove");
			String code = request.getParameter("code");
			String delflag = request.getParameter("delflag");
			componentDto.setCode(code);
			componentDto.setId(id);
			componentDto.setContentUrl(contentUrl);
			componentDto.setCategoryId(categoryId);
			componentDto.setTitle(title);
			componentDto.setTitleIcon(headpic);
			componentDto.setMoreUrl(moreUrl);
			componentDto.setDescription(description);
			componentDto.setDelflag(false);
		    if(displayDelete==null){
		    	componentDto.setDisplayDelete(false);
			}
		     if (displayRefresh == null) {
		    	 componentDto.setDisplayRefresh(false);
	        }
	        if (displayMove == null) {
	        	componentDto.setDisplayMove(false);
	        }
	        String saveJson = JacksonUtils.toJson(componentDto);
            String dubboResultInfo = componentDtoServiceCustomer.save(userJson, saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ComponentDto componentDtoResult = JacksonUtils.fromJson(resultInfo, ComponentDto.class);
                result.setResult(componentDtoResult);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
            	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
				result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            try {
                ////e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(componentDto);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 删除实体对象
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody MessageResult delete(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = componentDtoServiceCustomer.deleteObjectById(userInfo, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ComponentDto componentDto = JacksonUtils.fromJson(resultInfo, ComponentDto.class);
                result.setResult(componentDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            ////e.printStackTrace();
            log.error("调用delete方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }

        return result;
    }


    /**
     * 删除实体对象
     *
     * @param ids 实体对象id数组
     * @return
     */
    @RequestMapping(value = "/deleteBatch/{ids}", method = RequestMethod.DELETE)
    public @ResponseBody MessageResult deleteBatch(@PathVariable("ids") String ids) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = componentDtoServiceCustomer.deleteAllObjectByIds(userInfo, "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ComponentDto componentDto = JacksonUtils.fromJson(resultInfo, ComponentDto.class);
                result.setResult(componentDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            ////e.printStackTrace();
            log.error("调用delete方法:  【参数" + ids + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
 			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
        }

        return result;
    }

    /**
     * 修改修改实体对象
     *
     * @param id  实体对象id
     * @param map 实体对象属性map集合
     * @return
     */
 /*   @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public @ResponseBody MessageResult update(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        ComponentDto componentDto = null;
        if (map.get("displayClose") == null) {
            map.put("displayClose",false);
        }
        if (map.get("displayDelete") == null) {
            map.put("displayDelete",false);
        }
        if (map.get("displayRefresh") == null) {
            map.put("displayRefresh",false);
        }
        if (map.get("displayMove") == null) {
            map.put("displayMove",false);
        }
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = componentDtoServiceCustomer.getObjectById(userInfo, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                Map<String, Object> oldMap = JacksonUtils.fromJson(resultInfo, HashMap.class);
                oldMap.putAll(map);
                String updateJson = JacksonUtils.toJson(oldMap);*/
    
	@RequestMapping(value = "/update")
	public @ResponseBody
	MessageResult update(@RequestParam(value = "titleIcon") MultipartFile uploadfile,
			HttpServletRequest request, HttpServletResponse response) {
		MessageResult result = new MessageResult();
		SecurityUserBeanInfo userBeanInfo = LoginUtils
				.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		long length = uploadfile.getSize();
		if(length>1*1024*1024){
			result.setSuccess(false);
			result.setMsg("图片尺寸不能大于1M");
			return result;
		}
			
		ComponentDto componentDto = new ComponentDto();
		try {
				String id = request.getParameter("id");
				String dubboResultInfo = componentDtoServiceCustomer
						.getObjectById(userJson, "{\"id\":\"" + id + "\"}");
				DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils
						.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
				
				if (dubboServiceResultInfo.isSucess()) {
					String resultInfo = dubboServiceResultInfo.getResult();
					componentDto	= JacksonUtils.fromJson(resultInfo,ComponentDto.class);
					String title = request.getParameter("title");
					String displayDelete = request.getParameter("displayDelete");
					String moreUrl = request.getParameter("moreUrl");
					String description = request.getParameter("description");
					String displayRefresh = request.getParameter("displayRefresh");
					String contentUrl = request.getParameter("contentUrl");
					String categoryId = request.getParameter("categoryId");
					String displayMove = request.getParameter("displayMove");
					String code = request.getParameter("code");
					String delflag = request.getParameter("delflag");
					componentDto.setCode(code);
					componentDto.setId(id);
					componentDto.setContentUrl(contentUrl);
					componentDto.setCategoryId(categoryId);
					componentDto.setTitle(title);
					componentDto.setMoreUrl(moreUrl);
					componentDto.setDescription(description);
			
				    if(displayDelete==null){
				    	componentDto.setDisplayDelete(false);
					}
				     if (displayRefresh == null) {
				    	 componentDto.setDisplayRefresh(false);
			        }
			        if (displayMove == null) {
			        	componentDto.setDisplayMove(false);
			        }
					if(length>0){
						InputStream is = uploadfile.getInputStream();
						byte[] headpic = new byte[is.available()];  
						is.read(headpic);  
						is.close();  
						componentDto.setTitleIcon(headpic);
					}
					componentDto.setDelflag(false);
					String updateJson = JacksonUtils.toJson(componentDto);
    
    
                String updateDubboResultInfo = componentDtoServiceCustomer.update(userJson, updateJson);
                DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
                if (updateDubboServiceResultInfo.isSucess()) {
                    Integer i = JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
                    result.setResult(i);
                    result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                    result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
                } else {
                	 result.setSuccess(updateDubboServiceResultInfo.isSucess());
					 result.setMsg(updateDubboServiceResultInfo.getMsg());
					 result.setCode(updateDubboServiceResultInfo.getCode());
                }
            } else {
            	 result.setSuccess(dubboServiceResultInfo.isSucess());
				 result.setCode(ErrorInfoCode.NULL_ERROR.getValue());
				 result.setMsg("不存在更新的对象");
            }
        } catch (Exception e) {
            try {
                ////e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(componentDto);
                log.error("调用update方法: ======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
	   			 result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
	   			 result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 伪删除实体对象
     *
     * @param id 实体对象id
     * @return
     */
    @RequestMapping(value = "/deletePseudo/{id}", method = RequestMethod.DELETE)
    public @ResponseBody MessageResult deletePseudo(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = componentDtoServiceCustomer.deletePseudoObjectById(userInfo, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ComponentDto componentDto = JacksonUtils.fromJson(resultInfo, ComponentDto.class);
                result.setResult(componentDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            ////e.printStackTrace();
            log.error("调用deletePseudo方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
  			 result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
        }

        return result;
    }


    /**
     * 伪删除实体对象
     *
     * @param ids 实体对象id数组字符串
     * @return
     */
    @RequestMapping(value = "/deletePseudoBatch/{ids}", method = RequestMethod.DELETE)
    public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids") String ids) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = componentDtoServiceCustomer.deletePseudoAllObjectByIds(userInfo, "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ComponentDto componentDto = JacksonUtils.fromJson(resultInfo, ComponentDto.class);
                result.setResult(componentDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            ////e.printStackTrace();
            log.error("调用deletePseudoBatch方法:  【参数" + ids + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
 			 result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
        }

        return result;
    }

}
