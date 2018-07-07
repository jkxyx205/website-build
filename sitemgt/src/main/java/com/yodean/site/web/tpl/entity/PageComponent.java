package com.yodean.site.web.tpl.entity;

import com.rick.dev.persistence.DataEntity;
import com.rick.dev.utils.JacksonUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Map;

/**
 * Created by rick on 2017/7/12.
 */
@Entity
@Table(name="w_page_component")
public class PageComponent extends DataEntity<PageComponent> {
    @Column(name = "page_id")
    private Integer pageId;

    @Column(name = "component_id")
    private Integer componentId;

//    @Column(name = "content_type")
//    private Integer contentType;

    @Column(name = "category_id")
    private Integer categoryId;

    private String options;

    @Transient
    private Map optionsMap;

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

//    public Integer getContentType() {
//        return contentType;
//    }

//    public void setContentType(Integer contentType) {
//        this.contentType = contentType;
//    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getOptions() {
        return options == null ? "{}" : options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Map getOptionsMap() {
        Map map = JacksonUtils.readValue(getOptions(), Map.class);

        map.put("cpnId", this.getId());
        map.put("pageId", pageId);

        return map;
    }
}
