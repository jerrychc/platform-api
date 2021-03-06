package com.xinleju.platform.sys.org.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.corba.se.spi.ior.ObjectKey;
import com.xinleju.platform.sys.org.dto.OrgnazationExcelDto;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.encrypt.EndecryptUtil;
import com.xinleju.platform.sys.org.dto.OrgnazationNodeDto;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.sys.org.dto.service.UserDtoServiceCustomer;
import com.xinleju.platform.sys.org.entity.Orgnazation;
import com.xinleju.platform.sys.org.entity.PostUser;
import com.xinleju.platform.sys.org.entity.User;
import com.xinleju.platform.sys.org.service.OrgnazationService;
import com.xinleju.platform.sys.org.service.PostService;
import com.xinleju.platform.sys.org.service.PostUserService;
import com.xinleju.platform.sys.org.service.RootService;
import com.xinleju.platform.sys.org.service.UserService;
import com.xinleju.platform.sys.org.utils.EncryptionUtils;
import com.xinleju.platform.sys.res.entity.AppSystem;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.sys.sync.service.SyncDataService;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.utils.SyncDataThread;

/**
 * @author sy
 * 
 *
 */

public class UserDtoServiceProducer implements UserDtoServiceCustomer{
	private static Logger log = Logger.getLogger(UserDtoServiceProducer.class);
	@Autowired
	private UserService userService;

	@Autowired
	private RootService rootService;

	@Autowired
	private OrgnazationService orgnazationService;
	
	@Autowired
	private SyncDataService syncDataService;
	
	@Autowired
	private PostUserService postUserService;

	@Autowired
	private PostService postService;

	@Transactional(readOnly=false)
	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			User user=JacksonUtils.fromJson(saveJson, User.class);
			//校验用户名是否重复
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("loginName", user.getLoginName());
			Integer c=userService.checkLoginName(map);
			if(c>0){
				throw new InvalidCustomException("账号已存在，不可重复");
			}
			String pwd=user.getPassword();
			user.setPassword(EncryptionUtils.getEncryptInfo(user.getLoginName(),user.getPassword()));
			if(user.getEmailPwd()!=null&&StringUtils.isNotBlank(user.getEmailPwd())){
				EndecryptUtil test = new EndecryptUtil();
				String reValue = test.get3DESEncrypt(user.getId(), user.getEmailPwd());
			    reValue = reValue.trim().intern();
			    user.setEmailPwd(reValue);
			}
			int result=userService.save(user);
			//TODO(gyh--AD)
//			int result=userService.saveAndAd(user,pwd);
			
