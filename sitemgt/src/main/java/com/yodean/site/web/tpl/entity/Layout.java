package com.yodean.site.web.tpl.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rick.dev.persistence.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by rick on 2017/7/12.
 */
@Entity
@Table(name="w_layout")
public class Layout extends DataEntity<Layout> {
    private String title;

    @JsonIgnore
    private String html;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

}
