package com.xinleju.platform.sys.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.dao.CustomArchivesItemDao;
import com.xinleju.platform.sys.base.dto.CustomArchivesItemDto;
import com.xinleju.platform.sys.base.entity.CustomArchivesItem;
import com.xinleju.platform.sys.base.service.CustomArchivesItemService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 * 
 */

@Service
public class CustomArchivesItemServiceImpl extends  BaseServiceImpl<String,CustomArchivesItem> implements CustomArchivesItemService{
	

	@Autowired
	private CustomArchivesItemDao customArchivesItemDao;

	@Override
	public Integer getCurrentMaxSortByMainId(String mainId) {
		return customArchivesItemDao.getCurrentMaxSortByMainId(mainId);
	}

	@Override
	public List queryListSort(Map<String, Object> map) {
		return customArchivesItemDao.queryListSort(map);
	}

	@Override
	public Integer validateIsExist(CustomArchivesItem customArchivesItem, String type) {
		return customArchivesItemDao.validateIsExist(customArchivesItem,type);
	}

	@Override
	public DubboServiceResultInfo saveList(String userInfo, String saveJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		CustomArchivesItemDto customArchivesItemDto=JacksonUtils.fromJson(saveJson, CustomArchivesItemDto.class);
		List<CustomArchivesItemDto> customArchivesItemList=customArchivesItemDto.getCustomItemList();
		List<CustomArchivesItem> itemListAdd=new ArrayList<CustomArchivesItem>();
		List<CustomArchivesItem> itemListUpdate=new ArrayList<CustomArchivesItem>();
		Set<String> codeSet=new HashSet<String>();
		Set<String> nameSet=new HashSet<String>();
		Integer delCount=0;
		if(customArchivesItemList!=null && customArchivesItemList.size()>0){
			for(int i=0;i<customArchivesItemList.size();i++){
				CustomArchivesItemDto customArchivesItemDtoChild=customArchivesItemList.get(i);
				if(customArchivesItemDtoChild.getDelflag()){
					delCount++;
				}else{
					codeSet.add(customArchivesItemDtoChild.getCode());
					nameSet.add(customArchivesItemDtoChild.getName());
				}
				CustomArchivesItem customArchivesItem=new CustomArchivesItem();
				BeanUtils.copyProperties(customArchivesItemDtoChild, customArchivesItem);
				CustomArchivesItem customArchivesItemDb=customArchivesItemDao.getObjectById(customArchivesItem.getId());
				customArchivesItem.setSort(Long.valueOf(i+1));
				if(customArchivesItemDb==null){
					itemListAdd.add(customArchivesItem);
				}else{
					customArchivesItem.setConcurrencyVersion(customArchivesItemDb.getConcurrencyVersion());
					itemListUpdate.add(customArchivesItem);
				}
			}
			if(codeSet.size()!=customArchivesItemList.size()-delCount || nameSet.size()!=customArchivesItemList.size()-delCount){
				info.setResult("false");
				info.setSucess(true);
				info.setMsg("名称或编码重复，请重新输入!");
			}else{
				customArchivesItemDao.saveBatch(itemListAdd);
				customArchivesItemDao.updateBatch(itemListUpdate);
				info.setResult("true");
				info.setSucess(true);
				info.setMsg("批量操作成功!");
			}
		}
		return info;
	}

	@Override
	public String validateBeforeSave(String userInfo, String saveJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		CustomArchivesItemDto customArchivesItemDtoVal=this.validateIsExist(saveJson);
		try {
			if(customArchivesItemDtoVal.isCodeExist() || customArchivesItemDtoVal.isNameExist()){
				info.setResult(JacksonUtils.toJson(customArchivesItemDtoVal));
				info.setSucess(true);
				info.setMsg("编码或名称重复！");
			}else{
				CustomArchivesItem customArchivesItem=JacksonUtils.fromJson(saveJson, CustomArchivesItem.class);
				customArchivesItem.setId(IDGenerator.getUUID());
				Integer sort=customArchivesItemDao.getCurrentMaxSortByMainId(customArchivesItem.getMainId());
				customArchivesItem.setSort(sort==null?1:Long.valueOf(sort+1));
				customArchivesItemDao.save(customArchivesItem);
				BeanUtils.copyProperties(customArchivesItem, customArchivesItemDtoVal);
				info.setResult(JacksonUtils.toJson(customArchivesItemDtoVal));
				info.setSucess(true);
				info.setMsg("保存成功！");
			}
		} catch (DataAccessException e) {
			//e.printStackTrace();
			info.setSucess(false);
			info.setMsg("保存失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	private CustomArchivesItemDto validateIsExist(String saveJson) {
		CustomArchivesItem customArchivesItem=JacksonUtils.fromJson(saveJson, CustomArchivesItem.class);
		Integer isExistCode=customArchivesItemDao.validateIsExist(customArchivesItem,"code");
		Integer isExistName=customArchivesItemDao.validateIsExist(customArchivesItem,"name");
		CustomArchivesItemDto customArchivesItemDto = new CustomArchivesItemDto();
		if(isExistCode !=null && isExistCode>0){
			customArchivesItemDto.setCodeExist(true);
		}
		if(isExistName !=null && isExistName>0){
			customArchivesItemDto.setNameExist(true);
		}
		return customArchivesItemDto;
	}

	@Override
	public String validateBeforeUpdate(String userInfo, String updateJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		CustomArchivesItemDto customArchivesItemDtoVal=this.validateIsExist(updateJson);
		try {
			if(customArchivesItemDtoVal.isCodeExist() || customArchivesItemDtoVal.isNameExist()){
				info.setResult(JacksonUtils.toJson(customArchivesItemDtoVal));
				info.setSucess(true);
				info.setMsg("编码或名称重复！");
			}else{
				CustomArchivesItem customArchivesItem=JacksonUtils.fromJson(updateJson, CustomArchivesItem.class);
				int result= customArchivesItemDao.update(customArchivesItem);
				BeanUtils.copyProperties(customArchivesItem, customArchivesItemDtoVal);
				info.setResult(JacksonUtils.toJson(customArchivesItemDtoVal));
				info.setSucess(true);
				info.setMsg("更新对象成功!");
			}
		} catch (DataAccessException e) {
			//e.printStackTrace();
			info.setSucess(false);
			info.setMsg("保存失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public Page getPageSort(Map map) {
		// 分页显示
		Page page=new Page();
		// 获取分页list 数据
		List<Map<String,Object>> list = customArchivesItemDao.getPageSort(map);
		// 获取条件的总数据量
		Integer count = customArchivesItemDao.getPageSortCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		//封装成page对象 传到前台
		return page;
	}

	@Override
	public int updateCustomArchives(String id) {
		return customArchivesItemDao.updateCustomArchives(id);
	}

}
