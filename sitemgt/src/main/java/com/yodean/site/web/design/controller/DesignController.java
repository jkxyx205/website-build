package com.yodean.site.web.design.controller;

import com.rick.dev.config.Global;
import com.rick.dev.dto.ResponseDTO;
import com.rick.dev.plugin.ztree.model.TreeNode;
import com.rick.dev.utils.JacksonUtils;
import com.rick.dev.utils.VelocityUtils;
import com.yodean.site.web.common.cache.CacheUtils;
import com.yodean.site.web.common.cache.MenuCache;
import com.yodean.site.web.common.cache.PageCache;
import com.yodean.site.web.common.controller.WebController;
import com.yodean.site.web.content.entity.Site;
import com.yodean.site.web.content.service.SiteService;
import com.yodean.site.web.tpl.component.RegistrationCenter;
import com.yodean.site.web.tpl.dto.PageComponentDto;
import com.yodean.site.web.tpl.entity.*;
import com.yodean.site.web.tpl.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2017/7/12.
 */
@Controller
@RequestMapping("/web/{webId}/design")
public class DesignController extends WebController {
    public static final Logger logger = LoggerFactory.getLogger(DesignController.class);

    public static final String TPL_MODE_HEADER = "header";
    public static final String TPL_MODE_FOOTER = "footer";
    public static final String TPL_MODE_STYLE = "style";

    @Resource
    private SiteService siteService;

    @Resource
    private PageService pageService;

    @Resource
    private MenuService menuService;

    @Resource
    private PageComponentService pageComponentService;

    @Resource
    private ComponentService componentService;

    @Resource
    private ContentService contentService;

    @Resource
    private ThemeService themeService;

    @Resource
    private LayoutService layoutService;

    @Resource
    private HeaderService headerService;

    @Resource
    private FooterService footerService;


    @ModelAttribute
    public void populateModel(@PathVariable Integer webId, Model model) {
        model.addAttribute("menu", "design");
        model.addAttribute("webId", webId);
    }


    @GetMapping(value = "/page/{pageName}", produces = "text/html; charset=utf-8")
    @ResponseBody
    public String view(@PathVariable Integer webId, @PathVariable String pageName) {
        return siteService.getPageHtmlByPage(webId, pageName, SiteService.SiteStatus.DESIGN);
    }


//    @GetMapping(value = "/page/id/{id}", produces = "text/html; charset=utf-8")
//    @ResponseBody
//    public String viewPageById(@PathVariable Integer webId, @PathVariable Integer id) {
//        return siteService.getPageHtmlByPage(webId, SiteService.SiteStatus.DESIGN);
//    }


    @GetMapping
    public String gotoDesignPage(@PathVariable Integer webId, String pageName, Integer id, Model model) { //
        if (id == null || id < 1) {
            pageName = "index";
        } else if (pageName == null) {
            PageComponent pageComponent = pageComponentService.getPageComponentById(id);
            pageName = pageService.getPageById(pageComponent.getPageId()).getName();
            model.addAttribute("pageComponentId", id);
        }

        model.addAttribute("pageName", pageName);

        return "web/design";
    }

    @PostMapping("/update/{pageId}")
    @ResponseBody
    public ResponseDTO sortLayout(@PathVariable Integer pageId, String html) {
        pageService.updatePage(pageId, html); //更新页面

        //更新关系表
        pageComponentService.updateRelationShipByHTML(pageId, html);


        CacheUtils.clearPageCache(pageId);
        CacheUtils.clearComponentCache(pageId);
        return response(pageId) ;
    }

    @GetMapping("/componentDialog")
    public String componentDialog(@PathVariable Integer webId, Model model) {
        //historical
        List<TreeNode> treeNode = menuService.getTreeNode(webId);
        TreeNode selectNode = treeNode.get(0);
        model.addAttribute("selected", selectNode.getId());
        model.addAttribute("zNodes",JacksonUtils.toJSon(treeNode));
        model.addAttribute("list", JacksonUtils.toJSon(pageService.getComponentsByMenuId(selectNode.getId())));

        //add
        model.addAttribute("styles", JacksonUtils.toJSon(componentService.getComponentStyle()));
        return "web/settings/componentDialog";
    }

    @GetMapping("/component/{menuId}")
    @ResponseBody
    public ResponseDTO componentList(@PathVariable Integer webId, @PathVariable Integer menuId) {
        List<PageComponentDto> pageComponentDtoList;
        if (menuId == -1) { //回收站
            pageComponentDtoList = pageService.getDeletedComponentsByWebId(webId);
        } else {
            pageComponentDtoList = pageService.getComponentsByMenuId(menuId);
        }
        return response(pageComponentDtoList);
    }

