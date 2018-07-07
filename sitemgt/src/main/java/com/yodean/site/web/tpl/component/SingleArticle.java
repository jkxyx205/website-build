package com.yodean.site.web.tpl.component;

/**
 * Created by rick on 2017/11/7.
 */
public class SingleArticle extends AbstractArticleComponent  {
    public static final Integer CONTENT_TYPE = 1;

    public static final String DESCRIPTION = "图文信息";

    private static final SingleArticle singleArticle = new SingleArticle();

    private SingleArticle() {
        this(CONTENT_TYPE, DESCRIPTION);
    }

    protected SingleArticle(Integer contentType, String description) {
        super(contentType, description, 0, 0);
    }

    public static SingleArticle getInstance() {
        return singleArticle;
    }


    @Override
    public String gotoSettingPage() {
        return "web/settings/article";
    }

    @Override
    public String gotoContentPage(Integer webId, Integer categoryId) {
        return "/web/"+webId+"/content/article/single/" + categoryId;
    }
}
