package com.lee.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;










import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import com.lee.admin.service.SysLogService;
import com.lee.annotation.SysLog;
import com.lee.common.Constants;
import com.lee.common.utils.StringUtil;

/**
 * 切点类
 */
@Aspect
@Component
public class SysLogInterceptor {
	// 本地异常日志记录对象
	private static final Logger logger = LoggerFactory.getLogger(SysLogInterceptor.class);
	@Autowired
	private SysLogService logService;

	// Controller层切点
	@Pointcut("@annotation(com.lee.annotation.SysLog)")
	public void controllerAspect() {
	}

	@AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
	public void doAfterThrowing(JoinPoint point, Throwable e) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		Map<String,Object> logForm = new HashMap<String, Object>();
		Map<String, Object> map = null;
		String accountName = null;
		String ip = null;
		Map<String,Object> userSession = null;
		try {
			ip = request.getRemoteAddr();
		} catch (Exception ee) {
			ip = "无法获取登录用户Ip";
		}
		try {
			map = getControllerMethodDescription(point);
			// 登录名
			userSession = (Map<String, Object>) WebUtils.getSessionAttribute(request, Constants.userSession);
			accountName = (String) userSession.get("accountName");
		} catch (Exception ee) {
			accountName = "无法获取登录用户信息！";
		}

		logForm.put("accountName", accountName);
		logForm.put("module", map.get("module"));
		logForm.put("methods", "<font color=\"red\">执行方法异常:-->" + map.get("methods") + "</font>");
		logForm.put("description", "<font color=\"red\">执行方法异常:-->" + e + "</font>");
		logForm.put("actionTime", "0");
		logForm.put("userIP", ip);
		try {
			logService.create(logForm);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 前置通知 用于拦截Controller层记录用户的操作
	 *
	 * @param joinPoint
	 *            切点
	 */
	@Around("controllerAspect()")
	public Object doController(ProceedingJoinPoint point) {
		Object result = null;
		// 执行方法名
		String methodName = point.getSignature().getName();
		String className = point.getTarget().getClass().getSimpleName();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		Map<String,Object> logForm = new HashMap<String, Object>();
		Map<String, Object> map = null;
		Long start = 0L;
		Long end = 0L;
		Long time = 0L;
		String accountName = null;
		String ip = null;
		Map<String,Object> userSession = null;
		try {
			ip = request.getRemoteAddr();
		} catch (Exception e) {
			ip = "无法获取登录用户Ip";
		}
		try {
			map = getControllerMethodDescription(point);
			// 登录名
			userSession = (Map<String, Object>) WebUtils.getSessionAttribute(request, Constants.userSession);
			accountName = (String) userSession.get("accountName");
		} catch (Exception ee) {
			accountName = "无法获取登录用户信息！";
		}
		// 当前用户
		try {
			map = getControllerMethodDescription(point);
			// 执行方法所消耗的时间
			start = System.currentTimeMillis();
			result = point.proceed();
			end = System.currentTimeMillis();
			time = end - start;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		try {
			logForm.put("accountName", accountName);
			logForm.put("module", map.get("module"));
			logForm.put("methods", map.get("methods"));
			logForm.put("description", map.get("description"));
			logForm.put("actionTime", time.toString());
			logForm.put("userIP", ip);
			logService.create(logForm);
			// *========控制台输出=========*//
			System.out.println("=====通知开始=====");
			System.out.println("请求方法:" + className + "." + methodName + "()");
			System.out.println("方法描述:" + map);
			System.out.println("请求IP:" + ip);
			System.out.println("=====通知结束=====");
		} catch (Exception e) {
			// 记录本地异常日志
			logger.error("====通知异常====");
			logger.error("异常信息:{}", e.getMessage());
		}
		return result;
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 *
	 * @param joinPoint
	 *            切点
	 * @return 方法描述
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					map.put("module", method.getAnnotation(SysLog.class).module());
					map.put("methods", method.getAnnotation(SysLog.class).methods());
					String de = method.getAnnotation(SysLog.class).description();
					if (StringUtil.isEmpty(de))
						de = "执行成功!";
					map.put("description", de);
					break;
				}
			}
		}
		return map;
	}
}
