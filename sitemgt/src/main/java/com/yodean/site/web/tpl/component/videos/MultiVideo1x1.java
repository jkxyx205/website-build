package com.yodean.site.web.tpl.component.videos;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public class MultiVideo1x1 extends MultiVideo {

    public static final Integer CONTENT_TYPE = 353;

    public static final Integer ASPECTRATIO_W = 1;

    public static final Integer ASPECTRATIO_H = 1;

    public static final MultiVideo multiPic = new MultiVideo1x1();

    private MultiVideo1x1() {
        super(CONTENT_TYPE, ASPECTRATIO_W, ASPECTRATIO_H);
    }

    public static MultiVideo getInstance() {
        return multiPic;
    }
}
