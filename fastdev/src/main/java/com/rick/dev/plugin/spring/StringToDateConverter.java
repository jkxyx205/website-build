package com.rick.dev.plugin.spring;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rick on 2017/7/21.
 */
public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        if(StringUtils.isBlank(source)) {
            return null;
        }

        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(source.trim());
        } catch (Exception e) {
            try {
                format = new SimpleDateFormat("yyyyMMddHHmmss");
                date = format.parse(source.trim());
            } catch (Exception e1) {
                try {
                    format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = format.parse(source.trim());
                } catch (Exception e2) {
                }
            }
        }

        return date;
    }
}
