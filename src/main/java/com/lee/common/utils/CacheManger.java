package com.lee.common.utils;

import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.lee.common.memcached.client.IMemcachedCache;


public class CacheManger {

	private static String CACHESTYLE = "memcached";//memcached
	
	private static String redisKey = "badian_redis_";  //redis前缀key
	private static String memKey = "badian_mem_";  //memcache前缀key=fl_mem_
	public static int cycle = 24*60*60;//缓存周期一天
	
	/**
	 * 设置一段时间缓存
	 * @param key
	 * @param value
	 * @param seconds 0 永久
	 * @return
	 */
	public static void set(String key, String value,  int seconds){
		try {
//			if("redis".equals(CACHESTYLE)){
//				if(!"OK".equals(value) || !"PONG".equals(value)){
//					ShardedJedis shardedJedis = InitSource.getShardedJedisClient();
//					String set = null;
//					if(seconds == 0){
//						set = shardedJedis.set(redisKey+key, value);
//					}else{
//						set = shardedJedis.setex(redisKey+key, seconds, value);
//					}
//					InitSource.clearShardedJedisPool(shardedJedis);
//				}
//			}else 
			if("memcached".equals(CACHESTYLE)){
				IMemcachedCache cache = MemcacheTools.getCache("default");
				String keySet = memKey + key;
				if(seconds==0)
					cache.put(keySet, value);
				else
					cache.put(keySet, value, seconds);
			}else{
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setObject(String key,Object o,int seconds){
		IMemcachedCache cache = MemcacheTools.getCache("default");
		String keySet = memKey + key;
		if(seconds==0){
			cache.put(keySet, o);
		}else{
			cache.put(keySet, o, seconds);
		}
	}
	
	/**
	 * 设置永久缓存
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value){
		set(key, value,  0);
	}
	
	/**
	 * 公用缓存的get方法
	 * @param key
	 * @return
	 */
	public static String get(String key){
		try {
			String param = null;
//			if("redis".equals(CACHESTYLE)){
//				ShardedJedis shardedJedis = InitSource.getShardedJedisClient();
//				param = shardedJedis.get(redisKey+key);
//				if("PONG".equals(param) || "OK".equals(param)) {
//					param = null;
//				}
//				//System.out.println("********** key="+redisKey+key+"**********从缓存中拿到值*************value="+param);
//				InitSource.clearShardedJedisPool(shardedJedis);
//			}else 
			if("memcached".equals(CACHESTYLE)){
				IMemcachedCache cache = MemcacheTools.getCache("default");
				String keySet = memKey + key;
				param = (String)cache.get(keySet);
			}else{
			}
			return param;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static Object getObject(String key){
		Object o = null;
		try{
			IMemcachedCache cache = MemcacheTools.getCache("default");
			String keySet = memKey + key;
			o = cache.get(keySet);
		}catch(Exception e){
			e.printStackTrace();
		}
		return o;
	}
	
	/**
	 * 删除缓存key
	 * @param key 缓存key
	 */
	public static void del(String key){
        if("memcached".equals(CACHESTYLE)){
			IMemcachedCache cache = MemcacheTools.getCache("default");
			String keySet = memKey + key;
			cache.remove(keySet);
		}else{
		}
	}
	
	
	/**
	 * 缓存所有的key的集合
	 * @param key
	 * @return
	 */
	public static Set<String> keySet(){
        if("memcached".equals(CACHESTYLE)){
			IMemcachedCache cache = MemcacheTools.getCache("default");
			Set<String> keySet = cache.keySet();
			return keySet;
		}else{
			return null;
		}
	}
	
	/**
	 * 清除所有缓存
	 */
	public static void delAll(){
        if("memcached".equals(CACHESTYLE)){
			IMemcachedCache cache = MemcacheTools.getCache("default");
			cache.clear();
		}else{
		}
	}
	
	
	/**
	 * 这个接口返回的Key如果采用fast模式，
	 * 那么返回的key可能已经被清除或者失效，但是在内存中还有痕迹，如果是非fast模式，那么就会精确返回，但是效率较低
	 * 
	 * @param 是否需要去交验key是否存在
	 * @return
	 */
	public static Set<String> keySet(boolean fast){
        if("memcached".equals(CACHESTYLE)){
			IMemcachedCache cache = MemcacheTools.getCache("default");
			Set<String> keySet = cache.keySet(fast);
			return keySet;
		}else{
			return null;
		}
	}
	
	public static void main(String [] atrs){
		String str = "[{level=1, remark=士大夫,士大夫, vcid=1, download=http://124.160.105.98:8080/html/terminal/android/eChat.apk, type=1, version=1.0}, {level=1, remark=111, vcid=2, download=http://124.160.105.98:8080/w.html, type=2, version=1.0}, {level=2, remark=111, vcid=8, download=http://124.160.105.98:8080/w.html, type=2, version=1.122222}, {level=1, remark=111, vcid=9, download=http://124.160.105.98:8080/html/terminal/android/eChat.apk, type=1, version=1.1}, {level=2, remark=111, vcid=11, download=http://124.160.105.98:8080/html/terminal/android/eChat.apk, type=1, version=1.2}]";
		try{
			str=str.replace("=",":\"").replace(", ","\", ").replace("}\",","\"},").replace("}]", "\"}]");;
			JSONArray retjsonArr = JSONArray.parseArray(str);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
