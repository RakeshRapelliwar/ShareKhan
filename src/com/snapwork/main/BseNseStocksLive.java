package com.snapwork.main;

import net.rim.device.api.ui.UiApplication;
import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.util.AppConfigurer;
import com.snapwork.util.AutoScreenRefresherThread;
import com.snapwork.util.Debug;
import com.snapwork.util.HTTPGetConnection;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.view.InternetExitScreen;


//MainScreen

/**
 * 
 * <p>If the application started by invoking icon this class get called and started to load the screens.
 *
 */
public class BseNseStocksLive extends UiApplication
{
	private Action startUpAction = new Action(ActionCommand.CMD_SPLASH,null);
	public BseNseStocksLive()
	{
		AppConfigurer.configure();
		//Start auto screen refresher thread
        AutoScreenRefresherThread.startThread();
        //execute int startup action
		ActionInvoker.processCommand(startUpAction);
	}
	
	public BseNseStocksLive(String notificationType)
	{
		           
         
		//   boolean f = false;
	    //    while(true){
	//try {
	//	String url = HTTPGetConnection.getURL("http://www.google.com/");
	//	LOG.print("200 OK "+url);
	//	f=true;
	//} catch (Exception e) {
	//	f=false;
		//e.printStackTrace();
	//}
	//if(f)
	{
		
		//Configure application according to screen size 
		AppConfigurer.configure();
		//Start auto screen refresher thread
        AutoScreenRefresherThread.startThread();
        //execute int startup action
        com.snapwork.util.HTTPGetConnection.setConnections();  
		ActionInvoker.processCommand(new Action(ActionCommand.CMD_SPLASH_PRE,notificationType));
		//break;
	}
	     //  }
	}
	public static void closeApp()
	{
		UiApplication.getUiApplication().invokeLater(new Runnable()
		{
			public void run()
			{
				try{
               	 InternetExitScreen appExitView = new InternetExitScreen();
                        UiApplication.getUiApplication().pushScreen(appExitView);
               	 }
               	 catch(Exception e)
               	 {
               		  System.exit(0);
               	 }
				//ScreenInvoker.showDialog("Connection lost!..Please check internet connection and restart the App");
				//ScreenInvoker.removeRemovableScreen();
				//System.exit(1);
			}
		});
	}
	
	//eclipse -> Window -> Preferences -> Blackberry JDE -> Installed Components -> Java Home

	public boolean keyChar(char key, int status, int time)
	{
		return false;
	}

	public boolean keyDown(int keycode, int time)
	{
		/*if (keycode == 1769472)
		{
			if (UiApplication.getUiApplication().getScreenCount() == 1)
			{
				
			}
		}*/
		return false;
	}

	public boolean keyRepeat(int keycode, int time){
		return false;
	}

	public boolean keyStatus(int keycode, int time) {
		return false;
	}

	public boolean keyUp(int keycode, int time) {
		return false;
	}

	protected void finalize ()
	{
		
	}

}
