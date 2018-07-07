package com.rick.dev.plugin.fileupload.service;

import com.rick.dev.plugin.fileupload.entity.Document;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class UploadService {
	private static final String DEFAULT_UPLOADER_NAME = "u_files";

	private static final String DEFAULT_PARAM_FOLDER = "u_folder";

	private static final String DEFAULT_PARAM_WEB_ID = "webId";

	private static final String DEFAULT_PARAM_CATEGORY_ID = "u_categoryId";


	public static final int CATEGORY_GALLERY = 0; //图库

	public static final int CATEGORY_PIC = 1; //网站图片

	public static final int CATEGORY_FILE = 2; //网站文件

	public static final int CATEGORY_VIDEO = 3; //网站视频

	@Resource
	private Upload2Disk ud;
	
	@Resource
	private DocumentService docService;

	public List<Document> handle(HttpServletRequest request, String folder) {
		return handle(request, folder, DEFAULT_UPLOADER_NAME, null, null);
	}

	public List<Document> handle(HttpServletRequest request, String folder, String name, Integer webId, Integer categoryId) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		//文件存放地址
		String _folder = multipartRequest.getParameter(DEFAULT_PARAM_FOLDER);
		if(StringUtils.isNotBlank(_folder))
		 	folder = _folder;

		//文件所属
		String webIdStr = multipartRequest.getParameter(DEFAULT_PARAM_WEB_ID);
		if(webId == null && StringUtils.isNotBlank(webIdStr))
			 webId = Integer.parseInt(webIdStr);

		//获取文件分类
		String categoryIdStr = multipartRequest.getParameter(DEFAULT_PARAM_CATEGORY_ID);
		if(categoryId == null && StringUtils.isNotBlank(categoryIdStr))
			categoryId = Integer.parseInt(categoryIdStr);



		String pidValue = multipartRequest.getParameter("pid");

		Integer pid = null;

		if (StringUtils.isNotBlank(pidValue))
			pid = Integer.parseInt(pidValue);

	     // 获得文件：
		 List<MultipartFile> files = multipartRequest.getFiles(name);
		 List<Document> retList = new ArrayList<Document>(files.size());
		 
		try {
			for (MultipartFile file : files) {
				if (file.getSize()  == 0 || StringUtils.isEmpty(file.getName()))
					continue;
				retList.add(saveFile(file, folder, webId, categoryId, pid));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retList;
	}
	
	public Document saveFile(MultipartFile file, String folder, Integer webId, Integer categoryId, Integer pid) throws IOException {
		Document doc  = ud.store(folder,file); //save to disk
		doc.setCategoryId(categoryId);
		doc.setWebId(webId);
		doc.setPid(pid);
		docService.save(doc); //save to database
		return doc;
	}
}
