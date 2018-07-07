package com.yodean.site.web.tpl.component.tpl;

/**
 * Created by rick on 2017/11/7.
 */
public class HTMLEditor extends AbstractHTMLEditor {
    public static final Integer CONTENT_TYPE = 401;

    public static final String DESCRIPTION = "内嵌页面";

    private static final HTMLEditor htmlEditor = new HTMLEditor(CONTENT_TYPE, DESCRIPTION);

    protected HTMLEditor(Integer contentType, String description) {
        super(contentType, description);
    }

    public static HTMLEditor getInstance() {
        return htmlEditor;
    }

    @Override
    protected String getTplHTML() {
        return "<div><div style=\"text-align: center\">你好，点研科技</div></div>";
    }
}
