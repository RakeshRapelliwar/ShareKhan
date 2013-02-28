package com.snapwork.util;

/**
 * 
 * <p> This interface is used for callback which refreshes components and get values of BSE, NSE latest values, graphs and companies other details.
 *
 */
public interface AutoRefreshableStream
{
	public abstract void refreshComponentsFields();
}
