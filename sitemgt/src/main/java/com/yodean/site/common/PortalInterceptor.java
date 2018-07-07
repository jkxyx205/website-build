package com.yodean.site.common;

import com.rick.dev.config.Global;
import com.rick.dev.utils.SpringContextHolder;
import com.yodean.site.web.content.entity.Site;
import com.yodean.site.web.content.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

/**
 * Created by rick on 2018/1/2.
 */
public class PortalInterceptor implements HandlerInterceptor {
    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String LOCALHOST = "localhost";

    protected final SiteService siteService = SpringContextHolder.getBean("siteService");


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
                URL url = new URL(httpServletRequest.getRequestURL().toString());
        boolean flag =  true;
        String siteName = null;
        Site site = null;
        String host = url.getHost();
        if (!LOCALHOST.equals(host)) {

            if (host.indexOf(Global.domain) > -1 || (host + ":8000").indexOf(Global.domain) > -1) {//二级域名访问 //TODO
                siteName = host.substring(0, host.indexOf("."));
            } else { //个性域名访问
                site = siteService.findByDomain(host);
                if (null == site) {
                    flag = false;
                }  else {
                    siteName = site.getName();
                }


            }

            if(site == null && flag) {
                site = siteService.findByName(siteName);
                if(site == null) flag = false;
            }

            if (!flag) {
                httpServletResponse.sendRedirect("/error/404");
                return false;
            }

            httpServletRequest.setAttribute("siteName", siteName);
            httpServletRequest.setAttribute("webId", site.getId());
        }

        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
