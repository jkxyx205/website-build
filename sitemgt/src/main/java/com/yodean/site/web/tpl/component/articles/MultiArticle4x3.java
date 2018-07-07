package com.yodean.site.web.tpl.component.articles;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public class MultiArticle4x3 extends MultiArticle {

    public static final Integer CONTENT_TYPE = 151;

    public static final Integer ASPECTRATIO_W = 4;

    public static final Integer ASPECTRATIO_H = 3;

    public static final MultiArticle multiArticle = new MultiArticle4x3();

    private MultiArticle4x3() {
        super(CONTENT_TYPE, ASPECTRATIO_W, ASPECTRATIO_H);
    }

    public static MultiArticle getInstance() {
        return multiArticle;
    }
}
