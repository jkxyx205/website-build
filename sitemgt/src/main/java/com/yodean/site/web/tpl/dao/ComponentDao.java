package com.yodean.site.web.tpl.dao;

import com.yodean.site.web.tpl.entity.Component;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ComponentDao extends JpaRepository<Component, Integer> {

}
