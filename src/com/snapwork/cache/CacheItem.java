package com.snapwork.cache;

import net.rim.device.api.io.http.HttpHeaders;

/**
 * BF2CacheSampleApp - A sample BrowserField2 application that implements a short-term Web cache
 * 
 * - Available at the BlackBerry Support Forum (http://supportforums.blackberry.com)
 *   as an attachment of the article entitled "How to Implement a Web Cache for Your 
 *   BrowserField2 Application".
 * 
 * Class CacheItem
 * - Stores information about an HTTP resource and represents an entry in the CacheManagerImpl.
 *
 */
public class CacheItem {

	private String  url;	
	private long    expires;	
	private byte[] data;
	private HttpHeaders httpHeaders;
	
	public CacheItem(String url, long expires, byte[] data, HttpHeaders httpHeaders ) {
		this.url = url;
		this.expires = expires;
		this.data = data;
		this.httpHeaders = httpHeaders;
	}
	
	public String getUrl() {
		return url;
	}
	
	public long getExpires() {
		return expires;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public HttpHeaders getHttpHeaders() {
		return httpHeaders;
	}
}
