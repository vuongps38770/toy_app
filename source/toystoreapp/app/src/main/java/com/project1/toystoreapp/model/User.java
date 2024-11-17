package com.project1.toystoreapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("_id")
    private String id;
    private String username;
    private String password;
    private String phonenumber;
    private String email;
    private int role;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String phonenumber, String password) {
        this.phonenumber = phonenumber;
        this.password = password;
    }

    public User(String username, String password, String phonenumber, String email, int role) {
        this.username = username;
        this.password = password;
        this.phonenumber = phonenumber;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

}

