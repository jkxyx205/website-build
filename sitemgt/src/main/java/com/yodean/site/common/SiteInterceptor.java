package com.yodean.site.common;

import com.rick.dev.utils.JacksonUtils;
import com.rick.dev.utils.SpringContextHolder;
import com.yodean.site.web.content.entity.Site;
import com.yodean.site.web.content.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by rick on 2018/1/2.
 */
public class SiteInterceptor implements HandlerInterceptor {
    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final SiteService siteService = SpringContextHolder.getBean("siteService");


    private static ThreadLocal<Long> threadLocal = new ThreadLocal();

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        threadLocal.set(new Date().getTime());
        Map pathVariables = (Map) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (null != pathVariables) {
            String webId = (String)pathVariables.get("webId");
            if (webId != null) {
                Integer webIdNum = Integer.parseInt(webId);
                Site site = siteService.findById(webIdNum);
                httpServletRequest.setAttribute("webId", webIdNum);
                httpServletRequest.setAttribute("site", site);
                httpServletRequest.setAttribute("siteJSON", JacksonUtils.toJSon(site));
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

        logger.info("visit page [" + httpServletRequest.getRequestURI() + "] costs " + ""+ (new Date().getTime() - threadLocal.get())+"ms" );
    }
}
