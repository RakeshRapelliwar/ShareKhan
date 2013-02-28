package com.snapwork.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Vector;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiEngineInstance;
import net.rim.device.api.ui.component.LabelField;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.beans.FNOOrderConfirmationBean;
import com.snapwork.beans.FNOTradeOrderConfirmBean;
import com.snapwork.beans.HomeJson;
import com.snapwork.components.HomeScreenBanner;

public class Utils
{
        public static HomeScreenBanner BSEBANNER = null;
        public static long BSEBANNER_TIME = 0;
        public static String LASTUPDATETIME = "";
        public static int ENTRY_NEWS_COMMENTARY_DEFAULT = 0;
        public final static int NEWS = 1;
        public final static int COMMENTARY = 2;
        public final static int DEAFULT = 0;
        public static boolean FNO_SEARCH = false;
        public static boolean WATCHLIST;
        public static boolean WATCHLIST_MODE;
        public static String WATCHLIST_NAME = "";
        public static String WATCHLIST_LABEL = "";
        public static boolean LOGIN_STATUS = false;
        private static Vector vectorWatchlist;
        private static Vector vectorWatchlistData;
        private static Vector vectorWatchlistDataMain = null;
        public static boolean firstWebViewLoad;
        public static boolean sessionAlive = true;
        public static boolean MARKET_CLOSED = false;
        public static long sessionExpiredTime = 0;
        public static String WATCHLIST_QUOTE = "";
        public static boolean WATCHLIST_CLOSE = false;
        public static int WATCHLIST_INDEX = 0;
        public static boolean STOCK_PAGE_START;
        public static String NSE_SYMBOL = "";
        //public static Vector preLoaded = null;
        public static byte getCommandFromNotificationType(String notificationType)
        {
                try
                {
                        //System.out.println("notificationType : "+notificationType);

                        if(notificationType==null)
                        {
                                return ActionCommand.CMD_HOME_SCREEN; 
                        }
                        if(notificationType.toLowerCase().equals("news"))
                        {
                                return ActionCommand.CMD_TRADE_VIEW_SCREEN;
                        }
                        else if(notificationType.toLowerCase().equals("commentary"))
                        {
                                return ActionCommand.CMD_COMMENTARY_SCREEN;
                        }
                        else
                        {
                                return ActionCommand.CMD_HOME_SCREEN;
                        }
                }
                catch(Exception ex)
                {
                        Debug.debug("Error : "+ex.toString());
                }
                return ActionCommand.CMD_HOME_SCREEN;
        }
        
        public static String getBannersDataProvideUrl() {
            return Utils.urlEncode(AppConstants.bannersDataProvideUrl);
        }
        
        public static String getMarketStatusUrl() {
            return Utils.urlEncode(AppConstants.marketStatusUrl);
        }
        
        //Create Auth URL
       /* public static String getAuthURL(String userType,String strName, String strEmail,String strMobile)
        {
                String strURL = AppConstants.userRegistrationUrl;
                strURL = Utils.findAndReplace(strURL,"##USERTYPE##",userType);
                strURL = Utils.findAndReplace(strURL,"##NAME##",strName);
                strURL = Utils.findAndReplace(strURL,"##PASSWORD##",strEmail);
                strURL = Utils.findAndReplace(strURL,"##TPASSWORD##",strMobile);
                return Utils.urlEncode(strURL);
        }*/
        public static String getAuthURL(String userType,String strName, String strEmail,String strMobile, String imei, String version)
        {
                String strURL = AppConstants.userRegistrationUrl;
                strURL = Utils.findAndReplace(strURL,"##USERTYPE##",userType);
                strURL = Utils.findAndReplace(strURL,"##NAME##",strName);
                strURL = Utils.findAndReplace(strURL,"##PASSWORD##",strEmail);
                strURL = Utils.findAndReplace(strURL,"##TPASSWORD##",strMobile);
                strURL = Utils.findAndReplace(strURL,"##IMEI##",imei);
                strURL = Utils.findAndReplace(strURL,"##VERSION##",version);
                return Utils.urlEncode(strURL);
        }
        
        public static String getMemberShipChangeProfileURL(String loginID,String oldPassword, String newPassword, String colFlag, String custId)
        {
                String strURL = AppConstants.profileChangeMRUrl;
                strURL = Utils.findAndReplace(strURL,"##LOGINID##",loginID);
                strURL = Utils.findAndReplace(strURL,"##OLDPASSWORD##",oldPassword);
                strURL = Utils.findAndReplace(strURL,"##NEWPASSWORD##",newPassword);
                strURL = Utils.findAndReplace(strURL,"##COLFLAG##",colFlag);
                strURL = Utils.findAndReplace(strURL,"##CUSTID##",custId);
                return Utils.urlEncode(strURL);
        }
        
        public static String getTradingChangeProfileURL(String loginID,String oldPassword, String newPassword, String colFlag, String custId)
        {
                String strURL = AppConstants.profileChangeTRUrl;
                strURL = Utils.findAndReplace(strURL,"##LOGINID##",loginID);
                strURL = Utils.findAndReplace(strURL,"##OLDPASSWORD##",oldPassword);
                strURL = Utils.findAndReplace(strURL,"##NEWPASSWORD##",newPassword);
                strURL = Utils.findAndReplace(strURL,"##COLFLAG##",colFlag);
                strURL = Utils.findAndReplace(strURL,"##CUSTID##",custId);
                return Utils.urlEncode(strURL);
        }
        
        public static String getUpdateQAURL(String strURL, String loginID,String oldPassword, String newPassword)
        {
                //String strURL = AppConstants.updateQAMembership;
                strURL = Utils.findAndReplace(strURL,"##UserName##",loginID);
                strURL = Utils.findAndReplace(strURL,"##HintQ##",oldPassword);
                strURL = Utils.findAndReplace(strURL,"##HintA##",newPassword);
                return Utils.urlEncode(strURL);
        }
        
