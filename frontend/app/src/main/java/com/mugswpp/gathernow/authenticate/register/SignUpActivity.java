package com.mugswpp.gathernow.authenticate.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.mugswpp.gathernow.R;
import com.mugswpp.gathernow.authenticate.login.LogInActivity;

public class SignUpActivity extends AppCompatActivity {

    private TextView nameInput;
    private TextView emailInput;
    private TextView passwordInput;
    private TextView pwConfirmInput;
    private TextView alert;
    private ActivityResultLauncher<PickVisualMediaRequest> pickProfilePic;
    private SignUpViewModel signUpViewModel;

    String avatarFilePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameInput = findViewById(R.id.name);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        pwConfirmInput = findViewById(R.id.password_confirm);

        alert = findViewById(R.id.alert);

        View rootLayout = findViewById(R.id.signupRootLayout);

        // Add a touch listener to hide the keyboard when tapping on a blank space
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKB();
                return false;
            }
        });

        // Data source
        SignUpDataSource signUpDataSource = new SignUpDataSource();

        // Repository
        SignUpRepository signUpRepository = new SignUpRepository(signUpDataSource);

        // View Model
        signUpViewModel = new SignUpViewModel(getApplicationContext(), signUpRepository);
        signUpViewModel.getAvatarFilePath().observe(this, filePath -> {
            avatarFilePath = filePath;
        });
        signUpViewModel.getAlertMessage().observe(this, message -> {
            alert.setText(message);
            Log.d("Hello from SignUpActivity", message);
            if ("Sign up successfully".equals(message)) {
                // Link to the login page
                Log.d("Hello from SignUpActivity", "Sign up successfully");
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        pickProfilePic = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            signUpViewModel.handleImageSelection(uri);
        });

    }

    private void hideKB() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onUploadProfilePicture(View view) {
        // Launch the photo picker and let the user choose only images.
        pickProfilePic.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }

    public void onSignUp(View v) {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String password_confirm = pwConfirmInput.getText().toString();

        signUpViewModel.signUp(name, email, password, password_confirm, avatarFilePath);
    }
}