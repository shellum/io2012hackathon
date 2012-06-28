package com.ugtug.truempg.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Take care of creating our database, and provide a db access mechanism
public class DbAdapter extends SQLiteOpenHelper {

    private static final String SQL_DB_NAME = "truempg";
    private static final int CURRENT_DB_VERSION = 1;
    
    //Table definitions
    private static final String SQL_CREATE_VEHICLE = "CREATE TABLE vehicle (id INTEGER, name TEXT, make TEXT, model TEXT, year TEXT, vin TEXT)";

    private static DbAdapter dbAdapter;
    
    //Basic, required constructor
    private DbAdapter(Context context)
    {
        super(context, SQL_DB_NAME, null, CURRENT_DB_VERSION);
    }

    //Make sure we only keep one instance 
    public synchronized static DbAdapter getInstance(Context context)
    {
        if (dbAdapter == null) dbAdapter = new DbAdapter(context);
        return dbAdapter;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_VEHICLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { /*Not implemented*/ } 

}