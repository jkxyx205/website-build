package com.yodean.site.web.content.entity;

import com.rick.dev.persistence.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by rick on 2017/7/12.
 */
@Entity
@Table(name="c_category")
public class Category extends DataEntity<Category> {
    private String title;

//    private Integer type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public Integer getType() {
//        return type;
//    }

//    public void setType(Integer type) {
//        this.type = type;
//    }
}
