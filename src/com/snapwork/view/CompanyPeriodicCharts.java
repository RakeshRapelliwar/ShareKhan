package com.snapwork.view;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.areacharts.ChartProperties;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.LoadingComponent;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.util.AppConstants;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;

public class CompanyPeriodicCharts extends MainScreen implements ThreadedComponents,ActionListener {

	private BottomMenu bottomMenu = null;
	private String companyID = null;
	private String companyName = null;

	private String[] strChartCommandTexts = {"Intraday","Week","Month","6 Months","Year"};

	public CompanyPeriodicCharts(String title,String companyID,byte index) {
		this.companyID = companyID;
		this.companyName = title;
		createUI(title,index);
	}

	protected void paintBackground(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, AppConstants.screenWidth, AppConstants.screenHeight);
	}

	//Fields for the screen
	HorizontalFieldManager horizontalFieldManager = null;
	VerticalFieldManager verticalFieldManager = null;

	//Manager horizontalChartCommandBar = null;

	private void setTitleBar(String title,byte index) {
		try {
			if(TitleBar.getStringWidth(title + " : " +strChartCommandTexts[index]+" Chart")>AppConstants.screenWidth) {
				for(int i=1;i<title.length();i++) {
					if(TitleBar.getStringWidth(title.substring(0, i) + "... : " +strChartCommandTexts[index]+" Chart")>(AppConstants.screenWidth-AppConstants.screenWidth/10)) {
						setTitle(new TitleBar(title.substring(0, i-1) + "... : " +strChartCommandTexts[index]+" Chart"));
						i = title.length();
					}
				}
			} else {
				setTitle(new TitleBar(title + " : " +strChartCommandTexts[index]+" Chart"));  
			}
		} catch(Exception ex) {
			setTitle(new TitleBar(title + " : " +strChartCommandTexts[index]+" Chart"));    		
		}
	}

	public void createUI(String title,byte index) {
		//Sets the title
		setTitleBar(title,index);

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
		
		//Create Horizontal Field Manager
		horizontalFieldManager = new HorizontalFieldManager(FIELD_HCENTER | FIELD_HCENTER | FOCUSABLE) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, AppConstants.screenWidth, AppConstants.screenHeight);
			}
		};

		//Create Vertical Field Manager
		verticalFieldManager = new VerticalFieldManager(USE_ALL_WIDTH | FOCUSABLE) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, AppConstants.screenWidth, AppConstants.screenHeight);
			}
		};

		horizontalFieldManager.add(new LoadingComponent(AppConstants.loadingMessage,AppConstants.screenWidth,ChartProperties.getFullScreenChartProperties().getChartHeight()+ChartProperties.getFullScreenChartProperties().getChartxAxisHeight()));

		verticalFieldManager.add(horizontalFieldManager);
		//verticalFieldManager.add(horizontalChartCommandBar);

		mainManager.add(verticalFieldManager);
		add(mainManager);

		// Configure BottomMenu and set commands
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomMenuCommands);

		//horizontalChartCommandBar.getField(index).setFocus();
	}

	public void componentsPrepared(final byte componentID,final Object component) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				if(component==null) {
					ScreenInvoker.showDialog("No Data to Display!");
					UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
				} else  {
					if(component instanceof Field) {
						switch(componentID) {
						case ActionCommand.CMD_COMPANY_FULL_INTRA_CHART:
							setTitleBar(companyName,(byte)0);
							break;
						case ActionCommand.CMD_COMPANY_FULL_WEEK_CHART:
							setTitleBar(companyName,(byte)1);
							break;
						case ActionCommand.CMD_COMPANY_FULL_MONTH_CHART:
							setTitleBar(companyName,(byte)2);
							break;
						case ActionCommand.CMD_COMPANY_FULL_6MONTH_CHART:
							setTitleBar(companyName,(byte)3);
							break;
						case ActionCommand.CMD_COMPANY_FULL_12MONTH_CHART:
							setTitleBar(companyName,(byte)4);
							break;
						}
						horizontalFieldManager.deleteAll();
						horizontalFieldManager.add((Field)component);
					}
				}
			}
		});
	}

	public void componentsDataPrepared(byte componentID, final Object data) {

	}

	public void actionPerfomed(byte Command, Object sender) {
		switch(Command) {
		case ActionCommand.CMD_COMPANY_FULL_INTRA_CHART:
		case ActionCommand.CMD_COMPANY_FULL_WEEK_CHART:
		case ActionCommand.CMD_COMPANY_FULL_MONTH_CHART:
		case ActionCommand.CMD_COMPANY_FULL_6MONTH_CHART:
		case ActionCommand.CMD_COMPANY_FULL_12MONTH_CHART:
			Vector vectorCommand = new Vector();
			vectorCommand.addElement(companyName);
			vectorCommand.addElement(companyID);
			vectorCommand.addElement((ThreadedComponents)this);
			ActionInvoker.processCommand(new Action(Command,vectorCommand));
			break;
		default:
			ActionInvoker.processCommand(new Action(Command,null));
			break;
		}
	}

	public boolean onMenu(int instance) 
	{
		return false;
	}

	public boolean keyChar( char key, int status, int time )
	{
		return false;
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

	public Vector getComponentData() {
		// TODO Auto-generated method stub
		return null;
	}

}
