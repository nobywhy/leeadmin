package com.lee.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lee.admin.dao.SysUserDao;
import com.lee.admin.service.SysUserService;
import com.lee.common.dao.BaseDAO;
import com.lee.common.service.BaseServiceImpl;
import com.lee.common.utils.PasswordHelper;

@Service
public class SysUserServiceImpl extends BaseServiceImpl implements SysUserService {
	@Autowired
	private SysUserDao dao = null;

	@Resource(name="sysUserDao")
	public void setDAO(BaseDAO dao) {
		super.setDAO(dao);
	}

	@Override
	public Map<String, Object> login(String accountName, String password) throws UnknownAccountException,LockedAccountException,AuthenticationException {
		Map<String, Object> qm = new HashMap<String, Object>();
		qm.put("accountName", accountName);
		List<Map<String, Object>> userList = dao.getByCondition(qm);

		if (userList == null || userList.size() == 0) {
			throw new UnknownAccountException();// 帐号不存在
		}
		Map<String, Object> user = userList.get(0);
		if ("1".equals(user.get("locked"))) {
			throw new LockedAccountException(); // 帐号锁定
		}

		String pwd = PasswordHelper.encryptPassword(password);
		if (!pwd.equals(user.get("password"))) {
			throw new AuthenticationException(); // 密码错误
		}
		
		return user;
	}

}
