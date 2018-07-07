package com.rick.dev.plugin.fileupload.service;

import com.rick.dev.config.Global;
import com.rick.dev.plugin.fileupload.entity.Document;
import com.rick.dev.utils.FileUtils;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;



@Service
public class Upload2Disk {
	
//	@Value("${upload}")
//	private String upload;
	
	public static final String FOLDER_SEPARATOR = "/";
	
//	public static final String FOLDER_NAME_FORMAT = "yyyyMMdd";

	public static final double CROP_SIZE = 500 * 1024;
	@Resource
	private DocumentService documentService;
	
//
//	public String getUpload() {
//		return upload;
//	}

	private File getFolder(String subFolder) {
		String uploadPath = subFolder;// + File.separator + DateUtils.getDate(FOLDER_NAME_FORMAT);

		String folder = Global.upload + File.separator + uploadPath;

		File ff = new File(folder);
		if(!ff.exists()) {
			ff.mkdirs();
		}

		return ff;
	}

	private Document getInfoFromFile(MultipartFile file) {
		String fileName = StringUtils.stripFilenameExtension(file.getOriginalFilename());
		String fileExt = StringUtils.getFilenameExtension(file.getOriginalFilename());

		Document document = new Document();
		document.setName(fileName);
		document.setType(DocumentService.FILE_FILE);
		document.setContentType(file.getContentType());
		document.setSize(file.getSize());
		document.setExt(fileExt);

		return document;
	}

	public Document store(String subFolder,MultipartFile file) throws IOException {
		String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
		String name = UUID.randomUUID().toString();
		String fileStoreName = name + "." +  ext;

		File ff = getFolder(subFolder);
		File srcFile = new File(ff,fileStoreName);

		FileOutputStream fos = new FileOutputStream(srcFile);
		IOUtils.write(file.getBytes(), fos);
		fos.close();

//		file.transferTo(srcFile);

		Document doc = getInfoFromFile(file);
		doc.setPath((subFolder+ File.separator + name).replace(File.separator, FOLDER_SEPARATOR));

		if (doc.getContentType().startsWith("image")) {//处理图片文件
			//图片过大保存缩略图
			String fileStoreThumbnailName = name + "-thumbnail." + ext;
			String fileStoreThumbnailSamllName = name + "-thumbnail-small." + ext;
			File thumbnailFile = new File(ff, fileStoreThumbnailName);
			File thumbnailSmallFile = new File(ff, fileStoreThumbnailSamllName);

			if(doc.getSize() > CROP_SIZE) {
//				double scaleFactor  =  CROP_SIZE / doc.getSize();
//				scaleFactor = scaleFactor < 0.5d ? 0.5d :scaleFactor;

				Thumbnails.of(srcFile)
//						.outputQuality(scaleFactor)
//						.scale(1f)
						.size(1920, 1080)
						.toFile(thumbnailFile);


			} else {
				org.apache.commons.io.FileUtils.copyFile(srcFile, thumbnailFile);
			}

			//small picture
			Thumbnails.of(srcFile)
					.size(400, 400)
					.toFile(thumbnailSmallFile);
		}

		return doc;
	}

//	public String getRealPath(Document doc) {
//		String filePath = doc.getPath().replace(Upload2Disk.FOLDER_SEPARATOR,File.separator);
//		return getUpload() + File.separator + filePath + doc.getExt();
//	}
//
//	public String getThumbnailPath(Document doc) {
//		String filePath = doc.getPath().replace(Upload2Disk.FOLDER_SEPARATOR,File.separator);
//		return getUpload() + File.separator + filePath + "-thumbnail" + doc.getExt();
//	}

//	public Document cropPic(int id, Position position, int aspectRatioW, int aspectRatioH) throws IOException {
//		Document doc = documentService.findDocumentById(id);
//
//		File file = new File(doc.getFileAbsolutePath());
//
//		BufferedImage bi= ImageIO.read(file);
//
//		return cropPic(doc, position, bi.getWidth(), bi.getHeight(), aspectRatioW, aspectRatioH);
//	}

