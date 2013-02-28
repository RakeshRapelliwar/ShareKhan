package com.snapwork.util;

import com.snapwork.actions.ActionCommand;
import com.snapwork.view.UserRegistrationScreen;

public class AppConstants
{
    //Application Title
    public static final String appTitle = "Sharekhan Trade";

    //Database related variables
    public static final String appDBName = "SKFIN_BB_USER";
    public static final String appDBMyWatchListState = "SKFIN_BB_WL_USER";
    public static final String appDBTOC = "SKTOC";
    public static final String appWatchListDBName = "SKUSER_WL";
    public static final String appNotificationDBName = "SKUSER_NTFC";

    //App Updates check time duration
    public static final int intCheckForUpdatesTime = 60;//Configure in Minutes

    //Auto Refresh Screen Thread Refresh screen time
    public static final byte screenRefreshTime = 30;//30;//Set in Seconds

    //Bottom Bar Variables
    public static final boolean keepBottomItemSelected = false;
    
    // Test URL
   //URL
   // public static final String domainUrl = "http://220.226.189.186/SK_live/"; 
  //  public static final String domainUrl = "http://mbltrade.sharekhan.com/";
   public static final String domainUrl = "http://mtrade.sharekhan.com/";

    //Notification URL                   http://mtrade.sharekhan.com/commoditySearch.php?q=##KEYWORD##
   // public static final String notificationUrl = "http://finance.techepoch.com/m/GetNotification.php";

    //check connection URL
     public static final String checkConnectionUrl = domainUrl;//"http://finance.techepoch.com/update_BB.xml";//"http://50.16.244.58/testxml/checkConnection.xml";
     public static final String fundTransferUrl = "https://strade.sharekhan.com/rmmweb/mcs.sk?e=100&menu_id=9";

    //App Version on server URL
    //public static final String serverVersionCheckURL = "http://finance.techepoch.com/update_BB.xml";

    //User Registration Command
    //http://50.17.18.243/Sharekhan/loginSubmit.php?password=sski@123&tpassword=sski@456&userType=trading&username=sankara_n
   //  public static final String userRegistrationUrl = domainUrl+"loginSubmit1.php?password=##PASSWORD##&tpassword=##TPASSWORD##&userType=##USERTYPE##&username=##NAME##&CC=BLACKBERRY&v="+UserRegistrationScreen.VERSION;
    public static final String userRegistrationUrl = domainUrl+"loginSubmit1.php?password=##PASSWORD##&tpassword=##TPASSWORD##&userType=##USERTYPE##&username=##NAME##&MID=##IMEI##&CC=BLACKBERRY&v=##VERSION##";
    
    //Change Profile 
    //public static final String profileChangeMRUrl = domainUrl+"updatePassword.php?username=##LOGINID##&oldPassword=##OLDPASSWORD##&newPassword=##NEWPASSWORD##&userType=BR&col=##COLFLAG##&custId=##CUSTID##&debug=2&USER_AGENT=J2ME";
    //public static final String profileChangeTRUrl = domainUrl+"updatePassword.php?username=##LOGINID##&oldPassword=##OLDPASSWORD##&newPassword=##NEWPASSWORD##&userType=TR&col=##COLFLAG##&custId=##CUSTID##&debug=2&USER_AGENT=J2ME";
    public static final String profileChangeMRUrl = domainUrl+"SK_android/controller.php?RequestId=prupwd01&custId=##CUSTID##&userType=BR&username=##LOGINID##&oldPassword=##OLDPASSWORD##&newPassword=##NEWPASSWORD##";
    public static final String profileChangeTRUrl = domainUrl+"SK_android/controller.php?RequestId=prupwd01&custId=##CUSTID##&userType=TR&username=##LOGINID##&oldPassword=##OLDPASSWORD##&newPassword=##NEWPASSWORD##";
   
