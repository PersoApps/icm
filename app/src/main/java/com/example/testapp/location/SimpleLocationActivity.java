package com.example.testapp.location;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.example.testapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class SimpleLocationActivity extends AppCompatActivity {
    TextView latitude, longitude, elevation;

    FusedLocationProviderClient fusedLocationProvider;

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
                        requestLocationOnce();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_location);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        elevation = findViewById(R.id.elevation);
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);

        requestLocationOnce();

    }

    private void requestLocationOnce(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProvider.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    latitude.setText(String.valueOf(location.getLatitude()));
                    longitude.setText(String.valueOf(location.getLongitude()));
                    elevation.setText(String.valueOf(location.getAltitude()));
                }
            });
        }

    }
}