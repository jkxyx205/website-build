package com.yodean.site.web.common.cache;

import com.rick.dev.service.JdbcTemplateService;
import com.rick.dev.utils.SpringContextHolder;
import com.yodean.site.web.content.entity.Site;
import com.yodean.site.web.tpl.entity.Component;
import com.yodean.site.web.tpl.entity.Footer;
import com.yodean.site.web.tpl.entity.Header;
import com.yodean.site.web.tpl.entity.Layout;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by rick on 2017/9/5.
 */
public final class PlatformCache {
    private PlatformCache() {}

    private static JdbcTemplateService jdbcTemplateService;

    private static final Map<Integer, Component> componentCache = new TreeMap<Integer, Component>();
    private static final Map<Integer, Header> headerCache = new HashMap<Integer, Header>();
    private static final Map<Integer, Footer> footerCache = new HashMap<Integer, Footer>();

    private static final Map<Integer, Layout> layoutCache = new HashMap<Integer, Layout>();

    public static void build() {
        if (jdbcTemplateService == null) {
            jdbcTemplateService = SpringContextHolder.getBean("jdbcTemplateService");
        }
        loadSite();
        loadComponent();
        loadHeader();
        loadFooter();
        loadLayoutCache();
    }

    public static void loadSite() {
        String queryName = "com.yodean.site.web.tpl.site.getSiteById";
        List<Site> list = jdbcTemplateService.queryForSpecificParam(queryName, null, Site.class);
        for (Site site: list) {
            SiteUtils.set(site.getId(), site);
        }
    }

    public static void loadComponent() {
        String queryName = "com.yodean.site.web.content.getComponents";
        Map<String, Object> params = new HashMap<String, Object>();

        List<Component> list = jdbcTemplateService.queryForSpecificParam(queryName, params, Component.class);
        for (Component component: list) {
            componentCache.put(component.getId(), component);
        }
    }

    public static void loadHeader() {
        String queryName = "com.yodean.site.web.tpl.header.getHeaders";
        Map<String, Object> params = new HashMap<String, Object>();

        List<Header> list = jdbcTemplateService.queryForSpecificParam(queryName, params, Header.class);
        for (Header header: list) {
            headerCache.put(header.getId(), header);
        }

    }

    public static void loadFooter() {
        String queryName = "com.yodean.site.web.tpl.footer.getFooters";
        Map<String, Object> params = new HashMap<String, Object>();
        List<Footer> list = jdbcTemplateService.queryForSpecificParam(queryName, params, Footer.class);
        for (Footer footer : list) {
            footerCache.put(footer.getId(), footer);
        }
    }


    public static void loadLayoutCache() {
        List<Layout> list =  jdbcTemplateService.queryForSpecificParamSQL("SELECT w.id,w.title, w.html FROM w_layout w where w.del_flag = '1'", null,Layout.class);
        for (Layout layout : list) {
            layoutCache.put(layout.getId(), layout);
        }
    }

    public static Map<Integer, Component> getComponent() {
        return MapUtils.unmodifiableMap(componentCache);
    }

    public static Map<Integer, Header>  getHeader() {
        return MapUtils.unmodifiableMap(headerCache);
    }

    public static Map<Integer, Footer>  getFooter() {
        return MapUtils.unmodifiableMap(footerCache);
    }

    public static Map<Integer, Layout>  getLayout() {
        return MapUtils.unmodifiableMap(layoutCache);
    }
}
