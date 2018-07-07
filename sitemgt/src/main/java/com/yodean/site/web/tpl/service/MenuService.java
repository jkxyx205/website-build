package com.yodean.site.web.tpl.service;

import com.rick.dev.plugin.ztree.model.TreeNode;
import com.rick.dev.service.DefaultService;
import com.yodean.site.web.common.cache.MenuCache;
import com.yodean.site.web.tpl.dao.LayoutDao;
import com.yodean.site.web.tpl.dao.MenuDao;
import com.yodean.site.web.tpl.dto.ContentMenuDto;
import com.yodean.site.web.tpl.dto.MenuDto;
import com.yodean.site.web.tpl.dto.PageComponentDto;
import com.yodean.site.web.tpl.entity.Layout;
import com.yodean.site.web.tpl.entity.Menu;
import com.yodean.site.web.tpl.entity.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by rick on 2017/7/12.
 */
@Service
public class MenuService extends DefaultService {

    public static final String LINK_EMPTY = "0";

    public static final String LINK_INNER = "1";

    public static final String LINK_OUTER = "2";

    @Resource
    private MenuDao menuDao;

    @Resource
    private PageService pageService;

    @Resource
    private LayoutDao layoutDao;

    @Resource
    private PageComponentService pageComponentService;

    public List<Menu> getAllMenusByWebId(Integer webId) {
        final String QUERY_NAME_ALL_MENU = "com.yodean.site.web.tpl.menu.getMenu";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("webId", webId);

        List<Menu> menuList = jdbcTemplateService.queryForSpecificParam(QUERY_NAME_ALL_MENU, params, Menu.class);
        return menuList;
    }


    public List<MenuDto> getMenuByWebId(Integer webId) {
        return getMenu(webId, null);
    }

    public MenuDto getMenuById(Integer menuId) {
        List<MenuDto> menuDtoList = getMenu(null, menuId);
        if (CollectionUtils.isNotEmpty(menuDtoList))
            return menuDtoList.get(0);
        return null;
    }


    private List<MenuDto> getMenu(Integer webId, Integer menuId) {
        if (webId == null)
            webId = menuDao.findOne(menuId).getWebId();

        List<MenuDto> menuDtos = MenuCache.get(webId);
        if (menuDtos != null && menuId == null)
            return menuDtos;

        menuDtos = new ArrayList<MenuDto>();

        final String QUERY_NAME_ALL_MENU = "com.yodean.site.web.tpl.menu.getMenu";
        //get current site all pages
        Map<Integer, Page> pageMap = pageService.getPageMappingByWebId(webId);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("webId", webId);

        List<Menu> menuList = jdbcTemplateService.queryForSpecificParam(QUERY_NAME_ALL_MENU, params, Menu.class);



        Map<Integer, Menu> cache = new HashMap<Integer, Menu>();

        Map<Integer, MenuDto> dtoCache = new HashMap<Integer, MenuDto>();

        MenuDto _m = null;

        for (Menu m : menuList) {
            MenuDto md = new MenuDto();
            cache.put(m.getId(), m);
            dtoCache.put(m.getId(), md);

            if (m.getPid() == null) {
                md.setMenu(m);
                md.setPage(pageMap.get(m.getPageId()));
                menuDtos.add(md);
            }

            if ((menuId != null && m.getId().intValue() == menuId)) {
                _m = md;
            }
        }

        for (Menu m : menuList) {
            if (m.getPid() != null) {
                MenuDto md = dtoCache.get(m.getId());
                md.setMenu(m);
                md.setPage(pageMap.get(m.getPageId()));

                MenuDto pmd = dtoCache.get(m.getPid());

                if (pmd != null) {
                    pmd.getSubMenu().add(md);
                }
            }
        }

        MenuCache.set(webId, menuDtos);

        if(menuId != null )
            return Arrays.asList(_m);


        return menuDtos;

    }
    public List<ContentMenuDto> getContentMenuDtoByWebId(Integer webId) {
        return getContentMenuDto(webId, null);
    }

    public ContentMenuDto getContentMenuDtoById(Integer menuId) {
        List<ContentMenuDto>  contentMenuDtoList = getContentMenuDto(null, menuId);
        if (CollectionUtils.isNotEmpty(contentMenuDtoList)) {
            return contentMenuDtoList.get(0);
        }

        return null;
    }

    private List<ContentMenuDto> getContentMenuDto(Integer webId, Integer menuId) {
        List<MenuDto> menuList = getMenu(webId, menuId);

        List<ContentMenuDto> contentMenuDtoList = new ArrayList<ContentMenuDto>();

        nest(menuList, contentMenuDtoList);

        //添加回收站
//        ContentMenuDto bin = new ContentMenuDto();
//        Menu menu = new Menu();
//        menu.setTitle("回收站");
//        bin.setMenu(menu);
//        bin.setPageComponentDtoList(pageService.getDeletedComponentsByWebId(webId));
//        contentMenuDtoList.add(bin);

        return contentMenuDtoList;
    }

