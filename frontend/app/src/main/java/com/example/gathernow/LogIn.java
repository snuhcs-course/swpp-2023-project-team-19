package com.example.gathernow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogIn extends AppCompatActivity {
    private TextView emailInput;
    private TextView passwordInput;
    private TextView alert;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        alert = findViewById(R.id.alert);
    }

    public void loginButtonOnClick(View view) {
        // Check user's input
        String email = this.emailInput.getText().toString();
        String password = this.passwordInput.getText().toString();

        // if user hasn't filled anything but still clicked Log In
        if (email.isEmpty() || password.isEmpty()) {
            alert.setText("Please fill in all required fields!");
        }

        // check email input format
        else if (!email.contains("@") || !email.contains(".")) {
            alert.setText("Input should be a valid email address");
        }
        else {
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
                                Toast.makeText(LogIn.this, "Login successful", Toast.LENGTH_SHORT).show();
                                Log.d("Log In", "successful");

                                // Link to the login page
                                Intent intent = new Intent(LogIn.this, FragHome.class);
                                startActivity(intent);
                            }
                        }
                        else {
                            Toast.makeText(LogIn.this, "Empty response body", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if(response.code() == HttpURLConnection.HTTP_NOT_FOUND){
                            alert.setText("User not found. Do you want to create an account?");
                            // Click text to sign up
                            alert.setOnClickListener(view1 -> {
                                Intent intent = new Intent(LogIn.this, SignUp.class);
                                startActivity(intent);
                            });
                        }
                        else if(response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                            alert.setText("Incorrect password");
                        }
                    }
                }
                @Override
                public void onFailure(Call<CodeMessageResponse> call, Throwable t) {
                    Toast.makeText(LogIn.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}