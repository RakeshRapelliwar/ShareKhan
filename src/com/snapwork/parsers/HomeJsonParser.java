package com.snapwork.parsers;

import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Image;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.snapwork.beans.HomeJson;
import com.snapwork.components.Screen;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;
import com.snapwork.view.CurrentStat;

public class HomeJsonParser implements HttpResponse {
	public String feedurl = "";
	public Vector homeData = null;
	final HomeJson hJ = new HomeJson();
	Screen screen = null;
	ThreadedComponents threadedComponents = null;
	private boolean flag;
	private ReturnDataWithId rparse;
	private int id;

	public HomeJsonParser(String url, Screen screen,
			ThreadedComponents threadedComponents) {
		// if(screen instanceof CurrentStat)
		

		this.feedurl = url;

		homeData = new Vector();
		this.screen = screen;
		this.threadedComponents = threadedComponents;
	}

	public HomeJsonParser(boolean flag) {
		this.flag = flag;
	}

	public HomeJsonParser(String url, ReturnDataWithId rparse, int id) {
		this.feedurl = url;
		homeData = new Vector();
		this.rparse = rparse;
		this.id = id;
		getScreenData();
	}

	public void getScreenData() {
		HttpProcess.threadedHttpConnection(feedurl, this);
	}

	public void setResponse(final String rsponse) {
		LOG.print(rsponse);
		final Json js = new Json(rsponse);
		LOG.print("Response Json data size " + js.getdata.size());
		
//		UiApplication.getUiApplication().invokeLater(new Runnable() {
//			
//			public void run() {
//				// TODO Auto-generated method stub
//			    Dialog.alert(""+rsponse);
//			}
//		});
		
		
		
		for (int i = 0; i < js.getdata.size(); i++) {
			final Hashtable ht = (Hashtable) js.getdata.elementAt(i);
			hJ.setCompanyCode((String) ht.get("CompanyCode"));
			hJ.setLastTradedPrice((String) ht.get("LastTradedPrice"));
			hJ.setVolume((String) ht.get("Volume"));
			hJ.setPercentageDiff((String) ht.get("PercentageDiff"));
			hJ.setFiftyTwoWeekHigh((String) ht.get("FiftyTwoWeekHigh"));
			hJ.setFiftyTwoWeekLow((String) ht.get("FiftyTwoWeekLow"));
			hJ.setLastTradedTime((String) ht.get("LastTradedTime"));
			hJ.setChangePercent((String) ht.get("ChangePercent"));
			hJ.setChange((String) ht.get("Change"));
			hJ.setMarketCap((String) ht.get("MarketCap"));
			hJ.setHigh((String) ht.get("High"));
			hJ.setLow((String) ht.get("Low"));
			hJ.setPrevClose((String) ht.get("PrevClose"));
			hJ.setOpenInterest((String) ht.get("OpenInterest"));
			hJ.setMarketStatus((String) ht.get("MarketStatus"));
			hJ.setMarketLot((String) ht.get("MarketLot"));
			hJ.setChangeInOpenInterest((String) ht.get("ChangeInOpenInterest"));
			hJ.setSymbol((String) ht.get("Symbol"));
			hJ.setDisplayName1((String) ht.get("DisplayName1"));
			hJ.setDisplayName2((String) ht.get("DisplayName2"));
			hJ.setReligareCode((String) ht.get("ReligareCode"));
			
			
			
			
			
			
			hJ.setA((String) ht.get("A"));
			
			hJ.setD((String) ht.get("D"));
			
			hJ.setS((String) ht.get("S"));
			
			
			
			
//UiApplication.getUiApplication().invokeLater(new Runnable() {
//				
//				public void run() {
//					// TODO Auto-generated method stub
//				   Dialog.alert(hJ.getA());	
//				}
//			});

			// test

			/*
			 * try{hJ.setBuyOrder((String)ht.get("BestBuyQty1"),0);}
			 * catch(Exception e){hJ.setBuyOrder("0",0);}
			 * 
			 * try{hJ.setBuyOrder((String)ht.get("BestBuyQty2"),1);}
			 * catch(Exception e){hJ.setBuyOrder("0",1);}
			 * 
			 * try{hJ.setBuyOrder((String)ht.get("BestBuyQty3"),2);}
			 * catch(Exception e){hJ.setBuyOrder("0",2);}
			 * 
			 * try{hJ.setBuyOrder((String)ht.get("BestBuyQty4"),3);}
			 * catch(Exception e){hJ.setBuyOrder("0",3);}
			 * 
			 * try{hJ.setBuyOrder((String)ht.get("BestBuyQty5"),4);}
			 * catch(Exception e){hJ.setBuyOrder("0",4);}
			 * 
			 * try{hJ.setSellOrder((String)ht.get("BestBuyQty1"),0);}
			 * catch(Exception e){hJ.setSellOrder("0",1);}
			 * 
			 * try{hJ.setSellOrder((String)ht.get("BestBuyQty2"),1);}
			 * catch(Exception e){hJ.setSellOrder("0",1);}
			 * 
			 * try{hJ.setSellOrder((String)ht.get("BestBuyQty3"),2);}
			 * catch(Exception e){hJ.setSellOrder("0",2);}
			 * 
			 * 
			 * try{hJ.setSellOrder((String)ht.get("BestBuyQty4"),3);}
			 * catch(Exception e){hJ.setSellOrder("0",3);}
			 * 
			 * try{hJ.setSellOrder((String)ht.get("BestBuyQty5"),4);}
			 * catch(Exception e){hJ.setSellOrder("0",4);}
			 */

			// test
			try {
				hJ.setBuyQty((String) ht.get("BestBuyQty1"), 0);
			} catch (Exception e) {
				hJ.setBuyQty("0", 0);
			}

			try {
				hJ.setBuyQty((String) ht.get("BestBuyQty2"), 1);
			} catch (Exception e) {
				hJ.setBuyQty("0", 1);
			}

			try {
				hJ.setBuyQty((String) ht.get("BestBuyQty3"), 2);
			} catch (Exception e) {
				hJ.setBuyQty("0", 2);
			}

			try {
				hJ.setBuyQty((String) ht.get("BestBuyQty4"), 3);
			} catch (Exception e) {
				hJ.setBuyQty("0", 3);
			}

			try {
				hJ.setBuyQty((String) ht.get("BestBuyQty5"), 4);
			} catch (Exception e) {
				hJ.setBuyQty("0", 4);
			}

			try {
				hJ.setSellQty((String) ht.get("BestSellQty1"), 0);
			} catch (Exception e) {
				hJ.setSellQty("0", 0);
			}

			try {
				hJ.setSellQty((String) ht.get("BestSellQty2"), 1);
			} catch (Exception e) {
				hJ.setSellQty("0", 1);
			}

			try {
				hJ.setSellQty((String) ht.get("BestSellQty3"), 2);
			} catch (Exception e) {
				hJ.setSellQty("0", 2);
			}

			try {
				hJ.setSellQty((String) ht.get("BestSellQty4"), 3);
			} catch (Exception e) {
				hJ.setSellQty("0", 3);
			}

			try {
				hJ.setSellQty((String) ht.get("BestSellQty5"), 4);
			} catch (Exception e) {
				hJ.setSellQty("0", 4);
			}

			try {
				hJ.setBuyPrice((String) ht.get("Buyrate1"), 0);
			} catch (Exception e) {
				hJ.setBuyPrice("0", 0);
			}

			try {
				hJ.setBuyPrice((String) ht.get("Buyrate2"), 1);
			} catch (Exception e) {
				hJ.setBuyPrice("0", 1);
			}

			try {
				hJ.setBuyPrice((String) ht.get("Buyrate3"), 2);
			} catch (Exception e) {
				hJ.setBuyPrice("0", 2);
			}

			try {
				hJ.setBuyPrice((String) ht.get("Buyrate4"), 3);
			} catch (Exception e) {
				hJ.setBuyPrice("0", 3);
			}

			try {
				hJ.setBuyPrice((String) ht.get("Buyrate5"), 4);
			} catch (Exception e) {
				hJ.setBuyPrice("0", 4);
			}

			try {
				hJ.setSellPrice((String) ht.get("Sellrate1"), 0);
			} catch (Exception e) {
				hJ.setSellPrice("0", 0);
			}

			try {
				hJ.setSellPrice((String) ht.get("Sellrate2"), 1);
			} catch (Exception e) {
				hJ.setSellPrice("0", 1);
			}

			try {
				hJ.setSellPrice((String) ht.get("Sellrate3"), 2);
			} catch (Exception e) {
				hJ.setSellPrice("0", 2);
			}

			try {
				hJ.setSellPrice((String) ht.get("Sellrate4"), 3);
			} catch (Exception e) {
				hJ.setSellPrice("0", 3);
			}

			try {
				hJ.setSellPrice((String) ht.get("Sellrate5"), 4);
			} catch (Exception e) {
				hJ.setSellPrice("0", 4);
			}

			homeData.addElement(hJ.copy());
		}
		if (rparse != null) {
			rparse.setData(homeData, id);
		} else {
			if (!this.flag)
				screen.setData(homeData, threadedComponents);
		}
	}

