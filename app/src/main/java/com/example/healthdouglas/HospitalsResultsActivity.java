package com.example.healthdouglas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HospitalsResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals_results);
        try {
            Bundle bundle = getIntent().getExtras();

            ArrayList<String> PlaceNames;
            ArrayList<String> PlaceVicinity;

            PlaceNames = (ArrayList<String>) bundle.getSerializable("ShowName");
            PlaceVicinity = (ArrayList<String>) bundle.getSerializable("ShowVicinity");



        } catch(Exception error) {
            error.printStackTrace();
            Toast.makeText(this,"Connection Error", Toast.LENGTH_SHORT).show();
        }
    }
}