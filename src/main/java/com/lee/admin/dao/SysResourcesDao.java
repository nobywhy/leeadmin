package com.lee.admin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lee.common.dao.BaseDAO;

@Repository
public interface SysResourcesDao extends BaseDAO{

	
	public void createReturnId(Map<String,Object> m);
	
	public void deleteByParentId(long id);
	
	public List<Map<String,Object>> tree(Map<String,Object> m);
	
}
