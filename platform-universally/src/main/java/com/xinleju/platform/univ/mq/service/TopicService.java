package com.xinleju.platform.univ.mq.service;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.univ.mq.entity.Topic;

/**
 * @author xubaoyong
 * 
 * 
 */

public interface TopicService extends  BaseService <String,Topic>{
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
