package com.rick.dev.utils.sql;


import com.rick.dev.vo.PageModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Rick.Xu
 *
 */
public class SqlFormatter {
	
	private static final transient Logger logger = LoggerFactory.getLogger(SqlFormatter.class);
	
	private static final int QUERY_IN_MAX_COUNT = 1000;
		
	private static final String COLUNM_REGEX = "((?i)(to_char|NVL)?\\s*([(][^([(]|[)])]*[)])|[a-zA-Z0-9'[\\.]_[-]]+)";
	
	private static final String OPER_REGEX = "(?i)(like|!=|>=|<=|<|>|=|\\s+in|\\s+not\\s+in)";
	
	private static final String HOLDER_REGEX = "(([(]\\s*:\\w+\\s*[)])|(:\\w+))";
	
	public static final String PARAM_REGEX = ":[a-zA-Z]+\\w*";//":\\w+";
	
	private static final String OPER_IN_REGEX = "(?i)(\\s+in|\\s+not\\s+in)";
	
	private static final String FULL_REGIX = new StringBuilder().append(COLUNM_REGEX).append("\\s*").append(OPER_REGEX).append("\\s*").append(HOLDER_REGEX).toString();
	
	private static final String IN_FULL_REGIX = new StringBuilder().append(COLUNM_REGEX).append("\\s*").append(OPER_IN_REGEX).append("\\s*")
		    .append("[(][^)]+[)]").toString();
	
	private static final Map<String,String> DATE_FORMAT_MAP;
	
	static {
		DATE_FORMAT_MAP = new HashMap<String,String>(2);
		DATE_FORMAT_MAP.put("\\d{4}/\\d{2}/\\d{2}", "yyyy/MM/dd");
		DATE_FORMAT_MAP.put("\\d{4}-\\d{2}-\\d{2}", "yyyy-MM-dd");
	}
	
	public static String formatSql(String srcSql,Map<String,Object> param,Map<String, Object> formatMap,String paramInSeperator) {
		ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(srcSql);
		List<String> names = parsedSql.getParameterNames();
		
		if(formatMap == null || param == null) {
			srcSql =  srcSql.replaceAll(FULL_REGIX, "1 = 1");
			formatMap = Collections.emptyMap();
			param = Collections.emptyMap();
		} else {
			 
			List<ParamHolder> paramList = splitParam(srcSql);
			
			for(ParamHolder h : paramList) {
				String name = h.holder;
				names.remove(name);
				Object obj = param.get(name);
				
				obj = (obj == null ? "":obj);
				Object value = null;
				
				if(obj.getClass() == String[].class) {
					String[] values = (String[])obj;
					if(values.length > 0) {
						StringBuilder sb = new StringBuilder();
						for(String s : values) {
							sb.append(s).append(";");
						}
						value = sb.toString();
					}
				} else {
					value = obj; //String.valueOf(obj);
				}

				if((value.getClass() == String.class && StringUtils.isBlank((String) value))/*StringUtils.isBlank(value)*/) {
					//\\s+ => \\s* 20170301 by Rick.Xu
					srcSql = srcSql.replaceAll("\\s*" + h.full.replace(".", "\\.").replace("(", "\\(").replace(")", "\\)"), " 1 = 1");
					continue;
				}
				
				//if has the value
				//String format;
				if(h.oper.toUpperCase().endsWith("IN")) {
					String[] invalues = null;
					String strValue = String.valueOf(value);
					if ((invalues = strValue.split(paramInSeperator)).length > 0) {
						StringBuilder sb = new StringBuilder("in (");
						for(int i = 0; i< invalues.length; i++) {
							String newProName = name + i;
							sb.append(":").append(newProName).append(",");
							formatMap.put(newProName, invalues[i]);
						}
						sb.deleteCharAt(sb.length()-1);
						sb.append(")");
						sb.toString();
						srcSql = srcSql.replaceAll("((?i)in)\\s*[(]\\s*:" + h.holder + "\\s*[)]", sb.toString());
					}
					
				} else if("like".equalsIgnoreCase(h.oper)) {
					 //Oracle 写法
		        	 //srcSql = srcSql.replace(h.full, new StringBuilder("UPPER(").append(h.key).append(") ").append(h.oper).append(" '%'||UPPER(:").append(name).append(")||'%'"));
					 //mysql
					 srcSql = srcSql.replace(h.full, new StringBuilder("UPPER(").append(h.key).append(") ").append(h.oper).append(" CONCAT('%',UPPER(:").append(name).append("),'%')"));
		        	 formatMap.put(name, value);
				}  else {
					formatMap.put(name, value);
				}
			}
		}
		
		for (String p : names) {//其他解决不了的 用''
			p = ":"+p;
			String pp = p.substring(1);
			Object v = formatMap.get(pp) ;
			if (v == null || StringUtils.isBlank((String)v)) {
				v = param.get(pp);
				if (v == null || (v instanceof String && StringUtils.isBlank((String)v))) {
                     StringBuilder sb  = new StringBuilder(srcSql);
                     int len = p.length();
                     int index = 0;
                     int strLen = sb.length();
                     while ((index = sb.indexOf(p,index)) > -1) {
                         int nextLen = index + len;
                         char nextChar = ' ';
                         if (strLen > nextLen) {
                             nextChar = sb.charAt(nextLen);
                         }

                         if (nextChar < 'A' || nextChar > 'z') {
                             sb.delete(index, index + len);
                             sb.insert(index, "''");
                             index = index + 2;
                         } else {
                             index = index + len;
                         }
                     }
                    srcSql = sb.toString();
				} else {
					formatMap.put(pp, v);
				}
			}
		}

        //mysql变量变成?
		return changeInSQL(srcSql);
	}

