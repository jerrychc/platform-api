/**
 *
 */
package com.xinleju.platform.flow.utils;

/**
 * @author user
 * @data 2017-7-8 下午6:06:50
 * <p>Title:ContentFlowTreeData</p>
 * <p>description:</p>
 */
public class ContentFlowTreeData {
    private String id;

    private String parentId;


    private String name;

    private String pId;

    private String dataType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}


