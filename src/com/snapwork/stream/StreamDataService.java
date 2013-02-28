package com.snapwork.stream;

import java.util.Hashtable;

import net.rim.device.api.ui.UiApplication;

import com.lightstreamer.javameclient.midp.ConnectionInfo;
import com.lightstreamer.javameclient.midp.ConnectionListener;
import com.lightstreamer.javameclient.midp.ConnectionPolicy;
import com.lightstreamer.javameclient.midp.ConnectionProvider;
import com.lightstreamer.javameclient.midp.LSClient;
import com.lightstreamer.javameclient.midp.SimpleItemUpdate;
import com.lightstreamer.javameclient.midp.SimpleTableInfo;
import com.lightstreamer.javameclient.midp.SimpleTableListener;
import com.lightstreamer.javameclient.midp.SubscribedTableKey;
import com.snapwork.util.LOG;
import com.snapwork.view.MyWatchList;

public class StreamDataService implements SimpleTableListener{
	private StreamDataUpdate streamDataUpdate;
	private Hashtable hashtable = new Hashtable();
	private long time;
	private SubscribedTableKey stockKey = null;
	 private ConnectionPolicy policy = new ConnectionPolicy();
	 private LSClient lsClient;
	    private ConnectionInfo pushLightstreamerCom =
	        new ConnectionInfo("push.lightstreamer.com");
	    ConnectionListenerDispatcher listener = new ConnectionListenerDispatcher();
	    private String schema;
	    private String group;
	   public static boolean demo = false;
	   private boolean isClose;
	       
	    //static public ErrorPrompt errorPrompt;
	public StreamDataService(StreamDataUpdate streamDataUpdate, String schema, String group)
	{
		this.streamDataUpdate = streamDataUpdate;
		isClose = true;
		 policy.setPollingInterval(0);
		 this.schema = schema;
		 this.group = group;
	        //initiate the LSClient
	        lsClient = new LSClient();
	        //Always the same object instance will be passed to update events
	        lsClient.useReusableItemUpdates(true);
	        lsClient.useSingleConnection(true);
	        if(demo)
	        {
	        	  pushLightstreamerCom.setServer("push.lightstreamer.com");
	               pushLightstreamerCom.setPort(8888);
	              pushLightstreamerCom.setAdapter("DEMO");
	        }
	        else
	        {
	        pushLightstreamerCom.setServer("mstream.sharekhan.com");//220.226.189.185");
	      //  pushLightstreamerCom.setServer("220.226.189.188");
	        pushLightstreamerCom.setPort(80);
	        pushLightstreamerCom.setAdapter("SNAPWORK");
	        }
	        //set up a small internal buffer
	        lsClient.setBufferMax(2);
	        try {
	        	//Class bbProvider = Class.forName("com.snapwork.stream.BBConnectionProvider");
	            Class bbProvider = Class.forName("com.snapwork.stream.BBConnectionProviderOri");
	            //if we pass here it means that the BBConnectionProvider is in the classpath. Such class
	            //is an example implementation of the ConnectionProvider interface. 
	            //Such class have to be in the classpath only if we're building the BlackBerry version of the demo;
	            //if used with the plain-and-simple J2ME environment it will give compilation error as it uses some BB
	            //proprietary extensions to J2ME.
	            //That is why that class is isolated in its own source folder.
	            
	            lsClient.setConnectionProvider((ConnectionProvider)bbProvider.newInstance());
	            
	        } catch (ClassNotFoundException e) {
	            //BBConnectionProvider not available in the classpath, let the library use its default ConnectionProvider
	        } catch (InstantiationException e) {
	        } catch (IllegalAccessException e) {
	        }
	}
	public void OpenConnection()
	{
		//prepare and subscribe the stocklist table
        //String schema = "symbol ltp timestamp";
		//String schema = "symbol ltp timestamp prevclose dayhigh daylow percentchange changevalue";
       // String group = "item_13020033 item_12150008";
		SimpleTableInfo table;
		if(demo)
	{
		String schema = "last_price time pct_change last_price time pct_change last_price time";
       // String group = "item1 item2 item3 item4 item5 item6 item7 item8";
		String group = "item1 item2 item3 item4 item5 item6 item7 item8 item9 item10 item11 item12 item13 item14 item15 item16 item17 item18 item19 item20 item21 item22 item23 item24 item25 item26 item27 item28 item29 item30 item31 item32 item33 item34 item35 item36 item37 item38 item39 item40 item41 item42 item43 item44 item45 item46 item47 item48 item49 item50";
        table = new SimpleTableInfo(group, schema, "MERGE");
	}
	else
        table = new SimpleTableInfo(this.group, this.schema, "MERGE");
		LOG.print("this.group : "+this.group);
        table.setSnaspshotRequired(false);
        table.setRequestedMaxFrequency(1);
        if(demo)
        	table.setDataAdapter("QUOTE_ADAPTER");
        else
        	table.setDataAdapter("SNAP_QUOTE_ADAPTER");
        stockKey = lsClient.subscribeTable(table, this);
		 //lsClient.openConnection(pushLightstreamerCom, listener, policy);
        lsClient.openPollingConnection(pushLightstreamerCom, listener, policy);
        isClose = false;
	}

