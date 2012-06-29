package com.ugtug.truempg.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.ugtug.truempg.BuildConfig;
import com.ugtug.truempg.LoginActivity;
import com.ugtug.truempg.db.Vehicle;
import com.ugtug.truempg.db.VehicleDao;

//Allow data to be sent to/from the server
public class Net {

    //Send operation types explicitly because we might need data that is returned
    public static String OPERATION_ADD_MPG = "operationAddMpg";
    public static String OPERATION_ADD_VEHICLE = "operationAddVehicle";
    
    public static String ADD_VEHICLE_DESTINATION = "http://truefuelefficiency.appspot.com/rest/vehicles";
    public static String ADD_MPG_DESTINATION = "http://truefuelefficiency.appspot.com/rest/fillups";

    //Send new vehicle data to the server
    public static void sendVehicleData(Context context, String make, String model, String year, String vin)
    {
        //Pull out user data that has been saved
        SharedPreferences sp = context.getSharedPreferences(LoginActivity.SHARED_PREFS_FILE, 0);
        String email = sp.getString(LoginActivity.SHARED_PREFS_EMAIL, "blank@blank.blank");

        //Put together what we're posting
        String postData = "make=" + make + "&model=" + model + "&year=" + year + "&vin=" + vin + "&userId=" + email;
        
        //Send the post
        makeServerCall(context, OPERATION_ADD_VEHICLE, postData, ADD_VEHICLE_DESTINATION);
    }
    
    //Send new MPG data to the server
    public static void sendMpgData(Context context, String quantity, String mileage, String longitude, String latitude, String vehicleId)
    {
        //Put together what we're posting
        String postData = "quantity=" + quantity + "&mileage=" + mileage + "&longitude=" + longitude + "&latitude=" + latitude + "&vehicleId=" + vehicleId;
        
        //Send the post
        makeServerCall(context, OPERATION_ADD_MPG, postData, ADD_MPG_DESTINATION);
    }
    
    //Generic method to send a post
    public static void makeServerCall(Context context, String operation, String postData, String destination)
    {
        ServerCallPojo serverCallPojo = new ServerCallPojo();
        serverCallPojo.context = context;
        serverCallPojo.opertaion = operation;
        serverCallPojo.destination = destination;
        serverCallPojo.postData = postData;
        
        //Run this in the background so the UI isn't frozen
        new AsyncTask<ServerCallPojo, Integer, Integer>() {

            @Override
            protected Integer doInBackground(ServerCallPojo... params)
            {
                ServerCallPojo serverCallPojo = params[0];
                Context context = serverCallPojo.context;
                String operation = serverCallPojo.opertaion;
                String postData = serverCallPojo.postData;
                String destination = serverCallPojo.destination;
                
                try
                {
                    // Start up our network connection
                    URL url = new URL(destination);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Length", "" + postData.getBytes().length);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    // Send the actual data
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.write(postData.getBytes());

                    // Log for debugging and testing
                    if (BuildConfig.DEBUG) Log.d("TrueMpg", "POSTDATA: " + postData);

                    // Flush everything to the destination
                    out.flush();
                    out.close();

                    // Get ready for our response
                    StringBuilder response = new StringBuilder();

                    // As long as the server is responsing and everything looks
                    // okay...
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        BufferedReader connectionReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String responseLine;
                        // Collect the entire response
                        while ((responseLine = connectionReader.readLine()) != null)
                            response.append(responseLine);
                        
                        //Based off the operation, we might do something with the response
                        Gson gson = new Gson();
                        Vehicle vehicle = gson.fromJson(response.toString(), Vehicle.class);
                        
                        
                        if (operation.equals(OPERATION_ADD_VEHICLE))
                        {
                            VehicleDao vehicleDao = new VehicleDao(context);
                            vehicleDao.updateLastVehicle(vehicle.vehicleId);
                        }

                    }

                }
                // Catch everything for debugging
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                                
                return null;

            }
        }.execute(serverCallPojo);
    }
}