    //public static final String userRegistrationUrl = "http://auth.techepoch.com/register.php?action=signup&name=##NAME##&email=##EMAIL##&service=afinance&mobile=##MOBILE##";
    public static String WEBVIEW_URL = "";
    public static String WEBVIEW_URL_TEXT = "";
    //Chart Data provider URL
    public static final String bseChartDataProviderUrl = domainUrl+"sensexChartXML.php";
    public static final String nseChartDataProviderUrl = domainUrl+"niftyChartXML.php";
    
    public static final String nsefoChartDataProviderUrl = domainUrl+"nsefoChartXML.php";
    public static final String mcxChartDataProviderUrl = domainUrl+"mcxChartXML.php";
    public static final String ncdexChartDataProviderUrl = domainUrl+"ncdexChartXML.php";

    //http://mtrade.sharekhan.com/sensexChartXML.php
    	
    //	http://mtrade.sharekhan.com/niftyChartXML.php
    	
    
    
    //Company Chart Data provider URL
    public static final String companyChartDataProviderUrl = domainUrl+"chart_1day.php?companyCode=##COMPANYCODE##";
    public static final String nsecompanyChartDataProviderUrl = domainUrl+"chart_1day.php?companyCode=##COMPANYCODE##";

    //Banners Data Provider URL ShareKhan
//    public static final String bannersDataProvideUrl = domainUrl+"getQuote.php?companylist=17023928|17023929|14030056&xml_flag=true";
    
    
    public static final String bannersDataProvideUrl = domainUrl+"getQuote_sample.php?companylist=17023928|17023929|NIFTY_28-02-2013|MCXFO|NCDEXFO";

    
    //ItsMe    http://mtrade.sharekhan.com/getQuote_sample.php?companylist=17023928|17023929|NIFTY_28-02-2013|MCXFO|NCDEXFO
    
    
    
    public static final String marketStatusUrl = domainUrl+"marketStatus.php?exchange=BSE|MCX";

    //News Data Provider URL
    //public static final String newsDataProviderUrl = domainUrl+"newsAPI.php?pg=##PAGENO##";
    public static final String newsDataProviderUrl = domainUrl+"newsAPI.php?start=##PAGENO##";
    
    //Futures And Options Data Provider URL ShareKhan
    public static final String futuresDataProviderUrl = domainUrl+"getQuote.php?companylist=##COMPANY##_##MONTH1##|##COMPANY##_##MONTH2##|##COMPANY##_##MONTH3##";
    public static final String optionsDataProviderUrl = domainUrl+"getQuote.php?companylist=##COMPANY##_##MONTH##_##AMOUNT##_##CEPE##";
    public static String optionsMonth = "";
    public static String optionsAmount = "";
    public static String optionsCEPE = "";
  //Announcements Data Provider URL
   // public static final String annsDataProviderUrl = "http://money.rediff.com/money/jsp/getAnnouncements.jsp?companyCode=##COMPANYNO##&descriptorId=100&currentPageNo=##PAGENO##&rowPerPage=10";

    //Commentry Data Provider URL
   // public static final String commentaryDataProviderUrl = "http://finance.techepoch.com/m/commentary.php?pgno=##PAGENO##";//"http://finance.techepoch.com/live/commentary.xml";

    //Top Gainers and loosers BSE/NSE Data Provider Urls
    public static final String bseTopGainersLoosersUrl = "http://finance.techepoch.com/live/bse_gainers_losers.xml";
    public static final String nseTopGainersLoosersUrl = "http://finance.techepoch.com/live/nse_gainers_losers.xml";
    public static final String globalTopGainersLoosersUrl = "http://finance.techepoch.com/live/global_indicator_new.xml";

    //Company Latest Trading Details URLs ShareKhan
    public static final String companyLatestTradingDetailsProvidersUrl = domainUrl+"getQuote.php?companylist=##COMPANYCODE##&xml_flag=true&##TIMESTAMP##";
    
    public static final String getQuoteUrl = domainUrl+"getQuote.php?companylist=##COMPANYCODE##";
    
    //Terms & Condition URL
    public static final String strTCURL = "http://finance.techepoch.com/m/termsofservices.html";
   
    //IDENInfo.imeiToString(IDENInfo.getIMEI())
    //Company Search URL ShareKhan
    public static final String companySearchURL = domainUrl+"getSearchList.php?q=##KEYWORD##";
    
