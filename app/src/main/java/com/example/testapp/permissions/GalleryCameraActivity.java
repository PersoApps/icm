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

    public static final String GALLERY_PERMISSION_NAME = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String CAMERA_PERMISSION_NAME = Manifest.permission.CAMERA;
    public static final int GALLERY_PERMISSION_ID = 7;
    public static final int CAMERA_PERMISSION_ID = 8;

    //Camera files
    File file;
    Uri uri;

    //activities for result
    ActivityResultLauncher<String> mGetContentGallery = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uriLocal) {
                    Log.i("TAG_APP", "URI from the gallery: "+uriLocal.toString());
                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(uriLocal);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        image.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
    ActivityResultLauncher<Uri> mGetContentCamera = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result){
                        Log.i("TAG_APP", "URI from the gallery: "+uri.toString());
                        final InputStream imageStream;
                        try {
                            imageStream = getContentResolver().openInputStream(uri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            image.setImageBitmap(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
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
                requestPermission(GalleryCameraActivity.this, GALLERY_PERMISSION_NAME, "NEEDED TO ACCESS GALERY", GALLERY_PERMISSION_ID);
                loadGallery();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(GalleryCameraActivity.this, CAMERA_PERMISSION_NAME, "NEEDED TO ACCESS GALERY", CAMERA_PERMISSION_ID);
                loadCamera();
            }
        });

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
        if(requestCode == GALLERY_PERMISSION_ID){
            loadGallery();
        }else if (requestCode == CAMERA_PERMISSION_ID){
            loadCamera();
        }
    }

    private void loadGallery(){
        if (ContextCompat.checkSelfPermission(this,GALLERY_PERMISSION_NAME) == PackageManager.PERMISSION_GRANTED) {
            mGetContentGallery.launch("image/*");
        }

    }
    private void loadCamera(){
        //if (ContextCompat.checkSelfPermission(this,CAMERA_PERMISSION_NAME) == PackageManager.PERMISSION_GRANTED) {
            file = new File(getFilesDir(), "picFromCamera");
            uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            mGetContentCamera.launch(uri);
        //}
    }

}