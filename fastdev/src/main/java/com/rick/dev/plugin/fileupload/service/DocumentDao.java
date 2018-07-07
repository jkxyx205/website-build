package com.rick.dev.plugin.fileupload.service;

import com.rick.dev.plugin.fileupload.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentDao extends JpaRepository<Document, Integer> {
	
}
