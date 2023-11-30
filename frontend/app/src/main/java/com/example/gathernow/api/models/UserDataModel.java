package com.example.gathernow.api.models;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class UserDataModel {
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
    public UserDataModel(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserDataModel(String email, String password){
        this.email = email;
        this.password = password;
    }
    public UserDataModel(String name, String email, Integer user_id, String created_at, String avatar){
        this.name = name;
        this.email = email;
        this.user_id = user_id;
        this.created_at = created_at;
        this.avatar = avatar;

    }

    public UserDataModel() {

    }

    // get name, avatar
    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public Integer getUserId() {
        return user_id;
    }

    // set email, password
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
