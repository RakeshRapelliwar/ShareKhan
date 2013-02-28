package com.snapwork.actions;

public final class ActionCommand
{
	//For debug purpose use this command
	public final static byte CMD_DEBUG = 0;

	//When no action has to be performed use this action
	public final static byte CMD_NONE = 1;

	//Splash screen command
	public final static byte CMD_SPLASH = 2;
	//Splash screen command
	public final static byte CMD_SPLASH_PRE = 114;

	//Version Update Command
	public final static byte CMD_UPDATE_VERSION = 3;

	//Registration Command
	public final static byte CMD_USER_REGISTRATION = 4;

	//Home Screen Command
	public final static byte CMD_HOME_SCREEN = 5;

	//Holding Screen Command
	public final static byte CMD_ORDERBOOK_SCREEN = 36;

	public static final byte CMD_HOLDINGS_SCREEN = 38;

	public static final byte CMD_ACCOUNTS_SCREEN = 39;
	public static final byte CMD_FUND_TRANSFER_SCREEN = 120;
	public static final byte CMD_SPAN_CALC_SCREEN = 121;

	public static final byte CMD_ACCOUNTS_WITHDRAW_FUNDS_SCREEN = 107;

	public static final byte CMD_REPORTS_SCREEN = 40;
	public static final byte CMD_REPORTSB_SCREEN = 44;

	public static final byte CMD_PROFILE_SCREEN = 41;
	
	public static final byte CMD_PROFILE_CHANGE_SCREEN = 42;

	//WebView Screen Command
	public final static byte CMD_WEBVIEW_SCREEN = 37;

	//Home Screen Command
	public final static byte CMD_GRID_SCREEN = 35;

	//Nifty Screen Command
	public final static byte CMD_NIFTY_SCREEN = 6;

	//Watch List Screen Command
	public final static byte CMD_WL_SCREEN = 7;
	public final static byte CMD_WL_SCREEN_EDIT_SCREEN = 76;
	public final static byte CMD_WL_EDIT_SCREEN = 77;
	public final static byte CMD_WL_FILL_SCREEN = 78;
	public final static byte CMD_WATCHLIST_SCREEN = 79;

	//Market experts Screen Command
	public final static byte CMD_COMMENTARY_SCREEN = 8;

	//Top Gainers Loosers Screen Command
	public final static byte CMD_BSE_GL_SCREEN = 9;
	public final static byte CMD_NSE_GL_SCREEN = 23;
	public static final byte CMD_GLOBAL_GL_SCREEN = 98;

	//Top Gainers Loosers Screen Command
	public final static byte CMD_FUTURE_SCREEN = 87;
	public final static byte CMD_OPTION_SCREEN = 89;

	//Top Analyst Corner Screen Command
	public final static byte CMD_STATS_SCREEN = 97;
	public final static byte CMD_ANALYST_SCREEN = 96;
	public static final byte CMD_EXPERTS_SCREEN = 95;

	//News Screen Command
	public final static byte CMD_NEWS_SCREEN = 43;
	public final static byte CMD_TRADE_VIEW_SCREEN = 10;

	//Search Screen Command
	public final static byte CMD_SEARCH_SCREEN = 11;

	//Get Graph Data Command
	public final static byte CMD_BSE_GET_GRAPH_DATA = 12;
	
	
	public final static byte CMD_COMMODITY_GRAPH_DATA = 50;
	
	
	public final static byte CMD_COMMODITY_DATA = 51;
	
	
	public final static byte CMD_NSE_GET_GRAPH_DATA = 13;
	
	
	public final static byte CMD_NSEFO_GET_GRAPH_DATA = 47;
	public final static byte CMD_MCX_GET_GRAPH_DATA = 48;
	
	public final static byte CMD_NCDEX_GET_GRAPH_DATA = 49;
	

	//Get Home Screen Banner Data Command
	public final static byte CMD_BANNERS_HOME_SCREEN = 14;

	//Get Chart In Full Screen 
	public final static byte CMD_FULL_SCREEN_CHART = 15;

	//Paging commands
	public final static byte CMD_NEXT_PAGE = 16;
	public final static byte CMD_PREV_PAGE = 17;

	public final static byte CMD_NEWS_PAGER = 18;
	public final static byte CMD_NEWS_DETAIL = 19;
	public final static byte CMD_ANNOUNCEMENT_DETAIL = 91;
	public final static byte CMD_ANNOUNCEMENT_PAGER = 90;


	public final static byte CMD_COMMENTARY_PAGER = 21;
	public final static byte CMD_COMMENTARY_DETAIL = 22;

	public final static byte CMD_COMPANY_DETAILS_SCREEN = 24;
	public final static byte CMD_COMPANY_DETAILS_SCREEN_NSE = 80;
	public final static byte CMD_NIFTY_COMPANY_DETAILS_SCREEN = 45;
	public final static byte CMD_COMPANY_STATICS_BANNERS = 25;
	public final static byte CMD_COMPANY_INTRA_CHART = 26;

	public final static byte CMD_COMPANY_FULL_INTRA_CHART = 27;
	public final static byte CMD_COMPANY_FULL_WEEK_CHART = 28;
	public final static byte CMD_COMPANY_FULL_MONTH_CHART = 29;
	public final static byte CMD_COMPANY_FULL_6MONTH_CHART = 30;
	public final static byte CMD_COMPANY_FULL_12MONTH_CHART = 31;
	public final static byte CMD_SEARCH_COMPANY = 32;
	public final static byte CMD_SEARCH_COMMODITY = 74;
	public final static byte CMD_WL_REFRESH = 33;

	public final static byte CMD_CHECK_UPDATES = 34;
	public final static byte CMD_SETTINGS = 99;
	public final static byte CMD_EXIT = 100;	
	public final static byte CMD_APPMEMPASS = 115;
	public final static byte CMD_SESSION_EXPIRED = 101;
	
	public final static byte CMD_TRADE_MAIN = 102;
	public final static byte CMD_TRADE_MODIFY = 103;
	public final static byte CMD_TRADE_RESULT = 104;
	public final static byte CMD_REPORTS_ORDER_VIEW = 105;
	public final static byte CMD_SLIDE_VIEW = 106;

	public final static byte CMD_MARKET_DEPTH_SCREEN = 92;
	
	//FNO Trade commands
	public final static byte CMD_FNO_TRADE = 108;
	public final static byte CMD_FNO_TRADE_FRESH_ORDER_CONFIRMATION = 123;
	public final static byte CMD_FNO_TRADE_ORDER_CONFIRMATION = 109;
	public final static byte CMD_FNO_TRADE_ORDER_CONFIRM_POST = 110;
	public final static byte CMD_FNO_TRADE_TURNOVER_CONFIRMATION = 117;
	public final static byte CMD_FNO_TRADE_ORDER_DETAILS = 111;
	public final static byte CMD_FNO_TRADE_ORDER_MODIFY = 112;
	public final static byte CMD_FNO_TRADE_ORDER_TURNOVER = 113;

	public final static byte CMD_TOC = 116;
	
	
	public final static byte CMD_CURRENTSTAT = 124;


}
