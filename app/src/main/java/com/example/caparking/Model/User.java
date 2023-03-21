package com.example.caparking.Model;

public class User {
    private String name;
    private String email;
    private String password;
    private String fullname;

    public User() {
    }

    public User(String name,String email,String password,String fullname){
        this.name=name;
        this.email=email;
        this.password=password;
        this.fullname=fullname;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
