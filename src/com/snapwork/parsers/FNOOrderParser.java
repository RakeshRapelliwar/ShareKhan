package com.snapwork.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.microedition.lcdui.Image;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

import com.snapwork.beans.FNOOrderConfirmationBean;
import com.snapwork.components.Screen;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;

//To Work Upon


public class FNOOrderParser  implements HttpResponse {

    private String feedurl = "";
    private FNOOrderConfirmationBean fnoTradeOrderConfirmationBean;
    private Screen screen = null;

	public FNOOrderParser(String url,FNOOrderConfirmationBean fnoTradeOrderConfirmationBean,Screen screen) {
		LOG.print(url);
        this.feedurl = url;
        this.fnoTradeOrderConfirmationBean = fnoTradeOrderConfirmationBean;
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

        if(fnoTradeOrderConfirmationBean==null) {
        	fnoTradeOrderConfirmationBean = new FNOOrderConfirmationBean();
        }

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

                    if(tagname.equals("ltp")) {

                    	parser.require(XmlPullParser.START_TAG, null, "ltp");
                    	fnoTradeOrderConfirmationBean.setLtp(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "ltp");

                    } else if(tagname.equals("per_change")) {

                    	parser.require(XmlPullParser.START_TAG, null, "per_change");
                    	fnoTradeOrderConfirmationBean.setPerChange(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "per_change");

                    } else if(tagname.equals("change")) {

                    	parser.require(XmlPullParser.START_TAG, null, "change");
                    	fnoTradeOrderConfirmationBean.setChange(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "change");

                    } else if(tagname.equals("dpId_data")) {

                    	parser.require(XmlPullParser.START_TAG, null, "dpId_data");

                        while(parser.nextTag() != XmlPullParser.END_TAG) {
                        	parser.require(XmlPullParser.START_TAG, null, "dpId");
                        	fnoTradeOrderConfirmationBean.setDpId(parser.nextText());
                            parser.require(XmlPullParser.END_TAG, null, "dpId");	
                        }

                        parser.require(XmlPullParser.END_TAG, null, "dpId_data");

                    } else if(tagname.equals("page")) {

                    	parser.require(XmlPullParser.START_TAG, null, "page");
                    	fnoTradeOrderConfirmationBean.setPage(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "page");

                    } else if(tagname.equals("instrument")) {

                    	parser.require(XmlPullParser.START_TAG, null, "instrument");
                    	fnoTradeOrderConfirmationBean.setInstType(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "instrument");

                    } 
                    else {
                    	parser.skipSubTree();
                    }

                }

                parser.require(XmlPullParser.END_TAG, null, "root");

         } catch(Exception ex) {

        	 LOG.print("Exception : "+ex.getMessage());

        	 ex.printStackTrace();
         }

         Vector retValue = new Vector();

         if(fnoTradeOrderConfirmationBean!=null) {
         	retValue.addElement(fnoTradeOrderConfirmationBean);
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

}
