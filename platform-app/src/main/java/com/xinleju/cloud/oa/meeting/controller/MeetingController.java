package com.xinleju.cloud.oa.meeting.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.xinleju.cloud.oa.meeting.dto.MeetingSummaryDto;
import com.xinleju.cloud.oa.meeting.dto.service.MeetingSummaryDtoServiceCustomer;
import com.xinleju.platform.base.utils.*;

import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.sys.security.dto.service.AuthenticationDtoServiceCustomer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.cloud.oa.content.dto.ContentRowTypeDto;
import com.xinleju.cloud.oa.meeting.dto.MeetingDto;
import com.xinleju.cloud.oa.meeting.dto.MeetingPartnerDto;
import com.xinleju.cloud.oa.meeting.dto.MeetingReplyDto;
import com.xinleju.cloud.oa.meeting.dto.service.MeetingDtoServiceCustomer;
import com.xinleju.cloud.oa.meeting.dto.service.MeetingReplyDtoServiceCustomer;
import com.xinleju.platform.sys.org.dto.service.UserDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 会议基本信息控制层
 *
 * @author wangw
 */
@Controller
@RequestMapping("/oa/meeting/meeting")
public class MeetingController {

    private static Logger log = Logger.getLogger(MeetingController.class);

    @Autowired
    private MeetingDtoServiceCustomer meetingDtoServiceCustomer;
    @Autowired
    private UserDtoServiceCustomer userDtoServiceCustomer;
    @Autowired
    private MeetingReplyDtoServiceCustomer meetingReplyDtoServiceCustomer;
    @Autowired
    private AuthenticationDtoServiceCustomer authenticationDtoServiceCustomer;
    @Autowired
    private MeetingSummaryDtoServiceCustomer meetingSummaryDtoServiceCustomer;
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
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String dubboResultInfo = meetingDtoServiceCustomer.getMeetingAndAgendaById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                MeetingDto meetingDto = JacksonUtils.fromJson(resultInfo, MeetingDto.class);

