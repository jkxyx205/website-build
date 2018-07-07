package com.rick.dev.config;

import org.apache.commons.lang3.time.FastDateFormat;

import java.io.File;

/**
 * 全局配置类
 * @author ThinkGem
 * @version 2014-06-25
 */
public class Global {
	public static final String SITE_FOLDER = "site";

	public static final int PAGE_ROWS = 20;
 
	public static final String ENCODING = "utf-8";
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final FastDateFormat SDF_DATE = FastDateFormat.getInstance(DATE_FORMAT);
	
	public static final FastDateFormat SDF_TIME = FastDateFormat.getInstance(DATETIME_FORMAT);
	
	public static String realContextPath;
	
	public static String contextPath;
	
	public static File tempDir = new File("");

	public static String upload;

	public static String fileServer;

	public static String domain;
}
