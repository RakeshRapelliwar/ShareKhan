package com.snapwork.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.ContentConnection;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.main.BseNseStocksLive;
import com.snapwork.util.HttpConnectionFactory;
import com.snapwork.view.InternetExitScreen;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.collection.util.SortedReadableList;
import net.rim.device.api.compress.GZIPInputStream;
import net.rim.device.api.crypto.DigestOutputStream;
import net.rim.device.api.crypto.MD5Digest;
import net.rim.device.api.io.http.HttpHeaders;
import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.util.StringComparator;

public class HttpProcess
{
	public static HttpConnection getConnection(String url)
	{
		try
		{
			//url = AppendConnectionUids.getInstance()._GetUrlWithParameters(url);
			return (HttpConnection) Connector.open(HTTPGetConnection.getURL(url));
		}
		catch(Exception ex)
		{
			System.out.println("Error : "+ex.toString());
		}
		return null;
	}

	/* Sample code for redirect
	 * int resCode;
	String location = "http://company.com/temp1.aspx";
	while (true) {  
     HttpConnection connection = (HttpConnection) Connector.open(location + ";deviceside=true");
     connection.setRequestMethod(HttpConnection.GET);
     connection.setRequestProperty(HttpHeaders.HEADER_CONNECTION, "close");
     connection.setRequestProperty(HttpHeaders.HEADER_CONTENT_LENGTH, "0");
     resCode = connection.getResponseCode();
     if( resCode == HttpConnection.HTTP_TEMP_REDIRECT
          || resCode == HttpConnection.HTTP_MOVED_TEMP
          || resCode == HttpConnection.HTTP_MOVED_PERM ) {
          location = connection.getHeaderField("location").trim();
     } else {
          resCode = connection.getResponseCode();
          break;
     }
}
	 */

