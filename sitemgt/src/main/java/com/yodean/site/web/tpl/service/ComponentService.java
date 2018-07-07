package com.yodean.site.web.tpl.service;

import com.rick.dev.service.DefaultService;
import com.yodean.site.web.common.cache.PlatformCache;
import com.yodean.site.web.tpl.component.RegistrationCenter;
import com.yodean.site.web.tpl.dto.ComponentStyle;
import com.yodean.site.web.tpl.dto.ComponentTypeDto;
import com.yodean.site.web.tpl.entity.Component;
import com.yodean.site.web.tpl.entity.PageComponent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 2017/7/17.
 */
@Service
public class ComponentService extends DefaultService {
    @Resource
    private ContentService contentService;

    @Resource
    private PageComponentService pageComponentService;

    public Component getComponentById(Integer id) {
        return PlatformCache.getComponent().get(id);
    }


    public List<Component> getComponents() {
          return new ArrayList<Component>(PlatformCache.getComponent().values());
    }

    public List<Component> getComponentsByType(Integer type) {
        List<Component> components = getComponents();

        List<Component> selectable = new ArrayList<Component>();

        for (Component component : components) {
            if (component.getType().equals(type)) {
                selectable.add(component);
            }
        }
        return selectable;
    }

    public List<ComponentStyle> getComponentStyle() {
        List<ComponentStyle> componentStyleList = new ArrayList<ComponentStyle>();

        List<Component> list = getComponents();

        List<ComponentTypeDto> componentTypes  = displayComponentType();

        for (ComponentTypeDto componentType : componentTypes) {
            ComponentStyle componentStyle = new ComponentStyle();
            componentStyle.setComponentType(componentType);

            addComponents(componentStyle, list);

            componentStyleList.add(componentStyle);
        }

        return componentStyleList;
    }

    private void addComponents(ComponentStyle componentStyle, List<Component> componentList) {
        for (Component component : componentList) {
            if (componentStyle.getComponentType().getContentType().indexOf(";" + component.getType() + ";") > -1) {
                componentStyle.getComponentList().add(component);
            }
        }
    }

    private List<ComponentTypeDto> displayComponentType() {
        List<ComponentTypeDto> componentTypeList = new ArrayList<ComponentTypeDto>();



        ComponentTypeDto articleList = new ComponentTypeDto(";101;102;103;104;105;151;152;153;154;155;", "图文列表");

        ComponentTypeDto pic = new ComponentTypeDto(";201;202;203;204;251;252;253;254;255;", "图片");

        ComponentTypeDto video = new ComponentTypeDto(";301;351;352;353;354;355;", "视频");

        ComponentTypeDto article = new ComponentTypeDto(";501;502;503;504;505;506;507;508;509;510;", "模版");

//        ComponentTypeDto grid = new ComponentTypeDto(";403;", "布局");

        ComponentTypeDto advance = new ComponentTypeDto(";0;401;402;404;405;406;407;408;409;410;", "高级");


        componentTypeList.add(pic);
        componentTypeList.add(articleList);
        componentTypeList.add(video);
        componentTypeList.add(article);
//        componentTypeList.add(grid);
        componentTypeList.add(advance);

        return componentTypeList;
    }

    public String newComponentToPage(Integer webId, Integer pageId, Integer componentId, String label) {

        Component component = getComponentById(componentId);

        Integer contentType = component.getType();

        if(contentType == 403) { //新增布局
            return component.getHtml();
        }

        Integer categoryId = RegistrationCenter.getComponentType(contentType).addContent(label, component.getOptions());

        // add page_component
        PageComponent pc = new PageComponent();
        pc.setComponentId(component.getId());
        pc.setCategoryId(categoryId);
//        pc.setContentType(contentType);
        pc.setPageId(pageId);
        pc.setOptions(component.getOptions());
        pageComponentService.save(pc);

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"cpn cpn-"+pc.getId()+"\" id=\""+pc.getId()+"\">");
        sb.append(contentService.injectComponentContent(webId, pc.getId()));
        sb.append("</div></div>");

        return sb.toString();
    }

    public String historicalComponentToPage(Integer webId, Integer pageId, Integer componentPageId) {
        PageComponent ref = pageComponentService.getPageComponentById(componentPageId);
        ref.setId(null);
        ref.setPageId(pageId);
        pageComponentService.save(ref);

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"cpn\" id=\""+ref.getId()+"\">");
        sb.append(contentService.injectComponentContent(webId, componentPageId));
        sb.append("</div></div>");
        return sb.toString();
    }


}
