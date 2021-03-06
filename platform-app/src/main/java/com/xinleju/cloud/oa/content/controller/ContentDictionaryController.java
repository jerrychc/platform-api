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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.cloud.oa.content.dto.ContentChildTreeData;
import com.xinleju.cloud.oa.content.dto.ContentDictionaryDto;
import com.xinleju.cloud.oa.content.dto.service.ContentDictionaryDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 数据字典控制层
 *
 * @author admin
 */
@Controller
@RequestMapping("/oa/dictionary/contentDictionary")
public class ContentDictionaryController {

    private static Logger log = Logger.getLogger(ContentDictionaryController.class);

    @Autowired
    private ContentDictionaryDtoServiceCustomer contentDictionaryDtoServiceCustomer;

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
            String dubboResultInfo = contentDictionaryDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentDictionaryDto contentDictionaryDto = JacksonUtils.fromJson(resultInfo, ContentDictionaryDto.class);
                result.setResult(contentDictionaryDto);
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
            String dubboResultInfo = contentDictionaryDtoServiceCustomer.getPage(getUserJson(), paramaterJson);
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
     * @param paramater
     * @return
     */
    @RequestMapping(value = "/queryList", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult queryList(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboResultInfo = contentDictionaryDtoServiceCustomer.queryList(getUserJson(), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentDictionaryDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentDictionaryDto.class);
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
    MessageResult save(@RequestBody ContentDictionaryDto t) {
        MessageResult result = new MessageResult();
        try {
            String saveJson = JacksonUtils.toJson(t);
            String dubboResultInfo = contentDictionaryDtoServiceCustomer.save(getUserJson(), saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentDictionaryDto contentDictionaryDto = JacksonUtils.fromJson(resultInfo, ContentDictionaryDto.class);
                result.setResult(contentDictionaryDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                String msg = dubboServiceResultInfo.getExceptionMsg()==null?dubboServiceResultInfo.getMsg():"未知错误，保存失败！";
                result.setMsg(msg);
                result.setCode(dubboServiceResultInfo.getCode());
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
     * @param t
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    MessageResult delete(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentDictionaryDtoServiceCustomer.deleteObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentDictionaryDto contentDictionaryDto = JacksonUtils.fromJson(resultInfo, ContentDictionaryDto.class);
                result.setResult(contentDictionaryDto);
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
     * @param t
     * @return
     */
    @RequestMapping(value = "/deleteBatch/{ids}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    MessageResult deleteBatch(@PathVariable("ids") String ids) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentDictionaryDtoServiceCustomer.deleteAllObjectByIds(getUserJson(), "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentDictionaryDto contentDictionaryDto = JacksonUtils.fromJson(resultInfo, ContentDictionaryDto.class);
                result.setResult(contentDictionaryDto);
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
     * @param t
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public
    @ResponseBody
    MessageResult update(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        ContentDictionaryDto contentDictionaryDto = null;
        try {
            String dubboResultInfo = contentDictionaryDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                Map<String, Object> oldMap = JacksonUtils.fromJson(resultInfo, HashMap.class);
                oldMap.putAll(map);
                String updateJson = JacksonUtils.toJson(oldMap);
                String updateDubboResultInfo = contentDictionaryDtoServiceCustomer.update(getUserJson(), updateJson);
                DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
                if (updateDubboServiceResultInfo.isSucess()) {
                    Integer i = JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
                     MessageResult result2 = this.get(id);
                    result.setResult(result2.getResult());
                    result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                    result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());

                } else {
                    result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                    String msg = updateDubboServiceResultInfo.getMsg()!=null?updateDubboServiceResultInfo.getMsg():"未知错误，保存失败！";
                    result.setMsg(msg);
                    result.setCode(updateDubboServiceResultInfo.getCode());
                }
            } else {
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg("不存在更新的对象");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(contentDictionaryDto);
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
     * 伪删除实体对象
     *
     * @param t
     * @return
     */
    @RequestMapping(value = "/deletePseudo/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    MessageResult deletePseudo(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentDictionaryDtoServiceCustomer.deletePseudoObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentDictionaryDto contentDictionaryDto = JacksonUtils.fromJson(resultInfo, ContentDictionaryDto.class);
                result.setResult(contentDictionaryDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                String msg = dubboServiceResultInfo.getExceptionMsg()==null?dubboServiceResultInfo.getMsg():dubboServiceResultInfo.getExceptionMsg();
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + msg + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用deletePseudo方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + e.getMessage() + "】");
        }

        return result;
    }


    /**
     * 伪删除实体对象
     *
     * @param t
     * @return
     */
    @RequestMapping(value = "/deletePseudoBatch/{ids}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    MessageResult deletePseudoBatch(@PathVariable("ids") String ids) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentDictionaryDtoServiceCustomer.deletePseudoAllObjectByIds(getUserJson(), "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentDictionaryDto contentDictionaryDto = JacksonUtils.fromJson(resultInfo, ContentDictionaryDto.class);
                result.setResult(contentDictionaryDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用deletePseudoBatch方法:  【参数" + ids + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + e.getMessage() + "】");
        }

        return result;
    }

    @RequestMapping(value = "/queryDictionaryTree", method = {RequestMethod.POST}, consumes = "application/json")
    @ResponseBody
    public MessageResult queryDictionaryTree(@RequestBody Map<String,Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboResultInfo = contentDictionaryDtoServiceCustomer.queryDictionaryTree(getUserJson(), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentDictionaryDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentDictionaryDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryDictionaryTree方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    /**
     * 获取数据字典树结构
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @Deprecated
    @RequestMapping(value = "/getContentDictionaryTreeById/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult getContentDictionaryTreeById(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentDictionaryDtoServiceCustomer.getContentDictionaryTreeById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentChildTreeData> contentList = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentChildTreeData.class);
                result.setResult(contentList);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getContentList方法:  【参数】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    private String getUserJson() {
        SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        String userJson = JacksonUtils.toJson(userBeanInfo);
        return userJson;
    }
}
