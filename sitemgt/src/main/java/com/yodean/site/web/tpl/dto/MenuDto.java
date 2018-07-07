package com.yodean.site.web.tpl.dto;


import com.rick.dev.utils.JacksonUtils;
import com.yodean.site.web.tpl.entity.Menu;
import com.yodean.site.web.tpl.entity.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 2017/4/10.
 */
public class MenuDto implements Serializable {

    private Menu menu;

    private List<MenuDto> subMenu = new ArrayList<MenuDto>();

    private Page page;

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<MenuDto> getSubMenu() {
        return subMenu;
    }

    public Page getPage() {
        if (page == null) {
            page = new Page();
            page.setWebId(menu.getWebId());
            page.setLayoutId(1);
        }
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return JacksonUtils.toJSon(this);
    }
}