	private static String checkTransportConnection(String url)
	{
		try
		{
			//HttpConnectionFactory factory;// = new HttpConnectionFactory(url,HttpConnectionFactory.TRANSPORTS_ANY);
			while(true)
			{
				//TransportDetective td = new TransportDetective();
				//URLFactory urlFactory = new URLFactory(url);
				//url = urlFactory.getHttpTcpCellularUrl(td.getDefaultTcpCellularServiceRecord().getAPN(), "", "");
				//factory = new HttpConnectionFactory(url,HttpConnectionFactory.TRANSPORTS_ANY);
				HttpConnection c = null;//factory.getNextConnection();
				c = (HttpConnection) Connector.open(HTTPGetConnection.getURL(url));
				
				//c.setRequestProperty("Content-Type", "gzip; charset=utf-8");
				c.setRequestProperty("Accept-Encoding", "gzip, deflate");

				String msg = "";
				ByteArrayOutputStream os = null;
				DataInputStream is = null;
				try
				{     c.setRequestProperty(HttpHeaders.HEADER_CONNECTION, "close");
			     c.setRequestProperty(HttpHeaders.HEADER_CONTENT_LENGTH, "0");

					int responseCode = c.getResponseCode();
					byte[] data = null;
					/////////////////////
					LOG.print("URL Response Code : "+responseCode);
					//ShareKhan Redirect code added
					if( responseCode == HttpConnection.HTTP_TEMP_REDIRECT
							|| responseCode == HttpConnection.HTTP_MOVED_TEMP
							|| responseCode == HttpConnection.HTTP_MOVED_PERM ) {
						url = c.getHeaderField("location").trim();
						LOG.print("Redirect url location : "+url);

		                if(url.indexOf("&debug=2")<0)
		                	url = url + "&debug=2";
		                LOG.print("Redirect url location : "+url);

						continue;
					} 
					/////////////////////
					else if(responseCode == HttpConnection.HTTP_OK)
					{
						if ("gzip".equals(c.getEncoding())){

							//LOG.print("gzip - encoded"+c.getResponseMessage());
							//LOG.print("-----"+c.getResponseMessage());

							/*StringBuffer rawResponse = new StringBuffer();
							InputStream inputStream = c.openInputStream();//some input stream
								GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

											while (-1 != (length = gzipInputStream.read(responseData))) {
												rawResponse.append(new String(responseData, 0, length));

											}*/
							try
							{
								InputStream inputStream = c.openDataInputStream();
								GZIPInputStream gzis = new GZIPInputStream(inputStream);
								StringBuffer sb = new StringBuffer();

								char ch;
								// while ((ch = (char)gzis.read()) != -1)
								while ((ch = (char)gzis.read()) != -1 && ch != 65535)
								{
									sb.append(ch);
								}

								msg = sb.toString();

								gzis.close();
							}
							catch(IOException ioe)
							{

							}

							//////////
							/*int length = (int) c.getLength();
							is = c.openDataInputStream();
							if (length != -1)
							{
								data = new byte[length];
								is.readFully(data);
							}
							else
							{  // Length not available...
								os = new ByteArrayOutputStream();
							int ch;
							while ((ch = is.read()) != -1)
								os.write(ch);
							data = os.toByteArray();
							}
							////////////*/

							/*   ByteArrayOutputStream buffer = new ByteArrayOutputStream();
						    ByteArrayInputStream in = new ByteArrayInputStream(data);
						    try {
						      InputStream inflater = new GZIPInputStream(in);
						      byte[] bbuf = new byte[256];
						      while (true) {
						        int r = inflater.read(bbuf);
						        if (r < 0) {
						          break;
						        }
						        buffer.write(bbuf, 0, r);
						      }
						    } catch (IOException e) {
						    	msg = "-3";
								is=null;
								BseNseStocksLive.closeApp();
								return msg.trim();
						    }


						    //Charset charset =   Charset.forName("UTF-8");
						   msg = new String(buffer.toByteArray(),"UTF-8");*/
						}
						else{
							int length = (int) c.getLength();
							is = c.openDataInputStream();
							if (length != -1)
							{
								data = new byte[length];
								is.readFully(data);
							}
							else
							{  // Length not available...
								os = new ByteArrayOutputStream();
								int ch;
								while ((ch = is.read()) != -1)
									os.write(ch);
								data = os.toByteArray();
								os.close();
							}

							if(data!=null)
								msg = new String(data);
							else 
								msg = null;


							if (is != null)
							{
								is.close();
								Debug.debug("Input STream CLosed");
							}
							if (os != null)
							{
								os.close();
								Debug.debug("Output STream CLosed");
							}

						}
						if (c != null)
						{
							c.close();
							Debug.debug("Connection CLosed");
						}
						//LOG.print("msg"+msg);
					}

				}
				catch (SecurityException se)
				{
					msg = null;
					is=null;
					BseNseStocksLive.closeApp();
					return msg;
				} 
				catch (IOException ioe)
				{
					msg = null;
					is=null;
					BseNseStocksLive.closeApp();
					return msg;
				}
				catch (Exception e)
				{
					msg = null;
					is=null;
					BseNseStocksLive.closeApp();
					return msg;
				} 
				finally
				{
					c = null;
					os = null;
					is = null;
				}
				if(msg==null)
				{
					return msg;
				}
				else
				{
					//s
					boolean sessionFlag = true;
					KXmlParser parser = null;
					ByteArrayInputStream byteArrayInputStream = null;
					InputStreamReader iss = null;
					byte[] currentXMLBytes;
					try
					{
						currentXMLBytes = msg.getBytes();
						byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes);
						iss = new InputStreamReader(byteArrayInputStream);
						parser = new KXmlParser();
						parser.setInput(iss);
						parser.nextTag();
						parser.require(XmlPullParser.START_TAG, null, "root");
						while(parser.nextTag() != XmlPullParser.END_TAG)
						{
							parser.require(XmlPullParser.START_TAG, null, null);
							String tagname = parser.getName().trim();
							if(tagname.equals("response"))
							{LOG.print(" response ");
								String session = parser.nextText();
								if(session.equalsIgnoreCase("app:login"))
								{
									sessionFlag = false;
									Utils.sessionAlive = false;
								}
							}
							else
								parser.nextText();
							parser.require(XmlPullParser.END_TAG, null, tagname);
						}
						parser.require(XmlPullParser.END_TAG, null, "root");
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
					if(!sessionFlag)
					{
						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
						return null;
					}
					else
						return msg.trim();                  
				}
			}                           
		}
		catch(Exception ex)
		{
			System.out.println("Exception occured");
		}
		return "";
	}

	public static String getHttpConnection(String url)
	{
		return checkTransportConnection(url);
	}
	
