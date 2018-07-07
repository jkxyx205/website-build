package com.yodean.site.web.content.controller;

import com.rick.dev.config.Global;
import com.rick.dev.dto.ResponseDTO;
import com.rick.dev.plugin.fileupload.service.UploadService;
import com.rick.dev.vo.JqGrid;
import com.yodean.site.web.common.cache.CacheUtils;
import com.yodean.site.web.common.controller.WebController;
import com.yodean.site.web.content.entity.Article;
import com.yodean.site.web.content.entity.Site;
import com.yodean.site.web.content.service.ArticleService;
import com.yodean.site.web.content.service.SiteService;
import com.yodean.site.web.tpl.service.ContentService;
import com.yodean.site.web.tpl.service.PageComponentService;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rick on 2017/7/12.
 */
@Controller
@RequestMapping("/web/{webId}/content/article")
public class ArticleController extends WebController {

    @Resource
    private ContentService contentService;

    @Resource
    private ArticleService articleService;

    @Resource
    private UploadService uploadService;

    @Resource
    private SiteService siteService;

    @Resource
    private PageComponentService pageComponentService;


//    @ModelAttribute
//    public void populateModel(String label, @PathVariable Integer webId, Model model) {
//        model.addAttribute("label", label);
//    }

    /***
     * 文章列表页面
     */
    @GetMapping("/list/{categoryId}")
    public String gotoArticleListPage(@ModelAttribute Article article, Integer page, @PathVariable Integer categoryId, Model model,
                                      @RequestParam(defaultValue = "0") int aspectRatioW, @RequestParam(defaultValue = "0") int aspectRatioH, @RequestAttribute Integer cpnId) throws Exception {
        Long minCpnId = pageComponentService.getStatusOfComponent(cpnId); //
        model.addAttribute("minCpnId", minCpnId);

        if (minCpnId != null &&  minCpnId.intValue() != cpnId) {
            return "web/content-edit/common/refContentPage";
        }

        //choose edit page
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoryId", categoryId);
        params.put("title", article.getTitle());
        params.put("status", article.getStatus());

        model.addAttribute("aspectRatioW", aspectRatioW);
        model.addAttribute("aspectRatioH", aspectRatioH);


        JqGrid jqGrid = articleService.getArticles(params, page, Global.PAGE_ROWS);

        model.addAttribute("jqGrid", jqGrid);


        return "web/content-edit/article-list/list";
    }

    /***
     * 文章添加页面
     */
    @GetMapping("/add/{categoryId}")
    public String gotoArticleAddPage(@PathVariable Integer categoryId,Model model, Integer aspectRatioW, Integer aspectRatioH) {
        model.addAttribute("categoryId", categoryId);
        Article article = new Article();
        article.setStatus(ArticleService.ARTICLE_STATUS_BOX);
        article.setPublishDate(new Date());
        model.addAttribute("article", article);
        model.addAttribute("aspectRatioW", aspectRatioW);
        model.addAttribute("aspectRatioH", aspectRatioH);
        return "web/content-edit/article-list/edit";
    }


    /***
     * 文章修改页面
     */
    @GetMapping("/edit/{categoryId}/{id}")
    public String gotoArticleEditPage(@PathVariable Integer categoryId, @PathVariable Integer id, Model model, Integer aspectRatioW, Integer aspectRatioH) {
        model.addAttribute("categoryId", categoryId);
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        model.addAttribute("aspectRatioW", aspectRatioW);
        model.addAttribute("aspectRatioH", aspectRatioH);
        return "web/content-edit/article-list/edit";
    }



    /***
     * 文章编辑
     */
    @PostMapping("/save")
    @ResponseBody
    public ResponseDTO articleSave(HttpServletRequest request, @ModelAttribute  Article article, @PathVariable Integer webId) {
//        List<Document> uploadFileList = uploadService.handle(request, Global.IMAGE_FOLDER, webId);

        HtmlToPlainText htmlToPlainText = new HtmlToPlainText();
        String content = htmlToPlainText.getPlainText(Jsoup.parse(article.getHtml())).trim();

//        if (CollectionUtils.isNotEmpty(uploadFileList)) {
//            article.setCover(uploadFileList.get(0).getId());
//        }
        if (StringUtils.isBlank(article.getDescription())) {
            article.setDescription(content);
        }
        article.setDescription(StringUtils.abbreviate(article.getDescription(), 180));

        article.setContent(content);

        articleService.save(article);

        if (ArticleService.ARTICLE_STATUS_PUBLISHED.equals(article.getStatus()))
            CacheUtils.clearPageCacheByWebId(webId);
        return  response(article);
    }


    /***
     * 单图文页面
     */
    @GetMapping("/single/{categoryId}")
    public String gotoArticlePage(@PathVariable Integer categoryId, Model model) throws Exception {
        Article article = articleService.getActiveArticleByCategoryId(categoryId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("article", article);
        return "web/content-edit/article";
    }

    /***
     * html页面
     */
    @GetMapping("/html/{categoryId}")
    public String gotoHTMLPage(@PathVariable Integer categoryId, Model model, @PathVariable Integer webId) throws Exception {
        Article article = articleService.getActiveArticleByCategoryId(categoryId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("article", article);

        Site site = siteService.findById(webId);
        model.addAttribute("site", site);

        return "web/content-edit/html";
    }

    /***
     * 文章删除
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseDTO articleDelete(@PathVariable Integer id, @PathVariable Integer webId) {
        articleService.delete(id);
        CacheUtils.clearPageCacheByWebId(webId);
        return response(id);
    }

    /***
     * 文章 置顶功能
     */
    @PostMapping("/stickTop/{id}/{value}")
    @ResponseBody
    public ResponseDTO stickTop(@PathVariable Integer id, @PathVariable String value, @PathVariable Integer webId) {
//        articleService.delete(id);

        articleService.stickTop(id, value);

        CacheUtils.clearPageCacheByWebId(webId);
        return response(id);
    }

}
