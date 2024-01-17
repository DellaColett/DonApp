package com.example.donapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserInterface extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);
    }

    public void persData(View view){
        Intent intent = new Intent(this, PersonalData.class);
        startActivity(intent);
    }
    public void modCred(View view){
        Intent intent = new Intent(this, ModCredentials.class);
        startActivity(intent);
    }
    public void bookDon(View view){
        Intent intent = new Intent(this, BookDonation.class);
        startActivity(intent);
    }
    public void hospData(View view){
        Intent intent = new Intent(this, HospContact.class);
        startActivity(intent);
    }
}