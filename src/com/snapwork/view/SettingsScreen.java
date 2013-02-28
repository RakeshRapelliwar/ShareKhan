package com.snapwork.view;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomBasicEditField;
import com.snapwork.components.RadioButtonLayout;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.util.AppConstants;
import com.snapwork.util.DBPackager;
import com.snapwork.util.DBmanager;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.ScreenInvoker;

public class SettingsScreen extends MainScreen implements ActionListener,ThreadedComponents {

	private BottomMenu bottomMenu = null;
	private CustomBasicEditField txtFieldSearch = null;
	private RadioButtonGroup rbg = null;
	private RadioButtonField on = null;
	private RadioButtonField off = null;
	private LabelField message = null;

	//private Vector searchDataVector = null;

	public SettingsScreen() {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		createUI();
	}

	public void createUI() {
		//Sets the title
		//setBanner(new TitleBar("Search"));
		setTitle(new TitleBar("SETTINGS"));

		VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
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
		rbg = new RadioButtonGroup();
		boolean flag = true;
		if(DBmanager.getRecords(AppConstants.appNotificationDBName).size()>0)
		{
			flag = false;
		}
		on = new RadioButtonField("ON",rbg,flag, RadioButtonField.FIELD_RIGHT)
		{
			protected void paint(Graphics graphics) {
				graphics.setColor(0xeeeeee);
				super.paint(graphics);
			}
			protected boolean navigationClick(int keycode,
					int time){
				setSelected(true);
				//DBmanager.deleteRecordFromRMS("DISABLE", 0);
				DBmanager.dropDatabase(AppConstants.appNotificationDBName);
				//System.out.println("ON -=-=-=-=-=-=-=-=-=-=--=--=-=-=-=-=-=- ");
				return true;
			}
		}; //on.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
		flag = !flag;
		off = new RadioButtonField("OFF",rbg,flag, RadioButtonField.FIELD_RIGHT)
		{
			protected void paint(Graphics graphics) {
				graphics.setColor(0xeeeeee);
				super.paint(graphics);
			}
			/*protected void paint(Graphics graphics) {
        		graphics.setColor(0xffffff);
        		graphics.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
        		graphics.drawText("OFF", getWidth() - getFont().getAdvance("ON"), 1);
        	};*/
			protected boolean navigationClick(int keycode,
					int time){
				setSelected(true);
				if(DBmanager.getRecords(AppConstants.appNotificationDBName).size()==0)
					DBmanager.addData(AppConstants.appNotificationDBName, DBPackager.createNotificationPackage("DISABLE"));
				//System.out.println("OFF -=-=-=-=-=-=-=-=-=-=--=--=-=-=-=-=-=- ");
				return true;
			}
		};//on.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));

		message = new LabelField("Notifications", NON_FOCUSABLE | FIELD_HCENTER | FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				graphics.setColor(0xeeeeee);
				super.paint(graphics);
			}
		};
		message.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));

		RadioButtonLayout v = new RadioButtonLayout();
		v.add(message);
		v.add(on);
		v.add(off);
		mainManager.add(v);
		//mainManager.add(message);
		add(mainManager);
		// Configure BottomMenu and set commands
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_SEARCH_SCREEN, AppConstants.bottomMenuCommands);
	}

	public boolean onSavePrompt() {
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
		} else
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

	public void actionPerfomed(byte Command, Object data) {
		switch(Command) {
		case ActionCommand.CMD_SEARCH_COMPANY:
			if(txtFieldSearch.getText().trim().length()==0) {
				ScreenInvoker.showDialog("Please enter some keyword");
				return;
			}
			Vector vectorSearchData = new Vector();
			vectorSearchData.addElement(txtFieldSearch.getText());
			vectorSearchData.addElement((ThreadedComponents)this);

			ActionInvoker.processCommand(new Action(Command, vectorSearchData));
			break;
		default:
			Debug.debug("data : "+(data==null));
			ActionInvoker.processCommand(new Action(Command,data));
			break;
		}
	}

	public Vector getComponentData() {
		// TODO Auto-generated method stub
		return null;
	}

	public void componentsDataPrepared(byte componentID, Object data) {
		// TODO Auto-generated method stub

	}
}
