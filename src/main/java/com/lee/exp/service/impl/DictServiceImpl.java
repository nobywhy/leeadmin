package com.lee.exp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lee.common.dao.BaseDAO;
import com.lee.common.service.BaseServiceImpl;
import com.lee.exp.dao.DictDao;
import com.lee.exp.service.DictService;

@Service
public class DictServiceImpl extends BaseServiceImpl implements DictService {
	@Autowired
	private DictDao dao = null;

	@Override
	@Resource(name="dictDao")
	public void setDAO(BaseDAO dao) {
		super.setDAO(dao);
	}


}
