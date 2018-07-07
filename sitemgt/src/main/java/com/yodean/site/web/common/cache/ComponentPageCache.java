package com.yodean.site.web.common.cache;

import com.yodean.site.web.tpl.dto.PageComponentDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2017/9/4.
 */
public final class ComponentPageCache {
    private static final Map<Integer, List<PageComponentDto>> cache = new HashMap<Integer, List<PageComponentDto>>();

    private ComponentPageCache(){}

    public static void set(Integer pageId, List<PageComponentDto> pageComponentDtoList) {
        cache.put(pageId, pageComponentDtoList);
    }

    public static List<PageComponentDto> get(Integer pageId) {
        return cache.get(pageId);
    }

    public static void remove(Integer pageId) {
        cache.remove(pageId);
    }

}
