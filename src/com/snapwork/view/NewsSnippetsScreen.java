package com.snapwork.view;

import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.blackberry.api.mail.Multipart;
import net.rim.blackberry.api.mail.Session;
import net.rim.blackberry.api.mail.TextBodyPart;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.io.http.HttpDateParser;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.VirtualKeyboard;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.News;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomLabelField;
import com.snapwork.components.CustomLinkButtonUnderLine;
import com.snapwork.components.CustomMultilineTextField;
import com.snapwork.components.HomeScreenBanner;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.Utils;

public class NewsSnippetsScreen extends MainScreen implements ActionListener,ThreadedComponents,ReturnString, AutoRefreshableScreen {


	private BottomMenu bottomMenu = null;
	private byte selectedNewsIndex = -1;
	private Vector newsDataVector = null;
	public static int heightFirst = 0;
	private boolean bseBannerFlag;
	private int diff = 0;
	private boolean callProcess;
	private long timer;
	public NewsSnippetsScreen(Vector newsDataVector,byte selectedNewsIndex,int currentPageNo) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		if(getScreen().getVirtualKeyboard().isSupported())
		getScreen().getVirtualKeyboard().setVisibility(VirtualKeyboard.HIDE_FORCE);
		this.currentPageNo = currentPageNo;
		timer = System.currentTimeMillis();
		this.selectedNewsIndex = selectedNewsIndex;
		this.newsDataVector = newsDataVector;
		//if((Utils.BSEBANNER_TIME+30000)>System.currentTimeMillis())
		//	bseBanner = Utils.BSEBANNER;
		diff = FontLoader.getFont(AppConstants.SMALL_BOLD_FONT).getHeight()*3;
		createUI(newsDataVector,selectedNewsIndex);
	}

	VerticalFieldManager verticalFieldManager = null;
	public HomeScreenBanner bseBanner = null;
	LabelField shareNews = null;
	LabelField lastUpdatedDateTime = null;
	private int currentPageNo = -1;
	private int shareNewsText;

	public void createUI(final Vector newsDataVector,final byte selectedNewsIndex) {
		//Sets the title
		setTitle(new TitleBar("Market News"));
		VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}            
			protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(AppConstants.screenWidth, AppConstants.screenHeight);
				setExtent(AppConstants.screenWidth, AppConstants.screenHeight-TitleBar.getItemHeight());
			}
		};
		//Create Vertical Field Manager
		verticalFieldManager = new VerticalFieldManager(USE_ALL_WIDTH | FOCUSABLE) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, AppConstants.screenWidth, AppConstants.screenHeight);
			}
		};
		//shareNews  = getLinkLabel("Share news");
		//shareNews.setText("Share news");
		shareNews = new CustomLinkButtonUnderLine("Share news", FOCUSABLE | FIELD_LEFT | FIELD_VCENTER, 0xeeeeee, FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT), 10) {

			protected boolean navigationClick(int arg0, int arg1) {
				if((timer+100)<System.currentTimeMillis()){
					timer = System.currentTimeMillis();
					if(AppConstants.SEND_EMAIL)
					{
					try {
					News newsItem = ((News)newsDataVector.elementAt(shareNewsText));
					
					/*String email = Session.getDefaultInstance().getServiceConfiguration().getEmailAddress();
					if(email == null)
					{
						Dialog.alert("Please configure your Email settings!");
						return super.navigationClick(arg0, arg1);
					}
					else if(email.indexOf("@")<0)
					{
						Dialog.alert("Please configure your Email settings!");
						return super.navigationClick(arg0, arg1);
					}*/
					String defaultEmailAddress = Session.waitForDefaultSession().getServiceConfiguration().getEmailAddress() ;
					//MessageArguments msgArgs = new MessageArguments(MessageArguments.ARG_NEW,defaultEmailAddress,"subject",getContentData(newsItem.getContentURL())+"\nvia ShareMobile app visit http://www.sharekhan.com for more news and information.");
					MessageArguments msgArgs = new MessageArguments(MessageArguments.ARG_NEW,defaultEmailAddress,newsItem.getTitle(),getContentData(newsItem.getContentURL())+"\nvia ShareMobile app visit http://www.sharekhan.com for more news and information.");
					Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, msgArgs);
				} catch (Exception e) {
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Dialog.alert("Please configure your Email settings!");
						}});
				}
					}
				else
				{
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Dialog.alert("Please configure your Email settings!");
						}});
				}
			}
				return super.navigationClick(arg0, arg1);
			}
			
			
			protected boolean touchEvent(TouchEvent message) 
			{
				if(message.getEvent() == TouchEvent.CLICK) {
					if((timer+100)<System.currentTimeMillis()){
					timer = System.currentTimeMillis();
					if(AppConstants.SEND_EMAIL)
					{
					try {
						News newsItem = ((News)newsDataVector.elementAt(shareNewsText));
						
						String email = Session.getDefaultInstance().getServiceConfiguration().getEmailAddress();
						if(email == null)
						{
							Dialog.alert("Please configure your Email settings!");
							return super.touchEvent(message);
						}
						else if(email.indexOf("@")<0)
						{
							Dialog.alert("Please configure your Email settings!");
							return super.touchEvent(message);
						}
						String defaultEmailAddress = Session.waitForDefaultSession().getServiceConfiguration().getEmailAddress() ;
						//MessageArguments msgArgs = new MessageArguments(MessageArguments.ARG_NEW,defaultEmailAddress,"subject",getContentData(newsItem.getContentURL())+"\nvia ShareMobile app visit http://www.sharekhan.com for more news and information.");
						MessageArguments msgArgs = new MessageArguments(MessageArguments.ARG_NEW,defaultEmailAddress,newsItem.getTitle(),getContentData(newsItem.getContentURL())+"\nvia ShareMobile app visit http://www.sharekhan.com for more news and information.");
						Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, msgArgs);
					} catch (Exception e) {
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								Dialog.alert("Please configure your Email settings!");
							}});
					}
					}
					else
					{
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								Dialog.alert("Please configure your Email settings!");
							}});
					}
				}
				}
				return super.touchEvent(message);
			}
		};
		lastUpdatedDateTime = new LabelField("", NON_FOCUSABLE | FIELD_RIGHT){
			protected void paint(Graphics graphics) {
				graphics.setColor(0xeeeeee);
				super.paint(graphics);
			}
			
		};
		bseBanner = new HomeScreenBanner(FIELD_HCENTER, "BSE",1);
		if(Utils.bseJsonStore!=null)
		{
			LOG.print("Utils.bseJsonStore NOT NULL");
			HomeJson homeJson = Utils.bseJsonStore;
			lastUpdatedDateTime.setText("Last Updated : "+homeJson.getLastTradedTime());
			bseBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), homeJson.getPercentageDiff(),homeJson.getA(),homeJson.getD(),homeJson.getS());
		}//bseBanner = Utils.BSEBANNER;
		
		lastUpdatedDateTime.setFont(FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT));
		//shareNews.setFont(FontLoader.getFont(AppConstants.SMALL_BOLD_FONT));

		verticalFieldManager.add(shareNews);
		verticalFieldManager.add(lastUpdatedDateTime);
		verticalFieldManager.add(bseBanner);

		if(selectedNewsIndex==0)
			addSnippet(newsDataVector,selectedNewsIndex,(byte)0,true);
		else if(selectedNewsIndex==newsDataVector.size()-1)
			addSnippet(newsDataVector,selectedNewsIndex,(byte)2,false);
		else
			addSnippet(newsDataVector,selectedNewsIndex,(byte)1,false);

		mainManager.add(verticalFieldManager);
		add(mainManager);
		// Configure BottomMenu and set commands
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_TRADE_VIEW_SCREEN, AppConstants.bottomMenuCommands);

	}

	private void addSnippet(Vector newsDataVector,byte selectedNewsIndex,byte pageStatus,boolean isNextClicked) {
		News newsItem = ((News)newsDataVector.elementAt(selectedNewsIndex));
		Snippet snippet = new Snippet(newsItem.getTitle(),NewsScreen.StringToDate(newsItem.getTime()), getContentData(newsItem.getContentURL()),newsItem.getLandingUrl(),pageStatus,this,isNextClicked,selectedNewsIndex);
		verticalFieldManager.add(snippet);
		//snippet.setArrowFocus(isNextClicked);
	} 
	/*public String StringToDate(String dateToParse) {

        Date formatter = new Date(HttpDateParser.parse(dateToParse));
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd-MMM HH:mm");
        int offset = TimeZone.getDefault().getRawOffset();
        formatter.setTime(formatter.getTime() + offset);
        String strCustomDateTime = dateFormat.format(formatter);
        return strCustomDateTime;
	}*/
	public void gotoNextNews() {
		final NewsSnippetsScreen newsSnippetsScreen = this;
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				LOG.print(" gotoNextNews() method delete");
				verticalFieldManager.delete(verticalFieldManager.getField(verticalFieldManager.getFieldCount()-1));
				
			}
		});
		
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				LOG.print(" gotoNextNews() method");
				selectedNewsIndex++;
				//verticalFieldManager.delete(verticalFieldManager.getField(verticalFieldManager.getFieldCount()-1));
				if(selectedNewsIndex==newsDataVector.size()-1) {
					addSnippet(newsDataVector,selectedNewsIndex,(byte)2,true);
				} else {
					//selectedNewsIndex++;
					//addSnippet(newsDataVector,selectedNewsIndex,(byte)1,true);
					News newsItem = ((News)newsDataVector.elementAt(selectedNewsIndex));
					Snippet snippet = new Snippet(newsItem.getTitle(), NewsScreen.StringToDate(newsItem.getTime()), getContentData(newsItem.getContentURL()),newsItem.getLandingUrl(),(byte)1,newsSnippetsScreen,true,selectedNewsIndex);
					verticalFieldManager.add(snippet);
				}
			}
		});
			
	}

	public void gotoPrevNews() {
		LOG.print("selectedNewsIndex x-x-x-x-xx-x-x-xx-x-x-x-x-x"+selectedNewsIndex);
		
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				verticalFieldManager.delete(verticalFieldManager.getField(verticalFieldManager.getFieldCount()-1));
				/*LOG.print("selectedNewsIndex x-x-x-x-xx-x-x-xx-x-x-x-x-x"+selectedNewsIndex);
				if(selectedNewsIndex==0) {
					addSnippet(newsDataVector,selectedNewsIndex,(byte)0,false);
				} else {
					selectedNewsIndex--;
					addSnippet(newsDataVector,selectedNewsIndex,(byte)1,false);
				}
				*/
				selectedNewsIndex--;
				
				if(selectedNewsIndex==0)
					addSnippet(newsDataVector,selectedNewsIndex,(byte)0,true);
				else if(selectedNewsIndex==newsDataVector.size()-1)
					addSnippet(newsDataVector,selectedNewsIndex,(byte)2,false);
				else
					{
					addSnippet(newsDataVector,selectedNewsIndex,(byte)1,false);
					
					}
			}
		});		
	}

	public void componentsPrepared(byte componentID, Object component) {

	}

	public void componentsDataPrepared(byte componentID,final Object data) {
		switch(componentID) {
		case HomeScreen.BANNERS_DATA:
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Vector vector = (Vector)data;
					for(byte i=0;i<vector.size();i++) {
						HomeJson homeJson = (HomeJson)vector.elementAt(i);
						if(homeJson.getCompanyCode().equals(HomeScreen.BSE_COMPANYCODE)) {
							lastUpdatedDateTime.setText("Last Updated : "+homeJson.getLastTradedTime());
							bseBanner.setStartFlag(false);
							bseBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), homeJson.getPercentageDiff(),homeJson.getA(),homeJson.getD(),homeJson.getS());
							bseBanner.startUpdate();
							bseBannerFlag = true;
						}
					}
				}
			});
			break;
		}
	}

	public void actionPerfomed(byte Command, Object sender) {
		switch(Command) {
		//case ActionCommand.CMD_BANNERS_HOME_SCREEN:
			//ActionInvoker.processCommand(new Action(Command,(ThreadedComponents)this));		        	
		//	break;
		default:
			ActionInvoker.processCommand(new Action(Command,sender));
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
						if(getScreen().getVirtualKeyboard().isSupported())
						getScreen().getVirtualKeyboard().setVisibility(VirtualKeyboard.HIDE);
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

	private class Snippet extends Manager {


	private byte padding = 4;
	CustomLabelField newsTitle = null,newsSource = null;
	CustomMultilineTextField newsDetails = null;
	BitmapField leftArrow = null,rightArrow = null;
	private short arrowWidth = 0,arrowHeight = 0;
	private byte pageStatus = -1;//0 -> First Page,1 -> Not first and last page,2 -> Last Page

	public Snippet(String newsTitle,String sourceText,String detailsText,final String contentURL,byte pageStatus,final NewsSnippetsScreen newsSnippetsScreen,boolean isNextClicked,int index) {
		super(FOCUSABLE);

		this.pageStatus = pageStatus;
		shareNewsText = index;
		//this.newsTitle = new CustomLabelField(newsTitle, 0, 0xeeeeee, FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
		this.newsTitle = new CustomLabelField(newsTitle, 0, 0xeeeeee, FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT))
		{
			protected void layout(int width, int height)
			{
				super.layout(width, height);
				setExtent(width, height);
			}
		};
		heightFirst = newsTitle.length()<37?heightFirst = FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight() : FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight()*((newsTitle.length()/37)+((newsTitle.length()%36==0)?0:1));

		this.newsSource = new CustomLabelField(sourceText, 0, 0xeeeeee, FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
 int i = 5;
 int hgt = 1;
 if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
		i = 9;
 else if(AppConstants.screenHeight == 480)
 { i = 10; hgt = 2;}
		this.newsDetails = new CustomMultilineTextField(AppConstants.screenWidth - padding*i, ((hgt==2?20:1)+(FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_FONT).getHeight()*9 - diff))*hgt, FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_FONT), 0xeeeeee);
		this.newsDetails.setText(detailsText);

		arrowWidth = (short)ImageManager.getRightArrowImage().getWidth();
		arrowHeight = (short)ImageManager.getRightArrowImage().getHeight();

		this.leftArrow = new BitmapField(null,FOCUSABLE) {

			protected boolean navigationClick(int status, int time) {
				if((timer+100)<System.currentTimeMillis()){
					timer = System.currentTimeMillis();
					newsSnippetsScreen.gotoPrevNews();
			}
				return super.navigationClick(status, time);
			}

			protected boolean touchEvent(TouchEvent message) 
			{
				if(message.getEvent() == TouchEvent.CLICK) {
					if((timer+100)<System.currentTimeMillis()){
					timer = System.currentTimeMillis();
					newsSnippetsScreen.gotoPrevNews();
				}
				}
				return super.touchEvent(message);
			}
			
			protected void drawFocus(Graphics graphics, boolean on) {
				//Dont do anything and dont let the System
			}

			protected void onFocus(int direction) {
				super.onFocus(direction);
				invalidate();
			}

			protected void onUnfocus() {
				super.onUnfocus();
				invalidate();
			}

			protected void paint(Graphics graphics) {
				if(isFocus()) {
					graphics.setColor(Color.ORANGE);
					//graphics.fillRect(0, 0, arrowWidth+2, arrowHeight+2);
					graphics.drawLine(0,  arrowHeight+1,arrowWidth+2, arrowHeight+1);
				}
				graphics.drawBitmap(1, 1, arrowWidth, arrowHeight, ImageManager.getLeftArrowImage(), 0, 0);
			}

			protected void layout(int width, int height) {
				setExtent(arrowWidth+2, arrowHeight+2);
			}



		};

		this.rightArrow = new BitmapField(ImageManager.getRightArrowImage(),FOCUSABLE) {

			protected boolean navigationClick(int status, int time) {
				if((timer+100)<System.currentTimeMillis()){
					timer = System.currentTimeMillis();
					newsSnippetsScreen.gotoNextNews();
					}
				return super.navigationClick(status, time);
			}
			
			protected boolean touchEvent(TouchEvent message) 
			{
				if(message.getEvent() == TouchEvent.CLICK) {
					if((timer+100)<System.currentTimeMillis()){
					timer = System.currentTimeMillis();
					newsSnippetsScreen.gotoNextNews();
				}
				}
				return super.touchEvent(message);
			}
			protected void drawFocus(Graphics graphics, boolean on) {
				//Dont do anything and dont let the System
			}

			protected void onFocus(int direction) {
				super.onFocus(direction);
				invalidate();
			}

			protected void onUnfocus() {
				super.onUnfocus();
				invalidate();
			}

			protected void paint(Graphics graphics) {
				if(isFocus()) {
					graphics.setColor(Color.ORANGE);
					//graphics.fillRect(0, 0, arrowWidth+2, arrowHeight+2);
					graphics.drawLine(0,  arrowHeight+1,arrowWidth+2, arrowHeight+1);
				}
				graphics.drawBitmap(1, 1, arrowWidth, arrowHeight, ImageManager.getRightArrowImage(), 0, 0);
			}

			protected void layout(int width, int height) {
				setExtent(arrowWidth+2, arrowHeight+2);
			}

		};

		if(pageStatus!=0)
			add(this.leftArrow);
		add(this.newsTitle);
		add(this.newsSource);

		if(pageStatus!=2)			
			add(this.rightArrow);

		add(this.newsDetails);
		/*add(new CustomLinkButton("Continue Reading", FOCUSABLE, 0xeeeeee, FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT)) {

			protected boolean navigationClick(int status, int time) {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Browser.getDefaultSession().displayPage(contentURL);
					}
				});
				return super.navigationClick(status, time);
			}

		});*/

		if(pageStatus==2) {
			HorizontalFieldManager h = new HorizontalFieldManager()
			{
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}
			/*protected void sublayout(int width, int height) {
				setPositionChild(getField(0), (AppConstants.screenWidth/2)-(getField(0).getWidth()/2), 0);
				super.sublayout(width, height);
			}*/
		};
		h.add(new ButtonField("More", FOCUSABLE | DrawStyle.HCENTER) {
				public Font getFont() {
						return FontLoader.getFont(AppConstants.CHART_SMALL_FONT);
					}
			/*public int getPreferredWidth() {
				return FontLoader.getFont(AppConstants.CHART_SMALL_FONT).getAdvance("More")+10;
			}
			public int getPreferredHeight() {
				return FontLoader.getFont(AppConstants.CHART_SMALL_FONT).getHeight()+10;
			}*/
			protected boolean navigationClick(int status,int time) {
            	UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Action action = new Action(ActionCommand.CMD_TRADE_VIEW_SCREEN,new Integer(++newsSnippetsScreen.currentPageNo));
						ActionInvoker.processCommand(action);
						  }
				}); return super.navigationClick(status, time);
            }
			/*protected void layout(int arg0, int arg1) {
			super.layout(FontLoader.getFont(AppConstants.CHART_SMALL_FONT).getAdvance("More")+10, FontLoader.getFont(AppConstants.CHART_SMALL_FONT).getHeight()+10);
			}*/
    });
		add(h);
			/*add(new CustomLinkButton("More", FOCUSABLE, 0xeeeeee, FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT)) {

				protected boolean navigationClick(int status, int time) {
					Action action = new Action(ActionCommand.CMD_NEWS_SCREEN,new Integer(++newsSnippetsScreen.currentPageNo));
					ActionInvoker.processCommand(action);
					return super.navigationClick(status, time);
				}

			});
			*/
		}
		this.isNextClicked = isNextClicked;
	}


	private boolean isNextClicked = false;
	protected void onDisplay() {
		try {
			super.onDisplay();
			if(pageStatus!=0 && pageStatus!=2) {
				if(isNextClicked)
					rightArrow.setFocus();
				else
					leftArrow.setFocus();
			}
		} catch(Exception ex) {
			Debug.debug("Error : "+ex.toString());
		}
	}

	protected void sublayout(int width, int height) {
	
		if(pageStatus==0) { //First Page
			Debug.debug("GetField Count : "+this.getFieldCount());
			layoutChild(getField(0), AppConstants.screenWidth - padding*4 - arrowWidth*2 , heightFirst);
			layoutChild(getField(1), AppConstants.screenWidth - padding*4 - arrowWidth*2 , getField(1).getFont().getHeight());
			layoutChild(getField(2), width, height);
			if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
				layoutChild(getField(3), AppConstants.screenWidth - padding*4, (getField(3).getFont().getHeight()*7)+(getField(0).getFont().getHeight()*3) - diff);
			else if(AppConstants.screenHeight==480)
				layoutChild(getField(3), AppConstants.screenWidth - padding*4, (getField(3).getFont().getHeight()*7 - diff)*4);
			else
				layoutChild(getField(3), AppConstants.screenWidth - padding*4, getField(3).getFont().getHeight()*7 - diff);
			//setPositionChild(getField(0), padding*2 + arrowWidth, padding);
			setPositionChild(getField(0), padding*2, padding);
			setPositionChild(getField(2), AppConstants.screenWidth - padding - arrowWidth, padding );
			//setPositionChild(getField(1), padding*2 + arrowWidth,padding  + heightFirst);
			setPositionChild(getField(1), padding*2,padding  + heightFirst);
			if(AppConstants.screenHeight==480)
				setPositionChild(getField(3),padding*2 ,30 + Utils.snippetDiff +padding + heightFirst +getField(1).getFont().getHeight());
			else
				setPositionChild(getField(3),padding*2 , Utils.snippetDiff +padding + heightFirst +getField(1).getFont().getHeight());
			} else if(pageStatus == 2) { //Last Page
			layoutChild(getField(0), width, height);
			layoutChild(getField(1), AppConstants.screenWidth - padding*4 - arrowWidth*2 , heightFirst);
			layoutChild(getField(2), AppConstants.screenWidth - padding*4 - arrowWidth*2 , getField(2).getFont().getHeight());
			if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
				layoutChild(getField(3), AppConstants.screenWidth - padding*4, getField(3).getFont().getHeight()*7+getField(1).getFont().getHeight()*3 - diff);
			else
				layoutChild(getField(3), AppConstants.screenWidth - padding*4, getField(3).getFont().getHeight()*7 - diff);
			
			if(AppConstants.screenHeight==480)
				layoutChild(getField(4),FontLoader.getFont(AppConstants.CHART_SMALL_FONT).getAdvance("More")+10, (FontLoader.getFont(AppConstants.CHART_SMALL_FONT).getHeight()+10 - diff)*2);
			else 
				layoutChild(getField(4),FontLoader.getFont(AppConstants.CHART_SMALL_FONT).getAdvance("More")+10, FontLoader.getFont(AppConstants.CHART_SMALL_FONT).getHeight()+10 - diff);
			
			setPositionChild(getField(0), padding, padding);
			setPositionChild(getField(1), padding*2 + arrowWidth, padding);
			setPositionChild(getField(2), padding*2 + arrowWidth, padding+heightFirst );
			if(AppConstants.screenHeight==480)
				setPositionChild(getField(3), padding*2, (Utils.snippetDiff +heightFirst+getField(2).getFont().getHeight())*2);
			else if((padding*2 + getField(1).getFont().getHeight()*3)>(heightFirst+getField(1).getFont().getHeight()))
				setPositionChild(getField(3), padding*2, padding*2 + Utils.snippetDiff +getField(1).getFont().getHeight()*3);
			else
				setPositionChild(getField(3), padding*2, Utils.snippetDiff +heightFirst+getField(2).getFont().getHeight());
			if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			{
				setPositionChild(getField(4), AppConstants.screenWidth - (getField(4).getPreferredWidth()) - padding*2, Utils.snippetDiff +padding*2 + getField(1).getFont().getHeight()*6 + getField(3).getFont().getHeight()*7+padding/2);
			}
			else if(AppConstants.screenHeight==480)
				setPositionChild(getField(4), AppConstants.screenWidth - (getField(4).getPreferredWidth()) - padding*2, 40+Utils.snippetDiff +padding*2 + getField(1).getFont().getHeight()*3 + getField(3).getFont().getHeight()*7+padding/2);
			else
			{
				setPositionChild(getField(4), AppConstants.screenWidth - (getField(4).getPreferredWidth()) - padding*2, Utils.snippetDiff +padding*2 + getField(1).getFont().getHeight()*3 + getField(3).getFont().getHeight()*7+padding/2);
			}
		} else {
			layoutChild(getField(0), width, height);
			layoutChild(getField(1), AppConstants.screenWidth - padding*4 - arrowWidth*2 , heightFirst);
			layoutChild(getField(2), AppConstants.screenWidth - padding*4 - arrowWidth*2 , getField(2).getFont().getHeight());
			layoutChild(getField(3), width, height);
			if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
				layoutChild(getField(4), AppConstants.screenWidth - padding*4, getField(4).getFont().getHeight()*10 - diff);
			else if(AppConstants.screenHeight==480)
				layoutChild(getField(4), AppConstants.screenWidth - padding*4, (getField(4).getFont().getHeight()*7 - diff)*2);
			else
				layoutChild(getField(4), AppConstants.screenWidth - padding*4, getField(4).getFont().getHeight()*7 - diff);
			
			setPositionChild(getField(0), padding, padding);
			setPositionChild(getField(1), padding*2 + arrowWidth, padding);
			setPositionChild(getField(2), padding*2 + arrowWidth, padding  + heightFirst);					
			setPositionChild(getField(3), AppConstants.screenWidth - padding - arrowWidth, padding);
			if(AppConstants.screenHeight==480)
				setPositionChild(getField(4), padding*2, 40+Utils.snippetDiff +heightFirst+getField(2).getFont().getHeight());
			else if((padding*2 + getField(1).getFont().getHeight()*3)>(heightFirst+getField(2).getFont().getHeight()))
				setPositionChild(getField(4), padding*2, padding*2 + Utils.snippetDiff +getField(1).getFont().getHeight()*3);
			else
				setPositionChild(getField(4), padding*2, Utils.snippetDiff +heightFirst+getField(2).getFont().getHeight());
			}
		if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			{
				setExtent(AppConstants.screenWidth,padding*2 + FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight()*3+FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_FONT).getHeight()*7+padding/2+FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight()*5+2);
			}
		else if(AppConstants.screenHeight==480)
		{
			setExtent(AppConstants.screenWidth,(padding*2 + FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight()*3+FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_FONT).getHeight()*7+padding/2+FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight()*2+2)*2);
		}
		else
			setExtent(AppConstants.screenWidth,padding*2 + FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight()*3+FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_FONT).getHeight()*7+padding/2+FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight()*2+2);
	}

	protected void paintBackground(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.drawRect(0, 0, getWidth(), getHeight());
	}




	}

	public Vector getComponentData() {
		return null;
	}

	/*public void refreshFields() {
		if(bseBannerFlag) {
			bseBannerFlag = false;
			Debug.debug("Refreshing Banners And Charts");
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					//bseBanner.reset();
					actionPerfomed(ActionCommand.CMD_BANNERS_HOME_SCREEN, null);
				}
			});
		}
	}*/
	
	public LabelField getLinkLabel(final String text)
	{
		return new LabelField("",FOCUSABLE)
		{
			public void setFont(Font arg0) {
				super.setFont(FontLoader.getFont(AppConstants.SMALL_BOLD_FONT));
			}
			 protected void onFocus(int direction)
		        {
		                super.onFocus(direction);
		                invalidate();
		        }
		        
		        protected void onUnfocus()
		        {
		                super.onUnfocus();
		                invalidate();
		        }
	    	protected void paint(Graphics graphics) {
				graphics.setColor(0xeeeeee);
				graphics.setFont(FontLoader.getFont(AppConstants.SMALL_BOLD_FONT));
				graphics.drawText(text, 10, 0);
				//super.paint(graphics);
			}
			protected void paintBackground(Graphics graphics)
			{
				if(isFocus())
					graphics.setColor(Color.ORANGE);
				else
					graphics.setColor(0x000000);
				graphics.fillRoundRect(9, 0, getWidth()-18, getHeight(),8,8);
				//super.paintBackground(graphics);
			};
			public int getPreferredHeight() {
				return 8+FontLoader.getFont(AppConstants.SMALL_BOLD_FONT).getHeight();
			}
			public int getPreferredWidth() {
				return getFont().getAdvance(text)+20;
			}
			protected void layout(int width, int height) {
				super.layout(width, getPreferredHeight());
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
			protected boolean navigationClick(int arg0, int arg1) {
				News newsItem = ((News)newsDataVector.elementAt(selectedNewsIndex));
				try {
					String defaultEmailAddress = Session.waitForDefaultSession() .getServiceConfiguration().getEmailAddress() ;
					MessageArguments msgArgs = new MessageArguments(MessageArguments.ARG_NEW,defaultEmailAddress,"subject",newsItem.getContentURL()+"\nvia ShareMobile app visit http://www.sharekhan.com for more news and information.");
					Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, msgArgs);
				} catch (Exception e) {
					LOG.print("Exception occured : "+e.toString());
				}
				return super.navigationClick(arg0, arg1);
			};
			  protected void drawFocus(Graphics graphics, boolean on) 
		        {
		                //Dont do anything and dont let the System
		        }
		};
		
	}
	
	public String getContentData(String content)
	{
		StringBuffer sbf = new StringBuffer();
		int flag = 0;
		for(int i=0;i<content.length();i++)
		{
			if(flag == 2 )
			{
				sbf.append("\n\n");
				flag = 0;
			}
			if(content.charAt(i)=='<')
			{
				i++;
				while(true)
				{
					if(content.charAt(i)!='>')
					{
						if(content.charAt(i)!='b' && flag!=2)
							flag++;
						i++;
					}
					else
						break;
				}
			}
			else if(content.charAt(i)=='&')
			{
				int jx = i;
				i++;
				while(true)
				{
					if(content.charAt(i)==';')
					{
						break;
					}
					else if(jx+9<i)
					{
						i = jx;
						sbf.append(content.charAt(i));
						break;
					}
					else 
						i++;
						
				}
			}
			else
				sbf.append(content.charAt(i));
			
		}
		return sbf.toString();
	}

	public void refreshFields() {
		if(!callProcess)
		{	callProcess = true;
		 new ReturnStringParser(Utils.getBannersDataProvideUrl(), 225, this);
		 }
	}

	public void setReturnString(String string, int id) {
		 if(!(UiApplication.getUiApplication().getActiveScreen() instanceof NewsSnippetsScreen))
			 return;
		if(id == 225){
			callProcess = false;
		Vector vx = HomeJsonParser.getVector(string);
		LOG.print(vx.size()+"-=-=-=-=--=-=-=-=---==");
		Utils.bseJsonStore = (HomeJson) vx.elementAt(0);
		final HomeJson homeJson = Utils.bseJsonStore;
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
		lastUpdatedDateTime.setText("Last Updated : "+homeJson.getLastTradedTime());
		bseBanner.setStartFlag(false);
		bseBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), homeJson.getPercentageDiff(),homeJson.getA(),homeJson.getD(),homeJson.getS());
		bseBanner.startUpdate();
		}
		});}
	}

}
