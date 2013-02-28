package com.snapwork.view;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.GLTabView;
import com.snapwork.components.RefreshButton;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TitleBarRefresh;
import com.snapwork.interfaces.ReturnData;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.parsers.GlobalParser;
import com.snapwork.parsers.TopGainLoseItemParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.AutoScreenRefresherThread;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;

public class TopGLScreen extends MainScreen implements AutoRefreshableScreen,ActionListener,ThreadedComponents,ReturnData,ReturnDataWithId {

	private BottomMenu bottomMenu = null;
	public byte selectedIndex = 0;
	private String[] strGroup= {"BSE","NSE","Global"};
	private GLTabView glt;
	private  VerticalFieldManager mainManager;
	private boolean refreshFlag;
	private RefreshButton refreshme;
	private TopGLScreen topGLScreen;
	private TopTabber topTabber;
	private VerticalFieldManager titleBarFieldManager;
	
	public TopGLScreen(Vector vector,byte selectedIndex_) {
//		Dialog.alert("here");
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		AutoScreenRefresherThread.onLoad = false;
		LOG.print("inside top gl");
		AppConstants.REMOVE_FNO =true;
		refreshFlag = true;
		selectedIndex = selectedIndex_;
		refreshme = new RefreshButton()
		{
			protected boolean navigationClick(int arg0, int arg1) {
					
				refreshFields();
					return super.navigationClick(arg0, arg1);
				}
		};
		/*titleBarFieldManager = new VerticalFieldManager(USE_ALL_WIDTH) {

			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}

		};
		titleBarFieldManager.add(new TitleBarRefresh(strGroup[selectedIndex]+" Top Gainers-Losers",refreshme));
		titleBarFieldManager.add(new TopTabber(this,selectedIndex,(ThreadedComponents)this));
		setTitle(titleBarFieldManager);*/
		/*refreshme = new RefreshButton()
		{
			protected boolean navigationClick(int arg0, int arg1) {
					
				refreshFields();
					return super.navigationClick(arg0, arg1);
				}
		};*/
		 titleBarFieldManager = new VerticalFieldManager(USE_ALL_WIDTH) {

			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}

		};
		//titleBarFieldManager.add(new TitleBarRefresh(strGroup[selectedIndex]+" Top Gainers-Losers",refreshme));
		titleBarFieldManager.add(new TitleBarRefresh(" Top Gainers-Losers",refreshme));
		topTabber = new TopTabber(this,selectedIndex,(ThreadedComponents)this);
		titleBarFieldManager.add(topTabber);
		setTitle(titleBarFieldManager);
			
