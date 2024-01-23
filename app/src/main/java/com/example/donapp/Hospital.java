package com.example.donapp;

public class Hospital {

    private String name, code, phone;

    public Hospital(String nameIn, String codeIn, String phoneIn){
        name = nameIn;
        code = codeIn;
        phone = phoneIn;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getCode() { return code; }

}
