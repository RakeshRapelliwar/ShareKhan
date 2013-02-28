package com.snapwork.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import com.snapwork.beans.ChartItem;
import com.snapwork.beans.ChartItemHeading;
import com.snapwork.components.Screen;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;

public class ChartItemParser implements HttpResponse
{
	public String feedurl = "";
	public Vector chartData = null;
	final ChartItem cI = new ChartItem();
	final ChartItemHeading cIh = new ChartItemHeading();
	Screen screen = null;
	ThreadedComponents threadedComponents = null;
	private byte type=0;
	private ReturnDataWithId returnStringParser = null;
	private int id;
	public ChartItemParser(String url,Screen screen)
	{
		this.feedurl = url;
		chartData = new Vector();
		this.screen = screen;
	}
	public ChartItemParser(String url, ReturnDataWithId screen, int id)
	{
		this.feedurl = url;
		chartData = new Vector();
		this.returnStringParser = screen;
		this.id = id;
	}

	public ChartItemParser(String url,Screen screen,ThreadedComponents threadedComponents, byte type)
	{
		this.feedurl = url;
		chartData = new Vector();
		this.screen = screen;
		this.threadedComponents = threadedComponents;
		this.type = type;
	}

	public void getScreenData()
	{
		LOG.print("Chart Url "+feedurl);
		HttpProcess.threadedHttpConnection(feedurl, this);
	}

	public void setResponse(String rsponse)
	{
	if(rsponse==null)
	{
		if(returnStringParser==null)
			screen.setData(chartData,threadedComponents);
		else
			returnStringParser.setData(chartData, id);
		}
	else if(rsponse.length()==0)
	{
		if(returnStringParser==null)
		screen.setData(chartData,threadedComponents);
		else
			returnStringParser.setData(chartData, id);
	}
		else
	{
		KXmlParser parser = null;
		ByteArrayInputStream byteArrayInputStream = null;
		InputStreamReader is = null;
		byte[] currentXMLBytes;
		double midValue=-1;
		double maxIntradayYAxisValue = 0,minIntradayYAxisValue = -999999999;
		try
		{
			currentXMLBytes = rsponse.getBytes();
			byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes);
			is = new InputStreamReader(byteArrayInputStream);
			parser = new KXmlParser();
			parser.setInput(is);
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "fD");
			if(type==AppConstants.INTRADAY)
			{
				try
				{
					for(byte i=0;i<parser.getAttributeCount();i++)
					{
						if(parser.getAttributeName(i).equals("pC"))
						{
							midValue = Double.parseDouble(parser.getAttributeValue(i));
						}
					}
				}
				catch(Exception ex)
				{
					midValue  = -1;
				}
			}
			while(parser.nextTag() != XmlPullParser.END_TAG)
			{
				parser.require(XmlPullParser.START_TAG, null, null);
				String tagname2 = parser.getName().trim();
				if(tagname2.equals("tD"))
				{
					if(type==AppConstants.INTRADAY)
					{
						if(parser.getAttributeValue(0).length()>5)
							cI.setTime(parser.getAttributeValue(0).substring(0, parser.getAttributeValue(0).length()-3));
						else
							cI.setTime(parser.getAttributeValue(0));
						cI.setValue(Double.parseDouble(parser.nextText()));

						if(cI.getValue()>maxIntradayYAxisValue)
						{
							maxIntradayYAxisValue = cI.getValue();
						}

						if(cI.getValue() < minIntradayYAxisValue && cI.getValue()!=0)
						{
							minIntradayYAxisValue = cI.getValue();
						}
						else
						{
							if(minIntradayYAxisValue == -999999999 && cI.getValue()!=0)
							{
								minIntradayYAxisValue = cI.getValue();
							}
						}
					}
					else if(type==AppConstants.WEEK)
					{
						Date d = new Date(Long.parseLong(parser.getAttributeValue(0)));
						Calendar cl = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));  
						cl.setTime(d);
						String dt = "";
						int tm = cl.get(Calendar.DAY_OF_MONTH);
						int m = cl.get(Calendar.MONTH);
						switch(m)
						{
						case 0:dt="Jan";break;
						case 1:dt="Feb";break;
						case 2:dt="Mar";break;
						case 3:dt="Apr";break;
						case 4:dt="May";break;
						case 5:dt="Jun";break;
						case 6:dt="Jul";break;
						case 7:dt="Aug";break;
						case 8:dt="Sep";break;
						case 9:dt="Oct";break;
						case 10:dt="Nov";break;
						case 11:dt="dec";break;
						default:dt="Jan";
						}
						dt = Integer.toString(tm) + dt;
						cI.setTime(dt);
						cI.setValue(Double.parseDouble(parser.nextText()));
						if(cI.getValue()>maxIntradayYAxisValue)
						{
							maxIntradayYAxisValue = cI.getValue();
						}
						if(cI.getValue() < minIntradayYAxisValue && cI.getValue()!=0)
						{
							minIntradayYAxisValue = cI.getValue();
						}
						else
						{
							if(minIntradayYAxisValue == -999999999 && cI.getValue()!=0)
							{
								minIntradayYAxisValue = cI.getValue();
							}
						}
					}
					else if(type==AppConstants.MONTH || type==AppConstants.MONTH6 || type==AppConstants.YEAR)
					{
						Date d = new Date(Long.parseLong(parser.getAttributeValue(0)));
						Calendar cl = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));  
						cl.setTime(d);
						String dt = "";
						int tm = cl.get(Calendar.DAY_OF_MONTH);
						int m = cl.get(Calendar.MONTH);
						switch(m)
						{
						case 0:dt="Jan";break;
						case 1:dt="Feb";break;
						case 2:dt="Mar";break;
						case 3:dt="Apr";break;
						case 4:dt="May";break;
						case 5:dt="Jun";break;
						case 6:dt="Jul";break;
						case 7:dt="Aug";break;
						case 8:dt="Sep";break;
						case 9:dt="Oct";break;
						case 10:dt="Nov";break;
						case 11:dt="dec";break;
						default:dt="Jan";
						}
						dt = Integer.toString(tm) + dt;
						cI.setTime(dt);
						cI.setValue(Double.parseDouble(parser.nextText()));
						if(cI.getValue()>maxIntradayYAxisValue)
						{
							maxIntradayYAxisValue = cI.getValue();
						}
						if(cI.getValue() < minIntradayYAxisValue && cI.getValue()!=0)
						{
							minIntradayYAxisValue = cI.getValue();
						}
						else
						{
							if(minIntradayYAxisValue == -999999999 && cI.getValue()!=0)
							{
								minIntradayYAxisValue = cI.getValue();
							}
						}
					}
				}
				if(cI.getValue()!=0.0)
				{
					chartData.addElement(cI.copy());	
				}
			}               
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		LOG.print("minIntradayYAxisValue : "+minIntradayYAxisValue);
		LOG.print("maxIntradayYAxisValue : "+maxIntradayYAxisValue);
		//LOG.print("chartData.size() "+chartData.size());
		if(chartData.size()!=0)
		{
			chartData.addElement(new Double(minIntradayYAxisValue));
			chartData.addElement(new Double(maxIntradayYAxisValue));
			chartData.addElement(new Double(midValue));
		}
		if(returnStringParser==null)
		screen.setData(chartData,threadedComponents);
		else
			returnStringParser.setData(chartData, id);
		}
	}
	public void exception(Exception ex)
	{
	}
	public void setResponse(Image img, int id)
	{
	}
	public void setResponse(Image img)
	{
	}
	public void setResponse(Image image, String name)
	{
	}
}