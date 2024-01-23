package com.example.donapp;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String mail, password, name, surname, CF, birth, phone, last_b, last_plasma, last_plat, prov, b_type, donations;
    private char sex;

    public User(String mailIn, String passwordIn, String nameIn, String surnameIn, String CFIn, String birthIn, char sexIn, String phoneIn, String last_bIn, String last_plasmaIn, String last_platIn, String provIn, String b_typeIn, String donationsIn){
        mail = mailIn;
        password = passwordIn;
        name = nameIn;
        surname = surnameIn;
        CF = CFIn;
        birth = birthIn;
        sex = sexIn;
        phone = phoneIn;
        last_b = last_bIn;
        last_plasma = last_plasmaIn;
        last_plat = last_platIn;
        prov = provIn;
        b_type = b_typeIn;
        donations = donationsIn;
    }

    public String getMail(){
        return mail;
    }
    public void setMail(String mailIN) {
        mail = mailIN;
    }

    public String getPassword() { return password; }
    public void setPassword(String passwordIN){
        password = passwordIN;
    }

    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getCF() { return CF; }
    public String getBirth() { return birth; }
    public char getSex() { return sex; }

    public String getPhone() { return phone; }
    public void setPhone(String phoneIN) {
        phone = phoneIN;
    }

    public String getLast_b() { return last_b; }
    public void setLast_b(String last_bIN) {
        last_b = last_bIN;
    }

    public String getLast_plasma() { return last_plasma; }
    public void setLast_plasma(String last_plasmaIN) {
        last_plasma = last_plasmaIN;
    }

    public String getLast_plat() { return last_plat; }
    public void setLast_plat(String last_platIN) {
        last_plat = last_platIN;
    }

    public String getProv() { return prov; }
    public void setProv(String provIN) {
        prov = provIN;
    }

    public String getB_type() { return b_type; }

    public String getDonations(){
        return donations;
    }
    public void setDonations(String d){
        donations = d;
    }


    public String getUser() {
        return mail +" "+ password +" "+ name +" "+ surname +" "+ CF +" "+ birth +" "+ sex +" "+ phone +" "+ last_b +" "+ last_plasma +" "+ last_plat +" "+ prov +" "+ b_type;
    }

    public String getSoftUser() {
        return name + " " + surname + " " + CF + " " + b_type + " " + phone + "\n";
    }
}
