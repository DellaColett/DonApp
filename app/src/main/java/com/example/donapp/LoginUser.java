package com.example.donapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class LoginUser extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Mail = "MailKey";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

    }
    public void userInt(View view){

        List<User> userList = new ArrayList<>();
        readList(userList);

        boolean ok = false;
        int index = -999;

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        EditText editText = (EditText) findViewById(R.id.login_mail);
        String mail = editText.getText().toString();

        editText = (EditText) findViewById(R.id.login_password);
        String password = editText.getText().toString();

        for(int i=0;i<userList.size();i++) {
            if (mail.equals(userList.get(i).getMail())) {
                ok = true;
                index = i;
            }
        }
        if(ok){
            if(!password.equals(userList.get(index).getPassword())) {
                ok = false;
            }
        }

        if(ok) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Mail, mail);
            editor.apply();

            Intent intent = new Intent(this, UserInterface.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Mail doesn't exist or wrong password!", Toast.LENGTH_SHORT).show();
        }
    }

    public void forgPass(View view){
        Intent intent = new Intent(this, ForgottenPassword.class);
        startActivity(intent);
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