package com.yodean.site.web.tpl.component.senior;

import com.yodean.site.web.tpl.component.EmptyComponent;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public class Layout extends EmptyComponent {

    public static final Integer CONTENT_TYPE = 403;


    public static final EmptyComponent emptyComponent = new Layout();

    private Layout() {
        super(CONTENT_TYPE, "布局");
    }

    public static EmptyComponent getInstance() {
        return emptyComponent;
    }

}
