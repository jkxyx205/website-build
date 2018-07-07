package com.yodean.site.web.tpl.component;

/**
 * Created by rick on 2017/11/7.
 */
public class SinglePic extends AbstractPicComponent {
    public static final Integer CONTENT_TYPE = 201;

    public static final String DESCRIPTION = "单图片";


    private static final SinglePic singlePic = new SinglePic();

    private SinglePic() {
        this(CONTENT_TYPE, DESCRIPTION);
    }

    protected SinglePic(Integer contentType, String description) {
        super(contentType, description, 0, 0);
    }

    public static SinglePic getInstance() {
        return singlePic;
    }


    @Override
    public String gotoSettingPage() {
        return "web/settings/pic";
    }

    @Override
    public String gotoContentPage(Integer webId, Integer categoryId) {
        return "/web/"+webId+"/content/pic/single/" + categoryId;
    }

}
