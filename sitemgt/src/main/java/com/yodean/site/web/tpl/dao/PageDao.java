package com.yodean.site.web.tpl.dao;

import com.yodean.site.web.tpl.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PageDao extends JpaRepository<Page, Integer> {

}
