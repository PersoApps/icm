package com.example.testapp.permissions;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.example.testapp.R;

public class NewWaySimplePermissionActivity extends AppCompatActivity {
    TextView status;

    ActivityResultLauncher<String> getSinglePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result == true){
                        //granted
                        status.setText("Granted");
                        status.setTextColor(Color.GREEN);
                    }else{
                        //denied
                        status.setText("Denied");
                        status.setTextColor(Color.RED);
                    }
                }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_way_simple_permission);
        status = findViewById(R.id.permissionStatusNewWay);
        //onCreate
        getSinglePermission.launch(Manifest.permission.READ_CONTACTS);

    }
}