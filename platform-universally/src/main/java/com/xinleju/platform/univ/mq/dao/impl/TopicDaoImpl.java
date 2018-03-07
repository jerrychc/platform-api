package com.xinleju.platform.univ.mq.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.univ.mq.dao.TopicDao;
import com.xinleju.platform.univ.mq.entity.Topic;

/**
 * @author xubaoyong
 * 
 * 
 */

@Repository
public class TopicDaoImpl extends BaseDaoImpl<String,Topic> implements TopicDao{
	private Class entityClass;
	public TopicDaoImpl() {

		super();
		Type genType = this.getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
		this.entityClass = (Class)params[1];
	}



	public Topic getByName(String userInfo, String topicName) {

		String sql =  "select * from sys_tend_mq_topic where topic='"+topicName+"'";
		List list = this.getSqlSession().selectList(this.entityClass.getName() + ".getByName" , sql);

		return list == null || list.size() == 0 ? null: (Topic) list.get(0);

	}


	public Topic getByNameAndId(String userInfo, String topicName, String id) {
		String sql =  "select * from sys_tend_mq_topic where topic='"+topicName+"' and id != '"+id+"'";
		List list = this.getSqlSession().selectList(this.entityClass.getName() + ".getByName" , sql);

		return list == null || list.size() == 0 ? null: (Topic) list.get(0);
	}
}