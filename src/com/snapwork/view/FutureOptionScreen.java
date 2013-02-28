package com.snapwork.view;


import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.GL_FutureOptionView;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;

public class FutureOptionScreen extends MainScreen implements ActionListener,ThreadedComponents, FieldChangeListener {

	private BottomMenu bottomMenu = null;
	public byte selectedIndex = 0;
	private String[] strGroup = {"Futures","Options"};
	public FutureOptionScreen(Vector vector,byte selectedIndex) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		LOG.print("Futures and Options Screen");
		createUI(vector,selectedIndex);
	}

	public void createUI(Vector vector,byte selectedIndex) {
		//set the title bar
		VerticalFieldManager titleBarFieldManager = new VerticalFieldManager(USE_ALL_WIDTH) {

			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}

		};
		String title = AppConstants.COMPANY_NAME  + " Derivatives";
		titleBarFieldManager.add(new TitleBar(title));
		titleBarFieldManager.add(new TopTabber(this,selectedIndex,(ThreadedComponents)this));
		setTitle(titleBarFieldManager);
		VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR | Manager.USE_ALL_HEIGHT) {
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
		mainManager.add(new GL_FutureOptionView(vector,this,strGroup[selectedIndex], this));
		add(mainManager);
		// Configure BottomMenu and set commands
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_BSE_GL_SCREEN, AppConstants.bottomMenuCommands);
		
	} 

	public void componentsPrepared(byte componentID, Object component) {
		// TODO Auto-generated method stub

	}

	public void componentsDataPrepared(byte componentID, final Object data) {
		switch(componentID) {
		case ActionCommand.CMD_FUTURE_SCREEN:
			AppConstants.OPTIONS_FLAG = false;
			AppConstants.OPTIONS_FLAG_First = false;
			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					deleteAll();
					createUI((Vector)data, (byte)0);	
				}
			});
			break;
		case ActionCommand.CMD_OPTION_SCREEN:
			AppConstants.OPTIONS_FLAG = true;
			AppConstants.OPTIONS_FLAG_First = true;
			AppConstants.optionsMonth="";
			AppConstants.optionsAmount="";
			AppConstants.optionsCEPE="";
			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					deleteAll();
					createUI((Vector)data, (byte)1);	
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
		else
			if(key == Keypad.KEY_END)
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
		private byte padding = 4;
		byte selectedIndex = 0;

		public TopTabber(final ActionListener actionListner,byte selectedIndex,final ThreadedComponents threadedComponents) {
			super(FOCUSABLE);

			this.selectedIndex = selectedIndex;

			lblBSETab = new LabelField("Futures", FOCUSABLE) {

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
						graphics.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
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
					actionListner.actionPerfomed(ActionCommand.CMD_FUTURE_SCREEN, threadedComponents);
					return super.navigationClick(status, time);
				}
			};
			if(AppConstants.screenHeight>=480)
				lblBSETab.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
			else
				lblBSETab.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));

			lblNSETab = new LabelField("Options", FOCUSABLE) {

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getFont().getAdvance(getText())+2, getFont().getHeight());
				}

				protected void paint(Graphics graphics) {
					if(isFocus()) {
						graphics.setColor(Color.ORANGE);
						//graphics.drawRect(0, 0, getWidth(), getHeight());
						graphics.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
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
					actionListner.actionPerfomed(ActionCommand.CMD_OPTION_SCREEN, threadedComponents);
					return super.navigationClick(status, time);
				}
			};
			if(AppConstants.screenHeight>=480)
				lblNSETab.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
			else
				lblNSETab.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));

			add(lblBSETab);
			add(lblNSETab);

		}

		protected void sublayout(int width, int height) {
			layoutChild(getField(0), width, height);
			layoutChild(getField(1), width, height);
			setPositionChild(getField(0), AppConstants.screenWidth/4-getField(0).getPreferredWidth()/2, padding/2);
			setPositionChild(getField(1), AppConstants.screenWidth/2 + AppConstants.screenWidth/4-getField(1).getPreferredWidth()/2, padding/2);
			setExtent(AppConstants.screenWidth, getField(0).getPreferredHeight()+padding*2);
		}

		protected void drawFocus(Graphics graphics, boolean on) {

		}

		protected void paintBackground(Graphics graphics) {

			graphics.setColor(Color.GRAY);
			graphics.fillRect(0, 0, getWidth(), getHeight());
			graphics.setColor(Color.BLACK);
			if(selectedIndex==0 && !AppConstants.OPTIONS_FLAG)
				graphics.fillRect(AppConstants.screenWidth/2, 0, AppConstants.screenWidth/2, getField(0).getPreferredHeight()+padding);
			else
				graphics.fillRect(0, 0, AppConstants.screenWidth/2, getField(0).getPreferredHeight()+padding);
		}
	}

	public Vector getComponentData() {
		return null;
	}

	protected void paintBackground(Graphics graphics) {
		graphics.fillRect(0,0,getWidth(),getHeight());
		graphics.setColor(Color.BLACK);
	}
	protected boolean onSavePrompt() {
        return true;
    }

	public void fieldChanged(Field field, int context) {
        try {
        	LOG.print("Field Changed method");
        	BasicEditField targetField = (BasicEditField) field;
                if(targetField.getText().indexOf(".")!=-1) {
                	LOG.print("inner Field Changed method");
                        int lastIndex = targetField.getText().indexOf(".", targetField.getText().indexOf(".")+1);
                        if(lastIndex==-1) {
                                int dotIndex = targetField.getText().indexOf(".");
                                if((dotIndex+2)<(targetField.getText().length()-1)) {
                                        targetField.setText(targetField.getText().substring(0, dotIndex+3));
                                }
                                if(dotIndex==0) {
                                        targetField.setText("0"+targetField.getText());
                                }
                        } else {
                                targetField.setText("");
                        }
                }
        } catch(Exception ex) {
                LOG.print("Exception occured");
        }
}

}

