package com.example.gathernow.authenticate.register;

import android.util.Log;

import com.example.gathernow.api.CodeMessageResponse;
import com.example.gathernow.api.RetrofitClient;
import com.example.gathernow.api.ServiceApi;
import com.example.gathernow.authenticate.AuthCallback;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpDataSource {

    public SignUpDataSource() {

    }

    public void register(String name, String email, String password, String avatarFilePath, AuthCallback callback) {
        // Avatar Path
        MultipartBody.Part avatarPart = null;
        if (avatarFilePath != null) {
            File avatarFile = new File(avatarFilePath);
            RequestBody avatarBody = RequestBody.create(MediaType.parse("image/*"), avatarFile);
            avatarPart = MultipartBody.Part.createFormData("avatar", avatarFile.getName(), avatarBody);
        }
        Log.d("SignUp", "Info: " + name + ","+email + "," + password);

        // Other fields
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);

        ServiceApi service = RetrofitClient.getClient().create(ServiceApi.class);
        // Send the request to the server using the 'service' instance
        Call<CodeMessageResponse> call = service.userSignUp(avatarPart, nameBody, emailBody, passwordBody);
        call.enqueue(new Callback<CodeMessageResponse>() {
            @Override
            public void onResponse(Call<CodeMessageResponse> call, Response<CodeMessageResponse> response) {
//                Log.d("SignUp", "Response received");
                // Handle the response from the server
                if (response.isSuccessful()) {
                    CodeMessageResponse result = response.body();
                    if (result != null) {
//                        Log.d("SignUp", "Message: " + result.getMessage());
                        if (result.getMessage().equals("User registered successfully.")) {
                            callback.onSuccess();
                            Log.d("Hello from SignUpDataSource", "Success");
                        }
                    } else {
                        // Handle the case where the response body is null or empty
                        callback.onError("Empty response from the server");
//                        Log.d("Hello from SignUpDataSource", "Empty response from the server");
                    }
                } else {
                    // Handle the case where the response is not successful (e.g., non-2xx HTTP status)
                    callback.onError("Email already exists");
//                    Log.d("Hello from SignUpDataSource", "Error");
                }
            }

            @Override
            public void onFailure(Call<CodeMessageResponse> call, Throwable t) {
                // Handle the failure of the network request
                callback.onError("Network error");
//                Log.d("Hello from SignUpDataSource", "Network error");
            }
        });
    }
}
