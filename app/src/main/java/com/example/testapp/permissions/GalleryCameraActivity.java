package com.example.testapp.permissions;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class GalleryCameraActivity extends AppCompatActivity {
    Button gallery, camera;
    ImageView image;

    //Camera uri to save the photo
    Uri uriCamera;

    //activities for result
    ActivityResultLauncher<String> mGetContentGallery = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uriLocal) {
                    //Load image on a view
                    setImage(uriLocal);
                }
            });
    ActivityResultLauncher<Uri> mGetContentCamera = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    //Load image on a view
                    setImage(uriCamera);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_camera);
        gallery = findViewById(R.id.gallery);
        camera = findViewById(R.id.camera);
        image = findViewById(R.id.image);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGallery();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCamera();
            }
        });
    }

    private void loadGallery(){
        mGetContentGallery.launch("image/*");
    }

    private void loadCamera(){
        File file = new File(getFilesDir(), "picFromCamera");
        uriCamera = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
        mGetContentCamera.launch(uriCamera);
    }

    private void setImage(Uri uri){
        final InputStream imageStream;
        try {
            Log.i("TEST_APP", "Image Uri: "+uri.toString());
            imageStream = getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            image.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}