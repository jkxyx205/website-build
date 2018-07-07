package com.yodean.site.web.content.service;

import com.rick.dev.service.DefaultService;
import com.rick.dev.vo.JqGrid;
import com.rick.dev.vo.PageModel;
import com.yodean.site.web.content.dao.VideoDao;
import com.yodean.site.web.content.entity.Pic;
import com.yodean.site.web.content.entity.Video;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by rick on 2017/7/12.
 */
@Service
public class VideoService extends DefaultService {


   @Resource
   private VideoDao videoDao;

   public void save(Video video) {
       video.preSave();

       videoDao.save(video);
   }



   public void delete(Integer id) {
       videoDao.delete(id);
   }

   public Video getVideoById(Integer id) {
       Video video = videoDao.findOne(id);
       if (video != null && video.getEmbed().endsWith(".mp4")) {
           video.setEmbed("<video controls=\"\" autoplay=\"false\" name=\"media\"><source src=\""+video.getEmbed()+"\" type=\"video/mp4\"></video>");
       }

       return video;
   }

    public JqGrid getVideosByCategoryId(Integer id, Integer page, Integer rows) {
        Map<String, Object> params = new HashedMap();
        params.put("categoryId", id);
        return getVideos(params,page, rows);
    }


    public JqGrid getVideos(Map<String, Object> params, Integer page, Integer rows) {
        //choose edit page
        String queryName = "com.yodean.site.web.content.getVideosByCondition";


        PageModel pm = new PageModel();
        pm.setQueryName(queryName);
        pm.setPage(page);
        pm.setRows(rows);


        JqGrid jqGrid = null;
        try {
            jqGrid = jqgridService.getJqgirdData(pm, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jqGrid;
    }

    public Video getVideoByCategoryId(Integer categoryId) {
        Video video = new Video();
        video.setCategoryId(categoryId);
        Example<Video> example = Example.of(video);
        return videoDao.findOne(example);
    }

    public void sort(String[] ids) {
        int orderId = ids.length;

        for (int i = 0; i < orderId; i++) {
            Video video = getVideoById(Integer.parseInt(ids[i]));
            video.setOrderId(i);
            save(video);
        }
    }
}
