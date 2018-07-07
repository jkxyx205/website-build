package com.rick.dev.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rick.dev.service.extend.ColumnMapExtRowMapper;
import com.rick.dev.utils.sql.SqlFormatter;
import com.rick.dev.vo.JqGrid;
import com.rick.dev.vo.PageModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Rick.Xu on 2016/03/22.
 */
@Service
public class JqgridService {
    private static final String JQGIRD_PARAM_SORD = "sord";
    private static final String JQGIRD_PARAM_PAGE = "page";
    private static final String JQGIRD_PARAM_ROW = "rows";
    private static final String JQGIRD_PARAM_QUERYNAME = "queryName";
    private static final String JQGIRD_PARAM_SIDX = "sidx";
    private static final String DICT_TYPE = "dict";

    @Resource
    private JdbcTemplateService  jdbcTemplateService;

    public JqGrid getJqgirdData(HttpServletRequest request) throws Exception {
        Map<String,Object> param = JdbcTemplateService.getParametersAsMap(true, request);
        PageModel model = getPageModel(param);
        return getJqgirdData(model,param, Map.class);
    }

    public JqGrid getJqgirdData(String queryName, HttpServletRequest request) throws Exception {
        Map<String,Object> param = JdbcTemplateService.getParametersAsMap(true, request);
        PageModel model = getPageModel(param);
        model.setQueryName(queryName);
        return getJqgirdData(model,param, Map.class);
    }
    public  JqGrid getJqgirdData(final PageModel model,Map<String,Object> param) throws Exception {
        return getJqgirdData(model, param, Map.class);
    }
    public <T> JqGrid getJqgirdData(final PageModel model, Map<String,Object> param, final Class<T> clazz) throws Exception {
        long count = 0;

        if (model.getRows() != -1) {
            count = jdbcTemplateService.queryForSpecificParam(model.getQueryName(), param, new JdbcTemplateService.JdbcTemplateExecutor<Long>() {

                public Long query(JdbcTemplate jdbcTemplate, String queryString, Object[] args) {
                    queryString = SqlFormatter.formatSqlCount(queryString);
                    return jdbcTemplate.queryForObject(queryString, args, Long.class);
                }
            });

//            if (model.getRows() >= count){
//                model.setPage(1);
//            }
        }

        List<?> rows = jdbcTemplateService.queryForSpecificParam(model.getQueryName(), param, new JdbcTemplateService.JdbcTemplateExecutor<List<?>>() {

            public List<?> query(JdbcTemplate jdbcTemplate,
                                                   String queryString, Object[] args) {
            	//change add 20170223
                
                if(model.getRows() != -1) { //分页排序
                	queryString = pageSql(queryString,model);
                } else { //仅仅排序
                	queryString = wrapSordString(queryString, model.getSidx(), model.getSord());
                }
                //end
                
                //return jdbcTemplate.queryForList(sql, args);
                if (clazz == Map.class)
                    return jdbcTemplate.query(queryString, args, new ColumnMapExtRowMapper());
                else
                    return jdbcTemplate.query(queryString, args, new BeanPropertyRowMapper<T>(clazz));

//                    List<Map<String, Object>>  ret = jdbcTemplate.query(queryString, args, new ColumnMapExtRowMapper());



                //translate(ret, model.getDicMap());
//                return ret;

            }
        });
        if(model.getRows() == -1) {
            count = rows.size();
        }

        JqGrid bo = new JqGrid();

        long total;

        if(model.getRows() != -1) {
            if(count%model.getRows() == 0) {
                total = count/model.getRows();
            } else {
                total = count/model.getRows() + 1;
            }
            bo.setTotal(total);
            bo.setPage(model.getPage());
        }

        bo.setRows(rows);
        bo.setRecords(count);
        bo.setPageRows(model.getRows());

        return bo;
    }

   /* private void translate(List<Map<String, Object>>  ret, Map<String, String> dicMap) {
        if (dicMap == null) return;

        for(Map<String, Object> m: ret) {
            Set<String> names = m.keySet();
            for(String name : names) {
                if (dicMap.containsKey(name)) {
                    Object value = translate(dicMap.get(name), m.get(name));
                    m.put(name, value);
                }
            }
        }
    }*/

    public static String wrapSordString(String sql,String sidx, String sord) {
        StringBuilder sb = new StringBuilder("SELECT * FROM (");
        sb.append(sql).append(") temp");
        if(StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(sord)) {
            sb.append(" ORDER BY ").append(sidx).append(" ").append(sord);
            return sb.toString();
        } else {
            return sql;
        }
    }
   /* private Object translate(String dict,Object value) {
        if(value!= null && (value.getClass() == String.class)) {
            String v = (String)value;
            if(StringUtils.isNotBlank(v)) {
                String[] vs = v.split(",");
                if(vs.length == 1) {
                    return DictUtils.getDictLabel((String)value,dict,"");
                } else {
                    return DictUtils.getDictLabels((String)value,dict,"");
                }
            }
        }
        return value;
    }*/

    private PageModel getPageModel(Map<String,Object> param) throws IOException {
        PageModel model = new PageModel();
        model.setQueryName((String) param.get(JQGIRD_PARAM_QUERYNAME));
        if (param.get(JQGIRD_PARAM_PAGE) == null) param.put(JQGIRD_PARAM_PAGE,"1");
        if (param.get(JQGIRD_PARAM_ROW) == null) param.put(JQGIRD_PARAM_ROW,"-1");
        model.setPage(Integer.parseInt(param.get(JQGIRD_PARAM_PAGE).toString()));
        model.setRows(Integer.parseInt(param.get(JQGIRD_PARAM_ROW).toString()));

        model.setSord((String) param.get(JQGIRD_PARAM_SORD));
        model.setSidx((String) param.get(JQGIRD_PARAM_SIDX));

        ObjectMapper mapper = new ObjectMapper();

        if (param.get(DICT_TYPE) != null)
            model.setDicMap(mapper.readValue((String)param.get(DICT_TYPE), Map.class));

        return model;
    }

    private String pageSql(String sql, PageModel model) {
        if (model == null) {
            model = new PageModel();
        }
        return SqlFormatter.pageSql(sql,model);
    }
}