                result.setResult(meetingDto);
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
     * @param paramater
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.POST}, consumes = "application/json")
    public @ResponseBody MessageResult page(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboResultInfo = meetingDtoServiceCustomer.getPage(getUserJson(), paramaterJson);
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
    public @ResponseBody MessageResult queryList(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboResultInfo = meetingDtoServiceCustomer.queryList(getUserJson(), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<MeetingDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, MeetingDto.class);
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
     * 会议管理列表
     *
     * @param paramater
     * @return
     */
    @RequestMapping(value = "/meetingManagerList", method = {RequestMethod.POST}, consumes = "application/json")
    public @ResponseBody MessageResult meetingManagerList(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboResultInfo = meetingDtoServiceCustomer.meetingManagerList(getUserJson(), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<MeetingDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, MeetingDto.class);
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
    public @ResponseBody MessageResult save(@RequestBody MeetingDto t) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            t.setCreateUserId(userBeanInfo.getSecurityUserDto().getId());
            t.setCreateUserName(userBeanInfo.getSecurityUserDto().getRealName());
            String saveJson = JacksonUtils.toJson(t);
            String dubboResultInfo = meetingDtoServiceCustomer.save(getUserJson(), saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                MeetingDto meetingDto = JacksonUtils.fromJson(resultInfo, MeetingDto.class);
                result.setResult(meetingDto);
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
     * 删除实体对象
     *
     * @param t
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody MessageResult delete(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = meetingDtoServiceCustomer.deleteObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                MeetingDto meetingDto = JacksonUtils.fromJson(resultInfo, MeetingDto.class);
                result.setResult(meetingDto);
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
    public @ResponseBody MessageResult deleteBatch(@PathVariable("ids") String ids) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = meetingDtoServiceCustomer.deleteAllObjectByIds(getUserJson(), "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                MeetingDto meetingDto = JacksonUtils.fromJson(resultInfo, MeetingDto.class);
                result.setResult(meetingDto);
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
    public @ResponseBody MessageResult update(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        MeetingDto meetingDto = null;
        try {
            String dubboResultInfo = meetingDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                Map<String, Object> oldMap = JacksonUtils.fromJson(resultInfo, HashMap.class);
                oldMap.putAll(map);
                String updateJson = JacksonUtils.toJson(oldMap);
                String updateDubboResultInfo = meetingDtoServiceCustomer.update(getUserJson(), updateJson);
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
                String paramJson = mapper.writeValueAsString(meetingDto);
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
     * 会议撤回
     *
     * @param t
     * @return
     */
    @RequestMapping(value = "/withdraw/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public @ResponseBody MessageResult withdraw(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        MeetingDto meetingDto = null;
        try {
            String dubboResultInfo = meetingDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                meetingDto = JacksonUtils.fromJson(resultInfo, MeetingDto.class);
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                Date now = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
                Date beginDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(meetingDto.getBeginDate());
                boolean b = false;
                b = beginDate.after(now);
                if (meetingDto != null && !meetingDto.getStatus().equals("2")) {
                    result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                    result.setMsg("不是待开会议，不能撤回！");
                } else if (!b) {
                    result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                    result.setMsg("会议已经开始或者会议已经结束！");
                } else if (meetingDto != null && meetingDto.getStatus().equals("2") && b) {
                    Map<String, Object> oldMap = JacksonUtils.fromJson(resultInfo, HashMap.class);
                    oldMap.putAll(map);
                    String updateJson = JacksonUtils.toJson(oldMap);
                    String updateDubboResultInfo = meetingDtoServiceCustomer.update(getUserJson(), updateJson);
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

                }
            } else {
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg("不存在更新的对象");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(meetingDto);
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
     * 会后管理-会议归档
     *
     * @param t
     * @return
     */
    @RequestMapping(value = "/archiveMeeting/{ids}", method = RequestMethod.PUT, consumes = "application/json")
    public @ResponseBody MessageResult archiveMeeting(@PathVariable("ids") String ids, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = meetingDtoServiceCustomer.archiveMeeting(getUserJson(), ids, map);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                result.setResult(resultInfo);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用archiveMeeting方法:  【参数" + ids + "," + map + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.UPDATEERROR.isResult());
            result.setMsg(MessageInfo.UPDATEERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    /**
     * author:wangw
     * 上传会议纪要后，修改数据状态
     */
    @RequestMapping(value = "/uploadMeetingSummary/{id}", method = RequestMethod.GET, consumes = "application/json")
    public MessageResult uploadMeetingSummary(@PathVariable("id") String ids) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = meetingDtoServiceCustomer.uploadMeetingSummary(getUserJson(), ids);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                result.setResult(resultInfo);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用archiveMeeting方法:  【参数" + ids + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.UPDATEERROR.isResult());
            result.setMsg(MessageInfo.UPDATEERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;

    }


    private String getUserJson() {
        SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        String userJson = JacksonUtils.toJson(userBeanInfo);
        return userJson;
    }


    /**
     * 修改修改实体对象
     *
     * @param t
     * @return
     */
    @RequestMapping(value = "/updateMeeingStatusOfNoticeMsg/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public @ResponseBody MessageResult updateMeeingStatusOfNoticeMsg(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        MeetingDto meetingDto = null;
        try {
            String updateDubboResultInfo = meetingDtoServiceCustomer.updateMeeingStatusOfNoticeMsg(getUserJson(), map);
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
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(meetingDto);
                log.error("调用updateMeeingStatusOfNoticeMsg方法:  【参数" + id + "," + paramJson + "】======" + "【" + e.getMessage() + "】");
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
     * 返回符合条件的列表
     *
     * @param paramater
     * @return
     */
    @RequestMapping(value = "/getAllMeetingByParameter", method = {RequestMethod.POST}, consumes = "application/json")
    public @ResponseBody MessageResult getAllMeetingByParameter(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        try {
            String userId = userBeanInfo.getSecurityUserDto().getId();
            map.put("userId", userId);
            String dubboResultInfo = meetingDtoServiceCustomer.getAllMeetingByParameter(getUserJson(), map);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<MeetingDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, MeetingDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getAllMeetingByParameter方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    /**
     * 催办会议，取消会议，变更会议的操作
     *
     * @param paramater
     * @return
     */
    @RequestMapping(value = "/operationMeeting", method = {RequestMethod.POST}, consumes = "application/json")
    public @ResponseBody MessageResult operationMeeting(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        try {
            String userId = userBeanInfo.getSecurityUserDto().getId();
            map.put("userId", userId);
            String dubboResultInfo = meetingDtoServiceCustomer.operationMeeting(getUserJson(), map);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                result.setResult(resultInfo);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getAllMeetingByParameter方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    /**
     * 获取会议纪要人参加的会议列表
     *
     * @param paramater
     * @return
     */
    @RequestMapping(value = "/getAllMeetingByRecordUser", method = {RequestMethod.POST}, consumes = "application/json")
    public @ResponseBody MessageResult getAllMeetingByRecordUser(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        try {
            String userId = userBeanInfo.getSecurityUserDto().getId();
            map.put("userId", userId);
            String dubboResultInfo = meetingDtoServiceCustomer.getAllMeetingByRecordUser(getUserJson(), map);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<MeetingDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, MeetingDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getAllMeetingByParameter方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
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
    public @ResponseBody MessageResult saveTemporary(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            map.put("createUserId", userBeanInfo.getSecurityUserDto().getId());
            map.put("createUserName", userBeanInfo.getSecurityUserDto().getRealName());
            String saveJson = JacksonUtils.toJson(map);
            String dubboResultInfo = meetingDtoServiceCustomer.saveTemporary(getUserJson(), saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                MeetingDto MeetingDto = JacksonUtils.fromJson(resultInfo, MeetingDto.class);
                result.setResult(MeetingDto);
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

    @RequestMapping(value = "/getStartFlowInfo", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody MessageResult getStartFlowInfo(@RequestBody Map<String, Object> resmap) {
        MessageResult result = new MessageResult();
        String id = (String) resmap.get("businessId");
        SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
        if(resmap.get("token")!=null){//加入白名单处理获取表单流程业务变量查询
            String token = String.valueOf(resmap.get("token"));
            String[] args = token.split("@");
            if(args.length==2){
                securityUserBeanInfo.setTendCode(args[1]);
                SecurityUserBeanInfo securityUserBeanInfo1 = LoginUtils.getSecurityUserBeanInfo();
                SecurityUserDto securityUserDto = null;
                if (securityUserBeanInfo1 == null) {
                    Map<String,Object> paramMap = new HashMap<String,Object>();
                    paramMap.put("loginName",args[0]);
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

                    securityUserDto = securityUserBeanInfo1.getSecurityUserDto();
                }
                securityUserBeanInfo.setSecurityUserDto(securityUserDto);
                this.setUserAuthInfo(securityUserBeanInfo,securityUserDto);
            }
        }else{
            securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        }
        try {
            String dubboResultInfo = meetingDtoServiceCustomer.getStartFlowInfo(JacksonUtils.toJson(securityUserBeanInfo), id);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                result.setResult(resultInfo);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {

            log.error("调用get方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody MessageResult updateCount(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
        if(map.get("token")!=null){//加入白名单处理获取新闻/知识表单流程业务回调
            String token = String.valueOf(map.get("token"));
            String[] args = token.split("@");
            if(args.length==2){
                securityUserBeanInfo.setTendCode(args[1]);
                SecurityUserBeanInfo securityUserBeanInfo1 = LoginUtils.getSecurityUserBeanInfo();
                SecurityUserDto securityUserDto = null;
                if (securityUserBeanInfo1 == null) {
                    Map<String,Object> paramMap = new HashMap<String,Object>();
                    paramMap.put("loginName",args[0]);
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
                    securityUserDto = securityUserBeanInfo1.getSecurityUserDto();
                }
                securityUserBeanInfo.setSecurityUserDto(securityUserDto);
            }
        }else{
            securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        }
        try {
            String dubboResultInfo = meetingDtoServiceCustomer.updateStatus(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                Integer i = JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), Integer.class);
                result.setResult(i);
                result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg(dubboServiceResultInfo.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }

        } catch (Exception e) {
            log.error("调用update方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.UPDATEERROR.isResult());
            result.setMsg(MessageInfo.UPDATEERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }


    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public @ResponseBody MessageResult getUserInfo() {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            result.setResult(securityUserBeanInfo.getSecurityUserDto());
            result.setSuccess(MessageInfo.GETSUCCESS.isResult());
            result.setMsg(MessageInfo.GETSUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用getUserInfo方法:  【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }


    /**
     * 拷贝录入和会议变更的时候要复制附件
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/attachment/save", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody MessageResult saveAttachment(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        try {
            String saveJson = JacksonUtils.toJson(map);
            String dubboResultInfo = meetingDtoServiceCustomer.saveAttachment(getUserJson(), saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                MeetingDto meetingDto = JacksonUtils.fromJson(resultInfo, MeetingDto.class);
                result.setResult(meetingDto);
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
     * 获取potal页面的会议列表，
     *
     * @param paramater
     * @return
     */
    @RequestMapping(value = "/potalPage", method = {RequestMethod.POST}, consumes = "application/json")
    public @ResponseBody MessageResult poatlPage(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboResultInfo = meetingDtoServiceCustomer.getPotalPage(getUserJson(), paramaterJson);
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
     * 录入会议纪要时获取到的部分会议的业务对象
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/getMeetingInfo/{id}", method = RequestMethod.GET)
    public @ResponseBody MessageResult getMeetingInfo(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String dubboResultInfo = meetingDtoServiceCustomer.getMeetingSummInfoById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                MeetingDto meetingDto = JacksonUtils.fromJson(resultInfo, MeetingDto.class);

                result.setResult(meetingDto);
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

    @RequestMapping(value = "/getMeetingPortal",method = RequestMethod.GET,produces = "text/html; charset=utf-8")
    @ResponseBody
    public String getMeetingPortal(HttpServletRequest request){
        String contextPath = request.getContextPath();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("limit",5);
        paramMap.put("start",0);
        String html = "暂无数据！";
        try {
            String paramaterJson = JacksonUtils.toJson(paramMap);
            String dubboResultInfo = meetingDtoServiceCustomer.getPotalPage(getUserJson(), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PageBeanInfo pageInfo = JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
                List<Map<String,Object>> list = pageInfo.getList();

                StringBuffer buf = new StringBuffer();
                buf.append("<ul class=\"rules_list\" id=\"oaMettingSchedule\">");
                for (Map<String,Object> map:list) {
                    String id = (String) map.get("id");
                    String title = (String) map.get("title");
                    String status = (String) map.get("status");
                    String beginTime = (String) map.get("beginTime");
                    String beginTimeStr = "";
                    beginTime = beginTime.substring(0,10);

                    String url = contextPath + "/meeting/meeting/meeting_view.html?act=view&id="+id+"&update=false";
                    if("0".equals(status)){
                        url = contextPath + "/meeting/meeting/meeting_edit.html?act=update&id="+id+"&update=true";
                    }
                    buf.append("<li>");
                    buf.append("<a href=\""+url+"\" target=\"_blank\" title=\""+title+"\"><i></i>"+title+"</a>");
                    buf.append("<span class=\"date\">"+beginTime+"</span>");
                    buf.append("</li>");

                }
                buf.append("</ul>");
                html = buf.toString();
            }

        }catch (Exception e){
            log.error("调用getMeetingPortal出错："+e.getMessage());
            return "服务器错误！";
        }

        return html;
    }

    private void setUserAuthInfo(SecurityUserBeanInfo securityUserBeanInfo,SecurityUserDto securityUserDto){
        try {
                String authenticationInfodubboResultInfo = authenticationDtoServiceCustomer.getUserAuthenticationInfoWithoutResource(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson(securityUserDto));
                DubboServiceResultInfo authenticationInfodubboServiceResultInfo= JacksonUtils.fromJson(authenticationInfodubboResultInfo, DubboServiceResultInfo.class);
                if(authenticationInfodubboServiceResultInfo.isSucess()){
                    String authenticationInforesultInfo= authenticationInfodubboServiceResultInfo.getResult();
                    AuthenticationDto authenticationDto=JacksonUtils.fromJson(authenticationInforesultInfo, AuthenticationDto.class);
                    //获取用户标准岗位
                    List<SecurityStandardRoleDto> securityStandardRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getStandardRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
                    securityUserBeanInfo.setSecurityStandardRoleDtoList(securityStandardRoleDtoList);

                    //获取用户通用角色
                    List<SecurityStandardRoleDto> securityCurrencyRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getCurrencyRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
                    securityUserBeanInfo.setSecurityCurrencyRoleDtoList(securityCurrencyRoleDtoList);

                    //获取用户岗位
                    List<SecurityPostDto> securityPostDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getPostDtoList()),ArrayList.class,SecurityPostDto.class);
                    securityUserBeanInfo.setSecurityPostDtoList(securityPostDtoList);
                   /* //当前用户的菜单清单（未授权和已授权的）
                    List<SecurityResourceDto> SecurityResourceDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getResourceDtoList()),ArrayList.class,SecurityResourceDto.class);
                    securityUserBeanRelationInfo.setResourceDtoList(SecurityResourceDtoList);*/
                    //当前用户所在组织的类型
                    String securityOrganizationType = authenticationDto.getOrganizationType();
                    securityUserBeanInfo.setSecurityOrganizationType(securityOrganizationType);
                    //当前用户的一级公司
                    SecurityOrganizationDto securityTopCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopCompanyDto()),SecurityOrganizationDto.class);
                    securityUserBeanInfo.setSecurityTopCompanyDto(securityTopCompanyDto);
                    //当前用户的直属公司
                    SecurityOrganizationDto securityDirectCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectCompanyDto()),SecurityOrganizationDto.class);
                    securityUserBeanInfo.setSecurityDirectCompanyDto(securityDirectCompanyDto);
                    //当前用户的一级部门
                    SecurityOrganizationDto securityTopDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopDeptDto()),SecurityOrganizationDto.class);
                    securityUserBeanInfo.setSecurityTopDeptDto(securityTopDeptDto);
                    //当前用户的直属部门
                    SecurityOrganizationDto securityDirectDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectDeptDto()),SecurityOrganizationDto.class);
                    securityUserBeanInfo.setSecurityDirectDeptDto(securityDirectDeptDto);
                    //当前用户的项目
                    SecurityOrganizationDto securityGroupDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getGroupDto()),SecurityOrganizationDto.class);
                    securityUserBeanInfo.setSecurityGroupDto(securityGroupDto);
                    //当前用户的分期
                    SecurityOrganizationDto securityBranchDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getBranchDto()),SecurityOrganizationDto.class);
                    securityUserBeanInfo.setSecurityBranchDto(securityBranchDto);
                 }
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    @RequestMapping(value = "/validatCancelInstance", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody MessageResult validatCancelInstance(@RequestBody Map<String, Object> resmap) {
        MessageResult result = new MessageResult();
        String id = (String) resmap.get("businessId");
        SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
        if(resmap.get("token")!=null){//加入白名单处理获取表单流程业务变量查询
            String token = String.valueOf(resmap.get("token"));
            String[] args = token.split("@");
            if(args.length==2){
                securityUserBeanInfo.setTendCode(args[1]);
                SecurityUserBeanInfo securityUserBeanInfo1 = LoginUtils. getSecurityUserBeanInfo();
                SecurityUserDto securityUserDto = null;
                if (securityUserBeanInfo1 == null) {
                    Map<String,Object> paramMap = new HashMap<String,Object>();
                    paramMap.put("loginName",args[0]);
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

                    securityUserDto = securityUserBeanInfo1.getSecurityUserDto();
                }
                securityUserBeanInfo.setSecurityUserDto(securityUserDto);
                this.setUserAuthInfo(securityUserBeanInfo,securityUserDto);
            }
        }else{
            securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        }
        try {
            //String dubboResultInfo = meetingDtoServiceCustomer.getStartFlowInfo(JacksonUtils.toJson(securityUserBeanInfo), id);
            Map<String,Object> meetingSummaryMap = new HashMap<String,Object>();
            meetingSummaryMap.put("meetingId",id);
            String dubboResultInfo = meetingSummaryDtoServiceCustomer.queryList(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson(meetingSummaryMap));
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<MeetingSummaryDto> list = JacksonUtils.fromJson(resultInfo,List.class, MeetingSummaryDto.class);
                if(list!=null&&list.size()>0){
                    result.setResult(resultInfo);
                    result.setSuccess(MessageInfo.GETERROR.isResult());
                    result.setMsg("当前会议已经上报会议纪要！");
                }else{
                    result.setResult(resultInfo);
                    //result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                    result.setSuccess(MessageInfo.GETERROR.isResult());
                    result.setMsg("当前会议流程不能作废！");
                }

            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {

            log.error("调用get方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }
}
