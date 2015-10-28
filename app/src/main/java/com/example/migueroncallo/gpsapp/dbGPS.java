package com.example.migueroncallo.gpsapp;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by migueroncallo on 10/27/15.
 */
public class dbGPS extends SQLiteOpenHelper {

    private static final String DB_NAME = "GPS";
    private static final int DB_SCHEME_VERSION =1;

    public dbGPS(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(dbManager.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
