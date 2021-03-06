package com.yodean.site.web.tpl.component.videos;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public class MultiVideo14x5 extends MultiVideo {

    public static final Integer CONTENT_TYPE = 355;

    public static final Integer ASPECTRATIO_W = 14;

    public static final Integer ASPECTRATIO_H = 5;

    public static final MultiVideo multiPic = new MultiVideo14x5();

    private MultiVideo14x5() {
        super(CONTENT_TYPE, ASPECTRATIO_W, ASPECTRATIO_H);
    }

    public static MultiVideo getInstance() {
        return multiPic;
    }
}
