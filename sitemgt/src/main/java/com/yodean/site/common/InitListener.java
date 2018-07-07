package com.yodean.site.common;

import com.rick.dev.config.Global;
import com.yodean.site.web.common.cache.PlatformCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Created by rick on 2017/9/5.
 */
@Component
public class InitListener implements ApplicationListener<ContextRefreshedEvent>, ServletContextAware {
    @Value("${upload}")
    private String upload;

    @Value("${fileServer}")
    private String fileServer;


    @Value("${domain}")
    private String domain;

    public void onApplicationEvent(ContextRefreshedEvent event) {

        if(event.getApplicationContext().getParent() == null) {
            PlatformCache.build();
            Global.upload = upload;
            Global.fileServer = fileServer;
            Global.domain = domain;
        }
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        servletContext.setAttribute("fileServer", fileServer);
        servletContext.setAttribute("domain", domain);
    }
}
