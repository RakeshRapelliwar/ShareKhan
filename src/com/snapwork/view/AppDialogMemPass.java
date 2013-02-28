package com.snapwork.view;

import com.snapwork.util.AppConstants;
import com.snapwork.util.LOG;

import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class AppDialogMemPass extends PopupScreen {

	private HorizontalFieldManager horizontalFieldManager;
	public AppDialogMemPass(String strMsg) {
		super(new VerticalFieldManager(),Field.FOCUSABLE);
		createUI(strMsg);
	}

	public void createUI(String strMsg) {
		//set the title
		LabelField lblTitle = new LabelField(strMsg);

		this.horizontalFieldManager = new HorizontalFieldManager(FIELD_HCENTER);


		ButtonField btnYes = new ButtonField("OK",FOCUSABLE | FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {                                                   
				Browser.getDefaultSession().displayPage(AppConstants.changeMembershipPasswordURL);
				System.exit(0);
				return true;
			}
		};
		add(lblTitle);
		this.horizontalFieldManager.add(btnYes);
		add(this.horizontalFieldManager);
	}

	protected boolean keyDown( int keyCode, int time ) {
		int key = Keypad.key( keyCode );
		if(key == Keypad.KEY_END)
		{
			LOG.print("KEY_END EXIT from app");
			System.exit(0);
		}
		else if(key == Keypad.KEY_ESCAPE)
		{
			synchronized( UiApplication.getEventLock() ){

				if(isDisplayed()) 
				{
					close();

				}
			}
		}
			
		return super.keyDown(keyCode, time);
	}

}
