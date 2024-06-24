package com.example.healthdouglas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.healthdouglas.Adapter.HospitalAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class HospitalsResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals_results);
        try {
            RecyclerView recyclerView = findViewById(R.id.recyclerViewHospitals);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ArrayList<Hospital> hospitals = getIntent().getParcelableArrayListExtra("Hospitals");
//            for(Hospital hospital : hospitals){
//                Log.d("Checking Detail Hospital",hospital.toString());
//            }
            if(hospitals != null){
                HospitalAdapter adapter = new HospitalAdapter(hospitals);
                recyclerView.setAdapter(adapter);
            }
        } catch(Exception error) {
            error.printStackTrace();
            Toast.makeText(this,"Connection Error", Toast.LENGTH_SHORT).show();
        }
    }
}