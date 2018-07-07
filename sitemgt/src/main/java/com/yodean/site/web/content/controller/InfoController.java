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
@RequestMapping("/web/{webId}/content/info")
public class InfoController extends WebController {

    @Resource
    private SiteService siteService;

    /***
     * @param webId
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @GetMapping
    public String gotoContentPage(@PathVariable Integer webId, String label, Model model) throws InvocationTargetException, IllegalAccessException {
        model.addAttribute("label", label);
        return "web/content-edit/info";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseDTO save(@PathVariable Integer webId, Site site) {
        Site _site = siteService.findById(webId);

        _site.setLogo(site.getLogo());
        _site.setTel(site.getTel());
        _site.setAddr(site.getAddr());
        _site.setEmail(site.getEmail());
        _site.setFax(site.getFax());
        _site.setWechat(site.getWechat());
        _site.setWeibo(site.getWeibo());

        siteService.save(_site);
//        SiteUtils.remove(webId);
        CacheUtils.clearPageCacheByWebId(webId);


        return response(1);
    }
}
