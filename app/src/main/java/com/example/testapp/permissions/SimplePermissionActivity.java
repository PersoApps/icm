package com.example.testapp.permissions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.R;

import java.security.Permission;

public class SimplePermissionActivity extends AppCompatActivity {

    TextView permissionStatus;
    public static final String CONTACTS_PERMISSION_NAME = Manifest.permission.READ_CONTACTS;
    public static final int CONTACTS_PERMISSION_ID = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_permission);
        permissionStatus = findViewById(R.id.permissionStatus);
        requestPermission(this, CONTACTS_PERMISSION_NAME, "Needed", CONTACTS_PERMISSION_ID);
        initView();

    }

    private void initView(){
        if( ContextCompat.checkSelfPermission(this,CONTACTS_PERMISSION_NAME) == PackageManager.PERMISSION_GRANTED){
            permissionStatus.setText("Concedido");
            permissionStatus.setTextColor(Color.GREEN);
        }else {
            permissionStatus.setText("Denegado");
            permissionStatus.setTextColor(Color.RED);
        }
    }

    private void requestPermission(Activity context, String permission, String justification, int id){
        if (ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,permission)) {
                Toast.makeText(context, "Permiso requerido para mostrar los contactos", Toast.LENGTH_SHORT).show();
            }
            // request the permission.
            ActivityCompat.requestPermissions(context,new String[]{permission}, id);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CONTACTS_PERMISSION_ID){
            initView();
        }
    }
}