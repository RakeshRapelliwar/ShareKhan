package com.snapwork.util;

import java.util.Calendar;
import java.util.TimeZone;

import net.rim.device.api.ui.UiApplication;

import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.WatchListField;
import com.snapwork.util.AutoRefreshableStream;
import com.snapwork.view.HomeScreen;
import com.snapwork.view.MyWatchList;

public class AutoScreenRefresherThreadStream
{
	private static Thread autoScreenRefresherThread = null;
	public AutoScreenRefresherThreadStream()
	{
		
	}

	public static void startThread()
	{
		if(autoScreenRefresherThread == null)
		{
			autoScreenRefresherThread = new Thread(new Runnable()
			{

				public void run()
				{
					try 
					{
						while(true)
						{
							//((AutoRefreshableScreen)(ScreenCanvas.objCurrentScreen)).refreshFields();
							if(UiApplication.getUiApplication().getActiveScreen() instanceof MyWatchList)
								((AutoRefreshableStream)(UiApplication.getUiApplication().getActiveScreen())).refreshComponentsFields();
							Thread.sleep(1200);
						}
					}
					catch(Exception ex)
					{
						//Debug.debug("Screen Refresher Thread Error : "+ex.toString());
					}
				}
			});
			autoScreenRefresherThread.start();
		}
	}
	public static void stopThread()
	{
		autoScreenRefresherThread = null;
	}
}