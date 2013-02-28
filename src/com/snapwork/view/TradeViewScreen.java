package com.snapwork.view;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.TitleBar;
import com.snapwork.util.AppConstants;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.Utils;

public class TradeViewScreen extends MainScreen implements ActionListener, RemovableScreen
{
	Vector newsDataVector = null;
	private BottomMenu bottomMenu = null;
	public final static byte NEWS_SECTION = 0;
	ButtonField btnsrh;
	ButtonField btn;
	//private BrowserField myBrowserField ;
	//private CustomBrowserFieldListener myfield;

	public TradeViewScreen()
	{
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		createUI("ShareKhan Trade");
	}


	public void createUI(String title)
	{
		//set the title bar
		VerticalFieldManager titleBarFieldManager = new VerticalFieldManager(Manager.NO_VERTICAL_SCROLL | USE_ALL_WIDTH)
		{
			protected void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}

		};
		titleBarFieldManager.add(new TitleBar(title));
		VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR  | USE_ALL_WIDTH) {
        	public void paintBackground(Graphics graphics)
            {
               graphics.setColor(Color.BLACK);
               graphics.fillRect(0, 0, getWidth(), getHeight());
            }  
        	 protected void sublayout( int maxWidth, int maxHeight )
             {
             	super.sublayout(maxWidth,maxHeight);
             	if(bottomMenu.isAttached) {
 	            	if(getField(0).getPreferredHeight()<AppConstants.screenHeight-BottomMenu.BottomMenuImageHeight-TitleBar.getItemHeight()) {
 	            		setExtent(maxWidth, AppConstants.screenHeight-BottomMenu.BottomMenuImageHeight-TitleBar.getItemHeight());
 	            	}
             	} else {
 	            	if(getField(0).getPreferredHeight()<AppConstants.screenHeight-TitleBar.getItemHeight()) {
 	            		setExtent(maxWidth, AppConstants.screenHeight-TitleBar.getItemHeight());
 	            	}            		
             	}
             }
        };
        
			
			add(titleBarFieldManager);
			btnsrh = new ButtonField("Equities",  FOCUSABLE | DrawStyle.HCENTER)  {

				protected boolean navigationClick(int status,int time) {
					Utils.FNO_SEARCH = false;
					ActionInvoker.processCommand(new Action(ActionCommand.CMD_SEARCH_SCREEN,null));
					
					//UiApplication.getUiApplication().invokeLater(new Runnable() {
					//	public void run() {
					//		customPopUpScreen = new CustomPopUpScreen();
					//		UiApplication.getUiApplication().pushScreen(customPopUpScreen);
					//	}
					//});
					return super.navigationClick(status, time);
				}
				/*protected void layout(int arg0, int arg1) {
					setExtent(getFont().getAdvance("#Futures / Options#"), arg1);
					//super.layout(getFont().getAdvance("#Futures / Options#"), arg1);
				}*/

			};
			HorizontalFieldManager hSearch = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER )
			{
				public void paintBackground(Graphics graphics)
				{
					graphics.setColor(Color.BLACK);
					graphics.fillRect(0, 0, getWidth(), getHeight());
				}
				/**protected void sublayout(int width, int height) {
					layoutChild(getField(0), btnsrh.getFont().getAdvance("#Futures / Options#"), btnsrh.getHeight());
                         setPositionChild(getField(0), (AppConstants.screenWidth/2)-(btnsrh.getFont().getAdvance("#Futures / Options#")/2), 0);
                                        super.sublayout(width, height);
                                }*/
			};
			
			btn = new ButtonField("Futures / Options",  FOCUSABLE | DrawStyle.HCENTER)  {

				protected boolean navigationClick(int status,int time) {
					Utils.FNO_SEARCH = true;
					ActionInvoker.processCommand(new Action(ActionCommand.CMD_SEARCH_SCREEN,null));
					//UiApplication.getUiApplication().invokeLater(new Runnable() {
					//	public void run() {
					//		customPopUpScreen = new CustomPopUpScreen();
					//		UiApplication.getUiApplication().pushScreen(customPopUpScreen);
					//	}
					//});
					return super.navigationClick(status, time);
				}
				/*protected void layout(int arg0, int arg1) {
					setExtent(getFont().getAdvance("#Futures / Options#"), arg1);
					//super.layout(getFont().getAdvance("#Futures / Options#"), arg1);
				}*/

			};
			HorizontalFieldManager hFNOSearch = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER )
			{
				public void paintBackground(Graphics graphics)
				{
					graphics.setColor(Color.BLACK);
					graphics.fillRect(0, 0, getWidth(), getHeight());
				}
			/*	protected void sublayout(int width, int height) {
					layoutChild(getField(0), btn.getFont().getAdvance("#Futures / Options#"), btnsrh.getHeight());
                         setPositionChild(getField(0), (AppConstants.screenWidth/2)-(btn.getFont().getAdvance("#Futures / Options#")/2), 0);
                                        super.sublayout(width, height);
                                }*/
			};
			btnsrh.setMinimalWidth(btn.getFont().getAdvance("##Futures / Options##"));
			btn.setMinimalWidth(btn.getFont().getAdvance("##Futures / Options##"));
			hSearch.add(btnsrh);
			hFNOSearch.add(btn);
			mainManager.add(new LabelField("", NON_FOCUSABLE));
			mainManager.add(new LabelField("", NON_FOCUSABLE));
			
			mainManager.add(hSearch);
			mainManager.add(hFNOSearch);
			add(mainManager);
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_WEBVIEW_SCREEN, AppConstants.bottomMenuCommands);
	}



	public void actionPerfomed(byte Command, Object sender)
	{
		ActionInvoker.processCommand(new Action(Command,null));
	}

	public boolean onMenu(int instance) 
	{
		return false;
	}

	/*public boolean keyChar( char key, int status, int time )
	{
		return false;
	}*/

	public boolean keyDown( int keyCode, int time )
	{

		int key = Keypad.key( keyCode );
		if(key == Keypad.KEY_MENU)
		{
			UiApplication.getUiApplication().invokeLater(new Runnable()
			{
				public void run()
				{
					try {
						if(bottomMenu != null)
							bottomMenu.autoAttachDetachFromScreen();
					} catch (Exception e) {
					}
				}
			});
		}
		else if(key == Keypad.KEY_END)
		{
			LOG.print("KEY_END EXIT from app");
			System.exit(0);
		}else
			return super.keyDown(keyCode, time);

		return true;
	}
	protected void paintBackground(Graphics graphics)
	{
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getWidth(), getHeight());
	}
}

