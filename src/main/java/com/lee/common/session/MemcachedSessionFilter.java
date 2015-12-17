package com.lee.common.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lee.common.utils.CacheTools;


public class MemcachedSessionFilter extends HttpServlet implements Filter {

	private static final long serialVersionUID = -8145597084045758430L;
	private String sessionId = "MCSessionId";
	private String cookieDomain = ".hilinli.com";
	private String cookiePath = "/";
	private String memcachedName = "SessionMem";

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		String sid = null;
		if(sid == null) {
			Cookie cookies[] = request.getCookies();
			Cookie sCookie = null;
			
			if (cookies != null && cookies.length > 0) {
				for (int i = 0; i < cookies.length; i++) {
					sCookie = cookies[i];
					if (sCookie.getName().equals(sessionId)) {
						sid = sCookie.getValue();
					}
				}
			}
		}
		
		if (sid == null || sid.length() == 0) {
			sid = java.util.UUID.randomUUID().toString();
			Cookie mycookies = new Cookie(sessionId, sid);
			mycookies.setMaxAge(-1);
			if (this.cookieDomain != null && this.cookieDomain.length() > 0) {
				//mycookies.setDomain(this.cookieDomain);
			}
			mycookies.setPath(this.cookiePath);
			response.addCookie(mycookies);
		}
		filterChain.doFilter(new HttpServletRequestWrapper(sid, request),
				servletResponse);
	}

	public void init(FilterConfig config) throws ServletException {
		String value = config.getInitParameter("sessionId");
		if(value != null && value.trim().length() > 0) {
			sessionId = value.trim();
		}
		value = config.getInitParameter("cookieDomain");
		if(value != null && value.trim().length() > 0) {
			cookieDomain = value.trim();
		}
		value = config.getInitParameter("cookiePath");
		if(value != null && value.trim().length() > 0) {
			cookiePath = value.trim();
		}
		value = config.getInitParameter("memcachedName");
		if(value != null && value.trim().length() > 0) {
			memcachedName = value.trim();
		}
		CacheTools.init();		
		SessionService.setMemcacheName(memcachedName);
	}
	public void destroy() {		
		CacheTools.stop();		
		
	}
}
