package com.yodean.site.web.tpl.dto;

import com.rick.dev.utils.JacksonUtils;

import java.util.Map;

/**
 * Created by rick on 2017/7/12.
 */
public class PageComponentDto {
    private Integer id;

    private String label;

    private Integer pageId;

    private Integer componentId;

    private Integer contentType;

    private Integer categoryId;

    private String options;

    private Map<String, Object> content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Integer getComponentId() {
        return componentId;
    }

    public void setComponentId(Integer componentId) {
        this.componentId = componentId;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Map getOptionsMap() {
        return JacksonUtils.readValue(getOptions(), Map.class);
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }
}