        /*public static String getUpdateQATradingURL(String strURL, String loginID,String oldPassword, String newPassword)
        {
                //String strURL = AppConstants.updateQATrading;
                strURL = Utils.findAndReplace(strURL,"##UserName##",loginID);
                strURL = Utils.findAndReplace(strURL,"##HintQ##",oldPassword);
                strURL = Utils.findAndReplace(strURL,"##HintA##",newPassword);
                return Utils.urlEncode(strURL);
        }*/

        //Create News URL
        public static String getNewsURL(String pageNo)
        {
                String strURL = AppConstants.newsDataProviderUrl;
                strURL = Utils.findAndReplace(strURL,"##PAGENO##",pageNo);
                LOG.print("news URL -------------- strURL : "+strURL);
                return Utils.urlEncode(strURL);
        }
        
        //Create OrderBook
        public static String getReportsFNOURL(String userID)
        {
                String strURL = AppConstants.reportsFNOURL;
                strURL = Utils.findAndReplace(strURL,"##UserID##",userID);
                return Utils.urlEncode(strURL);
        }
        
        //Create Holdings
        public static String getHoldingsURL(String userID)
        {
                String strURL = AppConstants.holdingsURL;
                strURL = Utils.findAndReplace(strURL,"##UserID##",userID);
                return Utils.urlEncode(strURL);
        }
        
        //Create Reports
        public static String getReportsEquityURL(String userID)
        {
                String strURL = AppConstants.reportsEquityURL;
                strURL = Utils.findAndReplace(strURL,"##UserID##",userID);
                return Utils.urlEncode(strURL);
        }
        
        //Create Statements
        public static String getAccountsURL(String userID)
        {
                String strURL = AppConstants.accountsURL;
                strURL = Utils.findAndReplace(strURL,"##UserID##",userID);
                return Utils.urlEncode(strURL);
        }
        

        //FNO URLS 
        public static String getFNOTradeDetailsURL(String compcode, String userID)
        {
                String strURL = AppConstants.fnoTradeDetailsURL;
                strURL = Utils.findAndReplace(strURL,"##COMPCODE##",compcode);
                strURL = Utils.findAndReplace(strURL,"##UserID##",userID);
                return Utils.urlEncode(strURL);
        }
        
        public static String getFNOTradeOrderDetailsURL(String OrderID, String userID,String exchange,String rmsCode,String page)
        {
                String strURL = AppConstants.fnoTradeOrderDetailsURL;
                strURL = Utils.findAndReplace(strURL,"##ORDERID##",OrderID);
                strURL = Utils.findAndReplace(strURL,"##UserID##",userID);
                strURL = Utils.findAndReplace(strURL,"##EXCHANGE##",exchange);
                strURL = Utils.findAndReplace(strURL,"##RMSCODE##",rmsCode);
                if(page.equalsIgnoreCase("ReportsOrderf")) {
                    strURL = Utils.findAndReplace(strURL,"##PAGE##","reports");
                } else if(page.equalsIgnoreCase("OrderBKF&O")) {
                    strURL = Utils.findAndReplace(strURL,"##PAGE##","orderBK");                	
                }
                return Utils.urlEncode(strURL);
        }
        
