package com.xinleju.platform.univ.attachment.dto.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.univ.attachment.dto.AttachmentTempDto;
import com.xinleju.platform.univ.attachment.dto.service.AttachmentTempDtoServiceCustomer;
import com.xinleju.platform.univ.attachment.entity.AttachmentTemp;
import com.xinleju.platform.univ.attachment.service.AttachmentTempService;

/**
 * @author haoqp
 *
 *
 */

public class AttachmentTempDtoServiceProducer implements AttachmentTempDtoServiceCustomer{
    private static Logger log = Logger.getLogger(AttachmentTempDtoServiceProducer.class);
    @Autowired
    private AttachmentTempService attachmentTempService;

    public String save(String userInfo, String saveJson){
        // TODO Auto-generated method stub
        DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
            AttachmentTemp attachmentTemp=JacksonUtils.fromJson(saveJson, AttachmentTemp.class);
            attachmentTempService.save(attachmentTemp);
            attachmentTemp = attachmentTempService.getObjectById(attachmentTemp.getId());
            info.setResult(JacksonUtils.toJson(attachmentTemp));
            info.setSucess(true);
            info.setMsg("保存对象成功!");
        } catch (Exception e) {
            log.error("保存对象失败!"+e.getMessage());
            info.setSucess(false);
            info.setMsg("保存对象失败!");
            info.setExceptionMsg(e.getMessage());
        }
        return JacksonUtils.toJson(info);
    }

    @Override
    public String saveBatch(String userInfo, String saveJsonList) {
        DubboServiceResultInfo info = new DubboServiceResultInfo();
        try {
            List<AttachmentTemp> attachmentTempList = JacksonUtils.fromJson(saveJsonList, ArrayList.class, AttachmentTemp.class);
            attachmentTempService.saveBatch(attachmentTempList);
            info.setResult(JacksonUtils.toJson(attachmentTempList));
            info.setSucess(true);
            info.setMsg("保存对象成功!");
        } catch (Exception e) {
            log.error("保存对象失败!" + e.getMessage());
            info.setSucess(false);
            info.setMsg("保存对象失败!");
            info.setExceptionMsg(e.getMessage());
        }
        return JacksonUtils.toJson(info);
    }
    
    @Override
    public String saveFileUpload(String userInfo, String saveJsonList) {
        DubboServiceResultInfo info = new DubboServiceResultInfo();
        try {
            List<AttachmentTempDto> attachmentTempList = JacksonUtils.fromJson(saveJsonList, ArrayList.class, AttachmentTempDto.class);
            attachmentTempService.saveFileUpload(attachmentTempList);
            info.setResult(JacksonUtils.toJson(attachmentTempList));
            info.setSucess(true);
            info.setMsg("保存对象成功!");
        } catch (Exception e) {
            log.error("保存对象失败!" + e.getMessage());
            info.setSucess(false);
            info.setMsg("保存对象失败!");
            info.setExceptionMsg(e.getMessage());
        }
        return JacksonUtils.toJson(info);
    }

    @Override
    public String updateBatch(String userInfo, String updateJsonList) {
        DubboServiceResultInfo info = new DubboServiceResultInfo();
        try {
            List<AttachmentTemp> attachmentTempList = JacksonUtils.fromJson(updateJsonList, ArrayList.class, AttachmentTemp.class);
            int result = attachmentTempService.updateBatch(attachmentTempList);
            info.setResult(JacksonUtils.toJson(result));
            info.setSucess(true);
            info.setMsg("更新对象成功!");
        } catch (Exception e) {
            log.error("更新对象失败!" + e.getMessage());
            info.setSucess(false);
            info.setMsg("更新对象失败!");
            info.setExceptionMsg(e.getMessage());
        }
        return JacksonUtils.toJson(info);
    }

    @Override
    public String update(String userInfo, String updateJson)  {
        // TODO Auto-generated method stub
        DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
            AttachmentTemp attachmentTemp=JacksonUtils.fromJson(updateJson, AttachmentTemp.class);
            int result=   attachmentTempService.update(attachmentTemp);
            info.setResult(JacksonUtils.toJson(result));
            info.setSucess(true);
            info.setMsg("更新对象成功!");
        } catch (Exception e) {
            log.error("更新对象失败!"+e.getMessage());
            info.setSucess(false);
            info.setMsg("更新对象失败!");
            info.setExceptionMsg(e.getMessage());
        }
        return JacksonUtils.toJson(info);
    }

    @Override
    public String deleteObjectById(String userInfo, String deleteJson)
    {
        // TODO Auto-generated method stub
        DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
            AttachmentTemp attachmentTemp=JacksonUtils.fromJson(deleteJson, AttachmentTemp.class);
            int result= attachmentTempService.deleteObjectById(attachmentTemp.getId());
            info.setResult(JacksonUtils.toJson(result));
            info.setSucess(true);
            info.setMsg("删除对象成功!");
        } catch (Exception e) {
            log.error("更新对象失败!"+e.getMessage());
            info.setSucess(false);
            info.setMsg("删除更新对象失败!");
            info.setExceptionMsg(e.getMessage());
        }
        return JacksonUtils.toJson(info);
    }

    @Override
    public String deleteAllObjectByIds(String userInfo, String deleteJsonList)
    {
        // TODO Auto-generated method stub
        DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
            if (StringUtils.isNotBlank(deleteJsonList)) {
                Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
                List<String> list=Arrays.asList(map.get("id").toString().split(","));
                int result= attachmentTempService.deleteAllObjectByIds(list);
                info.setResult(JacksonUtils.toJson(result));
                info.setSucess(true);
                info.setMsg("删除对象成功!");
            }
        } catch (Exception e) {
            log.error("删除对象失败!"+e.getMessage());
            info.setSucess(false);
            info.setMsg("删除更新对象失败!");
            info.setExceptionMsg(e.getMessage());
        }
        return JacksonUtils.toJson(info);
    }

    @Override
    public String getObjectById(String userInfo, String getJson)
    {
        // TODO Auto-generated method stub
        DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
            AttachmentTemp attachmentTemp=JacksonUtils.fromJson(getJson, AttachmentTemp.class);
            AttachmentTemp	result = attachmentTempService.getObjectById(attachmentTemp.getId());
            info.setResult(JacksonUtils.toJson(result));
            info.setSucess(true);
            info.setMsg("获取对象成功!");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("获取对象失败!"+e.getMessage());
            info.setSucess(false);
            info.setMsg("获取对象失败!");
            info.setExceptionMsg(e.getMessage());
        }
        return JacksonUtils.toJson(info);
    }

    @Override
    public String getPage(String userInfo, String paramater) {
        // TODO Auto-generated method stub
        DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
            if(StringUtils.isNotBlank(paramater)){
                Map map=JacksonUtils.fromJson(paramater, HashMap.class);
                Page page=attachmentTempService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
                info.setResult(JacksonUtils.toJson(page));
                info.setSucess(true);
                info.setMsg("获取分页对象成功!");
            }else{
                Page page=attachmentTempService.getPage(new HashMap(), null, null);
                info.setResult(JacksonUtils.toJson(page));
                info.setSucess(true);
                info.setMsg("获取分页对象成功!");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("获取分页对象失败!"+e.getMessage());
            info.setSucess(false);
            info.setMsg("获取分页对象失败!");
            info.setExceptionMsg(e.getMessage());
        }
        return JacksonUtils.toJson(info);
    }

    @Override
    public String queryList(String userInfo, String paramater){
        // TODO Auto-generated method stub
        DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
            if(StringUtils.isNotBlank(paramater)){
                Map map=JacksonUtils.fromJson(paramater, HashMap.class);
                List list=attachmentTempService.queryList(map);
                info.setResult(JacksonUtils.toJson(list));
                info.setSucess(true);
                info.setMsg("获取列表对象成功!");
            }else{
                List list=attachmentTempService.queryList(null);
                info.setResult(JacksonUtils.toJson(list));
                info.setSucess(true);
                info.setMsg("获取列表对象成功!");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("获取列表对象失败!"+e.getMessage());
            info.setSucess(false);
            info.setMsg("获取列表对象失败!");
            info.setExceptionMsg(e.getMessage());
        }
        return JacksonUtils.toJson(info);
    }

    @Override
    public String getCount(String userInfo, String paramater)  {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String deletePseudoObjectById(String userInfo, String deleteJson)
    {
        // TODO Auto-generated method stub
        DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
            AttachmentTemp attachmentTemp=JacksonUtils.fromJson(deleteJson, AttachmentTemp.class);
            int result= attachmentTempService.deletePseudoObjectById(attachmentTemp.getId());
            info.setResult(JacksonUtils.toJson(result));
            info.setSucess(true);
            info.setMsg("删除对象成功!");
        } catch (Exception e) {
            log.error("更新对象失败!"+e.getMessage());
            info.setSucess(false);
            info.setMsg("删除更新对象失败!");
            info.setExceptionMsg(e.getMessage());
        }
        return JacksonUtils.toJson(info);
    }

    @Override
    public String deletePseudoAllObjectByIds(String userInfo, String deleteJsonList)
    {
        // TODO Auto-generated method stub
        DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
            if (StringUtils.isNotBlank(deleteJsonList)) {
                Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
                List<String> list=Arrays.asList(map.get("id").toString().split(","));
                int result= attachmentTempService.deletePseudoAllObjectByIds(list);
                info.setResult(JacksonUtils.toJson(result));
                info.setSucess(true);
                info.setMsg("删除对象成功!");
            }
        } catch (Exception e) {
            log.error("删除对象失败!"+e.getMessage());
            info.setSucess(false);
            info.setMsg("删除更新对象失败!");
            info.setExceptionMsg(e.getMessage());
        }
        return JacksonUtils.toJson(info);
    }


}
