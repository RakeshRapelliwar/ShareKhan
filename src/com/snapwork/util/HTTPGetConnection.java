package com.snapwork.util;

import net.rim.device.api.system.DeviceInfo;

public class HTTPGetConnection {
        public static TransportDetective td ;
        public static boolean isWIFIAvailable ;
        public static boolean isBIS_BAvailable ;
        public static boolean isMDSAvailable ;
        public static boolean isWap2Available ;
        public static boolean isWapAvailable ;
        public static boolean isTCP_cellularAvailable ;
        public static boolean isTCP_cellularDefaultAvailable ;
        public static int availableServices ;
        public static int availableCoverage ;
        public static String ConnectionUsed = "";
        public static int id = -1;
        public static void setConnections()
        {
                td = new TransportDetective();
                availableServices = td.getAvailableTransportServices();
                availableCoverage = td.getAvailableTransportCoverage();
                isWIFIAvailable = td.isCoverageAvailable(TransportDetective.TRANSPORT_TCP_WIFI);
                isBIS_BAvailable = td.isCoverageAvailable(TransportDetective.TRANSPORT_BIS_B);
                isMDSAvailable = td.isCoverageAvailable(TransportDetective.TRANSPORT_MDS);
                isWap2Available = td.isCoverageAvailable(TransportDetective.TRANSPORT_WAP2);
                isWapAvailable = td.isCoverageAvailable(TransportDetective.TRANSPORT_WAP);
                isTCP_cellularAvailable = td.isCoverageAvailable(TransportDetective.TRANSPORT_TCP_CELLULAR);
                isTCP_cellularDefaultAvailable = td.isCoverageAvailable(TransportDetective.DEFAULT_TCP_CELLULAR);
                id = -1;
                LOG.print("TransportDetective Finish");
                LOG.print("isWIFIAvailable "+isWIFIAvailable);
                LOG.print("isBIS_BAvailable "+isBIS_BAvailable);
                LOG.print("isMDSAvailable "+isMDSAvailable);
                LOG.print("isWap2Available "+isWap2Available);
                LOG.print("isWapAvailable "+isWapAvailable);
                LOG.print("isTCP_cellularAvailable "+isTCP_cellularAvailable);
                LOG.print("isTCP_cellularDefaultAvailable "+isTCP_cellularDefaultAvailable);
        }
        
        public static String getURL(String url)
        {
        	LOG.print(url);
        	if(DeviceInfo.isSimulator())
        	{
        		id = 1;
        		url = url+";deviceside=true";
        		return url;
        	}
        	
        	if(td == null)
        		setConnections();
                String connectionURL = "";
                if(isWIFIAvailable && (availableCoverage & TransportDetective.TRANSPORT_TCP_WIFI) > 0)
                {
                    ConnectionUsed = "WIFI";
                        URLFactory urlFactory = new URLFactory(url);
                           connectionURL = urlFactory.getHttpTcpWiFiUrl();
                           id = 1;
                }
                else if(isBIS_BAvailable && (availableCoverage & TransportDetective.TRANSPORT_BIS_B) > 0)
                {
                    ConnectionUsed = "BIS";
                        URLFactory urlFactory = new URLFactory(url);
                           connectionURL = urlFactory.getHttpBisUrl();
                           id = 2;
                }
                else if(isMDSAvailable && (availableCoverage & TransportDetective.TRANSPORT_MDS) > 0)
                {
                    ConnectionUsed = "MDS";
                        URLFactory urlFactory = new URLFactory(url);
                           connectionURL = urlFactory.getHttpMdsUrl(true);
                           id = 3;
                }
                else if(isWap2Available && (availableCoverage & TransportDetective.TRANSPORT_WAP2) > 0)
                {
                    ConnectionUsed = "WAP2";
                                URLFactory urlFactory = new URLFactory(url);
                           connectionURL = urlFactory.getHttpWap2Url(td.getWap2ServiceRecord());
                           id = 4;
                }
              /*  else if(isWapAvailable && (availableServices & TransportDetective.TRANSPORT_WAP) > 0)
                {
                        URLFactory urlFactory = new URLFactory(url);
                        
                           //connectionURL = urlFactory.getHttpWap1Url(gatewayAPN, gatewayIP, gatewayPort, sourceIP, sourcePort, username, password, enableWTLS);
                        connectionURL = urlFactory.getHttpWap1Url(td.getWapServiceRecord().getAPN(), td.getWapServiceRecord().getCAAddress(),String.valueOf(td.getWapServiceRecord().getCAPort()),td.getWapServiceRecord().getBBRHosts()[0],  String.valueOf(td.getWapServiceRecord().getBBRPorts()[0]), "", "", false);
                }*/
                else if(isTCP_cellularAvailable && (availableCoverage & TransportDetective.TRANSPORT_TCP_CELLULAR) > 0)
                {
                	LOG.print("B : isTCP_cellularAvailable inside "+url);
                    ConnectionUsed = "TCP Cellular";
                        try {
							URLFactory urlFactory = new URLFactory(url);
							   connectionURL = urlFactory.getHttpTcpCellularUrl(td.getDefaultTcpCellularServiceRecord().getAPN(), "", "");
						} catch (Exception e) {
							connectionURL = url;
							e.printStackTrace();
						}
						id = 5;
                           LOG.print("A : isTCP_cellularAvailable inside "+connectionURL);
                }
                else if(isTCP_cellularDefaultAvailable)
                {
                    ConnectionUsed = "TCP Cellular Default";
                        URLFactory urlFactory = new URLFactory(url);
                           connectionURL = urlFactory.getHttpDefaultTcpCellularUrl(td.getDefaultTcpCellularServiceRecord());
                           id = 6;
                }
                        return connectionURL;
                
        }
        
        public static String getS(String url)
        {
        	td = new TransportDetective();
            availableServices = td.getAvailableTransportServices();
            availableCoverage = td.getAvailableTransportCoverage();
            isWIFIAvailable = td.isCoverageAvailable(TransportDetective.TRANSPORT_TCP_WIFI);
            isBIS_BAvailable = td.isCoverageAvailable(TransportDetective.TRANSPORT_BIS_B);
            isMDSAvailable = td.isCoverageAvailable(TransportDetective.TRANSPORT_MDS);
            isWap2Available = td.isCoverageAvailable(TransportDetective.TRANSPORT_WAP2);
            isWapAvailable = td.isCoverageAvailable(TransportDetective.TRANSPORT_WAP);
            isTCP_cellularAvailable = td.isCoverageAvailable(TransportDetective.TRANSPORT_TCP_CELLULAR);
            isTCP_cellularDefaultAvailable = td.isCoverageAvailable(TransportDetective.DEFAULT_TCP_CELLULAR);
            LOG.print("TransportDetective Finish");
            LOG.print("isWIFIAvailable "+isWIFIAvailable);
            LOG.print("isBIS_BAvailable "+isBIS_BAvailable);
            LOG.print("isMDSAvailable "+isMDSAvailable);
            LOG.print("isWap2Available "+isWap2Available);
            LOG.print("isWapAvailable "+isWapAvailable);
            LOG.print("isTCP_cellularAvailable "+isTCP_cellularAvailable);
            LOG.print("isTCP_cellularDefaultAvailable "+isTCP_cellularDefaultAvailable);
            return getURL(url);
        }

}