        public static String getFNOPostURL(FNOOrderConfirmationBean confirmationBean) {

                String strURL = AppConstants.fnoTradePlaceOrderURL;
                
                strURL = Utils.findAndReplace(strURL,"##COMPCODE##",confirmationBean.getSymbolName());
                strURL = Utils.findAndReplace(strURL,"##EXCHANGE##",confirmationBean.getExchange());
                strURL = Utils.findAndReplace(strURL,"##EXPIRY##",confirmationBean.getExpiry());
                strURL = Utils.findAndReplace(strURL,"##LTP##",confirmationBean.getLtp());
                strURL = Utils.findAndReplace(strURL,"##INDEXTYPE##",confirmationBean.getInstType());
                strURL = Utils.findAndReplace(strURL,"##OPTYPE##",confirmationBean.getOptionType());
                strURL = Utils.findAndReplace(strURL,"##STRIKEPRICE##",confirmationBean.getStrikePrice());
                strURL = Utils.findAndReplace(strURL,"##MKTLOTS##",confirmationBean.getMktLot());
                strURL = Utils.findAndReplace(strURL,"##ACTION##",confirmationBean.getAction());
                strURL = Utils.findAndReplace(strURL,"##QTY##",confirmationBean.getQty());
                strURL = Utils.findAndReplace(strURL,"##DISCQTY##",confirmationBean.getDiscQty());
                strURL = Utils.findAndReplace(strURL,"##ORDERTYPE##",confirmationBean.getOrderType());
               // strURL = Utils.findAndReplace(strURL,"##LIMITPRICE##",confirmationBean.getLimitPrice());
                if(confirmationBean.getOrderType().equalsIgnoreCase("market"))
                {
                	strURL = Utils.findAndReplace(strURL,"&limitPrice=##LIMITPRICE##","");
                }
                else
                	strURL = Utils.findAndReplace(strURL,"##LIMITPRICE##",confirmationBean.getLimitPrice());
                 strURL = Utils.findAndReplace(strURL,"##STOPPRICE##",confirmationBean.getStopPrice());
                strURL = Utils.findAndReplace(strURL,"##PERCHANGE##",confirmationBean.getPerChange());
                strURL = Utils.findAndReplace(strURL,"##TYPE##",confirmationBean.getType());
                strURL = Utils.findAndReplace(strURL,"##CHANGE##",confirmationBean.getChange());
                strURL = Utils.findAndReplace(strURL,"##VALIDITY##",confirmationBean.getValidity());
                strURL = Utils.findAndReplace(strURL,"##USERID##",confirmationBean.getCustId());
                strURL = Utils.findAndReplace(strURL,"##DPID##",confirmationBean.getDpId());
                
                return strURL;
        }
        
        
        public static String getFNOPostModifyURL(FNOOrderConfirmationBean fnoOrderConfirmationBean) {

            String strURL = AppConstants.fnoTradeModifyOrderURL;

            strURL = Utils.findAndReplace(strURL,"##COMPCODE##",fnoOrderConfirmationBean.getSymbolName());
            strURL = Utils.findAndReplace(strURL,"##LTP##",fnoOrderConfirmationBean.getLtp());
            strURL = Utils.findAndReplace(strURL,"##PERCHANGE##",fnoOrderConfirmationBean.getPerChange());
            strURL = Utils.findAndReplace(strURL,"##CHANGE##",fnoOrderConfirmationBean.getChange());
            strURL = Utils.findAndReplace(strURL,"##USERID##",fnoOrderConfirmationBean.getCustId());
            strURL = Utils.findAndReplace(strURL,"##ORDERID##",fnoOrderConfirmationBean.getOrderNumber());
            strURL = Utils.findAndReplace(strURL,"##ACTION##",fnoOrderConfirmationBean.getAction());
            strURL = Utils.findAndReplace(strURL,"##INDEXTYPE##",fnoOrderConfirmationBean.getInstType());
            strURL = Utils.findAndReplace(strURL,"##OPTYPE##",fnoOrderConfirmationBean.getOptionType());
            strURL = Utils.findAndReplace(strURL,"##STRIKEPRICE##",fnoOrderConfirmationBean.getStrikePrice());
			 strURL = Utils.findAndReplace(strURL,"##EXPIRY##",fnoOrderConfirmationBean.getExpiry());
             strURL = Utils.findAndReplace(strURL,"##OREQ##",fnoOrderConfirmationBean.getEQ());
			 strURL = Utils.findAndReplace(strURL,"##MKTLOT##",fnoOrderConfirmationBean.getMktLot());
            strURL = Utils.findAndReplace(strURL,"##RMS##",fnoOrderConfirmationBean.getRmsCode());
			strURL = Utils.findAndReplace(strURL,"##TOKEN##",fnoOrderConfirmationBean.getToken());
            strURL = Utils.findAndReplace(strURL,"##QTY##",fnoOrderConfirmationBean.getQty());
            strURL = Utils.findAndReplace(strURL,"##ORDERTYPE##",fnoOrderConfirmationBean.getOrderType());
            if(fnoOrderConfirmationBean.getOrderType().equalsIgnoreCase("market"))
            {
            	strURL = Utils.findAndReplace(strURL,"&limitPrice=##LIMITPRICE##","");
            }
            else
            	strURL = Utils.findAndReplace(strURL,"##LIMITPRICE##",fnoOrderConfirmationBean.getLimitPrice());
            strURL = Utils.findAndReplace(strURL,"##STOPPRICE##",fnoOrderConfirmationBean.getStopPrice());
            strURL = Utils.findAndReplace(strURL,"##VALIDITY##",fnoOrderConfirmationBean.getValidity());

            return strURL;

    }
        public static String getFNOPostSQ2TradeURL(FNOOrderConfirmationBean fnoOrderConfirmationBean) {

            String strURL = AppConstants.fnoTradeSQ2URL;

            strURL = Utils.findAndReplace(strURL,"##COMPCODE##",fnoOrderConfirmationBean.getSymbolName());
            strURL = Utils.findAndReplace(strURL,"##USERID##",fnoOrderConfirmationBean.getCustId());
           strURL = Utils.findAndReplace(strURL,"##ACTION##",fnoOrderConfirmationBean.getAction());
            strURL = Utils.findAndReplace(strURL,"##QTY##",fnoOrderConfirmationBean.getQty());
            return strURL;

    }
        public static String getFNOPostSQ3TradeURL(FNOOrderConfirmationBean fnoOrderConfirmationBean) {

            String strURL = AppConstants.fnoTradeSQ3URL;

            strURL = Utils.findAndReplace(strURL,"##COMPCODE##",fnoOrderConfirmationBean.getSymbolName());
            strURL = Utils.findAndReplace(strURL,"##USERID##",fnoOrderConfirmationBean.getCustId());
            strURL = Utils.findAndReplace(strURL,"##DISCQTY##",fnoOrderConfirmationBean.getDiscQty());
            strURL = Utils.findAndReplace(strURL,"##MKTLOT##",fnoOrderConfirmationBean.getMktLot());
            strURL = Utils.findAndReplace(strURL,"##INDEXTYPE##",fnoOrderConfirmationBean.getInstType());
            strURL = Utils.findAndReplace(strURL,"##EXPIRY##",fnoOrderConfirmationBean.getExpiry());
            strURL = Utils.findAndReplace(strURL,"##OPTYPE##",fnoOrderConfirmationBean.getOptionType());
            strURL = Utils.findAndReplace(strURL,"##STRIKEPRICE##",fnoOrderConfirmationBean.getStrikePrice());
            strURL = Utils.findAndReplace(strURL,"##ACTION##",fnoOrderConfirmationBean.getAction());
            strURL = Utils.findAndReplace(strURL,"##QTY##",fnoOrderConfirmationBean.getQty());
            strURL = Utils.findAndReplace(strURL,"##ORDERTYPE##",fnoOrderConfirmationBean.getOrderType());
            
            if(fnoOrderConfirmationBean.getOrderType().equalsIgnoreCase("market"))
            {
            	strURL = Utils.findAndReplace(strURL,"&limitPrice=##LIMITPRICE##","");
            }
            else
            	strURL = Utils.findAndReplace(strURL,"##LIMITPRICE##",fnoOrderConfirmationBean.getLimitPrice());
           // strURL = Utils.findAndReplace(strURL,"##LIMITPRICE##",fnoOrderConfirmationBean.getLimitPrice());
            strURL = Utils.findAndReplace(strURL,"##STOPPRICE##",fnoOrderConfirmationBean.getStopPrice());
            strURL = Utils.findAndReplace(strURL,"##VALIDITY##",fnoOrderConfirmationBean.getValidity());
            return strURL;

    }
        
