package com.mugswpp.gathernow.main_ui;

public class UserRemoteRepository {
    private final UserRemoteDataSource userRemoteDataSource;
    public UserRemoteRepository(UserRemoteDataSource userRemoteDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
    }

    public void getUserInfo(int userId, CallbackInterface callbackInterface) {
        userRemoteDataSource.getUserInfo(userId, callbackInterface);
    }
}
