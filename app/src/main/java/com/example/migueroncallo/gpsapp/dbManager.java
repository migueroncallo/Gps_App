package com.example.migueroncallo.gpsapp;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
/**
 * Created by migueroncallo on 10/27/15.
 */
public class dbManager {

    public static final String TABLE_NAME = "Lecturas";
    public static final String CN_ID="_id";
    public static final String CN_NAME ="Name";
    public static final String CN_LATITUDE="latitude";
    public static final String CN_LONGITUDE="longitude";

public static final String CREATE_TABLE = "create table "+TABLE_NAME+" (" +CN_ID+" integer primary key autoincrement," +CN_NAME+ " text not null," +CN_LATITUDE+" text not null," +CN_LONGITUDE+" text not null);";

    private dbGPS dbgps;
    private SQLiteDatabase db;

    public dbManager(Context context) {

        dbgps = new dbGPS(context);
        db = dbgps.getWritableDatabase();
    }

    public ContentValues generate(String name, String latitude,String longitude){
        ContentValues values = new ContentValues();
        values.put(CN_NAME,name);
        values.put(CN_LATITUDE,latitude);
        values.put(CN_LONGITUDE,longitude);
        return values;
    }

    public void insert(String name, String latitude,String longitude)
    {
        db.insert(TABLE_NAME, null, generate(name, latitude, longitude));
    }

    public Cursor load()
    {
        String[] columns = new String[]{CN_ID,CN_NAME,CN_LATITUDE,CN_LONGITUDE};
        return db.query(TABLE_NAME,columns,null,null,null,null,null);
    }
}


