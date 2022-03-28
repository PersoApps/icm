package com.example.testapp.maps;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.IndexActivity;
import com.example.testapp.R;
import com.example.testapp.databinding.ActivityGoogleMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    EditText searchGoogle;

    private GoogleMap mMap;

    //Light sensor
    SensorManager sensorManager;
    Sensor lightSensor;
    SensorEventListener lightSensorListener;

    //Geocoder
    Geocoder geocoder;
    Marker searchMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        searchGoogle = findViewById(R.id.searchGoogle);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = createLightSensorListener();
        geocoder = new Geocoder(this);

        searchGoogle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    if(searchGoogle.getText()!=null) {
                        String address = searchGoogle.getText().toString();
                        Log.i(IndexActivity.TAG, "Query GeoCoder: " +address );
                        LatLng position = geoCoderSearchAdress(address);
                        if(position!=null){
                            addMarker(position, address, null, 0);// R.drawable.ic_baseline_directions_bike_24);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                        }
                    }
                }
                return false;
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng bogota = new LatLng(4.627010845237446, -74.06389749953162);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                String title = geoCoderSearchLocation(latLng);
                addMarker(latLng, title, null, R.drawable.ic_baseline_location_on_24 );
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private SensorEventListener createLightSensorListener(){
        SensorEventListener lsl = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(mMap!=null){
                    if(event.values[0]<5000){
                        Log.i(IndexActivity.TAG, "DARK MAP: "+event.values[0]);
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(GoogleMapsActivity.this, R.raw.dark_map));
                    }else{
                        Log.i(IndexActivity.TAG, "LIGHT MAP: "+event.values[0]);
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(GoogleMapsActivity.this, R.raw.light_map));
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        return lsl;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightSensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
    }

    private LatLng geoCoderSearchAdress(String address){
        LatLng position = null;
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 2);
            if (addresses != null && !addresses.isEmpty()) {
                Address addressResult = addresses.get(0);
                position = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
            } else {
                Toast.makeText(GoogleMapsActivity.this, "Address not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        };
        return position;
    }

    private  String geoCoderSearchLocation(LatLng latlng){
        String addr = null;
        Log.i(IndexActivity.TAG, "Search location name for coordinates: "+latlng.latitude+" "+latlng.longitude);
        try {
            List<Address> addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 2);
            Log.i(IndexActivity.TAG, addresses.toString());
            if (addresses != null && !addresses.isEmpty()) {
                addr = addresses.get(0).getAddressLine(0);
            } else {
                Toast.makeText(GoogleMapsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        };
        return addr;
    }

    private void addMarker(LatLng position, String title, String desc, int icon){
        if(mMap!=null){
            if(searchMarker!=null) searchMarker.remove();
            searchMarker = mMap.addMarker(new MarkerOptions().position(position));
            if (title != null) searchMarker.setTitle(title);
            if (desc != null) searchMarker.setSnippet(desc);
            if (icon > 0) searchMarker.setIcon(bitmapDescriptorFromVector(this, icon));
        }
    }
}