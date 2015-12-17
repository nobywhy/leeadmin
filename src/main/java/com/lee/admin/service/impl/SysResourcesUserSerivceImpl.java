package com.lee.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;






import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lee.admin.dao.SysResourcesUserDao;
import com.lee.admin.service.SysResourcesUserService;
import com.lee.common.dao.BaseDAO;
import com.lee.common.service.BaseServiceImpl;

@Service
public class SysResourcesUserSerivceImpl extends BaseServiceImpl implements SysResourcesUserService {
	@Autowired
	private SysResourcesUserDao dao = null;

	@Override
	@Resource(name="sysResourcesUserDao")
	public void setDAO(BaseDAO dao) {
		super.setDAO(dao);
	}

	@Override
	public List<Map<String, Object>> getUserResources(Map<String, Object> qm) {
		return dao.getUserResources(qm);
	}

	@Override
	public void deleteByResId(long resId) {
		dao.deleteByResId(resId);
	}

	@Override
	public void addAuthorize(long userId, String[] ids) {
		
		// 先删除所有权限，再添加
		dao.delete(userId);
		
		for (String resId : ids) {
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("resId", resId);
			m.put("userId", userId);
			dao.create(m);
		}
		
	}

}
