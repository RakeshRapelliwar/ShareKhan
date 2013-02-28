package mypackage;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.QualifiedCoordinates;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.TextField;

public class LocationTracker extends TimerTask {
	public final static String LOCATION_URL = "http://<url that you wish to post long/lat to>";

	private Timer timer;
	private double longitude;
	private double latitude;
	
	
	
	EditField edt;

	public LocationTracker(EditField edt) {
		timer = new Timer();
         this.edt=edt;
		try {
			// TODO: update criteria with desired location distances, etc
			LocationProvider.getInstance(new Criteria()).setLocationListener(
					new MyLocationListener(), 1, 1, 1);
		} catch (Exception e) {
			System.err.println(e.toString());
		}

		timer.schedule(this, 0, 10000);
	}

	public void run() {
		// HTTPClient.getPage(LOCATION_URL + "?longitude=" + longitude +
		// "&latitude=" + latitude);

	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	private class MyLocationListener implements LocationListener {

		
		public void locationUpdated(LocationProvider provider, Location location) {
			if (location != null && location.isValid()) {
				QualifiedCoordinates qc = location.getQualifiedCoordinates();

				try {
					// TODO: not thread safe (assignment should be done at one
					// time)
					LocationTracker.this.longitude = qc.getLongitude();
					LocationTracker.this.latitude = qc.getLatitude();

//					UiApplication.getUiApplication().invokeLater(
//							new Runnable() {
//
//								public void run() {
//									// TODO Auto-generated method stub
//									Dialog.alert("locationUpdated  " + longitude
//											+ "   :  " + latitude);
//								}
//							});
					
					edt.setText(""+System.currentTimeMillis()+"    "+ longitude
											+ " : " + latitude);
					

				} catch (final Exception e) {
					System.err.println("criccomini " + e.toString());

					UiApplication.getUiApplication().invokeLater(
							new Runnable() {

								public void run() {
									// TODO Auto-generated method stub
									Dialog.alert("criccomini " + e.toString());
								}
							});

				}
			} else {
				System.err.println("criccomini location not valid");

//				UiApplication.getUiApplication().invokeLater(new Runnable() {
//
//					public void run() {
//						// TODO Auto-generated method stub
//						Dialog.alert("criccomini location not valid");
//					}
//				});
				
				
				edt.setText(""+System.currentTimeMillis()+"    "+ "location not valid");

			}
		}

		public void providerStateChanged(LocationProvider provider, int newState) {
			if (newState == LocationProvider.OUT_OF_SERVICE) {
				// GPS unavailable due to IT policy specification

				UiApplication.getUiApplication().invokeLater(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						Dialog.alert("GPS unavailable due to IT policy specification ");
					}
				});

			} else if (newState == LocationProvider.TEMPORARILY_UNAVAILABLE) {
				// no GPS fix

				UiApplication.getUiApplication().invokeLater(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						Dialog.alert("no GPS fix ");
					}
				});
			}
		}
	}
}