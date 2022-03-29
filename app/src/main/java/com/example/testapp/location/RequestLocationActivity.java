package com.example.testapp.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.testapp.IndexActivity;
import com.example.testapp.R;
import com.example.testapp.layouts.MainActivity;
import com.example.testapp.model.MyLocation;
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

import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Date;

public class RequestLocationActivity extends AppCompatActivity {
    public static double RADIUS_OF_EARTH_KM = 6371;
    public static double ELDORADO_LAT = 4.701254326122318;
    public static double ELDORADO_LONG = -74.14600084094732;

    JSONArray locations = new JSONArray();

    //Last Known location
    Location lastKnownLocation;


    TextView latitude, longitude, elevation, distanceToElDorado;
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
        distanceToElDorado = findViewById(R.id.distanceToElDorado);
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = createLocationRequest();
        locationCallback = createLocationCallback();

        getLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);


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
                lastKnownLocation = locationResult.getLastLocation();
                Log.i(IndexActivity.TAG, "Location received: "+lastKnownLocation);
                latitude.setText(String.valueOf(lastKnownLocation.getLatitude()));
                longitude.setText(String.valueOf(lastKnownLocation.getLongitude()));
                elevation.setText(String.valueOf(lastKnownLocation.getAltitude()));
                double distance = distance(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), ELDORADO_LAT, ELDORADO_LONG);
                distanceToElDorado.setText(String.valueOf(distance));
                writeJSONObject();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLocationSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result*100.0)/100.0;
    }

    private void writeJSONObject(){
        MyLocation myLocation = new MyLocation();
        myLocation.setDate(new Date(System.currentTimeMillis()));
        myLocation.setLatitude(lastKnownLocation.getLatitude());
        myLocation.setLongitude(lastKnownLocation.getLongitude());
        locations.put(myLocation.toJSON());
        Writer output = null;
        String filename= "locations.json";
        try {
            File file = new File(getBaseContext().getExternalFilesDir(null), filename);
            Log.i(MainActivity.TAG_APP, "Ubicacion de archivo: "+file);
            output = new BufferedWriter(new FileWriter(file));
            output.write(locations.toString());
            output.close();
        } catch (Exception e) {
            //Log error
        }
    }


}