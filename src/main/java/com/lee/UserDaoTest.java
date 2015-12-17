package com.lee;

import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lee.admin.dao.SysUserDao;

public class UserDaoTest {

	
	public static void main(String[] args) {
//		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-ds.xml");
//		UserDao dao = ctx.getBean("userDao", UserDao.class);
//		Map<String, Object> byUuid = dao.getById(1);
//		System.out.println(byUuid);
		
		HashedCredentialsMatcher cm = new HashedCredentialsMatcher();
		cm.setHashAlgorithmName("md5");
		cm.setHashIterations(2);
		cm.setStoredCredentialsHexEncoded(true);
	}
}
