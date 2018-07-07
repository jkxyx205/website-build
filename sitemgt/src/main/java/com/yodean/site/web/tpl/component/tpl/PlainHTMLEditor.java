package com.yodean.site.web.tpl.component.tpl;

/**
 * Created by rick on 2017/11/7.
 */
public class PlainHTMLEditor extends AbstractHTMLEditor {
    public static final Integer CONTENT_TYPE = 501;

    public static final String DESCRIPTION = "纯文字页面";

    private static final PlainHTMLEditor htmlEditor = new PlainHTMLEditor(CONTENT_TYPE, DESCRIPTION);

    protected PlainHTMLEditor(Integer contentType, String description) {
        super(contentType, description);
    }

    @Override
    protected String getTplHTML() {
        return "<style type=\"text/css\">.main-block {\n" +
                "    text-align: center; \n" +
                "    color:#fff; \n" +
                "    position:relative; \n" +
                "  } \n" +
                "\n" +
                " @media (min-width: 64em) {\n" +
                "  .main-block {\n" +
                "    height: 300px;\n" +
                "  } \n" +
                "\n" +
                "  \n" +
                "  .main-block>div {\n" +
                "    position: absolute;top:50%;left:50%; transform: translate(-50%, -50%);-webkit-transform: translate(-50%, -50%);-moz-transform: translate(-50%, -50%);-ms-transform: translate(-50%, -50%);\n" +
                "  }\n" +
                " }\n" +
                "\n" +
                "  @media (max-width: 64em) {\n" +
                "         .main-block>div {\n" +
                "          padding: 20px;\n" +
                "        }\n" +
                "  }\n" +
                "</style>\n" +
                "<div class=\"main-block\">\n" +
                "<div>\n" +
                "<h1>前方也在下雨，何必匆忙赶路</h1>\n" +
                "\n" +
                "<p style=\"line-height:1.6;\">平凡如你我者，应是这个世界上的绝大多数。成功有着各种各样的定义，即便你觉得自己非常失败，那也只是某种意义上的失败；即便你觉得自己特别成功，那也只是和某些人比起来成功。</p>\n" +
                "<a class=\"btn\" href=\"http://www.baidu.com\" style=\"margin-top: 15px; color: #fff;border-color: #fff;\">查看更多</a></div>\n" +
                "</div>\n";
    }


    public static PlainHTMLEditor getInstance() {
        return htmlEditor;
    }



}
