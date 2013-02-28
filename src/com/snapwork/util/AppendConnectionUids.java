package com.snapwork.util;

import net.rim.blackberry.api.mail.ServiceConfiguration;
import net.rim.blackberry.api.mail.Session;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.WLANInfo;

public class AppendConnectionUids
{
	private static final String WIFI = ";interface=wifi";
	private static boolean USE_MDS_IN_SIMULATOR = false;
	static AppendConnectionUids uids;

	private AppendConnectionUids()
	{
	}

	public static AppendConnectionUids getInstance()
	{
		if (uids == null)
			uids = new AppendConnectionUids();
		return uids;
	}

	private boolean _IsWifiAvailable()
	{
		if ((DeviceInfo.getSoftwareVersion().substring(0, 5).compareTo("4.3.0")) >= 0)
		{
			return (WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED);
		}
		else
		{
			if (CoverageInfo.isCoverageSufficient(1, RadioInfo.WAF_WLAN, false))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	private String _GetConnectionString()
	{
		String connectionString = null;
		try
		{
			if (DeviceInfo.isSimulator())
			{
				if (USE_MDS_IN_SIMULATOR)
				{
					connectionString = ";deviceside=false";
				}
				else
				{
					connectionString = ";deviceside=true";
				}
			}
			else if ((CoverageInfo.getCoverageStatus() & 1) == 1)
			{
				String carrierUid = _GetCarrierBIBSUid();
				if (carrierUid == null)
				{
					connectionString = ";deviceside=true";
				}
				else
				{
					connectionString = ";deviceside=false;connectionUID="
						+ carrierUid + ";ConnectionType=mds-public";
				}
			}
			else if ((CoverageInfo.getCoverageStatus() & CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS) {
				connectionString = ";deviceside=false";
			}
			else if (CoverageInfo.getCoverageStatus() == CoverageInfo.COVERAGE_NONE) {
				connectionString = ";deviceside=true";
			}
			else
			{
				connectionString = ";deviceside=true";
			}
		}
		catch (Exception e)
		{
		}
		return connectionString;
	}

	private static String _GetCarrierBIBSUid()
	{
		/*ServiceRecord[] records = ServiceBook.getSB().getRecords();
		int currentRecord;
		for (currentRecord = 0; currentRecord < records.length; currentRecord++)
		{
			if (records[currentRecord].getCid().equals( "CMIME" ))
			{
				AppConstants.SEND_EMAIL = true;
			}
		}*/
		
        ServiceRecord[] records = ServiceBook.getSB().getRecords();
		int currentRecord;
		for (currentRecord = 0; currentRecord < records.length; currentRecord++)
		{
			if (records[currentRecord].getCid().toLowerCase().equals("ippp"))
			{
				if (records[currentRecord].getName().toLowerCase().indexOf(
				"bibs") >= 0)
				{
					return records[currentRecord].getUid();
				}
			}
		}
		return null;
	}

	public static String strConnectionPostFix = null;
	public String _GetUrlWithParameters(String url)
	{
		if(strConnectionPostFix==null)
		{
			if (_IsWifiAvailable())
			{
				strConnectionPostFix = ";deviceside=true;interface=wifi";
			}
			else
			{
				strConnectionPostFix = _GetConnectionString();
			}
		}
		url = url + strConnectionPostFix;
		return url;
	}

	public boolean CheckCoverage()
	{
		try {
			if (CoverageInfo.getCoverageStatus() != CoverageInfo.COVERAGE_NONE) {
				return true;
			}
			return false;
		}
		catch (Exception e)
		{
		}
		return false;
	}

	public boolean IsCoverageWeak()
	{
		try
		{
			int level = RadioInfo.getSignalLevel();
			if (level < -100)
				return true;
			return false;
		}
		catch (Exception e)
		{
		}
		return false;
	}
}