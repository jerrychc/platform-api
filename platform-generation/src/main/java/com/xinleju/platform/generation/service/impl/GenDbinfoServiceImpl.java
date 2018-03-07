package com.xinleju.platform.generation.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.generation.dao.GenDbinfoDao;
import com.xinleju.platform.generation.dto.GenerationDbinfoDto;
import com.xinleju.platform.generation.entity.GenerationDbinfo;
import com.xinleju.platform.generation.entity.GenerationScheme;
import com.xinleju.platform.generation.entity.GenerationSystable;
import com.xinleju.platform.generation.entity.GenerationSystableColumn;
import com.xinleju.platform.generation.service.GenDbinfoService;

@Service
public class GenDbinfoServiceImpl extends  BaseServiceImpl<String,GenerationDbinfo> implements GenDbinfoService{
	

	@Autowired
	private GenDbinfoDao genDbinfoDao;
	
	
	public void SaveTable(GenerationSystable g_systable,List<GenerationSystableColumn> l_column,List<GenerationScheme> l_scheme){
		Map<String,Object> mapTable = new HashMap<String,Object>();
//		GenerationScheme g_scheme = l_scheme.get(0);
//		mapTable.put("g_systable", g_systable);
//		mapTable.put("g_scheme", g_scheme);
//		mapTable.put("l_column", l_column);
		
		StringBuffer sb = new StringBuffer();
		sb.append(" create table "+ g_systable.getTableName() +" ");
		sb.append(" ( ");
		sb.append(" id                   varchar(64) not null comment '主键', ");
		sb.append(" create_person_id     varchar(64) comment '创建者的ID或者CODE，唯一标识', ");
		sb.append(" create_person_name   varchar(64) comment '创建者姓名', ");
		sb.append(" create_org_id        varchar(64) comment '创建人部门ID', ");
		sb.append(" create_org_name      varchar(64) comment '创建者部门名称', ");
		sb.append(" create_company_id    varchar(64) comment '创建者单位ID', ");
		sb.append(" create_company_name  varchar(64) comment '创建这单位名称', ");
		sb.append(" create_date          timestamp comment '创建时间', ");
		sb.append(" update_person_id     varchar(64) comment '更新者ID或者CODE', ");
		sb.append(" update_person_name   varchar(64) comment '更新者姓名', ");
		sb.append(" update_date          timestamp comment '更新时间', ");
		sb.append(" delflag              varchar(32) comment '逻辑删除标识（0：正常；1：删除）', ");
		sb.append(" concurrency_version              int comment '并发版本', ");
		
		for(GenerationSystableColumn gsc:l_column){
			//判断是否有列类型的长度
			if(null!=gsc.getColumnLength() && !gsc.getColumnLength().equals("")){
				//判断是否为空
				if(null!=gsc.getIsNull() && gsc.getIsNull().equals("0")){
					sb.append(" "+gsc.getColumnName()+" "+gsc.getColumnType()+"("+gsc.getColumnLength()+") not null comment '"+gsc.getComments()+"', ");
				}else{
					sb.append(" "+gsc.getColumnName()+" "+gsc.getColumnType()+"("+gsc.getColumnLength()+") comment '"+gsc.getComments()+"', ");
				}
				
			}else{
				//判断是否为空
				if(null!=gsc.getIsNull() && gsc.getIsNull().equals("0")){
					sb.append(" "+gsc.getColumnName()+" "+gsc.getColumnType()+" not null comment '"+gsc.getComments()+"', ");
				}else{
					sb.append(" "+gsc.getColumnName()+" "+gsc.getColumnType()+" comment '"+gsc.getComments()+"', ");
				}
			}
			
		}
		sb.append(" primary key (id) ");
		sb.append(" ) comment '"+g_systable.getComments()+"'; ");
//		sb.append(" alter table "+ g_systable.getTableName() +" comment '"+g_systable.getComments()+"'; ");
		
		mapTable.put("info", sb.toString());
		System.out.println("创建表sql:"+sb.toString());
//		GenerationDbinfoDto gdDto =  new GenerationDbinfoDto();
//		gdDto.setTableBody(mapTable);
		genDbinfoDao.save("createTable", mapTable);
	}

}
