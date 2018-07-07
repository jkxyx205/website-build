package com.yodean.site.web.tpl.entity;

import com.rick.dev.persistence.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by rick on 2017/7/12.
 */
@Entity
@Table(name="w_menu")
public class Menu extends DataEntity<Menu>{
    private String title;

    private Integer pid;

    @Column(name="link_type")
    private String linkType;

    private String href;

    @Transient
    private String absoluteHref;

    private Integer seq;

    @Column(name="page_id")
    private Integer pageId;

    @Column(name="web_id")
    private Integer webId;

    private String target;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getWebId() {
        return webId;
    }

    public void setWebId(Integer webId) {
        this.webId = webId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getAbsoluteHref() {
        return absoluteHref;
    }

    public void setAbsoluteHref(String absoluteHref) {
        this.absoluteHref = absoluteHref;
    }
}