	public void closeConnection()
	{
		if(lsClient != null)
		{
			lsClient.closeConnection();
			isClose = true;
			//lsClient.unsubscribeTable(stockKey);
			//lsClient = null;
		}
	}
	
	public boolean isClose()
	{
		return isClose;
	}
	public void onControlError(int arg0, String arg1) {
		//LOG.print("2 onControlError works..."+System.currentTimeMillis());
	}

	public void onEndOfSnapshot(int arg0) {
		//LOG.print("2 onEndOfSnapshot works..."+System.currentTimeMillis());
	}

	public void onRawUpdatesLost(int arg0, int arg1) {
		//LOG.print("2 onRawUpdatesLost works..."+System.currentTimeMillis());
	}

	public void onUnsubscribe() {
		//LOG.print("2 onUnsubscribe works..."+System.currentTimeMillis());
	}

	public synchronized void onUpdate(final int item, final SimpleItemUpdate update) {
		LOG.print(item+" 2 onUpdate works..."+System.currentTimeMillis());
		//2streamDataUpdate.streamDataUpdate(item, update);
		//UiApplication.getUiApplication().invokeLater(new Runnable() {
			//public void run() {
		setHashtable(item, update);
			//}});
		//4setHashtable(item, update,true);
	}
	 class ConnectionListenerDispatcher implements ConnectionListener {

	        public void onBufferFull() {
	        	//LOG.print("3 onBufferFull works..."+System.currentTimeMillis());
	        }

	        public void onClientError(String error) {
	        	//LOG.print("3 onClientError works..."+System.currentTimeMillis());
	        }

	        public void onConnectionEnd(int cause) {
	        	//LOG.print("3 onConnectionEnd works..."+System.currentTimeMillis());
	        }

	        public void onServerError(int code, String error) {
	        	//LOG.print("3 onServerError works..."+System.currentTimeMillis());
	        }

	        public void onStatusChange(String newStatus) {
	        	//LOG.print("3 onStatusChange works..."+System.currentTimeMillis());
	           // if (dis != null) {
	           //     dis.onStatusChange(newStatus);
	           // }
	        }
	        
	        
	    }
	public Hashtable getHashtable() {
		return hashtable;
	}
	public synchronized void setHashtable(final int item,  final SimpleItemUpdate update) {
		//final int item = item_;
		//final SimpleItemUpdate update = update_;
		
				try {
					if(!update.getFieldNewValue(StreamSchemaGroup.LTP).toUpperCase().equalsIgnoreCase("UNCHANGED"))
					{
						StreamBean bean = new StreamBean();
						bean.setCode(item);
						bean.setLtp(update.getFieldNewValue(StreamSchemaGroup.LTP));
						bean.setPerChange(update.getFieldNewValue(StreamSchemaGroup.PERCENT_CHANGE));
						bean.setChange(update.getFieldNewValue(StreamSchemaGroup.CHANGE_VALUE));
						hashtable.put(new Integer(bean.getCode()), bean.copy());
						if(time+500<System.currentTimeMillis())
						{
							streamDataUpdate.streamDataUpdate(hashtable);
							time = System.currentTimeMillis();
						}
					}
				} catch (Exception e) {
				}
			
	}
	public synchronized void setHashtable(final int  i, final SimpleItemUpdate update, boolean f) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				String lx = update.getFieldNewValue(StreamSchemaGroup.LTP).toUpperCase();
				String item = (String)MyWatchList.streamHash.get(new Integer(i-1));
				LOG.print(item+"....LTP "+lx);
				
				if(!lx.equalsIgnoreCase("UNCHANGED")){
					LOG.print(item+"-...LTP "+lx);
				hashtable.put(item, update);
				LOG.print("..............................................");
				LOG.print(update.getFieldNewValue(StreamSchemaGroup.LTP));
				if(time+500<System.currentTimeMillis())
				{
					LOG.print("--------------------------------------------");
					streamDataUpdate.streamDataUpdate(hashtable);
					time = System.currentTimeMillis();
				}
				}
			}});
	}
	 
}
