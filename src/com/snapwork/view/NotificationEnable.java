package com.snapwork.view;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.system.LED;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import com.snapwork.util.AppConstants;
import com.snapwork.util.DBmanager;
import com.snapwork.util.Utils;

public class NotificationEnable extends PopupScreen 
{
	private HorizontalFieldManager horizontalFieldManager;
	public NotificationEnable() 
	{
		super(new VerticalFieldManager(),Field.FOCUSABLE);
		createUI(AppConstants.appTitle+" :");
		LED.setConfiguration(10,1500, LED.BRIGHTNESS_100);
		LED.setState(LED.LED_TYPE_STATUS, LED.STATE_BLINKING);
	}

	public void createUI(final String notificationType)
	{
		LabelField lblTitle = new LabelField(notificationType);
		LabelField lblDesc = new LabelField("Notification feature is disabled. Do you want to enable?");
		this.horizontalFieldManager = new HorizontalFieldManager(FIELD_HCENTER);
		ButtonField btnYes = new ButtonField("Yes",FOCUSABLE | FIELD_HCENTER)
		{
			protected boolean navigationClick(int status, int time)
			{							
				try 
				{
					DBmanager.dropDatabase(AppConstants.appNotificationDBName);
					LED.setState(LED.STATE_OFF);
					String[] notificationAppReqArgs  = {"UiApp",notificationType};
					ApplicationDescriptor notificationAppReqDescriptor = new ApplicationDescriptor(ApplicationDescriptor.currentApplicationDescriptor(), notificationAppReqArgs);
					ApplicationManager.getApplicationManager().runApplication(notificationAppReqDescriptor);
					System.exit(1);
				} 
				catch (Exception e)
				{
					//System.out.println("Error Launching app : "+e.toString());
				}
				return true;
			}
		};

		ButtonField btnNo = new ButtonField("No",FOCUSABLE | FIELD_HCENTER)
		{
			protected boolean navigationClick(int status, int time)
			{
				try
				{
					LED.setState(LED.STATE_OFF);
					String[] notificationAppReqArgs  = {"UiApp",notificationType};
					ApplicationDescriptor notificationAppReqDescriptor = new ApplicationDescriptor(ApplicationDescriptor.currentApplicationDescriptor(), notificationAppReqArgs);
					ApplicationManager.getApplicationManager().runApplication(notificationAppReqDescriptor);
					System.exit(1);
				} 
				catch (Exception e)
				{
					//System.out.println("Error Launching app : "+e.toString());
				}
				return true;
			}
		};
		add(lblTitle);
		add(new SeparatorField());
		add(lblDesc);
		this.horizontalFieldManager.add(btnYes);
		this.horizontalFieldManager.add(btnNo);
		add(this.horizontalFieldManager);
	}
}
