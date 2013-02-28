package com.snapwork.view;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.io.http.HttpDateParser;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.News;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.NewsField;
import com.snapwork.components.Pager;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;

public class NewsScreen extends MainScreen implements ActionListener,ThreadedComponents {

	Vector newsDataVector = null;
    private BottomMenu bottomMenu = null;
    private VerticalFieldManager newsFieldManager;
    private Pager newsPager = null;
    public final static byte NEWS_SECTION = 0;
    private ActionListener actionListener = this;
    
	public NewsScreen(String title,Vector newsDataVector,int pageNo) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		createUI(title,newsDataVector,pageNo);
	}
	
	public void createUI(String title,Vector newsDataVector,int pageNo) {
		//set the title bar
    	VerticalFieldManager titleBarFieldManager = new VerticalFieldManager(USE_ALL_WIDTH) {

			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}
    		
    	};
    	
        VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
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
    	
    	newsPager = new Pager(this,pageNo);
    	titleBarFieldManager.add(new TitleBar("Market News"));
    	titleBarFieldManager.add(newsPager);
        setTitle(titleBarFieldManager);

		newsFieldManager = new VerticalFieldManager(FOCUSABLE) {
			public int getPreferredHeight() {
				try {
					return (getFieldCount()-1)*getField(0).getPreferredHeight();
				} catch(Exception ex) {
				}
				return 0;
			}
		};
		fillNewsData(newsDataVector,false);	
		mainManager.add(newsFieldManager);
		add(mainManager);
        // Configure BottomMenu and set commands
        bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_TRADE_VIEW_SCREEN, AppConstants.bottomMenuCommands);
	}

	public void fillNewsData(final Vector newsDataVector,boolean uiThreadRequired) {
		this.newsDataVector = newsDataVector;
		if(uiThreadRequired) {
			//final ActionListener actionListner = this;
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					newsFieldManager.deleteAll();
					for(int i=0;i<newsDataVector.size();i++) {
						News news = (News)newsDataVector.elementAt(i);
						Debug.debug(news.getTitle() + " , "+news.getSource());
						NewsField newsField = new NewsField(news.getTitle(),StringToDate(news.getTime()),actionListener,(byte)i);
						
						newsFieldManager.add(newsField);
					}
					newsFieldManager.add(new NullField(FOCUSABLE));
				}
			});
		} else {
			for(int i=0;i<newsDataVector.size();i++) {
				News news = (News)newsDataVector.elementAt(i);
				NewsField newsField = new NewsField(news.getTitle(), StringToDate(news.getTime()),actionListener,(byte)i);
				newsFieldManager.add(newsField);
			}
			newsFieldManager.add(new NullField(FOCUSABLE));
		}	
	}

	public static String StringToDate(String dateToParse) {
		 Calendar rightnow =
	           Calendar.getInstance(TimeZone.getDefault());

	           int timezoneOffset = TimeZone.getDefault().getOffset(1, rightnow.get(Calendar.YEAR), rightnow.get(Calendar.MONTH), rightnow.get(Calendar.DAY_OF_MONTH), rightnow.get(Calendar.DAY_OF_WEEK), rightnow.get(Calendar.MILLISECOND));
        Date formatter = new Date(HttpDateParser.parse(dateToParse));
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd-MMM HH:mm");
        //int offset = TimeZone.getDefault().getRawOffset();
        //formatter.setTime(formatter.getTime() + offset);
        formatter.setTime((formatter.getTime()-timezoneOffset));
        LOG.print("timezoneOffset : "+timezoneOffset);
        String strCustomDateTime = dateFormat.format(formatter);
        return strCustomDateTime;
	}

	public void actionPerfomed(byte Command, Object sender) {
        switch(Command) {
    		case ActionCommand.CMD_PREV_PAGE:
        	case ActionCommand.CMD_NEXT_PAGE:
        		Vector newsPageCommand = new Vector();
        		newsPageCommand.addElement(sender.toString());
        		newsPageCommand.addElement((ThreadedComponents)this);
        		ActionInvoker.processCommand(new Action(ActionCommand.CMD_NEWS_PAGER,newsPageCommand));
        		break;
        	case ActionCommand.CMD_NEWS_DETAIL:
        		Vector newsDetailCommand = new Vector();
        		newsDetailCommand.addElement(sender.toString());
        		newsDetailCommand.addElement(newsDataVector);
        		newsDetailCommand.addElement(new Integer(newsPager.pageNo));
        		ActionInvoker.processCommand(new Action(ActionCommand.CMD_NEWS_DETAIL,newsDetailCommand));        		
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

	public void componentsPrepared(byte componentID, Object component) {
		// TODO Auto-generated method stub
		
	}

	public void componentsDataPrepared(byte componentID, Object data) {
		switch(componentID) {
			case NEWS_SECTION:
				try {
					fillNewsData((Vector)data, true);
				}catch(Exception ex) {
					Debug.debug("Error Processing News Data : "+ex.toString());
				}
				break;
		}
		
	}

	public Vector getComponentData() {
		// TODO Auto-generated method stub
		return null;
	}
/*public boolean onClose() {
	if(Utils.ENTRY_NEWS_COMMENTARY_DEFAULT == Utils.NEWS)
	{
		Utils.ENTRY_NEWS_COMMENTARY_DEFAULT = Utils.DEAFULT;
		ActionInvoker.processCommand(new Action(ActionCommand.CMD_GRID_SCREEN,null));
	}
	return true;
}	*/
}
