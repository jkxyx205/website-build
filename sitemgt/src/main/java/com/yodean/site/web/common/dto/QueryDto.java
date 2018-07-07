package com.yodean.site.web.common.dto;

/**
 * Created by rick on 2017/9/20.
 */
public class QueryDto {
    private String type;

    private String href;

    private String cover;

    private String title;

    private String content;

    public QueryDto(String type, String href, String cover, String title, String content) {
        this.type = type;
        this.href = href;
        this.cover = cover;
        this.title = title;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getTypeStr() {
        if ("0".equals(type)) {
            return "图文";
        } else if ("1".equals(type)) {
            return "图片";
        } else if ("2".equals(type)) {
            return "视频";
        } else {
            return "其他";
        }
    }

    public String getHref() {
        return href;
    }

    public String getCover() {
        return cover;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
