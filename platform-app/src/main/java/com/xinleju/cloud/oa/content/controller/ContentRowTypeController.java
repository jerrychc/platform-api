package com.xinleju.cloud.oa.content.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.xinleju.cloud.oa.content.dto.ContentTypeDto;
import com.xinleju.cloud.oa.content.dto.service.ContentTypeDtoServiceCustomer;
import com.xinleju.platform.base.utils.*;

import com.xinleju.platform.out.app.org.service.OrgnazationOutServiceCustomer;
import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import com.xinleju.platform.sys.org.dto.service.UserDtoServiceCustomer;
import com.xinleju.platform.sys.res.dto.UserAuthDataOrgList;
import com.xinleju.platform.uitls.OpeLogInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.cloud.oa.content.dto.ContentRowTypeDto;
import com.xinleju.cloud.oa.content.dto.service.ContentRowTypeDtoServiceCustomer;
import com.xinleju.cloud.oa.util.CustomFileUtil;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.platform.univ.attachment.dto.service.AttachmentDtoServiceCustomer;


/**
 * 知识存储表控制层
 *
 * @author admin
 */
@Controller
@RequestMapping("/oa/content/contentRowType")
public class ContentRowTypeController {

    private static Logger log = Logger.getLogger(ContentRowTypeController.class);

    @Autowired
    private ContentRowTypeDtoServiceCustomer contentRowTypeDtoServiceCustomer;

    @Autowired
    private AttachmentDtoServiceCustomer attachmentDtoServiceCustomer;

    @Autowired
    private OrgnazationOutServiceCustomer orgnazationOutServiceCustomer;

    @Autowired
    private ContentTypeDtoServiceCustomer contentTypeDtoServiceCustomer;

