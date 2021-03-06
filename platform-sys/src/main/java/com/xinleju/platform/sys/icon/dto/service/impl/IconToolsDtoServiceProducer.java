package com.xinleju.platform.sys.icon.dto.service.impl;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.icon.dto.service.IconToolsDtoServiceCustomer;
import com.xinleju.platform.sys.icon.entity.IconTools;
import com.xinleju.platform.sys.icon.service.IconToolsService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.tools.data.JacksonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ly on 2017/12/1.
 */
public class IconToolsDtoServiceProducer implements IconToolsDtoServiceCustomer {
    private static Logger log = Logger.getLogger(IconToolsDtoServiceProducer.class);
    @Autowired
    private IconToolsService iconToolsService;

    public String save(String userInfo, String saveJson){
        // TODO Auto-generated method stub
        DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
            IconTools iconTools= JacksonUtils.fromJson(saveJson, IconTools.class);
            //校验编码重复
            Map<String,Object> param=new HashMap<String,Object>();
            param.put("code", iconTools.getCode());
            Integer isc=iconToolsService.getCodeCount(param);
            if(isc>0){
                info.setSucess(false);
                info.setMsg("编码重复!");
            }else{
                iconToolsService.save(iconTools);
                info.setResult(JacksonUtils.toJson(iconTools));
                info.setSucess(true);
                info.setMsg("保存对象成功!");
            }
        } catch (Exception e) {
            log.error("保存对象失败!"+e.getMessage());
            info.setSucess(false);
            info.setMsg("保存对象失败!");
            info.setExceptionMsg(e.getMessage());
        }
        return JacksonUtils.toJson(info);
    }

    @Override
    public String saveBatch(String userInfo, String saveJsonList)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String updateBatch(String userInfo, String updateJsonList)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String update(String userInfo, String updateJson)  {
        DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
            IconTools iconTools=JacksonUtils.fromJson(updateJson, IconTools.class);
            //校验编码重复
            Map<String,Object> param=new HashMap<String,Object>();
            param.put("code", iconTools.getCode());
            param.put("id", iconTools.getId());
            Integer isc=iconToolsService.getCodeCount(param);
            if(isc>0){
                info.setSucess(false);
                info.setMsg("编码重复!");
            }else {
                int result = iconToolsService.update(iconTools);
                info.setResult(JacksonUtils.toJson(result));
                info.setSucess(true);
                info.setMsg("更新对象成功!");
            }
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
            IconTools iconTools=JacksonUtils.fromJson(deleteJson, IconTools.class);
            int result= iconToolsService.deleteObjectById(iconTools.getId());
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
                List<String> list= Arrays.asList(map.get("id").toString().split(","));
                int result= iconToolsService.deleteAllObjectByIds(list);
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
            IconTools iconTools=JacksonUtils.fromJson(getJson, IconTools.class);
            IconTools	result = iconToolsService.getObjectById(iconTools.getId());
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
                @SuppressWarnings("unchecked")
                Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
                Page page=iconToolsService.getDataByPage(map);
                info.setResult(JacksonUtils.toJson(page));
                info.setSucess(true);
                info.setMsg("获取分页对象成功!");
            }else{
                Page page=iconToolsService.getPage(new HashMap<String,Object>(), null, null);
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
                List list=iconToolsService.queryList(map);
                info.setResult(JacksonUtils.toJson(list));
                info.setSucess(true);
                info.setMsg("获取列表对象成功!");
            }else{
                List list=iconToolsService.queryList(null);
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
            IconTools iconTools=JacksonUtils.fromJson(deleteJson, IconTools.class);
            int result= iconToolsService.deletePseudoObjectById(iconTools.getId());
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
                int result= iconToolsService.deletePseudoAllObjectByIds(list);
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
