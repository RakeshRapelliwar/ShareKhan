package com.snapwork.view;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.text.TextFilter;
import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomBasicEditField2;
import com.snapwork.components.CustomBasicPasswordField2;
import com.snapwork.components.CustomLabelField;
import com.snapwork.components.CustomMultilineTextField;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.TitleBar;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.parsers.Json;
import com.snapwork.parsers.TradeNowMainParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;
import com.snapwork.view.trade.ReportsOrderViewScreen;

public class TradingQAChangeScreen extends MainScreen implements ActionListener,RemovableScreen,ReturnDataWithId
{
	private BottomMenu bottomMenu = null;
	public TradingQAChangeScreen() {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		createUI("Trading Hint Q&A");
	}

	protected void paintBackground(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getWidth(), getHeight());
	}
	CustomBasicEditField2 objLoginID,objQue,objAns;
	CustomBasicPasswordField2   objPassword;
	String message = "Error!!!";
	public void createUI(String strTitle) {
		//set Title
		TitleBar titleBar = new TitleBar(strTitle); 
		setTitle(titleBar);
		VerticalFieldManager vfm = new VerticalFieldManager(HORIZONTAL_SCROLL |HORIZONTAL_SCROLLBAR | VERTICAL_SCROLL | VERTICAL_SCROLLBAR )
		{
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}  
			protected void sublayout(int width, int height) {
				super.sublayout(width,height);
				setExtent(width, AppConstants.screenHeight-TitleBar.getItemHeight());
			}
		
		};
		//final String text = "Sample text sample text sample text sample text sample text sample text sample text sample text sample text sample text?";
		final String text = "";
		CustomMultilineTextField lblQuestionLabel = new CustomMultilineTextField(AppConstants.screenWidth, ((FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance(text)/AppConstants.screenWidth)+1)*FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight(), FontLoader.getFont(AppConstants.BIG_PLAIN_FONT), 0xeeeeee)
		{
			protected void paintBackground(Graphics graphics) {
			graphics.setColor(0x333333);
			graphics.fillRect(0, 0, AppConstants.screenWidth, getHeight());
		}
		};
		lblQuestionLabel.setText(text);
		lblQuestionLabel.setEditable(false);
		final CustomLabelField lblLoginID = new CustomLabelField(" Login ID : ", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT))
		{
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x333333);
				graphics.fillRect(0, 0, AppConstants.screenWidth, getHeight());
			}
			public int getPreferredWidth() {
				return AppConstants.screenWidth;
			}
		};
		objLoginID = new CustomBasicEditField2(BasicEditField.NO_NEWLINE | BasicEditField.FOCUSABLE | BasicEditField.EDITABLE);
		objLoginID.setFilter(TextFilter.get(TextFilter.DEFAULT));
		objLoginID.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
		objLoginID.setText(UserInfo.getUserName());
		objLoginID.setEditable(false);
		HorizontalFieldManager hfmLoginID = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR )
		{
			protected void sublayout(int width, int height) {
				layoutChild(getField(0), (AppConstants.screenWidth/2)-10, getField(0).getPreferredHeight());
				setPositionChild(getField(0), 5, 0);
				//super.sublayout(AppConstants.screenWidth, height);
				setExtent(AppConstants.screenWidth, getField(0).getHeight());
			}
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x333333);
				graphics.fillRect(0, 0, AppConstants.screenWidth, getHeight());
			}
		};
		
		//MemberShip new hint Question
		final CustomLabelField lblQue = new CustomLabelField(" Enter new hint question : ", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT))
		{
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x333333);
				graphics.fillRect(0, 0, AppConstants.screenWidth, getHeight());
			}
			public int getPreferredWidth() {
				return AppConstants.screenWidth;
			}
		};
		objQue = new CustomBasicEditField2(Color.BLACK | BasicEditField.NO_NEWLINE | BasicEditField.NO_COMPLEX_INPUT );
		objQue.setFilter(TextFilter.get(TextFilter.DEFAULT));
		objQue.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));

		//MemberShip new hint Answer
		CustomLabelField lblAns = new CustomLabelField(" Enter new hint answer : ", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT))
		{
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x333333);
				graphics.fillRect(0, 0, AppConstants.screenWidth, getHeight());
			}
			public int getPreferredWidth() {
				return AppConstants.screenWidth;
			}
		};
		objAns = new CustomBasicEditField2(Color.BLACK | BasicEditField.NO_NEWLINE | BasicEditField.NO_COMPLEX_INPUT );
		objAns.setFilter(TextFilter.get(TextFilter.DEFAULT));
		objAns.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));


		//MemberShip Password Row
		CustomLabelField lblPassword = new CustomLabelField(" Enter trading password : ", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT))
		{
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x333333);
				graphics.fillRect(0, 0, AppConstants.screenWidth, getHeight());
			}
			public int getPreferredWidth() {
				return AppConstants.screenWidth;
			}
		};
		objPassword = new CustomBasicPasswordField2("", "", 12, PasswordEditField.FILTER_DEFAULT);

		HorizontalFieldManager horizontalFieldManager = new HorizontalFieldManager(VERTICAL_SCROLL | VERTICAL_SCROLLBAR | HORIZONTAL_SCROLL | HORIZONTAL_SCROLLBAR)
		{
			protected void sublayout(int width, int height) {
				layoutChild(getField(0), width, getField(0).getPreferredHeight());
				setPositionChild(getField(0), (AppConstants.screenWidth/2)-(getField(0).getWidth()/2), 0);
				setExtent(width, getField(0).getPreferredHeight());
			}
		};

		//Add Sign Up Button
		ButtonField objBtnSignUp = new ButtonField(FIELD_HCENTER)
		{
			public int getPreferredHeight() {
				return getFont().getHeight()*2;
			}
			
			protected boolean navigationClick(int status, int time) {
				boolean firstStep = true,secondStep = false,thirdStep = false;
				if(firstStep) {
					secondStep = true;
					boolean flag = false;
					boolean one = true;
					String text = "Please enter";
					if(objQue.getText().trim().length()==0)
					{
						text = text + " New hint Question,";
						flag = true;
						one = false;
					} 
					if (objAns.getText().trim().length()==0)
					{
						text = text + " New hint Answer,";
						flag = true;
						one = false;
					}
					if(objPassword.getText().trim().length()==0)
					{
						//ScreenInvoker.showDialog("Please enter Username");
						text = text + " Password,";
						secondStep = false;
						flag = true;
						one = false;
					} 

					if(!UserInfo.getUserPassTrading().equals(objPassword.getText()))
					{
						if(one)
							text = " Password does not match,";
						else
							text = text + " \n Password does not match,";
						secondStep = false;
						flag = true;
					}

					if(flag)
					{
						text = text.substring(0, text.length()-1)+"!";
						ScreenInvoker.showDialog(text);
						secondStep = false;
					}

					if(secondStep)
					{
						thirdStep = true;
						if(thirdStep)
						{
							startProfileChangeProcess(objQue.getText(),objAns.getText());
						}
					}
				} 
				return true;
			}
		};
		objBtnSignUp.setLabel("Change");
