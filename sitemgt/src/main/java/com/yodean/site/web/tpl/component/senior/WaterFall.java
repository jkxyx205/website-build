package com.yodean.site.web.tpl.component.senior;

import com.yodean.site.web.tpl.component.pics.MultiPic;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public class WaterFall extends MultiPic {

    public static final Integer CONTENT_TYPE = 203;

    public static final Integer ASPECTRATIO_W = 0;

    public static final Integer ASPECTRATIO_H = 0;

    public static final MultiPic multiPic = new WaterFall();

    private WaterFall() {
        super(CONTENT_TYPE, ASPECTRATIO_W, ASPECTRATIO_H);
    }

    public static MultiPic getInstance() {
        return multiPic;
    }

    @Override
    public String gotoSettingPage() {
        return "web/settings/waterfall";
    }

    @Override
    public String gotoContentPage(Integer webId, Integer categoryId) {
        return "/web/"+webId+"/content/pic/list/" + categoryId + "?listPage=waterfall-list&aspectRatioW="+this.getAspectRatioW()+"&aspectRatioH="+this.getAspectRatioH()+"";
    }
}
