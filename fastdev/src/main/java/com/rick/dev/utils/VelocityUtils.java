package com.rick.dev.utils;

import org.apache.commons.collections4.MapUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by rick on 2017/7/11.
 */
public class VelocityUtils {
    public static String tplFromString(String content, Map<String, Object> params) {
        // 初始化并取得Velocity引擎
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty("runtime.log.invalid.references", false);
        ve.init();
        // 取得velocity的模版内容, 模板内容来自字符传

        // 取得velocity的上下文context
        VelocityContext context = new VelocityContext();
        // 把数据填入上下文
        if (MapUtils.isNotEmpty(params)) {
            Set<Map.Entry<String, Object>> enSet = params.entrySet();
            for (Map.Entry<String, Object> en : enSet) {
                context.put(en.getKey(), en.getValue());
            }
        }

        // 输出流
        StringWriter writer = new StringWriter();
        // 转换输出
        ve.evaluate(context, writer, "", content); // 关键方法
        return writer.toString();

    }

    public static String tplFromTpl(String tpl, Map<String, Object> params) {
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        props.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        props.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        props.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");

        VelocityEngine ve = new VelocityEngine(props);
        ve.setProperty("runtime.log.invalid.references", false);
        VelocityContext context = new VelocityContext();

        ve.init();

        // 把数据填入上下文
        if (MapUtils.isNotEmpty(params)) {
            Set<Map.Entry<String, Object>> enSet = params.entrySet();
            for (Map.Entry<String, Object> en : enSet) {
                context.put(en.getKey(), en.getValue());
            }
        }

        Template template = ve.getTemplate(tpl);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }
}
