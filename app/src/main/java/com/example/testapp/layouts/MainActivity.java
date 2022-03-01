package com.example.testapp.layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.testapp.R;

public class MainActivity extends AppCompatActivity {
    //log
    public static final String TAG_APP = "TEST_APP";

    //data
    int times = 0;

    //Widgets
    EditText name;
    Button greet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Inflate
        name = findViewById(R.id.name);
        greet = findViewById(R.id.greet);

        //Add Listener to Button
        greet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cadena = name.getText().toString();
                //Toast.makeText(view.getContext(), "Hola "+cadena, Toast.LENGTH_LONG).show();
                times ++;
                Log.i(TAG_APP, "Button pressed "+times+" times");
                Intent intent = new Intent(view.getContext(), HomeActivity.class);
                intent.putExtra("greet", "Hola "+cadena);
                startActivity(intent);
            }
        });


    }
}