package com.snapwork.util;

public class UserInfo
{
	private static String strUserID;
	private static String dpid;
	private static String userName;
	private static String userPassMembership;
	private static String userPassTrading;
	private static String colFlag;

	public static String getUserID()
	{
		return strUserID;
	}

	public static void setUserID(String value)
	{
		strUserID = value;
	}

	public static String getUserName()
	{
		return userName;
	}

	public static void setUserName(String value)
	{
		userName = value;
	}
	
	public static String getUserPassMembership()
	{
		return userPassMembership;
	}

	public static void setUserPassMembership(String value)
	{
		userPassMembership = value;
	}
	
	public static String getUserPassTrading()
	{
		return userPassTrading;
	}

	public static void setUserPassTrading(String value)
	{
		userPassTrading = value;
	}
	
	public static String getDpid() {
		return dpid;
	}

	public static void setDpid(String dpId) {
		dpid = dpId;
	}
	
	public static String getColFlag() {
		return colFlag;
	}

	public static void setColFlag(String colflag) {
		colFlag = colflag;
	}
}
