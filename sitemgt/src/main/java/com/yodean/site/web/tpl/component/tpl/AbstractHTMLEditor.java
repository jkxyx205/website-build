package com.yodean.site.web.tpl.component.tpl;

import com.yodean.site.web.content.entity.Article;
import com.yodean.site.web.tpl.component.SingleArticle;
import com.yodean.site.web.tpl.component.utils.DefaultContent;

/**
 * Created by rick on 2017/11/7.
 */
public abstract class AbstractHTMLEditor extends SingleArticle {

    protected AbstractHTMLEditor(Integer contentType, String description) {
        super(contentType, description);
    }


    @Override
    public String gotoSettingPage() {
        return "web/settings/html";
    }

    @Override
    public String gotoContentPage(Integer webId, Integer categoryId) {
        return "/web/"+webId+"/content/article/html/" + categoryId;
    }

    @Override
    public Integer addContent(Integer categoryId, Integer initNum) {
        Article article = DefaultContent.getDefaultArticles(0,0, 1).get(0);
        article.setHtml(getTplHTML());
        article.setCategoryId(categoryId);
        articleService.save(article);
        return categoryId;
    }

    protected abstract String getTplHTML();


}
