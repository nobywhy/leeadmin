package com.lee.common.utils;

import com.lee.common.memcached.client.ICacheManager;
import com.lee.common.memcached.client.IMemcachedCache;
import com.lee.common.memcached.client.memcached.CacheUtil;
import com.lee.common.memcached.client.memcached.MemcachedCacheManager;


public class CacheTools {

	private static ICacheManager<IMemcachedCache> manager;
	public static boolean inited = false;
	static {		
		init();
	}
	
	public synchronized static void init() {	
		if(inited) return;
		System.out.println("......cache tools init....");
		manager = CacheUtil.getCacheManager(
				IMemcachedCache.class, MemcachedCacheManager.class.getName());
		manager.start();		
	

		inited = true;
	}
	
	public static void stop() {
		System.out.println("......cache tools stop.......");
		if(manager != null) {
			try {
				manager.stop();
			} catch (Exception e){}
		}
		inited = false;
	}
	/**
	 * 获取缺省cache
	 * @return
	 */
	public static IMemcachedCache getDefault() {
		return manager.getCache("default");
	}
	
	/**
	 * 根据cache名字获取
	 * @param cachename
	 * @return
	 */
	public static IMemcachedCache getCache(String cachename) {
		return manager.getCache(cachename);
	}
	
}
