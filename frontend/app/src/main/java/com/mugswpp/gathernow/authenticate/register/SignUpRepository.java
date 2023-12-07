package com.mugswpp.gathernow.authenticate.register;

import com.mugswpp.gathernow.authenticate.AuthCallback;

public class SignUpRepository {
    private final SignUpDataSource signUpDataSource;

    public SignUpRepository(SignUpDataSource signUpDataSource) {
        this.signUpDataSource = signUpDataSource;

    }
    public void register(String name, String email, String password, String avatarFilePath, AuthCallback callback) {
        signUpDataSource.register(name, email, password, avatarFilePath, callback);
    }
}
