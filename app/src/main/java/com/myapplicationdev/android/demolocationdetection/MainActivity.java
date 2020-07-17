package com.myapplicationdev.android.demolocationdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    Button LocationRetriever;
    Button LocationUpdater;
    Button LocationUpdateRemover;


    private FusedLocationProviderClient fusedLocationClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationRetriever = findViewById(R.id.btnGetLastLocation);
        LocationUpdater = findViewById(R.id.btnGetLocationUpdate);
        LocationUpdateRemover = findViewById(R.id.btnRemoveLocationUpdate);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        final Task<Location> task = fusedLocationClient.getLastLocation();
        final LocationRequest mLocationRequest= new LocationRequest();



        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(100);



        LocationRetriever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission() == true) {
                    task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                String message = "Lat: " + location.getLatitude() + "Lng: " + location.getLongitude();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(MainActivity.this, "No location Found", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }


            }
        });

        LocationUpdater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationCallback mlocationcallBack = new LocationCallback() {
                    String msg = "";
                    @Override
                    public void onLocationResult(LocationResult locationResult){
                        if (locationResult != null){
                            Location data = locationResult.getLastLocation();
                            double lat = data.getLatitude();
                            double lng = data.getLatitude();
                            msg  = "Lat : " + lat + "Long : "  + lng;
                        }
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                };
                fusedLocationClient.requestLocationUpdates(mLocationRequest,mlocationcallBack,null);
            }
        });


    }
    private boolean checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

}
