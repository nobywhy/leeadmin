package com.lee.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class FileUtil {
	protected static Logger userLog = Logger.getLogger("UserLog");
	
	/**
	 *  
	 * @param dest  源文件
	 * @param file  目标文件
	 * @throws IOException
	 */
	public static void copyFile(File dest,File file) throws IOException{
		FileInputStream fileInputStream = new FileInputStream(dest);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		copyFile(fileInputStream, fileOutputStream, 5120);
		fileInputStream.close();
		fileOutputStream.close();
	}
	public static void copyFile(InputStream is,File file) throws IOException{
		if(is==null)
			return;
		long start=System.currentTimeMillis();
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		byte[] temp=new byte[5120];
		int length=0;
		long size=0;
		while((length=is.read(temp))!=-1){    //当没有读取完时，继续读取  
            fileOutputStream.write(temp, 0, length);
            fileOutputStream.flush();
            size+=length;
        }  
		is.close();
		fileOutputStream.close();
		userLog.info("httpfile size="+size+",downloadtime="+(System.currentTimeMillis()-start));
	}
	
	/**
	 * 
	 * @param fileInputStream
	 * @param fileOutputStream
	 * @param bufferSize   缓冲区大小   适当的增大可以提高速度！但是内存的消耗很严重
	 * @throws IOException
	 */
	public static void copyFile(FileInputStream fileInputStream,FileOutputStream fileOutputStream,int bufferSize) throws IOException{
		FileChannel fic = fileInputStream.getChannel();
		FileChannel foc = fileOutputStream.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		foc.force(true);
		while(fic.read(buffer) != -1){
			buffer.flip();
			foc.write(buffer);
			buffer.clear();
		}
		foc.close();
		fic.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		List<Map> ls=new ArrayList<Map>();
		Map maps=new HashMap();
		maps.put("name","sdf");
		maps.put("id","sdf");
		maps.put("sex","sdf");
		maps.put("age","sdf");
		ls.add(maps);
		System.out.println(ls.get(0).keySet());
//		for(String key :ls.get(0).keySet()){
//			System.out.println(key);
//		}
//		long time = System.currentTimeMillis();
//		String dest = "E:\\叶问：终极一战DVD.rmvb";
//		String dest2 = "E:\\movies\\叶问：终极一战DVD.rmvb";
//		FileUtil.copyFile(new File(dest2), new File(dest));
//		System.out.println("=======================================================");
//		System.out.println( System.currentTimeMillis() - time);
//		System.gc();
	}
}
