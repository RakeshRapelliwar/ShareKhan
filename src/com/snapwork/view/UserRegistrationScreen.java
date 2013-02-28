package com.snapwork.view;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.CDMAInfo;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.SIMCardException;
import net.rim.device.api.system.SIMCardInfo;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.text.TextFilter;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.components.BlockField;
import com.snapwork.components.CustomBasicEditField;
import com.snapwork.components.CustomBasicPasswordField;
import com.snapwork.components.CustomLabelField;
import com.snapwork.components.CustomLinkButtonUnderLine;
import com.snapwork.components.CustomObjectChoiceFieldReg;
import com.snapwork.components.FieldGroup;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.Snippets;
import com.snapwork.components.TitleBar;
import com.snapwork.parsers.Json;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.HttpConnectionFactory;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;
import net.rim.device.api.system.IDENInfo;

public class UserRegistrationScreen extends MainScreen implements ActionListener,RemovableScreen
{
	private Thread thread;
	public static Thread threadIndices;
	public static String id = "";
	public static String mempass = "";
	public static String tradepass = "";
	public static int errorScreen = 0;
	private String imei ;
	private String version ;
	public static String VERSION = "1.3";
	private String IMSIString = "";
	private String IMEIString = "";
	public UserRegistrationScreen() {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		byte[] IMSICode = null ;
		byte[] IMEICode = null ;
		try {
			switch (RadioInfo.getNetworkType()) {
			case RadioInfo.NETWORK_CDMA:
				IMSICode = CDMAInfo.getIMSI();
				IMEIString = CDMAInfo.getDecimalMEID();
				break;
			case RadioInfo.NETWORK_GPRS:
				IMSICode = SIMCardInfo.getIMSI();
				IMEICode = GPRSInfo.getIMEI();
				break;
			case RadioInfo.NETWORK_IDEN:
				IMSICode = SIMCardInfo.getIMSI();
				IMEICode = IDENInfo.getIMEI();
				break;
			default:
				break;
			}
			if(IMSICode!=null){
				StringBuffer IMSIStringb = new StringBuffer();
				int codeLength = IMSICode.length;
				for(int i=0; i<codeLength ;i++) {
					IMSIStringb.append(IMSICode[i]);
				}
				if(IMSIStringb!=null)
				{
					if(IMSIStringb.toString().equals("null"))
					{	
						IMSIString = IMSIStringb.toString();
						LOG.print("IMSIString : "+IMSIString);
					}
				}
				}
			if(IMEICode!=null && IMEIString.length()==0){
			StringBuffer txt = new StringBuffer();
			for(int z=0; z<IMEICode.length; z++) {
				txt.append(IMEICode[z]);
			}
			IMEIString = txt.toString();
			}
		} catch (Exception e) {
			IMSIString = "";
			IMEIString = "";
		}
		finally
		{
			if(IMEIString == null)
				IMEIString = "";
			else if(IMEIString.equalsIgnoreCase("null"))
				IMEIString = "";
			if(IMSIString == null)
				IMEIString = "";
			else if(IMSIString.equalsIgnoreCase("null"))
				IMSIString = "";
		}
		if(SearchEFScreen.niftyIndices==null)
			loadFNOIndices();
		if(AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			Utils.snippetDiff = 15;
		/*imei = IDENInfo.imeiToString(IDENInfo.getIMEI());
		if(imei == null)
			imei = "";*/
		version = ApplicationDescriptor.currentApplicationDescriptor().getVersion();
		if(version == null)
			version = "";
		//createUI(AppConstants.appTitle+" : Registration");
		createUI("Account Login");
	}

	protected void paintBackground(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	CustomBasicEditField objName;
	CustomObjectChoiceFieldReg userType; String[] choice = {"Trading","Guest"};
	CustomBasicPasswordField  objPassMembership,objPassTrading;
	String errorStatus = "";
	public void createUI(String strTitle) {
		//set Title
		TitleBar titleBar = new TitleBar(strTitle); 
		setTitle(titleBar);
		final int titleBarHeight = titleBar.getPreferredHeight();

		VerticalFieldManager vfm = new VerticalFieldManager(HORIZONTAL_SCROLL |HORIZONTAL_SCROLLBAR | VERTICAL_SCROLL | VERTICAL_SCROLLBAR );
		vfm.setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		FieldGroup fieldGroup = new FieldGroup("Registration");

		CustomLabelField lblUserTypeLabel = new CustomLabelField(" User Type", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));

		
		userType = new CustomObjectChoiceFieldReg("", choice,0, ObjectChoiceField.USE_ALL_WIDTH | FIELD_HCENTER);
		userType.setEditable(false);
		userType.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);
		//CustomObjectChoiceFieldReg userTyp = new CustomObjectChoiceFieldReg("Trading", choice,0, ObjectChoiceField.FIELD_RIGHT |ObjectChoiceField.USE_ALL_WIDTH | FIELD_HCENTER);
		//add(userTyp);
		//userType.setMinimalWidth(FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getAdvance("TradingPassw") + Snippets.padding*2);
		//Name Row
		CustomLabelField lblNameLabel = new CustomLabelField(" Username", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		objName = new CustomBasicEditField(Color.BLACK | BasicEditField.NO_COMPLEX_INPUT);
		objName.setFilter(TextFilter.get(TextFilter.DEFAULT));
		objName.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));


