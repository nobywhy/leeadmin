package com.lee.admin.controller;

import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lee.admin.service.SysLogService;
import com.lee.annotation.RequiresPermissions;
import com.lee.common.controller.BaseController;
import com.lee.common.pageutil.Page;

@Controller
@RequestMapping("admin/syslog")
public class SysLogController extends BaseController {

	// @Resource(name="userSerivce")
	@Autowired
	private SysLogService logService;

	@RequiresPermissions("syslog_list")
	@RequestMapping("list.do")
	public String list(Model model) {
		Map<String, Object> queryParams = getFormMap();
		Page<Map<String, Object>> page = logService.getByConditionPage(queryParams);
		model.addAttribute("page", page);
		model.addAttribute("queryParams", queryParams);
		return "admin/syslog/list";
	}

	
	
}
