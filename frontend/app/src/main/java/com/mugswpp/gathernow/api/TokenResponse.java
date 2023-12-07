package com.mugswpp.gathernow.api;

public class TokenResponse {
    private String message;
    private String user_id;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
