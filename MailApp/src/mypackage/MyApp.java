package mypackage;

import net.rim.device.api.ui.UiApplication;


public class MyApp extends UiApplication
{
   
    public static void main(String[] args)
    {
        
        MyApp theApp = new MyApp();       
        theApp.enterEventDispatcher();
    }
    

   
    public MyApp()
    {        
        
        pushScreen(new MyScreen());
    }    
}
