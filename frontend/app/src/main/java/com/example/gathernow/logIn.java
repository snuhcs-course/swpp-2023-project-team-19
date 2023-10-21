package com.example.gathernow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class logIn extends AppCompatActivity {
    private TextView email;
    private TextView password;
    private TextView alertMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        alertMsg = findViewById(R.id.alert);
    }

    public void loginButtonOnClick(View view) {
        // Check user's input
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();

        // if user hasn't filled anything but still clicked Log In
        if (emailInput.isEmpty() || passwordInput.isEmpty()) {
            alertMsg.setText("Please fill in all required fields!");
        }

        // check email input format
        else if (!emailInput.contains("@") || !emailInput.contains(".")) {
            alertMsg.setText("Input should be a valid email address");
        }

        // TODO: Email doesn't exist in the system

        // TODO: Wrong password

        else {
            Intent intent = new Intent(logIn.this, fragHome.class);
            startActivity(intent);
        }

    }
}