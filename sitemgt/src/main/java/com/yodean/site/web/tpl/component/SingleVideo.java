package com.yodean.site.web.tpl.component;

/**
 * Created by rick on 2017/11/7.
 */
public class SingleVideo extends AbstractVideoComponent {
    public static final Integer CONTENT_TYPE = 301;

    public static final String DESCRIPTION = "在线视频";


    private static final SingleVideo singleVideo = new SingleVideo();

    private SingleVideo() {
        this(CONTENT_TYPE, DESCRIPTION);
    }

    protected SingleVideo(Integer contentType, String description) {
        super(contentType, description, 0, 0);
    }

    public static SingleVideo getInstance() {
        return singleVideo;
    }


    @Override
    public String gotoSettingPage() {
        return "web/settings/video";
    }

    @Override
    public String gotoContentPage(Integer webId, Integer categoryId) {
        return "/web/"+webId+"/content/video/single/" + categoryId;
    }

}
