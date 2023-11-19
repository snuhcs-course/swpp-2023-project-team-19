package com.example.gathernow.authenticate.login;

import com.example.gathernow.authenticate.AuthCallback;

public class LoginRepository {
    private final LogInDataSource logInDataSource;

    public LoginRepository(LogInDataSource logInDataSource) {
        this.logInDataSource = logInDataSource;

    }
    public void login(String email, String password, AuthCallback callback) {
        logInDataSource.logIn(email, password, callback);
    }



}
