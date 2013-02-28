package com.snapwork.stream;

import java.io.IOException;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;

import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.io.transport.TransportInfo;
import net.rim.device.api.io.transport.options.TcpCellularOptions;
import net.rim.device.api.system.DeviceInfo;

import com.lightstreamer.javameclient.midp.ConnectionProvider;
import com.snapwork.util.HTTPGetConnection;


/**
 * This class represents a simple example on how to implement the ConnectionProvider interface to
 * let the library work on BlackBerry devices. 
 * 
 * 
 * you need to sign your application to let it work on real devices if you want to use the code snippet in this comment;
 * reengineering the code around this snippet should decrease the connection time as you don't have to try connections
 * at random before getting the right one. Also the current implementation  assumes that if an APN is configured it will 
 * work while if it is not configured it will not: this may not be the case.
  
        ServiceBook sb = ServiceBook.getSB();
        ServiceRecord[] records = sb.findRecordsByCid("IPPP");
        if(records != null) {
            for(int i = records.length-1; i >= 0; i--) {
                ServiceRecord rec = records[i];
                if(rec.isValid() && !rec.isDisabled()) {
                        if(rec.getEncryptionMode() == ServiceRecord.ENCRYPT_RIM) {
                            return ";deviceside=false";
                        } else {
                            return ";deviceside=false;ConnectionType=mds-public";
                        }
                }
            }
        }
        return ";deviceside=true"; // No IPPP records, fallback to TCP.
         
 * Also if you're only targeting newest BB you may be able to use the ConnectionFactory with success thus implementing 
 * a very simple ConnectionProvider with the following code:
  
    return new ConnectionFactory().getConnection(url).getConnection();
    
 *
 */
public class BBConnectionProviderOri implements ConnectionProvider {
    
    private int more = 0;
    private static int successful = 0;
    
    public ConnectionProvider clone() {
        return new BBConnectionProviderOri();
    }

    public Connection getNextConnection(String url, boolean arg1)
            throws IOException {

        if (more == 0 && successful != 0) {
            Connection res = getAConnection(url,successful);
            if (res != null) {
                return res;
            } else {
                successful = 0;
            }
        }
        
        more++;
        
        while (more < 6) {
            Connection res = getAConnection(url,more);
            if (res != null) {
                return res;
            }
            
            more++;
        }
        return Connector.open(url,Connector.READ_WRITE, true);
        
    }
    
    private Connection getAConnection(String url, int toGet) 
        throws IOException {
        
        switch(toGet) {
            case 1:  
            	if(DeviceInfo.isSimulator())
            	{
            		return Connector.open(HTTPGetConnection.getURL(url),Connector.READ_WRITE, true);
            	}
            	else
            	{
            		if (hasCoverage(TransportInfo.TRANSPORT_TCP_WIFI)) {
            			return Connector.open(url+";interface=wifi",Connector.READ_WRITE, true);
            		}
                }
                break;
                
            case 2: 
                if (hasCoverage(TransportInfo.TRANSPORT_TCP_CELLULAR) && hasAPN()) {
                    return Connector.open(url+";deviceside=true",Connector.READ_WRITE, true);
                }
                break;
            
            case 3:
                if (hasCoverage(TransportInfo.TRANSPORT_TCP_CELLULAR) && !hasAPN()) {
                    return Connector.open(url+";deviceside=false;ConnectionType=mds-public",Connector.READ_WRITE, true);
                }
                break;
                
            case 4:
                if (hasCoverage(TransportInfo.TRANSPORT_TCP_CELLULAR) && !hasAPN()) {
                    return Connector.open(url+";deviceside=false",Connector.READ_WRITE, true);
                }
                break;     
            case 5:
                //automatic selection does not to work on some 5.0 OS BBs.
                return new ConnectionFactory().getConnection(url).getConnection();
                
        }
        return null;
    }
    

    public boolean hasConnections() {
        return more < 6;
    }

    public void rebuild() {
        more = 0;  
    }

    public void setType(int arg0) {
    }

    public void wasSuccessful() {
        successful = more;
    }
    
    private boolean hasCoverage(int type) {
        return (TransportInfo.isTransportTypeAvailable(type) && TransportInfo.hasSufficientCoverage(type));
    }
    
    private boolean hasAPN(){
        TcpCellularOptions tco = new TcpCellularOptions();
        return tco.getApn() != null;
    }
    

}
