package com.rick.dev.plugin.fileupload.entity;

import com.rick.dev.config.Global;
import com.rick.dev.persistence.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.File;

@Entity
@Table(name="sys_document")
public class Document extends DataEntity<Document> {
	private Integer pid;
	
	private String name;

	@Column(name="path")
	private String path;

	@Column(name="ext")
	private String ext;
	
	@Column(name="content_type")
	private String contentType;
	
	@Column(name="type")
	private String type;
	
	@Column(name="size")
	private Long size;

	@Column(name="category_id")
	private Integer categoryId;

	@Column(name="web_id")
	private Integer webId;


	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getWebId() {
		return webId;
	}

	public void setWebId(Integer webId) {
		this.webId = webId;
	}

	public String getUrlPath() {
		return Global.fileServer + "/" + this.path + "."+ this.ext;
	}


	public String getUrlThumbnailPath() {
		return Global.fileServer + "/" + this.path + "-thumbnail." + this.ext;
	}

	public String getUrlThumbnailSmallPath() {
		return Global.fileServer + "/" + this.path + "-thumbnail-small." + this.ext;
	}



	public String getFullName() {
		return this.name + "." + this.ext;
	}

	public String getFilePath() {
		return this.path + "." + this.ext;
	}

	public String getFileThumbnailPath() {
		return this.path + "-thumbnail." + this.ext;
	}

	public String getFileThumbnailSmallPath() {
		return this.path + "-thumbnail-small." + this.ext;
	}


	public String getFileAbsolutePath() {
		return Global.upload + File.separator + this.path + "." + this.ext;
	}

	public String getFileAbsouteThumbnailPath() {
		return Global.upload + File.separator + this.path + "-thumbnail." + this.ext;
	}

	public String getFileAbsouteThumbnailSmallPath() {
		return Global.upload + File.separator + this.path + "-thumbnail-small." + this.ext;
	}

	public String getVideoEmbedHTML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<video width=\"480\" height=\"350\" controls autoplay>");
		sb.append("<source src=\"");
		sb.append(getUrlPath());
		sb.append(" type=\"video/mp4\"/>");
		sb.append("</video>");
		return sb.toString();
	}
}
