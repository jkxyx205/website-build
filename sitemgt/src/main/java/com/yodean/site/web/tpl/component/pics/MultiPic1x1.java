package com.yodean.site.web.tpl.component.pics;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public class MultiPic1x1 extends MultiPic {

    public static final Integer CONTENT_TYPE = 253;

    public static final Integer ASPECTRATIO_W = 1;

    public static final Integer ASPECTRATIO_H = 1;

    public static final MultiPic multiPic = new MultiPic1x1();

    private MultiPic1x1() {
        super(CONTENT_TYPE, ASPECTRATIO_W, ASPECTRATIO_H);
    }

    public static MultiPic getInstance() {
        return multiPic;
    }
}
