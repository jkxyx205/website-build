package com.rick.dev.plugin.ztree.service;

import com.rick.dev.plugin.ztree.model.TreeNode;
import com.rick.dev.service.JdbcTemplateService;
import com.rick.dev.service.JdbcTemplateService.JdbcTemplateExecutor;
import com.rick.dev.utils.ServletContextUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public class ZtreeServiceImpl implements ZtreeService {
	
	private static final String QUERY_PARAM_QUERYNAME = "queryName";
	
	@Resource
	private JdbcTemplateService jdbcTemplateService;

	public List<TreeNode> getSubTreeNode(final String id,HttpServletRequest request) {
		Map<String,Object> param = ServletContextUtil.getMap(true, request);
		
		String queryName = (String) param.get(QUERY_PARAM_QUERYNAME);
		if(StringUtils.isBlank(queryName)) {
			throw new RuntimeException("queryName can not be blank" );
		}
		
		return jdbcTemplateService.queryForSpecificParam(queryName, param, new JdbcTemplateExecutor<List<TreeNode>>() {

			public List<TreeNode> query(JdbcTemplate jdbcTemplate,
					String queryString, Object[] args) {
				String treeSql = treeSql(queryString,id);
				int len = args.length;
				String[] newArgs = new String[len + 1];
				System.arraycopy(newArgs, 0, args, 0, len);
				newArgs[len] = id;
				return jdbcTemplate.query(treeSql, newArgs, new BeanPropertyRowMapper<TreeNode>(TreeNode.class));
			}
		});
	}

	public List<TreeNode> getTreeNode(HttpServletRequest request) {
		Map<String,Object> param = ServletContextUtil.getMap(true, request);
		
		String queryName = (String) param.get(QUERY_PARAM_QUERYNAME);
		if(StringUtils.isBlank(queryName)) {
			throw new RuntimeException("queryName can not be blank" );
		}
		
		return jdbcTemplateService.queryForSpecificParam(queryName, param, new JdbcTemplateExecutor<List<TreeNode>>() {

			public List<TreeNode> query(JdbcTemplate jdbcTemplate,
					String queryString, Object[] args) {
				return jdbcTemplate.query(queryString, args, new BeanPropertyRowMapper<TreeNode>(TreeNode.class));
			}
		});
	}
	
	private String treeSql(String sql,String id) {
		//case when count(1)=0 then 'false' else 'true' end as parent 注是mysql的写法
		String rsql = "select _temp_tree.id,_temp_tree.name, " +
				" case when max(p.id) is null then 'false' else 'true' end as parent" + 
				" from ("+sql+")  _temp_tree  LEFT JOIN ("+sql+") p on _temp_tree.id = p.pId where _temp_tree.pid = ? GROUP BY _temp_tree.id,_temp_tree.name";
		return rsql;
	}

}
