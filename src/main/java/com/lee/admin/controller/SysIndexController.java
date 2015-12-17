package com.lee.admin.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import com.lee.admin.service.SysResourcesService;
import com.lee.admin.service.SysResourcesUserService;
import com.lee.admin.service.SysUserService;
import com.lee.annotation.RequiresAuthentication;
import com.lee.annotation.SysLog;
import com.lee.common.Constants;
import com.lee.common.controller.BaseController;

@Controller
@RequestMapping("admin")
public class SysIndexController extends BaseController {

	protected static Logger log = Logger.getLogger(SysIndexController.class);
	
	@Autowired
	private SysResourcesService resourcesService;
	@Autowired
	private SysResourcesUserService resourcesUserService;
	@Autowired
	private SysUserService userService;
	
	@RequiresAuthentication
	@RequestMapping("index.do")
	public String index(Model model) {
		return "admin/index";
	}

	@RequiresAuthentication
	@RequestMapping("top.do")
	public String top() {
		return "admin/top";
	}

	@RequiresAuthentication
	@RequestMapping("left.do")
	public String left(Model model,HttpServletRequest request,HttpServletResponse response) {
		
		Map<String, Object> qm = new HashMap<String, Object>(1);
		qm.put("userId", getUserId(request,response));
		qm.put("parentId", "0");
		qm.put("status", 1);
		List<Map<String, Object>> userResources = resourcesUserService.getUserResources(qm);
		
		// 支持两级菜单
		for (Map<String, Object> map : userResources) {
			Map<String, Object> sqm = new HashMap<String, Object>();
			sqm.put("parentId", map.get("id"));
			sqm.put("userId", getUserId(request,response));
			sqm.put("status", 1);
			List<Map<String, Object>> subResources = resourcesUserService.getUserResources(sqm);
			map.put("subResources", subResources);
		}

		model.addAttribute("resources", userResources);
		return "admin/left";
	}

	
	@RequiresAuthentication
	@RequestMapping("welcome.do")
	public String welcome() {
		return "admin/welcome";
	}
		
	@RequestMapping("login.do")
	public String login() {
		return "admin/login";
	}

	/**
	 * 登录
	 * @param model
	 * @param accountName
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping("signin.do")
	public String signin(Model model,@RequestParam(value="accountName") String accountName,@RequestParam(value="password") String password,HttpServletRequest request) {
		try {
			Map<String, Object> user = userService.login(accountName,password);
			// 保存用户信息到session中
			saveUserSession(request,user);
			log.info(accountName+"登录成功");
			return "redirect:index.do";
		} catch (LockedAccountException lae) {
			model.addAttribute("error", "用户已经被锁定不能登录，请与管理员联系！");
			log.error("用户已经被锁定不能登录，请与管理员联系！", lae);
		} catch (AuthenticationException e) {
			model.addAttribute("error", "用户或密码不正确！");
			log.error("用户或密码不正确！", e);
		}
		return "admin/login";
	}

	/**
	 * 退出
	 * @return
	 */
	@RequestMapping("logout.do")
	public String logout(HttpServletRequest request){
		WebUtils.setSessionAttribute(request, Constants.userSession, null);
		WebUtils.setSessionAttribute(request, Constants.userSessionId, null);
		WebUtils.setSessionAttribute(request, Constants.premissions, null);
		return "redirect:login.do";
	}
	
	/**
	 * 错误页面
	 * @return
	 */
	@RequestMapping("/error.do")
	public String error() {
		return "admin/error";
	}
	
	/**
	 * 未授权页面
	 * @return
	 */
	@RequestMapping("/unauthorized.do")
	public String unauthorized(){
		return "admin/none_authority";
	}
	
	
	/**
	 * 保存用户信息到session中
	 * @param request
	 * @param user
	 */
	private void saveUserSession(HttpServletRequest request,Map<String, Object> user) {
		WebUtils.setSessionAttribute(request, Constants.userSession, user);
		WebUtils.setSessionAttribute(request, Constants.userSessionId, user.get("id"));
//		HttpSession session = request.getSession();
//		session.setAttribute(Constants.userSession, user);
//		session.setAttribute(Constants.userSessionId, user.get("id"));
		// 保存用户的权限信息到session中
		saveUserPerssionSession(user.get("id")+"",request);
	}
	
	/**
	 * 保存用户的权限信息到session中
	 * @param userId
	 * @param request
	 */
	public void saveUserPerssionSession(String userId,HttpServletRequest request){
		Map<String, Object> qm = new HashMap<String, Object>(1);
		qm.put("userId", userId);
		qm.put("status", 1);
		List<Map<String, Object>> userResources = resourcesUserService.getUserResources(qm);
		
		// 用户权限添加到session中
		Set<String> premissions = new HashSet<String>();
		for (Map<String, Object> resources : userResources) {
			premissions.add(resources.get("resKey").toString());
		}
		//request.getSession().setAttribute(Constants.premissions, premissions);
		WebUtils.setSessionAttribute(request, Constants.premissions, premissions);
	}
	

}
