package com.rick.dev.utils;

import java.math.BigDecimal;

public class FileUtils {
	public static String getFileExtend(String originalFilename) {
		int num =  originalFilename.lastIndexOf(".");
		
		if (num == -1)
			return "";
		
		return originalFilename.substring(num);
	}
	
	public static String getFormatSize(long size) {  
        double kiloByte = size/1024d;  
        if(kiloByte < 1) {  
            return size + "B";  
        }  
          
        double megaByte = kiloByte/1024;  
        if(megaByte < 1) {  
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));  
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";  
        }  
          
        double gigaByte = megaByte/1024;  
        if(gigaByte < 1) {  
            BigDecimal result2  = new BigDecimal(Double.toString(megaByte));  
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";  
        }  
          
        double teraBytes = gigaByte/1024;  
        if(teraBytes < 1) {  
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));  
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";  
        }  
        BigDecimal result4 = new BigDecimal(teraBytes);  
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";  
    }  
}
