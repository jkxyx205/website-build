package com.rick.dev.plugin.ckeditor;

import com.rick.dev.plugin.fileupload.entity.Document;
import com.rick.dev.plugin.fileupload.service.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/ckeditor")
public class CkEditorController {
	private static String CONTENT_FOLDER = "ckeditor";

	@Resource
	private UploadService uploadService;

	@RequestMapping(value="/uploadImg",method=RequestMethod.POST)
	public void uploadImg(HttpServletRequest request,     
            HttpServletResponse response) throws FileNotFoundException, IOException {
		//TODO  传递webId值
		 List<Document> list = uploadService.handle(request, CONTENT_FOLDER, "upload", 0, 1);
		 String callback = request.getParameter("CKEditorFuncNum"); 
		 
		 
		 response.setContentType("text/html");
		 
		 PrintWriter out = response.getWriter();
		 out.write("<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction("+callback+",'/download/"+list.get(0).getId()+"','');</script>");
		 out.close();
			
			
	}
	
	@RequestMapping(value="/uploadFile",method=RequestMethod.POST)
	@ResponseBody
	public List<Document> uploadFile(HttpServletRequest request,
            HttpServletResponse response) throws FileNotFoundException, IOException {
		//TODO
		return uploadService.handle(request, CONTENT_FOLDER, "upload", 0, 2);
	}
	
}
