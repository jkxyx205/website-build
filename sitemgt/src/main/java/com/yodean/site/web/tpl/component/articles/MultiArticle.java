package com.yodean.site.web.tpl.component.articles;

import com.yodean.site.web.tpl.component.AbstractArticleComponent;


/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public abstract class MultiArticle extends AbstractArticleComponent {


    public static final String DESCRIPTION = "文章列表";

    protected MultiArticle(Integer contentType, Integer aspectRatioW, Integer aspectRatioH) {
        super(contentType, DESCRIPTION, aspectRatioW, aspectRatioH);
    }


    public String gotoSettingPage() {
        return "web/settings/article-list";
    }

    @Override
    public String gotoContentPage(Integer webId, Integer categoryId) {
        return "/web/"+webId+"/content/article/list/" + categoryId + "?aspectRatioW="+this.getAspectRatioW()+"&aspectRatioH="+this.getAspectRatioH()+"";
    }

}
