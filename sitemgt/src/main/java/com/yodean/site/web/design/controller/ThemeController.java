package com.yodean.site.web.design.controller;

import com.rick.dev.utils.ServletContextUtil;
import com.yodean.site.web.tpl.entity.Theme;
import com.yodean.site.web.tpl.service.ThemeService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by rick on 2017/9/6.
 */
@RestController
@RequestMapping("/theme")
public class ThemeController {
    @Resource
    private ThemeService themeService;

    @RequestMapping("/image/{id}")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws IOException {
        Theme theme = themeService.getThemeById(id);

        OutputStream os = ServletContextUtil.getOsFromResponse(response, request, "");
        IOUtils.write(FileUtils.readFileToByteArray(new File(theme.getThumbnail())), os);
        os.close();
    }

    @RequestMapping("/header/{id}")
    public void header(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws IOException {
        Theme theme = themeService.getThemeById(id);

        OutputStream os = ServletContextUtil.getOsFromResponse(response, request, "");
        IOUtils.write(FileUtils.readFileToByteArray(new File(theme.getHeaderThumbnail())), os);
        os.close();
    }


    @RequestMapping("/footer/{id}")
    public void footer(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws IOException {
        Theme theme = themeService.getThemeById(id);

        OutputStream os = ServletContextUtil.getOsFromResponse(response, request, "");
        IOUtils.write(FileUtils.readFileToByteArray(new File(theme.getFooterThumbnail())), os);
        os.close();
    }
}
