package com.snapwork.view;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.beans.Expiry;
import com.snapwork.beans.FoScrips;
import com.snapwork.components.CustomLabelField;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.Screen;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.Json;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.DBmanager;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.HTTPGetConnection;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

public class SplashPreScreen extends MainScreen implements FieldChangeListener{

	private CustomLabelField lblStatusMessage = null;
	private int date ;
	private int month ;
	private Expiry expiry;
	public SplashPreScreen(String notificationType)
    {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
    	add(new BitmapField(ImageManager.getSplashImage(), USE_ALL_HEIGHT | USE_ALL_WIDTH | NON_FOCUSABLE));
    	//Set The Ui Elements
        lblStatusMessage = new CustomLabelField(AppConstants.loadingMessage, USE_ALL_WIDTH | USE_ALL_HEIGHT, Color.BLUE, FontLoader.getFont(AppConstants.EXTRA_SMALL_BOLD_FONT)) {

			protected void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}

    	};
    	lblStatusMessage.setChangeListener(new FieldChangeListener()
    	{
			public void fieldChanged(Field field, int context) {
				//doPaint();
			}
		});
        setStatus(lblStatusMessage);
        lblStatusMessage.setText("Checking internet connection...");
        //doPaint();
       
	startAppIntializationProcess(notificationType);
       

	   
				// getExpiryData();
    }
    
    public void updateUI(final String text)
    {
    	   	UiApplication.getUiApplication().invokeLater(new Runnable()
    	   	{
				
				public void run()
				{
					lblStatusMessage.setText(text);
				}
			});
    }
    public void startAppIntializationProcess(final String notificationType)
    {
    	//Check Connection
    	Runnable runnable = new Runnable()
    	{
			public void run()
			{
				try
				{
					int i = 10;
					while(true){
			        if(i>4000)
			        {
			        	ActionInvoker.processCommand(new Action(ActionCommand.CMD_SPLASH,notificationType));
			            break;
					}
			       Thread.sleep(i);
			       i = i + 10;
			        
					}
				} catch(Exception ex) {
				}
			
			}
		};
		new Thread(runnable).start();
    }
    
	public void fieldChanged(Field field, int context) {
		//doPaint();
	}
	
}
