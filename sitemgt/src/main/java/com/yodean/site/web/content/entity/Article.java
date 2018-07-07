package com.yodean.site.web.content.entity;

import com.rick.dev.persistence.DataEntity;
import com.rick.dev.utils.DateUtils;
import com.yodean.site.web.content.service.ArticleService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by rick on 2017/7/12.
 */
@Entity
@Table(name="c_article")
public class Article extends DataEntity<Article> {
    private String title;

    private String author;

    private String cover;

    @Column(name="publish_date")
    private Date publishDate;

    @Column(name="stick_top")
    private String stickTop = "0";

    private String html;

    private String content;

    private String description;

    private String status;

    @Column(name="category_id")
    private Integer categoryId;

    @Column(name="times_read", updatable = false)
    private Integer timesRead;


    @Column(name="order_id")
    private Integer orderId;

    public Article() {}

    public Article(String title, String cover, String html) {
        this.title = title;
        this.cover = cover;
        this.html = this.content = this.description = html;
        this.author = "点研智软";
        this.setStatus(ArticleService.ARTICLE_STATUS_PUBLISHED);
        this.setPublishDate(new Date());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getStickTop() {
        return stickTop;
    }

    public void setStickTop(String stickTop) {
        this.stickTop = stickTop;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getTimesRead() {
        return timesRead;
    }

    public void setTimesRead(Integer timesRead) {
        this.timesRead = timesRead;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getPublishDateString() {
        if (publishDate == null) return "";

        return DateUtils.formatDate(publishDate, "yyyy-MM-dd");
    }
}
