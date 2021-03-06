package com.xinleju.platform.sys.org.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.org.dto.service.PostUserDtoServiceCustomer;
import com.xinleju.platform.sys.org.entity.Post;
import com.xinleju.platform.sys.org.entity.PostUser;
import com.xinleju.platform.sys.org.entity.RoleUser;
import com.xinleju.platform.sys.org.service.PostUserService;
import com.xinleju.platform.sys.org.service.RoleUserService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class PostUserDtoServiceProducer implements PostUserDtoServiceCustomer{
	private static Logger log = Logger.getLogger(PostUserDtoServiceProducer.class);
	@Autowired
	private PostUserService postUserService;
	@Autowired
	private RoleUserService roleUserService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   PostUser postUser=JacksonUtils.fromJson(saveJson, PostUser.class);
		   postUserService.save(postUser);
		   info.setResult(JacksonUtils.toJson(postUser));
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
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(saveJsonList)) {
				Map map = JacksonUtils.fromJson(saveJsonList, HashMap.class);
				String userIds = (String)map.get("userIds");
				String postIds = (String)map.get("postIds");
				String uuids = (String)map.get("uuids");
				String type = (String)map.get("type");
				
				//一个用户多岗位保存
				if(type.equals("posts")){

					Map mapCon = new HashMap<>();
					//根据岗位Id查询出原先有的用户
					mapCon.put("userId", userIds);
					mapCon.put("delflag", false);
					List<PostUser>  listOld = postUserService.queryList(mapCon);
					//要删除的列表
					List<String> listDelIds = new ArrayList<String>();
					//要插入的列表
					List<String> listAddIds = new ArrayList<String>();
					String [] postIdsList = postIds.split(",");
					String [] uuidsList = uuids.split(",");
					//查询出数据库中的，而列表中没有的进行删除操作
					for(int i =0;i<listOld.size();i++){
						boolean isexist = false;
						PostUser postUserOld = listOld.get(i);
						for(int y =0;y<postIdsList.length;y++){
							if(postUserOld.getPostId().equals(postIdsList[y])){
								isexist = true;
							}
						}
						if(!isexist){
							listDelIds.add(postUserOld.getId());
						}
					}
					if(listDelIds.size()>0){
						postUserService.deletePseudoAllObjectByIds(listDelIds);
					}
					//查询列表中有的，并且数据库中没有的进行插入操作
					for(int y =0;y<postIdsList.length;y++){
						boolean isexist = false;
						for(int i =0;i<listOld.size();i++){
							PostUser postUserOld = listOld.get(i);
							if(postUserOld.getPostId().equals(postIdsList[y])){
								isexist = true;
							}
						}
						if(!isexist){
							listAddIds.add(postIdsList[y]);
						}
					}
					if(listAddIds.size()>0){
						for(int i =0;i<listAddIds.size();i++){
							PostUser postUser = new PostUser();
							postUser.setId(IDGenerator.getUUID());
							postUser.setUserId(userIds);
							postUser.setPostId(listAddIds.get(i));
							postUserService.save(postUser);
						}
					}
				//一个岗位多个用户保存
				}else if(type.equals("users")){
					Map mapCon = new HashMap<>();
					//根据岗位Id查询出原先有的用户
					mapCon.put("postId", postIds);
					mapCon.put("delflag", false);
					List<PostUser>  listOld = postUserService.queryList(mapCon);
					//要删除的列表
					List<String> listDelIds = new ArrayList<String>();
					//要插入的列表
					List<String> listAddIds = new ArrayList<String>();
					String [] userIdsList = userIds.split(",");
					String [] uuidsList = uuids.split(",");
					//查询出数据库中的，而列表中没有的进行删除操作
					for(int i =0;i<listOld.size();i++){
						boolean isexist = false;
						PostUser postUserOld = listOld.get(i);
						for(int y =0;y<userIdsList.length;y++){
							if(postUserOld.getUserId().equals(userIdsList[y])){
								isexist = true;
							}
						}
						if(!isexist){
							listDelIds.add(postUserOld.getId());
						}
					}
					if(listDelIds.size()>0){
						postUserService.deletePseudoAllObjectByIds(listDelIds);
					}
					//查询列表中有的，并且数据库中没有的进行插入操作
					for(int y =0;y<userIdsList.length;y++){
						boolean isexist = false;
						for(int i =0;i<listOld.size();i++){
							PostUser postUserOld = listOld.get(i);
							if(postUserOld.getUserId().equals(userIdsList[y])){
								isexist = true;
							}
						}
						if(!isexist){
							listAddIds.add(userIdsList[y]);
						}
					}
					if(listAddIds.size()>0){
						for(int i =0;i<listAddIds.size();i++){
							PostUser postUser = new PostUser();
							postUser.setId(IDGenerator.getUUID());
							postUser.setUserId(listAddIds.get(i));
							postUser.setPostId(postIds);
							postUserService.save(postUser);
						}
					}
				}
				info.setResult("保存成功");
				info.setSucess(true);
				info.setMsg("保存成功!");
			} else {
				info.setResult("保存失败");
				info.setSucess(true);
				info.setMsg("保存失败");
			}
		} catch (Exception e) {
			log.error("保存失败!" + e.getMessage());
			info.setResult("保存失败");
			info.setSucess(false);
			info.setMsg("保存失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
		// TODO Auto-generated method stub
	}
	
	@Override
	public String deleteAllObjectByUserOrPostIds(String userInfo, String deleteJsonList)
			 {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(deleteJsonList)) {
				Map map = JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				String type = (String)map.get("type");
				Map<String, Object> paramater = new HashMap<String, Object>();
				//一个用户多岗位  删除
				if(type.equals("posts")){
					String userIds = (String)map.get("userIds");
					List<String> postIds = (List<String>)map.get("postIds");
//					String [] postIdsList = postIds.split(",");
//					paramater.put("userId", userIds);
//					List<PostUser> listPostUser = postUserService.queryList(paramater);
//					List<String> listPostUserIds = new ArrayList<String>();
//					for(PostUser postUser:listPostUser){
//						for(String id:postIds){
//							if(postUser.getPostId().equals(id)){
//								listPostUserIds.add(postUser.getId());
//							}
//						}
//					}
					if(postIds.size()>0){
						postUserService.deleteAllObjectByIds(postIds);
					}
					if(postIds.size()>0){
						roleUserService.deleteAllObjectByIds(postIds);
					}
				//一个岗位多个用户 删除
				}else if(type.equals("users")){
					List<String> userIds = (List<String>)map.get("userIds");
					String postIds = (String)map.get("postIds");
//					String [] userIdsList = userIds.split(",");
					paramater.put("postId", postIds);
					List<PostUser> listPostUser = postUserService.queryList(paramater);
					List<String> listPostUserIds = new ArrayList<String>();
					for(PostUser postUser:listPostUser){
						for(String id:userIds){
							if(id.equals(postUser.getUserId())){
								listPostUserIds.add(postUser.getId());
							}
						}
					}
					if(listPostUserIds.size()>0){
						postUserService.deleteAllObjectByIds(listPostUserIds);
					}
				}
				
				
				info.setResult("批量删除成功");
				info.setSucess(true);
				info.setMsg("批量删除成功!");
			} else {
				info.setResult("批量删除失败");
				info.setSucess(true);
				info.setMsg("批量删除失败");
			}
		} catch (Exception e) {
			log.error("批量删除失败!" + e.getMessage());
			info.setResult("批量删除失败");
			info.setSucess(false);
			info.setMsg("批量删除失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
		// TODO Auto-generated method stub
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
			   PostUser postUser=JacksonUtils.fromJson(updateJson, PostUser.class);
			   int result=   postUserService.update(postUser);
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
			   PostUser postUser=JacksonUtils.fromJson(deleteJson, PostUser.class);
			   int result= postUserService.deleteObjectById(postUser.getId());
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
				   int result= postUserService.deleteAllObjectByIds(list);
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
			PostUser postUser=JacksonUtils.fromJson(getJson, PostUser.class);
			PostUser	result = postUserService.getObjectById(postUser.getId());
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
				Page page=postUserService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=postUserService.getPage(new HashMap(), null, null);
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
				List list=postUserService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=postUserService.queryList(null);
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

	/**
	 * 批量保存post_user和role_user
	 * @param objectList
	 */
	@Override
	public String savePostUserAndRoleUser(String userInfo, String jsonData) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(jsonData)){
				Map param=JacksonUtils.fromJson(jsonData, HashMap.class);
				postUserService.savePostUserBatch(param);
				info.setResult("保存成功");
				info.setSucess(true);
				info.setMsg("保存成功!");
			}else{
				info.setResult("保存失败");
				info.setSucess(true);
				info.setMsg("保存失败");
			}
		} catch (Exception e) {
			//e.printStackTrace();
			 log.error("保存失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("保存失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	/**
	 * 设置主岗
	 * @param objectList
	 */
	@Override
	public String setDefaultPost(String userInfo, String dataJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> map=JacksonUtils.fromJson(dataJson, HashMap.class);
			String id=map.get("id").toString();
			PostUser pu = postUserService.getObjectById(id);
			if(pu==null){
				throw new InvalidCustomException("不存在此记录");
			}
			//设置主岗，并把其他的设置为不是主岗
			int result=postUserService.setDefaultPost(map);
			
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

}
