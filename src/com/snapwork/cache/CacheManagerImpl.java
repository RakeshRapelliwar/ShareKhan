package com.snapwork.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;
import javax.microedition.io.InputConnection;

import net.rim.device.api.browser.field2.BrowserFieldRequest;
import net.rim.device.api.browser.field2.BrowserFieldResponse;
import net.rim.device.api.io.http.HttpHeaders;

/**
 * BF2CacheSampleApp - A sample BrowserField2 application that implements a short-term Web cache
 * 
 * - Available at the BlackBerry Support Forum (http://supportforums.blackberry.com)
 *   as an attachment of the article entitled "How to Implement a Web Cache for Your 
 *   BrowserField2 Application".
 * 
 * Class CacheManagerImpl
 * - Provides a simple implementation of the CacheManager interface to implement a 
 *   Web cache for BrowserField2 applications. 
 *
 */
public class CacheManagerImpl implements CacheManager {

    private static final int MAX_STANDARD_CACHE_AGE = 2592000;
	private Hashtable cacheTable;
	
	public CacheManagerImpl() {
		cacheTable = new Hashtable();
	}
	
    public boolean isRequestCacheable(BrowserFieldRequest request) {
        // Only HTTP requests are cacheable
        if (!request.getProtocol().equals("http")) {
            return false;
        }
        
        // Don't cache the request whose method is not "GET".
        if (request instanceof HttpConnection) {
            if (!((HttpConnection) request).getRequestMethod().equals("GET")) {
                return false;
            }
        }
        
        // Don't cache the request with post data.
        if (request.getPostData() != null) {
                return false;
        }
                
        // Don't cache authentication request.
        if (request.getHeaders().getPropertyValue("Authorization") != null) {
            return false;
        }        
        
        return true;    	
    }
    
    public boolean isResponseCacheable(HttpConnection response) {
        try {
            if (response.getResponseCode() != 200) {
                return false;
            }
        } catch (IOException ioe) {
            return false;
        }

        if (!response.getRequestMethod().equals("GET")) {
            return false;
        }
        
        if (containsPragmaNoCache(response)) {
            return false;
        }

        if (isExpired(response)) {
            return false;
        }
        
        if (containsCacheControlNoCache(response)) {
            return false;
        }
        
        if ( response.getLength() <= 0 ) {
        	return false;
        }
        
        // additional checks can be implemented here to inspect
        // the HTTP cache-related headers of the response object
       
        return true;
    }
    
    private boolean isExpired(HttpConnection response) {
        try {
            long expires = response.getExpiration(); // getExpiration() returns 0 if not known
            if (expires > 0 && expires <= (new Date()).getTime()) {
                return true;
            }

            return false;
        } catch (IOException ioe) {
            return true;
        }
    }
    
    private boolean containsPragmaNoCache(HttpConnection response) {
        try {
            if (response.getHeaderField("pragma") != null && response.getHeaderField("pragma").toLowerCase().indexOf("no-cache") >= 0) {
                return true;
            } 
            
            return false;
        } catch (IOException ioe) {
            return true;
        }
    }
    
    private boolean containsCacheControlNoCache(HttpConnection response) {
        try {
            String cacheControl = response.getHeaderField("cache-control");
            if (cacheControl != null) {
                cacheControl = removeSpace(cacheControl.toLowerCase());
                if (cacheControl.indexOf("no-cache") >= 0 
                    || cacheControl.indexOf("no-store") >= 0 
                    || cacheControl.indexOf("private") >= 0 
                    || cacheControl.indexOf("max-age=0") >= 0) {
                    return true;        
                }
                
                long maxAge = parseMaxAge(cacheControl);
                if (maxAge > 0 && response.getDate() > 0) {
                    long date = response.getDate();
                    long now = (new Date()).getTime();                    
                    if (now > date + maxAge) {
                        // Already expired
                        return true;
                    }
                }
            } 

            return false;
        } catch (IOException ioe) {
            return true;
        }
    }    
    
