package com.snapwork.view;

import com.snapwork.util.LOG;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class InternetExitScreen extends PopupScreen
{
	private HorizontalFieldManager horizontalFieldManager;
	public InternetExitScreen()
	{
		super(new VerticalFieldManager(),Field.FOCUSABLE);
		createUI();
	}

	public void createUI() {
		//set the title
		LabelField lblTitle = new LabelField("Internet Connection takes long time to respond!\nDo you want to exit?");

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
