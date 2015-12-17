package com.lee.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.lee.common.dao.BaseDAO;
import com.lee.common.pageutil.Page;

public class BaseServiceImpl implements BaseService {
	private BaseDAO dao = null;

	public void setDAO(BaseDAO dao) {
		this.dao = dao;
	}

	public void create(Map<String, Object> m) {
		dao.create(m);
	}

	public void update(Map<String, Object> m) {
		dao.update(m);
	}

	public void delete(long id) {
		dao.delete(id);
	}

	public Map<String, Object> getById(long id) {
		return dao.getById(id);
	}

	public Page<Map<String, Object>> getByConditionPage(Map<String, Object> qm) {
		int count = dao.getByConditionCount(qm);
		int pageIndex = 1;
		Object pindex = qm.get("pageIndex");
		if(!StringUtils.isEmpty(pindex)) {
			pageIndex = Integer.parseInt(pindex+"");
		}
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageIndex,count);
		qm.put("first", page.getFirst());
		qm.put("pageSize", page.getPageSize());
		List<Map<String, Object>> list = dao.getByConditionPage(qm);
		page.setElements(list);
		return page;
	}

	@Override
	public int getByConditionCount(Map<String, Object> qm) {
		return dao.getByConditionCount(qm);
	}

	@Override
	public List<Map<String, Object>> getByCondition(Map<String, Object> qm) {
		return dao.getByCondition(qm);
	}

}
