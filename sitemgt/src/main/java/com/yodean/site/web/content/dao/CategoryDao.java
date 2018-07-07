package com.yodean.site.web.content.dao;

import com.yodean.site.web.content.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryDao extends JpaRepository<Category, Integer> {
}
