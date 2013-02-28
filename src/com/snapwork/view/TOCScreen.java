package com.snapwork.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.VirtualKeyboard;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.areacharts.ChartComponent;
import com.snapwork.areacharts.ChartProperties;
import com.snapwork.beans.HomeJson;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomLinkButtonUnderLine;
import com.snapwork.components.CustomMultilineTextField;
import com.snapwork.components.WrapLabelField;
import com.snapwork.components.HomeScreenBanner;
import com.snapwork.components.LoadingComponent;
import com.snapwork.components.RefreshButton;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.util.AppConstants;
import com.snapwork.util.AutoScreenRefresherThread;
import com.snapwork.util.DBPackager;
import com.snapwork.util.DBmanager;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.Utils;
import com.snapwork.view.trade.TradeNowMainScreen;

public class TOCScreen extends MainScreen  {


public TOCScreen() {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		if(getScreen().getVirtualKeyboard().isSupported())
			getScreen().getVirtualKeyboard().setVisibility(VirtualKeyboard.HIDE_FORCE);
		createUI();
	}
	public void createUI() {
		setTitle(new TitleBar("Terms and Conditions"));

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
		final Bitmap bmp = ImageManager.resizeBitmap(ImageManager.getTradeNow(),FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance("ACCEPTOO"),FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()+8);
		CustomMultilineTextField details = new CustomMultilineTextField(AppConstants.screenWidth ,AppConstants.screenHeight-BottomMenu.getItemHeight()-TitleBar.getItemHeight(), FontLoader.getFont(AppConstants.TOC_FONT), 0xeeeeee);
		/*RichTextField details = new RichTextField(read(), Field.NON_FOCUSABLE)
		{
			protected void paint(Graphics graphics) {
			graphics.setColor(0xeeeeee);
			super.paint(graphics);
		}
	};*/
	//	WrapLabelField details = new WrapLabelField(read(), FOCUSABLE , FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT), 0xeeeeee);
		details.setText(read());
		mainManager.add(details);
		add(mainManager);
		VerticalFieldManager vfmBottomMenu = new VerticalFieldManager(Manager.USE_ALL_WIDTH) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		HorizontalFieldManager hfmBottomMenuBar = new HorizontalFieldManager(HorizontalFieldManager.HORIZONTAL_SCROLL | USE_ALL_WIDTH)
		{
			/*public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}*/
			
		protected void sublayout(int arg0, int arg1) {
			layoutChild(getField(0), getField(0).getPreferredWidth(),  getField(0).getPreferredHeight());
			layoutChild(getField(1), getField(1).getPreferredWidth(),  getField(1).getPreferredHeight());
			setPositionChild(getField(0), 5, 0);
			setPositionChild(getField(1), AppConstants.screenWidth- getField(1).getPreferredWidth()-5, 0);
			//super.sublayout(AppConstants.screenWidth,arg1);
			setExtent(AppConstants.screenWidth,getField(1).getPreferredHeight());
		}	
		};
			final String textButton = "Accept";
			BitmapField btnAccept = new BitmapField(bmp,  FOCUSABLE | DrawStyle.HCENTER)  {

				protected boolean navigationClick(int status,int time) {
					byte[] pack = DBPackager.convertTOByte(1);
					DBmanager.addData(AppConstants.appDBTOC, pack);
					 ActionInvoker.processCommand(new Action(ActionCommand.CMD_USER_REGISTRATION,null));
					return super.navigationClick(status, time);
				}
				private boolean isFocused = false;
				protected void onFocus(int direction) 
				{
					isFocused = true;
					invalidate();
				}
				protected void onUnfocus() {
					isFocused = false;
					invalidate();
				}
				protected void paintBackground(Graphics graphics) {
					graphics.setBackgroundColor(Color.BLACK);
					graphics.clear();
				}
				protected void paint(Graphics graphics) 
				{
					if(isFocused) {
						graphics.setColor(Color.ORANGE);
					} else {
						graphics.setColor(0xeeeeee);
					}
					//graphics.fillRect(0, 0, getWidth(), getHeight());
					graphics.drawBitmap(0,0, bmp.getWidth(), bmp.getHeight(), bmp, 0, 0);                                        
					graphics.drawText(textButton,(bmp.getWidth()/2)-(getFont().getAdvance(textButton)/2),(bmp.getHeight()/2)-(getFont().getHeight()/2));
					
				}
				protected void drawFocus(Graphics graphics, boolean on) 
				{

				}
				public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
				public int getPreferredHeight() {return  bmp.getHeight();}//(getFont().getHeight()*2)-(getFont().getHeight()/2);}
				public int getPreferredWidth() {return bmp.getWidth();}//getFont().getAdvance("Trade Now");}
				
				protected void layout(int arg0, int arg1) {
				setExtent(getPreferredWidth(), getPreferredHeight());
				}
				};
				
				final String textButtonModify = "Decline";
				BitmapField btnDecline = new BitmapField(bmp,  FOCUSABLE | DrawStyle.HCENTER)  {

					protected boolean navigationClick(int status,int time) {
						/*synchronized( UiApplication.getEventLock() ){

							if(isDisplayed()) 
							{
								close();

							}
						}*/
						System.exit(0);
					return super.navigationClick(status, time);
					}
					private boolean isFocused = false;
					protected void onFocus(int direction) 
					{
						isFocused = true;
						invalidate();
					}
					protected void onUnfocus() {
						isFocused = false;
						invalidate();
					}
					protected void paintBackground(Graphics graphics) {
						graphics.setBackgroundColor(Color.BLACK);
						graphics.clear();
					}
					protected void paint(Graphics graphics) 
					{
						if(isFocused) {
							graphics.setColor(Color.ORANGE);
						} else {
							graphics.setColor(0xeeeeee);
						}
						//graphics.fillRect(0, 0, getWidth(), getHeight());
						graphics.drawBitmap(0,0, bmp.getWidth(), bmp.getHeight(), bmp, 0, 0);                                        
						graphics.drawText(textButtonModify,(bmp.getWidth()/2)-(getFont().getAdvance(textButtonModify)/2),(bmp.getHeight()/2)-(getFont().getHeight()/2));
						
					}
					protected void drawFocus(Graphics graphics, boolean on) 
					{

					}
					public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
					public int getPreferredHeight() {return  bmp.getHeight();}//(getFont().getHeight()*2)-(getFont().getHeight()/2);}
					public int getPreferredWidth() {return bmp.getWidth();}//getFont().getAdvance("Trade Now");}
					
					protected void layout(int arg0, int arg1) {
					setExtent(getPreferredWidth(), getPreferredHeight());
					}
					};
					hfmBottomMenuBar.add(btnAccept);
					hfmBottomMenuBar.add(btnDecline);
		vfmBottomMenu.add(hfmBottomMenuBar);
		setStatus(vfmBottomMenu);
		btnAccept.setFocus();
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

	private String readTextFile(String fName) {
		  String result = null;
		  FileConnection fconn = null;
		  DataInputStream is = null;
		  try {
		   fconn = (FileConnection) Connector.open(fName, Connector.READ_WRITE);
		   is = fconn.openDataInputStream();
		   byte[] data = IOUtilities.streamToBytes(is);
		   result = new String(data);
		  } catch (IOException e) {
		   System.out.println(e.getMessage());
		  } finally {
		   try {
		    if (null != is)

		     is.close();
		    if (null != fconn)
		     fconn.close();
		   } catch (IOException e) {
		    System.out.println(e.getMessage());
		   }
		  }
		  return result==null?"":result;
		 }
	private void writeTextFile(String fName, String text) {
		  DataOutputStream os = null;
		  FileConnection fconn = null;
		  try {
		   fconn = (FileConnection) Connector.open(fName, Connector.READ_WRITE);
		   if (!fconn.exists())
		    fconn.create();

		   os = fconn.openDataOutputStream();
		   os.write(text.getBytes());
		  } catch (IOException e) {
		   System.out.println(e.getMessage());
		  } finally {
		   try {
		    if (null != os)
		     os.close();
		    if (null != fconn)
		     fconn.close();
		   } catch (IOException e) {
		    System.out.println(e.getMessage());
		   }
		  }
		 }
public String read()
{


    byte[] data = null;
	try {
		Class classs = Class.forName("com.snapwork.main.AppEntryPoint");

		InputStream is = classs.getResourceAsStream("/tos.txt");

		data = IOUtilities.streamToBytes(is);
	} catch (ClassNotFoundException e) {
		data = null;
	} catch (IOException e) {
		data = null;
	}

    String result = "no text";
    if(data!=null)
    result = new String(data);
    return result;
}
}