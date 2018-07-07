package com.yodean.site.web.tpl.dto;

import com.yodean.site.web.tpl.entity.Page;

/**
 * Created by rick on 2017/9/7.
 */
public final class PageInstance {
    private static final Page view = new Page();

    static {
        view.setId(-1);
        view.setLayoutId(-1);
        view.setName("default");
    }

    public static Page getPage() {
        return view;
    }
}
