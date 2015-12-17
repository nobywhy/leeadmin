package com.lee.admin.service;

import java.util.List;
import java.util.Map;

import com.lee.common.service.BaseService;

public interface SysResourcesUserService extends BaseService{

	public List<Map<String,Object>> getUserResources(Map<String,Object> qm);
	
	/**
	 * 授权
	 * @param userId 用户id
	 * @param ids 权限ID数组
	 */
	public void addAuthorize(long userId,String[] ids);
	
	public void deleteByResId(long resId);
	
}
