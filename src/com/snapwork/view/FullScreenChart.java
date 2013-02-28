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
import com.snapwork.areacharts.ChartComponent;
import com.snapwork.areacharts.ChartProperties;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.LoadingComponent;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.util.AppConstants;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;

public class FullScreenChart extends MainScreen implements ThreadedComponents,ActionListener {
	
    private BottomMenu bottomMenu = null;
	
	public FullScreenChart(String title) {
		createUI(title);
	}
	//Fields for the screen
    HorizontalFieldManager horizontalFieldManager = null;
    VerticalFieldManager verticalFieldManager = null;
    
    public void createUI(String title) {
          //Sets the title
          setTitle(new TitleBar(title));
          
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
      			
      			protected void sublayout(int width,int height) {
      				super.sublayout(width, height);
      				setExtent(AppConstants.screenWidth, AppConstants.screenHeight-BottomMenu.getItemHeight()-TitleBar.getItemHeight());
      			}
      			
          };

          horizontalFieldManager.add(new LoadingComponent(AppConstants.loadingMessage,AppConstants.screenWidth,ChartProperties.getFullScreenChartProperties().getChartHeight()+ChartProperties.getFullScreenChartProperties().getChartxAxisHeight()));

          verticalFieldManager.add(horizontalFieldManager);

          mainManager.add(verticalFieldManager);
          add(mainManager);
          // Configure BottomMenu and set commands
          bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_GRID_SCREEN, AppConstants.bottomMenuCommands);
    }

	public void componentsPrepared(byte componentID,final Object component) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					if(component instanceof Field) {
						horizontalFieldManager.deleteAll();
						horizontalFieldManager.add((Field)component);
					}
				}
			});
	}

	public void componentsDataPrepared(byte componentID, final Object data) {

	}
	
	public void actionPerfomed(byte Command, Object sender) {
        switch(Command) {
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