        public static String getFNOPostSQ4TradeURL(FNOOrderConfirmationBean fnoOrderConfirmationBean) {

            String strURL = AppConstants.fnoTradeSQ4URL;

            strURL = Utils.findAndReplace(strURL,"##COMPCODE##",fnoOrderConfirmationBean.getSymbolName());
            strURL = Utils.findAndReplace(strURL,"##USERID##",fnoOrderConfirmationBean.getCustId());
            strURL = Utils.findAndReplace(strURL,"##DISCQTY##",fnoOrderConfirmationBean.getDiscQty());
            strURL = Utils.findAndReplace(strURL,"##MKTLOT##",fnoOrderConfirmationBean.getMktLot());
            strURL = Utils.findAndReplace(strURL,"##INDEXTYPE##",fnoOrderConfirmationBean.getInstType());
            strURL = Utils.findAndReplace(strURL,"##EXPIRY##",fnoOrderConfirmationBean.getExpiry());
            strURL = Utils.findAndReplace(strURL,"##OPTYPE##",fnoOrderConfirmationBean.getOptionType());
            strURL = Utils.findAndReplace(strURL,"##STRIKEPRICE##",fnoOrderConfirmationBean.getStrikePrice());
            strURL = Utils.findAndReplace(strURL,"##ACTION##",fnoOrderConfirmationBean.getAction());
            strURL = Utils.findAndReplace(strURL,"##QTY##",fnoOrderConfirmationBean.getQty());
            strURL = Utils.findAndReplace(strURL,"##ORDERTYPE##",fnoOrderConfirmationBean.getOrderType());
            //strURL = Utils.findAndReplace(strURL,"##LIMITPRICE##",fnoOrderConfirmationBean.getLimitPrice());
            if(fnoOrderConfirmationBean.getOrderType().equalsIgnoreCase("market"))
            {
            	strURL = Utils.findAndReplace(strURL,"&limitPrice=##LIMITPRICE##","");
            }
            else
            	strURL = Utils.findAndReplace(strURL,"##LIMITPRICE##",fnoOrderConfirmationBean.getLimitPrice());
            strURL = Utils.findAndReplace(strURL,"##STOPPRICE##",fnoOrderConfirmationBean.getStopPrice());
            strURL = Utils.findAndReplace(strURL,"##VALIDITY##",fnoOrderConfirmationBean.getValidity());
            return strURL;

    }
        public static String getFNOPostSQ5TradeURL(FNOOrderConfirmationBean fnoOrderConfirmationBean) {

            String strURL = AppConstants.fnoTradeSQ5URL;

            strURL = Utils.findAndReplace(strURL,"##COMPCODE##",fnoOrderConfirmationBean.getSymbolName());
            strURL = Utils.findAndReplace(strURL,"##USERID##",fnoOrderConfirmationBean.getCustId());
            strURL = Utils.findAndReplace(strURL,"##DISCQTY##",fnoOrderConfirmationBean.getDiscQty());
            strURL = Utils.findAndReplace(strURL,"##MKTLOT##",fnoOrderConfirmationBean.getMktLot());
            strURL = Utils.findAndReplace(strURL,"##INDEXTYPE##",fnoOrderConfirmationBean.getInstType());
            strURL = Utils.findAndReplace(strURL,"##EXPIRY##",fnoOrderConfirmationBean.getExpiry());
            strURL = Utils.findAndReplace(strURL,"##OPTYPE##",fnoOrderConfirmationBean.getOptionType());
            strURL = Utils.findAndReplace(strURL,"##STRIKEPRICE##",fnoOrderConfirmationBean.getStrikePrice());
            strURL = Utils.findAndReplace(strURL,"##ACTION##",fnoOrderConfirmationBean.getAction());
            strURL = Utils.findAndReplace(strURL,"##QTY##",fnoOrderConfirmationBean.getQty());
            strURL = Utils.findAndReplace(strURL,"##ORDERTYPE##",fnoOrderConfirmationBean.getOrderType());
            //strURL = Utils.findAndReplace(strURL,"##LIMITPRICE##",fnoOrderConfirmationBean.getLimitPrice());
            if(fnoOrderConfirmationBean.getOrderType().equalsIgnoreCase("market"))
            {
            	strURL = Utils.findAndReplace(strURL,"&limitPrice=##LIMITPRICE##","");
            }
            else
            	strURL = Utils.findAndReplace(strURL,"##LIMITPRICE##",fnoOrderConfirmationBean.getLimitPrice());
            
            strURL = Utils.findAndReplace(strURL,"##STOPPRICE##",fnoOrderConfirmationBean.getStopPrice());
            strURL = Utils.findAndReplace(strURL,"##VALIDITY##",fnoOrderConfirmationBean.getValidity());
            return strURL;

    }
        
        public static String getFNOExpiryAndStrikeDataURL(String compcode)
        {
                String strURL = AppConstants.fnoExpiryAndStrikeDataURL+compcode.replace('&', '-');
                return Utils.urlEncode(strURL);
        }
        
        public static String getFnoTradeOrderTurnOverDetailsURL(String symbol,String exchange,String action,String qty,String page) {
        	String strURL = AppConstants.fnoTradeOrderTurnOverDetailsURL;
            strURL = Utils.findAndReplace(strURL,"##SYMBOL##",symbol);
            strURL = Utils.findAndReplace(strURL,"##EXCHANGE##",exchange);
            strURL = Utils.findAndReplace(strURL,"##ACTION##",action);
            strURL = Utils.findAndReplace(strURL,"##QTY##",qty);
            strURL = Utils.findAndReplace(strURL,"##CUSTID##",UserInfo.getUserID());
            strURL = Utils.findAndReplace(strURL,"##PAGE##",page);
        	return urlEncode(strURL);
        }
        
        //Create WatchList MY, NIFTY, BSE
        public static String getWatchListURL(int i, String userID)
        {
                String strURL = AppConstants.watchListMY;
                /*if(i==2)
                        strURL = AppConstants.watchListNIFTY;
                else if(i==3)
                        strURL = AppConstants.watchListBSE;*/
                strURL = Utils.findAndReplace(strURL,"##UserID##",userID);
                return Utils.urlEncode(strURL);
        }
        
