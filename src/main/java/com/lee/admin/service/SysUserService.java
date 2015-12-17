package com.lee.admin.service;

import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;

import com.lee.common.service.BaseService;

public interface SysUserService extends BaseService{

	Map<String,Object> login(String accountName, String password)  throws UnknownAccountException,LockedAccountException,AuthenticationException ;

}
