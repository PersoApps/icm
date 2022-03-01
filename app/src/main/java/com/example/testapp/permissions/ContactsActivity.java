package com.example.testapp.permissions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.permissions.adapter.ContactsAdapter;

public class ContactsActivity extends AppCompatActivity {
    public static final String CONTACTS_PERMISSION_NAME = Manifest.permission.READ_CONTACTS;
    public static final int CONTACTS_PERMISSION_ID = 6;
    ListView contactList;

    //Adaptador

    //Datos
    String[] projection;
    Cursor cursor;
    ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        contactList = findViewById(R.id.listContacts);
        requestPermission(this, CONTACTS_PERMISSION_NAME, "Needed", CONTACTS_PERMISSION_ID);

        projection = new String[]{ContactsContract.Profile._ID,ContactsContract.Profile.DISPLAY_NAME_PRIMARY};
        contactsAdapter = new ContactsAdapter(this, null, 0);
        contactList.setAdapter(contactsAdapter);

        initView();
    }

    private void initView(){
        if( ContextCompat.checkSelfPermission(this,CONTACTS_PERMISSION_NAME) == PackageManager.PERMISSION_GRANTED){
            cursor=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
            contactsAdapter.changeCursor(cursor);
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