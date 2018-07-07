package com.yodean.site.web.content.entity;

import com.rick.dev.persistence.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by rick on 2017/7/12.
 */
@Entity
@Table(name="c_video")
public class Video extends DataEntity<Video> {
    private String title;

    private String cover;

    private String embed;

    @Column(name = "category_id")
    private Integer categoryId;

    private String description;

    @Column(name = "order_id")
    private Integer orderId;

    public Video() {}

    public Video(String title, String cover, String embed, String description) {
        this.title = title;
        this.cover = cover;
        this.embed = embed;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getEmbed() {
        return embed;
    }

    public void setEmbed(String embed) {
        this.embed = embed;
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
