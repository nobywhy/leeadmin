package com.lee.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lee.admin.dao.SysLogDao;
import com.lee.admin.service.SysLogService;
import com.lee.common.dao.BaseDAO;
import com.lee.common.service.BaseServiceImpl;

@Service
public class SysLogServiceImpl extends BaseServiceImpl implements SysLogService {
	@Autowired
	private SysLogDao dao = null;

	@Resource(name="sysLogDao")
	public void setDAO(BaseDAO dao) {
		super.setDAO(dao);
	}


}
