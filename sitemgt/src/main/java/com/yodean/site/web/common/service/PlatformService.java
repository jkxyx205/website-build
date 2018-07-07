package com.yodean.site.web.common.service;

import com.rick.dev.config.Global;
import com.yodean.site.web.content.entity.Site;
import com.yodean.site.web.content.service.SiteService;
import com.yodean.site.web.tpl.entity.Footer;
import com.yodean.site.web.tpl.entity.Header;
import com.yodean.site.web.tpl.entity.Page;
import com.yodean.site.web.tpl.entity.Theme;
import com.yodean.site.web.tpl.service.*;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.yodean.site.web.common.service.StaticService.HTML_DIR;

/**
 * Created by rick on 2017/9/5.
 */
@Service
public class PlatformService {
    public final static Integer WEB_ID = 5;
    private static final int STYLE_NUM = 9;
    private static final int BLOCK_NUM = 8;

    @Resource
    private PageService pageService;
    @Resource
    private MenuService menuService;
    @Resource
    private ContentService contentService;
    @Resource
    private SiteService siteService;

    @Resource
    private HeaderService headerService;

    @Resource
    private FooterService footerService;

    @Value("${upload}")
    private String upload;

    @Value("${domain}")
    private String domain;

    @Value("${phantomjs}")
    private String phantomjs;

    @Resource
    private StaticService staticService;

    @Resource
    private IndexService indexService;

    @Resource
    private ThemeService themeService;

    public void publish(Integer webId) throws IOException {
        staticService.staticSite(webId); //页面静态化

        Site site = siteService.findById(webId);
        site.setVersion(site.getVersion() + 1);
        site.setStatus(SiteService.STATUS_PUBLISHED);
        siteService.save(site);

        //更新全文索引
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    indexService.buildIndex(webId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //首页快照
        new Thread(new Runnable() {
            @Override
            public void run() {

                String url = "http://" + Global.domain + "/web/"+webId+"/view/page/index?lazyload=false";
                String folder = upload + File.separator + Global.SITE_FOLDER + File.separator + webId + File.separator + HTML_DIR + File.separator;

                String srcPath = folder + "index.png";
                try {
                    screenShot(url, srcPath);
                    //
                    Thumbnails.of(srcPath)
                            .scale(.5f)
                            .toFile(new File(folder, "index-thumbnail.png"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }



    public String getPageHtmlByPage(final Site site,Integer pageId, final boolean full) {
        final Page page = pageService.getPageById(pageId);
        final Integer webId = page.getWebId();
        final String pageName = page.getName();
        return siteService.getPageHtml(site, pageId, new LayoutHtml() {

            public String html(Map<String, Object> params) {
                params.put("activeId", menuService.getActiveMenu(webId, pageName));
                if (full) {
                    return contentService.injectPageContent(webId, page.getHtml());
                } else {
                    return "";
                }

            }
        });
    }

    public String screenShot(String url, String filePath) throws IOException {
        Runtime rt = Runtime.getRuntime();
        String cmd = phantomjs + File.separator + "bin"+File.separator+"phantomjs "+phantomjs+ File.separator + "bin"+File.separator+"image.js "+url+" "+filePath+"";

        Process p = rt.exec(cmd);
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sbf = new StringBuffer();
        String tmp;
        while((tmp = br.readLine())!=null){
            sbf.append(tmp);
        }
        br.close();
        is.close();
        return sbf.toString();
    }

    public void allSite2HTML() {
        File folder = new File(upload + File.separator + "tpl");
        if (!folder.exists()) {
            folder.mkdir();
        } else {
//            try {
//                FileUtils.cleanDirectory(new File(folder, "style"));
//                FileUtils.cleanDirectory(new File(folder, "header"));
//                FileUtils.cleanDirectory(new File(folder, "footer"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

//        List<Site> sites = siteService.getAllSites();
        List<Header> headers = headerService.getHeaders();
        List<Footer> footers = footerService.getFooters();

        List<Theme> themeList = new ArrayList<Theme>();
        Site site = siteService.findById(WEB_ID);
//        for (Site site : sites) {
            Collection<Page> pages = pageService.getPageMappingByWebId(site.getId()).values();
            for (Page page : pages) {
                if ("index".equals(page.getName())) {
                    for(int j = 1; j <= STYLE_NUM ; j++) {
                        for (int i = 1; i <= BLOCK_NUM ; i++) {
                            for(Header header : headers) {
                                for(Footer footer : footers) {
                                    String url = domain + "tool/platform/"+page.getId()+"/"+j+"/"+header.getId() + "/" + footer.getId() + "/" + i;
                                    String name = page.getId() + "-" + j +  "-" +  header.getId() + "-" + footer.getId() + "-" + i +  ".png";
                                    String path = upload + "/tpl/style/" + name;

                                    try {
                                        if(!new File(path).exists())
                                            screenShot(url,path);

//                                        Theme theme = new Theme(page.getId(), j, header.getId(), footer.getId(), i);
//                                        themeList.add(theme);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
//                    themeService.initThemes(themeList);
                    //页眉
//                    for(int j = 1; j <= STYLE_NUM ; j++) {
//                        for(int i = 1; i <= BLOCK_NUM ; i++) {
//                            for(Header header : headers) {
//                                String url = domain + "tool/platform/header/"+page.getId()+"/"+j+"/"+header.getId() + "/" + i;
//                                String name = page.getId() + "-" + j +  "-" +  header.getId()  + "-" + i +  ".png";
//                                String path = upload + "/tpl/header/" + name;
//                                try {
//                                    screenShot(url,path);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        }
//                    }

                    //页脚
//                    for(int j = 1; j <= STYLE_NUM ; j++) {
//                        for(int i = 1; i <= BLOCK_NUM ; i++) {
//                            for(Footer footer : footers) {
//                                String url = domain + "tool/platform/footer/"+page.getId()+"/"+ j + "/" + footer.getId() + "/" + i;
//                                String name = page.getId() + "-" + j +  "-" + footer.getId() + "-" + i +  ".png";
//                                String path = upload + "/tpl/footer/" + name;
//                                try {
//                                    screenShot(url,path);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        }
//                    }
                }
            }
//        }


    }

    public String siteHTML(Integer pageId, Integer styleId, Integer header, Integer footer, Integer blockId, boolean full) {
        Integer webId = pageService.getPageById(pageId).getWebId();
        Site site = siteService.findById(webId);

        site.setId(webId);
        site.setStyleId(styleId);
        site.setHeaderId(header);
        site.setFooterId(footer);
        site.setBlockId(blockId);

        String html = getPageHtmlByPage(site, pageId, full);
        return html;
    }

}
