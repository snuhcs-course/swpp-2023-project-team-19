package com.example.gathernow.api.models;

import com.google.gson.annotations.SerializedName;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    MultipartBody.Part avatarPart;
    RequestBody namePart;
    RequestBody emailPart;
    RequestBody passwordPart;

    // SignUp
    public UserDataModel(String name, String email, String password, String avatarFilePath){
//        this.name = name;
//        this.email = email;
//        this.password = password;
        if (avatarFilePath != null) {
            File avatarFile = new File(avatarFilePath);
            RequestBody avatarBody = RequestBody.create(MediaType.parse("image/*"), avatarFile);
            avatarPart = MultipartBody.Part.createFormData("avatar", avatarFile.getName(), avatarBody);
        }
        this.namePart = RequestBody.create(MediaType.parse("text/plain"), name);
        this.emailPart = RequestBody.create(MediaType.parse("text/plain"), email);
        this.passwordPart = RequestBody.create(MediaType.parse("text/plain"), password);
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

    public void setAvatarPart(MultipartBody.Part avatarPart) {
        this.avatarPart = avatarPart;
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

    // set name, avatar
    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
