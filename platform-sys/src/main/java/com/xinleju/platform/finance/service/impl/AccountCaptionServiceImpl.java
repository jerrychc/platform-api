package com.xinleju.platform.finance.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.finance.dao.AccountCaptionDao;
import com.xinleju.platform.finance.dto.AccountCaptionDto;
import com.xinleju.platform.finance.entity.AccountCaption;
import com.xinleju.platform.finance.service.AccountCaptionService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class AccountCaptionServiceImpl extends  BaseServiceImpl<String,AccountCaption> implements AccountCaptionService{
	

	@Autowired
	private AccountCaptionDao accountCaptionDao;

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.service.AccountCaptionService#getAccountCaptionTree()
	 */
	@Override
	public List<AccountCaptionDto> getAccountCaptionTree(Map map)throws Exception {
		List<AccountCaptionDto> list =new ArrayList<AccountCaptionDto>();
		List<AccountCaption> accountCaptionList =accountCaptionDao.getAccountCaptionList(map);
		List<String> parentIds =accountCaptionDao.getAccountCaptionParentIdsList(map);
		for (AccountCaption accountCaption : accountCaptionList) {
			AccountCaptionDto accountCaptionDto =new AccountCaptionDto();
			BeanUtils.copyProperties(accountCaption, accountCaptionDto);
			   String sort = accountCaptionDto.getSort();
				  String[] split = sort.split("-");
				  Long i=(long) split.length;
				  accountCaptionDto.setLevel(i);
				  accountCaptionDto.setExpanded(true);
				  accountCaptionDto.setLoaded(true);
		    	  String id = accountCaptionDto.getId();
		    	   if(parentIds.contains(id)){
		    			 accountCaptionDto.setIsLeaf(false);
		    		 }else{
		    			 accountCaptionDto.setIsLeaf(true); 
		    		 };
		    		list.add(accountCaptionDto);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.service.AccountCaptionService#queryCaptionList(java.lang.Object)
	 */
	@Override
	public List queryCaptionList(Map map) throws Exception {
		return accountCaptionDao.getAccountCaptionList(map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.service.AccountCaptionService#deleteAccountCaptionById(java.lang.String)
	 */
	@Override
	public int deleteAccountCaptionById(String id) throws Exception {
		 
		List<String> ids= accountCaptionDao.getAccountCaptionListById(id);
		return accountCaptionDao.deletePseudoAllObjectByIds(ids);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.service.AccountCaptionService#saveAccountCaption(com.xinleju.platform.finance.entity.AccountCaption)
	 */
	@Override
	public int saveAccountCaption(AccountCaption accountCaption)
			throws Exception {
		String parentId = accountCaption.getParentId();
		Map<String,Object>map=new HashMap<String, Object>();
		map.put("parentId", parentId);
		map.put("accountSetId", accountCaption.getAccountSetId());
		List<AccountCaption> list=accountCaptionDao.getAccountCaptionListByParentId(map);
		if(list!=null&&list.size()>0){
			
			String sort = list.get(list.size()-1).getSort();
			if(parentId.equals("")){
			/*	String[] s =sort.split("\\$");*/
				 int oldNumber =Integer.parseInt(sort);
				 int newNumber=(oldNumber+1);
				 String newSort= String.format("%04d", newNumber); 
				 accountCaption.setSort(newSort);
				 accountCaption.setPrefixId(accountCaption.getId());
			}else{
			 int i=sort.lastIndexOf("-");
			 String parentSort = sort.substring(0,i);
			 String oldSort = sort.substring(i+1,sort.length());
			 int oldNumber =Integer.parseInt(oldSort);
			 int newNumber=(oldNumber+1);
			 String newSort=parentSort+"-"+ String.format("%04d", newNumber); 
			 accountCaption.setSort(newSort);
			 AccountCaption parentObj = accountCaptionDao.getObjectById(parentId);
			 accountCaption.setPrefixId(parentObj.getPrefixId()+"-"+accountCaption.getId());
			}
		}else{
			if(parentId.equals("")){
				accountCaption.setSort("0001");
				 accountCaption.setPrefixId(accountCaption.getId());
			}else{
				AccountCaption parentAccountCaption = accountCaptionDao.getObjectById(parentId);
				String parentSort =parentAccountCaption.getSort();
				 String newSort=parentSort+"-"+"0001";
				 accountCaption.setSort(newSort);
				 AccountCaption parentObj = accountCaptionDao.getObjectById(parentId);
				 accountCaption.setPrefixId(parentObj.getPrefixId()+"-"+accountCaption.getId());
			}
		}
		return	accountCaptionDao.save(accountCaption);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.service.AccountCaptionService#updateAccountCaption(com.xinleju.platform.finance.entity.AccountCaption)
	 */
	@Override
	public int updateAccountCaption(AccountCaption accountCaption)
			throws Exception {
		AccountCaption oldAccountCaption = accountCaptionDao.getObjectById(accountCaption.getId());
		String rePrefixId = oldAccountCaption.getPrefixId();
		 String fullOldSort = oldAccountCaption.getSort();
		 if(oldAccountCaption.getParentId()==null){
			 oldAccountCaption.setParentId("");
		 }
		if(oldAccountCaption.getParentId().equals(accountCaption.getParentId())){
			accountCaptionDao.update(accountCaption);
		}else{
				String parentId = accountCaption.getParentId();
				Map<String,Object>map=new HashMap<String, Object>();
				map.put("parentId", parentId);
				map.put("accountSetId", accountCaption.getAccountSetId());
				List<AccountCaption> list=accountCaptionDao.getAccountCaptionListByParentId(map);
				if(list!=null&&list.size()>0){
					String sort = list.get(list.size()-1).getSort();
					if(parentId.equals("")){
				/*		String[] s =sort.split("\\$");*/
						 int oldNumber =Integer.parseInt(sort);
						 int newNumber=(oldNumber+1);
						 String newSort= String.format("%04d", newNumber); 
						 accountCaption.setSort(newSort);
						 accountCaption.setPrefixId(accountCaption.getId());
							Map<String,Object>newMap=new HashMap<String, Object>();
							newMap.put("prefixId", rePrefixId+"-");
							newMap.put("accountSetId", oldAccountCaption.getAccountSetId());
							List<AccountCaption> chirdList = accountCaptionDao.getAccountCaptionList(newMap);
							if(chirdList!=null&&chirdList.size()>0){//修改子级的sort 和 prefixId
								for (AccountCaption accountCaptionChird : chirdList) {
									String chirdOldSort = accountCaptionChird.getSort();
									String replaceSort = chirdOldSort.replaceFirst(fullOldSort, newSort);
									String chirdPrefixId = accountCaptionChird.getPrefixId();
									String replacePrefixId = chirdPrefixId.replace(rePrefixId, accountCaption.getId());
									accountCaptionChird.setSort(replaceSort);
									accountCaptionChird.setPrefixId(replacePrefixId);
									accountCaptionDao.update(accountCaptionChird);
								}
							}
							accountCaptionDao.update(accountCaption);
					}else{
						 int i=sort.lastIndexOf("-");
						 String parentSort = sort.substring(0,i);
						 String oldSort = sort.substring(i+1,sort.length());
						 int oldNumber =Integer.parseInt(oldSort);
						 int newNumber=(oldNumber+1);
						 String newSort=parentSort+"-"+ String.format("%04d", newNumber); 
						 accountCaption.setSort(newSort);
						 AccountCaption parentObject = accountCaptionDao.getObjectById(parentId);//
						 AccountCaption oldParentObject = accountCaptionDao.getObjectById(oldAccountCaption.getParentId());
						 String newPrefixId=parentObject.getPrefixId()+"-"+accountCaption.getId();//a-b-c
						 accountCaption.setPrefixId(newPrefixId);
				
							Map<String,Object>newMap=new HashMap<String, Object>();
							newMap.put("prefixId", rePrefixId+"-");
							newMap.put("accountSetId", oldAccountCaption.getAccountSetId());
							List<AccountCaption> chirdList = accountCaptionDao.getAccountCaptionList(newMap);
							if(chirdList!=null&&chirdList.size()>0){//修改子级的sort 和 prefixId
								for (AccountCaption accountCaptionChird : chirdList) {
									String chirdOldSort = accountCaptionChird.getSort();
									String replaceSort = chirdOldSort.replaceFirst(fullOldSort, newSort);
									String chirdPrefixId = accountCaptionChird.getPrefixId();
									String replacePrefixId = chirdPrefixId.replace(rePrefixId, newPrefixId);
									accountCaptionChird.setSort(replaceSort);
									accountCaptionChird.setPrefixId(replacePrefixId);
									accountCaptionDao.update(accountCaptionChird);
								}
							}
							accountCaptionDao.update(accountCaption);
					}
			}else{
				 AccountCaption parentObject = accountCaptionDao.getObjectById(parentId);
				 String parentSort= parentObject.getSort();
				 String parentPrefixId = parentObject.getPrefixId();
				 accountCaption.setSort(parentSort+"-"+"0001");
				 accountCaption.setPrefixId(parentPrefixId+"-"+accountCaption.getId());
				 String newSort=accountCaption.getSort();
				 String newPrefixId=accountCaption.getPrefixId();
				 Map<String,Object>newMap=new HashMap<String, Object>();
					newMap.put("prefixId", rePrefixId+"-");
					newMap.put("accountSetId", oldAccountCaption.getAccountSetId());
					List<AccountCaption> chirdList = accountCaptionDao.getAccountCaptionList(newMap);
					if(chirdList!=null&&chirdList.size()>0){//修改子级的sort 和 prefixId
						for (AccountCaption accountCaptionChird : chirdList) {
							String chirdOldSort = accountCaptionChird.getSort();
							String replaceSort = chirdOldSort.replaceFirst(fullOldSort, newSort);
							String chirdPrefixId = accountCaptionChird.getPrefixId();
							String replacePrefixId = chirdPrefixId.replace(rePrefixId, newPrefixId);
							accountCaptionChird.setSort(replaceSort);
							accountCaptionChird.setPrefixId(replacePrefixId);
							accountCaptionDao.update(accountCaptionChird);
						}
					}
				 accountCaptionDao.update(accountCaption);
			}
		}
	return 1;
	}
	
	public List queryByBudCodeList(Map map)throws Exception{
		return accountCaptionDao.queryByBudCodeList(map);
	}
}
