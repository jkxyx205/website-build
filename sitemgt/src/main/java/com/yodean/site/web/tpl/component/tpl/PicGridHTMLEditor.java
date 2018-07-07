package com.yodean.site.web.tpl.component.tpl;

import com.rick.dev.config.Global;

/**
 * Created by rick on 2017/11/7.
 */
public class PicGridHTMLEditor extends AbstractHTMLEditor {
    public static final Integer CONTENT_TYPE = 503;

    public static final String DESCRIPTION = "图片导航";

    private static final PicGridHTMLEditor htmlEditor = new PicGridHTMLEditor(CONTENT_TYPE, DESCRIPTION);

    protected PicGridHTMLEditor(Integer contentType, String description) {
        super(contentType, description);
    }

    @Override
    protected String getTplHTML() {
        return "<div>\n" +
                "<div style=\"width: 33.333333%; float:left;\"><a class=\"cover\" href=\"\"> <img alt=\"\" src=\""+ Global.fileServer+"/tpl/init/3x4/1.jpg\" style=\"width: 100%;\" /> </a></div>\n" +
                "\n" +
                "<div style=\"width: 44.444444%; float: left;\"><a class=\"cover\" href=\"\"> <img alt=\"\" src=\""+Global.fileServer+"/tpl/init/2x1/1.jpg\" style=\"width: 100%;\" /> </a></div>\n" +
                "\n" +
                "<div style=\"width: 22.222222%; float: left;\"><a class=\"cover\" href=\"\"> <img alt=\"\" src=\""+Global.fileServer+"/tpl/init/1x1/3.jpg\" style=\"width: 100%;\" /> </a></div>\n" +
                "\n" +
                "<div style=\"width: 22.222222%; float: left;\"><a class=\"cover\" href=\"\"> <img alt=\"\" src=\""+Global.fileServer+"/tpl/init/1x1/4.jpg\" style=\"width: 100%;\" /> </a></div>\n" +
                "\n" +
                "<div style=\"width: 44.444444%; float: left;\"><a class=\"cover\" href=\"\"> <img alt=\"\" src=\""+Global.fileServer+"/tpl/init/2x1/2.jpg\" style=\"width: 100%;\" /> </a></div>\n" +
                "<div style=\"clear: both;\"></div>" +
                "</div>";
    }


    public static PicGridHTMLEditor getInstance() {
        return htmlEditor;
    }



}
