package com.mugswpp.gathernow.main_ui;

import android.util.Log;

import com.mugswpp.gathernow.api.RetrofitClient;
import com.mugswpp.gathernow.api.ServiceApi;
import com.mugswpp.gathernow.api.models.UserDataModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRemoteDataSource {
    private final ServiceApi service;
    public UserRemoteDataSource() {
        this.service = RetrofitClient.getClient().create(ServiceApi.class);
    }

    public void getUserInfo(int userId, CallbackInterface userCallback) {
        service.getUserInfo(userId).enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                if (response.isSuccessful()) {
                    UserDataModel result = response.body();
                    if (result != null) {
                        Log.d("UserRemoteDataSource", "Loaded user info");
                        userCallback.onSuccess(result);
                    } else {
                        userCallback.onError("The user info is empty");
                    }
                } else {
                    userCallback.onError("Failed to get user info");
                }
            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                userCallback.onError("Network error");
            }
        });
    }
}
