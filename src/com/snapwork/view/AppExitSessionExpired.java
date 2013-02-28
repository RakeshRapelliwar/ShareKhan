package com.snapwork.view;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.util.AppConstants;
import com.snapwork.util.LOG;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class AppExitSessionExpired extends PopupScreen {

    
     public AppExitSessionExpired(String strTitle) {
            super(new VerticalFieldManager(),Field.FOCUSABLE);            
            createUI(strTitle);
    }
  
    public void createUI(final String notificationType) {
                    //set the title
                   LabelField lblTitle = new LabelField(AppConstants.appTitle + " Session Expired ");// + strTitle);
                   
                   HorizontalFieldManager h = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER)
       			{
       				public void paintBackground(Graphics graphics)
       				{
       					graphics.setBackgroundColor(Color.BLACK);
       					graphics.clear();
       					}
       				};
       			h.add(new ButtonField("Login",  FOCUSABLE | DrawStyle.HCENTER)  {

       				protected boolean navigationClick(int status,int time) {
       					Action action = new Action(ActionCommand.CMD_USER_REGISTRATION,null);
						ActionInvoker.processCommand(action);
       					return super.navigationClick(status, time);
       				}
       			});
                   
                   add(lblTitle);
                   add(h);
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
public boolean onClose() {
	Action action = new Action(ActionCommand.CMD_GRID_SCREEN,null);
	ActionInvoker.processCommand(action);
	return true;
}
}
