package com.example.donapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

public class HospContact extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Mail = "MailKey";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_contact);

        final Hospital[] hospitals = new Hospital[7];
        hospitals[0] = new Hospital("Gemelli di Roma", "RM-0001", "06 30157262");
        hospitals[1] = new Hospital("Belcolle di Viterbo", "VT-0002", "0761 338621");
        hospitals[2] = new Hospital("Riuniti di Foggia", "FG-0003", "0881 732189");
        hospitals[3] = new Hospital("Cardarelli di Napoli", "NA-0004", "081 7472489");
        hospitals[4] = new Hospital("Policlinico di Milano", "MI-0005", "02 55034306");
        hospitals[5] = new Hospital("Molinette di Torino", "TO-0006", "011 6334101");
        hospitals[6] = new Hospital("S.Orsola di Bologna", "BO-0007", "051 2143539");

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
        TextView h_name = findViewById(R.id.hosp_name);
        TextView h_phone = findViewById(R.id.hosp_phone);
        String prov = userList.get(index).getProv();
        for (Hospital hospital : hospitals) {
            if (hospital.getCode().contains(prov)) {
                h_name.setText(hospital.getName());
                h_phone.setText(hospital.getPhone());
            }
        }
    }

    public void callHosp(View view){
        TextView textView = findViewById(R.id.hosp_phone);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+textView.getText().toString()));
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