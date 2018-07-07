package com.yodean.site.web.content.service;

import com.rick.dev.service.DefaultService;
import com.rick.dev.vo.JqGrid;
import com.rick.dev.vo.PageModel;
import com.yodean.site.web.content.dao.PicDao;
import com.yodean.site.web.content.entity.Pic;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by rick on 2017/7/12.
 */
@Service
public class PicService extends DefaultService {


   @Resource
   private PicDao picDao;

   public void save(Pic pic) {
       pic.preSave();
       if (StringUtils.isNotBlank(pic.getHref()) && !pic.getHref().startsWith("http://") && !pic.getHref().startsWith("/"))
           pic.setHref("http://"+pic.getHref());

       picDao.save(pic);
   }



    public void delete(Integer id) {
       picDao.delete(id);
   }

    public Pic getPicById(Integer id) {
        return picDao.findOne(id);
    }

    public JqGrid getPicsByCategoryId(Integer categoryId, Integer page, Integer rows) {
        Map<String, Object> params = new HashedMap();
        params.put("categoryId", categoryId);
        return getPics(params,page, rows);
    }

    public JqGrid getPics(Map<String, Object> params, Integer page, Integer rows)  {
        //choose edit page
        String queryName = "com.yodean.site.web.content.getPicsByCondition";


        PageModel pm = new PageModel();
        pm.setQueryName(queryName);
        pm.setPage(page);
        pm.setRows(rows);


        JqGrid<Pic> jqGrid = null;
        try {
            jqGrid = jqgridService.getJqgirdData(pm, params, Pic.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jqGrid;

    }

    public Pic getPicByCategoryId(Integer categoryId) {
        Pic pic = new Pic();
        pic.setCategoryId(categoryId);
        Example<Pic> example = Example.of(pic);
        return picDao.findOne(example);
    }


    public void sort(String[] ids) {
        int orderId = ids.length;

        for (int i = 0; i < orderId; i++) {
            Pic pic = getPicById(Integer.parseInt(ids[i]));
            pic.setOrderId(i);
            save(pic);
        }
    }
}