//		vfm.add(lblQuestionLabel);
//		vfm.add(new SeparatorField());
		//vfm.add(Utils.separator(10, 0x000000));
		//vfm.add(lblQue);
		HorizontalFieldManager hfmQue = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR )
		{
			protected void sublayout(int width, int height) {
				layoutChild(getField(0), AppConstants.screenWidth-30, height);
				setPositionChild(getField(0), 5, 0);
				//super.sublayout(AppConstants.screenWidth, height);
				setExtent(AppConstants.screenWidth, getField(0).getHeight());
			}
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x333333);
				graphics.fillRect(0, 0, AppConstants.screenWidth, getHeight());
			}
		};
		HorizontalFieldManager hfmAns = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR)
		{
			protected void sublayout(int width, int height) {
				layoutChild(getField(0), AppConstants.screenWidth-30, getField(0).getPreferredHeight());
				setPositionChild(getField(0), 5, 0);
				setExtent(AppConstants.screenWidth, getField(0).getHeight());
				//super.sublayout(AppConstants.screenWidth, height);
			}
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x333333);
				graphics.fillRect(0, 0, AppConstants.screenWidth, getHeight());
			}
		};
		HorizontalFieldManager hfmPassword = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR)
		{
			protected void sublayout(int width, int height) {
				layoutChild(getField(0), (AppConstants.screenWidth/2)-10, height);
				setPositionChild(getField(0), 5, 0);
				//super.sublayout(AppConstants.screenWidth, height);
				setExtent(AppConstants.screenWidth, getField(0).getHeight());
			}
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x333333);
				graphics.fillRect(0, 0, AppConstants.screenWidth, getHeight());
			}
		};
		hfmLoginID.add(objLoginID);
		hfmQue.add(objQue);
		hfmAns.add(objAns);
		hfmPassword.add(objPassword);
		vfm.add(Utils.separator(FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight(), 0x000000));
		vfm.add(Utils.separatorRound(FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight(), 0x333333, true));
		vfm.add(lblLoginID);
		vfm.add(hfmLoginID);
		vfm.add(Utils.separator(FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight(), 0x333333));
		vfm.add(lblQue);
		vfm.add(hfmQue);
		vfm.add(Utils.separator(FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight(), 0x333333));
		vfm.add(lblAns);
		vfm.add(hfmAns);
		vfm.add(Utils.separator(FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight(), 0x333333));
		vfm.add(lblPassword);
		vfm.add(hfmPassword);
		vfm.add(Utils.separatorRound(FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight()+10, 0x333333, false));
		horizontalFieldManager.add(objBtnSignUp);
		vfm.add(new LabelField());
		vfm.add(horizontalFieldManager);
		add(vfm);
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
		
	}

	public void startProfileChangeProcess(final String strQue,final String strAns) {
		ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
		//doPaint();
		new TradeNowMainParser(Utils.getUpdateQAURL(AppConstants.updateQATrading, UserInfo.getUserName(), strQue,strAns), this, 904);
	}

	public boolean onSavePrompt()
	{
		return true;
	}

	public boolean onMenu(int instance) 
	{
		return false;
	}

	public String parseAndSaveRegistrationInfo(String strResponse)
	{

		Json js = new Json(strResponse);
		String msg="Error!!!";
		for(int i=0;i<js.getdata.size();i++)
		{
			Hashtable ht = (Hashtable) js.getdata.elementAt(i);
			msg = (String)ht.get("STATUS");
			message = (String)ht.get("MSG");
		}
		if(msg.equalsIgnoreCase("SUCCESS"))
		{
			return msg;
		}
		else
			return message;
	}

	public void actionPerfomed(byte Command, Object sender)
	{
		ActionInvoker.processCommand(new Action(Command,null));
	}

	public boolean validateEmail(String str) {
		String at = "@";
		String dot = ".";
		int lat = str.indexOf(at);
		int lstr = str.length();
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
	
	protected boolean keyDown( int keyCode, int time ) {
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
		return super.keyDown(keyCode, time);
	}

	public void setData(Vector vector, int id) {
		if(id == 904)
		{
			Hashtable hash = (Hashtable) vector.elementAt(1);
			if(hash.size() == 0)
			{
				UiApplication.getUiApplication().invokeAndWait(new Runnable() {
					public void run() {
						ScreenInvoker.removeRemovableScreen();  
					}
				});
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.alert("Unable to change the question. Please try again later!");
					}
				});
				return;
			}
			String text = (String)hash.get("STATUS");
			if (text.equalsIgnoreCase("ERROR"))
			{
				UiApplication.getUiApplication().invokeAndWait(new Runnable() {
					public void run() {
						ScreenInvoker.removeRemovableScreen();  
					}
				});
				final String msg = (String)hash.get("MSG");
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_PROFILE_SCREEN,null));
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.alert(msg);
					}
				});
				
			}
			else //if(text.equalsIgnoreCase("SUCCESS"))
			{
				UiApplication.getUiApplication().invokeAndWait(new Runnable() {
					public void run() {
						ScreenInvoker.removeRemovableScreen();  
					}
				});
				Vector vectorCommandData = new Vector();
				vectorCommandData.addElement("Trading Hint Q&A");
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_PROFILE_SCREEN, vectorCommandData));
				final String msg = (String)hash.get("MSG");
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.alert(msg);
					}
				});
			}          
		}
	}
}
