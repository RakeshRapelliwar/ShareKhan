package com.snapwork.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.microedition.lcdui.Image;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

import com.snapwork.beans.FNOOrderConfirmationBean;
import com.snapwork.beans.HomeJson;
import com.snapwork.components.Screen;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;

//To Work Upon


public class FNOTradeOrderTurnOverParser  implements HttpResponse {

    private String feedurl = "";
    private Screen screen = null;
    private HomeJson bannerData;

	public FNOTradeOrderTurnOverParser(String url,Screen screen,HomeJson bannerData) {
        this.feedurl = url;
        this.screen = screen;
        this.bannerData = bannerData;
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

        FNOOrderConfirmationBean fnoTradeOrderConfirmationBean = new FNOOrderConfirmationBean();

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

                    if(tagname.equals("symbolName")) {

                    	parser.require(XmlPullParser.START_TAG, null, "symbolName");
                    	fnoTradeOrderConfirmationBean.setSymbolName(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "symbolName");
                    } else if(tagname.equals("expiry")) {

                    	parser.require(XmlPullParser.START_TAG, null, "expiry");
                    	fnoTradeOrderConfirmationBean.setExpiry(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "expiry");

                    }  else if(tagname.equals("action")) {

                        	parser.require(XmlPullParser.START_TAG, null, "action");
                        	fnoTradeOrderConfirmationBean.setAction(parser.nextText());
                            parser.require(XmlPullParser.END_TAG, null, "action");

                    }  else if(tagname.equals("qty")) {

                    	parser.require(XmlPullParser.START_TAG, null, "qty");
                    	fnoTradeOrderConfirmationBean.setQty(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "qty");

                    }  else if(tagname.equals("mkt-lots")) {

                    	parser.require(XmlPullParser.START_TAG, null, "mkt-lots");
                    	fnoTradeOrderConfirmationBean.setMktLot(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "mkt-lots");

                    }  else if(tagname.equals("optType")) {

                    	parser.require(XmlPullParser.START_TAG, null, "optType");
                    	fnoTradeOrderConfirmationBean.setOptionType(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "optType");

                    }  else if(tagname.equals("strikePrice")) {

                    	parser.require(XmlPullParser.START_TAG, null, "strikePrice");
                    	fnoTradeOrderConfirmationBean.setStrikePrice(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "strikePrice");

                    } else if(tagname.equals("ltp")) {

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

                    } else {
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
         
         if(bannerData!=null) {
        	 retValue.addElement(bannerData);
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
