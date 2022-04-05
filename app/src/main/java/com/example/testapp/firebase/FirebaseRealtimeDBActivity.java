package com.example.testapp.firebase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.IndexActivity;
import com.example.testapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseRealtimeDBActivity extends AppCompatActivity {
    public static final String FB_USERS_PATH="users/";
    Button saveUser;
    EditText userName, userLastName, userAge;
    LinearLayout listUsers;

    FirebaseDatabase database;
    DatabaseReference myRef;
    ValueEventListener vel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_realtime_dbactivity);
        database = FirebaseDatabase.getInstance();
        saveUser = findViewById(R.id.saveUser);
        userAge = findViewById(R.id.userAge);
        userLastName = findViewById(R.id.userLastName);
        userName = findViewById(R.id.userName);
        listUsers = findViewById(R.id.listUsers);
        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save data
                saveUserToFB();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        subscribeToChangesFBDB();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(vel!=null){
            myRef.removeEventListener(vel);
        }

    }

    private void saveUserToFB(){
        myRef = database.getReference(FB_USERS_PATH);
        String key = myRef.push().getKey();
        myRef = database.getReference(FB_USERS_PATH+key);
        String name = userName.getText().toString();
        String lastName = userLastName.getText().toString();
        int age = Integer.valueOf(userAge.getText().toString());
        MyUser user = new MyUser(name, lastName, age);
        myRef.setValue(user);
    }

    private void readOnceFBDB(){
        myRef = database.getReference(FB_USERS_PATH);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                    MyUser user = singleSnapshot.getValue(MyUser.class);
                    Log.i(IndexActivity.TAG, user.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void subscribeToChangesFBDB(){
        myRef = database.getReference(FB_USERS_PATH);
        vel = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updateList(snapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateList(DataSnapshot snapshot) {
        listUsers.removeAllViews();
        for(DataSnapshot singleSnapshot : snapshot.getChildren()){
            MyUser user = singleSnapshot.getValue(MyUser.class);
            Log.i(IndexActivity.TAG, user.toString());
            TextView tv = new TextView(this);
            tv.setText(user.toString());
            listUsers.addView(tv);
        }
    }


}