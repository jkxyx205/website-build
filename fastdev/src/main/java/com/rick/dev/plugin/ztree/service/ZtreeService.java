package com.rick.dev.plugin.ztree.service;

import com.rick.dev.plugin.ztree.model.TreeNode;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ZtreeService {
	/**
	 * 获取子节点
	 * @param id
	 * @return
	 */
	public List<TreeNode> getSubTreeNode(String id,HttpServletRequest request);
	
	/**
	 * 获取所有节点
	 * @return
	 */
	public List<TreeNode> getTreeNode(HttpServletRequest request);
	
}