        //Create WatchList MY, NIFTY, BSE
        public static String getWatchListURL(String userID, String code)
        {
                String strURL = AppConstants.watchListScrips;
                /*if(i==2)
                        strURL = AppConstants.watchListNIFTY;
                else if(i==3)
                        strURL = AppConstants.watchListBSE;*/
                strURL = Utils.findAndReplace(strURL,"##UserID##",userID);
                strURL = Utils.findAndReplace(strURL,"##CODE##",code);
                return Utils.urlEncode(strURL);
        }
        //add watchlist
        public static String getWatchListURLADD(String userID, String text, String exchange)
        {
                String strURL = AppConstants.watchListAdd;
                strURL = Utils.findAndReplace(strURL,"##UserID##",userID);
                strURL = Utils.findAndReplace(strURL,"##TEXT##",text);
                strURL = Utils.findAndReplace(strURL,"##EXCH##",exchange);
                System.out.println("----------------this is a strURL : "+strURL);
                return Utils.urlEncode(strURL);
        }
        
        // Remove watchlist 
        public static String getWatchListURLRemove(String userID, String text)
        {
                String strURL = AppConstants.watchListRemove;
                strURL = Utils.findAndReplace(strURL,"##UserID##",userID);
                strURL = Utils.findAndReplace(strURL,"##TEXT##",text);
                return Utils.urlEncode(strURL);
        }
        
        // Remove watchlist scrips
        public static String getWatchListURLRemoveScrips(String userID, String text, String exchange, String scrips)
        {
                String strURL = AppConstants.watchListRemoveScrips;
                strURL = Utils.findAndReplace(strURL,"##UserID##",userID);
                strURL = Utils.findAndReplace(strURL,"##TEXT##",text);
                strURL = Utils.findAndReplace(strURL,"##EXCH##",exchange);
                strURL = Utils.findAndReplace(strURL,"##CCODE##",scrips);
                LOG.print(strURL);
                return strURL;
        }
        
        // Add watchlist scrips
        public static String getWatchListURLAddScrips(String userID, String text, String exchange, String scrips)
        {
                String strURL = AppConstants.watchListAddScrips;
                strURL = Utils.findAndReplace(strURL,"##UserID##",userID);
                strURL = Utils.findAndReplace(strURL,"##TEXT##",URLEncode.replace(text));
                strURL = Utils.findAndReplace(strURL,"##EXCH##",exchange);
                strURL = Utils.findAndReplace(strURL,"##CCODE##",URLEncode.replace26(scrips));
                System.out.println(" thksn strURL : "+strURL);
                return Utils.urlEncode(strURL);
        }
        //Create Statements
        public static String getTradeNowURL(String compcode, String userID, String exchange)
        {
                String strURL = AppConstants.tradeNowURL;
                strURL = Utils.findAndReplace(strURL,"##COMPCODE##",compcode);
                strURL = Utils.findAndReplace(strURL,"##UserID##",userID);
                strURL = Utils.findAndReplace(strURL,"##EXCHANGE##",exchange);
                return Utils.urlEncode(strURL);
        }

        

        //Create Commentary URL
        /*public static String getCommentaryURL(String pageNo)
        {
                String strURL = AppConstants.commentaryDataProviderUrl;
                strURL = Utils.findAndReplace(strURL,"##PAGENO##",pageNo);
                return Utils.urlEncode(strURL);
        }*/

        //Get Company Latest Trading Details URL
        public static String getCompanyLatestTradingDetailsURL(String companyCode)
        {
                String strURL = AppConstants.companyLatestTradingDetailsProvidersUrl;
                //if(!AppConstants.NSE)
                        strURL = Utils.findAndReplace(strURL, "##COMPANYCODE##", URLEncode.replace(companyCode));
                /*else
                        strURL = Utils.findAndReplace(strURL, "##COMPANYCODE##", FoScrips.getValue(companyCode));*/
                strURL = Utils.findAndReplace(strURL, "##TIMESTAMP##", System.currentTimeMillis()+"");
                return strURL;
        }
        
        //Get Company Latest Trading Details URL
        public static String getQuoteURL(String companyCode)
        {
                String strURL = AppConstants.getQuoteUrl;
                        strURL = Utils.findAndReplace(strURL, "##COMPANYCODE##", companyCode);
                return strURL;
        }

        //Futures URL
        //http://money.rediff.com/money1/current_status.php?companylist=##COMPANY##_##MONTH1##|##COMPANY##_##MONTH2##|##COMPANY##_##MONTH2##";
        public static String getFuturesURL(String companyCode,String month1, String month2, String month3)
        {
                String strURL = AppConstants.futuresDataProviderUrl;
                //System.out.println("companyCode --------------------------"+companyCode);
                if(companyCode.equals("17023929"))
                {
                        strURL = Utils.findAndReplace(strURL, "##COMPANY##", "NIFTY");
                        strURL = Utils.findAndReplace(strURL, "##COMPANY##", "NIFTY");
                        strURL = Utils.findAndReplace(strURL, "##COMPANY##", "NIFTY");
                }
                else
                {
                        strURL = Utils.findAndReplace(strURL, "##COMPANY##", companyCode);
                        strURL = Utils.findAndReplace(strURL, "##COMPANY##", companyCode);
                        strURL = Utils.findAndReplace(strURL, "##COMPANY##", companyCode);
                }

                strURL = Utils.findAndReplace(strURL, "##MONTH1##", month1);
                strURL = Utils.findAndReplace(strURL, "##MONTH2##", month2);
                strURL = Utils.findAndReplace(strURL, "##MONTH3##", month3);
                return strURL;
        }
        //Options URL
        //"http://money.rediff.com/money1/current_status.php?companylist=##COMPANY##_##MONTH##_##AMOUNT##_##CEPE##"
        public static String getOptionsURL(String companyCode,String month, String amount, String cepe)
        {
                String strURL = AppConstants.optionsDataProviderUrl;
                strURL = Utils.findAndReplace(strURL, "##COMPANY##", companyCode);
                strURL = Utils.findAndReplace(strURL, "##MONTH##", month);
                strURL = Utils.findAndReplace(strURL, "##AMOUNT##", amount);
                strURL = Utils.findAndReplace(strURL, "##CEPE##", cepe);
                return strURL;
        }


