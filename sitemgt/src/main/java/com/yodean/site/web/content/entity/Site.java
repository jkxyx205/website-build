package com.yodean.site.web.content.entity;

import com.rick.dev.config.Global;
import com.rick.dev.persistence.DataEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by rick on 2017/7/12.
 */
@Entity
@Table(name = "w_site")
public class Site extends DataEntity<Site> {
    private String name;

    private String title;

    private String domain;

    private String type;

    private String keywords;

    private String description;

    @Column(name = "header_id")
    private Integer headerId;

    @Column(name="footer_id")
    private Integer footerId;

    private String tpl;

    @Column(name="style_id")
    private Integer styleId;

    @Column(name="block_id")
    private Integer blockId;

    private String status;

    private String logo;

    private String tel;

    private String email;

    private String fax;

    private String addr;

    private String wechat;

    private String weibo;

    private String favicon;

    private String icp;

    private String lang;

    private String owner;

    private String gateman;

    private String phone;

    private Integer version;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTpl() {
        return tpl;
    }

    public void setTpl(String tpl) {
        this.tpl = tpl;
    }

    public Integer getStyleId() {
        return styleId;
    }

    public void setStyleId(Integer styleId) {
        this.styleId = styleId;
    }

    public Integer getBlockId() {
        return blockId;
    }

    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }


    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getIcp() {
        return icp;
    }

    public void setIcp(String icp) {
        this.icp = icp;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGateman() {
        return gateman;
    }

    public void setGateman(String gateman) {
        this.gateman = gateman;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getVisit() {
        if(StringUtils.isNotEmpty(this.domain)) {
            return "http://" + this.domain;
        }

        return "http://" + this.name + "." + Global.domain;
    }

    public String getStyleCSS() {
        String style = getStyleId() + "/sass/" + getBlockId() + ".css";
        return "style/" + style;
    }

    public String getFooterCSS() {
        String style = getStyleId() + "/sass/" + getBlockId() + ".css";
        return "footer/" + getFooterId()  + "/"+ style;

    }

    public String getHeaderCSS() {
        String style = getStyleId() + "/sass/" + getBlockId() + ".css";
        return "header/" + getHeaderId()  + "/"+ style;
    }
}
