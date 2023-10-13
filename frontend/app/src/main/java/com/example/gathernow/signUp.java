package com.example.gathernow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class signUp extends AppCompatActivity {

    private TextView nameInput;
    private TextView emailInput;
    private TextView passwordInput;
    private TextView pwConfirmInput;

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
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || password_confirm.isEmpty()){
                    String alert_msg = "Please fill in all required fields";
                    alert.setText(alert_msg);
                }

                // wrong email format
                else if (!email.contains("@") || !email.contains(".")){
                    String alert_msg = "Email format is incorrect";
                    alert.setText(alert_msg);
                }

                // password does not match
                else if (!password.equals(password_confirm)) {
                    String alert_msg = "Password does not match";
                    alert.setText(alert_msg);
                }

                // TO-DO
                // email is already existed in database
                /*
                else if () {
                    String alert_msg = "Email existed";
                    alert.setText(alert_msg);
                }
                */


                // more extra exceptions:
                // name, email, password input are too long
                // name, password are too short

                else {
                    // just for reference, to be deleted later
                    String alert_msg = "signing in...";
                    alert.setText(alert_msg);

                    // Link to the login page or homepage
                    Intent intent = new Intent(v.getContext(), searchHome.class);
                    startActivity(intent);
                }
            }
        });
    }

}