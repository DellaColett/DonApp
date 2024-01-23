package com.example.donapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class PersonalData extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Mail = "MailKey";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String m = sharedPreferences.getString(Mail, "Mail");
        List<User> userList = new ArrayList<>();
        readList(userList);

        Spinner prov_spinner = findViewById(R.id.prov_spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.province_array1, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prov_spinner.setAdapter(adapter);

        int index = -999;
        boolean ok = false;

        for(int i=0; i<userList.size(); i++){
            if(userList.get(i).getMail().equals(m)){
                ok = true;
                index = i;
            }
        }
        if(ok){
            TextView textView = findViewById(R.id.user_name);
            textView.setText(userList.get(index).getName());
            textView = findViewById(R.id.user_surname);
            textView.setText(userList.get(index).getSurname());
            textView = findViewById(R.id.user_birthday);
            textView.setText(userList.get(index).getBirth());
            textView = findViewById(R.id.user_CF);
            textView.setText(userList.get(index).getCF());
            textView = findViewById(R.id.user_sex);
            String c = String.valueOf(userList.get(index).getSex());
            textView.setText(c);
            textView = findViewById(R.id.user_blood_type);
            textView.setText(userList.get(index).getB_type());
            EditText editText = findViewById(R.id.user_phone);
            editText.setText(userList.get(index).getPhone());
            textView = findViewById(R.id.user_province);
            textView.setText(userList.get(index).getProv());
            editText = findViewById(R.id.user_last_blood);
            editText.setText(userList.get(index).getLast_b());
            editText = findViewById(R.id.user_last_plasma);
            editText.setText(userList.get(index).getLast_plasma());
            editText = findViewById(R.id.user_last_platelets);
            editText.setText(userList.get(index).getLast_plat());
        }
    }

    public void confChanges(View view){
        Spinner prov_spinner = findViewById(R.id.prov_spinner1);
        int j = prov_spinner.getSelectedItemPosition();

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String m = sharedPreferences.getString(Mail, "Mail");
        List<User> userList = new ArrayList<>();
        readList(userList);

        int index = -999;

        for(int i=0; i<userList.size(); i++){
            if(userList.get(i).getMail().equals(m)){
                index = i;
            }
        }
        EditText editText = findViewById(R.id.user_phone);
        userList.get(index).setPhone(editText.getText().toString());
        editText = findViewById(R.id.user_last_blood);
        userList.get(index).setLast_b(editText.getText().toString());
        editText = findViewById(R.id.user_last_plasma);
        userList.get(index).setLast_plasma(editText.getText().toString());
        editText = findViewById(R.id.user_last_platelets);
        userList.get(index).setLast_plat(editText.getText().toString());
        switch(j){
            case 0:
                break;
            case 1:
                userList.get(index).setProv("BO");
                break;
            case 2:
                userList.get(index).setProv("FG");
                break;
            case 3:
                userList.get(index).setProv("MI");
                break;
            case 4:
                userList.get(index).setProv("NA");
                break;
            case 5:
                userList.get(index).setProv("TO");
                break;
            case 6:
                userList.get(index).setProv("RM");
                break;
            case 7:
                userList.get(index).setProv("VT");
                break;
        }
        saveUser(userList);
        finish();
    }

    public void readList(List<User> userListIn){
        boolean endOfFile = false;
        User tempUser;
        try(
                FileInputStream userFile = openFileInput("User.dat");
                ObjectInputStream userStream = new ObjectInputStream(userFile)
        ){
            tempUser = (User) userStream.readObject();
            while(!endOfFile){
                try {
                    userListIn.add(tempUser);
                    tempUser = (User) userStream.readObject();
                }
                catch (EOFException e){
                    endOfFile = true;
                }
            }
        }
        catch (FileNotFoundException e){
            Toast.makeText(getApplicationContext(),"File Not Found!",Toast.LENGTH_SHORT).show();
        }
        catch (ClassNotFoundException e){
            Toast.makeText(getApplicationContext(),"Class Not Found!",Toast.LENGTH_SHORT).show();
        }
        catch (StreamCorruptedException e){
            Toast.makeText(getApplicationContext(),"Corrupted Stream!",Toast.LENGTH_SHORT).show();
        }
        catch (IOException e){
            Toast.makeText(getApplicationContext(),"I/O exception in read!",Toast.LENGTH_SHORT).show();
        }
    }
    public void saveUser(List<User> userListIn) {
        try(
                FileOutputStream userFile = openFileOutput("User.dat", MODE_PRIVATE);
                ObjectOutputStream userStream = new ObjectOutputStream(userFile)
        ){
            for (User item : userListIn){
                userStream.writeObject(item);
            }
        }
        catch (IOException e){
            Toast.makeText(getApplicationContext(), "I/O exception in write!", Toast.LENGTH_SHORT).show();
        }
    }
}