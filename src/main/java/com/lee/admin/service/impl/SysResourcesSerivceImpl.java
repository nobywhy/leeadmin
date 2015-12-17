package com.lee.admin.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lee.admin.dao.SysResourcesDao;
import com.lee.admin.dao.SysResourcesUserDao;
import com.lee.admin.service.SysResourcesService;
import com.lee.common.dao.BaseDAO;
import com.lee.common.service.BaseServiceImpl;

@Service
public class SysResourcesSerivceImpl extends BaseServiceImpl implements SysResourcesService {
	@Autowired
	private SysResourcesDao dao = null;
	@Autowired
	private SysResourcesUserDao resourcesUserDao;
	
	@Override
	@Resource(name="sysResourcesDao")
	public void setDAO(BaseDAO dao) {
		super.setDAO(dao);
	}
	
	
	/**
	 * 1、删除子菜单权限
	 * 2、删除该菜单下的子菜单
	 * 3、删除菜单权限
	 * 4、删除菜单
	 */
	@Override
	public void delete(long id) {
		
		
		// 删除子菜单权限
		Map<String, Object> qm = new HashMap<String, Object>();
		qm.put("parentId", id);
		List<Map<String, Object>> subRes = dao.getByCondition(qm);
		for (Map<String, Object> map : subRes) {
			resourcesUserDao.deleteByResId(Integer.parseInt(map.get("id")+""));
		}
		
		
		// 删除该菜单下的子菜单
		dao.deleteByParentId(id);
		
		// 删除菜单权限
		resourcesUserDao.deleteByResId(id);
		
		// 删除菜单
		super.delete(id);
	}


	@Override
	public int createReturnId(Map<String, Object> m) {
		dao.createReturnId(m);
		int id = Integer.parseInt(m.get("id")+"");
		// 添加增删改查按钮
		addCRUD(m, id);

		return id;
	}

	@Override
	public void update(Map<String, Object> m) {
		dao.update(m);
		// 添加增删改查按钮
		long id = Long.parseLong(m.get("id")+"");
		addCRUD(m, id);
	}
	

	/**
	 * 在菜单下添加增删改查按钮
	 * @param m 父菜单
	 * @param id 菜单id
	 */
	private void addCRUD(Map<String, Object> m, long id) {
		String[] cruds = (String[]) m.get("curd[]");
		if(cruds == null || cruds.length==0) return;
		
		for (String string : cruds) {
			
			String name = m.get("name")+getName(string);
			String resKey = m.get("resKey")+"_"+string;
			String resUrl =  m.get("resKey")+"/"+string+".do";
			
			// 查询resKey是否存在
			Map<String,Object> qm = new HashMap<String,Object>();
			qm.put("resKey", resKey);
			int count = dao.getByConditionCount(qm);
			if (count>0) {
				continue;
			}
			
			// 添加
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("parentId", id);
			map.put("name", name);
			map.put("resKey", resKey);
			map.put("type", 2);
			map.put("resUrl", resUrl);
			map.put("description", name);
			map.put("status", 1);
			dao.create(map);
		}
	}
	
	private String getName(String key){
		if("add".equals(key)){
			return "_新增";
		} else if("update".equals(key)) {
			return "_修改";
		} else if("delete".equals(key)) {
			return "_删除";
		} else if("audit".equals(key)) {
			return "_审核";
		} else if("list".equals(key)) {
			return "_查询";
		}
		return "";
	}


	@Override
	public void deleteByParentId(long pid) {
		dao.deleteByParentId(pid);
	}


	@Override
	public List<Map<String, Object>> tree(Map<String,Object> m) {
		return dao.tree(m);
	}
	

}