	////HTTPS secure connection start
	public static String getHttpsMD5Connection(String url)
	{
		return checkTransportHttpsMD5Connection(url);
	}
	private static String checkTransportHttpsMD5Connection(String url)
	{
		try
		{
			URLEncodedPostData oPostData = new URLEncodedPostData(URLEncodedPostData.DEFAULT_CHARSET, false);
			//oPostData.
			StringBuffer convert = new StringBuffer();
			String temp = "";
			String securekey = "";
			Vector v = new Vector();
			Hashtable hash = new Hashtable();
			boolean flag = true;
			if(url.indexOf("?")>-1)
			{
				convert.append(url.substring(url.indexOf("?")+1, url.length()));
				temp = convert.toString();
				url = "https"+url.substring(4, url.indexOf("?"));
				LOG.print(url);
			}
			//System.out.println(convert);
			//else
			while(true)
			{
				if(temp.indexOf("&")>-1)
				{
					LOG.print(temp.substring(0, temp.indexOf("&")));
					String data = temp.substring(0, temp.indexOf("&"));
					if(data.indexOf("RequestId=")<0)
						securekey = securekey + data;
					v.addElement(data.substring(0, temp.indexOf("=")));
					if(data.indexOf("=")==data.length()-1)
					{
						v.addElement("");
						LOG.print("temp.indexOf(\"=\")==temp.length()-1");
						LOG.print("temp.indexOf(\"=\") = "+data.indexOf("="));
						LOG.print("temp.length() = "+data.length()+"\n---------------------------------");
					}
					else
					{
						v.addElement(data.substring(temp.indexOf("=")+1, data.length()));
						LOG.print("temp.indexOf(\"=\") = "+data.indexOf("="));
						LOG.print("temp.length() = "+data.length()+"\n---------------------------------");
					}
					//hash.put((String)data.substring(0, temp.indexOf("=")), (String)data.substring(temp.indexOf("=")+1, data.length()));
					convert.delete(0, temp.indexOf("&")+1);
					temp = convert.toString();
				}
				else
				{
					LOG.print(temp);
					String data = temp;
					securekey = securekey + data;
					v.addElement(data.substring(0, temp.indexOf("=")));
					v.addElement(data.substring(temp.indexOf("=")+1, data.length()));
					//hash.put((String)data.substring(0, temp.indexOf("=")), (String)data.substring(temp.indexOf("=")+1, data.length()));
					break;
				}
					
			}
			securekey = securekey+"key=S!N@A#P$W%O^R&K*T!R@A#D$E"; 
			
			LOG.print("\\/"+securekey);
			//hash.put("key", "S!N@A#P$W%O^R&K*T!R@A#D$E");
			//securekey = getSignature(hash, "key=S!N@A#P$W%O^R&K*T!R@A#D$E");
			securekey = getMd5Hash(securekey);
			LOG.print(">"+securekey);
			for(int i=0; i<v.size()-1;i++)
			{
				LOG.print(v.elementAt(i)+",");
				oPostData.append((String)v.elementAt(i), (String)v.elementAt(++i));
				if(i==v.size()-1)
					oPostData.append("key", securekey);
			}
			
			 boolean redirect = false;
			HttpsMD5ConnectionFactory factory;// = new HttpConnectionFactory(url,HttpConnectionFactory.TRANSPORTS_ANY);
			while(true)
			{
				if(redirect)
				{
					
					redirect = false;
					oPostData = new URLEncodedPostData(URLEncodedPostData.DEFAULT_CHARSET, false);
					convert = new StringBuffer();
					securekey = "";
					v = new Vector();
					temp = "";
					if(url.indexOf("?")>-1)
					{
						convert.append(url.substring(url.indexOf("?")+1, url.length()));
						temp = convert.toString();
						url = url.substring(0, url.indexOf("?"));
						LOG.print(url);
					}
					while(true)
					{
						
						if(temp.indexOf("&")>-1)
						{
							LOG.print(temp.substring(0, temp.indexOf("&")));
							String data = temp.substring(0, temp.indexOf("&"));
							String skey = data.substring(0, temp.indexOf("="));
							LOG.print("----------->>>>>>>>> "+skey);
							if(!skey.equals("key"))
							{
								if(data.indexOf("RequestId=")<0)
								 securekey = securekey + data;
								
								v.addElement(data.substring(0, temp.indexOf("=")));
								
								if(data.indexOf("=")==data.length()-1)
							{
								v.addElement("");
								LOG.print("temp.indexOf(\"=\")==temp.length()-1");
								LOG.print("temp.indexOf(\"=\") = "+data.indexOf("="));
								LOG.print("temp.length() = "+data.length()+"\n---------------------------------");
							}
							else
							{
								v.addElement(data.substring(temp.indexOf("=")+1, data.length()));
								LOG.print("temp.indexOf(\"=\") = "+data.indexOf("="));
								LOG.print("temp.length() = "+data.length()+"\n---------------------------------");
							}
						}
							else
								LOG.print("<<<<<<----------->>>>>>>>> "+skey+"not added ");
							//hash.put((String)data.substring(0, temp.indexOf("=")), (String)data.substring(temp.indexOf("=")+1, data.length()));
							convert.delete(0, temp.indexOf("&")+1);
							temp = convert.toString();
						}
						else
						{
							LOG.print(temp);
							String data = temp;
							securekey = securekey + data;
							String skey = data.substring(0, temp.indexOf("="));
							if(!skey.equals("key"))
							{
								v.addElement(data.substring(0, temp.indexOf("=")));
								v.addElement(data.substring(temp.indexOf("=")+1, data.length()));
							}
							//hash.put((String)data.substring(0, temp.indexOf("=")), (String)data.substring(temp.indexOf("=")+1, data.length()));
							break;
						}
							
					}
					
					securekey = securekey+"key=S!N@A#P$W%O^R&K*T!R@A#D$E"; 
					
					LOG.print("\\/"+securekey);
					//hash.put("key", "S!N@A#P$W%O^R&K*T!R@A#D$E");
					//securekey = getSignature(hash, "key=S!N@A#P$W%O^R&K*T!R@A#D$E");
					securekey = getMd5Hash(securekey);
					LOG.print(">"+securekey);
					for(int i=0; i<v.size()-1;i++)
					{
						LOG.print(v.elementAt(i)+",");
						oPostData.append((String)v.elementAt(i), (String)v.elementAt(++i));
						if(i==v.size()-1)
							{
								oPostData.append("key", securekey);
							}
					}
				}
				//	factory = new HttpsMD5ConnectionFactory(url,HttpConnectionFactory.TRANSPORTS_ANY);
				//HttpsConnection c = factory.getNextConnection();
				HttpConnection c = null;//factory.getNextConnection();
				c = (HttpConnection) Connector.open(HTTPGetConnection.getURL(url));
			c.setRequestMethod(HttpConnection.POST); 
				    c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				
				   // c.setRequestProperty("User-Agent", "BlackBerry/3.2.1");
				    //c.setRequestProperty("Content-Language", "en-US");
				    c.setRequestProperty("Connection", "close");
				    c.setRequestProperty("Content-Length", Integer.toString(oPostData.size()));
			 
				c.setRequestProperty("Accept-Encoding", "gzip, deflate");
				String msg = "";
				ByteArrayOutputStream os = null;
				DataInputStream is = null;
				try
				{     c.setRequestProperty(HttpHeaders.HEADER_CONNECTION, "close");
			     c.setRequestProperty(HttpHeaders.HEADER_CONTENT_LENGTH, "0");
			     OutputStream out = c.openOutputStream();
			     out.write(oPostData.getBytes());
			     out.flush();
			     out.close();
					
					int responseCode = c.getResponseCode();
					byte[] data = null;
					/////////////////////
					LOG.print("URL Response Code : "+responseCode);
					//ShareKhan Redirect code added
					if( responseCode == HttpConnection.HTTP_TEMP_REDIRECT
							|| responseCode == HttpConnection.HTTP_MOVED_TEMP
							|| responseCode == HttpConnection.HTTP_MOVED_PERM ) {
						url = c.getHeaderField("location").trim();
						LOG.print("Redirect url location : "+url);

		                if(url.indexOf("&debug=2")<0)
		                	url = url + "&debug=2";
							redirect = true;
						LOG.print("Redirect url location : "+url);

						continue;
					} 
					/////////////////////
					else if(responseCode == HttpConnection.HTTP_OK)
					{
						if ("gzip".equals(c.getEncoding())){

							//LOG.print("gzip - encoded"+c.getResponseMessage());
							//LOG.print("-----"+c.getResponseMessage());

							/*StringBuffer rawResponse = new StringBuffer();
							InputStream inputStream = c.openInputStream();//some input stream
								GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

											while (-1 != (length = gzipInputStream.read(responseData))) {
												rawResponse.append(new String(responseData, 0, length));

											}*/
							try
							{
								InputStream inputStream = c.openDataInputStream();
								GZIPInputStream gzis = new GZIPInputStream(inputStream);
								StringBuffer sb = new StringBuffer();

								char ch;
								// while ((ch = (char)gzis.read()) != -1)
								while ((ch = (char)gzis.read()) != -1 && ch != 65535)
								{
									sb.append(ch);
								}

								msg = sb.toString();

								gzis.close();
							}
							catch(IOException ioe)
							{

							}

							//////////
							/*int length = (int) c.getLength();
							is = c.openDataInputStream();
							if (length != -1)
							{
								data = new byte[length];
								is.readFully(data);
							}
							else
							{  // Length not available...
								os = new ByteArrayOutputStream();
							int ch;
							while ((ch = is.read()) != -1)
								os.write(ch);
							data = os.toByteArray();
							}
							////////////*/

							/*   ByteArrayOutputStream buffer = new ByteArrayOutputStream();
						    ByteArrayInputStream in = new ByteArrayInputStream(data);
						    try {
						      InputStream inflater = new GZIPInputStream(in);
						      byte[] bbuf = new byte[256];
						      while (true) {
						        int r = inflater.read(bbuf);
						        if (r < 0) {
						          break;
						        }
						        buffer.write(bbuf, 0, r);
						      }
						    } catch (IOException e) {
						    	msg = "-3";
								is=null;
								BseNseStocksLive.closeApp();
								return msg.trim();
						    }


						    //Charset charset =   Charset.forName("UTF-8");
						   msg = new String(buffer.toByteArray(),"UTF-8");*/
						}
						else{
							int length = (int) c.getLength();
							is = c.openDataInputStream();
							if (length != -1)
							{
								data = new byte[length];
								is.readFully(data);
							}
							else
							{  // Length not available...
								os = new ByteArrayOutputStream();
								int ch;
								while ((ch = is.read()) != -1)
									os.write(ch);
								data = os.toByteArray();
								os.close();
							}

							if(data!=null)
								msg = new String(data);
							else 
								msg = null;


							if (is != null)
							{
								is.close();
								Debug.debug("Input STream CLosed");
							}
							if (os != null)
							{
								os.close();
								Debug.debug("Output STream CLosed");
							}

						}
						if (c != null)
						{
							c.close();
							Debug.debug("Connection CLosed");
						}
						//LOG.print("msg"+msg);
					}

				}
				catch (SecurityException se)
				{
					msg = "-3";
					is=null;
					BseNseStocksLive.closeApp();
					return msg.trim();
				} 
				catch (IOException ioe)
				{
					msg = "-2";
					is = null;
					BseNseStocksLive.closeApp();
					return msg.trim();
				}
				catch (Exception e)
				{
					msg = "-1";
					is = null;
					BseNseStocksLive.closeApp();
					return msg.trim();
				} 
				finally
				{
					c = null;
					os = null;
					is = null;
				}
				if(msg==null)
				{
					return msg;
				}
				else
				{
					/*//s
					boolean sessionFlag = true;
					KXmlParser parser = null;
					ByteArrayInputStream byteArrayInputStream = null;
					InputStreamReader iss = null;
					byte[] currentXMLBytes;*/
					try
					{
						/*currentXMLBytes = msg.getBytes();
						byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes);
						iss = new InputStreamReader(byteArrayInputStream);
						parser = new KXmlParser();
						parser.setInput(iss);
						parser.nextTag();
						parser.require(XmlPullParser.START_TAG, null, "root");
						while(parser.nextTag() != XmlPullParser.END_TAG)
						{
							parser.require(XmlPullParser.START_TAG, null, null);
							String tagname = parser.getName().trim();
							if(tagname.equals("response"))
							{LOG.print(" response ");
								String session = parser.nextText();
								if(session.equalsIgnoreCase("app:login"))
								{
									sessionFlag = false;
									Utils.sessionAlive = false;
								}
							}
							else
								parser.nextText();
							parser.require(XmlPullParser.END_TAG, null, tagname);
						}
						parser.require(XmlPullParser.END_TAG, null, "root");
						*/
						
						if(msg.indexOf("app:login")>-1 || msg.indexOf("app://login")>-1)
						{
							//sessionFlag = false;
							Utils.sessionAlive = false;
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
							return null;
						}
						else
						{
							return msg.trim();
						}
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
					/*if(!sessionFlag)
					{
						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
						return null;
					}
					else
						return msg.trim();     */             
				}
			}                           
		}
		catch(Exception ex)
		{
			System.out.println("Exception occured");
		}
		return "";
	}
	////HTTPS secure connection end
	////adasdasdasdasd
	public void post()
	{
	URLEncodedPostData postData = new URLEncodedPostData(URLEncodedPostData.DEFAULT_CHARSET, false);
	//passing q’s value and ie’s value
	postData.append("UserName","your username");
	postData.append("Password", "your password");

	ConnectionFactory conFactory = new ConnectionFactory();
	ConnectionDescriptor conDesc = null;
	try{
	conDesc = conFactory.getConnection("https://www.pmamsmartselect.com/PMAMSS-Webservices/MobileService.asmx/LoginCheck;deviceside=true");
	}catch(Exception e){
	System.out.println(e.toString()+":"+e.getMessage());
	}
	String response = ""; // this variable used for the server response
	// if we can get the connection descriptor from ConnectionFactory
	if(null != conDesc){
	try{
	HttpConnection connection = (HttpConnection)conDesc.getConnection();
	//set the header property
	connection.setRequestMethod(HttpConnection.POST);
	connection.setRequestProperty("Content-Length", Integer.toString(postData.size())); //body content of post data
	connection.setRequestProperty("Connection", "close"); // close the connection after success sending request and receiving response from the server
	connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // we set the content of this request as application/x-www-form-urlencoded, because the post data is encoded as form-urlencoded(if you print the post data string, it will be like this -> q=remoQte&ie=UTF-8).

	//now it is time to write the post data into OutputStream
	OutputStream out = connection.openOutputStream();
	out.write(postData.getBytes());
	out.flush();
	out.close();

	int responseCode = connection.getResponseCode(); //when this code is called, the post data request will be send to server, and after that we can read the response from the server if the response code is 200 (HTTP OK).
	if(responseCode == HttpConnection.HTTP_OK){
	//read the response from the server, if the response is ascii character, you can use this following code, otherwise, you must use array of byte instead of String
	InputStream in = connection.openInputStream();
	StringBuffer buf = new StringBuffer();
	int read = -1;
	while((read = in.read())!= -1)
	buf.append((char)read);
	response = buf.toString();
	}
	//Dialog.inform(response);
	//don’t forget to close the connection
	connection.close();

	}catch(Exception e){
	System.out.println(e.toString()+":"+e.getMessage());
	}
	}
	}
	////adasdasdasdasd

	public static boolean checkHttpConnection()
	{
		try
		{
			String url = AppConstants.checkConnectionUrl;
			
			//HttpConnectionFactory factory = new HttpConnectionFactory(url,HttpConnectionFactory.TRANSPORTS_ANY);
			while(true)
			{
				//HttpConnection c = factory.getNextConnection();
				LOG.print("http check start...!");
				//TransportDetective td = new TransportDetective();
			//	URLFactory urlFactory = new URLFactory(url);
			//	  url = urlFactory.getHttpTcpCellularUrl(td.getDefaultTcpCellularServiceRecord().getAPN(), "", "");
			
				HttpConnection c = (HttpConnection) Connector.open(HTTPGetConnection.getURL(url));
				LOG.print("http after initialisation...!");
				try
				{
					c.setRequestMethod(HttpConnection.GET);
					//c.setRequestProperty("Content-Type", "gzip; charset=utf-8");
					c.setRequestProperty("Accept-Encoding", "gzip, deflate");
					int responseCode = c.getResponseCode();
					if (c != null)
					{
						c.close();
						Debug.debug("Connection Closed ");
					}
					if(responseCode==200)
					{
						return true;
					}
					else
					{
						UiApplication.getUiApplication().invokeAndWait(new Runnable() {
    						public void run() {
    							Dialog.alert("No Network Connection!");  
    						}
    					});
					}
				}
				catch (SecurityException se)
				{
					LOG.print("Check connection,Security issue : "+se.toString()+" (message)"+se.getMessage());
					UiApplication.getUiApplication().invokeAndWait(new Runnable() {
						public void run() {
							Dialog.alert("No Network Connection!");  
						}
					});
				} 
				catch (IOException ioe)
				{
					LOG.print("Check connection,IO issue : "+ioe.toString()+" (message)"+ioe.getMessage());
					UiApplication.getUiApplication().invokeAndWait(new Runnable() {
						public void run() {
							Dialog.alert("No Network Connection!");  
						}
					});
				}
				catch (Exception e)
				{
					LOG.print("Check connection,issue : "+e.toString()+" (message)"+e.getMessage());
					UiApplication.getUiApplication().invokeAndWait(new Runnable() {
						public void run() {
							Dialog.alert("No Network Connection!");  
						}
					});
				} 
				finally
				{
					c = null;
				}    
			}
		}
		catch(Exception ex)
		{
			Debug.debug("Exception occured : "+ex.toString());
		}
		return false;
	}

	public static Bitmap downloadImage(String url) throws IOException
	{
		ContentConnection connection = null;
		connection = (ContentConnection) Connector.open(AppendConnectionUids.getInstance()._GetUrlWithParameters(url));
		DataInputStream iStrm = connection.openDataInputStream();
		ByteArrayOutputStream bStrm = null;
		Bitmap objBMP = null;
		try
		{
			byte imageData[];
			int length = (int) connection.getLength();
			if (length != -1)
			{
				imageData = new byte[length];
				iStrm.readFully(imageData);
			}
			else
			{  // Length not available...
				bStrm = new ByteArrayOutputStream();
				int ch;
				while ((ch = iStrm.read()) != -1)
					bStrm.write(ch);
				imageData = bStrm.toByteArray();
				bStrm.close();
			}
			// Create the image from the byte array
			objBMP = Bitmap.createBitmapFromBytes(imageData, 0, -1,1);
		}
		catch(Exception ex)
		{
			System.out.println("downloadImage,Error : "+ex.toString());
		}
		finally
		{
			// Clean up
			if (iStrm != null)
				iStrm.close();
			iStrm = null;
			if (connection != null)
				connection.close();
			connection = null;
			if (bStrm != null)
				bStrm.close();
			bStrm = null;
		}
		return (objBMP);
	}

	public static void threadedHttpConnection(final String url,final HttpResponse httpResponse)
	{
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				String response = getHttpConnection(url);
				httpResponse.setResponse(response);
			}
		});
		t.start();
	}
	
	public static void threadedHttpsMD5Connection(final String url,final HttpResponse httpResponse)
	{
		
		
		
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				String response = getHttpsMD5Connection(url);
				httpResponse.setResponse(response);
			}
		});
		t.start();
	}
	
	
	public static String getMd5Hash(String input) 
	{
	        // length here is 16, I tried forcing it to 32, only first 16 chars are hashed, rest are zero filled.
			try {
				MD5Digest digest = new MD5Digest();
				//digest.update(requestString.toString().getBytes("iso-8859-1"), 0, requestString.length());
				digest.update(input.getBytes(), 0, input.length());
				byte[] digestResult = digest.getDigest();
				
				return convertToHex(digestResult);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;
	    }
	
	
	
	private static String getSignature(Hashtable arguments, String secret) {
		try {
			SortedReadableList keysList = new SortedReadableList(StringComparator.getInstance(true));
			keysList.loadFrom(arguments.keys());
			keysList.sort();
			StringBuffer requestString = new StringBuffer();
			
			for (int i = 0; i < keysList.size(); i ++) {
				String key = (String)keysList.getAt(i);
				String val = (String)arguments.get(key);
				requestString.append(key + "=" + val);
			}
			requestString.append(secret);
			LOG.print(requestString.toString());
			MD5Digest digest = new MD5Digest();
			//digest.update(requestString.toString().getBytes("iso-8859-1"), 0, requestString.length());
			digest.update(requestString.toString().getBytes(), 0, requestString.length());
			byte[] digestResult = digest.getDigest();
			
			return convertToHex(digestResult);
		} catch (Exception e) {
			return null;
		}
	}
	
	private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        
        return buf.toString();
    }
}