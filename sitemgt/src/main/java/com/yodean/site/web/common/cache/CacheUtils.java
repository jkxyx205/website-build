package com.yodean.site.web.common.cache;

import com.rick.dev.utils.SpringContextHolder;
import com.yodean.site.web.tpl.service.PageService;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Set;

/**
 * Created by rick on 2017/9/4.
 */
public final class CacheUtils {
    private CacheUtils() {}

    private static final PageService pageService = SpringContextHolder.getBean("pageService");

    public static void clearSiteCache(Integer webId) {
        SiteUtils.remove(webId);
    }

    public static void clearWebCache(Integer webId) {
        clearMenuCacheByWebId(webId);
        clearComponentCacheByWebId(webId);
    }

    public static void clearMenuCacheByWebId(Integer webId) {
        MenuCache.remove(webId);
        clearPageCacheByWebId(webId);
    }

    public static void clearPageCacheByWebId(Integer webId) {
        Set<Integer> pageIdSet = pageService.getPageMappingByWebId(webId).keySet();
        Integer[] pageIds = pageIdSet.toArray(new Integer[pageIdSet.size()]);
        clearPageCache(pageIds);
    }

    public static void clearPageCache(Integer ...pageIds) {
        if (ArrayUtils.isNotEmpty(pageIds)) {
            for (Integer pageId : pageIds) {
                PageCache.remove(pageId);
                ComponentPageCache.remove(pageId);
            }
        }
    }

    public static void clearComponentCacheByWebId(Integer webId) {
        Set<Integer> pageIdSet = pageService.getPageMappingByWebId(webId).keySet();
        Integer[] pageIds = pageIdSet.toArray(new Integer[pageIdSet.size()]);
        clearComponentCache(pageIds);
    }

    public static void clearComponentCache(Integer ...pageIds) {
        if (ArrayUtils.isNotEmpty(pageIds)) {
            for (Integer pageId : pageIds) {
                ComponentPageCache.remove(pageId);
            }
        }
    }
}
