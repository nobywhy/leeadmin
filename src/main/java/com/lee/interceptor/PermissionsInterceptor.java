package com.lee.interceptor;

import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.lee.admin.service.SysResourcesUserService;
import com.lee.annotation.RequiresAuthentication;
import com.lee.annotation.RequiresPermissions;
import com.lee.common.Constants;
import com.lee.common.utils.StringUtil;

/**
 * 权限拦截器
 * 
 * @author lw
 *
 */
public class PermissionsInterceptor implements HandlerInterceptor {

	@Autowired
	private SysResourcesUserService resourcesUserService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
			String contextPath = request.getContextPath();
			
			RequiresPermissions pequiresPermissions = ((HandlerMethod) handler).getMethodAnnotation(RequiresPermissions.class);
			RequiresAuthentication requiresAuthentication = ((HandlerMethod) handler).getMethodAnnotation(RequiresAuthentication.class);
			if(requiresAuthentication!=null) {// 需要登录
				String userId = WebUtils.getSessionAttribute(request, Constants.userSessionId)+"";
				if (StringUtil.isBlank(userId)) {// 未登录
					response.sendRedirect(contextPath+"/admin/login.do");
					return false;
				}
			}
			
			
			// 没有声明需要权限,或者声明不验证权限
			if (pequiresPermissions == null) {
				return true;
			} else {
				
				String value = pequiresPermissions.value();
				String userId = request.getSession().getAttribute(Constants.userSessionId) + "";
				if (StringUtil.isBlank(userId)) {// 未登录
					response.sendRedirect(contextPath+"/admin/login.do");
					return false;
				}

				Set<String> premissions = (Set<String>) request.getSession().getAttribute(Constants.premissions);
				if(premissions==null) {
					response.sendRedirect(contextPath+"/admin/unauthorized.do");
					return false;
				}
				if (!premissions.contains(value)) {// 没有权限
					response.sendRedirect(contextPath+"/admin/unauthorized.do");
					return false;
				}else{
					return true;
				}

			}
		}
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
