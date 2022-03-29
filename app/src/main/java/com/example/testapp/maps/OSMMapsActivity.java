package com.example.testapp.maps;

import android.app.UiModeManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.IndexActivity;
import com.example.testapp.R;
import com.google.android.gms.maps.model.LatLng;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.TilesOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OSMMapsActivity extends AppCompatActivity {
    TextView searchOSM;
    MapView map;
    //starting point for the map
    double latitude = 4.62;
    double longitude = -74.07;
    GeoPoint startPoint = new 	GeoPoint(latitude, longitude);
    Marker longPressedMarker, searchMarker;
    Geocoder geocoder;
    RoadManager roadManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osmmaps);
        searchOSM = findViewById(R.id.searchOSM);
        geocoder = new Geocoder(this);
        //setContentView(R.layout.activity_open_street_maps);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map =findViewById(R.id.osmMap);
        roadManager = new OSRMRoadManager(this, "USER_AGENT");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getZoomController().activate();
        map.getOverlays().add(createEventsOverlay());
        searchOSM.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    if(searchOSM.getText()!=null) {
                        String address = searchOSM.getText().toString();
                        Log.i(IndexActivity.TAG, "Query GeoCoder: " +address );
                        LatLng position = geoCoderSearchAdress(address);
                        if(position!=null){
                            GeoPoint found = new GeoPoint(position.latitude, position.longitude);
                            searchMarker = createMarker(found, address,  null, 0 );//R.drawable.ic_baseline_directions_bike_24
                            map.getOverlays().add(searchMarker);
                            map.getController().setCenter(new GeoPoint(position.latitude, position.longitude));
                            IMapController mapController = map.getController();
                            mapController.setZoom(18.0);
                            drawRoute(found, startPoint);
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
        IMapController mapController = map.getController();
        mapController.setZoom(18.0);
        mapController.setCenter(this.startPoint);

        UiModeManager uiManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        if(uiManager.getNightMode() == UiModeManager.MODE_NIGHT_YES )
            map.getOverlayManager().getTilesOverlay().setColorFilter(TilesOverlay.INVERT_COLORS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    private MapEventsOverlay createEventsOverlay(){
        MapEventsOverlay eventsOverlay = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                longPressOnMap(p);
                return true;
            }
        });
        return eventsOverlay;
    }

    private void longPressOnMap(GeoPoint p){
        if(longPressedMarker!=null)
            map.getOverlays().remove(longPressedMarker);
        String description = this.geoCoderSearchLocation(new LatLng(p.getLatitude(), p.getLongitude()));
        longPressedMarker = createMarker(p, "New Location", description,R.drawable.ic_baseline_directions_bike_24);
        map.getOverlays().add(longPressedMarker);
        drawRoute(p, startPoint);
    }

    private Marker createMarker(GeoPoint p, String title, String description, int iconId){
        Marker marker = null;
        if(map!=null) {
            marker = new Marker(map);
            if (title != null) marker.setTitle(title);
            if (description != null) marker.setSubDescription(description);
            if (iconId != 0) {
                Drawable myIcon = getResources().getDrawable(iconId, this.getTheme());
                marker.setIcon(myIcon);
            }else{
                Drawable myIcon = getResources().getDrawable(R.drawable.ic_baseline_location_on_24, this.getTheme());
                marker.setIcon(myIcon);
            }
            marker.setPosition(p);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        }
        return marker;
    }

    private LatLng geoCoderSearchAdress(String address){
        LatLng position = null;
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 2);
            if (addresses != null && !addresses.isEmpty()) {
                Address addressResult = addresses.get(0);
                position = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
            } else {
                Toast.makeText(OSMMapsActivity.this, "Address not found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(OSMMapsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        };
        return addr;
    }

    private void drawRoute(GeoPoint start, GeoPoint finish){
        map.getOverlays().clear();
        ArrayList<GeoPoint> routePoints = new ArrayList<>();
        routePoints.add(start);
        routePoints.add(finish);
        Road road = roadManager.getRoad(routePoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        if(map!=null){
            map.getOverlays().add(roadOverlay);
        }
    }
}
