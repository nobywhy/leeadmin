package com.lee.common.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.lee.admin.controller.SysIndexController;

public class ExceptionHandler implements HandlerExceptionResolver {  
  
	protected static Logger log = Logger.getLogger(ExceptionHandler.class);
	
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,  
            Exception ex) {  
        Map<String, Object> model = new HashMap<String, Object>();  
        model.put("ex", ex);  
        log.error(ex.getMessage(), ex);
        // 根据不同错误转向不同页面  
//        if(ex instanceof BusinessException) {  
//            return new ModelAndView("error-business", model);  
//        }else if(ex instanceof ParameterException) {  
//            return new ModelAndView("error-parameter", model);  
//        } else {  
//            return new ModelAndView("error", model);  
//        }  
        return new ModelAndView("admin/error", model);  
    }  
}  