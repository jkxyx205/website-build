package com.yodean.site.web.content.service;

import com.rick.dev.service.DefaultService;
import com.rick.dev.vo.JqGrid;
import com.rick.dev.vo.PageModel;
import com.yodean.site.web.content.dao.ArticleDao;
import com.yodean.site.web.content.entity.Article;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2017/7/12.
 */
@Service
public class ArticleService extends DefaultService {

    public static final String ARTICLE_STATUS_BOX = "0";
    public static final String ARTICLE_STATUS_PUBLISHED = "1";

   @Resource
   private ArticleDao articleDao;

   public void save(Article article) {
       article.preSave();

       articleDao.save(article);
   }



   public void delete(Integer id) {
       articleDao.delete(id);
   }

   public Article getArticleById(Integer id) {
       return articleDao.findOne(id);
   }

    public Article getActiveArticleByCategoryId(Integer categoryId) {
//        Map<String, Object> params = new HashedMap();
//        params.put("categoryId", id);
//        params.put("status", ArticleService.ARTICLE_STATUS_PUBLISHED);
//        return getArticles(params,page, rows);
        Article article = new Article();
        article.setCategoryId(categoryId);
        Example<Article> example = Example.of(article);
        return articleDao.findOne(example);
    }


    /***
     * 获取已发布文章列表
     * @param id
     * @return
     */
    public JqGrid getActiveArticlesByCategoryId(Integer id, Integer page, Integer rows) {
        Map<String, Object> params = new HashedMap();
        params.put("categoryId", id);
        params.put("status", ArticleService.ARTICLE_STATUS_PUBLISHED);
        return getArticles(params,page, rows);
    }

    public JqGrid getArticles(Map<String, Object> params, Integer page, Integer rows)  {
        //choose edit page
        String queryName = "com.yodean.site.web.content.getArticlesByCondition";


        PageModel pm = new PageModel();
        pm.setQueryName(queryName);
        pm.setPage(page);
        pm.setRows(rows);
        pm.setSidx("stickTop desc ,orderId desc, ");
        pm.setSord("publishDate desc");

        JqGrid jqGrid = null;
        try {
            jqGrid = jqgridService.getJqgirdData(pm, params, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jqGrid;

    }

    public List<Article> getArticlesByWebId(Integer webId) {
        String queryName = "com.yodean.site.web.content.getArticlesByCondition";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("webId", webId);

        return jdbcTemplateService.queryForSpecificParam( queryName, params, Article.class);
    }

    public void stickTop(Integer id, String value) {

        Article article = getArticleById(id);

        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("id", id);
        param.put("categoryId", article.getCategoryId());

        Integer orderId = jdbcTemplateService.queryForSpecificParamSQLSingle("select (IFNULL(max(order_id), 0) + 1) rowid from c_article c WHERE c.category_id = :categoryId",
                param,
                Integer.class);
        param.put("orderId", orderId);
        param.put("value", value);

//        if ("0".equals(value)) {
//            param.put("orderId", null);
//        }

        jdbcTemplateService.updateForSpecificParamSQL("UPDATE c_article SET order_id = :orderId, stick_top=:value WHERE id = :id", param);

    }
}
