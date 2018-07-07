package com.yodean.site.web.tpl.dao;

import com.yodean.site.web.content.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SiteDao extends JpaRepository<Site, Integer> {

}
