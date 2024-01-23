package com.example.donapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
    public void browse(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bologna.avisemiliaromagna.it/wp-content/uploads/2020/04/Questionario-Anamnestico2020.pdf"));
        startActivity(intent);
    }
}