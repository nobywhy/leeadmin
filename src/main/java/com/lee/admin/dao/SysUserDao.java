package com.lee.admin.dao;

import org.springframework.stereotype.Repository;

import com.lee.common.dao.BaseDAO;

@Repository
public interface SysUserDao extends BaseDAO{

	public void deleteByParentId(long pid);
	
}
