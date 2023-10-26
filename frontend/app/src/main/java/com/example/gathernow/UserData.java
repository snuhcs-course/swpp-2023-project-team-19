package com.example.gathernow;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("name")
    String name;

    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    // SignUp
    public UserData(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserData(String email, String password){
        this.email = email;
        this.password = password;
    }
}
