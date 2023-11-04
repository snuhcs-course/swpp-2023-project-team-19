package com.example.gathernow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

        pickProfilePic = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                // Load picture from uri
                InputStream inputStream;
                File outputFile = new File(getApplicationContext().getFilesDir(), "profile_img.jpg");
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                    FileOutputStream outputStream = new FileOutputStream(outputFile);
                    Bitmap selectedImgBitmap = getRotatedImage(inputStream);

                    if (selectedImgBitmap != null) {
                        // Compress bitmap
                        selectedImgBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                        outputStream.close();
                        avatarFilePath = outputFile.getPath();
                        Log.d("SignUpActivity Testing", "Profile picture saved to: " + avatarFilePath);
                    }

                } catch (IOException e) {
                    Log.e("SignUpActivity Testing", "Error");
                    e.printStackTrace();
                }
            }
        });

    }

    private Bitmap getRotatedImage(InputStream inputStream) {
        // Clone the input stream because inputStream can only be read once
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream inputStreamClone;
        InputStream inputStreamClone2;
        try {
            inputStream.transferTo(baos);
            inputStreamClone = new ByteArrayInputStream(baos.toByteArray());
            inputStreamClone2 = new ByteArrayInputStream(baos.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("SignUpActivity Testing", "IO Exception when cloning the input stream");
            return null;
        }

        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(inputStreamClone);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("SignUpActivity Testing", "IO Exception when reading exif");
        }
        if (exifInterface != null) {
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degrees = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degrees = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degrees = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degrees = 270;
                    break;
            }
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            Bitmap selectedImgBitmap = BitmapFactory.decodeStream(inputStreamClone2);
            return Bitmap.createBitmap(selectedImgBitmap, 0, 0, selectedImgBitmap.getWidth(), selectedImgBitmap.getHeight(), matrix, true);

        }
        return BitmapFactory.decodeStream(inputStream);
    }

    public void onUploadProfilePicture(View view) {
        // Launch the photo picker and let the user choose only images.
        pickProfilePic.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }

    public void onSignUp(View v) {
        String name1 = nameInput.getText().toString();
        String email1 = emailInput.getText().toString();
        String password1 = passwordInput.getText().toString();
        String password_confirm1 = pwConfirmInput.getText().toString();

        TextView alert = (TextView) findViewById(R.id.alert);
        alert.setText("");

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
            MultipartBody.Part avatarPart = null;
            if (avatarFilePath != null) {
                avatarFile = new File(avatarFilePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), avatarFile);
                avatarPart = MultipartBody.Part.createFormData("avatar", avatarFile.getName(), requestFile);
            }

            RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name1);
            RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email1);
            RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password1);

            service = RetrofitClient.getClient().create(ServiceApi.class);
            // Send the request to the server using the 'service' instance
            Call<CodeMessageResponse> call = service.userSignUp(avatarPart, nameBody, emailBody, passwordBody);
            call.enqueue(new Callback<CodeMessageResponse>() {
                @Override
                public void onResponse(Call<CodeMessageResponse> call, Response<CodeMessageResponse> response) {
                    // Handle the response from the server
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
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e("SignUpActivity Testing", "Error Response: " + errorResponse);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CodeMessageResponse> call, Throwable t) {
                    // Handle the failure of the network request
                    Log.e("SignUpActivity Testing", "Sign Up Error: " + t.getMessage()); // Log the error
                    Toast.makeText(SignUpActivity.this, "Sign Up Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}