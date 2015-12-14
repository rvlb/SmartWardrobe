package com.arara.smartwardrobe;

public class User {

    String name, password, passwordRep;

    public User(String name) {
        this.name = name;
        this.password = "";
        this.passwordRep = "";
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.passwordRep = "";
    }

    public User(String name, String password, String passwordRep) {
        this.name = name;
        this.password = password;
        this.passwordRep = passwordRep;
    }
}
