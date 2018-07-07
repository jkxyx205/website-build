package com.rick.dev.plugin.ztree.web;

import com.rick.dev.plugin.ztree.model.TreeNode;
import com.rick.dev.plugin.ztree.service.ZtreeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/ztree")
public class ZtreeController {
	
	@Resource
	private ZtreeService ztreeService;
	
	@RequestMapping("/init")
	@ResponseBody
	public List<TreeNode> initZtree(HttpServletRequest request) {
		return ztreeService.getTreeNode(request);
	}
	
	@RequestMapping("/init/{id}")
	@ResponseBody
	public List<TreeNode> initZtree(HttpServletRequest request,@PathVariable String id) {
		List<TreeNode> list =  ztreeService.getSubTreeNode(id,request);
		return list;
	}

}
