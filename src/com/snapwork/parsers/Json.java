package com.snapwork.parsers;

import java.util.Hashtable;
import java.util.Vector;

import com.snapwork.util.LOG;

public class Json
{
	private Vector objects;
	public Vector getdata;
	public Hashtable ht;
	private String string;
	public Json(String string)
	{
		//string = string.replace("null", "\"NULL\"");
		string = replaceAll(string, "null", "\"\"");
		string = replaceAll(string, "NULL", "\"\"");
		string = replaceAll(string, "Null", "\"\"");
		objects =  new Vector();
		this.string = string;
		if(this.string==null)
		{
			ht = new Hashtable();
			getdata = new Vector();
			//getdata.addElement(ht);
			//setObject(getdata);
		}
		else if(this.string.length()==0)
		{
			ht = new Hashtable();
			getdata = new Vector();
			//getdata.addElement(ht);
			//setObject(getdata);
		}
		else if(this.string.equalsIgnoreCase("[{}]")  || this.string.equalsIgnoreCase("[]"))
		{
			ht = new Hashtable();
			getdata = new Vector();
		}
		else if(this.string.indexOf('{')<0)
			{
				
				if(this.string.indexOf(':')<0)
				{

					ht = new Hashtable();
					getdata = new Vector();
					//getdata.addElement(ht);
					//setObject(getdata);
				}
				else
				{
					StringBuffer sbf = new StringBuffer(this.string);
					sbf.insert(1, '{');
					sbf.insert(sbf.length()-1, '}');
					this.string = sbf.toString();
					LOG.print(this.string);
					start();
				}
				/*else
				{
					ht = new Hashtable();
					ht.put(this.string.substring(2, this.string.indexOf(':')-1), this.string.substring(this.string.indexOf(':')+2,this.string.length()-2));
					getdata = new Vector();
					getdata.addElement(ht);
					setObject(getdata);
				}*/
			}
			else
				start();
	}
	private void start()
	{
		try
		{
			getdata = new Vector();
			StringBuffer stf = new StringBuffer();
			Vector v = new Vector();
			Vector tempv = new Vector();
			for(int i=0;i<this.string.length();i++)
			{
				if(this.string.charAt(i)=='['  )
				{
					;
				}
				else if(this.string.charAt(i)=='{') 
				{
					stf = new StringBuffer();
					stf.append('{');
				}
				else if(this.string.charAt(i)=='}')
				{
					stf.append('}');
					v.addElement(stf.toString());
				}
				else 
				{
					stf.append(this.string.charAt(i));
				}
			}
			if(!v.isEmpty())
				for(int i =0;i<v.size();i++)
				{
					ht = new Hashtable();
					tempv = process((String)v.elementAt(i));

					setObject(tempv);
					getdata.addElement(ht);

				}
		}
		catch(Exception ex)
		{ 
		}
	}

	private Vector process(String string)
	{
		StringBuffer sbf = new StringBuffer();
		StringBuffer s = new StringBuffer();
		JsonVector jVec = new JsonVector();
		sbf.append(string);
		JsonObject object = new JsonObject();
		for(int i=0;i<sbf.length();i++)
		{ 

			if(sbf.charAt(i)=='{')
			{
				i++;
				while(i<sbf.length())
				{
					if(sbf.charAt(i)=='"')
					{
						i++;
						while(i<sbf.length())
						{
							if(sbf.charAt(i)==']')
							{
								i++;
							}
							else if(sbf.charAt(i)=='{')
							{
								jVec.setObjectEmpty();
								i++;
							}
							else if(sbf.charAt(i)==':' && sbf.charAt(i+1)=='"' || sbf.charAt(i)==',' && sbf.charAt(i+1)=='"')
								i++;
							else if(sbf.charAt(i)=='}' )
							{
								i++;
								return jVec.getObject();
							}
							else if(sbf.charAt(i)=='"')
							{

								if(object.getType()==null && s.toString().length()>0){
									object.setType(s.toString());
									if(sbf.charAt(i+2)=='"' && sbf.charAt(i+3)=='"'||sbf.charAt(i+2)=='n' && sbf.charAt(i+3)=='u' && sbf.charAt(i+4)=='l' && sbf.charAt(i+5)=='l' || sbf.charAt(i+2)=='N' && sbf.charAt(i+3)=='U' && sbf.charAt(i+4)=='L' && sbf.charAt(i+5)=='L')
									{
										object.setValue("null");
										ht.put(object.getType(), "null");
										s = new StringBuffer();
										jVec.setObject(object);
										object= new JsonObject();
									}
								}
								else if(s.toString().length()>0)
								{
									object.setValue(s.toString());
									ht.put(object.getType(), s.toString());
									s = new StringBuffer();
									jVec.setObject(object);
									object= new JsonObject();
								}
								s = new StringBuffer();
								i++;
							}
							else
							{
								s.append(sbf.charAt(i));
								i++;
							}
						}
					}
				}
			}
		}
		return null;
	}
	public Vector getObject()
	{
		return this.objects;
	}
	public void setObject(Vector object)
	{
		this.objects.addElement(object);
	}
	public String replaceAll(String source, String pattern,
            String replacement) {
        if (source == null) {
            return "";
        }
       
        StringBuffer sb = new StringBuffer();
        int idx = -1;
        int patIdx = 0;

        while ((idx = source.indexOf(pattern, patIdx)) != -1) {
            sb.append(source.substring(patIdx, idx));
            sb.append(replacement);
            patIdx = idx + pattern.length();
        }
        sb.append(source.substring(patIdx));
        return sb.toString();

    }
}
