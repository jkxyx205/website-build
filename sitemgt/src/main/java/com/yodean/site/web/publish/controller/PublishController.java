package com.yodean.site.web.publish.controller;

import com.yodean.site.web.common.controller.WebController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by rick on 2017/7/12.
 */
@Controller
@RequestMapping("/web/{webId}/publish")
public class PublishController extends WebController {

    @ModelAttribute
    public void populateModel(@PathVariable Integer webId, Model model) {
        model.addAttribute("menu", "publish");
    }

    @GetMapping
    public String gotoPublishPage(Model model) {
        return "web/publish";
    }
}
