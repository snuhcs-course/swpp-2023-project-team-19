package com.example.gathernow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private TextView nameInput;
    private TextView emailInput;
    private TextView passwordInput;
    private TextView pwConfirmInput;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameInput = findViewById(R.id.name);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        pwConfirmInput = findViewById(R.id.password_confirm);


        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String password_confirm = pwConfirmInput.getText().toString();

        Button signupbtn = (Button) findViewById(R.id.signup);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String password_confirm = pwConfirmInput.getText().toString();

                TextView alert = (TextView) findViewById(R.id.alert);

                // EXCEPTIONS
                // does not fill in all required fields
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || password_confirm.isEmpty()) {
                    alert.setText("Please fill in all required fields");
                }

                // wrong email format
                else if (!email.contains("@") || !email.contains(".")) {
                    alert.setText("Email format is incorrect");
                }

                // password does not match
                else if (!password.equals(password_confirm)) {
                    alert.setText("Password does not match");
                }

                // check name.length
                else if (name.length() > 30){
                    alert.setText("Name should be under 30 characters");
                }

                // check password.length
                else if (password.length() < 8 || password.length() > 30){
                    alert.setText("Password must be between 8 and 30 characters");
                }

                // pass pre-check
                else {
                    service = RetrofitClient.getClient().create(ServiceApi.class);
                    UserData requestData = new UserData(name, email, password);
                    service.userSignUp(requestData).enqueue(new Callback<CodeMessageResponse>() {
                        @Override
                        public void onResponse(Call<CodeMessageResponse> call, Response<CodeMessageResponse> response) {
                            if (response.isSuccessful()) {
                                CodeMessageResponse result = response.body();
                                if (result != null) {
                                    if (result.getMessage().equals("User registered successfully.")) {
                                        // Handle the case where the user registration was successful
                                        Toast.makeText(SignUp.this, "User registered successfully.", Toast.LENGTH_SHORT).show();
                                        // Link to the login page
                                        Intent intent = new Intent(v.getContext(), LogIn.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    // Handle the case where the response body is null or empty
                                    Toast.makeText(SignUp.this, "Empty response from the server", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle the case where the response is not successful (e.g., non-2xx HTTP status)
                                Toast.makeText(SignUp.this, "Email already exists.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<CodeMessageResponse> call, Throwable t) {
                            Toast.makeText(SignUp.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}