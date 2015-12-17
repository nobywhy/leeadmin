package com.lee.common.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.lee.common.Constants;
import com.lee.common.utils.HttpUtil;
import com.lee.common.utils.ImgServiceManage;
import com.lee.common.utils.PicServiceManage;

@Controller
public class UploadController extends BaseController {

//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@ResponseBody
//	@RequestMapping(value = "upload.do")
//	public String upload(@RequestParam MultipartFile[] files) {
//		JSONObject json = new JSONObject();
//		if (files == null || files.length == 0) {
//			json.put("code", "2001");
//			json.put("message", "请输入上传文件");
//			return json.toJSONString();
//		}
//
//		for (MultipartFile ufile : files) {
//			if (!ufile.isEmpty()) {
//				// File file = new File(path + new Date().getTime() + ".jpg");
//				// //服务器上新建文件
//				CommonsMultipartFile cf = (CommonsMultipartFile) ufile;
//				DiskFileItem fi = (DiskFileItem) cf.getFileItem();
//				File file = fi.getStoreLocation();
//				try {
//					String compressSize = "";
//					Map map = new HashMap();
//					map.put(PicServiceManage.key, ImgServiceManage.analyzeParam(compressSize));
//					map = ImgServiceManage.saveSinglePicture(file, ufile.getOriginalFilename(), map, true);
//
//					Object web_url = null;
//					if (map == null || map.size() == 0 || (web_url = map.get("web_url")) == null) {
//						// WebRes.infoSend(WebRes.resToJson(0, "2001",
//						// "文件上传失败"));
//						json.put("code", "2001");
//						json.put("message", "文件上传失败");
//						return json.toJSONString();
//					}
//
//					File diskFile = new File(ImgServiceManage.getDiskPathByWebpath(web_url.toString()));
//					if (!diskFile.exists()) {
//						// 硬盘地址不存在,拷贝失败
//						json.put("code", "2001");
//						json.put("message", "文件上传失败");
//						return json.toJSONString();
//					}
//					// Map resultMap = new HashMap();
//					json.put("img_url", web_url.toString());
//					json.put("org_url", map.get("org_url"));
//					json.put("width", map.get("width"));
//					json.put("height", map.get("height"));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		json.put("code", "success");
//		return json.toJSONString();
//	}

	@ResponseBody
	@RequestMapping("upload.do")
	public String upload(@RequestParam MultipartFile[] files,int width,int height) {
		try {
			JSONObject json = new JSONObject();
			if (files == null || files.length == 0) {
				json.put("code", "2001");
				json.put("message", "请选择图片");
				return json.toJSONString();
			}
			
			for (MultipartFile ufile : files) {
				if (!ufile.isEmpty()) {
 					// //服务器上新建文件
					CommonsMultipartFile cf = (CommonsMultipartFile) ufile;
					DiskFileItem fi = (DiskFileItem) cf.getFileItem();
					File file = fi.getStoreLocation();
					
					Map fileUpload = HttpUtil.fileUpload(file, ufile.getOriginalFilename(), width, height, Constants.UPLOADURL);
					if (fileUpload == null || fileUpload.isEmpty()) {
						json.put("error", 1);
						json.put("message", "图片上传异常");
						return json.toJSONString();
					}
					if (fileUpload.get("result").equals("1000")) {
						json.put("error", 0);
						json.put("url", fileUpload.get("picurl"));
						json.put("orgurl", fileUpload.get("orgurl"));
					} else {
						json.put("error", 1);
						json.put("message", fileUpload.get("msg"));
					}
				}
			}
			return json.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	public void uploadFile(@RequestParam MultipartFile[] videos) {
//		try {
//			JSONObject json = new JSONObject();
//			// 获取Struts2中的多媒体请求包装器对象
//			MultiPartRequestWrapper requestWrapper = (MultiPartRequestWrapper) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//			// 文件名字符串数组
//			String[] fileNames = requestWrapper.getFileNames("video");
//			// 文件数组
//			File[] files = requestWrapper.getFiles("video");
//			String type = getReq().getParameter("type");
//
//			if (!ServletFileUpload.isMultipartContent(getReq())) {
//				json.put("error", 1);
//				json.put("message", "请选择文件");
//				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
//				json.write(ServletActionContext.getResponse().getWriter());
//				return;
//			}
//
//			String url = Constants.UPLOADFILEURL;
//			for (int i = 0; i < files.length; i++) {
//				// 0:语音，1:视频,2:apk
//				Map fileUpload = HttpUtil.fileUpload(files[i], fileNames[i], url, type);
//				if (fileUpload == null || fileUpload.isEmpty()) {
//					json.put("error", 1);
//					json.put("message", "文件上传异常");
//					ServletActionContext.getResponse().setCharacterEncoding("utf-8");
//					json.write(ServletActionContext.getResponse().getWriter());
//					return;
//				}
//				if (fileUpload.get("result").equals("1000")) {
//					json.put("error", 0);
//					json.put("url", fileUpload.get("web_url"));
//				} else {
//					json.put("error", 1);
//					json.put("message", fileUpload.get("msg"));
//				}
//
//				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
//				json.write(ServletActionContext.getResponse().getWriter());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
}
