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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class ModCredentials extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Mail = "MailKey";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_credentials);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String m = sharedPreferences.getString(Mail, "Mail");
        List<User> userList = new ArrayList<>();
        readList(userList);

        int index = -999;
        for(int i=0; i<userList.size(); i++){
            if(userList.get(i).getMail().equals(m)){
                index = i;
            }
        }
        TextView textView = findViewById(R.id.prev_mail);
        textView.setText(m);
    }

    public void credChange(View view){

        EditText editText = findViewById(R.id.new_mail);
        String new_mail = editText.getText().toString();
        editText = findViewById(R.id.prev_password);
        String prev_password = editText.getText().toString();
        editText = findViewById(R.id.new_password);
        String new_password = editText.getText().toString();
        editText = findViewById(R.id.conf_new_password);
        String conf_new_password = editText.getText().toString();

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String m = sharedPreferences.getString(Mail, "Mail");
        List<User> userList = new ArrayList<>();
        readList(userList);

        boolean check = false;
        int index = -999;
        for(int i=0; i<userList.size(); i++){
            if(userList.get(i).getMail().equals(m)){
                index = i;
            }
        }

        for(int i=0; i<userList.size();i++){
            if(new_mail.equals(userList.get(i).getMail())){
                check = true;
            }
        }

        if(userList.get(index).getMail().equals(new_mail)){
            Toast.makeText(getApplicationContext(), "New mail can't be equals to the previous one", Toast.LENGTH_SHORT).show();
        }
        else if(check){
            Toast.makeText(getApplicationContext(), "Mail already taken", Toast.LENGTH_SHORT).show();
        }
        else if(!userList.get(index).getPassword().equals(prev_password)){
            Toast.makeText(getApplicationContext(), "Wrong previous password", Toast.LENGTH_SHORT).show();
        }
        else if(new_password.equals(prev_password)){
            Toast.makeText(getApplicationContext(), "New password can't be equals to the previous one", Toast.LENGTH_SHORT).show();
        }
        else if(!new_password.equals(conf_new_password)){
            Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Credentials changed correctly", Toast.LENGTH_SHORT).show();
            if(!new_mail.isEmpty()){
                userList.get(index).setMail(new_mail);
            }
            userList.get(index).setPassword(new_password);
            saveUser(userList);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Mail, new_mail);
            editor.apply();

            finish();
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
}