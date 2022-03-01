package com.example.testapp.layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.model.Pais;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ArrayList<Pais> paises = new ArrayList<>();
    String[] data;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initPaises();
        initArray(50);
        list = findViewById(R.id.listView);
        ArrayAdapter<Pais> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paises);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), position + " " + list.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initArray(int size) {
        data = new String[size];
        for (int i = 0; i < size; i++) {
            data[i] = i % 2 == 0 ? "hola" : "mundo";
        }
    }

    private void initPaises() {
        JSONObject json = null;
        try {
            json = new JSONObject(loadJSONFromAsset());
            JSONArray paisesJsonArray = json.getJSONArray("paises");
            for (int i = 0; i < paisesJsonArray.length(); i++) {
                JSONObject jsonObject = paisesJsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre_pais");
                String capital = jsonObject.getString("nombre_pais");
                String sigla = jsonObject.getString("sigla");
                String nombreInt = jsonObject.getString("nombre_pais_int");
                this.paises.add(new Pais(nombre, capital, sigla, nombreInt ));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("paises.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}