			SecurityUserBeanInfo securityUserBeanInfo=JacksonUtils.fromJson(userInfo, SecurityUserBeanInfo.class);
			try{
				//同步数据到第三方系统
				SyncDataThread sdt = new SyncDataThread(user.getId(),syncDataService,securityUserBeanInfo.getTendCode());
				sdt.start();
				Thread.sleep(500);
			}catch(Exception e){
			}
			info.setResult(JacksonUtils.toJson(user));
			info.setSucess(true);
			info.setMsg("保存对象成功!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("保存对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("保存对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	@Transactional(readOnly=false)
	@Override
	public String resetPassword(String userInfo, String paramJson){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map map=JacksonUtils.fromJson(paramJson, HashMap.class);
			
			String sign = (String)map.get("sign");
			String pwd=null;
			//重置全部用户密码
			if(sign.equals("1")){
				List<User> userList = userService.queryList(null);
				for(User user : userList){
					pwd=(String)map.get("password");
					user.setPassword(EncryptionUtils.getEncryptInfo(user.getLoginName(),(String)map.get("password")));
					userService.update(user);
					//TODO(gyh--AD)
//					userService.updateAndAd(user,pwd);
				}
			}else if(sign.equals("0")){
				//重置单个用户密码
				String userId = (String)map.get("userId");
				User oldUser=userService.getObjectById(userId);
				pwd=(String)map.get("password");
				oldUser.setPassword(EncryptionUtils.getEncryptInfo(oldUser.getLoginName(),(String)map.get("password")));
				userService.update(oldUser);
				//TODO(gyh--AD)
//				userService.updateAndAd(oldUser,pwd);
			}else{
				info.setResult("重置失败");
				info.setSucess(false);
				info.setMsg("参数为空");
			}

			info.setResult("重置成功");
			info.setSucess(true);
			info.setMsg("重置成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("重置失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("重置失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 批量保存用户排序号
	 * @param map
	 * @return
	 */
	public String saveUsersSort(String userInfo, String saveJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> map=JacksonUtils.fromJson(saveJson, HashMap.class);
			
			Integer res=userService.saveUsersSort(map);
			
			info.setResult(JacksonUtils.toJson(res));
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
//			User user=JacksonUtils.fromJson(updateJson, User.class);
			Map<String,Object> userMap=JacksonUtils.fromJson(updateJson, HashMap.class);
			
			User oldUser=userService.getObjectById(userMap.get("id").toString());
			oldUser.setLoginName(oldUser.getLoginName().replace("'", "\\'"));
			oldUser.setRealName(oldUser.getRealName().replace("'", "\\'"));
			boolean pwdFlag=false;
			String pwd=null;
			if(userMap.containsKey("password")&&StringUtils.isNotBlank(userMap.get("password").toString())){
				pwdFlag=true;
				pwd=userMap.get("password").toString();
				userMap.put("password", EncryptionUtils.getEncryptInfo(userMap.get("loginName").toString(),userMap.get("password").toString()));
			}
			/*if(userMap.containsKey("emailPwd")&&userMap.get("emailPwd")!=null&&StringUtils.isNotBlank(userMap.get("emailPwd").toString())){
				EndecryptUtil test = new EndecryptUtil();
				String reValue = test.get3DESEncrypt(userMap.get("id").toString(), userMap.get("emailPwd").toString());
			    reValue = reValue.trim().intern();
				userMap.put("emailPwd", reValue);
			}*/
			if (userMap.containsKey("emailPwd")){
				String emailPwd = (String) userMap.get("emailPwd");
				if (StringUtils.isNotBlank(emailPwd)) {
					EndecryptUtil test = new EndecryptUtil();
					String reValue = test.get3DESEncrypt(emailPwd, (String) userMap.get("id"));
					userMap.put("emailPwd", reValue);
				}
			}
			String oldJson=JacksonUtils.toJson(oldUser);
			Map<String,Object> oldMap=JacksonUtils.fromJson(oldJson, HashMap.class);
			oldMap.putAll(userMap);
			String newJson= JacksonUtils.toJson(oldMap);
			User user=JacksonUtils.fromJson(newJson,User.class);
			//校验用户名是否重复 todo 修改时，保存不了
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("loginName", user.getLoginName());
			map.put("id", user.getId());
			Integer c=userService.checkLoginName(map);
			if(c>0){
				throw new InvalidCustomException("登录名已存在，不可重复");
			}
			int result=userService.update(user);
			//TODO(gyh--AD)
			/*int result=0;//userService.update(user);
			if(pwdFlag){
				result= userService.updateAndAd(user,pwd);
			}else{
				result= userService.update(user);
			}*/
				
			SecurityUserBeanInfo securityUserBeanInfo=JacksonUtils.fromJson(userInfo, SecurityUserBeanInfo.class);
			try{
				//同步数据到第三方系统
				SyncDataThread sdt = new SyncDataThread(user.getId(),syncDataService,securityUserBeanInfo.getTendCode());
				sdt.start();
			}catch(Exception e){
			}
			System.out.println("-----------------------------------------++++++++++++++++++++++++++++++++++++");
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
			User user=JacksonUtils.fromJson(deleteJson, User.class);
			int result= userService.deleteObjectById(user.getId());
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
				int result= userService.deleteAllObjectByIds(list);
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
			User user=JacksonUtils.fromJson(getJson, User.class);
			User	result = userService.getObjectById(user.getId());
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
				Page page=userService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				//add by dgh 平台首页需要用户岗位信息
				Boolean hasPost = (Boolean) map.get("hasPost");
				if(hasPost!=null&&hasPost){
					List<User> userList = page.getList();
					List<UserDto> userDtoList = new ArrayList<UserDto>();

					userList.stream().forEach(user->{
						try {
							UserDto userDto = new UserDto();
							BeanUtils.copyProperties(user,userDto);
							Map<String,Object> postParam = new HashMap<String,Object>();
							postParam.put("userId",user.getId());
							List<Map<String,Object>> postMapList = postService.queryPostRoleListByUserId(postParam);
							if(!postMapList.isEmpty()){
								userDto.setPostName((String) postMapList.get(0).get("roleName"));
							}
							userDtoList.add(userDto);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});

					page.setList(userDtoList);
				}
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			}else{
				Page page=userService.getPage(new HashMap(), null, null);
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
				List list=userService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=userService.queryList(null);
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
	public String queryAdminList(String userInfo) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", "2");
			List<User> list = userService.queryList(map);
			info.setResult(JacksonUtils.toJson(list));
			info.setSucess(true);
			info.setMsg("获取管理员列表对象成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取管理员列表对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取管理员列表对象失败!");
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
	public String queryUserListByOrgId(String userInfo, String paramater){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				List<UserDto> list = new ArrayList<UserDto>();

				//查询出来所有组织机构
				List<Orgnazation> list_org  = orgnazationService.queryAllOrgListReturnOrg();

				//查询出来所有人
				List<UserDto> list_user  = userService.queryAllUserListReturnUser(map);

				if("0".equals(map.get("includelow"))){
					list = userService.queryUserListByOrgId(map);
				}else if("1".equals(map.get("includelow"))){
					List<Orgnazation> list_result = new ArrayList<Orgnazation>();
					if(null == map.get("orgId") || map.get("orgId").equals("")){
						list = userService.queryUserListByOrgId(map);
					}else{
						Orgnazation orgnazation =  orgnazationService.getObjectById(map.get("orgId").toString());
						if(null!=orgnazation){
							list_result.add(orgnazation);
							list_result = getOrgList(list_result,orgnazation,list_org);
						}
						for(Orgnazation o:list_result){
							for(UserDto u:list_user){
								if(u.getBelongOrgId().equals(o.getId())){
									//								u.setBelongOrgId(o.getName());
									list.add(u);
								}
							}
						}
					}
				}
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=userService.queryUserListByOrgId(null);
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


	/**
	 * 递归查询组织结构子节点
	 * @param orgnazationNodeDto
	 * @return
	 */
	public List<Orgnazation> getOrgList(List<Orgnazation> list_result , Orgnazation orgnazation,List<Orgnazation> list_org) throws Exception{
		List<Orgnazation> list1  = queryOrgChildNodeReturnOrg(orgnazation.getId(),list_org);
		list_result.addAll(list1);
		if(list1!=null && list1.size()>0){
			for(Orgnazation orgnazation1:list1){
				getOrgList(list_result,orgnazation1,list_org);
			}
		}else{
			return list_result;
		}
		return list_result;
	}

	//查询组织结构子节点（代替从数据库中进行查询）
	public List<Orgnazation> queryOrgChildNodeReturnOrg(String parentId,List<Orgnazation> list_org){
		List<Orgnazation> listOrgChildNode = new ArrayList<Orgnazation>();
		for(Orgnazation orgNodeDto:list_org){
			if(parentId.equals(orgNodeDto.getParentId())){
				listOrgChildNode.add(orgNodeDto);
			}
		}
		return listOrgChildNode;
	}

	@Override
	public String queryUserListByPostId(String userInfo, String paramater) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramater)) {
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				List list = userService.queryUserListByPostId(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			} else {
				List list = userService.queryUserListByPostId(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取列表对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryUserListByRoleId(String userInfo, String paramater) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramater)) {
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				List list = userService.queryUserListByRoleId(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			} else {
				List list = userService.queryUserListByRoleId(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取列表对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	//根据虚拟角色查用户
	public String queryRoleUserByRoleId(String userInfo, String paramater) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramater)) {
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				List list = userService.queryRoleUserByRoleId(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			} else {
				List list = userService.queryUserListByRoleId(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			log.error("获取列表对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	@Override
	public String deletePseudoObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			AppSystem appSystem=JacksonUtils.fromJson(deleteJson, AppSystem.class);
			int result= userService.deletePseudoObjectById(appSystem.getId());
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
				int result= userService.deletePseudoAllObjectByIds(list);
				//删除用户的同时删除岗位和用户的关系
				List<String> listPostUserIds = new ArrayList<String>();
				for(int j= 0;j<list.size(); j++){
				   Map<String, Object> paramater = new HashMap<String, Object>();
				   paramater.put("userId", list.get(j));
				   List<PostUser> listPostUser = postUserService.queryList(paramater);
				   if(null != listPostUser && listPostUser.size()>0){
					   for(int i = 0; i<listPostUser.size();i++){
						   PostUser pu = listPostUser.get(i);
						   listPostUserIds.add(pu.getId());
					   }
				   }
			   }
				   
			   if(listPostUserIds.size()>0){
				   postUserService.deleteAllObjectByIds(listPostUserIds);
			   }
				
				
				SecurityUserBeanInfo securityUserBeanInfo=JacksonUtils.fromJson(userInfo, SecurityUserBeanInfo.class);
				try{
					//同步数据到第三方系统
					SyncDataThread sdt = new SyncDataThread(map.get("id").toString(),syncDataService,securityUserBeanInfo.getTendCode());
					sdt.start();
				}catch(Exception e){
				}
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


	/**
	 * 获取岗位tree带组织机构的
	 * @param paramater
	 * @return
	 */
	@Override
	public String getUserTree(String userInfo, String paramater){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try{
			Map<String,Object> map =JacksonUtils.fromJson(paramater, HashMap.class);
			//查询出来第一级根目录
			List<OrgnazationNodeDto> list  = rootService.queryListRoot(map);
			//查询出来所有目录
			List<OrgnazationNodeDto> list_cata  = rootService.queryAllRoot(map);
			//查询出来所有组织机构
			List<OrgnazationNodeDto> list_org  = orgnazationService.queryAllOrgList(map);

			//查询出来所有岗位
			List<OrgnazationNodeDto> list_user  = userService.queryAllUserList(map);

			for(OrgnazationNodeDto orgnazationNodeDto:list){
				getRootList(orgnazationNodeDto,list_cata,list_org,list_user);
			}

			info.setResult(JacksonUtils.toJson(list));
			info.setSucess(true);
			info.setMsg("获取树对象成功!");
			System.out.println(JacksonUtils.toJson(list));
		}catch(Exception e){
			log.error("获取树对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取树对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}


	/**
	 * 递归获取目录子节点
	 * @param orgnazationNodeDto
	 * @return
	 */
	public OrgnazationNodeDto getRootList(OrgnazationNodeDto orgnazationNodeDto,List<OrgnazationNodeDto> list_cata,List<OrgnazationNodeDto> list_org,List<OrgnazationNodeDto> list_user) throws Exception{
		//		List<OrgnazationNodeDto> list1  = rootService.queryListRoot(orgnazationNodeDto.getId());
		List<OrgnazationNodeDto> list1  = queryCataChildNode(orgnazationNodeDto.getId(),list_cata);
		orgnazationNodeDto.setChildren(list1);
		if(list1!=null && list1.size()>0){
			for(OrgnazationNodeDto orgnazationNodeDto1:list1){
				getRootList(orgnazationNodeDto1,list_cata,list_org,list_user);
			}
		}else{
			List<OrgnazationNodeDto> ll= getOrgRootList(orgnazationNodeDto,list_org);
			for(OrgnazationNodeDto orgnazationNodeDto2:ll){
				getOrgList(orgnazationNodeDto2,list_org,list_user);
			}
			return orgnazationNodeDto;
		}
		getOrgList(orgnazationNodeDto,list_org,list_user);
		return orgnazationNodeDto;
	}

	//获取目录子节点目录（代替从数据库中进行查询）
	public List<OrgnazationNodeDto> queryCataChildNode(String parentId,List<OrgnazationNodeDto> list_cata){
		List<OrgnazationNodeDto> list_childNode = new ArrayList<OrgnazationNodeDto>();
		for(OrgnazationNodeDto orgNodeDto:list_cata){
			if(parentId.equals(orgNodeDto.getParentId())){
				list_childNode.add(orgNodeDto);
			}
		}

		return list_childNode;

	}


	/**
	 * 获取目录下的一级集团和公司
	 * @param orgnazationNodeDto
	 * @return
	 */
	public List<OrgnazationNodeDto> getOrgRootList(OrgnazationNodeDto orgnazationNodeDto,List<OrgnazationNodeDto> list_org ) throws Exception{
		//		List<OrgnazationNodeDto> list1  = orgnazationService.queryOrgListRoot(orgnazationNodeDto.getId());
		List<OrgnazationNodeDto> list1  = queryOrgRootNode(orgnazationNodeDto.getId(),list_org);
		orgnazationNodeDto.setChildren(list1);
		return list1;
	}

	/**
	 * 递归查询组织结构子节点
	 * @param orgnazationNodeDto
	 * @return
	 */
	public OrgnazationNodeDto getOrgList(OrgnazationNodeDto orgnazationNodeDto,List<OrgnazationNodeDto> list_org,List<OrgnazationNodeDto> list_user) throws Exception{
		//		List<OrgnazationNodeDto> list1  = orgnazationService.queryOrgList(orgnazationNodeDto.getId());
		//查询组织结构子节点（代替从数据库中进行查询）
		List<OrgnazationNodeDto> list1  = queryOrgChildNode(orgnazationNodeDto.getId(),list_org);
		/******************查询下属用户******************/
		List<OrgnazationNodeDto> list2  = queryUserChildNode(orgnazationNodeDto.getId(),list_user);
		/******************查询下属用户******************/
		list2.addAll(list1);
		orgnazationNodeDto.setChildren(list2);
		if(list1!=null && list1.size()>0){
			for(OrgnazationNodeDto orgnazationNodeDto1:list1){
				getOrgList(orgnazationNodeDto1,list_org,list_user);
			}
		}else{
			return orgnazationNodeDto;
		}
		return orgnazationNodeDto;
	}

	//查询组织结构子节点（代替从数据库中进行查询）
	public List<OrgnazationNodeDto> queryOrgChildNode(String parentId,List<OrgnazationNodeDto> list_org){
		List<OrgnazationNodeDto> listOrgChildNode = new ArrayList<OrgnazationNodeDto>();
		for(OrgnazationNodeDto orgNodeDto:list_org){
			if(parentId.equals(orgNodeDto.getParentId())){
				listOrgChildNode.add(orgNodeDto);
			}
		}
		return listOrgChildNode;
	}

	//查询目录下的一级集团或者公司（代替从数据库中进行查询）
	public List<OrgnazationNodeDto> queryOrgRootNode(String rootId,List<OrgnazationNodeDto> list_org){
		List<OrgnazationNodeDto> listOrgChildNode = new ArrayList<OrgnazationNodeDto>();
		for(OrgnazationNodeDto orgNodeDto:list_org){
			if(rootId.equals(orgNodeDto.getRootId())){
				if(orgNodeDto.getParentId() == null || "".equals(orgNodeDto.getParentId())){
					if("company".equals(orgNodeDto.getType()) || "zb".equals(orgNodeDto.getType())){
						listOrgChildNode.add(orgNodeDto);
					}
				}
			}
		}
		return listOrgChildNode;
	}

	//查询组织结构下的岗位
	public List<OrgnazationNodeDto> queryUserChildNode(String parentId,List<OrgnazationNodeDto> list_user){
		List<OrgnazationNodeDto> listUserChildNode = new ArrayList<OrgnazationNodeDto>();
		for(OrgnazationNodeDto orgNodeDto:list_user){
			if(parentId.equals(orgNodeDto.getParentId())){
				listUserChildNode.add(orgNodeDto);
			}
		}
		return listUserChildNode;
	}

	/**
	 * 修改密码
	 * @param guuid-map
	 */
	public String updateMyPwd(String userInfo, String paramater){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
//			UserDto user=JacksonUtils.fromJson(userInfo, UserDto.class);
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			Map map=JacksonUtils.fromJson(paramater, HashMap.class);
			String oldPwd=(String)map.get("oldPwd");//旧密码
			String newPwd=(String)map.get("newPwd");//新密码
			if(oldPwd == null || StringUtils.isBlank(oldPwd)){
				throw new InvalidCustomException("旧密码不可为空");
			}
			if(newPwd == null || StringUtils.isBlank(newPwd)){
				throw new InvalidCustomException("新密码不可为空");
			}
			User oldUser=userService.getObjectById(user.getSecurityUserDto().getId());
			String opInput=EncryptionUtils.getEncryptInfo(oldUser.getLoginName(),oldPwd);
			if(!opInput.equals(oldUser.getPassword())){
				throw new InvalidCustomException("旧密码输入错误");
			}
			oldUser.setPassword(EncryptionUtils.getEncryptInfo(oldUser.getLoginName(),newPwd));
			int result= userService.update(oldUser);
			//TODO(gyh--AD)
//			int result= userService.updateAndAd(oldUser,newPwd);
			SecurityUserBeanInfo securityUserBeanInfo=JacksonUtils.fromJson(userInfo, SecurityUserBeanInfo.class);
			try{
				//同步数据到第三方系统
				SyncDataThread sdt = new SyncDataThread(oldUser.getId(),syncDataService,securityUserBeanInfo.getTendCode());
				sdt.start();
			}catch(Exception e){
			}
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("修改密码成功!");
		} catch (Exception e) {
			log.error("修改密码失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("修改密码失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	/**
	 * 获取登录用户详情
	 * @param guuid-map
	 */
	public String getMyInfo(String userInfo, String paramater){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("userId", user.getSecurityUserDto().getId());
			UserDto oldUser=userService.selectUserInfoById(map);
			if(oldUser == null){
				throw new InvalidCustomException("不存在此用户");
			}
			info.setResult(JacksonUtils.toJson(oldUser));
			info.setSucess(true);
			info.setMsg("info成功!");
		} catch (Exception e) {
			log.error("info失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("info失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 根据用户Id，获取用户的公司，部门，项目，分期
	 * @param userInfo
	 * @param paramater
	 * @return
	 */
	@Override
	public String getOrgInfoByUserId(String userInfo, String paramater) { 
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map map=JacksonUtils.fromJson(paramater, HashMap.class);
			
			AuthenticationDto authenticationDto = userService.getOrgInfoByUserIdOrOrgId(map);
			info.setResult(JacksonUtils.toJson(authenticationDto));
		    info.setSucess(true);
		    info.setMsg("获取认证信息成功");
		} catch (Exception e) {
			 log.error("获取认证信息失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg(e.getMessage());
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 *  根据用户Id返回用户信息（可以多个）
	 * @param userInfo
	 * @param paramater
	 * @return
	 */
	@Override
	public String getUserInfoByUserIds(String userInfo, String paramater) { 
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map map=JacksonUtils.fromJson(paramater, HashMap.class);
			List<Map<String,String>> listUser = new ArrayList<Map<String,String>>();
			if(null != map.get("ids")){
				String ids = (String)map.get("ids");
				String[] idsList = ids.split(",");
				Map mapCon = new HashMap<String,Object>();
				mapCon.put("ids", idsList);
				listUser=userService.queryUsersByIds(mapCon);
			}
			info.setResult(JacksonUtils.toJson(listUser));
		    info.setSucess(true);
		    info.setMsg("获取认证信息成功");
		} catch (Exception e) {
			 log.error("获取认证信息失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg(e.getMessage());
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 *根据搜索条件查询用户及其岗位
	 * @param userInfo
	 * @return
	 */
	@Override
	public String queryUserAndPostsByUname(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map map=JacksonUtils.fromJson(paramater, HashMap.class);
			List<Map<String,Object>> listUser=userService.queryUserAndPostsByUname(map);
			info.setResult(JacksonUtils.toJson(listUser));
		    info.setSucess(true);
		    info.setMsg("获取认证信息成功");
		} catch (Exception e) {
			 log.error("获取认证信息失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg(e.getMessage());
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	/**
	 * 根据搜索条件查询用户
	 * @param userInfo
	 * @return
	 */
	//@Override
	public String selectUserByQuery(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map map=JacksonUtils.fromJson(paramater, HashMap.class);
			if (map.get("limit")!=null&&Integer.valueOf(map.get("limit").toString())==-1 && map.get("fristLimit")!=null) {
				map.put("limit", map.get("fristLimit"));
			}
			List<UserDto> listUser=userService.selectUserByQuery(map);
			info.setResult(JacksonUtils.toJson(listUser));
			info.setSucess(true);
			info.setMsg("获取认证信息成功");
		} catch (Exception e) {
			log.error("获取认证信息失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg(e.getMessage());
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 根据条件查询用户
	 * @param userInfo
	 * @return
	 */
	public String selectUserByQueryPage(String userInfo, String paramater){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map map=JacksonUtils.fromJson(paramater, HashMap.class);
			Page page=userService.selectUserByQueryPage(map);
			info.setResult(JacksonUtils.toJson(page));
		    info.setSucess(true);
		    info.setMsg("获取分页对象成功!");
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
	public String updateIMuser(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> userMap=JacksonUtils.fromJson(paramater, HashMap.class);
			
			User oldUser=userService.getObjectById(userMap.get("userId").toString());
			
			if(null!=userMap.get("mobile") && !"".equals(userMap.get("mobile").toString())){
				oldUser.setMobile(userMap.get("mobile").toString());
			}
			if(null!=userMap.get("isMale") && !"".equals(userMap.get("isMale").toString())){
				oldUser.setIsMale(userMap.get("isMale").toString());
			}
			if(null!=userMap.get("email") && !"".equals(userMap.get("email").toString())){
				oldUser.setEmail(userMap.get("email").toString());
			}
			int result = -1;
			if(null!=userMap.get("password") && !"".equals(userMap.get("password").toString())){
				oldUser.setPassword(userMap.get("password").toString());
//				result= userService.updateAndAd(oldUser,userMap.get("plaintextPassword").toString());
				result=   userService.update(oldUser);
			}else{
				result=   userService.update(oldUser);
			}
			
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
	/**
	 * 读excel并插入db，校验数据的合法性，插入数据库，并返回结果
	 */
	@Override
	public String readExcelAndInsert(String userJson, List<UserDto> userList) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String, Object> c = userService.readExcelAndInsert(userList);
			info.setResult(JacksonUtils.toJson(c));
			info.setSucess(true);
			info.setMsg("导入成功!");
		} catch (Exception e) {
			log.error("导入失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("导入失败");
			info.setExceptionMsg(e.getMessage().replace(e.getClass().getName(), ""));
		}
		return JacksonUtils.toJson(info);
	}
	@Override
	public String lockUsersOrNot(String userJson,  String paramJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param = JacksonUtils.fromJson(paramJson,HashMap.class);
			int c = userService.lockUsersOrNot(param);
			info.setResult(c+"");
			info.setSucess(true);
			info.setMsg("导入成功!");
		} catch (Exception e) {
			log.error("导入失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("导入失败");
			info.setExceptionMsg(e.getMessage().replace(e.getClass().getName(), ""));
		}
		return JacksonUtils.toJson(info);
	}
}
