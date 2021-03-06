package com.xinleju.platform.sys.notice.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.holders.LongHolder;
import javax.xml.rpc.holders.StringHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tempuri.MobileListGroup;
import org.tempuri.MobsetApi;
import org.tempuri.MobsetApiLocator;
import org.tempuri.MobsetApiSoapStub;
import org.tempuri.holders.ArrayOfSmsIDListHolder;

import com.util.MD5;
import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.notice.dao.SysNoticePhoneMsgDao;
import com.xinleju.platform.sys.notice.dao.SysNoticePhoneServerDao;
import com.xinleju.platform.sys.notice.entity.SysNoticePhoneMsg;
import com.xinleju.platform.sys.notice.entity.SysNoticePhoneServer;
import com.xinleju.platform.sys.notice.service.SysNoticePhoneMsgService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 * 
 */

@Service
public class SysNoticePhoneMsgServiceImpl extends  BaseServiceImpl<String,SysNoticePhoneMsg> implements SysNoticePhoneMsgService{
	

	@Autowired
	private SysNoticePhoneMsgDao sysNoticePhoneDao;
	@Autowired
	private SysNoticePhoneServerDao sysNoticePhoneServerDao;
	
	@Override
	public Page queryVaguePage(Map<String, Object> map) {
		Page page=new Page();
		List<Map<String,Object>> list = sysNoticePhoneDao.getPageData(map);
		Integer count = sysNoticePhoneDao.getPageDataCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		return page;
	}

