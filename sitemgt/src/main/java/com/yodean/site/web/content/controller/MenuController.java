package com.yodean.site.web.content.controller;

import com.rick.dev.dto.ResponseDTO;
import com.yodean.site.web.common.cache.CacheUtils;
import com.yodean.site.web.common.controller.WebController;
import com.yodean.site.web.tpl.dto.MenuDto;
import com.yodean.site.web.tpl.entity.Menu;
import com.yodean.site.web.tpl.service.LayoutService;
import com.yodean.site.web.tpl.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by rick on 2017/7/31.
 */
@Controller
@RequestMapping("/web/{webId}/menu")
public class MenuController extends WebController {

    @Resource
    private MenuService menuService;

    @Resource
    private LayoutService layoutService;

    @GetMapping
    public String gotoMenuPage(@PathVariable Integer webId, Model model) throws InvocationTargetException, IllegalAccessException {
        model.addAttribute("menu", "menu");
        List<MenuDto> menuList = menuService.getMenuByWebId(webId);
        model.addAttribute("menuList", menuList);
        model.addAttribute("layoutList",layoutService.getLayouts());
        return "web/menu";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseDTO save(@PathVariable final Integer webId, Menu menu) {
        menu = menuService.save(menu);
        MenuDto menuDto = new MenuDto();
        menuDto.setMenu(menu);
        CacheUtils.clearMenuCacheByWebId(webId);
        return response(menuDto);
    }

    @PostMapping("/saveList")
    @ResponseBody
    public ResponseDTO saveList(@PathVariable final Integer webId, @RequestBody List<MenuDto> menuDtoList) {
        menuService.saveList(menuDtoList);
        CacheUtils.clearMenuCacheByWebId(webId);
        return response(menuDtoList.size());
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseDTO delMenu(@PathVariable final Integer webId, @PathVariable Integer id) {
        menuService.delete(id);
        CacheUtils.clearMenuCacheByWebId(webId);
        return response(id);
    }
}
