package com.lee.common.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;



/**
 * @author Administrator
 * 
 */
public class FLUtfil {
	/**
	 * 字符串支付为空
	 * 
	 * @param str
	 * @return true: 为空, false:不为空
	 */
	public static Boolean isHasLen(String str) {
		if (str == null || str.trim().length() == 0 || "null".equals(str)
				|| "undefined".equals(str)) {
			return true;
		}
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
//	public static void webJson(String result, String msg, Map data,
//			HttpServletResponse response) {
//		try {
//			JSONObject json = new JSONObject();
//			json.put("code", result);
//			json.put("msg", getCodereMsg(result));
//			if(data!=null)
//				json.put("info", data);
//			response.setCharacterEncoding("utf-8");
//			json.write(response.getWriter());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return;
//	}
//	public static void webJsonstr(String result, String msg, Map data,
//			HttpServletResponse response) {
//		try {
//			JSONObject json = new JSONObject();
//			json.put("result", result);
//			json.put("msg", msg);
//			if(data!=null)
//				json.put("info", data);
//			response.setCharacterEncoding("utf-8");
//			json.write(response.getWriter());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return;
//	}

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
//	public static void webJsonList(String result, String msg, List<Map> listData,int counts,
//			HttpServletResponse response) {
//		try {
//			JSONObject json = new JSONObject();
//			json.put("result", result);
//			json.put("msg", getCodereMsg(result));
//			if(listData!=null)
//				json.put("info", listData);
//			json.put("infoCount", counts);
//			response.setCharacterEncoding("utf-8");
//			json.write(response.getWriter());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return;
//	}
	
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
//	public static void webJsonList1(String result, String msg, List<Map<String,String>> listData,int counts,
//			HttpServletResponse response) {
//		try {
//			JSONObject json = new JSONObject();
//			json.put("code", result);
//			json.put("msg",getCodereMsg(result));
//			if(listData!=null)
//				json.put("info", listData);
//			json.put("infoCount", counts);
//			response.setCharacterEncoding("utf-8");
//			json.write(response.getWriter());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return;
//	}
	
//	public static void webJsonList1(String result, String msg, List<Map<String,String>> listData,int counts,
//			HttpServletResponse response,String ids) {
//		try {
//			JSONObject json = new JSONObject();
//			json.put("code", result);
//			json.put("msg",getCodereMsg(result));
//			if(listData!=null)
//				json.put("info", listData);
//			json.put("infoCount", counts);
//			json.put("ids", ids);
//			response.setCharacterEncoding("utf-8");
//			json.write(response.getWriter());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return;
//	}
	
	/**
	 * 组装json  定制的 含有经纬度返回参数
	 * 
	 * @param result
	 *            状态标志
	 * @param msg
	 *            异常时的文字提示
	 * @param data
	 *            正常时返回的数据
	 * @param response
	 */
//	public static void webJsonListll(String result, String msg, List<Map> listData,Map mapdate,int counts,
//			HttpServletResponse response) {
//		try {
//			JSONObject json = new JSONObject();
//			json.put("result", result);
//			json.put("msg", getCodereMsg(result));
//			if(listData!=null)
//				json.put("info", listData);
//			if(mapdate!=null)
//				json.put("point", mapdate);
//			json.put("infoCount", counts);
//			response.setCharacterEncoding("utf-8");
//			//json.write(response.getWriter());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return;
//	}
	
	/**
	 * 根据编码获得错误信息
	 * @param code 编码
	 * @return msg  错误信息
	 */
//	public static String getCodereMsg(String code) {
//		String config=getmsginfoConfig(); 
//		try{
//		JSONObject  retJson = JSONObject.fromObject(config);
//		String msg = retJson.get(code).toString();
//		return msg;
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		return "";
//	}
//	
	/**
	 * 获取错误代码配置
	 * @return
	 */
//	public static String getmsginfoConfig(){
//		String config = CacheManger.get("msginfoConfig");
//		if(FLUtfil.isHasLen(config)){
//			config = FreemarkerUtil.process("com/fq/template/MsgInfo.txt", null);
//			CacheManger.set("msginfoConfig", config);
//		}
//		return config;
//	}

    /**
     * 根据经纬度，获取两点间的距离
     * 
     * @author zhijun.wu
     * @param lng1 经度
     * @param lat1 纬度
     * @param lng2
     * @param lat2
     * @return
     *
     * @date 2011-8-10
     */
    public static int distanceByLngLat(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = lat1 * Math.PI / 180;
        double radLat2 = lat2 * Math.PI / 180;
        double a = radLat1 - radLat2;
        double b = lng1 * Math.PI / 180 - lng2 * Math.PI / 180;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
        s = Math.round(s * 10000) / 10000;                                                                                                                                                               
        int t=(int)s;
        return t;
    }
    
    public static Double distanceByDouble(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = lat1 * Math.PI / 180;
        double radLat2 = lat2 * Math.PI / 180;
        double a = radLat1 - radLat2;
        double b = lng1 * Math.PI / 180 - lng2 * Math.PI / 180;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
        s = Math.round(s * 10000) / 10000;                                                                                                                                                               
        return s;
    }
    
	/**
	 * 获取xml标签的值: 针对于一级标签
	 * @param xml 
	 * @param element 标签  
	 * @return
	 */
	public static String getElementValueOfXML(String xml, String element){
		if(xml == null || element == null) return null;
		 try {
			 	Document document = DocumentHelper.parseText(xml);
				Element root = document.getRootElement();
				return root.elementText(element);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 return null;
	}
	public static String getElementValueOfXML(String xml, String parentElement, String childElement){
		if(xml == null || parentElement == null || childElement == null) return null;
		 try {
			 	Document document = DocumentHelper.parseText(xml);
				Element root = document.getRootElement();
				Element parentEle = root.element(parentElement);
				return parentEle.elementText(childElement);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 return null;
	}
	/**
	 * 获取随机数
	 * @param count 多少位 
	 * @return
	 */
	public static String getRandom(int count){
		Random random = new Random(); 
        String result="";

        for(int i=0;i<count;i++){
            result+=random.nextInt(10);    
        }
        return result;
	}
    
  //根据imgs串逗号隔开的 组装成json格式
  	public static String toimgsfromjsonimg(String keys,String imgs){
  		String retstr="";
  		String[] keysarry=keys.split(",");
  		if(!FLUtfil.isHasLen(imgs)&&!FLUtfil.isHasLen(keys)){
  			String[] arrys=imgs.split("~");
  			for	(int i=0;i<arrys.length;i++){
  				if(!FLUtfil.isHasLen(arrys[i])){
  					String[] strarrys=arrys[i].split(",");
  					retstr+="{";
  					for (int j = 0; j < strarrys.length; j++) {
  						if(!FLUtfil.isHasLen(strarrys[j])){
  							retstr+="\""+keysarry[j]+"\":\""+ strarrys[j]+"\",";
  						}
					}
  					retstr=retstr.substring(0,retstr.length()-1);
  					retstr+="},";
  				}
  			}
  			if(!FLUtfil.isHasLen(retstr)) retstr="["+retstr.substring(0,retstr.length()-1)+"]";
  		}
  		return retstr;
  	}
  	 //根据imgs串逗号隔开的数据库in删除格式
  	public static String toimgsfrominimg(String imgs){
  		String retstr="";
  		if(!FLUtfil.isHasLen(imgs)){
  			String[] arrys=imgs.split(",");
  			for	(int i=0;i<arrys.length;i++){
  				if(!FLUtfil.isHasLen(arrys[i])){
  					retstr+="'"+arrys[i]+"',";
  				}
  			}
  			if(!FLUtfil.isHasLen(retstr)) retstr=retstr.substring(0,retstr.length()-1);
  		}
  		return retstr;
  	}
	
  	/**
  	 * 根据经纬度获得省市区地址
  	 * @param longitude
  	 * @param latitude
  	 * @return
  	 */
  	public static Map tolonlatfromaddress(String longitude,String latitude){
  		Map retmap=new HashMap();
  		try {
  			String url="http://api.map.baidu.com/geocoder?output=json&location=";
  			if(FLUtfil.isHasLen(longitude)||FLUtfil.isHasLen(latitude)) return null;
			String str=HttpUtil.getRequest(url+latitude+","+longitude);
			if(FLUtfil.isHasLen(str)) return null;
			JSONObject jb= JSONObject.parseObject(str);
			if(jb.get("result")==null)return null;
			JSONObject jb2= JSONObject.parseObject(jb.getString("result"));
			System.out.println(jb2);
			JSONObject jb3= JSONObject.parseObject(jb2.getString("addressComponent"));
			retmap.put("city", jb3.get("city"));
			retmap.put("area", jb3.get("district"));
			retmap.put("province", jb3.get("province"));
			retmap.put("street", jb3.get("street"));
			
			String address = ""+jb3.get("city")+jb3.get("district")+jb3.get("street")+jb3.get("street_number");
			String address1 = ""+jb3.get("district")+jb3.get("street");
			if(StringUtil.isNotBlank(address)){
				address += "附近";
			}
			if(StringUtil.isNotBlank(address1)){
				address1 += "附近";
			}
			retmap.put("address", address);
			retmap.put("address1",address1);
			System.out.println(retmap.get("address"));
			return retmap;
		} catch (Exception e) {
		}
		return null;
  	}
  	
  	public static Map getAddressBySoSo(String longitude,String latitude){
  		Map retmap=new HashMap();
  		try{
  			String url = "http://apis.map.qq.com/ws/place/v1/search?keyword=&boundary=nearby("+latitude+","+longitude+",1000)&key=RY3BZ-E47CQ-3TJ5N-GUYUU-TXLIE-F6F5A";
  			if(FLUtfil.isHasLen(longitude)||FLUtfil.isHasLen(latitude)) return null;
			String str=HttpUtil.getRequest(url);
			if(FLUtfil.isHasLen(str)) return null;
			JSONObject jb= JSONObject.parseObject(str);
			if(jb.get("data")==null)return null;
			//JSONObject jb2= JSONObject.fromObject(jb.get("data"));
			JSONArray ja = (JSONArray)jb.get("data");
			if(ja.get(0) != null){
				JSONObject o = (JSONObject)ja.get(0);
				retmap.put("address", o.get("title"));
			}
			System.out.println(ja);
  		}catch(Exception e){
  			e.printStackTrace();
  		}
  		return retmap;
  	}
  	
  	
  	/***
  	 * 通过关键字查询附近的商家相关
  	 * @return
  	 */
  	public static List getPlaceBaidu(String longitude,String latitude){
  		List nlist = new ArrayList();
  		try{
//  			String url = "http://api.map.baidu.com/place/v2/search?query=酒吧$酒店$KTV$商店$生活娱乐$餐饮$宾馆&output=json&scope=1&filter=industry_type" +
//  					"&page_size=50&page_num=0&ak=B684e28a85c136722087a7c8a530c3ad"  美食，酒店，休闲娱乐，小区，商场，超市，酒吧，电影院，KTV，景点;
  			String url = "http://api.map.baidu.com/place/v2/search?query=美食$酒店$休闲娱乐$小区$商场$超市$酒吧$电影院$KTV$景点&radius=1000&output=json&ak=B684e28a85c136722087a7c8a530c3ad&page_size=50&page_num=0&filter=industry_type&location=";
  			//String url = "http://api.map.baidu.com/geocoder/v2/?ak=B684e28a85c136722087a7c8a530c3ad&output=json&pois=1&page_size=50&location=";
			String str=HttpUtil.getRequest(url+latitude+","+longitude);
			str = str.replace("renderReverse&&renderReverse(", "");
			str = str.replace(")","");
			if(FLUtfil.isHasLen(str)) return null;
			JSONObject jb= JSONObject.parseObject(str);
			System.out.println(jb);
			if(jb.get("results")==null)return null;
			JSONArray jb1= jb.getJSONArray("results");
			
			for(int i=0;i<jb1.size();i++){
				JSONObject jo = jb1.getJSONObject(i);
				nlist.add(jo);
				System.out.println(jo.get("name") +"-----------------"+jo.get("address"));
			}
			
  		}catch(Exception e){
  			e.printStackTrace();
  		}
  		return nlist;
  	}
  	
  	
  	/****
  	 * 
  	 * http://api.map.baidu.com/place/v2/suggestion/
  	 * http://api.map.baidu.com/place/v2/suggestion?query=天安门&region=131&output=json&ak=E4805d16520de693a3fe707cdc962045
  	 * @param query
  	 * @return
  	 */
  	public static List SuggestionList(String query){
  		List nlist = new ArrayList();
  		try{
  			String url = "http://api.map.baidu.com/place/v2/suggestion?query="+query+"&region=全国&output=json&ak=B684e28a85c136722087a7c8a530c3ad";
  			String str=HttpUtil.getRequest(url);
			str = str.replace("renderReverse&&renderReverse(", "");
			str = str.replace(")","");
			if(FLUtfil.isHasLen(str)) return null;
			JSONObject jb= JSONObject.parseObject(str);
			if(jb.get("result")==null)return null;
			JSONArray jb1= (JSONArray)jb.get("result");
			for(int i=0;i<jb1.size();i++){
				JSONObject jo = jb1.getJSONObject(i);
				jo.put("address", ""+jo.get("city")+jo.get("district")+"");
				nlist.add(jo);
			}
  		}catch (Exception e) {
			// TODO: handle exception
		}
  		return nlist;
  	}
  	
  	/**
	 * 获得距离
	 * @return
	 */
	public static String infojinweidu(String longitudestr,String latitudestr,String slooklongitude,String slooklatitude){
		if(FLUtfil.isHasLen(longitudestr)||FLUtfil.isHasLen(latitudestr)||FLUtfil.isHasLen(slooklongitude)||FLUtfil.isHasLen(slooklatitude)){
			return "";
		}
		DecimalFormat df = new DecimalFormat("#.##");// km转换		
		double longitude, latitude;// 经纬度
		try {// 获取相距多少米			
				longitude = Double.parseDouble(longitudestr);
				latitude = Double.parseDouble(latitudestr);
				double looklongitude = Double.parseDouble(slooklongitude);// 所查看用户经度
				double looklatitude = Double.parseDouble(slooklatitude);// 所查看用户纬度
				if(longitude==0||latitude==0||looklatitude==0||looklongitude==0) return "0.00km";
				Double distance = distanceByDouble(longitude,latitude, looklongitude, looklatitude);// 相距多少米
				return df.format(distance/1000)+ "km";
			
		} catch (Exception ex) {
			return "";
		}	
	}
	
	/**
	 * 通过图片地址 格式化成  http://xxx*.xxx
	 * @param url 图片地址
	 * @return
	 */
	public static String imgformat(String url){
		String delimgs = url;// 删除图片地址
		if(!FLUtfil.isHasLen(delimgs)){
			String houzui=delimgs.substring(delimgs.lastIndexOf("."),delimgs.length());
			String qianzhui=delimgs.substring(0,delimgs.lastIndexOf("."));
			if(qianzhui.indexOf("_org")>-1 || qianzhui.indexOf("_mc")>-1 || qianzhui.indexOf("_fc")>-1){
				delimgs=qianzhui.replace("_org", "*").replace("_mc", "*").replace("_fc", "*")+houzui;
			}
			else{
				delimgs=qianzhui+"*"+houzui;
			}
		}
		return delimgs;
	}
	/**
	 * 根据手机号码,获取手机网段名(移动,联通,电信等)
	 * @param mobile
	 * @return
	 */
	public static String getNetworkName(String mobile){
		String name = "其他";
		 if(isHasLen(mobile)) return name;
		 if(mobile.indexOf("134")==0 || mobile.indexOf("135")==0 || mobile.indexOf("136")==0 || mobile.indexOf("137")==0 || 
				 mobile.indexOf("138")==0 || mobile.indexOf("138")==0 || mobile.indexOf("150")==0 || mobile.indexOf("151")==0 || 
				 mobile.indexOf("152")==0 || mobile.indexOf("154")==0 || mobile.indexOf("157")==0 || mobile.indexOf("158")==0 || 
				 mobile.indexOf("159")==0 || mobile.indexOf("183")==0 || mobile.indexOf("187")==0 || mobile.indexOf("188")==0 || 
				 mobile.indexOf("147")==0 ) name = "移动";
		 if(mobile.indexOf("130")==0 || mobile.indexOf("131")==0 || mobile.indexOf("132")==0 || mobile.indexOf("155")==0 || 
				 mobile.indexOf("156")==0 || mobile.indexOf("185")==0 || mobile.indexOf("186")==0) name = "联通";
		 if(mobile.indexOf("133")==0 || mobile.indexOf("153")==0 || mobile.indexOf("189")==0 || mobile.indexOf("180")==0
				) name = "电信";
		return name;
		 
	}
    public static void main(String[] args) throws Exception {    
//    	String result ="<?xml version='1.0' encoding='gbk' ?><response><code>03</code><message><desmobile>13388601850</desmobile><msgid>20130517145424336959</msgid></message></response>";
//    	String desmobile = FLUtfil.getElementValueOfXML(result, "message","desmobile"); //返回消息标签
//		String msgid = FLUtfil.getElementValueOfXML(result, "message","msgid");  //返回短信id
//		System.out.println(desmobile+"=="+msgid);
    	//String imgurls= FLUtfil.toimgsfromjsonimg("url,source,pp_longitude,pp_latitude","11,22,33,44~1a,2b,3c,4d~aa,bb,cc,dd~11a,22b,33c,44d~");
    	//System.out.println(imgurls);
    	
    	FLUtfil ff = new FLUtfil();
    	//Map m = ff.tolonlatfromaddress("113.9337790000", "22.5403020000");
    	System.out.println(ff.SuggestionList("文一路"));
    	//System.out.println(ff.tolonlatfromaddress("119.9858533144", "26.8748289023"));
    	//System.out.println(ff.getAddressBySoSo("119.9858533144", "26.8748289023"));
    }
}