        //Get Watch Listed Company Data
        public static String getWatchListedCompanyData(String companyCodes)
        {
                String strURL = AppConstants.companyLatestTradingDetailsProvidersUrl;
                strURL = Utils.findAndReplace(strURL, "##COMPANYCODE##", companyCodes);
                strURL = Utils.findAndReplace(strURL, "##TIMESTAMP##", System.currentTimeMillis()+"");
                return strURL; 
        }

        //Add Records to Database record store
        public static boolean addRecordToWatchList(String companyID,String source,String companyName)
        {
                try
                {
                        byte[] data = DBPackager.createWatchListCompanyPackage(companyID, source,companyName);
                        DBmanager.addData(AppConstants.appWatchListDBName, data);
                        return true;
                }
                catch(Exception ex)
                {
                        Debug.debug("addRecordToWatchList,Error : "+ex.toString());
                } 
                return false;
        }

        public static void setWatchListedCompanyRecords(Vector vector)
        {
                vectorWatchlist = vector;
        }
        
        //Get Watch Listed Company
        public static Vector getWatchListedCompanyRecords()
        {
                /*Vector watchListVector = DBmanager.getRecords(AppConstants.appWatchListDBName);
                if(watchListVector==null)
                        return null;
                Vector vector = null;
                try
                {
                        for(int i=0;i<watchListVector.size();i++)
                        {
                                ByteArrayInputStream bais = new ByteArrayInputStream((byte[]) watchListVector.elementAt(i));
                                DataInputStream dis = new DataInputStream(bais);
                                if(vector==null)
                                {
                                        vector = new Vector();
                                }
                                vector.addElement(dis.readUTF()); //Company ID
                                vector.addElement(dis.readUTF()); //Group
                                vector.addElement(dis.readUTF()); //Company Name
                                dis.close();
                                bais.close();
                        }
                }
                catch(Exception ex)
                {
                        Debug.debug("Error : Reading from Database : "+ex.toString());
                }*/
                return vectorWatchlist;
        }
        
        public static void setWatchListedCompanyRecordsData(Vector vector) {
                vectorWatchlistData = vector;
        }
        public static Vector getWatchListedCompanyRecordsData() {
                return vectorWatchlistData;
        }
        
        public static void setWatchListedCompanyRecordsDataMain(Vector vector) {
                vectorWatchlistDataMain = vector;
        }
        public static Vector getWatchListedCompanyRecordsDataMain() {
                return vectorWatchlistDataMain;
        }

        //Check company in the watchList
        public static boolean checkCompanyInTheWatchList(String companyID)
        {
                Vector watchListVector = DBmanager.getRecords(AppConstants.appWatchListDBName);
                if(watchListVector==null)
                        return false;
                String strCompanyID;
                try
                {
                        for(int i=0;i<watchListVector.size();i++)
                        {
                                ByteArrayInputStream bais = new ByteArrayInputStream((byte[]) watchListVector.elementAt(i));
                                DataInputStream dis = new DataInputStream(bais);
                                strCompanyID = dis.readUTF();//Company ID
                                dis.readUTF(); //Group
                                dis.readUTF();
                                //Company ID
                                if(strCompanyID.equals(companyID))
                                {
                                        dis.close();
                                        bais.close();
                                        return true;
                                }
                                dis.close();
                                bais.close();
                        }
                        return false;
                }
                catch(Exception ex)
                {
                        Debug.debug("Error : Reading from Database : "+ex.toString());
                }
                return false;
        }

        //Delete rrecord from database recordstore
        public static boolean deleteCompanyFromWatchList(String CompanyID)
        {
                try
                {
                        Vector watchList = DBmanager.getRecordId(AppConstants.appWatchListDBName);
                        Debug.debug("watchList.size() : "+watchList.size());
                        String companyCode = null;
                        for(int i=0;i<watchList.size()/2;i++)
                        {
                                int recordID = Integer.parseInt(watchList.elementAt(i*2).toString());
                                ByteArrayInputStream bais = new ByteArrayInputStream((byte[]) watchList.elementAt(i*2+1));
                                DataInputStream dis = new DataInputStream(bais);
                                companyCode = dis.readUTF();
                                dis.readUTF();
                                dis.readUTF();
                                dis.close();
                                bais.close();
                                Debug.debug("companyCode : "+companyCode);
                                Debug.debug("CompanyID : "+CompanyID);
                                if(companyCode.equals(CompanyID))
                                {
                                        Debug.debug("Matches");
                                        Debug.debug("recordID : "+recordID);
                                        boolean status = DBmanager.deleteRecordFromRMS(AppConstants.appWatchListDBName, recordID);
                                        return status;
                                }
                        }

                }
                catch(Exception ex)
                {
                        Debug.debug("Error Deleting record from Database : "+ex.toString());
                }
                return false;
        }

