package com.lee.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lee.admin.service.SysResourcesService;
import com.lee.admin.service.SysResourcesUserService;
import com.lee.admin.service.SysUserService;
import com.lee.annotation.RequiresPermissions;
import com.lee.annotation.SysLog;
import com.lee.common.controller.BaseController;
import com.lee.common.pageutil.Page;
import com.lee.common.utils.PasswordHelper;
import com.lee.common.utils.StringUtil;

@Controller
@RequestMapping("admin/user")
public class SysUserController extends BaseController {

	// @Resource(name="userSerivce")
	@Autowired
	private SysUserService userService;
	@Autowired
	private SysResourcesService resourcesService;
	@Autowired
	private SysResourcesUserService resourcesUserService;

	@RequiresPermissions("user_list")
	@RequestMapping("list.do")
	public String list(Model model) {
		Map<String, Object> queryParams = getFormMap();
		Page<Map<String, Object>> page = userService.getByConditionPage(queryParams);
		model.addAttribute("page", page);
		model.addAttribute("queryParams", queryParams);
		return "admin/user/list";
	}

	@RequiresPermissions("user_add")
	@RequestMapping("toAdd.do")
	public String toAdd(Model model,HttpServletRequest request) {
		Map<String, Object> queryParams = getFormMap();
		
		String id = request.getParameter("id");
		if(StringUtil.isNotBlank(id)) {
			Map<String, Object> user = userService.getById(Integer.parseInt(id));
			model.addAttribute("user", user);
		}
		
		model.addAttribute("queryParams", queryParams);
		return "admin/user/add";
	}

	@RequiresPermissions("user_add")
	@RequestMapping("add.do")
	@SysLog(module="用户管理",methods="新增/修改用户")
	public String add(Model model) {
		Map<String, Object> formParams = getFormMap();
		String id = formParams.get("id")+"";
		if(StringUtil.isBlank(id)) {
			String encryptPassword =  PasswordHelper.encryptPassword(formParams.get("password")+"");
			formParams.put("password",encryptPassword);
			userService.create(formParams);
		} else {
			userService.update(formParams);
		}
		
		return "redirect:list.do";
	}

	/**
	 * 用户权限树
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user_add")
	@RequestMapping("toAuthorize.do")
	public String toAuthorize(Model model,HttpServletRequest request) {
		
		String accountName= request.getParameter("accountName");
		model.addAttribute("accountName", accountName);
		
		String uid = request.getParameter("uid");
		model.addAttribute("uid", uid);
				
		// 所有可用菜单
		Map<String, Object> treeQm = new HashMap<String, Object>();
		treeQm.put("status", 1);
		List<Map<String, Object>> tree = resourcesService.tree(treeQm);
		
		// 用户的菜单
		Map<String, Object> qm = new HashMap<String, Object>(1);
		qm.put("userId", uid);
		qm.put("status", 1);
		List<Map<String, Object>> userResources = resourcesUserService.getUserResources(qm);
		for (Map<String, Object> map : tree) {
			map.put("open", true);
			String id = map.get("id") + "";
			for (Map<String, Object> userRes : userResources) {
				String userResId = userRes.get("id") + "";
				if (id.equals(userResId)) {
					map.put("checked", true);
				}
			}
		}
		String zNode = JSONArray.toJSONString(tree);
		model.addAttribute("zNode", zNode);
		return "admin/user/authorize";
	}
	
	
	@RequiresPermissions("user_delete")
	@ResponseBody
	@RequestMapping(value="delete.do")
	@SysLog(module="用户管理",methods="删除用户")
	public JSONObject delete(HttpServletRequest request){
		String id = request.getParameter("id");
		userService.delete(Integer.parseInt(id));
		JSONObject jsonResult = getJsonResult("1000", "message", null);
		return jsonResult;
	}
	
	/**
	 * 用户授权
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions("user_add")
	@RequestMapping("authorize.do")
	@SysLog(module="用户管理",methods="授权")
	public String authorize(Model model,HttpServletRequest request) {
		
		// 用户ID
		String uid= request.getParameter("uid");
		// 权限ID数组
		String ids = request.getParameter("ids");
		// 授权
		resourcesUserService.addAuthorize(Integer.parseInt(uid), ids.split(","));
		return "redirect:list.do";
	}
	
}
