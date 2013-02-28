package com.snapwork.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class DBPackager
{
	public static byte[] createUserInfoPackage(String strUserID)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeUTF(strUserID);
			return baos.toByteArray();
		}
		catch(Exception ex)
		{
		}
		return null;
	}

	public static byte[] createVersionInfoPackage(String strAppVersion,long lngUpdateDateTime) {
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeUTF(strAppVersion);
			dos.writeLong(lngUpdateDateTime);
			return baos.toByteArray();
		}
		catch(Exception ex)
		{
		}
		return null;
	}

	public static byte[] createWatchListCompanyPackage(String companyID,String source,String companyName) {
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeUTF(companyID);
			dos.writeUTF(source);
			dos.writeUTF(companyName);
			return baos.toByteArray();
		}
		catch(Exception ex)
		{
		}
		return null;
	}

	public static byte[] createNotificationPackage(String source)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeUTF(source);
			return baos.toByteArray();
		}
		catch(Exception ex)
		{
		}
		return null;
	}
	public static byte[] convertTOByte(int source) {
    	try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(source);
            return baos.toByteArray();
    	} catch(Exception ex) {
            //System.out.println("createWatchListCompanyPackage,Error : "+ex.toString());        		
    	}
        return null;
    }
	
	public static byte[] convertTOByte2(int source, int index) {
    	try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(source);
            dos.writeInt(index);
            return baos.toByteArray();
    	} catch(Exception ex) {
            //System.out.println("createWatchListCompanyPackage,Error : "+ex.toString());        		
    	}
        return null;
    }
    
}