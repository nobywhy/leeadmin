package com.lee.admin.service;

import java.util.List;
import java.util.Map;

import com.lee.common.service.BaseService;

public interface SysResourcesService extends BaseService{

	public int createReturnId(Map<String,Object> m);
	public void deleteByParentId(long pid);
	public List<Map<String,Object>> tree(Map<String,Object> m);
}
