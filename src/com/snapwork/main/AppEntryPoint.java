package com.snapwork.main;

import javax.microedition.media.Controllable;

import com.snapwork.util.AppConstants;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.Utils;

import net.rim.blackberry.api.mail.ServiceConfiguration;
import net.rim.blackberry.api.mail.Session;
import net.rim.device.api.lcdui.control.DirectionControl;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;

/**
 * 
 * <p>This class is Started the Application and Service.
 * <p>Update Provision - Not Available
 * <p>IDENInfo.imeiToString(IDENInfo.getIMEI())
 */

public class AppEntryPoint
{
        public static void main(String[] args)
        {
               LOG.enable();
              // LOG.disable();
        	   com.snapwork.util.HTTPGetConnection.setConnections();
               LOG.print("DeviceInfo.getSoftwareVersion() : "+DeviceInfo.getSoftwareVersion());
                try{
                        AppConstants.OS = Integer.parseInt(DeviceInfo.getSoftwareVersion().substring(0, DeviceInfo.getSoftwareVersion().indexOf(".")));
                        LOG.print("AppConstants.OS : "+AppConstants.OS);
                        Session mailSession = Session.getDefaultInstance();
                ServiceConfiguration config = mailSession.getServiceConfiguration();
                String email = config.getEmailAddress();
                //String email = Session.getDefaultInstance().getServiceConfiguration().getEmailAddress();
                if (email == null)
                                AppConstants.SEND_EMAIL = false;
                        else if(email.length()>1)
                                AppConstants.SEND_EMAIL = true;
                }
                catch(Exception e){}
                if(args[0].equals("UiApp"))
                {
                        String[] defaultReqApp = {"UiApp"};
                        String[] commentaryReqApp = {"UiApp","Commentary"};
                        String[] newsReqApp = {"UiApp","News"};
                        
                        ApplicationDescriptor[] runningAppDescriptors = ApplicationManager.getApplicationManager().getVisibleApplications();
                        ApplicationDescriptor defaultDescriptor = new ApplicationDescriptor(ApplicationDescriptor.currentApplicationDescriptor(), defaultReqApp);
                        ApplicationDescriptor commentaryDescriptor = new ApplicationDescriptor(ApplicationDescriptor.currentApplicationDescriptor(), commentaryReqApp);
                        ApplicationDescriptor newsDescriptor = new ApplicationDescriptor(ApplicationDescriptor.currentApplicationDescriptor(), newsReqApp);
                        boolean isAppRunning = false;
                        if(args.length>1)
                        {
                                /*if(args[1].equalsIgnoreCase("News"))
                                        Utils.ENTRY_NEWS_COMMENTARY_DEFAULT = Utils.NEWS;
                                else
                                        Utils.ENTRY_NEWS_COMMENTARY_DEFAULT = Utils.COMMENTARY;*/
                        }
                        try
                        {
                                for(int i=0;i<runningAppDescriptors.length;i++)
                                {
                                        if(runningAppDescriptors[i].equals(defaultDescriptor))
                                        {
                                                ApplicationManager.getApplicationManager().runApplication(defaultDescriptor);
                                                isAppRunning = true;
                                                i=runningAppDescriptors.length;
                                        }
                                        /*else if(runningAppDescriptors[i].equals(newsDescriptor))
                                        {
                                                ApplicationManager.getApplicationManager().runApplication(newsDescriptor);
                                                isAppRunning = true;
                                                i=runningAppDescriptors.length;
                                        }
                                        else if (runningAppDescriptors[i].equals(commentaryDescriptor))
                                        {
                                                ApplicationManager.getApplicationManager().runApplication(commentaryDescriptor);
                                                isAppRunning = true;
                                                i=runningAppDescriptors.length;
                                        }*/
                                }
                        }
                        catch(Exception e)
                        {
                                //System.out.println("Error : "+e.toString());
                        }
                        if(isAppRunning==false) {
                                if(args.length==2) {
                                        BseNseStocksLive bseNseStocksLive = new BseNseStocksLive(args[1]);
                                        Utils.disableOrientationChange();
                                        if (!bseNseStocksLive.isHandlingEvents())
                                        {
                                                bseNseStocksLive.enterEventDispatcher();
                                        }
                                }
                                else
                                {
                                        BseNseStocksLive bseNseStocksLive = new BseNseStocksLive();
                                        Utils.disableOrientationChange();
                                        if (!bseNseStocksLive.isHandlingEvents())
                                        {
                                                bseNseStocksLive.enterEventDispatcher();
                                        }
                                }
                        }
                }
                /*else if(args[0].equals("BgApp"))
                {
                        new AppBackgroundWorker();
                }
                else if(args!=null && args.length!=0 && args[0].equals("NotificationHandler"))
                {
                        AppNotificationManager appNotificationManager = new AppNotificationManager(args[1],args[2]);
                        appNotificationManager.enterEventDispatcher();
                }*/
        }
}
