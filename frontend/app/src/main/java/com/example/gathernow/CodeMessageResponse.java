package com.example.gathernow;
import com.google.gson.annotations.SerializedName;
public class CodeMessageResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("user_id")
    private Integer user_id;

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    public String setMessage(String message) {
        return message;
    }


    public Integer getUserId(){return user_id;}
}

