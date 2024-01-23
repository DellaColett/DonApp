package com.example.donapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HospInterface extends AppCompatActivity {

    public static final String MyHOSP = "MyHosp";
    public static final String Code = "CodeKey";
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_interface);

        Spinner blood_spinner = (Spinner) findViewById(R.id.b_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.blood_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blood_spinner.setAdapter(adapter);

        TextView textView = findViewById(R.id.list_donors);
        textView.setMovementMethod(new ScrollingMovementMethod());
    }

    public void searchBtn(View view){
        shared = getSharedPreferences(MyHOSP, MODE_PRIVATE);
        String code = shared.getString(Code, "code");
        List<User> userList = new ArrayList<>();
        readList(userList);

        Spinner blood_spinner = findViewById(R.id.b_type_spinner);

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String todayString = dateFormat.format(today);

        TextView textView = findViewById(R.id.list_donors);
        List<User> s = new ArrayList<>();

        RadioButton blood = findViewById(R.id.radio_blood);
        RadioButton plasma = findViewById(R.id.radio_plasma);

        RadioButton all = findViewById(R.id.radio_all);
        String temp = "";

        if(all.isChecked()){
            for(int i=0; i<userList.size(); i++){
                if(code.contains(userList.get(i).getProv())){
                    s.add( userList.get(i));
                }
            }
        }
        else{
            if(blood.isChecked()){
                for(int i=0; i<userList.size(); i++){
                    if(code.contains(userList.get(i).getProv())){
                        if(getUserYear(todayString) - getUserYear(userList.get(i).getLast_b()) > 1){
                            s.add(userList.get(i));
                        }
                        else if(userList.get(i).getSex() == 'M'){
                            if(getUserYear(todayString)*365 + getUserMonth(todayString)*30 + getUserDay(todayString) - getUserYear(userList.get(i).getLast_b())*365 - getUserMonth(userList.get(i).getLast_b())*30 - getUserDay(userList.get(i).getLast_b()) >= 90){
                                s.add(userList.get(i));
                            }
                        }
                        else{
                            if(getUserYear(todayString)*365+getUserMonth(todayString)*30+getUserDay(todayString) - (getUserYear(userList.get(i).getLast_b())*365+getUserMonth(userList.get(i).getLast_b())*30+getUserDay(userList.get(i).getLast_b())) >= 180) {
                                s.add(userList.get(i));
                            }
                        }
                    }
                }
            }
            else if(plasma.isChecked()){
                for(int i=0; i<userList.size(); i++){
                    if(code.contains(userList.get(i).getProv())) {
                        if(getUserYear(todayString)*365+getUserMonth(todayString)*30+getUserDay(todayString) - (getUserYear(userList.get(i).getLast_plasma())*365+getUserMonth(userList.get(i).getLast_plasma())*30+getUserDay(userList.get(i).getLast_plasma())) >= 14){
                            s.add(userList.get(i));
                        }
                    }
                }
            }
            else{
                for(int i=0; i<userList.size(); i++){
                    if(code.contains(userList.get(i).getProv())) {
                        if(getUserYear(todayString)*365+getUserMonth(todayString)*30+getUserDay(todayString) - (getUserYear(userList.get(i).getLast_plat())*365+getUserMonth(userList.get(i).getLast_plat())*30+getUserDay(userList.get(i).getLast_plat())) >= 14){
                            s.add(userList.get(i));
                        }
                    }
                }
            }
        }
        int index = blood_spinner.getSelectedItemPosition();
        String b_type = "";
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
        for (int i = 0; i<s.size(); i++){
            if(s.get(i).getB_type().equals(b_type)){
                temp += s.get(i).getSoftUser();
            }
        }
        textView.setText(temp);
    }

    public static int getUserYear(String s){
        int year;
        char[] c = new char[4];
        s.getChars(6, 10, c, 0);
        String temp = String.valueOf(c[0]) + String.valueOf(c[1]) + String.valueOf(c[2]) + String.valueOf(c[3]);
        year = Integer.parseInt(temp);
        return year;
    }

    public static int getUserMonth(String s){
        int month;
        char[] c = new char[2];
        s.getChars(3, 5, c, 0);
        String temp = String.valueOf(c[0]) + String.valueOf(c[1]);
        month = Integer.parseInt(temp);
        return month;
    }

    public static int getUserDay(String s){
        int day;
        char[] c = new char[2];
        s.getChars(0, 2, c, 0);
        String temp = String.valueOf(c[0]) + String.valueOf(c[1]);
        day = Integer.parseInt(temp);
        return day;
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