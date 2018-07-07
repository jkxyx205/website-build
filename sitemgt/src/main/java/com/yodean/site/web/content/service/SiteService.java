package com.yodean.site.web.content.service;

import com.rick.dev.config.Global;
import com.rick.dev.persistence.DataEntity;
import com.rick.dev.service.DefaultService;
import com.rick.dev.service.JdbcTemplateService;
import com.rick.dev.utils.JacksonUtils;
import com.rick.dev.utils.VelocityUtils;
import com.yodean.site.web.common.cache.PageCache;
import com.yodean.site.web.common.cache.SiteUtils;
import com.yodean.site.web.common.dto.QueryDto;
import com.yodean.site.web.common.service.IndexService;
import com.yodean.site.web.content.entity.Site;
import com.yodean.site.web.tpl.component.SingleVideo;
import com.yodean.site.web.tpl.dao.SiteDao;
import com.yodean.site.web.tpl.dto.PageInstance;
import com.yodean.site.web.tpl.entity.Footer;
import com.yodean.site.web.tpl.entity.Header;
import com.yodean.site.web.tpl.entity.Menu;
import com.yodean.site.web.tpl.entity.Page;
import com.yodean.site.web.tpl.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * Created by rick on 2017/7/12.
 */
@Service
public class SiteService extends DefaultService {

    public static enum SiteStatus {
        DESIGN,VIEW,PUBLISH
    }

    public static final String STATUS_BUILDING = "0";

    public static final String STATUS_PUBLISHED = "1";

    public static final String STATUS_STOP = "2";

    @Resource
    private HeaderService headerService;

    @Resource
    private FooterService footerService;

    @Resource
    private PageService pageService;


    @Resource
    private ContentService contentService;

    @Resource
    private MenuService menuService;

    @Resource
    private SiteDao siteDao;

    @Resource
    private IndexService indexService;

    public void save(Site site) {
        site.preSave();
        siteDao.save(site);
        SiteUtils.set(site.getId(), site);
    }

    public void delete(Integer id) {
        Site site = findById(id);
        site.setDelFlag(Site.DEL_FLAG_REMOVE);
        site.setDomain(null);
        save(site);
    }

    public List<Site> getAllSites() {
        Site site = new Site();
        site.setDelFlag(Site.DEL_FLAG_NORMAL);

        Example<Site> siteExample = Example.of(site);

        return siteDao.findAll(siteExample, new Sort(Sort.Direction.DESC, "id"));
    }

    public Site findById(Integer id) {
        Site site =SiteUtils.get(id);
        if (site == null) {
            site = siteDao.findOne(id);
            SiteUtils.set(site.getId(), site);
        }
        return site;
    }

    public Site findByName(String name) {
       Site site = new Site();
       site.setName(name);
       site.setDelFlag(DataEntity.DEL_FLAG_NORMAL);
       Example<Site> ex = Example.of(site);
       return siteDao.findOne(ex);
    }

    public Site findByDomain(String domain) {
        Site site = new Site();
        site.setDomain(domain);
        site.setDelFlag(DataEntity.DEL_FLAG_NORMAL);
        Example<Site> ex = Example.of(site);
        return siteDao.findOne(ex);
    }



    private Page getPageByName(String siteName, String pageName) {
        String queryName = "SELECT\n" +
                "\tid, name, web_id,html,layout_id as layoutId\n" +
                "FROM\n" +
                "\tw_page\n" +
                "WHERE\n" +
                "\tNAME = :pageName AND EXISTS (\n" +
                "\t\tSELECT\n" +
                "\t\t\t1\n" +
                "\t\tFROM\n" +
                "\t\t\tw_site\n" +
                "\t\tWHERE\n" +
                "\t\t\tNAME = :siteName\n" +
                "\t\tAND w_page.web_id = w_site.id\n" +
                "\t)";

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("siteName", siteName);
        params.put("pageName", pageName);

        return jdbcTemplateService.queryForSpecificParamSQL(queryName, params, Page.class).get(0);
    }

    /***
     * 获取网站的页面信息
     * @param webId 网站id
     * @param pageName 页面名称
     * @param siteStatus 打开方式
     * @return 完整html
     */
    public String getPageHtmlByPage(final Integer webId, final String pageName, SiteStatus siteStatus) {
        //布局骨架html
        Page page = pageService.getPageFrameHtml(webId, pageName);
        return getPageHtmlByPage(page, siteStatus);
    }

    public String getPageHtmlByPage(Integer pageId, SiteStatus siteStatus) {
        Page page = pageService.getPageById(pageId);
        return getPageHtmlByPage(page, siteStatus);
    }

//    public String getPageHtmlByPage(String siteName, String pageName, boolean isView) {
//        return getPageHtmlByPage(getPageByName(siteName, pageName), isView);
//    }

//    public String getPageHtmlFromFile(Integer web, String pageName) {
//
//    }