    @Autowired
    private UserDtoServiceCustomer userDtoServiceCustomer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    /**
     * 根据Id获取业务对象
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult get(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentRowTypeDto contentRowTypeDto = JacksonUtils.fromJson(resultInfo, ContentRowTypeDto.class);
                 Map attributeValueMap = JacksonUtils.fromJson (contentRowTypeDto.getAttributeValue (),HashMap.class);
                contentRowTypeDto.setAttributeValueMap (attributeValueMap);
                result.setResult(contentRowTypeDto);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            log.error("调用get方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }

    @RequestMapping(value = "/validateDataAuth/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult validateDataAuth(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String currentUserId = userBeanInfo.getSecurityUserDto().getId();
            String userType = userBeanInfo.getSecurityUserDto().getType();

            String contentRowTypeInfo = contentRowTypeDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo contentRowTypeResultInfo = JacksonUtils.fromJson(contentRowTypeInfo, DubboServiceResultInfo.class);
            String contentTypeId = null;
            if (contentRowTypeResultInfo.isSucess()) {
                String resultInfo = contentRowTypeResultInfo.getResult();
                ContentRowTypeDto contentRowTypeDto = JacksonUtils.fromJson(resultInfo, ContentRowTypeDto.class);
                contentTypeId = contentRowTypeDto.getContentTypeId();
            }

            String userJson = JacksonUtils.toJson(userBeanInfo);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id",id);
            map.put("contentTypeId",contentTypeId);
            map.put("currentUserId",currentUserId);
            map.put("isAdminUser","2".equals(userType));
            String paramaterJson = JacksonUtils.toJson(getUserDataAuthSearchList(map));
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.getPage(userJson, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PageBeanInfo pageInfo = JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
                List list = pageInfo.getList();
                if(list==null||list.size()==0){
                    result.setResult(false);
                }else{
                    result.setResult(true);
                }

                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }


    /**
     * 返回分页对象
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult page(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String currentUserId = userBeanInfo.getSecurityUserDto().getId();
            String userType = userBeanInfo.getSecurityUserDto().getType();

            String userJson = JacksonUtils.toJson(userBeanInfo);
            map.put("currentUserId",currentUserId);
            map.put("isAdminUser","2".equals(userType));
            String paramaterJson = JacksonUtils.toJson(getUserDataAuthSearchList(map));
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.getPage(userJson, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PageBeanInfo pageInfo = JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
                result.setResult(pageInfo);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }

    /**
     * 返回符合条件的列表
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryList", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult queryList(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        try {
            String paramaterJson = JacksonUtils.toJson(getUserDataAuthSearchList(map));
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.queryList(getUserJson(), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentRowTypeDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentRowTypeDto.class);
             //   Map<Integer,List<ContentRowTypeDto>> maplist = (Map<Integer,List<ContentRowTypeDto>>)list.stream ().collect (Collectors.groupingBy (ContentRowTypeDto::getBigVersion));
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }

        } catch (Exception e) {
           // e.printStackTrace();
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }


    /**
     * 保存实体对象
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    MessageResult save(@RequestBody   Map<String, Object> map) {
        MessageResult result = new MessageResult();
        try {

            String saveJson = JacksonUtils.toJson(map);
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.save(getUserJson(), saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentRowTypeDto contentRowTypeDto = JacksonUtils.fromJson(resultInfo, ContentRowTypeDto.class);
                result.setResult(contentRowTypeDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(map);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg());
                result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            } catch (JsonProcessingException e1) {
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
    public
    @ResponseBody
    MessageResult delete(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.deleteObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentRowTypeDto contentRowTypeDto = JacksonUtils.fromJson(resultInfo, ContentRowTypeDto.class);
                result.setResult(contentRowTypeDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用delete方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + e.getMessage() + "】");
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
    public
    @ResponseBody
    MessageResult deleteBatch(@PathVariable("ids") String ids) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.deleteAllObjectByIds(getUserJson(), "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentRowTypeDto contentRowTypeDto = JacksonUtils.fromJson(resultInfo, ContentRowTypeDto.class);
                result.setResult(contentRowTypeDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用delete方法:  【参数" + ids + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    /**
     * 修改修改实体对象
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = "application/json")
    @OpeLogInfo(sysCode="OA",node = "新闻/文档更新")
    public
    @ResponseBody
    MessageResult update(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        ContentRowTypeDto contentRowTypeDto = null;
        try {
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if(dubboServiceResultInfo.isSucess()){
            Map<String, Object> oldMap = JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), HashMap.class);
           /*     //数据权限过滤
                Map<String,List<OrgnazationDto>> authMap = (Map<String,List<OrgnazationDto>>)getUserDataAuthEditList (map).get("dataAuth");
                List<OrgnazationDto> companyList = JacksonUtils.fromJson (JacksonUtils.toJson (authMap.get("companyList")),List.class,OrgnazationDto.class);
                List<OrgnazationDto> deptList = JacksonUtils.fromJson (JacksonUtils.toJson (authMap.get("deptList")),List.class,OrgnazationDto.class);
                if(companyList.isEmpty ()&&deptList.isEmpty ()){
                    if(!Objects.equals (oldMap.get("createPersonId"),LoginUtils.getSecurityUserBeanInfo ().getSecurityUserDto ().getId ())){
                        result.setSuccess(false);
                        result.setCode(ErrorInfoCode.DATAAUTH_ERROR.getValue());
                        result.setMsg("数据权限不足！");
                        return result;
                    };
                }else{
                    String deptId =  String.valueOf (map.get("belongDeptId"));
                    List<OrgnazationDto> allowList = deptList.stream ().filter (e-> Objects.equals (e.getId (),deptId)).collect (Collectors.toList ());
                    if(allowList==null||allowList.isEmpty ()){
                        result.setSuccess(false);
                        result.setCode(ErrorInfoCode.DATAAUTH_ERROR.getValue());
                        result.setMsg("数据权限不足！");
                        return result;
                    }
                }
*/
                oldMap.remove("attributeValue");
                oldMap.putAll(map);
                String updateJson = JacksonUtils.toJson(oldMap);
                //JSONObject json = (JSONObject) JSONObject.toJSON (oldMap);
                //String updateJson = json.toJSONString ();
                String updateDubboResultInfo = contentRowTypeDtoServiceCustomer.update(getUserJson(), updateJson);
                DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
                if (updateDubboServiceResultInfo.isSucess()) {
                    Integer i = JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
                    result.setResult(i);
                    result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                    result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
                } else {
                    result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                    result.setMsg(updateDubboServiceResultInfo.getMsg());
                    result.setCode(updateDubboServiceResultInfo.getCode());
                }
            } else {
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setCode(ErrorInfoCode.NULL_ERROR.getValue());
                result.setMsg("不存在更新的对象");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(contentRowTypeDto);
                log.error("调用update方法:  【参数" + id + "," + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg(MessageInfo.UPDATEERROR.getMsg());
                result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 获取新闻、文档总数
     *
     * @param contentType
     * @return
     */
    @RequestMapping(value = "/queryContentType/{contentType}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult queryContentType(@PathVariable("contentType") String contentType) {
        //实例返回数据集对象
        MessageResult result = new MessageResult();
        //调用提供者接口获取数据集
        String dubboResultCount = contentRowTypeDtoServiceCustomer.queryTypeCount(getUserJson(), "{\"contentType\":\"" + contentType + "\"}");
        try {
            //DubboServiceResultInfo统一格式接收数据对象
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultCount, DubboServiceResultInfo.class);
            //查询状态返回True，Message装在前端数据
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                result.setResult(resultInfo);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                //获取失败返回前端失败信息
                result.setSuccess(MessageInfo.GETERROR.isResult());
                //获取服务提供者异常信息
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryContentType方法:  【参数" + contentType + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }


    /**
     * 根据Id获取业务对象
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/getContentRowType/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult getContentRowType(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentRowTypeDto contentRowTypeDto = JacksonUtils.fromJson(resultInfo, ContentRowTypeDto.class);
                result.setResult(contentRowTypeDto);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用get方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }




    /**
     * 修改实体对象状态
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/updateState/{ids}/{param}/{state}", method = RequestMethod.PUT, consumes = "application/json")
    @OpeLogInfo(sysCode="OA",node = "新闻/文档状态修改")
    public
    @ResponseBody
    MessageResult updateState(@PathVariable("ids") String ids, @PathVariable("state") String state,@PathVariable("param") String param) {
        MessageResult result = new MessageResult();
        try {
                Map<String, Object> paramMap = new HashMap<String,Object>();
                paramMap.put("state",state);
                paramMap.put("ids", ids);
                paramMap.put("param",param);
                String updateJson = JacksonUtils.toJson(paramMap);
                String updateDubboResultInfo = contentRowTypeDtoServiceCustomer.updateStatus(getUserJson(), updateJson);
                DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
                if (updateDubboServiceResultInfo.isSucess()) {
                    Integer i = JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
                    result.setResult(i);
                    result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                    result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
                } else {
                    result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                    result.setMsg(updateDubboServiceResultInfo.getMsg());
                    result.setCode(updateDubboServiceResultInfo.getCode());
                }
        } catch (Exception e) {
                log.error("调用update方法:  【参数" + ids + "," + ids + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg(MessageInfo.UPDATEERROR.getMsg());
                result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }

    /**
     * 修改实体对象状态
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/updateProcessState", method = RequestMethod.POST, consumes = "application/json")
    @OpeLogInfo(sysCode="OA",node = "流程回调状态修改")
    public
    @ResponseBody
    MessageResult updateProcessState(@RequestBody Map<String,Object> map) {
        MessageResult result = new MessageResult();
        String updateJson = JacksonUtils.toJson(map);
        SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
        if(map.get("token")!=null){//加入白名单处理获取新闻/知识表单流程业务回调
            String token = String.valueOf(map.get("token"));
            String[] args = token.split("@");
            if(args.length==2){
                SecurityUserBeanInfo securityUserBeanInfo1 = LoginUtils.getSecurityUserBeanInfo();
                SecurityUserDto securityUserDto = null;
                if (securityUserBeanInfo1 == null) {
                    Map<String,Object> paramMap = new HashMap<String,Object>();
                    paramMap.put("loginName",args[0]);
                    //securityUserBeanInfo.setTendId(securityUserBeanInfo1.getTendId());
                    securityUserBeanInfo.setTendCode(args[1]);
                    String userDubboInfoStr = userDtoServiceCustomer.queryList(JacksonUtils.toJson(securityUserBeanInfo),JacksonUtils.toJson(paramMap));
                    DubboServiceResultInfo userDubboInfo = JacksonUtils.fromJson(userDubboInfoStr,DubboServiceResultInfo.class);
                    if(userDubboInfo.isSucess()){
                        String userInfo = userDubboInfo.getResult();
                        List<SecurityUserDto> userDtos = JacksonUtils.fromJson(userInfo,List.class,SecurityUserDto.class);
                        if(userDtos!=null&&userDtos.size()>0){
                            securityUserDto = userDtos.get(0);
                        }
                    }

                }else{
                    securityUserBeanInfo.setTendId(securityUserBeanInfo1.getTendId());
                    securityUserBeanInfo.setTendCode(args[1]);
                    securityUserDto = securityUserBeanInfo1.getSecurityUserDto();
                }
                securityUserBeanInfo.setSecurityUserDto(securityUserDto);
            }
        }else{
            securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        }
        try {
            String updateDubboResultInfo = contentRowTypeDtoServiceCustomer.updateProcessState(JacksonUtils.toJson(securityUserBeanInfo), updateJson);
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
        } catch (Exception e) {
            log.error("调用update方法:  【参数" + updateJson + "," + updateJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.UPDATEERROR.isResult());
            result.setMsg(MessageInfo.UPDATEERROR.getMsg());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }
    /**
     * 根据Id获取业务对象
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/getAvailabContentRowType/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult getAvailabContentRowType(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();

        try {
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.getAvailabContentRowType(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                Map map = JacksonUtils.fromJson(resultInfo, Map.class);
                result.setResult(map);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用get方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }


        return result;
    }


    /**
     * 批量删除实体对象
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteAll/{id}/{type}", method = RequestMethod.DELETE)
    @OpeLogInfo(sysCode="OA",node = "新闻/文档删除")
    public
    @ResponseBody
    MessageResult deleteAll(@PathVariable("id") String ids,@PathVariable("type") String type) {
        MessageResult result = new MessageResult();
        try {
            HashMap<String,Object> requestJson = new HashMap();
            requestJson.put("ids",ids);
            requestJson.put("type",type);
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.deletePseudoAllObjectByIds(getUserJson(),JacksonUtils.toJson(requestJson));
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentRowTypeDto contentRowTypeDto = JacksonUtils.fromJson(resultInfo, ContentRowTypeDto.class);
                result.setResult(contentRowTypeDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用delete方法:  【参数" + ids + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + e.getMessage() + "】");
        }

        return result;
    }

    /**
     * 返回符合条件的列表
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryAttachmentList", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult queryAttachmentList(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboPageResultInfo = contentRowTypeDtoServiceCustomer.getPage(getUserJson(), paramaterJson);
            DubboServiceResultInfo dubboPageServiceResultInfo = JacksonUtils.fromJson(dubboPageResultInfo, DubboServiceResultInfo.class);
            List<AttachmentDto> listTem = new ArrayList<AttachmentDto>();
            if (dubboPageServiceResultInfo.isSucess()) {
                String resultPageInfo = dubboPageServiceResultInfo.getResult();
                PageBeanInfo pageInfo = JacksonUtils.fromJson(resultPageInfo, PageBeanInfo.class);
                List pageList = pageInfo.getList();
                for (Object object : pageList) {
                	AttachmentDto attachmentDtoTem = new AttachmentDto();
                	Map map1 = JacksonUtils.fromJson(JacksonUtils.toJson(object), Map.class);
                	//获取该新闻下面的附件信息
                	map.put("businessId", map1.get("id").toString().split(","));
            		String paramaterJson1 = JacksonUtils.toJson(map);
            		String dubboResultInfo = attachmentDtoServiceCustomer.queryListByObject(getUserJson(), paramaterJson1);
                    DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
                    if (dubboServiceResultInfo.isSucess()) {
                        String resultInfo = dubboServiceResultInfo.getResult();
                        List<AttachmentDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, AttachmentDto.class);
                        if(list.size() > 0){
                        	//此处可能有问题，如果一个文档下面有多个附件，页面该早呢么显示，有点疑问
                        	for (AttachmentDto attachmentDto : list) {
                        		BeanUtils.copyProperties(attachmentDto, attachmentDtoTem);
							}
                        }
                        result.setResult(list);
                        result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                        result.setMsg(MessageInfo.GETSUCCESS.getMsg());
                    } 
                    attachmentDtoTem.setBusinessId(map1.get("id").toString());
                	attachmentDtoTem.setName(map1.get("title").toString());
                    listTem.add(attachmentDtoTem);
				}
                pageInfo.setList(listTem);
                result.setResult(pageInfo);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            }else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboPageServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryList方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    /**
     * 批量打包下载文件生成zip文件下载
     *
     *//*
    @RequestMapping(value = "/loadFiles", method = RequestMethod.GET)
	public void downloadFiles(HttpServletRequest request, HttpServletResponse response,String param)
			throws ServletException, IOException {
		//可配置文件中配置磁盘路径,路径传参数拼接，得到文件夹下的子文件夹
		String realPath = "";

		String inputFileName = "C:/test/";
		if (param != null && !param.trim().equals("")) {
			inputFileName = param.toUpperCase();
		}
		String zipFileName = realPath + "temp.zip"; // 压缩后的zip文件 可随意定一个磁盘路径或者相对路径
		try {
			CustomFileUtil.zip(inputFileName, zipFileName);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
		File temp = new File(zipFileName);
		CustomFileUtil.downloadFile(temp, response, true);
	}*/
    @RequestMapping(value = "/downloadAll", method = RequestMethod.POST)
    public ResponseEntity<byte[]> downloadAll(@RequestParam final String attListJson) {
//		MessageResult result=new MessageResult();
        if (StringUtils.isEmpty(attListJson)) {
//			result.setMsg(MessageInfo.QUERYERROR.getMsg());
//			result.setSuccess(MessageInfo.QUERYERROR.isResult());
            return null;
        }
        try {
            List<AttachmentDto> attList = JacksonUtils.fromJson(URLDecoder.decode(attListJson, "UTF-8"), ArrayList.class, AttachmentDto.class);
            String[] fileLocation = {System.getProperty("java.io.tmpdir") + UUID.randomUUID().toString()};
            new FastDFSClient().downloadFiles(attList, fileLocation);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileLocation[0].substring(fileLocation[0].lastIndexOf(File.separatorChar) + 1));
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(fileLocation[0])), headers, HttpStatus.CREATED);

//			result.setResult(fileLocation[0]);
//			result.setSuccess(MessageInfo.QUERYSUCCESS.isResult());
//			result.setMsg(MessageInfo.QUERYSUCCESS.getMsg());
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用downloadAll方法:  【参数" + attListJson + "】======" + "【" + e.getMessage() + "】");
//			result.setSuccess(MessageInfo.QUERYERROR.isResult());
//			result.setMsg(MessageInfo.QUERYERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return null;
    }


    /**
     * 批量打包下载文件生成zip文件下载
     *
     */

    @RequestMapping(value = "/downloadZip", method = {RequestMethod.POST})
    public ResponseEntity<byte[]> downloadFiles(@RequestBody String tcLwIds) throws ServletException, IOException {
        MessageResult result = new MessageResult();
        List<String> files = new ArrayList<String>();
        if ("" == tcLwIds || null == tcLwIds) {
            System.out.println("下载的文件不存在");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "下载的文件不存在");
            return null;
        }
        tcLwIds = URLDecoder.decode(tcLwIds, "UTF-8");
        String[] tcLwIdArray = tcLwIds.split(",");
        for (String tcLwId : tcLwIdArray) {
            files.add(tcLwId);
        }
        String fileName = UUID.randomUUID().toString() + ".zip";
        // 在服务器端创建打包下载的临时文件
        String outFilePath = "D:\\tms_log";
        CustomFileUtil.createFile(outFilePath, fileName);
        File file = new File(outFilePath + fileName);
        // 文件输出流
        FileOutputStream outStream = new FileOutputStream(file);
        // 压缩流
        ZipOutputStream toClient = new ZipOutputStream(outStream);
        CustomFileUtil.zipFile(files, toClient);
        toClient.close();
        outStream.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

    /***
     * 生成系统环境变量
     */
    @RequestMapping(value = "/getSysProperties", method = {RequestMethod.GET})
    public
    @ResponseBody
    MessageResult getSysProperties(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        String sysStr = System.getProperty("java.io.tmpdir");
        result.setResult(sysStr);
        return result;
    }

    /**
     * 根据Id获取业务对象
     *
     * @return 业务对象
     */
    @RequestMapping(value = "/getContentRowData/{code}", method = RequestMethod.GET,produces = "text/html; charset=utf-8")
    @ResponseBody
    public String getContentRowData(@PathVariable("code") String contentTypeCode,
                                    @RequestParam(value = "companyId",required=false) String companyId,
                                    @RequestParam(value="newsLimit",required = false) Integer newsLimit) {
        MessageResult result = new MessageResult();
        String contentRowHtml = "";
        newsLimit = newsLimit==null?6:newsLimit;
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userId = userBeanInfo.getSecurityUserDto().getId();

            ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
            String cacheCompanyId = valueOperations.get("CHANGE_COMPANY_"+contentTypeCode+"_"+userBeanInfo.getSecurityUserDto().getId());
            if(companyId == null){
                companyId = cacheCompanyId;
            }else{
                valueOperations.set("CHANGE_COMPANY_"+contentTypeCode+"_"+userBeanInfo.getSecurityUserDto().getId(),companyId);
            }

            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("contentTypeCode",contentTypeCode);
            paramMap.put("companyId",companyId);
            paramMap.put("limit",newsLimit);
            paramMap.put("currentUserId",userBeanInfo.getSecurityUserDto().getId());

            //获取redis缓存中的已读新闻
            Set<String> keySet = redisTemplate.keys("NEWS_MARK_READ_"+userId+"_*");
            List<String> haveReadIds = new ArrayList<String>();
            for (String key:keySet) {
                haveReadIds.add(key.replace("NEWS_MARK_READ_"+userId+"_",""));
            }
            paramMap.put("haveReadIds",haveReadIds);
            String paramJson = JacksonUtils.toJson(getUserDataAuthSearchList(paramMap));

            String dubboResultInfo = contentRowTypeDtoServiceCustomer.getContentRowDataForPortal(getUserJson(), paramJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                contentRowHtml = resultInfo;

            } else {
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getContentData方法:  【参数" + contentTypeCode + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return contentRowHtml;
    }

    @RequestMapping(value = "/getNewsReadForPersonal",method = RequestMethod.GET)
    @ResponseBody
    public MessageResult getNewsReadForPersonal(@RequestParam(name = "contentRowTypeId",required = false) String contentRowTypeId) {
        MessageResult result = new MessageResult();
        try{
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            SecurityUserDto securityUserDto = userBeanInfo.getSecurityUserDto();
            String userId = securityUserDto.getId();
            String markReadKey = "NEWS_MARK_READ_"+userId;


            //SetOperations<String,Object> setOperations = redisTemplate.opsForSet();
            ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
            if (StringUtils.isNotBlank(contentRowTypeId)) {
                Boolean hasNewsReadKey = redisTemplate.hasKey(markReadKey+"_"+contentRowTypeId);
                if(!hasNewsReadKey){
                    String dubboResultInfo = contentRowTypeDtoServiceCustomer.getObjectById(JacksonUtils.toJson(userBeanInfo),"{\"id\":\""+contentRowTypeId+"\"}");
                    DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo,DubboServiceResultInfo.class);
                    if(dubboServiceResultInfo.isSucess()){
                        String contentRowTypeJson = dubboServiceResultInfo.getResult();
                        ContentRowTypeDto contentRowTypeDto = JacksonUtils.fromJson(contentRowTypeJson,ContentRowTypeDto.class);
                        String contentTypeId = contentRowTypeDto.getContentTypeId();
                        String contentTypeDubboResultInfo = contentTypeDtoServiceCustomer.getObjectById(JacksonUtils.toJson(userBeanInfo),"{\"id\":\""+contentTypeId+"\"}");
                        DubboServiceResultInfo contentTypeDubboServiceResultInfo = JacksonUtils.fromJson(contentTypeDubboResultInfo,DubboServiceResultInfo.class);
                        if(contentTypeDubboServiceResultInfo.isSucess()){
                            String contentTypeJson = contentTypeDubboServiceResultInfo.getResult();
                            ContentTypeDto contentTypeDto = JacksonUtils.fromJson(contentTypeJson,ContentTypeDto.class);
                            Integer newsIconDays = contentTypeDto.getNewIconDays();
                            newsIconDays = newsIconDays == null?0:newsIconDays;
                            Date publishDate = contentRowTypeDto.getPublishDate();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(publishDate);

                            Date endDate = new Date(publishDate.getTime()+(newsIconDays+1)*24*60*60*1000L);
                            Date currentDate = new Date();
                            long expireSeconds = endDate.getTime()-currentDate.getTime();
                            valueOperations.set(markReadKey+"_"+contentRowTypeId,contentRowTypeId,expireSeconds, TimeUnit.MILLISECONDS);
                        }
                    }
                }

                result.setSuccess(true);
                result.setResult(contentRowTypeId);
            }else{
                Set<String> keySet = redisTemplate.keys("NEWS_MARK_READ_"+userId+"_*");
                result.setSuccess(true);
                result.setResult(JacksonUtils.toJson(keySet));
            }

        }catch (Exception e){
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
            log.error("调用getNewsReadForPersonal方法:"+"【"+e.getMessage()+"】");
        }

        return result;
    }

    /**
     * 根据Id获取业务对象
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public MessageResult getUserInfo() {
        MessageResult result = new MessageResult();
        try {
            result.setResult(LoginUtils.getSecurityUserBeanInfo());
            result.setSuccess(MessageInfo.GETSUCCESS.isResult());
            result.setMsg(MessageInfo.GETSUCCESS.getMsg());
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getUserInfo方法:" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }
    /**
     * 根据Id获取业务对象菜单
     */
    @RequestMapping(value = "/getUserBeanRelationInfo", method = RequestMethod.GET)
    @ResponseBody
    public MessageResult getUserBeanRelationInfo() {
        MessageResult result = new MessageResult();
        try {
            result.setResult(LoginUtils.getSecurityUserBeanRelationInfo());
            result.setSuccess(MessageInfo.GETSUCCESS.isResult());
            result.setMsg(MessageInfo.GETSUCCESS.getMsg());
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getUserInfo方法:" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }
    /**
     * 根据计算点击量
     */
    @RequestMapping(value = "/hit/{id}",  method = {RequestMethod.POST}, consumes = "application/json")
    @ResponseBody
    public MessageResult hits(@PathVariable String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.updateContentRowTypeHit(getUserJson(),id);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if(dubboServiceResultInfo.isSucess()){
                result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
            }else{
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg("计算点击量失败！");
                result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            }

        } catch (Exception e) {
            log.error("调用hits方法:" + "【" + e.getMessage() + "】");
            result.setSuccess(false);
            result.setMsg(MessageInfo.UPDATEERROR.getMsg());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }

    /**
     * 返回符合条件的列表
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/querySameList", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult querySameList(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.querySameList(getUserJson(), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentRowTypeDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentRowTypeDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryList方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }

    /**
     * 返回历史版本列表
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/versionList", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult queryVersionList(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.queryVersionList(getUserJson(), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List list = JacksonUtils.fromJson(resultInfo, ArrayList.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryList方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }

    /**
     * 获取流程变量回调
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryVariableForFlow", method = {RequestMethod.POST}, consumes = "application/json")
    @ResponseBody
    public MessageResult queryVariableForFlow(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
            if(map.get("token")!=null){//加入白名单处理获取表单流程业务变量查询
                String token = String.valueOf(map.get("token"));
                String[] args = token.split("@");
                if(args.length==2){
                    securityUserBeanInfo.setTendCode(args[1]);
                }
            }else{
                securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            }
        try {
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.queryVariableForFlow(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                //List list = JacksonUtils.fromJson(resultInfo, ArrayList.class);
                result.setResult(resultInfo);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryList方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }
    /**
     * 暂存数据
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/temporary/save", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    MessageResult saveTemporary(@RequestBody Map<String,Object> map) {
        MessageResult result = new MessageResult();
        try {
            String saveJson = JacksonUtils.toJson(map);
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.saveTemporary(getUserJson(), saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentRowTypeDto contentRowTypeDto = JacksonUtils.fromJson(resultInfo, ContentRowTypeDto.class);
                result.setResult(contentRowTypeDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(map);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg());
                result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            } catch (JsonProcessingException e1) {
            }
        }
        return result;
    }
    /**
     * 新版本存附件数据
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/attachment/save", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    MessageResult saveAttachment(@RequestBody Map<String,Object> map) {
        MessageResult result = new MessageResult();
        try {
            String saveJson = JacksonUtils.toJson(map);
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.saveAttachment(getUserJson(), saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentRowTypeDto contentRowTypeDto = JacksonUtils.fromJson(resultInfo, ContentRowTypeDto.class);
                result.setResult(contentRowTypeDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(map);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg());
                result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            } catch (JsonProcessingException e1) {
            }
        }
        return result;
    }



    @RequestMapping(value = "/queryListForMobile", method = {RequestMethod.POST}, consumes = "application/json")
    @ResponseBody
    public MessageResult queryListForMobile(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = null;
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            SecurityUserDto securityUserDto = userBeanInfo.getSecurityUserDto();

            //当前用户id放入参数中
            map.put("currentUserId",securityUserDto.getId());

            String userJson = JacksonUtils.toJson(userBeanInfo);

            //获取当前用户数据权限
            Map<String,String> dataAuthMap = new HashMap<> ();
            dataAuthMap.put ("appCode","OA");
            dataAuthMap.put("userIds",userBeanInfo.getSecurityUserDto ().getId ());
            dataAuthMap.put ("itemCode", "newsSearch");

            //查询知识大类类型
            Map<String,Object> contentTypeParam = new HashMap<String,Object>();
            String contentTypeCodes = (String) map.get("contentTypeCodes");
            if(StringUtils.isNotBlank(contentTypeCodes)){
                contentTypeParam.put("code",contentTypeCodes.split(",")[0]);
                String contentTypeDubboInfo = contentTypeDtoServiceCustomer.queryList(userJson,JacksonUtils.toJson(contentTypeParam));
                DubboServiceResultInfo contentTypeResultDubboInfo = JacksonUtils.fromJson(contentTypeDubboInfo,DubboServiceResultInfo.class);
                if(contentTypeResultDubboInfo.isSucess()){
                    String contentTypeResult = contentTypeResultDubboInfo.getResult();
                    ContentTypeDto contentTypeDto = null;
                    List<ContentTypeDto> contentTypeDtos = JacksonUtils.fromJson(contentTypeResult,List.class,ContentTypeDto.class);
                    if(contentTypeDtos!=null&&contentTypeDtos.size()>0){
                        contentTypeDto = contentTypeDtos.get(0);
                    }

                    if(contentTypeDto!=null){
                        String contentType = contentTypeDto.getContentType();
                        if("NEWS".equals(contentType)){
                            dataAuthMap.put ("itemCode", "newsSearch");
                        }else{
                            dataAuthMap.put ("itemCode", "docSearch");
                        }

                    }
                }
            }


            String authResultJson = orgnazationOutServiceCustomer.getUserDataAuthCoAndDeptListByItemCode(userJson,JacksonUtils.toJson (dataAuthMap));
            DubboServiceResultInfo authResultInfo = JacksonUtils.fromJson (authResultJson,DubboServiceResultInfo.class);
            if(authResultInfo.isSucess ()){
                Map<String,UserAuthDataOrgList> authDataMap = JacksonUtils.fromJson (authResultInfo.getResult (),Map.class);
                map.put ("dataAuth",authDataMap.get (userBeanInfo.getSecurityUserDto ().getId ()));
            }

            paramaterJson = JacksonUtils.toJson(map);

            String dubboResultInfo = contentRowTypeDtoServiceCustomer.queryListForMobile(userJson, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PageBeanInfo pageInfo = JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
                result.setResult(pageInfo);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryListForMobile方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }
    /**
     * 获取最新版本数据
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/get/current/version/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult getCurrentVersion(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.getCurrentVersionById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult ();
                Map<String,Object> resultMap = JacksonUtils.fromJson (resultInfo,Map.class);
                result.setResult(resultMap);
                result.setSuccess(dubboServiceResultInfo.isSucess ());
                result.setMsg(dubboServiceResultInfo.getMsg ());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            log.error("调用get方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }

    /**
     * 根据Id获取业务对象
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/queryObjectInfoById/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult queryObjectInfoById(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.queryObjectInfoById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                Map<String,Object> contentRowTypeDto = JacksonUtils.fromJson(resultInfo, Map.class);
                result.setResult(contentRowTypeDto);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            log.error("调用queryObjectInfoById方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }

    /**
     * 检查数据修改和删除权限
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/check/dataAuthEdit/{id}", method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResult dataAuthEdit(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        try {
            List<String> ids = Arrays.asList (id.split (","));
            List<ContentRowTypeDto> list = new ArrayList<> ();
            if(ids!=null&&!ids.isEmpty ()){
             /*   for(String e:ids){
                    String dubboResultInfo = contentRowTypeDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + e + "\"}");
                    DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
                    list.add(JacksonUtils.fromJson (dubboServiceResultInfo.getResult (),ContentRowTypeDto.class));
                }*/
                ids.stream ().forEach (e->{
                    String dubboResultInfo = contentRowTypeDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + e + "\"}");
                    DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
                    list.add(JacksonUtils.fromJson (dubboServiceResultInfo.getResult (),ContentRowTypeDto.class));
                });

            }

                //数据权限过滤
                Map<String, List<OrgnazationDto>> authMap = (Map<String, List<OrgnazationDto>>) getUserDataAuthEditList (map).get ("dataAuth");
                List<OrgnazationDto> deptList = JacksonUtils.fromJson (JacksonUtils.toJson (authMap.get ("deptList")), List.class, OrgnazationDto.class);
                if (deptList.isEmpty ()) {
                    List<ContentRowTypeDto> listUn = list.parallelStream ().filter (e->!Objects.equals (e.getCreatePersonId (), LoginUtils.getSecurityUserBeanInfo ().getSecurityUserDto ().getId ())).collect(Collectors.toList());
                   /* for(ContentRowTypeDto dd:list){
                        if(!Objects.equals (dd.getCreatePersonId (), LoginUtils.getSecurityUserBeanInfo ().getSecurityUserDto ().getId ())){
                            result.setSuccess (false);
                            result.setCode (ErrorInfoCode.DATAAUTH_ERROR.getValue ());
                            result.setMsg ("数据权限不足！");
                            return result;
                        }
                    }*/
                   if(listUn!=null&&!listUn.isEmpty ()){
                       result.setSuccess (false);
                       result.setCode (ErrorInfoCode.DATAAUTH_ERROR.getValue ());
                       result.setMsg ("数据权限不足！");
                       return result;
                   }
                } else {
                    for(ContentRowTypeDto dd:list){
                        String deptId = dd.getBelongDeptId ();
                        List<OrgnazationDto> allowList = deptList.parallelStream ().filter (e -> Objects.equals (e.getId (), deptId)).collect (Collectors.toList ());
                        if (allowList == null || allowList.isEmpty ()) {
                            result.setSuccess (false);
                            result.setCode (ErrorInfoCode.DATAAUTH_ERROR.getValue ());
                            result.setMsg ("数据权限不足！");
                            return result;
                        }
                    }
                }
                result.setSuccess (true);

        } catch (Exception e) {
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }

    private String getUserJson() {
        SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();

        String userJson = JacksonUtils.toJson(userBeanInfo);
        return userJson;
    }

    /**
     * 获取用户查询数据权限
     * @param map
     * @return
     */
    private Map<String,Object> getUserDataAuthSearchList(Map map){
        SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        String userJson = JacksonUtils.toJson(userBeanInfo);
        String contentTypeId = (String) (map.get ("contentTypeId"));
        ContentTypeDto contentTypeDto = null;
        if (contentTypeId==null){
            String contentTypeCode = (String) map.get("contentTypeCode");
            if (contentTypeCode != null) {
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("code",contentTypeCode);
                String dubboResultInfo = contentTypeDtoServiceCustomer.queryList(userJson,JacksonUtils.toJson(paramMap));
                DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
                if (dubboServiceResultInfo.isSucess()) {
                    String resultInfo = dubboServiceResultInfo.getResult();
                    List<ContentTypeDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentTypeDto.class);
                    if (list != null && list.size() > 0) {
                        contentTypeDto = list.get(0);
                        //contentTypeId = contentTypeDto.getId();
                    }
                }
            }
        }
        Map<String,String> dataAuthMap = new HashMap<> ();
        dataAuthMap.put ("appCode","OA");
        dataAuthMap.put("userIds",userBeanInfo.getSecurityUserDto ().getId ());
        if (contentTypeDto == null) {
            String resultInfo = contentTypeDtoServiceCustomer.getObjectById (getUserJson (),"{\"id\":\"" + contentTypeId + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson (resultInfo,DubboServiceResultInfo.class);
            contentTypeDto = JacksonUtils.fromJson (dubboServiceResultInfo.getResult (),ContentTypeDto.class);
        }

        if(contentTypeDto!=null&&Objects.equals (contentTypeDto.getDataAuthSearch (),Objects.equals (contentTypeDto.getContentType(),"NEWS")?"newsSearch":"docSearch")){
            dataAuthMap.put ("itemCode", Objects.equals (contentTypeDto.getContentType(),"NEWS")?"newsSearch":"docSearch");
            String authResultJson = orgnazationOutServiceCustomer.getUserDataAuthCoAndDeptListByItemCode(userJson,JacksonUtils.toJson (dataAuthMap));
            DubboServiceResultInfo authResultInfo = JacksonUtils.fromJson (authResultJson,DubboServiceResultInfo.class);
            if(authResultInfo.isSucess ()){
                Map<String,UserAuthDataOrgList> authDataMap = JacksonUtils.fromJson (authResultInfo.getResult (),Map.class);
                map.put ("dataAuth",authDataMap.get (userBeanInfo.getSecurityUserDto ().getId ()));
            }
        }else{
            Map<String, List<OrgnazationDto>> data = new HashMap<> ();
            data.put ("deptList",new ArrayList<OrgnazationDto> ());
            map.put ("dataAuth",data);
        }
        return map;
    }

    /**
     * 获取用户修改和删除数据权限
     * @return
     */
    private Map<String,Object> getUserDataAuthEditList(Map map){
        SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        String userJson = JacksonUtils.toJson(userBeanInfo);
        String contentTypeId = String.valueOf (map.get ("contentTypeId"));
        Map<String,String> dataAuthMap = new HashMap<> ();
        dataAuthMap.put ("appCode","OA");
        dataAuthMap.put("userIds",userBeanInfo.getSecurityUserDto ().getId ());
        String resultInfo = contentTypeDtoServiceCustomer.getObjectById (getUserJson (),"{\"id\":\"" + contentTypeId + "\"}");
        DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson (resultInfo,DubboServiceResultInfo.class);
        ContentTypeDto contentTypeDto = JacksonUtils.fromJson (dubboServiceResultInfo.getResult (),ContentTypeDto.class);
        if(contentTypeDto!=null&&Objects.equals (contentTypeDto.getDataAuthEdit (),Objects.equals (map.get ("contentType"),"NEWS")?"newsEdit":"docEdit")){
            dataAuthMap.put ("itemCode", Objects.equals (map.get ("contentType"),"NEWS")?"newsEdit":"docEdit");
            String authResultJson = orgnazationOutServiceCustomer.getUserDataAuthCoAndDeptListByItemCode(userJson,JacksonUtils.toJson (dataAuthMap));
            DubboServiceResultInfo authResultInfo = JacksonUtils.fromJson (authResultJson,DubboServiceResultInfo.class);
            if(authResultInfo.isSucess ()){
                Map<String,UserAuthDataOrgList> authDataMap = JacksonUtils.fromJson (authResultInfo.getResult (),Map.class);
                map.put ("dataAuth",authDataMap.get (userBeanInfo.getSecurityUserDto ().getId ()));
            }
        }else{
            Map<String, List<OrgnazationDto>> data = new HashMap<> ();
            data.put ("deptList",new ArrayList<OrgnazationDto> ());
            map.put ("dataAuth",data);
        }
        return map;
    }

    /**
     * 根据编码类型获取redis中的编码
     */
    @RequestMapping(value = "/getCodeByCodeType", method = RequestMethod.GET)
    @ResponseBody
    public MessageResult getCodeByCodeType(@RequestParam(value = "code") String code,@RequestParam(value = "codeType") String codeType) {
        MessageResult result = new MessageResult();
        try {
            ContentRowTypeDto contentRowTypeDto = new ContentRowTypeDto();
            contentRowTypeDto.setCode(code);
            contentRowTypeDto.setCodeType(codeType);
            String saveJson = JacksonUtils.toJson(contentRowTypeDto);
            String dubboResultInfo = contentRowTypeDtoServiceCustomer.getCodeByCodeType(getUserJson(), saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                String rtnCode = JacksonUtils.fromJson(resultInfo, String.class);
                result.setResult(rtnCode);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getCode());
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("getCodeByCodeType:" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }
}
