package com.example.donapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void loginUser(View view){
        Intent intent = new Intent(this, LoginUser.class);
        startActivity(intent);
    }

    public void regUser(View view){
        Intent intent = new Intent(this, RegisterUser.class);
        startActivity(intent);
    }

    public void enterHosp(View view){
        Intent intent = new Intent(this, HospInterface.class);
        startActivity(intent);
    }
}