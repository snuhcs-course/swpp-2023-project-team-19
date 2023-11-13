package com.example.gathernow.authenticate.register;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gathernow.authenticate.AuthCallback;
import com.example.gathernow.utils.ImageHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SignUpViewModel extends ViewModel {
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();
    private final SignUpRepository signUpRepository;
    private final MutableLiveData<String> avatarFilePath = new MutableLiveData<>();
    private Context context;

    public SignUpViewModel(Context context, SignUpRepository signUpRepository) {
        this.signUpRepository = signUpRepository;
        this.context = context;
    }

    public MutableLiveData<String> getAlertMessage() {
        return alertMessage;
    }

    public MutableLiveData<String> getAvatarFilePath() {
        return avatarFilePath;
    }

    public void handleImageSelection(Uri uri) {
        if (uri != null) {
            // Load picture from uri
            InputStream inputStream;
            File outputFile = new File(context.getFilesDir(), "profile_img.jpg");
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                Bitmap selectedImgBitmap = ImageHelper.getCorrectlyRotatedImg(inputStream);

                if (selectedImgBitmap != null) {
                    // Compress bitmap
                    selectedImgBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                    outputStream.close();
                    avatarFilePath.postValue(outputFile.getPath());
                    Log.d("SignUpActivity Testing", "Profile picture saved to: " + outputFile.getPath());
                }

            } catch (IOException e) {
                Log.e("SignUpActivity Testing", "Error");
                e.printStackTrace();
            }
        }
    }

    public void signUp(String name, String email, String password, String pwConfirmation, String avatarFilePath) {
        if (!isValidInput(name, email, password, pwConfirmation)) {
            return;
        }
        signUpRepository.register(name, email, password, avatarFilePath, new AuthCallback() {
            @Override
            public void onSuccess() {
                alertMessage.postValue("Sign up successfully");
            }

            @Override
            public void onError(String message) {
                alertMessage.postValue(message);
            }
        });
    }

    private boolean isValidInput(String name, String email, String password, String pwConfirmation) {
        // does not fill in all required fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || pwConfirmation.isEmpty()) {
            alertMessage.postValue("Please fill in all required fields");
            return false;
        }

        // wrong email format
        else if (!email.contains("@") || !email.contains(".")) {
            alertMessage.postValue("Email format is incorrect");
            return false;
        }

        // password does not match
        else if (!password.equals(pwConfirmation)) {
            alertMessage.postValue("Password does not match");
            return false;
        }

        // check name.length
        else if (name.length() > 30) {
            alertMessage.postValue("Name should be under 30 characters");
            return false;
        }

        // check password.length
        else if (password.length() < 8 || password.length() > 30) {
            alertMessage.postValue("Password must be between 8 and 30 characters");
            return false;
        }
        return true;
    }
}
