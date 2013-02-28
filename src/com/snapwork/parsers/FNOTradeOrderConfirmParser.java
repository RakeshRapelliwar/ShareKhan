package com.snapwork.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import net.rim.device.api.ui.image.Image;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

import com.snapwork.beans.FNOTradeOrderConfirmBean;
import com.snapwork.components.Screen;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;

public class FNOTradeOrderConfirmParser implements HttpResponse {

    private String feedurl = "";
    private Screen screen = null;

	public FNOTradeOrderConfirmParser(String url,Screen screen) {
        this.feedurl = url;
        this.screen = screen;
	}

    public void getScreenData()
    {
            HttpProcess.threadedHttpsMD5Connection(feedurl, this);
    }

	public void setResponse(String rsponse) {
		if(rsponse == null)
			return;
		// TODO Auto-generated method stub
   		LOG.print("rsponse : "+rsponse);
        KXmlParser parser = null;
        ByteArrayInputStream byteArrayInputStream = null;
        InputStreamReader is = null;

        FNOTradeOrderConfirmBean tradeOrderConfirmBean = new FNOTradeOrderConfirmBean();

        byte[] currentXMLBytes;
        try
        {
                currentXMLBytes = rsponse.getBytes();
                byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes);
                is = new InputStreamReader(byteArrayInputStream);
                parser = new KXmlParser();
                parser.setInput(is);
                parser.nextTag();

                parser.require(XmlPullParser.START_TAG, null, "root");
                
                while(parser.nextTag() != XmlPullParser.END_TAG) {

                    String tagname = parser.getName().trim();

                    if(tagname.equals("order_number")) {

                    	parser.require(XmlPullParser.START_TAG, null, "order_number");
                    	tradeOrderConfirmBean.setOrderNumber(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "order_number");

                    } else if(tagname.equals("exchange")) {

                    	parser.require(XmlPullParser.START_TAG, null, "exchange");
                    	tradeOrderConfirmBean.setExchange(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "exchange");

                    } else if(tagname.equals("custId")) {

                    	parser.require(XmlPullParser.START_TAG, null, "custId");
                    	tradeOrderConfirmBean.setCustId(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "custId");

                    } else if(tagname.equals("dpId")) {

                    	parser.require(XmlPullParser.START_TAG, null, "dpId");
                    	tradeOrderConfirmBean.setDpId(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "dpId");

                    } else if(tagname.equals("error_msg")) {

                    	parser.require(XmlPullParser.START_TAG, null, "error_msg");
                    	tradeOrderConfirmBean.setErrorMessage(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "error_msg");

                    }else {
                    	parser.skipSubTree();
                    }

                }

                parser.require(XmlPullParser.END_TAG, null, "root");

         } catch(Exception ex) {

        	 LOG.print("Exception : "+ex.getMessage());

        	 ex.printStackTrace();
         }

         Vector retValue = new Vector();

         if(tradeOrderConfirmBean!=null) {
         	retValue.addElement(tradeOrderConfirmBean);
         }

         screen.setData(retValue, null);
	}

	public void exception(Exception ex) {
		// TODO Auto-generated method stub

	}

	public void setResponse(Image img) {
		// TODO Auto-generated method stub

	}

	public void setResponse(Image image, int id) {
		// TODO Auto-generated method stub

	}

	public void setResponse(Image image, String name) {
		// TODO Auto-generated method stub

	}

	public void setResponse(javax.microedition.lcdui.Image img) {
		// TODO Auto-generated method stub
		
	}

	public void setResponse(javax.microedition.lcdui.Image image, int id) {
		// TODO Auto-generated method stub
		
	}

	public void setResponse(javax.microedition.lcdui.Image image, String name) {
		// TODO Auto-generated method stub
		
	}

}
