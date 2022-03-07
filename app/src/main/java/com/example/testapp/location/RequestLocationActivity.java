package com.example.testapp.location;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.example.testapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class RequestLocationActivity extends AppCompatActivity {

    TextView latitude, longitude, elevation;
    FusedLocationProviderClient fusedLocationProvider;

    LocationRequest locationRequest;
    LocationCallback locationCallback;

    ActivityResultLauncher<String> getLocationPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(!result){
                        latitude.setText("Not available, no permission");
                        longitude.setText("Not available, no permission");
                        elevation.setText("Not available, no permission");
                    }else {
                       //start location updates
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_location);

        latitude = findViewById(R.id.latitudeRL);
        longitude = findViewById(R.id.longitudeRL);
        elevation = findViewById(R.id.elevationRL);
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = createLocationRequest();
        locationCallback = createLocationCallback();

        getLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);

    }

    private LocationRequest createLocationRequest(){
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private LocationCallback createLocationCallback(){
        return new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                latitude.setText(String.valueOf(location.getLatitude()));
                longitude.setText(String.valueOf(location.getLongitude()));
                elevation.setText(String.valueOf(location.getAltitude()));
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProvider.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void stopLocationUpdates(){
        fusedLocationProvider.removeLocationUpdates(locationCallback);
    }
}