package com.yodean.site.web.tpl.dto;


import com.yodean.site.web.tpl.entity.Menu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rick on 2017/7/17.
 */
public class ContentMenuDto {
    private Menu menu;

    private List<ContentMenuDto> subMenu = new ArrayList<ContentMenuDto>();

    /***
     * 所有控件
     */
    private List<PageComponentDto> pageComponentDtoList = new ArrayList<PageComponentDto>();


    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<ContentMenuDto> getSubMenu() {
        return subMenu;
    }


    public List<PageComponentDto> getPageComponentDtoList() {
        return pageComponentDtoList;
    }

    public void setPageComponentDtoList(List<PageComponentDto> pageComponentDtoList) {
        this.pageComponentDtoList = pageComponentDtoList;
    }

    /***
     * 去重控件
     * @return
     */
    public List<PageComponentDto> getPageComponentContentDtoList() {
        List<PageComponentDto> pageComponentContentDtoList = new ArrayList<PageComponentDto>();
        Set<String> cpnSet = new HashSet<String>();

        for (PageComponentDto pc : pageComponentDtoList) {
            Integer contentType = pc.getContentType();
            Integer categoryId = pc.getCategoryId();

            String contentKey = contentType + "-" + categoryId;

            if (cpnSet.contains(contentKey))
                continue;

            pc.setLabel(pc.getLabel());
            pageComponentContentDtoList.add(pc);
            cpnSet.add(contentKey);
        }

        return pageComponentContentDtoList;
    }
}