	public Document cropPic(Document doc, Position position, int w, int h, int aspectRatioW, int aspectRatioH) {
		if (!doc.getContentType().startsWith("image")) {
			return doc;
		}

		Integer[] wh = cropSize(w, h, aspectRatioW, aspectRatioH);

		File file = new File(doc.getFileAbsouteThumbnailPath());

		String name = UUID.randomUUID().toString();
		String fileStoreName = name + "." + doc.getExt();

		String path = Global.SITE_FOLDER + File.separator + doc.getWebId();

		File ff = getFolder(path);
		File image = new File(ff, fileStoreName);
		try {
			Thumbnails.of(file)
					.size(wh[0], wh[1])
					.sourceRegion(position, wh[0], wh[1])
//						.scale(.5)
					.toFile(image);

			doc.setPath((path + File.separator + name).replace(File.separator, FOLDER_SEPARATOR));
			doc.setRemarks(doc.getFilePath());    //放大显示，裁剪图

		} catch (IOException e) {

		}


		return doc;
	}


	public Document cropPic(Document doc,int aspectRatioW, int aspectRatioH) throws IOException {
		if (!doc.getContentType().startsWith("image")) {
			return doc;
		}


		File file = new File(doc.getFileAbsouteThumbnailPath());

		String name = UUID.randomUUID().toString();
		String fileStoreName = name + "." + doc.getExt();

		String path = Global.SITE_FOLDER + File.separator + doc.getWebId();


		File ff = getFolder(path);
		File image = new File(ff, fileStoreName);

		BufferedImage srcImage = ImageIO.read(file);

		Integer[] wh = cropSize(srcImage.getWidth(), srcImage.getHeight(), aspectRatioW, aspectRatioH);

		Thumbnails.of(file)
				.size(wh[0], wh[1])
				.sourceRegion(Positions.CENTER, wh[0], wh[1])
				.toFile(image);
		doc.setRemarks(doc.getFilePath());    //放大显示 压缩原图
		doc.setPath((path + File.separator + name).replace(File.separator, FOLDER_SEPARATOR));


		return doc;
	}

	/*public Document cropPic(int id, int srcWidth, int x, int y, int ww, int aspectRatioW, int aspectRatioH) throws IOException {
		Document doc = documentService.findDocumentById(id);

		if (doc.getDocType().startsWith("image")) {
			File file = new File(getRealPath(doc));

			BufferedImage bi= ImageIO.read(file);
			Float w = new Float(bi.getWidth());

			Float factor = w / srcWidth;

			w = factor * ww;
			Float h = w * aspectRatioH /aspectRatioW;

			x = (int)(factor * x);
			y = (int)(factor * y);

			Position position = new Coordinate(x, y);
			doc = cropPic(doc, position, w.intValue(), h.intValue(),aspectRatioW, aspectRatioH);
		}

		return doc;
	}*/

//	public Document cropPic(int id, int srcWidth, int x, int y, int ww, int aspectRatioW, int aspectRatioH) throws IOException {
//		Document doc = documentService.findDocumentById(id);
//
//		if (doc.getDocType().startsWith("image")) {
//			File file = new File(getRealPath(doc));
//
//			BufferedImage bi= ImageIO.read(file);
//			Float w = new Float(bi.getWidth());
//
//			Float factor = w / srcWidth;
//
//			w = factor * ww;
//			Float h = w * aspectRatioH /aspectRatioW;
//
//			x = (int)(factor * x);
//			y = (int)(factor * y);
//
//			Position position = new Coordinate(x, y);
//			doc = cropPic(doc, position, w.intValue(), h.intValue(),aspectRatioW, aspectRatioH);
//		}
//
//		return doc;
//	}


	private static Integer[] cropSize(Integer w, Integer h, int aspectRatioW, int aspectRatioH) {
		if(aspectRatioW == 0 || aspectRatioH == 0)
			return new Integer[]{w, h};


		if (w * aspectRatioH != h * aspectRatioW) {

			int r;

			if (w * aspectRatioH > h * aspectRatioW) {
				r = h / aspectRatioH;
			} else {
				r = w / aspectRatioW;
			}

			w = aspectRatioW * r;
			h = aspectRatioH * r;
		}

		Integer[] arr = new Integer[2];
		arr[0] = w;
		arr[1] = h;
		return arr;

	}
}