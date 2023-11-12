package com.example.gathernow.log_in;

public class AuthorizationRepository {
    private final LogInDataSource logInDataSource;

    public AuthorizationRepository(LogInDataSource logInDataSource) {
        this.logInDataSource = logInDataSource;

    }
    public void login(String email, String password, LogInCallback callback) {
        logInDataSource.logIn(email, password, callback);
    }


}
