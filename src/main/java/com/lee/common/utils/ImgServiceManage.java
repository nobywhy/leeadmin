package com.lee.common.utils;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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


/***
 * 所有图片处理
 * 
 * @author dlf
 *
 */
public class ImgServiceManage {
	protected static Logger imgLog = Logger.getLogger("ImgLog");
	static String mempath;
	static String hiswebpaths[];
	static String hisdiskpaths[];
	static int maxwidth = 0;
	static int maxheight = 0;
	static String curwebpath; // 当前新增图片的web路径
	static String curdiskpath;// 当前新增图片的物理地址
	// static String voidewebpath; //当前新增图片的web路径
	// static String voidediskpath;//当前新增图片的物理地址
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

	static {
		Element root = null;
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		try {
			doc = builder.build(PicServiceManage.class.getResourceAsStream("/pictconfig.xml"));
			root = doc.getRootElement();

			// single photo start
			Element global = root.getChild("global");
			key = global.getAttributeValue("key");
			defaultUserpicHttpUrl = global.getAttributeValue("defaultUserpicHttpUrl");
			try {
				maxwidth = Integer.parseInt(global.getAttributeValue("maxwidth"));
				maxheight = Integer.parseInt(global.getAttributeValue("maxheight"));
			} catch (Exception e) {
			}
			if (maxwidth <= 0)
				maxwidth = 760;
			if (maxheight < 0)
				maxheight = 0;
			try {
				if ("windows".equalsIgnoreCase(global.getAttributeValue("os"))) {
					islinux = false;
				} else {
					islinux = true;
				}
			} catch (Exception e) {
				islinux = true;
			}
			if (islinux) {
				mempath = global.getAttributeValue("tmpfilepath");
			}
			file_suffix_inlcude = global.getAttributeValue("file_suffix_inlcude");
			String path_mapping = global.getAttributeValue("path_mapping");

			List<Element> hispathmaps = root.getChild("webpathmaps").getChildren("node");
			if (hispathmaps != null && hispathmaps.size() > 0) {
				hiswebpaths = new String[hispathmaps.size()];
				hisdiskpaths = new String[hispathmaps.size()];
				int k = -1;
				int j = 0;
				for (Element e : hispathmaps) {
					k++;
					hiswebpaths[k] = e.getAttributeValue("webpath");
					hisdiskpaths[k] = e.getAttributeValue("diskpath");
					if (j == 0 && path_mapping.equalsIgnoreCase(e.getAttributeValue("name"))) {
						j++;
						curwebpath = hiswebpaths[k];
						curdiskpath = hisdiskpaths[k];
					}
					// if(j==1 &&
					// path_mapping.equalsIgnoreCase(e.getAttributeValue("name"))){
					// voidewebpath = hiswebpaths[k];
					// voidediskpath = hisdiskpaths[k];
					// System.out.println(voidewebpath+"---------------"+voidediskpath);
					// }
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

	public static Map saveFile(File file, String filename, boolean deleteFile, String dispath) {
		if (file == null)
			return null;
		String type = getVideo(filename);
		Map resultMap = new HashMap();
		try {
			// 获取编码后的文件名
			String realname = getFileName(type);
			String diskfile_url = curdiskpath + dispath + realname;
			File diskfile = new File(diskfile_url);
			diskfile.getParentFile().mkdirs();

			FileUtil.copyFile(file, diskfile);
			resultMap.put("web_url", curwebpath + dispath + realname);
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (deleteFile) {
					file.delete();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 功能:保存单张图片,供接口调用,会删除参数file
	 * 
	 * @param file
	 * @return web_url,width,height
	 * @throws Exception
	 */
	public static Map saveSinglePicture(File file, String imgname, Map compressSizeMap, boolean deleteFile) throws Exception {
		if (file == null)
			return null;
		String type = getImgType(imgname);
		try {
			return savePict(file, type, compressSizeMap);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (deleteFile) {
					file.delete();
				}
			} catch (Exception e) {
			}

		}
	}

	/**
	 * 功能:保存原图,压缩图片.不会删除参数file.
	 * 
	 * @param file
	 *            原图
	 * @param type
	 *            图片类型
	 * @param compressSizeMap
	 *            压缩尺寸
	 * @return web_url,width,height
	 * @throws Exception
	 */
	private static Map savePict(File file, String type, Map compressSizeMap) throws Exception {
		try {
			// 获取编码后的文件名
			String realname = getFileName(type);
			// 硬盘真实路径
			String diskfile_url = curdiskpath + realname;
			File diskfile = new File(diskfile_url);
			diskfile.getParentFile().mkdirs();
			// 原图
			String orgfile_url = curdiskpath + realname.replace(".", "_org.");
			File orgfile = new File(orgfile_url);
			FileUtil.copyFile(file, orgfile); // 保存原图
			return singlePic(compressSizeMap, orgfile, realname);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 单图业务
	 * 
	 * @param file
	 * @param type
	 * @param compressSizeMap
	 * @return
	 */
	private static Map singlePic(Map compressSizeMap, File orgfile, String realname) throws Exception {
		Map resultMap = new HashMap();
		BufferedImage bi = ImageIO.read(orgfile);
		if (true) {
			int[] req_size = (int[]) compressSizeMap.get(PicServiceManage.key);
			req_size[0] = req_size[0] == -1 ? maxwidth : req_size[0];// width
			req_size[1] = req_size[1] == -1 ? maxheight : req_size[1];
			String disk_url = curdiskpath + realname;
			Map tempMap = compress2saveFile("70%", req_size[0], req_size[1], orgfile, disk_url, bi);
			resultMap.put("web_url", curwebpath + realname);
			resultMap.put("org_url", curwebpath + realname.replace(".", "_org."));
			if (tempMap != null) {
				resultMap.put("width", tempMap.get("width"));
				resultMap.put("height", tempMap.get("height"));
			}
		}
		bi = null;
		return resultMap;
	}

	private static Map compress2saveFile(String compress, int width, int height, File orgfile, String diskfile_url, BufferedImage bi) {
		Map resultMap = new HashMap();
		try {
			// 计算压缩比例
			double rate = compute_rate(width, height, bi.getWidth(), bi.getHeight());
			int _width = bi.getWidth();
			int _height = bi.getHeight();
			if (rate > 0.0 || orgfile.length() > 100000) {// 需要压缩
				if (rate > 0.0) {
					_width = (int) (_width * rate);
					_height = (int) (_height * rate);
				}
				try {
					if (islinux) {
						String cmd = "convert -quality " + compress + " +profile \"*\" -resize " + _width + "x" + _height + " " + orgfile.getPath() + " "
								+ diskfile_url;
						imgLog.info("cmd+" + cmd);
						Process proc = Runtime.getRuntime().exec(cmd);
						proc.waitFor();
					} else {
						// Image Itemp = bi.getScaledInstance(_width, _height,
						// BufferedImage.SCALE_SMOOTH);

						AffineTransform affineTransform = new AffineTransform();
						affineTransform.setToScale(_width, _height);

						BufferedImage outputImage = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
						Graphics2D gd2 = outputImage.createGraphics();
						gd2.drawImage(bi, affineTransform, null);
						gd2.dispose();

						File newfile = new File(diskfile_url);
						ImageIO.write(outputImage, diskfile_url.substring(diskfile_url.indexOf(".") + 1), newfile);

						// AffineTransformOp op = new
						// AffineTransformOp(AffineTransform
						// .getScaleInstance(rate, rate), null);
						// Itemp = op.filter(bi, null);
						//
						// ImageIO.write((BufferedImage) Itemp,
						// diskfile_url.substring(diskfile_url.indexOf(".")+1),
						// newfile);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			} else {
				FileUtil.copyFile(orgfile, new File(diskfile_url));
			}
			resultMap.put("width", _width);
			resultMap.put("height", _height);
		} catch (Exception e) {
			return null;
		}
		return resultMap;
	}

	/**
	 * 根据图片web地址返回物理地址
	 * 
	 * @param webpath
	 * @return
	 */
	public static String getDiskPathByWebpath(String webpath) {
		if (webpath == null || webpath.trim().length() == 0)
			return null;
		for (int i = 0; i < hiswebpaths.length; i++) {
			if (webpath.indexOf(hiswebpaths[i]) == 0) {
				String ret = webpath.replace(hiswebpaths[i], hisdiskpaths[i]);
				// System.out.println("web->disk, in hismap:" + webpath + "==" +
				// ret);
				return ret;
			}
		}
		// System.out.println("not found, " + webpath);
		return null;
	}

	private static double compute_rate(int max_width, int max_height, int source_width, int source_height) {
		double rate = max_width * 1.0 / source_width;
		double rateh = max_height * 1.0 / source_height;
		if ((rate > 0.0 && rate < 1.0) || (rateh > 0.0 && rateh < 1.0)) {
			// 等比例压缩
			if (rate > rateh && rateh > 0.0) {
				rate = rateh;
			}
			if (rate < rateh) {
				if (rate == 0.0)
					rate = rateh;
			}
			return rate;
		}
		return 0.0;
	}

	public static int[] analyzeParam(String param) {
		int[] result = { -1, -1 };
		if (param == null)
			return result;
		String[] params = param.split(",");
		if (params.length == 2) {
			try {
				result[0] = Integer.valueOf(params[0]);
				result[1] = Integer.valueOf(params[1]);
				if (result[0] <= 0)
					result[0] = -1;
				if (result[1] <= 0)
					result[1] = -1;
			} catch (Exception e) {
				result[0] = -1;
				result[1] = -1;
			}
		}
		return result;
	}

	/**
	 * 根据WEB路径删除文件
	 * 
	 * @param webpath
	 */
	public static void deleteFileByWebpath(String webpath) {
		if (FLUtfil.isHasLen(webpath))
			return;
		String diskpath = getDiskPathByWebpath(webpath);
		if (diskpath != null) {
			try {
				new File(diskpath).delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void deleteFileORGByWebpath(String webpath) {
		if (FLUtfil.isHasLen(webpath))
			return;
		String w = webpath.replace(curwebpath, "");
		w = w.replace(".", "_org.");
		w = curwebpath + w;
		String diskpath = getDiskPathByWebpath(w);
		if (diskpath != null) {
			try {
				new File(diskpath).delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String getHeadPhotoWebUrl4Userid(long userid, String type) {
		int disk = (int) userid / 1000000000;
		String st = "00000000000" + userid;
		String one = st.substring(st.length() - 9, st.length() - 6);
		String two = st.substring(st.length() - 6, st.length() - 3);
		String three = st.substring(st.length() - 3, st.length());
		// 头像 类型全部限定jpg
		return curwebpath + disk + "/" + one + "/" + two + "/" + three + ".jpg";
		// return curwebpath+disk+"/"+one+"/"+two+"/"+three+"."+type;
	}

	/**
	 * 拷贝图片至头像_业务
	 * 
	 * @param source_web_url
	 *            源web地址
	 * @param to_web_url
	 *            目标web地址(非原图,非小图)
	 * @param width
	 *            small图的最大宽度
	 * @param height
	 *            small图的最大高度
	 * @return
	 */
	public static boolean copyHeadPhotoFromWebUrl(String source_web_url, String to_web_url, int maxWidth, int maxHeight) throws Exception {
		if (StringUtil.isBlank(source_web_url) || StringUtil.isBlank(to_web_url)) {
			return false;
		}
		// 获取目标硬盘地址
		String to_disk_url = getDiskPathByWebpath(to_web_url);
		if (to_disk_url == null) {
			return false;
		}
		// 获取源文件
		InputStream in = null;
		try {
			// 36K 180
			// String
			// st="http://124.160.105.98/d0/photos/2013/08/02/13/pic_1375422498377_d4b5b6f5-7354-4ef0-924c-487256bc34ad.jpg";
			// 5.8M 760
			// String
			// st="http://124.160.105.98/d0/photos/2013/08/02/13/pic_1375423139980_adaf9a5e-6d45-43ca-bc30-7e0cb41ce851_org_201382.jpg";
			// 1.3M 550
			// String
			// st="http://124.160.105.98/d0/photos/2013/08/02/13/pic_1375423139980_adaf9a5e-6d45-43ca-bc30-7e0cb41ce851_org.jpg";
			long start = System.currentTimeMillis();
			URL url = new URL(source_web_url);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			// 设置超时
			huc.setConnectTimeout(10000);// 10秒链接超时
			huc.setReadTimeout(30000);// 30秒读取数据超时
			huc.connect();
			if (huc.getResponseCode() != HttpURLConnection.HTTP_OK) {
				imgLog.info("头像拷贝:" + source_web_url + "链接不成功");
				return false;
			}
			long filesize = huc.getContentLength();
			// 超过10M
			if (filesize > 10000000) {
				imgLog.info("头像拷贝:图片大小" + source_web_url + "超过10M(" + filesize + ")");
				return false;
			}
			// 处理图片
			in = huc.getInputStream();
			imgLog.info("头像拷贝:链接时间" + (System.currentTimeMillis() - start));
			// 1: 目标org地址生成
			File to_disk_file = new File(to_disk_url);
			to_disk_file.getParentFile().mkdirs();// 目标硬盘路径生成

			int index = to_disk_url.lastIndexOf(".");
			String to_disk_url_org = to_disk_url.substring(0, index) + "_org." + to_disk_url.substring(index + 1);// 生成目标org地址;
			File to_disk_file_org = new File(to_disk_url_org);
			// 拷贝3种图片
			FileUtil.copyFile(in, to_disk_file_org);// 1 org文件拷贝
			FileUtil.copyFile(to_disk_file_org, to_disk_file);// 2
																// 使用地址拷贝(秀点图片系统中,不带任何后缀的地址)
			// small图片
			String to_disk_url_8080 = to_disk_url.substring(0, index) + "_small." + to_disk_url.substring(index + 1);
			compress2saveFile("80%", maxWidth, maxHeight, to_disk_file, to_disk_url_8080, ImageIO.read(to_disk_file));// 3
																														// 缩略图
																														// 80,80
		} catch (Exception e) {
			imgLog.info("头像拷贝:异常_" + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 根据图片名字获取图片类型
	 * 
	 * @param imgurl
	 * @return
	 */
	public static String getImgType(String imgname) {
		String type = null;
		int idx = imgname.lastIndexOf(".");
		if (idx > 0)
			type = imgname.substring(idx + 1).toLowerCase();
		if (file_suffix_inlcude.indexOf(type) < 0) {
			type = "jpg";
		}
		return type;
	}

	public static String getVideo(String video) {
		String type = null;
		int idx = video.lastIndexOf(".");
		if (idx > 0)
			type = video.substring(idx + 1).toLowerCase();

		imgLog.info("type:" + type);

		if (file_suffix_inlcude.indexOf(type) < 0) {
			type = "mp4";
		}
		return type;
	}

	public static java.text.SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd/HH");

	public static String getFileName(String filetype) {
		return df.format(new Date()) + "/pic_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + "." + filetype;
	}

	public static String getFileNameByOpenid(String openid) {
		return curwebpath + df.format(new Date()) + "/" + openid + "/pic_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + ".jpg";
	}

	public static String getFileNameByOpenid() {
		return curwebpath + df.format(new Date()) + "/pic_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + ".jpg";
	}
}
