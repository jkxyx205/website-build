package com.yodean.site.web.tpl.service;

import com.rick.dev.config.Global;
import com.rick.dev.service.DefaultService;
import com.rick.dev.utils.VelocityUtils;
import com.yodean.site.web.content.entity.Article;
import com.yodean.site.web.content.service.ArticleService;
import com.yodean.site.web.content.service.PicService;
import com.yodean.site.web.content.service.VideoService;
import com.yodean.site.web.tpl.component.AbstractComponent;
import com.yodean.site.web.tpl.component.RegistrationCenter;
import com.yodean.site.web.tpl.entity.Component;
import com.yodean.site.web.tpl.entity.PageComponent;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rick on 2017/7/13.
 */
@Service
public class ContentService extends DefaultService {
    @Resource
    private ArticleService articleService;

    @Resource
    private PicService picService;

    @Resource
    private VideoService videoService;

    @Resource
    private ComponentService componentService;

    @Resource
    private PageComponentService pageComponentService;


    /***
     * 获取内容区域的html
     * @param pageFrameHtml
     * @return
     */
    public String injectPageContent(Integer webId, String pageFrameHtml)  {
        Document document = Jsoup.parse(pageFrameHtml);

        Elements elements = document.select(".cpn");

        for(Element element : elements) {
            String id = element.attr("id");
            if (StringUtils.isBlank(id))
                continue;
            else {
                element.append(injectComponentContent(webId, Integer.parseInt(id)));
            }
        }

//         return document.getElementById("layout").outerHtml();
        return  document.outerHtml();
    }


    /***
     * 获取控件&内容的html
     * @param pageComponentId page componentId
     * @return
     */
    public String injectComponentContent(Integer webId, Integer pageComponentId)  {
        return injectComponentContent(webId, pageComponentId, null);
    }


    public String injectComponentContent(Integer webId, Integer pageComponentId, Map optionsMap)  {
        PageComponent pc =pageComponentService.getPageComponentById(pageComponentId);
        if (null == pc) return ""; //TODO why can this be null

        if (optionsMap == null) optionsMap = Collections.EMPTY_MAP;

        Map all = pc.getOptionsMap();

        all.putAll(optionsMap); //合并传参

        return injectComponentContent(webId, pc.getComponentId(), pc.getCategoryId(), all);
    }


    public String injectComponentContent(Integer webId, Integer componentId, Integer categoryId, Map optionsMap ) {
        Component component = componentService.getComponentById(componentId);
        return injectComponentContent(webId, component, component.getType(), categoryId, optionsMap);
    }

    public String injectComponentContent(Integer webId, Integer componentId, Integer contentType, Integer categoryId, Map optionsMap ) {
        Component component = componentService.getComponentById(componentId);
        return injectComponentContent(webId, component, contentType, categoryId, optionsMap);
    }

    public String injectComponentContent(Integer webId, Component component, Integer contentType, Integer categoryId, Map optionsMap ) {
        AbstractComponent abstractComponent = RegistrationCenter.getComponentType(contentType);
        if (MapUtils.isEmpty(optionsMap)) optionsMap = new HashMap<String, Object>();

        Map full = component.getOptionsMap();
        full.putAll(optionsMap);

        Map<String, Object> map = abstractComponent.getContent(categoryId, full);

        map.put("webId", webId);
        map.put("componentId", component.getId());
        map.put("contentType", contentType);
        map.put("categoryId", categoryId);
        map.put("time", RandomStringUtils.randomAlphabetic(5));

        map.put("options", full);
        map.put("fileServer", Global.fileServer);

        return VelocityUtils.tplFromString(component.getHtml(), map);
    }

    /***
     * 全文检索
     * @param webId
     * @param componentId
     * @param dataMap
     * @return
     */
    public String injectFullText(Integer webId, Integer componentId, Map dataMap) {
        Component component = componentService.getComponentById(componentId);
        dataMap.put("webId", webId);
        dataMap.put("componentId", componentId);

        dataMap.put("time", RandomStringUtils.randomAlphabetic(5));
        dataMap.put("fileServer", Global.fileServer);
        return VelocityUtils.tplFromString(component.getHtml(), dataMap);

    }


    /***
     * 文章详情
     * @param id
     * @return
     */
    public String injectArticleText(Integer id) {
        Map<String, Object> dataMap = new HashMap<String, Object>(1);
        Component component = componentService.getComponentById(-2);
        Article article = articleService.getArticleById(id);
        dataMap.put("c", article);
        dataMap.put("fileServer", Global.fileServer);
        return VelocityUtils.tplFromString(component.getHtml(), dataMap);

    }

    /***
     * 查看更多列表
     * @param webId
     * @param pageComponentId
     * @param optionsMap
     * @return
     */

    public String injectListComponent(Integer webId, Integer pageComponentId, Map optionsMap)  {
        PageComponent pc =pageComponentService.getPageComponentById(pageComponentId);
        if (optionsMap == null) optionsMap = Collections.EMPTY_MAP;
        return injectComponentContent(webId, pc.getComponentId(), pc.getCategoryId(), optionsMap);
    }
}
