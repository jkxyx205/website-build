package com.yodean.site.web.common.cache;

import com.yodean.site.web.tpl.dto.MenuDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2017/9/4.
 */
public final class MenuCache {
    private static final Map<Integer, List<MenuDto>> cache = new HashMap<Integer, List<MenuDto> >();

    private MenuCache(){}

    public static void set(Integer webId, List<MenuDto> menuDtos) {
        cache.put(webId, menuDtos);
    }

    public static List<MenuDto>  get(Integer webId) {
        return cache.get(webId);
    }

    public static void remove(Integer webId) {
        cache.remove(webId);
    }

}
