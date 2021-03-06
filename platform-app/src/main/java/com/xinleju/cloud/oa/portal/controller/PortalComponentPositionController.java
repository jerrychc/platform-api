package com.xinleju.cloud.oa.portal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.cloud.oa.portal.dto.PortalComponentPositionDto;
import com.xinleju.cloud.oa.portal.dto.PortalPageDto;
import com.xinleju.cloud.oa.portal.dto.service.PortalComponentPositionDtoServiceCustomer;
import com.xinleju.cloud.oa.portal.dto.service.PortalPageDtoServiceCustomer;
import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.platform.uitls.OpeLogInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 站点组件位置表控制层
 *
 * @author admin
 */
@Controller
@RequestMapping("/oa/portal/portalComponentPosition")
public class PortalComponentPositionController {

    private static Logger log = Logger.getLogger(PortalComponentPositionController.class);

    @Autowired
    private PortalComponentPositionDtoServiceCustomer portalComponentPositionDtoServiceCustomer;

    @Autowired
    private PortalPageDtoServiceCustomer portalPageDtoServiceCustomer;

    /**
     * 根据Id获取业务对象
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public MessageResult get(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalComponentPositionDtoServiceCustomer.getObjectById(userInfo, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PortalComponentPositionDto portalComponentPositionDto = JacksonUtils.fromJson(resultInfo, PortalComponentPositionDto.class);
                result.setResult(portalComponentPositionDto);
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
     * @param map 分页参数json字符串
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.POST}, consumes = "application/json")
    @ResponseBody
    public MessageResult page(@RequestBody Map<String,Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalComponentPositionDtoServiceCustomer.getPage(userInfo, paramaterJson);
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
     * @param map 查询条件参数map集合
     * @return
     */
    @RequestMapping(value = "/queryList", method = {RequestMethod.POST}, consumes = "application/json")
    @ResponseBody
    public MessageResult queryList(@RequestBody Map<String,Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalComponentPositionDtoServiceCustomer.findComponentsByPortalId(userInfo,paramaterJson);
            //portalComponentPositionDtoServiceCustomer.queryList(userInfo, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<Map<String,Object>> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, Map.class);
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

    @RequestMapping(value = "/queryAllList", method = {RequestMethod.POST}, consumes = "application/json")
    @ResponseBody
    public MessageResult queryAllList(@RequestBody Map<String,Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalComponentPositionDtoServiceCustomer.queryList(userInfo, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<PortalComponentPositionDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, PortalComponentPositionDto.class);
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
     * @param t 需要保存的对象
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public MessageResult save(@RequestBody PortalComponentPositionDto t) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String saveJson = JacksonUtils.toJson(t);
            String dubboResultInfo = portalComponentPositionDtoServiceCustomer.save(userInfo, saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PortalComponentPositionDto portalComponentPositionDto = JacksonUtils.fromJson(resultInfo, PortalComponentPositionDto.class);
                result.setResult(portalComponentPositionDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
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
     * 批量保存门户组件位置
     * @param positionMaps 门户组件位置map集合数组
     * @return
     */
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    @OpeLogInfo(node = "门户设计",sysCode = "OA")
    public MessageResult saveBatch(@RequestBody List<Map<String,Object>> positionMaps) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String portalName = "";
            if(positionMaps!=null&&positionMaps.size()>0){
                String portalPageId = (String) positionMaps.get(0).get("portalPageId");
                if(StringUtils.isNotBlank(portalPageId)){
                    String portalPageDubboInfo = portalPageDtoServiceCustomer.getObjectById(userInfo,"{\"id\":\"" + portalPageId + "\"}");
                    DubboServiceResultInfo portalDubboServiceResultInfo = JacksonUtils.fromJson(portalPageDubboInfo, DubboServiceResultInfo.class);
                    if(portalDubboServiceResultInfo.isSucess()){
                        String resultInfo = portalDubboServiceResultInfo.getResult();
                        PortalPageDto portalPageDto = JacksonUtils.fromJson(resultInfo, PortalPageDto.class);
                        portalName = portalPageDto.getPortalPageName();
                    }
                }
            }

            String saveJson = JacksonUtils.toJson(positionMaps);
            String dubboResultInfo = portalComponentPositionDtoServiceCustomer.saveBatch(userInfo, saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<Map<String,Object>> portalComponentPositionMaps = JacksonUtils.fromJson(resultInfo, List.class,Map.class);
                result.setResult("门户【"+portalName+"】布局修改成功:"+resultInfo);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(positionMaps);
                log.error("调用saveBatch方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
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
     * 批量保存门户组件位置
     * @param positionMaps 门户组件位置map集合数组
     * @return
     */
    @RequestMapping(value = "/saveBatchForPersonal", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public MessageResult saveBatchForPersonal(@RequestBody List<Map<String,Object>> positionMaps) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String saveJson = JacksonUtils.toJson(positionMaps);
            String dubboResultInfo = portalComponentPositionDtoServiceCustomer.saveBatchForPersonal(userInfo, saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<Map<String,Object>> portalComponentPositionDto = JacksonUtils.fromJson(resultInfo,List.class, Map.class);
                result.setResult(portalComponentPositionDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(positionMaps);
                log.error("调用saveBatch方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
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
     * @param id 实体对象主键id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public MessageResult delete(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalComponentPositionDtoServiceCustomer.deleteObjectById(userInfo, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PortalComponentPositionDto portalComponentPositionDto = JacksonUtils.fromJson(resultInfo, PortalComponentPositionDto.class);
                result.setResult(portalComponentPositionDto);
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
     * @param ids 需要删除的实体对象主键id字符串，以逗号分隔
     * @return
     */
    @RequestMapping(value = "/deleteBatch/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    public MessageResult deleteBatch(@PathVariable("ids") String ids) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalComponentPositionDtoServiceCustomer.deleteAllObjectByIds(userInfo, "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PortalComponentPositionDto portalComponentPositionDto = JacksonUtils.fromJson(resultInfo, PortalComponentPositionDto.class);
                result.setResult(portalComponentPositionDto);
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
     * @param id 需要更新的对象主键id
     * @param map 需要更新的对象属性-值map集合
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseBody
    public MessageResult update(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        PortalComponentPositionDto portalComponentPositionDto = null;
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalComponentPositionDtoServiceCustomer.getObjectById(userInfo, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                Map<String, Object> oldMap = JacksonUtils.fromJson(resultInfo, HashMap.class);
                oldMap.putAll(map);
                String updateJson = JacksonUtils.toJson(oldMap);
                String updateDubboResultInfo = portalComponentPositionDtoServiceCustomer.update(userInfo, updateJson);
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
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(portalComponentPositionDto);
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


}
