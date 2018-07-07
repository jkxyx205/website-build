package com.yodean.site.web.tpl.component.videos;

import com.yodean.site.web.tpl.component.AbstractVideoComponent;


/**
 * Created by rick on 2017/11/7.
 * 多视频
 */
public abstract class MultiVideo extends AbstractVideoComponent {

    public static final String DESCRIPTION = "视频列表";

    protected MultiVideo(Integer contentType, Integer aspectRadioW, Integer aspectRadioH) {
        super(contentType, DESCRIPTION, aspectRadioW, aspectRadioH);
    }

    public String gotoSettingPage() {
        return "web/settings/video-list";
    }

    @Override
    public String gotoContentPage(Integer webId, Integer categoryId) {
        return "/web/"+webId+"/content/video/list/" + categoryId + "?aspectRatioW="+this.getAspectRatioW()+"&aspectRatioH="+this.getAspectRatioH()+"";
    }

}
