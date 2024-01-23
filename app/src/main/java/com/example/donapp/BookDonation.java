package com.example.donapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookDonation extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Mail = "MailKey";
    public static final String MyDATES = "MyDates";
    public static final String Dates = "DatesKey";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_donation);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String m = sharedPreferences.getString(Mail, "Mail");

        List<User> userList = new ArrayList<>();
        readList(userList);
        int index = 0;
        for(int i=0; i<userList.size(); i++){
            if(userList.get(i).getMail().equals(m)){
                index = i;
            }
        }
        TextView textView = findViewById(R.id.donations);
        textView.setText(userList.get(index).getDonations());
        textView.setMovementMethod(new ScrollingMovementMethod());
    }

    public void book(View view){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String m = sharedPreferences.getString(Mail, "Mail");

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String todayString = dateFormat.format(today);

        List<User> userList = new ArrayList<>();
        readList(userList);

        RadioButton blood = findViewById(R.id.book_blood);
        RadioButton plasma = findViewById(R.id.book_plasma);
        EditText editText = findViewById(R.id.book_date);
        String date = editText.getText().toString();
        TextView textView = findViewById(R.id.donations);

        int index = -999;
        boolean ok = true;

        for(int i=0; i<userList.size(); i++){
            if(userList.get(i).getMail().equals(m)){
                index = i;
            }
        }
        if(getUserYear(todayString)*365+getUserMonth(todayString)*30+getUserDay(todayString) > getUserYear(date)*365+getUserMonth(date)*30+getUserDay(date)){
            ok = false;
            Toast.makeText(getApplicationContext(), "You cannot book a donation for a past day", Toast.LENGTH_SHORT).show();
        }
        if(ok) {
            if (blood.isChecked()) {
                if (getUserYear(date) - getUserYear(userList.get(index).getLast_b()) > 1) {
                    textView.append(date + "  Blood\n");
                } else if (userList.get(index).getSex() == 'M') {
                    if (getUserYear(date)*365+getUserMonth(date)*30+getUserYear(date) - (getUserYear(userList.get(index).getLast_b())*365+getUserMonth(userList.get(index).getLast_b())*30+getUserDay(userList.get(index).getLast_b())) >= 90) {
                        textView.append(date + "  Blood\n");
                    } else {
                        Toast.makeText(getApplicationContext(), "You cannot book a donation before 3 months since the previous one", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (getUserYear(date)*12+getUserMonth(date) - (getUserYear(userList.get(index).getLast_b())*12+getUserMonth(userList.get(index).getLast_b())) >= 6) {
                        textView.append(date + "  Blood\n");
                    } else {
                        Toast.makeText(getApplicationContext(), "You cannot book a donation before 6 months since the previous one", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (plasma.isChecked()) {
                if (getUserYear(date)*365+getUserMonth(date)*30+getUserDay(date) - (getUserYear(userList.get(index).getLast_plasma())*365+getUserMonth(userList.get(index).getLast_plasma())*30+getUserDay(userList.get(index).getLast_plasma())) >= 14){
                    textView.append(date + "  Plasma\n");
                } else {
                    Toast.makeText(getApplicationContext(), "You cannot book a donation before 14 days since the previous one", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (getUserYear(date)*365+getUserMonth(date)*30+getUserDay(date) - (getUserYear(userList.get(index).getLast_plat())*365+getUserMonth(userList.get(index).getLast_plat())*30+getUserDay(userList.get(index).getLast_plat())) >= 14) {
                    textView.append(date + "  Platelets\n");
                } else {
                    Toast.makeText(getApplicationContext(), "You cannot book a donation before 14 days since the previous one", Toast.LENGTH_SHORT).show();
                }
            }
        }
        userList.get(index).setDonations(textView.getText().toString());
        saveUser(userList);
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

}