    public static final String commoditySearchURL = domainUrl+"commoditySearch.php?q=##KEYWORD##";
    
    
    
    public static final String commoditySpecificSearchURL = domainUrl+"getQuote_sample.php?companylist=##KEYWORD##";
    
    
    
    
    public static final String commoditySpecificChartURL = domainUrl+"chart_1day_mxnx.php?companyCode=##KEYWORD##&symbol=##SYMBOL##";
    
    
       
    
    public static final String companyFNOSearchURL_getData = domainUrl+"FOSearch.php?companylist=##STOCK##";
    public static final String companyFNOSearchURL = domainUrl+"SK_android/FOSearch.php?searchStock=##STOCK##&expiryDate=##DATE##";
    public static final String companyFNOSearchURL_STRIKE = domainUrl+"SK_android/FOSearch.php?searchStock=##STOCK##&expiryDate=##DATE##&optionType=##CEPE##&strikePrice=##STRIKE##";
    //WebView URL'S
    //public static String tradeNowURL = domainUrl+"orderEQ.php?ccode=##COMPCODE##&perchange=##CPER##&change=##CHANGE##&custId=##UserID##&ltp=##LTP##&dpId=##DPID##";
    public static String tradeNowURL = domainUrl+"SK_android/controller.php?RequestId=treq01&restrictBack=1&ccode=##COMPCODE##&custId=##UserID##&exchange=##EXCHANGE##";
    public static String reportsFNOURL = domainUrl+"SK_android/controller.php?RequestId=stfnoo01&rtype=order&custId=##UserID##";//"orderBK.php?custId=##UserID##";
    public static String holdingsURL = domainUrl+"SK_android/controller.php?RequestId=steqdpsr01&custId=##UserID##&rtype=dpsr";
    public static String reportsEquityURL = domainUrl+"SK_android/controller.php?RequestId=steqo01&restrictBack=1&custId=##UserID##&rtype=order";
    public static String accountsURL = domainUrl+"accounts.php?custId=##UserID##&btnWithdrawl=Withdraw+Funds&debug=2";
    //http://50.17.18.243/SK/accounts.php?custId=250037&btnWithdrawl=Withdraw+Funds