    @GetMapping(value = "/add/{pageId}/{type}/{id}",produces = "text/html; charset=utf-8")
    @ResponseBody
    public String componentRef(@PathVariable Integer webId, @PathVariable Integer pageId, @PathVariable  Integer type, @PathVariable Integer id, String title) {

        if (0 == type) { //new
            title = title + "-" +  new SimpleDateFormat("yyMMddHHmmss").format(new Date());

            return componentService.newComponentToPage(webId, pageId, id, title);
        } else if (1 == type){ //historical
            return componentService.historicalComponentToPage(webId, pageId, id);
        }

       return "";
    }

    @GetMapping("/settingDialog/{pageComponentId}")
    public String settingDialog(@PathVariable Integer pageComponentId,  Model model) {
        PageComponentDto componentDto = pageComponentService.getPageComponentDtoById(pageComponentId);

        List<Component> components = componentService.getComponentsByType(componentDto.getContentType());

        model.addAttribute("componentDto", JacksonUtils.toJSon(componentDto));
        model.addAttribute("components", JacksonUtils.toJSon(components));
        return  RegistrationCenter.getComponentType(componentDto.getContentType()).gotoSettingPage();
    }

    @PostMapping(value = "/settings/add/{pageId}/{pageComponentId}")
    @ResponseBody
    public ResponseDTO componentSettings(@PathVariable Integer webId,@PathVariable Integer pageId, @PathVariable Integer pageComponentId, @RequestParam Map<String, Object> params) {
        PageComponentDto componentDto = pageComponentService.getPageComponentDtoById(pageComponentId);
        RegistrationCenter.getComponentType(componentDto.getContentType()).addSetting(params);
        CacheUtils.clearPageCache(pageId);
        return response(contentService.injectComponentContent(webId, pageComponentId));
    }


    @GetMapping(value = "/load/{pageComponentId}")
    @ResponseBody
    public ResponseDTO loadComponent(@PathVariable Integer webId, @PathVariable Integer pageComponentId, @RequestParam Map<String, Object> params) {
        PageComponent pc =pageComponentService.getPageComponentById(pageComponentId);
        Map<String, Object> optionsMap = pc.getOptionsMap();
        optionsMap.putAll(params);

        return response(contentService.injectComponentContent(webId, pc.getComponentId(), pc.getCategoryId(), optionsMap));
    }

    @PostMapping("/block/update/{blockId}")
    @ResponseBody
    public ResponseDTO setWebBlock(@PathVariable Integer webId, @PathVariable  Integer blockId) {
        Site site = siteService.findById(webId);
        site.setBlockId(blockId);
        siteService.save(site);
        CacheUtils.clearPageCacheByWebId(webId);
        return response(blockId);
    }

    @GetMapping("/themeDialog/{styleId}/{headerId}/{footerId}/{blockId}")
    public String themeDialog(@PathVariable Integer styleId, @PathVariable Integer headerId, @PathVariable Integer footerId, @PathVariable Integer blockId,  Model model) {
        List<Theme> themeList =  themeService.getThemes(null, headerId, footerId, blockId);
        model.addAttribute("themeList", themeList);
        model.addAttribute("styleId", styleId);
        return "web/settings/themeDialog";
    }

    @PostMapping("/theme/update/{styleId}")
    @ResponseBody
    public ResponseDTO setWebTheme(@PathVariable Integer webId, @PathVariable  Integer styleId) {
        Site site = siteService.findById(webId);

        //Theme theme = themeService.getThemeById(themeId);

        site.setStyleId(styleId);

        siteService.save(site);
        CacheUtils.clearPageCacheByWebId(webId);
        return response(styleId);
    }

    /***
     * 重置布局
     * @param model
     * @return
     */
    @GetMapping("/layoutDialog")
    public String layoutDialog(Model model) {
        List<Layout> layoutList = layoutService.getLayouts();
        model.addAttribute("layoutList", layoutList);
        return "web/settings/layoutDialog";
    }


