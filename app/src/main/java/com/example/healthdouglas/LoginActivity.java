package com.example.healthdouglas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthdouglas.database.DatabaseUserInfo;


public class LoginActivity extends AppCompatActivity {

    EditText edUserName, edPassword;
    Button btnLogin;
    TextView NewRegisterHere;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUserName = findViewById(R.id.editTextAppFullName);
        edPassword = findViewById(R.id.editTextLoginPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        NewRegisterHere = findViewById(R.id.textViewExistingUser);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edUserName.getText().toString();
                String password = edPassword.getText().toString();
                DatabaseUserInfo DBInfo = new DatabaseUserInfo(getApplicationContext(),"hc_db",null,1);
                if(username.length()==0 || password.length()==0){
                    Log.d("Check1","No User Input");
                    Toast.makeText(getApplicationContext(),"Insert User Info",Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("UP",username + " " + password);

                    Boolean a = DBInfo.login(username,password)==1;
                    Log.d("DOWN",a.toString());
                    if(DBInfo.login(username,password)==1){
                        Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_LONG).show();
                        //store to data memory when login:
                        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username",username);
                        //save data with key and value:
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    } else{
                        Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        NewRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }
}