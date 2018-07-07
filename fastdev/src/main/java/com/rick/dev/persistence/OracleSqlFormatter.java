package com.rick.dev.persistence;

import com.rick.dev.vo.PageModel;
import org.apache.commons.lang.StringUtils;




/**
 * @author Rick.Xu
 *
 */
public class OracleSqlFormatter extends AbstractSqlFormatter {
	public String pageSql(String sql, PageModel model) {
		StringBuilder sb = new  StringBuilder();
		
		int startIndex = 0;
		int endIndex = Integer.MAX_VALUE;
		
		if(model != null) {
			startIndex = (model.getPage()-1)*model.getRows();
			endIndex = startIndex + model.getRows();
		}
		//
		sb.append("SELECT * FROM ( SELECT A.*, ROWNUM RN FROM (SELECT * FROM ")
		  .append("(").append(sql).append(") temp_");
		
		if (model != null
				&& (StringUtils.isNotBlank(model.getSidx()) && StringUtils
						.isNotBlank(model.getSord()))) {
			sb.append(" ORDER BY ").append(model.getSidx()).append(" ")
					.append(model.getSord());
		}
		
		sb.append(") A WHERE ROWNUM <=").append(endIndex).append(") WHERE RN > ").append(startIndex);
		return sb.toString();
	}

	@Override
	public String conactString(String name) {
		 return "'%'||UPPER(:" + name + ")||'%'";
	}
}
