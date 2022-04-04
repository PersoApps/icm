package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.testapp.layouts.MainActivity;
import com.example.testapp.location.RequestLocationActivity;
import com.example.testapp.location.SimpleLocationActivity;
import com.example.testapp.maps.GoogleMapsActivity;
import com.example.testapp.maps.MapProviderSelectorActivity;
import com.example.testapp.permissions.PermissionHomeActivity;

public class IndexActivity extends AppCompatActivity {
    public static final String TAG = "ICM_APP";
    Button basicui, permissions, location, maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        basicui = findViewById(R.id.index_basicui);
        permissions = findViewById(R.id.index_permissions);
        location = findViewById(R.id.index_location);
        maps = findViewById(R.id.index_maps);
    }

    public void basicUi(View v){
        startActivity(new Intent(IndexActivity.this, MainActivity.class));
    }
    public void permissions(View v){
        startActivity(new Intent(IndexActivity.this, PermissionHomeActivity.class));
    }
    public void location(View v){
        startActivity(new Intent(IndexActivity.this, RequestLocationActivity.class));
    }
    public void maps(View v){
        startActivity(new Intent(IndexActivity.this, MapProviderSelectorActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.indexmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menusignout){
            //close session
        }
        return super.onOptionsItemSelected(item);
    }
}