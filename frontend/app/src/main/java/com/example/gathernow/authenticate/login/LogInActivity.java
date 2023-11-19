package com.example.gathernow.authenticate.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.gathernow.FragHome;
import com.example.gathernow.R;
import com.example.gathernow.authenticate.register.SignUpActivity;

public class LogInActivity extends AppCompatActivity {
    private TextView emailInput;
    private TextView passwordInput;
    private TextView alert;
    private LogInViewModel logInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        alert = findViewById(R.id.alert);

        // Data source
        LogInDataSource logInDataSource = new LogInDataSource(LogInActivity.this);

        // Repository
        LoginRepository loginRepository = new LoginRepository(logInDataSource);

        // View Model
        logInViewModel = new ViewModelProvider(this, new LogInViewModelFactory(loginRepository)).get(LogInViewModel.class);
        logInViewModel.getAlertMessage().observe(this, message -> {
            if (message.equals("User not found")) {
                message = "User not found. Do you want to sign up?";
            }
            alert.setText(message);
            if ("Login successful".equals(message)) {
                // Link to the login page
                Log.d("Hello from LogInActivity", "Login successful");
                Intent intent = new Intent(LogInActivity.this, FragHome.class);
                startActivity(intent);
                finish();
            }
            if (message.equals("User not found. Do you want to sign up?")) {
                alert.setOnClickListener(v -> {
                    Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
        });
    }

    public void loginButtonOnClick(View view) {
        // Check user's input
        String email = this.emailInput.getText().toString();
        String password = this.passwordInput.getText().toString();

        logInViewModel.logIn(email, password);


    }
}