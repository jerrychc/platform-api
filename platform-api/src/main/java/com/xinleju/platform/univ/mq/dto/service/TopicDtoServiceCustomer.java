package com.xinleju.platform.univ.mq.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface TopicDtoServiceCustomer extends BaseDtoServiceCustomer{
    /**
     *@param userInfo
     * @param topicName
     * @return
     */
    public String getByName(String userInfo, String topicName);

}
