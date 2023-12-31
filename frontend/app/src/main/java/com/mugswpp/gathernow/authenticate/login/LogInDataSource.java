package com.mugswpp.gathernow.authenticate.login;

import android.content.Context;
import android.util.Log;

import com.mugswpp.gathernow.api.models.UserDataModel;
import com.mugswpp.gathernow.api.CodeMessageResponse;
import com.mugswpp.gathernow.api.RetrofitClient;
import com.mugswpp.gathernow.api.ServiceApi;
import com.mugswpp.gathernow.api.models.UserDataModelBuilder;
import com.mugswpp.gathernow.authenticate.AuthCallback;
import com.mugswpp.gathernow.authenticate.UserLocalDataSource;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInDataSource {
    private ServiceApi service;
    private Context context;
    public LogInDataSource(Context context) {
        this.context = context;
    }

    public void logIn(String email, String password, AuthCallback callback) {
        service = RetrofitClient.getClient().create(ServiceApi.class);
        UserDataModel requestData = new UserDataModelBuilder()
                .setEmail(email)
                .setPassword(password)
                .build();
        service.userLogIn(requestData).enqueue(new Callback<CodeMessageResponse>() {
            @Override
            public void onResponse(Call<CodeMessageResponse> call, Response<CodeMessageResponse> response) {
                // Login successful
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    CodeMessageResponse result = response.body();
                    if (result != null) {
                        String message = result.getMessage();
                        if ("Login successful.".equals(message)) {
                            // save user_id to share preference
                            String userId = String.valueOf(result.getUserId());
                            UserLocalDataSource userLocalDataSource = new UserLocalDataSource(context);
                            userLocalDataSource.storeUserId(userId);
                            callback.onSuccess();
                        }
                    }

                } else {
                    if(response.code() == HttpURLConnection.HTTP_NOT_FOUND){
                        Log.d("Hello from LogInDataSource", "User not found. Do you want to create an account?");
                        callback.onError("User not found");
                    }
                    else if(response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                        Log.d("Hello from LogInDataSource", "Incorrect password");
                        callback.onError("Incorrect password");
                    }
                }
            }
            @Override
            public void onFailure(Call<CodeMessageResponse> call, Throwable t) {
                Log.d("Hello from LogInDataSource", "No internet connection");
                callback.onError("No internet");
//                Toast.makeText(LogInActivity.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
