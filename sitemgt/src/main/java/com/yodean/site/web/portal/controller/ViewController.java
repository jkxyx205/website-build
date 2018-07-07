package com.yodean.site.web.portal.controller;

import com.rick.dev.config.Global;
import com.yodean.site.web.content.service.SiteService;
import com.yodean.site.web.tpl.dto.MenuDto;
import com.yodean.site.web.tpl.service.MenuService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2017/7/12.
 */
@RestController
@RequestMapping("/portal")
public class ViewController {
    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String PAGE_FULLTEXT = "fulltext";


    @Resource
    private SiteService siteService;

    @Resource
    private MenuService menuService;

    @GetMapping(value = {"/", ""}, produces = "text/html; charset=utf-8")
    public String viewIndexPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
       return viewPage("index",request, response);
    }

    @GetMapping(value = "/{pageName}", produces = "text/html; charset=utf-8")
    public String viewPage(@PathVariable String pageName, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Integer webId = (Integer)request.getAttribute("webId");

        if(PAGE_FULLTEXT.equals(pageName)) {
                String keywords = request.getParameter("keywords");
                return siteService.getFullTextHTML(webId, keywords);
        } else {
            return IOUtils.toString(new URL(""+Global.fileServer+"/site/"+webId+"/html/"+pageName+".html"), "utf-8");
        }

    }


    @GetMapping(value = "/{webId}/{pageName}", produces = "text/html; charset=utf-8")
    public String viewPage(@PathVariable Integer webId, @PathVariable String pageName) throws MalformedURLException {
        return siteService.getPageHtmlByPage(webId, pageName, SiteService.SiteStatus.PUBLISH);

    }


    @GetMapping(value = "/article/{id}", produces = "text/html; charset=utf-8")
    public String viewArticlePage(@PathVariable Integer id, HttpServletRequest request) {
        Integer webId = (Integer)request.getAttribute("webId");
        return siteService.getArticleHTML(webId, id);
    }

    @GetMapping(value = "/video/{id}", produces = "text/html; charset=utf-8")
    public String viewVideoPage(@PathVariable Integer id, HttpServletRequest request) {
        Integer webId = (Integer)request.getAttribute("webId");
        return siteService.getVideoHTML(webId, id);
    }


    @GetMapping(value = "/list/{cpnId}", produces = "text/html; charset=utf-8")
    public String viewListPage(@PathVariable Integer cpnId, @RequestParam Map<String, Object> params, HttpServletRequest request) {
        Integer webId = (Integer)request.getAttribute("webId");
        params.put("rows", 50);
        params.put("paging", true);
        params.put("listType", 0);
        params.put("line", 5);
        params.put("minNum", 0);
        return siteService.getListHTML(webId, cpnId, params);
    }


    @GetMapping(value="/{webId}/menu")
    public List<MenuDto> getMenuByWebId(Integer webId) {
        return menuService.getMenuByWebId(webId);
    }


}
