package com.yodean.site.web.tpl.component.senior;

import com.yodean.site.web.content.entity.Pic;
import com.yodean.site.web.tpl.component.AbstractPicComponent;
import com.yodean.site.web.tpl.component.utils.DefaultContent;

import java.util.List;

/**
 * Created by rick on 2017/11/7.
 * 固定比例
 */
public class CarouselPic extends AbstractPicComponent {
    public static final Integer CONTENT_TYPE = 202;

    public static final String DESCRIPTION = "轮播图";

    public static final Integer ASPECTRATIO_W = 14;

    public static final Integer ASPECTRATIO_H = 5;

    private static final CarouselPic carouselPic = new CarouselPic();

    private CarouselPic() {
        super(CONTENT_TYPE, DESCRIPTION, ASPECTRATIO_W, ASPECTRATIO_H);
    }

    public static CarouselPic getInstance() {
        return carouselPic;
    }


    @Override
    public String gotoSettingPage() {
        return "web/settings/carousel-pic";
    }

    @Override
    protected Integer addContent(Integer categoryId, Integer initNum) {
        List<Pic> list = DefaultContent.getDefaultCarouselPic();
        list.get(0).setOptions("{\"position\":\"banner-center\",\"opacity\":0,\"fontSize\":\"36px\",\"fontStyle\":\"normal\",\"fontFamily\":\"微软雅黑\",\"color\":\"#ffffff\",\"fontSize2\":\"20px\",\"fontStyle2\":\"normal\",\"fontFamily2\":\"微软雅黑\",\"color2\":\"#ffffff\"}");
        return super.addContent(categoryId, list);
    }

    @Override
    public String gotoContentPage(Integer webId, Integer categoryId) {
        return "/web/"+webId+"/content/pic/carousel-list/" + categoryId + "?aspectRatioW="+this.getAspectRatioW()+"&aspectRatioH="+this.getAspectRatioH()+"";
    }

}