    //FNO Trade URLS
    public static String fnoTradeDetailsURL = domainUrl+"orderFO.php?symbol=##COMPCODE##&custId=##UserID##&debug=2";
    public static String fnoExpiryAndStrikeDataURL = domainUrl+"ajaxNsefoData.php?symbol=";
    public static String fnoTradePlaceOrderURL = domainUrl+"SK_android/controller.php?RequestId=trfno04&symbolName=##COMPCODE##&exchange=NSEFO&instType=##INDEXTYPE##&expiry=##EXPIRY##&optType=##OPTYPE##&strikePrice=##STRIKEPRICE##&mkt-lots=##MKTLOTS##&action=##ACTION##&qty=##QTY##&disc-qty=##DISCQTY##&orderType=##ORDERTYPE##&limitPrice=##LIMITPRICE##&stopPrice=##STOPPRICE##&type=NOR&validity=##VALIDITY##&custId=##USERID##";
    public static String fnoTradeModifyOrderURL = domainUrl+"SK_android/controller.php?RequestId=stfnoo03&scripcode=##COMPCODE##&exchange=NSEFO&ltp=##LTP##&per_change=##PERCHANGE##&change=##CHANGE##&custId=##USERID##&disc-qty=&orderId=##ORDERID##&action=##ACTION##&instType=##INDEXTYPE##&optType=##OPTYPE##&strikePrice=##STRIKEPRICE##&page=reports&expiry=##EXPIRY##&userAgent=bb&order_exe_qty=##OREQ##&order_exe_price=0.00&btnConfirm=&mkt-lots=##MKTLOT##&rmsCode=##RMS##&token=##TOKEN##&qty=##QTY##&orderType=##ORDERTYPE##&limitPrice=##LIMITPRICE##&stopPrice=##STOPPRICE##&validity=##VALIDITY##";
    public static String fnoTradeSQ2URL = domainUrl+"SK_android/controller.php?RequestId=stfnoto02&symbol=##COMPCODE##&exchange=NSEFO&qty=##QTY##&action=##ACTION##&custId=##USERID##&page=turnover";
    public static String fnoTradeSQ3URL = domainUrl+"SK_android/controller.php?RequestId=stfnoto03&symbolName=##COMPCODE##&exchange=NSEFO&custId=##USERID##&disc-qty=&mkt-lots=##MKTLOT##&instType=##INDEXTYPE##&expiry=##EXPIRY##&optType=##OPTYPE##&strikePrice=##STRIKEPRICE##&action=##ACTION##&qty=##QTY##&orderType=##ORDERTYPE##&limitPrice=##LIMITPRICE##&stopPrice=##STOPPRICE##&validity=##VALIDITY##";
    public static String fnoTradeSQ4URL = domainUrl+"SK_android/controller.php?RequestId=stfnoto04&symbolName=##COMPCODE##&exchange=NSEFO&custId=##USERID##&disc-qty=&mkt-lots=##MKTLOT##&instType=##INDEXTYPE##&expiry=##EXPIRY##&optType=##OPTYPE##&strikePrice=##STRIKEPRICE##&action=##ACTION##&qty=##QTY##&orderType=##ORDERTYPE##&limitPrice=##LIMITPRICE##&stopPrice=##STOPPRICE##&validity=##VALIDITY##";
    public static String fnoTradeSQ5URL = domainUrl+"SK_android/controller.php?RequestId=stfnoto05&symbolName=##COMPCODE##&exchange=NSEFO&custId=##USERID##&disc-qty=&mkt-lots=##MKTLOT##&instType=##INDEXTYPE##&expiry=##EXPIRY##&optType=##OPTYPE##&strikePrice=##STRIKEPRICE##&action=##ACTION##&qty=##QTY##&orderType=##ORDERTYPE##&limitPrice=##LIMITPRICE##&stopPrice=##STOPPRICE##&validity=##VALIDITY##";
    //"SK_android/controller.php?RequestId=trfno04&symbolName=##COMPCODE##&exchange=##EXCHANGE##&custId=##USERID##&disc-qty=##DISCQTY##&mkt-lots=##MKTLOTS##&instType=##INDEXTYPE##&expiry=##EXPIRY##&optType=##OPTYPE##&strikePrice=##STRIKEPRICE##&action=##ACTION##&qty=##QTY##&orderType=##ORDERTYPE##&limitPrice=##LIMITPRICE##&stopPrice=##STOPPRICE##&validity=##VALIDITY##";
    public static String fnoTradeFreshOrderURL = domainUrl+"SK_android/controller.php?RequestId=trfno02&symbolName=##COMPCODE##&exchange=##EXCHANGE##&custId=##USERID##&disc-qty=##DISCQTY##&mkt-lots=##MKTLOTS##&instType=##INDEXTYPE##&expiry=##EXPIRY##&optType=##OPTYPE##&strikePrice=##STRIKEPRICE##&action=##ACTION##&qty=##QTY##&orderType=##ORDERTYPE##&limitPrice=##LIMITPRICE##&stopPrice=##STOPPRICE##&validity=##VALIDITY##";
    //+"placeFOOrder.php?symbolName=##COMPCODE##&exchange=##EXCHANGE##&ltp=##LTP##&instType=##INDEXTYPE##&expiry=##EXPIRY##&optType=##OPTYPE##&strikePrice=##STRIKEPRICE##&mkt-lots=##MKTLOTS##&action=##ACTION##&qty=##QTY##&disc-qty=##DISCQTY##&orderType=##ORDERTYPE##&limitPrice=##LIMITPRICE##&stopPrice=##STOPPRICE##&per_change=##PERCHANGE##&type=##TYPE##&change=##CHANGE##&validity=##VALIDITY##&custId=##USERID##&dpId=##DPID##&btnConfirm=Confirm&btnModify=&debug=2";
    public static String fnoTradeOrderDetailsURL = domainUrl+"orderTransaction.php?custId=##UserID##&orderId=##ORDERID##&exchange=##EXCHANGE##&btnModify=Modify&page=##PAGE##&rmsCode=##RMSCODE##&debug=2";
    //public static String fnoTradeModifyOrderURL = domainUrl+"modifyOrder.php?scripcode=##SCRIPCODE##&exchange=##EXCHANGE##&ltp=##LTP##&per_change=##PERCHANGE##&change=##CHANGE##&custId=##USERID##&orderId=##ORDERID##&action=##ACTION##&instType=##INDEXTYPE##&optType=##OPTYPE##&strikePrice=##STRIKEPRICE##&page=##PAGE##&expiry=##EXPIRY##&order_exe_qty=0&order_exe_price=&btnConfirm=Confirm&mkt-lots=##MKTLOTS##&rmsCode=##RMSCODE##&qty=##QTY##&orderType=##ORDERTYPE##&limitPrice=##LIMITPRICE##&stopPrice=##STOPPRICE##&validity=##VALIDITY##&debug=2";
    public static String fnoTradeOrderTurnOverDetailsURL = domainUrl+"orderFO.php?symbol=##SYMBOL##&exchange=##EXCHANGE##&action=##ACTION##&qty=##QTY##&custId=##CUSTID##&page=##PAGE##&debug=2";    

