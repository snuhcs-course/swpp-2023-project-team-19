package com.example.gathernow;

import com.google.gson.annotations.SerializedName;

public class UserData1 {
    @SerializedName("name")
    String name;

    @SerializedName("email")
    String email;

    @SerializedName("user_id")
    Integer user_id;

    @SerializedName("created_at")
    String created_at;

    @SerializedName("avatar")
    String avatar;

    public UserData1(String name, String email, Integer user_id, String created_at, String avatar){
        this.name = name;
        this.email = email;
        this.user_id = user_id;
        this.created_at = created_at;
        this.avatar = avatar;

    }
}
