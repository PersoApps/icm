package com.example.testapp.layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.testapp.R;

public class RelativeActivity extends AppCompatActivity {
    Spinner spinner;
    EditText name;
    Button frame, web, list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_relative);
        spinner = findViewById(R.id.spinner);
        name = findViewById(R.id.nameR);
        frame = findViewById(R.id.frame);
        web =  findViewById(R.id.web);
        list = findViewById(R.id.list);

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RelativeActivity.this, WebActivity.class));
            }
        });

        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelativeActivity.this, FrameActivity.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("education", spinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });


        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RelativeActivity.this, ListActivity.class));
            }
        });






    }
}