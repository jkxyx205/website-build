package com.yodean.site.web.platform.controller;

import com.rick.dev.dto.ResponseDTO;
import com.yodean.site.web.common.controller.WebController;
import com.yodean.site.web.common.service.PlatformService;
import com.yodean.site.web.content.entity.Site;
import com.yodean.site.web.content.service.SiteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Created by rick on 2017/10/17.
 */
@Controller
@RequestMapping("/site")
public class BuildController extends WebController{

    @Resource
    private SiteService siteService;

    @Resource
    private PlatformService platformService;

    @ModelAttribute
    public void populateModel(Model model) {
        model.addAttribute("menu", "build");
    }

    @RequestMapping("/list")
    public String buildSite(Model model) {
        List<Site> sites = siteService.getAllSites();
        model.addAttribute("sites", sites);
        return "site/site-list";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseDTO add(String title) throws IOException {
        Site site = siteService.copySite(title, 1);
//        platformService.publish(site.getId());//网站发布

        return response(site);
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseDTO delete(@PathVariable Integer id) {
        siteService.delete(id);
        return response(id);
    }


    @PostMapping("/status/{id}/{status}")
    @ResponseBody
    public ResponseDTO status(@PathVariable Integer id, @PathVariable String status) {
        Site site = siteService.findById(id);
        site.setStatus(status);
        siteService.save(site);
        return response(id);
    }


    @GetMapping("/checkPublishStatus/{webId}")
    @ResponseBody
    public ResponseDTO checkPublishStatus(@PathVariable Integer webId) {
        List<String> list = siteService.checkNoneBuildPage(webId);
        return response(list);
    }
}