    //updateQA for membership and trading 
    public static String updateQAMembership = domainUrl+"updateHintQA.php?username=##UserName##&hintQ=##HintQ##&hintA=##HintA##&userType=BR&debug=2";
    public static String updateQATrading = domainUrl+"updateHintQA.php?username=##UserName##&hintQ=##HintQ##&hintA=##HintA##&userType=TR&debug=2";

    //WatchList MyWatchList, NIFTY, BSE
    public static String watchListMY = domainUrl+"SK_android/getWatchlistDetails_v1.php?custId=##UserID##";
    public static String watchListScrips = domainUrl+"SK_android/getWatchlistScrips_v1.php?custId=##UserID##&template=##CODE##";
    

	public static String watchListAdd = domainUrl+"SK_android/addNewWatchlist_v1.php?custId=##UserID##&template=##TEXT##&exchange=##EXCH##";
    public static String watchListRemove = domainUrl+"SK_android/deleteWatchlist_v1.php?custId=##UserID##&template=##TEXT##";
    public static String watchListRemoveScrips = domainUrl+"SK_android/deleteScriptfromWatchlist_v1.php?custId=##UserID##&template=##TEXT##&exchange=##EXCH##&scrip=##CCODE##";
    public static String watchListAddScrips = domainUrl+"SK_android/addScriptoWatchlist_v1.php?custId=##UserID##&template=##TEXT##&exchange=##EXCH##&scrip=##CCODE##";
    // public static String watchListBSE = domainUrl+"getWatchlistScrips.php?custId=##UserID##&template=SENSEX";
    public static String getFNOURL =  domainUrl + "getFoList.php?flag=1";


	public static String getFNODATEURL =  domainUrl + "getFoList.php?flag=2";
	
	//Change membership password URL
	public static String changeMembershipPasswordURL = "https://strade.sharekhan.com/rmmweb/mcs.sk?e=133";
	//Forget membership password URL
	public static String forgetMembershipPasswordURL = "https://strade.sharekhan.com/rmmweb/mcs.sk?e=135";

