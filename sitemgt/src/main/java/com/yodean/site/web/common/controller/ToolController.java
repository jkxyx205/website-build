package com.yodean.site.web.common.controller;

import com.rick.dev.dto.ResponseDTO;
import com.yodean.site.web.common.cache.PlatformCache;
import com.yodean.site.web.common.service.IndexService;
import com.yodean.site.web.common.service.PlatformService;
import com.yodean.site.web.common.service.StaticService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by rick on 2017/9/4.
 */
@RestController
@RequestMapping("/tool")
public class ToolController extends WebController{

    @Resource
    private StaticService staticService;

    @Resource
    private PlatformService platformService;

    @Resource
    private IndexService indexService;

    @GetMapping("/static/site/{webId}")
    public ResponseDTO staticSite(@PathVariable Integer webId) throws IOException {
        staticService.staticSite(webId);
        return response("");
    }

//    @GetMapping("/static/page/{pageId}")
//    public ResponseDTO staticPage(@PathVariable Integer pageId) {
//        staticService.staticPage(pageId);
//        return response("");
//    }

    @GetMapping("/static/platform")
    public ResponseDTO platform() {
        PlatformCache.build();
        return response("");
    }

    @GetMapping("/static/platform/html")
    public ResponseDTO index2html() {
//        platformService.allSite2HTML();
        return response("");
    }


    @GetMapping(value = "/publish/{webId}")
    public ResponseDTO publish(@PathVariable Integer webId) throws IOException {
        platformService.publish(webId);
        return response("");
    }



    @GetMapping(value = "/platform/{pageId}/{styleId}/{headerId}/{footerId}/{blockId}", produces = "text/html; charset=utf-8")
    public String index2html2(@PathVariable Integer pageId, @PathVariable Integer styleId,
                                   @PathVariable Integer headerId, @PathVariable Integer footerId, @PathVariable Integer blockId) {
        return platformService.siteHTML(pageId, styleId, headerId, footerId, blockId, true);
    }

    @GetMapping(value = "/platform/header/{pageId}/{styleId}/{headerId}/{blockId}", produces = "text/html; charset=utf-8")
    public String index2header2(@PathVariable Integer pageId, @PathVariable Integer styleId,
                              @PathVariable Integer headerId, @PathVariable Integer blockId) {
        return platformService.siteHTML(pageId, styleId, headerId, null, blockId, false);
    }

    @GetMapping(value = "/platform/footer/{pageId}/{styleId}/{footerId}/{blockId}", produces = "text/html; charset=utf-8")
    public String index2footer2(@PathVariable Integer pageId, @PathVariable Integer styleId,
                                @PathVariable Integer footerId, @PathVariable Integer blockId) {
        return platformService.siteHTML(pageId, styleId, null, footerId, blockId, false);
    }





    @GetMapping("/index/{webId}")
    public ResponseDTO buildIndex(@PathVariable Integer webId) throws IOException {
        indexService.buildIndex(webId);
        return response("");
    }
}
