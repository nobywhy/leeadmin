package com.lee.common.utils;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;



/**
 * 图片服务管理
 * @author liuxr
 *
 */
public class PicServiceManage {
	protected static Logger imgLog = Logger.getLogger("ImgLog");
	static String mempath;
	static String hiswebpaths[];
	static String hisdiskpaths[];
	static int maxwidth=0;
	static int maxheight=0;
	static String curwebpath; //当前新增图片的web路径
	static String curdiskpath;//当前新增图片的物理地址
	public static String key;
	
	static boolean inited = false;
	static boolean islinux = true;
	private static String file_suffix_inlcude;
	
	static int my_comments_save;
	static int my_comments_width_default; 
	static int my_comments_height_default; 
	public static String my_comments_image_suffix; 
	
	static int freeshoot_comments_save; 
	static int freeshoot_comments_width_default; 
	static int freeshoot_comments_height_default; 
	public static String freeshoot_comments_image_suffix; 
	
	static int freeshoot_list_save; 
	static int freeshoot_list_width_default; 
	static int freeshoot_list_height_default; 
	public static String freeshoot_list_image_suffix; 
	
	private static String defaultUserpicHttpUrl;
	
	static{
		Element root = null;
 		SAXBuilder builder = new SAXBuilder();
		Document doc;
		try {
			doc = builder.build(PicServiceManage.class.getResourceAsStream("/pictconfig.xml"));
			root = doc.getRootElement();
			//2013-7-10 freeshoot start
			Element freeshoot = root.getChild("freeshoot");
			try {
				my_comments_save=Integer.parseInt(freeshoot.getAttributeValue("my_comments_save"));
				my_comments_width_default=Integer.parseInt(freeshoot.getAttributeValue("my_comments_width_default"));
				my_comments_height_default=Integer.parseInt(freeshoot.getAttributeValue("my_comments_height_default")); 
				my_comments_image_suffix=freeshoot.getAttributeValue("my_comments_image_suffix"); 
				
				freeshoot_comments_save=Integer.parseInt(freeshoot.getAttributeValue("freeshoot_comments_save")); 
				freeshoot_comments_width_default=Integer.parseInt(freeshoot.getAttributeValue("freeshoot_comments_width_default")); 
				freeshoot_comments_height_default=Integer.parseInt(freeshoot.getAttributeValue("freeshoot_comments_height_default")); 
				freeshoot_comments_image_suffix=freeshoot.getAttributeValue("freeshoot_comments_image_suffix"); 
				
				freeshoot_list_save=Integer.parseInt(freeshoot.getAttributeValue("freeshoot_list_save")); 
				freeshoot_list_width_default=Integer.parseInt(freeshoot.getAttributeValue("freeshoot_list_width_default")); 
				freeshoot_list_height_default=Integer.parseInt(freeshoot.getAttributeValue("freeshoot_list_height_default")); 
				freeshoot_list_image_suffix=freeshoot.getAttributeValue("freeshoot_list_image_suffix"); 
			} catch (Exception e) {
				
			}
			//freeshoot end
			//single photo start
			Element global = root.getChild("global");
			key=global.getAttributeValue("key");
			defaultUserpicHttpUrl=global.getAttributeValue("defaultUserpicHttpUrl");
			try {
				maxwidth = Integer.parseInt(global.getAttributeValue("maxwidth"));
				maxheight = Integer.parseInt(global.getAttributeValue("maxheight"));
			} catch (Exception e) {
			}
			if (maxwidth <= 0)
				maxwidth = 760;
			if (maxheight <0)
				maxheight = 0;
			try {
				if("windows".equalsIgnoreCase(global.getAttributeValue("os"))) {
					islinux = false;
				} else {
					islinux = true;				
				}
			} catch (Exception e) {
				islinux = true;
			}
			if(islinux) {
				mempath = global.getAttributeValue("tmpfilepath");
			}
			file_suffix_inlcude=global.getAttributeValue("file_suffix_inlcude");
			String path_mapping = global.getAttributeValue("path_mapping");
			
			List<Element> hispathmaps = root.getChild("webpathmaps").getChildren("node");
			if (hispathmaps != null && hispathmaps.size() > 0) {
				hiswebpaths = new String[hispathmaps.size()];
				hisdiskpaths = new String[hispathmaps.size()];
				int k = -1;
				int j=0;
				for (Element e : hispathmaps) {
					k++;
					hiswebpaths[k] = e.getAttributeValue("webpath");
					hisdiskpaths[k] = e.getAttributeValue("diskpath");
					if(j==0 && path_mapping.equalsIgnoreCase(e.getAttributeValue("name"))){
						j++;
						curwebpath = hiswebpaths[k];
						curdiskpath= hisdiskpaths[k];
					}
				}
			}
		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * 功能:保存单张图片,供接口调用,会删除参数file
	 * @param file
	 * @return 
	 *  	web_url,width,height
	 * @throws Exception
	 */
	public static Map saveSinglePicture(File file, String imgname, Map compressSizeMap,boolean deleteFile) throws Exception {
		if(file == null) 
			return null;
		String type = getImgType(imgname);
		try {
			return savePict(file, type, compressSizeMap);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(deleteFile){
					file.delete();
				}
			} catch (Exception e){};
		}		
	}
	/**
	 * 功能:保存原图,压缩图片.不会删除参数file.
	 * @param file 原图
	 * @param type 图片类型
	 * @param compressSizeMap 压缩尺寸
	 * @return
	 * 		web_url,width,height
	 * @throws Exception
	 */
	private static Map savePict(File file, String type,Map compressSizeMap) throws Exception {
		try {
			//获取编码后的文件名
			String realname = getFileName(type);
			//硬盘真实路径
			String diskfile_url = curdiskpath + realname;
			File diskfile=new File(diskfile_url);
			diskfile.getParentFile().mkdirs();
			//原图
			String orgfile_url = curdiskpath + realname.replace(".", "_org.");
			File orgfile=new File(orgfile_url);
			FileUtil.copyFile(file, orgfile); //保存原图
			return singlePic(compressSizeMap,orgfile,realname);
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 单图业务
	 * @param file
	 * @param type
	 * @param compressSizeMap
	 * @return
	 */
	private static Map singlePic(Map compressSizeMap,File orgfile, String realname) throws Exception{
		Map resultMap=new HashMap();
		BufferedImage bi = ImageIO.read(orgfile);
		if(true){
			int[] req_size=(int[])compressSizeMap.get(PicServiceManage.key);
			req_size[0]=req_size[0]==-1?maxwidth:req_size[0];//width
			req_size[1]=req_size[1]==-1?maxheight:req_size[1];
			String disk_url=curdiskpath+realname;
			Map tempMap=compress2saveFile("70%",req_size[0],req_size[1],orgfile,disk_url,bi);
			resultMap.put("web_url", curwebpath+realname);
			if(tempMap!=null){
				resultMap.put("width", tempMap.get("width"));
				resultMap.put("height", tempMap.get("height"));
			}
		}
		bi=null;
		return resultMap;
	}
	/*public static String[] savePict(File file, String type,Map compressSizeMap) throws Exception {
		if(mwidth > maxwidth) mwidth = maxwidth;
		BufferedImage bi = null;
		try {
			String realname = getFileName(type);
			
			String diskfilename = curdiskpath + realname;
			String orgname = diskfilename.replace("." + type, "_org." + type);
			System.out.println("diskfilename="+diskfilename+",curwebpath="+curwebpath);
			new File(diskfilename).getParentFile().mkdirs();
			bi = ImageIO.read(file);
			System.out.println("file="+file+",filename="+file.getName()+",filepath="+file.getPath()+",fileCanonicalPath="+file.getCanonicalPath()+",type="+type+",bi="+bi);
			
			double rate = mwidth  * 1.0/ bi.getWidth();
			double rateh = maxheight  * 1.0/ bi.getHeight();
			int width = 0;
			int height = 0;
			if ((rate > 0.0 && rate < 1.0) || (rateh > 0.0 && rateh < 1.0)) {
				if (rate > rateh && rateh > 0.0) {										
					rate = rateh;
				}
				height = (int)(bi.getHeight() * rate);
				width = (int) (bi.getWidth() * rate);
			} else {
				rate = 0f;
			}
			File tmpfile = new File(orgname);
			FileUtil.copyFile(file, new File(orgname)); //保存原图
			if (rate > 0.0) {// 需要压缩
				if(islinux) {	
					try {
						String cmd = "convert +profile \"*\" -resize " + width + "x" + height +" " + orgname + " " + diskfilename;
						System.out.println("cmd:" + cmd);
						Process proc = Runtime.getRuntime().exec(cmd);
						proc.waitFor() ;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
//						try {
//							tmpfile.delete();
//						} catch (Exception e){};
					}
				} else {
					Image Itemp = bi.getScaledInstance(width, height,
							BufferedImage.SCALE_SMOOTH);
					AffineTransformOp op = new AffineTransformOp(AffineTransform
							.getScaleInstance(rate, rate), null);
					Itemp = op.filter(bi, null);
					File newfile = new File(diskfilename);
					ImageIO.write((BufferedImage) Itemp, type, newfile);
				}
			} else {// 不需要压缩
				FileUtil.copyFile(file, new File(diskfilename)); 
			}
			String ret[] = new String[2];
			ret[0] = curwebpath + realname;
			ret[1] = diskfilename;
			if(islinux) {
				try {
					String cmd = "convert +profile \"*\" -quality 70 " + orgname + " " + orgname;
					Process proc = Runtime.getRuntime().exec(cmd);
					proc.waitFor() ;
				} catch (Exception e) {
					e.printStackTrace();
				} 					
			}
			return ret;
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		} finally {
			bi = null;
		}
	}*/
	/**
	 * 存储随拍图片,供接口调用,会删除参数file
	 * @param file
	 * @param imgname
	 * @param paramMap
	 * @return
	 * 		 web_url,width,height
	 * @throws Exception
	 */
	public static Map savePicture2(File file, String imgname, Map paramMap) throws Exception {
		if(file == null) 
			return null;
		String type = getImgType(imgname);
		try {
			return savePict2(file, type, paramMap);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				file.delete();
			} catch (Exception e){};
		}		
	}
	private static double compute_rate(int max_width,int max_height,int source_width,int source_height){
		double rate = max_width  * 1.0/ source_width;
		double rateh = max_height  * 1.0/ source_height;
		if ((rate > 0.0 && rate < 1.0) || (rateh > 0.0 && rateh < 1.0)) {
			//等比例压缩
			if(rate > rateh && rateh > 0.0){
				rate = rateh;
			}
			if(rate < rateh){
				if(rate==0.0)
					rate=rateh;
			}
			return rate;
		}
		return 0.0;
	}
	/**
	 * 正常发随拍,不会删除参数file
	 * @param file
	 * @param type
	 * @param paramMap
	 * @return
	 *     web_url,width,height
	 * @throws Exception
	 */
	public static Map savePict2(File file, String type,Map paramMap) throws Exception {
		try {
			//获取编码后的文件名
			String realname = getFileName(type);
			//硬盘真实路径
			String diskfile_url = curdiskpath + realname;
			File diskfile=new File(diskfile_url);
			diskfile.getParentFile().mkdirs();
			//原图
			String orgfile_url = curdiskpath + realname.replace(".", "_org.");
			File orgfile=new File(orgfile_url);
			FileUtil.copyFile(file, orgfile); //保存原图
			return suipai(paramMap,orgfile,realname);
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 目前异步发随拍用到过,不会关闭参数inputstream
	 * @param file
	 * @param type
	 * @param paramMap
	 * @return
	 *     web_url,width,height
	 * @throws Exception
	 */
	public static Map savePict3(InputStream in,String type,Map paramMap) throws Exception {
		try {
			//获取编码后的文件名
			String realname = getFileName(type);
			//硬盘真实路径
			String diskfile_url = curdiskpath + realname;
			File diskfile=new File(diskfile_url);
			diskfile.getParentFile().mkdirs();
			//原图
			String orgfile_url = curdiskpath + realname.replace(".", "_org.");
			//流不要关闭
			FileUtil.copyFile(in, new File(orgfile_url)); //保存原图
			File orgfile=new File(orgfile_url);
			return suipai(paramMap,orgfile,realname);
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 随拍业务
	 * @param paramMap
	 * @param orgfile
	 * @param realname
	 * @return
	 * @throws Exception
	 */
	private static Map suipai(Map paramMap,File orgfile,String realname) throws Exception{
		Map resultMap=new HashMap();
		/*
		 * 终端做过处理,这个70%暂时不要
		 * if(islinux) {	
			String cmd = "convert -quality 70% " + orgfile.getPath() + " " + orgfile.getPath();
			Process proc = Runtime.getRuntime().exec(cmd);
			proc.waitFor() ;
		}*/
		BufferedImage bi = ImageIO.read(orgfile);
		//my_comments
		if(my_comments_save==1){
			int[] req_size=(int[])paramMap.get(PicServiceManage.my_comments_image_suffix);
			req_size[0]=req_size[0]==-1?my_comments_width_default:req_size[0];
			req_size[1]=req_size[1]==-1?my_comments_height_default:req_size[1];
			String mc_name=realname.replace(".", "_"+PicServiceManage.my_comments_image_suffix+".");
			String mc_disk_url=curdiskpath+mc_name;
			compress2saveFile("60%",req_size[0],req_size[1],orgfile,mc_disk_url,bi);
			/*if(map!=null && map.size()>0){
				map.put("web_url", curwebpath+mc_name);
				map.put("disk_url", mc_disk_url);
				resultMap.put(PicServiceManage.my_comments_image_suffix, map);
			}*/
		}
		//freeshoot_comments
		if(freeshoot_comments_save==1){
			int[] req_size=(int[])paramMap.get(PicServiceManage.freeshoot_comments_image_suffix);
			req_size[0]=req_size[0]==-1?freeshoot_comments_width_default:req_size[0];
			req_size[1]=req_size[1]==-1?freeshoot_comments_height_default:req_size[1];
			String fc_name=realname.replace(".", "_"+PicServiceManage.freeshoot_comments_image_suffix+".");
			String fc_disk_url=curdiskpath+fc_name;
			//90% 改为80%
			compress2saveFile("80%",req_size[0],req_size[1],orgfile,fc_disk_url,bi);
			/*if(map!=null && map.size()>0){
				map.put("web_url", curwebpath+fc_name);
				map.put("disk_url", fc_disk_url);
				resultMap.put(PicServiceManage.freeshoot_comments_image_suffix, map);
			}*/
		}
		//freeshoot_list
		if(freeshoot_list_save==1){
			int[] req_size=(int[])paramMap.get(PicServiceManage.freeshoot_list_image_suffix);
			req_size[0]=req_size[0]==-1?freeshoot_list_width_default:req_size[0];
			req_size[1]=req_size[1]==-1?freeshoot_list_width_default:req_size[1];
			//String fl_name=realname.replace("." + type, "_"+PicServiceManage.freeshoot_list_image_suffix+"." + type);
			String fl_disk_url=curdiskpath+realname;
			Map tempMap=compress2saveFile("60%",req_size[0],req_size[1],orgfile,fl_disk_url,bi);
			/*if(map!=null && map.size()>0){
				map.put("web_url", curwebpath+realname);
				map.put("disk_url", fl_disk_url);
				resultMap.put(PicServiceManage.freeshoot_list_image_suffix, map);
			}*/
			if(tempMap!=null){
				resultMap.put("web_url", curwebpath+realname);
				resultMap.put("width", tempMap.get("width"));
				resultMap.put("height", tempMap.get("height"));
			}
		}
		bi=null;
		return resultMap;
	}
	private static Map compress2saveFile(String compress,int width,int height,File orgfile,String diskfile_url,BufferedImage bi){
		Map resultMap=new HashMap();
		try {
			//计算压缩比例
			double rate=compute_rate(width,height,bi.getWidth(),bi.getHeight());
			int _width=bi.getWidth();
			int _height=bi.getHeight();
			if (rate > 0.0 || orgfile.length()>100000) {// 需要压缩
				if(rate > 0.0){
					_width=(int)(_width*rate);
					_height=(int)(_height*rate);
				}
				try {
					if(islinux) {	
						String cmd = "convert -quality "+compress+" +profile \"*\" -resize " + _width + "x" + _height +" " + orgfile.getPath() + " " + diskfile_url;
						Process proc = Runtime.getRuntime().exec(cmd);
						proc.waitFor() ;
					} else {
						Image Itemp = bi.getScaledInstance(_width, _height,
								BufferedImage.SCALE_SMOOTH);
						AffineTransformOp op = new AffineTransformOp(AffineTransform
								.getScaleInstance(rate, rate), null);
						Itemp = op.filter(bi, null);
						File newfile = new File(diskfile_url);
						ImageIO.write((BufferedImage) Itemp, diskfile_url.substring(diskfile_url.indexOf(".")+1), newfile);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}else{
				FileUtil.copyFile(orgfile, new File(diskfile_url)); 
			}
			resultMap.put("width", _width);
			resultMap.put("height", _height);
		} catch (Exception e) {
			return null;
		}
		return resultMap;
	}
	public static java.text.SimpleDateFormat df = new SimpleDateFormat(
			"yyyy/MM/dd/HH");

	public static String getFileName(String filetype) {
		return df.format(new Date()) + "/pic_" + System.currentTimeMillis()
				+ "_" + UUID.randomUUID().toString() + "." + filetype;
	}
	public static String getSimpleFileName(String filetype) {
		return "pic_" + System.currentTimeMillis()
		+ "_" + UUID.randomUUID().toString() + "." + filetype;
	}
	/**
	 * 根据WEB路径删除文件
	 * @param webpath
	 */
	public static void deleteFileByWebpath(String webpath) {
		if(FLUtfil.isHasLen(webpath)) return;
		String diskpath = getDiskPathByWebpath(webpath);
		if(diskpath != null) {
			try {
				new File(diskpath).delete();
			} catch (Exception e){
				e.printStackTrace();
			};
		}
	}
	/**
	 * 根据WEB路径删除文件  批量
	 * @param webpaths 比如： http://xxx.jpg,http://www.xx.jpg 格式
	 */
	public static void deleteFileByWebpathArray(String webpaths) {
		if(FLUtfil.isHasLen(webpaths)) return;
		String[] sa=webpaths.split(",");
		for(String img : sa){
			if(FLUtfil.isHasLen(img)) continue;
			deleteFileByWebpath(img);
		}
	}
	/**
	 * 根据图片web地址返回物理地址
	 * @param webpath
	 * @return
	 */
	public static String getDiskPathByWebpath(String webpath) {		
		if(webpath == null || webpath.trim().length() == 0) return null;
		for (int i = 0; i < hiswebpaths.length; i++) {
			if (webpath.indexOf(hiswebpaths[i]) == 0) {
				String ret = webpath.replace(hiswebpaths[i], hisdiskpaths[i]);
				//System.out.println("web->disk, in hismap:" + webpath + "==" + ret);
				return ret; 
			}
		}
		//System.out.println("not found, " + webpath);
		return null;
	}
	/**
	 * 根据图片物理地址返回web地址
	 * @param webpath
	 * @return
	 */
	public static String getWebpathByDiskPath(String diskpath) {		
		if(StringUtil.isBlank(diskpath)) 
			return null;
		for (int i = 0; i < hisdiskpaths.length; i++) {
			if (diskpath.indexOf(hisdiskpaths[i]) == 0) {
				String ret = diskpath.replace(hisdiskpaths[i],hiswebpaths[i]);
				//System.out.println("disk->web, in hismap:" + diskpath + "==" + ret);
				return ret; 
			}
		}
		//System.out.println("not found, " + diskpath);
		return null;
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
		if (file_suffix_inlcude.indexOf(type)<0) {
			type = "jpg";
		}
		return type;
	}
	/**
	 * 拷贝地址内容,返回新web地址
	 * @param web_url
	 * @return
	 */
	public static String getNewWebUrl4Url(String web_url) throws Exception{
		if(StringUtil.isBlank(web_url)){
			return null;
		}
		String disk_url=getDiskPathByWebpath(web_url);
		if(disk_url==null)
			return null;
		File disk_file=new File(disk_url);
		if(!disk_file.exists()){
			return null;
		}
		String type=disk_url.substring(disk_url.lastIndexOf(".")+1,disk_url.length());
		String new_disk_file=disk_file.getParent()+"/"+getSimpleFileName(type);
		FileUtil.copyFile(disk_file, new File(new_disk_file));
		return getWebpathByDiskPath(new_disk_file);
	}
	/**
	 * 拷贝图片至头像_业务
	 * @param source_web_url 能取到原图的web地址
	 * @param to_web_url 普通web地址(非原图,非小图)
	 * @param width 最大缩略图宽度
	 * @param height 最大高度
	 * @return
	 */
	public static boolean copyHeadPhoto(String source_web_url,String to_web_url,int maxWidth,int maxHeight) throws Exception{
		if(StringUtil.isBlank(source_web_url) || StringUtil.isBlank(to_web_url)){
			return false;
		}
		//获取源硬盘地址
		String source_disk_url=getDiskPathByWebpath(source_web_url);
		if(source_disk_url==null){
			return false;
		}
		//获取目标硬盘地址
		String to_disk_url=getDiskPathByWebpath(to_web_url);
		if(to_disk_url==null){
			return false;
		}
		//源文件 是否存在
		File source_disk_file=new File(source_disk_url);
		if(!source_disk_file.exists()){
			return false;
		}
		//源org文件,地址
		int index_sdu=source_disk_url.lastIndexOf(".");
		String source_disk_url_org=source_disk_url.substring(0,index_sdu)+"_org."+source_disk_url.substring(index_sdu+1);
		File source_disk_file_org=new File(source_disk_url_org);
		//目标org地址
		int index=to_disk_url.lastIndexOf(".");
		String to_disk_url_org=to_disk_url.substring(0, index)+"_org."+to_disk_url.substring(index+1);//生成目标org地址;
		File to_disk_file_org=new File(to_disk_url_org);
		/*
		 * 拷贝图片 start
		 */
		File to_disk_file=new File(to_disk_url);
		to_disk_file.getParentFile().mkdirs();//目标硬盘地址生成
		
		FileUtil.copyFile(source_disk_file,to_disk_file);//1 源文件拷贝
		//源org文件是否存在
		if(source_disk_file_org.exists()){
			//存在即拷贝
			FileUtil.copyFile(source_disk_file_org,to_disk_file_org);//2 org文件拷贝
		}else{
			to_disk_file_org.createNewFile();
		}
		String to_disk_url_8080=to_disk_url.substring(0, index)+"_small."+to_disk_url.substring(index+1);
		compress2saveFile("80%",maxWidth,maxHeight,to_disk_file,to_disk_url_8080,ImageIO.read(to_disk_file));//3 缩略图 80,80
		/*
		 * 拷贝图片 end
		 */
		return true;
	}
	/**
	 * 删除头像(用默认图片地址代替),清理头像时间戳
	 * @param userid 用户userid
	 * @return
	 */
	public static boolean deleteHeadPhoto(String userid) throws Exception{
		if(StringUtil.isBlank(defaultUserpicHttpUrl) || StringUtil.isBlank(userid)){
			return false;
		}
		//获取源硬盘地址
		String source_disk_url=getDiskPathByWebpath(defaultUserpicHttpUrl);
		if(source_disk_url==null){
			return false;
		}
		//源文件 是否存在
		File source_disk_file=new File(source_disk_url);
		if(!source_disk_file.exists()){
			return false;
		}
		//获取目标硬盘地址
		String userpicHttpUrl=getHeadPhotoWebUrl4Userid(Long.valueOf(userid), null);
		String to_disk_url=getDiskPathByWebpath(userpicHttpUrl);
		if(to_disk_url==null){
			return false;
		}
		/*
		 * 拷贝图片 start
		 */
		File to_disk_file=new File(to_disk_url);
		to_disk_file.getParentFile().mkdirs();//目标硬盘地址生成
		FileUtil.copyFile(source_disk_file,to_disk_file);//1 源文件拷贝
		//源org文件地址,small文件地址
		int index_sdu=source_disk_url.lastIndexOf(".");
		String source_disk_url_org=source_disk_url.substring(0,index_sdu)+"_org."+source_disk_url.substring(index_sdu+1);
		File source_disk_file_org=new File(source_disk_url_org);
		String source_disk_url_small=source_disk_url.substring(0,index_sdu)+"_small."+source_disk_url.substring(index_sdu+1);
		File source_disk_file_small=new File(source_disk_url_small);
		//目标org地址,small地址
		int index=to_disk_url.lastIndexOf(".");
		String to_disk_url_org=to_disk_url.substring(0, index)+"_org."+to_disk_url.substring(index+1);//生成目标org地址;
		File to_disk_file_org=new File(to_disk_url_org);
		String to_disk_url_small=to_disk_url.substring(0, index)+"_small."+to_disk_url.substring(index+1);//生成目标small地址;
		File to_disk_file_small=new File(to_disk_url_small);
		
		//源org文件是否存在
		if(source_disk_file_org.exists()){
			//存在即拷贝
			FileUtil.copyFile(source_disk_file_org,to_disk_file_org);//2 org文件拷贝
		}else{
			to_disk_file_org.createNewFile();
		}
		//源small文件是否存在
		if(source_disk_file_small.exists()){
			//存在即拷贝
			FileUtil.copyFile(source_disk_file_small,to_disk_file_small);//2 small文件拷贝
		}else{
			to_disk_file_small.createNewFile();
		}
		//updateUserTimestamp(userid);//更新时间戳
		/*
		 * 拷贝图片 end
		 */
		return true;
	}
	public static String getHeadPhotoWebUrl4Userid(long userid,String type){
		int disk=(int)userid/1000000000;
		String st="00000000000"+userid;
		String one=st.substring(st.length()-9,st.length()-6);
		String two=st.substring(st.length()-6,st.length()-3);
		String three=st.substring(st.length()-3,st.length());
		//头像 类型全部限定jpg
		return curwebpath+disk+"/"+one+"/"+two+"/"+three+".jpg";
//		return curwebpath+disk+"/"+one+"/"+two+"/"+three+"."+type;
	}
	//用户时间戳的缓存key
	public static String user_timestamp="user_timestamp";

//	public static String getHeadPUrl4Time(String userid){
//		String user_tt=CacheManger.get(userid+":"+user_timestamp);
//		if(StringUtil.isBlank(user_tt)){
//			user_tt=""+System.currentTimeMillis();
//			CacheManger.set(userid+":"+user_timestamp, user_tt);
//		}
//		return getHeadPhotoWebUrl4Userid(Long.valueOf(userid), null)+"?"+user_tt;
//	}
//	public static void updateUserTimestamp(String userid){
//		CacheManger.set(userid+":"+user_timestamp, ""+System.currentTimeMillis());
//	}
	public static int[] analyzeParam(String param){
		int[] result={-1,-1};
		if(param==null)
			return result;
		String[] params=param.split(",");
		if(params.length==2){
			try {
				result[0]=Integer.valueOf(params[0]);
				result[1]=Integer.valueOf(params[1]);
				if(result[0]<=0)
					result[0]=-1;
				if(result[1]<=0)
					result[1]=-1;
			} catch (Exception e) {
				result[0]=-1;
				result[1]=-1;
			}
		}
		return result;
	}
	/**
	 * 拷贝图片至头像_业务
	 * @param source_web_url 源web地址
	 * @param to_web_url 目标web地址(非原图,非小图)
	 * @param width  small图的最大宽度
	 * @param height small图的最大高度
	 * @return
	 */
	public static boolean copyHeadPhotoFromWebUrl(String source_web_url,String to_web_url,int maxWidth,int maxHeight) throws Exception{
		if(StringUtil.isBlank(source_web_url) || StringUtil.isBlank(to_web_url)){
			return false;
		}
		//获取目标硬盘地址
		String to_disk_url=getDiskPathByWebpath(to_web_url);
		if(to_disk_url==null){
			return false;
		}
		//获取源文件
		InputStream in=null;
		try {
			//36K  180
//				String st="http://124.160.105.98/d0/photos/2013/08/02/13/pic_1375422498377_d4b5b6f5-7354-4ef0-924c-487256bc34ad.jpg";
			//5.8M 760
//				String st="http://124.160.105.98/d0/photos/2013/08/02/13/pic_1375423139980_adaf9a5e-6d45-43ca-bc30-7e0cb41ce851_org_201382.jpg";
			//1.3M 550
//				String st="http://124.160.105.98/d0/photos/2013/08/02/13/pic_1375423139980_adaf9a5e-6d45-43ca-bc30-7e0cb41ce851_org.jpg";
			long start=System.currentTimeMillis();
			URL url=new URL(source_web_url);
			HttpURLConnection huc=(HttpURLConnection) url.openConnection();
			//设置超时
			huc.setConnectTimeout(10000);//10秒链接超时
			huc.setReadTimeout(30000);//30秒读取数据超时
			huc.connect();
			if(huc.getResponseCode()!=HttpURLConnection.HTTP_OK){
				imgLog.info("头像拷贝:"+source_web_url+"链接不成功");
				return false;
			}
			long filesize=huc.getContentLength();
			//超过10M
			if(filesize>10000000){
				imgLog.info("头像拷贝:图片大小"+source_web_url+"超过10M("+filesize+")");
				return false;
			}
			//处理图片
			in=huc.getInputStream();
			imgLog.info("头像拷贝:链接时间"+(System.currentTimeMillis()-start));
			//1: 目标org地址生成
			File to_disk_file=new File(to_disk_url);
			to_disk_file.getParentFile().mkdirs();//目标硬盘路径生成
			
			int index=to_disk_url.lastIndexOf(".");
			String to_disk_url_org=to_disk_url.substring(0, index)+"_org."+to_disk_url.substring(index+1);//生成目标org地址;
			File to_disk_file_org=new File(to_disk_url_org);
			//拷贝3种图片
			FileUtil.copyFile(in,to_disk_file_org);//1 org文件拷贝
			FileUtil.copyFile(to_disk_file_org,to_disk_file);//2 使用地址拷贝(秀点图片系统中,不带任何后缀的地址)
			//small图片
			String to_disk_url_8080=to_disk_url.substring(0, index)+"_small."+to_disk_url.substring(index+1);
			compress2saveFile("80%",maxWidth,maxHeight,to_disk_file,to_disk_url_8080,ImageIO.read(to_disk_file));//3 缩略图 80,80
		} catch (Exception e) {
			imgLog.info("头像拷贝:异常_"+e.getMessage());
			e.printStackTrace();
			return false;
		}finally{
			try {
				if(in!=null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	public static void main(String []args){
//		String weburl = "http://localhost:7777/yt/uploadfile/2013/05/29/17/pic_1369821384180_e0745150-cc3c-4866-bc30-5e94fe010146_org.jpg";
//		deleteFileByWebpath(weburl);
//		String weburl="http://localhost:8080/badscorejob/uploadfile/pics/2013/07/10/16/pic_1373445175149_88a0fef4-7eb2-4671-897a-d4767c54dcb6_mc.jpg";
//		String tourl="http://localhost:8080/badscorejob/uploadfile/pics/2013/07/10/16/pic_aaaa.jpg";
//		System.out.println(getNewWebUrl4Url(weburl));
//		System.out.println(copyFile(weburl, tourl, 0));
//		String type="jpg";
//		System.out.println(getHeadPhotoWebUrl4Userid(100, type));
		String userpic=PicServiceManage.getHeadPhotoWebUrl4Userid(100082, null);
		try {
			PicServiceManage.copyHeadPhotoFromWebUrl("http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/30", userpic, 100, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
