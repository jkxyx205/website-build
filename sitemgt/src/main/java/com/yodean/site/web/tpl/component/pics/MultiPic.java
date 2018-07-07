package com.yodean.site.web.tpl.component.pics;

import com.yodean.site.web.tpl.component.AbstractPicComponent;


/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public abstract class MultiPic extends AbstractPicComponent {

    public static final String DESCRIPTION = "图片列表";

    protected MultiPic(Integer contentType, Integer aspectRadioW, Integer aspectRadioH) {
        super(contentType, DESCRIPTION, aspectRadioW, aspectRadioH);
    }

    public String gotoSettingPage() {
        return "web/settings/pic-list";
    }

    @Override
    public String gotoContentPage(Integer webId, Integer categoryId) {
        return "/web/"+webId+"/content/pic/list/" + categoryId + "?aspectRatioW="+this.getAspectRatioW()+"&aspectRatioH="+this.getAspectRatioH()+"";
    }

}
