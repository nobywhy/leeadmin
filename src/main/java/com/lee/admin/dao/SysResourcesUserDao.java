package com.lee.admin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lee.common.dao.BaseDAO;

@Repository
public interface SysResourcesUserDao extends BaseDAO{

	
	public List<Map<String,Object>> getUserResources(Map<String,Object> qm);
	
	public void deleteByResId(long resId);
	
}
