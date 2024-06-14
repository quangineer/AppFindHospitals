package com.example.healthdouglas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","").toString();
        Toast.makeText(getApplicationContext(),"Welcome, " + username,Toast.LENGTH_SHORT).show();

        CardView ExitCard = findViewById(R.id.cardExit);
        ExitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1st: create an object of type SharedPreferences:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //clear username 1st b4 logout:
                editor.clear();
                editor.apply();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            }
        });

        CardView FindHospitalsNearYou = findViewById(R.id.cardFindHospitalsNearYou);
        FindHospitalsNearYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,FindHospitalsNearYourActivity.class));
            }
        });

        CardView NutritionAnalysis = findViewById(R.id.cardNutritionAnalysis);
        NutritionAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,NutritionAnalysisActivity.class));
            }
        });

        CardView HealthArticles = findViewById(R.id.cardHealthArticles);
        HealthArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,HealthArticlesActivity.class));
            }
        });


    }
}