package com.yodean.site.web.tpl.component.senior;

import com.yodean.site.web.content.entity.Pic;
import com.yodean.site.web.tpl.component.SinglePic;
import com.yodean.site.web.tpl.component.utils.DefaultContent;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public class BMap extends SinglePic {

    public static final Integer CONTENT_TYPE = 402;

    public static final String DESCRIPTION = "地图";

    public static final BMap map = new BMap(CONTENT_TYPE, DESCRIPTION);

    protected BMap(Integer contentType, String description) {
        super(contentType, description);
    }

    public static BMap getInstance() {
        return map;
    }

    @Override
    protected Integer addContent(Integer categoryId, Integer initNum) {
        Pic pic = DefaultContent.getDefaultPics(0,0, 1).get(0);
        pic.setTitle("苏州创意产业园");
        pic.setCategoryId(categoryId);
        picService.save(pic);
        return categoryId;
    }

    @Override
    public String gotoSettingPage() {
        return "web/settings/map";
    }

    @Override
    public String gotoContentPage(Integer webId, Integer categoryId) {
        return "/web/"+webId+"/content/pic/map/" + categoryId;
    }
}
