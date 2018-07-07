package com.yodean.site.web.tpl.component.pics;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public class MultiPic2x3 extends MultiPic {

    public static final Integer CONTENT_TYPE = 254;

    public static final Integer ASPECTRATIO_W = 2;

    public static final Integer ASPECTRATIO_H = 3;

    public static final MultiPic multiPic = new MultiPic2x3();

    private MultiPic2x3() {
        super(CONTENT_TYPE, ASPECTRATIO_W, ASPECTRATIO_H);
    }

    public static MultiPic getInstance() {
        return multiPic;
    }
}
