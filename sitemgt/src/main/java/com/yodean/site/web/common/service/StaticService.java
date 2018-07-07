package com.yodean.site.web.common.service;


import com.rick.dev.config.Global;
import com.yodean.site.web.common.cache.ComponentPageCache;
import com.yodean.site.web.common.cache.MenuCache;
import com.yodean.site.web.common.cache.PageCache;
import com.yodean.site.web.content.service.SiteService;
import com.yodean.site.web.tpl.dto.PageComponentDto;
import com.yodean.site.web.tpl.entity.Page;
import com.yodean.site.web.tpl.service.MenuService;
import com.yodean.site.web.tpl.service.PageService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by rick on 2017/9/4.
 */

@Service
public class StaticService {

    @Resource
    private PageService pageService;

    @Resource
    private SiteService siteService;

    @Resource
    private MenuService menuService;

    @Value("${upload}")
    private String upload;

    public static final String HTML_DIR = "html";


    public void staticSite(Integer webId) throws IOException {
        staticMenu(webId);

        Set<Integer> pageIdSet = pageService.getPageMappingByWebId(webId).keySet();
        Integer[] pageIds = pageIdSet.toArray(new Integer[pageIdSet.size()]);
        staticComponent(pageIds);
    }

    public void staticMenu(Integer webId) throws IOException {
        MenuCache.remove(webId);
        MenuCache.set(webId, menuService.getMenuByWebId(webId));
        Collection<Page> pages = pageService.getPageMappingByWebId(webId).values();
        staticPage(pages, webId);
    }

    public void staticPage(Collection<Page> pages, Integer webId) throws IOException {
        if (CollectionUtils.isNotEmpty(pages)) {
            for(Page page : pages) {
//                PageCache.remove(page.getId());
                String html = siteService.getPageHtmlByPage(page.getId(), SiteService.SiteStatus.PUBLISH);
                PageCache.set(page.getId(), html);
                //生成html文件
                String htmlFolder = upload + File.separator + Global.SITE_FOLDER + File.separator + webId + File.separator + HTML_DIR;

                FileUtils.write(new File(htmlFolder, page.getName() + ".html"), html, "utf-8");
            }
        }
//        Integer[] pageIds = pageIdSet.toArray(new Integer[pageIdSet.size()]);
//        if (ArrayUtils.isNotEmpty(pageIds)) {
//            for (Integer pageId : pageIds) {
//                PageCache.remove(pageId);
//                String html = siteService.getPageHtmlByPage(pageId, true);
//                PageCache.set(pageId, html);
//                //生成html文件
//                String htmlFolder = upload + File.separator + Global.SITE_FOLDER + File.separator + webId + File.separator + HTML_DIR;
//
//                FileUtils.write(new File(htmlFolder, pageId + ".html"), html, "utf-8");
//            }
//        }
    }

    public void staticComponent(Integer ...pageIds) {
        if (ArrayUtils.isNotEmpty(pageIds)) {
            for (Integer pageId : pageIds) {
                ComponentPageCache.remove(pageId);
                List<PageComponentDto> pageComponentDtoList = pageService.getComponentsByPageId(pageId);
                ComponentPageCache.set(pageId, pageComponentDtoList);


            }
        }
    }

}
