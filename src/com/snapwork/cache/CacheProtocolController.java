package com.snapwork.cache;

import javax.microedition.io.HttpConnection;
import javax.microedition.io.InputConnection;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldRequest;
import net.rim.device.api.browser.field2.ProtocolController;

/**
 * BF2CacheSampleApp - A sample BrowserField2 application that implements a short-term Web cache
 * 
 * - Available at the BlackBerry Support Forum (http://supportforums.blackberry.com)
 *   as an attachment of the article entitled "How to Implement a Web Cache for Your 
 *   BrowserField2 Application".
 * 
 * Class CacheProtocolController
 * - Extends BrowserField2 ProtocolController to implement a page/resource caching system
 *
 */
public class CacheProtocolController extends ProtocolController{

	// The BrowserField instance
    private BrowserField browserField;
    
    // CacheManager will take care of cached resources 
    private CacheManager cacheManager;

	public CacheProtocolController(BrowserField browserField) {
		super(browserField);
		this.browserField = browserField;
	}
	
	private CacheManager getCacheManager() {
		if ( cacheManager == null ) {
			cacheManager = new CacheManagerImpl();
		}
		return cacheManager;
	}
	
	/**
	 * Handle navigation requests (e.g., link clicks)
	 */
	public void handleNavigationRequest(BrowserFieldRequest request) throws Exception {
		InputConnection ic = handleResourceRequest(request);
		browserField.displayContent(ic, request.getURL());
	}

	/**
	 * Handle resource request (e.g., images, external css/javascript resources)
	 */
	public InputConnection handleResourceRequest(BrowserFieldRequest request) throws Exception {
		// if requested resource is cacheable (e.g., an "http" resource), use the cache
		if (getCacheManager() != null && getCacheManager().isRequestCacheable(request)) {
            InputConnection ic = null;
            // if requested resource is cached, retrieve it from cache
            if (getCacheManager().hasCache(request.getURL()) && !getCacheManager().hasCacheExpired(request.getURL())) {
                ic = getCacheManager().getCache(request.getURL());
            }
            // if requested resource is not cached yet, cache it
            else {
            	ic = super.handleResourceRequest(request);
                if (ic instanceof HttpConnection) {
                    HttpConnection response = (HttpConnection) ic;
                    if (getCacheManager().isResponseCacheable(response)) {
                        ic = getCacheManager().createCache(request.getURL(), response);
                    }
                }
            }
            return ic;
		}
		// if requested resource is not cacheable, load it as usual
		return super.handleResourceRequest(request);
	}

}
