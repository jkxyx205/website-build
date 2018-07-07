package com.yodean.site.web.tpl.dto;

import com.yodean.site.web.tpl.entity.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 2017/8/19.
 */
public class ComponentStyle {
    private ComponentTypeDto componentType;

    private List<Component> componentList = new ArrayList<Component>();

    public ComponentTypeDto getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentTypeDto componentType) {
        this.componentType = componentType;
    }

    public List<Component> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<Component> componentList) {
        this.componentList = componentList;
    }
}
