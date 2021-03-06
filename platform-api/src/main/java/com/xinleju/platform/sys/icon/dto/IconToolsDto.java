package com.xinleju.platform.sys.icon.dto;

import com.xinleju.platform.base.dto.BaseDto;

/**
 * Created by ly on 2017/12/1.
 */
public class IconToolsDto extends BaseDto {

    private String name;

    private String code;

    private String extendName;

    private String fullName;

    private String url;

    private Long iconSize;

    private String iconType;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExtendName() {
        return extendName;
    }

    public void setExtendName(String extendName) {
        this.extendName = extendName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getIconSize() {
        return iconSize;
    }

    public void setIconSize(Long iconSize) {
        this.iconSize = iconSize;
    }

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType;
    }

}