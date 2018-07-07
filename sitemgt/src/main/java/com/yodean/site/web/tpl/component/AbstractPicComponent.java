package com.yodean.site.web.tpl.component;

import com.rick.dev.utils.JacksonUtils;
import com.rick.dev.vo.JqGrid;
import com.yodean.site.web.content.entity.Pic;
import com.yodean.site.web.tpl.component.utils.DefaultContent;

import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public abstract class AbstractPicComponent extends AbstractComponent {


    public AbstractPicComponent(Integer contentType, String description, Integer aspectRatioW, Integer aspectRatioH) {
        super(contentType, description, aspectRatioW, aspectRatioH);
    }

    @Override
    protected Integer addContent(Integer categoryId, Integer initNum) {
        List<Pic> list = DefaultContent.getDefaultPics(getAspectRatioW(), getAspectRatioH(),initNum);
        return addContent(categoryId, list);
    }


    protected Integer addContent(Integer categoryId, List<Pic> list) {
        for (Pic pic : list) {
            pic.setCategoryId(categoryId);
            picService.save(pic);
        }

        return categoryId;
    }



    @Override
    protected JqGrid getContent(Integer categoryId, Integer page, Integer rows, Integer minNum) {
        JqGrid grid = picService.getPicsByCategoryId(categoryId, page, rows);
        List<Map<String,Object>> list = grid.getRows();
        int curCount = list.size();
        if (page == 1 && curCount < minNum) { //占位符
            //get Placehoder
            int dissCount = minNum - curCount ;
            Pic pic = DefaultContent.getDefaultPic(getAspectRatioW(), getAspectRatioH());
            for (int i = 1; i <= dissCount; i++) {
                list.add(JacksonUtils.objectMapper.convertValue(pic, Map.class));
            }

        }
        return grid;
    }



}
