package com.snapwork.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.microedition.lcdui.Image;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

import com.snapwork.beans.FNOTradeBean;
import com.snapwork.beans.HomeJson;
import com.snapwork.components.Screen;
import com.snapwork.util.FNOTradeConfiguration;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;

public class FNOTradeParser implements HttpResponse {

    private String feedurl = "";
    private String indexName = "";
    private Screen screen = null;
    private FNOTradeConfiguration fnoTradeConfiguration;
    private HomeJson bannerData;
    private String date;

	public FNOTradeParser(String url,String indexName,Screen screen,FNOTradeConfiguration fnoTradeConfiguration,HomeJson bannerData, String date) {
		LOG.print("feedurl : "+url);
        this.feedurl = url;
        this.indexName = indexName;
        this.screen = screen;
        this.fnoTradeConfiguration = fnoTradeConfiguration;
        
        this.bannerData = bannerData;
        this.date = date;
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

        FNOTradeBean fnoTradeBean = new FNOTradeBean();
        fnoTradeBean.setIndexName(indexName);

        byte[] currentXMLBytes;
        try
        {
                currentXMLBytes = rsponse.getBytes();
                byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes);
                is = new InputStreamReader(byteArrayInputStream);
                parser = new KXmlParser();
                parser.setInput(is);
                parser.nextTag();

                parser.require(XmlPullParser.START_TAG, null, "Data");

                while(parser.nextTag() != XmlPullParser.END_TAG) {

                    String tagname = parser.getName().trim();

                    if(tagname.equals("ExpiryData")) {

                    	parser.require(XmlPullParser.START_TAG, null, "ExpiryData");

                        while(parser.nextTag() != XmlPullParser.END_TAG) {
                        	parser.require(XmlPullParser.START_TAG, null, "Expiry");
                        	fnoTradeBean.addExpiryData(parser.nextText());
                            parser.require(XmlPullParser.END_TAG, null, "Expiry");	
                        }

                        parser.require(XmlPullParser.END_TAG, null, "ExpiryData");

                    } else if(tagname.equals("MinLot")) {

                    	parser.require(XmlPullParser.START_TAG, null, "MinLot");
                    	fnoTradeBean.setMinLot(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "MinLot");

                    } else if(tagname.equals("flag")) {

                    	parser.require(XmlPullParser.START_TAG, null, "flag");
                    	fnoTradeBean.setFlag(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "flag");

                    } else if(tagname.equals("StrikePriceData")) {

                        parser.require(XmlPullParser.START_TAG, null, "StrikePriceData");

                        while(parser.nextTag() != XmlPullParser.END_TAG) {
                        	parser.require(XmlPullParser.START_TAG, null, "StrikePrice");
                        	fnoTradeBean.addStrikeData(parser.nextText());
                            parser.require(XmlPullParser.END_TAG, null, "StrikePrice");	
                        }

                        parser.require(XmlPullParser.END_TAG, null, "StrikePriceData");

                    } else {
                    	parser.skipSubTree();
                    }

                }

                parser.require(XmlPullParser.END_TAG, null, "Data");

         } catch(Exception ex) {

        	 LOG.print("Exception : "+ex.getMessage());

        	 ex.printStackTrace();
         }

         Vector retValue = new Vector();

         if(fnoTradeBean!=null) {
         	retValue.addElement(fnoTradeBean);
         	retValue.addElement(fnoTradeConfiguration);
         }
         
         if(bannerData!=null) {
        	 retValue.addElement(bannerData);
         }
         retValue.addElement(date);

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
