package com.yodean.site.web.tpl.dto;

/**
 * Created by rick on 2017/8/23.
 */
public class ComponentTypeDto {
    private String contentType;
    private String description;


    public ComponentTypeDto(String contentType, String description) {
       this.contentType = contentType;
       this.description = description;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDescription() {
        return description;
    }
}
