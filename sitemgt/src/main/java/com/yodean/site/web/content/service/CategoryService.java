package com.yodean.site.web.content.service;

import com.rick.dev.service.DefaultService;
import com.yodean.site.web.content.dao.CategoryDao;
import com.yodean.site.web.content.entity.Category;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by rick on 2017/7/12.
 */
@Service
public class CategoryService extends DefaultService {

   @Resource
   private CategoryDao categoryDao;

   public void save(Category catetory) {
       catetory.preSave();

       categoryDao.save(catetory);
   }

   public void delete(Integer id) {
       categoryDao.delete(id);
   }

   public Category getCategoryById(Integer id) {
       return categoryDao.findOne(id);
   }
}
