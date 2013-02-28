package com.snapwork.components;

import java.util.Vector;

/**
 * 
 * <p>This class used in UserRegistrationScreen and it holds BlockFields eg. UserName, Password
 *
 */

public class FieldGroup 
{
	public Vector objFieldGroupVector = null;
	public String strGroupName;
	public FieldGroup(String strGroupName) 
	{
		this.strGroupName = strGroupName;
		objFieldGroupVector = new Vector();
	}

	public void add(BlockField objBlockField) 
	{
		objFieldGroupVector.addElement(objBlockField);
	}

	public BlockField[] getFields() 
	{
		BlockField[] blockFields = new BlockField[objFieldGroupVector.size()];
		for(int i=0;i<objFieldGroupVector.size();i++) 
		{
			blockFields[i] = (BlockField) objFieldGroupVector.elementAt(i);
		}
		objFieldGroupVector = null;
		return blockFields;
	}
}
