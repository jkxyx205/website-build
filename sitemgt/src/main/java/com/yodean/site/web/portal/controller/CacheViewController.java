package com.yodean.site.web.portal.controller;

import com.yodean.site.web.content.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by rick on 2017/7/12.
 */
@Controller
@RequestMapping("/web/{webId}/view")
public class CacheViewController {
    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SiteService siteService;


    @GetMapping(value = "/page/{pageName}", produces = "text/html; charset=utf-8")
    @ResponseBody
    public String view(@PathVariable Integer webId, @PathVariable String pageName, @RequestParam(defaultValue="true") boolean lazyload) {
        String html = siteService.getPageHtmlByPage(webId, pageName, SiteService.SiteStatus.VIEW);

        if (!lazyload){
            html = html.replaceAll("src\\S+tpl/init/\\S+0[.]jpg\"", "").replace("data-original", "src").replace("wow","");
        }

        return html;
    }



    @GetMapping
    public String gotoViewPage(@PathVariable Integer webId) { //
        return "web/view";
    }

    @GetMapping(value = "/article/{id}", produces = "text/html; charset=utf-8")
    @ResponseBody
    public String viewArticlePage(@PathVariable Integer id, HttpServletRequest request) {
        //TODO 更换链接
        Integer webId = (Integer)request.getAttribute("webId");
        return siteService.getArticleHTML(webId, id);
    }

}
