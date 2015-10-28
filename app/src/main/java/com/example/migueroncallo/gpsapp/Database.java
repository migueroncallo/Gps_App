package com.example.migueroncallo.gpsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.database.Cursor;
import android.widget.TextView;

public class Database extends AppCompatActivity {

    private TextView showN,showLat,showLon;
    private  dbManager bd;
    Cursor cursor;
    private String nombre="",lat="",lon="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        showN = (TextView) findViewById(R.id.textView7);
        showLat = (TextView) findViewById(R.id.textView8);
        showLon = (TextView) findViewById(R.id.textView9);
        bd = new dbManager(this);
        cursor = bd.load();
        if(cursor.getCount()>0) {

            if (cursor.moveToFirst()) {

                do {

                    nombre=nombre+cursor.getString(1)+" \r\n";
                    lat=lat+cursor.getString(2)+" \r\n";
                    lon=lon+cursor.getString(3)+"\r\n";


                } while(cursor.moveToNext());
            }
            showN.setText("" + nombre);
            showLat.setText("" + lat);
            showLon.setText("" + lon);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_database, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
