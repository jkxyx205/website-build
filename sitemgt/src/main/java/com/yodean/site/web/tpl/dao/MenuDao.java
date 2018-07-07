package com.yodean.site.web.tpl.dao;

import com.yodean.site.web.tpl.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MenuDao extends JpaRepository<Menu, Integer> {

}
