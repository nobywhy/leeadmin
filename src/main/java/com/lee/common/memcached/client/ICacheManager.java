/**
 * 
 */
package com.lee.common.memcached.client;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CacheManager 统一接口
 * 
 * @author eskynet<eskynet@eskynet.com>
 * 
 */
public interface ICacheManager<T extends ICache<?, ?>> {
	/**
	 * 获取配置在memcached.xml中的Cache客户端
	 * 
	 * @param name
	 * @return
	 */
	public T getCache(String name);

	public void addCache(String name, T cache);

	public void removeCache(String name);

	public Set<String> getCacheNameSet();

	public void setConfigFile(String configFile);

	public ConcurrentHashMap<String, IMemcachedCache> getCachepool();

	/**
	 * 做一定的初始化工作
	 */
	public void start();

	/**
	 * 做资源回收工作
	 */
	public void stop();

	/**
	 * 重新载入Cache配置
	 * 
	 * @param configFile
	 */
	public void reload(String configFile);

	/**
	 * 主动集群中node内容拷贝
	 * 
	 * @param fromCache
	 * @param cluster
	 */
	public void clusterCopy(String fromCache, String cluster);

	/**
	 * 设置响应统计时间间隔(单位秒,默认为0,0表示不需要做响应统计)
	 * 
	 * @param seconds
	 */
	public void setResponseStatInterval(int seconds);

}
