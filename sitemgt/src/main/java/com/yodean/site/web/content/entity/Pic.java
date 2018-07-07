package com.yodean.site.web.content.entity;

import com.rick.dev.persistence.DataEntity;
import com.rick.dev.utils.DateUtils;
import com.rick.dev.utils.JacksonUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.Map;

/**
 * Created by rick on 2017/7/12.
 */
@Entity
@Table(name="c_pic")
public class Pic extends DataEntity<Pic> {
    private String title;

    private String description;

    private String src;

    @Column(name="full_src")
    private String fullSrc;

    private String href;

    @Column(name = "category_id")
    private Integer categoryId;


    @Column(name = "order_id")
    private Integer orderId;

    private String options;

    public Pic() {}

    public Pic(String title, String fullSrc, String description) {
        this.title = title;
        this.description = description;
        this.fullSrc = this.src =  fullSrc;
        this.href = "";
        Date now = new Date();
        this.setCreateDate(now);
        this.setUpdateDate(now);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getFullSrc() {
        return fullSrc;
    }

    public void setFullSrc(String fullSrc) {
        this.fullSrc = fullSrc;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Transient
    private String publishDateString;

    public void setPublishDateString(String publishDateString) {
        this.publishDateString = publishDateString;
    }

    public String getPublishDateString() {
        if (getCreateDate() != null)
            return DateUtils.formatDate(getCreateDate() , "yyyy-MM-dd");

        return publishDateString;
    }
}
