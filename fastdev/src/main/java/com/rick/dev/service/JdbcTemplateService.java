package com.rick.dev.service;


import com.rick.dev.utils.sql.SqlFormatter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rick.Xu on 2016/03/24.
 */
@Service
public class JdbcTemplateService {

    private static Logger logger = LoggerFactory.getLogger(QueryService.class);

    public static final String PARAM_IN_SEPERATOR = ";";

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private JdbcTemplate jdbcTemplate;

    public static Map<String,Object> getParametersAsMap(boolean skipBlank,HttpServletRequest request) {
        Enumeration<String> en = request.getParameterNames();
        Map<String, Object> map = new HashMap<String, Object>();
        while(en.hasMoreElements()) {
            String name = en.nextElement();
            String[] values = request.getParameterValues(name);

            //多选 会在name后面加[]
            name = name.replace("[]", "");

            if(values != null) {
                if(values.length>1) {
                    StringBuilder sb = new StringBuilder();
                    for(String v:values) {
                        if(skipBlank && StringUtils.isBlank(v))
                            continue;
                        sb.append(v).append(";");
                    }
                    sb.deleteCharAt(sb.length()-1);

                    map.put(name, sb.toString());
                } else  {
                    String value = values[0];
                    if(skipBlank && StringUtils.isBlank(value))
                        continue;
                    map.put(name,value);
                }
            }
        }
        return map;
    }

    private String getQueryStringByQueryName(String queryName, Map<String, Object> param) {
        MappedStatement mappedStatement = sqlSessionFactory.getConfiguration().getMappedStatement(queryName);
        String sql = mappedStatement.getBoundSql(param).getSql();
        return sql;
    }

    public <T> T queryForSpecificParamSQL(String sql,
                                           Map<String, Object> param,
                                           String paramInSeperator,
                                           JdbcTemplateExecutor<T> jdbcTemplateExecutor) {
        Map<String, Object> formatMap = new HashMap<String, Object>();
        String formatSql = SqlFormatter.formatSql(sql, param, formatMap,
                paramInSeperator);

        Object[] args = NamedParameterUtils.buildValueArray(formatSql,
                formatMap);
        formatSql = formatSql.replaceAll(SqlFormatter.PARAM_REGEX,"?"); //mysql
        return jdbcTemplateExecutor.query(jdbcTemplate, formatSql, args);
    }

    public <T> T queryForSpecificParam(String queryName,
                                       Map<String, Object> param,
                                       JdbcTemplateExecutor<T> jdbcTemplateExecutor) {
        return queryForSpecificParam(queryName, param, PARAM_IN_SEPERATOR,
                jdbcTemplateExecutor);
    }

    public <T> T queryForSpecificParam(String queryName,
                                       Map<String, Object> param, String paramInSeperator,
                                       JdbcTemplateExecutor<T> jdbcTemplateExecutor) {
        String sql = getQueryStringByQueryName(queryName, param);
        return  queryForSpecificParamSQL(sql, param, paramInSeperator,jdbcTemplateExecutor);
    }

    public long queryForSpecificParamCount(String queryName,
                                           Map<String, Object> param) {
        String sql = getQueryStringByQueryName(queryName, param);
        return  queryForSpecificParamSQL(sql, param, PARAM_IN_SEPERATOR,new JdbcTemplateExecutor<Long>() {

            public Long query(JdbcTemplate jdbcTemplate, String queryString, Object[] args) {
                return jdbcTemplate.queryForObject(queryString, args, Long.class);
            }
        });
        //
    }

    public interface JdbcTemplateExecutor<T> {
        public T query(JdbcTemplate jdbcTemplate, String queryString,
                       Object[] args);
    }
    
    //好用的工具类
    public List<Map<String, Object>> queryForSpecificParam(String queryName, Map<String, Object> param) {
    	String sql = getQueryStringByQueryName(queryName, param);
    	return queryForSpecificParamSQL(sql, param);
    }


    public List<Map<String, Object>> queryForSpecificParamSQL(String sql, Map<String, Object> param) {
    	return queryForSpecificParamSQL(sql, param, PARAM_IN_SEPERATOR, new JdbcTemplateExecutor<List<Map<String, Object>>>() {
			public List<Map<String, Object>> query(JdbcTemplate jdbcTemplate, String queryString, Object[] args) {
				return  jdbcTemplate.queryForList(queryString, args);
			}});
    }
    
    public void updateForSpecificParamSQL(String sql, Map<String, Object> param) {
    	queryForSpecificParamSQL(sql, param, PARAM_IN_SEPERATOR, new JdbcTemplateExecutor<Void>() {
			public Void query(JdbcTemplate jdbcTemplate, String queryString, Object[] args) {
				jdbcTemplate.update(queryString, args);
				return null;
			}});
    }

    public <T> List<T> queryForSpecificParam(String queryName, Map<String, Object> param, Class<T> clazz) {
        String sql = getQueryStringByQueryName(queryName, param);
        return queryForSpecificParamSQL(sql, param, clazz);
    }


    public <T> List<T> queryForSpecificParamSQL(String sql, Map<String, Object> param, final Class<T> clazz) {
        //TODO
        return queryForSpecificParamSQL(sql, param, PARAM_IN_SEPERATOR, new JdbcTemplateExecutor<List<T>>() {
            public List<T> query(JdbcTemplate jdbcTemplate, String queryString, Object[] args) {
                if (clazz == String.class || Number.class.isAssignableFrom(clazz) || Character.class ==clazz || Boolean.class == clazz) {
                    return jdbcTemplate.queryForList(queryString, args, clazz);
                }

                return  jdbcTemplate.query(queryString, args,new BeanPropertyRowMapper<T>(clazz));
            }});
    }


    /***
     * 查找一个数据
     * @param sql
     * @param param
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T queryForSpecificParamSQLSingle(String sql,
                                           Map<String, Object> param, final Class<T> clazz) {
        return  queryForSpecificParamSQL(sql, param, PARAM_IN_SEPERATOR,new JdbcTemplateExecutor<T>() {

            public T query(JdbcTemplate jdbcTemplate, String queryString, Object[] args) {
                return jdbcTemplate.queryForObject(queryString, args, clazz);
            }
        });
        //
    }

    
}