    /***
     * 获取网站的页面信息
     * @param page 页面
     * @return 完整html
     */
    public String getPageHtmlByPage(final Page page, SiteStatus siteStatus) {
        String html;
        if ((html = PageCache.get(page.getId())) == null) {
            final Integer webId = page.getWebId();
            final String pageName = page.getName();
            html = getPageHtml(page.getWebId(), page, new LayoutHtml() {
                public String html(Map<String, Object> params) {
                    //布局骨架html
                    params.put("activeId", menuService.getActiveMenu(webId, pageName));
                    return contentService.injectPageContent(webId, page.getHtml());
                }
            });

            PageCache.set(page.getId(), html);
        }

//        if (siteStatus == SiteStatus.VIEW || siteStatus == SiteStatus.PUBLISH) {
//            html +="<script>\n" +
//                    " $(\"body\").niceScroll();" +
//                    "</script>";

            if (siteStatus == SiteStatus.VIEW) {//页面预览
                html +="<script>\n" +
                        "    $('header a[href^=\"/\"]').each(function () {\n" +
                        "        this.link = '/web/"+page.getWebId()+"/view/page' + this.href.substring(this.href.lastIndexOf('/'))\n" +
                        "        this.href = this.link\n" +
                        "    })\n" +
                        "    $('#layout a[href*=\"/article\"]').each(function () {\n" +
                        "        this.link = '/web/"+page.getWebId()+"/view/article' + this.href.substring(this.href.lastIndexOf('/'))\n" +
                        "        this.href = this.link\n" +
                        "    })\n" +
                        "</script>";
//            }

        } else if (siteStatus == SiteStatus.DESIGN) {//页面设计
            html += "<link rel=\"stylesheet\" href=\""+Global.fileServer+"/static/css/design/design-iframe.css\">" +
                    "<script src=\"https://cdn.bootcss.com/vue/2.4.2/vue.min.js\"></script>" +
                    "<link rel=\"stylesheet\" href=\""+Global.fileServer+"/static/plugins/column-selector/column-selector.css\">\n" +
                    "<link rel=\"stylesheet\" href=\""+Global.fileServer+"/static/plugins/noUiSlider/nouislider.min.css\">\n" +
                    "<script src=\""+Global.fileServer+"/static/plugins/column-selector/jquery-column-selector.js\"></script>\n" +
                    "<script src=\""+Global.fileServer+"/static/plugins/noUiSlider/nouislider.min.js\"></script>\n" +
                    "<script src=\""+Global.fileServer+"/static/plugins/wNumb.js\"></script>" +
                    "<script src=\""+Global.fileServer+"/static/plugins/jquery-ui/jquery-ui.js\"></script>" +
                    "<script src=\""+Global.fileServer+"/static/js/web/design/design-iframe.js\"></script>"+
                    "<link href=\""+Global.fileServer+"/static/plugins/zTree/css/zTreeStyle/zTreeStyle.css\" rel=\"stylesheet\" />\n" +
                    "<script src=\""+Global.fileServer+"/static/plugins/zTree/js/jquery.ztree.core-3.5.min.js\"></script>\n" +
                    "<script src=\""+Global.fileServer+"/static/plugins/zTree/js/jquery.ztree.excheck-3.5.min.js\"></script>\n" +
                    "<script src=\""+Global.fileServer+"/static/plugins/zTree/custom/tree.js\"></script>\n" +
                    "<script src=\""+Global.fileServer+"/static/plugins/zTree/custom/combobox-tree.js\"></script>";

        }
        return html;
    }

    /***
     * 获取单图文的页面
     * @param webId
     * @param id
     * @return
     */
    public String getArticleHTML(final Integer webId, final Integer id) {
        return getPageHtml(webId, new LayoutHtml() {
            public String html(Map<String, Object> params) {
                return contentService.injectArticleText(id);
            }
        });
    }

    /***
     * 获取单视频的页面
     * @param webId
     * @param id
     * @return
     */
    public String getVideoHTML(final Integer webId, final Integer id) {
        return getPageHtml(webId, new LayoutHtml() {
            public String html(Map<String, Object> params) {
                return contentService.injectComponentContent(webId,18, SingleVideo.CONTENT_TYPE,  id, null);
            }
        });
    }

    /***
     * 获取列表界面
     * @param webId
     * @param cpnId
     * @return
     */
    public String getListHTML(final Integer webId, final Integer cpnId, final Map<String, Object> optionsMap) {
        return getPageHtml(webId, new LayoutHtml() {
            public String html(Map<String, Object> params) {

                return "<section id=\"layout\">\n" +
                        "    <div class=\"container\">\n" +
                        "        <div class=\"row\">\n" +
                        "            <div class=\"col-md-12\">\n" +
                        "               <div class=\"cpn\">\n" +
                        contentService.injectListComponent(webId, cpnId, optionsMap) +
                        "               </div>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</section>";

            }

        });
    }

    /***
     * 全文检索
     * @param webId
     * @return
     */
    public String getFullTextHTML(final Integer webId, final String keywords) {
        final Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("keywords", keywords);

        List<QueryDto> queryDtoList = null;
        try {
            queryDtoList = indexService.search(webId, keywords);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } finally {
            if (queryDtoList == null)
                queryDtoList = Collections.emptyList();

            dataMap.put("list", queryDtoList);
            dataMap.put("hits", queryDtoList.size());
        }

        return getPageHtml(webId, new LayoutHtml() {
            public String html(Map<String, Object> params) {
                return contentService.injectFullText(webId, -1, dataMap);
            }
        });
    }


