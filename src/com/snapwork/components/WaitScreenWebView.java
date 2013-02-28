package com.snapwork.components;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.LOG;

/**
 * 
 * <p>This class shows loading message for WebView 
 *
 */

public class WaitScreenWebView extends MainScreen implements RemovableScreen
{
	private String _message="";
	/**
	 * 
	 * @param title Screen Title
	 * @param message Loading message
	 */
	public WaitScreenWebView(String title, String message)
	{
		_message = message;
		VerticalFieldManager titleBarFieldManager = new VerticalFieldManager(USE_ALL_WIDTH | USE_ALL_HEIGHT)
		{
			protected void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
				graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
				int hgt = (AppConstants.screenHeight/2)-(graphics.getFont().getHeight()/2);
				graphics.setColor(0x737573);
				graphics.fillRoundRect(9, hgt-graphics.getFont().getHeight()-1, AppConstants.screenWidth-18, (graphics.getFont().getHeight()*3)+2, 8, 8);
				graphics.setColor(0x181818);
				graphics.fillRoundRect(10, hgt-graphics.getFont().getHeight(), AppConstants.screenWidth-20, graphics.getFont().getHeight()*3, 8, 8);
				graphics.setColor(0xeeeeee);
				graphics.drawText(_message, (AppConstants.screenWidth/2)-(graphics.getFont().getAdvance(_message)/2), hgt);
			}
		};
		titleBarFieldManager.add(new TitleBar(title));
		add(titleBarFieldManager);
	}
	public boolean onClose()
	{
		ActionInvoker.processCommand(new Action(ActionCommand.CMD_GRID_SCREEN,null));
		return false;
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
