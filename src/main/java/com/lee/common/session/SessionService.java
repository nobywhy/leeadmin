package com.lee.common.session;

import java.util.HashMap;
import java.util.Map;

import com.lee.common.memcached.client.IMemcachedCache;
import com.lee.common.utils.CacheTools;

public class SessionService {

	private static SessionService instance = null;
	private static IMemcachedCache cache;
	private static String MemcachedName = "SessionMem";
	//private static HashMap cache;

	public static void setMemcacheName(String name) {
		MemcachedName = name;
	}
	
	public static SessionService getInstance() {
		if(instance == null) {
			cache = CacheTools.getCache(MemcachedName);
			instance = new SessionService();
		}
		return instance;
	}

	public Map getSession(String id) {
		Map session = (Map) cache.get(id);
		if (session == null) {
			session = new HashMap();
			try {
				cache.put(id, session);
			} catch (Exception e) {
				try {
				cache = CacheTools.getCache(MemcachedName);
				cache.put(id, session);
				} catch (Exception e1){}
			}
		}
		return session;
	}

	public void saveSession(String id, Map session) {
		cache.put(id, session);
	}

	public void removeSession(String id) {
		cache.remove(id);
	}
}
