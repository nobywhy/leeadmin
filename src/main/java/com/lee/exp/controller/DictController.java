package com.lee.exp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lee.exp.service.DictService;
import com.lee.annotation.RequiresPermissions;
import com.lee.annotation.SysLog;
import com.lee.common.controller.BaseController;
import com.lee.common.pageutil.Page;
import com.lee.common.utils.PasswordHelper;
import com.lee.common.utils.StringUtil;

@Controller
@RequestMapping("exp/dict")
public class DictController extends BaseController {

	private static Logger log = Logger.getLogger(DictController.class);
	
	@Autowired
	private DictService dictService;

	@RequiresPermissions("dict_list")
	@RequestMapping("list.do")
	public String list(Model model,HttpServletRequest request) {
		Map<String, Object> queryParams = getFormMap();
		
		String parent_id = request.getParameter("parent_id");
		if(StringUtil.isBlank(parent_id)) {
			parent_id = "0";
			queryParams.put("parent_id", parent_id);
		}
		// 上级字典
		Map<String, Object> parentDict = getParentDict(request, parent_id);
		model.addAttribute("parentDict", parentDict);
		
		// 字典列表
		Page<Map<String, Object>> page = dictService.getByConditionPage(queryParams);
		model.addAttribute("page", page);
		model.addAttribute("queryParams", queryParams);
		return "exp/dict/list";
	}

	@RequiresPermissions("dict_add")
	@RequestMapping("toAdd.do")
	public String toAdd(Model model,HttpServletRequest request) {
		Map<String, Object> queryParams = getFormMap();
		
		// 上级字典
		String parent_id = request.getParameter("parent_id");
		Map<String, Object> parentDict = getParentDict(request, parent_id);
		model.addAttribute("parentDict", parentDict);
		
		// 编辑
		String id = request.getParameter("id");
		if(StringUtil.isNotBlank(id)) {
			Map<String, Object> dict = dictService.getById(Long.parseLong(id));
			model.addAttribute("dict", dict);
		}
		
		model.addAttribute("queryParams", queryParams);
		return "exp/dict/add";
	}

	private Map<String, Object> getParentDict(HttpServletRequest request,String parent_id) {
		if(StringUtil.isNotBlank(parent_id)) {
			Long pid = Long.parseLong(parent_id);
			Map<String, Object> parentDict = dictService.getById(pid);
			return parentDict;
		}
		return null;
	}

	@RequiresPermissions("dict_add")
	@RequestMapping("add.do")
	@SysLog(module="数据字典管理",methods="新增/修改数据字典")
	public String add(Model model) {
		Map<String, Object> formParams = getFormMap();
		String id = formParams.get("id")+"";
		if(StringUtil.isBlank(id)) {
			dictService.create(formParams);
		} else {
			dictService.update(formParams);
		}
		
		return "redirect:list.do?parent_id="+formParams.get("parent_id");
	}

	
	@RequiresPermissions("dict_delete")
	@ResponseBody
	@RequestMapping(value="delete.do")
	@SysLog(module="数据字典管理",methods="删除数据字典")
	public JSONObject delete(HttpServletRequest request){
		String id = request.getParameter("id");
		dictService.delete(Long.parseLong(id));
		JSONObject jsonResult = getJsonResult("1000", "success", null);
		return jsonResult;
	}
	
}
