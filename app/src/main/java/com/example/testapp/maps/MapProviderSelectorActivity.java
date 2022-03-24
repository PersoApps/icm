package com.example.testapp.maps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.testapp.R;

public class MapProviderSelectorActivity extends AppCompatActivity {
    ImageButton googleMaps, osmMaps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_provider_selector);

        googleMaps = findViewById(R.id.googleMaps);
        osmMaps = findViewById(R.id.osmMaps);

        googleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GoogleMapsActivity.class));
            }
        });
        osmMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), OSMMapsActivity.class));
            }
        });
    }
}