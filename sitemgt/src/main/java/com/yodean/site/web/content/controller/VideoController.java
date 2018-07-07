package com.yodean.site.web.content.controller;

import com.rick.dev.dto.ResponseDTO;
import com.rick.dev.plugin.fileupload.entity.Document;
import com.rick.dev.plugin.fileupload.service.Upload2Disk;
import com.rick.dev.plugin.fileupload.service.UploadService;
import com.rick.dev.vo.JqGrid;
import com.yodean.site.web.common.cache.CacheUtils;
import com.yodean.site.web.common.controller.WebController;
import com.yodean.site.web.content.entity.Video;
import com.yodean.site.web.content.service.VideoService;
import com.yodean.site.web.tpl.service.ContentService;
import com.yodean.site.web.tpl.service.PageComponentService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2017/7/12.
 */
@Controller
@RequestMapping("/web/{webId}/content/video")
public class VideoController extends WebController {

    private static final String UPLOAD_NAME = "video_upload";
    @Resource
    private ContentService contentService;


    @Resource
    private VideoService videoService;

    @Resource
    private UploadService uploadService;

    @Resource
    private Upload2Disk ud;

    @Resource
    private PageComponentService pageComponentService;


//    @ModelAttribute
//    public void populateModel(String label, @PathVariable Integer webId, Model model) {
//        model.addAttribute("label", label);
//    }

    /***
     * 视频列表页面
     */
    @GetMapping("/list/{categoryId}")
    public String gotoVideoListPage(Integer page, @PathVariable Integer categoryId, Model model,
                                    @RequestParam(defaultValue = "0") int aspectRatioW, @RequestParam(defaultValue = "0") int aspectRatioH, @RequestAttribute Integer cpnId) throws Exception {
        Long minCpnId = pageComponentService.getStatusOfComponent(cpnId); //
        model.addAttribute("minCpnId", minCpnId);

        if (minCpnId != null &&  minCpnId.intValue() != cpnId) {
            return "web/content-edit/common/refContentPage";
        }

        //choose edit page
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoryId", categoryId);

        JqGrid jqGrid = videoService.getVideos(params, page, -1);
//
       model.addAttribute("jqGrid", jqGrid);
       model.addAttribute("categoryId", categoryId);
        model.addAttribute("aspectRatioW", aspectRatioW);
        model.addAttribute("aspectRatioH", aspectRatioH);


        return "web/content-edit/video-list/list";
    }


    /***
     *
     * @param request
     * @param video
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public ResponseDTO videoSave(HttpServletRequest request, @ModelAttribute Video video, Integer webId) throws IOException {
//        List<Document> uploadFileList = uploadService.handle(request, Global.SITE_FOLDER + File.separator + webId, webId);

//        if (CollectionUtils.isNotEmpty(uploadFileList)) {
//            //crop the pic
//            Document doc = ud.cropPic((uploadFileList.get(0)).getId(), Positions.TOP_CENTER, 9, 5);
//            video.setCover(doc.getId());
//        }

        videoService.save(video);
        CacheUtils.clearPageCacheByWebId(webId);

        return response(video);
    }


    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseDTO videoDelete(@PathVariable Integer id) {
        videoService.delete(id);

        return response(id);
    }

    @PostMapping("/sort/{categoryId}")
    @ResponseBody
    public ResponseDTO videoSort(@PathVariable Integer webId, @PathVariable Integer categoryId, String idsStr) {

        videoService.sort(idsStr.split(";"));

        CacheUtils.clearPageCacheByWebId(webId);
        return response(idsStr);
    }

    /***
     * 单视频编辑
     */
    @GetMapping("/single/{categoryId}")
    public String gotoVideoPage(@PathVariable Integer categoryId, Model model) throws Exception {
        Video video = videoService.getVideoByCategoryId(categoryId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("video", video);
        return "web/content-edit/video";
    }

    /***
     * 视频单个上传
     */
    @PostMapping("upload")
    @ResponseBody
    public ResponseDTO uploadVideo(MultipartHttpServletRequest multipartRequest, @PathVariable Integer webId) throws IOException {
        ResponseDTO responseDTO = response();

        try {
            List<MultipartFile> files = multipartRequest.getFiles(UPLOAD_NAME);

            for (MultipartFile file : files) {
                if (file.getSize()  == 0 || StringUtils.isEmpty(file.getName()))
                    continue;
                Document document = ud.store("site" + File.separator + webId, file);
                responseDTO.setData(document);
            }
        } catch (IOException e) {
            responseDTO.setStatus(ResponseDTO.STATUS_FAIL);
            throw  e;
        } finally {
            return responseDTO;
        }


    }
}