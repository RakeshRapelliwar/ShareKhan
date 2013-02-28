package com.snapwork.view;

import com.snapwork.util.AppConstants;
import com.snapwork.util.LOG;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class AppExitView extends PopupScreen {

	private String strTitle = "";
	private HorizontalFieldManager horizontalFieldManager;
	public AppExitView(String strTitle) {
		super(new VerticalFieldManager(),Field.FOCUSABLE);

		this.strTitle = strTitle;

		createUI(strTitle);
	}

	public void createUI(final String notificationType) {
		//set the title
		LabelField lblTitle = new LabelField(AppConstants.appTitle + ". " + strTitle);

		this.horizontalFieldManager = new HorizontalFieldManager(FIELD_HCENTER);


		ButtonField btnYes = new ButtonField("Yes",FOCUSABLE | FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {                                                   
				try
				{                            
					System.exit(0);
				}
				catch (Exception e)
				{

				}
				return true;
			}
		};

		ButtonField btnNo = new ButtonField("No",FOCUSABLE | FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {
				synchronized( UiApplication.getEventLock() ){

					if(isDisplayed()) 
					{
						close();

					}
				}
				return true;
			}
		};

		add(lblTitle);
		this.horizontalFieldManager.add(btnYes);
		this.horizontalFieldManager.add(btnNo);
		add(this.horizontalFieldManager);
	}

	protected boolean keyDown( int keyCode, int time ) {
		int key = Keypad.key( keyCode );
		if(key == Keypad.KEY_END)
		{
			LOG.print("KEY_END EXIT from app");
			System.exit(0);
		}
		return super.keyDown(keyCode, time);
	}

}
