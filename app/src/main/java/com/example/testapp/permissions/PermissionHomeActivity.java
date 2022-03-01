package com.example.testapp.permissions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.testapp.R;

public class PermissionHomeActivity extends AppCompatActivity {
    ImageButton contacts, camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_home);

        contacts = findViewById(R.id.sp_contacts);
        camera = findViewById(R.id.sp_camera);

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PermissionHomeActivity.this, NewWaySimplePermissionActivity.class));
                //startActivity(new Intent(PermissionHomeActivity.this, ContactsActivity.class));
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PermissionHomeActivity.this, SimplePermissionActivity.class));
                startActivity(new Intent(PermissionHomeActivity.this, GalleryCameraActivity.class));
            }
        });
    }
}