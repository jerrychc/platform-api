package com.xinleju.platform.sys.base.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.lang.Integer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.dao.BaseCorporationDao;
import com.xinleju.platform.sys.base.dto.BaseCorporationAccontDto;
import com.xinleju.platform.sys.base.dto.BaseCorporationDto;
import com.xinleju.platform.sys.base.entity.BaseCorporation;
import com.xinleju.platform.sys.base.entity.BaseCorporationAccont;
import com.xinleju.platform.sys.base.service.BaseCorporationAccontService;
import com.xinleju.platform.sys.base.service.BaseCorporationService;
import com.xinleju.platform.sys.base.utils.StatusType;

/**
 * @author admin
 * 
 * 
 */

@Service
public class BaseCorporationServiceImpl extends  BaseServiceImpl<String,BaseCorporation> implements BaseCorporationService{
	

	@Autowired
	private BaseCorporationDao baseCorporationDao;
	@Autowired
	private BaseCorporationAccontService baseCorporationAccontService;
	/**
	 * author:liuf
	 * describe: 根据id查询主子表
	 * param:id
	 */
	@Override
	public BaseCorporationDto getCorporationAndAccontById(String id) throws Exception {
		
		BaseCorporationDto baseCorporationDto =new BaseCorporationDto();
		//查询主表
		BaseCorporation baseCorporation = baseCorporationDao.getObjectById(id);
		BeanUtils.copyProperties(baseCorporation,baseCorporationDto);
		//查询子表
		List<BaseCorporationAccont>  baseCorporationAccontList=    baseCorporationAccontService.getBaseCorporationAccontListByCorporationId(id);
		List<BaseCorporationAccontDto> corporationAccontDtoList=new ArrayList<>();
		if(baseCorporationAccontList!=null&&baseCorporationAccontList.size()>0){
	    	for (BaseCorporationAccont baseCorporationAccont : baseCorporationAccontList) {
	    		BaseCorporationAccontDto baseCorporationAccontDto=new BaseCorporationAccontDto();
	    		BeanUtils.copyProperties(baseCorporationAccont,baseCorporationAccontDto);
	    		corporationAccontDtoList.add(baseCorporationAccontDto);
			}
	     }
		//拼接主子表
		baseCorporationDto.setList(corporationAccontDtoList);
    	return baseCorporationDto;
		
	}
	/**
	 * author:liuf
	 * describe: 级联保存
	 * param:baseCorporationDto
	 */
	@Override
	public void saveCorporationAndAccont(BaseCorporationDto baseCorporationDto) throws Exception {
		List<BaseCorporationAccontDto> list = baseCorporationDto.getList();
		List<BaseCorporationAccont> baseCorporationAccontList =new ArrayList<>();
		 // 循环dto对象 拿到list子表 把子表dto转化为实体对象 然后封装到list集合里面
		if(list!=null&&list.size()>0){
		for (BaseCorporationAccontDto baseCorporationAccontDto : list) {
			BaseCorporationAccont baseCorporationAccont=new BaseCorporationAccont();
			BeanUtils.copyProperties(baseCorporationAccontDto,baseCorporationAccont);
			baseCorporationAccontList.add(baseCorporationAccont);
		}
		}  //批量保存
		baseCorporationAccontService.saveBatch(baseCorporationAccontList);
		BaseCorporation baseCorporation=new BaseCorporation();
		BeanUtils.copyProperties(baseCorporationDto,baseCorporation);
		baseCorporationDao.save(baseCorporation);
	}
	/**
	 * author:liuf
	 * describe: 删除（单条数据）
	 * param:baseCorporationDto
	 */
	@Override
	public int deleteCorporationAndAccontByCorporationId(String id)throws Exception {
		// TODO Auto-generated method stub
		BaseCorporationDto baseCorporationDto = this.getCorporationAndAccontById(id);
		List<BaseCorporationAccontDto> list = baseCorporationDto.getList();
		List<String> ids = new ArrayList<>();
		if(list!=null&&list.size()>0){
		for (BaseCorporationAccontDto baseCorporationAccontDto : list) {
			BaseCorporationAccont baseCorporationAccont=new BaseCorporationAccont();
			BeanUtils.copyProperties(baseCorporationAccontDto,baseCorporationAccont);
			ids.add(baseCorporationAccont.getId());
		}
		}
		baseCorporationAccontService.deletePseudoAllObjectByIds(ids);
		BaseCorporation   baseCorporation =new BaseCorporation();
		BeanUtils.copyProperties(baseCorporationDto,baseCorporation);
		int i = baseCorporationDao.deletePseudoObjectById(id);
		return i;
	}
	/**
	 * author:liuf
	 * describe: 级联修改
	 * param:baseCorporationDto
	 */
	@Override
	public int updateCorporationAndAccont(BaseCorporationDto baseCorporationDto)
			throws Exception {
		//获取新的子表dto
		List<BaseCorporationAccontDto> list = baseCorporationDto.getList();
		List<BaseCorporationAccont> savebaseCorporationAccont=new ArrayList<>();
		List<BaseCorporationAccont> updatebaseCorporationAccont=new ArrayList<>();
		if(list!=null&&list.size()>0){
		for(BaseCorporationAccontDto baseCorporationAccontDto:list){
			BaseCorporationAccont newBaseCorporationAccont= new BaseCorporationAccont();
			BeanUtils.copyProperties(baseCorporationAccontDto, newBaseCorporationAccont);
			BaseCorporationAccont oldbaseCorporationAccont = baseCorporationAccontService.getObjectById(newBaseCorporationAccont.getId());
			if(oldbaseCorporationAccont==null){
				savebaseCorporationAccont.add(newBaseCorporationAccont);
			}else{
				updatebaseCorporationAccont.add(newBaseCorporationAccont);
			}
		}
		}
		baseCorporationAccontService.saveBatch(savebaseCorporationAccont);
		baseCorporationAccontService.updateBatch(updatebaseCorporationAccont);
		BaseCorporation baseCorporation=new BaseCorporation();
		BeanUtils.copyProperties(baseCorporationDto, baseCorporation);
		int result = baseCorporationDao.update(baseCorporation);
		return result;
	}
	/**
	 * author:liuf
	 * describe:修改对象的状态
	 * param:baseCorporationDtoBean
	 */
	@Override
	public int updateStatus(BaseCorporationDto baseCorporationDtoBean)
			throws Exception {
		BaseCorporation baseCorporation=new BaseCorporation();
		BeanUtils.copyProperties(baseCorporationDtoBean, baseCorporation);
		int i=0;
		String status = baseCorporationDtoBean.getStatus();
		if(StatusType.StatusOpen.getCode().equals(status)){//启用状态改为禁用
			baseCorporation.setStatus(StatusType.StatusClosed.getCode());
			baseCorporation.setDisabledId("");
			baseCorporation.setDisabledDate(new Timestamp(System.currentTimeMillis()));
			 i = baseCorporationDao.update(baseCorporation);
		}else if(StatusType.StatusClosed.getCode().equals(status)){//禁用状态改为启用
			baseCorporation.setStatus(StatusType.StatusOpen.getCode());
			i=baseCorporationDao.update(baseCorporation);
		}
		return i;
	}
	/**
	 * author:liuf
	 * describe: 批量删除
	 * param:ids
	 */
	@Override
	public int deleteAllByIds(String ids) throws Exception {
		String[] split = ids.split(",");
		List<String> newAcountIds=new ArrayList<>();
		List<String> CorporationIds=new ArrayList<>();
		for (int i = 0; i < split.length; i++) {
			 BaseCorporationDto baseCorporationDto = this.getCorporationAndAccontById(split[i]);
			 List<BaseCorporationAccontDto> list = baseCorporationDto.getList();
			if(list!=null&&list.size()>0){
				for (BaseCorporationAccontDto baseCorporationAccontDto : list) {
					BaseCorporationAccont baseCorporationAccont =new BaseCorporationAccont();
					BeanUtils.copyProperties(baseCorporationAccontDto, baseCorporationAccont);
					newAcountIds.add(baseCorporationAccont.getId());
				}
			}
			CorporationIds.add(split[i]);
		}
		baseCorporationAccontService.deletePseudoAllObjectByIds(newAcountIds);
		int i = baseCorporationDao.deletePseudoAllObjectByIds(CorporationIds);
		return i;
	
	
	}
	/**
	 * author:liuf
	 * describe:分页查询
	 * param:map
	 */
	@Override
	public Page getAllListByPage(Map map)
			throws Exception {
		Page page=new Page();
		 List<Map<String,Object>> list = baseCorporationDao.getBaseCorporationList(map);
		 Integer count = baseCorporationDao.getBaseCorporationCount(map);
		 page.setLimit((Integer) map.get("limit"));
		  page.setList(list);
		  page.setStart((Integer) map.get("start"));
		  page.setTotal(count);
		  return page;
	}
	@Override
	public List<Map<String,Object>> getBaseCorporationList(Map  map)throws Exception{
		return baseCorporationDao.getBaseCorporationList(map);
	}
}