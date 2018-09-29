package com.example.shivamvk.mindfulmachine;

public class User {
    String name,email,number,emailverified,numberverified,usertype,alternatenumber;


    public User(String name, String email, String number, String alternatenumber, String emailverified, String numberverified, String usertype) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.emailverified = emailverified;
        this.numberverified = numberverified;
        this.usertype = usertype;
        this.alternatenumber = alternatenumber;
    }


    public String getAlternatenumber() {
        return alternatenumber;
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

