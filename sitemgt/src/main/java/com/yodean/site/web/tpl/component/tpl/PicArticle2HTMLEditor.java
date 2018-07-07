package com.yodean.site.web.tpl.component.tpl;

import com.rick.dev.config.Global;

/**
 * Created by rick on 2017/11/7.
 */
public class PicArticle2HTMLEditor extends AbstractHTMLEditor {
    public static final Integer CONTENT_TYPE = 504;

    public static final String DESCRIPTION = "上图下文";

    private static final PicArticle2HTMLEditor htmlEditor = new PicArticle2HTMLEditor(CONTENT_TYPE, DESCRIPTION);

    protected PicArticle2HTMLEditor(Integer contentType, String description) {
        super(contentType, description);
    }

    @Override
    protected String getTplHTML() {
        return "<div>\n" +
                "<div>\n" +
                "<p style=\"text-align:center\"><img alt=\"\" src=\""+ Global.fileServer+"/tpl/init/14x5/10.jpg\" style=\"width: 100%;\" /></p>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"content\">\n" +
                "<h1>关于我们</h1>\n" +
                "\n" +
                "<p style=\"line-height: 1.6\">点研智能软件（集团）成立于2015年，是亚太地区领先的企业管理软件、企业互联网服务和企业金融服务提供商，是中国最大的ERP、CRM、人力资源管理、商业分析、内审、小微企业管理软件和财政、汽车、烟草等行业应用解决方案提供商。点研智能软件平台是中国大型企业和组织应用最广泛的企业互联网开放平台，畅捷通平台支持千万级小微企业公有云服务。点研智能软件平台在金融、医疗卫生、电信、能源等行业应用以及企业协同、企业通信、企业支付、P2P、培训教育、管理咨询等服务领域快速发展。 </p>\n" +
                "<a class=\"btn\" href=\"#\" style=\"margin-top: 20px;\">查看更多</a></div>\n" +
                "\n" +
                "<div style=\"clear:both;\">&nbsp;</div>\n" +
                "</div>\n";
    }


    public static PicArticle2HTMLEditor getInstance() {
        return htmlEditor;
    }



}
