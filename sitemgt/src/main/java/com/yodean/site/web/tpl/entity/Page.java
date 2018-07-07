package com.yodean.site.web.tpl.entity;

import com.rick.dev.persistence.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by rick on 2017/7/12.
 */
@Entity
@Table(name = "w_page")
public class Page extends DataEntity<Page> {
    private String name;

    @Column(name="web_id")
    private Integer webId;

    @Column(name="layout_id")
    private Integer layoutId;

    private String html;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWebId() {
        return webId;
    }

    public void setWebId(Integer webId) {
        this.webId = webId;
    }

    public Integer getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Integer layoutId) {
        this.layoutId = layoutId;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
