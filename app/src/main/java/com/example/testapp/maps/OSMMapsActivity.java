package com.example.testapp.maps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.UiModeManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.testapp.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;

public class OSMMapsActivity extends AppCompatActivity {
    MapView map;
    //starting point for the map
    double latitude = 4.62;
    double longitude = -74.07;
    GeoPoint startPoint = new 	GeoPoint(latitude, longitude);
    Marker longPressedMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osmmaps);
        //setContentView(R.layout.activity_open_street_maps);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map =findViewById(R.id.osmMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getZoomController().activate();
        map.getOverlays().add(createEventsOverlay());
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
        String description = "("+p.getLatitude()+", "+p.getLongitude()+")";
        longPressedMarker = createMarker(p, "New Location", description,0 );//R.drawable.ic_baseline_directions_bike_24
        map.getOverlays().add(longPressedMarker);
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
}
