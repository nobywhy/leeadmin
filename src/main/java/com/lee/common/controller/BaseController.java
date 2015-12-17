package com.lee.common.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;
import com.lee.common.Constants;
import com.lee.common.utils.StringUtil;

public class BaseController {

	/**
	 * 获取当前登录用户信息
	 * @return
	 */
	public Map<String,Object> getUserInfo(HttpServletRequest request,HttpServletResponse response){
		return (Map<String, Object>) WebUtils.getSessionAttribute(request, Constants.userSession);
	}
	
	/**
	 * 获取当前登录用户id
	 * @return
	 */
	public int getUserId(HttpServletRequest request,HttpServletResponse response) {
		if(StringUtil.isBlank(WebUtils.getSessionAttribute(request, Constants.userSessionId)+"")) {
			try {
				response.sendRedirect("login.do");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 0;
		}
		return Integer.parseInt(WebUtils.getSessionAttribute(request, Constants.userSessionId)+"");
	}
	
	
	
	/**
	 * 所有请求参数封装成map
	 * @return
	 */
	public Map<String,Object> getFormMap(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		return  getFormMap(request);
	}
	
	
	/**
	 * 所有请求参数封装成map
	 * @return
	 */
	public Map<String,Object> getFormMap(HttpServletRequest request){
		//HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		Enumeration<String> en = request.getParameterNames();
		Map<String, Object> map = new HashMap<String, Object>();
		while (en.hasMoreElements()) {
			String nms = en.nextElement().toString();
			// 数组
			if(nms.endsWith("[]")){
				String[] values = request.getParameterValues(nms);
				if(values!=null && values.length!=0 && values.toString()!="[]"){
					map.put( nms,values);
				}
			}else{
				String as = request.getParameter(nms);
				if(!StringUtils.isEmpty(as)){
					map.put( nms, as);
				}
			}
		}
		return  map;
	}
	
	/**
	 * 封装json结果
	 * @param code
	 * @param message
	 * @param data
	 * @return
	 */
	public JSONObject getJsonResult(String code,String message,Object data) {
		JSONObject json =new JSONObject();
		json.put("code", "1000");
		json.put("message", message);
		if(data!=null)json.put("data", data);
		return json;
	}
	
	
}