		createUI(vector,selectedIndex);
	}

	public void createUI(Vector vector,byte selectedIndex_) {
		//set the title bar
	
		
		
		boolean fl = true;
		if(getFieldCount()>0)
		{fl = false;
			UiApplication.getUiApplication().invokeAndWait(new Runnable() {
				public void run() {

					try {
						deleteAll();
					
					} 
					catch (Exception e) {
						LOG.print("Exception in TopGL");
					}
				}
			});
		}
		
		else{fl =true;
		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {
				titleBarFieldManager.delete(topTabber);
				topTabber.setIndex(GLTabView.selectedIndex);
				titleBarFieldManager.add(topTabber);
				setTitle(titleBarFieldManager);
			}
		});
		
		/*VerticalFieldManager titleBarFieldManager = new VerticalFieldManager(USE_ALL_WIDTH) {

			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}

		};
		titleBarFieldManager.add(new TitleBarRefresh(strGroup[selectedIndex_]+" Top Gainers-Losers",refreshme));
		titleBarFieldManager.add(new TopTabber(this,selectedIndex_,(ThreadedComponents)this));
		setTitle(titleBarFieldManager);*/
		}
		mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
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
		mainManager.add(new GLTabView(vector,this,strGroup[selectedIndex_]));
		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {

				try {	add(mainManager);
				} 
				catch (Exception e) {
					LOG.print("Exception in TopGL");
				}
			}
		});
		
		// Configure BottomMenu and set commands
		if(fl){
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_BSE_GL_SCREEN, AppConstants.bottomMenuCommands);
		}
	} 

	public void del(final Vector vector)
	{
		final TopGLScreen tgl = this;
		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {

				try {
					delete(mainManager);
					//mainManager.deleteAll();
					mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
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
					mainManager.add(new GLTabView(vector,tgl,strGroup[selectedIndex]));
					add(mainManager);
				} catch (Exception e) {
					LOG.print("Exception in TOpGL");
				}
			}
		});
		refreshFlag = false;
	}

	public void componentsPrepared(byte componentID, Object component) {
		// TODO Auto-generated method stub

	}

	public void componentsDataPrepared(byte componentID, final Object data) {
		if(!(UiApplication.getUiApplication().getActiveScreen() instanceof TopGLScreen))
			return;
		switch(componentID) {
		case ActionCommand.CMD_BSE_GL_SCREEN:
			AppConstants.REMOVE_FNO =true;
			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					deleteAll();
					createUI((Vector)data, (byte)0);	
					refreshme.setLoading(false);
				}
			});
			break;
		case ActionCommand.CMD_NSE_GL_SCREEN:
			AppConstants.REMOVE_FNO = false;
			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					deleteAll();
					createUI((Vector)data, (byte)1);	
					refreshme.setLoading(false);	
				}
			});
			break;
		case ActionCommand.CMD_GLOBAL_GL_SCREEN:
			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					deleteAll();
					createUI((Vector)data, (byte)2);	
					refreshme.setLoading(false);	
				}
			});
			break;
		}
	}

	public void actionPerfomed(byte Command, Object sender) {
		switch (Command) {
		case ActionCommand.CMD_COMPANY_DETAILS_SCREEN:
			ActionInvoker.processCommand(new Action(Command, sender));
			break;
		default:
			ActionInvoker.processCommand(new Action(Command, sender));
			break;
		}
	}

	public boolean onMenu(int instance) 
	{
		return false;
	}
	protected  void makeMenu(Menu menu, int instance)
    {
        ContextMenu contextMenu = ContextMenu.getInstance();
        contextMenu.setTarget(this);
        contextMenu.clear();
        this.makeContextMenu(contextMenu);
        menu.deleteAll();
        menu.add(contextMenu);
    }
    /*public void makeContextMenu(ContextMenu contextMenu)
    {
            contextMenu.addItem(YourMenuItem);   

    }*/

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

	private class TopTabber extends Manager {

		LabelField lblBSETab = null;
		LabelField lblNSETab = null;
		LabelField lblGLOBALTab = null;
		private byte padding = 4;
		byte selectedIndex = 0;

		public TopTabber(final ActionListener actionListner,byte selectedIndex,final ThreadedComponents threadedComponents) {
			super(FOCUSABLE);

			this.selectedIndex = selectedIndex;

			lblBSETab = new LabelField("BSE", FOCUSABLE) {

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getFont().getAdvance(getText())+2, getFont().getHeight());
				}

				public int getPreferredHeight() {
					return getFont().getHeight()+2;
				}

				public int getPreferredWidth() {
					return getFont().getAdvance(getText())+2;
				}

				protected void paint(Graphics graphics) {
					if(isFocus()) {
						graphics.setColor(Color.ORANGE);
						//graphics.drawRect(0, 0, getWidth(), getHeight());
						graphics.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1);
					}
					graphics.setColor(0xeeeeee);
					graphics.drawText(getText(), 0, 0);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
					//Dont do anything and let the system
				}

				protected void onFocus(int direction) {
					super.onFocus(direction);
					invalidate();
				}

				protected void onUnfocus() {
					super.onUnfocus();
					invalidate();
				}

				protected boolean navigationClick(int status, int time) {
					GLTabView.selectedIndex = 0;
					actionListner.actionPerfomed(ActionCommand.CMD_BSE_GL_SCREEN, threadedComponents);
					return super.navigationClick(status, time);
				}
				protected boolean touchEvent(TouchEvent arg0) 
				{
					return super.touchEvent(arg0);
				};
			};
			if(AppConstants.screenHeight>=480)
				lblBSETab.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
			else
				lblBSETab.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));

			lblNSETab = new LabelField("NSE", FOCUSABLE) {

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getFont().getAdvance(getText())+2, getFont().getHeight());
				}

				protected void paint(Graphics graphics) {
					if(isFocus()) {
						graphics.setColor(Color.ORANGE);
						//graphics.drawRect(0, 0, getWidth(), getHeight());
						graphics.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1);
					}
					graphics.setColor(0xeeeeee);
					graphics.drawText(getText(), 0, 0);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
					//Dont do anything and let the system
				}

				protected void onFocus(int direction) {
					super.onFocus(direction);
					invalidate();
				}

				protected void onUnfocus() {
					super.onUnfocus();
					invalidate();
				}

				protected boolean navigationClick(int status, int time) {
					GLTabView.selectedIndex = 1;
					actionListner.actionPerfomed(ActionCommand.CMD_NSE_GL_SCREEN, threadedComponents);
					return super.navigationClick(status, time);
				}
				protected boolean touchEvent(TouchEvent arg0) 
				{
					return super.touchEvent(arg0);
				};
			};
			if(AppConstants.screenHeight>=480)
				lblNSETab.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
			else
				lblNSETab.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));

			lblGLOBALTab = new LabelField("Global", FOCUSABLE) {

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getFont().getAdvance(getText())+2, getFont().getHeight());
				}

				protected void paint(Graphics graphics) {
					if(isFocus()) {
						graphics.setColor(Color.ORANGE);
						//graphics.drawRect(0, 0, getWidth(), getHeight());
						graphics.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1);
					}
					graphics.setColor(0xeeeeee);
					graphics.drawText(getText(), 0, 0);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
					//Dont do anything and let the system
				}

				protected void onFocus(int direction) {
					super.onFocus(direction);
					invalidate();
				}

				protected void onUnfocus() {
					super.onUnfocus();
					invalidate();
				}

				protected boolean navigationClick(int status, int time) {
					GLTabView.selectedIndex = 2;
					actionListner.actionPerfomed(ActionCommand.CMD_GLOBAL_GL_SCREEN, threadedComponents);
					return super.navigationClick(status, time);
				}
				protected boolean touchEvent(TouchEvent arg0) 
				{
					return super.touchEvent(arg0);
				};
			};
			if(AppConstants.screenHeight>=480)
				lblGLOBALTab.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
			else
				lblGLOBALTab.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
			add(lblBSETab);
			add(lblNSETab);
			add(lblGLOBALTab);

		}
		

		protected void sublayout(int width, int height) {
			layoutChild(getField(0), width, height);
			layoutChild(getField(1), width, height);
			layoutChild(getField(2), width, height);
			setPositionChild(getField(0),0+ ((AppConstants.screenWidth/3)/2)-(getField(0).getPreferredWidth()/2), padding/2);
			setPositionChild(getField(1),(AppConstants.screenWidth/3)+ ((AppConstants.screenWidth/3)/2)-(getField(1).getPreferredWidth()/2), padding/2);
			setPositionChild(getField(2),((AppConstants.screenWidth/3)*2)+ ((AppConstants.screenWidth/3)/2)-(getField(2).getPreferredWidth()/2), padding/2);

			setExtent(AppConstants.screenWidth, getField(0).getPreferredHeight()+padding*2);
		}

		protected void drawFocus(Graphics graphics, boolean on) {

		}
