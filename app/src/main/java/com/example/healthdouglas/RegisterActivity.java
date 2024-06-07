package com.example.healthdouglas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthdouglas.database.DatabaseUserInfo;

public class RegisterActivity extends AppCompatActivity {

    EditText EditUserName, EditEmail, EditPassword, EditConfirmPassword;
    TextView GoBackLogin;
    Button BtnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditUserName = findViewById(R.id.editTextAppFullName);
        EditEmail = findViewById(R.id.editTextAppAddress);
        EditPassword = findViewById(R.id.editTextAppContactNumber);
        EditConfirmPassword = findViewById(R.id.editTextAppFees);
        BtnRegister = findViewById(R.id.buttonBookAppointment);
        GoBackLogin = findViewById(R.id.textViewExistingUser);

        GoBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserName = EditUserName.getText().toString();
                String Email = EditEmail.getText().toString();
                String Password = EditPassword.getText().toString();
                String ConfirmPassword = EditConfirmPassword.getText().toString();
                DatabaseUserInfo DB = new DatabaseUserInfo(getApplicationContext(),"DouglasCare",null,1);

                if(Email.length() !=0){
                    if(emailValid(Email)){}
                    else{
                        Toast.makeText(getApplicationContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
                    };
                }

                if(UserName.length()==0 || Email.length()==0 || Password.length()==0 || ConfirmPassword.length()==0){
                    Toast.makeText(getApplicationContext(),"Please fill the blanks",Toast.LENGTH_SHORT).show();
                } else{
                    if(Password.compareTo(ConfirmPassword)==0){
                        if(passwordValidate(Password)){
                            DB.register(UserName,Email,Password);
                            Toast.makeText(getApplicationContext(),"Successfully Recorded Profile",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Password must contain at least 8 combined characters,letters,digits and special symbols", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Password does not match Confirmed Password",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public static boolean emailValid(String emailReview){
        boolean hasAtSymbol = emailReview.contains("@");
        boolean hasMailWord = emailReview.toLowerCase().contains("mail");
        return hasMailWord && hasAtSymbol;
    }

    public static boolean passwordValidate(String passwordReview){
        int f1=0,f2=0,f3=0;
        if(passwordReview.length() < 8){
            return false;
        } else {
            for(int i =0;i<passwordReview.length();i++){
                if(Character.isLetter(passwordReview.charAt(i))){
                    f1=1;
                }
            }
            for(int k=0;k<passwordReview.length();k++){
                if(Character.isDigit(passwordReview.charAt(k))){
                    f2=1;
                }
            }
            for(int z=0;z<passwordReview.length();z++){
                char SpecialChar = passwordReview.charAt(z);
                if(SpecialChar>=33 && SpecialChar<=46 || SpecialChar==64){
                    f3=1;
                }
            }
            if(f1==1 && f2==1 && f3==1){
                return true;
            } else {return false;}
        }
    }
}