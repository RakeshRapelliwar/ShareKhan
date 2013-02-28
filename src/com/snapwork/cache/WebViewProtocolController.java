package com.snapwork.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;
import javax.microedition.io.InputConnection;

import com.snapwork.util.LOG;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldRequest;
import net.rim.device.api.browser.field2.BrowserFieldResponse;
import net.rim.device.api.browser.field2.ProtocolController;
import net.rim.device.api.io.http.HttpHeaders;


public class WebViewProtocolController extends ProtocolController{

	// The BrowserField instance
    private BrowserField browserField;
    public static Hashtable storeResponse;
    
  public WebViewProtocolController(BrowserField browserField) {
		super(browserField);
		this.browserField = browserField;
		if(storeResponse == null)
		{
			storeResponse = new Hashtable();
		}
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
		// if requested resource is not cacheable, load it as usual
		InputConnection ic = (InputConnection)storeResponse.get(request.getURL());//getCache(request.getURL());
		if(ic!=null)
		{
			LOG.print("cash data return");
			return ic;
		}
		else
		{
			LOG.print("cash stored");
		}
		ic = super.handleResourceRequest(request);
       
		// if (ic instanceof HttpConnection) {
          //  HttpConnection response = (HttpConnection) ic;
           // createCache(request.getURL(), response);
         storeResponse.put(request.getURL(), (InputConnection)ic);
        //}
            
		return ic;
		//return super.handleResourceRequest(request);
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
	        	LOG.print("createCache exception 1");
	            data = null;
	        } finally {
	            if (is != null) {
	                try {
	                    is.close();
	                } catch (IOException ioe) {
	                	LOG.print("createCache exception 2");
	                }
	            }
	            if (response != null) {
	                try {
	                    response.close();
	                } catch (IOException ioe) {
	                	LOG.print("createCache exception 3");
	                }
	            } 
	        }

	        if (data == null) {
	            return null;
	        } 

	        
	        // Copy headers
	        HttpHeaders headers = copyResponseHeaders(response);
	      //  headers.

	        // add item to cache
	        storeResponse.put(url, new WebViewCacheItem(url, 1l, data, headers));
	         
	        return new BrowserFieldResponse(url, data, headers);
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
	 
	 public InputConnection getCache(String url) {
	        Object o = storeResponse.get(url);
	        if(o == null) return null;
	        
	        if (o instanceof WebViewCacheItem) {
	        	WebViewCacheItem ci = (WebViewCacheItem) o;
	            return new BrowserFieldResponse(url, ci.getData(), ci.getHttpHeaders());
	        }        
	        return null;
	    }

}
