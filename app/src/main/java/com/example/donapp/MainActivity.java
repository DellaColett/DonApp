package com.example.donapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String MyHOSP = "MyHosp";
    public static final String Code = "CodeKey";
    SharedPreferences shared;

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

        final Hospital[] hospitals = new Hospital[7];
        hospitals[0] = new Hospital("Gemelli di Roma", "RM-0001", "06 30157262");
        hospitals[1] = new Hospital("Belcolle di Viterbo", "VT-0002", "0761 338621");
        hospitals[2] = new Hospital("Riuniti di Foggia", "FG-0003", "0881 732189");
        hospitals[3] = new Hospital("Cardarelli di Napoli", "NA-0004", "081 7472489");
        hospitals[4] = new Hospital("Policlinico di Milano", "MI-0005", "02 55034306");
        hospitals[5] = new Hospital("Molinette di Torino", "TO-0006", "011 6334101");
        hospitals[6] = new Hospital("S.Orsola di Bologna", "BO-0007", "051 2143539");

        shared = getSharedPreferences(MyHOSP, MODE_PRIVATE);

        EditText editText = findViewById(R.id.hosp_code);
        String h_code = editText.getText().toString();
        boolean ok = false;

        for(Hospital hospital: hospitals){
            if(h_code.equals(hospital.getCode())){
                ok = true;
                break;
            }
        }

        if(ok){
            SharedPreferences.Editor editor = shared.edit();
            editor.putString(Code, h_code);
            editor.apply();

            Intent intent = new Intent(this, HospInterface.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Invalid code", Toast.LENGTH_SHORT).show();
        }
    }

}