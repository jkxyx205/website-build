package com.yodean.site.web.content.dao;

import com.yodean.site.web.content.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VideoDao extends JpaRepository<Video, Integer> {

}
