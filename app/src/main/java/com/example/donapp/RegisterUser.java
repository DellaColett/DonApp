package com.example.donapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RegisterUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        Spinner sex_spinner = (Spinner) findViewById(R.id.sex_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sex_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex_spinner.setAdapter(adapter);

        Spinner prov_spinner = (Spinner) findViewById(R.id.province_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.province_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prov_spinner.setAdapter(adapter);

        Spinner blood_spinner = (Spinner) findViewById(R.id.blood_type_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.blood_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blood_spinner.setAdapter(adapter);
    }
    public void userInt(View view){
        boolean confirm = false;
        char sex = 'O';
        String b_type = "", prov = "";

        List<User> userList = new ArrayList<>();
        readList(userList);

        EditText editText = (EditText) findViewById(R.id.reg_mail);
        String mail = editText.getText().toString();

        editText = (EditText) findViewById(R.id.reg_password);
        String password = editText.getText().toString();
        editText = (EditText) findViewById(R.id.reg_confirm_pass);
        String pass_conf = editText.getText().toString();
        if(pass_conf.equals(password)){
            confirm = true;
        }
        else{
            Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
        }

        editText = (EditText) findViewById(R.id.reg_name);
        String name = editText.getText().toString();
        editText = (EditText) findViewById(R.id.reg_surname);
        String surname = editText.getText().toString();
        editText = (EditText) findViewById(R.id.reg_CF);
        String CF = editText.getText().toString();
        editText = (EditText) findViewById(R.id.reg_birth);
        String birth = editText.getText().toString();
        editText = (EditText) findViewById(R.id.reg_phone);
        String phone = editText.getText().toString();
        editText = (EditText) findViewById(R.id.reg_last_b);
        String last_b = editText.getText().toString();
        editText = (EditText) findViewById(R.id.reg_last_plasma);
        String last_plasma = editText.getText().toString();
        editText = (EditText) findViewById(R.id.reg_last_plat);
        String last_plat = editText.getText().toString();
        Spinner sex_spinner = (Spinner) findViewById(R.id.sex_spinner);
        Spinner prov_spinner = (Spinner) findViewById(R.id.province_spinner);
        Spinner blood_spinner = (Spinner) findViewById(R.id.blood_type_spinner);
        int index = sex_spinner.getSelectedItemPosition();
        switch (index){
            case 0:
                sex = 'M';
                break;
            case 1:
                sex = 'F';
                break;
            case 2:
                sex = 'O';
                break;
        }
        index = prov_spinner.getSelectedItemPosition();
        switch(index){
            case 0:
                prov = "BO";
                break;
            case 1:
                prov = "FG";
                break;
            case 2:
                prov = "MI";
                break;
            case 3:
                prov = "NA";
                break;
            case 4:
                prov = "TO";
                break;
            case 5:
                prov = "RM";
                break;
            case 6:
                prov = "VT";
                break;
        }
        index = blood_spinner.getSelectedItemPosition();
        switch(index){
            case 0:
                b_type = "0-";
                break;
            case 1:
                b_type = "0+";
                break;
            case 2:
                b_type = "A-";
                break;
            case 3:
                b_type = "A+";
                break;
            case 4:
                b_type = "B-";
                break;
            case 5:
                b_type = "B+";
                break;
            case 6:
                b_type = "AB-";
                break;
            case 7:
                b_type = "AB+";
                break;
        }

        for(int i = 0; i < userList.size(); i++){
            if (userList.get(i).getMail().equals(mail)){
                confirm = false;
                Toast.makeText(getApplicationContext(), "Mail already exists!", Toast.LENGTH_SHORT).show();
            }
        }
        if(mail.isEmpty() || name.isEmpty() || surname.isEmpty() || CF.isEmpty() || birth.isEmpty() || phone.isEmpty()){
            Toast.makeText(getApplicationContext(), "Mandatory info missing", Toast.LENGTH_SHORT).show();
            confirm = false;
        }

        if(last_b.isEmpty()){
            last_b = birth;
        }
        if(last_plasma.isEmpty()){
            last_plasma = birth;
        }
        if(last_plat.isEmpty()){
            last_plat = birth;
        }

        if(confirm){
            String don = null;
            User user = new User(mail, password, name, surname, CF, birth, sex, phone, last_b, last_plasma, last_plat, prov, b_type, don);
            userList.add(user);
            saveUser(userList);
            Intent intent = new Intent(this, LoginUser.class);
            startActivity(intent);
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