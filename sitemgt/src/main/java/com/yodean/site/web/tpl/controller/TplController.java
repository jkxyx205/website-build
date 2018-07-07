package com.yodean.site.web.tpl.controller;

import com.yodean.site.web.common.controller.WebController;
import com.yodean.site.web.content.service.SiteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by rick on 2017/7/12.
 */
@Controller
@RequestMapping("/web/{webId}/tpl")
public class TplController extends WebController {
    @Resource
    private SiteService siteService;


    @GetMapping
    public String gotoTplPage(@PathVariable Integer webId,Model model) {
        model.addAttribute("menu", "tpl");
        return "web/tpl";
    }
}
