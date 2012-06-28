package com.ugtug.truempg.db;

import java.util.List;
import java.util.ArrayList;


import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;

//Auto-generated DAO class
public class VehicleDao {

    public static final String TABLE_NAME = "vehicle";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MAKE = "make";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_VIN = "vin";

    private SQLiteDatabase db;

    private DbAdapter dbAdapter;

    public VehicleDao(Context context)
    {
        dbAdapter = DbAdapter.getInstance(context.getApplicationContext());
    }

    public void updateLastVehicle(long vehicleId)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ID, vehicleId);
        db = dbAdapter.getWritableDatabase();
        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + Vehicle.INVALID_ID, null);
        db.close();
    }
    
    public long addVehicle(Vehicle vehicle)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_MAKE, vehicle.make);
        contentValues.put(COLUMN_MODEL, vehicle.model);
        contentValues.put(COLUMN_NAME, vehicle.name);
        contentValues.put(COLUMN_VIN, vehicle.vin);
        contentValues.put(COLUMN_YEAR, vehicle.year);
        db = dbAdapter.getWritableDatabase();
        long newId = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        return newId;
    }

    public long updateVehicle(Vehicle vehicle)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_MAKE, vehicle.make);
        contentValues.put(COLUMN_MODEL, vehicle.model);
        contentValues.put(COLUMN_NAME, vehicle.name);
        contentValues.put(COLUMN_VIN, vehicle.vin);
        contentValues.put(COLUMN_YEAR, vehicle.year);
        db = dbAdapter.getWritableDatabase();
        long newId = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = " + vehicle.id, null);
        db.close();

        return newId;
    }

    public long deleteVehicle(long id)
    {
        db = dbAdapter.getWritableDatabase();
        long numDeleted = db.delete(TABLE_NAME, COLUMN_ID + " = " + id, null);
        db.close();

        return numDeleted;
    }

    public List<Vehicle> selectAll(boolean mostRecentFirst)
    {
        return selectAll(mostRecentFirst, null);
    }

    public List<Vehicle> selectAll(boolean mostRecentFirst, String where)
    {
        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        String[] columnsToSelect = {COLUMN_ID, COLUMN_MAKE, COLUMN_MODEL, COLUMN_NAME, COLUMN_VIN, COLUMN_YEAR};
        String orderBy =  COLUMN_ID + " ASC";

        if (mostRecentFirst) orderBy =  COLUMN_ID + " DESC";

        db = dbAdapter.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columnsToSelect, where, null, null, null, orderBy);

        while (cursor.moveToNext())
        {
            Vehicle vehicle = new Vehicle();
            vehicle.id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            vehicle.make = cursor.getString(cursor.getColumnIndex(COLUMN_MAKE));
            vehicle.model = cursor.getString(cursor.getColumnIndex(COLUMN_MODEL));
            vehicle.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            vehicle.vin = cursor.getString(cursor.getColumnIndex(COLUMN_VIN));
            vehicle.year = cursor.getString(cursor.getColumnIndex(COLUMN_YEAR));
            vehicleList.add(vehicle);
        }
        cursor.close();
        db.close();

        return vehicleList;
    }

    public Vehicle selectBySomeId(String where)
    {
        String[] columnsToSelect = {COLUMN_ID, COLUMN_MAKE, COLUMN_MODEL, COLUMN_NAME, COLUMN_VIN, COLUMN_YEAR};
        db = dbAdapter.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columnsToSelect, where, null, null, null, null);

        if (!cursor.moveToNext()) {
            cursor.close();
            db.close();
            return null;
        }

        Vehicle vehicle = new Vehicle();
        vehicle.id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        vehicle.make = cursor.getString(cursor.getColumnIndex(COLUMN_MAKE));
        vehicle.model = cursor.getString(cursor.getColumnIndex(COLUMN_MODEL));
        vehicle.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        vehicle.vin = cursor.getString(cursor.getColumnIndex(COLUMN_VIN));
        vehicle.year = cursor.getString(cursor.getColumnIndex(COLUMN_YEAR));
        cursor.close();
        db.close();

        return vehicle;
    }

    public Vehicle selectById(long id)
    {
        return selectBySomeId("_id = " + id);
    }

}