    public String getPageHtml(Integer webId, LayoutHtml layoutHtml) {
        Site site = findById(webId);
        return getPageHtml(site, PageInstance.getPage(), layoutHtml);
    }

    public String getPageHtml(Integer webId, Page page,  LayoutHtml layoutHtml) {
        Site site = findById(webId);
        return getPageHtml(site, page, layoutHtml);
    }

    public String getPageHtml(Site site, Integer pageId, LayoutHtml layoutHtml) {
        Page page = pageService.getPageById(pageId);
        return getPageHtml(site, page, layoutHtml);
    }

    public String getPageHtml(Integer webId, Integer pageId, LayoutHtml layoutHtml) {
        Site site = findById(webId);
        Page page = pageService.getPageById(pageId);
        return getPageHtml(site, page, layoutHtml);
    }

    public String getPageHtml(Site site, Page page, LayoutHtml layoutHtml) {

        StringBuilder htmlBuilder = new StringBuilder();
        Header header = headerService.getHeaderById(site.getHeaderId());
        //页眉html
        htmlBuilder.append("<div id=\"header-container\">" +(header == null ? "": header.getHtml())+ "</div>");

        //获取模版框架html
        Map<String, Object> params = new HashMap<String, Object>();

//        params.put("pageId", page.getId());
//        params.put("layoutId", page.getLayoutId());
//        params.put("pageName", page.getName());
        params.put("page", page);
        params.put("pageJSON", JacksonUtils.toJSon(page));

        params.put("menuList", menuService.getMenuByWebId(site.getId())); //菜单
//        params.put("title", site.getTitle());
//        params.put("keywords", site.getKeywords());
//        params.put("description", site.getDescription());
//        params.put("webId", site.getId());
//        params.put("siteJSON", JacksonUtils.toJSon(site));
        params.put("site", site);
        params.put("fileServer", Global.fileServer);
        //样式


//        params.put("style", site.getStyleCSS());
//        params.put("headerStyle", site.getHeaderCss());
//        params.put("footerStyle", site.getFooterCSS());

        //页脚html
        htmlBuilder.append("<section id=\"layout\">"+ layoutHtml.html(params) + "</section>");
        Footer footer = footerService.getFooterById(site.getFooterId());

        htmlBuilder.append("</div><div id=\"footer-container\" class=\"cpn\">" + ((footer == null) ? "" : footer.getHtml()) + "</div><div id=\"-1\">");

        params.put("siteBody", VelocityUtils.tplFromString(htmlBuilder.toString(), params));

        //内容
        String html = VelocityUtils.tplFromTpl("vm/" + site.getTpl(), params);
        return html;
    }

    /**初始化site
     *
     * @return
     */

    public Site copySite(String title, Integer webId) {
        //添加网站
        Site site = findById(webId);
        site.setId(null);
        site.setTitle(title);
        site.setName(RandomStringUtils.randomAlphanumeric(10));

        site.preSave();
        siteDao.save(site);

        //添加页面
        Collection<Page> pages = pageService.getPageMappingByWebId(webId).values();
        Map<Integer, Integer> pageIdMap = new HashMap<Integer, Integer>(pages.size());
        for (Page page : pages) {
            Integer oldId = page.getId();
            page.setId(null);
            page.setWebId(site.getId());
            page.preSave();
            pageService.save(page);
            pageIdMap.put(oldId, page.getId());
        }

        //添加菜单
        List<Menu> menuList = menuService.getAllMenusByWebId(webId);
        for (Menu menu: menuList) {
            menu.setId(null);
            if (menu.getPageId() != null) {
                menu.setWebId(site.getId());
                menu.setPageId(pageIdMap.get(menu.getPageId()));
            }

            menu.preSave();
            menuService.save(menu);
        }

        return site;
    }

    /**
     * 查找没有任何控件的页面
     * @param webId
     * @return 菜单名称
     */
    public List<String> checkNoneBuildPage(Integer webId) {
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("webId", webId);

        String sql = "SELECT\n" +
                "\twm.title\n" +
                "FROM\n" +
                "\tw_page wp,\n" +
                "\tw_menu wm\n" +
                "WHERE\n" +
                "\twp.id = wm.page_id\n" +
                "AND wp.web_id = :webId\n" +
                "AND wp.del_flag = '1'\n" +
                "AND wm.del_flag = '1'\n" +
                "AND NOT EXISTS (\n" +
                "\tSELECT\n" +
                "\t\t1\n" +
                "\tFROM\n" +
                "\t\tw_page_component wpc\n" +
                "\tWHERE\n" +
                "\t\twp.id = wpc.page_id\n" +
                "\tAND wpc.del_flag = '1'\n" +
                ")";

        return jdbcTemplateService.queryForSpecificParamSQL(sql,params, String.class);
    }
}
