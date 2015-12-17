package com.lee.common.service;

import java.util.List;
import java.util.Map;

import com.lee.common.pageutil.Page;




public interface BaseService {
	public void create(Map<String,Object> m);
	public void update(Map<String,Object> m);
	public void delete(long id);
	
	public Map<String,Object> getById(long id);
	public int getByConditionCount(Map<String,Object> qm);
	public Page<Map<String,Object>> getByConditionPage(Map<String,Object> qm);
	public List<Map<String,Object>> getByCondition(Map<String,Object> qm);
}
