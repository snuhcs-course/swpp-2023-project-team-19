package com.example.gathernow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private TextView nameInput;
    private TextView emailInput;
    private TextView passwordInput;
    private TextView pwConfirmInput;
    private ServiceApi service;
    private ActivityResultLauncher<PickVisualMediaRequest> pickProfilePic;

    String avatarFilePath = null;
    File avatarFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        nameInput = findViewById(R.id.name);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        pwConfirmInput = findViewById(R.id.password_confirm);

        pickProfilePic = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        Log.d("SignUpActivity Testing", "Profile picture selected: " + uri);
                        // Load picture from uri
                        InputStream inputStream;
                        File outputFile = new File(getApplicationContext().getFilesDir(), "tmp_file.jpg");
                        try {
                            inputStream = getContentResolver().openInputStream(uri);
                            FileOutputStream outputStream = new FileOutputStream(outputFile);
                            Bitmap selectedImgBitmap = BitmapFactory.decodeStream(inputStream);
                            // Compress bitmap
                            selectedImgBitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream);
                            outputStream.close();
                            avatarFilePath = outputFile.getPath();
                            Log.d("SignUpActivity Testing", "Profile picture saved to: " + avatarFilePath);
                        } catch (IOException e) {
                            Log.e("SignUpActivity Testing", "Error");
                            e.printStackTrace();
                        }
                    }
                });

    }

    public void onUploadProfilePicture(View view) {
        // Launch the photo picker and let the user choose only images.
        pickProfilePic.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    public void onSignUp(View v) {
        String name1 = nameInput.getText().toString();
        String email1 = emailInput.getText().toString();
        String password1 = passwordInput.getText().toString();
        String password_confirm1 = pwConfirmInput.getText().toString();

        TextView alert = (TextView) findViewById(R.id.alert);

        // EXCEPTIONS
        // does not fill in all required fields
        if (name1.isEmpty() || email1.isEmpty() || password1.isEmpty() || password_confirm1.isEmpty()) {
            alert.setText("Please fill in all required fields");
        }

        // wrong email format
        else if (!email1.contains("@") || !email1.contains(".")) {
            alert.setText("Email format is incorrect");
        }

        // password does not match
        else if (!password1.equals(password_confirm1)) {
            alert.setText("Password does not match");
        }

        // check name.length
        else if (name1.length() > 30) {
            alert.setText("Name should be under 30 characters");
        }

        // check password.length
        else if (password1.length() < 8 || password1.length() > 30) {
            alert.setText("Password must be between 8 and 30 characters");
        }

        // pass pre-check
        else {
            if (avatarFilePath != null){
                if (new File(getApplicationContext().getFilesDir(), "tmp_file.jpg").exists()){
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), new File(getApplicationContext().getFilesDir(), "tmp_file.jpg"));
                    // Create a MultipartBody.Part from the request body
                    MultipartBody.Part avatarPart = MultipartBody.Part.createFormData("avatar", new File(getApplicationContext().getFilesDir(), "tmp_file.jpg").getName(), requestFile);
                    // Create a UserData object with name, email, and password
                    UserData requestData = new UserData(name1, email1, password1);
                    // Send the request to the server

                    service = RetrofitClient.getClient().create(ServiceApi.class);
                    service.userSignUp(requestData, avatarPart).enqueue(new Callback<CodeMessageResponse>() {
                        @Override
                        public void onResponse(Call<CodeMessageResponse> call, Response<CodeMessageResponse> response) {
                            Log.d("SignUpActivity Testing", "HTTP Status Code: " + response.code());
                            if (response.isSuccessful()) {
                                CodeMessageResponse result = response.body();
                                if (result != null) {
                                    if (result.getMessage().equals("User registered successfully.")) {
                                        // Handle the case where the user registration was successful
                                        Toast.makeText(SignUpActivity.this, "User registered successfully.", Toast.LENGTH_SHORT).show();
                                        // Link to the login page
                                        Intent intent = new Intent(v.getContext(), LogInActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    // Handle the case where the response body is null or empty
                                    Toast.makeText(SignUpActivity.this, "Empty response from the server", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle the case where the response is not successful (e.g., non-2xx HTTP status)
                                Toast.makeText(SignUpActivity.this, "Email already exists.", Toast.LENGTH_SHORT).show();
                                Log.e("SignUpActivity Testing", "Sign Up Error: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<CodeMessageResponse> call, Throwable t) {
                            Log.e("SignUpActivity Testing", "Sign Up Error: " + t.getMessage()); // Log the error
                            Toast.makeText(SignUpActivity.this, "Sign Up Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(this, "Avatar file does not exist", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Please upload a profile picture", Toast.LENGTH_SHORT).show();
            }
        }
    }
}