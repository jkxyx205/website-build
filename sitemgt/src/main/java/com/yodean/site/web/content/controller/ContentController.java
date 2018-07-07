package com.yodean.site.web.content.controller;

import com.rick.dev.dto.ResponseDTO;
import com.rick.dev.plugin.fileupload.entity.Document;
import com.rick.dev.plugin.fileupload.service.DocumentService;
import com.rick.dev.plugin.fileupload.service.UploadService;
import com.rick.dev.service.JqgridService;
import com.rick.dev.vo.JqGrid;
import com.rick.dev.vo.PageModel;
import com.yodean.site.web.common.controller.WebController;
import com.yodean.site.web.tpl.component.RegistrationCenter;
import com.yodean.site.web.tpl.dto.ContentMenuDto;
import com.yodean.site.web.tpl.dto.MenuDto;
import com.yodean.site.web.tpl.dto.PageComponentDto;
import com.yodean.site.web.tpl.service.MenuService;
import com.yodean.site.web.tpl.service.PageComponentService;
import com.yodean.site.web.tpl.service.PageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2017/7/12.
 */
@Controller
@RequestMapping("/web/{webId}/content")
public class ContentController extends WebController {
    @Resource
    private MenuService menuService;

    @Resource
    private DocumentService documentService;

    @Resource
    private PageComponentService pageComponentService;

    @Resource
    private PageService pageService;

    @Resource
    private JqgridService jqgridService;

    @GetMapping
    public String gotoContentPage(@PathVariable Integer webId,Model model, String pageName, Integer id) throws InvocationTargetException, IllegalAccessException {
        model.addAttribute("menu", "content");

        List<ContentMenuDto> menuList = menuService.getContentMenuDtoByWebId(webId);
        model.addAttribute("menuList", menuList);

        if (id != null && id > -1) {
            PageComponentDto cpn = pageComponentService.getPageComponentDtoById(id);
            model.addAttribute("cpn", cpn);

        }


        model.addAttribute("pageComponentId", id);
//        if (StringUtils.isNotEmpty(pageName)) {
            model.addAttribute("pageName", pageName);
//        }

        return "web/content";
    }

    @GetMapping("/bin")
    @ResponseBody
    public  List<PageComponentDto> getBinList(@PathVariable Integer webId) {
        return pageService.getDeletedComponentsByWebId(webId);
    }


    /***
     * 统一编辑界面
     */
    @GetMapping("/update/{cpnId}/{componentId}/{contentType}/{categoryId}")
    public String gotoEditorPage(@PathVariable Integer webId, @RequestAttribute @PathVariable Integer cpnId, @PathVariable Integer componentId, @PathVariable Integer contentType, @PathVariable Integer categoryId, String label, Model model) {
        //choose edit page
//        model.addAttribute("webId", webId);
//        model.addAttribute("cpnId", cpnId);
//        model.addAttribute("componentId", componentId);
//        model.addAttribute("contentType", contentType);
//        model.addAttribute("categoryId", categoryId);
//        model.addAttribute("label", label);

//        model.addAttribute("status", pageComponentService.getStatusOfComponent(cpnId));

        return "forward:"+ RegistrationCenter.getComponentType(contentType).gotoContentPage(webId, categoryId);
    }



    @GetMapping("/picDialog")
    public String picDialog(@PathVariable Integer webId, Model model) throws Exception {
        Document exm = new Document();
        exm.setWebId(webId);
        exm.setCategoryId(UploadService.CATEGORY_PIC);


        JqGrid<Document>  jqGrid = documentService.findDocument(exm, 1);
        model.addAttribute("list", jqGrid.getRows());
        return "web/common/picDialog";
    }


    @GetMapping("/videoDialog")
    public String videoDialog(@PathVariable Integer webId, Model model) throws Exception {
        Document exm = new Document();
        exm.setWebId(webId);
        exm.setCategoryId(UploadService.CATEGORY_VIDEO);

        JqGrid<Document>  jqGrid = documentService.findDocument(exm, 1);
        model.addAttribute("list", jqGrid.getRows());
        return "web/common/videoDialog";
    }


    @GetMapping("/api/picList")
    @ResponseBody
    public List<Document> picDialog(@PathVariable Integer webId, Document exm, Integer page) throws Exception {
        if (UploadService.CATEGORY_GALLERY == exm.getCategoryId())
            exm.setWebId(null);

        JqGrid<Document>  jqGrid = documentService.findDocument(exm, page);
        return jqGrid.getRows();
    }







    @GetMapping("/pageDialog")
    @ResponseBody
    public List<MenuDto>  pageDialog(@PathVariable Integer webId, Model model) {
        List<MenuDto> menuList = menuService.getMenuByWebId(webId);
        return menuList;
//        model.addAttribute("menuList", menuList);
//        return "web/common/link/pageDialog";
    }

    /***
     * 获取所有发布的图文
     * @param webId
     * @return
     */
    @GetMapping("/articleDialog")
    public String articleDialog(@PathVariable Integer webId, Model model) throws Exception {

        PageModel pageModel = new PageModel("com.yodean.site.web.content.getSiteArticles");
        pageModel.setPage(1);
        pageModel.setRows(10);

        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("webId", webId);

        JqGrid jqGrid = jqgridService.getJqgirdData(pageModel, params);
        model.addAttribute("jqGrid", jqGrid);
        model.addAttribute("webId", webId);
        return "web/common/link/articleDialog";
    }

    @GetMapping("/article/list")
    @ResponseBody
    public JqGrid articleList(@PathVariable Integer webId, @RequestParam(defaultValue = "1", name="article-page") Integer page, @RequestParam(defaultValue = "",  name="article-title") String title) throws Exception {

        PageModel pageModel = new PageModel("com.yodean.site.web.content.getSiteArticles");
        pageModel.setPage(page);
        pageModel.setRows(10);

        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("webId", webId);
        params.put("title", title);


        JqGrid jqGrid = jqgridService.getJqgirdData(pageModel, params);

        return jqGrid;
    }

    @PostMapping("remove/{categoryId}")
    @ResponseBody
    public ResponseDTO removeComponent(@PathVariable Integer categoryId) {
        pageComponentService.deleteByContentId(categoryId);
        return response(categoryId);
    }

    @PostMapping("removeAll/{webId}")
    @ResponseBody
    public ResponseDTO removeAllComponent(@PathVariable Integer webId) {
        List<PageComponentDto> removedPageComponents = pageService.getDeletedComponentsByWebId(webId);
        for (PageComponentDto pageComponentDto : removedPageComponents) {
            pageComponentService.deleteByContentId(pageComponentDto.getCategoryId());
        }

        return response(webId);
    }

}
