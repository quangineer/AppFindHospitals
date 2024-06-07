package com.example.healthdouglas.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseUserInfo extends SQLiteOpenHelper {
    public DatabaseUserInfo(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateQueryTable = "create table DouglasCustomers(username text, email text, password text)";
        db.execSQL(CreateQueryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void register(String username, String email, String password){
        ContentValues CV = new ContentValues();
        CV.put("username",username);
        CV.put("email",email);
        CV.put("password",password);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("DouglasCustomers",null,CV);
        db.close();
    }

    public int login(String username, String password){
        int result = 0;
        String str[] = new String[2];
        str[0] = username;
        str[1] = password;
        SQLiteDatabase db = getReadableDatabase();
        Cursor C = db.rawQuery("select * from DouglasCustomers where username=? and password=?",str);
        if(C.moveToFirst()){
            result = 1;
        }
        return result;
    }
}