	public static Vector getVector(String rsponse) {
		LOG.print("HomeJson getVector : " + rsponse);
		Vector hData = new Vector();
		HomeJson h = new HomeJson();
		Json js = new Json(rsponse);
		LOG.print("HomeJson getVector Json Size: " + js.getdata.size());
		for (int i = 0; i < js.getdata.size(); i++) {
			Hashtable ht = (Hashtable) js.getdata.elementAt(i);
			h.setCompanyCode((String) ht.get("CompanyCode"));
			h.setLastTradedPrice((String) ht.get("LastTradedPrice"));
			h.setVolume((String) ht.get("Volume"));
			h.setPercentageDiff((String) ht.get("PercentageDiff"));
			h.setFiftyTwoWeekHigh((String) ht.get("FiftyTwoWeekHigh"));
			h.setFiftyTwoWeekLow((String) ht.get("FiftyTwoWeekLow"));
			h.setLastTradedTime((String) ht.get("LastTradedTime"));
			h.setChangePercent((String) ht.get("ChangePercent"));
			h.setChange((String) ht.get("Change"));
			h.setMarketCap((String) ht.get("MarketCap"));
			h.setHigh((String) ht.get("High"));
			h.setLow((String) ht.get("Low"));
			h.setPrevClose((String) ht.get("PrevClose"));
			h.setOpenInterest((String) ht.get("OpenInterest"));
			h.setMarketLot((String) ht.get("MarketLot"));
			h.setMarketStatus((String) ht.get("MarketStatus"));
			h.setChangeInOpenInterest((String) ht.get("ChangeInOpenInterest"));
			h.setSymbol((String) ht.get("Symbol"));
			h.setDisplayName1((String) ht.get("DisplayName1"));
			h.setDisplayName2((String) ht.get("DisplayName2"));
			h.setReligareCode((String) ht.get("ReligareCode"));
			
			
			
			
h.setA((String) ht.get("A"));
			
			h.setD((String) ht.get("D"));
			
			h.setS((String) ht.get("S"));
			
			// test

			/*
			 * try{h.setBuyOrder((String)ht.get("BestBuyQty1"),0);}
			 * catch(Exception e){h.setBuyOrder("0",0);}
			 * 
			 * try{h.setBuyOrder((String)ht.get("BestBuyQty2"),1);}
			 * catch(Exception e){h.setBuyOrder("0",1);}
			 * 
			 * try{h.setBuyOrder((String)ht.get("BestBuyQty3"),2);}
			 * catch(Exception e){h.setBuyOrder("0",2);}
			 * 
			 * try{h.setBuyOrder((String)ht.get("BestBuyQty4"),3);}
			 * catch(Exception e){h.setBuyOrder("0",3);}
			 * 
			 * try{h.setBuyOrder((String)ht.get("BestBuyQty5"),4);}
			 * catch(Exception e){h.setBuyOrder("0",4);}
			 * 
			 * try{h.setSellOrder((String)ht.get("BestBuyQty1"),0);}
			 * catch(Exception e){h.setSellOrder("0",1);}
			 * 
			 * try{h.setSellOrder((String)ht.get("BestBuyQty2"),1);}
			 * catch(Exception e){h.setSellOrder("0",1);}
			 * 
			 * try{h.setSellOrder((String)ht.get("BestBuyQty3"),2);}
			 * catch(Exception e){h.setSellOrder("0",2);}
			 * 
			 * 
			 * try{h.setSellOrder((String)ht.get("BestBuyQty4"),3);}
			 * catch(Exception e){h.setSellOrder("0",3);}
			 * 
			 * try{h.setSellOrder((String)ht.get("BestBuyQty5"),4);}
			 * catch(Exception e){h.setSellOrder("0",4);}
			 */

			// test
			try {
				h.setBuyQty((String) ht.get("BestBuyQty1"), 0);
			} catch (Exception e) {
				h.setBuyQty("0", 0);
			}

			try {
				h.setBuyQty((String) ht.get("BestBuyQty2"), 1);
			} catch (Exception e) {
				h.setBuyQty("0", 1);
			}

			try {
				h.setBuyQty((String) ht.get("BestBuyQty3"), 2);
			} catch (Exception e) {
				h.setBuyQty("0", 2);
			}

			try {
				h.setBuyQty((String) ht.get("BestBuyQty4"), 3);
			} catch (Exception e) {
				h.setBuyQty("0", 3);
			}

			try {
				h.setBuyQty((String) ht.get("BestBuyQty5"), 4);
			} catch (Exception e) {
				h.setBuyQty("0", 4);
			}

			try {
				h.setSellQty((String) ht.get("BestSellQty1"), 0);
			} catch (Exception e) {
				h.setSellQty("0", 0);
			}

			try {
				h.setSellQty((String) ht.get("BestSellQty2"), 1);
			} catch (Exception e) {
				h.setSellQty("0", 1);
			}

			try {
				h.setSellQty((String) ht.get("BestSellQty3"), 2);
			} catch (Exception e) {
				h.setSellQty("0", 2);
			}

			try {
				h.setSellQty((String) ht.get("BestSellQty4"), 3);
			} catch (Exception e) {
				h.setSellQty("0", 3);
			}

			try {
				h.setSellQty((String) ht.get("BestSellQty5"), 4);
			} catch (Exception e) {
				h.setSellQty("0", 4);
			}

			try {
				h.setBuyPrice((String) ht.get("Buyrate1"), 0);
			} catch (Exception e) {
				h.setBuyPrice("0", 0);
			}

			try {
				h.setBuyPrice((String) ht.get("Buyrate2"), 1);
			} catch (Exception e) {
				h.setBuyPrice("0", 1);
			}

			try {
				h.setBuyPrice((String) ht.get("Buyrate3"), 2);
			} catch (Exception e) {
				h.setBuyPrice("0", 2);
			}

			try {
				h.setBuyPrice((String) ht.get("Buyrate4"), 3);
			} catch (Exception e) {
				h.setBuyPrice("0", 3);
			}

			try {
				h.setBuyPrice((String) ht.get("Buyrate5"), 4);
			} catch (Exception e) {
				h.setBuyPrice("0", 4);
			}

			try {
				h.setSellPrice((String) ht.get("Sellrate1"), 0);
			} catch (Exception e) {
				h.setSellPrice("0", 0);
			}

			try {
				h.setSellPrice((String) ht.get("Sellrate2"), 1);
			} catch (Exception e) {
				h.setSellPrice("0", 1);
			}

			try {
				h.setSellPrice((String) ht.get("Sellrate3"), 2);
			} catch (Exception e) {
				h.setSellPrice("0", 2);
			}

			try {
				h.setSellPrice((String) ht.get("Sellrate4"), 3);
			} catch (Exception e) {
				h.setSellPrice("0", 3);
			}

			try {
				h.setSellPrice((String) ht.get("Sellrate5"), 4);
			} catch (Exception e) {
				h.setSellPrice("0", 4);
			}

			hData.addElement(h.copy());
		}
		return hData;
	}

	public void exception(Exception ex) {

	}

	public void setResponse(Image img, int id) {
	}

	public void setResponse(Image img) {
	}

	public void setResponse(Image image, String name) {
	}
}