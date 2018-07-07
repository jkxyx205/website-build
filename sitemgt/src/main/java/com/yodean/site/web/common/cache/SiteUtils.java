package com.yodean.site.web.common.cache;

import com.yodean.site.web.content.entity.Site;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rick on 2018/1/2.
 */
public final class SiteUtils {

    private SiteUtils() {}

    private static final Map<Integer, Site> cache = new HashMap<Integer, Site>();

    public static void set(Integer webId, Site site) {
        cache.put(webId, site);
    }

    public static Site get(Integer webId) {
        return cache.get(webId);
    }

    public static void remove(Integer webId) {
        cache.remove(webId);
    }
}
