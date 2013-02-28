package com.snapwork.view;

import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.snapwork.util.AppConstants;

public class VersionUpdatesScreen extends PopupScreen {
	
	public VersionUpdatesScreen(String strUpdatesMessage,String strUpdateURL, String forceUpdate) {
		super(new VerticalFieldManager(), FOCUSABLE);

		createUI(strUpdatesMessage,strUpdateURL,forceUpdate.equals("yes"));
	}
	
	public void createUI(String strUpdatesMessage,final String strUpdateURL, boolean isForceUpdate) {
		
		LabelField lblTitle = new LabelField(AppConstants.appTitle);
		LabelField lblUpdatesMessage = new LabelField(strUpdatesMessage);
		
		VerticalFieldManager verticalFieldManager = new VerticalFieldManager(FIELD_HCENTER);
	    
		ButtonField btnUpdateNow = new ButtonField("Update Now",FOCUSABLE | FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {
				Browser.getDefaultSession().displayPage(strUpdateURL);
				System.exit(1);
                return true;
            }
		};
		
		verticalFieldManager.add(btnUpdateNow);
		
		
		if(!isForceUpdate) {
			ButtonField btnUpdateLater = new ButtonField("Update Later",FOCUSABLE | FIELD_HCENTER) {
				protected boolean navigationClick(int status, int time) {
					close();
	                return true;
	            }				
			};
			verticalFieldManager.add(btnUpdateLater);
		}
		
		add(lblTitle);
		add(new SeparatorField());
		add(lblUpdatesMessage);
		add(verticalFieldManager);
	}
	
}
