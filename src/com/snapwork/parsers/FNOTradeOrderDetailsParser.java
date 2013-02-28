package com.snapwork.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.microedition.lcdui.Image;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

import com.snapwork.beans.FNOOrderConfirmationBean;
import com.snapwork.beans.FNOTradeOrderConfirmBean;
import com.snapwork.beans.HomeJson;
import com.snapwork.components.Screen;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;//UiApplication

public class FNOTradeOrderDetailsParser implements HttpResponse {

    private String feedurl = "";
    private Screen screen = null;
    private String scripCode = "";
    private HomeJson bannerData;

	public FNOTradeOrderDetailsParser(String url,Screen screen,String scripCode,HomeJson bannerData) {
		LOG.print("feedurl : "+url);
        this.feedurl = url;
        this.screen = screen;
        this.scripCode = scripCode;
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
        
        FNOOrderConfirmationBean fnoOrderConfirmationBean = new FNOOrderConfirmationBean();
        FNOTradeOrderConfirmBean fnoTradeOrderConfirmBean = new FNOTradeOrderConfirmBean();

        fnoOrderConfirmationBean.setScripCode(scripCode);

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
                    	fnoOrderConfirmationBean.setLtp(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "ltp");

                    } else if(tagname.equals("per_change")) {

                    	parser.require(XmlPullParser.START_TAG, null, "per_change");
                    	fnoOrderConfirmationBean.setPerChange(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "per_change");

                    } else if(tagname.equals("minlot")) {

                    	parser.require(XmlPullParser.START_TAG, null, "minlot");
                    	fnoOrderConfirmationBean.setMktLot(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "minlot");

                    } else if(tagname.equals("order_id")) {
                    	parser.require(XmlPullParser.START_TAG, null, "order_id");
                    	fnoTradeOrderConfirmBean.setOrderNumber(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "order_id");

                    } else if(tagname.equals("company_code")) {
                    	parser.require(XmlPullParser.START_TAG, null, "company_code");
                    	fnoOrderConfirmationBean.setSymbolName(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "company_code");

                    } else if(tagname.equals("exchange")) {
                    	parser.require(XmlPullParser.START_TAG, null, "exchange");
                    	fnoOrderConfirmationBean.setExchange(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "exchange");

                    } else if(tagname.equals("change")) {

                    	parser.require(XmlPullParser.START_TAG, null, "change");
                    	fnoOrderConfirmationBean.setChange(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "change");

                    } else if(tagname.equals("custId")) {

                    	parser.require(XmlPullParser.START_TAG, null, "custId");
                    	fnoOrderConfirmationBean.setCustId(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "custId");

                    } else if(tagname.equals("action")) {

                    	parser.require(XmlPullParser.START_TAG, null, "action");
                    	fnoOrderConfirmationBean.setAction(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "action");

                    } else if(tagname.equals("qty")) {

                    	parser.require(XmlPullParser.START_TAG, null, "qty");
                    	fnoOrderConfirmationBean.setQty(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "qty");

                    } else if(tagname.equals("disc_qty")) {

                    	parser.require(XmlPullParser.START_TAG, null, "disc_qty");
                    	fnoOrderConfirmationBean.setDiscQty(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "disc_qty");

                    } else if(tagname.equals("stopPrice")) {

                    	parser.require(XmlPullParser.START_TAG, null, "stopPrice");
                    	fnoOrderConfirmationBean.setStopPrice(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "stopPrice");

                    } else if(tagname.equals("orderType")) {

                    	parser.require(XmlPullParser.START_TAG, null, "orderType");
                    	fnoOrderConfirmationBean.setOrderType(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "orderType");

                    } else if(tagname.equals("type")) {

                    	parser.require(XmlPullParser.START_TAG, null, "type");
                    	fnoOrderConfirmationBean.setType(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "type");

                    } else if(tagname.equals("limitPrice")) {

                    	parser.require(XmlPullParser.START_TAG, null, "limitPrice");
                    	fnoOrderConfirmationBean.setLimitPrice(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "limitPrice");

                    } else if(tagname.equals("expiry")) {

                    	parser.require(XmlPullParser.START_TAG, null, "expiry");
                    	fnoOrderConfirmationBean.setExpiry(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "expiry");

                    } else if(tagname.equals("optType")) {

                    	parser.require(XmlPullParser.START_TAG, null, "optType");
                    	fnoOrderConfirmationBean.setOptionType(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "optType");

                    } else if(tagname.equals("strikePrice")) {

                    	parser.require(XmlPullParser.START_TAG, null, "strikePrice");
                    	fnoOrderConfirmationBean.setStrikePrice(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "strikePrice");

                    } else if(tagname.equals("validity")) {

                    	parser.require(XmlPullParser.START_TAG, null, "validity");
                    	fnoOrderConfirmationBean.setValidity(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "validity");

                    } else if(tagname.equals("dpId_data")) {

                    	parser.require(XmlPullParser.START_TAG, null, "dpId_data");

                        while(parser.nextTag() != XmlPullParser.END_TAG) {
                        	parser.require(XmlPullParser.START_TAG, null, "dpId");
                        	fnoOrderConfirmationBean.setDpId(parser.nextText());
                            parser.require(XmlPullParser.END_TAG, null, "dpId");	
                        }

                        parser.require(XmlPullParser.END_TAG, null, "dpId_data");

                    } else if(tagname.equals("page")) {

                    	parser.require(XmlPullParser.START_TAG, null, "page");
                    	fnoOrderConfirmationBean.setPage(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "page");

                    } else if(tagname.equals("instrument")) {

                    	parser.require(XmlPullParser.START_TAG, null, "instrument");
                    	fnoOrderConfirmationBean.setInstType(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "instrument");

                    } else if(tagname.equals("rmscode")) {

                    	parser.require(XmlPullParser.START_TAG, null, "rmscode");
                    	fnoOrderConfirmationBean.setRmsCode(parser.nextText());
                        parser.require(XmlPullParser.END_TAG, null, "rmscode");

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

     	 retValue.addElement(fnoOrderConfirmationBean);
     	 retValue.addElement(fnoTradeOrderConfirmBean);
     	 if(bannerData!=null)
     	 retValue.addElement(bannerData);

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