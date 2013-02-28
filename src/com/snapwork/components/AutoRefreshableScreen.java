package com.snapwork.components;

/**
 * 
 * <p> This interface is used for callback which refreshes components and get values of BSE, NSE latest values, graphs and companies other details.
 *
 */
public interface AutoRefreshableScreen
{
	public abstract void refreshFields();
}
