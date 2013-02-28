package com.snapwork.util;

import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class DBmanager
{
	public static RecordStore openDatabase(String recordStoreName)
	{
		try
		{
			RecordStore rs = RecordStore.openRecordStore(recordStoreName, true);
			return rs;
		}
		catch(Exception ex)
		{
		}
		return null;
	}

	public static boolean addData(String recordStoreName,byte[] data)
	{
		try
		{
			RecordStore rms = openDatabase(recordStoreName);
			rms.addRecord(data, 0, data.length);
			rms.closeRecordStore();
			return true;
		}
		catch(Exception ex)
		{
		}
		return false;
	}

	public static Vector getRecords(String recordStoreName)
	{
		Vector objVector = new Vector();
		try
		{
			RecordStore objRS = null;
			RecordEnumeration objRE = null;
			objRS = openDatabase(recordStoreName);
			objRE = objRS.enumerateRecords(null, null, true);
			while(objRE.hasNextElement())
			{
				objVector.addElement(objRE.nextRecord());
			}
			objRS.closeRecordStore();
		}
		catch(Exception ex)
		{
		}
		return objVector;
	}

	public static boolean dropDatabase(String recordStoreName)
	{
		boolean isDeleted = false;
		try
		{
			RecordStore.deleteRecordStore(recordStoreName);
			isDeleted = true;
		}
		catch (Exception ex)
		{
		}
		return isDeleted;
	}

	public static boolean deleteRecordFromRMS(String recordStoreName ,int recordId)
	{
		boolean flag = true;
		try
		{
			RecordStore recordStore = null;
			recordStore = openDatabase(recordStoreName);
			recordStore.deleteRecord(recordId);
			closeRecordStore(recordStore);
		}
		catch(Exception e)
		{
			flag = false;
		}
		return flag;
	}

	public static Vector getRecordId(String recordStoreName)
	{
		Vector tempRecordId = new Vector();
		RecordStore recordStore =null;
		RecordEnumeration re = null;
		RecordEnumeration recordData = null;
		try
		{
			recordStore = openDatabase(recordStoreName);
			re = recordStore.enumerateRecords(null,null,true);// Enumerates the records
			recordData = recordStore.enumerateRecords(null,null,true);// Enumerates the records
			while(re.hasNextElement() && recordData.hasNextElement())
			{
				tempRecordId.addElement(Integer.toString(re.nextRecordId()));
				tempRecordId.addElement(recordData.nextRecord());
			}
		}
		catch (Exception e)
		{
		}
		finally
		{
			if (re != null)
			{
				try
				{
					re.destroy();
				}
				catch (Exception e)
				{
				}
			}
			if (recordData != null)
			{
				try
				{
					recordData.destroy();
				}
				catch (Exception e)
				{
				}
			}

			// Closes the record store
			if (recordStore != null)
			{
				closeRecordStore(recordStore);
			}
		}
		return tempRecordId;
	}

	public static boolean closeRecordStore(RecordStore recordStore)
	{
		boolean flag = true;
		try
		{
			recordStore.closeRecordStore();
			recordStore = null;
		}
		catch(RecordStoreException recordStoreexception)
		{
			flag = false;
		}
		return flag;
	}
}