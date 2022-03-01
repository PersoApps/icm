package com.example.testapp.layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.testapp.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    //data
    ArrayList<Long> serie = new ArrayList<>();

    //Views
    Button next;
    LinearLayout fiboList;
    ScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //inflate
        next = findViewById(R.id.fibo);
        fiboList = findViewById(R.id.fibolist);
        scroll = findViewById(R.id.scroll);

        initFibonacci();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long next = getNext();
                serie.add(next);
                fiboList.addView(createView(next));
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void initFibonacci(){
        serie.add(new Long(0));
        serie.add(new Long(1));
        fiboList.addView(createView(0));
        fiboList.addView(createView(1));
    }

    private TextView createView(long value){
        TextView view = new TextView(this);
        view.setText(String.valueOf(value));
        view.setTextSize(20);
        view.setTextColor(getResources().getColor(R.color.azulito, null));
        return view;
    }

    private long getNext(){
        int index = serie.size()-1;
        return serie.get(index)+serie.get(index-1);
     }
}