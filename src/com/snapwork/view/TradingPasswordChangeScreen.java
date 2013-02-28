package com.snapwork.view;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
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
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomBasicEditField;
import com.snapwork.components.CustomBasicPasswordField;
import com.snapwork.components.CustomLabelField;
import com.snapwork.components.CustomMultilineTextField;
import com.snapwork.components.FieldGroup;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.Snippets;
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

public class TradingPasswordChangeScreen extends MainScreen implements ActionListener,RemovableScreen,ReturnDataWithId
{
	public static String msg_http="Error";
	private BottomMenu bottomMenu = null;
	public TradingPasswordChangeScreen() {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		createUI("Trading Password");
	}

	protected void paintBackground(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		
	}

	CustomBasicEditField loginID;
	CustomBasicPasswordField  objPassOld,objPassNew, objPassRepeatConfirm;
	public void createUI(String strTitle) {
		//set Title
		TitleBar titleBar = new TitleBar(strTitle); 
		setTitle(titleBar);
		int titleBarHeight = titleBar.getPreferredHeight();

		VerticalFieldManager vfm = new VerticalFieldManager(HORIZONTAL_SCROLL |HORIZONTAL_SCROLLBAR | VERTICAL_SCROLL | VERTICAL_SCROLLBAR )
		{
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
				/*graphics.setColor(0x333333);
				int rowHeight = FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight()*2;
				int heightMin = rowHeight/4;
				
				graphics.fillRoundRect(2, ((rowHeight*1)-heightMin-10), AppConstants.screenWidth-4, ((rowHeight*4)-heightMin-10)-((rowHeight*1)-heightMin-10), 8, 8);
				
				graphics.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
				graphics.setColor(0xeeeeee);
				graphics.drawText(":", (AppConstants.screenWidth/2)-3, (rowHeight*1)-heightMin);
				graphics.drawText(":", (AppConstants.screenWidth/2)-3, (rowHeight*2)-heightMin);
				graphics.drawText(":", (AppConstants.screenWidth/2)-3, (rowHeight*3)-heightMin);*/
				graphics.setColor(0x333333);
				int rowHeight = FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*2;
				if(AppConstants.screenHeight>240)
					rowHeight = FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*3;
				int heightMin = rowHeight/4 + 10;
				graphics.fillRoundRect(2, ((rowHeight*1)-heightMin-2), AppConstants.screenWidth-4, ((rowHeight*5)-heightMin+2)-((rowHeight*1)-heightMin-2), 8, 8);
				if(AppConstants.screenHeight<241){
				graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				graphics.setColor(0xeeeeee);
				graphics.drawText(":", (AppConstants.screenWidth/2)-3, (rowHeight*1)-heightMin+(rowHeight/2)-(graphics.getFont().getHeight()/2));
				graphics.drawText(":", (AppConstants.screenWidth/2)-3, (rowHeight*2)-heightMin+(rowHeight/2)-(graphics.getFont().getHeight()/2));
				graphics.drawText(":", (AppConstants.screenWidth/2)-3, (rowHeight*3)-heightMin+(rowHeight/2)-(graphics.getFont().getHeight()/2));
				graphics.drawText(":", (AppConstants.screenWidth/2)-3, (rowHeight*4)-heightMin+(rowHeight/2)-(graphics.getFont().getHeight()/2));
				}
				
			}  
			protected void sublayout(int width, int height) {
				super.sublayout(width,height);
				setExtent(width, AppConstants.screenHeight-TitleBar.getItemHeight());
			}
		};