		//EmailID Row
		CustomLabelField lblMembershipPass = new CustomLabelField(" Password", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));

		//Password Row
		objPassMembership = new CustomBasicPasswordField("", "", 30, PasswordEditField.FILTER_DEFAULT);


		//Trading Password Row
		CustomLabelField lblTradePass = new CustomLabelField(" Trading Password", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		objPassTrading = new CustomBasicPasswordField("", "", 30, PasswordEditField.FILTER_DEFAULT);

		objName.setText("sankara_n");
		objPassMembership.setText("sski@123");
		objPassTrading.setText("sski@456");
		
		
		
if(errorScreen>0) 
{
	objName.setText(id);
	objPassMembership.setText(mempass);
	objPassTrading.setText(tradepass);
	UiApplication.getUiApplication().invokeLater(new Runnable() {
		public void run() {
	if(errorScreen == 1)
		objName.setFocus();
	else if(errorScreen == 2)
		objPassMembership.setFocus();
	else if(errorScreen == 3)
		objPassTrading.setFocus();
	errorScreen = 0;
		}});
}
		HorizontalFieldManager horizontalFieldManager = new HorizontalFieldManager(VERTICAL_SCROLL | VERTICAL_SCROLLBAR | HORIZONTAL_SCROLL | HORIZONTAL_SCROLLBAR);
		//Add Sign Up Button
		ButtonField objBtnSignUp = new ButtonField("Login",FIELD_HCENTER) {
		/*	public int getPreferredWidth() {
				return FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getAdvance("#SUBMIT#");
			}
			protected void layout(int arg0, int arg1) {
				super.layout(FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getAdvance("#SUBMIT#"), FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight());
			}*/
			protected boolean navigationClick(int status, int time) {
				boolean firstStep = true,secondStep = false,thirdStep = false;
				if(userType.getSelectedIndex()==0){
					if(firstStep) {
					secondStep = true;
					boolean flag = false;
					String text = "Please enter";
					if(objName.getText().trim().length()==0) {
						//ScreenInvoker.showDialog("Please enter Username");
						text = text + " Username,";
						secondStep = false;
						flag = true;
					} 
					if(objPassMembership.getText().trim().length()==0) {
						text = text + "Membership Password,";
						flag = true;
					} 
					if (objPassTrading.getText().trim().length()==0) {
						text = text + " Trading Password,";
						flag = true;
					}
					
					if(objName.getText().trim().length()<1)
					{
						//text = "Username length should be min 4 characters!";
						text = "Username length should be min 1 character!";
					}
					else if(objPassMembership.getText().trim().length()<6)
					{
						text = "Membership password length should be min 6 characters!";
					}
					else if(objPassTrading.getText().trim().length()<6)
					{
						text = "Trading password length should be min 6 characters!";
					}
					if(flag)
					{
						text = text.substring(0, text.length()-1)+"!";
						ScreenInvoker.showDialog(text);
						secondStep = false;
					}

					if(secondStep) {
						thirdStep = true;
						if(thirdStep) {
							id = objName.getText();
							mempass = objPassMembership.getText();
							tradepass = objPassTrading.getText();
							//Dialog.alert("SIM : "+IMSIString+"\n IMEI : "+IMEIString +"\n Device ID : "+DeviceInfo.getDeviceId());
							startRegistrationProcess(objName.getText().trim(),objPassMembership.getText(),objPassTrading.getText());
						}

					}
				}
				}
				else // For Guest Accounts
				{
					if(firstStep) {
						secondStep = true;
						boolean flag = false;
						String text = "Please enter";
						if(objName.getText().trim().length()==0) {
							//ScreenInvoker.showDialog("Please enter Username");
							text = text + " Username,";
							secondStep = false;
							flag = true;
						} 
						if(objPassMembership.getText().trim().length()==0) {
							text = text + " Password,";
							flag = true;
						} 
						/*if (objTradePassRepeat.getText().trim().length()==0) {
							text = text + " Trading Password,";
							flag = true;
						}*/
						if(flag)
						{
							text = text.substring(0, text.length()-1)+"!";
							ScreenInvoker.showDialog(text);
							secondStep = false;
						}

						if(secondStep) {
							thirdStep = true;
							if(thirdStep) {
								startRegistrationProcess(objName.getText().trim(),objPassMembership.getText(),objPassTrading.getText());
							}

						}
					}
				}
				
				return true;
			}
		};
		//objBtnSignUp.setLabel("Submit");
		HorizontalFieldManager hfmUserType = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR);
		HorizontalFieldManager hfmName = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR);
		HorizontalFieldManager hfmPassMembership = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR);
		HorizontalFieldManager hfmPassTrading = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR);
		HorizontalFieldManager hfmobj = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR | FIELD_HCENTER)
		{
			public int getPreferredWidth() {
				return AppConstants.screenWidth;
			}
			protected void sublayout(int arg0, int arg1) {
				// TODO Auto-generated method stub
				super.sublayout(AppConstants.screenWidth, arg1);
			}
		};
		//userType.setMinimalWidth(FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getAdvance(lblMobileLabel.getText()) + Snippets.padding*2);
		hfmUserType.add(userType);
		hfmName.add(objName);
		hfmPassMembership.add(objPassMembership);
		hfmPassTrading.add(objPassTrading);
		hfmobj.add(objBtnSignUp);

		int maxWidthForLabel = FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getAdvance(lblTradePass.getText()) + Snippets.padding*2+4;
		int rowHeight = lblNameLabel.getFont().getHeight()*2;
		if(AppConstants.screenHeight>240)
			rowHeight = lblNameLabel.getFont().getHeight()*3;
		int heightMin = rowHeight/4 + 10;

		// fieldGroup.add(new BlockField(0, 0, BlockField.H_CENTER, BlockField.V_TOP, objLabel));

		fieldGroup.add(new BlockField(3, rowHeight-heightMin-3, BlockField.H_LEFT, BlockField.V_NONE, lblUserTypeLabel));
		//fieldGroup.add(userType);
		fieldGroup.add(new BlockField(maxWidthForLabel, (rowHeight)-heightMin-7, BlockField.H_NONE, BlockField.V_NONE, hfmUserType));

		fieldGroup.add(new BlockField(3, (rowHeight*2)-heightMin, BlockField.H_LEFT, BlockField.V_NONE, lblNameLabel));
		fieldGroup.add(new BlockField(maxWidthForLabel, (rowHeight*2)-heightMin, BlockField.H_NONE, BlockField.V_NONE, hfmName));

		fieldGroup.add(new BlockField(3, (rowHeight*3)-heightMin, BlockField.H_LEFT, BlockField.V_NONE, lblMembershipPass));
		fieldGroup.add(new BlockField(maxWidthForLabel, (rowHeight*3)-heightMin, BlockField.H_NONE, BlockField.V_NONE, hfmPassMembership));

		fieldGroup.add(new BlockField(3, (rowHeight*4)-heightMin, BlockField.H_LEFT, BlockField.V_NONE, lblTradePass));
		fieldGroup.add(new BlockField(maxWidthForLabel, (rowHeight*4)-heightMin, BlockField.H_NONE, BlockField.V_NONE, hfmPassTrading));

		fieldGroup.add(new BlockField(3, rowHeight*4 + FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight() + FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight()/2, BlockField.H_CENTER, BlockField.V_NONE, horizontalFieldManager));
		fieldGroup.add(new BlockField(0, rowHeight*5 - heightMin - 5, BlockField.H_CENTER, BlockField.V_BOTTOM, hfmobj));
		int xn = 4;
		if(AppConstants.screenHeight>240)
			xn = xn+1;
		Snippets snippet = new Snippets(0, fieldGroup.getFields(), this, ActionCommand.CMD_NONE, null,rowHeight*xn + FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight()*(xn-1));
		vfm.add(snippet);
		vfm.add(getLinkLabel("Forgot Membership Password","forgot"));
		vfm.add(Utils.separator(10, 0x000000));
		vfm.add(getLinkLabel("Change Membership Password","change"));
		vfm.add(Utils.separator(10, 0x000000));
		vfm.add(getLabels("Sharekhan Limited version "+VERSION,"NSE SEBI Reg no.: INB/INF 231073330","Member Code no.: 10733"));
		vfm.add(getLabels("","BSE SEBI Reg No.: INB/INF 011073351","Member Code no.: 748"));
		add(vfm);
		
		//code to comment start
		if(DeviceInfo.isSimulator())
		{
			/*objName.setText("smsalim");
			objPassMembership.setText("smsalim2");
			objPassTrading.setText("22baad");*/
			
			/*objName.setText("sankara_n");
			objPassMembership.setText("sski@123");
			objPassTrading.setText("sski@456");*/
			
			/*objName.setText("niranjanmp");
			objPassMembership.setText("sharekhan1");
			objPassTrading.setText("331fc3");*/
		}
		//code to comment end
	/*	UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				Dialog.alert(HttpConnectionFactory.ConnectionString);
			}});*/
	}
	
	public boolean onMenu(int arg0) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void startRegistrationProcess(final String strUserName,final String strPassMem,final String strPassTrd) {
		thread = new Thread(new Runnable() {
			public void run() {
				try {
					if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
						ScreenInvoker.showWaitScreen(AppConstants.loadingMessage,AppConstants.SMALL_BOLD_FONT);
					else
						ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
					//doPaint();

					UserInfo.setUserName(strUserName);
					UserInfo.setUserPassMembership(strPassMem);
					UserInfo.setUserPassTrading(strPassTrd);
					/*imei = getIMEINumber();
					if(imei == null)*/
						imei = "";
					//Show wait registering moments
					//String strAuthURL = Utils.getAuthURL(choice[userType.getSelectedIndex()],strUserName, strPassMem,strPassTrd);
						String mid = IMSIString.length()>0?IMSIString:IMEIString;
						if(mid.length()==0)
						{
							try {
								mid = DeviceInfo.getDeviceId()+"";
							} catch (Exception e) {
								mid = IMSIString.length()>0?IMSIString:IMEIString;
							}
							finally
							{
								if(mid==null)
									mid = "";
								else if(mid.equalsIgnoreCase("null"))
									mid = "";
							}
						}
					String strAuthURL = Utils.getAuthURL(choice[userType.getSelectedIndex()],strUserName.trim(), strPassMem,strPassTrd,mid,VERSION);
					LOG.print(strAuthURL);
					String strResponse = HttpProcess.getHttpsMD5Connection(strAuthURL);
					final String result = parseAndSaveRegistrationInfo(strResponse);
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							if(errorStatus.equalsIgnoreCase("4"))
							{
								//ScreenInvoker.removeRemovableScreen();
								Vector vecstr = new Vector();
								vecstr.addElement(result);
								ActionInvoker.processCommand(new Action(ActionCommand.CMD_USER_REGISTRATION,null));
								ActionInvoker.processCommand(new Action(ActionCommand.CMD_APPMEMPASS,vecstr));
								//AppDialogMemPass appDialogView = new AppDialogMemPass(result);
								//UiApplication.getUiApplication().pushScreen(appDialogView);
								//Dialog.alert(result);
							}
							/*else if(errorStatus.equalsIgnoreCase("5"))
							{
								errorScreen = 1;
								ScreenInvoker.removeRemovableScreen();
								ActionInvoker.processCommand(new Action(ActionCommand.CMD_USER_REGISTRATION,null));
								Dialog.alert(result);
							}*/
							else if (result.equalsIgnoreCase("SUCCESS"))
							{
								Utils.LOGIN_STATUS = true;
								Utils.sessionAlive = true;
								Utils.keepSessionAlive();
								errorScreen = 0;
								id = "";
								mempass = "";
								tradepass = "";
								if(Utils.ENTRY_NEWS_COMMENTARY_DEFAULT == Utils.NEWS)
								{
									//Utils.ENTRY_NEWS_COMMENTARY_DEFAULT = Utils.DEAFULT;
									ActionInvoker.processCommand(new Action(ActionCommand.CMD_TRADE_VIEW_SCREEN,null));
								}
								else if(Utils.ENTRY_NEWS_COMMENTARY_DEFAULT == Utils.COMMENTARY)
								{
									//Utils.ENTRY_NEWS_COMMENTARY_DEFAULT = Utils.DEAFULT;
									ActionInvoker.processCommand(new Action(ActionCommand.CMD_COMMENTARY_SCREEN,null));
								}
								else
								{
									ScreenInvoker.removeRemovableScreen();
									ActionInvoker.processCommand(new Action(ActionCommand.CMD_GRID_SCREEN,null));
								}
							} else {
								if(result.indexOf("user")>-1 || result.indexOf("Login")>-1 )
								{
									errorScreen = 1;
								}
								else if(result.indexOf("Browsing")>-1)
								{
									errorScreen = 2;
								}
								else if(result.indexOf("Trading")>-1)
								{
									errorScreen = 3;
								}
								else
									errorScreen = 4;
								ScreenInvoker.removeRemovableScreen();
								ActionInvoker.processCommand(new Action(ActionCommand.CMD_USER_REGISTRATION,null));
								Dialog.alert(result);
							}                                                       
						}
					});
				} catch (Exception ex) {
					//ScreenInvoker.removeRemovableScreen();
					//Dialog.alert("Error occured during Registration Process");
				}
			}
		});
		thread.start();
	}

	
	
	public boolean onSavePrompt() {
		return true;
	}

	public String parseAndSaveRegistrationInfo(String strResponse)
	{
		/*	///TEST AREA START
			UserInfo.setUserID("250037");
			UserInfo.setDpid("1");
			UserInfo.setColFlag("SSKI");
			return "SUCCESS";
			///TEST AREA END*/
		LOG.print(strResponse);
		Json js = new Json(strResponse);
		String customerId="";
		String msg="Kindly check your internet connection!";
		if(js.getdata.size()<1)
			return msg;
		//for(int i=0;i<js.getdata.size();i++)
		//{
			try
			{
				Hashtable ht = (Hashtable) js.getdata.elementAt(0);
			customerId = (String)ht.get("CUSTOMER_ID");
			msg = (String)ht.get("ERROR_MSG");
			errorStatus = (String)ht.get("ERROR_STATUS");
			if((String)ht.get("DP_ID")!=null)
			UserInfo.setDpid((String)ht.get("DP_ID"));
			UserInfo.setUserID(customerId);
			//UserInfo.setUserID("1212121212");
			UserInfo.setColFlag((String)ht.get("COLLABORATORFLAG"));
			if(errorStatus.equalsIgnoreCase("4") || errorStatus.equalsIgnoreCase("5"))
			{
				return msg;
			}
			else if(customerId.length()==0)
				return msg;
			else if(msg.equalsIgnoreCase("SUCCESS"))
			{
				UserInfo.setUserID(customerId);
				//UserInfo.setUserID("1212121212");
				return msg;
			}
			else
				return msg;
	}catch(Exception e){ return msg;}
		//}
		//return msg;
		/*if(errorStatus.equalsIgnoreCase("4"))
		{
			return msg;
		}
		else if(customerId.length()==0)
			return msg;
		else if(msg.equalsIgnoreCase("SUCCESS"))
		{
			UserInfo.setUserID(customerId);
			//UserInfo.setUserID("1212121212");
			return msg;
		}
		else
			return msg;*/
		
		
	}

	public void actionPerfomed(byte Command, Object sender) {
		// TODO Auto-generated method stub

	}

	public boolean validateEmail(String str) {
		String at = "@";
		String dot = ".";
		int lat = str.indexOf(at);
		int lstr = str.length();
		// int ldot=str.indexOf(dot);
		if (str.indexOf(at) == -1) {
			return false;
		}

		if (str.indexOf(at) == -1 || str.indexOf(at) == 0
				|| str.indexOf(at) == lstr) {
			return false;
		}

		if (str.indexOf(dot) == -1 || str.indexOf(dot) == 0
				|| str.indexOf(dot) == lstr) {
			return false;
		}

		if (str.indexOf(at, (lat + 1)) != -1) {
			return false;
		}

		if (str.substring(lat - 1, lat) == dot
				|| str.substring(lat + 1, lat + 2) == dot) {
			return false;
		}

		if (str.indexOf(dot, (lat + 2)) == -1) {
			return false;
		}

		if (str.indexOf(" ") != -1) {
			return false;
		}

		return true;
	}
	public boolean onClose() {
		if(Utils.sessionExpiredTime == 0)
			System.exit(0);
		else
		{
			Action action = new Action(ActionCommand.CMD_GRID_SCREEN,null);
			ActionInvoker.processCommand(action);
		}
		return true;
	}
	
	public LabelField getLabels(final String text1, final String text2, final String text3)
	{
		return new LabelField("",FOCUSABLE)
		{
			public void setFont(Font arg0) {
				super.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
			}
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x000000);
				graphics.fillRect(0, 0, getPreferredWidth(),getPreferredHeight());
			}
			protected void paint(Graphics graphics) {
				graphics.setColor(0x222222);
				graphics.fillRect(0, 2+(getFont().getHeight()), getPreferredWidth(), 4+(getFont().getHeight()*2));
				graphics.setColor(0x999999);
				graphics.drawText(text1,5,2);
				graphics.drawText(text2,5,4+(getFont().getHeight()));
				graphics.drawText(text3, 5, 6+(getFont().getHeight()*2));
			}
			public int getPreferredHeight() {
				return 6+(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight()*5);
			}
			public int getPreferredWidth() {
				return AppConstants.screenWidth;
			}
			protected void layout(int width, int height) {
				super.layout(width, getPreferredHeight());
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
		};
	}
	
	public LabelField getLinkLabel(final String text1, final String differ)
	{
		return new LabelField("",FOCUSABLE)
		{
			public void setFont(Font arg0) {
				super.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
			}
			 protected void onFocus(int direction) {
	                super.onFocus(direction);
	                invalidate();
	        }
	        
	        protected void onUnfocus() {
	                super.onUnfocus();
	                invalidate();
	        }
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x000000);
				graphics.fillRect(0, 0, getPreferredWidth(),getPreferredHeight());
			}
			protected void paint(Graphics graphics) {
				graphics.setColor(0x000000);
				graphics.fillRect(0, 0, getPreferredWidth(),getPreferredHeight());
				graphics.setColor(Color.BLUE);
				graphics.drawText(text1,5,(getPreferredHeight()/2)-(getFont().getHeight()/2));
				graphics.setColor(isFocus()?Color.ORANGE:Color.BLUE);
				graphics.drawLine(5, (getPreferredHeight()/2)-(getFont().getHeight()/2) + getFont().getHeight() , 4 + getFont().getAdvance(text1), (getPreferredHeight()/2)-(getFont().getHeight()/2) + getFont().getHeight());
			}
			public int getPreferredHeight() {
				return 8+FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight();
			}
			public int getPreferredWidth() {
				return AppConstants.screenWidth;
			}
			protected void layout(int width, int height) {
				super.layout(width, getPreferredHeight());
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
			
			protected boolean navigationClick(int arg0, int arg1) {
				if(differ.equals("change"))
				{
					LOG.print(differ);
					Browser.getDefaultSession().displayPage(AppConstants.changeMembershipPasswordURL);
				}
				else if(differ.equals("forgot"))
				{
					LOG.print(differ);
					Browser.getDefaultSession().displayPage(AppConstants.forgetMembershipPasswordURL);
				}
				return super.navigationClick(arg0, arg1);
			}
		};
	}
	  public boolean keyDown( int keyCode, int time )
      {
              int key = Keypad.key( keyCode );
              if(key == Keypad.KEY_END)
              {
                      Utils.WATCHLIST_CLOSE = true;
                      LOG.print("KEY_END EXIT from app");
                      MyWatchList.isFINISHED = true;
                      System.exit(0);
              }
              return super.keyDown(keyCode, time);
      }
	  
	  public static void loadFNOIndices() {
			threadIndices = new Thread(new Runnable() {
				public void run() {
					try {
						String strResponse = HttpProcess.getHttpConnection(AppConstants.domainUrl + "SK_android/FNOIndicesList.php");
						final String result = strResponse;
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								if (result!=null)
								{
									Json js = new Json(result);
									if(js.getdata.size()!=0){
									SearchEFScreen.niftyIndices = new String[js.getdata.size()];
									Vector v = new Vector();
									for(int i=0;i<js.getdata.size();i++)
									{
										Hashtable ht = (Hashtable) js.getdata.elementAt(i);
										if(ht.containsKey("IndexName"))
										{
											v.addElement((String)ht.get("IndexName"));
										}
									}
									int j=0;
									String text = "NIFTY";
									if(v.contains(text))
									{
										SearchEFScreen.niftyIndices[j] = text;
										v.removeElement(text);
										j++;
									}
									text = "MINIFTY";
									if(v.contains(text))
									{
										SearchEFScreen.niftyIndices[j] = text;
										v.removeElement(text);
										j++;
									}
									text = "BANKNIFTY";
									if(v.contains(text))
									{
										SearchEFScreen.niftyIndices[j] = text;
										v.removeElement(text);
										j++;
									}
									text = "CNXIT";
									if(v.contains(text))
									{
										SearchEFScreen.niftyIndices[j] = text;
										v.removeElement(text);
										j++;
									}
									text = "CNXINFRA";
									if(v.contains(text))
									{
										SearchEFScreen.niftyIndices[j] = text;
										v.removeElement(text);
										j++;
									}
									text = "CNXPSE";
									if(v.contains(text))
									{
										SearchEFScreen.niftyIndices[j] = text;
										v.removeElement(text);
										j++;
									}
									text = "DJIA";
									if(v.contains(text))
									{
										SearchEFScreen.niftyIndices[j] = text;
										v.removeElement(text);
										j++;
									}
									for(int i=0;j<SearchEFScreen.niftyIndices.length;j++,i++)
									{
										if(v.size()!=0)
										{
											text = (String)v.elementAt(i);
											SearchEFScreen.niftyIndices[j] = text;
											v.removeElement(text);
										}
									}
									}
								} 
								
							}
						});
					} catch (Exception ex) {
						SearchEFScreen.niftyIndices = null;
					}
				}
			});
			threadIndices.start();
		}
}
