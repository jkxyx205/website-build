package com.yodean.site.web.tpl.component.pics;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public class MultiPic14x5 extends MultiPic {

    public static final Integer CONTENT_TYPE = 255;

    public static final Integer ASPECTRATIO_W = 14;

    public static final Integer ASPECTRATIO_H = 5;

    public static final MultiPic multiPic = new MultiPic14x5();

    private MultiPic14x5() {
        super(CONTENT_TYPE, ASPECTRATIO_W, ASPECTRATIO_H);
    }

    public static MultiPic getInstance() {
        return multiPic;
    }
}
