package com.lee.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lee.admin.service.SysResourcesService;
import com.lee.admin.service.SysResourcesUserService;
import com.lee.annotation.RequiresPermissions;
import com.lee.annotation.SysLog;
import com.lee.common.controller.BaseController;
import com.lee.common.pageutil.Page;

@Controller
@RequestMapping("admin/resources")
public class SysResourcesController extends BaseController {

	// @Resource(name="userSerivce")
	@Autowired
	private SysResourcesService resourcesService;

	@Autowired
	private SysResourcesUserService resourcesUserService;
	
	@RequiresPermissions("resources_list")
	@RequestMapping("list.do")
	public String list(Model model) {
		Map<String, Object> queryParams = getFormMap();
		queryParams.put("type", "1");
		Page<Map<String, Object>> page = resourcesService.getByConditionPage(queryParams);
		model.addAttribute("page", page);
		model.addAttribute("queryParams", queryParams);
		return "admin/resources/list";
	}

	@RequiresPermissions("resources_add")
	@RequestMapping("toAdd.do")
	public String toAdd(Model model,HttpServletRequest request){
		// 查询参数
		Map<String, Object> queryParams = getFormMap();
		model.addAttribute("queryParams", queryParams);
		
		// id
		String id = request.getParameter("id");
		if(!StringUtils.isEmpty(id)) {
			Map<String, Object> resources = resourcesService.getById(Integer.parseInt(id));
			if(resources!=null && resources.size()>0) {
				int parentId = Integer.parseInt(resources.get("parentId")+"");
				Map<String, Object> parentRes = resourcesService.getById(parentId);
				resources.put("parentName", parentRes.get("name"));
			}
			model.addAttribute("resources", resources);
		}
		
		return "admin/resources/add";
	}
	
	@RequiresPermissions("resources_add")
	@RequestMapping(value="add.do")
	@SysLog(module="菜单管理",methods="添加/修改菜单")
	public String add(Model model){
		Map<String, Object> m = getFormMap();
		Object id = m.get("id");
		if(id== null) {
			resourcesService.createReturnId(m);
		}else {
			resourcesService.update(m);
		}
		
		return "redirect:list.do";
	}
	
//	@RequestMapping("toEdit.do")
//	public String toEdit(Model model,HttpServletRequest request){
//		Map<String, Object> queryParams = getFormMap();
//		model.addAttribute("queryParams", queryParams);
//		return "resources/edit";
//	}
	
//	@RequestMapping(value="edit.do")
//	public String edit(Model model){
//		Map<String, Object> m = getFormMap();
//		resourcesService.update(m);
//		return "redirect:list.do";
//	}
	
	@RequiresPermissions("resources_delete")
	@ResponseBody
	@RequestMapping(value="delete.do")
	@SysLog(module="菜单管理",methods="删除菜单")
	public JSONObject delete(HttpServletRequest request){
		String id = request.getParameter("id");
		resourcesService.delete(Integer.parseInt(id));
		JSONObject jsonResult = getJsonResult("1000", "success", null);
		return jsonResult;
	}
	
	@RequiresPermissions("resources_add")
	@RequestMapping("tree.do")
	public String tree(Model model){
		// 所有可用菜单
		Map<String, Object> qm = new HashMap<String, Object>();
		qm.put("status", 1);
		List<Map<String, Object>> tree = resourcesService.tree(qm);
//		for (Map<String, Object> map : tree) {
//			map.put("open", true);
//		}
		
		// 添加根节点
		Map<String,Object> root = new HashMap<String, Object>();
		root.put("id", 0);
		root.put("name", "根节点");
		root.put("open", true);
		tree.add(root);
		
		// 转成json字符串
		String zNode = JSONArray.toJSONString(tree);
		model.addAttribute("zNode", zNode);
		return "admin/resources/tree";
	}
	
	
}