    //Bottom Menu Commands
    public static final byte[] bottomMenuCommands = {ActionCommand.CMD_GRID_SCREEN,ActionCommand.CMD_WATCHLIST_SCREEN,ActionCommand.CMD_BSE_GL_SCREEN,ActionCommand.CMD_NEWS_SCREEN,ActionCommand.CMD_SEARCH_SCREEN,ActionCommand.CMD_CURRENTSTAT};
    public static final byte[] bottomTradeMenuCommands = {ActionCommand.CMD_GRID_SCREEN,ActionCommand.CMD_WATCHLIST_SCREEN,ActionCommand.CMD_REPORTSB_SCREEN,ActionCommand.CMD_SEARCH_SCREEN};
    //public static final byte[] gridMenuCommands = {ActionCommand.CMD_HOME_SCREEN,ActionCommand.CMD_WATCHLIST_SCREEN,ActionCommand.CMD_ORDERBOOK_SCREEN,ActionCommand.CMD_HOLDINGS_SCREEN,ActionCommand.CMD_REPORTS_SCREEN,ActionCommand.CMD_TRADE_VIEW_SCREEN,ActionCommand.CMD_PROFILE_SCREEN,ActionCommand.CMD_ACCOUNTS_SCREEN,ActionCommand.CMD_SEARCH_SCREEN};
    public static final byte[] gridMenuCommands = {ActionCommand.CMD_CURRENTSTAT,ActionCommand.CMD_WATCHLIST_SCREEN,ActionCommand.CMD_ORDERBOOK_SCREEN,ActionCommand.CMD_HOLDINGS_SCREEN,ActionCommand.CMD_REPORTS_SCREEN,ActionCommand.CMD_SEARCH_SCREEN,ActionCommand.CMD_PROFILE_SCREEN,ActionCommand.CMD_ACCOUNTS_SCREEN, ActionCommand.CMD_FUND_TRANSFER_SCREEN, ActionCommand.CMD_SPAN_CALC_SCREEN};
    
    public static final byte[] bottomMenuForReportsCommands = {ActionCommand.CMD_GRID_SCREEN,ActionCommand.CMD_WATCHLIST_SCREEN,ActionCommand.CMD_REPORTS_SCREEN,ActionCommand.CMD_SEARCH_SCREEN};

    //Note following varriables are not final so they dont need to change
    //This varriables will be edited by AppConfigure Class
    public static int screenWidth = 320;
    public static int screenHeight = 240;

    //This varriables are required to resize images accordingly
    public static int baseBuildScreenWidth = 320;
    public static int baseBuildScreenHeight = 240;

    //Loading screen COnstants
    public static final String loadingMessage = "Loading...";

    //Padding
    public static byte padding = 6;
    public static int newsScreenStaticBlock = 12399673;
  //  public static int newsScreenStaticBlock = 0x000000;

	public static String source="";

	public static String COMPANY_NAME="";

	public static boolean REMOVE_FNO=false;

	public static boolean OPTIONS_FLAG = false;

	public static boolean OPTIONS_FLAG_First;

	public static int MOVE_MONTH=0;

    //Font Sizes
    public static final byte SMALL_BOLD_FONT = 0;
    public static final byte SMALL_PLAIN_FONT = 1;
    public static final byte MEDIUM_BOLD_FONT = 2;
    public static final byte MEDIUM_PLAIN_FONT = 3;
    public static final byte BIG_BOLD_FONT = 4;
    public static final byte BIG_PLAIN_FONT = 44;
    public static final byte BIG_PLAIN_FONT22 = 49;
    public static final byte EXTRA_SMALL_BOLD_FONT = 5;
    public static final byte EXTRA_BOLD_FONT = 6;
    public static final byte HYPER_LINK_FONT = 7;
    public static final byte CHART_SMALL_FONT = 8;
    public static final byte TOC_FONT = 12;
    public static final byte MEDIUM_SPECIAL_FONT = 9;
    public static final byte MEDIUM_SPECIAL_BANNER_FONT = 10;
    public static final byte MEDIUM_SPECIAL_BANNER_FONT_FNO = 11;

    //Graph Types
    public static final byte INTRADAY = 0;
    public static final byte WEEK = 1;
    public static final byte MONTH = 2;
    public static final byte MONTH6 = 3;
    public static final byte YEAR = 4;

	public static final byte REPORTS_FONT = 45;
	public static final byte REPORTSMARGIN_FONT = 46;
	public static final byte REPORTSMARGINBOLD_FONT = 47;

	public static final byte LABEL_FONT = 48;

	public static boolean NSE = false;

	public static int OS = 5;

	public static boolean SEND_EMAIL;
}