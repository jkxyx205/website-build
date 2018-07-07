package com.yodean.site.web.tpl.component.videos;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public class MultiVideo4x3 extends MultiVideo {

    public static final Integer CONTENT_TYPE = 351;

    public static final Integer ASPECTRATIO_W = 4;

    public static final Integer ASPECTRATIO_H = 3;

    public static final MultiVideo multiPic = new MultiVideo4x3();

    private MultiVideo4x3() {
        super(CONTENT_TYPE, ASPECTRATIO_W, ASPECTRATIO_H);
    }

    public static MultiVideo getInstance() {
        return multiPic;
    }
}
