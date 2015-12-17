package com.lee.common.memcached.client.memcached;

/**
 * Memcache的客户端配置
 * @author eskynet<eskynet@eskynet.com>
 *
 */
public class MemcachedClientConfig
{
	private String name;
	/**
	 * 是否需要压缩
	 */
	private boolean compressEnable;
	
	/**
	 * 大于这个数里开始压缩
	 */
	private long compressThreshold;	
	
	
	/**
	 * 默认编码方式UTF-8
	 */
	private String defaultEncoding;
	/**
	 * 处理错误的类名，需要全路径
	 */
	private String errorHandler;
	/**
	 * 客户端对应的SocketIOPool
	 */
	private String socketPool;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isCompressEnable()
	{
		return compressEnable;
	}

	public void setCompressEnable(boolean compressEnable)
	{
		this.compressEnable = compressEnable;
	}
	

	
	public long getCompressThreshold() {
		return compressThreshold;
	}

	public void setCompressThreshold(long compressThreshold) {
		this.compressThreshold = compressThreshold;
	}

	public String getDefaultEncoding()
	{
		return defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding)
	{
		this.defaultEncoding = defaultEncoding;
	}

	public String getErrorHandler()
	{
		return errorHandler;
	}

	public void setErrorHandler(String errorHandler)
	{
		this.errorHandler = errorHandler;
	}

	public String getSocketPool()
	{
		return socketPool;
	}

	public void setSocketPool(String socketPool)
	{
		this.socketPool = socketPool;
	}
	
}
