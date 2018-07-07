package com.yodean.site.web.tpl.component.articles;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public class MultiArticle14x5 extends MultiArticle {

    public static final Integer CONTENT_TYPE = 155;

    public static final Integer ASPECTRATIO_W = 14;

    public static final Integer ASPECTRATIO_H = 5;

    public static final MultiArticle multiArticle = new MultiArticle14x5();

    private MultiArticle14x5() {
        super(CONTENT_TYPE, ASPECTRATIO_W, ASPECTRATIO_H);
    }

    public static MultiArticle getInstance() {
        return multiArticle;
    }
}
