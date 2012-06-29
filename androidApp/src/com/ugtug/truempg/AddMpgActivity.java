package com.ugtug.truempg;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ugtug.truempg.db.Vehicle;
import com.ugtug.truempg.db.VehicleDao;
import com.ugtug.truempg.util.Net;

//This activity allows MPG data to be uploaded to a central server
//It also allows a user to start an add vehicle activity
public class AddMpgActivity extends Activity {

    public static final String DELIMITER_LAT_LON = ",";
    public static final String DELIMITER_DESC = ":";
    
    private TextView addVehicleLink;
    private TextView latLonText;
    private Spinner vehicles;
    private TextView gallons;
    private TextView mileage;
    int position = 0;
    private LocationManager locationManager;
    private List<LocationProvider> providers = new ArrayList<LocationProvider>();
    private List<MyLocationListener> listeners = new ArrayList<MyLocationListener>();
    
    //Standard onCreate
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mpg);
        
        //Hook up our views
        vehicles = (Spinner)findViewById(R.id.vehicle_list);
        gallons = (TextView)findViewById(R.id.add_gallons);
        mileage = (TextView)findViewById(R.id.add_mileage);
        addVehicleLink = (TextView)findViewById(R.id.add_vehicle_link);
        latLonText = (TextView)findViewById(R.id.add_location);
        
        //Add an HTML like link to cancel
        addVehicleLink.setText(Html.fromHtml("<u>"+getResources().getString(R.string.generic_add_vehicle)+"</u>"));
    
        //Setup gps/location info
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //For all possible providers
        List<String> providerNames = locationManager.getAllProviders();
        for(String providerName : providerNames) 
        {
            //Register listeners with each provider that is available
            LocationProvider provider = locationManager.getProvider(providerName);
            MyLocationListener listener = null;
            
            //Depending on the provider, use different icons and status fields
            if (LocationManager.GPS_PROVIDER.equals(providerName)) listener = new MyLocationListener(latLonText, locationManager);
            else if (LocationManager.NETWORK_PROVIDER.equals(providerName)) listener = new MyLocationListener(latLonText, locationManager);
            else continue;
            
            //Turn on updates
            locationManager.requestLocationUpdates(providerName, 0, 0, listener);
            
            //Register a listener
            providers.add(provider);
            listeners.add(listener);
        }
    }
    
    //This is an onClick method for sending MPG data to a server
    public void addMpg(View view)
    {
        Vehicle v = (Vehicle)vehicles.getSelectedItem();
        String gallonsString = gallons.getText().toString().replace(".", "").replace(",", "");
        String mileageString = mileage.getText().toString().replace(".", "").replace(",", "");
        String latLonTextString = latLonText.getText().toString();
        String latitude = "0";
        String longitude = "0";
        String[] descParts = latLonTextString.split(DELIMITER_DESC);
        if (descParts.length == 2)
        {
            String[] latlon = descParts[1].split(DELIMITER_LAT_LON);
            latitude = latlon[0];
            longitude = latlon[1];
        }
        
        Net.sendMpgData(this, gallonsString, mileageString, latitude, longitude, ""+v.vehicleId);
        
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(getResources().getString(R.string.thanks_mpg));
        dialogBuilder.setPositiveButton(getResources().getString(R.string.generic_okay), null);
        dialogBuilder.create().show();
        
    }
    
    //This populates data in the vehicle list
    private class VehicleAdapter extends ArrayAdapter<Vehicle>
    {
        //Basic constructor
        public VehicleAdapter(List<Vehicle> vehicleList)
        {       
            super(AddMpgActivity.this, R.layout.vehicle_row, R.id.vehicle_name, vehicleList);
        }
        
        //Tell the system what the expanded dropdown rows should look like
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent);
        }
        
        //Create a row
        public View getView(int position, View convertView, ViewGroup parent)
        {
            //Try to recycle the row
            View row = convertView;
            
            //Do we need to recreate it?
            if (row == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.vehicle_row, parent, false);
            }

            //Set the vehicle name
            TextView vehicleName = (TextView)row.findViewById(R.id.vehicle_name);
            
            //Get the person in question
            Vehicle vehicle = getItem(position);
            vehicleName.setText(vehicle.name);
            return row;
        }
    }
    
    //Standard on resume to refresh the vehicle list
    @Override
    public void onResume()
    {
        super.onResume();
        VehicleDao vd = new VehicleDao(this);
        List<Vehicle> vehicleList = vd.selectAll(true);
        
        if (vehicleList.size()==0)
        {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage(getResources().getString(R.string.add_mpg_need_vehicle));
            dialogBuilder.setPositiveButton(getResources().getString(R.string.generic_okay), new OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    addVehicle(null);
                }
            });
            dialogBuilder.create().show();
        }
        else
        {
            vehicles.setAdapter(new VehicleAdapter(vehicleList));
        }
    }
    
    //Allow the user to add a vehicle
    public void addVehicle(View view)
    {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        startActivity(intent);
    }
    
    //Cancel the current activity
    public void cancel(View view)
    {
        finish();
    }
    
}
