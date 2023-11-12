package com.example.gathernow.log_in;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.gathernow.UserData;
import com.example.gathernow.api.CodeMessageResponse;
import com.example.gathernow.api.RetrofitClient;
import com.example.gathernow.api.ServiceApi;
import com.example.gathernow.log_in.LogInCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInDataSource {
    private ServiceApi service;
    private final SharedPreferences sharedPref;
    private final SharedPreferences.Editor editor;
    public LogInDataSource(Context context) {
        sharedPref = context.getSharedPreferences("UserId", MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void storeUserId(String userId) {
        editor.putString("user_id", userId);
        editor.apply();
    }

    public String getUserId() {
        return sharedPref.getString("user_id", null);
    }

    public void logIn(String email, String password, LogInCallback callback) {
        service = RetrofitClient.getClient().create(ServiceApi.class);
        UserData requestData = new UserData(email, password);
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
                            storeUserId(userId);
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
