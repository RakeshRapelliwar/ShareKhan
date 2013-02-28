package com.snapwork.parsers;

import java.util.Vector;

public class JsonVector
{
	private Vector Object = new Vector();

	public JsonVector()
	{
	}
	public Vector getObject()
	{
		return Object;
	}
	public void setObject(JsonObject object)
	{
		Object.addElement(object);
	}
	public void setObjectEmpty()
	{
		Object.removeAllElements();
	}
}