public void setIndex(byte selectedIndex)
{
	this.selectedIndex = selectedIndex;
	invalidate();
	}
		protected void paintBackground(Graphics graphics) {

			graphics.setColor(Color.GRAY);
			graphics.fillRect(0, 0, getWidth(), getHeight());

			graphics.setColor(Color.BLACK);
			if(selectedIndex==1)
			{
				graphics.fillRect(0, 0, (AppConstants.screenWidth/3), getField(0).getPreferredHeight()+padding);
				graphics.fillRect((AppConstants.screenWidth/3)*2, 0, (AppConstants.screenWidth/3), getField(0).getPreferredHeight()+padding);
			}
			else if(selectedIndex==2)
			{
				graphics.fillRect(0, 0, (AppConstants.screenWidth/3)*2, getField(0).getPreferredHeight()+padding);
				graphics.setColor(Color.GRAY);
				graphics.drawLine((AppConstants.screenWidth/3), 0, (AppConstants.screenWidth/3), getField(0).getPreferredHeight()+padding);
			}
			else
			{
				graphics.fillRect((AppConstants.screenWidth/3), 0, (AppConstants.screenWidth/3)*2, getField(0).getPreferredHeight()+padding);
				graphics.setColor(Color.GRAY);
				graphics.drawLine((AppConstants.screenWidth/3)*2, 0, (AppConstants.screenWidth/3)*2, getField(0).getPreferredHeight()+padding);
			}


		}

	}
	protected void paintBackground(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getWidth(), getHeight());
	}
	public Vector getComponentData() {
		// TODO Auto-generated method stub
		return null;
	}

	public void refreshFields() {
		if(refreshme.getLoading()) return;
		LOG.print("Refreshing Top GL Tab");
		if(refreshFlag){
			refreshme.setLoading(true);
			String url ;
			if(GLTabView.selectedIndex == 0)
				{
					url = AppConstants.bseTopGainersLoosersUrl;
					TopGainLoseItemParser topGainerLoserItemParser = new TopGainLoseItemParser(url,this);
					topGainerLoserItemParser.getScreenData();
				}
			else if(GLTabView.selectedIndex == 1)
				{
					url = AppConstants.nseTopGainersLoosersUrl;
					TopGainLoseItemParser topGainerLoserItemParser = new TopGainLoseItemParser(url,this);
					topGainerLoserItemParser.getScreenData();
				}
			else if(GLTabView.selectedIndex == 2)
			{
				url = AppConstants.globalTopGainersLoosersUrl;
				GlobalParser globalGainerLoserItemParser = new GlobalParser(url,this);
				globalGainerLoserItemParser.getScreenData();
			}
		}
	}

	public void setData(final Vector vector) {
		//del(vector);
		
		refreshme.setLoading(false);
		//if(GLTabView.selectedIndex ==  selectedIndex)
		//{
		LOG.print("--GLTabView.selectedIndex "+GLTabView.selectedIndex);
		LOG.print("--selectedIndex "+selectedIndex);
		if(GLTabView.selectedIndex == selectedIndex){
			createUI(vector, GLTabView.selectedIndex);
		}
		//}
		//final GLTabView gl = new GLTabView(vector,this,strGroup[selectedIndex]);
		/*final TopGLScreen tgl = this;
		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {

		try {
			delete(mainManager);
			//mainManager.deleteAll();
			 mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
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
			mainManager.add(new GLTabView(vector,tgl,strGroup[selectedIndex]));
			add(mainManager);
		} catch (Exception e) {
			LOG.print("Exception in TOpGL");
		}
			}
		});
		refreshFlag = false;*/

	}

	public void setData(Vector vector, int id) {
		refreshme.setLoading(false);
		if(GLTabView.selectedIndex == selectedIndex){
		createUI(vector, GLTabView.selectedIndex);
		}
	}

}
