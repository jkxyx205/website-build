package com.yodean.site.web.tpl.component;

import com.rick.dev.vo.JqGrid;

/**
 * Created by rick on 2017/12/22.
 */
public class EmptyComponent extends AbstractComponent {
    public EmptyComponent(Integer contentType, String description) {
        super(contentType, description, 0, 0);
    }

    @Override
    public String gotoSettingPage() {
        return null;
    }

    @Override
    public String gotoContentPage(Integer webId, Integer categoryId) {
        return null;
    }

    @Override
    protected Integer addContent(Integer categoryId, Integer col) {
        return categoryId;
    }

    @Override
    protected JqGrid getContent(Integer categoryId, Integer page, Integer rows, Integer minNum) {
        JqGrid jqGrid = new JqGrid();
        return jqGrid;
    }
}