		FieldGroup fieldGroup = new FieldGroup("Trading");
		CustomLabelField lblLoginIDTypeLabel = new CustomLabelField(" Login ID", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));

		loginID = new CustomBasicEditField(Color.BLACK | NON_FOCUSABLE);
		loginID.setText(UserInfo.getUserName());
		loginID.setFilter(TextFilter.get(TextFilter.DEFAULT));
		loginID.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));

		//EmailID Row
		CustomLabelField lblEmailLabel = new CustomLabelField(" Old Password", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		objPassOld = new CustomBasicPasswordField("", "", 50, PasswordEditField.FILTER_DEFAULT);


		//Trading Password Row
		CustomLabelField lblMobileLabel = new CustomLabelField(" New Password", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		objPassNew = new CustomBasicPasswordField("", "", 50, PasswordEditField.FILTER_DEFAULT);

		//Trading Password Row
		CustomLabelField lblMobileLabel2 = new CustomLabelField(" Confirm Password", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		objPassRepeatConfirm = new CustomBasicPasswordField("", "", 50, PasswordEditField.FILTER_DEFAULT);

		HorizontalFieldManager horizontalFieldManager = new HorizontalFieldManager(VERTICAL_SCROLL | VERTICAL_SCROLLBAR | HORIZONTAL_SCROLL | HORIZONTAL_SCROLLBAR);


		//Add Sign Up Button
		ButtonField objBtnSignUp = new ButtonField(FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {
				boolean firstStep = true,secondStep = false,thirdStep = false;
				if(firstStep) {
					secondStep = true;
					boolean flag = false;
					boolean once = true;
					String text = "Please enter";
					if(objPassOld.getText().trim().length()==0)
					{
						text = text + " Old Password,";
						flag = true;
						once = false;
					} 
					if (objPassNew.getText().trim().length()==0)
					{
						text = text + " New Password,";
						flag = true;
						once = false;
					}
					if(objPassRepeatConfirm.getText().trim().length()==0)
					{
						//ScreenInvoker.showDialog("Please enter Username");
						text = text + " Confirm Password,";
						secondStep = false;
						flag = true;
						once = false;
					} 

					if(!objPassNew.getText().equals(objPassRepeatConfirm.getText()))
					{
						if(once)
							text = " New password does not match the confirm password,";
						else
							text = text + " \n New password does not match the confirm password,";
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
							startProfileChangeProcess(objPassOld.getText(),objPassNew.getText());
						}

					}
				} 
				return true;
			}
		};
		objBtnSignUp.setLabel("Change");
		HorizontalFieldManager hfmUserType = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR);
		HorizontalFieldManager hfmName = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR);
		HorizontalFieldManager hfmEmail = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR);
		HorizontalFieldManager hfmMobileNo = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR);
		hfmUserType.add(loginID);
		hfmName.add(objPassOld);
		hfmEmail.add(objPassNew);
		hfmMobileNo.add(objPassRepeatConfirm);

		int maxWidthForLabel = lblMobileLabel2.getFont().getAdvance(lblMobileLabel2.getText()) + Snippets.padding*2;
		int rowHeight = lblLoginIDTypeLabel.getFont().getHeight()*2;
		if(AppConstants.screenHeight>240)
			rowHeight = lblLoginIDTypeLabel.getFont().getHeight()*3;
		int heightMin = rowHeight/4;

		// fieldGroup.add(new BlockField(0, 0, BlockField.H_CENTER, BlockField.V_TOP, objLabel));

		fieldGroup.add(new BlockField(0, rowHeight-heightMin, BlockField.H_LEFT, BlockField.V_NONE, lblLoginIDTypeLabel));
		fieldGroup.add(new BlockField(maxWidthForLabel, (rowHeight)-heightMin, BlockField.H_NONE, BlockField.V_NONE, hfmUserType));

		fieldGroup.add(new BlockField(0, (rowHeight*2)-heightMin, BlockField.H_LEFT, BlockField.V_NONE, lblEmailLabel));
		fieldGroup.add(new BlockField(maxWidthForLabel, (rowHeight*2)-heightMin, BlockField.H_NONE, BlockField.V_NONE, hfmName));

		fieldGroup.add(new BlockField(0, (rowHeight*3)-heightMin, BlockField.H_LEFT, BlockField.V_NONE, lblMobileLabel));
		fieldGroup.add(new BlockField(maxWidthForLabel, (rowHeight*3)-heightMin, BlockField.H_NONE, BlockField.V_NONE, hfmEmail));

		fieldGroup.add(new BlockField(0, (rowHeight*4)-heightMin, BlockField.H_LEFT, BlockField.V_NONE, lblMobileLabel2));
		fieldGroup.add(new BlockField(maxWidthForLabel, (rowHeight*4)-heightMin, BlockField.H_NONE, BlockField.V_NONE, hfmMobileNo));

		fieldGroup.add(new BlockField(0, rowHeight*4 + FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight() + FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight()/2, BlockField.H_CENTER, BlockField.V_NONE, horizontalFieldManager));
		fieldGroup.add(new BlockField(0, rowHeight*5 - heightMin - 5, BlockField.H_CENTER, BlockField.V_BOTTOM, objBtnSignUp));
		int xn = 4;
		if(AppConstants.screenHeight>240)
			xn = xn+1;
		Snippets snippet = new Snippets(0, fieldGroup.getFields(), this, ActionCommand.CMD_NONE, null,rowHeight*xn + FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight()*xn);
		vfm.add(snippet);
		String text = "Note : \n >> Password length should be min 8 characters & max 12 characters.";
		CustomMultilineTextField lblInfo1 = new CustomMultilineTextField(AppConstants.screenWidth, ((FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getAdvance(text)/AppConstants.screenWidth)+2)*FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight(), FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT), 0xeeeeee, 0x000000);
		lblInfo1.setText(text);
		lblInfo1.setEditable(false);
		vfm.add(lblInfo1);
		vfm.add(Utils.separator(10, 0x000000));

		text = " >> It should have combination of alpha-numeric characters.";
		CustomMultilineTextField lblInfo2 = new CustomMultilineTextField(AppConstants.screenWidth, ((FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getAdvance(text)/AppConstants.screenWidth)+1)*FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight(), FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT), 0xeeeeee, 0x000000);
		lblInfo2.setText(text);
		lblInfo2.setEditable(false);
		vfm.add(lblInfo2);
		vfm.add(Utils.separator(10, 0x000000));

		text = " >> Preferably one special character like ! @ #$ % & *";
		CustomMultilineTextField lblInfo3 = new CustomMultilineTextField(AppConstants.screenWidth, ((FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getAdvance(text)/AppConstants.screenWidth)+1)*FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight(), FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT), 0xeeeeee, 0x000000);
		lblInfo3.setText(text);
		lblInfo3.setEditable(false);
		vfm.add(lblInfo3);
		vfm.add(Utils.separator(10, 0x000000));

		add(vfm);
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
		
	}

	public void startProfileChangeProcess(final String strEmail,final String strMobileNo) {
		ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
		//doPaint();
		new TradeNowMainParser(Utils.getTradingChangeProfileURL(UserInfo.getUserName().toUpperCase(), strEmail,strMobileNo,UserInfo.getColFlag(),UserInfo.getUserID()), this, 902);
	}

	public boolean onSavePrompt()
	{
		return true;
	}

	public boolean onMenu(int instance) 
	{
		return false;
	}

	public String parseAndSaveRegistrationInfo(String strResponse) {

		Json js = new Json(strResponse);
		//String customerId="";
		String msg ="ERROR";
		Hashtable ht = (Hashtable) js.getdata.elementAt(0);
		msg = (String)ht.get("STATUS");
		msg_http = (String)ht.get("MSG");
		return msg;
	}

	public void actionPerfomed(byte Command, Object sender) {
		ActionInvoker.processCommand(new Action(Command,null));
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
			if(id == 902)
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
							Dialog.alert("Unable to change the password. Please try again later!");
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
					vectorCommandData.addElement("TRADING PASSWORD");
					ActionInvoker.processCommand(new Action(ActionCommand.CMD_USER_REGISTRATION, vectorCommandData));
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
