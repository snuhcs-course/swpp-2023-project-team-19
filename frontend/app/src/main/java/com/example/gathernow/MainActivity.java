package com.example.gathernow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gathernow.R;
import com.example.gathernow.authenticate.UserLocalDataSource;
import com.example.gathernow.authenticate.login.LogInActivity;
import com.example.gathernow.authenticate.register.SignUpActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check if user already logged in
        UserLocalDataSource userLocalDataSource = new UserLocalDataSource(this);
        String userId = userLocalDataSource.getUserId();
        if (userId != null) {
            Intent intent = new Intent(this, FragHome.class);
            startActivity(intent);
            finish();
        }

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

    private boolean isBackPressed = false;

    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            finishAffinity();
            System.exit(0);
        }
        this.isBackPressed = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        // reset back pressed after 2 seconds
        new android.os.Handler().postDelayed(
                () -> isBackPressed = false, 2000);
    }


}
