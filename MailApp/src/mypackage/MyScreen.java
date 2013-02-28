package mypackage;

import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.blackberry.api.invoke.PhoneArguments;
import net.rim.blackberry.api.mail.Address;
import net.rim.blackberry.api.mail.Message;
import net.rim.device.api.system.CodeModuleManager;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class MyScreen extends MainScreen {
	/**
	 * Creates a new MyScreen object
	 */

	private String to, from, subject, body;

	ButtonField button = new ButtonField("Invoke Mail");
	ButtonField button1 = new ButtonField("Invoke Call");
	ButtonField button2 = new ButtonField("Invoke GPS");
	ButtonField button3 = new ButtonField("Invoke GoogleMap");
	
	
	
	EditField edt;

	public MyScreen() {
		// Set the displayed title of the screen
		setTitle("MyTitle");

		add(button);
		add(button1);
		add(button2);
		add(button3);

	}

	protected boolean navigationClick(int status, int time) {
		Field f = getLeafFieldWithFocus();
		if (f == button) {
			if(edt!=null)
			{
				delete(edt);
				edt=null;
			}
			try {
				Message m = new Message();
				Address a = new Address("mLi@rim.com", "Ming Li");
				Address[] addresses = { a };
				m.addRecipients(
						net.rim.blackberry.api.mail.Message.RecipientType.TO,
						addresses);
				m.setContent("A message for you...");
				m.setSubject("Email for you");
				Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,
						new MessageArguments(m));

			} catch (Exception e) {
				Dialog.alert("" + e);
			}

		} else if (f == button2) {
			
			if(edt==null)
			{
				edt=new EditField();
				edt.setText("LocatinTracker  :  ");
				add(edt);
				
			}
			
			
			new LocationTracker(edt);
		} else if(f==button1){
			
			if(edt!=null)
			{
				delete(edt);
				edt=null;
			}
			PhoneArguments phoneArgs;
			phoneArgs = new PhoneArguments(PhoneArguments.ARG_CALL,
					"9920931482");
			Invoke.invokeApplication(Invoke.APP_TYPE_PHONE, phoneArgs);
		}else{
			if(edt!=null)
			{
				delete(edt);
				edt=null;
			}
			int mh = CodeModuleManager.getModuleHandle("GoogleMaps");
		    if (mh == 0)
		    {
		         try
		         {
		           String url="http://m.google.com/maps";
		           net.rim.blackberry.api.browser.BrowserSession bSession = net.rim.blackberry.api.browser.Browser.getDefaultSession();
		           bSession.displayPage(url);
		           bSession.showBrowser();
//		           UiApplication.getUiApplication().pushScreen(new Locations());
		          } catch (Exception e) {
		                   e.printStackTrace();
		           }
		   }
			
			
		}
		return super.navigationClick(status, time);
	}
}
