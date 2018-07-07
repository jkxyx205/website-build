package com.yodean.site.web.platform.controller;

import com.rick.dev.dto.ResponseDTO;
import com.rick.dev.plugin.fileupload.entity.Document;
import com.rick.dev.plugin.fileupload.service.DocumentService;
import com.rick.dev.plugin.fileupload.service.Upload2Disk;
import com.rick.dev.plugin.fileupload.service.UploadService;
import com.yodean.site.web.common.controller.WebController;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by rick on 2018/1/2.
 */
@Controller
@RequestMapping("/web/{webId}/gallery")
public class GalleryController extends WebController {
    @Resource
    private DocumentService documentService;

    @Resource
    private Upload2Disk upload2Disk;

    @Value("${gallery_init}")
    private String galleryInit;


    /***
     * @param webId
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @GetMapping
    public String gotoContentPage(@PathVariable Integer webId, String add, @RequestParam(defaultValue = "1") Integer pid, Model model) throws InvocationTargetException, IllegalAccessException {
        model.addAttribute("pid", pid);
        model.addAttribute("add", add);
        model.addAttribute("menu", "gallery");
        return "web/gallery";
    }

    @PostMapping("/save")
    public String save(Document param, MultipartFile img, Model model) throws IOException {

        Document doc = upload2Disk.store("tpl"+ File.separator+"init"+File.separator+"gallery", img);


        doc.setType(DocumentService.FILE_FILE);
        doc.setCategoryId(UploadService.CATEGORY_GALLERY);
        doc.setPid(param.getPid());
        doc.setName(param.getName());

        documentService.save(doc);
        return "web/gallery";
    }

    @GetMapping("/sync")
    @ResponseBody
    public ResponseDTO syncGallery() throws Exception {
        File dirnameFile = new File(galleryInit);

        //遍历文件夹
        File[] categories = dirnameFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && pathname.getName().matches("\\d+");
            }
        });

        for(File category : categories) {
            File[] images = category.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith("jpeg") || name.endsWith("JPEG") ||
                            name.endsWith("png") || name.endsWith("PNG")
                            ||name.endsWith("jpg") ||name.endsWith("jpg")
                            ||name.endsWith("gif") ||name.endsWith("GIF");
                }
            });

            for(File image : images) {
                progress(Integer.parseInt(category.getName()), image);
            }
        }

        return response("success");

    }


    private void progress(Integer parentId, final File image) throws Exception {
//        FileItem fileItem = new DiskFileItem(
//                "file","image/jpeg" , false, image.getName(), (int)image.length(), image.getParentFile()
//        );
//
//        fileItem.write(image);
//
//        CommonsMultipartFile multipartFile = new CommonsMultipartFile(fileItem);

        Document doc = upload2Disk.store("tpl"+ File.separator+"init"+File.separator+"gallery", new MultipartFile() {

            @Override
            public String getName() {
                return image.getName();
            }

            @Override
            public String getOriginalFilename() {
                return image.getName();
            }

            @Override
            public String getContentType() {
                return "image/jpeg" ;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return image.length();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return IOUtils.toByteArray(getInputStream());
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new FileInputStream(image);
            }

            @Override
            public void transferTo(File file) throws IOException, IllegalStateException {

            }
        });
        doc.setType(DocumentService.FILE_FILE);
        doc.setCategoryId(UploadService.CATEGORY_GALLERY);
        doc.setPid(parentId);


        documentService.save(doc);
        FileUtils.forceDelete(image);

    }
}
