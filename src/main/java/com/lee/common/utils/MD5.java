package com.lee.common.utils;


import org.apache.shiro.crypto.hash.SimpleHash;

public class MD5 {

	private static final String algorithmName = "md5";
	private static final int hashIterations = 1;

	/**
	 * 获取md5摘要 MessageDigest类为应用程序提供信息摘要算法的功胄1�7
	 * 
	 * @param strMd5
	 * @return
	 */
	public static String md5(String strMd5) {
		String sRet = null;
		SimpleHash simpleHash = new SimpleHash(algorithmName, strMd5, "", hashIterations);
		sRet = simpleHash.toHex();
		return sRet;
	}
	
	/**
	 * md5加密
	 * @param strMd5 需要加密的字符串
	 * @param hashIterations 迭代次数，默认是1
	 * @return
	 */
	public static String md5(String strMd5,int hashIterations) {
		String sRet = null;
		SimpleHash simpleHash = new SimpleHash(algorithmName, strMd5, "", hashIterations);
		sRet = simpleHash.toHex();
		return sRet;
	}

	public static void main(String[] argv) {
		// admin pwd
		// System.out.println(MD5.md5(MD5.md5("123456") + "9c8b77"));
		System.out.println(MD5.md5("9000AG0027111111"));
	}
}
