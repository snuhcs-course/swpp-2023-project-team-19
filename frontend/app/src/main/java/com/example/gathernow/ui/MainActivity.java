package com.example.gathernow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gathernow.R;
import com.example.gathernow.authenticate.login.LogInActivity;
import com.example.gathernow.authenticate.register.SignUpActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sign up button
        Button signUpButton = (Button) findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SignUpActivity.class);
            startActivity(intent);
        });

        // log in button
        Button logInButton = findViewById(R.id.loginButton);
        logInButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), LogInActivity.class);
            startActivity(intent);
        });
    }


}