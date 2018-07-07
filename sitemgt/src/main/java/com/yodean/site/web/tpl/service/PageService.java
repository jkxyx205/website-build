package com.yodean.site.web.tpl.service;

import com.rick.dev.service.DefaultService;
import com.rick.dev.service.JdbcTemplateService;
import com.yodean.site.web.common.cache.ComponentPageCache;
import com.yodean.site.web.content.service.CategoryService;
import com.yodean.site.web.tpl.dao.PageDao;
import com.yodean.site.web.tpl.dto.ContentMenuDto;
import com.yodean.site.web.tpl.dto.PageComponentDto;
import com.yodean.site.web.tpl.entity.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by rick on 2017/7/12.
 */
@Service
public class PageService extends DefaultService{
    @Resource
    private PageDao pageDao;


    @Resource
    private MenuService menuService;

    @Resource
    private CategoryService categoryService;

    /***
     * 获取页面内容区域的骨架代码
     * @param webId
     * @param pageName
     * @return
     */
    public Page getPageFrameHtml(Integer webId, String pageName) {
        String queryName = "com.yodean.site.web.tpl.page.getPageFrameHtml";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", pageName);
        params.put("webId", webId);
        List<Page> list = jdbcTemplateService.queryForSpecificParam(queryName, params, Page.class);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public Page getPageById(Integer id) {
        return pageDao.findOne(id);
    }

    /***
     * 获取网站所有的控件
     * @param webId
     * @return
     */
    public List<PageComponentDto> getComponentsByWebId(Integer webId) {
        List<PageComponentDto> pageComponentDtoList = new ArrayList<PageComponentDto>();
        List<ContentMenuDto> contentMenuDtoList = menuService.getContentMenuDtoByWebId(webId);
        if (CollectionUtils.isNotEmpty(contentMenuDtoList)){
            for (ContentMenuDto contentMenuDto : contentMenuDtoList) {
                bindComponents(pageComponentDtoList, contentMenuDto);
            }
        }
        return pageComponentDtoList;
    }
    /***
     * 获取菜单下（包括子菜单的）的所有控件
     * @param
     * @return
     */
    public List<PageComponentDto> getComponentsByMenuId(Integer menuId) {

        List<PageComponentDto> pageComponentDtoList = new ArrayList<PageComponentDto>();

        ContentMenuDto contentMenuDto = menuService.getContentMenuDtoById(menuId);
        bindComponents(pageComponentDtoList, contentMenuDto);

        return pageComponentDtoList;
    }

    private void bindComponents(List<PageComponentDto> pageComponentDtoList ,ContentMenuDto contentMenuDto) {
        CollectionUtils.addAll(pageComponentDtoList, contentMenuDto.getPageComponentDtoList().iterator());

        if (CollectionUtils.isNotEmpty(contentMenuDto.getSubMenu())) {
            for (ContentMenuDto subContentMenuDto : contentMenuDto.getSubMenu()) {
                bindComponents(pageComponentDtoList, subContentMenuDto);
            }
        }
    }

    /***
     * 获取网站所有页面
     * @param webId
     * @return
     */
    public Map<Integer, Page> getPageMappingByWebId(final Integer webId) {
        final String QUERY_NAME_ALL_PAGE = "com.yodean.site.web.tpl.page.getPageFrameHtml";


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("webId", webId);

        return jdbcTemplateService.queryForSpecificParam(QUERY_NAME_ALL_PAGE, params, new JdbcTemplateService.JdbcTemplateExecutor<Map<Integer, Page>>() {

            public Map<Integer, Page> query(JdbcTemplate jdbcTemplate, String queryString, Object[] args) {
                final Map<Integer, Page> pageMap = new HashMap<Integer, Page> ();

                jdbcTemplate.query(queryString, args, new RowCallbackHandler() {
                    public void processRow(ResultSet resultSet) throws SQLException {
                        Page page = new Page();
                        page.setName(resultSet.getString("name"));
                        page.setHtml(resultSet.getString("html"));
                        page.setLayoutId(resultSet.getInt("layoutId"));
                        page.setId(resultSet.getInt("id"));
                        page.setWebId(resultSet.getInt("webId"));
                        pageMap.put(page.getId(), page);
                    }
                });

                return pageMap;
            }
        });
    }

    public List<PageComponentDto> getDeletedComponentsByWebId(Integer webId) {
        final String queryName = "com.yodean.site.web.tpl.page.getDeletedComponentsByWebId";
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("webId", webId);

        List<PageComponentDto> pageComponentDtoList = jdbcTemplateService.queryForSpecificParam(queryName, params, PageComponentDto.class);

        //去除重复内容
        List<PageComponentDto> pageComponentContentDtoList = new ArrayList<PageComponentDto>();
        Set<Integer> cpnSet = new HashSet<Integer>();

        for (PageComponentDto pc : pageComponentDtoList) {
            Integer categoryId = pc.getCategoryId();

            if (cpnSet.contains(categoryId))
                continue;

            pc.setLabel(pc.getLabel());
            pageComponentContentDtoList.add(pc);
            cpnSet.add(categoryId);
        }

        return pageComponentContentDtoList;
    }


    /***
     * 获取所有控件
     * @param pageId
     * @return
     */
    public List<PageComponentDto> getComponentsByPageId(Integer pageId) {

        List<PageComponentDto> pageComponentDtoList = ComponentPageCache.get(pageId);

        if (pageComponentDtoList != null)
            return pageComponentDtoList;

        pageComponentDtoList = new ArrayList<PageComponentDto>();
        Page page = pageDao.findOne(pageId);
        if (page == null)
            return pageComponentDtoList;

        final String queryName = "com.yodean.site.web.tpl.page.getComponentsByPageId";
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("pageId", pageId);

        pageComponentDtoList = jdbcTemplateService.queryForSpecificParam(queryName, params, PageComponentDto.class);

        ComponentPageCache.set(pageId, pageComponentDtoList);

        return pageComponentDtoList;
    }


    /***
     * 获取所有pageComponent 根据html
     * @param html
     * @return 23;323;
     */

    public String getCpnString(String html) {
        StringBuilder sb = new StringBuilder();

        Document document = Jsoup.parse(html);
        Elements elements = document.select(".cpn");
        for(Element element : elements) {
            sb.append(element.attr("id")).append(";");
        }
        sb.append("-1");

        return sb.toString();
    }
//    public List<PageComponentDto> getComponentsByPageId2(Integer pageId) {
//
//        List<PageComponentDto> pageComponentDtoList = new ArrayList<PageComponentDto>();
//        Page page = pageDao.findOne(pageId);
//        if (page == null)
//            return pageComponentDtoList;
//
//        Document document = Jsoup.parse(page.getHtml());
//        Elements elements = document.select(".cpn");
//        Set<String> cpnSet = new HashSet<String>();
//        for(Element element : elements) {
//            String id = element.attr("id");
//            PageComponent pc = pageComponentService.getPageComponentById(Integer.parseInt(id));
//            Integer componentId = pc.getComponentId() ;
//            String contentType = pc.getContentType();
//            Integer categoryId = pc.getContentId();
//
//            String contentKey = contentType + "-" + categoryId;
//
//            if (cpnSet.contains(contentKey))
//                continue;
//
//            Map<String, Object> map = contentService.getContentById(contentType, categoryId, pc.getOptionsMap(), false);
//
//            PageComponentDto pageComponentDto = new PageComponentDto();
//            pageComponentDto.setId(pc.getId());
//            pageComponentDto.setComponentId(componentId);
//            pageComponentDto.setContentType(contentType);
//            pageComponentDto.setContentId(categoryId);
//            pageComponentDto.setLabel(map.get("label") + " " );
//            pageComponentDto.setPageId(pageId);
//            pageComponentDto.setOptionsMap(pc.getOptionsMap());
//
//            pageComponentDtoList.add(pageComponentDto);
//
//            cpnSet.add(contentKey);
//        }
//
//        return pageComponentDtoList;
//    }



    public void updatePage(Integer pageId, String html) {
        Page page = pageDao.findOne(pageId);

        page.setHtml(html);

        pageDao.save(page);
    }

    public void save(Page page) {
        page.preSave();
        pageDao.save(page);
    }
}
