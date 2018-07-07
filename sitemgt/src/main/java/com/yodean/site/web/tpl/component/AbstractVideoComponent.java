package com.yodean.site.web.tpl.component;

import com.rick.dev.utils.JacksonUtils;
import com.rick.dev.vo.JqGrid;
import com.yodean.site.web.content.entity.Video;
import com.yodean.site.web.tpl.component.utils.DefaultContent;

import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2017/11/7.
 * 多图片
 */
public abstract class AbstractVideoComponent extends AbstractComponent {


    public AbstractVideoComponent(Integer contentType, String description, Integer aspectRatioW, Integer aspectRatioH) {
        super(contentType, description, aspectRatioW, aspectRatioH);
    }


    @Override
    protected Integer addContent(Integer categoryId, Integer initNum) {
        List<Video> list = DefaultContent.getDefaultVideos(getAspectRatioW(), getAspectRatioH(),initNum);
        return addContent(categoryId, list);
    }


    protected Integer addContent(Integer categoryId, List<Video> list) {
        for (Video video  : list) {
            video.setCategoryId(categoryId);
            videoService.save(video);
        }

        return categoryId;
    }



    @Override
    protected JqGrid getContent(Integer categoryId, Integer page, Integer rows, Integer minNum) {
        JqGrid grid = videoService.getVideosByCategoryId(categoryId, page, rows);
        List<Map<String,Object>> list = grid.getRows();
        int curCount = list.size();
        if (page == 1 && curCount < minNum) { //占位符
            //get Placehoder
            int dissCount = minNum - curCount ;
            Video video = DefaultContent.getDefaultVideo(getAspectRatioW(), getAspectRatioH());
            for (int i = 1; i <= dissCount; i++) {
                list.add(JacksonUtils.objectMapper.convertValue(video, Map.class));
            }

        }
        return grid;
    }



}
