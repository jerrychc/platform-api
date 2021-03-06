package com.xinleju.cloud.oa.content.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.cloud.oa.content.dto.ContentRowAttributeDto;
import com.xinleju.cloud.oa.content.dto.service.ContentRowAttributeDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 数据项属性设置控制层
 *
 * @author admin
 */
@Controller
@RequestMapping("/oa/content/contentRowAttribute")
public class ContentRowAttributeController {

    private static Logger log = Logger.getLogger(ContentRowAttributeController.class);

    @Autowired
    private ContentRowAttributeDtoServiceCustomer contentRowAttributeDtoServiceCustomer;

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
            String dubboResultInfo = contentRowAttributeDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentRowAttributeDto contentRowAttributeDto = JacksonUtils.fromJson(resultInfo, ContentRowAttributeDto.class);
                result.setResult(contentRowAttributeDto);
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
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboResultInfo = contentRowAttributeDtoServiceCustomer.getPage(getUserJson(), paramaterJson);
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
            log.error("调用page方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
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
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboResultInfo = contentRowAttributeDtoServiceCustomer.queryList(getUserJson(), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentRowAttributeDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentRowAttributeDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
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
     * 保存实体对象
     *
     * @param t
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    MessageResult save(@RequestBody ContentRowAttributeDto t) {
        MessageResult result = new MessageResult();
        try {
            String saveJson = JacksonUtils.toJson(t);
            String save = contentRowAttributeDtoServiceCustomer.save(getUserJson(), saveJson);
            String dubboResultInfo = save;
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentRowAttributeDto contentRowAttributeDto = JacksonUtils.fromJson(resultInfo, ContentRowAttributeDto.class);
                result.setResult(contentRowAttributeDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                String msg = dubboServiceResultInfo.getExceptionMsg()==null?dubboServiceResultInfo.getMsg():dubboServiceResultInfo.getExceptionMsg();
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + msg + "】");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(t);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
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
    public
    @ResponseBody
    MessageResult delete(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentRowAttributeDtoServiceCustomer.deleteObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentRowAttributeDto contentRowAttributeDto = JacksonUtils.fromJson(resultInfo, ContentRowAttributeDto.class);
                result.setResult(contentRowAttributeDto);
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
            String dubboResultInfo = contentRowAttributeDtoServiceCustomer.deleteAllObjectByIds(getUserJson(), "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentRowAttributeDto contentRowAttributeDto = JacksonUtils.fromJson(resultInfo, ContentRowAttributeDto.class);
                result.setResult(contentRowAttributeDto);
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
    public
    @ResponseBody
    MessageResult update(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        ContentRowAttributeDto contentRowAttributeDto = null;
        try {
            String dubboResultInfo = contentRowAttributeDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                Map<String, Object> oldMap = JacksonUtils.fromJson(resultInfo, HashMap.class);
                oldMap.putAll(map);
                String updateJson = JacksonUtils.toJson(oldMap);
                String updateDubboResultInfo = contentRowAttributeDtoServiceCustomer.update(getUserJson(), updateJson);
                DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
                if (updateDubboServiceResultInfo.isSucess()) {
                    Integer i = JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
                    result.setResult(i);
                    result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                    result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
                } else {
                    result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                    String msg = updateDubboServiceResultInfo.getExceptionMsg()==null?updateDubboServiceResultInfo.getMsg():updateDubboServiceResultInfo.getExceptionMsg();
                    result.setMsg(updateDubboServiceResultInfo.getMsg() + "【" + msg + "】");
                }
            } else {
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg("不存在更新的对象");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(contentRowAttributeDto);
                log.error("调用update方法:  【参数" + id + "," + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg(MessageInfo.UPDATEERROR.getMsg() + "【" + e.getMessage() + "】");
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    /**
     * author:wangw
     * 删除实体对象
     *
     * @param t
     * @return oper 是操作类型  增删改查
     */
    @RequestMapping(value = "/saveOrUpdateRow", method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResult saveOrUpdateRow(@RequestBody ContentRowAttributeDto t, String oper) {
        MessageResult result = new MessageResult();
        if (StringUtils.isNotEmpty(oper)) {
            if ("del".equals(oper)) {
                String dubboResultInfo = contentRowAttributeDtoServiceCustomer.deleteObjectById(getUserJson(), "{\"id\":\"" + t.getId() + "\"}");
                DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
                if (dubboServiceResultInfo.isSucess()) {
                    String resultInfo = dubboServiceResultInfo.getResult();
                    ContentRowAttributeDto customArchivesDto = JacksonUtils.fromJson(resultInfo, ContentRowAttributeDto.class);
                    result.setResult(customArchivesDto);
                    result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                    result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
                } else {
                    result.setSuccess(MessageInfo.DELETEERROR.isResult());
                    result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
                }
            } else if ("add".equals(oper)) {
                t.setId(IDGenerator.getUUID());
                // t.setContentTypeId(contentTypeId.split(",")[0]);
                String saveJson = JacksonUtils.toJson(t);
                String dubboResultInfo = contentRowAttributeDtoServiceCustomer.save(getUserJson(), saveJson);
                DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
                if (dubboServiceResultInfo.isSucess()) {
                    String resultInfo = dubboServiceResultInfo.getResult();
                    ContentRowAttributeDto customArchivesDto = JacksonUtils.fromJson(resultInfo, ContentRowAttributeDto.class);
                    result.setResult(customArchivesDto);
                    result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                    result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
                } else {
                    result.setSuccess(MessageInfo.SAVEERROR.isResult());
                    result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
                }
            } else if ("edit".equals(oper)) {
                String dubboResultInfo = contentRowAttributeDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + t.getId() + "\"}");
                DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
                if (dubboServiceResultInfo.isSucess()) {
                    String resultInfo = dubboServiceResultInfo.getResult();
                    Map<String, Object> oldMap = JacksonUtils.fromJson(resultInfo, HashMap.class);
                    oldMap.put("code", t.getFieldCode());
                    String updateJson = JacksonUtils.toJson(oldMap);
                    String updateDubboResultInfo = contentRowAttributeDtoServiceCustomer.update(getUserJson(), updateJson);
                    DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
                    if (updateDubboServiceResultInfo.isSucess()) {
                        Integer i = JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
                        result.setResult(i);
                        result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                        result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
                    } else {
                        result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                        result.setMsg(updateDubboServiceResultInfo.getMsg() + "【" + updateDubboServiceResultInfo.getExceptionMsg() + "】");
                    }
                } else {
                    result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                    result.setMsg("不存在更新的对象");
                }
            }

        }
        return result;

    }


    /**
     * 返回分页对象,不带任何分页信息
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/pageInfo", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult pageInfo(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboResultInfo = contentRowAttributeDtoServiceCustomer.getPageInfo(getUserJson(), paramaterJson);
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
            log.error("调用page方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }


    /**
     * 根据大类Id获取数据属性对象
     *
     * @param contentTypeId 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/queryContentRowAttributeList/{contentTypeId}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult queryContentRowAttributeList(@PathVariable("contentTypeId") String contentTypeId) {
        MessageResult result = new MessageResult();


        try {
            String dubboResultInfo = contentRowAttributeDtoServiceCustomer.queryContentRowAttributeList(getUserJson(), contentTypeId);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentRowAttributeDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentRowAttributeDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryContentRowAttributeList方法:  【参数" + contentTypeId + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }


    /**
     * 批量修改修改实体对象
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateBatchAttribute/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public
    @ResponseBody
    MessageResult updateBatchAttribute(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        ContentRowAttributeDto contentRowAttributeDto = null;
        map.put("ids", id);
        try {
            String dubboResultInfo = contentRowAttributeDtoServiceCustomer.updateBatchAttribute(getUserJson(), JacksonUtils.toJson(map));
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                Integer i = JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), Integer.class);
                result.setResult(i);
                result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg("不存在更新的对象");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(contentRowAttributeDto);
                log.error("调用update方法:  【参数" + id + "," + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg(MessageInfo.UPDATEERROR.getMsg() + "【" + e.getMessage() + "】");
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 根据大类ID获取列表页查询条件
     *
     * @param contentTypeId 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/queryContentRowQueryAttributeList/{contentTypeId}", method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResult queryContentRowQueryAttributeList(@PathVariable("contentTypeId") String contentTypeId, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        map.put("contentTypeId", contentTypeId);
        try {
            String dubboResultInfo = contentRowAttributeDtoServiceCustomer.queryContentRowQueryAttributeList(getUserJson(), JacksonUtils.toJson(map));
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentRowAttributeDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentRowAttributeDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryContentRowQueryAttributeList方法:  【参数" + contentTypeId + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }


    /**
     * 获取用户登陆信息
     *
     * @return
     */
    private String getUserJson() {
        SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        String userJson = JacksonUtils.toJson(userBeanInfo);
        return userJson;
    }

    /**
     * 修改修改实体对象
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sortAttr/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public
    @ResponseBody
    MessageResult sortAttr(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        try {
            String saveJson = JacksonUtils.toJson(map);
            String save = contentRowAttributeDtoServiceCustomer.updateAttrSort(getUserJson(), saveJson);
            String dubboResultInfo = save;
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentRowAttributeDto contentRowAttributeDto = JacksonUtils.fromJson(resultInfo, ContentRowAttributeDto.class);
                result.setResult(contentRowAttributeDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                String msg = dubboServiceResultInfo.getExceptionMsg()==null?dubboServiceResultInfo.getMsg():dubboServiceResultInfo.getExceptionMsg();
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + msg + "】");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(map);
                log.error("调用sortAttr方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

}
