package com.snapwork.util;

public class LOG
{
	private static boolean STATUS;

	/**
	 * Use to enable the LOG and LOG will print messages
	 */
	public static void enable()
	{
		STATUS = true;
	}

	/**
	 * Use to disable the LOG and LOG will not print messages
	 */
	public static void disable()
	{
		STATUS = false;
	}

	/**
	 * 
	 * @param sysout Print the String e.g. System.out.println(sysout);
	 * Use enable, disable method to activate, deactivate the LOG respectively
	 */
	public static void print(String sysout)
	{
		
			System.out.println(sysout);
	}
}
