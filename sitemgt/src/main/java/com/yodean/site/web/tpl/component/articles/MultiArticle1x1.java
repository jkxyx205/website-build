package com.yodean.site.web.tpl.component.articles;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public class MultiArticle1x1 extends MultiArticle {

    public static final Integer CONTENT_TYPE = 153;

    public static final Integer ASPECTRATIO_W = 1;

    public static final Integer ASPECTRATIO_H = 1;

    public static final MultiArticle multiArticle = new MultiArticle1x1();

    private MultiArticle1x1() {
        super(CONTENT_TYPE, ASPECTRATIO_W, ASPECTRATIO_H);
    }

    public static MultiArticle getInstance() {
        return multiArticle;
    }
}
