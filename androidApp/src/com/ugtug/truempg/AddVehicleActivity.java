package com.ugtug.truempg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.ugtug.truempg.db.Vehicle;
import com.ugtug.truempg.db.VehicleDao;
import com.ugtug.truempg.util.Net;

//Allow the user to save a new vehicle for use in adding MPG data
public class AddVehicleActivity extends Activity {

    //All our UI views
    private TextView cancelLink;
    private TextView makeView;
    private TextView modelView;
    private TextView yearView;
    private TextView vinView;
    private TextView nameView;
    
    //Standard onCreate
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle);
        
        //Hook up all our views
        makeView = (TextView)findViewById(R.id.add_vehicle_make);
        modelView = (TextView)findViewById(R.id.add_vehicle_model);
        yearView = (TextView)findViewById(R.id.add_vehicle_year);
        vinView = (TextView)findViewById(R.id.add_vehicle_vin);
        nameView = (TextView)findViewById(R.id.add_vehicle_name);
        cancelLink = (TextView)findViewById(R.id.cancel_link);
        
        //Create an HTML like link for going back (cancelling)
        cancelLink.setText(Html.fromHtml("<u>"+getResources().getString(R.string.generic_cancel)+"</u>"));
    }
    
    //Cancel this activity
    public void cancel(View view)
    {
        finish();
    }
    
    //Persist what the UI has to a new vehicle
    public void addVehicle(View view)
    {
        //Grab data from the UI
        String vin = vinView.getText().toString();
        String make = makeView.getText().toString();
        String model = modelView.getText().toString();
        String year = yearView.getText().toString().replace(".", "").replace(",", "");
        String name = nameView.getText().toString();
        
        //Create a new vehicle using the data
        //The id will later be re-written with the server's vehicle id
        Vehicle vehicle = new Vehicle();
        vehicle.vehicleId = Vehicle.INVALID_ID;
        vehicle.make = make;
        vehicle.model = model;
        vehicle.year = year;
        vehicle.vin = vin;
        vehicle.name = name;
        
        //Persist the data, and return to the add MPG activity
        VehicleDao vd = new VehicleDao(this);
        vd.addVehicle(vehicle);
        
        Net.sendVehicleData(this, make, model, year, vin);
        
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(getResources().getString(R.string.add_vehicle_done));
        dialogBuilder.setPositiveButton(getResources().getString(R.string.generic_okay), new OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                AddVehicleActivity.this.finish();
            }
        });
        dialogBuilder.create().show();
    }
    
}
