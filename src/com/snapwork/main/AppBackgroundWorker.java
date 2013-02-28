package com.snapwork.main;

import java.util.Vector;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;
import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.beans.Notification;
import com.snapwork.components.Screen;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.util.AppConstants;

public class AppBackgroundWorker extends Application
{
	AppBackgroundWorker()
	{
		//Wait for Ui Operations to complete
		while(ApplicationManager.getApplicationManager().inStartup())
		{
			try
			{
				Thread.sleep(2000);
			} catch(Exception ex) {
				//System.out.println("Error in Backbround wait for ui to be ready thread  : "+ex.toString());
			}
		}
		//Register App Icon and show indication

		//Start the indication thread
		while(true) {
			try {
				Screen screen = new Screen() {

					public void setData(Vector vac,ThreadedComponents threadedComponents) {
						if(vac!=null) {
							try {
								Notification notification = (Notification)vac.elementAt(0);
								String[] args = new String[8];
								args[0] = "NotificationHandler";
								args[1] = notification.getType();
								args[2] = notification.getTitle();
								ApplicationDescriptor applicationDescriptor = new ApplicationDescriptor(ApplicationDescriptor.currentApplicationDescriptor(), args);
								ApplicationManager.getApplicationManager().runApplication(applicationDescriptor);
							}
							catch(Exception ex)
							{
								//System.out.println("Error in Notification Parse : "+ex.toString());
							}
						}
					}
				};
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_CHECK_UPDATES,screen));
				Thread.sleep(AppConstants.intCheckForUpdatesTime * 60 * 1000);
			}
			catch(Exception ex)
			{

			}
		}
	}
}
