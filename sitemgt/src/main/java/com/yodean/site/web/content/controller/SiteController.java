package com.yodean.site.web.content.controller;

import com.rick.dev.dto.ResponseDTO;
import com.yodean.site.web.common.cache.CacheUtils;
import com.yodean.site.web.common.controller.WebController;
import com.yodean.site.web.content.entity.Site;
import com.yodean.site.web.content.service.SiteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by rick on 2018/1/2.
 */
@Controller
@RequestMapping("/web/{webId}/site")
public class SiteController extends WebController {

    @Resource
    private SiteService siteService;

    /***
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @GetMapping
    public String gotoContentPage(Model model) throws InvocationTargetException, IllegalAccessException {
        model.addAttribute("menu", "site");
        return "web/site";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseDTO save(@PathVariable Integer webId, Site site) {
        Site _site = siteService.findById(webId);
        _site.setKeywords(site.getKeywords());
        _site.setDescription(site.getDescription());
        _site.setIcp(site.getIcp());
        _site.setPhone(site.getPhone());
        _site.setGateman(site.getGateman());
        _site.setFavicon(site.getFavicon());
        _site.setLang(site.getLang());
        _site.setOwner(site.getOwner());
        _site.setDomain(site.getDomain());
        _site.setTitle(site.getTitle());

        siteService.save(_site);
//        SiteUtils.remove(webId);
        CacheUtils.clearPageCacheByWebId(webId);


        return response(1);
    }
}
