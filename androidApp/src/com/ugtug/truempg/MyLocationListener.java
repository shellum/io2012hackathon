package com.ugtug.truempg;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class MyLocationListener implements LocationListener {

	
	//Saved views that this class modifies
	private TextView updateText;
	private LocationManager locationManager;
	
	//Save off everything we need later
	public MyLocationListener(TextView updateText, LocationManager locationManager)
	{
		this.updateText = updateText;
		this.locationManager = locationManager;
	}
	
	//If we get a fix...
	@Override
	public void onLocationChanged(Location location)
	{	
		double longitude = (location.getLongitude());
		double latitude = (location.getLatitude());
		
		//Tell the UI about it
		updateText.setText(updateText.getContext().getResources().getString(R.string.add_mpg_location) + latitude + AddMpgActivity.DELIMITER_LAT_LON + longitude);
        //Stop getting fixes from this provider
        locationManager.removeUpdates(this);
	}

	@Override public void onProviderDisabled(String provider) {/*Not Implemented*/}
	@Override public void onProviderEnabled(String provider) {/*Not Implemented*/}
	@Override public void onStatusChanged(String provider, int status, Bundle extras) {/*Not Implemented*/}

}