        //Get Company Chart Data
        public static String getCompanyChartData(String companyCode)
        {
        	companyCode = URLEncode.replace(companyCode);
                String strURL = AppConstants.commoditySpecificChartURL;
                if(AppConstants.NSE)
                {
                        strURL = AppConstants.nsecompanyChartDataProviderUrl;
                        strURL = Utils.findAndReplace(strURL, "##COMPANYCODE##", companyCode);//FoScrips.getValue(companyCode));
                }
                else 
                        strURL = Utils.findAndReplace(strURL, "##COMPANYCODE##", companyCode);
                return strURL;
        }
        
        
        public static String getCommodityChartData(String companyCode,String symbol)
        {
        	companyCode = URLEncode.replace(companyCode);
                String strURL = AppConstants.commoditySpecificChartURL;
//                if(AppConstants.NSE)
//                {
                        strURL = Utils.findAndReplace(strURL, "##KEYWORD##",companyCode);
                        strURL = Utils.findAndReplace(strURL, "##SYMBOL##", symbol);//FoScrips.getValue(companyCode));
//                }
//                else 
//                        strURL = Utils.findAndReplace(strURL, "##COMPANYCODE##", companyCode);
                return strURL;
        }
        
        
        
        
        
        
        
        
        
        
        public static String getCompanyChartFNOData(String companyCode)
        {
        	companyCode = URLEncode.replace(companyCode);
        	     String strURL = AppConstants.companyChartDataProviderUrl;
                 strURL = Utils.findAndReplace(strURL, "##COMPANYCODE##", companyCode);
                return strURL;
        }
        //Get Company Chart Data
        public static String getCompanyWeekChartData(String companyCode)
        {
                /*String strURL = AppConstants.companyWeekChartDetailsUrl;
                strURL = Utils.findAndReplace(strURL, "##COMPANYCODE##", companyCode);*/
                return "";
        }

        //Get Company Chart Data
        public static String getCompanyMonthChartData(String companyCode)
        {
                /*String strURL = AppConstants.companyMonthChartDetailsUrl;
                strURL = Utils.findAndReplace(strURL, "##COMPANYCODE##", companyCode);*/
                return "";
        }

        //Get Company Chart Data
        public static String getCompany6MonthChartData(String companyCode)
        {
                /*String strURL = AppConstants.company6MonthChartDetailsUrl;
                strURL = Utils.findAndReplace(strURL, "##COMPANYCODE##", companyCode);*/
                return "";
        }

        //Get Company Chart Data
        public static String getCompany12MonthChartData(String companyCode)
        {
               /* String strURL = AppConstants.company12MonthChartDetailsUrl;
                strURL = Utils.findAndReplace(strURL, "##COMPANYCODE##", companyCode);*/
                return "";
        }

        public static String getCompanySearchURL(String companyName)
        {
        	companyName = URLEncode.replace(companyName);
                String strURL = AppConstants.companySearchURL;
                strURL = Utils.urlEncode(Utils.findAndReplace(strURL, "##KEYWORD##", companyName));
                return strURL; 
        }
        public static String getCompanyFNOSearchURL_getData(String companyName)
        {
        	if(companyName.indexOf('&')>-1)
        	{
        		int i = 0;
        		while(i<companyName.length())
        		{
        			if(companyName.indexOf('&')>-1)
        				companyName = companyName.replace('&', '-');
        			else
        				break;
        			i++;
        		}
        	}
        	    String strURL = AppConstants.companyFNOSearchURL_getData;
                strURL = Utils.urlEncode(Utils.findAndReplace(strURL, "##STOCK##", companyName));
                return strURL; 
        }
        public static String getCompanyFNOSearchURL(String companyName, String date)
        {
        	if(companyName.indexOf('&')>-1)
        	{
        		int i = 0;
        		while(i<companyName.length())
        		{
        			if(companyName.indexOf('&')>-1)
        				companyName = companyName.replace('&', '-');
        			else
        				break;
        			i++;
        		}
        	}
        	      String strURL = AppConstants.companyFNOSearchURL;
                strURL = Utils.urlEncode(Utils.findAndReplace(strURL, "##STOCK##", companyName));
                strURL = Utils.urlEncode(Utils.findAndReplace(strURL, "##DATE##", date));
                return strURL; 
        }
        public static String getCompanyFNOSearchURL_STRIKE(String companyName, String date, String cepe, String strike)
        {
        	if(companyName.indexOf('&')>-1)
        	{
        		int i = 0;
        		while(i<companyName.length())
        		{
        			if(companyName.indexOf('&')>-1)
        				companyName = companyName.replace('&', '-');
        			else
        				break;
        			i++;
        		}
        	}
        		String strURL = AppConstants.companyFNOSearchURL_STRIKE;
                strURL = Utils.urlEncode(Utils.findAndReplace(strURL, "##STOCK##", companyName));
                strURL = Utils.urlEncode(Utils.findAndReplace(strURL, "##DATE##", date));
                strURL = Utils.urlEncode(Utils.findAndReplace(strURL, "##CEPE##", cepe));
                strURL = Utils.urlEncode(Utils.findAndReplace(strURL, "##STRIKE##", strike));
                return strURL; 
        }
        //URL Encode
        public static String urlEncode(String sUrl)
        {
                StringBuffer urlOK = new StringBuffer();
                for (int i = 0; i < sUrl.length(); i++)
                {
                        char ch = sUrl.charAt(i);
                        switch (ch)
                        {
                        case '<':
                                urlOK.append("%3C");
                        break;
                        case '>':
                                urlOK.append("%3E");
                                break;
                        case ' ':
                                urlOK.append("%20");
                                break;
                        case ',':
                                urlOK.append("%2C");
                                break;
                        default:
                                urlOK.append(ch);
                                break;
                        }
                }
                return urlOK.toString();
        }

        public static String removeAnchorTags(String strText)
        {
                while(strText.indexOf("</a>")!=-1)
                {
                        strText = strText.substring(0, strText.indexOf("<a")) + strText.substring(strText.indexOf(">", strText.indexOf("<a"))+1,strText.indexOf("</a>")) + strText.substring(strText.indexOf("</a>")+4); 
                }
                Debug.debug("removeAnchorTags : "+ strText);
                return strText;
        }

        //Find string and replace value
        public static String findAndReplace(String mainString,String findStr,String strValue)
        {
                String retValue = mainString;
                try
                {
                	if(mainString.indexOf(findStr)>-1)
                        retValue = mainString.substring(0, mainString.indexOf(findStr)) + strValue + mainString.substring(mainString.indexOf(findStr) + findStr.length());
                }
                catch(Exception ex)
                {
                        //System.out.println("findAndReplace, Error : (mainString : "+mainString+" ), (findStr : "+findStr+" ), (strValue : "+strValue+" ) "+ ex.toString());
                }
                return retValue;
        }