	@Override
	public String saveSendMsg(Map<String, Object> map) throws Exception {
		try{
			//查询默认短信通道信息
			SysNoticePhoneServer sysNoticePhoneServer = sysNoticePhoneServerDao.getObjectByDefault(new HashMap<String,Object>());
			if(null!=sysNoticePhoneServer){
				if(null!=map.get("phones")&&!"".equals((String)map.get("phones"))){
					SysNoticePhoneMsg bean = new SysNoticePhoneMsg();
					String uuid = IDGenerator.getUUID();
					bean.setId(uuid);
					bean.setPhones((String)map.get("phones"));
					bean.setMsg((String)map.get("msg"));
					bean.setStatus("2");
					bean.setNum(1);
					bean.setPhonelServerId(sysNoticePhoneServer.getId());
					bean.setTemplate("");
					bean.setIsTemplate("0");
					sysNoticePhoneDao.save(bean);
					
					//发送短信
					MobsetApi mobsetApi=new MobsetApiLocator();
			        MobsetApiSoapStub mobset = (MobsetApiSoapStub)mobsetApi.getMobsetApiSoap();
			        SimpleDateFormat dateFormater = new SimpleDateFormat("MMddHHmmss");
					Date date=new Date();
			        String timeStamp = dateFormater.format(date);
			        MD5 md5 = new MD5();
			        String password = md5.getMD5ofStr(sysNoticePhoneServer.getIdNumber()+sysNoticePhoneServer.getPassword()+timeStamp);
			        String addNum = "";
			        long longSms = 1;
			        String timer = dateFormater.format(date);
					String[] phones = map.get("phones").toString().split(",");
			        MobileListGroup[] mobileList = new MobileListGroup[phones.length];
					for(int i=0;i<phones.length;i++){
				        MobileListGroup mobileListGroup = new MobileListGroup();
				        mobileListGroup.setMobile(phones[i]);
						mobileList[i] = mobileListGroup;
					}
					
			        LongHolder count = new LongHolder();
			        StringHolder errMsg = new StringHolder();
			        ArrayOfSmsIDListHolder smsIDList = new ArrayOfSmsIDListHolder();
			        mobset.sms_Send(new Long(sysNoticePhoneServer.getIdNumber()), sysNoticePhoneServer.getUsername(), password, timeStamp, addNum, timer, longSms, mobileList, (String)map.get("msg"), count, errMsg, smsIDList);
					
					//修改短信状态
					Map<String, Object> upMap = new HashMap<String,Object>();
					upMap.put("id", uuid);
					sysNoticePhoneDao.updateStatus(upMap);
				}else{
					throw new Exception("参数【接收手机号为空】!");//phones 为空
				}
			}else{
				throw new Exception("查询默认短信通道出错,数据为null!");//查询默认通道为空
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return "1";
	}

	@Override
	public String saveSendMsgTest(Map<String, Object> map) throws Exception {
		try{
			//查询默认短信通道信息
			SysNoticePhoneServer sysNoticePhoneServer = sysNoticePhoneServerDao.getObjectById((String)map.get("id"));
			if(null!=sysNoticePhoneServer){
				if(null!=map.get("phones")&&!"".equals((String)map.get("phones"))){
					SysNoticePhoneMsg bean = new SysNoticePhoneMsg();
					String uuid = IDGenerator.getUUID();
					bean.setId(uuid);
					bean.setPhones((String)map.get("phones"));
					bean.setMsg((String)map.get("msg"));
					bean.setStatus("2");
					bean.setNum(1);
					bean.setPhonelServerId(sysNoticePhoneServer.getId());
					bean.setTemplate("");
					bean.setIsTemplate("0");
					sysNoticePhoneDao.save(bean);
					
					//发送短信
					MobsetApi mobsetApi=new MobsetApiLocator();
			        MobsetApiSoapStub mobset = (MobsetApiSoapStub)mobsetApi.getMobsetApiSoap();
			        SimpleDateFormat dateFormater = new SimpleDateFormat("MMddHHmmss");
					Date date=new Date();
			        String timeStamp = dateFormater.format(date);
			        MD5 md5 = new MD5();
			        String password = md5.getMD5ofStr(sysNoticePhoneServer.getIdNumber()+sysNoticePhoneServer.getPassword()+timeStamp);
			        String addNum = "";
			        long longSms = 1;
			        String timer = dateFormater.format(date);
					String[] phones = map.get("phones").toString().split(",");
			        MobileListGroup[] mobileList = new MobileListGroup[phones.length];
					for(int i=0;i<phones.length;i++){
				        MobileListGroup mobileListGroup = new MobileListGroup();
				        mobileListGroup.setMobile(phones[i]);
						mobileList[i] = mobileListGroup;
					}
					
			        LongHolder count = new LongHolder();
			        StringHolder errMsg = new StringHolder();
			        ArrayOfSmsIDListHolder smsIDList = new ArrayOfSmsIDListHolder();
			        mobset.sms_Send(new Long(sysNoticePhoneServer.getIdNumber()), sysNoticePhoneServer.getUsername(), password, timeStamp, addNum, timer, longSms, mobileList, (String)map.get("msg"), count, errMsg, smsIDList);
					
					//修改短信状态
					Map<String, Object> upMap = new HashMap<String,Object>();
					upMap.put("id", uuid);
					sysNoticePhoneDao.updateStatus(upMap);
				}else{
					throw new Exception("参数【接收手机号为空】!");//phones 为空
				}
			}else{
				throw new Exception("查询默认短信通道出错,数据为null!");//查询默认通道为空
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return "1";
	}

	@Override
	public String updateAgainSendMsg(Map<String, Object> map) throws Exception {
		try{
			//查询默认短信通道信息
			SysNoticePhoneServer sysNoticePhoneServer = sysNoticePhoneServerDao.getObjectByDefault(new HashMap<String,Object>());
			if(null!=sysNoticePhoneServer){
				SysNoticePhoneMsg newBean = JacksonUtils.fromJson(JacksonUtils.toJson(map),SysNoticePhoneMsg.class); 
				
				SysNoticePhoneMsg oldBean = sysNoticePhoneDao.getObjectById(newBean.getId());
				Integer num = oldBean.getNum();
				oldBean.setNum(num+1);
				oldBean.setStatus("2");
				oldBean.setPhonelServerId(sysNoticePhoneServer.getId());
				sysNoticePhoneDao.update(oldBean);
				
				//发送短信
				MobsetApi mobsetApi=new MobsetApiLocator();
		        MobsetApiSoapStub mobset = (MobsetApiSoapStub)mobsetApi.getMobsetApiSoap();
		        SimpleDateFormat dateFormater = new SimpleDateFormat("MMddHHmmss");
				Date date=new Date();
		        String timeStamp = dateFormater.format(date);
		        MD5 md5 = new MD5();
		        String password = md5.getMD5ofStr(sysNoticePhoneServer.getIdNumber()+sysNoticePhoneServer.getPassword()+timeStamp);
		        String addNum = "";
		        long longSms = 1;
		        String timer = dateFormater.format(date);
				String[] phones = newBean.getPhones().toString().split(",");
				        MobileListGroup[] mobileList = new MobileListGroup[phones.length];
				for(int i=0;i<phones.length;i++){
			        MobileListGroup mobileListGroup = new MobileListGroup();
			        mobileListGroup.setMobile(phones[i]);
							mobileList[i] = mobileListGroup;
				}
				
		        LongHolder count = new LongHolder();
		        StringHolder errMsg = new StringHolder();
		        ArrayOfSmsIDListHolder smsIDList = new ArrayOfSmsIDListHolder();
		        mobset.sms_Send(new Long(sysNoticePhoneServer.getIdNumber()), sysNoticePhoneServer.getUsername(), password, timeStamp, addNum, timer, longSms, mobileList, newBean.getMsg(), count, errMsg, smsIDList);
			
				//修改短信状态
				Map<String, Object> upMap = new HashMap<String,Object>();
				upMap.put("id", oldBean.getId());
				sysNoticePhoneDao.updateStatus(upMap);
				
			}else{
				throw new Exception("查询默认短信通道出错,数据为null!");//查询默认通道为空
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return "1";
	}
	
}
