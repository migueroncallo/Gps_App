package com.example.migueroncallo.gpsapp;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class GoogleApiActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener, com.google.android.gms.location.LocationListener{


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public long UPDATE_INTERVAL = 10000;
    public long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL/2;
    String data, test;
    Cursor cursor;
    private dbManager db;
    TextView tvLat, tvLong;
    Button LowP, MedP, HiP, dataB, backB, stopB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_api);

        tvLat = (TextView) findViewById(R.id.LatView);
        tvLong = (TextView)findViewById(R.id.LongView);
        LowP = (Button)findViewById(R.id.lowProf);
        MedP = (Button)findViewById(R.id.medProf);
        HiP = (Button)findViewById(R.id.hiProf);
        dataB = (Button)findViewById(R.id.DbButton);
        backB = (Button)findViewById(R.id.BackButton);
        stopB = (Button)findViewById(R.id.StopB);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        data="";
        db = new dbManager(this);

        cursor = db.load();

        if (cursor.getCount() > 0) {

            String[] from = new String[]{db.CN_NAME};

        }


        LowP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test = "Low API Profile";
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(UPDATE_INTERVAL);
                mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                startGps();
            }
        });

        MedP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test = "Med API Profile";
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(UPDATE_INTERVAL);
                mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                startGps();
            }
        });

        HiP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test = "High API Profile";
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(UPDATE_INTERVAL);
                mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                startGps();
            }
        });

        stopB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGps();
            }
        });

        dataB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoogleApiActivity.this, Database.class);
                startActivity(intent);
            }
        });

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoogleApiActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
        Toast.makeText(this,"Connecting...", Toast.LENGTH_SHORT).show();
    }

    public void stopGps(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
    }

    public void startGps(){
        if(!mGoogleApiClient.isConnected()){
            mGoogleApiClient.connect();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_google_api, menu);
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

    @Override
    public void onConnected(Bundle bundle) {

        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        tvLat.setText("Lat = " + location.getLatitude() + "");
        tvLong.setText("Long = " + location.getLongitude() + "");
        db.insert(test, "" + location.getLatitude(), "" + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
