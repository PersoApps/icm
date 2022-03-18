package com.example.testapp.location;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.testapp.IndexActivity;
import com.example.testapp.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class RequestLocationActivity extends AppCompatActivity {

    TextView latitude, longitude, elevation;
    FusedLocationProviderClient fusedLocationProvider;

    LocationRequest locationRequest;
    LocationCallback locationCallback;
    boolean settingsOK = false;

    ActivityResultLauncher<String> getLocationPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(!result){
                        latitude.setText("Not available, no permission");
                        longitude.setText("Not available, no permission");
                        elevation.setText("Not available, no permission");
                    }else{
                        startLocationUpdates();
                    }
                }
            });

    ActivityResultLauncher<IntentSenderRequest> getLocationSettings = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.i(IndexActivity.TAG, "Result from settings: "+result.getResultCode());
                    if(result.getResultCode() == RESULT_OK){
                        settingsOK = true;
                        startLocationUpdates();
                    }else{
                        elevation.setText("GPS is off");
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
        checkLocationSettings();

    }

    private void checkLocationSettings(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.i(IndexActivity.TAG, "GPS is ON");
                settingsOK = true;
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(((ApiException) e).getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED){
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    IntentSenderRequest isr = new IntentSenderRequest.Builder(resolvable.getResolution()).build();
                    getLocationSettings.launch(isr);
                }else {
                    elevation.setText("No GPS available");
                }
            }
        });

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
                Log.i(IndexActivity.TAG, "Location received: "+location);
                latitude.setText(String.valueOf(location.getLatitude()));
                longitude.setText(String.valueOf(location.getLongitude()));
                elevation.setText(String.valueOf(location.getAltitude()));
            }
        };
    }


    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(settingsOK) {
                fusedLocationProvider.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        }
    }

    private void stopLocationUpdates(){
        fusedLocationProvider.removeLocationUpdates(locationCallback);
    }
}