    @PostMapping("/layout/update/{pageId}/{layoutId}")
    @ResponseBody
    public ResponseDTO setPageLayout(@PathVariable Integer pageId, @PathVariable  Integer layoutId) {
        Page page = pageService.getPageById(pageId);
        page.setLayoutId(layoutId);
//
        Layout layout = layoutService.getLayoutById(layoutId);
        page.setHtml(layout.getHtml());
        pageService.save(page);
//
//        //解除page和控件的关系
        pageComponentService.removeAllComponentsByPageId(pageId);
//
        CacheUtils.clearPageCache(pageId);

        //TODO
//        MenuCache.remove(webId);
//        return response("");
        return response(layout.getHtml());

    }

    /***
     * 插入布局
     * @param model
     * @return
     */
    @GetMapping("/layoutDialog2")
    public String layoutDialog2(Model model) {
        List<Layout> layoutList = layoutService.getLayouts();
        model.addAttribute("layoutList", layoutList);
        return "web/settings/layoutDialog2";
    }



    @PostMapping("/layout/insert/{pageId}/{layoutId}")
    @ResponseBody
    public ResponseDTO insertPageLayout(@PathVariable Integer pageId, @PathVariable  Integer layoutId) {
        Layout layout = layoutService.getLayoutById(layoutId);
        return response(layout.getHtml());
    }

    //修改页眉
    @GetMapping("/headerDialog/{styleId}/{headerId}/{footerId}/{blockId}")
    public String headerDialog(@PathVariable Integer styleId, @PathVariable Integer headerId, @PathVariable Integer footerId, @PathVariable Integer blockId,  Model model) {
        List<Theme> themeList =  themeService.getThemes(styleId, null, footerId, blockId);
        model.addAttribute("themeList", themeList);
        model.addAttribute("headerId", headerId);
        return "web/settings/headerDialog";
    }

    @PostMapping("/header/update/{headerId}")
    @ResponseBody
    public ResponseDTO setWebHeader(@PathVariable Integer webId, @PathVariable  Integer headerId) {
        Site site = siteService.findById(webId);
        site.setHeaderId(headerId);
        siteService.save(site);
        CacheUtils.clearPageCacheByWebId(webId);

        Header header = headerService.getHeaderById(headerId);

        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("menuList", menuService.getMenuByWebId(site.getId())); //菜单
        params.put("fileServer", Global.fileServer); //TODO
        params.put("site", siteService.findById(webId));
        return response(VelocityUtils.tplFromString(header.getHtml(), params));
    }

    //修改页脚
    @GetMapping("/footerDialog/{styleId}/{headerId}/{footerId}/{blockId}")
    public String footerDialog(@PathVariable Integer styleId, @PathVariable Integer headerId, @PathVariable Integer footerId, @PathVariable Integer blockId,  Model model) {
        List<Theme> themeList =  themeService.getThemes(styleId, headerId, null, blockId);
        model.addAttribute("themeList", themeList);
        model.addAttribute("footerId", footerId);
        return "web/settings/footerDialog";
    }

    @PostMapping("/footer/update/{footerId}")
    @ResponseBody
    public ResponseDTO setWebFooter(@PathVariable Integer webId, @PathVariable  Integer footerId) {
        Site site = siteService.findById(webId);
        site.setFooterId(footerId);
        siteService.save(site);
        CacheUtils.clearPageCacheByWebId(webId);

        Footer footer = footerService.getFooterById(footerId);

        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("menuList", menuService.getMenuByWebId(site.getId())); //菜单
        params.put("fileServer", Global.fileServer); //TODO
        params.put("site", siteService.findById(webId));
        return response(VelocityUtils.tplFromString(footer.getHtml(), params));
    }


    /***
     * 获取页眉／页脚／风格的模版数据
     * @param styleId
     * @param headerId
     * @param footerId
     * @param blockId
     * @param model
     * @param mode header footer style
     * @return
     */
    @GetMapping("/api/tpl/{styleId}/{headerId}/{footerId}/{blockId}")
    @ResponseBody
    public List<Theme>  tplDialog(@PathVariable Integer styleId, @PathVariable Integer headerId, @PathVariable Integer footerId, @PathVariable Integer blockId,  Model model, String mode) {

        List<Theme> themeList = null;

        if (TPL_MODE_HEADER.equals(mode)) {
            themeList =  themeService.getThemes(styleId, null, footerId, blockId);
        } else if(TPL_MODE_STYLE.equals(mode)) {
            themeList =  themeService.getThemes(null, headerId, footerId, blockId);
        } else if (TPL_MODE_FOOTER.equals(mode)) {
            themeList =  themeService.getThemes(styleId, headerId, null, blockId);
        }

        return themeList;
    }
}
