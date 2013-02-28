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

public class SplashScreen extends MainScreen implements FieldChangeListener,RemovableScreen, ReturnString {

	private CustomLabelField lblStatusMessage = null;
	private int date ;
	private int month ;
	private Expiry expiry;
	public SplashScreen(String notificationType)
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
    	getExpiryData();
//    	//Check Connection
//    	Runnable runnable = new Runnable()
//    	{
//			public void run()
//			{
//				try
//				{
//					//Check Connection
//					//if(HTTPGetConnection.getURL("url").length())
//					//int i = 100;
//					//while(true){
//			      //  if(i>4000)
//			      //  {
//			        	if(HttpProcess.checkHttpConnection()/*HTTPGetConnection.ConnectionUsed.length()>0*/)
//					{
//			        	
//			        	getExpiryData();
//			        	
//					}
//					else
//					{
//						try{
//							UiApplication.getUiApplication().invokeAndWait(new Runnable() {
//								public void run() {
//									Dialog.alert("No Network Connection!");  
//								}
//							});
//                       	 }
//                       	 catch(Exception e)
//                       	 {
//                       		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
//        						public void run() {
//        							Dialog.alert("No Network Connection!");  
//        						}
//        					});
//                       	 }
//					}
//			       // break;
//					//}
//			      //  Thread.sleep(i);
//			       // i = i + 100;
//			        
//					//}
//				} catch(Exception ex) {
//					UiApplication.getUiApplication().invokeAndWait(new Runnable() {
//						public void run() {
//							Dialog.alert("No Network Connection!");  
//						}
//					});
//				}
//			
//			}
//		};
//		new Thread(runnable).start();
    }
//    public void startApp(final String notificationType)
//    {
//    	//getExpiryData();
//    	//Check Connection
//    	Runnable runnable = new Runnable()
//    	{
//    		public void run()
//			{
//				/*try
//				{*/
//					//Check Connection
//					//if(HTTPGetConnection.getURL("url").length())
//					int i=0;
//			        while(true)
//					{
//			        	
//			        	try {
//								Thread.sleep(1000);
//								i = i+ 1000;
//							} catch (InterruptedException e1) {
//							}
//			        	if(i>4000)
//			        	{
//			        		startAppIntializationProcess(notificationType);
//			        		break;
//			        	}
//			        	
//					}
//					/*else
//					{
//						try{
//							UiApplication.getUiApplication().invokeAndWait(new Runnable() {
//								public void run() {
//									Dialog.alert("No Network Connection!");  
//								}
//							});
//                       	 }
//                       	 catch(Exception e)
//                       	 {
//                       		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
//        						public void run() {
//        							Dialog.alert("No Network Connection!");  
//        						}
//        					});
//                       	 }
//					}*/
//				/*} catch(Exception ex) {
//					UiApplication.getUiApplication().invokeAndWait(new Runnable() {
//						public void run() {
//							Dialog.alert("No Network Connection!");  
//						}
//					});
//				}*/
//			
//			}
//		};
//		new Thread(runnable).start();
//    }
//    public void startAppIntializationProcess(final String notificationType)
//    {
//    	//getExpiryData();
//    	//Check Connection
//    	Runnable runnable = new Runnable()
//    	{
//    		public void run()
//			{
//				/*try
//				{*/
//					//Check Connection
//					//if(HTTPGetConnection.getURL("url").length())
//					boolean flag = false;
//			        while(true)
//					{
//			        	try{
//			        		HTTPGetConnection.getURL("http://www.test.com");
//			        		flag = true;
//			        	}
//			        	catch(Exception e)
//			        	{
//			        		flag = false;
//			        		try {
//								Thread.sleep(100);
//							} catch (InterruptedException e1) {
//							}
//			        	}
//			        	if(flag)
//			        	{
//			        		getExpiryData();
//			        		break;
//			        	}
//			        	
//					}
//					/*else
//					{
//						try{
//							UiApplication.getUiApplication().invokeAndWait(new Runnable() {
//								public void run() {
//									Dialog.alert("No Network Connection!");  
//								}
//							});
//                       	 }
//                       	 catch(Exception e)
//                       	 {
//                       		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
//        						public void run() {
//        							Dialog.alert("No Network Connection!");  
//        						}
//        					});
//                       	 }
//					}*/
//				/*} catch(Exception ex) {
//					UiApplication.getUiApplication().invokeAndWait(new Runnable() {
//						public void run() {
//							Dialog.alert("No Network Connection!");  
//						}
//					});
//				}*/
//			
//			}
//		};
//		new Thread(runnable).start();
//    }

	public void fieldChanged(Field field, int context) {
		//doPaint();
	}
	
	public void next()
	{
		//DBmanager.dropDatabase(AppConstants.appDBTOC);
		
		Vector vector = DBmanager.getRecords(AppConstants.appDBTOC);
		if(vector.size() == 0)
		{
			Action action = new Action(ActionCommand.CMD_TOC,null);
			ActionInvoker.processCommand(action);
		}
		else
		{
			Action action = new Action(ActionCommand.CMD_USER_REGISTRATION,null);
			ActionInvoker.processCommand(action);
		}
	}
	
	public void updateFNO(int date, int month)
	{
		this.date = date;
		this.month = month;
		new ReturnStringParser(AppConstants.getFNOURL, 222, this);
	}

	public void setReturnString(String string, int id) {
		LOG.print(string+"------------> "+id);
		if(id == 111)
		{
			try {
				Calendar cl = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
				cl.setTime(new Date());
				int date_ = cl.get(Calendar.DATE);
				int month_ = cl.get(Calendar.MONTH);
				month++;
				Vector vector = DBmanager.getRecords(AppConstants.appDBName);
				if(vector.size()==0)
				 {
					updateFNO(date_, month_);
				 }
				else
				{
					ByteArrayInputStream bais = new ByteArrayInputStream((byte[]) vector.elementAt(vector.size()-1));
					DataInputStream dis = new DataInputStream(bais);
					int _date  = Integer.parseInt(dis.readUTF());
					int _month = Integer.parseInt(dis.readUTF());
					dis.close();
					bais.close();
					if(date_ != _date)
					{
						updateFNO(_date, _month);
					}
					else if(month_ != _month)
					{
						updateFNO(_date, _month);
					}
					else
						getFNOdata();
				}
			} catch (NumberFormatException e) {
				System.exit(0);
			} catch (IOException e) {
				System.exit(0);
			} 
			expiry = null;
		}
	else if(id == 222)
		{
			Json js = new Json(string);
			Hashtable holder = new Hashtable();
			for(int i=0;i<js.getdata.size();i++)
			{
				Hashtable ht = (Hashtable) js.getdata.elementAt(i);
				holder.put((String)ht.get("CompanyCode"), (String)ht.get("Symbol"));
			}
			FoScrips.putHashTable(holder);
			next();
		}
	}
	
	public void getFNOdata()
	{
		 Hashtable ht = new Hashtable();
		try
		{
			Vector vector = DBmanager.getRecords(AppConstants.appDBName);
			for(int i=0; i<vector.size(); i++)
			{
				ByteArrayInputStream bais = new ByteArrayInputStream((byte[]) vector.elementAt(i));
				DataInputStream dis = new DataInputStream(bais);
				ht.put(dis.readUTF(),dis.readUTF());
				dis.close();
				bais.close();
			}
			
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			FoScrips.putHashTable(ht);
			next();
		}
	}
	
	public void getExpiryData()
	{
		expiry = new Expiry(this, 111); 
	}

}
