package com.xinleju.platform.univ.mq.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.univ.mq.dao.TopicDao;
import com.xinleju.platform.univ.mq.entity.Topic;
import com.xinleju.platform.univ.mq.service.TopicService;

/**
 * @author xubaoyong
 * 
 * 
 */

@Service
public class TopicServiceImpl extends  BaseServiceImpl<String,Topic> implements TopicService{
	

	@Autowired
	private TopicDao mqTopicDao;
	public Topic getByName(String userInfo, String topicName) {
		return mqTopicDao.getByName(userInfo,topicName);
	}

	@Override
	public Topic getByNameAndId(String userInfo, String topicName, String id) {
		return mqTopicDao.getByNameAndId(userInfo,topicName,id);
	}
}
