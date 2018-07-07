package com.yodean.site.web.tpl.component;

import com.yodean.site.web.tpl.component.articles.*;
import com.yodean.site.web.tpl.component.pics.*;
import com.yodean.site.web.tpl.component.senior.BMap;
import com.yodean.site.web.tpl.component.senior.CarouselPic;
import com.yodean.site.web.tpl.component.senior.Layout;
import com.yodean.site.web.tpl.component.senior.WaterFall;
import com.yodean.site.web.tpl.component.tpl.*;
import com.yodean.site.web.tpl.component.videos.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rick on 2017/11/7.
 */
public class RegistrationCenter {
    private static final Map<Integer, AbstractComponent> componentMap = new HashMap<Integer, AbstractComponent>() {
    };

    static { //注册控件类型
        //单图文    type = 1
        componentMap.put(SingleArticle.CONTENT_TYPE, SingleArticle.getInstance());

        //多图文 type = 151-200
        componentMap.put(MultiArticle4x3.CONTENT_TYPE, MultiArticle4x3.getInstance());
        componentMap.put(MultiArticle9x5.CONTENT_TYPE, MultiArticle9x5.getInstance());
        componentMap.put(MultiArticle1x1.CONTENT_TYPE, MultiArticle1x1.getInstance());
        componentMap.put(MultiArticle2x3.CONTENT_TYPE, MultiArticle2x3.getInstance());
        componentMap.put(MultiArticle14x5.CONTENT_TYPE, MultiArticle14x5.getInstance());


        //单图片  type = 201
        componentMap.put(SinglePic.CONTENT_TYPE, SinglePic.getInstance());

        //定比例轮播图 type = 202
        componentMap.put(CarouselPic.CONTENT_TYPE, CarouselPic.getInstance());
        //瀑布流
        componentMap.put(WaterFall.CONTENT_TYPE, WaterFall.getInstance());

        //多图片 type = 251-300
        componentMap.put(MultiPic4x3.CONTENT_TYPE, MultiPic4x3.getInstance());
        componentMap.put(MultiPic9x5.CONTENT_TYPE, MultiPic9x5.getInstance());
        componentMap.put(MultiPic1x1.CONTENT_TYPE, MultiPic1x1.getInstance());
        componentMap.put(MultiPic2x3.CONTENT_TYPE, MultiPic2x3.getInstance());
        componentMap.put(MultiPic14x5.CONTENT_TYPE, MultiPic14x5.getInstance());


        //单视频     type = 301
        componentMap.put(SingleVideo.CONTENT_TYPE, SingleVideo.getInstance());

        //多视频 ID = 371, 374, 352, 356, 355, 377, 369
        componentMap.put(MultiVideo4x3.CONTENT_TYPE, MultiVideo4x3.getInstance());
        componentMap.put(MultiVideo9x5.CONTENT_TYPE, MultiVideo9x5.getInstance());
        componentMap.put(MultiVideo1x1.CONTENT_TYPE, MultiVideo1x1.getInstance());
        componentMap.put(MultiVideo2x3.CONTENT_TYPE, MultiVideo2x3.getInstance());
        componentMap.put(MultiVideo14x5.CONTENT_TYPE, MultiVideo14x5.getInstance());


        //高级 401 402 403
        componentMap.put(HTMLEditor.CONTENT_TYPE, HTMLEditor.getInstance());
        componentMap.put(BMap.CONTENT_TYPE, BMap.getInstance());

        componentMap.put(Layout.CONTENT_TYPE, Layout.getInstance());

        //图文模版 501 502 503 504 505 506
        componentMap.put(PlainHTMLEditor.CONTENT_TYPE, PlainHTMLEditor.getInstance());
        componentMap.put(PicArticleHTMLEditor.CONTENT_TYPE, PicArticleHTMLEditor.getInstance());
        componentMap.put(PicGridHTMLEditor.CONTENT_TYPE, PicGridHTMLEditor.getInstance());
        componentMap.put(PicArticle2HTMLEditor.CONTENT_TYPE, PicArticle2HTMLEditor.getInstance());
        componentMap.put(PicArticle3HTMLEditor.CONTENT_TYPE, PicArticle3HTMLEditor.getInstance());
        componentMap.put(PicArticle4HTMLEditor.CONTENT_TYPE, PicArticle4HTMLEditor.getInstance());



    }

    public static final AbstractComponent getComponentType(Integer componentType) {
        return componentMap.get(componentType);
    }

}
