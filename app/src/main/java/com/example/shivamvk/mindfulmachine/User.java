package com.example.shivamvk.mindfulmachine;

public class User {
    String name,email,number,emailverified,numberverified,usertype,companyname,address;


    public User(String name, String email, String number, String emailverified, String numberverified, String usertype, String companyname, String address) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.emailverified = emailverified;
        this.numberverified = numberverified;
        this.usertype = usertype;
        this.companyname = companyname;
        this.address = address;
    }

    public String getCompanyname() {
        return companyname;
    }

    public String getAddress() {
        return address;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }


    public String getEmailverified() {
        return emailverified;
    }

    public String getNumberverified() {
        return numberverified;
    }

    public String getUsertype() {
        return usertype;
    }
}