    public InputConnection createCache(String url, HttpConnection response) {

        byte[] data = null;
        InputStream is = null;
        try {
            // Read data
            int len = (int) response.getLength();
            if (len > 0) {
                is = response.openInputStream();
                int actual = 0;
                int bytesread = 0 ;
                data = new byte[len];
                while ((bytesread != len) && (actual != -1)) {
                    actual = is.read(data, bytesread, len - bytesread);
                    bytesread += actual;
                }
            }       
        } catch (IOException ioe) {
            data = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ioe) {
                }
            } 
        }

        if (data == null) {
            return null;
        } 

        // Calculate expires
        long expires = calculateCacheExpires(response);
        
        // Copy headers
        HttpHeaders headers = copyResponseHeaders(response);
      //  headers.

        // add item to cache
        cacheTable.put(url, new CacheItem(url, expires, data, headers));
        
        return new BrowserFieldResponse(url, data, headers);
    }
    
    private long calculateCacheExpires(HttpConnection response) {
        long date = 0;
        try {
            date = response.getDate();
        } catch (IOException ioe) {
        }
        
        if (date == 0) {
            date = (new Date()).getTime();
        }

        long expires = getResponseExpires(response);
        
        // If an expire date has not been specified assumes the maximum time
        if ( expires == 0 ) {
        	return date + (MAX_STANDARD_CACHE_AGE * 1000L);
        }
        
        return expires;
    }
    
    private long getResponseExpires(HttpConnection response) {
        try {
            // Calculate expires from "expires"
            long expires = response.getExpiration();
            if (expires > 0) {
                return expires;
            }
            
            // Calculate expires from "max-age" and "date"
            if (response.getHeaderField("cache-control") != null) {
                String cacheControl = removeSpace(response.getHeaderField("cache-control").toLowerCase());
                long maxAge = parseMaxAge(cacheControl);
                long date = response.getDate();
                
                if (maxAge > 0 && date > 0) {
                    return (date + maxAge);
                }
            }
        } catch (IOException ioe) {
        }
        
        return 0;
    }
    
    private long parseMaxAge(String cacheControl) {
        if (cacheControl == null) {
            return 0;
        }
        
        long maxAge = 0;
        if (cacheControl.indexOf("max-age=") >= 0) {
            int maxAgeStart = cacheControl.indexOf("max-age=") + 8;
            int maxAgeEnd = cacheControl.indexOf(',', maxAgeStart);
            if (maxAgeEnd < 0) {
                maxAgeEnd = cacheControl.length();
            }
            
            try {
                maxAge = Long.parseLong(cacheControl.substring(maxAgeStart, maxAgeEnd));
            } catch (NumberFormatException nfe) {
            }
        }
        
                // Multiply maxAge by 1000 to convert seconds to milliseconds
                maxAge *= 1000L;
        return maxAge;
    }
    
    private static String removeSpace(String s) {
        StringBuffer result= new StringBuffer();
        int count = s.length();
        for (int i = 0; i < count; i++) {
            char c = s.charAt(i);
            if (c != ' ') {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    private HttpHeaders copyResponseHeaders(HttpConnection response) {
        HttpHeaders headers = new HttpHeaders();
        try {
            int index = 0;
            while (response.getHeaderFieldKey(index) != null) {
                headers.addProperty(response.getHeaderFieldKey(index), response.getHeaderField(index));
                index++;
            }
        } catch (IOException ioe) {
        }
        
        return headers;
    }    
    
    public boolean hasCache(String url) {
    	return cacheTable.containsKey(url);
    }
   
    public boolean hasCacheExpired(String url) {
        Object o = cacheTable.get(url);
        
        if (o instanceof CacheItem) {
            CacheItem ci = (CacheItem) o;
            long date = (new Date()).getTime();
            if (ci.getExpires() > date) {
                return false;
            } else {
                // Remove the expired cache item
                clearCache(url);
            }
        }
        
        return true;
    }
    
    public void clearCache(String url) {
        cacheTable.remove(url);
    }    
    
    public InputConnection getCache(String url) {
        Object o = cacheTable.get(url);        
        if (o instanceof CacheItem) {
            CacheItem ci = (CacheItem) o;
            return new BrowserFieldResponse(url, ci.getData(), ci.getHttpHeaders());
        }        
        return null;
    }
}
