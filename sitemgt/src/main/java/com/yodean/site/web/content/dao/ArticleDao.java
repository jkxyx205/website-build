package com.yodean.site.web.content.dao;

import com.yodean.site.web.content.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ArticleDao extends JpaRepository<Article, Integer> {

}
