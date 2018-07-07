package com.rick.dev.persistence;

import com.rick.dev.vo.PageModel;
import org.apache.commons.lang.StringUtils;

/**
 * @author Rick.Xu
 *
 */
public class MysqlSqlFormatter extends AbstractSqlFormatter{
	public String pageSql(String sql, PageModel model) {
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

	@Override
	public String conactString(String name) {
		return " CONCAT('%',UPPER(:" + name + "),'%')";
	}
}


