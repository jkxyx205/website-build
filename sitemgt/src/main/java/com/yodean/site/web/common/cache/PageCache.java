package com.yodean.site.web.common.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rick on 2017/9/4.
 */
public final class PageCache {
    private PageCache(){}

    private static final Map<Integer, String> cache = new HashMap<Integer, String>();

    public static void set(Integer pageId, String html) {
        cache.put(pageId, html);
    }

    public static String get(Integer pageId) {
        return cache.get(pageId);
    }

    public static void remove(Integer pageId) {
        cache.remove(pageId);
    }

}
