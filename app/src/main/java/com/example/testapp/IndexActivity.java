package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.firebase.FirebaseRealtimeDBActivity;
import com.example.testapp.firebase.LoginActivity;
import com.example.testapp.layouts.MainActivity;
import com.example.testapp.location.RequestLocationActivity;
import com.example.testapp.maps.MapProviderSelectorActivity;
import com.example.testapp.permissions.PermissionHomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class IndexActivity extends AppCompatActivity {
    public static final String TAG = "ICM_APP";
    Button basicui, permissions, location, maps, firebase;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        auth = FirebaseAuth.getInstance();
        basicui = findViewById(R.id.index_basicui);
        permissions = findViewById(R.id.index_permissions);
        location = findViewById(R.id.index_location);
        maps = findViewById(R.id.index_maps);
        firebase = findViewById(R.id.index_firebase);
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

    public void firebase(View v){
        startActivity(new Intent(IndexActivity.this, FirebaseRealtimeDBActivity.class));
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
            auth.signOut();
            Intent toLogin = new Intent(this, LoginActivity.class);
            toLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(toLogin);
            //close session
        }
        return super.onOptionsItemSelected(item);
    }
}