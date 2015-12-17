package com.lee.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 发送post
 * 
 * @author liuxiangrong 2013-05-16 下午 13：52：23
 *
 */

public class HttpUtil {
	public static HttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());

	/**
	 * @param commString
	 *            需要发送的url参数串
	 * @param address
	 *            需要发送的url地址
	 * @return rec_string 国都返回的XML格式的串
	 * @catch Exception
	 */
	public static String postURL(String commString, String address) {
		String rec_string = "";
		URL url = null;
		HttpURLConnection urlConn = null;
		try {
			/* 得到url地址的URL类 */
			url = new URL(address);
			/* 获得打开需要发送的url连接 */
			urlConn = (HttpURLConnection) url.openConnection();
			/* 设置连接超时时间 */
			urlConn.setConnectTimeout(30000);
			/* 设置读取响应超时时间 */
			urlConn.setReadTimeout(30000);
			/* 设置post发送方式 */
			urlConn.setRequestMethod("POST");
			/* 发送commString */
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			OutputStreamWriter out;
			out = new OutputStreamWriter(urlConn.getOutputStream(), "GBK");
			out.write(commString);
			out.flush();
			out.close();
			/* 发送完毕 获取返回流，解析流数据 */
			BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "GBK"));
			StringBuffer sb = new StringBuffer();
			int ch;
			while ((ch = rd.read()) > -1) {
				sb.append((char) ch);
			}
			rec_string = sb.toString().trim();
			/* 解析完毕关闭输入流 */
			rd.close();
		} catch (Exception e) {
			/* 异常处理 */
			rec_string = "-107";
			System.out.println(e);
		} finally {
			if (urlConn != null) {
				/* 关闭URL连接 */
				urlConn.disconnect();
			}
		}
		/* 返回响应内容 */
		return rec_string;
	}

	public static String postURL(String commString, String address, String encode) {
		String rec_string = "";
		URL url = null;
		HttpURLConnection urlConn = null;
		try {
			/* 得到url地址的URL类 */
			url = new URL(address);
			/* 获得打开需要发送的url连接 */
			urlConn = (HttpURLConnection) url.openConnection();
			/* 设置连接超时时间 */
			urlConn.setConnectTimeout(30000);
			/* 设置读取响应超时时间 */
			urlConn.setReadTimeout(30000);
			/* 设置post发送方式 */
			urlConn.setRequestMethod("POST");
			/* 发送commString */
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			OutputStreamWriter out;
			out = new OutputStreamWriter(urlConn.getOutputStream(), encode);
			out.write(commString);
			out.flush();
			out.close();
			/* 发送完毕 获取返回流，解析流数据 */
			BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), encode));
			StringBuffer sb = new StringBuffer();
			int ch;
			while ((ch = rd.read()) > -1) {
				sb.append((char) ch);
			}
			rec_string = sb.toString().trim();
			/* 解析完毕关闭输入流 */
			rd.close();
		} catch (Exception e) {
			/* 异常处理 */
			rec_string = "-107";
			System.out.println(e);
		} finally {
			if (urlConn != null) {
				/* 关闭URL连接 */
				urlConn.disconnect();
			}
		}
		/* 返回响应内容 */
		return rec_string;
	}

	/**
	 * 通过get请求url地址,获取返回结果
	 * 
	 * @param url
	 *            发送请求的url
	 * @return
	 * @throws Exception
	 */
	public static String getRequest(String url) throws Exception {
		HttpGet get = new HttpGet(url);
		// 发送get请求
		HttpResponse httpResponse = httpClient.execute(get);
		// 如果服务器成功返回响应
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			// 获取服务器响应字符串
			String result = EntityUtils.toString(httpResponse.getEntity());
			return result;
		}
		return null;
	}

	/**
	 * 通过post请求url地址,获取返回结果
	 * 
	 * @param url
	 * @param rawParams
	 * @return
	 * @throws Exception
	 */
	public static String postRequest(String url, Map<String, String> rawParams) throws Exception {
		HttpPost post = new HttpPost(url);
		// 如果传递参数比较多,可以先封装参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : rawParams.keySet()) {
			// 封装请求参数
			params.add(new BasicNameValuePair(key, rawParams.get(key)));
		}
		// 设置请求参数
		post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
		// 发送post请求
		HttpResponse httpResponse = httpClient.execute(post);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			return result;
		}
		return null;
	}

	public static JSONObject getJSONObjectPostRequest(String url, Map<String, String> rawParams) throws Exception {
		String json = postRequest(url, rawParams);
		if (json == null)
			return null;
		JSONObject object = JSONObject.parseObject(json.toString());
		return object;
	}

	public static JSONObject getJSONObjectRequest(String url, Map<String, String> rawParams) throws Exception {
		String json = postRequest(url, rawParams);
		if (json == null)
			return null;
		JSONObject object = JSONObject.parseObject(json);
		return object;
	}

	public static Map fileUpload(File file, String filename, int width, int height, String url) {
		Map resultMap = new HashMap();
		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(15000);// 15秒链接超时
		client.getHttpConnectionManager().getParams().setSoTimeout(15000);// 15秒读取超时
		// Constants.UPLOADURL
		PostMethod pm = new PostMethod(url);
		try {
			FilePart param_f = new FilePart("filename", file);
			StringPart param_size = new StringPart("compressSize", width + "," + height);
			MultipartRequestEntity mre = new MultipartRequestEntity(new Part[] { param_f, param_size }, pm.getParams());
			pm.setRequestEntity(mre);
			// 使用系统提供的默认的恢复策略
			pm.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			int status = client.executeMethod(pm);
			if (status != HttpStatus.SC_OK) {
				// httplog.info("http链接异常:status=" + status);
				resultMap.put("result", "m_error");
				resultMap.put("msg", "http链接异常:status=" + status);
				return resultMap;
			}
			String result = pm.getResponseBodyAsString();
			if (result != null) {
				result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
			}
			JSONObject jobj = JSONObject.parseObject(result);
			if ("1000".equals(jobj.getString("code"))) {
				JSONObject tempObj = jobj.getJSONObject("info");
				if (tempObj != null) {
					resultMap.put("result", "1000");
					resultMap.put("msg", "success");
					resultMap.put("picurl", tempObj.getString("img_url"));
					resultMap.put("orgurl", tempObj.getString("org_url"));
					resultMap.put("width", tempObj.getString("width"));
					resultMap.put("height", tempObj.getString("height"));
				} else {
					resultMap.put("result", "s_error");
					resultMap.put("msg", "参数异常,无url返回");
				}
			} else {
				resultMap.put("result", jobj.getString("code"));
				resultMap.put("msg", jobj.getString("msg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "m_exception");
			resultMap.put("msg", "文件上传异常:" + e.getMessage());
			return resultMap;
		}
		return resultMap;
	}

	// public static Map fileUpload(File file, String filename, String url,
	// String type) {
	// Map resultMap = new HashMap();
	// HttpClient client = new HttpClient();
	// client.getHttpConnectionManager().getParams().setConnectionTimeout(15000);//
	// 15秒链接超时
	// client.getHttpConnectionManager().getParams().setSoTimeout(15000);//
	// 15秒读取超时
	// // Constants.UPLOADURL
	// PostMethod pm = new PostMethod(url);
	// try {
	// FilePart param_f = new FilePart("video", file);
	// StringPart videoFileName = new StringPart("names", filename, "UTF-8");
	// StringPart fileType = new StringPart("type", type);
	// MultipartRequestEntity mre = new MultipartRequestEntity(new Part[] {
	// param_f, videoFileName, fileType }, pm.getParams());
	// pm.setRequestEntity(mre);
	// // 使用系统提供的默认的恢复策略
	// pm.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new
	// DefaultHttpMethodRetryHandler());
	// int status = client.executeMethod(pm);
	// if (status != HttpStatus.SC_OK) {
	// httplog.info("http链接异常:status=" + status);
	// resultMap.put("result", "m_error");
	// resultMap.put("msg", "http链接异常:status=" + status);
	// return resultMap;
	// }
	// String result = pm.getResponseBodyAsString();
	// // if (result != null) {
	// // result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
	// // }
	// JSONObject jobj = new JSONObject(result.trim());
	// if ("1000".equals(jobj.getString("code"))) {
	// JSONObject tempObj = jobj.getJSONObject("info");
	// if (tempObj != null) {
	// resultMap.put("result", "1000");
	// resultMap.put("msg", "success");
	// resultMap.put("web_url", tempObj.getString("web_url"));
	// } else {
	// resultMap.put("result", "s_error");
	// resultMap.put("msg", "参数异常,无url返回");
	// }
	// } else {
	// resultMap.put("result", jobj.getString("code"));
	// resultMap.put("msg", jobj.getString("msg"));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// resultMap.put("result", "m_exception");
	// resultMap.put("msg", "文件上传异常:" + e.getMessage());
	// return resultMap;
	// }
	// return resultMap;
	// }

	public static void main(String[] arags) {
		String url = "http://uhzb007113.chinaw3.com/pagecap!getRateCount.htm";
		String nick = "展展1213";
		Map map = new HashMap();
		map.put("nick", nick);
		try {
			JSONObject str = getJSONObjectPostRequest(url, map);
			// System.out.println(str.getInt("rateCount"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
