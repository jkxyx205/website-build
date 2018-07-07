package com.yodean.site.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by rick on 2017/12/28.
 */
public class SiteSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

    protected final Log logger = LogFactory.getLog(this.getClass());


    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (logger.isErrorEnabled()) {
//            Map<String, Object> model = new HashMap<String, Object>();
//            model.put("ex", ex);
            logger.error("error:", ex);
        }
        return super.doResolveException(request, response, handler, ex);
    }
}
