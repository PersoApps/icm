package com.example.testapp.layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.testapp.R;

public class FrameActivity extends AppCompatActivity {
    TextView textFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        textFrame = findViewById(R.id.textFrame);
        String text = getIntent().getStringExtra("name");
        String education = getIntent().getStringExtra("education");
        textFrame.setText(text+"\n"+education);

    }
}