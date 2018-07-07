package com.yodean.site.web.tpl.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rick.dev.persistence.DataEntity;
import com.rick.dev.utils.JacksonUtils;
import com.yodean.site.web.tpl.component.utils.ComponentHTMLSnippet;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by rick on 2017/7/12.
 */
@Entity
@Table(name = "w_component")
public class Component extends DataEntity<Component> {
    private String title;

    private String html;

    private Integer type;

    private String options;

    public String getTitle() {
        return title;
    }

    private static final Set<Integer> listType = new HashSet<Integer>();
    private static final Set<Integer> listIgnoreById = new HashSet<Integer>();
    private static final Set<Integer> listId = new HashSet<Integer>();

    private static final Set<Integer> singleIgnoreById = new HashSet<Integer>();

    static {
        for (int i = 1; i <= 5; i++) {
            listType.add(150 + i);
            listType.add(250 + i);
            listType.add(350 + i);
        }
        listId.add(206);

        listIgnoreById.add(205);

        singleIgnoreById.add(202);
        singleIgnoreById.add(203);
        singleIgnoreById.add(205);

        for(int i = 0; i <= 8; i++ ) {//布局
            singleIgnoreById.add(403 + i);
        }




    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonIgnore
    public String getHtml() {
        if((listType.contains(this.getType()) || listId.contains(this.getId())) && !listIgnoreById.contains(this.getId()))  {//
            return ComponentHTMLSnippet.LIST.replace(ComponentHTMLSnippet.PLACEHOLDER, html).replace(ComponentHTMLSnippet.CLASS, StringUtils.defaultString(getRemarks()));
        } else if(!singleIgnoreById.contains(this.getId())) {
            return ComponentHTMLSnippet.SINGLE.replace(ComponentHTMLSnippet.PLACEHOLDER, html).replace(ComponentHTMLSnippet.CLASS, StringUtils.defaultString(getRemarks()));
        }
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Map getOptionsMap() {
        Map optionsMap = JacksonUtils.readValue(getOptions(), Map.class);
        if(optionsMap == null) {
            optionsMap = new HashMap<String, Object>(4);
        }

        //获取控件图片比例
        Integer aspectRatioW = 0;
        Integer aspectRatioH = 0;
        if (type == 251 || type == 151 || type == 351) {
            aspectRatioW = 4;
            aspectRatioH = 3;
        } else if (type == 252 || type == 152 || type == 352) {
            aspectRatioW = 9;
            aspectRatioH = 5;
        } else if (type == 253 || type == 153 || type == 353) {
            aspectRatioW = 1;
            aspectRatioH = 1;
        } else if (type == 254 || type == 154 || type == 354) {
            aspectRatioW = 2;
            aspectRatioH = 3;
        } else if (type == 255 || type == 155 || type == 355) {
            aspectRatioW = 14;
            aspectRatioH = 5;
        }

        Integer sliderSpace = 15;
        if (this.getId() == 256 || this.getId() ==266
                ||this.getId() == 156 || this.getId() ==166
                ||this.getId() == 356 || this.getId() ==366) { //轮播图缝隙为0
            sliderSpace = 0;
        }

        optionsMap.put("aspectRatioW", aspectRatioW);
        optionsMap.put("aspectRatioH", aspectRatioH);
        optionsMap.put("aspectRatio", aspectRatioW + "x"+ aspectRatioH);
        optionsMap.put("sliderSpace", sliderSpace);

        return optionsMap;
    }
}