        //get Tag Value
        public static String getTagValue(String strTagName,String strResponse)
        {
                String strRetValue = "";
                try
                {
                        strRetValue = strResponse.substring(strResponse.indexOf("<"+strTagName+">") + ("<"+strTagName+">").length(), strResponse.indexOf("</"+strTagName+">"));
                }
                catch(Exception ex)
                {
                        //System.out.println("Error : "+ex.toString());
                }
                return strRetValue;
        }

        public static void disableOrientationChange()
        {
                // force app to use only portrait mode
                int directions = Display.ORIENTATION_PORTRAIT;/*Display.DIRECTION_NORTH*/;
                UiEngineInstance engineInstance = Ui.getUiEngineInstance();
                if (engineInstance != null)
                {
                        engineInstance.setAcceptableDirections(directions);
                }
        }
        public static int snippetDiff = 0;
        public static HomeJson bseJsonStore;
        
        public static void keepSessionAlive()
        {
        	sessionAlive = true;
               /* final int sleep = (15*60*1000)-1; //(30*1000)-1;
                Utils.sessionExpiredTime = System.currentTimeMillis();
                sessionAlive = true;
                new Thread(new Runnable() {
                        public void run() {
                                try
                                {
                                        Thread.sleep(15*60*1000); //15 Minutes session out time
                                        //Thread.sleep(30*1000); //Test session out time 30 seconds
                                        if((Utils.sessionExpiredTime+sleep)<System.currentTimeMillis())
                                        {
                                                sessionAlive = false;
                                                ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
                                        }
                                }
                                catch(Exception e)
                                {
                                        
                                }
                        }
                }).start();*/
        }
        
        public static double DecimalRound(double num,int numDecim){
        /*	long p=1;
        	//next line – calculate pow(10,brDecim)
        	for(int i=0;i<numDecim;i++)p*=10;

        	return (double)(int)(p*num+0.5)/p;
        	*/
        	long p=1;
         	boolean flag = false;
         	if(num<0)
         	{
         		num = -num;
         		flag = true;
         	}
         	//next line – calculate pow(10,brDecim)
         	for(int i=0;i<numDecim;i++)p*=10;
         	num = (double)(int)(p*num+0.5)/p;
         	if(flag) num = -num;
         	return num;
        	}
        
        public static String DecimalRoundString(double num,int numDecim){
         	long p=1;
         	boolean flag = false;
         	if(num<0)
         		{
         		num = -num;
         		flag = true;
         		}
         	for(int i=0;i<numDecim;i++)p*=10;
         	num = (double)(int)(p*num+0.5)/p;
         	if(flag) num = -num;
         	String s = Double.toString(num);
         	if(s.indexOf(".")==s.length()-2)
         		s = s+ "0";
         	return s;
         	}
        
        public static LabelField separator(final int sepHeight, final int sepColor)
    	{
    		return new LabelField("",LabelField.NON_FOCUSABLE){
    			protected void layout(int arg0, int arg1) {
    				setExtent(AppConstants.screenWidth, sepHeight);
    			}
    			protected void paint(Graphics graphics) {
    				graphics.setColor(sepColor);
    				graphics.fillRect(0, 0, AppConstants.screenWidth, sepHeight);
    			}
    			protected void paintBackground(Graphics graphics) {
    				graphics.setBackgroundColor(sepColor);
    			}
    			};
    		
    	}
        public static LabelField separator(final int sepHeight)
    	{
    		return new LabelField("",LabelField.NON_FOCUSABLE){
    			protected void layout(int arg0, int arg1) {
    				setExtent(AppConstants.screenWidth, sepHeight);
    			}
    			};
    		
    	}
        public static LabelField separatorWidth(final int sepwidth)
    	{
    		return new LabelField("",LabelField.NON_FOCUSABLE){
    			protected void layout(int arg0, int arg1) {
    				setExtent(sepwidth, 10);
    			}
    			protected void paint(Graphics graphics) {
    				graphics.setColor(0x000000);
    				super.paint(graphics);
    			}
    			};
    		
    	}
        public static LabelField separatorRound(final int sepHeight, final int sepColor,final boolean top)
    	{
    		return new LabelField("",LabelField.NON_FOCUSABLE){
    			protected void layout(int arg0, int arg1) {
    				setExtent(AppConstants.screenWidth, sepHeight);
    			}
    			protected void paint(Graphics graphics) {
    				graphics.setColor(sepColor);
    				if(top)
    					graphics.fillRoundRect(0, 0, AppConstants.screenWidth, sepHeight+10, 8, 8);
    				else
    					graphics.fillRoundRect(0, -10, AppConstants.screenWidth, sepHeight, 8, 8);
    			}
    			protected void paintBackground(Graphics graphics) {
    				graphics.setBackgroundColor(sepColor);
    			}
    			};
    		
    	}
        public static Field BlankField(final int sepHeight,final int sepWidth, final int sepColor)
    	{
    		return new Field(Field.NON_FOCUSABLE){
    			protected void layout(int arg0, int arg1) {
    				setExtent(sepWidth, sepHeight);
    			}
    			protected void paint(Graphics graphics) {
    				graphics.setColor(sepColor);
    				graphics.fillRect(0, 0, sepWidth, sepHeight);
    			}
    			protected void paintBackground(Graphics graphics) {
    				graphics.setBackgroundColor(sepColor);
    			}
    			};
    		
    	}
        
        public static String replaceString(String string, String old, String replace)
    	{
    		if(string.indexOf(old)>-1)
    		{
    			string = string.substring(0, string.indexOf(old)) + replace + string.substring(string.indexOf(old)+old.length(), string.length());
    		}
    		return string;
    	}
}
