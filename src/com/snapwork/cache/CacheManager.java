package com.snapwork.cache;

import javax.microedition.io.HttpConnection;
import javax.microedition.io.InputConnection;

import net.rim.device.api.browser.field2.BrowserFieldRequest;

/**
 * BF2CacheSampleApp - A sample BrowserField2 application that implements a short-term Web cache
 * 
 * - Available at the BlackBerry Support Forum (http://supportforums.blackberry.com)
 *   as an attachment of the article entitled "How to Implement a Web Cache for Your 
 *   BrowserField2 Application".
 * 
 * Interface CacheManager
 * - Provides an abstract interface for cache management.
 *
 */

public interface CacheManager {
    public boolean isRequestCacheable(BrowserFieldRequest request);
    public boolean isResponseCacheable(HttpConnection response);
    public boolean hasCache(String url);
    public boolean hasCacheExpired(String url);
    public InputConnection getCache(String url);
    public InputConnection createCache(String url, HttpConnection response);
    public void clearCache(String url);    
}
