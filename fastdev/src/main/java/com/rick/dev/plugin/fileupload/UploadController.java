package com.rick.dev.plugin.fileupload;

import com.rick.dev.plugin.fileupload.entity.Document;
import com.rick.dev.plugin.fileupload.service.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;


@Controller
@RequestMapping("/upload")
public class UploadController{
	
	private static final String FORM_FOLDER = File.separator + "document";
	
	
	@Resource
	private UploadService uploadService;
	
	
	/**
	 * 表单上传
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value={"/file"},method=RequestMethod.POST)
	@ResponseBody
	public List<Document> uploadFile(HttpServletRequest request,
									 HttpServletResponse response) {
		
		 
		 List<Document> retList = uploadService.handle(request, FORM_FOLDER);
		 return retList;
	}
	
	
	
	
}