    private void nest(List<MenuDto> menuList, List<ContentMenuDto> contentMenuDtoList) {

        for (MenuDto md : menuList) {
            ContentMenuDto cmd = new ContentMenuDto();

            Menu menu = md.getMenu();

            if (MenuService.LINK_INNER.equals(menu.getLinkType())) {
                List<PageComponentDto> PageComponentDtoList = pageService.getComponentsByPageId(menu.getPageId());
                cmd.setPageComponentDtoList(PageComponentDtoList);
            }

            cmd.setMenu(menu);

            if (CollectionUtils.isNotEmpty(md.getSubMenu())) {
                nest(md.getSubMenu(), cmd.getSubMenu());
            }

            contentMenuDtoList.add(cmd);
        }
    }

    /***
     * 根据页面获取active id
     * @param pageName
     * @return
     */
    public Integer getActiveMenu(Integer webId, String pageName) {
        final String QUERY_STRING= "com.yodean.site.web.tpl.menu.getActiveMenuId";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("webId", webId);
        params.put("pageName", pageName);
        List<Map<String, Object>> map = jdbcTemplateService.queryForSpecificParam(QUERY_STRING, params);
        if (CollectionUtils.isNotEmpty(map))
            return (Integer) map.get(0).get("id");
        return -1;

    }


    /***
     * 根据id删除菜单以及子菜单
     * @param id
     */
    public void delete(Integer id) {
        MenuDto menuDto = getMenuById(id);
        deleteMenu(menuDto);
    }

    private void deleteMenu(MenuDto menuDto) {
        //解除页面与控件的关系
        Integer pageId = menuDto.getMenu().getPageId();

        if (null != pageId) {
            pageComponentService.removeAllComponentsByPageId(pageId);
        }

        menuDao.delete(menuDto.getMenu().getId());

        List<MenuDto> subMenuDtoList = menuDto.getSubMenu();
        if (CollectionUtils.isNotEmpty(subMenuDtoList)) {
            for (MenuDto subMenuDto: subMenuDtoList) {
                deleteMenu(subMenuDto);
            }
        }
    }

    /***
     * 保存所有菜单
     */
    public void saveList(List<MenuDto> menuDtoList) {
        if (CollectionUtils.isNotEmpty(menuDtoList)) {
            for (MenuDto menuDto: menuDtoList) {
                save(getFormatedMenu(menuDto));
                saveList(menuDto.getSubMenu());
            }

        }
    }

    public Menu save(Menu menu) {
        menu.preSave();
        return menuDao.save(menu);
    }



    public List<TreeNode> getTreeNode(Integer webId) {
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("webId", webId);

        List<TreeNode> treeNodes = jdbcTemplateService.queryForSpecificParamSQL("SELECT\n" +
                "  id,\n" +
                "  pid AS pId,\n" +
                "  title AS name\n" +
                "FROM\n" +
                "  w_menu m\n" +
                "WHERE\n" +
                "  m.web_id = :webId\n" +
                "  AND m.link_type <> '2'\n" +
                "  AND m.del_flag = '1' ORDER BY seq asc", params, TreeNode.class);

        TreeNode treeNode = new TreeNode();
        treeNode.setId(-1);
        treeNode.setName("回收站");
        treeNodes.add(treeNode);
        return treeNodes;

    }

    private Menu getFormatedMenu(MenuDto menuDto) {
        Menu menu = menuDto.getMenu();
        if (CollectionUtils.isNotEmpty(menuDto.getSubMenu()))
            menu.setLinkType(LINK_EMPTY);

        if(LINK_EMPTY.equals(menu.getLinkType())) {
            menu.setTarget("_self");
        } else if (LINK_OUTER.equals(menu.getLinkType())) {
            menu.setTarget("_blank");
            if (StringUtils.isNotBlank(menu.getHref()) && !menu.getHref().startsWith("http://") && !menu.getHref().startsWith("/"))
                menu.setHref("http://"+menu.getHref());

        } else if (LINK_INNER.equals(menu.getLinkType())) {//page
            Page page = menuDto.getPage();
            //if exists page ignore

            if (!(menu.getPageId() != null && menu.getPageId() > 0)) {

                if (menu.getPageId() == null) { //新增页面
//                page.setId(null);
                    page.setName(RandomStringUtils.randomAlphanumeric(10));
                }

                //create page
                Layout layout = layoutDao.findOne(page.getLayoutId());
                page.setHtml(layout.getHtml());
                pageService.save(page);

                menu.setPageId(page.getId());
                menu.setTarget("_self");
            }

        }

        return menu;
    }

}
