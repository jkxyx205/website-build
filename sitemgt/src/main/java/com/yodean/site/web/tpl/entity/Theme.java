package com.yodean.site.web.tpl.entity;

import com.rick.dev.config.Global;
import com.rick.dev.persistence.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.File;

/**
 * Created by rick on 2017/9/6.
 */
@Entity
@Table(name = "w_theme")
public class Theme extends DataEntity<Theme> {
    @Column(name = "page_id")
    private Integer pageId;

    @Column(name = "style_id")
    private Integer styleId;

    @Column(name = "header_id")
    private Integer headerId;

    @Column(name = "footer_id")
    private Integer footerId;

    @Column(name = "block_id")
    private Integer blockId;

    public Theme() {}

    public Theme(Integer pageId, Integer styleId, Integer headerId, Integer footerId, Integer blockId) {
        this.pageId = pageId;
        this.styleId = styleId;
        this.headerId = headerId;
        this.footerId = footerId;
        this.blockId = blockId;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Integer getStyleId() {
        return styleId;
    }

    public void setStyleId(Integer styleId) {
        this.styleId = styleId;
    }

    public Integer getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Integer headerId) {
        this.headerId = headerId;
    }

    public Integer getFooterId() {
        return footerId;
    }

    public void setFooterId(Integer footerId) {
        this.footerId = footerId;
    }

    public Integer getBlockId() {
        return blockId;
    }

    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
    }

    public String getThumbnail() {
        return Global.upload + File.separator + "tpl" + File.separator + ""+pageId+"-"+styleId+"-"+headerId+"-"+footerId+"-"+blockId+".png";
    }
    public String getHeaderThumbnail() {
        return Global.upload + File.separator + "tpl" + File.separator + ""+pageId+"-"+styleId+"-"+headerId+"-"+blockId+"-h.png";
    }

    public String getFooterThumbnail() {
        return Global.upload + File.separator + "tpl" + File.separator + ""+pageId+"-"+styleId+"-"+footerId+"-"+blockId+"-f.png";
    }

}
