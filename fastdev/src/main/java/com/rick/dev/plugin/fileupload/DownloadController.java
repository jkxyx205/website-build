package com.rick.dev.plugin.fileupload;

import com.rick.dev.plugin.fileupload.entity.Document;
import com.rick.dev.plugin.fileupload.service.DocumentService;
import com.rick.dev.plugin.fileupload.service.Upload2Disk;
import com.rick.dev.utils.ServletContextUtil;
import net.coobird.thumbnailator.geometry.Coordinate;
import net.coobird.thumbnailator.geometry.Position;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/download")
public class DownloadController{
	
	@Resource
	private DocumentService docService;
	
	@Resource
	private Upload2Disk ud;
 
	@RequestMapping("/{id}")
	public void download(HttpServletRequest request,HttpServletResponse response,@PathVariable int id) throws IOException {
		Document doc = docService.findDocumentById(id);
		OutputStream os = ServletContextUtil.getOsFromResponse(response, request, doc.getName());
		IOUtils.write(FileUtils.readFileToByteArray(new File(doc.getFileAbsolutePath())), os);
		os.close();
	}

	@RequestMapping("/thumbnail/{id}")
	public void downloadThumbnail(HttpServletRequest request,HttpServletResponse response,@PathVariable int id) throws IOException {
		Document doc = docService.findDocumentById(id);
		OutputStream os = ServletContextUtil.getOsFromResponse(response, request, doc.getName());
		IOUtils.write(FileUtils.readFileToByteArray(new File(doc.getFileAbsolutePath())), os);
		os.close();

	}

//	@PostMapping("/crop/{id}")
//	@ResponseBody
//	public Document crop(@PathVariable Integer id, int srcWidth, int x, int y, int w, int aspectRatioW, int aspectRatioH) throws IOException {
//		return ud.cropPic(id, srcWidth, x, y, w, aspectRatioW, aspectRatioH);
//	}

	/***
	 * 手动裁剪
	 * @param id
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param aspectRatioW
	 * @param aspectRatioH
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/manual_crop/{id}")
	@ResponseBody
	public Document crop(@PathVariable Integer id, int x, int y, int w, int h, int aspectRatioW, int aspectRatioH) throws IOException {
		Position position = new Coordinate(x, y);
		Document doc = docService.findDocumentById(id);
		return ud.cropPic(doc, position, w, h, aspectRatioW, aspectRatioH);

	}

	/***
	 * 自动裁剪
	 * @param id
	 * @param aspectRatioW
	 * @param aspectRatioH
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/auto_crop/{id}")
	@ResponseBody
	public Document crop(@PathVariable Integer id, int aspectRatioW, int aspectRatioH) throws IOException {
		Document doc = docService.findDocumentById(id);
		return ud.cropPic(doc, aspectRatioW, aspectRatioH);
	}


	/***
	 * 批量自动裁剪
	 * @param ids
	 * @param aspectRatioW
	 * @param aspectRatioH
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/multi_auto_crop")
	@ResponseBody
	public List<Document> crop(String ids, int aspectRatioW, int aspectRatioH) throws IOException {
		String[] idList = ids.split(";");
		List<Document> list = new ArrayList<Document>(idList.length);

		for (String id : idList) {
			Document doc = docService.findDocumentById(Integer.parseInt(id));
			list.add(ud.cropPic(doc, aspectRatioW, aspectRatioH));
		}

		return list;
	}

}
