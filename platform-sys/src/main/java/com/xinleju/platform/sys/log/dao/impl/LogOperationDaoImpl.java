package com.xinleju.platform.sys.log.dao.impl;

import java.util.*;

import com.xinleju.platform.flow.utils.StringUtil;
import com.xinleju.platform.sys.log.dto.LogOperationDto;
import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.log.dao.LogOperationDao;
import com.xinleju.platform.sys.log.entity.LogOperation;
import sun.rmi.runtime.Log;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class LogOperationDaoImpl extends BaseDaoImpl<String,LogOperation> implements LogOperationDao{

	public LogOperationDaoImpl() {
		super();
	}

	/**
	 * 分页数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> getPageData(Map<String, Object> map){
		Long t1 = System.currentTimeMillis();
		List<Map<String,String>> logOperations =  getSqlSession().selectList("com.xinleju.platform.sys.log.entity.LogOperation.getPageData", map);
		Long t2 = System.currentTimeMillis();
		System.out.println("1>>>>>>>>>>>>>>>>>>>>>>>>>"+(t2-t1));
		Set<String> sysCodes = new HashSet<>();
		Map<String,List<Map<String,String>>> codeLogMap = new HashMap<>();
		String sysCode = null;
		for (int i = 0; i < logOperations.size() ; i++) {
			Map<String,String> log = logOperations.get(i);
			sysCode = log.get("sysCode");
			sysCodes.add(sysCode);
			if(codeLogMap.get(sysCode) != null){
				codeLogMap.get(sysCode).add(log);
			}else {
				List<Map<String,String>> list = new ArrayList<>();
				list.add(log);
				codeLogMap.put(sysCode,list);
			}

		}
		Long t3 = System.currentTimeMillis();
		System.out.println("2>>>>>>>>>>>>>>>>>>>>>>>>>"+(t3-t2));
		if(sysCodes.size()>0){
			map.put("sysCodes",sysCodes);
			List<Map<String,String>> apps = getSqlSession().selectList("com.xinleju.platform.sys.log.entity.LogOperation.queryAppName", map);
			Long t4 = System.currentTimeMillis();
			System.out.println("3>>>>>>>>>>>>>>>>>>>>>>>>>"+(t4-t3));
			for (int i = 0; i < apps.size() ; i++) {
				Map<String,String> app = apps.get(i);
				List<Map<String,String>> list_code = codeLogMap.get(app.get("code"));
				if(list_code != null){
					for (int j = 0; j < list_code.size(); j++) {
						Map<String,String> log = list_code.get(j);
						log.put("appName",app.get("name"));
					}
				}
				List<Map<String,String>> list_id = codeLogMap.get(app.get("id"));
				if(list_id != null){
					for (int j = 0; j < list_id.size(); j++) {
						Map<String,String> log = list_id.get(j);
						log.put("appName",app.get("name"));
					}
				}
			}
			Long t5 = System.currentTimeMillis();
			System.out.println("4>>>>>>>>>>>>>>>>>>>>>>>>>"+(t5-t4));
		}
		return logOperations;
	}
	/**
	 * 总条数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer getPageDataCount(Map<String, Object> map){
		return  getSqlSession().selectOne("com.xinleju.platform.sys.log.entity.LogOperation.getPageDataCount", map);
	}
	
	
}
