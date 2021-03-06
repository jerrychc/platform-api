package com.xinleju.platform.univ.mq.utils;

import org.springframework.core.io.AbstractFileResolvingResource;

import java.io.*;

/**
 * Created by xubaoyong on 2017/3/30.
 */
public class MemoryXmlResource extends AbstractFileResolvingResource {
    private String xmlInfo;
    public MemoryXmlResource(String xmlInfo) {
        this.xmlInfo = xmlInfo;
    }
    @Override
    public String getDescription() {
        String desc = "动态加载bean,准备的MemoryXmlResource";
        return desc;
    }




    public InputStream getInputStream() throws IOException {
//        StringReader in = new StringReader(xmlInfo);
        return  xmlInfo ==null ? null : new ByteArrayInputStream(xmlInfo.getBytes());
    }
}
