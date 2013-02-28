package com.snapwork.view;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.HomeJson;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.RefreshButton;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TitleBarRefresh;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.view.trade.SlideView;

public class MarketDepthScreen extends MainScreen implements ActionListener,ReturnDataWithId, AutoRefreshableScreen{

	VerticalFieldManager verticalFieldManager = null;
	private BottomMenu bottomMenu = null;
	private RefreshButton refreshme;
	private String url;
	private ReturnDataWithId rparse = this;
	private int REFRESH_ID = 78;
	public static Vector marketDepthdata_;
	private VerticalFieldManager mainManager;
	private HomeJson homeJson;
	public MarketDepthScreen(Vector marketDepthdata) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		LOG.print("Market Depth Screen "+marketDepthdata.size());
		//if(marketDepthdata.size()==5)
			marketDepthdata_ = marketDepthdata;
		//else
		//	marketDepthdata = marketDepthdata_;
		
		
		url = (String)marketDepthdata.elementAt(0);
		//marketDepthdata.removeElementAt(0);
	
			
		LOG.print("Market Depth Screen - Title");
		//setTitle(new TitleBar("Market Depth"));
		refreshme = new RefreshButton()
		{
			protected boolean navigationClick(int arg0, int arg1) {
				if(!getLoading())
				{
					setLoading(true);
					new HomeJsonParser(url, rparse, REFRESH_ID);
				}
				return super.navigationClick(arg0, arg1);
				}
		};
		//Sets the title
		setTitle(new TitleBarRefresh("Market Depth", refreshme));
		mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR ) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight()<AppConstants.screenHeight?AppConstants.screenHeight:getHeight());
			}
			protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(maxWidth,maxHeight);
				setExtent(maxWidth, AppConstants.screenHeight- TitleBar.getItemHeight());
			}
		};
		LOG.print("Market Depth Screen - mainManager");

		verticalFieldManager = new VerticalFieldManager(FOCUSABLE)
		{
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		mainManager.add(verticalFieldManager);
		add(mainManager);
		 //bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_TRADE_VIEW_SCREEN, AppConstants.bottomMenuCommands);
	       
		createUI(marketDepthdata);
	}

	public void createUI(Vector marketDepthdata) {
		
			LOG.print("Market Depth Screen - verticleFieldManager");
			LOG.print("Market Depth Vector Size : "+marketDepthdata.size());
			String str1[],str2[],str3[],str4[];
			str1 = (String[])marketDepthdata.elementAt(1);
			str2 = (String[])marketDepthdata.elementAt(2);
			str3 = (String[])marketDepthdata.elementAt(3);
			str4 = (String[])marketDepthdata.elementAt(4);
			int len = 0;
			String str="";
			for(int i=0;i<str1.length;i++)
			{
				if(len < str1[i].length())
				{
					len = str1[i].length();
					str = str1[i];
				}
				if(len < str4[i].length())
				{
					len = str4[i].length();
					str = str4[i];
				}
			}
			int maxWth  = FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getAdvance(str)+6;
			if(maxWth < FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getAdvance("BestBuyQty")+6)
				maxWth = FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getAdvance("BestBuyQty")+6;
			//verticalFieldManager.add(getLabelField(NON_FOCUSABLE, "Order","Qty","Price",FontLoader.getFont(AppConstants.BIG_BOLD_FONT),0x769698,0xffffff,maxWth,true)); //0x769698 light //0x638284 dark
			verticalFieldManager.add(getHeaderLabelField(FOCUSABLE, "BestBuy Qty", "BestBuy Price", FontLoader.getFont(AppConstants.BIG_BOLD_FONT)));
			
			boolean fl = false;
			for(int x=0;x<5;x++)
			{
				fl =!fl;
				verticalFieldManager.add(getLabelField(NON_FOCUSABLE, str1[x],str2[x],FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),fl?0x4a4942:0x73756b,0xffffff,maxWth,false));
			}
			verticalFieldManager.add(getHeaderLabelField(FOCUSABLE, "BestSell Qty", "BestSell Price", FontLoader.getFont(AppConstants.BIG_BOLD_FONT)));
			fl = false;
			for(int x=0;x<5;x++)
			{
				fl = !fl;
				verticalFieldManager.add(getLabelField(NON_FOCUSABLE, str3[x],str4[x],FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),fl?0x4a4942:0x73756b,0xffffff,maxWth,false));
			}

			refreshme.setLoading(false);
			}

	public void actionPerfomed(byte Command, Object sender) {
		ActionInvoker.processCommand(new Action(Command,null));
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
            	return false;

            return false;
	}

	public boolean keyUp( int keyCode, int time )
	{
		return super.keyUp(keyCode, time);
	}

	public Vector getComponentData() {
		return null;
	}
	
	public LabelField getLabelField(long style, final String strTitle, final String text2,final Font fntFont,final int bgColor,final int fgColor,final int maxWth, final boolean flag)
	{
		return new LabelField(strTitle, style) 
		{
			protected void layout(int width, int height) 
			{
				super.layout(width, height);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}

			public int getPreferredHeight() 
			{
				return (fntFont.getHeight()+10);
			}

			public int getPreferredWidth() 
			{
				return AppConstants.screenWidth;
			}

			protected void paint(Graphics graphics) 
			{
				graphics.setColor(0x000000);
				graphics.fillRect(0, 0, getWidth(), getHeight());
				graphics.setColor(bgColor);
				graphics.fillRect(1, 2, getWidth()-2, getHeight()-4);
				graphics.setColor(fgColor);
				graphics.setFont(fntFont);
				String t1 = strTitle;
				if(strTitle.indexOf('.')>-1)
				{
					if(strTitle.indexOf('.')==strTitle.length()-2)
					{
						t1 = strTitle + "0";
					}
				}
				graphics.drawText(t1, 20, 5);
				String t3 = text2;
				if(!flag)
				if(text2.indexOf('.')>-1)
				{
					if(text2.indexOf('.')==text2.length()-2)
					{
						t3 = text2 + "0";
					}
				}
				else 
				{
					if(!t3.equalsIgnoreCase("null"))
						t3 = t3 + ".00";
				}
				graphics.drawText(t3, AppConstants.screenWidth - fntFont.getAdvance(t3) - 6 , 5);
			}

			protected void drawFocus(Graphics graphics, boolean on) 
			{

			}
		};
	}
	
	
	
	public LabelField getHeaderLabelField(long style, final String strTitle, final String strTitle2, final Font fntFont)
	{
		return new LabelField(strTitle, style) 
		{
			protected void layout(int width, int height) 
			{
				super.layout(getPreferredWidth(), height);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
			public void setFont(Font arg0) {
				super.setFont(fntFont);
			}
			public int getPreferredHeight() 
			{
				return (fntFont.getHeight()+6);
			}

			public int getPreferredWidth() 
			{
				return AppConstants.screenWidth;
			}
			
			protected void paint(Graphics graphics) 
			{
				if(!isFocus())
				{
					//graphics.setColor(0x1059c6); //0x5f7b90
					graphics.setColor(0x639084);
					graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
				}
				graphics.setColor(0xeeeeee);
				graphics.drawText(strTitle, 20, 3);
				graphics.drawText(strTitle2, getPreferredWidth() - getFont().getAdvance(strTitle2) - 6, 3);
			}

		};
	}

	public void setData(Vector vector, int id) {
		
		if( id == REFRESH_ID)
		{
			homeJson = (HomeJson)vector.elementAt(0);
			UiApplication.getUiApplication().invokeLater(new Runnable()
			{
			public void run()
			{
				Vector marketDepthdata = new Vector();
				marketDepthdata.addElement(homeJson.getBuyQty());
				marketDepthdata.addElement(homeJson.getBuyQty());
				marketDepthdata.addElement(homeJson.getBuyPrice());
				marketDepthdata.addElement(homeJson.getSellQty());
				marketDepthdata.addElement(homeJson.getSellPrice());
				verticalFieldManager.deleteAll();
				createUI(marketDepthdata);
			} 
			});
		}
		
	}

	public void refreshFields() {
		if(homeJson!=null)
		{
if(homeJson.getMarketStatus().equalsIgnoreCase("CLOSED"))
{
	return;
}
}
		if(!refreshme.getLoading())
		{
			refreshme.setLoading(true);
			new HomeJsonParser(url, rparse, REFRESH_ID);
		}
	}
}
