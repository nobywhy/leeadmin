package com.lee.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public class StringUtil {
	public static boolean isEmpty(String str)
	{
		return str == null || str.length() == 0;
	}
	public static boolean isNotEmpty(String str)
	{
		return str != null && str.length() > 0;
	}
	public static boolean isBlank(String str)
	{
		int length;
		if (str == null || (length = str.length()) == 0 || "null".equals(str))
			return true;
		for (int i = 0; i < length; i++)
			if (!Character.isWhitespace(str.charAt(i)))
				return false;

		return true;
	}

	public static boolean isNotBlank(String str)
	{
		int length;
		if (str == null || (length = str.length()) == 0)
			return false;
		for (int i = 0; i < length; i++)
			if (!Character.isWhitespace(str.charAt(i)))
				return true;

		return false;
	}
	/**
	 * 组装json
	 * 
	 * @param result
	 *            状态标志
	 * @param msg
	 *            异常时的文字提示
	 * @param data
	 *            正常时返回的数据
	 * @param response
	 */
	public static void webJson(String result, String msg, Map data,
			HttpServletResponse response) {
		try {
			JSONObject json = new JSONObject();
			json.put("result", result);
			json.put("msg", msg);
			if(data!=null)
				json.put("info", data);
			response.setCharacterEncoding("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 组装json
	 * 
	 * @param result
	 *            状态标志
	 * @param msg
	 *            异常时的文字提示
	 * @param data
	 *            正常时返回的数据
	 * @param response
	 */
	public static void webJsonList(String result, String msg, List<Map> listData,HttpServletResponse response) {
		try {
			JSONObject json = new JSONObject();
			json.put("result", result);
			json.put("msg", msg);
			if(listData!=null)json.put("info", listData);
			response.setCharacterEncoding("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	/**
	 * 根据图片名字获取图片类型
	 * @param imgurl
	 * @return
	 */
	public static String getImgType(String imgname){
		String type = null;
		int idx = imgname.lastIndexOf(".");
		if(idx > 0) type = imgname.substring(idx + 1).toLowerCase();
		String file_suffix_inlcude = "jpg,jpeg,bmp,gif,png";
		if (file_suffix_inlcude.indexOf(type)<0) {
			type = "jpg";
		}
		return type;
	}
	
	public static String barTime(Date create_time){
		if(create_time == null)return null;
		String rtime = null;
		long endT = create_time.getTime();
		long startT = new Date().getTime();
		long ss = (startT-endT)/(1000); //共计秒数
	    int mm = (int)ss/60;   //共计分钟数
	    if(mm >= 60){
	    	int hh=(int)ss/3600; 
	    	if(hh >= 24){
	    		int dd=(int)hh/24; 
	    		rtime = dd + "天前";
	    	}else{
	    		rtime = hh+"小时前";
	    	}
	    }else if(mm == 0){
	    	rtime = "刚刚";
	    }else{
	    	rtime = mm+"分钟前";
	    }
		return rtime;
	}
	
	public static String formateDate(Date date,String fmt){
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.format(date);
	}
	
}
