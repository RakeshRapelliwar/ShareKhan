package com.snapwork.view;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.text.TextFilter;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomBasicEditField;
import com.snapwork.components.CustomLinkButton;
import com.snapwork.components.ProfileChangeListField;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;

public class ProfileChangeScreen extends MainScreen implements ActionListener {
	private BottomMenu bottomMenu = null;
	private VerticalFieldManager profileChangeResults = null;
	private CustomBasicEditField txtFieldSearch = null;
	private long timer;
	
	public ProfileChangeScreen()
	{
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		createUI();
	}

	public void createUI()
	{
		timer = System.currentTimeMillis();
		VerticalFieldManager titleBar = new VerticalFieldManager(USE_ALL_WIDTH);
		titleBar.add(new TitleBar("Profile Change"));

		VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR)
		{
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}            
			protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(AppConstants.screenWidth, AppConstants.screenHeight);
				setExtent(AppConstants.screenWidth, AppConstants.screenHeight-BottomMenu.getItemHeight()-TitleBar.getItemHeight());
			}
		};

		final CustomLinkButton btnGo = new CustomLinkButton(" Go ", FIELD_VCENTER | FOCUSABLE, 0xeeeeee, FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT)) {

			protected boolean navigationClick(int status, int time) {
				if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
				actionPerfomed(ActionCommand.CMD_SEARCH_COMPANY, null);
			}
				return super.navigationClick(status, time);
			}

			protected boolean touchEvent(TouchEvent message) {
				if(message.getEvent() == TouchEvent.CLICK) {
					if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
					actionPerfomed(ActionCommand.CMD_SEARCH_COMPANY, null);
					}
			}return super.touchEvent(message);
			}
		};

		txtFieldSearch = new CustomBasicEditField(BasicEditField.NO_NEWLINE) {

			public int getPreferredHeight() {
				return getFont().getHeight()+(AppConstants.padding/3)*2;
			}

			public int getPreferredWidth() {
				return AppConstants.screenWidth - AppConstants.padding*2 - btnGo.getPreferredWidth();
			}

			protected boolean navigationClick(int status, int time) {
				actionPerfomed(ActionCommand.CMD_SEARCH_COMPANY, null);
				return super.navigationClick(status, time);
			}
			public boolean keyDown( int keyCode, int time )
			{
				int key = Keypad.key( keyCode );

				if(key == Keypad.KEY_ENTER) {
					actionPerfomed(ActionCommand.CMD_SEARCH_COMPANY, null);
				} else
					return super.keyDown(keyCode, time);

				return true;
			}

		};
		txtFieldSearch.setFilter(TextFilter.get(TextFilter.DEFAULT));
		txtFieldSearch.setFont(FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_FONT));
		HorizontalFieldManager txtFieldManager = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR);
		txtFieldManager.add(txtFieldSearch);
		setTitle(titleBar);
		profileChangeResults = new VerticalFieldManager(USE_ALL_WIDTH);
		mainManager.add(profileChangeResults);
		add(mainManager);
		//bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_SEARCH_SCREEN, AppConstants.bottomMenuCommands);
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
		Vector data = new Vector();
		data.addElement("Membership Password");
		data.addElement("Trading Password");
		/*data.addElement("Membership Hint Q&A");
		data.addElement("Trading Hint Q&A");*/
		fillProfileChangeData((Vector)data);
	}

	public boolean onSavePrompt()
	{
		return true;
	}

	public boolean onMenu(int instance) 
	{
		return false;
	}

	public boolean keyChar( char key, int status, int time )
	{
		return super.keyChar(key, status, time);
	}

	public boolean keyDown( int keyCode, int time )
	{
		int key = Keypad.key( keyCode );

		if(key == Keypad.KEY_MENU) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
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
		}
		else
			return super.keyDown(keyCode, time);

		return true;
	}

	public boolean keyUp( int keyCode, int time )
	{
		return super.keyUp(keyCode, time);
	}

	public void componentsPrepared(byte componentID, Object component) {
		// TODO Auto-generated method stub

	}

	public void fillProfileChangeData(final Vector profileChangeDataVector)
	{
		final ActionListener actionListner = this;
		UiApplication.getUiApplication().invokeLater(new Runnable()
		{
			public void run() {
				if(profileChangeDataVector==null)
				{
					Dialog.alert("No Records Found!...");
					return;
				}
				else if(profileChangeDataVector.size()==0)
				{
					Dialog.alert("No Records Found!...");
					return;
				}
				profileChangeResults.deleteAll();
				for(int i=0;i<profileChangeDataVector.size();i++)
				{
					String profileChange = (String)profileChangeDataVector.elementAt(i);
					LOG.print("profileChange : "+profileChange);
					ProfileChangeListField profileListField = new ProfileChangeListField(FOCUSABLE, profileChange, actionListner);
					profileChangeResults.add(profileListField);
					}
				profileChangeResults.add(new NullField(FOCUSABLE));
			}
		});
	}
	public void actionPerfomed(byte Command, Object data) {
		ActionInvoker.processCommand(new Action(Command,data));
	}
}
