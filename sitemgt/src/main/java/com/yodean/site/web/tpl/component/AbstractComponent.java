package com.yodean.site.web.tpl.component;

import com.rick.dev.config.Global;
import com.rick.dev.utils.JacksonUtils;
import com.rick.dev.utils.SpringContextHolder;
import com.rick.dev.vo.JqGrid;
import com.yodean.site.web.content.entity.Category;
import com.yodean.site.web.content.service.ArticleService;
import com.yodean.site.web.content.service.CategoryService;
import com.yodean.site.web.content.service.PicService;
import com.yodean.site.web.content.service.VideoService;
import com.yodean.site.web.tpl.component.utils.DefaultOptions;
import com.yodean.site.web.tpl.entity.PageComponent;
import com.yodean.site.web.tpl.service.PageComponentService;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by rick on 2017/11/7.
 */
public abstract class AbstractComponent {
    private Integer contentType; //控件类型

    private String description;    //控件描述

    private Integer aspectRatioW; //图片比例

    private Integer aspectRatioH;

    protected final ArticleService articleService = SpringContextHolder.getBean("articleService");
    protected final PicService picService = SpringContextHolder.getBean("picService");
    protected final VideoService videoService = SpringContextHolder.getBean("videoService");
    protected final CategoryService categoryService =  SpringContextHolder.getBean("categoryService");

    protected final PageComponentService pageComponentService = SpringContextHolder.getBean("pageComponentService");


    public AbstractComponent(Integer contentType, String description, Integer aspectRatioW, Integer aspectRatioH) {
        this.contentType = contentType;
        this.description = description;
        this.aspectRatioW = aspectRatioW;
        this.aspectRatioH = aspectRatioH;
    }

    public Integer getContentType() {
        return contentType;
    }

    public String getDescription() {
        return description;
    }

    public Integer getAspectRatioW() {
        return aspectRatioW;
    }

    public Integer getAspectRatioH() {
        return aspectRatioH;
    }

    public abstract String gotoSettingPage(); //设置页面

    public abstract String gotoContentPage(Integer webId, Integer categoryId); //编辑页面地址

    public Integer addContent(String label, String defaultOptions) { //添加默认内容
            CategoryService categoryService = SpringContextHolder.getBean("categoryService");
            Category category = new Category();
            category.setTitle(label);
            categoryService.save(category);


            int col = Integer.parseInt(JacksonUtils.readValue(defaultOptions, Map.class).get(DefaultOptions.COL) + "");
            int line = Integer.parseInt(JacksonUtils.readValue(defaultOptions, Map.class).get(DefaultOptions.LINE) + "");

            return addContent(category.getId(), col * line);
    }

    protected abstract Integer addContent(Integer categoryId, Integer col) ;

    public Map<String, Object>  getContent(Integer categoryId, Map<String, Object> optionsMap) { //获取内容
        Map<String, Object> map = new HashMap<String, Object>(5);

        map.put("label", categoryService.getCategoryById(categoryId).getTitle());

        //options
        int page = 1,rows = 1;
        int minNum = 1;
        int col = 1;

        Object pageObject = optionsMap.get("page");
        if (pageObject != null) {
            page = Integer.parseInt(pageObject+ "");
        } else{
            optionsMap.put("page", page);
        }

        if (null != optionsMap.get("col")) {
            col = Integer.parseInt(optionsMap.get("col") + "");
        } else {
            optionsMap.put("col", col);
        }

        Object rowsObject = optionsMap.get("rows");
        if (rowsObject != null) {
            minNum = rows = Integer.parseInt(rowsObject+ "");
        } else {
            try {
                int line = Integer.parseInt(optionsMap.get("line") + "");

                minNum = rows = line * col;
                if ("1".equals(optionsMap.get("listType") + "")) {//轮播
                    optionsMap.put("paging", false);
                    rows = Global.PAGE_ROWS * 2;//最多显示40条

                }
            } catch (Exception e) {
                rows = Global.PAGE_ROWS;
            } finally {
                optionsMap.put("rows", rows);
            }

        }

        Object minNumObject = optionsMap.get("minNum");
        if (minNumObject != null)
            minNum = Integer.parseInt(minNumObject+ "");


        JqGrid jqGrid = getContent(categoryId, page, rows, minNum);
        optionsMap.put("records", jqGrid.getRecords());
        optionsMap.put("total",jqGrid.getTotal());
        optionsMap.put("mdCol", getBootstrapGridParam(col));
        map.put("list", jqGrid.getRows());
        //将第一个数据放入map中
        if (jqGrid.getTotal() > 0)
            map.put("c", jqGrid.getRows().get(0));
        return map;
    }

    private int getBootstrapGridParam(Integer col) {
        int mdCol =  5 != col ? 12 / col :col;
        return mdCol;
    }


    protected abstract JqGrid getContent(Integer categoryId, Integer page, Integer rows, Integer minNum);

    public void addSetting(Map<String, Object> params) {
        formatParams(params);
        PageComponent pageComponent = addPageComponentSetting(params);
        //
        String label = (String)params.get("label");
        Category category = categoryService.getCategoryById(pageComponent.getCategoryId());
        category.setTitle(label);
        categoryService.save(category);
    }

    private PageComponent addPageComponentSetting(Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        PageComponent pageComponent = pageComponentService.getPageComponentById(id);

        Integer componentId = (Integer)params.get("componentId");

        pageComponent.setComponentId(componentId);
        pageComponent.setOptions(JacksonUtils.toJSon(params));
        pageComponentService.save(pageComponent);

        return pageComponent;
    }

    private static void formatParams(Map<String, Object> params) {
        String intVal = "componentId;id;page;col;line;sliderAutoplay;sliderSpeed;height;";
        String floatVal = "listDuration;listDelay;";
        Set<Map.Entry<String, Object>> set = params.entrySet();

        for (Map.Entry en : set) {

            if (intVal.indexOf(en.getKey() + ";") > -1) {
                Object obj = en.getValue();
                if (obj instanceof String && StringUtils.isNotBlank((String)obj)) {
                    params.put((String) en.getKey(), Integer.parseInt((String)obj));
                }

            } else if (floatVal.indexOf(en.getKey() + ";") > -1) {
                Object obj = en.getValue();
                if (obj instanceof String && StringUtils.isNotBlank((String)obj)) {
                    params.put((String) en.getKey(), Float.parseFloat((String)obj));
                }

            } else if (Boolean.FALSE.toString().equals(en.getValue()) || Boolean.TRUE.toString().equals(en.getValue()) ) {
                Object obj = en.getValue();
                if (obj instanceof String && StringUtils.isNotBlank((String)obj)) {
                    params.put((String) en.getKey(), Boolean.parseBoolean((String)obj));
                }
            }
        }
    }
}
