package com.example.gathernow;

import com.google.gson.annotations.SerializedName;

import java.io.File;

import okhttp3.MultipartBody;

public class UserData {
    @SerializedName("name")
    String name;

    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    @SerializedName("user_id")
    Integer user_id;

    @SerializedName("created_at")
    String created_at;

    @SerializedName("avatar")
    String avatar;

    transient MultipartBody.Part avatarPart;
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
    public UserData(String name, String email, Integer user_id, String created_at, String avatar){
        this.name = name;
        this.email = email;
        this.user_id = user_id;
        this.created_at = created_at;
        this.avatar = avatar;

    }
}
