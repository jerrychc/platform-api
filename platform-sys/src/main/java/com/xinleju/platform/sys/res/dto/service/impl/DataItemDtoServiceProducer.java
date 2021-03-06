package com.xinleju.platform.sys.res.dto.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.res.dto.service.DataItemDtoServiceCustomer;
import com.xinleju.platform.sys.res.entity.DataItem;
import com.xinleju.platform.sys.res.entity.DataPoint;
import com.xinleju.platform.sys.res.service.DataItemService;
import com.xinleju.platform.sys.res.service.DataPointService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */

public class DataItemDtoServiceProducer implements DataItemDtoServiceCustomer{
	private static Logger log = Logger.getLogger(DataItemDtoServiceProducer.class);
	@Autowired
	private DataItemService dataItemService;
	@Autowired
	private DataPointService dataPointService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			DataItem dataItem=JacksonUtils.fromJson(saveJson, DataItem.class);
			dataItemService.save(dataItem);
			info.setResult(JacksonUtils.toJson(dataItem));
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
	/**
	 * 保存作用域控制对象，并校验appId+itemCode是否已存在
	 * @param saveJson 保存的对象
	 * @throws Exception
	 */
	@Override
	public String saveDataItem(String userInfo, String saveJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			DataItem dataItem=JacksonUtils.fromJson(saveJson, DataItem.class);
			if(StringUtils.isBlank(dataItem.getAppId())){
				throw new InvalidCustomException("作用域不可为空");
			}
			if(StringUtils.isBlank(dataItem.getItemCode())){
				throw new InvalidCustomException("业务对象编码不可为空");
			}
			//校验某系统下的作用域编码是否已存在
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("appId", dataItem.getAppId());
			param.put("itemCode", dataItem.getItemCode());
			Integer c=dataItemService.checkAppIdAndItemCode(param);
			if(c!=null&&c>0){
				throw new InvalidCustomException("该系统下此业务编码已存在，不可重复");
			}
			//保存数据
			dataItemService.save(dataItem);
			info.setResult(JacksonUtils.toJson(dataItem));
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
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			DataItem dataItem=JacksonUtils.fromJson(updateJson, DataItem.class);
			if(StringUtils.isBlank(dataItem.getAppId())){
				throw new InvalidCustomException("作用域不可为空");
			}
			if(StringUtils.isBlank(dataItem.getItemCode())){
				throw new InvalidCustomException("业务对象编码不可为空");
			}
			//校验某系统下的作用域编码是否已存在 add by gyh
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("appId", dataItem.getAppId());
			param.put("itemCode", dataItem.getItemCode());
			param.put("id", dataItem.getId());				
			Integer c=dataItemService.checkAppIdAndItemCode(param);
			if(c!=null&&c>0){
				throw new InvalidCustomException("该系统下此业务编码已存在，不可重复");
			}
			int result=   dataItemService.update(dataItem);
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
			DataItem dataItem=JacksonUtils.fromJson(deleteJson, DataItem.class);
			int result= dataItemService.deleteObjectById(dataItem.getId());
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
				int result= dataItemService.deleteAllObjectByIds(list);
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
			DataItem dataItem=JacksonUtils.fromJson(getJson, DataItem.class);
			DataItem	result = dataItemService.getObjectById(dataItem.getId());
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
				Page page=dataItemService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			}else{
				Page page=dataItemService.getPage(new HashMap(), null, null);
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
				List list=dataItemService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=dataItemService.queryList(null);
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

	/**
	 * 查询作用域业务对象及其控制点 add by gyh
	 * @param paramater 查询参数
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryDataItemAndPointList(String userInfo, String paramater){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<Map<String,Object>> list=dataItemService.queryDataItemAndPointList(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List<Map<String,Object>> list=dataItemService.queryDataItemAndPointList(null);
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
	/**
	 * 查询作用域业务对象及其控制点和数据授权情况 add by gyh
	 * @param paramater 查询参数
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryDataItemAndPointObjList(String userInfo, String paramater){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map<String,Object> param=JacksonUtils.fromJson(paramater, HashMap.class);
				List<Map<String,Object>> dataItemlist=dataItemService.queryDataItemAndPointAuthList(param);
				info.setResult(JacksonUtils.toJson(dataItemlist));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List<Map<String,Object>> list=dataItemService.queryDataItemAndPointAuthList(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			//e.printStackTrace();
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
	public String deletePseudoObjectById(String userInfo, String deleteJson) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deletePseudoAllObjectByIds(String userInfo,
			String deleteJsonList) {
		// TODO Auto-generated method stub
		return null;
	}

	//校验appId+itemCode是否已存在，不可重复 add by gyh 
	@Override
	public String checkRepeat(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				DataItem dataItem=JacksonUtils.fromJson(paramater, DataItem.class);
				if(StringUtils.isBlank(dataItem.getAppId())){
					throw new InvalidCustomException("作用域不可为空");
				}
				if(StringUtils.isBlank(dataItem.getItemCode())){
					throw new InvalidCustomException("业务对象编码不可为空");
				}
				//校验某系统下的作用域编码是否已存在
				Map<String,Object> param=new HashMap<String,Object>();
				param.put("appId", dataItem.getAppId());
				param.put("itemCode", dataItem.getItemCode());
				param.put("id", dataItem.getId());				
				Integer c=dataItemService.checkAppIdAndItemCode(param);
				if(c!=null&&c>0){
					throw new InvalidCustomException("该系统下此业务编码已存在，不可重复");
				}
				info.setResult(paramater);
				info.setSucess(true);
				info.setMsg("该编码不重复，可以使用");
			}else{
				throw new InvalidCustomException("参数不可为空");
			}
		}  catch (Exception e) {
			log.error("checkRepeat失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("checkRepeat失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
}
