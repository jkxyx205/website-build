package com.rick.dev.listener;

import com.rick.dev.config.Global;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletContextEvent;
import java.io.File;
import java.io.FileNotFoundException;

public class SysContextLoaderListener extends ContextLoaderListener {
    
	//private static final transient Logger logger = LoggerFactory.getLogger(SpringInit.class);
	public SysContextLoaderListener() {
        super();
    }
    
    public void contextInitialized(ServletContextEvent event) {
    	try {
    		Global.realContextPath = WebUtils.getRealPath(event.getServletContext(), File.separator);
    		Global.contextPath = event.getServletContext().getContextPath();
    		
    		WebUtils.getTempDir(event.getServletContext());

    		super.contextInitialized(event);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
     
    public void contextDestroyed(ServletContextEvent event) {
    	
    }
    
}