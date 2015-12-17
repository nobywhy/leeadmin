package com.lee.common.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

public class PasswordHelper {
	private static final String algorithmName = "md5";
	private static final int hashIterations = 2;

	public static String encryptPassword(String pwd) {
		SimpleHash simpleHash = new SimpleHash(algorithmName, pwd, "", hashIterations);
		return simpleHash.toHex();
	}

	public static void main(String[] args) {
		SimpleHash simpleHash = new SimpleHash(algorithmName, "123456", "", 0);
		System.out.println(simpleHash.toHex());
	}
}