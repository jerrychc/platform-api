package com.xinleju.platform.univ.mq.dao;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.univ.mq.entity.Topic;

/**
 * @author xubaoyong
 *
 */

public interface TopicDao extends BaseDao<String, Topic> {

    /**
     *@param userInfo
     * @param topicName
     * @return
     */
	public Topic getByName(String userInfo,String topicName);

    /**
     *
     * @param userInfo
     * @param topicName
     * @param id
     * @return
     */
    public Topic getByNameAndId(String userInfo,String topicName,String id);
}