	public static String formatSqlCount(String srcSql) {
//		srcSql = srcSql.replaceAll("(?i)(order.*(asc|desc))","");

		StringBuilder sb = new  StringBuilder();
		sb.append("SELECT COUNT(*) FROM (").append(removeOrders(srcSql)).append(") temp");
		return sb.toString();
	}
	
	private static List<ParamHolder> splitParam(String sql) {
		Pattern pat = Pattern.compile(FULL_REGIX);  
		Matcher mat = pat.matcher(sql);  
		List<ParamHolder> paramList = new ArrayList<ParamHolder>();
		
		while (mat.find()) {
			 ParamHolder holder = new ParamHolder();
			 String matchRet = mat.group().trim();
			 holder.full = matchRet;
			 //再进行拆分
			 Pattern pat1 = Pattern.compile("^" + COLUNM_REGEX);  
			 Matcher mat1 = pat1.matcher(matchRet);
			 while(mat1.find()) {
				 String matchRet1 = mat1.group().trim();
				 holder.key = matchRet1;
			 }
			 
			 //再进行拆分
			 Pattern pat2 = Pattern.compile(OPER_REGEX);  
			 Matcher mat2 = pat2.matcher(matchRet);
			 while(mat2.find()) {
				 String matchRet2 = mat2.group().trim();
				 holder.oper = matchRet2;
			 }
			 
			 
			//再进行拆分
			 Pattern pat3 = Pattern.compile(PARAM_REGEX);  
			 Matcher mat3 = pat3.matcher(matchRet);
			 while(mat3.find()) {
				 String matchRet3 = mat3.group().trim();
				 holder.holder = matchRet3.substring(1);
			 }
            paramList.add(holder);
        }
		//logger.debug(paramList.toString());
		return paramList;
	}

   private static class ParamHolder {
		private String full;
		
		private String key;
		
		private String oper;
		
		private String holder;
		
		@Override
		public String toString() {
			return new StringBuilder().append(full).append("/").append(key).append("/").append(oper).append("/").append(holder).toString();
		}
	}

    public static String pageSql(String sql, PageModel model) {
        StringBuilder sb = new  StringBuilder();
        sb.append("SELECT * FROM ")
                .append("(").append(sql).append(") temp_");

        if (model != null && (StringUtils.isNotBlank(model.getSidx()) && StringUtils
                .isNotBlank(model.getSord()))) {
            sb.append(" ORDER BY ").append(model.getSidx()).append(" ")
                    .append(model.getSord());
        }

        if (model != null)
            sb.append(" limit ").append((model.getPage()-1) *  model.getRows()).append(",").append(model.getRows());

        return sb.toString();
    }
   
   public static String changeInSQL(String sql) {
		Pattern pat = Pattern.compile(IN_FULL_REGIX);  
		Matcher mat = pat.matcher(sql);  

		while (mat.find()) {
			 ParamHolder holder = new ParamHolder();
			 String matchRet = mat.group().trim();
			 holder.full = matchRet;
			 //再进行拆分
			 Pattern pat1 = Pattern.compile("^" + COLUNM_REGEX);  
			 Matcher mat1 = pat1.matcher(matchRet);
			 while(mat1.find()) {
				 String matchRet1 = mat1.group().trim();
				 holder.key = matchRet1;
			 }
			 
			 //再进行拆分
			 Pattern pat2 = Pattern.compile(OPER_REGEX);  
			 Matcher mat2 = pat2.matcher(matchRet);
			 while(mat2.find()) {
				 String matchRet2 = mat2.group().trim();
				 holder.oper = matchRet2;
			 }
			 
			//再进行拆分
			 Pattern pat3 = Pattern.compile("[(].*[)]");  
			 Matcher mat3 = pat3.matcher(matchRet);
			 while(mat3.find()) {
				 String matchRet3 = mat3.group().trim();
				 holder.holder = matchRet3;
			 }
			 
			//
			StringBuilder newInSQL = new StringBuilder();
			newInSQL.append("(");
			
			String[] params = holder.holder.replaceAll("[(]|[)]", "").split(",");
			
			if (params.length <= QUERY_IN_MAX_COUNT) {
				continue;
			} else {
				int cn = params.length % QUERY_IN_MAX_COUNT == 0 ? params.length/QUERY_IN_MAX_COUNT: params.length/QUERY_IN_MAX_COUNT +1 ;
				for (int i = 0; i < cn; i++) {
					newInSQL.append(holder.key).append(" ").append(holder.oper).append("(");
					//
					for (int j = 1; j <= QUERY_IN_MAX_COUNT ;j++) {
						if (i*QUERY_IN_MAX_COUNT+j <= params.length)
							newInSQL.append(params[i*QUERY_IN_MAX_COUNT+j-1]).append(",");
					}
					newInSQL.deleteCharAt(newInSQL.length()-1);
						
					newInSQL.append(") OR ");
				}
				newInSQL.delete(newInSQL.length() - 4, newInSQL.length());
				
				newInSQL.append(")");
				
				sql = sql.replace(holder.full, newInSQL.toString());
			}
		}
		return sql;
	}
   
   /** 
    * 去除hql的orderBy子句。 
    * @param hql 
    * @return 
    */  
   @SuppressWarnings("unused")
   private static String removeOrders(String qlString) {  
       Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);  
       Matcher m = p.matcher(qlString);  
       StringBuffer sb = new StringBuffer();  
       while (m.find()) {  
           m.appendReplacement(sb, "");  
       }
       m.appendTail(sb);
       return sb.toString();  
   }
 
}