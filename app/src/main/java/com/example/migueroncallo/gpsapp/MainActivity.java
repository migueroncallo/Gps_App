package com.example.migueroncallo.gpsapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    TextView tvLat;
    TextView tvLong;
    String test;
    Button mStop;
    Button mLo, mMed, mHi, mGoogle, mDBcheck;
    private dbManager db;
    private LocationManager mLocationManager;
    private GoogleMap mMap;
    int REQUEST_CODE_ASK_PERMISSIONS;
    String data;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLat = (TextView) findViewById(R.id.LatView);
        tvLong = (TextView) findViewById(R.id.LongView);
        mStop = (Button) findViewById(R.id.StopButton);
        mDBcheck = (Button) findViewById(R.id.dbCheck);
        mLo = (Button)findViewById(R.id.LowPro);
        data="";
        db = new dbManager(this);
        mMed = (Button)findViewById(R.id.MedPro);
        mHi = (Button)findViewById(R.id.HiPro);
        mGoogle = (Button) findViewById(R.id.googleButton);
        REQUEST_CODE_ASK_PERMISSIONS = 123;
        cursor = db.load();

        if (cursor.getCount() > 0) {

            String[] from = new String[]{db.CN_NAME};

        }


        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        verifyGPS();

        mLo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBestProviderLow();
            }
        });
        mMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBestProviderMed();
            }
        });

        mHi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBestProviderHi();
            }
        });
        mDBcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Database.class);
                startActivity(intent);
            }
        });

        mGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, GoogleApiActivity.class);
                startActivity(intent);
            }
        });



        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGPS();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void stopGPS() {
        stopSensor();

    }

        @TargetApi(Build.VERSION_CODES.M)
    public void startGPS() {

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS );
            return;
        }


        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


    }

    @TargetApi(Build.VERSION_CODES.M)
    public void stopSensor(){
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS );
            return;
        }



        mLocationManager.removeUpdates(this);

    }

    @Override
    public void onStop(){
        super.onStop();
        stopSensor();
    }

    private void centerMap(LatLng mapCenter){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mapCenter));

        CameraPosition cameraPosition = CameraPosition.builder().target(mapCenter).zoom(18.0f).bearing(0f).tilt(45).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);

    }

    @Override
    public void onLocationChanged(Location location){
        tvLat.setText("Lat = " + location.getLatitude() + "");
        tvLong.setText("Long = " + location.getLongitude() + "");
        db.insert(test, "" + location.getLatitude(), "" + location.getLongitude());
//        centerMap(new LatLng(location.getLatitude(), location.getLongitude()));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){

    }


    private boolean verifyGPS(){
        boolean enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!enabled){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS no habilitado");
            builder.setMessage("Habilitar ahora?");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setCancelable(false);

            builder.setNegativeButton("Ahora no", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    return;
                }
            });

            builder = builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    return;
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
        return false;
    }


    private String getBestProviderLow(){

        test = "Low profile";
        Criteria mCriteria = new Criteria();
        mCriteria.setHorizontalAccuracy(Criteria.ACCURACY_LOW);
        mCriteria.setVerticalAccuracy(Criteria.ACCURACY_LOW);
        mCriteria.setBearingAccuracy(Criteria.ACCURACY_LOW);
        mCriteria.setPowerRequirement(Criteria.POWER_LOW);
        String bestProvider = mLocationManager.getBestProvider(mCriteria, true);
        Toast.makeText(this, "Provider "+bestProvider, Toast.LENGTH_SHORT).show();
        startGPS();

        return bestProvider;

    }
    private String getBestProviderMed(){

        test = "Med Profile";
        Criteria mCriteria = new Criteria();
        mCriteria.setHorizontalAccuracy(Criteria.ACCURACY_MEDIUM);
        mCriteria.setVerticalAccuracy(Criteria.ACCURACY_MEDIUM);
        mCriteria.setBearingAccuracy(Criteria.ACCURACY_MEDIUM);
        mCriteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String bestProvider = mLocationManager.getBestProvider(mCriteria, true);
        Toast.makeText(this, "Provider "+bestProvider, Toast.LENGTH_SHORT).show();
        startGPS();
        return bestProvider;

    }

    private String getBestProviderHi(){

        test = "High Profile";
        Criteria mCriteria = new Criteria();
        mCriteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        mCriteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        mCriteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
        mCriteria.setPowerRequirement(Criteria.POWER_HIGH);
        String bestProvider = mLocationManager.getBestProvider(mCriteria, true);
        Toast.makeText(this, "Provider "+bestProvider, Toast.LENGTH_SHORT).show();
        startGPS();
        return bestProvider;
    }
    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS );
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(getBestProviderLow());
        LatLng lastLocation = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(lastLocation).title("Home?"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLocation));

    }
}
