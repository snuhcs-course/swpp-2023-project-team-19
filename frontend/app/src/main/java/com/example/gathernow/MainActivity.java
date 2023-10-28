package com.example.gathernow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sign up button
        Button signUpButton = (Button) findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FragHome.class);
            startActivity(intent);
        });

        // log in button
        Button logInButton = findViewById(R.id.loginButton);
        logInButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), LogIn.class);
            startActivity(intent);
        });
    }



}