package com.example.donapp;

import java.io.Serializable;

public class User implements Serializable {
    private String mail, password, name, surname, CF, birth, phone, last_b, last_plasma, last_plat, prov, b_type;
    private char sex;

    public User(String mailIn, String passwordIn, String nameIn, String surnameIn, String CFIn, String birthIn, char sexIn, String phoneIn, String last_bIn, String last_plasmaIn, String last_platIn, String provIn, String b_typeIn){
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
    }

    public String getMail(){
        return mail;
    }
}
