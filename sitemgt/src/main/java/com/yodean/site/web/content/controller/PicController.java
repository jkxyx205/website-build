package com.yodean.site.web.content.controller;

import com.rick.dev.dto.ResponseDTO;
import com.rick.dev.plugin.fileupload.service.Upload2Disk;
import com.rick.dev.plugin.fileupload.service.UploadService;
import com.rick.dev.vo.JqGrid;
import com.yodean.site.web.common.cache.CacheUtils;
import com.yodean.site.web.common.controller.WebController;
import com.yodean.site.web.content.entity.Pic;
import com.yodean.site.web.content.service.PicService;
import com.yodean.site.web.tpl.service.ContentService;
import com.yodean.site.web.tpl.service.PageComponentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2017/7/12.
 */
@Controller
@RequestMapping("/web/{webId}/content/pic")
public class PicController extends WebController {

    @Resource
    private PicService picService;

    @Resource
    private PageComponentService pageComponentService;

//    @ModelAttribute
//    public void populateModel(String label, @PathVariable Integer webId, Model model) {
//        model.addAttribute("label", label);
//    }

    /***
     * 图片列表页面
     */
    @GetMapping("/list/{categoryId}")
    public String gotoPicListPage(Integer page, @PathVariable Integer categoryId, Model model, @RequestParam(defaultValue="list") String listPage,
                                  @RequestParam(defaultValue = "0") int aspectRatioW, @RequestParam(defaultValue = "0") int aspectRatioH,  @RequestAttribute Integer cpnId) throws Exception {

        Long minCpnId = pageComponentService.getStatusOfComponent(cpnId); //
        model.addAttribute("minCpnId", minCpnId);

        if (minCpnId != null &&  minCpnId.intValue() != cpnId) {
            return "web/content-edit/common/refContentPage";
        }

        //choose edit page
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoryId", categoryId);


        JqGrid jqGrid = picService.getPics(params, page, -1);
//
       model.addAttribute("jqGrid", jqGrid);
       model.addAttribute("categoryId", categoryId);

        model.addAttribute("aspectRatioW", aspectRatioW);
        model.addAttribute("aspectRatioH", aspectRatioH);
        return "web/content-edit/pic-list/" + listPage;
    }


    /***
     * 轮播图列表页面
     */
    @GetMapping("/carousel-list/{categoryId}")
    public String gotoCarouselListPage(Integer page, @PathVariable Integer categoryId, Model model,  @RequestAttribute Integer cpnId) throws Exception {

        Long minCpnId = pageComponentService.getStatusOfComponent(cpnId); //
        model.addAttribute("minCpnId", minCpnId);

        if (minCpnId != null &&  minCpnId.intValue() != cpnId) {
            return "web/content-edit/common/refContentPage";
        }

        //choose edit page
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoryId", categoryId);


        JqGrid jqGrid = picService.getPics(params, page, -1);
//
        model.addAttribute("jqGrid", jqGrid);
        model.addAttribute("categoryId", categoryId);

        return "web/content-edit/pic-list/carousel-list";
    }

    /***
     *
     * @param pic
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public ResponseDTO picSave(@ModelAttribute Pic pic, @PathVariable Integer webId) throws IOException {
        picService.save(pic);
        CacheUtils.clearPageCacheByWebId(webId);

        return response(pic);
    }

    /***
     *
     * @param pics
     * @return
     */
    @PostMapping("/batchSave")
    @ResponseBody
    public ResponseDTO picBatchSave(@RequestBody List<Pic> pics, @PathVariable Integer webId) throws IOException {

        for (Pic pic : pics) {
            picService.save(pic);
        }

        CacheUtils.clearPageCacheByWebId(webId);

        return response(pics);
    }


    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseDTO picDelete(@PathVariable Integer id, @PathVariable Integer webId) {
        picService.delete(id);
        CacheUtils.clearPageCacheByWebId(webId);
        return response(id);
    }

    @PostMapping("/sort/{categoryId}")
    @ResponseBody
    public ResponseDTO picSort(@PathVariable Integer webId, @PathVariable Integer categoryId, String idsStr) {

        picService.sort(idsStr.split(";"));

        CacheUtils.clearPageCacheByWebId(webId);
        return response(idsStr);
    }




    /***
     * 单图片
     */
    @GetMapping("/single/{categoryId}")
    public String gotoPicPage(@PathVariable Integer categoryId, Model model) throws Exception {
        Pic pic = picService.getPicByCategoryId(categoryId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("pic", pic);
        return "web/content-edit/pic";
    }

    /***
     * 地图
     */
    @GetMapping("/map/{categoryId}")
    public String gotoMapPage(@PathVariable Integer categoryId, Model model) throws Exception {
        Pic pic = picService.getPicByCategoryId(categoryId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("pic", pic);
        return "web/content-edit/map";
    }

}
